package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import io.github.OMAL_Maze.Map.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWallCollisionTest {

    private io.github.OMAL_Maze.Main main;

    @BeforeEach
    void setUp() {
        main = new io.github.OMAL_Maze.Main();
        main.viewport = new com.badlogic.gdx.utils.viewport.FitViewport(880, 880);
        main.entities = new com.badlogic.gdx.utils.Array<>();
        Gdx.testInput().clear();
    }

    @Test
    void playerDoesNotMoveThroughWallBuilding() {
        Texture texture = new Texture(Gdx.files.internal("dummy.png"));
        Player player = new Player(0, 0, 16, 16, texture);

        Building wall = new Building(10, 0, 16, 16, texture);
        Array<Building> buildings = new Array<>();
        buildings.add(wall);

        Gdx.testInput().pressKey(Input.Keys.RIGHT, true);
        float beforeX = player.sprite.getX();

        player.movement(0.1f, new Array<>(), buildings);

        assertEquals(beforeX, player.sprite.getX(), 0.0001f, "Player should not pass through wall");
    }
}