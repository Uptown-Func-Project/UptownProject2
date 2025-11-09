package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class MazeLoader {
    /**
     * Static, called to load the data from an assets json file into a MazeData class.
     * Uses the Google "GSON" package to put the data into the class attributes.
     * @param fileName name of the JSON file to load.
     * @return MazeData object that includes all entities, buildings, texture paths, and level change triggers.
     */
    public static MazeData loadMaze(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        String json = file.readString();
        Gson gson = new Gson();
        //Parse the file into a json object.
        JsonObject rootObject = JsonParser.parseString(json).getAsJsonObject();
        //Create a hashmap to store the files in. This is needed as the json loader cannot automatically map them.
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        //Iterates through the keys to put each level into a class.
        for (String key : rootObject.keySet()) {
            MazeData.LevelData levelData = gson.fromJson(rootObject.get(key), MazeData.LevelData.class);
            levels.put(key, levelData);
        }
        //Spawns an instance of the MazeData object so that it can be returned.
        MazeData mazeData = new MazeData();
        //Puts the levels into the MazeData class map
        mazeData.setLevels(levels);
        return mazeData;
    }
}