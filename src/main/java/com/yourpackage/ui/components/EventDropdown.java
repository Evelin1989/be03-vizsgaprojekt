package com.yourpackage.ui.components;

import com.yourpackage.repository.EventRepository;

import com.yourpackage.ui.GuiManager;

import com.yourpackage.models.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;

// esemény kiválasztó dropdown menü

public class EventDropdown {
    private ComboBox<Event> eventComboBox;
    private GuiManager guiManager;
    private EventRepository eventRepository;

    public EventDropdown(GuiManager guiManager) {
        this.guiManager = guiManager;
        this.eventRepository = new EventRepository();
        eventComboBox = new ComboBox<>();
        initializeDropdown();
        eventComboBox.getStyleClass().add("combo-box");
    }

    public Event getSelectedEvent() {
        return eventComboBox.getValue();
    }

    private void initializeDropdown() {
        try {
            List<Event> events = eventRepository.getEventsFromDatabase();
            eventComboBox.setItems(FXCollections.observableArrayList(events));
            eventComboBox.setPromptText("Válasszon eseményt");

            eventComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Event item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            });

            eventComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Event item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ComboBox<Event> getDropdown() {
        return eventComboBox;
    }
}
