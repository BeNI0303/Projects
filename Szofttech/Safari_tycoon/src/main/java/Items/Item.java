package Items;


import MovingEntity.Drawable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Item implements Drawable{
    
    protected int id;
    protected int price;
    @JsonIgnore
    protected BufferedImage img;
    protected Point coordinate;
    protected int hp;
    public final int maxhp = 500;
    
    public Item(int id, int p,String imgSrc){
        this.id = id;
        this.price = p;
        this.hp = maxhp;
        loadImg(imgSrc);
    }
    
    public Item(){
        super();
    }

    public Item(int id, int p,String imgSrc,int x, int y){
        this.id = id;
        this.price = p;
        this.hp = maxhp;
        loadImg(imgSrc);
        this.coordinate = new Point(x,y);
    }
    @Override
    public int[] getDrawImageParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x;
        data[1] = y;
        data[2] = tileSize;
        data[3] = tileSize;
        return data;
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x;
        data[1] = y;
        data[2] = tileSize;
        data[3] = tileSize;
        return data;
    }
    public int getId() {
        return id;
    }
    @Override
    @JsonIgnore
    public BufferedImage getIcon() {
        return img;
    }
    @Override
    public Point getCoordinate() {
        return coordinate;
    }
    
    public boolean decreaseHp(int n){
        if(hp <= 0){
            return false;
        }
        hp-=n;
        return true;
    }

    public int getPrice() {
        return price;
    }

    public int getHp() {
        return hp;
    }
    
    private void loadImg(String s){
        try {
            this.img = ImageIO.read(getClass().getResourceAsStream(s));
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    public void draw(Graphics2D g2){
        
    };

    @Override
    public int[] getRangeBoxParams(int x, int y, int tileSize) {
        int[] data = new int[4];
        data[0] = x - 10;
        data[1] = y - 10;
        data[2] = tileSize + 20;
        data[3] = tileSize + 20;
        return data;
    }
}
