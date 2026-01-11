package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuInitialStateTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
        Gdx.testInput().clear();
        Gdx.testGraphics().setDeltaTime(1.0f);
    }

    @Test
    void beforePlayingUserIsInMainMenu() {
        assertTrue(main.isInMainMenu(), "User should be in the main menu before playing");
    }
}
