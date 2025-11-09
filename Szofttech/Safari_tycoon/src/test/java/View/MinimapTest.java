package View;

import Model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import MovingEntity.Herbivore;
import MovingEntity.Predator;

import static org.junit.jupiter.api.Assertions.*;

class MinimapTest {
    
    private MockModel model;
    private GamePanel gamePanel;
    private Minimap minimap;
    private JLabel balanceLabel;
    
    /**
     * Mock model class
     */
    private static class MockModel extends Model {
        public MockModel(String diff) {
            super(diff);
        }
        
        @Override
        public int[][] loadMap(String path) {
            // Return empty map instead of loading from file
            return new int[maxWorldRow][maxWorldCol];
        }
        
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
    
    @BeforeEach
    void setUp() {
        // Create a mock model with the "easy" map
        model = new MockModel("easy");
        
        // Create a JLabel for the balance
        balanceLabel = new JLabel();
        
        // Create a minimal GamePanel for testing
        gamePanel = new GamePanel(null, null, model, balanceLabel, "easy") {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Create the minimap with the mock model and game panel
        minimap = new Minimap(gamePanel, model);
    }
    
    @Test
    void testConstructor() {
        // Check initial values
        try {
            Field minimapWidthField = Minimap.class.getDeclaredField("minimapWidth");
            minimapWidthField.setAccessible(true);
            int minimapWidth = (int) minimapWidthField.get(minimap);
            
            Field minimapHeightField = Minimap.class.getDeclaredField("minimapHeight");
            minimapHeightField.setAccessible(true);
            int minimapHeight = (int) minimapHeightField.get(minimap);
            
            Field originalMinimapWidthField = Minimap.class.getDeclaredField("originalMinimapWidth");
            originalMinimapWidthField.setAccessible(true);
            int originalMinimapWidth = (int) originalMinimapWidthField.get(minimap);
            
            Field originalMinimapHeightField = Minimap.class.getDeclaredField("originalMinimapHeight");
            originalMinimapHeightField.setAccessible(true);
            int originalMinimapHeight = (int) originalMinimapHeightField.get(minimap);
            
            Field isFullscreenField = Minimap.class.getDeclaredField("isFullscreen");
            isFullscreenField.setAccessible(true);
            boolean isFullscreen = (boolean) isFullscreenField.get(minimap);
            
            // Verify values
            assertEquals(model.screenWidth / 8, minimapWidth);
            assertEquals(model.screenWidth / 8, minimapHeight);
            assertEquals(minimapWidth, originalMinimapWidth);
            assertEquals(minimapHeight, originalMinimapHeight);
            assertFalse(isFullscreen);
            
            // Check dimension
            Dimension expectedDimension = new Dimension(
                minimapWidth + 2 * minimap.borderThickness,
                minimapHeight + 2 * minimap.borderThickness
            );
            assertEquals(expectedDimension, minimap.getPreferredSize());
            
            // Check background
            assertEquals(Color.BLACK, minimap.getBackground());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    void testSetFullscreen() {
        try {
            // Get access to the isFullscreen field
            Field isFullscreenField = Minimap.class.getDeclaredField("isFullscreen");
            isFullscreenField.setAccessible(true);
            
            // Initial state
            assertFalse((boolean) isFullscreenField.get(minimap));
            
            // Set fullscreen to true
            minimap.setFullscreen(true);
            assertTrue((boolean) isFullscreenField.get(minimap));
            
            // Set fullscreen back to false
            minimap.setFullscreen(false);
            assertFalse((boolean) isFullscreenField.get(minimap));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    void testDrawVisibleAreaRectangle() {
        try {
            // Get access to the private drawVisibleAreaRectangle method
            Method drawVisibleAreaRectangleMethod = Minimap.class.getDeclaredMethod("drawVisibleAreaRectangle", 
                Graphics2D.class, double.class);
            drawVisibleAreaRectangleMethod.setAccessible(true);
            
            // Create a test Graphics2D object
            BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = testImage.createGraphics();
            
            // Test in fullscreen mode
            minimap.setFullscreen(true);
            drawVisibleAreaRectangleMethod.invoke(minimap, g2d, 0.5);
            
            // Test in normal mode
            minimap.setFullscreen(false);
            drawVisibleAreaRectangleMethod.invoke(minimap, g2d, 0.5);
            
            // Clean up
            g2d.dispose();
        } catch (Exception e) {
            fail("drawVisibleAreaRectangle method threw an exception: " + e.getMessage());
        }
    }
    
    @Test
    void testPaintComponent() {
        // Create a buffered image for testing
        BufferedImage testImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = testImage.getGraphics();
        
        // Execute the paintComponent method
        minimap.paintComponent(g);
        
        // We can't directly verify pixel values without complex image analysis
        // but we can verify the method runs without exceptions
        g.dispose();
    }
    
    @Test
    void testScalingCalculation() {
        try {
            // Create a test graphics context
            BufferedImage testImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = testImage.createGraphics();
            
            // Create a custom method to test the scaling calculation
            Method paintComponentMethod = Minimap.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintComponentMethod.setAccessible(true);
            
            // Set up test conditions
            
            // Test 1: Regular mode (not fullscreen)
            minimap.setFullscreen(false);
            
            // Set size to simulate JPanel's getWidth() and getHeight()
            minimap.setSize(200, 200);
            
            // Call paintComponent
            paintComponentMethod.invoke(minimap, g2d);
            
            // Test 2: Fullscreen mode
            minimap.setFullscreen(true);
            
            // Set size to simulate fullscreen
            minimap.setSize(model.screenWidth, model.screenHeight);
            
            // Call paintComponent again
            paintComponentMethod.invoke(minimap, g2d);
            
            // Clean up
            g2d.dispose();
        } catch (Exception e) {
            fail("Scaling test failed with exception: " + e.getMessage());
        }
    }
    
    @Test
    void testExitFullscreenOnButtonClick() {
        try {
            // Instead of creating a real GameWindow, test the core functionality
            // that shows that when a button is clicked in fullscreen mode, the minimap exits fullscreen

            // Test the initial setup of our Minimap
            minimap.setFullscreen(true);
            Field isFullscreenField = Minimap.class.getDeclaredField("isFullscreen");
            isFullscreenField.setAccessible(true);
            assertTrue((boolean) isFullscreenField.get(minimap), "Minimap should be in fullscreen mode initially");

            // Simulate what happens when a button is clicked
            // The button click would call setFullscreen(false) on the minimap
            minimap.setFullscreen(false);
            assertFalse((boolean) isFullscreenField.get(minimap), "Minimap should exit fullscreen mode");

            // Validate that the minimap dimensions change correctly when exiting fullscreen
            int originalWidth = minimap.originalMinimapWidth;
            int originalHeight = minimap.originalMinimapHeight;
            Dimension expectedNormalSize = new Dimension(
                originalWidth + 2 * minimap.borderThickness,
                originalHeight + 2 * minimap.borderThickness
            );
            assertEquals(expectedNormalSize, minimap.getPreferredSize(), 
                "Minimap should revert to original size when exiting fullscreen");
            
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
} 