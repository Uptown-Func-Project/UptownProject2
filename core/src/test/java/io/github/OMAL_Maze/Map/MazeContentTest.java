package io.github.OMAL_Maze.Map;

import io.github.OMAL_Maze.Entities.EntityData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MazeContentTest {

    @Test
    void mazeContainsWallsAndSeeds() throws Exception {
        MazeData data = MazeLoader.loadMaze("loadAssets/assets.json");
        Map<String, MazeData.LevelData> levels = data.getAllLevels();
        assertNotNull(levels);
        assertFalse(levels.isEmpty());

        boolean foundAnySeeds = false;
        boolean foundAnyWalls = false;

        for (MazeData.LevelData level : levels.values()) {
            int[][] walls = level.getWalls();
            if (walls != null && walls.length > 0) foundAnyWalls = true;

            List<EntityData> entities = level.getEntities();
            if (entities != null) {
                for (EntityData e : entities) {
                    if (e != null && e.getType() != null && e.getType().equalsIgnoreCase("Seeds")) {
                        foundAnySeeds = true;
                        break;
                    }
                }
            }
        }

        assertTrue(foundAnyWalls, "At least one level should define walls in assets.json");
        assertTrue(foundAnySeeds, "At least one level should define Seeds entities in assets.json");
    }

    @Test
    void mainCreatesWallBuildingsFromMazeWalls() throws Exception {
        MazeData data = MazeLoader.loadMaze("loadAssets/assets.json");
        MazeData.LevelData level1 = data.getLevel("level_1");
        assertNotNull(level1);

        io.github.OMAL_Maze.Main main = new io.github.OMAL_Maze.Main();
        main.tileSize = 40;

        java.lang.reflect.Method createBuildings = io.github.OMAL_Maze.Main.class.getDeclaredMethod("createBuildings", MazeData.LevelData.class);
        createBuildings.setAccessible(true);

        com.badlogic.gdx.utils.Array<Building> buildings = (com.badlogic.gdx.utils.Array<Building>) createBuildings.invoke(main, level1);
        assertNotNull(buildings);
        assertTrue(buildings.size > 0, "Walls should be converted into buildings");

        boolean foundInvisibleWall = false;
        for (int i = 0; i < buildings.size; i++) {
            if (!buildings.get(i).getVisible()) {
                foundInvisibleWall = true;
                break;
            }
        }
        assertTrue(foundInvisibleWall, "Walls should be spawned as invisible building objects");
    }
}