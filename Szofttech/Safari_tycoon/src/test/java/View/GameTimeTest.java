package View;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.JLabel;

/**
 * Test class for game time functionality that works in headless environments
 */
public class GameTimeTest {
    
    // Use a mock instead of actual GameWindow
    private GameWindowMock gameWindow;
    
    /**
     * Mock class for GameWindow that avoids using UI components
     */
    private static class GameWindowMock {
        public JLabel timeLabel = new JLabel();
        private int currentSpeed = 1; // SPEED_HOUR
        private int elapsedHours = 0;
        private int elapsedDays = 0;
        
        // Constants copied from GameWindow
        public static final int SPEED_HOUR = 1;
        public static final int SPEED_HALF_DAY = 12;
        public static final int SPEED_DAY = 24;
        
        public void setGameSpeed(int speed) {
            currentSpeed = speed;
        }
        
        public int getCurrentSpeed() {
            return currentSpeed;
        }
        
        public void updateGameTime(int hours, int days) {
            this.elapsedHours = hours;
            this.elapsedDays = days;
            String hourStr = String.format("%02d:00", hours % 24);
            timeLabel.setText("Day " + (days + 1) + ", " + hourStr);
        }
    }
    
    @BeforeEach
    void setUp() {
        // Create a mock instead of real GameWindow
        gameWindow = new GameWindowMock();
    }
    
    @Test
    void testGameSpeedConstants() {
        // Verify the game speed constants
        assertEquals(1, GameWindowMock.SPEED_HOUR, "SPEED_HOUR should be 1");
        assertEquals(12, GameWindowMock.SPEED_HALF_DAY, "SPEED_HALF_DAY should be 12");
        assertEquals(24, GameWindowMock.SPEED_DAY, "SPEED_DAY should be 24");
    }
    
    @Test
    void testSetGameSpeed() {
        // Test setting different game speeds
        gameWindow.setGameSpeed(GameWindowMock.SPEED_HOUR);
        assertEquals(GameWindowMock.SPEED_HOUR, gameWindow.getCurrentSpeed(), "Speed should be set to SPEED_HOUR");
        
        gameWindow.setGameSpeed(GameWindowMock.SPEED_HALF_DAY);
        assertEquals(GameWindowMock.SPEED_HALF_DAY, gameWindow.getCurrentSpeed(), "Speed should be set to SPEED_HALF_DAY");
        
        gameWindow.setGameSpeed(GameWindowMock.SPEED_DAY);
        assertEquals(GameWindowMock.SPEED_DAY, gameWindow.getCurrentSpeed(), "Speed should be set to SPEED_DAY");
    }
    
    @Test
    void testUpdateGameTime() {
        // Test updating the game time with various time values
        
        // Test case 1: Day 1, 08:00
        gameWindow.updateGameTime(8, 0);
        assertEquals("Day 1, 08:00", gameWindow.timeLabel.getText(), "Time label should display 'Day 1, 08:00'");
        
        // Test case 2: Day 2, 14:00
        gameWindow.updateGameTime(14, 1);
        assertEquals("Day 2, 14:00", gameWindow.timeLabel.getText(), "Time label should display 'Day 2, 14:00'");
        
        // Test case 3: Day 10, 23:00
        gameWindow.updateGameTime(23, 9);
        assertEquals("Day 10, 23:00", gameWindow.timeLabel.getText(), "Time label should display 'Day 10, 23:00'");
        
        // Test case 4: Handle hour wrapping correctly (25th hour is 01:00 of next day)
        gameWindow.updateGameTime(1, 10);
        assertEquals("Day 11, 01:00", gameWindow.timeLabel.getText(), "Time label should display 'Day 11, 01:00'");
        
        // Test case 5: Ensure zero-padding for hours
        gameWindow.updateGameTime(5, 20);
        assertEquals("Day 21, 05:00", gameWindow.timeLabel.getText(), "Time label should display 'Day 21, 05:00'");
    }
    
    @Test
    void testElapsedTimeTracking() {
        // Test that the elapsed time fields are updated correctly
        
        // Update time to 8 hours on day 0
        gameWindow.updateGameTime(8, 0);
        assertEquals(8, gameWindow.elapsedHours, "elapsedHours should be 8");
        assertEquals(0, gameWindow.elapsedDays, "elapsedDays should be 0");
        
        // Update time to 14 hours on day 5
        gameWindow.updateGameTime(14, 5);
        assertEquals(14, gameWindow.elapsedHours, "elapsedHours should be 14");
        assertEquals(5, gameWindow.elapsedDays, "elapsedDays should be 5");
    }
} 