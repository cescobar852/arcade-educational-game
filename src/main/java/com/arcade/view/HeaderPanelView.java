package com.arcade.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds the game header with lives icons and score labels.
 */
public class HeaderPanelView {

    private static final String HEART_RESOURCE = "/images/icons/Heart.png";
    private static final double HEART_ICON_SIZE = 30.0;
    private static final double HEART_ICON_SPACING = 6.0;
    private static final Pattern DIGIT_PATTERN = Pattern.compile("(\\d+)");
    private static final Image HEART_IMAGE = loadImage(HEART_RESOURCE);

    private final BorderPane root;
    private final HBox livesContainer;
    private final Label livesFallbackLabel;
    private final Label scoreLabel;
    private String currentLivesText;

    public HeaderPanelView(String livesText, String scoreText) {
        root = new BorderPane();
        livesContainer = new HBox(HEART_ICON_SPACING);
        livesContainer.setAlignment(Pos.CENTER_LEFT);
        livesFallbackLabel = new Label();
        scoreLabel = new Label(scoreText);

        updateLivesDisplay(livesText);
        root.setLeft(livesContainer);
        root.setRight(scoreLabel);
        root.setPadding(new Insets(0.0, 24.0, 0.0, 24.0));
        root.setPrefHeight(72.0);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setLivesText(String livesText) {
        updateLivesDisplay(livesText);
    }

    public void setScoreText(String scoreText) {
        scoreLabel.setText(scoreText);
    }

    private void updateLivesDisplay(String livesText) {
        currentLivesText = livesText == null ? "" : livesText;

        int remainingLives = extractLivesCount(currentLivesText);
        if (HEART_IMAGE == null || remainingLives < 0) {
            livesFallbackLabel.setText(currentLivesText);
            livesContainer.getChildren().setAll(livesFallbackLabel);
            return;
        }

        if (remainingLives <= 0) {
            livesContainer.getChildren().clear();
            return;
        }

        livesContainer.setSpacing(HEART_ICON_SPACING);
        livesContainer.getChildren().clear();
        for (int index = 0; index < remainingLives; index++) {
            ImageView heartIcon = new ImageView(HEART_IMAGE);
            heartIcon.setFitWidth(HEART_ICON_SIZE);
            heartIcon.setFitHeight(HEART_ICON_SIZE);
            heartIcon.setPreserveRatio(true);
            heartIcon.setSmooth(true);
            heartIcon.setCache(true);
            livesContainer.getChildren().add(heartIcon);
        }
    }

    private int extractLivesCount(String livesText) {
        if (livesText == null || livesText.isBlank()) {
            return -1;
        }

        Matcher matcher = DIGIT_PATTERN.matcher(livesText);
        if (matcher.find()) {
            try {
                return Math.max(0, Integer.parseInt(matcher.group(1)));
            } catch (NumberFormatException exception) {
                return -1;
            }
        }

        int heartCount = 0;
        for (int index = 0; index < livesText.length(); index++) {
            char current = livesText.charAt(index);
            if (current == '\u2665' || current == '\u2764') {
                heartCount++;
            }
        }

        return heartCount > 0 ? heartCount : -1;
    }

    private static Image loadImage(String resourcePath) {
        URL resource = HeaderPanelView.class.getResource(resourcePath);
        if (resource == null) {
            return null;
        }

        try {
            Image image = new Image(resource.toExternalForm());
            return image.isError() ? null : image;
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
