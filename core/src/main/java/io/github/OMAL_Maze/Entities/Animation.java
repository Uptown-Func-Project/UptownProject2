package io.github.OMAL_Maze.Entities;

/**
 * Animation through frame
 */
public class Animation {
    private int currentFrame = 0;
    private double frameTimer = 0;
    private final double frameDuration; // seconds per frame
    private final int frameCount;
    private boolean looping = true;
    private boolean finished = false;

    /**
     * Animation constructor, specifies timing and frameCount
     * Animation loops by default
     * 
     * @param frameDuration How long each frame is shown in seconds
     * @param frameCount Total number of frames in animation
     */
    public Animation(double frameDuration, int frameCount) {
        this.frameDuration = frameDuration;
        this.frameCount = frameCount;
    }

    /**
     * updates animation state based on time passed.
     * Moves to next frame when enough time passes
     * If looping enabled, goes back to frame 0 after last frame
     * If disabled, stops last frame and marks as finished
     * 
     * @param dt delta time, time passed since last update
     */
    public void update(double dt) {
        if (finished && !looping) return;
        
        frameTimer += dt;
        if (frameTimer >= frameDuration) {
            frameTimer -= frameDuration;
            currentFrame++;
            
            if (currentFrame >= frameCount) {
                if (looping) {
                    currentFrame = 0;
                } else {
                    currentFrame = frameCount - 1;
                    finished = true;
                }
            }
        }
    }
    public int getCurrentFrame() { return currentFrame; }
    public void setLooping(boolean loop) { this.looping = loop; }
    /**
     * resets animation to initial state.
     *
     */
    public void reset() { 
        currentFrame = 0; 
        frameTimer = 0; 
        finished = false;
    }
    public boolean isFinished() { return finished; }
}
