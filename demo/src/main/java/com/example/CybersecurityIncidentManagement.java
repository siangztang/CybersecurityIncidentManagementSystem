package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * JavaFX App
 */
public class CybersecurityIncidentManagement extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Cybersecurity Incident Management");
        stage.centerOnScreen();
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }

}