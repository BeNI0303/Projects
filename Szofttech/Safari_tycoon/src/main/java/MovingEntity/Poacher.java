package MovingEntity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Poacher extends Person {
    
    private Animal target;
    private boolean activeAtNight;
    public static final String path = "/poacher.png";
    
    public Poacher(int x, int y, int price, int range, int time) {
        super(x, y, price, range, time, path);
        this.target = null;
        // Poachers are active both day and night
        this.activeAtNight = true;
        this.direction = "up";
    }
    
    
    public Poacher() {
        super();
    }
    
    public Person clonePerson() {
        return new Poacher(this.coordinate.x, this.coordinate.y, price, range, time);
    }
    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x + personSizeScale / 2;
        data[1] = y + personSizeScale / 2;
        data[2] = tileSize - personSizeScale / 2;
        data[3] = tileSize - personSizeScale / 2;
        return data;
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x + personSizeScale / 2;
        data[1] = y + tileSize / 2 + personSizeScale;
        data[2] = tileSize - personSizeScale / 2;
        data[3] = tileSize / 2 - personSizeScale;
        return data;
    }
    @Override
    public int[] getRangeBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x + personSizeScale - range;
        data[1] = y + tileSize / 2 + personSizeScale- range;
        data[2] = tileSize - personSizeScale + range*2;
        data[3] = tileSize / 2 - personSizeScale + range*2;
        return data;
    }

    @Override
    public void interact(Entity e) {
    
    }
    
    public boolean seeAnimal(int x, int y, int w, int h, int tileSize) {
        int[] paramsThis = getRangeBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        Ellipse2D poacher = new Ellipse2D.Double(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle obj = new Rectangle(x, y, w, h);
        Area a1 = new Area(poacher);
        Area a2 = new Area(obj);
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
    
    }
    
    public boolean isActiveAtNight() {
        return activeAtNight;
    }
}
