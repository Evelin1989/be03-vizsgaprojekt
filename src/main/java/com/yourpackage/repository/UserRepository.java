package com.yourpackage.repository;

import com.yourpackage.database.DatabaseManager;

import com.yourpackage.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String DB_URL = "jdbc:sqlite:app.db";

    // résztvevők törlése az adatbázisból
    public void deleteAllEntries() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM users";
            statement.executeUpdate(deleteQuery);
        }
    }

    // egy résztvevő törlése az adatbázisból
    public void deleteEntry(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // résztvevők listájának lekérdezése az adatbázisból
    public List<User> getNamesFromDatabase() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement()) {
            String query = "SELECT id, firstname, lastname, dob, status FROM users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dob").toString(),
                        resultSet.getInt("status"));
                users.add(user);
            }
        }

        return users;
    }

    // résztvevő frissítése az adatbázisba
    public void updateUser(User user) throws SQLException {
        String updateQuery = "UPDATE users SET firstname = ?, lastname = ?, dob = ?, status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, user.getFirstName());
            updateStatement.setString(2, user.getLastName());
            updateStatement.setDate(3, Date.valueOf(user.getDateOfBirth())); // Convert to SQL Date
            updateStatement.setInt(4, user.getStatus());
            updateStatement.setInt(5, user.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // résztvevő lekérdezése az adatbázisból
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getDate("dob").toString(),
                    rs.getInt("status")
                );
            } else {
                return null;
            }
        }
    }
}
