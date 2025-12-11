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
    void testBeginButtonCreation() {
        BeginButton b = new BeginButton(fh);
        assertNotNull(b);
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
    void testButtonGetDimensions() {
        BeginButton b = new BeginButton(fh);
        assertTrue(b.getWidth() >= 0);
        assertTrue(b.getHeight() >= 0);
    }

    @Test
    void testAllButtonsInitiallyInactive() {
        BeginButton a = new BeginButton(fh);
        QuitButton q = new QuitButton(fh);
        PauseButton p = new PauseButton(fh);
        UnpauseButton u = new UnpauseButton(fh);
        MuteButton m = new MuteButton(fh);
        StartButton s = new StartButton(fh);
        assertFalse(a.isActive());
        assertFalse(q.isActive());
        assertFalse(p.isActive());
        assertFalse(u.isActive());
        assertFalse(m.isActive());
        assertFalse(s.isActive());
    }

    @Test
    void testMultipleButtonsIndependent() {
        BeginButton a = new BeginButton(fh);
        QuitButton q = new QuitButton(fh);
        a.setActive(true);
        assertTrue(a.isActive());
        assertFalse(q.isActive());
    }

    @Test
    void testMuteButtonGetMutedStr() {
        MuteButton m = new MuteButton(fh);
        assertNotNull(m.getMutedStr());
        assertTrue(m.getMutedStr().length()>0);
    }
}
