package MovingEntity;

public class Cheetah extends Predator {

    public static final int price = 200;
    public static final int id = 103;
    public static final String path = "/cheetah.png";
    public Cheetah(int x, int y, int age, int foodSupply, int waterSupply) {
        super(x, y, price, age, foodSupply, waterSupply,path,id);
    }
    
    public Cheetah() {
        super();
    }
    
    @Override
    public Animal cloneAnimal () {
        return new Cheetah(this.coordinate.x, this.coordinate.y, age, foodSupply, waterSupply);
    }
    
    @Override
    public boolean isMature() {
        //SWITCH MAGIC NUMBER TO CONSTANT
        return age > 1;
    }
}
