package View;

import Controller.MouseHandler;
import MovingEntity.Antelope;
import MovingEntity.Cheetah;
import MovingEntity.Giraffe;
import MovingEntity.Jeep;
import MovingEntity.Lion;
import MovingEntity.Ranger;
import MovingEntity.Zebra;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ButtonPanel extends JPanel {

    private final JButton grassB;
    private final JButton bushB;
    private final JButton treeB;
    private final JButton lakeB;
    private final JButton roadB;

    private final JButton antelope;
    private final JButton giraffe;
    private final JButton zebra;
    private final JButton lion;
    private final JButton cheetah;

    private final JButton ranger;
    private final JButton jeep;
    
    private final JButton unselect;
    private final JButton sellAnimal;
    private final JButton deleteRoadPath;

    private GamePanel gp;

    public ButtonPanel(GamePanel gp) {
        
        this.setPreferredSize(new Dimension(300, 75));
        this.setLayout(new GridLayout(3, 6,3,3));
        this.gp = gp;
        //Make buttons, giving properties
        grassB = new JButton(String.format("Grass: %d", gp.getModel().grass.getPrice()));
        grassB.putClientProperty("value", gp.getModel().grass.getId());
        //grassB.setBorder(new LineBorder(Color.BLACK, 5));
        bushB = new JButton(String.format("Bush: %d", gp.getModel().bush.getPrice()));
        bushB.putClientProperty("value", gp.getModel().bush.getId());
        treeB = new JButton(String.format("Tree: %d", gp.getModel().tree.getPrice()));
        treeB.putClientProperty("value", gp.getModel().tree.getId());
        lakeB = new JButton(String.format("Lake: %d", gp.getModel().lake.getPrice()));
        lakeB.putClientProperty("value", gp.getModel().lake.getId());
        roadB = new JButton(String.format("Road: %d", gp.getModel().road.getPrice()));
        roadB.putClientProperty("value", gp.getModel().road.getId());

        antelope = new JButton(String.format("Antelope: %d", Antelope.price));
        antelope.putClientProperty("value", Antelope.id);
        giraffe = new JButton(String.format("Giraffe: %d", Giraffe.price));
        giraffe.putClientProperty("value", Giraffe.id);
        zebra = new JButton(String.format("Zebra: %d", Zebra.price));
        zebra.putClientProperty("value", Zebra.id);
        lion = new JButton(String.format("Lion: %d", Lion.price));
        lion.putClientProperty("value", Lion.id);
        cheetah = new JButton(String.format("Cheetah: %d", Cheetah.price));
        cheetah.putClientProperty("value", Cheetah.id);

        ranger = new JButton(String.format("Ranger: %d", Ranger.price));
        ranger.putClientProperty("value", "Ranger");
        jeep = new JButton(String.format("Jeep: %d", Jeep.price));
        jeep.putClientProperty("value", "Jeep");
        
        unselect = new JButton("X");
        unselect.putClientProperty("value", "remove");
        sellAnimal = new JButton("Sell");
        sellAnimal.putClientProperty("value", "sell");
        deleteRoadPath = new JButton("Remove road path");
        deleteRoadPath.putClientProperty("value", "delete");
        //Giving listeners
        MouseHandler mouseH = gp.getMouseH();
        grassB.addActionListener(mouseH);
        bushB.addActionListener(mouseH);
        treeB.addActionListener(mouseH);
        lakeB.addActionListener(mouseH);
        roadB.addActionListener(mouseH);
        antelope.addActionListener(mouseH);
        giraffe.addActionListener(mouseH);
        zebra.addActionListener(mouseH);
        lion.addActionListener(mouseH);
        cheetah.addActionListener(mouseH);
        jeep.addActionListener(mouseH);
        ranger.addActionListener(mouseH);
        unselect.addActionListener(mouseH);
        sellAnimal.addActionListener(mouseH);
        deleteRoadPath.addActionListener(mouseH);
        Color ligthBrown = new Color(210, 144, 78);
        //Buttons added to panel
        this.add(new JLabel("Fields:", JLabel.CENTER) {
            {
                setOpaque(false);
            }
        });
        this.add(grassB);
        this.add(bushB);
        this.add(treeB);
        this.add(lakeB);
        this.add(roadB);
        this.add(new JLabel("Animals:", JLabel.CENTER){
            {
                setOpaque(false);
            }
        });
        this.add(antelope);
        this.add(giraffe);
        this.add(zebra);
        this.add(lion);
        this.add(cheetah);
        this.add(new JLabel("Other:", JLabel.CENTER){
            {
                setOpaque(false);
            }
        });
        this.add(ranger);
        this.add(jeep);
        this.add(deleteRoadPath);
        this.add(sellAnimal);
        this.add(unselect);
        
    }

    /**
     * Gets the GamePanel associated with this ButtonPanel
     * @return the GamePanel
     */
    public GamePanel getGamePanel() {
        return this.gp;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(getClass().getResource("/shopBg.png")).getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
