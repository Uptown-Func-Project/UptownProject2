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

class DeanBehaviorTest {
    private Main main;
    private Texture texture;

    @BeforeEach
    void setUp() {
        main = new Main();
        main.tileSize = 40;
        main.viewport = new FitViewport(880, 880);
        main.entities = new Array<>();
        texture = new Texture(Gdx.files.internal("dummy.png"));
    }

    @Test
    void deanSpawnsWhenPlayerEntersTriggerWithoutDegree() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        player.hasDegree = false;
        Main.player = player;
        main.entities.add(player);

        Dean dean = new Dean(0, 0, 16, 16, texture, "dean");

        Field hidden = Main.class.getDeclaredField("hiddenEventsRemaining");
        hidden.setAccessible(true);
        int hiddenBefore = hidden.getInt(main);

        player.sprite.setX(dean.spawnTrigger.x + 1);
        player.sprite.setY(dean.spawnTrigger.y + 1);

        dean.logic();

        assertTrue(dean.visible, "Dean should be visible after spawning");
        assertEquals(Dean.deanState.ANGRY, dean.state, "Dean should spawn angry by default");
        assertTrue(dean.isMoving, "Dean should start moving on spawn");
        assertEquals(hiddenBefore - 1, hidden.getInt(main), "Spawning dean should decrement hidden events by 1");
    }

    @Test
    void deanDoesNotSpawnWhenPlayerHasDegree() {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        player.hasDegree = true;
        Main.player = player;
        main.entities.add(player);

        Dean dean = new Dean(0, 0, 16, 16, texture, "dean");

        player.sprite.setX(dean.spawnTrigger.x + 1);
        player.sprite.setY(dean.spawnTrigger.y + 1);

        dean.logic();

        assertFalse(dean.visible, "Dean should not spawn when player already has a degree");
    }

    @Test
    void deanBecomesHappyAndRewardsPlayerWhenDegreeShown() throws Exception {
        Player player = new Player(100, 100, 16, 16, texture, "player");
        player.hasDegree = true;
        Main.player = player;
        main.entities.add(player);

        main.setSecondsRemaining(100);
        float speedBefore = player.speed;

        Dean dean = new Dean(100, 100, 16, 16, texture, "dean");
        dean.visible = true;
        dean.state = Dean.deanState.ANGRY;
        dean.isMoving = true;
        main.entities.add(dean);

        Field good = Main.class.getDeclaredField("goodEventsRemaining");
        good.setAccessible(true);
        int goodBefore = good.getInt(main);

        dean.sprite.setPosition(player.sprite.getX(), player.sprite.getY());

        dean.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(Dean.deanState.HAPPY, dean.state, "Dean should become happy when player has degree");
        assertEquals(speedBefore * 2f, player.speed, 0.0001f, "Player should be rewarded with speed boost");
        assertEquals(130, main.getSecondsRemaining(), "Player should be rewarded with 30 extra seconds");
        assertEquals(goodBefore - 1, good.getInt(main), "Showing degree should decrement good events by 1");
    }
}
