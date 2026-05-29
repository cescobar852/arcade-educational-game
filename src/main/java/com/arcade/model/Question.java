package com.arcade.model;

import java.util.List;

/**
 * Represents a question as pure data for the arcade educational game.
 */
public class Question {

    private Integer id;
    private String category;
    private String difficulty;
    private String questionText;
    private String extraContent;
    private List<String> options;
    private String correctAnswer;

    /**
     * Creates an empty question.
     */
    public Question() {
    }

    /**
     * Creates a question with all fields initialized.
     *
     * @param id the question identifier
     * @param category the question category
     * @param difficulty the question difficulty
     * @param questionText the main question text
     * @param extraContent the additional content for the question
     * @param options the available answer options
     * @param correctAnswer the correct answer
     */
    public Question(Integer id, String category, String difficulty, String questionText,
                    String extraContent, List<String> options, String correctAnswer) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.extraContent = extraContent;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question identifier.
     *
     * @return the question identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the question identifier.
     *
     * @param id the question identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the question category.
     *
     * @return the question category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the question category.
     *
     * @param category the question category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the question difficulty.
     *
     * @return the question difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the question difficulty.
     *
     * @param difficulty the question difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns the main question text.
     *
     * @return the main question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the main question text.
     *
     * @param questionText the main question text
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Returns the additional content for the question.
     *
     * @return the additional content for the question
     */
    public String getExtraContent() {
        return extraContent;
    }

    /**
     * Sets the additional content for the question.
     *
     * @param extraContent the additional content for the question
     */
    public void setExtraContent(String extraContent) {
        this.extraContent = extraContent;
    }

    /**
     * Returns the available answer options.
     *
     * @return the available answer options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * Sets the available answer options.
     *
     * @param options the available answer options
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }

    /**
     * Returns the correct answer.
     *
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer.
     *
     * @param correctAnswer the correct answer
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
