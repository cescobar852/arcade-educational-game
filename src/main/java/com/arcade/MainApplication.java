package com.arcade;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main entry point for the arcade educational game.
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {

        Label titleLabel = new Label("Arcade Educational Game");

        StackPane root = new StackPane();
        root.getChildren().add(titleLabel);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Arcade Educational Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}