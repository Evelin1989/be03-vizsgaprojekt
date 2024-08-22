package com.yourpackage.logic;

import com.yourpackage.models.Event;
import com.yourpackage.models.User;
import com.yourpackage.models.FinalResult;
import com.yourpackage.repository.UserRepository;
import com.yourpackage.repository.ResultsRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

public class Logic {

    private UserRepository userRepository;
    private ResultsRepository resultsRepository;

    public Logic(UserRepository userRepository, ResultsRepository resultsRepository) {
        this.userRepository = userRepository;
        this.resultsRepository = resultsRepository;
    }

    /**
     * Szimulálja a versenyt a megadott eseményre.
     *
     * @param selectedEvent A választott esemény.
     * @return Egy observable lista eredményekhez.
     * @throws SQLException Ha hiba történik az adatbázis elérésekor.
     */
    public ObservableList<FinalResult> simulateRace(Event selectedEvent) throws SQLException {
        if (selectedEvent == null) {
            throw new IllegalArgumentException("Válassz eseményt, mielőtt számolunk!");
        }
    
        List<User> users = userRepository.getNamesFromDatabase();
        // alapból a résztvevők rendezése a status szerint
        sortUsersByStatus(users);
    
        int eventId = selectedEvent.getId();
        String eventName = selectedEvent.getName();
        Map<Integer, Integer> finalResults = new HashMap<>();
        Random random = new Random();
        
        // táv szerinti negatív szorzók számítása
        double distanceMultiplier = getDistanceMultiplier(selectedEvent.getDistance());
        System.out.println("Distance Multiplier for event '" + eventName + "': " + distanceMultiplier);
    
        for (User user : users) {
            int status = user.getStatus();
            int consecutiveWorkouts = 0;
            int consecutiveMisses = 0;
    
            System.out.println("Simulating for user: " + user.getFirstName() + " " + user.getLastName());
    
            for (int day = 1; day <= 30; day++) {
                boolean worksOut = random.nextBoolean();
                System.out.println("Day " + day + ": Works out? " + worksOut);
    
                if (worksOut) {
                    consecutiveWorkouts++;
                    consecutiveMisses = 0;
                    if (consecutiveWorkouts >= 3) {
                        status += 50;
                    } else {
                        status += 25;
                    }
                } else {
                    consecutiveWorkouts = 0;
                    consecutiveMisses++;
                    if (consecutiveMisses >= 3) {
                        status -= 50;
                    } else {
                        status -= 25;
                    }
                }
    
                // esetleg ha nem akarjuk hogy 0 alá menjen
                // if (status < 0) {
                //     status = 0;
                // }
    
                System.out.println("Day " + day + ": Status = " + status);
            }
    
            status = (int) (status * distanceMultiplier);
            System.out.println("Final Status after applying multiplier: " + status);
    
            finalResults.put(user.getId(), status);
            resultsRepository.insertFinalResult(eventId, user.getId(), status);
        }
    
        // konvertálja a mapot egy listára
        List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(finalResults.entrySet());
    
        // csökkenő sorrendben rendezés
        sortedEntries.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));
    
        ObservableList<FinalResult> resultList = FXCollections.observableArrayList();
        int position = 1;
    
        for (Map.Entry<Integer, Integer> entry : sortedEntries) {
            Integer userId = entry.getKey();
            Integer finalStatus = entry.getValue();
    
            User user = userRepository.getUserById(userId);
            if (user != null) {
                FinalResult finalResult = new FinalResult(
                    0,
                    eventId,
                    eventName,
                    userId,
                    user.getFirstName() + " " + user.getLastName(),
                    position++,
                    finalStatus
                );
                resultList.add(finalResult);
            }
        }
    
        return resultList;
    }

    private double getDistanceMultiplier(String distance) {
        String normalizedDistance = distance != null ? distance.trim().toLowerCase() : "";
        
        System.out.println("Normalized Distance: '" + normalizedDistance + "'");
    
        switch (normalizedDistance) {
            case "42km":
                return 0.6;
            case "21km":
                return 0.8;
            case "10km":
                return 0.9;
            default:
                System.out.println("Unknown distance. Using default multiplier.");
                return 1.0;
        }
    }

    /**
     * Rendezze a résztvevőket a status szerint csökkenő sorrendben.
     *
     * @param users részvevők listája, amit rendezni akarunk.
     * @return a rendezett lista.
     */
    public static List<User> sortUsersByStatus(List<User> users) {
        users.sort((u1, u2) -> Integer.compare(u2.getStatus(), u1.getStatus()));
        return users;
    }

    /**
     * Rendezze a résztvevőket név szerint növekvő sorrendben.
     *
     * @param users Résztvevők listája, amit rendezni akarunk.
     * @return a rendezett lista.
     */
    public static List<User> sortUsersByName(List<User> users) {
        users.sort((u1, u2) -> u1.getFirstName().compareTo(u2.getFirstName()));
        return users;
    }

    /**
     * Exportálja a eredményeket CSV fájlba.
     *
     * @param finalResults Végeredmények listája, amit exportálni akarunk.
     * @param filePath     A CSV fájl elérési útja.
     */
    public static void exportToCSV(List<FinalResult> finalResults, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            writer.append("Id,Event Id,Event Name,User Id,User Name,Position,Status\n");

            for (FinalResult result : finalResults) {
                writer.append(String.format("%d,%d,%s,%d,%s,%d,%d\n",
                        result.getId(),
                        result.getEventId(),
                        result.getEventName(),
                        result.getUserId(),
                        result.getUserName(),
                        result.getPosition(),
                        result.getStatus()));
            }

            System.out.println("CSV exportálása sikeres.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
