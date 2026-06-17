package com.arcade.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Builds the main menu screen.
 */
public class MainMenuView {

    private final StackPane root;
    private final Button playButton;
    private final Button exitButton;

    public MainMenuView() {
        root = new StackPane();

        VBox content = new VBox(24);
        Label titleLabel = new Label("ARCADE GAME");
        playButton = new Button("PLAY");
        exitButton = new Button("EXIT");

        playButton.setPrefWidth(220);
        exitButton.setPrefWidth(220);

        content.getChildren().addAll(createCenteredRow(titleLabel),
                createCenteredRow(playButton),
                createCenteredRow(exitButton));

        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    private StackPane createCenteredRow(Label label) {
        StackPane row = new StackPane();
        row.getChildren().add(label);
        return row;
    }

    private StackPane createCenteredRow(Button button) {
        StackPane row = new StackPane();
        row.getChildren().add(button);
        return row;
    }
}
