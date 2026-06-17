package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the category selection screen.
 */
public class CategorySelectionView {

    private final StackPane root;
    private final List<Button> categoryButtons;

    public CategorySelectionView(List<String> categories) {
        root = new StackPane();

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("SELECT CATEGORY");
        content.getChildren().add(createCenteredRow(titleLabel));

        List<Button> buttons = new ArrayList<>();
        if (categories != null) {
            for (String category : categories) {
                Button categoryButton = new Button(category);
                categoryButton.setPrefWidth(240);
                buttons.add(categoryButton);
                content.getChildren().add(createCenteredRow(categoryButton));
            }
        }

        if (buttons.isEmpty()) {
            Label emptyLabel = new Label("NO CATEGORIES AVAILABLE");
            content.getChildren().add(createCenteredRow(emptyLabel));
        }

        categoryButtons = List.copyOf(buttons);
        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public List<Button> getCategoryButtons() {
        return categoryButtons;
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
