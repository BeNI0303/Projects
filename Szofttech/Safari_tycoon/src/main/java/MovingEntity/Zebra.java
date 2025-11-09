package MovingEntity;

public class Zebra extends Herbivore {
    
    public static final int price = 80;
    public static final int id = 102;
    public static final String path = "/zebra.png";
    public Zebra(int x, int y, int age, int foodSupply, int waterSupply) {
        super(x, y, price, age, foodSupply, waterSupply, path,id);
    }
    
    public Zebra() {
        super();
    }
    
    @Override
    public Animal cloneAnimal () {
        return new Zebra(this.coordinate.x, this.coordinate.y, age, foodSupply, waterSupply);
    }

    @Override
    public boolean isMature() {
        //SWITCH MAGIC NUMBER TO CONSTANT
        return age > 2;
    }
}
