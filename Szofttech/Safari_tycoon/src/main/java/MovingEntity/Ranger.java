package MovingEntity;

import Model.Model;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Ranger extends Person {
    
    public static final int price = 800;
    public static final String path = "/ranger.png";
    public static final int spawnX = 1181;
    public static final int spawnY = 2327;
    private Animal target;
    private transient boolean activeAtNight;
    private transient boolean isSelected;
    protected int ageBuffer = 500;
    protected int avoidBuffer = 0;
    protected boolean canGo = true;

    public Ranger(int x, int y, int range, int time) {
        super(x, y, price, range, time, path);
        this.target = null;
        // Rangers are active both day and night
        this.activeAtNight = true;
        this.isSelected = false;
    }
    
    public Ranger() {
        super();
    }
    
    public Person clonePerson() {
        return new Ranger(coordinate.x, coordinate.y, price, time);
    }

    public Animal getTarget() {
        return target;
    }

    public boolean isIsSelected() {
        return isSelected;
    }
    @Override
    public void update(int tileSize) {
        if (!canGo || !isSelected) {
            setAction();
            move(tileSize);
           
        } else {
            updatePosition(tileSize);
        }
        if (avoidBuffer > 0) {
            avoidBuffer--;
        }
        if (avoidBuffer <= 0) {
            canGo = true;
        }
        
    }
    public void noItemOnTheWay(){
        avoidBuffer = 50;
        canGo = false;
    }
    
    //MOVES THE RANGER TOWARDS THE TARGET ANIMAL
    public void updatePosition(int tileSize) {
        if (target == null) {
            //getActualDirection();
            //move(tileSize);
            return;
        }
        //System.out.println("Ranger speed is: "+ speed);
        double deltaX = target.getCoordinate().x - coordinate.x;
        double deltaY = target.getCoordinate().y - coordinate.y;

        moveToSomething(deltaX, deltaY);
        
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        
        if (checkIfCanShootTarget(distance)) {
            this.shootAnimal();
            return;
        }
        //System.out.println("Ranger at " + xPos + " " + yPos );
    }
    
    public boolean seePoacher(int x, int y, int width, int height, int tileSize) {
        int[] paramsThis = getRangeBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        Ellipse2D rangerEllipse = new Ellipse2D.Double(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle poacherHitbox = new Rectangle(x, y, width, height);
        Area rangerArea = new Area(rangerEllipse);
        Area poacherArea = new Area(poacherHitbox);
        rangerArea.intersect(poacherArea);
        return !rangerArea.isEmpty();
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
 
    public void setTarget(Animal target) {
        this.target = target;
    }

    @Override
    public void interact(Entity e) {
        
    }

    @Override
    public void draw(Graphics2D g2) {
        
    }
    
    public boolean checkIfCanShootTarget(double distanceToTarget) {
        if (distanceToTarget < this.range) {
            return true;
        }
        return false;
    }

    public void shootAnimal() {
        this.target.setHp(0);
        Model.incraseBalance(target.getPrice());
        this.isSelected = false;
        this.target = null;
    }
    public void shootPoacher(Poacher target) {
        target.setHp(0);
    }
    
    public boolean isActiveAtNight() {
        return activeAtNight;
    }

    @Override
    public String toString() {
        return "Ranger{" + "target=" + target + ", activeAtNight=" + activeAtNight + ", isSelected=" + isSelected + '}';
    }
}
