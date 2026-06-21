package com.arcade;

import com.arcade.controller.CategoryController;
import com.arcade.controller.GameController;
import com.arcade.controller.MenuController;
import com.arcade.controller.RankingController;
import com.arcade.service.AudioService;
import com.arcade.service.GameService;
import com.arcade.service.QuestionService;
import com.arcade.service.RankingService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX entry point for the arcade educational game.
 */
public class MainApplication extends Application {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private AudioService audioService;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(), WINDOW_WIDTH, WINDOW_HEIGHT);

        audioService = new AudioService();
        audioService.loadPlaylist();

        QuestionService questionService = new QuestionService();
        GameService gameService = new GameService(questionService);
        RankingService rankingService = new RankingService();

        MenuController menuController = new MenuController(scene);
        CategoryController categoryController = new CategoryController(scene, questionService);
        GameController gameController = new GameController(scene, gameService, rankingService);
        RankingController rankingController = new RankingController(scene, rankingService);

        menuController.setCategoryController(categoryController);
        menuController.setExitAction(stage::close);
        categoryController.setGameController(gameController);
        gameController.setRankingController(rankingController);
        rankingController.setMenuController(menuController);

        stage.setTitle("Arcade Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMaxWidth(WINDOW_WIDTH);
        stage.setMaxHeight(WINDOW_HEIGHT);

        menuController.start();
        stage.show();
        audioService.play();
    }

    @Override
    public void stop() {
        if (audioService != null) {
            audioService.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
