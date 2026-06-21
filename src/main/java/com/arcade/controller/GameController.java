package com.arcade.controller;

import com.arcade.model.Question;
import com.arcade.service.GameService;
import com.arcade.service.RankingService;
import com.arcade.view.AnswerCardView;
import com.arcade.view.GameView;
import com.arcade.view.ResultView;
import javafx.scene.Scene;

import java.util.List;

/**
 * Controls the game screen and routes answer submissions to the backend.
 */
public class GameController {

    private final Scene scene;
    private final GameService gameService;
    private final RankingService rankingService;
    private RankingController rankingController;

    public GameController(Scene scene, GameService gameService, RankingService rankingService) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene must not be null.");
        }
        if (gameService == null) {
            throw new IllegalArgumentException("GameService must not be null.");
        }
        if (rankingService == null) {
            throw new IllegalArgumentException("RankingService must not be null.");
        }

        this.scene = scene;
        this.gameService = gameService;
        this.rankingService = rankingService;
    }

    public void setRankingController(RankingController rankingController) {
        this.rankingController = rankingController;
    }

    public void showGameView(String selectedCategory) {
        gameService.startGame(selectedCategory);
        renderCurrentGame();
    }

    public void showResultView() {
        showResultView(gameService.getScore());
    }

    public void showResultView(int finalScore) {
        ResultView view = new ResultView(finalScore);
        view.getSubmitButton().setOnAction(event ->
                submitPlayerScore(view.getPlayerNameField().getText(), finalScore));
        scene.setRoot(view.getRoot());
    }

    private void renderCurrentGame() {
        Question currentQuestion = gameService.getCurrentQuestion();
        if (currentQuestion == null) {
            showResultView(gameService.getScore());
            return;
        }

        GameView view = new GameView(
                formatQuestionText(currentQuestion),
                currentQuestion.getOptions(),
                formatLivesText(gameService.getRemainingLives()),
                formatScoreText(gameService.getScore())
        );

        List<AnswerCardView> answerCards = view.getAnswerCards();
        for (AnswerCardView answerCard : answerCards) {
            answerCard.getRoot().setOnAction(event -> handleAnswer(answerCard.getRoot().getText()));
        }

        scene.setRoot(view.getRoot());
    }

    private void handleAnswer(String selectedAnswer) {
        gameService.submitAnswer(selectedAnswer);

        if (gameService.isGameOver()) {
            showResultView(gameService.getScore());
            return;
        }

        renderCurrentGame();
    }

    private void submitPlayerScore(String playerName, int finalScore) {
        rankingService.addScore(playerName, finalScore);

        if (rankingController != null) {
            rankingController.showRankingView();
        }
    }

    private String formatQuestionText(Question question) {
        String questionText = question.getQuestionText() == null ? "" : question.getQuestionText().trim();
        String extraContent = question.getExtraContent() == null ? "" : question.getExtraContent().trim();

        if (extraContent.isEmpty()) {
            return questionText;
        }

        if (questionText.isEmpty()) {
            return extraContent;
        }

        return questionText + "\n\n" + extraContent;
    }

    private String formatLivesText(int remainingLives) {
        StringBuilder livesBuilder = new StringBuilder("Lives: ");
        for (int index = 0; index < Math.max(0, remainingLives); index++) {
            if (index > 0) {
                livesBuilder.append(' ');
            }
            livesBuilder.append('\u2665');
        }
        return livesBuilder.toString();
    }

    private String formatScoreText(int score) {
        return "Score: " + Math.max(0, score);
    }
}
