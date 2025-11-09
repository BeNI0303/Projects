package MovingEntity;

import Items.Bush;
import Items.Grass;
import Items.Item;
import Items.Lake;
import Items.Tree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Lion.class, name = "Lion"),
    @JsonSubTypes.Type(value = Cheetah.class, name = "Cheetah"),
    @JsonSubTypes.Type(value = Antelope.class, name = "Antelope"),
    @JsonSubTypes.Type(value = Zebra.class, name = "Zebra"),
    @JsonSubTypes.Type(value = Giraffe.class, name = "Giraffe")
})
public class Animal extends Entity{

    protected int age;
    protected int foodSupply;
    protected int waterSupply;
    protected ArrayList<Drawable> foodSources = new ArrayList<>();
    protected ArrayList<Item> drinkSources = new ArrayList<>();
    protected int range = 50;
    protected int id;
    
    @JsonIgnore
    protected String path;
    protected boolean isPackLeader;
    protected Animal leader;
    protected int hungerBuffer = 50;
    protected int thirstBuffer = 50;
    protected int ageBuffer = 500;
    protected int avoidBuffer = 0;
    protected boolean canGo = true;
    public final int animalSizeScale = 10;
    public final int hunger = 40;
    public final int thirst = 60;
    public final int fullFood = 200;
    public final int fullWater = 100;
    protected int hp = 1;

    public Animal() {
        this.isPackLeader = false;
        this.leader = null;
        this.age = 0;
        this.foodSupply = 0;
        this.waterSupply = 0;
        this.range = 0;
        this.path = "";
    }

    public Animal(int x, int y, int price, int age, int foodSupply, int waterSupply, String path, int id) {
        super(x, y, price, path);
        this.age = age;
        this.foodSupply = foodSupply;
        this.waterSupply = waterSupply;
        this.path = path;
        this.id = id;
    }

    @Override
    public boolean collide(int x, int y, int w, int h, int tileSize) {
        int[] paramsThis = getHitBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        Rectangle animal = new Rectangle(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle item = new Rectangle(x, y, w, h);
        return animal.intersects(item);
    }

    

    @Override
    public void update(int tileSize) {
        starvation();
        thirst();
        aging();
        if (isPackLeader) {
            setAction();
            move(tileSize);
        } else {
            move(tileSize);
        }
        if(avoidBuffer >0){
            avoidBuffer--;
        }
        if(avoidBuffer <= 0){
            canGo = true;
        }
    }

    @Override
    public void move(int tileSize) {
        if (foodSupply < hunger && !foodSources.isEmpty() && canGo) {
            foodSources.sort(Comparator.comparingDouble(i -> this.coordinate.distance(i.getCoordinate())));
            if (isAvailable(foodSources.getFirst(), tileSize)) {
                Point food = foodSources.getFirst().getCoordinate();
                int[] foodParams = foodSources.getFirst().getHitBoxParams(food.x, food.y, tileSize);
                double dx = foodParams[0] - getHitBoxParams(coordinate.x, coordinate.y, tileSize)[0];
                double dy = foodParams[1] - getHitBoxParams(coordinate.x, coordinate.y, tileSize)[1];
                moveToSomething(dx, dy);
                if (this.collide(foodParams[0], foodParams[1], foodParams[2], foodParams[3], tileSize)) {
                    this.eat(foodSources.getFirst());
                }
            } else {
                foodSources.removeFirst();
            }
        } else if (waterSupply < thirst && !drinkSources.isEmpty() && canGo) {
            drinkSources.sort(Comparator.comparingDouble(i -> this.coordinate.distance(i.getCoordinate())));
            if (isAvailable(drinkSources.getFirst(), tileSize)) {
                Point water = drinkSources.getFirst().getCoordinate();
                int[] drinkParam = drinkSources.getFirst().getHitBoxParams(water.x, water.y, tileSize);
                double dx = drinkParam[0] - getHitBoxParams(coordinate.x, coordinate.y, tileSize)[0];
                double dy = drinkParam[1] - getHitBoxParams(coordinate.x, coordinate.y, tileSize)[1];
                moveToSomething(dx, dy);
                if (this.collide(drinkParam[0], drinkParam[1], drinkParam[2], drinkParam[3], tileSize)) {
                    this.drink(drinkSources.getFirst());
                }
            }else{
                drinkSources.removeFirst();
            }
        } else {
            if (isPackLeader) {
                defaultMove();
            } else {
                double dx = leader.coordinate.x - coordinate.x;
                double dy = leader.coordinate.y - coordinate.y;

                if (Math.abs(dx) > tileSize && Math.abs(dy) > tileSize && canGo) {
                    moveToSomething(dx, dy);
                } else {
                    setAction();
                    defaultMove();
                }

            }
        }
    }
    

    public boolean seeFood(int x, int y, int w, int h, int tileSize) {
        int[] paramsThis = getRangeBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        Ellipse2D animal = new Ellipse2D.Double(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Ellipse2D obj = new Ellipse2D.Double(x, y, w, h);
        Area a1 = new Area(animal);
        Area a2 = new Area(obj);
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    

    

    public boolean isAvailable(Drawable i, int tileSize) {
        int[] paramsThis = getRangeBoxParams(this.getCoordinate().x, this.getCoordinate().y, tileSize);
        int[] itemParams = i.getHitBoxParams(i.getCoordinate().x, i.getCoordinate().y, tileSize);
        Ellipse2D animal = new Ellipse2D.Double(paramsThis[0], paramsThis[1], paramsThis[2], paramsThis[3]);
        Rectangle2D obj = new Rectangle2D.Double(itemParams[0], itemParams[1], itemParams[2], itemParams[3]);
        if (animal.intersects(obj)) {
            return i.getHp() > 0;
        }
        return true;
    }

    @Override
    public void interact(Entity e) {

    }

    public Animal cloneAnimal() {
        return new Animal(coordinate.x, coordinate.y, price, age, foodSupply, waterSupply, path, id);
    }

    @Override
    @JsonIgnore
    public boolean isActive() {
        return (foodSupply > 0 && waterSupply > 0);
    }

    @Override
    public void draw(Graphics2D g2) {

    }

    public void eat(Drawable e) {
        if(e instanceof Item i){
            i.decreaseHp((fullFood - foodSupply) / 2);
            foodSupply += (fullFood - foodSupply);
        }else if(e instanceof Animal a){
            a.hp = 0;
            foodSupply = fullFood;
        }
  
    }

    public void drink(Item i) {
        waterSupply = fullWater;
    }
    
    public void starvation(){
        if(hungerBuffer > 0){
            hungerBuffer--;
        }else{
            foodSupply--;
            hungerBuffer = 50;
        }
    }
    
    public void thirst(){
        if(thirstBuffer > 0){
            thirstBuffer--;
        }else{
            waterSupply--;
            thirstBuffer = 50;
        }
    }
    
    public void aging(){
        if(ageBuffer > 0){
            ageBuffer--;
        }else{
            age++;
            ageBuffer=500;
        }
    }
    public void noItemOnTheWay(){
        avoidBuffer = 200;
        canGo = false;
    }

    public void addSource(Drawable s) {
        if (s instanceof Lake l && !drinkSources.contains(s)) {
            drinkSources.add(l);
        } else if (!foodSources.contains(s)) {
            if (s instanceof Bush) {
                foodSources.add(s);
            } else if (s instanceof Grass) {
                foodSources.add(s);
            } else if (s instanceof Tree) {
                foodSources.add(s);
            } else if(s instanceof Herbivore){
                foodSources.add(s);
            }
        }
    }

    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - tileSize / 2;
        data[1] = y;
        data[2] = tileSize - animalSizeScale;
        data[3] = tileSize / 2 - animalSizeScale;
        return data;
    }

    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - tileSize / 2;
        data[1] = y - tileSize / 2;
        data[2] = tileSize - animalSizeScale;
        data[3] = tileSize - animalSizeScale;
        return data;
    }

    @Override
    public int[] getRangeBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - tileSize / 2 - range;
        data[1] = y - 50;
        data[2] = tileSize - animalSizeScale + range * 2;
        data[3] = tileSize / 2 - animalSizeScale + range * 2;
        return data;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public boolean isMature() {
        return false;
    }

    public boolean isCanGo() {
        return canGo;
    }

    
    public int getAge() {
        return age;
    }

    public int getFoodSupply() {
        return foodSupply;
    }

    public int getWaterSupply() {
        return waterSupply;
    }

    public ArrayList<Drawable> getFoodSources() {
        return foodSources;
    }

    public ArrayList<Item> getDrinkSources() {
        return drinkSources;
    }

    public int getRange() {
        return range;
    }

    @Override
    public Point getCoordinate() {
        return coordinate;
    }

    public int getPrice() {
        return price;
    }
    
    @JsonIgnore
    public BufferedImage getIcon() {
        return icon;
    }

    public String getDirection() {
        return direction;
    }

    public int getActionLockCounter() {
        return actionLockCounter;
    }

    public String getPath() {
        return path;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFoodSupply(int foodSupply) {
        this.foodSupply = foodSupply;
    }

    public void setWaterSupply(int waterSupply) {
        this.waterSupply = waterSupply;
    }

    public void setFoodSources(ArrayList<Drawable> foodSources) {
        this.foodSources = foodSources;
    }

    public void setDrinkSources(ArrayList<Item> drinkSources) {
        this.drinkSources = drinkSources;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setToLeader() {
        isPackLeader = true;
    }

    public void setLeader(Animal a) {
        leader = a;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setActionLockCounter(int actionLockCounter) {
        this.actionLockCounter = actionLockCounter;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
        
    @Override
    public int getHp() {
        return hp;
    }

}
