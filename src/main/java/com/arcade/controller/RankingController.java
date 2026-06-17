package com.arcade.controller;

import com.arcade.model.RankingEntry;
import com.arcade.service.RankingService;
import com.arcade.view.RankingView;
import javafx.scene.Scene;

import java.util.List;

/**
 * Controls the ranking screen and the return to the main menu.
 */
public class RankingController {

    private final Scene scene;
    private final RankingService rankingService;
    private MenuController menuController;

    public RankingController(Scene scene, RankingService rankingService) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene must not be null.");
        }
        if (rankingService == null) {
            throw new IllegalArgumentException("RankingService must not be null.");
        }

        this.scene = scene;
        this.rankingService = rankingService;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public void showRankingView() {
        List<RankingEntry> rankingEntries = rankingService.getRanking();
        RankingView view = new RankingView(rankingEntries);
        view.getMainMenuButton().setOnAction(event -> returnToMenu());
        scene.setRoot(view.getRoot());
    }

    public void returnToMenu() {
        if (menuController != null) {
            menuController.start();
        }
    }
}
