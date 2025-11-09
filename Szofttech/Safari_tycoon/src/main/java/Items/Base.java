package Items;
public class Base extends Item{

    public Base(int p) {
        super(0, p, "/base.png");
        
    }
    public Base(int p, int x, int y){
        super(0,p,"/base.png",x,y);
    }
    public Base(){
        super();
    }
    
}
