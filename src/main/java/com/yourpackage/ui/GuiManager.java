package com.yourpackage.ui;

import com.yourpackage.logic.Logic;

import com.yourpackage.models.Event;
import com.yourpackage.models.User;
import com.yourpackage.models.FinalResult;

import com.yourpackage.database.DatabaseManager;

import com.yourpackage.repository.UserRepository;
import com.yourpackage.repository.EventRepository;
import com.yourpackage.repository.ResultsRepository;

import com.yourpackage.ui.components.ConfirmationDialog;
import com.yourpackage.ui.components.EditUserDialog;
import com.yourpackage.ui.components.WorkoutDetailsDialog;
import com.yourpackage.ui.components.ParticipantForm;
import com.yourpackage.ui.components.EventDropdown;
import com.yourpackage.ui.components.DescriptionLabel;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class GuiManager {
    private BorderPane root;
    private ParticipantForm participantForm;
    private EventDropdown eventDropdown;
    private DescriptionLabel descriptionLabel;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private ResultsRepository resultsRepository;
    private TableView<FinalResult> finalResultsTable;
    private Logic logic;

    public GuiManager() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        descriptionLabel = new DescriptionLabel();
        userRepository = new UserRepository();
        eventRepository = new EventRepository();
        resultsRepository = new ResultsRepository();
        logic = new Logic(userRepository, resultsRepository);

        participantForm = new ParticipantForm(this);
        eventDropdown = new EventDropdown(this);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Event getSelectedEvent() {
        return eventDropdown.getSelectedEvent();
    }

    // GUI kezdése, bal és jobb oldal létrehozása, adatok frissítése
    public void initializeGui() {
        initializeFinalResultsTable();

        VBox leftSide = createLeftSide();
        VBox rightSide = createRightSide();

        root.setLeft(leftSide);
        root.setRight(rightSide);

        refreshNames();
    }

    // GUI bal oldal
    private VBox createLeftSide() {
        VBox leftSide = new VBox(10);
        leftSide.setPadding(new Insets(10));

        VBox userListContainer = new VBox(10);

        // részvevő lista legyen görgethető
        ScrollPane scrollPane = new ScrollPane(userListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setId("userListScrollPane");

        // form, dropdown és gombok hozzáadása a bal oldahoz
        leftSide.getChildren().addAll(
            participantForm.getForm(),
            eventDropdown.getDropdown(),
            createButton("Számold ki ki nyer.", this::simulateRace, "primary-button"),
            createButton("Rendezés státusz szerint", this::orderUsers, "secondary-button"),
            createButton("Rendezés név szerint", this::orderUsersByName, "secondary-button"),
            createButton("Összes résztvevő törlése", this::deleteAllUsers, "danger-button"),
            scrollPane
        );
        return leftSide;
    }

    // GUI jobb oldal
    private VBox createRightSide() {
        VBox rightSide = new VBox(10);
        rightSide.setPadding(new Insets(10));

        // tábla és gombok hozzáadása a jobb oldalhoz
        rightSide.getChildren().addAll(
            descriptionLabel.getLabel(),
            finalResultsTable,
            createButton("Tábla törlése", this::resetFinalResults, "danger-button"),
            createButton("Exportálás CSV-be", this::exportToCSV, "secondary-button")
        );

        descriptionLabel.getLabel().getStyleClass().add("description-label");

        return rightSide;
    }

    // metódus gombok létrehozásására
    private Button createButton(String text, Runnable action, String styleClass) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        button.getStyleClass().add(styleClass);
        return button;
    }

    // törli az összes résztvevőt
    private void deleteAllUsers() {
        try {
            boolean confirmed = ConfirmationDialog.showConfirmation(
                "Biztos törlöd? Akkor újra kell indítani a programot. Vagy kézzel újra felvinni minden résztvevőt.",
                "Biztos törlöd az összes résztvevőt?"
            );
            if (confirmed) {
                userRepository.deleteAllEntries();
                refreshNames();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // szimuláció futtatása
    private void simulateRace() {
        try {
            Event selectedEvent = getSelectedEvent();
            // ellenőrzi hogy válaszott-e eseményt a felhasználó
            if (selectedEvent == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Válassz eseményt, mielőtt számolunk!");
                alert.showAndWait();
                return;
            }

            ObservableList<FinalResult> results = logic.simulateRace(selectedEvent);
            finalResultsTable.setItems(results);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }

    // törli az összes végeredményt
    private void resetFinalResults() {
        try {
            finalResultsTable.getItems().clear();
            resultsRepository.clearFinalResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CSV exportálása
    private void exportToCSV() {
        try {
            List<FinalResult> finalResults = resultsRepository.getFinalResults();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            Stage stage = (Stage) root.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                Logic.exportToCSV(finalResults, file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "CSV exportálása sikeres.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // rendezés státusz szerint
    private void orderUsers() {
        try {
            List<User> users = userRepository.getNamesFromDatabase();
            users = Logic.sortUsersByStatus(users);
            refreshNames(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // rendezés név szerint
    private void orderUsersByName() {
        try {
            List<User> users = userRepository.getNamesFromDatabase();
            users = Logic.sortUsersByName(users);
            refreshNames(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // résztvevő lista frissítése
    public void refreshNames() {
        try {
            List<User> users = userRepository.getNamesFromDatabase();
            refreshNames(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // résztvevő lista frissítése
    public void refreshNames(List<User> users) {
        VBox leftSide = (VBox) root.getLeft();

        ScrollPane scrollPane = (ScrollPane) leftSide.getChildren().get(leftSide.getChildren().size() - 1);

        VBox userListContainer = (VBox) scrollPane.getContent();

        userListContainer.setPrefHeight(600);
        userListContainer.setPrefWidth(600);

        userListContainer.getChildren().clear();

        userListContainer.getStyleClass().add("user-list-container");

        int index = 1;
        for (User user : users) {
            HBox userBox = createUserBox(user, index++);
            userListContainer.getChildren().add(userBox);
        }
    }

    // résztvevő lista elem -> egy résztvevő
    private HBox createUserBox(User user, int number) {
        HBox userBox = new HBox(10);

        Label numberLabel = new Label(String.valueOf(number + "."));

        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName() + " | " + user.getDateOfBirth() + " | " + user.getStatus());

        Button deleteButton = createButton("Törlés", () -> deleteUser(user), "danger-button");
        Button editButton = createButton("Szerkesztés", () -> editUser(user), "secondary-button");

        userBox.getChildren().addAll(numberLabel, nameLabel, editButton, deleteButton);
        userBox.getStyleClass().add("user-box");

        return userBox;
    }

    // résztvevő törlése
    private void deleteUser(User user) {
        try {
            boolean confirmed = ConfirmationDialog.showConfirmation("Biztos törlöd?", "Biztos törlöd ezt a résztvevőt?");
            if (confirmed) {
                userRepository.deleteEntry(user.getId());
                refreshNames();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // résztvevő szerkesztése
    private void editUser(User user) {
        try {
            EditUserDialog editDialog = new EditUserDialog(user);
            if (editDialog.showAndWait()) {
                userRepository.updateUser(user);
                refreshNames();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // végeredmény tábla létrehozása
    private void initializeFinalResultsTable() {
        finalResultsTable = new TableView<>();
        finalResultsTable.setPrefHeight(500);
        finalResultsTable.setPrefWidth(600);

        TableColumn<FinalResult, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<FinalResult, Integer> eventIdColumn = new TableColumn<>("Esemény ID");
        eventIdColumn.setCellValueFactory(cellData -> cellData.getValue().eventIdProperty().asObject());

        TableColumn<FinalResult, String> eventNameColumn = new TableColumn<>("Esemény Típusa");
        eventNameColumn.setCellValueFactory(cellData -> cellData.getValue().eventNameProperty());

        TableColumn<FinalResult, Integer> userIdColumn = new TableColumn<>("Résztvevő ID");
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());

        TableColumn<FinalResult, String> userNameColumn = new TableColumn<>("Résztvevő Neve");
        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());

        TableColumn<FinalResult, Integer> positionColumn = new TableColumn<>("Pozíció");
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty().asObject());

        TableColumn<FinalResult, Integer> statusColumn = new TableColumn<>("Teljesítmény"); 
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty().asObject());

        finalResultsTable.getColumns().addAll(idColumn, eventIdColumn, eventNameColumn, userIdColumn, userNameColumn, positionColumn, statusColumn);

        // eventListener hozzáadása a sorokra
        // dupla kattintás esetén megjeleníti a hónap részletezést
        finalResultsTable.setRowFactory(tv -> {
            TableRow<FinalResult> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    FinalResult rowData = row.getItem();
                    try {
                        User user = userRepository.getUserById(rowData.getUserId());
                        if (user != null) {
                            showWorkoutDetails(user);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    // hónap részletezés megjelenítése
    private void showWorkoutDetails(User user) {
        WorkoutDetailsDialog dialog = new WorkoutDetailsDialog(user);
        dialog.showAndWait();
    }
}