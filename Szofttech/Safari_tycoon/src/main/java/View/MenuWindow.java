package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MenuWindow extends View {

    private final JLabel title;
    private final JButton newGame;
    private final JButton loadGame;

    public MenuWindow(View w) {
        super();
        this.setTitle("Menu");
        this.panel = new BackgroundPanel("/background.jpg");
        this.panel.setLayout(new GridLayout(4, 1, 0, 20));
        Dimension d = new Dimension(400, 400);
        this.panel.setPreferredSize(d);
        this.setLocation((this.screenSize.width - d.width) / 2, (this.screenSize.height - d.height) / 2);
        this.panel.setDoubleBuffered(true);

        //Buttons and labels
        this.title = new JLabel("Safari Tycoon", JLabel.CENTER);
        this.newGame = new JButton("New game");
        this.loadGame = new JButton("Load game");

        newGame.setOpaque(true);
        newGame.setContentAreaFilled(true);
        newGame.setBackground(new Color(205, 170, 125));
        newGame.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 70), 2));
        
        loadGame.setOpaque(true);
        loadGame.setContentAreaFilled(true);
        loadGame.setBackground(new Color(205, 170, 125));
        loadGame.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 70), 2));
        
        //Giving action listeners to the buttons
        this.newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchWindow(w, 1);
            }
        });
        this.loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchWindow(w, 2);
            }
        });

        panel.setBorder(new EmptyBorder(0, Math.round(d.width / 6), 20, Math.round(d.width / 6)));

        this.panel.add(title);
        this.panel.add(newGame);
        this.panel.add(loadGame);
        JPanel emptySpace = new JPanel();
        emptySpace.setVisible(false);
        panel.add(emptySpace);

        this.add(this.panel);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Window switching
     * @param w the actual window reference
     * @param type window type
     */
    private void switchWindow(View w, int type) {
        this.dispose();
        if (type == 1) {
            w = new DiffWindow(w);
        } else if (type == 2) {
            w = new LoadGameWindow(w);
            //String savedFile = "load saved txt here";
            //w = new GameWindow(w, savedFile);
        } else {
            System.out.println("No saved game found.");
        }

    }
}
