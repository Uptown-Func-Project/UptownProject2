package io.github.OMAL_Maze.Buttons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.files.FileHandle;

class ButtonStateTest {

    private FileHandle fh;

    @BeforeEach
    void setUp() {
        fh = new FileHandle("buttonTextures/startNew.png");
    }

    @Test
    void testBeginButtonInitiallyInactive() {
        BeginButton b = new BeginButton(fh);
        assertFalse(b.isActive());
    }

    @Test
    void testBeginButtonSetActive() {
        BeginButton b = new BeginButton(fh);
        b.setActive(true);
        assertTrue(b.isActive());
    }

    @Test
    void testBeginButtonSetInactive() {
        BeginButton b = new BeginButton(fh);
        b.setActive(true);
        b.setActive(false);
        assertFalse(b.isActive());
    }

    @Test
    void testMuteButtonGetMutedStr() {
        MuteButton m = new MuteButton(fh);
        assertNotNull(m.getMutedStr());
        assertTrue(m.getMutedStr().length()>0);
    }
}
