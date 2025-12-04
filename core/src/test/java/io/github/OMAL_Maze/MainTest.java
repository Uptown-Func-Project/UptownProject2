package io.github.OMAL_Maze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Main application logic.
 */
class MainTest {

    private Main main;

    @BeforeEach
    void setUp() {
        // Note: Main extends ApplicationAdapter which requires libGDX context
        // For headless testing, you may need to use HeadlessApplication
        main = new Main();
    }

    @Test
    void testSetVolume() {
        // Test setting volume to a valid value
        main.setVolume(50f);
        assertEquals(50f, main.volume, 0.001f, "Volume should be set to 50");
    }

    @Test
    void testSetVolumeMinimum() {
        // Test setting volume to minimum
        main.setVolume(0f);
        assertEquals(0f, main.volume, 0.001f, "Volume should be set to 0");
    }

    @Test
    void testSetVolumeMaximum() {
        // Test setting volume to maximum
        main.setVolume(100f);
        assertEquals(100f, main.volume, 0.001f, "Volume should be set to 100");
    }

    @Test
    void testDefaultVolume() {
        // Test that default volume is 100
        assertEquals(100f, main.volume, 0.001f, "Default volume should be 100");
    }
}
