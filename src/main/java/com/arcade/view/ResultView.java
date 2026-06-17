package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Builds the result screen.
 */
public class ResultView {

    private final StackPane root;
    private final Label finalScoreLabel;
    private final TextField playerNameField;
    private final Button submitButton;

    public ResultView(int finalScore) {
        root = new StackPane();

        VBox content = new VBox(16);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("GAME FINISHED");
        finalScoreLabel = new Label("FINAL SCORE: " + Math.max(0, finalScore));
        Label playerNameLabel = new Label("PLAYER NAME");

        playerNameField = new TextField();
        playerNameField.setPromptText("PLAYER NAME");
        playerNameField.setPrefWidth(240);

        submitButton = new Button("SUBMIT");
        submitButton.setPrefWidth(220);

        content.getChildren().addAll(
                createCenteredRow(titleLabel),
                createCenteredRow(finalScoreLabel),
                createCenteredRow(playerNameLabel),
                createCenteredRow(playerNameField),
                createCenteredRow(submitButton));

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

    private StackPane createCenteredRow(Label label) {
        StackPane row = new StackPane();
        row.getChildren().add(label);
        return row;
    }

    private StackPane createCenteredRow(TextField textField) {
        StackPane row = new StackPane();
        row.getChildren().add(textField);
        return row;
    }

    private StackPane createCenteredRow(Button button) {
        StackPane row = new StackPane();
        row.getChildren().add(button);
        return row;
    }
}
