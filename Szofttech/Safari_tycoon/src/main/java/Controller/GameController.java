package Controller;

import Items.Item;
import Items.Lake;
import Items.Road;
import Model.Model;
import Model.SavedGameData;
import MovingEntity.Animal;
import MovingEntity.Entity;
import MovingEntity.Herbivore;
import MovingEntity.Jeep;
import MovingEntity.Poacher;
import MovingEntity.Predator;
import MovingEntity.Ranger;
import MovingEntity.Visitor;
import Services.SaveLoadGameService;
import View.GamePanel;
import View.GameWindow;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class GameController implements Runnable {

    private KeyHandler keyH;
    private MouseHandler mouseH;
    private GamePanel gamePanel;
    private Model model;
    private GameWindow gw;
    private Thread gameThread;
    final int FPS = 60;
    public final int NANO_TIME_DIVIDER = 1000000000;

    // Game time variables
    private int gameSpeed = GameWindow.SPEED_HOUR;
    private double timeAccumulator = 0;
    private int elapsedHours = 8; // Start at 8 for better visibility
    private int elapsedDays = 0;
    private double timePerFrame = 1.0 / 120.0;
    private int iteratorForJeep = 0;
    private float darknessFactor = 0.0f; // 0.0 = full day, 1.0 = full night
    private float minuteOfDay = 0.0f; // Track minutes for smoother transitions
    private int breedingBuffer = 20;
    private boolean lost = false;
    private boolean win = false;

    public GameController(KeyHandler keyH, MouseHandler mouseH, GamePanel gp, Model model, GameWindow gw) {
        this.keyH = keyH;
        this.mouseH = mouseH;
        this.gamePanel = gp;
        this.model = model;
        this.gw = gw;
    }

    //Places the item if it is possible
    public void setTileType(int type) {
        int x = getWorldPosition()[0];
        int y = getWorldPosition()[1];

        int tileColumn = (int) x / model.tileSize;
        int tileRow = (int) y / model.tileSize;

        boolean successfulPlacement = model.purchaseItem(type);

        if (successfulPlacement && model.canPlaceOnPosition(tileRow, tileColumn, type)) {

            //System.out.println(model.getBalance());
            if (successfulPlacement) {
                model.tiles[tileRow][tileColumn] = type;
                if (model.tiles[tileRow][tileColumn] != 5 && model.tiles[tileRow][tileColumn] != 0) {
                    model.addItem(type, tileColumn, tileRow);
                }
            } else {
                if (model.getBalance() <= 0) {
                    System.out.println("Not enough balance to place the item.");
                } else {
                    System.out.println("No items selected.");
                }

            }
        } else {
            System.out.println("Can't place it there.");
        }
    }

    //Gives the position on the world from the screen position
    public int[] getWorldPosition() {
        int[] co = {mouseH.mX + gamePanel.getCam().worldP.x - gamePanel.getCam().screenP.x, mouseH.mY + gamePanel.getCam().worldP.y - gamePanel.getCam().screenP.y};
        return co;
    }

    public Point getTileWorldCo(int row, int col) {
        return new Point(col * model.tileSize, row * model.tileSize);
    }

    public void update() {
        gamePanel.requestFocusInWindow();
        gamePanel.getCam().update();

        updateGameTime();

        gw.balanceLabel.setText(String.format("Balance: %d", model.getBalance()));
        //Clicked on the board
        if (mouseH.removeRoad) {
            if (!model.getLastRoad().equals(model.getEndRoad())) {
                model.initRoadPath();
            }
            mouseH.removeRoad = false;
        }
        if (mouseH.removeSelection) {
            for (Ranger ranger : model.getRangers()) {
                if (ranger.isIsSelected()) {
                    ranger.setIsSelected(false);
                }
            }
            mouseH.removeSelection = false;
        }
        if (mouseH.clicked) {
            model.addAnimal(mouseH.selectedAnimal, getWorldPosition()[0], getWorldPosition()[1], true);
            setTileType(mouseH.selectedItem);
            if (mouseH.sellAnimal) {
                sellAnimal();
            }

            Point clickPoint = new Point(getWorldPosition()[0], getWorldPosition()[1]);
            //System.out.println("Currently clicked point: " + clickPoint.toString());

            Ranger selectedRanger = getSelectedRanger();

            if (selectedRanger == null) {
                handleRangerClick(clickPoint);
            } else {
                boolean animalClicked = handleAnimalClick(clickPoint, selectedRanger);
            }
            clickPoint = null;

            mouseH.clicked = false;
        }
        if (mouseH.jeepClicked) {
            model.addJeep();
            mouseH.jeepClicked = false;
        }
        if (mouseH.rangerClicked) {
            model.addRanger();
            mouseH.rangerClicked = false;
        }
        for (int i = 0; i < getSpeedFactor(); i++) {
            if (!isNightTime()) {
                model.spawnVisitor();
            }
            model.spawnPoacher(elapsedDays);
        }

        checkDeath();

        if (gw != null) {
            gw.checkForMinimapToggle(keyH);
        }

        if (gw != null && gw.isMinimapFullscreen()) {
            gw.getMinimap().repaint();
        }

        for (Animal animal : model.getAnimals()) {
            // Apply speed factor to animal movement

            for (int i = 0; i < getSpeedFactor(); i++) {
                model.setIsLeader(animal);
                animalItemCollideCheck(animal);
                animalAnimalCollideCheck(animal);
                animal.update(model.tileSize);
                checkItemValid();
                animalItemCollideCheck(animal);
                animalAnimalCollideCheck(animal);

            }
        }
        lookingForFood();

        breed(model.getAntelopes());
        breed(model.getCheetahs());
        breed(model.getGiraffes());
        breed(model.getLions());
        breed(model.getZebras());

        for (Visitor visitor : model.getVisitors()) {
            for (int i = 0; i < getSpeedFactor(); i++) {
                if (!isNightTime() && !visitor.isInJeep()) {
                    visitorCollideCheck(visitor);
                    visitor.update(model.tileSize);
                    visitorCollideCheck(visitor);
                    visitor.decreaseRate();
                    seeAnimal(visitor);
                    checkGetIn(visitor);
                } else if (!visitor.isInJeep()) {
                    visitor.setCoordinate(new Point(Visitor.spawnPointX, Visitor.spawnPointY));
                }

            }
        }

        for (Ranger ranger : model.getRangers()) {
            for (int i = 0; i < getSpeedFactor(); i++) {
                rangerItemCollideCheck(ranger);
                ranger.update(model.tileSize);
                lookingForPoacher(ranger);
                rangerItemCollideCheck(ranger);
            }

        }

        if (model.getPoacher() != null) {
            for (int i = 0; i < getSpeedFactor(); i++) {
                if (model.getPoacher() != null) {
                    model.getPoacher().update(model.tileSize);
                    killAnimal();
                    poacherItemColldide();
                }
            }
        }

        for (Jeep jeep : model.getJeeps()) {
            seeAnimalFromJeep(jeep);
        }
        visitorDisappear();

        // Apply speed factor to Jeep movement
        JeepMovement();

        if (checkEnd()) {
            gw.gameEndOptionPane();
            gameThread = null;

        }

    }

    private void updateGameTime() {
        timeAccumulator += timePerFrame * getSpeedFactor();

        // Each game hour passes when we accumulate 1.0 unit of time
        if (timeAccumulator >= 1.0) {
            int hoursToAdd = (int) timeAccumulator;
            elapsedHours += hoursToAdd;
            timeAccumulator -= hoursToAdd;

            if (elapsedHours >= 24) {
                elapsedDays += elapsedHours / 24;
                elapsedHours %= 24;
            }

            // Reset minute counter when hour changes
            minuteOfDay = 0;

            gw.updateGameTime(elapsedHours, elapsedDays);
        } else {
            // Update minute of the hour (for smooth transitions)
            minuteOfDay = (float) (timeAccumulator * 60.0);
        }

        // Calculate time of day in minutes (0-1439)
        float timeInMinutes = elapsedHours * 60 + minuteOfDay;

        // Update darkness factor based on time of day
        updateDarknessFactor(timeInMinutes);
    }

    private void updateDarknessFactor(float timeInMinutes) {
        // Dawn starts at 4:00 (240 minutes)
        // Full day at 6:00 (360 minutes)
        // Dusk starts at 21:00 (1260 minutes)
        // Full night at 22:00 (1380 minutes)

        if (timeInMinutes >= 240 && timeInMinutes < 360) {
            // Dawn - gradually getting lighter
            darknessFactor = 1.0f - ((timeInMinutes - 240) / 120.0f);
        } else if (timeInMinutes >= 360 && timeInMinutes < 1260) {
            // Full day
            darknessFactor = 0.0f;
        } else if (timeInMinutes >= 1260 && timeInMinutes < 1380) {
            // Dusk - gradually getting darker
            darknessFactor = (timeInMinutes - 1260) / 120.0f;
        } else {
            // Full night
            darknessFactor = 1.0f;
        }
    }

    public float getDarknessFactor() {
        return darknessFactor;
    }

    public boolean isNightTime() {
        // For compatibility with existing code
        return darknessFactor > 0.5f;
    }

    public void setGameSpeed(int speed) {
        this.gameSpeed = speed;
    }

    public void checkItemValid() {
        for (int i = 0; i < model.getItems().size(); i++) {
            Item item = model.getItems().get(i);
            if (item.getHp() <= 0) {
                model.tiles[item.getCoordinate().y / model.tileSize][item.getCoordinate().x / model.tileSize] = 0;
                model.getItems().remove(i);
            }
        }
    }

    public void seeAnimal(Visitor v) {
        for (Animal animal : model.getAnimals()) {
            v.isAvailable(animal, model.tileSize);
        }
    }

    public void seeAnimalFromJeep(Jeep j) {
        for (Animal animal : model.getAnimals()) {
            j.seeAnimal(animal, model.tileSize);
        }
    }

    public boolean sellAnimal() {
        for (int i = 0; i < model.getAnimals().size(); i++) {
            Animal animal = model.getAnimals().get(i);
            int[] aParams = animal.getDrawImageParams(animal.getCoordinate().x, animal.getCoordinate().y, model.tileSize);
            Rectangle r = new Rectangle(aParams[0], aParams[1], aParams[2], aParams[3]);
            if (r.contains(new Point(getWorldPosition()[0], getWorldPosition()[1]))) {
                model.setBalance(model.getBalance() + animal.getPrice());
                model.getAnimals().remove(i);
                return true;
            }
        }
        return false;
    }

    public void checkDeath() {
        for (int i = model.getAnimals().size() - 1; i >= 0; i--) {
            Animal animal = model.getAnimals().get(i);
            if (animal.getFoodSupply() <= 0 || animal.getWaterSupply() <= 0 || animal.getAge() >= 100 || animal.getHp() <= 0) {

                model.getAnimals().remove(animal);
                Ranger selectedRanger = getSelectedRanger();
                if (selectedRanger != null && selectedRanger.getTarget() == animal) {
                    selectedRanger.setIsSelected(false);
                    selectedRanger.setTarget(null);
                    //System.out.println("Ranger got deselected after kill");
                }
            }

        }
        if (model.getPoacher() != null && model.getPoacher().getHp() == 0) {
            model.setPoacher(null);
        }
    }

    private void lookingForFood() {
        for (Animal animal : model.getAnimals()) {
            for (Item item : model.getItems()) {
                int[] i = item.getRangeBoxParams(item.getCoordinate().x, item.getCoordinate().y, model.tileSize);
                if (item instanceof Lake) {
                    if (animal.seeFood(i[0], i[1], i[2], i[3], model.tileSize)) {
                        animal.addSource(item);
                    }
                } else {
                    if (animal instanceof Herbivore) {
                        if (animal.seeFood(i[0], i[1], i[2], i[3], model.tileSize)) {
                            animal.addSource(item);
                        }
                    }
                }
            }
            if (animal instanceof Predator) {
                for (Animal food : model.getAnimals()) {
                    int[] f = food.getRangeBoxParams(food.getCoordinate().x, food.getCoordinate().y, model.tileSize);
                    if (animal.seeFood(f[0], f[1], f[2], f[3], model.tileSize)) {

                    }
                }
            }

        }
    }

    public void lookingForPoacher(Ranger ranger) {
        if (model.getPoacher() == null) {
            return;
        }
        Poacher poacherToCheck = model.getPoacher();
        int[] poacherBox = poacherToCheck.getHitBoxParams(poacherToCheck.getCoordinate().x, poacherToCheck.getCoordinate().y, model.tileSize);
        if (ranger.seePoacher(poacherBox[0], poacherBox[1], poacherBox[2], poacherBox[3], model.tileSize)) {
            model.setPoacher(null);
        }
    }

    public void breed(ArrayList<Animal> a) {
        if (canBreed(a)) {
            if (breedingBuffer > 0) {
                breedingBuffer--;
            } else {
                breedingBuffer = 20;
                Random rand = new Random();
                int randomNumber = rand.nextInt(100) + 1;
                if (randomNumber <= 20) {
                    model.addAnimal(a.getLast().getId(), a.getLast().getCoordinate().x + model.tileSize, a.getLast().getCoordinate().y + model.tileSize, false);
                }
            }
        }
    }

    public boolean canBreed(ArrayList<Animal> a) {
        if (a.size() < 2) {
            return false;
        }
        int readyCount = 0;

        for (Animal animal : a) {
            if (animal.getAge() >= 10
                    && animal.getWaterSupply() >= 80
                    && animal.getFoodSupply() >= 80) {
                readyCount++;
            }

            if (readyCount >= 2) {
                return true;
            }
        }

        return false;
    }

    public void animalItemCollideCheck(Animal animal) {
        for (Item item : model.getItems()) {
            int[] i = item.getHitBoxParams(item.getCoordinate().x, item.getCoordinate().y, model.tileSize);
            if (animal.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                animal.noItemOnTheWay();
                animal.setDirectionAfterCollide();
                while (animal.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                    animal.defaultMove();
                }
            }
        }
    }

    public void animalAnimalCollideCheck(Animal animal1) {
        for (Animal animal2 : model.getAnimals()) {
            int[] o = animal2.getHitBoxParams(animal2.getCoordinate().x, animal2.getCoordinate().y, model.tileSize);
            if (animal1 != animal2 && animal1.collide(o[0], o[1], o[2], o[3], model.tileSize)) {
                animal1.noItemOnTheWay();
                animal2.setDirectionAfterCollide();
                animal1.setDirectionAfterCollide();
                animal1.defaultMove();
                animal2.defaultMove();
            }
        }

    }

    public void visitorCollideCheck(Visitor visitor) {
        for (Item item : model.getItems()) {
            int[] i = item.getHitBoxParams(item.getCoordinate().x, item.getCoordinate().y, model.tileSize);
            if (visitor.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                visitor.setDirectionAfterCollide();
                while (visitor.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                    visitor.defaultMove();
                }
            }
        }
        for (Visitor visitor1 : model.getVisitors()) {
            int[] o = visitor1.getHitBoxParams(visitor1.getCoordinate().x, visitor1.getCoordinate().y, model.tileSize);
            if (visitor != visitor1 && visitor.collide(o[0], o[1], o[2], o[3], model.tileSize)) {
                visitor1.setDirectionAfterCollide();
                visitor.setDirectionAfterCollide();
                visitor.defaultMove();
                visitor1.defaultMove();
            }
        }
        for (Animal animal : model.getAnimals()) {
            int[] o = animal.getHitBoxParams(animal.getCoordinate().x, animal.getCoordinate().y, model.tileSize);
            if (visitor.collide(o[0], o[1], o[2], o[3], model.tileSize)) {
                animal.setDirectionAfterCollide();
                visitor.setDirectionAfterCollide();
                visitor.defaultMove();
                animal.defaultMove();
            }
        }
        Poacher p = model.getPoacher();
        if (p != null) {
            int[] o = p.getHitBoxParams(p.getCoordinate().x, p.getCoordinate().y, model.tileSize);
            if (visitor.collide(o[0], o[1], o[2], o[3], model.tileSize)) {
                p.setDirectionAfterCollide();
                visitor.setDirectionAfterCollide();
                visitor.defaultMove();
                p.defaultMove();
            }
        }
    }

    public void rangerItemCollideCheck(Ranger r) {
        for (Item item : model.getItems()) {
            int[] i = item.getHitBoxParams(item.getCoordinate().x, item.getCoordinate().y, model.tileSize);
            if (r.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                if (r.isIsSelected()) {
                    r.noItemOnTheWay();
                }
                r.setDirectionAfterCollide();
                while (r.collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                    r.defaultMove();
                }
            }
        }
    }

    private void poacherItemColldide() {
        for (Item item : model.getItems()) {
            if (model.getPoacher() == null) {
                return;
            }
            int[] i = item.getHitBoxParams(item.getCoordinate().x, item.getCoordinate().y, model.tileSize);
            if (model.getPoacher().collide(i[0], i[1], i[2], i[3], model.tileSize)) {
                model.getPoacher().setDirectionAfterCollide();
                model.getPoacher().defaultMove();
            }
        }
    }

    private void killAnimal() {
        for (Animal animal : model.getAnimals()) {
            if (model.getPoacher() == null) {
                return;
            }
            int[] animalParam = animal.getHitBoxParams(animal.getCoordinate().x, animal.getCoordinate().y, model.tileSize);
            if (model.getPoacher() != null && model.getPoacher().seeAnimal(animalParam[0], animalParam[1], animalParam[2], animalParam[3], model.tileSize)) {
                animal.setHp(0);
                model.setPoacher(null);
            }
        }
    }

    public void JeepMovement() {
        for (int speedIteration = 0; speedIteration < getSpeedFactor(); speedIteration++) {
            for (int i = 0; i < model.getJeeps().size(); i++) {
                int iter = model.getJeeps().get(i).getIteratorForRoad();
                if (iter < model.getRoadPath().size() && model.getEndRoad().equals(model.getLastRoad())) {
                    if (i == 0 || model.getJeeps().get(i - 1).isUnlocked()) {
                        Point p = getTileWorldCo(model.getRoadPath().get(iter).x, model.getRoadPath().get(iter).y);
                        boolean reached = model.getJeeps().get(i).move(p);
                        if (reached) {
                            model.getJeeps().get(i).incrementReachedTileCount();
                        }
                        if (model.getJeeps().get(i).getIteratorForRoad() >= model.getRoadPath().size()) {
                            Jeep first = model.getJeeps().remove(0);
                            visitorsGetOutOfTheJeep(first);
                            first.setIteratorForRoad(1);
                            first.setCoordinate(new Point(912, 2304));
                            first.setUnlocked(false);
                            first.setReachedTileCount(0);
                            model.getJeeps().add(first);
                        }
                    }
                }

            }
        }

    }

    public void visitorDisappear() {
        for (int i = 0; i < model.getVisitors().size(); i++) {
            Visitor visitor = model.getVisitors().get(i);
            if (visitor.getTime() <= 0) {
                model.getVisitors().remove(i);
            }
        }

    }

    public void visitorsGetOutOfTheJeep(Jeep j) {
        ArrayList<Visitor> vs = j.getVisitors();
        for (int i = 0; i < vs.size(); i++) {
            vs.get(i).setCoordinate(new Point(Visitor.spawnPointX, Visitor.spawnPointY));
            vs.get(i).setInJeep(false);
        }
        vs.clear();
    }

    public void checkGetIn(Visitor v) {
        for (Jeep jeep : model.getJeeps()) {
            jeep.getIn(v, model.tileSize, model);
        }
    }

    public boolean checkEnd() {
        if (model.getBalance() <= 0) {
            lost = true;
            return true;
        }
        if (elapsedDays % 2 == 0 && elapsedDays > 0) {
            if (model.getPredators().size() < 2) {
                lost = true;
                return true;
            } else if (model.getHerbivores().size() < 5) {
                lost = true;
                return true;
            }
        }
        if (model.diff.equals("easy")) {
            if (elapsedDays >= 30) {
                win = true;
                return true;
            }
        } else if (model.diff.equals("medium")) {
            if (elapsedDays >= 60) {
                win = true;
                return true;
            }
        } else if (model.diff.equals("hard")) {
            if (elapsedDays >= 90) {
                win = true;
                return true;
            }
        }
        return false;
    }

    public Ranger getSelectedRanger() {
        return model.getRangers().stream().filter(Ranger::isIsSelected)
                .findFirst().orElse(null);
    }

    //SETS THE SELECTED RANGER'S TARGET TO THE CLICKED ANIMAL
    public boolean handleAnimalClick(Point point, Ranger r) {
        if (r.getTarget() != null) {
            return false;
        }

        for (Animal animal : model.getAnimals()) {
            //System.out.println("Checking animal at " + animal.getCoordinate());
            if (wasEntityClicked(animal, point)) {
                //System.out.println("Animal was clicked!");
                r.setTarget(animal);
                return true;
            }
        }
        return false;
    }

    //SETS THE SELECTED RANGER IF ONE WAS CLICKEDü
    //MAKING SURE THAT ONLY ONE RANGER IS SELECTED AT ALL TIMES
    public void handleRangerClick(Point point) {

        Ranger selectedRanger = null;

        for (Ranger ranger : model.getRangers()) {
            boolean isClicked = wasEntityClicked(ranger, point);
            ranger.setIsSelected(isClicked);
            //System.out.println("Ranger got clicked");
            if (isClicked) {
                selectedRanger = ranger;
                ranger.setTarget(null);
                return;
            }
        }
    }

    //CHECKS IF AN ENTITY HAS BEEN CLICKED
    //BY CALCULATING IF CLICK POINT IS INSIDE THE ENTITY HITBOX
    public boolean wasEntityClicked(Entity entity, Point clickedPoint) {

        Point coord = entity.getCoordinate();
        int[] hitBox = entity.getDrawImageParams(coord.x, coord.y, model.tileSize);

        Rectangle entityHitbox = new Rectangle(hitBox[0], hitBox[1], hitBox[2], hitBox[3]);

        if (entityHitbox.contains(clickedPoint)) {
            //System.out.println("Entity clicked at: " + clickedPoint + " entity: " + entity);
            return true;
        }
        return false;
    }

    private int getSpeedFactor() {
        switch (gameSpeed) {
            case GameWindow.SPEED_HOUR:
                return 1;
            case GameWindow.SPEED_HALF_DAY:
                return 12;
            case GameWindow.SPEED_DAY:
                return 24;
            default:
                return 1;
        }
    }

    public boolean isLost() {
        return lost;
    }

    public boolean isWin() {
        return win;
    }

    public void gameStart() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = NANO_TIME_DIVIDER / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1) {
                update();
                gamePanel.repaint();
                delta--;
                drawCount++;
            }

            if (timer >= NANO_TIME_DIVIDER) {
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;

            }
        }
    }

    //saves the map and game state
    public void saveGame(String saveName) {
        double rating = 0;
        if (model.getVisitors().size() > 0) {
            rating = Visitor.rate;
        }
        SavedGameData dataToSave = new SavedGameData(model.getBalance(), rating, model.getVisitors(), model.getJeeps(),
                model.getAnimals(), model.getRangers(), model.getPoacher(), model.getRoadPath(),
                model.getLastRoad(), model.getEndRoad());

        SaveLoadGameService.saveGame(saveName, model.tiles, dataToSave);
    }

}
