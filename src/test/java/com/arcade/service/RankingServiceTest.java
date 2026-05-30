package com.arcade.service;

import com.arcade.model.RankingEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests ranking persistence, loading, sorting, and limit handling.
 */
class RankingServiceTest {

    @TempDir
    Path tempDir;

    private Path rankingFile;
    private RankingService rankingService;

    @BeforeEach
    void setUp() {
        rankingFile = tempDir.resolve("ranking.json");
        rankingService = new RankingService(rankingFile);
    }

    @Test
    void shouldCreateEmptyRankingWhenFileDoesNotExist() {
        assertFalse(Files.exists(rankingFile), "Ranking file should not exist before the test starts.");

        List<RankingEntry> ranking = rankingService.loadRanking();

        assertNotNull(ranking, "Ranking should not be null when the file is missing.");
        assertTrue(ranking.isEmpty(), "Ranking should be empty when the file is missing.");
        assertTrue(Files.exists(rankingFile), "Ranking file should be created when it does not exist.");
    }

    @Test
    void shouldLoadRankingSuccessfully() {
        List<RankingEntry> expectedRanking = List.of(
                new RankingEntry("Alice", 1200, LocalDateTime.of(2026, 5, 30, 14, 30, 0)),
                new RankingEntry("Bob", 900, LocalDateTime.of(2026, 5, 30, 15, 0, 0))
        );
        rankingService.saveRanking(expectedRanking);

        List<RankingEntry> loadedRanking = rankingService.loadRanking();

        assertEquals(2, loadedRanking.size(), "Two ranking entries should be loaded.");
        assertEquals("Alice", loadedRanking.get(0).getPlayerName(), "First player name should match.");
        assertEquals(1200, loadedRanking.get(0).getScore(), "First score should match.");
        assertEquals(LocalDateTime.of(2026, 5, 30, 14, 30, 0), loadedRanking.get(0).getPlayedAt(),
                "First timestamp should match.");
        assertEquals("Bob", loadedRanking.get(1).getPlayerName(), "Second player name should match.");
        assertEquals(900, loadedRanking.get(1).getScore(), "Second score should match.");
        assertEquals(LocalDateTime.of(2026, 5, 30, 15, 0, 0), loadedRanking.get(1).getPlayedAt(),
                "Second timestamp should match.");
    }

    @Test
    void shouldAddNewScore() {
        rankingService.addScore("Alice", 1200);

        List<RankingEntry> ranking = rankingService.loadRanking();

        assertEquals(1, ranking.size(), "A single ranking entry should be stored.");
        assertEquals("Alice", ranking.get(0).getPlayerName(), "Player name should be stored correctly.");
        assertEquals(1200, ranking.get(0).getScore(), "Score should be stored correctly.");
    }

    @Test
    void shouldPersistRankingAfterAddingScore() {
        rankingService.addScore("Alice", 1200);

        RankingService reloadedService = new RankingService(rankingFile);
        List<RankingEntry> ranking = reloadedService.loadRanking();

        assertEquals(1, ranking.size(), "The persisted ranking should remain available after reload.");
        assertEquals("Alice", ranking.get(0).getPlayerName(), "Player name should persist after reload.");
        assertEquals(1200, ranking.get(0).getScore(), "Score should persist after reload.");
    }

    @Test
    void shouldReturnRankingEntries() {
        rankingService.saveRanking(List.of(
                new RankingEntry("Alice", 1200, LocalDateTime.of(2026, 5, 30, 14, 30, 0)),
                new RankingEntry("Bob", 900, LocalDateTime.of(2026, 5, 30, 15, 0, 0))
        ));

        List<RankingEntry> ranking = rankingService.getRanking();

        assertEquals(2, ranking.size(), "All stored ranking entries should be returned.");
        assertEquals("Alice", ranking.get(0).getPlayerName(), "First player name should match.");
        assertEquals("Bob", ranking.get(1).getPlayerName(), "Second player name should match.");
    }

    @Test
    void shouldReturnTopScoresOrderedDescending() {
        rankingService.saveRanking(List.of(
                new RankingEntry("Alice", 500, LocalDateTime.of(2026, 5, 30, 14, 30, 0)),
                new RankingEntry("Bob", 1200, LocalDateTime.of(2026, 5, 30, 15, 0, 0)),
                new RankingEntry("Carol", 900, LocalDateTime.of(2026, 5, 30, 16, 0, 0))
        ));

        List<RankingEntry> topScores = rankingService.getTopScores(3);

        assertEquals(3, topScores.size(), "Three ranking entries should be returned.");
        assertEquals(1200, topScores.get(0).getScore(), "Highest score should be first.");
        assertEquals(900, topScores.get(1).getScore(), "Second highest score should be second.");
        assertEquals(500, topScores.get(2).getScore(), "Lowest score should be last.");
    }

    @Test
    void shouldLimitTopScoresResult() {
        rankingService.saveRanking(List.of(
                new RankingEntry("Alice", 1500, LocalDateTime.of(2026, 5, 30, 14, 0, 0)),
                new RankingEntry("Bob", 1400, LocalDateTime.of(2026, 5, 30, 15, 0, 0)),
                new RankingEntry("Carol", 1300, LocalDateTime.of(2026, 5, 30, 16, 0, 0)),
                new RankingEntry("Daniel", 1200, LocalDateTime.of(2026, 5, 30, 17, 0, 0))
        ));

        List<RankingEntry> topScores = rankingService.getTopScores(3);

        assertEquals(3, topScores.size(), "Top scores should be limited to three entries.");
    }

    @Test
    void shouldStorePlayerNameCorrectly() {
        rankingService.addScore("Cristian", 700);

        List<RankingEntry> ranking = rankingService.loadRanking();

        assertEquals("Cristian", ranking.get(0).getPlayerName(), "Player name should be persisted correctly.");
    }

    @Test
    void shouldStoreScoreCorrectly() {
        rankingService.addScore("Alice", 840);

        List<RankingEntry> ranking = rankingService.loadRanking();

        assertEquals(840, ranking.get(0).getScore(), "Score should be persisted correctly.");
    }

    @Test
    void shouldHandleInvalidRankingJsonGracefully() throws Exception {
        Files.writeString(rankingFile, "{ invalid json", StandardCharsets.UTF_8);

        List<RankingEntry> ranking = rankingService.loadRanking();

        assertNotNull(ranking, "Ranking should not be null when the file is invalid.");
        assertTrue(ranking.isEmpty(), "Ranking should be empty when the file content is invalid.");
    }
}
