package io.github.OMAL_Maze;
import java.util.List;

public class MazeData {
    private int[][] walls;
    private List<EntityData> entities;

    public int[][] getWalls() {
        return walls;
    }

    public List<EntityData> getEntities() {
        return entities;
    }
}
