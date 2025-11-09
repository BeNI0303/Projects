package beadando_3;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Food {
    private Point location;
    private Snake snake;
    private ArrayList<Stone> stones;
    
    //Konstruktor
    public Food(Snake snake, ArrayList<Stone>stones) {
        this.snake = snake;
        this.stones = stones;
        relocate(snake.getBody());
    }
    
    
    //Az adott ételt lehelyezi úgy hogy ne legyen kígyón
    public void relocate(LinkedList<Point> snakeb) {
        Random rand = new Random();
        int x;
        int y;
        do{
            x = rand.nextInt(450 / 10) * 10;
            y = rand.nextInt(350 / 10) * 10;
        }while(snakeb.contains(new Point(x,y)) && stones.contains(new Point(x,y)));

        location = new Point(x, y);
    }

    //Kirajzolja az ételt
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawRect(location.x-1, location.y-1, 11, 11);
        
        g.setColor(Color.RED);
        g.fillRect(location.x, location.y, 10, 10);
    }
    //Lekéri az étel helyzetéts
    public Point getLocation() {
        return location;
    }
}

