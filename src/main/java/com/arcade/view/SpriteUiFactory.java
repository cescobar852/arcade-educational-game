package com.arcade.view;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Shared helpers for sprite-based menu and background rendering.
 */
final class SpriteUiFactory {

    private static final String BUTTON_SPRITESHEET_RESOURCE = "/images/cards/cards-spritesheet.png";

    private static final double BUTTON_BACKGROUND_X = 252.0;
    private static final double BUTTON_BACKGROUND_Y = 223.0;
    private static final double BUTTON_BACKGROUND_WIDTH = 96.0;
    private static final double BUTTON_BACKGROUND_HEIGHT = 29.0;

    private static final double BUTTON_LABEL_FONT_SIZE = 18.0;

    private SpriteUiFactory() {
    }

    static ImageView createCoverBackgroundImageView(StackPane root, String resourcePath) {
        if (root == null || resourcePath == null) {
            return null;
        }

        Image image = loadImage(resourcePath);
        if (image == null) {
            return null;
        }

        ImageView imageView = new ImageView(image);
        imageView.setMouseTransparent(true);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());

        root.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) ->
                updateCoverViewport(imageView, image, newBounds.getWidth(), newBounds.getHeight()));
        updateCoverViewport(imageView, image, root.getLayoutBounds().getWidth(), root.getLayoutBounds().getHeight());

        return imageView;
    }

    static Button createSpriteButton(String text, double prefWidth) {
        String normalizedText = normalizeText(text);
        Button button = new Button(normalizedText);
        button.setMnemonicParsing(false);
        button.setFocusTraversable(false);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setWrapText(true);
        button.setPrefWidth(prefWidth);

        StackPane graphic = createSpriteButtonGraphic(normalizedText, prefWidth);
        if (graphic != null) {
            double prefHeight = Math.max(1.0, Math.round(prefWidth * BUTTON_BACKGROUND_HEIGHT / BUTTON_BACKGROUND_WIDTH));
            button.setMinSize(prefWidth, prefHeight);
            button.setPrefSize(prefWidth, prefHeight);
            button.setMaxSize(prefWidth, prefHeight);
            button.setBackground(Background.EMPTY);
            button.setBorder(Border.EMPTY);
            button.setPadding(Insets.EMPTY);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setGraphic(graphic);
        }

        return button;
    }

    static StackPane createCenteredRow(Node node) {
        StackPane row = new StackPane();
        if (node != null) {
            row.getChildren().add(node);
        }
        return row;
    }

    static void styleScreenLabel(
            Label label,
            boolean useBackgroundStyle,
            double fontSize,
            FontWeight fontWeight,
            Pos alignment,
            TextAlignment textAlignment) {
        if (label == null) {
            return;
        }

        Pos appliedAlignment = alignment == null ? Pos.CENTER : alignment;
        TextAlignment appliedTextAlignment = textAlignment == null ? TextAlignment.CENTER : textAlignment;

        label.setAlignment(appliedAlignment);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setTextAlignment(appliedTextAlignment);
        label.setWrapText(true);

        if (useBackgroundStyle) {
            FontWeight appliedWeight = fontWeight == null ? FontWeight.NORMAL : fontWeight;
            label.setFont(Font.font("System", appliedWeight, fontSize));
            label.setTextFill(Color.WHITE);
        }
    }

    private static StackPane createSpriteButtonGraphic(String text, double prefWidth) {
        Image spritesheet = loadImage(BUTTON_SPRITESHEET_RESOURCE);
        if (spritesheet == null) {
            return null;
        }

        double prefHeight = Math.max(1.0, Math.round(prefWidth * BUTTON_BACKGROUND_HEIGHT / BUTTON_BACKGROUND_WIDTH));
        ImageView backgroundLayer = createLayer(
                spritesheet,
                BUTTON_BACKGROUND_X,
                BUTTON_BACKGROUND_Y,
                BUTTON_BACKGROUND_WIDTH,
                BUTTON_BACKGROUND_HEIGHT,
                prefWidth,
                prefHeight);

        if (backgroundLayer == null) {
            return null;
        }

        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("System", FontWeight.BOLD, BUTTON_LABEL_FONT_SIZE));
        label.setMouseTransparent(true);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.BLACK);
        label.setWrapText(true);

        StackPane buttonContainer = new StackPane(backgroundLayer, label);
        buttonContainer.setPrefSize(prefWidth, prefHeight);
        buttonContainer.setMinSize(prefWidth, prefHeight);
        buttonContainer.setMaxSize(prefWidth, prefHeight);
        buttonContainer.setMouseTransparent(true);
        StackPane.setAlignment(backgroundLayer, Pos.CENTER);
        StackPane.setAlignment(label, Pos.CENTER);
        return buttonContainer;
    }

    private static ImageView createLayer(
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

    private static Image loadImage(String resourcePath) {
        URL resource = SpriteUiFactory.class.getResource(resourcePath);
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

    private static void updateCoverViewport(ImageView imageView, Image image, double containerWidth, double containerHeight) {
        if (imageView == null || image == null || containerWidth <= 0.0 || containerHeight <= 0.0) {
            return;
        }

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        if (imageWidth <= 0.0 || imageHeight <= 0.0) {
            return;
        }

        double containerRatio = containerWidth / containerHeight;
        double imageRatio = imageWidth / imageHeight;
        double viewportX = 0.0;
        double viewportY = 0.0;
        double viewportWidth = imageWidth;
        double viewportHeight = imageHeight;

        if (containerRatio > imageRatio) {
            viewportHeight = imageWidth / containerRatio;
            viewportY = (imageHeight - viewportHeight) / 2.0;
        } else if (containerRatio < imageRatio) {
            viewportWidth = imageHeight * containerRatio;
            viewportX = (imageWidth - viewportWidth) / 2.0;
        }

        imageView.setViewport(new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight));
    }

    private static String normalizeText(String text) {
        return text == null ? "" : text;
    }
}
