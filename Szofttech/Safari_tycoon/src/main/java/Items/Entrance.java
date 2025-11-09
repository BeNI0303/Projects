/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Items;

/**
 *
 * @author Levente
 */
public class Entrance extends Item{
    public Entrance(int p) {
        super(7,p,"/base.png");
    }
    public Entrance(int p, int x, int y){
        super(7,p,"/base.png",x,y);
    }
    public Entrance(){
        super();
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x-1;
        data[1] = y-1;
        data[2] = tileSize+1;
        data[3] = tileSize+1;
        return data;
    }
}
