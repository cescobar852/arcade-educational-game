package com.arcade.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Builds the temporary message overlay.
 */
public class MessageOverlayView {

    private final StackPane root;

    public MessageOverlayView() {
        root = new StackPane();

        VBox content = new VBox(16);
        Label titleLabel = new Label("MESSAGE");
        Label placeholderLabel = new Label("(placeholder)");

        content.getChildren().addAll(createCenteredRow(titleLabel),
                createCenteredRow(placeholderLabel));

        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    private StackPane createCenteredRow(Label label) {
        StackPane row = new StackPane();
        row.getChildren().add(label);
        return row;
    }
}
