package MovingEntity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Visitor extends Person {
    
    private int speciesCount;
    private int headCount;
    private boolean activeAtNight;
    public static final String path = "/visitor.png";
    public static final int spawnPointX = 1181;
    public static final int spawnPointY = 2327;
    public static double rate = 1.0;

    private int timeBufferSize = 60;
    private int timeBuffer = timeBufferSize;
    private double forPredator = 0.05;
    private double forHerbivore = 0.01;
    private ArrayList<Animal> seenAnimal = new ArrayList<>();
    private boolean inJeep = false;
    
    public Visitor(int x, int y, int price, int range, int time) {
        super(x, y, price, range, time, path);
    
        this.speciesCount = 0;
        this.headCount = 0;
        // Visitors (tourists) are only active during day time
        this.activeAtNight = false;
    }
    
    public Visitor(){
        super();
    }
    @Override
    public Person clonePerson() {
        Visitor clone = new Visitor(coordinate.x, coordinate.y, price, range, time);
        return clone;
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

    @Override
    public void interact(Entity e) {
    
    }
    
    public void increaseRate(double d){
        if(inJeep){
            rate += d*2;
        }else{
            rate += d;
        }
    }
    
    public void isAvailable(Animal o, int tileSize) {
        int[] paramsThis = getRangeBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        int[] itemParams = o.getHitBoxParams(o.getCoordinate().x, o.getCoordinate().y, tileSize);
        Ellipse2D animal = new Ellipse2D.Double(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle2D obj = new Rectangle2D.Double(itemParams[0], itemParams[1], itemParams[2], itemParams[3]);
        if (animal.intersects(obj)) {
            addAnimal(o);
        }
    }

    public void addAnimal(Animal a){
        if (!seenAnimal.contains((Animal)a)) {
            if(a instanceof Predator){
                increaseRate(forPredator); 
                seenAnimal.add((Animal)a);
            }else if(a instanceof Herbivore){
                increaseRate(forHerbivore);
                seenAnimal.add((Animal)a);
            }
        }
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
    public void decreaseRate(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(100) + 1;
        if(randomNumber <= 80){
            timeBuffer--;
        }
        if(timeBuffer <= 0){
            time--;
            timeBuffer = timeBufferSize;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
    
    }

    public void changeSatisfaction(int satisfaction) {
        
    }
    
    public void update(int tileSize){
        setAction();
        move(tileSize);
    }
    
    public boolean isActiveAtNight() {
        return activeAtNight;
    }

    public boolean isInJeep() {
        return inJeep;
    }

    public void setInJeep(boolean inJeep) {
        this.inJeep = inJeep;
    }

    public static void setRate(double rate) {
        Visitor.rate = rate;
    }
    
    
    
}
