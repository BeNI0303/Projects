/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.JFrame;

/**
 *
 * @author Levente
 */
public class ViewManager {
    private View actual;
    
    //Storing the reference of the actual window
    public ViewManager(){
        actual = new MenuWindow(actual);
    }
}
