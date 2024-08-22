package com.yourpackage.repository;

import com.yourpackage.models.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private static final String DB_URL = "jdbc:sqlite:app.db";

    // események beillesztése az adatbázisba
    public void insertEvent(Connection connection, String name, String distance, int temperature) throws SQLException {
        String insertQuery = "INSERT INTO events (name, distance, temperature) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, distance);
            preparedStatement.setInt(3, temperature);
            preparedStatement.executeUpdate();
        }
    }

    // események olvasása az adatbázisból
    public List<Event> getEventsFromDatabase() throws SQLException {
        List<Event> events = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            String query = "SELECT id, name, distance, temperature FROM events";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("distance"),
                        resultSet.getInt("temperature"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }
}
