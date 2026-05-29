package com.arcade.service;

import com.arcade.model.Question;
import com.arcade.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

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
        if (loadedQuestions == null || loadedQuestions.isEmpty()) {
            throw new IllegalStateException("Questions file does not contain any questions.");
        }

        questions.clear();
        questions.addAll(loadedQuestions);
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
}
