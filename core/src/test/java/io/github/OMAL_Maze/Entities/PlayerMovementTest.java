package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Map.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMovementTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
        main.viewport = new FitViewport(880, 880);
        main.entities = new Array<>();
        Gdx.testInput().clear();
    }

    @Test
    void pressingRightMovesPlayerRight() {
        Texture texture = new Texture(com.badlogic.gdx.Gdx.files.internal("dummy.png"));
        Player player = new Player(10, 10, 16, 16, texture);

        Gdx.testInput().pressKey(Input.Keys.RIGHT, true);

        float beforeX = player.sprite.getX();
        player.movement(0.1f, new Array<>(), new Array<Building>());
        float afterX = player.sprite.getX();

        assertTrue(afterX > beforeX, "Player x should increase when RIGHT is pressed");
    }

    @Test
    void pressingUpMovesPlayerUp() {
        Texture texture = new Texture(com.badlogic.gdx.Gdx.files.internal("dummy.png"));
        Player player = new Player(10, 10, 16, 16, texture);

        Gdx.testInput().pressKey(Input.Keys.UP, true);

        float beforeY = player.sprite.getY();
        player.movement(0.1f, new Array<>(), new Array<Building>());
        float afterY = player.sprite.getY();

        assertTrue(afterY > beforeY, "Player y should increase when UP is pressed");
    }

    @Test
    void pressingLeftMovesPlayerLeft() {
        Texture texture = new Texture(com.badlogic.gdx.Gdx.files.internal("dummy.png"));
        Player player = new Player(10, 10, 16, 16, texture);

        Gdx.testInput().pressKey(Input.Keys.LEFT, true);

        float beforeX = player.sprite.getX();
        player.movement(0.1f, new Array<>(), new Array<Building>());
        float afterX = player.sprite.getX();

        assertTrue(afterX < beforeX, "Player x should decrease when LEFT is pressed");
    }

    @Test
    void pressingDownMovesPlayerDown() {
        Texture texture = new Texture(com.badlogic.gdx.Gdx.files.internal("dummy.png"));
        Player player = new Player(10, 10, 16, 16, texture);

        Gdx.testInput().pressKey(Input.Keys.DOWN, true);

        float beforeY = player.sprite.getY();
        player.movement(0.1f, new Array<>(), new Array<Building>());
        float afterY = player.sprite.getY();

        assertTrue(afterY < beforeY, "Player y should decrease when DOWN is pressed");
    }
}