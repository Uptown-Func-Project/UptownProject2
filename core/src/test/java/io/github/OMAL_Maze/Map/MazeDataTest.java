package io.github.OMAL_Maze.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

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
    void testMazeDataSetLevels() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        assertDoesNotThrow(() -> mazeData.setLevels(levels), "Should set levels without throwing");
    }

    @Test
    void testMazeDataGetLevel() {
        assertNull(mazeData.getLevel("non-existent"), "Non-existent level should return null");
    }

    @Test
    void testMazeDataEmptyLevels() {
        Map<String, MazeData.LevelData> levels = mazeData.getAllLevels();
        assertTrue(levels.isEmpty(), "New MazeData should have empty levels");
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
