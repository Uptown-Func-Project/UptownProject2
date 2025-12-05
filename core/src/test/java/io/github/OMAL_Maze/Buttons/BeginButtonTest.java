package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.files.FileHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BeginButton.
 */
class BeginButtonTest {

    @Mock
    private FileHandle mockFileHandle;

    private BeginButton beginButton;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        beginButton = new BeginButton(mockFileHandle);
    }

    @Test
    void testBeginButtonCreation() {
        assertNotNull(beginButton, "BeginButton should be created");
    }

    @Test
    void testBeginButtonPosition() {
        assertEquals(200, beginButton.getX(), "BeginButton X position should be 200");
        assertEquals(150, beginButton.getY(), "BeginButton Y position should be 150");
    }

    @Test
    void testBeginButtonInheritsFromAbstractButton() {
        assertTrue(beginButton instanceof AbstractButton, "BeginButton should inherit from AbstractButton");
    }

    @Test
    void testBeginButtonNotActiveByDefault() {
        assertFalse(beginButton.isActive(), "BeginButton should not be active by default");
    }

    @Test
    void testBeginButtonCanBeActivated() {
        beginButton.setActive(true);
        assertTrue(beginButton.isActive(), "BeginButton should be active after setActive(true)");
    }

    @Test
    void testBeginButtonDisposeDoesNotThrow() {
        assertDoesNotThrow(() -> beginButton.dispose(), "Dispose should not throw exception");
    }
}
