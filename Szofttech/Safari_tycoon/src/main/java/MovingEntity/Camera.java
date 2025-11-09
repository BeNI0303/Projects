/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovingEntity;

import Controller.KeyHandler;
import Model.Model;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Levente
 */
public class Camera {
    public Point worldP;
    public final Point screenP;
    public int speed;
    Model model;
    KeyHandler keyH;
    
    public Camera(Model m, KeyHandler keyH){
        this.model = m;
        this.keyH = keyH;
        this.worldP = new Point(model.tileSize * 25,model.tileSize * 43);
        this.screenP = new Point(model.screenWidth/2, model.screenHeight/2);
        this.speed = 6;
    }
    
    public void update(){
        if(keyH.upPressed && worldP.y - screenP.y > 0){
            worldP.y -= speed;
        }else if(keyH.downPressed && worldP.y + screenP.y < model.worldHeight){
            worldP.y += speed;
        }else if(keyH.leftPressed && worldP.x - screenP.x > 0){
            worldP.x -= speed;
        }else if(keyH.rightPressed && worldP.x + screenP.x < model.worldWidth){
            worldP.x += speed;
        }
    }
    
    public void draw(Graphics2D g2){      
        g2.fillOval(screenP.x, screenP.y, 0, 0);
    }
}
