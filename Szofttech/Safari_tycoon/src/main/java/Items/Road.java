package Items;
public class Road extends Item{

    public Road(int p) {
        super(5,p,"/road.png");
    }
    public Road(int p, int x, int y){
        super(5,p,"/road.png",x,y);
    }
    public Road(){
        super();
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x+1;
        data[1] = y+1;
        data[2] = tileSize-1;
        data[3] = tileSize-1;
        return data;
    }
}
