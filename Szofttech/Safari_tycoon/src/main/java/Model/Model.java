package Model;

import Items.*;
import MovingEntity.Animal;
import MovingEntity.Antelope;
import MovingEntity.Cheetah;
import MovingEntity.Entity;
import MovingEntity.Giraffe;
import MovingEntity.Herbivore;
import MovingEntity.Jeep;
import MovingEntity.Lion;
import MovingEntity.Poacher;
import MovingEntity.Ranger;
import MovingEntity.Visitor;
import MovingEntity.Zebra;
import Services.SaveLoadGameService;
import MovingEntity.Person;
import MovingEntity.Predator;

import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

public class Model {
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 13;
    //World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    public final Base base;
    public final Bush bush;
    public final Grass grass;
    public final Lake lake;
    public final Road road;
    public final Tree tree;
    public final Edge edge;
    public final Entrance entr;
    public final int ticketPrice = 500;
    public final int tileTypeNum = 8;
    public final int animalTypeNum = 5;
    public int tiles[][];
    public Item tileTypes[]; 
    public Animal animalTypes[];
    private ArrayList<Animal> animals;
    private ArrayList<Jeep> jeeps;
    private ArrayList<Ranger> rangers;
    private ArrayList<Visitor> visitors;
    private ArrayList<Item> items;
    private Poacher poacher;
    private ArrayList<Point> roadPath;
    private static int balance;
    private Point lastRoad;
    private Point endRoad;
    private int spawnBufferSize = 200;
    private int visitorSpawnBuffer = spawnBufferSize;
    private int poacherSpawnBuffer = spawnBufferSize + 100;
    private Ranger selectedRanger;
    public final String diff; 

    public Model(String diff) {
        this.diff = diff;
        this.tileTypes = new Item[tileTypeNum];
        this.animalTypes = new Animal[animalTypeNum];
        this.base = new Base(0);
        this.bush = new Bush(100);
        this.grass = new Grass(80);
        this.lake = new Lake(400);
        this.road = new Road(300);
        this.tree = new Tree(120);
        this.edge = new Edge(0);
        this.entr = new Entrance(0);
        
        
        this.animals = new ArrayList<>();
        this.jeeps = new ArrayList<>();
        this.rangers = new ArrayList<>();
        this.roadPath = new ArrayList<>();
        this.items = new ArrayList<>();
        this.visitors = new ArrayList<>();
        this.tileTypes[base.getId()] = base;
        this.tileTypes[bush.getId()] = bush;
        this.tileTypes[tree.getId()] = tree;
        this.tileTypes[grass.getId()] = grass;
        this.tileTypes[lake.getId()] = lake;
        this.tileTypes[road.getId()] = road;
        this.tileTypes[edge.getId()] = edge;
        this.tileTypes[entr.getId()] = entr;
        this.animalTypes[Antelope.id-100] = new Antelope(0, 0, 0, 100, 100);
        this.animalTypes[Giraffe.id-100] = new Giraffe(0, 0, 0, 100, 100);
        this.animalTypes[Zebra.id-100] = new Zebra(0, 0, 0, 100, 100);
        this.animalTypes[Cheetah.id-100] = new Cheetah(0, 0, 0, 100, 100);
        this.animalTypes[Lion.id-100] = new Lion(0, 0, 0, 100, 100);
        if(diff.equals("easy")){
            balance = 5000;
        }else if(diff.equals("medium")){
            balance = 4000;
        }else if(diff.equals("hard")){  
            balance = 3000;
        }else{
            balance = 0;
        }
        
        if (diff.equals("easy") || diff.equals("medium") || diff.equals("hard")) {
            tiles = loadMap("./src/main/resources/base_maps/" + diff + ".txt");
        } else {
            tiles = loadMap("./src/main/resources/map/" + diff + ".txt");
        }
        
        if (!diff.equals("easy") && !diff.equals("medium") && !diff.equals("hard")) { //only load saved data if not starting a new game
            loadSavedGameData(diff);
        }else{
            initRoadPath();
        }
        

    }

    //CHECKS IF SELECTED ITEM ID IS VALID AND
    //COMPLETES THE TRANSACTION
    //RETURNS TRUE IF TRANSACTION COMPLETED
    public boolean purchaseItem(int itemId) {
        boolean isIdValid = itemId >= 1 && itemId < tileTypes.length;

        if (isIdValid) {
            Item item = tileTypes[itemId];
            if (balance >= item.getPrice()) {
                balance -= item.getPrice();
                //System.out.println("Current balance: " + balance);
                return true;
            }
        }
        return false;
    }

    //CHECKS IF TILE IS DIFFERENT THAN THE PLACED TYPE AND
    //IF BALANCE IS SUFFICIENT
    public boolean canPlaceOnPosition(int row, int col, int type) {
        if(type != 5){
            return (tiles[row][col] == 0);
        }
        
        if(type == 5){
            if(tiles[row][col] != 0) return false;
            if(endRoad.equals(lastRoad)) return false;
            int cnt = 0;
            if(tiles[row - 1][col] == 5 ) cnt++;
            if(tiles[row + 1][col] == 5 && !endRoad.equals(new Point(row+1,col))) cnt++;
            if(tiles[row][col - 1] == 5 ) cnt++;
            if(tiles[row][col + 1] == 5 ) cnt++;
            if(cnt == 1 && (lastRoad.equals(new Point(row-1,col)) || lastRoad.equals(new Point(row+1,col)) || lastRoad.equals(new Point(row,col-1)) || lastRoad.equals(new Point(row,col+1)))){
                lastRoad = new Point(new Point(row,col));
                this.roadPath.add(lastRoad);
                if(endRoad.equals(new Point(row+1,col))){
                    lastRoad = (Point)endRoad.clone();
                    this.roadPath.add(lastRoad);
                } 
                return true;
            }
            return false;
        }
        
        return false;
        
    }

    public int[][] loadMap(String name) {

        int[][] map = new int[maxWorldRow][maxWorldCol];

        try {

            RandomAccessFile input = new RandomAccessFile(name, "r");
            int i = 0;

            while (input.getFilePointer() < input.length()) {

                String[] values = input.readLine().split(" ");
                for (int j = 0; j < maxWorldCol; j++) {
                    map[i][j] = Integer.parseInt(values[j]);
                    if(map[i][j] != 5 && map[i][j] != 0){
                        addItem(map[i][j], j, i);
                    }
                }
                i++;
            }

            input.close();
        } catch (IOException exc) {
            System.out.println(exc);
        }

        return map;
    }
    
    /**
     * Loads the saved game data into the model
     * @param name 
     */
    public void loadSavedGameData(String name) {
        System.out.println(name);
        SavedGameData dataToLoad = SaveLoadGameService.loadSavedGameState(name);
        this.balance = dataToLoad.getBalance();
        Visitor.setRate(dataToLoad.getRating());
        
        for (Jeep jeep : dataToLoad.getJeeps()) {
            this.jeeps.add(jeep.copyJeep());
        }
        for (Animal a : dataToLoad.getAnimals()) {
            this.animals.add(a.cloneAnimal());
        }

        ArrayList<Person> temp = new ArrayList<>();
        for (Person person : dataToLoad.getRangers()) {
            temp.add(person.clonePerson());
        }
        
        for (Person person : dataToLoad.getVisitors()) {
            temp.add(person.clonePerson());
        }
        for (Person person : temp) {

            if(person instanceof Visitor v){
                this.visitors.add(v);
            }else{
                this.rangers.add((Ranger)person);
            }
        }

        this.poacher = dataToLoad.getPoacher();
        this.roadPath = dataToLoad.getRoadPath();
        this.endRoad = dataToLoad.getEndRoad();
        this.lastRoad = dataToLoad.getLastRoad();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }
    
    public ArrayList<Animal> getAntelopes(){
        ArrayList<Animal> antelopes = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Antelope antelope){
                antelopes.add(antelope);
            }
        }
        return antelopes;
    }
    
    public ArrayList<Animal> getGiraffes(){
        ArrayList<Animal> giraffes = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Giraffe giraffe){
                giraffes.add(giraffe);
            }
        }
        return giraffes;
    }
    
    public ArrayList<Animal> getCheetahs(){
        ArrayList<Animal> cheetahs = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Cheetah cheetah){
                cheetahs.add(cheetah);
            }
        }
        return cheetahs;
    }
    
    public ArrayList<Animal> getLions(){
        ArrayList<Animal> lions = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Lion lion){
                lions.add(lion);
            }
        }
        return lions;
    }
    
    public ArrayList<Animal> getZebras(){
        ArrayList<Animal> zebras = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Zebra zebra){
                zebras.add(zebra);
            }
        }
        return zebras;
    }
    
    public ArrayList<Predator> getPredators(){
        ArrayList<Predator> predators = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Predator p){
                predators.add(p);
            }
        }
        return predators;
    }
    
    public ArrayList<Herbivore> getHerbivores(){
        ArrayList<Herbivore> herbivores = new ArrayList();
        for (Animal animal : animals) {
            if(animal instanceof Herbivore h){
                herbivores.add(h);
            }
        }
        return herbivores;
    }

    
    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public ArrayList<Entity> getAllEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.addAll(getAnimals());
        entities.addAll(getRangers());
        entities.addAll(getVisitors());
        return entities;
    }

    
    public ArrayList<Jeep> getJeeps() {
        return jeeps;
    }
    
    public void initRoadPath(){
        for (Point point : roadPath) {
            tiles[point.x][point.y] = 0;
        }
        if(!roadPath.isEmpty()){
            balance += (road.getPrice() * (roadPath.size() - 1));
        }
        roadPath.clear();
        this.lastRoad = new Point(48,19);
        this.endRoad = new Point(48,29);
        this.tiles[48][19] = 5;
        this.tiles[48][29] = 5;
        this.roadPath.add(lastRoad);
    }
    
    
    public static void incraseBalance(int n){
        balance+=n;
    }

    public ArrayList<Ranger> getRangers() {
        return rangers;
    }

    public Poacher getPoacher() {
        return poacher;
    }

    public void setPoacher(Poacher poacher) {
        this.poacher = poacher;
    }
  
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Point> getRoadPath() {
        return roadPath;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    
    public Point getLastRoad() {
        return lastRoad;
    }

    public Point getEndRoad() {
        return endRoad;
    }

    public Ranger getSelectedRanger() {
        return selectedRanger;
    }

    public void setSelectedRanger(Ranger selectedRanger) {
        this.selectedRanger = selectedRanger;
    }
    
    
    public void setIsLeader(Animal a){
        if((a instanceof Antelope && getAntelopes().get(0) == a) || 
           (a instanceof Giraffe && getGiraffes().get(0) == a) ||
           (a instanceof Cheetah && getCheetahs().get(0) == a) ||
           (a instanceof Lion && getLions().get(0) == a) ||
           (a instanceof Zebra && getZebras().get(0) == a)){
            a.setToLeader();
        }else{
           if(a instanceof Antelope){
               a.setLeader(getAntelopes().get(0));
           }
           if(a instanceof Giraffe){
               a.setLeader(getGiraffes().get(0));
           }
           if(a instanceof Cheetah){
               a.setLeader(getCheetahs().get(0));
           }
           if(a instanceof Lion){
               a.setLeader(getLions().get(0));
           }
           if(a instanceof Zebra){
               a.setLeader(getZebras().get(0));
           }
        }
    }

    //Adding the new animal to the list if it is possible
    public void addAnimal(int aId, int x, int y, boolean isBought) {
        if(aId == 0) return;
        if(aId < 100 || aId >= (100+animalTypeNum)){
            System.err.println("Nincs ilyen állat");
            return;
        }
        Animal a = animalTypes[aId-100].cloneAnimal();
        if (a != null && a.getPrice() <= balance) {
            if(isBought) balance -= a.getPrice();
            a.setCoordinate(new Point(x, y));
            animals.add(a);
            setIsLeader(a);
            if (selectedRanger != null) {
                selectedRanger.setIsSelected(false);
                selectedRanger.setTarget(null);
            }
        }
    }
    
    public void spawnVisitor(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(100) + 1;
        if(randomNumber <= 30){
            visitorSpawnBuffer--;
        }
        if(visitorSpawnBuffer <= 0){
            addVisitor();
            visitorSpawnBuffer = spawnBufferSize;
        }
    }
    
    public void addVisitor(){
        Visitor v = new Visitor(Visitor.spawnPointX, Visitor.spawnPointY, (int)(ticketPrice * Visitor.rate) , 10, 60);
        visitors.add(v);
        balance+= v.getPrice();
    }

    //Adding the new jeep to the list if it is possible
    public void addJeep() {
        if (Jeep.price <= balance && lastRoad.equals(endRoad) && jeeps.size() < roadPath.size()/2) {
            balance -= Jeep.price;
            jeeps.add(new Jeep(new Point(Jeep.spawnX, Jeep.spawnY), 200,1));
        }
    }

    //Adding the new ranger to the list if it is possible
    public void addRanger() {
        if (Ranger.price <= balance) {
            balance -= Ranger.price;
            rangers.add(new Ranger(Ranger.spawnX, Ranger.spawnY, 120, 10));
        }
    }

    public void addItem(int t,int x, int y){
        if(t == 0) return;
        if(t == 1){
            items.add(new Grass(grass.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 2){
            items.add(new Lake(lake.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 3){
            items.add(new Bush(bush.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 4){
            items.add(new Tree(tree.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 5){
            items.add(new Road(road.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 6){
            items.add(new Edge(edge.getPrice(), x*tileSize, y*tileSize));
        }else if(t == 7){
            items.add(new Entrance(entr.getPrice(), x*tileSize, y*tileSize));
        }
    }
    
    public boolean spawnPoacher(int dayNum){
        Random rand = new Random();
        int randomNumber = rand.nextInt(100) + 1;
        if(randomNumber <= 70 && dayNum >= 0 && poacher == null){
            poacherSpawnBuffer--;
        }
        if(poacherSpawnBuffer <= 0){
            poacher = new Poacher(makePoacherCoordinate()[0], makePoacherCoordinate()[1], 0, 100, 60);
            poacherSpawnBuffer = spawnBufferSize + 100;
            return true;
        }
        return false;
    }
    
    public int[] makePoacherCoordinate(){
        Random rand = new Random();
        int x = 0;
        int y = 0;
        do{
            x = rand.nextInt(2400);
            y = rand.nextInt(2400);
        }while(!canSpawn(x, y));
        return new int[]{getTileType(x,y)[0] * tileSize,getTileType(x,y)[1] * tileSize};
    }
   
    private int[] getTileType(int x, int y){
        int[] tileTypes = {y / tileSize, x / tileSize};
        return tileTypes;
    }
    private boolean canSpawn(int x, int y){
        int col = getTileType(x,y)[1];
        int row = getTileType(x,y)[0];
        if(tiles[row][col] == 0 ){
            return true;
        }
        return false;
    }
    /**
     * Checks if a person can be active based on time of day
     * @param person The person to check
     * @param isNightTime Whether it's currently night time
     * @return True if the person can be active, false otherwise
     */
    public boolean isPersonActive(Person person, boolean isNightTime) {
        if (isNightTime) {
            return person.isActiveAtNight();
        }
        return true; // Everyone is active during the day
    }

}
