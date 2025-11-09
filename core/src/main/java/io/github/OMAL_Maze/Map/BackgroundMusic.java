package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.audio.Sound;

public class BackgroundMusic {
    Sound music;
    long id;

    /**
     * Spawns an object to handle the background music.
     * @param music The sound object storing the audio for the background music.
     */
    public BackgroundMusic(Sound music) {
        this.music = music;
    }

    /**
     * Starts the music and stores the ID as an attribute.
     * @param volume Volume as a percentage (0-100f)
     */
    public void start(float volume) {
        this.id = this.music.play(volume);
        this.music.setLooping(id, true);
    }

    /**
     * Stops the music using the ID attribute.
     */
    public void stop() {
        this.music.stop(this.id);
    }

    /**
     * Pauses the music using the ID attribute.
     */
    public void pause() {
        this.music.pause(this.id);
    }

    /**
     * Resumes the music using the ID attribute.
     */
    public void resume() {
        this.music.resume(this.id);
    }

    /**
     * Changes the volume by stopping and restarting the sound.
     * @param volume New volume as a percentage (0-100f)
     */
    public void changeVolume(float volume) {
        this.stop();
        this.start(volume);
    }
}
