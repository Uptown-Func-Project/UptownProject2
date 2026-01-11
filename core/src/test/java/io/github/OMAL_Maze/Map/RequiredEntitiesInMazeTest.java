package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.Gdx;
import io.github.OMAL_Maze.Entities.EntityData;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RequiredEntitiesInMazeTest {

    @Test
    void mazeContainsKeyGameplayEntities() {
        MazeData data = MazeLoader.loadMaze("loadAssets/assets.json");
        assertNotNull(data);

        Set<String> types = new HashSet<>();
        int coinCount = 0;

        for (Map.Entry<String, MazeData.LevelData> entry : data.getAllLevels().entrySet()) {
            MazeData.LevelData level = entry.getValue();
            if (level == null || level.getEntities() == null) continue;
            for (EntityData e : level.getEntities()) {
                if (e == null) continue;
                types.add(e.getType());
                if ("Coin".equals(e.getType())) coinCount++;
            }
        }

        assertTrue(types.contains("Professor"), "Maze should contain a Professor (quiz guy!!)");
        assertTrue(types.contains("degreeGuy"), "Maze should contain degreeGuy (central hall degree pickup guy)");
        assertTrue(types.contains("Dean"), "Maze should contain Dean (end obstacle guy)");
        assertTrue(types.contains("Puddle"), "Maze should contain Puddle");
        assertTrue(Gdx.files.internal("entityTextures/batss.png").exists(), "Bat texture asset should exist");
        assertTrue(types.contains("Goose") || types.contains("Geesey"), "Maze should contain geese obstacles");
        assertTrue(coinCount > 0, "Maze should contain coins placed around the map");
    }
}
