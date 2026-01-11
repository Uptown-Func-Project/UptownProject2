package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.OMAL_Maze.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuAccessTest {

    private FitViewport viewport;

    @BeforeEach
    void setUp() {
        viewport = new FitViewport(880, 880);
        Gdx.testInput().clear();
        Gdx.testGraphics().setDeltaTime(1.0f);
    }

    @Test
    void beginButtonRegistersClickInsideBounds() {
        BeginButton begin = new BeginButton(Gdx.files.internal("buttonTextures/startNew.png"));

        int clickX = begin.getX() + 1;
        int clickY = begin.getY() + 1;

        Gdx.testInput().setTouched(true);
        Gdx.testInput().setPointer(clickX, clickY);

        assertTrue(begin.isClicked(viewport), "Click within button bounds should register");
    }

    @Test
    void beginButtonDoesNotRegisterClickOutsideBounds() {
        BeginButton begin = new BeginButton(Gdx.files.internal("buttonTextures/startNew.png"));

        Gdx.testInput().setTouched(true);
        Gdx.testInput().setPointer(begin.getX() + begin.getWidth() + 100, 0);

        assertFalse(begin.isClicked(viewport), "Click outside button bounds should not register");
    }

    @Test
    void muteButtonTogglesMainVolume() {
        Main main = new Main();
        assertEquals(100f, main.volume, 0.0001f);

        MuteButton mute = new MuteButton(Gdx.files.internal("buttonTextures/greenbutton.png"));

        int clickX = mute.getX() + 1;
        int clickY = mute.getY() + 1;

        Gdx.testInput().setTouched(true);
        Gdx.testInput().setPointer(clickX, clickY);

        mute.isClicked(viewport);
        assertEquals(0f, main.volume, 0.0001f, "First click should mute so volume=0");
        assertEquals("Unmute", mute.getMutedStr());

        Gdx.testInput().setTouched(false);
        Gdx.testInput().setTouched(true);
        mute.isClicked(viewport);
        assertEquals(100f, main.volume, 0.0001f, "Second click should unmute so volume=100");
        assertEquals("Mute", mute.getMutedStr());
    }
}