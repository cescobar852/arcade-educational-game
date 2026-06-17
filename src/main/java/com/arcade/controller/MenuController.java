package com.arcade.controller;

import com.arcade.view.MainMenuView;
import javafx.scene.Scene;

/**
 * Controls the main menu navigation.
 */
public class MenuController {

    private final Scene scene;
    private CategoryController categoryController;
    private Runnable exitAction = () -> { };

    public MenuController(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene must not be null.");
        }

        this.scene = scene;
    }

    public void setCategoryController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    public void setExitAction(Runnable exitAction) {
        this.exitAction = exitAction == null ? () -> { } : exitAction;
    }

    public void start() {
        showMainMenu();
    }

    public void showMainMenu() {
        MainMenuView view = new MainMenuView();
        view.getPlayButton().setOnAction(event -> openCategorySelection());
        view.getExitButton().setOnAction(event -> exitAction.run());
        scene.setRoot(view.getRoot());
    }

    public void openCategorySelection() {
        if (categoryController != null) {
            categoryController.showCategorySelection();
        }
    }
}
