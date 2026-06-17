package com.arcade.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Builds the game header with lives and score labels.
 */
public class HeaderPanelView {

    private final BorderPane root;
    private final Label livesLabel;
    private final Label scoreLabel;

    public HeaderPanelView(String livesText, String scoreText) {
        root = new BorderPane();
        livesLabel = new Label(livesText);
        scoreLabel = new Label(scoreText);

        root.setLeft(livesLabel);
        root.setRight(scoreLabel);
        root.setPadding(new Insets(0.0, 24.0, 0.0, 24.0));
        root.setPrefHeight(72.0);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setLivesText(String livesText) {
        livesLabel.setText(livesText);
    }

    public void setScoreText(String scoreText) {
        scoreLabel.setText(scoreText);
    }
}
