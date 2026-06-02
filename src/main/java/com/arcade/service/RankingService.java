package com.arcade.service;

import com.arcade.model.RankingEntry;
import com.arcade.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Handles persistence and retrieval of ranking entries stored in a JSON file.
 */
public class RankingService {

    private static final Path DEFAULT_RANKING_FILE_PATH = Paths.get("src", "main", "resources", "ranking.json");
    private static final DateTimeFormatter PLAYED_AT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path rankingFilePath;
    private final ObjectMapper objectMapper;

    /**
     * Creates a ranking service that uses the default ranking file.
     */
    public RankingService() {
        this(DEFAULT_RANKING_FILE_PATH);
    }

    /**
     * Creates a ranking service that uses the provided ranking file.
     *
     * @param rankingFilePath the ranking file path
     */
    public RankingService(Path rankingFilePath) {
        if (rankingFilePath == null) {
            throw new IllegalArgumentException("Ranking file path must not be null.");
        }

        this.rankingFilePath = rankingFilePath;
        this.objectMapper = JsonUtils.createObjectMapper();
    }

    /**
     * Loads ranking entries from the JSON file.
     *
     * @return the loaded ranking entries
     */
    public List<RankingEntry> loadRanking() {
        if (Files.notExists(rankingFilePath)) {
            System.out.println("Ranking file not found. Creating a new empty ranking.");
            persistEmptyRanking();
            return new ArrayList<>();
        }

        try {
            String jsonContent = Files.readString(rankingFilePath, StandardCharsets.UTF_8);
            if (jsonContent.isBlank()) {
                System.out.println("Ranking file is empty. Using an empty ranking.");
                persistEmptyRanking();
                return new ArrayList<>();
            }

            JsonNode rootNode = objectMapper.readTree(jsonContent);
            if (rootNode == null || !rootNode.isArray()) {
                System.out.println("Ranking file contains invalid JSON. Using an empty ranking.");
                return new ArrayList<>();
            }

            List<RankingEntry> ranking = new ArrayList<>();
            for (JsonNode entryNode : rootNode) {
                ranking.add(readRankingEntry(entryNode));
            }

            return ranking;
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("Unable to load ranking file. Using an empty ranking.");
            return new ArrayList<>();
        }
    }

    /**
     * Saves the provided ranking entries into the JSON file.
     *
     * @param ranking the ranking entries to persist
     */
    public void saveRanking(List<RankingEntry> ranking) {
        List<RankingEntry> rankingToPersist = ranking == null ? Collections.emptyList() : ranking;
        ArrayNode rankingArray = objectMapper.createArrayNode();

        for (RankingEntry rankingEntry : rankingToPersist) {
            if (rankingEntry == null) {
                continue;
            }

            rankingArray.add(createEntryNode(rankingEntry));
        }

        try {
            Path parentDirectory = rankingFilePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rankingArray);
            Files.writeString(
                    rankingFilePath,
                    jsonContent,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save ranking file.", exception);
        }
    }

    /**
     * Adds a new score to the ranking and persists it immediately.
     *
     * @param playerName the player name
     * @param score the final score
     */
    public void addScore(String playerName, int score) {
        List<RankingEntry> ranking = loadRanking();
        ranking.add(new RankingEntry(normalizePlayerName(playerName), Math.max(0, score), LocalDateTime.now().withNano(0)));
        saveRanking(ranking);
    }

    /**
     * Returns all stored ranking entries.
     *
     * @return the stored ranking entries
     */
    public List<RankingEntry> getRanking() {
        return loadRanking();
    }

    /**
     * Returns the top ranking entries ordered by score in descending order.
     *
     * @param limit the maximum number of entries to return
     * @return the top ranking entries
     */
    public List<RankingEntry> getTopScores(int limit) {
        if (limit <= 0) {
            return new ArrayList<>();
        }

        List<RankingEntry> ranking = new ArrayList<>(loadRanking());
        ranking.sort(
                Comparator.comparingInt(RankingEntry::getScore)
                        .reversed()
                        .thenComparing(RankingEntry::getPlayedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(entry -> normalizePlayerName(entry.getPlayerName()), String.CASE_INSENSITIVE_ORDER)
        );

        if (ranking.size() <= limit) {
            return ranking;
        }

        return new ArrayList<>(ranking.subList(0, limit));
    }

    private RankingEntry readRankingEntry(JsonNode entryNode) {
        if (entryNode == null || !entryNode.isObject()) {
            throw new IllegalArgumentException("Invalid ranking entry.");
        }

        JsonNode playerNameNode = entryNode.get("playerName");
        JsonNode scoreNode = entryNode.get("score");
        JsonNode playedAtNode = entryNode.get("playedAt");

        if (playerNameNode == null || !playerNameNode.isTextual()) {
            throw new IllegalArgumentException("Invalid player name.");
        }

        if (scoreNode == null || !scoreNode.isIntegralNumber()) {
            throw new IllegalArgumentException("Invalid score.");
        }

        if (playedAtNode == null || !playedAtNode.isTextual()) {
            throw new IllegalArgumentException("Invalid play timestamp.");
        }

        String playerName = normalizePlayerName(playerNameNode.asText());
        int score = scoreNode.intValue();
        LocalDateTime playedAt = parsePlayedAt(playedAtNode.asText());

        return new RankingEntry(playerName, score, playedAt);
    }

    private ObjectNode createEntryNode(RankingEntry rankingEntry) {
        ObjectNode entryNode = objectMapper.createObjectNode();
        entryNode.put("playerName", normalizePlayerName(rankingEntry.getPlayerName()));
        entryNode.put("score", Math.max(0, rankingEntry.getScore()));

        LocalDateTime playedAt = rankingEntry.getPlayedAt();
        if (playedAt == null) {
            entryNode.putNull("playedAt");
        } else {
            entryNode.put("playedAt", PLAYED_AT_FORMATTER.format(playedAt.withNano(0)));
        }

        return entryNode;
    }

    private void persistEmptyRanking() {
        try {
            saveRanking(Collections.emptyList());
        } catch (IllegalStateException exception) {
            System.out.println("Unable to create ranking file. Using an empty ranking.");
        }
    }

    private String normalizePlayerName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return "Anonymous";
        }

        return playerName.trim();
    }

    private LocalDateTime parsePlayedAt(String playedAtValue) {
        try {
            return LocalDateTime.parse(playedAtValue, PLAYED_AT_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid play timestamp.", exception);
        }
    }
}
