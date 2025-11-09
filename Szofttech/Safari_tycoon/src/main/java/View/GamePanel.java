package View;

import Controller.GameController;
import Controller.KeyHandler;
import Controller.MouseHandler;
import Items.Item;
import Model.Model;
import MovingEntity.Animal;
import MovingEntity.Camera;
import MovingEntity.Drawable;
import MovingEntity.Jeep;
import MovingEntity.Ranger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.AlphaComposite;
import java.awt.RadialGradientPaint;
import Items.Entrance;
import MovingEntity.Poacher;
import MovingEntity.Visitor;

public class GamePanel extends JPanel {

    private Model model;
    private KeyHandler keyH;
    private Camera cam;
    private MouseHandler mouseH;
    private JLabel balanceLabel;
    private GameWindow gameWindow;
    private GameController gameController;

    // Cache frequently used values and reuse objects
    private BufferedImage lightOverlay;
    private BufferedImage mergedLights;

    public GamePanel(MouseHandler mh, KeyHandler kh, Model m, JLabel label, String mapFile) {
        this.model = m;
        this.mouseH = mh;
        this.keyH = kh;
        this.cam = new Camera(model, keyH);
        this.balanceLabel = label;
        this.balanceLabel.setText(String.format("Balance: %d", model.getBalance()));
        this.balanceLabel.setHorizontalAlignment(JLabel.CENTER);
        this.setPreferredSize(new Dimension(model.screenWidth, model.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
        this.lightOverlay = new BufferedImage(m.screenWidth, m.screenHeight, BufferedImage.TYPE_INT_ARGB);
        this.mergedLights = new BufferedImage(m.screenWidth, m.screenHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public void setGameController(GameController gc) {
        this.gameController = gc;
    }

    @Override
    public void paintComponent(Graphics g) {
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

        drawModel(g2, model.getAnimals(), model.getJeeps(), model.getRangers(), model.getItems(), model.getVisitors(),model.getPoacher());
        cam.draw(g2);

        g2.dispose();
    }

    /**
     * Drawing the model to the screen
     *
     * @param g2
     * @param animals the animals on the board to draw
     * @param jeeps the jeeps on the board to draw
     * @param rangers the rangers on the board to draw
     */
    public void drawModel(Graphics2D g2, ArrayList<Animal> animals, ArrayList<Jeep> jeeps, ArrayList<Ranger> rangers, ArrayList<Item> items, ArrayList<Visitor> visitors, Poacher p) {
        ArrayList<Drawable> drawMovings = new ArrayList<>();
        int col = 0;
        int row = 0;
        boolean isNight = gameController != null && gameController.isNightTime();
        float darknessFactor = gameController != null ? gameController.getDarknessFactor() : 0.0f;

        // Draw all tiles
        while (col < model.maxWorldCol && row < model.maxWorldRow) {
            int worldX = col * model.tileSize;
            int worldY = row * model.tileSize;
            int[] sc = getScreenPosition(worldX, worldY);

            if (sc[0] + model.tileSize < 0 || sc[1] + model.tileSize < 0 || sc[0] > getWidth() || sc[1] > getHeight()) {
                col++;
                if (col == model.maxWorldCol) {
                    col = 0;
                    row++;
                }
                continue; // Skip off-screen tiles
            }

            if (model.tiles[row][col] == 5 || model.tiles[row][col] == 6 || model.tiles[row][col] == 7) {
                g2.drawImage(model.tileTypes[model.tiles[row][col]].getIcon(), sc[0], sc[1], model.tileSize, model.tileSize, null);
            } else {
                g2.drawImage(model.tileTypes[0].getIcon(), sc[0], sc[1], model.tileSize, model.tileSize, null);
            }

            if (sc[0] <= mouseH.meX && sc[0] + model.tileSize >= mouseH.meX && sc[1] <= mouseH.meY && sc[1] + model.tileSize >= mouseH.meY) {
                g2.setColor(Color.BLACK);
                g2.drawRect(sc[0]+1, sc[1]+1, model.tileSize - 2, model.tileSize - 2);
            }

            col++;
            if (col == model.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Apply night effects if needed
        if (darknessFactor > 0.4f) {
            Graphics2D lightG2 = lightOverlay.createGraphics();
            Graphics2D mergedG2 = mergedLights.createGraphics();

            // Create base darkness overlay
            lightG2.setColor(new Color(0, 0, 0, Math.min(200, (int)(darknessFactor * 200))));
            lightG2.fillRect(0, 0, getWidth(), getHeight());
            
            // Fill with semi-transparent darkness
            lightG2.setColor(new Color(0, 0, 0, Math.min(170, (int)(darknessFactor * 170))));
            lightG2.fillRect(0, 0, getWidth(), getHeight());
            
            mergedG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            
            // Draw all light sources to the merged lights image
            col = 0;
            row = 0;
            while (col < model.maxWorldCol && row < model.maxWorldRow) {
                if (isTilePlacedOrRoad(row, col)) {
                    int worldX = col * model.tileSize + model.tileSize / 2;
                    int worldY = row * model.tileSize + model.tileSize / 2;
                    int[] sc = getScreenPosition(worldX, worldY);
                    
                    int tileType = model.tiles[row][col];
                    int lightRadius = (tileType == 3 || tileType == 4) ? 3 * model.tileSize : 2 * model.tileSize;
                    
                    // Create a radial gradient for the light
                    RadialGradientPaint gradient = new RadialGradientPaint(
                        sc[0], sc[1], lightRadius,
                        new float[] {0.0f, 0.5f, 1.0f},
                        new Color[] {
                            new Color(255, 255, 200, 180),  // center
                            new Color(255, 255, 200, 80),   // middle
                            new Color(255, 255, 200, 0)     // edge
                        }
                    );
                    
                    mergedG2.setPaint(gradient);
                    mergedG2.fillOval(sc[0] - lightRadius, sc[1] - lightRadius, lightRadius * 2, lightRadius * 2);
                }
                
                col++;
                if (col == model.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            
            // Apply the merged lights to the light overlay
            lightG2.setComposite(AlphaComposite.DstOut);
            lightG2.drawImage(mergedLights, 0, 0, null);
            
            // Draw light circles for rangers and jeeps
            for (Ranger ranger : rangers) {
                int[] sc = getScreenPosition(ranger.getCoordinate().x, ranger.getCoordinate().y);
                int[] hitBoxParams = ranger.getHitBoxParams(sc[0], sc[1], model.tileSize);
                int centerX = hitBoxParams[0] + hitBoxParams[2] / 2;
                int centerY = hitBoxParams[1] + hitBoxParams[3] / 2;
                int lightRadius = 3 * model.tileSize;
                if (darknessFactor > 0.5f) {
                    g2.setColor(new Color(255, 255, 200, 35));
                    g2.fillOval(centerX - lightRadius, centerY - lightRadius, lightRadius * 2, lightRadius * 2);

                    g2.setColor(new Color(255, 255, 200, 50));
                    g2.fillOval(centerX - lightRadius / 2, centerY - lightRadius / 2, lightRadius, lightRadius);
                }
                lightG2.fillOval(centerX - lightRadius, centerY - lightRadius, lightRadius * 2, lightRadius * 2);
            }
            
            for (Jeep jeep : jeeps) {
                int[] sc = getScreenPosition(jeep.getCoordinate().x, jeep.getCoordinate().y);
                int lightRadius = 4 * model.tileSize;
                int centerX = sc[0] + model.tileSize/2;
                int centerY = sc[1] + model.tileSize/2;
                // During night/transitions, create headlight effect for jeeps
                if (darknessFactor > 0.5f) {
                    // Draw light circle
                    
                    
                    g2.setColor(new Color(255, 255, 200, 40));
                    g2.fillOval(centerX - lightRadius, centerY - lightRadius, lightRadius * 2, lightRadius * 2);
                    
                    // Draw smaller, brighter inner circle
                    g2.setColor(new Color(255, 255, 200, 55));
                    g2.fillOval(centerX - lightRadius/2, centerY - lightRadius/2, lightRadius, lightRadius);
                }
                lightG2.fillOval(centerX - lightRadius, centerY - lightRadius, lightRadius * 2, lightRadius * 2);
            }
            
            mergedG2.dispose();
            lightG2.dispose();
            g2.drawImage(lightOverlay, 0, 0, null);
            
            // Add subtle glow effects
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2.drawImage(mergedLights, 0, 0, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // Add all entities to draw list
        for (Item item : items) {
            drawMovings.add(item);
        }
        for (Animal animal : animals) {
            drawMovings.add(animal);
        }
        for (Jeep jeep : jeeps) {
            drawMovings.add(jeep);
        }
        for (Ranger ranger : rangers) {
            drawMovings.add(ranger);
        }
        for (Visitor visitor : visitors) {
            drawMovings.add(visitor);
        }
        if(p != null){
           drawMovings.add(p); 
        }
        

        // Sort by hitbox for proper draw ordering
        drawMovings.sort(Comparator.comparingInt(d -> (d.getHitBoxParams(d.getCoordinate().x, d.getCoordinate().y, model.tileSize)[1] + 
                                                      d.getHitBoxParams(d.getCoordinate().x, d.getCoordinate().y, model.tileSize)[3])));
        
        // Draw all entities with night visibility rules
        for (Drawable d : drawMovings) {
            int[] sc = getScreenPosition(d.getCoordinate().x, d.getCoordinate().y);
            int[] params = d.getHitBoxParams(sc[0], sc[1], model.tileSize);
            
            // Draw hitboxes if enabled
            if (keyH.hitBoxesOn) {
                if(d instanceof Visitor v && v.isInJeep()){
                }else{
                    g2.setColor(Color.BLACK);
                    g2.drawRect(params[0], params[1], params[2], params[3]);
                    params = d.getRangeBoxParams(sc[0], sc[1], model.tileSize);
                    g2.setColor(Color.GREEN);
                    g2.drawOval(params[0], params[1], params[2], params[3]);
                }
            }
            
            // Apply night visibility rules for animals
            if (d instanceof Animal animal) {
                boolean animalVisible = true;
                float visibilityFactor = 1.0f;
                
                if (darknessFactor > 0.0f) {
                    float closestDistance = Float.MAX_VALUE;
                    
                    // Check distance to rangers
                    for (Ranger ranger : rangers) {
                        float distance = getPointDistance(animal.getCoordinate(), ranger.getCoordinate());
                        closestDistance = Math.min(closestDistance, distance);
                    }
                    
                    // Check distance to jeeps
                    for (Jeep jeep : jeeps) {
                        float distance = getPointDistance(animal.getCoordinate(), jeep.getCoordinate());
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
                
                if (animalVisible) {
                    if (darknessFactor > 0.0f && visibilityFactor < 1.0f) {
                        Composite originalComposite = g2.getComposite();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, visibilityFactor));
                        params = d.getDrawImageParams(sc[0], sc[1], model.tileSize);
                        g2.drawImage(d.getIcon(), params[0], params[1], params[2], params[3], null);
                        g2.setComposite(originalComposite);
                    } else {
                        params = d.getDrawImageParams(sc[0], sc[1], model.tileSize);
                        g2.drawImage(d.getIcon(), params[0], params[1], params[2], params[3], null);
                    }
                }
            } else {
                // Draw non-animal entities normally
                if (!(d instanceof Entrance) && !(d instanceof Visitor)) {
                    params = d.getDrawImageParams(sc[0], sc[1], model.tileSize);
                    g2.drawImage(d.getIcon(), params[0], params[1], params[2], params[3], null);
                }
                if(d instanceof Visitor v && !isNight){
                    if(!v.isInJeep()){
                        params = v.getDrawImageParams(sc[0], sc[1], model.tileSize);
                        g2.drawImage(v.getIcon(), params[0], params[1], params[2], params[3], null);
                    }
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

    // Check if a tile contains plants, water, or roads
    private boolean isTilePlacedOrRoad(int row, int col) {
        if (row < 0 || col < 0 || row >= model.maxWorldRow || col >= model.maxWorldCol) {
            return false;
        }
        int tileType = model.tiles[row][col];
        return tileType >= 1 && tileType <= 5;
    }
    
    //Getting the position on the screen from the word position
    public int[] getScreenPosition(int x, int y) {
        int[] co = {x - cam.worldP.x + cam.screenP.x, y - cam.worldP.y + cam.screenP.y};
        return co;
    }

    public Model getModel() {
        return model;
    }

    public MouseHandler getMouseH() {
        return mouseH;
    }

    public Camera getCam() {
        return cam;
    }

    /**
     * Sets the GameWindow for this panel
     * @param gameWindow the GameWindow that contains this panel
     */
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
    
    /**
     * Gets the GameWindow for this panel
     * @return the GameWindow that contains this panel
     */
    public GameWindow getGameWindow() {
        return this.gameWindow;
    }

}
