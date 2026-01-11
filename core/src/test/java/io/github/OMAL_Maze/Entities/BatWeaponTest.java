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

class BatWeaponTest {
    private Main main;
    private Texture texture;

    @BeforeEach
    void setUp() {
        main = new Main();
        main.tileSize = 40;
        main.viewport = new FitViewport(880, 880);
        main.entities = new Array<>();
        texture = new Texture(Gdx.files.internal("dummy.png"));
        Gdx.testInput().clear();
    }

    @Test
    void batSwingDamagesAndKnocksBackVisibleGoose() {
        Player player = new Player(100, 100, 16, 16, texture, "player");
        player.hasBat = true;
        Main.player = player;
        main.entities.add(player);

        Goose goose = new Goose(120, 100, 16, 16, texture, "goose");
        goose.visible = true;
        int hpBefore = goose.healthPoints;
        main.entities.add(goose);

        Gdx.testInput().pressKey(Input.Keys.SPACE, true);

        player.movement(0.1f, main.entities, new Array<Building>());

        assertEquals(hpBefore - 1, goose.healthPoints, "Bat swing should reduce gooses health by 1");
        assertTrue(goose.knockbackActive, "Bat swing should apply knockback to the goose");
    }
}
