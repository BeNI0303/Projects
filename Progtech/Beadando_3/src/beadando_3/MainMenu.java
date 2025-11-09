package beadando_3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu{
    private JFrame frame = new JFrame();
    private Timer timer;
    public MainMenu() {
        
        
        // Ablak beállításai
        frame.setTitle("Snake Game - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(2,2,10,10));

        // Cím megjelenítése
        JLabel titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel);

        // Új játék indítása gomb
        JButton startGameButton = new JButton("Start New Game");
        startGameButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        frame.add(startGameButton);

        // Top eredmények megtekintése gomb
        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.setFont(new Font("Arial", Font.PLAIN, 18));
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Leaderboard.showTopScores();
            }
        });
        frame.add(leaderboardButton);

        // Kilépés gomb
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.add(exitButton);
        frame.setVisible(true);
    }

    // Új játék indítása
    private void startGame() {
        frame.dispose(); // Főmenü bezárása
        JFrame gameFrame = new JFrame("Snake Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(500, 400);
        gameFrame.setLocationRelativeTo(null);

        GameBoard gameBoard = new GameBoard();
        gameFrame.add(gameBoard);
        gameFrame.setVisible(true);

        // Játék futtatása
        timer = new Timer(60, new ActionListener() { // Frissítés 60 ms-onként
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameBoard.updateGame()){
                    timer.stop();
                    gameFrame.dispose();
                    frame.setVisible(true);
                }
            }
        });
        timer.start();
    }
}
