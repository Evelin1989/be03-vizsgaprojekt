package com.yourpackage.repository;

import com.yourpackage.models.Event;
import com.yourpackage.models.User;
import com.yourpackage.models.FinalResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultsRepository {
    private static final String DB_URL = "jdbc:sqlite:app.db";

    public ResultsRepository() {
        createFinalResultsTable();
    }

    // tábla létrehozása a végeredményekhez
    private void createFinalResultsTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS final_results ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_id INTEGER, "
                + "event_name TEXT, "
                + "user_id INTEGER, "
                + "user_name TEXT, "
                + "position INTEGER, "
                + "status INTEGER"
                + ");";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // végeredmények beillesztése az adatbázisba
    public void insertFinalResult(int eventId, int userId, int position) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String eventNameQuery = "SELECT name FROM events WHERE id = ?";
            String userQuery = "SELECT firstname || ' ' || lastname AS name, status FROM users WHERE id = ?";

            String eventName;
            String userName;
            int status;

            // esemény nevet lekérdezzük
            try (PreparedStatement eventStatement = connection.prepareStatement(eventNameQuery)) {
                eventStatement.setInt(1, eventId);
                ResultSet eventResultSet = eventStatement.executeQuery();
                eventName = eventResultSet.getString("name");
            }

            // résztvevő nevet és státuszt lekérdezzük
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setInt(1, userId);
                ResultSet userResultSet = userStatement.executeQuery();
                userName = userResultSet.getString("name");
                status = userResultSet.getInt("status");
            }

            // végeredményt beszúrítjuk
            String insertQuery = "INSERT INTO final_results (event_id, event_name, user_id, user_name, position, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, eventId);
                insertStatement.setString(2, eventName);
                insertStatement.setInt(3, userId);
                insertStatement.setString(4, userName);
                insertStatement.setInt(5, position);
                insertStatement.setInt(6, status);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // végeredmények frissítése az adatbázisba
    public void updateFinalResult(int eventId, int userId) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String eventNameQuery = "SELECT name FROM events WHERE id = ?";
            String userQuery = "SELECT firstname || ' ' || lastname AS name, status FROM users WHERE id = ?";
    
            String eventName;
            String userName;
            int status;
    
            // frissített esemény nevet lekérdezzük
            try (PreparedStatement eventStatement = connection.prepareStatement(eventNameQuery)) {
                eventStatement.setInt(1, eventId);
                ResultSet eventResultSet = eventStatement.executeQuery();
                eventName = eventResultSet.getString("name");
            }
    
            // frissített résztvevő nevet és státuszt lekérdezzük
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setInt(1, userId);
                ResultSet userResultSet = userStatement.executeQuery();
                userName = userResultSet.getString("name");
                status = userResultSet.getInt("status");
            }
    
            // frissítés végeredményhez
            String updateQuery = "UPDATE final_results SET event_name = ?, user_name = ?, status = ? WHERE event_id = ? AND user_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, eventName);
                updateStatement.setString(2, userName);
                updateStatement.setInt(3, status);
                updateStatement.setInt(4, eventId);
                updateStatement.setInt(5, userId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // végeredmények lekérdezése az adatbázisból
    public List<FinalResult> getFinalResults() throws SQLException {
        List<FinalResult> finalResults = new ArrayList<>();
        String query = "SELECT id, event_id, event_name, user_id, user_name, position, status FROM final_results";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                FinalResult finalResult = new FinalResult(
                    resultSet.getInt("id"),
                    resultSet.getInt("event_id"),
                    resultSet.getString("event_name"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getInt("position"),
                    resultSet.getInt("status")
                );
                finalResults.add(finalResult);
            }
        }
        return finalResults;
    }

    // végeredmények törlése az adatbázisból
    public void clearFinalResults() throws SQLException {
        String deleteQuery = "DELETE FROM final_results";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}