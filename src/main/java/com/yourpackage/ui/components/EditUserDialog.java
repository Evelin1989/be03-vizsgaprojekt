package com.yourpackage.ui.components;

import com.yourpackage.models.User;
import com.yourpackage.database.DatabaseManager;
import com.yourpackage.repository.UserRepository;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

// résztvevő szerkesztés felugró ablak

public class EditUserDialog {

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField dateOfBirthField;
    private TextField statusField;

    private User user;
    private boolean isConfirmed = false;

    public EditUserDialog(User user) {
        this.user = user;
    }

    public boolean showAndWait() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Résztvevő szerkesztése");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label firstNameLabel = new Label("Keresztnév:");
        grid.add(firstNameLabel, 0, 0);
        firstNameField = new TextField(user.getFirstName());
        grid.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Vezetéknév:");
        grid.add(lastNameLabel, 0, 1);
        lastNameField = new TextField(user.getLastName());
        grid.add(lastNameField, 1, 1);

        Label dateOfBirthLabel = new Label("Születési dátum:");
        grid.add(dateOfBirthLabel, 0, 2);
        dateOfBirthField = new TextField(user.getDateOfBirth());
        grid.add(dateOfBirthField, 1, 2);

        Label statusLabel = new Label("Státusz:");
        grid.add(statusLabel, 0, 3);
        statusField = new TextField(String.valueOf(user.getStatus()));
        grid.add(statusField, 1, 3);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());
            user.setDateOfBirth(dateOfBirthField.getText());
            user.setStatus(Integer.parseInt(statusField.getText()));
            isConfirmed = true;
            dialogStage.close();
        });
        grid.add(okButton, 0, 4);

        Button cancelButton = new Button("Mégse");
        cancelButton.setOnAction(e -> dialogStage.close());
        grid.add(cancelButton, 1, 4);

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return isConfirmed;
    }
}
