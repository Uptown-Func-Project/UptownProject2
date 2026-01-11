package com.badlogic.gdx;

import com.badlogic.gdx.audio.Sound;

public final class TestSound implements Sound {
    public int playCount;
    public float lastPlayVolume = 1f;
    public long nextId = 1L;
    public int stopCount;
    public int pauseCount;
    public int resumeCount;
    public int setLoopingCount;
    public boolean lastLooping;

    @Override public long play() {
        playCount++;
        return nextId++;
    }

    @Override public long play(float volume) {
        playCount++;
        lastPlayVolume = volume;
        return nextId++;
    }

    @Override public long play(float volume, float pitch, float pan) {
        playCount++;
        lastPlayVolume = volume;
        return nextId++;
    }

    @Override public long loop() {
        return nextId++;
    }

    @Override public long loop(float volume) {
        return nextId++;
    }

    @Override public long loop(float volume, float pitch, float pan) {
        return nextId++;
    }

    @Override public void stop() {
        stopCount++;
    }
    
    @Override public void stop(long soundId) {
        stopCount++;
    }

    @Override public void pause() {
        pauseCount++;
    }

    @Override public void pause(long soundId) {
         pauseCount++;
    }

    @Override public void resume() {
        resumeCount++;
    }

    @Override public void resume(long soundId) {
        resumeCount++;
    }

    @Override public void setLooping(long soundId, boolean looping) {
        setLoopingCount++; lastLooping = looping;
    }

    @Override public void setPan(long soundId, float pan, float volume) {}
    @Override public void setPitch(long soundId, float pitch) {}
    @Override public void setVolume(long soundId, float volume) {}
    @Override public void dispose() {}
}