package MovingEntity;

import java.awt.Graphics2D;

public abstract class Predator extends Animal {
    
    public Predator(int x, int y, int price, int age, int foodSupply, int waterSupply, String path, int id) {
        super(x, y, price, age, foodSupply, waterSupply, path, id);
    }

    public Predator() {
    }

    @Override
    public void interact(Entity e) {
        
    }
    

    @Override
    public void draw(Graphics2D g2) {
        g2.drawString("Predator", this.getCoordinate().x, this.getCoordinate().y);
    }
}
