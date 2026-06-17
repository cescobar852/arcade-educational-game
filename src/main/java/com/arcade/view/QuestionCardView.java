package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

/**
 * Builds the non-interactive question card.
 */
public class QuestionCardView {

    private static final double CARD_WIDTH = 1120.0;
    private static final double CARD_HEIGHT = 220.0;

    private final Button root;

    public QuestionCardView(String questionText) {
        root = new Button(questionText);
        root.setFocusTraversable(false);
        root.setMouseTransparent(true);
        root.setMnemonicParsing(false);
        root.setAlignment(Pos.CENTER);
        root.setTextAlignment(TextAlignment.CENTER);
        root.setWrapText(true);
        root.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        root.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
    }

    public Button getRoot() {
        return root;
    }

    public void setQuestionText(String questionText) {
        root.setText(questionText);
    }
}
