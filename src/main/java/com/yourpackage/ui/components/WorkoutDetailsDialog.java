package com.yourpackage.ui.components;

import com.yourpackage.models.User;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;
import javafx.geometry.Insets;

// hónap részletezés felugró ablak, béta változat
public class WorkoutDetailsDialog extends Stage {
    public WorkoutDetailsDialog(User user) {
        setTitle("Hónap részletezése: " + user.getFirstName() + " " + user.getLastName());
        initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label workoutDetailsLabel = new Label(getWorkoutDetails(user));
        layout.getChildren().add(workoutDetailsLabel);

        Scene scene = new Scene(layout, 300, 600);
        setScene(scene);
    }

    // felkészülési hónap részletezése, napi bontásban
    private String getWorkoutDetails(User user) {
        StringBuilder workoutDetails = new StringBuilder();
        Random random = new Random();
        int status = user.getStatus();
        int consecutiveWorkouts = 0;
        int consecutiveMisses = 0;
    
        workoutDetails.append("Hónap részletezése: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append(":\n");
    
        for (int day = 1; day <= 30; day++) {
            boolean worksOut = random.nextBoolean();
    
            if (worksOut) {
                consecutiveWorkouts++;
                consecutiveMisses = 0;
                if (consecutiveWorkouts >= 3) {
                    status += 50;
                } else {
                    status += 25;
                }
                workoutDetails.append("- Nap ").append(day).append(": Edzett +25 (").append(status).append(")\n");
            } else {
                consecutiveWorkouts = 0;
                consecutiveMisses++;
                if (consecutiveMisses >= 3) {
                    status -= 50;
                } else {
                    status -= 25;
                }
                workoutDetails.append("- Nap ").append(day).append(": Pihent -25 (").append(status).append(")\n");
            }
    
        }
    
        return workoutDetails.toString();
    }
}
