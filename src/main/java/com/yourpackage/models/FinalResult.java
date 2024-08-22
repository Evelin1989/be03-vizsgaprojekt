package com.yourpackage.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class FinalResult {
    private final IntegerProperty id;
    private final IntegerProperty eventId;
    private final StringProperty eventName;
    private final IntegerProperty userId;
    private final StringProperty userName;
    private final IntegerProperty position;
    private final IntegerProperty status;

    public FinalResult(int id, int eventId, String eventName, int userId, String userName, int position, int status) {
        this.id = new SimpleIntegerProperty(id);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.eventName = new SimpleStringProperty(eventName);
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
        this.position = new SimpleIntegerProperty(position);
        this.status = new SimpleIntegerProperty(status);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getEventId() { return eventId.get(); }
    public IntegerProperty eventIdProperty() { return eventId; }

    public String getEventName() { return eventName.get(); }
    public StringProperty eventNameProperty() { return eventName; }

    public int getUserId() { return userId.get(); }
    public IntegerProperty userIdProperty() { return userId; }

    public String getUserName() { return userName.get(); }
    public StringProperty userNameProperty() { return userName; }

    public int getPosition() { return position.get(); }
    public IntegerProperty positionProperty() { return position; }

    public int getStatus() { return status.get(); }
    public IntegerProperty statusProperty() { return status; }
}
