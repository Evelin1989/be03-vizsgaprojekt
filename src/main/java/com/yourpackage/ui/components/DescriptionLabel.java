package com.yourpackage.ui.components;

import javafx.scene.control.Label;

// leírás mező komponens, leírás, ami felbontástól függően lehet hogy nem fog teljesen látszódni.
// README.md fájlban benne van.

public class DescriptionLabel {
    private Label descriptionLabel;

    public DescriptionLabel() {
        descriptionLabel = new Label();
        descriptionLabel.setStyle("-fx-font-size: 12px;");
        setText();
    }

    private void setText() {
        descriptionLabel.setText(
            "Leírás\n\n" +
            "1. 9 résztvevő txt fileból olvasva.\n" +
            "   - Opcionálisan hozzáadható még a formban.\n" +
            "   - Manuálisan szerkeszthető, eltávolítható minden résztvevő.\n" +
            "2. Felkészülési matek. 30 nap.\n" +
            "   - Minden résztvevőnek van 50% esélye hogy edz az adott napon vagy nem.\n" +
            "   - Ha edz, növeli a számlálót 25-el.\n" +
            "   - Ha nem edz, csökkenti a számlálót 25-el.\n" +
            "   - Ha egymás után 3 napig nem edz, akkor jobban csökkenti a számlálót. -50\n" +
            "   - Ha egymás után 3 napig edz, akkor jobban növeli a számlálót. +50\n" +
            "3. Generálja le a futók listáját felkészülés szerint.\n" +
            "4. Írja bele az adatbázisba.\n" +
            "5. Szimulálja a versenyt.\n" +
            "   Táv szerinti .6 .8 .9 negatív szorzók.\n" +
            "6. Írja ki az eredményeket.\n" +
            "6. Eredményekre dupla klikk a részletes napi edzés lebontásért.\n" +
            "7. Exportálja a kimenetet CSV-be."
        );
    }

    public Label getLabel() {
        return descriptionLabel;
    }
}
