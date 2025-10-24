package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;

public class MazeLoader {
    public static MazeData loadMaze(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        String json = file.readString();
        Gson gson = new Gson();
        return gson.fromJson(json, MazeData.class);
    }
}
