package com.arcade.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

/**
 * Builds a clickable answer option card.
 */
public class AnswerCardView {

    private static final double CARD_WIDTH = 548.0;
    private static final double CARD_HEIGHT = 96.0;

    private final Button root;

    public AnswerCardView(String optionText) {
        root = new Button(optionText);
        root.setMnemonicParsing(false);
        root.setAlignment(Pos.CENTER);
        root.setTextAlignment(TextAlignment.CENTER);
        root.setWrapText(true);
        root.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        root.setMaxSize(Double.MAX_VALUE, CARD_HEIGHT);
    }

    public Button getRoot() {
        return root;
    }

    public void setText(String optionText) {
        root.setText(optionText);
    }
}
