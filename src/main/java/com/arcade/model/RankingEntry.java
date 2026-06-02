package com.arcade.model;

import java.time.LocalDateTime;

/**
 * Represents a completed game result stored in the ranking file.
 */
public class RankingEntry {

    private String playerName;
    private int score;
    private LocalDateTime playedAt;

    /**
     * Creates an empty ranking entry.
     */
    public RankingEntry() {
    }

    /**
     * Creates a ranking entry with all fields initialized.
     *
     * @param playerName the player name
     * @param score the final score
     * @param playedAt the date and time when the game was played
     */
    public RankingEntry(String playerName, int score, LocalDateTime playedAt) {
        this.playerName = playerName;
        this.score = score;
        this.playedAt = playedAt;
    }

    /**
     * Returns the player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player name.
     *
     * @param playerName the player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the final score.
     *
     * @return the final score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the final score.
     *
     * @param score the final score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the date and time when the game was played.
     *
     * @return the play timestamp
     */
    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    /**
     * Sets the date and time when the game was played.
     *
     * @param playedAt the play timestamp
     */
    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}
