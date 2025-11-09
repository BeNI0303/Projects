package MovingEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Antelope extends Herbivore {
    
    public static final int price = 50;
    public static final int id = 100;
    public static final String path = "/antilope.png";
    
    public Antelope() {
        super();
    }
    @Override
    public Animal cloneAnimal () {
        return new Antelope(this.coordinate.x, this.coordinate.y, age, foodSupply, waterSupply);
    }
    
    public Antelope(int x, int y, int age, int foodSupply, int waterSupply) {
        super(x, y, price, age, foodSupply, waterSupply, path,id);
    }
    
    @Override
    @JsonProperty("mature")
    public boolean isMature() {
        //SWITCH MAGIC NUMBER TO CONSTANT
        return age > 1;
    }
}
