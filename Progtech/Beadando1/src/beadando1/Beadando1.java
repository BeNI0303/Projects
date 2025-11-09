package beadando1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Beadando1 {
    public static void main(String[] args) {
        ArrayList<Noveny> novenyek = new ArrayList();
        int novenyszam;
        int napszam = 0;
        try (RandomAccessFile raf = new RandomAccessFile("be.txt","r")){
            /*
                Fájl beolvasás
            */
            String sor = raf.readLine();
            if(sor != null){
                novenyszam = Integer.parseInt(sor);
            }else{
                throw new Exception("Nincs megadva noveny szam!");
            }
            sor = raf.readLine();
            while(sor != null){
                String[] data = sor.split(" ");
                if(!sor.isEmpty()){
                    if(data.length == 3){
                        if(data[1].equals("a")){
                            novenyek.add(new Puffancs(data[0],Integer.parseInt(data[2])));
                        }
                        else if(data[1].equals("d")){
                            novenyek.add(new Deltafa(data[0],Integer.parseInt(data[2])));
                        }
                        else if(data[1].equals("p")){
                            novenyek.add(new Parabokor(data[0],Integer.parseInt(data[2])));
                        }else{
                            System.out.println(data[0]);
                            throw new Exception("Nincs ilyen fajta noveny");
                        }
                    }else if(data.length == 1){
                       napszam = Integer.parseInt(data[0]); 
                    }else{
                        throw new Exception("Nem megfelelő a sor formája");
                    }
                }else{
                    throw new Exception("Ures a sor");
                }
                
                sor = raf.readLine();
            }
            /*
                Fájl beolvasás vége
            */
            
            /*
                Napok szimulálása:
                    Első nap nincs sugárzás, majd minden nap lenullázzuk az alfa és a delta sugárzás napi igényét, 
                    valamint végig megyünk a növényeken. Kiválasztjuk az adott növény milyen fajta,
                    és ez után beállítjuk a tápanyagát megfelelően ezzel egybe a növények igényét is változtatjuk.
                    A nap végén beállítjuk a következő napi sugárzást az igényeknek megfelelően.
            */
            Sugarzas aktualis = Nincs_sugarzas.getInstance();
            for (int i = 0; i < napszam; i++) {
                Alfa_sugarzas alfa = Alfa_sugarzas.getInstance();
                Delta_sugarzas delta = Delta_sugarzas.getInstance();
                alfa.setIgeny(0);
                delta.setIgeny(0);
                for (Noveny noveny : novenyek) {
                    if(aktualis.getClass() == Alfa_sugarzas.class){
                        noveny.setTapanyag((Alfa_sugarzas)aktualis);
                    }else if(aktualis.getClass() == Delta_sugarzas.class)
                    {
                        noveny.setTapanyag((Delta_sugarzas)aktualis);
                    }else{
                        noveny.setTapanyag((Nincs_sugarzas)aktualis);
                    }
                }
                if(alfa.getIgeny() >= (delta.getIgeny()+3)){
                    aktualis = alfa;
                }else if(delta.getIgeny() >= (alfa.getIgeny()+3)){
                    aktualis = delta;
                }else{
                    aktualis = Nincs_sugarzas.getInstance();
                }
            }
            /*
                Napok szimulálásának vége
            */
            
            /*
                Túlélő növények kiírása
            */
            System.out.println("Tulelo novenyek: ");
            for (Noveny noveny : novenyek) {
                if(noveny.ele){
                    System.out.print(noveny.getNev() + ", ");
                }
            }
            
        /*
            Külön féle hibák elkapása, és külön hibaüzenetek kiírása.
        */    
        } catch (IOException exp) {
            System.err.println(exp.getMessage());
        }catch (NumberFormatException e){
            System.err.println("Nem szamot adtal meg!");
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
}
