package com.arcade.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the current state of a running game session.
 */
public class GameSession {

    private final List<Question> loadedQuestions;
    private int currentScore;
    private int remainingLives;
    private int currentQuestionIndex;
    private boolean gameOver;

    /**
     * Creates an empty game session with default values.
     */
    public GameSession() {
        this.loadedQuestions = new ArrayList<>();
    }

    /**
     * Creates a game session with all fields initialized.
     *
     * @param currentScore the current score
     * @param remainingLives the remaining lives
     * @param currentQuestionIndex the current question index
     * @param loadedQuestions the loaded questions
     * @param gameOver the game over state
     */
    public GameSession(int currentScore, int remainingLives, int currentQuestionIndex,
                       List<Question> loadedQuestions, boolean gameOver) {
        this();
        setCurrentScore(currentScore);
        setRemainingLives(remainingLives);
        setCurrentQuestionIndex(currentQuestionIndex);
        setLoadedQuestions(loadedQuestions);
        setGameOver(gameOver);
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Sets the current score.
     *
     * @param currentScore the current score
     */
    public void setCurrentScore(int currentScore) {
        this.currentScore = Math.max(0, currentScore);
    }

    /**
     * Returns the remaining lives.
     *
     * @return the remaining lives
     */
    public int getRemainingLives() {
        return remainingLives;
    }

    /**
     * Sets the remaining lives.
     *
     * @param remainingLives the remaining lives
     */
    public void setRemainingLives(int remainingLives) {
        this.remainingLives = Math.max(0, remainingLives);
    }

    /**
     * Returns the current question index.
     *
     * @return the current question index
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Sets the current question index.
     *
     * @param currentQuestionIndex the current question index
     */
    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = Math.max(0, currentQuestionIndex);
    }

    /**
     * Returns the loaded questions for the session.
     *
     * @return the loaded questions
     */
    public List<Question> getLoadedQuestions() {
        return Collections.unmodifiableList(loadedQuestions);
    }

    /**
     * Replaces the loaded questions for the session.
     *
     * @param loadedQuestions the loaded questions
     */
    public void setLoadedQuestions(List<Question> loadedQuestions) {
        this.loadedQuestions.clear();
        if (loadedQuestions != null) {
            this.loadedQuestions.addAll(loadedQuestions);
        }
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over; otherwise {@code false}
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over state.
     *
     * @param gameOver the game over state
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
