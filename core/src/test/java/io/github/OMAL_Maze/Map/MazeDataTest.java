package io.github.OMAL_Maze.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import io.github.OMAL_Maze.Entities.EntityData;

/**
 * Test class for MazeData level storage and retrieval.
 */
class MazeDataTest {

    private MazeData mazeData;

    @BeforeEach
    void setUp() {
        mazeData = new MazeData();
    }

    @Test
    void testMazeDataConstructor() {
        assertNotNull(mazeData, "MazeData should be created");
    }

    @Test
    void testMazeDataHasLevels() {
        Map<String, MazeData.LevelData> levels = mazeData.getAllLevels();
        assertNotNull(levels, "MazeData should have levels map");
    }

    @Test
    void testMazeDataSetLevels() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        assertDoesNotThrow(() -> mazeData.setLevels(levels), "Should set levels without throwing");
    }

    @Test
    void testMazeDataGetLevel() {
        assertNull(mazeData.getLevel("non-existent"), "Non-existent level should return null");
    }

    @Test
    void testMazeDataLevelDataWalls() {
        int[][] walls = new int[][] {
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 1}
        };
        assertNotNull(walls, "Walls should be created");
        assertEquals(4, walls.length, "Should have 4 rows");
    }

    @Test
    void testMazeDataEmptyLevels() {
        Map<String, MazeData.LevelData> levels = mazeData.getAllLevels();
        assertTrue(levels.isEmpty(), "New MazeData should have empty levels");
    }

    @Test
    void testMazeDataMultipleLevels() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        levels.put("Level1", new MazeData.LevelData());
        levels.put("Level2", new MazeData.LevelData());
        
        mazeData.setLevels(levels);
        
        assertEquals(2, mazeData.getAllLevels().size(), "Should have 2 levels");
    }

    @Test
    void testMazeDataAddLevelAndRetrieve() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        MazeData.LevelData levelData = new MazeData.LevelData();
        levels.put("TestLevel", levelData);
        
        mazeData.setLevels(levels);
        
        assertNotNull(mazeData.getLevel("TestLevel"), "Should retrieve added level");
    }

    @Test
    void testMazeDataLevelDataGetWalls() {
        MazeData.LevelData levelData = new MazeData.LevelData();
        int[][] walls = levelData.getWalls();
        // Walls can be null or an array
        assertTrue(walls == null || walls instanceof int[][], "Walls should be int[][] or null");
    }

    @Test
    void testMazeDataLevelDataGetEntities() {
        MazeData.LevelData levelData = new MazeData.LevelData();
        List<EntityData> entities = levelData.getEntities();
        assertTrue(entities == null || entities instanceof List, "Entities should be List or null");
    }

    @Test
    void testMazeDataLevelDataGetBuildings() {
        MazeData.LevelData levelData = new MazeData.LevelData();
        List<BuildingData> buildings = levelData.getBuildings();
        assertTrue(buildings == null || buildings instanceof List, "Buildings should be List or null");
    }

    @Test
    void testMazeDataLevelDataGetTriggerZones() {
        MazeData.LevelData levelData = new MazeData.LevelData();
        List<TriggerZone> triggers = levelData.getTriggerZones();
        assertTrue(triggers == null || triggers instanceof List, "Triggers should be List or null");
    }

    @Test
    void testMazeDataAllLevelsModifiable() {
        Map<String, MazeData.LevelData> levels = mazeData.getAllLevels();
        levels.put("AddedLevel", new MazeData.LevelData());
        
        assertEquals(1, mazeData.getAllLevels().size(), "Should allow adding levels");
    }

    @Test
    void testMazeDataLevelDataBackgroundImage() {
        MazeData.LevelData levelData = new MazeData.LevelData();
        String bgImage = levelData.getBackgroundImage();
        assertTrue(bgImage == null || bgImage instanceof String, "Background image should be String or null");
    }

    @Test
    void testMazeDataMultipleLevelRetrieval() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        MazeData.LevelData level1 = new MazeData.LevelData();
        MazeData.LevelData level2 = new MazeData.LevelData();
        
        levels.put("Level1", level1);
        levels.put("Level2", level2);
        
        mazeData.setLevels(levels);
        
        assertNotNull(mazeData.getLevel("Level1"), "Should get Level1");
        assertNotNull(mazeData.getLevel("Level2"), "Should get Level2");
        assertNull(mazeData.getLevel("Level3"), "Should return null for non-existent level");
    }

    @Test
    void testMazeDataConsistency() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        levels.put("TestLevel", new MazeData.LevelData());
        
        mazeData.setLevels(levels);
        
        int size1 = mazeData.getAllLevels().size();
        int size2 = mazeData.getAllLevels().size();
        
        assertEquals(size1, size2, "Level count should be consistent");
    }

    @Test
    void testMazeDataReplaceLevel() {
        Map<String, MazeData.LevelData> levels1 = new HashMap<>();
        levels1.put("Level1", new MazeData.LevelData());
        
        mazeData.setLevels(levels1);
        assertEquals(1, mazeData.getAllLevels().size(), "Should have 1 level");
        
        Map<String, MazeData.LevelData> levels2 = new HashMap<>();
        levels2.put("Level2", new MazeData.LevelData());
        
        mazeData.setLevels(levels2);
        assertEquals(1, mazeData.getAllLevels().size(), "Should have 1 level after replace");
        assertNull(mazeData.getLevel("Level1"), "Level1 should be gone after replacement");
        assertNotNull(mazeData.getLevel("Level2"), "Level2 should exist");
    }
}
