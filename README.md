### Vizsgafeladat README.

### Leírás:

Vizsgafeladatomnak egy valósabb alkalmazást készítettem, jelen formájában egy futóversenyt szimuláló CRUD-alkalmazást, több adatbázis használatával és JavaFX-el.

Ez egy - akár a mindennapi életben is használható alkalmazás lehetne -, pl. iskolákban, hivatalokban, és a többi.

A lényeg, hogy szerettem volna egy felhasználói felületet készíteni, amivel egyszereűen lehet adatokat bekérni, azokat validálni, hogy megfelelő formátumban vannak e, rendezni őket, szerkeszteni, törölni és exportálni.

1. Az app indulásnál, fileból beolvassa a résztvevőket, és beleírja az adatbázisba őket. /resources/participants.txt
2. A felhasználó adhat hozzá részvevőt, szerkesztheti a résztvevőket, és törölheti is őket az adatbázisból.
3. A felhasználó rendezheti a résztvevőket név és státusz szerint.
4. A felhasználó választhat egy eseményt, és futóversenyt szimulálhat.
    - LOGIC.java: A felkészülés 30 napos.
    - Minden résztvevőnek van 50% esélye hogy edz az adott napon vagy nem.
    - Ha edz, növeli a számlálót 25-el.
    - Ha nem edz, csökkenti a számlálót 25-el.
    - Ha egymás után 3 napig nem edz, akkor jobban csökkenti a számlálót. -50
    - Ha egymás után 3 napig edz, akkor jobban növeli a számlálót. +50
5. Szimulálja a versenyt.
6. Írja bele az adatbázisba.
7. Írja ki az eredményeket.
8. Eredményekre dupla klikk a részletes napi edzés lebontásért.
9. Exportálja a kimenetet CSV-be.

### Dependencies, egyéb toolok:

* [Java 21 LTS](https://www.java.com/)
* [Maven 3.9.8](https://maven.apache.org/)
* [SQLite 3+](https://www.sqlite.org/index.html)
* [PlantUML VSCode extension](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)

### Két indító parancs: 
- Egyes parancsok lehet hogy nem működnek a powershell-ben, csak a cmd-ben.

`mvn clean install`

`mvn exec:java -Dexec.mainClass="com.yourpackage.App"`

### UML diagram alapok az UML.md fileban.
- PlantUML VSCODE extension kell hozzá.
- ALT + D indítás.

![UML diagram](/uml.jpg)

### Hiányzik:
1. Részletes Unit testek.
2. Több refaktorálás.
3. Részletes eredmények lekérdezése valós adatokkal.
4. Egyéb hibák javítása.