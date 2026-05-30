package com.arcade.service;

import com.arcade.model.GameSession;
import com.arcade.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Provides the core game loop and state transitions for the terminal game.
 */
public class GameService {

    private static final int INITIAL_LIVES = 3;
    private static final int QUESTIONS_PER_DIFFICULTY = 4;
    private static final int QUESTIONS_PER_SESSION = 12;
    private static final int EASY_SCORE = 100;
    private static final int MEDIUM_SCORE = 200;
    private static final int HARD_SCORE = 300;
    private static final String EASY = "EASY";
    private static final String MEDIUM = "MEDIUM";
    private static final String HARD = "HARD";

    private final QuestionService questionService;
    private final GameSession gameSession;

    /**
     * Creates a game service with a default question service.
     */
    public GameService() {
        this(new QuestionService());
    }

    /**
     * Creates a game service with the provided question service.
     *
     * @param questionService the question service to use
     */
    public GameService(QuestionService questionService) {
        if (questionService == null) {
            throw new IllegalArgumentException("QuestionService must not be null.");
        }

        this.questionService = questionService;
        this.gameSession = new GameSession();
    }

    /**
     * Starts a new game session using the first available category and resets the state.
     */
    public void startGame() {
        List<String> availableCategories = questionService.getAvailableCategories();
        if (availableCategories.isEmpty()) {
            throw new IllegalStateException("Game cannot start without questions.");
        }

        startGame(availableCategories.get(0));
    }

    /**
     * Starts a new game session using only questions from the selected category.
     * The session is built in EASY, MEDIUM, HARD order and requires four questions per difficulty.
     *
     * @param category the selected category
     */
    public void startGame(String category) {
        if (category == null || category.isBlank()) {
            startGame();
            return;
        }

        initializeGameSession(buildProgressiveSession(category));
    }

    /**
     * Returns the questions loaded in the current game session.
     *
     * @return the loaded questions for the active session
     */
    public List<Question> getLoadedQuestions() {
        return gameSession.getLoadedQuestions();
    }

    /**
     * Returns the current question in the session.
     *
     * @return the current question, or {@code null} if the game is over or no questions remain
     */
    public Question getCurrentQuestion() {
        if (gameSession.isGameOver() || gameSession.getRemainingLives() <= 0 || !hasMoreQuestions()) {
            gameSession.setGameOver(true);
            return null;
        }

        return gameSession.getLoadedQuestions().get(gameSession.getCurrentQuestionIndex());
    }

    /**
     * Submits an answer for the current question.
     *
     * @param answer the answer provided by the player
     * @return {@code true} if the answer is correct; otherwise {@code false}
     */
    public boolean submitAnswer(String answer) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion == null) {
            return false;
        }

        if (!isValidAnswer(currentQuestion, answer)) {
            return false;
        }

        boolean correct = isCorrectAnswer(currentQuestion, answer);
        if (correct) {
            gameSession.setCurrentScore(gameSession.getCurrentScore() + getScoreForQuestion(currentQuestion));
        } else {
            gameSession.setRemainingLives(gameSession.getRemainingLives() - 1);
        }

        gameSession.setCurrentQuestionIndex(gameSession.getCurrentQuestionIndex() + 1);
        updateGameOverStatus();
        return correct;
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public int getScore() {
        return gameSession.getCurrentScore();
    }

    /**
     * Returns the remaining lives.
     *
     * @return the remaining lives
     */
    public int getRemainingLives() {
        return gameSession.getRemainingLives();
    }

    /**
     * Returns the 1-based number of the current question.
     *
     * @return the current question number, or {@code 0} if no questions are loaded
     */
    public int getCurrentQuestionNumber() {
        if (gameSession.getLoadedQuestions().isEmpty()) {
            return 0;
        }

        return Math.min(gameSession.getCurrentQuestionIndex() + 1, gameSession.getLoadedQuestions().size());
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over; otherwise {@code false}
     */
    public boolean isGameOver() {
        return gameSession.isGameOver();
    }

    /**
     * Returns whether there are more questions available.
     *
     * @return {@code true} if more questions remain; otherwise {@code false}
     */
    public boolean hasMoreQuestions() {
        return gameSession.getCurrentQuestionIndex() < gameSession.getLoadedQuestions().size();
    }

    private boolean isValidAnswer(Question currentQuestion, String answer) {
        if (currentQuestion == null || answer == null || answer.isBlank()) {
            return false;
        }

        List<String> options = currentQuestion.getOptions();
        if (options == null || options.isEmpty()) {
            return false;
        }

        String normalizedAnswer = answer.trim();
        for (String option : options) {
            if (option != null && option.trim().equalsIgnoreCase(normalizedAnswer)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCorrectAnswer(Question currentQuestion, String answer) {
        String correctAnswer = currentQuestion.getCorrectAnswer();
        if (correctAnswer == null || answer == null) {
            return false;
        }

        return correctAnswer.trim().equalsIgnoreCase(answer.trim());
    }

    /**
     * Returns the score value associated with the current question difficulty.
     *
     * @param question the question to evaluate
     * @return the score value for the question difficulty, or zero when the difficulty is invalid
     */
    private int getScoreForQuestion(Question question) {
        if (question == null) {
            return 0;
        }

        String difficulty = question.getDifficulty();
        if (difficulty == null) {
            return 0;
        }

        return switch (difficulty.trim().toUpperCase(Locale.ROOT)) {
            case EASY -> EASY_SCORE;
            case MEDIUM -> MEDIUM_SCORE;
            case HARD -> HARD_SCORE;
            default -> 0;
        };
    }

    private void updateGameOverStatus() {
        gameSession.setGameOver(gameSession.getRemainingLives() <= 0 || !hasMoreQuestions());
    }

    /**
     * Builds the ordered 12-question session for the selected category.
     *
     * @param category the selected category
     * @return the progressive session questions, or an empty list when the category does not qualify
     */
    private List<Question> buildProgressiveSession(String category) {
        List<Question> easyQuestions = questionService.getQuestionsByCategoryAndDifficulty(category, EASY);
        List<Question> mediumQuestions = questionService.getQuestionsByCategoryAndDifficulty(category, MEDIUM);
        List<Question> hardQuestions = questionService.getQuestionsByCategoryAndDifficulty(category, HARD);

        if (easyQuestions.size() < QUESTIONS_PER_DIFFICULTY
                || mediumQuestions.size() < QUESTIONS_PER_DIFFICULTY
                || hardQuestions.size() < QUESTIONS_PER_DIFFICULTY) {
            return List.of();
        }

        List<Question> sessionQuestions = new ArrayList<>(QUESTIONS_PER_SESSION);
        sessionQuestions.addAll(easyQuestions.subList(0, QUESTIONS_PER_DIFFICULTY));
        sessionQuestions.addAll(mediumQuestions.subList(0, QUESTIONS_PER_DIFFICULTY));
        sessionQuestions.addAll(hardQuestions.subList(0, QUESTIONS_PER_DIFFICULTY));
        return sessionQuestions;
    }

    private void initializeGameSession(List<Question> loadedQuestions) {
        List<Question> questionsToLoad = loadedQuestions == null ? Collections.emptyList() : loadedQuestions;

        gameSession.setLoadedQuestions(questionsToLoad);
        gameSession.setCurrentScore(0);
        gameSession.setRemainingLives(INITIAL_LIVES);
        gameSession.setCurrentQuestionIndex(0);
        updateGameOverStatus();
    }
}
