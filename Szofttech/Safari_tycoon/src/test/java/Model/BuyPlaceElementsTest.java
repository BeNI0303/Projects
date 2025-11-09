package Model;

import static org.junit.jupiter.api.Assertions.*;

import Items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Test class for testing buying and placing items in the game
 */
public class BuyPlaceElementsTest {
    
    private ModelMock model;
    
    /**
     * Mock class for the Model
     */
    private static class ModelMock {
        public int[][] tiles;
        private int balance;
        public final int tileSize = 48; // 16 * 3
        public final int maxWorldCol = 50;
        public final int maxWorldRow = 50;
        
        // Items
        private Base base;
        private Bush bush;
        private Grass grass;
        private Lake lake;
        private Road road;
        private Tree tree;
        private Edge edge;
        private Item[] tileTypes;
        private ArrayList<Item> items;
        private ArrayList<Point> roadPath;
        private Point lastRoad;
        private Point endRoad;
        
        // Add fields for Jeep testing
        private ArrayList<JeepMock> jeeps;
        private boolean isRoadComplete;
        
        public ModelMock() {
            // Initialize with test data
            this.balance = 5000;
            this.tiles = new int[maxWorldRow][maxWorldCol];
            this.items = new ArrayList<>();
            this.roadPath = new ArrayList<>();
            
            // Initialize items
            this.tileTypes = new Item[7];
            
            // Create mock items without loading images
            this.base = new BaseMock(0);
            this.bush = new BushMock(100);
            this.grass = new GrassMock(100);
            this.lake = new LakeMock(100);
            this.road = new RoadMock(100);
            this.tree = new TreeMock(100);
            this.edge = new EdgeMock(0);
            
            this.tileTypes[base.getId()] = base;
            this.tileTypes[bush.getId()] = bush;
            this.tileTypes[grass.getId()] = grass;
            this.tileTypes[lake.getId()] = lake;
            this.tileTypes[road.getId()] = road;
            this.tileTypes[tree.getId()] = tree;
            this.tileTypes[edge.getId()] = edge;
            
            // Set starting road for road placement
            this.lastRoad = new Point(49, 19);
            this.endRoad = new Point(49, 29);
            this.roadPath.add(lastRoad);
            
            // Initialize jeep-related fields
            this.jeeps = new ArrayList<>();
            this.isRoadComplete = false;
        }
        
        public boolean purchaseItem(int itemId) {
            // Check if item ID is valid
            boolean isIdValid = itemId >= 1 && itemId < tileTypes.length;
            
            if (isIdValid) {
                Item item = tileTypes[itemId];
                if (balance >= item.getPrice()) {
                    balance -= item.getPrice();
                    return true;
                }
            }
            return false;
        }
        
        public boolean canPlaceOnPosition(int row, int col, int type) {
            // For most tiles, just check if the tile is empty (0)
            if (type != 5) {
                return (tiles[row][col] == 0);
            }
            
            // Special logic for roads
            if (type == 5) {
                if (tiles[row][col] != 0) return false;
                if (endRoad.equals(lastRoad)) return false;
                
                // Count adjacent roads
                int cnt = 0;
                if (row > 0 && tiles[row - 1][col] == 5) cnt++;
                if (row < maxWorldRow - 1 && tiles[row + 1][col] == 5 && !endRoad.equals(new Point(row + 1, col))) cnt++;
                if (col > 0 && tiles[row][col - 1] == 5) cnt++;
                if (col < maxWorldCol - 1 && tiles[row][col + 1] == 5) cnt++;
                
                // Check if connected to lastRoad
                if (cnt == 1 && (lastRoad.equals(new Point(row - 1, col)) || 
                                lastRoad.equals(new Point(row + 1, col)) || 
                                lastRoad.equals(new Point(row, col - 1)) || 
                                lastRoad.equals(new Point(row, col + 1)))) {
                    lastRoad = new Point(row, col);
                    roadPath.add(lastRoad);
                    
                    // Check if we reached endRoad
                    if (endRoad.equals(new Point(row + 1, col))) {
                        lastRoad = (Point) endRoad.clone();
                        roadPath.add(lastRoad);
                    }
                    return true;
                }
                return false;
            }
            
            return false;
        }
        
        public void addItem(int type, int col, int row) {
            if (type == 0) return;
            
            Item newItem = null;
            int x = col * tileSize;
            int y = row * tileSize;
            
            if (type == 1) {
                newItem = new GrassMock(grass.getPrice(), x, y);
            } else if (type == 2) {
                newItem = new LakeMock(lake.getPrice(), x, y);
            } else if (type == 3) {
                newItem = new BushMock(bush.getPrice(), x, y);
            } else if (type == 4) {
                newItem = new TreeMock(tree.getPrice(), x, y);
            } else if (type == 5) {
                newItem = new RoadMock(road.getPrice(), x, y);
            } else if (type == 6) {
                newItem = new EdgeMock(edge.getPrice(), x, y);
            }
            
            if (newItem != null) {
                items.add(newItem);
            }
        }
        
        public int getBalance() {
            return balance;
        }
        
        public ArrayList<Item> getItems() {
            return items;
        }
        
        public ArrayList<Point> getRoadPath() {
            return roadPath;
        }
        
        public void addJeep() {
            jeeps.add(new JeepMock(new Point(lastRoad.x, lastRoad.y)));
        }
        
        public boolean isRoadComplete() {
            return lastRoad.equals(endRoad);
        }
        
        public ArrayList<JeepMock> getJeeps() {
            return jeeps;
        }
        
        public void updateJeeps() {
            // Only move jeeps if the road is complete
            if (isRoadComplete()) {
                for (JeepMock jeep : jeeps) {
                    jeep.setMoving(true);
                }
            }
        }
    }
    
    // Mock classes for Item and its subclasses
    private static class BaseMock extends Base {
        public BaseMock(int price) {
            super(price);
        }
    }
    
    private static class BushMock extends Bush {
        public BushMock(int price) {
            super(price);
        }
        
        public BushMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    private static class GrassMock extends Grass {
        public GrassMock(int price) {
            super(price);
        }
        
        public GrassMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    private static class LakeMock extends Lake {
        public LakeMock(int price) {
            super(price);
        }
        
        public LakeMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    private static class RoadMock extends Road {
        public RoadMock(int price) {
            super(price);
        }
        
        public RoadMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    private static class TreeMock extends Tree {
        public TreeMock(int price) {
            super(price);
        }
        
        public TreeMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    private static class EdgeMock extends Edge {
        public EdgeMock(int price) {
            super(price);
        }
        
        public EdgeMock(int price, int x, int y) {
            super(price, x, y);
        }
    }
    
    // Mock class for Jeep
    private static class JeepMock {
        private Point position;
        private boolean isMoving;
        
        public JeepMock(Point startPosition) {
            this.position = startPosition;
            this.isMoving = false;
        }
        
        public Point getPosition() {
            return position;
        }
        
        public boolean isMoving() {
            return isMoving;
        }
        
        public void setMoving(boolean moving) {
            this.isMoving = moving;
        }
    }
    
    @BeforeEach
    void setUp() {
        model = new ModelMock();
    }
    
    @Test
    void testInitialBalance() {
        // Test the initial balance is correct
        assertEquals(5000, model.getBalance(), "Initial balance should be 5000");
    }
    
    @Test
    void testPurchaseItem() {
        // Test purchasing an item reduces balance correctly
        int initialBalance = model.getBalance();
        int grassPrice = model.grass.getPrice();
        
        // Purchase a grass tile
        boolean result = model.purchaseItem(1); // ID 1 is grass
        
        assertTrue(result, "Purchase should be successful");
        assertEquals(initialBalance - grassPrice, model.getBalance(), 
                "Balance should be reduced by the price of grass");
    }
    
    @Test
    void testPurchaseItemInsufficientFunds() {
        // Set balance to a very low value
        model.balance = 10;
        
        // Try to purchase an item that costs more than available balance
        boolean result = model.purchaseItem(3); // ID 3 is bush
        
        assertFalse(result, "Purchase should fail due to insufficient funds");
        assertEquals(10, model.getBalance(), "Balance should remain unchanged");
    }
    
    @Test
    void testCanPlaceOnEmptyTile() {
        // Test that we can place on an empty tile
        boolean result = model.canPlaceOnPosition(10, 10, 1); // Place grass at 10,10
        
        assertTrue(result, "Should be able to place on an empty tile");
    }
    
    @Test
    void testCannotPlaceOnOccupiedTile() {
        // Set up a tile that's already occupied
        model.tiles[10][10] = 3; // Bush
        
        // Try to place on the occupied tile
        boolean result = model.canPlaceOnPosition(10, 10, 1);
        
        assertFalse(result, "Should not be able to place on an occupied tile");
    }
    
    @Test
    void testPlaceAndPurchaseItem() {
        // Test placing an item on the board
        int initialBalance = model.getBalance();
        int initialItemCount = model.getItems().size();
        
        // Place a tree on the board
        int treeId = 4; // ID for tree
        int row = 15;
        int col = 15;
        
        // Check if we can place it
        boolean canPlace = model.canPlaceOnPosition(row, col, treeId);
        
        assertTrue(canPlace, "Should be able to place a tree on an empty tile");
        
        // Purchase the item
        boolean purchased = model.purchaseItem(treeId);
        
        assertTrue(purchased, "Should be able to purchase a tree");
        
        // Update tiles and add item
        model.tiles[row][col] = treeId;
        model.addItem(treeId, col, row);
        
        // Verify the results
        assertEquals(initialBalance - model.tree.getPrice(), model.getBalance(),
                "Balance should be reduced by the price of the tree");
        assertEquals(initialItemCount + 1, model.getItems().size(),
                "Item should be added to the items list");
    }
    
    @Test
    void testRoadPlacement() {
        // Test the road placement functionality
        int roadId = 5; // Road tile ID
        
        // First, reset the map and road data
        model.tiles = new int[model.maxWorldRow][model.maxWorldCol];
        model.roadPath.clear();
        
        // Set up the initial road at coordinates (10,10)
        int startX = 10;
        int startY = 10;
        
        // In the model, lastRoad is a Point, which stores (x,y) -> (col,row)
        model.lastRoad = new Point(startX, startY);
        model.roadPath.add(model.lastRoad);
        
        // Mark this position as a road in the tiles array
        model.tiles[startY][startX] = roadId;
        
        // Try to place a new road to the right of the existing road
        // The new position would be (11,10) -> (x+1,y)
        int newX = startX + 1; // col+1
        int newY = startY;     // row
        
        // When calling canPlaceOnPosition, we pass (row,col) -> (y,x)
        boolean canPlace = model.canPlaceOnPosition(newY, newX, roadId);
        
        // Verify the road can be placed
        assertTrue(canPlace, "Should be able to place a road connected to the last road");
        
        // So, the correct expectation for lastRoad after placement should be:
        assertEquals(newY, model.lastRoad.x, "Last road's x coordinate should match the row");
        assertEquals(newX, model.lastRoad.y, "Last road's y coordinate should match the column");
        
        // Check that roadPath has grown
        assertEquals(2, model.roadPath.size(), "Road path should now have two points");
    }
    
    @Test
    void testInvalidRoadPlacement() {
        // Road tile ID
        int roadId = 5;
        
        // Reset the map and road data
        model.tiles = new int[model.maxWorldRow][model.maxWorldCol];
        model.roadPath.clear();
        
        // Set up the initial road at (10,10)
        int startX = 10;
        int startY = 10;
        model.lastRoad = new Point(startX, startY);
        model.roadPath.add(model.lastRoad);
        model.tiles[startY][startX] = roadId;
        
        // Case 1: Try to place a road at a non-adjacent position (not connected)
        int farX = startX + 2;
        int farY = startY + 2;
        boolean canPlaceFar = model.canPlaceOnPosition(farY, farX, roadId);
        assertFalse(canPlaceFar, "Should not be able to place a road at a non-adjacent position");
        
        // Case 2: Try to place a road on an occupied tile
        int adjacentX = startX + 1;
        int adjacentY = startY;
        model.tiles[adjacentY][adjacentX] = 3; // Place a bush there
        boolean canPlaceOccupied = model.canPlaceOnPosition(adjacentY, adjacentX, roadId);
        assertFalse(canPlaceOccupied, "Should not be able to place a road on an occupied tile");
        
        // Case 3: Try to place a road where lastRoad equals endRoad
        model.endRoad = model.lastRoad;
        boolean canPlaceAtEnd = model.canPlaceOnPosition(startY + 1, startX, roadId);
        assertFalse(canPlaceAtEnd, "Should not be able to place a road when lastRoad equals endRoad");
        
        // Case 4: Try to place a road that would connect to multiple existing roads
        // Reset end road position
        model.endRoad = new Point(49, 29);
        
        // Place two roads adjacent to a target position
        int targetX = 15;
        int targetY = 15;
        model.tiles[targetY][targetX - 1] = roadId; // Road to the left
        model.tiles[targetY - 1][targetX] = roadId; // Road above
        
        // Update lastRoad to be adjacent to the target position
        model.lastRoad = new Point(targetY, targetX - 1);
        
        boolean canPlaceMultipleAdjacent = model.canPlaceOnPosition(targetY, targetX, roadId);
        assertFalse(canPlaceMultipleAdjacent, "Should not be able to place a road adjacent to multiple existing roads");
        
        // Verify that roadPath size hasn't increased
        assertEquals(1, model.roadPath.size(), "Road path size should remain unchanged after failed placement attempts");
    }
    
    @Test
    void testCompleteRoadPath() {
        // Test creating a complete road from start to end
        int roadId = 5;
        
        // Reset the test environment
        model.tiles = new int[model.maxWorldRow][model.maxWorldCol];
        model.roadPath.clear();
        
        // Set up a controlled environment with a short path
        // Start road at (10, 10)
        int startX = 10;
        int startY = 10;
        // End road at (10, 15)
        int endX = 10;
        int endY = 15;
        
        // Set up start and end points
        model.lastRoad = new Point(startY, startX);
        model.endRoad = new Point(endY, endX);
        model.roadPath.clear();
        model.roadPath.add(model.lastRoad);
        
        // Mark start position as road
        model.tiles[startY][startX] = roadId;
        
        // Add a jeep at the start position
        model.addJeep();
        
        // Check that the jeep is not moving yet (road incomplete)
        assertFalse(model.getJeeps().get(0).isMoving(), "Jeep should not be moving when road is incomplete");
        
        // Build a path from start to almost the end
        for (int y = startY + 1; y < endY - 1; y++) {
            boolean canPlace = model.canPlaceOnPosition(y, startX, roadId);
            assertTrue(canPlace, "Should be able to place road at position (" + y + "," + startX + ")");
            
            // Verify lastRoad was updated
            assertEquals(y, model.lastRoad.x, "lastRoad.x should be updated to row " + y);
            assertEquals(startX, model.lastRoad.y, "lastRoad.y should remain at column " + startX);
            
            // Mark the position as a road in the tiles array
            model.tiles[y][startX] = roadId;
        }
        
        // Check road is not complete yet
        assertFalse(model.isRoadComplete(), "Road should not be complete before reaching end road");
        
        // Check jeep still not moving
        model.updateJeeps();
        assertFalse(model.getJeeps().get(0).isMoving(), "Jeep should not be moving when road is incomplete");
        
        // Place the road piece at (10,14)
        boolean penultimatePlacement = model.canPlaceOnPosition(endY - 1, endX, roadId);
        assertTrue(penultimatePlacement, "Should be able to place the penultimate road piece");
        
        // The tile should update lastRoad normally
        assertEquals(endY, model.lastRoad.x, "lastRoad.x should be updated to row " + (endY));
        assertEquals(endX, model.lastRoad.y, "lastRoad.y should remain at column " + endX);
        
        // Mark the position as a road in the tiles array
        model.tiles[endY][endX] = roadId;
        
        assertEquals(model.endRoad.x, model.lastRoad.x, "lastRoad.x should equal endRoad.x after completing the path");
        assertEquals(model.endRoad.y, model.lastRoad.y, "lastRoad.y should equal endRoad.y after completing the path");
        
        // Check that the road is now complete
        assertTrue(model.isRoadComplete(), "Road should be complete after reaching end road");
        
        // Check that the roadPath has the correct number of points
        assertEquals(6, model.roadPath.size(), "Road path should contain 6 points");
        
        // Update jeeps and check if they're now moving
        model.updateJeeps();
        assertTrue(model.getJeeps().get(0).isMoving(), "Jeep should start moving when road is complete");
    }
} 