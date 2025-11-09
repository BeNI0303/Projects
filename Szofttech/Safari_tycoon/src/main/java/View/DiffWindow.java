/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Levente
 */
public class DiffWindow extends View{
    private final JLabel title;
    private final JLabel subTitle;
    private final JButton easy;
    private final JButton medium;
    private final JButton hard;
    
    public DiffWindow(View w){
        super();
        this.setTitle("Menu");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.panel = new BackgroundPanel("/background.jpg");
        this.panel.setLayout(new GridLayout(5, 1, 10, 10));
        Dimension d = new Dimension(400,400);
        this.panel.setPreferredSize(d);
        this.setLocation((this.screenSize.width - d.width) / 2,(this.screenSize.height - d.height) / 2);
        this.panel.setDoubleBuffered(true);
        
        //At window closing switching back to menu
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
               switchWindow(w,0,"");  
            }
        });
        
        
        //Buttons, labels added
        this.title = new JLabel("Safari Tycoon", JLabel.CENTER);
        this.subTitle = new JLabel("Select Difficulty", JLabel.CENTER);
        this.easy = new JButton("Easy");
        this.medium = new JButton("Medium");
        this.hard = new JButton("Hard");
        
        easy.setOpaque(true);
        easy.setContentAreaFilled(true);
        easy.setBackground(new Color(205, 170, 125));
        easy.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 70), 2));
        
        medium.setOpaque(true);
        medium.setContentAreaFilled(true);
        medium.setBackground(new Color(205, 170, 125));
        medium.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 70), 2));
        
        hard.setOpaque(true);
        hard.setContentAreaFilled(true);
        hard.setBackground(new Color(205, 170, 125));
        hard.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 70), 2));
        
        panel.setBorder(new EmptyBorder(0,Math.round(d.width/6),20,Math.round(d.width/6)));
        
        //Action to start the game
        ActionListener make = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String difficulty = e.getActionCommand().toLowerCase();
                switchWindow(w,2,difficulty);
            }
        };
        
        //Giving the listeners
        this.easy.addActionListener(make);
        this.medium.addActionListener(make);
        this.hard.addActionListener(make);
        
        //Buttons, labels added to panel
        this.panel.add(title);
        this.panel.add(subTitle);
        this.panel.add(easy);
        this.panel.add(medium);
        this.panel.add(hard);
        this.add(this.panel);
        this.pack();
        this.setVisible(true);
    }
    
    
    /**
     * Window switching
     * @param w the actual window reference
     * @param type window type
     * @param difficulty selected difficulty
     */
    private void switchWindow(View w, int type, String difficulty){
        this.dispose();
        if(type == 0){
            w = new MenuWindow(w);
        }else if(type == 2){
            w = new GameWindow(w, difficulty);
        }
    }
}
