package com.example.javafxwithjdbc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
            Parent parent = fxmlLoader.load();

            Scene mainScene = new Scene(parent);
            primaryStage.setTitle("Sample JavaFX application");
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}