package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.audio.Sound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackgroundMusicTest {

    private static final class FakeSound implements Sound {
        int playCount;
        int stopCount;
        int pauseCount;
        int resumeCount;
        int setLoopingCount;
        long lastId;
        float lastPlayVolume;
        boolean lastLooping;

        @Override
        public long play() {
            playCount++;
            lastId = 1L;
            return lastId;
        }

        @Override
        public long play(float volume) {
            playCount++;
            lastPlayVolume = volume;
            lastId = 42L + playCount;
            return lastId;
        }

        @Override
        public long play(float volume, float pitch, float pan) {
            playCount++;
            lastPlayVolume = volume;
            lastId = 1L;
            return lastId;
        }

        @Override
        public long loop() {
            return 0;
        }

        @Override
        public long loop(float volume) {
            return 0;
        }

        @Override
        public long loop(float volume, float pitch, float pan) {
            return 0;
        }

        @Override
        public void stop() {
            stopCount++;
        }

        @Override
        public void stop(long soundId) {
            stopCount++;
            lastId = soundId;
        }

        @Override
        public void pause() {
            pauseCount++;
        }

        @Override
        public void pause(long soundId) {
            pauseCount++;
            lastId = soundId;
        }

        @Override
        public void resume() {
            resumeCount++;
        }

        @Override
        public void resume(long soundId) {
            resumeCount++;
            lastId = soundId;
        }

        @Override
        public void setLooping(long soundId, boolean looping) {
            setLoopingCount++;
            lastId = soundId;
            lastLooping = looping;
        }

        @Override
        public void setPan(long soundId, float pan, float volume) {}

        @Override
        public void setPitch(long soundId, float pitch) {}

        @Override
        public void setVolume(long soundId, float volume) {}

        @Override
        public void dispose() {}
    }

    @Test
    void startPlaysAndLoops() {
        FakeSound sound = new FakeSound();
        BackgroundMusic music = new BackgroundMusic(sound);

        music.start(0.5f);

        assertEquals(1, sound.playCount);
        assertEquals(0.5f, sound.lastPlayVolume, 0.0001f);
        assertEquals(1, sound.setLoopingCount);
        assertTrue(sound.lastLooping);
    }

    @Test
    void changeVolumeStopsThenRestarts() {
        FakeSound sound = new FakeSound();
        BackgroundMusic music = new BackgroundMusic(sound);

        music.start(0.25f);
        music.changeVolume(0.75f);

        assertEquals(2, sound.playCount);
        assertEquals(1, sound.stopCount, "changeVolume should stop once");
        assertEquals(0.75f, sound.lastPlayVolume, 0.0001f);
    }
}