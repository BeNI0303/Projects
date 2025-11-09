package MovingEntity;

public class Giraffe extends Herbivore {

    public static final int price = 100;
    public static final String path = "/giraffe.png";
    public static final int id = 101;
    public Giraffe(int x, int y, int age, int foodSupply, int waterSupply) {
        super(x, y, price, age, foodSupply, waterSupply,path,id);
    }
    
    public Giraffe() {
        super();
    }
    
    @Override
    public Animal cloneAnimal () {
        return new Giraffe(this.coordinate.x, this.coordinate.y, age, foodSupply, waterSupply);
    }
    
    @Override
    public boolean isMature() {
        //SWITCH MAGIC NUMBER TO CONSTANT
        return age > 3;
    }
}
