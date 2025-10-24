package io.github.OMAL_Maze;
import java.util.List;

public class MazeData {
    private String backgroundImage;
    private int[][] walls;
    private List<EntityData> entities;
    private List<BuildingData> buildings;

    public int[][] getWalls() {
        return walls;
    }

    public List<EntityData> getEntities() {
        return entities;
    }
    public List<BuildingData> getBuildings() {
        return buildings;
    }
    public String getBackgroundImage() {return this.backgroundImage;}
}
