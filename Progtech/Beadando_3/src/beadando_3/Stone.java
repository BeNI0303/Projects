package beadando_3;
import java.awt.*;
import java.util.*;

public class Stone {
    private Point location;
    
    //Konstruktor
    public Stone(int x, int y) {
        location = new Point(x, y);
    }
    
    //Kő kirajzolása
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawRect(location.x-1, location.y-1, 11, 11); // Nagyobb méretű kör
        g.setColor(Color.GRAY);
        g.fillRect(location.x, location.y, 10, 10);
    }

    //Kő helyzeténej lekérdezése
    public Point getLocation() {
        return location;
    }
}

