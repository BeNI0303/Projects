package beadando1;

public abstract class Noveny {
    protected String nev;
    protected int tapanyag;
    protected boolean ele;
    
    /*Abstract osztály miatt csak a gyerekek férhetnek a konstruktorhoz*/
    protected Noveny(String nev, int tapanyag){
        this.nev = nev;
        this.tapanyag = tapanyag;
        this.ele = true;
    }
    
    /*Név lekérdezése*/
    public String getNev(){
        return nev;
    }
    
    /*Tápanyag lekérdezése*/
    public int getTapanyag() {
        return tapanyag;
    }

    /*Él-e lekérdezése*/
    public boolean isEle() {
        return ele;
    }
    
    /*Név beállítása*/
    public void setNev(String nev) {
        this.nev = nev;
    }
    
    /*  
        Látogatói tervezési minta alapján külön sugárzásra külön viselkedik a setTápanyag.
        3 sugárzási fajtára egy egy tápanyag beállító metódus
    */
    public abstract void setTapanyag(Alfa_sugarzas sugarzas);
    public abstract void setTapanyag(Delta_sugarzas sugarzas);
    public abstract void setTapanyag(Nincs_sugarzas sugarzas);

    /*Él-e beállítása*/
    public void setEle(boolean ele) {
        this.ele = ele;
    }
    
}

class Puffancs extends Noveny{
    public Puffancs(String nev, int tapanyag){
        super(nev, tapanyag);
    }
    
    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Alfa sugárzásra a tápanyag változás beállítása. Az alfa sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Alfa_sugarzas sugarzas){
        if(ele){
            tapanyag += 2;
            if(tapanyag <= 0 || tapanyag > 10){
                ele = false;
            }
            if(ele){
                Alfa_sugarzas alfas = Alfa_sugarzas.getInstance();
                alfas.setIgeny(alfas.getIgeny()+ (10-tapanyag));
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Nincs sugárzásra a tápanyag változás beállítása. Az alfa sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Nincs_sugarzas sugarzas){
        if(ele){
            tapanyag -= 1;
            if(tapanyag <= 0 || tapanyag > 10){
                ele = false;
            }
            if(ele){
                Alfa_sugarzas alfas = Alfa_sugarzas.getInstance();
                alfas.setIgeny(alfas.getIgeny()+ (10-tapanyag));
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Delta sugárzásra a tápanyag változás beállítása. Az alfa sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Delta_sugarzas sugarzas){
        if(ele){
            tapanyag -= 2;
            if(tapanyag <= 0 || tapanyag > 10){
                ele = false;
            }
            if(ele){
                Alfa_sugarzas alfas = Alfa_sugarzas.getInstance();
                alfas.setIgeny(alfas.getIgeny()+ (10-tapanyag));
            }
        }
    }
}

class Deltafa extends Noveny{
    public Deltafa(String nev, int tapanyag){
        super(nev, tapanyag);
    }
    
    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Alfa sugárzásra a tápanyag változás beállítása. Az Delta sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Alfa_sugarzas sugarzas){
        if(ele){
            tapanyag -= 3;
            if(tapanyag <= 0){
                ele = false;
            }
            if(ele){
                Delta_sugarzas deltas = Delta_sugarzas.getInstance();
                if(tapanyag < 5){
                    deltas.setIgeny(deltas.getIgeny() + 4);
                }
                else if(tapanyag >= 5 && tapanyag <= 10){
                    deltas.setIgeny(deltas.getIgeny() + 1);
                }
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Nincs sugárzásra a tápanyag változás beállítása. Az Delta sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Nincs_sugarzas sugarzas){
        if(ele){
            tapanyag -= 1;
            if(tapanyag <= 0){
                ele = false;
            }
            if(ele){
                Delta_sugarzas deltas = Delta_sugarzas.getInstance();
                if(tapanyag < 5){
                    deltas.setIgeny(deltas.getIgeny() + 4);
                }
                else if(tapanyag >= 5 && tapanyag <= 10){
                    deltas.setIgeny(deltas.getIgeny() + 1);
                }
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot. Majd ha ez után is él leadja az igényt.
        Delta sugárzásra a tápanyag változás beállítása. Az Delta sugárzásnak leadni az igényt.
    */
    @Override
    public void setTapanyag(Delta_sugarzas sugarzas){
        if(ele){
            tapanyag += 4;
            if(tapanyag <= 0){
                ele = false;
            }
            if(ele){
                Delta_sugarzas deltas = Delta_sugarzas.getInstance();
                if(tapanyag < 5){
                    deltas.setIgeny(deltas.getIgeny() + 4);
                }
                else if(tapanyag >= 5 && tapanyag <= 10){
                    deltas.setIgeny(deltas.getIgeny() + 1);
                }
            }
        }
    }
}

class Parabokor extends Noveny{
    public Parabokor(String nev, int tapanyag){
        super(nev, tapanyag);
    }
    
    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot.
        Alfa sugárzásra a tápanyag változás beállítása.
    */
    @Override
    public void setTapanyag(Alfa_sugarzas sugarzas){
        if(ele){
            tapanyag += 1;
            if(tapanyag <= 0){
                ele = false;
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot.
        Nincs sugárzásra a tápanyag változás beállítása.
    */
    @Override
    public void setTapanyag(Nincs_sugarzas sugarzas){
        if(ele){
            tapanyag -= 1;
            if(tapanyag <= 0){
                ele = false;  
            }
        }
    }

    /*  
        Először megnézzük él e a növény ha igen változtatjuk a tápanyagot.
        Delta sugárzásra a tápanyag változás beállítása.
    */
    @Override
    public void setTapanyag(Delta_sugarzas sugarzas){
        if(ele){
            tapanyag += 1;
            if(tapanyag <= 0){
                ele = false;
            }
        }
    }
}