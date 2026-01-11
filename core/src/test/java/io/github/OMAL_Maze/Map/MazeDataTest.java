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
    void testMazeDataSetLevels() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        assertDoesNotThrow(() -> mazeData.setLevels(levels), "Should set levels without throwing");
    }

    @Test
    void testMazeDataGetLevel() {
        assertNull(mazeData.getLevel("non-existent"), "Non-existent level should return null");
    }

    void testMazeDataEmptyLevels() {
        Map<String, MazeData.LevelData> levels = mazeData.getAllLevels();
        assertTrue(levels.isEmpty(), "New MazeData should have empty levels");
    }

    void testMazeDataAddLevelAndRetrieve() {
        Map<String, MazeData.LevelData> levels = new HashMap<>();
        MazeData.LevelData levelData = new MazeData.LevelData();
        levels.put("TestLevel", levelData);
        
        mazeData.setLevels(levels);
        
        assertNotNull(mazeData.getLevel("TestLevel"), "Should retrieve added level");
    }
}
