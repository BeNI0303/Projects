package MovingEntity;

import Model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Jeep implements Drawable{
    
    public static final int price = 500;
    public static final String path = "/jeep.png";
    public static final int spawnX = 912;
    public static final int spawnY = 2304;
    public static final int rental = 50;
    
    private Point coordinate;
    private int range;
    private ArrayList<Visitor> visitors;
    private int iteratorForRoad;
    private boolean unlocked = false;
    private int reachedTileCount = 0;
    
    @JsonIgnore
    private transient BufferedImage icon;   //problem with serialization!!!

    public Jeep(Point coordinate, int range, int iter) {
        this.coordinate = coordinate;
        this.range = range;
        this.iteratorForRoad = iter;
        visitors = new ArrayList<Visitor>();
        try {
            this.icon = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public Jeep(){
        super();
    }
    
    public Jeep copyJeep() {
        return new Jeep(coordinate, range, iteratorForRoad);
    }
    
    public boolean move(Point toGo){
        if(toGo.x > coordinate.x) coordinate.x++;
        if(toGo.y > coordinate.y) coordinate.y++;
        if(toGo.x < coordinate.x) coordinate.x--;
        if(toGo.y < coordinate.y) coordinate.y--;
        if(coordinate.equals(toGo)){
            iteratorForRoad++;
            return true;
        }
        return false;
    }
    public boolean collide(int x, int y) {
        return false;
    }
    @Override
    public Point getCoordinate() {
        return coordinate;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void draw(Graphics2D g2) {
    
    }

    public int getIteratorForRoad() {
        return iteratorForRoad;
    }

    public void setIteratorForRoad(int iteratorForRoad) {
        this.iteratorForRoad = iteratorForRoad;
    }
    
    public boolean addVisitor(Visitor v){
        if(visitors.size() < 4){
            visitors.add(v);
            v.setInJeep(true);
            return true;
        }
        return false;
    }
    
    

    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x;
        data[1] = y + tileSize / 2;
        data[2] = tileSize;
        data[3] = tileSize / 2;
        return data;
    }
    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x;
        data[1] = y;
        data[2] = tileSize;
        data[3] = tileSize;
        return data;
    }

    @Override
    public int[] getRangeBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - range;
        data[1] = y - range;
        data[2] = tileSize + range*2;
        data[3] = tileSize + range*2;
        return data;
    }
    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }
    public int getReachedTileCount() {
        return reachedTileCount;
    }

    public void incrementReachedTileCount() {
        this.reachedTileCount++;
        if (reachedTileCount >= 2) {
            unlock();
        }
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public void setReachedTileCount(int reachedTileCount) {
        this.reachedTileCount = reachedTileCount;
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public void getIn(Visitor v, int tileSize, Model m){
        int[] jParams = getHitBoxParams(coordinate.x, coordinate.y, tileSize);
        int[] vParams = v.getHitBoxParams(v.getCoordinate().x, v.getCoordinate().y, tileSize);
        Rectangle jeep = new Rectangle(jParams[0],jParams[1],jParams[2],jParams[3]);
        Rectangle visitor = new Rectangle(vParams[0],vParams[1],vParams[2],vParams[3]);
        if(jeep.intersects(visitor)){
            v.setCoordinate(new Point(Visitor.spawnPointX,Visitor.spawnPointY));
            if(addVisitor(v)){
                m.setBalance(m.getBalance()+rental);
            }
        }
    }
    
    public void seeAnimal(Animal a, int tileSize){
        int[] jParams = getRangeBoxParams(coordinate.x, coordinate.y, tileSize);
        int[] vParams = a.getHitBoxParams(a.getCoordinate().x, a.getCoordinate().y, tileSize);
        Rectangle jeep = new Rectangle(jParams[0],jParams[1],jParams[2],jParams[3]);
        Rectangle animal = new Rectangle(vParams[0],vParams[1],vParams[2],vParams[3]);
        if(jeep.intersects(animal)){
            for (Visitor visitor : visitors) {
                visitor.addAnimal(a);
            }
        }
    }
    
    @Override
    @JsonIgnore
    public int getHp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    } 
}
