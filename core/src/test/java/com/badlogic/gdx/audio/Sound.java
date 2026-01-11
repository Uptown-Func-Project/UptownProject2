package com.badlogic.gdx.audio;

public interface Sound {
    long play();
    long play(float volume);
    long play(float volume, float pitch, float pan);
    long loop();
    long loop(float volume);
    long loop(float volume, float pitch, float pan);
    void stop();
    void stop(long soundId);
    void pause();
    void pause(long soundId);
    void resume();
    void resume(long soundId);
    void setLooping(long soundId, boolean looping);
    void setPan(long soundId, float pan, float volume);
    void setPitch(long soundId, float pitch);
    void setVolume(long soundId, float volume);
    void dispose();
}
