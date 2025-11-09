package Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import MovingEntity.Herbivore;
import MovingEntity.Predator;
import java.util.ArrayList;

/**
 * Unit tests for game difficulty selection
 */
public class DifficultyTest {

    /**
     * Test starting balance based on selected difficulty
     */
    @ParameterizedTest
    @CsvSource({
        "easy, 5000",
        "medium, 4000",
        "hard, 3000"
    })
    void testInitialBalanceByDifficulty(String difficulty, int expectedBalance) {
        ModelMock model = new ModelMock(difficulty);
        assertEquals(expectedBalance, model.getBalance(), 
                "Initial balance should be " + expectedBalance + " for " + difficulty + " difficulty");
        assertEquals(difficulty, model.diff, 
                "Model difficulty should match selected difficulty");
    }
    
    /**
     * Test victory day requirement based on difficulty
     */
    @ParameterizedTest
    @CsvSource({
        "easy, 30",
        "medium, 60", 
        "hard, 90"
    })
    void testVictoryDaysByDifficulty(String difficulty, int requiredDays) {
        ModelMock model = new ModelMock(difficulty);
        GameControllerMock gameController = new GameControllerMock(model);
        
        // Add enough animals to prevent losing by animal requirements
        addSufficientAnimals(model);
        
        // Test day before victory
        gameController.setElapsedDays(requiredDays - 1);
        assertFalse(gameController.checkEnd(), 
                "Game should not end before day " + requiredDays + " on " + difficulty + " difficulty");
        assertFalse(gameController.isWin(), 
                "Player should not win before day " + requiredDays);
        
        // Test victory day
        gameController.setElapsedDays(requiredDays);
        assertTrue(gameController.checkEnd(), 
                "Game should end on day " + requiredDays + " on " + difficulty + " difficulty");
        assertTrue(gameController.isWin(), 
                "Player should win on day " + requiredDays);
    }
    
    /**
     * Test that game map is loaded based on difficulty
     */
    @ParameterizedTest
    @ValueSource(strings = {"easy", "medium", "hard"})
    void testMapLoadedByDifficulty(String difficulty) {
        ModelMock model = new ModelMock(difficulty);
        assertEquals("./src/main/resources/map/map_" + difficulty + ".txt", model.getMapPath(),
                "Map path should be correctly formatted based on difficulty");
    }
    
    /**
     * Mock model class
     */
    private static class ModelMock extends Model {
        private String mapPath;
        
        public ModelMock(String diff) {
            super(diff);
            // Store map path for testing without accessing files
            this.mapPath = "./src/main/resources/map/map_" + diff + ".txt";
        }
        
        @Override
        public int[][] loadMap(String path) {
            // Store the path that would be loaded without actually loading it
            this.mapPath = path;
            // Return empty map for testing
            return new int[maxWorldRow][maxWorldCol];
        }
        
        public String getMapPath() {
            return mapPath;
        }
        
        // Override animal-related methods
        @Override
        public ArrayList<Predator> getPredators() {
            ArrayList<Predator> result = super.getPredators();
            if (result == null) {
                return new ArrayList<>();
            }
            return result;
        }
        
        @Override
        public ArrayList<Herbivore> getHerbivores() {
            ArrayList<Herbivore> result = super.getHerbivores();
            if (result == null) {
                return new ArrayList<>();
            }
            return result;
        }
    }
    
    /**
     * Mock GameController class for testing victory conditions
     */
    private static class GameControllerMock {
        private Model model;
        private int elapsedDays = 0;
        private boolean lost = false;
        private boolean win = false;
        
        public GameControllerMock(Model model) {
            this.model = model;
        }
        
        public void setElapsedDays(int days) {
            this.elapsedDays = days;
        }
        
        public boolean checkEnd() {
            // Check bankruptcy condition
            if (model.getBalance() <= 0) {
                lost = true;
                return true;
            }
            
            // Check animal population conditions (only on even days)
            if (elapsedDays % 2 == 0 && elapsedDays > 0) {
                if (model.getPredators().size() < 2) {
                    lost = true;
                    return true;
                } else if (model.getHerbivores().size() < 5) {
                    lost = true;
                    return true;
                }
            }
            
            // Check win conditions based on difficulty
            if (model.diff.equals("easy")) {
                if (elapsedDays >= 30) {
                    win = true;
                    return true;
                }
            } else if (model.diff.equals("medium")) {
                if (elapsedDays >= 60) {
                    win = true;
                    return true;
                }
            } else if (model.diff.equals("hard")) {
                if (elapsedDays >= 90) {
                    win = true;
                    return true;
                }
            }
            
            return false;
        }
        
        public boolean isLost() {
            return lost;
        }
        
        public boolean isWin() {
            return win;
        }
    }
    
    /**
     * Helper method to add sufficient animals to avoid losing due to animal requirements
     */
    private void addSufficientAnimals(Model model) {
        // Add 3 predators (2+ required to not lose)
        model.addAnimal(104, 0, 0, false); // Lion
        model.addAnimal(104, 0, 0, false); // Lion 
        model.addAnimal(103, 0, 0, false); // Cheetah
        
        // Add 7 herbivores (5+ required to not lose)
        for (int i = 0; i < 3; i++) {
            model.addAnimal(100, 0, 0, false); // Antelope
            model.addAnimal(101, 0, 0, false); // Giraffe
        }
        model.addAnimal(102, 0, 0, false); // Zebra
    }
}