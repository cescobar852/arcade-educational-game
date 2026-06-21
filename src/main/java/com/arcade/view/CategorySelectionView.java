package com.arcade.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Builds the category selection screen.
 */
public class CategorySelectionView {

    private static final String BACKGROUND_RESOURCE = "/images/backgrounds/menu-background.png";

    private final StackPane root;
    private final List<Button> categoryButtons;

    public CategorySelectionView(List<String> categories) {
        root = new StackPane();

        ImageView backgroundImageView = SpriteUiFactory.createCoverBackgroundImageView(root, BACKGROUND_RESOURCE);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("SELECT CATEGORY");
        SpriteUiFactory.styleScreenLabel(
                titleLabel,
                backgroundImageView != null,
                30.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);
        content.getChildren().add(SpriteUiFactory.createCenteredRow(titleLabel));

        List<Button> buttons = new ArrayList<>();
        if (categories != null) {
            for (String category : categories) {
                Button categoryButton = SpriteUiFactory.createSpriteButton(category, 240);
                buttons.add(categoryButton);
                content.getChildren().add(SpriteUiFactory.createCenteredRow(categoryButton));
            }
        }

        if (buttons.isEmpty()) {
            Label emptyLabel = new Label("NO CATEGORIES AVAILABLE");
            SpriteUiFactory.styleScreenLabel(
                    emptyLabel,
                    backgroundImageView != null,
                    18.0,
                    FontWeight.BOLD,
                    Pos.CENTER,
                    TextAlignment.CENTER);
            content.getChildren().add(SpriteUiFactory.createCenteredRow(emptyLabel));
        }

        categoryButtons = List.copyOf(buttons);

        if (backgroundImageView != null) {
            root.getChildren().add(backgroundImageView);
        }
        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public List<Button> getCategoryButtons() {
        return categoryButtons;
    }
}
