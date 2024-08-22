package com.yourpackage.ui.components;

import com.yourpackage.database.DatabaseManager;
import com.yourpackage.models.User;

import com.yourpackage.ui.GuiManager;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.sql.SQLException;

// résztvevő űrlap, form, résztvevő hozzáadása és az adatok helyességének validálása
// ellenőrzi a születési dátum formátumát YYYY-MM-DD
// ellenőrzi a státusz értékét 0 és 100 között
// ellenőrzi hogy a státusz szám-e
// ellenőrzi hogy minden ki van e töltve.
// hiba esetén a felugró ablakban megjelenik a hibaüzenet

public class ParticipantForm {
    private GridPane formGrid;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField dobField;
    private TextField statusField;
    private Button addButton;
    private GuiManager guiManager;

    public ParticipantForm(GuiManager guiManager) {
        this.guiManager = guiManager;
        initializeForm();
    }

    private void initializeForm() {
        formGrid = new GridPane();
        formGrid.setPadding(new Insets(10));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        formGrid.getStyleClass().add("grid-pane");

        firstNameField = new TextField();
        lastNameField = new TextField();
        dobField = new TextField();
        statusField = new TextField();
        addButton = new Button("Résztvevő hozzáadása");

        addButton.setOnAction(event -> addParticipant());

        formGrid.addRow(0, new Label("Keresztnév:"), firstNameField);
        firstNameField.getStyleClass().add("text-field");

        formGrid.addRow(1, new Label("Vezetéknév:"), lastNameField);
        
        formGrid.addRow(2, new Label("Születési dátum:"), dobField);
        
        formGrid.addRow(3, new Label("Teljesítmény:"), statusField);
        
        formGrid.add(addButton, 1, 4);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label");
        return label;
    }

    private void addParticipant() {
    try {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dob = dobField.getText().trim();
        String statusText = statusField.getText().trim();

        // ellenőrzi, hogy minden mező kitöltött-e
        if (!firstName.isEmpty() && !lastName.isEmpty() && !dob.isEmpty() && !statusText.isEmpty()) {

            // ellenőrzi a születési dátum formátumát
            if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                showAlert("Hiba", "A születési dátum formátuma nem megfelelő! YYYY-MM-DD formátum használata!");
                return;
            }

            try {
                int status = Integer.parseInt(statusText);

                // ellenőrzi a státusz értékét
                if (status < 0 || status > 100) {
                    showAlert("Hiba", "A státusz értékének 0 és 100 között kell lennie!");
                    return;
                }

                DatabaseManager.insertParticipant(firstName, lastName, dob, status);

                // beillesztés után törli a mezőket
                firstNameField.clear();
                lastNameField.clear();
                dobField.clear();
                statusField.clear();

                // firssíti a listát
                guiManager.refreshNames();

            } catch (NumberFormatException e) {
                showAlert("Hiba", "A státusz értékének számnak kell lennie!");
            }
        } else {
            showAlert("Hiba", "Tölts ki minden mezőt, és próbáld újra!");
        }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // felugró ablak megjelenítése
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        dobField.clear();
        statusField.clear();
    }

    public GridPane getForm() {
        return formGrid;
    }
}
