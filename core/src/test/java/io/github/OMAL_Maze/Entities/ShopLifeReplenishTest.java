package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.OMAL_Maze.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopLifeReplenishTest {
    private Main main;
    private Texture texture;

    @BeforeEach
    void setUp() {
        main = new Main();
        main.viewport = new FitViewport(880, 880);
        main.entities = new Array<>();
        texture = new Texture(Gdx.files.internal("dummy.png"));
    }

    @Test
    void buyingFoodIncreasesHeartsAndLosesCoins() {
        Player player = new Player(0, 0, 16, 16, texture, "player");
        player.coins = 2;
        Main.player = player;
        main.entities.add(player);

        Food food = new Food(0, 0, 16, 16, texture, "food1");
        main.entities.add(food);

        int heartsBefore = player.hearts;
        int coinsBefore = player.coins;

        player.logic();

        assertEquals(heartsBefore + 1, player.hearts, "Food purchase should give 1 life");
        assertEquals(coinsBefore - 2, player.coins, "Food should cost 2 coins");
        assertFalse(main.entities.contains(food, true), "Food should be gone after purchase");
    }
}
