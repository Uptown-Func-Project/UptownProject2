package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class MazeLoader {
    public static MazeData loadMaze(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        String json = file.readString();
        Gson gson = new Gson();
        JsonObject rootObject = JsonParser.parseString(json).getAsJsonObject();
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        for (String key : rootObject.keySet()) {
            MazeData.LevelData levelData = gson.fromJson(rootObject.get(key), MazeData.LevelData.class);
            levels.put(key, levelData);
        }
        MazeData mazeData = new MazeData();
        mazeData.setLevels(levels);
        return mazeData;
    }
}
