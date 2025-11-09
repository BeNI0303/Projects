package Items;

public class Bush extends Item{
    
    
    public Bush(int p) {
        super(3,p,"/bush.png");
    }
    public Bush(int p, int x, int y){
        super(3,p,"/bush.png",x,y);
    }
    public Bush(){
        super();
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x+6;
        data[1] = y+6;
        data[2] = tileSize-10;
        data[3] = tileSize-14;
        return data;
    }
}
