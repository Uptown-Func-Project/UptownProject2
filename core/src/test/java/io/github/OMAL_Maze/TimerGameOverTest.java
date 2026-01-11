package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.OMAL_Maze.Map.BackgroundMusic;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TimerGameOverTest {

    @Test
    void timerReachingZeroActivatesGameOverAndPlaysSoundOnce() throws Exception {
        Main main = new Main();
        main.viewport = new FitViewport(880, 880);

        main.GameOverScreen = new Screen(null, main.viewport, "screenTextures/GAME OVER.png");
        main.backgroundMusic = new BackgroundMusic(Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3")));

        main.setSecondsRemaining(0);
        main.secondsDecreasing = true;
        main.setVolume(0.33f);

        Method startTimer = Main.class.getDeclaredMethod("startTimer");
        startTimer.setAccessible(true);
        startTimer.invoke(main);

        Timer.Task task = Timer.getLastScheduledTask();
        assertNotNull(task, "Timer should schedule a task");

        task.run();
        assertTrue(main.GameOverScreen.getActive(), "GameOver screen should be activated");
        assertNotNull(Gdx.testAudio().lastSound, "GameOver sound should be created");
        assertEquals(1, Gdx.testAudio().lastSound.playCount, "GameOver sound should play once");
        assertEquals(0.33f, Gdx.testAudio().lastSound.lastPlayVolume, 0.0001f, "Sound should play at current volume");

        task.run();
        assertEquals(1, Gdx.testAudio().lastSound.playCount, "GameOver sound should not replay on subsequent ticks");

        Field timerText = Main.class.getDeclaredField("timerText");
        timerText.setAccessible(true);
        assertEquals("Time: 00:00", timerText.get(main));
    }
}