package com.arcade.controller;

import com.arcade.view.CategorySelectionView;
import com.arcade.service.QuestionService;
import javafx.scene.Scene;

import java.util.List;

/**
 * Controls category selection navigation.
 */
public class CategoryController {

    private final Scene scene;
    private final QuestionService questionService;
    private GameController gameController;

    public CategoryController(Scene scene, QuestionService questionService) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene must not be null.");
        }
        if (questionService == null) {
            throw new IllegalArgumentException("QuestionService must not be null.");
        }

        this.scene = scene;
        this.questionService = questionService;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void showCategorySelection() {
        List<String> availableCategories = questionService.getAvailableCategories();
        CategorySelectionView view = new CategorySelectionView(availableCategories);
        List<javafx.scene.control.Button> categoryButtons = view.getCategoryButtons();

        for (int index = 0; index < categoryButtons.size(); index++) {
            String selectedCategory = availableCategories.get(index);
            categoryButtons.get(index).setOnAction(event -> openGame(selectedCategory));
        }

        scene.setRoot(view.getRoot());
    }

    private void openGame(String selectedCategory) {
        if (gameController != null) {
            gameController.showGameView(selectedCategory);
        }
    }
}
