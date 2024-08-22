# UML Diagram for the Running App Project

```plantuml
@startuml

class App {
    +main(DatabaseManager)
    +start(guiManager)
}

class GuiManager {
    +initializeFinalResultsTable()
    +fetchWorkoutDetailsForUser()
}

class ConfirmationDialog {
    +showConfirmation(String title, String content): boolean
}

class EditUserDialog {
    +showAndWait(): boolean
}

class DescriptionLabel {
    +getLabel(): Label
}

class EventDropdown {
    +Verseny adatok legördülő menü.
    +getDropdown(): ComboBox<Event>
}

class ParticipantForm {
    +getForm(): GridPane
}

class WorkoutDetailsDialog {
    +showAndWait(): void
}

class DatabaseManager {
    +getConnection()
    +initializeDatabase()
    +insertInitialData()
    +recordExists()
    +insertParticipant()
}

class Logic {
    +simulateRace(Event selectedEvent)
    +saveWorkoutDetailsToDatabase(int userId, int eventId, List<Boolean> workoutDetails)
    +getDistanceMultiplier(String distance)
    +sortUsersByStatus(List<User> users)
    +sortUsersByName(List<User> users)
    +exportToCSV(List<FinalResult> finalResults, String filePath)
}

class UserRepository {
    +getNamesFromDatabase(): List<User>
    +getUserById(int userId): User
}

class ResultsRepository {
    +insertFinalResult(int eventId, int userId, int status)
}

class EventRepository {
    +getEventsFromDatabase(): List<Event>
}

class Components {
    +UI / Kliens oldali komponensek.
}

class User {
    -int id
    -String firstName
    -String lastName
    -int status
    +getId()
    +getFirstName()
    +getLastName()
    +getStatus()
}

class Event {
    -int id
    -String name
    -String distance
    +getId()
    +getName()
    +getDistance()
}

class FinalResult {
    -int id
    -int eventId
    -String eventName
    -int userId
    -String userName
    -int position
    -int status
    +getId()
    +getEventId()
    +getEventName()
    +getUserId()
    +getUserName()
    +getPosition()
    +getStatus()
}

App --> GuiManager
App --> DatabaseManager
DatabaseManager --> UserRepository
DatabaseManager --> EventRepository
DatabaseManager --> ResultsRepository

GuiManager --> DatabaseManager
GuiManager --> Logic
GuiManager --> Components

Components --> EventDropdown
Components --> ParticipantForm
Components --> DescriptionLabel
Components --> ConfirmationDialog
Components --> EditUserDialog
Components --> WorkoutDetailsDialog

Logic --> UserRepository
Logic --> ResultsRepository
Logic --> EventRepository

UserRepository --> User
EventRepository --> Event
ResultsRepository --> FinalResult

@enduml
