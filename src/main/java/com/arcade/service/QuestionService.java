package com.arcade.service;

import com.arcade.model.Question;
import com.arcade.util.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Centralizes question loading and access for the application.
 */
public class QuestionService {

    private final List<Question> questions;

    /**
     * Creates a question service and loads the initial question set.
     */
    public QuestionService() {
        this.questions = new ArrayList<>();
        loadQuestions();
    }

    /**
     * Loads questions from the default JSON resource into memory.
     */
    public void loadQuestions() {
        List<Question> loadedQuestions = JsonUtils.loadQuestions();
        questions.clear();
        if (loadedQuestions != null) {
            questions.addAll(loadedQuestions);
        }
    }

    /**
     * Returns all loaded questions as an immutable copy.
     *
     * @return the loaded questions
     */
    public List<Question> getAllQuestions() {
        return List.copyOf(questions);
    }

    /**
     * Finds a question by its identifier.
     *
     * @param id the question identifier
     * @return the matching question, or {@code null} if not found
     */
    public Question getQuestionById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Question id must not be null.");
        }

        for (Question question : questions) {
            if (id.equals(question.getId())) {
                return question;
            }
        }

        return null;
    }

    /**
     * Returns the available question categories in their display order.
     *
     * @return the unique categories loaded from the current question set
     */
    public List<String> getAvailableCategories() {
        Map<String, String> categoriesByKey = new LinkedHashMap<>();

        for (Question question : questions) {
            if (question == null) {
                continue;
            }

            String category = normalizeCategory(question.getCategory());
            if (category == null) {
                continue;
            }

            categoriesByKey.putIfAbsent(category.toLowerCase(Locale.ROOT), category);
        }

        return List.copyOf(categoriesByKey.values());
    }

    /**
     * Returns only the questions that belong to the requested category.
     *
     * @param category the category to filter by
     * @return the matching questions, or an empty list if no match exists
     */
    public List<Question> getQuestionsByCategory(String category) {
        String requestedCategory = normalizeCategory(category);
        if (requestedCategory == null) {
            return List.of();
        }

        String requestedCategoryKey = requestedCategory.toLowerCase(Locale.ROOT);
        List<Question> matchingQuestions = new ArrayList<>();

        for (Question question : questions) {
            if (question == null) {
                continue;
            }

            String questionCategory = normalizeCategory(question.getCategory());
            if (questionCategory != null
                    && questionCategory.toLowerCase(Locale.ROOT).equals(requestedCategoryKey)) {
                matchingQuestions.add(question);
            }
        }

        return List.copyOf(matchingQuestions);
    }

    /**
     * Returns only the questions that belong to the requested category and difficulty.
     *
     * @param category the category to filter by
     * @param difficulty the difficulty to filter by
     * @return the matching questions, or an empty list if no match exists
     */
    public List<Question> getQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        String requestedCategory = normalizeCategory(category);
        String requestedDifficulty = normalizeDifficulty(difficulty);
        if (requestedCategory == null || requestedDifficulty == null) {
            return List.of();
        }

        String requestedCategoryKey = requestedCategory.toLowerCase(Locale.ROOT);
        List<Question> matchingQuestions = new ArrayList<>();

        for (Question question : questions) {
            if (question == null) {
                continue;
            }

            String questionCategory = normalizeCategory(question.getCategory());
            String questionDifficulty = normalizeDifficulty(question.getDifficulty());
            if (questionCategory != null
                    && questionDifficulty != null
                    && questionCategory.toLowerCase(Locale.ROOT).equals(requestedCategoryKey)
                    && questionDifficulty.equals(requestedDifficulty)) {
                matchingQuestions.add(question);
            }
        }

        return List.copyOf(matchingQuestions);
    }

    /**
     * Temporary console entry point for manual verification.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        QuestionService questionService = new QuestionService();
        List<Question> loadedQuestions = questionService.getAllQuestions();

        System.out.println("Questions loaded: " + loadedQuestions.size());
        if (!loadedQuestions.isEmpty()) {
            Question firstQuestion = loadedQuestions.get(0);
            System.out.println("First question id: " + firstQuestion.getId());
            System.out.println("First question text: " + firstQuestion.getQuestionText());

            Integer questionId = firstQuestion.getId();
            Question foundQuestion = questionService.getQuestionById(questionId);
            System.out.println(
                    "Question lookup for id " + questionId + ": "
                            + (foundQuestion != null ? foundQuestion.getQuestionText() : "Question not found")
            );
        }
    }

    private String normalizeCategory(String category) {
        if (category == null) {
            return null;
        }

        String normalizedCategory = category.trim();
        if (normalizedCategory.isEmpty()) {
            return null;
        }

        return normalizedCategory;
    }

    private String normalizeDifficulty(String difficulty) {
        if (difficulty == null) {
            return null;
        }

        String normalizedDifficulty = difficulty.trim();
        if (normalizedDifficulty.isEmpty()) {
            return null;
        }

        String upperCaseDifficulty = normalizedDifficulty.toUpperCase(Locale.ROOT);
        return switch (upperCaseDifficulty) {
            case "EASY", "MEDIUM", "HARD" -> upperCaseDifficulty;
            default -> null;
        };
    }
}
