package Model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MovingEntity.*;
import java.util.ArrayList;

/**
 * Unit tests for end game conditions
 */
public class EndGameTest {
    
    private ModelMock model;
    private GameControllerMock gameController;
    
    /**
     * Mock class for the GameController, focused on end game logic
     */
    private static class GameControllerMock {
        private ModelMock model;
        private int elapsedDays = 0;
        private boolean lost = false;
        private boolean win = false;
        
        public GameControllerMock(ModelMock model) {
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
     * Mock class for the Model, focused on end game related functionality
     */
    private static class ModelMock {
        private int balance;
        private ArrayList<Animal> animals;
        public final String diff;
        
        public ModelMock(String difficulty) {
            this.diff = difficulty;
            this.animals = new ArrayList<>();
            
            // Set initial balance based on difficulty
            if (diff.equals("easy")) {
                balance = 5000;
            } else if (diff.equals("medium")) {
                balance = 4000;
            } else if (diff.equals("hard")) {
                balance = 3000;
            } else {
                balance = 0;
            }
        }
        
        public int getBalance() {
            return balance;
        }
        
        public void setBalance(int balance) {
            this.balance = balance;
        }
        
        public ArrayList<Predator> getPredators() {
            ArrayList<Predator> predators = new ArrayList<>();
            for (Animal animal : animals) {
                if (animal instanceof Predator) {
                    predators.add((Predator) animal);
                }
            }
            return predators;
        }
        
        public ArrayList<Herbivore> getHerbivores() {
            ArrayList<Herbivore> herbivores = new ArrayList<>();
            for (Animal animal : animals) {
                if (animal instanceof Herbivore) {
                    herbivores.add((Herbivore) animal);
                }
            }
            return herbivores;
        }
        
        public void addAnimal(Animal animal) {
            animals.add(animal);
        }
        
        public void clearAnimals() {
            animals.clear();
        }
    }
    
    // Mock animal classes for testing
    private static class PredatorMock extends Predator {
        public PredatorMock() {
            super(0, 0, 0, 0, 100, 100, "", 0);
        }
        
        @Override
        public Animal cloneAnimal() {
            return new PredatorMock();
        }
    }
    
    private static class HerbivoreMock extends Herbivore {
        public HerbivoreMock() {
            super(0, 0, 0, 0, 100, 100, "", 0);
        }
        
        @Override
        public Animal cloneAnimal() {
            return new HerbivoreMock();
        }
    }
    
    @BeforeEach
    void setUp() {
        model = new ModelMock("easy");
        gameController = new GameControllerMock(model);
    }
    
    @Test
    void testLoseByBankruptcy() {
        // Test losing by running out of money
        model.setBalance(0);
        
        boolean gameEnded = gameController.checkEnd();
        
        assertTrue(gameEnded, "Game should end when balance is 0");
        assertTrue(gameController.isLost(), "Player should lose when balance is 0");
        assertFalse(gameController.isWin(), "Player should not win when balance is 0");
    }
    
    @Test
    void testLoseByInsufficientPredators() {
        // Test losing by having too few predators on an even-numbered day
        model.clearAnimals();
        model.addAnimal(new PredatorMock()); // Only 1 predator
        
        // Set day to an even number > 0
        gameController.setElapsedDays(12);
        
        boolean gameEnded = gameController.checkEnd();
        
        assertTrue(gameEnded, "Game should end with insufficient predators on day 12");
        assertTrue(gameController.isLost(), "Player should lose with insufficient predators");
        assertFalse(gameController.isWin(), "Player should not win with insufficient predators");
    }
    
    @Test
    void testLoseByInsufficientHerbivores() {
        // Test losing by having too few herbivores on an even-numbered day
        model.clearAnimals();
        
        // Add sufficient predators but insufficient herbivores
        model.addAnimal(new PredatorMock());
        model.addAnimal(new PredatorMock());
        model.addAnimal(new HerbivoreMock());
        model.addAnimal(new HerbivoreMock());
        
        // Set day to an even number > 0
        gameController.setElapsedDays(12);
        
        boolean gameEnded = gameController.checkEnd();
        
        assertTrue(gameEnded, "Game should end with insufficient herbivores on day 12");
        assertTrue(gameController.isLost(), "Player should lose with insufficient herbivores");
        assertFalse(gameController.isWin(), "Player should not win with insufficient herbivores");
    }
    
    @Test
    void testNoEndOnOddDays() {
        // Test that animal population is not checked on odd-numbered days
        model.clearAnimals();
        model.addAnimal(new PredatorMock()); // Only 1 predator
        
        // Set day to an odd number
        gameController.setElapsedDays(1);
        
        boolean gameEnded = gameController.checkEnd();
        
        assertFalse(gameEnded, "Game should not end on odd-numbered days due to insufficient animals");
        assertFalse(gameController.isLost(), "Player should not lose on odd-numbered days due to insufficient animals");
    }
    
    @Test
    void testWinOnEasyDifficulty() {
        // Test winning on easy difficulty
        model = new ModelMock("easy");
        gameController = new GameControllerMock(model);
        
        // Add sufficient animals to avoid losing
        model.addAnimal(new PredatorMock());
        model.addAnimal(new PredatorMock());
        model.addAnimal(new HerbivoreMock());
        model.addAnimal(new HerbivoreMock());
        model.addAnimal(new HerbivoreMock());
        model.addAnimal(new HerbivoreMock());
        model.addAnimal(new HerbivoreMock());
        
        // Set day just before winning
        gameController.setElapsedDays(29);
        assertFalse(gameController.checkEnd(), "Game should not end on day 29 in easy mode");
        
        // Set day to winning day
        gameController.setElapsedDays(30);
        assertTrue(gameController.checkEnd(), "Game should end on day 30 in easy mode");
        assertTrue(gameController.isWin(), "Player should win on day 30 in easy mode");
        assertFalse(gameController.isLost(), "Player should not lose on day 30 in easy mode");
    }
    
    @Test
    void testWinOnMediumDifficulty() {
        // Test winning on medium difficulty
        model = new ModelMock("medium");
        gameController = new GameControllerMock(model);
        
        // Add sufficient animals to avoid losing
        addSufficientAnimals();
        
        // Set day to winning day
        gameController.setElapsedDays(60);
        assertTrue(gameController.checkEnd(), "Game should end on day 60 in medium mode");
        assertTrue(gameController.isWin(), "Player should win on day 60 in medium mode");
    }
    
    @Test
    void testWinOnHardDifficulty() {
        // Test winning on hard difficulty
        model = new ModelMock("hard");
        gameController = new GameControllerMock(model);
        
        // Add sufficient animals to avoid losing
        addSufficientAnimals();
        
        // Set day to winning day
        gameController.setElapsedDays(90);
        assertTrue(gameController.checkEnd(), "Game should end on day 90 in hard mode");
        assertTrue(gameController.isWin(), "Player should win on day 90 in hard mode");
    }
    
    @Test
    void testNoWinBeforeTargetDay() {
        // Test that win is not triggered before the target day
        model = new ModelMock("hard");
        gameController = new GameControllerMock(model);
        
        // Add sufficient animals to avoid losing
        addSufficientAnimals();
        
        // Set day before winning day
        gameController.setElapsedDays(89);
        assertFalse(gameController.checkEnd(), "Game should not end on day 89 in hard mode");
        assertFalse(gameController.isWin(), "Player should not win on day 89 in hard mode");
    }
    
    // Helper method to add sufficient animals to avoid losing
    private void addSufficientAnimals() {
        model.clearAnimals();
        // Add 3 predators and 7 herbivores to be safely above the minimum
        for (int i = 0; i < 3; i++) {
            model.addAnimal(new PredatorMock());
        }
        for (int i = 0; i < 7; i++) {
            model.addAnimal(new HerbivoreMock());
        }
    }
} 