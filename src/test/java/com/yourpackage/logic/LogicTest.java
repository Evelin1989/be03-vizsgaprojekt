// package com.yourpackage.logic;

// import com.yourpackage.models.Event;
// import com.yourpackage.models.User;
// import com.yourpackage.models.FinalResult;
// import com.yourpackage.repository.UserRepository;
// import com.yourpackage.repository.ResultsRepository;
// import javafx.collections.ObservableList;
// import org.junit.Before;
// import org.junit.Test;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.Assert.*;

// public class LogicTest {

//     private Logic logic;
//     private UserRepository userRepository;
//     private ResultsRepository resultsRepository;

//     @Before
//     public void setUp() {
//         // Stub implementations of repositories
//         userRepository = new UserRepository() {
//             @Override
//             public List<User> getNamesFromDatabase() throws SQLException {
//                 List<User> users = new ArrayList<>();
//                 users.add(new User(1, "John", "Doe", 100));
//                 users.add(new User(2, "Jane", "Smith", 80));
//                 return users;
//             }

//             @Override
//             public User getUserById(int id) {
//                 if (id == 1) return new User(1, "John", "Doe", 100);
//                 if (id == 2) return new User(2, "Jane", "Smith", 80);
//                 return null;
//             }
//         };

//         resultsRepository = new ResultsRepository() {
//             @Override
//             public void insertFinalResult(int eventId, int userId, int status) {
//                 // You can add assertions or logic to verify the method calls here
//             }
//         };

//         logic = new Logic(userRepository, resultsRepository);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testSimulateRace_NullEvent_ThrowsException() throws SQLException {
//         logic.simulateRace(null);
//     }

//     @Test
//     public void testSimulateRace_EmptyUserList_ReturnsEmptyResults() throws SQLException {
//         UserRepository emptyUserRepository = new UserRepository() {
//             @Override
//             public List<User> getNamesFromDatabase() {
//                 return new ArrayList<>();
//             }

//             @Override
//             public User getUserById(int id) {
//                 return null;
//             }
//         };

//         Logic logicWithEmptyUserRepo = new Logic(emptyUserRepository, resultsRepository);
//         Event event = new Event(1, "10km", "Running Event", 10);
//         ObservableList<FinalResult> results = logicWithEmptyUserRepo.simulateRace(event);
//         assertTrue(results.isEmpty());
//     }

//     @Test
//     public void testSimulateRace_ValidEvent_ReturnsCorrectResults() throws SQLException {
//         Event event = new Event(1, "10km", "Running Event", 10);
//         ObservableList<FinalResult> results = logic.simulateRace(event);
//         assertNotNull(results);
//         assertEquals(2, results.size());

//         // Verify the positions
//         assertEquals(1, results.get(0).getPosition());
//         assertEquals(2, results.get(1).getPosition());

//         // Verify the users in the correct order
//         assertEquals("John Doe", results.get(0).getUserName());
//         assertEquals("Jane Smith", results.get(1).getUserName());
//     }

//     @Test
//     public void testSimulateRace_CorrectMultiplierApplied() throws SQLException {
//         Event event = new Event(1, "42km", "Marathon", 42);
//         ObservableList<FinalResult> results = logic.simulateRace(event);
//         assertNotNull(results);

//         // Check if the multiplier was applied correctly
//         assertEquals((int) (100 * 0.6), results.get(0).getStatus());
//         assertEquals((int) (80 * 0.6), results.get(1).getStatus());
//     }
// }
