package com.arcade.view;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Builds a clickable answer option card.
 */
public class AnswerCardView {

    private static final double CARD_WIDTH = 548.0;
    private static final double CARD_HEIGHT = 96.0;

    private static final String SPRITESHEET_RESOURCE = "/images/cards/cards-spritesheet.png";

    private static final double BACKGROUND_X = 481.0;
    private static final double BACKGROUND_Y = 4.0;
    private static final double BACKGROUND_WIDTH = 100.0;
    private static final double BACKGROUND_HEIGHT = 128.0;

    private static final double BACKGROUND_FIT_WIDTH = CARD_WIDTH * 0.35;
    private static final double BACKGROUND_FIT_HEIGHT = CARD_HEIGHT;

    private final Button root;
    private Label textLabel;

    public AnswerCardView(String optionText) {
        String normalizedText = normalizeText(optionText);

        root = new Button(normalizedText);
        root.setMnemonicParsing(false);
        root.setAlignment(Pos.CENTER);
        root.setTextAlignment(TextAlignment.CENTER);
        root.setWrapText(true);
        root.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        root.setMaxSize(Double.MAX_VALUE, CARD_HEIGHT);

        StackPane graphic = createSpriteGraphic(normalizedText);
        if (graphic != null) {
            root.setBackground(Background.EMPTY);
            root.setBorder(Border.EMPTY);
            root.setPadding(Insets.EMPTY);
            root.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            root.setGraphic(graphic);
        }
    }

    public Button getRoot() {
        return root;
    }

    public void setText(String optionText) {
        String normalizedText = normalizeText(optionText);
        root.setText(normalizedText);

        if (textLabel != null) {
            textLabel.setText(normalizedText);
        }
    }

    private StackPane createSpriteGraphic(String optionText) {
        Image spritesheet = loadSpritesheet();
        if (spritesheet == null) {
            return null;
        }

        ImageView backgroundLayer = createLayer(
                spritesheet,
                BACKGROUND_X,
                BACKGROUND_Y,
                BACKGROUND_WIDTH,
                BACKGROUND_HEIGHT,
                BACKGROUND_FIT_WIDTH,
                BACKGROUND_FIT_HEIGHT);

        if (backgroundLayer == null) {
            return null;
        }

        Label label = new Label(optionText);
        label.setAlignment(Pos.CENTER);
        label.setMouseTransparent(true);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.BLACK);
        label.setWrapText(true);

        StackPane cardContainer = new StackPane(backgroundLayer, label);
        cardContainer.setPrefSize(BACKGROUND_FIT_WIDTH, BACKGROUND_FIT_HEIGHT);
        cardContainer.setMinSize(BACKGROUND_FIT_WIDTH, BACKGROUND_FIT_HEIGHT);
        cardContainer.setMaxSize(BACKGROUND_FIT_WIDTH, BACKGROUND_FIT_HEIGHT);
        cardContainer.setMouseTransparent(true);
        StackPane.setAlignment(backgroundLayer, Pos.CENTER);
        StackPane.setAlignment(label, Pos.CENTER);

        textLabel = label;
        return cardContainer;
    }

    private Image loadSpritesheet() {
        URL resource = getClass().getResource(SPRITESHEET_RESOURCE);
        if (resource == null) {
            return null;
        }

        try {
            Image image = new Image(resource.toExternalForm());
            if (image.isError()) {
                return null;
            }
            return image;
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private ImageView createLayer(
            Image spritesheet,
            double x,
            double y,
            double width,
            double height,
            double fitWidth,
            double fitHeight) {
        ImageView imageView = new ImageView(spritesheet);
        imageView.setViewport(new Rectangle2D(x, y, width, height));
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setMouseTransparent(true);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    private String normalizeText(String text) {
        return text == null ? "" : text;
    }
}
