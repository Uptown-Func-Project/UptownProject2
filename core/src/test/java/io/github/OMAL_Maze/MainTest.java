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

    @Test
    void testSetSecondsRemainingToZero() {
        main.setSecondsRemaining(0);
        assertEquals(0, main.getSecondsRemaining(), "Seconds remaining should be set to 0");
    }

    @Test
    void testSetSecondsRemainingToHighValue() {
        main.setSecondsRemaining(500);
        assertEquals(500, main.getSecondsRemaining(), "Seconds remaining should be set to 500");
    }

    @Test
    void testDecrementHiddenEventCounter() {
        assertDoesNotThrow(() -> main.decrementHiddenEventCounter(), 
            "decrementHiddenEventCounter should not throw exception");
    }

    @Test
    void testDecrementBadEventCounter() {
        assertDoesNotThrow(() -> main.decrementBadEventCounter(), 
            "decrementBadEventCounter should not throw exception");
    }

    @Test
    void testDecrementGoodEventCounter() {
        assertDoesNotThrow(() -> main.decrementGoodEventCounter(), 
            "decrementGoodEventCounter should not throw exception");
    }

    @Test
    void testDefaultTileSize() {
        // Tile size is calculated in create() but we can test the initial value
        assertEquals(0, main.tileSize, "Default tile size should be 0 before create()");
    }

    @Test
    void testVolumeWithNegativeValue() {
        main.setVolume(-10f);
        assertEquals(-10f, main.volume, 0.001f, "Volume should accept negative values");
    }

    @Test
    void testVolumeWithLargeValue() {
        main.setVolume(1000f);
        assertEquals(1000f, main.volume, 0.001f, "Volume should accept large values");
    }

    @Test
    void testSetSecondsRemainingMultipleTimes() {
        main.setSecondsRemaining(100);
        assertEquals(100, main.getSecondsRemaining(), "Should be 100");
        main.setSecondsRemaining(200);
        assertEquals(200, main.getSecondsRemaining(), "Should be 200");
        main.setSecondsRemaining(50);
        assertEquals(50, main.getSecondsRemaining(), "Should be 50");
    }

    @Test
    void testMainConstructor() {
        Main newMain = new Main();
        assertNotNull(newMain, "Main constructor should create a new instance");
        assertEquals(100f, newMain.volume, 0.001f, "New instance should have default volume");
    }

    @Test
    void testGameOverMethodDoesNotThrow() {
        assertDoesNotThrow(() -> main.gameOver(), 
            "gameOver method should not throw exception even without libGDX context");
    }

    @Test
    void testRenderMethodDoesNotThrow() {
        assertDoesNotThrow(() -> main.render(), 
            "render method should handle being called without full initialization");
    }

    @Test
    void testDisposeMethodDoesNotThrow() {
        assertDoesNotThrow(() -> main.dispose(), 
            "dispose method should not throw exception");
    }

    @Test
    void testResizeMethodDoesNotThrow() {
        assertDoesNotThrow(() -> main.resize(800, 600), 
            "resize method should not throw exception");
    }
}
