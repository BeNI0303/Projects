package MovingEntity;

import View.GamePanel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "@class"
)
public abstract class Entity implements Drawable {

    protected Point coordinate;
    protected int price;
    @JsonIgnore
    protected transient BufferedImage icon;
    protected String direction;
    protected int actionLockCounter;
    public int speed = 1;

    public Entity(int x, int y, int price, String path) {

        this.actionLockCounter = 0;
        this.coordinate = new Point(x, y);
        this.price = price;
        try {
            this.icon = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.getMessage();
        }
        getActualDirection();
    }

    public Entity() {
    }

    @Override
    public Point getCoordinate() {
        return coordinate;
    }

    @Override
    public BufferedImage getIcon() {
        return icon;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            getActualDirection();
            actionLockCounter = 0;
        }
    }

    public void getActualDirection() {
        Random random = new Random();
        int i = random.nextInt(100) + 1;

        if (i <= 25) {
            direction = "up";
        }
        if (i > 25 && i <= 50) {
            direction = "down";
        }
        if (i > 50 && i <= 75) {
            direction = "left";
        }
        if (i > 75) {
            direction = "right";
        }
    }

    

    public void update(int tileSize) {
        setAction();
        move(tileSize);
    }

    public void move(int tileSize) {
        if (direction == "down") {
            coordinate.y += speed;
        } else if (direction == "up") {
            coordinate.y -= speed;
        } else if (direction == "left") {
            coordinate.x -= speed;
        } else if (direction == "right") {
            coordinate.x += speed;
        }
    }
    public void moveToSomething(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
                coordinate.x += speed;
            } else if (dx < 0) {
                direction = "left";
                coordinate.x -= speed;
            }
        } else {
            if (dy > 0) {
                direction = "down";
                coordinate.y += speed;
            } else if (dy < 0) {
                direction = "up";
                coordinate.y -= speed;
            }
        }

    }
    public void setDirectionAfterCollide() {
        if (direction.equals("up")) {
            direction = "down";
        } else if (direction.equals("down")) {
            direction = "up";
        } else if (direction.equals("left")) {
            direction = "right";
        } else if (direction.equals("right")) {
            direction = "left";
        }
    }
    public void defaultMove() {
        if ("down".equals(direction)) {
            coordinate.y += speed;
        } else if ("up".equals(direction)) {
            coordinate.y -= speed;
        } else if ("left".equals(direction)) {
            coordinate.x -= speed;
        } else if ("right".equals(direction)) {
            coordinate.x += speed;
        }
    }

    @Override
    public String toString() {
        return "Entity at coordinates: " + coordinate;
    }

    public abstract boolean collide(int x, int y, int w, int h, int tileSize);

    public abstract void interact(Entity e);

    public abstract boolean isActive();

    public abstract void draw(Graphics2D g2);

    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize) {
        return null;
    }

    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize) {
        return null;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    
}
