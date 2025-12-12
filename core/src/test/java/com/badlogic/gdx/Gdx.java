package com.badlogic.gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class Gdx {
    public static Audio audio = new Audio();
    public static Files files = new Files();
    public static Graphics graphics = new Graphics();

    public static class Audio {
        public Sound newSound(FileHandle fh) {
            return new com.badlogic.gdx.audio.Sound() {
                @Override public long play() { return 1L; }
                @Override public long play(float volume) { return 1L; }
                @Override public long play(float volume, float pitch, float pan) { return 1L; }
                @Override public long loop() { return 1L; }
                @Override public long loop(float volume) { return 1L; }
                @Override public long loop(float volume, float pitch, float pan) { return 1L; }
                @Override public void stop() {}
                @Override public void stop(long soundId) {}
                @Override public void pause() {}
                @Override public void pause(long soundId) {}
                @Override public void resume() {}
                @Override public void resume(long soundId) {}
                @Override public void setLooping(long soundId, boolean looping) {}
                @Override public void setPitch(long soundId, float pitch) {}
                @Override public void setVolume(long soundId, float volume) {}
                @Override public void dispose() {}
            };
        }
    }

    public static class Files {
        public FileHandle internal(String path) {
            return new FileHandle(path);
        }
    }

    public static class Graphics {
        public float getDeltaTime() { return 0.016f; }
    }
}
