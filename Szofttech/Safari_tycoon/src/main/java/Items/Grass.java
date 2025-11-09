package Items;
public class Grass extends Item{

    public Grass(int p) {
        super(1,p,"/grass.png");
    }
    public Grass(int p, int x, int y){
        super(1,p,"/grass.png",x,y);
    }
    public Grass(){
        super();
    }
    
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x+5;
        data[1] = y+5;
        data[2] = tileSize-14;
        data[3] = tileSize-10;
        return data;
    }
}
