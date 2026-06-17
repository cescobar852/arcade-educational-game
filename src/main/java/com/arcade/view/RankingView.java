package com.arcade.view;

import com.arcade.model.RankingEntry;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds the ranking screen.
 */
public class RankingView {

    private static final DateTimeFormatter PLAYED_AT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final StackPane root;
    private final Button mainMenuButton;

    public RankingView(List<RankingEntry> rankingEntries) {
        root = new StackPane();

        VBox content = new VBox(16);
        content.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("RANKING");
        VBox rankingList = new VBox(8);
        rankingList.setAlignment(Pos.CENTER_LEFT);
        rankingList.setMaxWidth(760);

        if (rankingEntries == null || rankingEntries.isEmpty()) {
            rankingList.getChildren().add(createEntryLabel("NO RANKING ENTRIES AVAILABLE"));
        } else {
            int index = 1;
            for (RankingEntry rankingEntry : rankingEntries) {
                rankingList.getChildren().add(createEntryLabel(formatEntry(index++, rankingEntry)));
            }
        }

        ScrollPane rankingScrollPane = new ScrollPane(rankingList);
        rankingScrollPane.setFitToWidth(true);
        rankingScrollPane.setPrefViewportHeight(420.0);
        rankingScrollPane.setMaxWidth(760.0);

        mainMenuButton = new Button("MAIN MENU");
        mainMenuButton.setPrefWidth(220);

        content.getChildren().addAll(
                createCenteredRow(titleLabel),
                rankingScrollPane,
                createCenteredRow(mainMenuButton));

        root.getChildren().add(content);
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }

    private Label createEntryLabel(String text) {
        Label entryLabel = new Label(text);
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
