package MovingEntity;

import View.GamePanel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Person extends Entity {
    
    protected int range;
    protected int time;
    protected boolean activeAtNight;
    public final int personSizeScale = 10;
    @JsonIgnore
    protected String path;
    protected double xPos;
    protected double yPos;
    protected int hp = 1;
    
    public Person() {
        this.range = 0;
        this.time = 0;
        this.path = "";
    }

    public Person(int x, int y, int price, int range, int time, String path) {
        super(x, y, price, path);
        this.xPos = x;
        this.yPos = y;
        this.range = range;
        this.time = time;
        this.path = path;
        this.activeAtNight = false; // By default, people are not active at night
    }

    public Person clonePerson(){
        return new Person(coordinate.x, coordinate.y, price, range, time, path);
    }
    
    @Override
    public boolean collide(int x, int y, int w, int h,int tileSize) {
        int[] paramsThis = getHitBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        Rectangle person = new Rectangle(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle obj = new Rectangle(x, y, w, h);
        return person.intersects(obj);
    }

    @Override
    public void interact(Entity e) {
        
    }
    
    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x - tileSize / 2;
        data[1] = y - tileSize / 2;
        data[2] = tileSize - personSizeScale;
        data[3] = tileSize - personSizeScale;
        return data;
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x - tileSize / 2;
        data[1] = y;
        data[2] = tileSize - personSizeScale;
        data[3] = tileSize / 2 - personSizeScale;
        return data;
    }
    
    @Override
    @JsonIgnore
    public boolean isActive() {
        return true;
    }
    
    public boolean isActiveAtNight() {
        return activeAtNight;
    }
    
    public void setActiveAtNight(boolean activeAtNight) {
        this.activeAtNight = activeAtNight;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawString("Person", this.getCoordinate().x, this.getCoordinate().y);
    }

    public void move(int dx, int dy) {
        Point newCoordinate = (new Point(
            this.getCoordinate().x + dx,
            this.getCoordinate().y + dy));
        
        this.setCoordinate(newCoordinate);
    }
    
    public String getPath() {
        return path;
    }

    @Override
    public int[] getRangeBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - tileSize / 2 - 100;
        data[1] = y - 100;
        data[2] = tileSize - personSizeScale + 200;
        data[3] = tileSize / 2 - personSizeScale + 200;
        return data;
    }

    public int getTime() {
        return time;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    
    @Override
    @JsonIgnore
    public int getHp() {
        return hp;
    }
}
