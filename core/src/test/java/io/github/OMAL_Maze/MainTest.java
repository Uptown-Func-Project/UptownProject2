package io.github.OMAL_Maze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
    }

    @Test
    void testSetVolume() {
        main.setVolume(50f);
        assertEquals(50f, main.volume, 0.001f, "Volume should be set to 50");
    }

    @Test
    void testDefaultVolume() {
        assertEquals(100f, main.volume, 0.001f, "Default volume should be 100");
    }


    @Test
    void testGetInstance() {
        Main instance = Main.getInstance();
        assertNotNull(instance, "getInstance should return the current instance");
        assertEquals(main, instance, "getInstance should return the same instance");
    }

    @Test
    void testGetSecondsRemaining() {
        int seconds = main.getSecondsRemaining();
        assertEquals(300, seconds, "Default seconds remaining should be 300 (5 minutes)");
    }

    @Test
    void testSetSecondsRemaining() {
        main.setSecondsRemaining(250);
        assertEquals(250, main.getSecondsRemaining(), "Seconds remaining should be set to 250");
    }
}
