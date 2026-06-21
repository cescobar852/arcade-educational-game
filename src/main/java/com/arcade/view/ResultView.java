package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Builds the result screen.
 */
public class ResultView {

    private static final String BACKGROUND_RESOURCE = "/images/backgrounds/menu-background.png";

    private final StackPane root;
    private final Label finalScoreLabel;
    private final TextField playerNameField;
    private final Button submitButton;

    public ResultView(int finalScore) {
        root = new StackPane();

        ImageView backgroundImageView = SpriteUiFactory.createCoverBackgroundImageView(root, BACKGROUND_RESOURCE);

        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("GAME FINISHED");
        SpriteUiFactory.styleScreenLabel(
                titleLabel,
                backgroundImageView != null,
                32.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);

        finalScoreLabel = new Label("FINAL SCORE: " + Math.max(0, finalScore));
        SpriteUiFactory.styleScreenLabel(
                finalScoreLabel,
                backgroundImageView != null,
                20.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);

        Label playerNameLabel = new Label("PLAYER NAME");
        SpriteUiFactory.styleScreenLabel(
                playerNameLabel,
                backgroundImageView != null,
                18.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);

        playerNameField = new TextField();
        playerNameField.setPromptText("PLAYER NAME");
        playerNameField.setPrefWidth(240);
        playerNameField.setMaxWidth(240);
        playerNameField.setAlignment(Pos.CENTER);

        submitButton = SpriteUiFactory.createSpriteButton("SUBMIT", 220);

        content.getChildren().addAll(
                SpriteUiFactory.createCenteredRow(titleLabel),
                SpriteUiFactory.createCenteredRow(finalScoreLabel),
                SpriteUiFactory.createCenteredRow(playerNameLabel),
                SpriteUiFactory.createCenteredRow(playerNameField),
                SpriteUiFactory.createCenteredRow(submitButton));

        if (backgroundImageView != null) {
            root.getChildren().add(backgroundImageView);
        }

        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public TextField getPlayerNameField() {
        return playerNameField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Label getFinalScoreLabel() {
        return finalScoreLabel;
    }

    public void setFinalScore(int finalScore) {
        finalScoreLabel.setText("FINAL SCORE: " + Math.max(0, finalScore));
    }
}
