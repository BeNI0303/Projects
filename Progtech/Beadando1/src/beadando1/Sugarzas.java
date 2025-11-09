package beadando1;
public interface Sugarzas {
    public int getIgeny();
    public void setIgeny(int igeny);
}

/*
    Singleton class
*/
class Alfa_sugarzas implements Sugarzas{
    private static Alfa_sugarzas single_instance = null;
    private int igeny;
    
    /*
        Singleton class létrehozása, lekérdezése
    */
    private Alfa_sugarzas(){
        igeny = 0;
    }
    
    public static synchronized Alfa_sugarzas getInstance(){
        if(single_instance == null){
            single_instance = new Alfa_sugarzas();
        }
        return single_instance;
    }
    
    /*
        Singleton class létrehozása, lekérdezéze vége
    */
    
    /*
        igény lekérdezése
    */
    @Override
    public int getIgeny() {
        return igeny;
    }
    
    /*
        igény beállítása
    */
    @Override
    public void setIgeny(int igeny) {
        this.igeny = igeny;
    }
}

class Delta_sugarzas implements Sugarzas{
    private static Delta_sugarzas single_instance = null;
    private int igeny;
    
    /*
        Singleton class létrehozása, lekérdezése
    */
    private Delta_sugarzas(){
        igeny = 0;
    }
    
    public static synchronized Delta_sugarzas getInstance(){
        if(single_instance == null){
            single_instance = new Delta_sugarzas();
        }
        return single_instance;
    }
    
    /*
        Singleton class létrehozása, lekérdezéze vége
    */
    
    /*
        igény lekérdezése
    */    
    @Override
    public int getIgeny() {
        return igeny;
    }

    /*
        igény beállítása
    */
    @Override
    public void setIgeny(int igeny) {
        this.igeny = igeny;
    }
}

class Nincs_sugarzas implements Sugarzas{
    private static Nincs_sugarzas single_instance = null;
    private int igeny;
    
    /*
        Singleton class létrehozása, lekérdezése
    */
    private Nincs_sugarzas(){
        igeny = 0;
    }
    
    public static synchronized Nincs_sugarzas getInstance(){
        if(single_instance == null){
            single_instance = new Nincs_sugarzas();
        }
        return single_instance;
    }
    
    /*
        Singleton class létrehozása, lekérdezéze vége
    */
    
    /*
        igény lekérdezése
    */    
    @Override
    public int getIgeny() {
        return igeny;
    }

    /*
        igény beállítása
    */
    @Override
    public void setIgeny(int igeny) {
        this.igeny = igeny;
    }
}

