package Items;
public class Lake extends Item{

    public Lake(int p) {
        super(2,p,"/lake.png");
    }
    public Lake(int p, int x, int y){
        super(2,p,"/lake.png",x,y);
    }
    public Lake(){
        super();
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x+3;
        data[1] = y+3;
        data[2] = tileSize-6;
        data[3] = tileSize-6;
        return data;
    }
}
