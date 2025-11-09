package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean ctrlPressed;
    public boolean mPressed;
    public boolean hPressed;
    public boolean hitBoxesOn = true;
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
        }
        if (code == KeyEvent.VK_M){
            mPressed = true;
        }
        if (code == KeyEvent.VK_H){
            hPressed = true;
        }
        if(hitBoxesOn && hPressed && ctrlPressed){
            hitBoxesOn = false;
        }else if(!hitBoxesOn && hPressed && ctrlPressed){
            hitBoxesOn = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
        }
        if (code == KeyEvent.VK_M){ 
            mPressed = false;
        }
        if (code == KeyEvent.VK_H){
            hPressed = false;
        }
    }
    
}
