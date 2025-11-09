package Items;
public class Tree extends Item{

    public Tree(int p) {
        super(4,p,"/tree.png");
    }
    public Tree(int p, int x, int y){
        super(4,p,"/tree.png",x,y);
    }
    public Tree(){
        super();
    }
    @Override
    public int[] getHitBoxParams(int x, int y, int tileSize){
        int[] data = new int[4];
        data[0] = x + 10;
        data[1] = y + tileSize / 2 + 6;
        data[2] = tileSize - 20;
        data[3] = tileSize / 2 - 12;
        return data;
    }
}
