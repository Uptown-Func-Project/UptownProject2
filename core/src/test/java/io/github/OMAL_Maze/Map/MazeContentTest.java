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

        assertTrue(foundAnyWalls, "At least one level should have walls");
        assertTrue(foundAnySeeds, "At least one level should have seeds");
    }
}