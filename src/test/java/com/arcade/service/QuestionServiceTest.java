package com.arcade.service;

import com.arcade.model.Question;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests question loading and category filtering from the JSON resource.
 */
class QuestionServiceTest {

    @Test
    void shouldLoadQuestionsSuccessfully() {
        QuestionService questionService = new QuestionService();
        List<Question> questions = questionService.getAllQuestions();

        assertNotNull(questions, "Loaded questions should not be null.");
        assertFalse(questions.isEmpty(), "Loaded questions should not be empty.");
    }

    @Test
    void shouldLoadAllQuestionFieldsCorrectly() {
        QuestionService questionService = new QuestionService();
        Question question = questionService.getAllQuestions().get(0);

        assertNotNull(question.getId(), "Question id should be present.");
        assertNotNull(question.getCategory(), "Question category should be present.");
        assertFalse(question.getCategory().isBlank(), "Question category should not be blank.");
        assertNotNull(question.getDifficulty(), "Question difficulty should be present.");
        assertEquals(question.getDifficulty().toUpperCase(), question.getDifficulty(),
                "Question difficulty should be stored in uppercase.");
        assertNotNull(question.getQuestionText(), "Question text should be present.");
        assertNotNull(question.getOptions(), "Question options should be present.");
        assertFalse(question.getOptions().isEmpty(), "Question options should not be empty.");
        assertNotNull(question.getCorrectAnswer(), "Question correct answer should be present.");
    }

    @Test
    void shouldReturnAvailableCategories() {
        QuestionService questionService = new QuestionService();
        List<String> categories = questionService.getAvailableCategories();

        assertFalse(categories.isEmpty(), "Available categories should not be empty.");
        assertTrue(categories.stream().allMatch(category -> category != null && !category.isBlank()),
                "Available categories should contain readable values.");
    }

    @Test
    void shouldReturnUniqueCategories() {
        QuestionService questionService = new QuestionService();
        List<String> categories = questionService.getAvailableCategories();

        assertEquals(categories.size(), new HashSet<>(categories).size(),
                "Available categories should not contain duplicates.");
    }

    @Test
    void shouldReturnQuestionsForCategory() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategory(category);

        assertFalse(questions.isEmpty(), "Questions for a known category should not be empty.");
        assertTrue(questions.stream()
                        .allMatch(question -> category.equalsIgnoreCase(question.getCategory())),
                "All returned questions should belong to the requested category.");
    }

    @Test
    void shouldReturnEmptyListForUnknownCategory() {
        QuestionService questionService = new QuestionService();
        List<Question> questions = questionService.getQuestionsByCategory("Unknown Category");

        assertTrue(questions.isEmpty(), "Unknown categories should return an empty list.");
    }

    @Test
    void shouldReturnQuestionsByCategoryAndDifficulty() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, "EASY");

        assertFalse(questions.isEmpty(), "Questions for a known category and difficulty should not be empty.");
        assertTrue(questions.stream()
                        .allMatch(question -> category.equalsIgnoreCase(question.getCategory())
                                && "EASY".equals(question.getDifficulty())),
                "All returned questions should match the requested category and difficulty.");
    }

    @Test
    void shouldReturnOnlyEasyQuestions() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, "EASY");

        assertFalse(questions.isEmpty(), "Easy questions should be available for the selected category.");
        assertTrue(questions.stream().allMatch(question -> "EASY".equals(question.getDifficulty())),
                "All returned questions should be EASY.");
    }

    @Test
    void shouldReturnOnlyMediumQuestions() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, "MEDIUM");

        assertFalse(questions.isEmpty(), "Medium questions should be available for the selected category.");
        assertTrue(questions.stream().allMatch(question -> "MEDIUM".equals(question.getDifficulty())),
                "All returned questions should be MEDIUM.");
    }

    @Test
    void shouldReturnOnlyHardQuestions() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, "HARD");

        assertFalse(questions.isEmpty(), "Hard questions should be available for the selected category.");
        assertTrue(questions.stream().allMatch(question -> "HARD".equals(question.getDifficulty())),
                "All returned questions should be HARD.");
    }

    @Test
    void shouldReturnEmptyListForUnknownDifficulty() {
        QuestionService questionService = new QuestionService();
        String category = questionService.getAvailableCategories().get(0);
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, "EXPERT");

        assertTrue(questions.isEmpty(), "Unknown difficulties should return an empty list.");
    }
}
