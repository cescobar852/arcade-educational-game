package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Builds the main menu screen.
 */
public class MainMenuView {

    private static final String BACKGROUND_RESOURCE = "/images/backgrounds/menu-background.png";

    private final StackPane root;
    private final Button playButton;
    private final Button exitButton;

    public MainMenuView() {
        root = new StackPane();

        ImageView backgroundImageView = SpriteUiFactory.createCoverBackgroundImageView(root, BACKGROUND_RESOURCE);
        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("ARCADE GAME");
        SpriteUiFactory.styleScreenLabel(
                titleLabel,
                backgroundImageView != null,
                32.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);

        playButton = SpriteUiFactory.createSpriteButton("PLAY", 220);
        exitButton = SpriteUiFactory.createSpriteButton("EXIT", 220);

        content.getChildren().addAll(SpriteUiFactory.createCenteredRow(titleLabel),
                SpriteUiFactory.createCenteredRow(playButton),
                SpriteUiFactory.createCenteredRow(exitButton));

        if (backgroundImageView != null) {
            root.getChildren().add(backgroundImageView);
        }
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
}
