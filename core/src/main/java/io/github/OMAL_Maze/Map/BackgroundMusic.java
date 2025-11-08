package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.audio.Sound;

public class BackgroundMusic {
    Sound music;
    long id;
    public BackgroundMusic(Sound music) {
        this.music = music;
    }
    public void start(float volume) {
        this.id = this.music.play(volume);
        this.music.setLooping(id, true);
    }
    public void stop() {
        this.music.stop(this.id);
    }
    public void pause() {
        this.music.pause(this.id);
    }
    public void resume() {
        this.music.resume(this.id);
    }
    public void changeVolume(float volume) {
        this.stop();
        this.start(volume);
    }
}
