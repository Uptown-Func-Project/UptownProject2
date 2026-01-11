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
        Player player = new Player(0, 0, 16, 16, texture);
        Main.player = player;
        main.entities.add(player);

        Goose goose = new Goose(20, 0, 16, 16, texture);
        goose.isMoving = true;
        goose.state = Goose.gooseState.ANGRY;
        main.entities.add(goose);

        int beforeHearts = player.hearts;
        goose.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(beforeHearts - 1, player.hearts, "Goose bite should decrease hearts by 1");
        assertTrue(goose.bitPlayer, "Goose should enter bite cooldown state");
        assertFalse(goose.isMoving, "Goose should stop moving after biting");

        Field bad = Main.class.getDeclaredField("badEventsRemaining");
        bad.setAccessible(true);
        assertEquals(0, bad.getInt(main), "Bite should mark bad event as completed");
    }

    @Test
    void gooseBecomesHappyAndStopsAttackingWhenGivenSeeds() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture);
        player.hasSeeds = true;
        Main.player = player;
        main.entities.add(player);

        main.setSecondsRemaining(100);
        float beforeSpeed = player.speed;

        Goose goose = new Goose(20, 0, 16, 16, texture);
        goose.isMoving = true;
        goose.state = Goose.gooseState.ANGRY;
        main.entities.add(goose);

        goose.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(Goose.gooseState.HAPPY, goose.state, "Goose should become happy when player has seeds");
        assertFalse(player.hasSeeds, "Seeds should be consumed");
        assertEquals(beforeSpeed * 2f, player.speed, 0.0001f, "Player should be rewarded with speed boost");
        assertEquals(130, main.getSecondsRemaining(), "Player should be rewarded with +30 seconds");
        assertEquals(3, player.hearts, "Player shouldn't loose hearts when giving seeds");

        Field good = Main.class.getDeclaredField("goodEventsRemaining");
        good.setAccessible(true);
        assertEquals(0, good.getInt(main), "Giving seeds should mark good event as completed");
    }

    @Test
    void gooseSpawnsWhenPlayerEntersTriggerAndMarksHiddenEvent() throws Exception {
        Player player = new Player(0, 0, 16, 16, texture);
        Main.player = player;
        main.entities.add(player);

        Goose goose = new Goose(0, 0, 16, 16, texture);
        goose.player = player;

        player.sprite.setX(goose.spawnTrigger.x + 1);
        player.sprite.setY(goose.spawnTrigger.y + 1);

        goose.logic();

        assertTrue(goose.visible, "Goose should become visible after spawning");
        assertEquals(Goose.gooseState.ANGRY, goose.state, "Goose should spawn angry by default");
        assertTrue(goose.isMoving, "Goose should start moving on spawn");

        Field hidden = Main.class.getDeclaredField("hiddenEventsRemaining");
        hidden.setAccessible(true);
        assertEquals(0, hidden.getInt(main), "Spawning goose should mark hidden event as completed");
    }
}