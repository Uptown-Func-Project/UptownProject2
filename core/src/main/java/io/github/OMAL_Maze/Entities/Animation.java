package io.github.OMAL_Maze.Entities;

public class Animation {
    private int currentFrame = 0;
    private double frameTimer = 0;
    private final double frameDuration; // seconds per frame
    private final int frameCount;
    private boolean looping = true;
    private boolean finished = false;

    public Animation(double frameDuration, int frameCount) {
        this.frameDuration = frameDuration;
        this.frameCount = frameCount;
    }

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
    public void reset() { 
        currentFrame = 0; 
        frameTimer = 0; 
        finished = false;
    }
    public boolean isFinished() { return finished; }
}
