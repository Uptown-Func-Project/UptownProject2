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

class GooseBehaviorTest {
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
    void gooseBitesPlayerWhenNoSeeds() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        Main.player = player;
        main.entities.add(player);

        Goose goose = new Goose(20, 0, 16, 16, texture, "goose");
        goose.isMoving = true;
        goose.state = Goose.gooseState.ANGRY;
        main.entities.add(goose);

        int beforeHearts = player.hearts;
        goose.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(beforeHearts - 1, player.hearts, "Goose bite should decrease hearts by 1");
        assertTrue(goose.bitPlayer, "Goose should enter bite cooldown");
        assertFalse(goose.isMoving, "Goose should stop moving after biting");
    }

    @Test
    void gooseBecomesHappyAndStopsAttackingWhenGivenSeeds() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        player.hasSeeds = true;
        Main.player = player;
        main.entities.add(player);

        main.setSecondsRemaining(100);
        float beforeSpeed = player.speed;

        Goose goose = new Goose(20, 0, 16, 16, texture, "goose");
        goose.isMoving = true;
        goose.state = Goose.gooseState.ANGRY;
        main.entities.add(goose);

        Field good = Main.class.getDeclaredField("goodEventsRemaining");
        good.setAccessible(true);
        int goodBefore = good.getInt(main);

        goose.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(Goose.gooseState.HAPPY, goose.state, "Goose should be happy when player has seeds");
        assertEquals(beforeSpeed * 2f, player.speed, 0.0001f, "Player should be rewarded with speed boost");
        assertEquals(130, main.getSecondsRemaining(), "Player should be rewarded with +30 seconds");
        assertEquals(3, player.hearts, "Player shouldn't lose hearts when giving seeds");
        assertEquals(goodBefore - 1, good.getInt(main), "Giving seeds should decrement good events by 1");
    }

    @Test
    void gooseSpawnsWhenPlayerEntersTriggerAndTriggersHiddenEvent() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        Main.player = player;
        main.entities.add(player);

        Goose goose = new Goose(0, 0, 16, 16, texture, "goose");
        goose.player = player;

        Field hidden = Main.class.getDeclaredField("hiddenEventsRemaining");
        hidden.setAccessible(true);
        int hiddenBefore = hidden.getInt(main);

        player.sprite.setX(goose.spawnTrigger.x + 1);
        player.sprite.setY(goose.spawnTrigger.y + 1);

        goose.logic();

        assertTrue(goose.visible, "Goose should become visible after spawning");
        assertEquals(Goose.gooseState.ANGRY, goose.state, "Goose should spawn angry by default");
        assertTrue(goose.isMoving, "Goose should start moving when spawn");
        assertEquals(hiddenBefore - 1, hidden.getInt(main), "Spawning goose should decrease hidden events by 1");
    }
}