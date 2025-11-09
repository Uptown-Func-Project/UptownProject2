package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.github.OMAL_Maze.Map.Building;
/**
 * represents a character which extends the {@link Entity} class
 * has movement and speed
 */
public class Character extends Entity {
    float speed;
    float accelerate;
    float friction;
    float Xspeed = 0;
    float Yspeed = 0;
    float moveY;
    float moveX;

    public Character(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height,entityTexture);
    }

    /**
     * Handles the movement of each character. Overridden in each implementation.
     * @param delta Time since the last frame. Used for timers.
     * @param entities Array of entities
     * @param buildings Array of buildings
     */
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {}

    /**
     * Uses the speed attribute to cap the character's current velocity
     * @param delta Time since the last frame.
     */
    public void capSpeed(float delta) {
        if (Xspeed>speed) Xspeed = speed;
        if (Yspeed>speed) Yspeed = speed;
        if (Xspeed<-speed) Xspeed = -speed;
        if (Yspeed<-speed) Yspeed = -speed;
        moveX = Xspeed * delta;
        moveY = Yspeed * delta;
    }
}
