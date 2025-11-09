
package Items;

public class Edge extends Item{

    public Edge(int p) {
        super(6,p,"/edge.png");
    }
    public Edge(int p, int x, int y){
        super(6,p,"/edge.png",x,y);
    }
    public Edge(){
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

