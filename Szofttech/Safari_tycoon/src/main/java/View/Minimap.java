package View;

import Model.Model;
import MovingEntity.Entity;
import MovingEntity.Jeep;
import MovingEntity.Animal;
import MovingEntity.Ranger;
import Controller.GameController;
import Items.Item;
import Items.Entrance;
import MovingEntity.Visitor;

import javax.swing.*;
import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.awt.RadialGradientPaint;

public class Minimap extends JPanel {
    final int borderThickness = 5;
    final int originalMinimapWidth;
    final int originalMinimapHeight;
    private final Model model;
    private final GamePanel gp;
    private final int minimapWidth;
    private final int minimapHeight;
    private boolean isFullscreen = false;
    private GameController gameController;

    // Cache frequently used values and reuse objects
    private BufferedImage lightOverlay;
    private BufferedImage mergedLights;

    public Minimap(GamePanel gp, Model model) {
        this.model = model;
        this.gp = gp;
        this.minimapWidth = model.screenWidth / 8;
        this.minimapHeight = model.screenWidth / 8;
        this.originalMinimapWidth = minimapWidth;
        this.originalMinimapHeight = minimapHeight;
        this.setPreferredSize(new Dimension(minimapWidth + 2 * borderThickness, minimapHeight + 2 * borderThickness));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.lightOverlay = new BufferedImage(model.screenWidth, model.screenHeight, BufferedImage.TYPE_INT_ARGB);
        this.mergedLights = new BufferedImage(model.screenWidth, model.screenHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public void setGameController(GameController gc) {
        this.gameController = gc;
    }

    public void setFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Clear reusable images
        Graphics2D lightG2 = lightOverlay.createGraphics();
        lightG2.setComposite(AlphaComposite.Clear);
        lightG2.fillRect(0, 0, lightOverlay.getWidth(), lightOverlay.getHeight());
        lightG2.dispose();

        Graphics2D mergedG2 = mergedLights.createGraphics();
        mergedG2.setComposite(AlphaComposite.Clear);
        mergedG2.fillRect(0, 0, mergedLights.getWidth(), mergedLights.getHeight());
        mergedG2.dispose();

        // Calculate the current size of the minimap
        int currentWidth = getWidth() - 2 * borderThickness;
        int currentHeight = getHeight() - 2 * borderThickness;

        // Calculate the scale factor to fit the entire game world within the minimap
        double scaleX = (double) currentWidth / model.worldWidth;
        double scaleY = (double) currentHeight / model.worldHeight;
        double scale = Math.min(scaleX, scaleY);

        // Calculate the offset to center the content in fullscreen mode
        int offsetX = 0;
        int offsetY = 0;
        if (isFullscreen) {
            offsetX = (getWidth() - (int) (model.worldWidth * scale)) / 2;
            offsetY = (getHeight() - (int) (model.worldHeight * scale)) / 2;
        }

        float darknessFactor = gameController != null ? gameController.getDarknessFactor() : 0.0f;

        // Draw all tiles
        int[][] tiles = model.tiles;
        int scaledTileSize = (int) Math.round(model.tileSize * scale);
        
        for (int row = 0; row < model.maxWorldRow; row++) {
            for (int col = 0; col < model.maxWorldCol; col++) {
                int tileX = borderThickness + offsetX + col * scaledTileSize;
                int tileY = borderThickness + offsetY + row * scaledTileSize;


                if (tiles[row][col] == 5) {
                    g2.drawImage(model.tileTypes[tiles[row][col]].getIcon(), tileX, tileY, scaledTileSize, scaledTileSize, null);
                } else {
                    g2.drawImage(model.tileTypes[0].getIcon(), tileX, tileY, scaledTileSize, scaledTileSize, null);
                    g2.drawImage(model.tileTypes[tiles[row][col]].getIcon(), tileX, tileY, scaledTileSize, scaledTileSize, null);
                }
            }
        }
        // Apply base darkness overlay first
        if (darknessFactor > 0.4f) {
            // Create base darkness overlay
            g2.setColor(new Color(0, 0, 0, Math.min(215, (int)(darknessFactor * 215))));
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Optimize light effects by reusing cached images
            lightG2 = lightOverlay.createGraphics();
            mergedG2 = mergedLights.createGraphics();
            mergedG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            
            // Draw all light sources to the merged lights image
            for (int row = 0; row < model.maxWorldRow; row++) {
                for (int col = 0; col < model.maxWorldCol; col++) {
                    if (isTilePlacedOrRoad(row, col)) {
                        int tileX = borderThickness + offsetX + col * scaledTileSize + scaledTileSize/2;
                        int tileY = borderThickness + offsetY + row * scaledTileSize + scaledTileSize/2;
                        
                        int tileType = model.tiles[row][col];
                        int lightRadius = (tileType == 3 || tileType == 4) ? 3 * scaledTileSize : 2 * scaledTileSize;
                        
                        // Create a radial gradient for the light
                        RadialGradientPaint gradient = new RadialGradientPaint(
                            tileX, tileY, lightRadius,
                            new float[] {0.0f, 0.5f, 1.0f},
                            new Color[] {
                                new Color(255, 255, 200, 180),  // center
                                new Color(255, 255, 200, 80),   // middle
                                new Color(255, 255, 200, 0)     // edge
                            }
                        );
                        
                        mergedG2.setPaint(gradient);
                        mergedG2.fillOval(tileX - lightRadius, tileY - lightRadius, lightRadius * 2, lightRadius * 2);
                    }
                }
            }
            
            // Apply the merged lights to the light overlay
            lightG2.setComposite(AlphaComposite.DstOut);
            lightG2.drawImage(mergedLights, 0, 0, null);
            
            // Draw light circles for rangers and jeeps
            for (Ranger ranger : model.getRangers()) {
                Point coordinate = ranger.getCoordinate();
                int elementCol = (int) (coordinate.x / model.tileSize);
                int elementRow = (int) (coordinate.y / model.tileSize);
                int elementX = borderThickness + offsetX + elementCol * scaledTileSize + scaledTileSize/2;
                int elementY = borderThickness + offsetY + elementRow * scaledTileSize + scaledTileSize/2;
                
                int lightRadius = 3 * scaledTileSize;
                lightG2.fillOval(elementX - lightRadius, elementY - lightRadius, lightRadius * 2, lightRadius * 2);
            }
            
            for (Jeep jeep : model.getJeeps()) {
                Point coordinate = jeep.getCoordinate();
                int elementCol = (int) (coordinate.x / model.tileSize);
                int elementRow = (int) (coordinate.y / model.tileSize);
                int elementX = borderThickness + offsetX + elementCol * scaledTileSize + scaledTileSize/2;
                int elementY = borderThickness + offsetY + elementRow * scaledTileSize + scaledTileSize/2;
                
                int lightRadius = 4 * scaledTileSize;
                lightG2.fillOval(elementX - lightRadius, elementY - lightRadius, lightRadius * 2, lightRadius * 2);
            }
            
            mergedG2.dispose();
            lightG2.dispose();
            g2.drawImage(lightOverlay, 0, 0, null);
            
            // Add subtle glow effects
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2.drawImage(mergedLights, 0, 0, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // Draw the entities based on visibility rules
        drawEntities(g2, offsetX, offsetY, scaledTileSize, darknessFactor);

        // Draw the visible area rectangle
        drawVisibleAreaRectangle(g2, scale);

        // Draw the minimap border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRect(borderThickness / 2, borderThickness / 2, currentWidth + borderThickness, currentHeight + borderThickness);

        g2.dispose();
    }

    private void drawEntities(Graphics2D g2, int offsetX, int offsetY, int scaledTileSize, float darknessFactor) {
        boolean isNight = gameController != null && gameController.isNightTime();
        // Always draw rangers and jeeps
        for (Ranger ranger : model.getRangers()) {
            Point coordinate = ranger.getCoordinate();
            int elementCol = (int) (coordinate.x / model.tileSize);
            int elementRow = (int) (coordinate.y / model.tileSize);
            int elementX = borderThickness + offsetX + elementCol * scaledTileSize + scaledTileSize/2;
            int elementY = borderThickness + offsetY + elementRow * scaledTileSize + scaledTileSize/2;
            
            // During night/transitions, create a visibility circle around rangers
            if (darknessFactor > 0.5f) {
                // Draw light circle
                g2.setColor(new Color(255, 255, 200, 35));
                int lightRadius = 3 * scaledTileSize;
                g2.fillOval(elementX - lightRadius, elementY - lightRadius, lightRadius * 2, lightRadius * 2);
                
                // Draw smaller, brighter inner circle
                g2.setColor(new Color(255, 255, 200, 50));
                g2.fillOval(elementX - lightRadius/2, elementY - lightRadius/2, lightRadius, lightRadius);
            }
            
            g2.drawImage(ranger.getIcon(), elementX - scaledTileSize/2, elementY - scaledTileSize/2, scaledTileSize, scaledTileSize, null);
        }
        
        for (Jeep jeep : model.getJeeps()) {
            Point coordinate = jeep.getCoordinate();
            int elementCol = (int) (coordinate.x / model.tileSize);
            int elementRow = (int) (coordinate.y / model.tileSize);
            int elementX = borderThickness + offsetX + elementCol * scaledTileSize;
            int elementY = borderThickness + offsetY + elementRow * scaledTileSize;
            
            // During night/transitions, create light effect for jeeps
            if (darknessFactor > 0.5f) {
                // Draw light circle
                int lightRadius = 4 * scaledTileSize;
                int centerX = elementX + scaledTileSize/2;
                int centerY = elementY + scaledTileSize/2;
                
                g2.setColor(new Color(255, 255, 200, 40));
                g2.fillOval(centerX - lightRadius, centerY - lightRadius, lightRadius * 2, lightRadius * 2);
                
                // Draw smaller, brighter inner circle
                g2.setColor(new Color(255, 255, 200, 55));
                g2.fillOval(centerX - lightRadius/2, centerY - lightRadius/2, lightRadius, lightRadius);
            }
            
            g2.drawImage(jeep.getIcon(), elementX, elementY, scaledTileSize, scaledTileSize, null);
        }
        
        // Draw animals with night visibility rules
        for (Entity entity : model.getAllEntities()) {
            if (entity instanceof Animal) {
                Animal animal = (Animal)entity;
                Point animalPoint = animal.getCoordinate();
                
                // Calculate visibility based on darkness and proximity
                boolean animalVisible = true;
                float visibilityFactor = 1.0f;
                
                if (darknessFactor > 0.0f) {
                    // Get closest ranger/jeep distance
                    float closestDistance = Float.MAX_VALUE;
                    
                    // Check distance to rangers
                    for (Ranger ranger : model.getRangers()) {
                        float distance = getPointDistance(animalPoint, ranger.getCoordinate());
                        closestDistance = Math.min(closestDistance, distance);
                    }
                    
                    // Check distance to jeeps
                    for (Jeep jeep : model.getJeeps()) {
                        float distance = getPointDistance(animalPoint, jeep.getCoordinate());
                        closestDistance = Math.min(closestDistance, distance);
                    }
                    
                    float maxVisDistance = 5 * model.tileSize;
                    
                    if (closestDistance > maxVisDistance) {
                        visibilityFactor = 1.0f - darknessFactor;
                        if (visibilityFactor < 0.1f) {
                            animalVisible = false;
                        }
                    } else {
                        float distanceFactor = closestDistance / maxVisDistance;
                        visibilityFactor = Math.max(0.2f, 1.0f - (darknessFactor * distanceFactor));
                    }
                }
                
                // Draw the animal if visible
                if (animalVisible) {
                    int elementCol = (int) (animalPoint.x / model.tileSize);
                    int elementRow = (int) (animalPoint.y / model.tileSize);
                    int elementX = borderThickness + offsetX + elementCol * scaledTileSize;
                    int elementY = borderThickness + offsetY + elementRow * scaledTileSize;

                    if (darknessFactor > 0.0f && visibilityFactor < 1.0f) {
                        // Save current composite
                        Composite originalComposite = g2.getComposite();
                        
                        // Apply alpha based on visibility
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, visibilityFactor));
                        g2.drawImage(animal.getIcon(), elementX, elementY, scaledTileSize, scaledTileSize, null);
                        
                        // Restore the original composite
                        g2.setComposite(originalComposite);
                    } else {
                        // Normal drawing in daylight
                        g2.drawImage(animal.getIcon(), elementX, elementY, scaledTileSize, scaledTileSize, null);
                    }
                }
            }
        }

        // Skip drawing visitors at night
        if (!isNight) {
            for (Entity visitor : model.getVisitors()) {
                Point coordinate = visitor.getCoordinate();
                int elementCol = (int) (coordinate.x / model.tileSize);
                int elementRow = (int) (coordinate.y / model.tileSize);
                int elementX = borderThickness + offsetX + elementCol * scaledTileSize;
                int elementY = borderThickness + offsetY + elementRow * scaledTileSize;

                g2.drawImage(visitor.getIcon(), elementX, elementY, scaledTileSize, scaledTileSize, null);
            }
        }

        // Draw items at night
        if (darknessFactor > 0.0f) {
            for (Item item : model.getItems()) {
                if (!(item instanceof Entrance)) {
                    Point itemPoint = item.getCoordinate();
                    int itemCol = (int) (itemPoint.x / model.tileSize);
                    int itemRow = (int) (itemPoint.y / model.tileSize);
                    int itemX = borderThickness + offsetX + itemCol * scaledTileSize;
                    int itemY = borderThickness + offsetY + itemRow * scaledTileSize;

                    Composite originalComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                    g2.drawImage(item.getIcon(), itemX, itemY, scaledTileSize, scaledTileSize, null);
                    g2.setComposite(originalComposite);
                }
            }
        }
    }

    // Helper method to calculate actual distance between points
    private float getPointDistance(Point p1, Point p2) {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    private void drawVisibleAreaRectangle(Graphics2D g2, double scale) {
        if (isFullscreen) {
            return; // Skip drawing the rectangle in fullscreen mode
        }
        // Calculate the top-left corner of the visible area based on the camera's center position
        int visibleX = (int) Math.round((gp.getCam().worldP.x - (double) model.screenWidth / 2) * scale);
        int visibleY = (int) Math.round((gp.getCam().worldP.y - (double) model.screenHeight / 2) * scale);
        int visibleWidth = (int) Math.round(model.screenWidth * scale);
        int visibleHeight = (int) Math.round(model.screenHeight * scale);

        // Define the minimap content area
        int minimapContentWidth = getWidth() - 2 * borderThickness;
        int minimapContentHeight = getHeight() - 2 * borderThickness;

        // Clamp the rectangle to the edges of the minimap content area
        if (visibleX < 0) {
            visibleX = 0;
        }
        if (visibleX + visibleWidth > minimapContentWidth) {
            visibleX = minimapContentWidth - visibleWidth;
        }
        if (visibleY < 0) {
            visibleY = 0;
        }
        if (visibleY + visibleHeight > minimapContentHeight) {
            visibleY = minimapContentHeight - visibleHeight;
        }

        visibleX += borderThickness;
        visibleY += borderThickness;

        // Draw the transformed rectangle
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(visibleX, visibleY, visibleWidth, visibleHeight);
    }
    
    // Check if a tile contains plants, water, or roads
    private boolean isTilePlacedOrRoad(int row, int col) {
        if (row < 0 || col < 0 || row >= model.maxWorldRow || col >= model.maxWorldCol) {
            return false;
        }
        int tileType = model.tiles[row][col];
        return tileType >= 1 && tileType <= 5;
    }
}
