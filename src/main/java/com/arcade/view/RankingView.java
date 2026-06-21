package com.arcade.view;

import com.arcade.model.RankingEntry;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds the ranking screen.
 */
public class RankingView {

    private static final DateTimeFormatter PLAYED_AT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String BACKGROUND_RESOURCE = "/images/backgrounds/menu-background.png";

    private final StackPane root;
    private final Button mainMenuButton;

    public RankingView(List<RankingEntry> rankingEntries) {
        root = new StackPane();

        ImageView backgroundImageView = SpriteUiFactory.createCoverBackgroundImageView(root, BACKGROUND_RESOURCE);

        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("RANKING");
        SpriteUiFactory.styleScreenLabel(
                titleLabel,
                backgroundImageView != null,
                32.0,
                FontWeight.BOLD,
                Pos.CENTER,
                TextAlignment.CENTER);
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");

        VBox rankingList = new VBox(8);
        rankingList.setAlignment(Pos.CENTER_LEFT);
        rankingList.setMaxWidth(760);
        rankingList.setFillWidth(true);
        rankingList.setBackground(Background.EMPTY);
        rankingList.setStyle(
                "-fx-background-color: transparent;"
        );
        if (rankingEntries == null || rankingEntries.isEmpty()) {
            rankingList.getChildren().add(createEntryLabel("NO RANKING ENTRIES AVAILABLE", backgroundImageView != null));
        } else {
            int index = 1;
            for (RankingEntry rankingEntry : rankingEntries) {
                rankingList.getChildren().add(createEntryLabel(formatEntry(index++, rankingEntry), backgroundImageView != null));
            }
        }

        ScrollPane rankingScrollPane = new ScrollPane(rankingList);
        rankingScrollPane.setFitToWidth(true);
        rankingScrollPane.setPrefViewportHeight(420.0);
        rankingScrollPane.setMaxWidth(760.0);
        rankingScrollPane.setBackground(Background.EMPTY);
        rankingScrollPane.setBorder(Border.EMPTY);
        rankingScrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;"
        );

        mainMenuButton = SpriteUiFactory.createSpriteButton("MAIN MENU", 220);

        content.getChildren().addAll(
                SpriteUiFactory.createCenteredRow(titleLabel),
                rankingScrollPane,
                SpriteUiFactory.createCenteredRow(mainMenuButton));

        if (backgroundImageView != null) {
            root.getChildren().add(backgroundImageView);
        }

        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }

    private Label createEntryLabel(String text, boolean useBackgroundStyle) {
        Label entryLabel = new Label(text);
        SpriteUiFactory.styleScreenLabel(
                entryLabel,
                useBackgroundStyle,
                18.0,
                FontWeight.NORMAL,
                Pos.CENTER_LEFT,
                TextAlignment.LEFT);
        entryLabel.setStyle("-fx-text-fill: #FFFFFF;");
        entryLabel.setWrapText(true);
        return entryLabel;
    }

    private String formatEntry(int index, RankingEntry rankingEntry) {
        if (rankingEntry == null) {
            return index + ". ";
        }

        String playerName = rankingEntry.getPlayerName() == null ? "Anonymous" : rankingEntry.getPlayerName();
        String playedAt = rankingEntry.getPlayedAt() == null
                ? "N/A"
                : rankingEntry.getPlayedAt().format(PLAYED_AT_FORMATTER);

        return index + ". " + playerName + " - " + rankingEntry.getScore() + " - " + playedAt;
    }
}
