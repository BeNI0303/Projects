package beadando_2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu {
    private JFrame frame;
    
    
    //Létrehozza a menü kinézetét és elindítja a játékot a megfelelő mérettel.
    public Menu(){
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setTitle("Menü");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());

        
         JPanel headderPanel = new JPanel();
         
         headderPanel.setLayout(new BoxLayout(headderPanel, BoxLayout.PAGE_AXIS));
         headderPanel.add(Box.createRigidArea(new Dimension(0,1)));
         headderPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
         
        JLabel titleLabel = new JLabel("Játék Menü", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel label = new JLabel("Válaszd ki mekkora legyen a játéktér", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        
        headderPanel.add(titleLabel);
        headderPanel.add(label);
        
        frame.add(headderPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,3));

        JButton start1 = new JButton("3x3");
        JButton start2 = new JButton("5x5");
        JButton start3 = new JButton("7x7");
        
        start1.addActionListener((ActionEvent e) -> {
            Game game = new Game(3,this);
            frame.dispose();
        });
        
        start2.addActionListener((ActionEvent e) -> {
            Game game = new Game(5,this);
            frame.dispose();
        });
        
        start3.addActionListener((ActionEvent e) -> {
            Game game = new Game(7,this);
            frame.dispose();
        });
        
        
        buttonPanel.add(start1);
        buttonPanel.add(start2);
        buttonPanel.add(start3);

        JButton exitButton = new JButton("Kilépés");
        exitButton.addActionListener(e -> System.exit(0));
        JPanel exit = new JPanel();
        exit.setLayout(new BoxLayout(exit, BoxLayout.LINE_AXIS));
        exit.add(exitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(exit,BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    
    public JFrame getFrame(){
        return frame;
    }
}
