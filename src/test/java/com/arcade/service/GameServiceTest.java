package com.arcade.service;

import com.arcade.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the core game flow and progressive difficulty session state transitions.
 */
class GameServiceTest {

    private static final int QUESTIONS_PER_DIFFICULTY = 4;
    private static final int QUESTIONS_PER_SESSION = 12;

    private QuestionService questionService;
    private GameService gameService;
    private String selectedCategory;

    @BeforeEach
    void setUp() {
        questionService = new QuestionService();
        List<String> availableCategories = questionService.getAvailableCategories();
        assertFalse(availableCategories.isEmpty(), "At least one category should be available.");

        selectedCategory = availableCategories.get(0);
        gameService = new GameService(questionService);
        gameService.startGame(selectedCategory);
    }

    @Test
    void shouldStartGameWithDefaultValues() {
        assertEquals(0, gameService.getScore(), "Score should start at zero.");
        assertEquals(3, gameService.getRemainingLives(), "Lives should start at three.");
        assertFalse(gameService.isGameOver(), "Game should not be over after starting.");
    }

    @Test
    void shouldReturnCurrentQuestionAfterGameStarts() {
        assertNotNull(gameService.getCurrentQuestion(), "Current question should be available after the game starts.");
    }

    @Test
    void shouldNotAdvanceWhenAnswerValidationFails() {
        Question currentQuestion = gameService.getCurrentQuestion();
        int scoreBefore = gameService.getScore();
        int livesBefore = gameService.getRemainingLives();

        assertFalse(gameService.submitAnswer(""),
                "Invalid answers should be rejected without advancing the game.");
        assertEquals(scoreBefore, gameService.getScore(),
                "Score should remain unchanged after invalid input.");
        assertEquals(livesBefore, gameService.getRemainingLives(),
                "Lives should remain unchanged after invalid input.");
        assertSame(currentQuestion, gameService.getCurrentQuestion(),
                "The same question should remain active after invalid input.");
        assertFalse(gameService.isGameOver(),
                "The game should continue after invalid input.");
    }

    @Test
    void shouldContinueGameWhileLivesRemainAndQuestionsExist() {
        Question currentQuestion = gameService.getCurrentQuestion();

        assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        assertFalse(gameService.isGameOver(),
                "The game should continue while lives remain and more questions exist.");
        assertTrue(gameService.hasMoreQuestions(),
                "More questions should remain after answering the first question.");
        assertNotNull(gameService.getCurrentQuestion(),
                "Another question should be available while the game continues.");
    }

    @Test
    void shouldEndGameWhenNoLivesRemain() {
        while (gameService.getRemainingLives() > 0) {
            Question currentQuestion = gameService.getCurrentQuestion();
            assertNotNull(currentQuestion, "A question should be available while lives remain.");
            assertFalse(gameService.submitAnswer(getWrongAnswer(currentQuestion)),
                    "The submitted answer should be incorrect.");
        }

        assertEquals(0, gameService.getRemainingLives(), "Lives should reach zero after repeated wrong answers.");
        assertTrue(gameService.isGameOver(), "Game should be over after all lives are lost.");
        assertNull(gameService.getCurrentQuestion(),
                "No current question should be available after the game is over.");
    }

    @Test
    void shouldEndGameWhenAllQuestionsAreAnswered() {
        Question currentQuestion;
        while ((currentQuestion = gameService.getCurrentQuestion()) != null) {
            assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                    "The submitted answer should be correct.");
        }

        assertTrue(gameService.isGameOver(), "Game should be over after all questions have been answered.");
        assertTrue(gameService.getRemainingLives() > 0,
                "Lives should remain after answering all questions correctly.");
        assertFalse(gameService.hasMoreQuestions(),
                "No more questions should remain after all questions are answered.");
    }

    @Test
    void shouldIncreaseScoreAfterCorrectAnswer() {
        Question currentQuestion = gameService.getCurrentQuestion();
        boolean correct = gameService.submitAnswer(currentQuestion.getCorrectAnswer());

        assertTrue(correct, "The submitted answer should be correct.");
        assertEquals(100, gameService.getScore(), "Score should increase by 100 after a correct answer.");
    }

    @Test
    void shouldNotDecreaseLivesAfterCorrectAnswer() {
        int livesBefore = gameService.getRemainingLives();
        Question currentQuestion = gameService.getCurrentQuestion();

        assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        assertEquals(livesBefore, gameService.getRemainingLives(),
                "Lives should remain unchanged after a correct answer.");
    }

    @Test
    void shouldDecreaseLivesAfterWrongAnswer() {
        int livesBefore = gameService.getRemainingLives();
        Question currentQuestion = gameService.getCurrentQuestion();

        assertFalse(gameService.submitAnswer(getWrongAnswer(currentQuestion)),
                "The submitted answer should be incorrect.");
        assertEquals(livesBefore - 1, gameService.getRemainingLives(),
                "Lives should decrease by one after a wrong answer.");
    }

    @Test
    void shouldNotIncreaseScoreAfterWrongAnswer() {
        int scoreBefore = gameService.getScore();
        Question currentQuestion = gameService.getCurrentQuestion();

        assertFalse(gameService.submitAnswer(getWrongAnswer(currentQuestion)),
                "The submitted answer should be incorrect.");
        assertEquals(scoreBefore, gameService.getScore(),
                "Score should remain unchanged after a wrong answer.");
    }

    @Test
    void shouldDetectGameOverAfterLosingAllLives() {
        while (gameService.getRemainingLives() > 0) {
            Question currentQuestion = gameService.getCurrentQuestion();
            assertNotNull(currentQuestion, "A question should be available while lives remain.");
            gameService.submitAnswer(getWrongAnswer(currentQuestion));
        }

        assertEquals(0, gameService.getRemainingLives(), "Lives should reach zero after repeated wrong answers.");
        assertTrue(gameService.isGameOver(), "Game should be over after all lives are lost.");
    }

    @Test
    void shouldDetectGameOverWhenQuestionsAreFinished() {
        Question currentQuestion;
        while ((currentQuestion = gameService.getCurrentQuestion()) != null) {
            assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                    "The submitted answer should be correct.");
        }

        assertTrue(gameService.isGameOver(), "Game should be over after all questions have been answered.");
    }

    @Test
    void shouldAdvanceToNextQuestionAfterSubmission() {
        Question currentQuestion = gameService.getCurrentQuestion();
        Question nextQuestion;

        assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        nextQuestion = gameService.getCurrentQuestion();

        assertNotNull(nextQuestion, "A next question should be available after submitting an answer.");
        assertNotEquals(currentQuestion, nextQuestion, "The current question should advance after submission.");
    }

    @Test
    void shouldStartGameUsingSelectedCategory() {
        gameService.startGame(selectedCategory);

        assertFalse(gameService.getLoadedQuestions().isEmpty(),
                "Selected category should load at least one question.");
        assertFalse(gameService.isGameOver(),
                "The game should not be over after starting with a valid category.");
        assertNotNull(gameService.getCurrentQuestion(),
                "A question should be available after starting with a valid category.");
    }

    @Test
    void shouldLoadOnlyQuestionsFromSelectedCategory() {
        gameService.startGame(selectedCategory);

        List<Question> loadedQuestions = gameService.getLoadedQuestions();
        assertFalse(loadedQuestions.isEmpty(), "Selected category should load questions.");
        assertTrue(loadedQuestions.stream()
                        .allMatch(question -> selectedCategory.equalsIgnoreCase(question.getCategory())),
                "Only questions from the selected category should be loaded.");
    }

    @Test
    void shouldNotLoadQuestionsFromOtherCategories() {
        String otherCategory = questionService.getAvailableCategories().stream()
                .filter(category -> !category.equalsIgnoreCase(selectedCategory))
                .findFirst()
                .orElseThrow();

        gameService.startGame(selectedCategory);

        assertTrue(gameService.getLoadedQuestions().stream()
                        .noneMatch(question -> otherCategory.equalsIgnoreCase(question.getCategory())),
                "Questions from other categories should be excluded.");
    }

    @Test
    void shouldHandleCategoryWithoutQuestions() {
        gameService.startGame("Unknown Category");

        assertTrue(gameService.getLoadedQuestions().isEmpty(),
                "Unknown categories should not load any questions.");
        assertTrue(gameService.isGameOver(),
                "The game should end safely when no questions are loaded.");
        assertNull(gameService.getCurrentQuestion(),
                "No current question should be available for an unknown category.");
    }

    @Test
    void shouldCreateSessionWithTwelveQuestions() {
        assertEquals(QUESTIONS_PER_SESSION, gameService.getLoadedQuestions().size(),
                "The session should contain exactly twelve questions.");
    }

    @Test
    void shouldCreateSessionWithFourEasyQuestions() {
        long easyQuestions = gameService.getLoadedQuestions().stream()
                .filter(question -> "EASY".equals(question.getDifficulty()))
                .count();

        assertEquals(QUESTIONS_PER_DIFFICULTY, easyQuestions,
                "The session should contain exactly four EASY questions.");
    }

    @Test
    void shouldCreateSessionWithFourMediumQuestions() {
        long mediumQuestions = gameService.getLoadedQuestions().stream()
                .filter(question -> "MEDIUM".equals(question.getDifficulty()))
                .count();

        assertEquals(QUESTIONS_PER_DIFFICULTY, mediumQuestions,
                "The session should contain exactly four MEDIUM questions.");
    }

    @Test
    void shouldCreateSessionWithFourHardQuestions() {
        long hardQuestions = gameService.getLoadedQuestions().stream()
                .filter(question -> "HARD".equals(question.getDifficulty()))
                .count();

        assertEquals(QUESTIONS_PER_DIFFICULTY, hardQuestions,
                "The session should contain exactly four HARD questions.");
    }

    @Test
    void shouldApplyEasyScoreValue() {
        Question currentQuestion = gameService.getCurrentQuestion();

        assertEquals("EASY", currentQuestion.getDifficulty(),
                "The first question should be EASY.");
        assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        assertEquals(100, gameService.getScore(), "Easy questions should award 100 points.");
    }

    @Test
    void shouldApplyMediumScoreValue() {
        answerCorrectQuestions(QUESTIONS_PER_DIFFICULTY);

        Question mediumQuestion = gameService.getCurrentQuestion();
        int scoreBefore = gameService.getScore();

        assertEquals("MEDIUM", mediumQuestion.getDifficulty(),
                "The fifth question should be MEDIUM.");
        assertTrue(gameService.submitAnswer(mediumQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        assertEquals(scoreBefore + 200, gameService.getScore(), "Medium questions should award 200 points.");
    }

    @Test
    void shouldApplyHardScoreValue() {
        answerCorrectQuestions(QUESTIONS_PER_DIFFICULTY * 2);

        Question hardQuestion = gameService.getCurrentQuestion();
        int scoreBefore = gameService.getScore();

        assertEquals("HARD", hardQuestion.getDifficulty(),
                "The ninth question should be HARD.");
        assertTrue(gameService.submitAnswer(hardQuestion.getCorrectAnswer()),
                "The submitted answer should be correct.");
        assertEquals(scoreBefore + 300, gameService.getScore(), "Hard questions should award 300 points.");
    }

    @Test
    void shouldRejectCategoryWithoutEnoughQuestions() {
        gameService.startGame("Unknown Category");

        assertTrue(gameService.getLoadedQuestions().isEmpty(),
                "A category without enough questions should not build a session.");
        assertTrue(gameService.isGameOver(),
                "The game should be marked as over when the session cannot be created.");
        assertNull(gameService.getCurrentQuestion(),
                "No current question should be available when session creation fails.");
    }

    @Test
    void shouldPreserveDifficultyProgressionOrder() {
        List<String> difficulties = gameService.getLoadedQuestions().stream()
                .map(Question::getDifficulty)
                .toList();

        assertEquals(List.of(
                        "EASY", "EASY", "EASY", "EASY",
                        "MEDIUM", "MEDIUM", "MEDIUM", "MEDIUM",
                        "HARD", "HARD", "HARD", "HARD"),
                difficulties,
                "Questions should progress from EASY to MEDIUM to HARD.");
    }

    private void answerCorrectQuestions(int questionCount) {
        for (int index = 0; index < questionCount; index++) {
            Question currentQuestion = gameService.getCurrentQuestion();
            assertNotNull(currentQuestion, "A question should be available while answering correctly.");
            assertTrue(gameService.submitAnswer(currentQuestion.getCorrectAnswer()),
                    "The submitted answer should be correct.");
        }
    }

    private String getWrongAnswer(Question question) {
        for (String option : question.getOptions()) {
            if (option != null && !option.equalsIgnoreCase(question.getCorrectAnswer())) {
                return option;
            }
        }

        return "Incorrect answer";
    }
}
