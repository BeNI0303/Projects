package Services;

import Model.SavedGameData;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;

//https://www.geeksforgeeks.org/convert-java-object-to-json-string-using-jackson-api/
/**
 * Service class for handling the saving of the current game status
 */
public class SaveLoadGameService {
    
    private final static String PATH = System.getProperty("user.dir") + "/src/main/resources/";
    
    public static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Saves the map into a .txt file
     * @param filename
     * @param tiles 
     */
    public static void saveMapToFile(String filename, int[][] tiles) {

        String filePath = PATH + "/map/" + filename + ".txt";
        System.out.println("Map txt saved to: " + filePath);
        
        try {
            RandomAccessFile mapFile = new RandomAccessFile(filePath, "rw");
            
            mapFile.setLength(0);
            
            String row = "";
            
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    mapFile.writeBytes(tiles[i][j] + " ");
                }
                mapFile.seek(mapFile.getFilePointer() - 1);     //remove end whitespace
                mapFile.writeBytes("\n");
            }
            
            mapFile.close();
        } catch (IOException exc) {
            System.out.println("File exception: " + exc);
        }
    }
    
    /**
     * Saves the game state data as a JSON file
     * @param filename
     * @param data 
     */
    public static void saveGameState(String filename, SavedGameData data) {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);    //formatted instead of single line print
        try {
            String jsonString = objectMapper.writeValueAsString(data);
            objectMapper.writeValue(new File(PATH + "/saved_game_data/" + filename + ".json"), data);
        } catch (Exception exc) {
            System.out.println("Exception when saving json file: " + exc);
        }
    }
    
    /**
     * Loads the game state data from JSON file into a SavedGameData object
     * @param filename
     */
    public static SavedGameData loadSavedGameState(String filename) { 
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);    //formatted instead of single line print
        File file = new File(PATH  + "saved_game_data/" + filename + ".json");
        System.out.println("Json file path: " + PATH  + "saved_game_data/" + filename + ".json");
        try {
            return objectMapper.readValue(file, SavedGameData.class);
        } catch (Exception exc) {
            System.out.println("Exception when loading json file: " + exc);
            return null;    //handle it later!!!
        }
    }
    
    public static void saveGame(String saveName, int[][] tiles, SavedGameData data) {
        saveMapToFile(saveName, tiles);
        saveGameState(saveName, data);
    }
}
