package Controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.event.MouseInputListener;
import View.ButtonPanel;
import View.GamePanel;

public class MouseHandler implements MouseInputListener, ActionListener {

    public int mX, mY;
    public int meX, meY;
    public boolean clicked;
    public int selectedItem;
    public int selectedAnimal;
    public boolean jeepClicked;
    public boolean rangerClicked;
    public boolean sellAnimal;
    public boolean removeRoad;
    public boolean removeSelection;
    
    public MouseHandler() {
    }

    //PLACES DOWN ITEM AT CLICKED COORDINATES
    @Override
    public void mouseClicked(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
        clicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        meX = e.getX();
        meY = e.getY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        
        // Get access to the ButtonPanel to check if minimap is in fullscreen mode
        ButtonPanel buttonPanel = null;
        Component component = ((JButton) e.getSource()).getParent();
        while (component != null && !(component instanceof ButtonPanel)) {
            component = component.getParent();
        }
        
        if (component instanceof ButtonPanel) {
            buttonPanel = (ButtonPanel) component;
            GamePanel gamePanel = buttonPanel.getGamePanel();
            
            // If we're in fullscreen minimap mode, exit it before processing the button click
            if (gamePanel != null && gamePanel.getGameWindow() != null && 
                gamePanel.getGameWindow().isMinimapFullscreen()) {
                gamePanel.getGameWindow().toggleMinimapFullscreen();
            }
        }
        
        //Sets the selected variables based on which button pressed
        if(button.getClientProperty("value").equals("Jeep")){
            jeepClicked = true;
            selectedAnimal = 0;
            selectedItem = 0;
            sellAnimal = false;
        }else if(button.getClientProperty("value").equals("Ranger")){
            rangerClicked = true;
            selectedAnimal = 0;
            selectedItem = 0;
            sellAnimal = false;
        }else if(button.getClientProperty("value").equals("delete")){
            removeRoad = true;
            selectedAnimal = 0;
            selectedItem = 0;
            sellAnimal = false;
        }else if(button.getClientProperty("value").equals("remove")){
            selectedAnimal = 0;
            selectedItem = 0;
            clicked = false;
            sellAnimal = false;
            removeSelection = true;
        }else if (button.getClientProperty("value").equals("sell")){
            sellAnimal = true;
            selectedAnimal = 0;
            selectedItem = 0;
        }else if((int)button.getClientProperty("value") >= 100){
            selectedAnimal = (int)button.getClientProperty("value");
            selectedItem = 0;
            sellAnimal = false;
        } else {
            selectedItem = (int) button.getClientProperty("value");
            selectedAnimal = 0;
            sellAnimal = false;
        }   
    }
}
