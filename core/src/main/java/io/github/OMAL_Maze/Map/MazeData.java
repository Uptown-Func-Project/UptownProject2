package io.github.OMAL_Maze.Map;
import io.github.OMAL_Maze.Entities.EntityData;

import java.util.List;
import java.util.Map;

public class MazeData {
    private Map<String, LevelData> levels;

    // Constructor to handle the root object
    public MazeData() {
        this.levels = new java.util.HashMap<>();
    }

    public LevelData getLevel(String levelName) {
        return levels.get(levelName);
    }

    public Map<String, LevelData> getAllLevels() {
        return levels;
    }

    public void setLevels(Map<String, LevelData> levels) {
        this.levels=levels;
    }

    public static class LevelData {
        private String backgroundImage;
        private int[][] walls;
        private List<EntityData> entities;
        private List<BuildingData> buildings;
        private List<TriggerZone> triggerZones;

        public int[][] getWalls() {
            return walls;
        }

        public List<EntityData> getEntities() {
            return entities;
        }

        public List<BuildingData> getBuildings() {
            return buildings;
        }

        public List<TriggerZone> getTriggerZones() {
            return triggerZones;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }
    }
}