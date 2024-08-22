package com.yourpackage;

import com.yourpackage.database.DatabaseManager;
import com.yourpackage.ui.GuiManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class App extends Application {

    public static void main(String[] args) {
        // adatbázis létrehozása és adatok beillesztése
        DatabaseManager.initializeDatabase();
        launch(args);
    }

    // GUI start metódus
    @Override
    public void start(Stage primaryStage) {
        GuiManager guiManager = new GuiManager();
        guiManager.initializeGui();

        // GUI megjelenítése 1024x768 pixelben
        Scene scene = new Scene(guiManager.getRoot(), 1024, 768);

        // CSS file pár egyéni dizájnhoz
        String css = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Futóverseny - Evelin Énekes.");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
