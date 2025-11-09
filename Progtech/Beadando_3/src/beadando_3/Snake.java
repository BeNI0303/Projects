package beadando_3;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Snake {
    private LinkedList<Point> body;
    private int direction; // 0: Fel, 1: Jobbra, 2: Le, 3: Balra
    
    //Kígyó lekérése
    public LinkedList<Point> getBody(){
        return body;
    }
    
    //Konstruktor
    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(250, 200)); // Fej
        body.add(new Point(230, 200));  // Csörgő
        
        Random rand = new Random();
        direction = rand.nextInt(4); // Random irány kiválasztása
    }
    
    //Kígyó mozgatása
    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);
        
        switch (direction) {
            case 0: newHead.y -= 10; break; // Up
            case 1: newHead.x += 10; break; // Right
            case 2: newHead.y += 10; break; // Down
            case 3: newHead.x -= 10; break; // Left
        }
        
        body.addFirst(newHead);
        body.removeLast();
    }
    
    //Kígyó kirajzolása
    public void draw(Graphics2D g) {
        for (Point p : body) {
            // Körvonal (fekete)
            g.setColor(Color.BLACK);
            g.drawRect(p.x-1, p.y-1, 11, 11); // Nagyobb méretű kör
            g.setColor(Color.GREEN);
            g.fillRect(p.x, p.y, 10, 10); // Kígyó testének kirajzolása
        }
    }

    //Kígyó irányának megváltoztatása
    public void changeDirection(int keyCode) {
        if (keyCode == KeyEvent.VK_W) direction = 0;
        if (keyCode == KeyEvent.VK_D) direction = 1;
        if (keyCode == KeyEvent.VK_S) direction = 2;
        if (keyCode == KeyEvent.VK_A) direction = 3;
    }

    //Étellel kerül egy pontba
    public boolean collidesWithFood(Food food) {
        return body.getFirst().equals(food.getLocation());
    }

    //Falba ütközik
    public boolean collidesWithWall() {
        Point head = body.getFirst();
        return head.x < 0 || head.x >= 475 || head.y < 0 || head.y >= 350;
    }

    //Saját magával ütközik
    public boolean collidesWithItself() {
        Point head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    //Nő a kígyó
    public void grow() {
        body.addLast(body.getLast());
    }

    //Kővel ütközik a kígyó
    public boolean collidesWithStone(ArrayList<Stone> stones) {
        Point head = body.getFirst();
        for (Stone stone : stones) {
            if (stone.getLocation().equals(head)) {
                return true;
            }
        }
        return false;
    }
}

