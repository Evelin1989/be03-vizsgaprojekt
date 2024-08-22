package com.yourpackage.database;

import com.yourpackage.models.Event;
import com.yourpackage.models.User;
import com.yourpackage.repository.EventRepository;
import com.yourpackage.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:app.db";
    private static EventRepository eventRepository = new EventRepository();
    private static UserRepository userRepository = new UserRepository();
    private static Connection connection;
    
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            
            // táblák létrehozása
            initializeUsersTable(connection);

            initializeEventsTable(connection);

            insertInitialData(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tábla létrehozása az eseményekhez
    public static void initializeEventsTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String createEventTableQuery = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "distance TEXT," +
                    "temperature INTEGER)";
            statement.executeUpdate(createEventTableQuery);

            // üres-e a tábla?
            String checkQuery = "SELECT COUNT(*) AS count FROM events";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            if (resultSet.next() && resultSet.getInt("count") == 0) {
                // ha üres, akkor adatok beillesztése
                eventRepository.insertEvent(connection, "Marathon", "42km", 15);
                eventRepository.insertEvent(connection, "Half-Marathon", "21km", 20);
                eventRepository.insertEvent(connection, "10K", "10km", 25);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tábla létrehozása a résztvevőkhöz
    private static void initializeUsersTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstname TEXT," +
                "lastname TEXT," +
                "dob DATE," +
                "status INTEGER)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }

    // tábla létrehozása az eredményekhez
    private static void initializeFinalResultsTable(Connection connection) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS final_results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "event_id INTEGER," +
                "event_name TEXT," + 
                "user_id INTEGER," +
                "user_name TEXT," +  
                "position INTEGER," +
                "status INTEGER," + 
                "FOREIGN KEY (event_id) REFERENCES events(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // metódus a participants.txt fileból beolvasva az adatokat az adatbázisba
    private static void insertInitialData(Connection connection) {
        try (InputStream inputStream = DatabaseManager.class.getResourceAsStream("/participants.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String dob = parts[2].trim();
                    int status = Integer.parseInt(parts[3].trim());

                    // benne van-e már a résztvevő?
                    if (!recordExists(connection, firstName, lastName, dob, status)) {
                        // csak akkor tegye bele az adatot, ha nem létezik
                        String insertQuery = "INSERT INTO users (firstname, lastname, dob, status) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setString(1, firstName);
                            preparedStatement.setString(2, lastName);
                            preparedStatement.setDate(3, Date.valueOf(dob));
                            preparedStatement.setInt(4, status);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // metódus megnézi létezik-e a résztvevő
    private static boolean recordExists(Connection connection, String firstName, String lastName, String dob, int status) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM users WHERE firstname = ? AND lastname = ? AND dob = ? AND status = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate(3, Date.valueOf(dob));
            preparedStatement.setInt(4, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                // ha count > 0, akkor a résztvevő létezik
                return count > 0;
            }
        }
        return false;
    }

    // metódus ami hozzáad egy új résztvevőt az adatbázisoz
    public static void insertParticipant(String firstName, String lastName, String dob, int status) throws SQLException {
        String insertQuery = "INSERT INTO users (firstname, lastname, dob, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate(3, Date.valueOf(dob));
            preparedStatement.setInt(4, status);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
