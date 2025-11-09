package beadando_3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.Timer;

public class GameBoard extends JPanel {
    private final int WIDTH = 500;
    private final int HEIGHT = 400;
    private Snake snake;
    private Food food;
    private ArrayList<Stone> stones;
    private int score;
    private int elapsedTime = 0;
    private Timer timer;

    //Konsruktor létrehozza a kígyót az ételt és a köveket. Kezeli a keypresseket.
    public GameBoard() {
        Random rand = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.snake = new Snake();
        this.stones = new ArrayList<>();
        this.food = new Food(snake,stones);
        this.score = 0;
        setFocusable(true);
        
        int randomStone = rand.nextInt(11);
        for (int i = 0; i < randomStone; i++) {
            int x;
            int y;
            do{
                x = rand.nextInt(450 / 10) * 10;
                y = rand.nextInt(350 / 10) * 10;
            }while(snake.getBody().contains(new Point(x,y)));
            stones.add(new Stone(x,y));
        }
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                snake.changeDirection(e.getKeyCode());
            }
        });
        
        // Idő kijelző inicializálása
        JLabel timeLabel = new JLabel("Elapsed Time: 0s");
        timeLabel.setBounds(10, 10, 150, 30);
        add(timeLabel);

        // Timer inicializálása
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timeLabel.setText("Elapsed Time: " + elapsedTime + "s");
            }
        });

        // Indítsd el a Timer-t
        timer.start();
    }

    //Kirajzolja kígyót, az ételt, és a köveket, meg a pálya hátterét.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = new ImageIcon("homok.jpg").getImage();
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage,0,0, WIDTH, HEIGHT,this); // Background
        
        //Kígyó kirajzolása
        snake.draw(g2d);
        
        //Étel kirajzolása
        food.draw(g2d);
        
        //Kövek kirajzolása
        for (Stone stone : stones) {
            stone.draw(g2d);
        }

        // Score kirajzolása
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + score, 10, 20);
    }

    //Figyeli hogy neki megy e valaminek a kígyó és mozgatja.
    public boolean updateGame() {
        // Game logic for moving snake and checking for collisions
        if (snake.collidesWithFood(food)) {
            score++;
            snake.grow();
            food.relocate(snake.getBody());
        }
        
        if (snake.collidesWithWall() || snake.collidesWithItself() || snake.collidesWithStone(stones)) {
            // Game over logic
            showGameOver();
            return true;
        }
        snake.move();
        repaint();
        return false;
        
    }

    //Játé végén megjeleníti a panelt a névbeíráshoz.
    private void showGameOver() {
        timer.stop();
        // Display game over and prompt for saving score
        String name = JOptionPane.showInputDialog("Game Over! Enter your name:");
        if (name != null && !name.trim().isEmpty()) {
            // Save score to database or leaderboard
            Leaderboard.saveScore(name, score);
        }
    }
}

