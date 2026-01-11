package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Map.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GeeseySpawnTest {
    private Main main;
    private Texture texture;

    @BeforeEach
    void setUp() {
        main = new Main();
        main.tileSize = 40;
        main.viewport = new FitViewport(880, 880);
        main.entities = new Array<>();
        Main.buildings = new Array<>();
        texture = new Texture(Gdx.files.internal("dummy.png"));
    }

    @Test
    void geeseySpawnsWhenPlayerHasBat() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        player.hasBat = true;
        Main.player = player;
        main.entities.add(player);

        Geesey geesey = new Geesey(0, 0, 16, 16, texture, "geesey");
        main.entities.add(geesey);

        Field hidden = Main.class.getDeclaredField("hiddenEventsRemaining");
        hidden.setAccessible(true);
        int hiddenBefore = hidden.getInt(main);

        geesey.logic();

        assertTrue(geesey.visible, "Geesey should become visible after spawning");
        assertEquals(Geesey.gooseState.ANGRY, geesey.state, "Geesey should spawn angry by default");
        assertTrue(geesey.isMoving, "Geesey should start moving on spawn");
        assertEquals(hiddenBefore - 1, hidden.getInt(main), "Spawning geesey should decrement hidden events by 1");

        geesey.movement(0.1f, main.entities, new Array<Building>());
    }
}
