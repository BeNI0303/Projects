package MovingEntity;

public class Lion extends Predator {

    public static final int price = 300;
    public static final int id = 104;
    public static final String path = "/lion.png";
    public Lion(int x, int y, int age, int foodSupply, int waterSupply) {
        super(x, y, price, age, foodSupply, waterSupply,path,id);
    }
    
    public Lion() {
        super();
    }
    
    @Override
    public Animal cloneAnimal () {
        return new Lion(this.coordinate.x, this.coordinate.y, age, foodSupply, waterSupply);
    }
    
    @Override
    public boolean isMature() {
        //SWITCH MAGIC NUMBER TO CONSTANT
        return age > 2;
    }
}
