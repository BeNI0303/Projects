package Model;

import MovingEntity.*;
import java.awt.Point;
import java.util.ArrayList;

public class SavedGameData {
    
    private final int balance;
    private double rating;
    private final ArrayList<Visitor> visitors;
    private final ArrayList<Jeep> jeeps;
    private final ArrayList<Animal> animals;
    private final ArrayList<Ranger> rangers;
    private final Poacher poacher;
    private ArrayList<Point> roadPath;
    private Point lastRoad;
    private Point endRoad;
    public transient int tiles[][];     //transient because tiles is loaded from map txt

    public SavedGameData() {
        this.balance = 0;
        this.rating = 0;
        this.visitors = new ArrayList<>();
        this.jeeps = new ArrayList<>();
        this.animals = new ArrayList<>();
        this.rangers = new ArrayList<>();
        this.poacher = null;
        this.roadPath = new ArrayList<>();
        this.lastRoad = null;
        this.endRoad = null;
        this.tiles = null;
    }
    
    public SavedGameData(int balance, double rating, ArrayList<Visitor> visitors, ArrayList<Jeep> jeeps,
                         ArrayList<Animal> animals, ArrayList<Ranger> rangers, Poacher poacher,
                         ArrayList<Point> roadPath, Point lastRoad, Point endRoad) {
        
        this.balance = balance;
        this.rating = rating;
        this.visitors = visitors;
        this.jeeps = jeeps;
        this.animals = animals;
        this.rangers = rangers;
        this.poacher = poacher;
        this.roadPath = roadPath;
        this.lastRoad = lastRoad;
        this.endRoad = endRoad;
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public ArrayList<Jeep> getJeeps() {
        return jeeps;
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public ArrayList<Ranger> getRangers() {
        return rangers;
    }

    public Poacher getPoacher() {
        return poacher;
    }

    public ArrayList<Point> getRoadPath() {
        return roadPath;
    }

    public Point getLastRoad() {
        return lastRoad;
    }

    public Point getEndRoad() {
        return endRoad;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "SavedGameData{" +
               "balance=" + balance +
               ", visitors=" + visitors +
               ", jeeps=" + jeeps +
               ", animals=" + animals +
               ", rangers=" + rangers +
               ", poacher=" + poacher +
               ", roadPath=" + roadPath +
               '}';
    }
}
