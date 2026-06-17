package com.arcade.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Builds the fixed visual skeleton of the game screen.
 */
public class GameView {

    private static final double CONTENT_WIDTH = 1120.0;
    private static final double ANSWER_ROW_SPACING = 24.0;
    private static final double ANSWER_COLUMN_SPACING = 16.0;

    private final BorderPane root;
    private final HeaderPanelView headerPanelView;
    private final QuestionCardView questionCardView;
    private final List<AnswerCardView> answerCards;

    public GameView(String questionText, List<String> options, String livesText, String scoreText) {
        root = new BorderPane();

        headerPanelView = new HeaderPanelView(normalizeText(livesText), normalizeText(scoreText));
        questionCardView = new QuestionCardView(normalizeText(questionText));

        List<String> normalizedOptions = normalizeOptions(options);
        AnswerCardView answerCardOne = new AnswerCardView(normalizedOptions.get(0));
        AnswerCardView answerCardTwo = new AnswerCardView(normalizedOptions.get(1));
        AnswerCardView answerCardThree = new AnswerCardView(normalizedOptions.get(2));
        AnswerCardView answerCardFour = new AnswerCardView(normalizedOptions.get(3));
        answerCards = List.of(answerCardOne, answerCardTwo, answerCardThree, answerCardFour);

        VBox content = new VBox(24.0);
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);
        content.setPadding(new Insets(24.0, 0.0, 24.0, 0.0));

        StackPane questionArea = new StackPane(questionCardView.getRoot());
        questionArea.setPrefWidth(CONTENT_WIDTH);
        questionArea.setMaxWidth(CONTENT_WIDTH);
        questionArea.setAlignment(Pos.CENTER);

        VBox answersArea = new VBox(ANSWER_COLUMN_SPACING);
        answersArea.setAlignment(Pos.CENTER);
        answersArea.setMaxWidth(CONTENT_WIDTH);
        answersArea.getChildren().addAll(
                createAnswerRow(answerCardOne, answerCardTwo),
                createAnswerRow(answerCardThree, answerCardFour));

        content.getChildren().addAll(questionArea, answersArea);

        root.setTop(headerPanelView.getRoot());
        root.setCenter(content);
    }

    public BorderPane getRoot() {
        return root;
    }

    public List<AnswerCardView> getAnswerCards() {
        return answerCards;
    }

    public HeaderPanelView getHeaderPanelView() {
        return headerPanelView;
    }

    public QuestionCardView getQuestionCardView() {
        return questionCardView;
    }

    private HBox createAnswerRow(AnswerCardView leftCard, AnswerCardView rightCard) {
        HBox row = new HBox(ANSWER_ROW_SPACING);
        row.setAlignment(Pos.CENTER);
        row.setPrefWidth(CONTENT_WIDTH);
        row.setMaxWidth(CONTENT_WIDTH);
        row.getChildren().addAll(leftCard.getRoot(), rightCard.getRoot());
        HBox.setHgrow(leftCard.getRoot(), Priority.ALWAYS);
        HBox.setHgrow(rightCard.getRoot(), Priority.ALWAYS);
        return row;
    }

    private List<String> normalizeOptions(List<String> options) {
        List<String> normalizedOptions = new ArrayList<>(4);

        if (options != null) {
            for (String option : options) {
                if (normalizedOptions.size() == 4) {
                    break;
                }
                normalizedOptions.add(normalizeText(option));
            }
        }

        while (normalizedOptions.size() < 4) {
            normalizedOptions.add("");
        }

        return normalizedOptions;
    }

    private String normalizeText(String text) {
        return text == null ? "" : text;
    }
}
