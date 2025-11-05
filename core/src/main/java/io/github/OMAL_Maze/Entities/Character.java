package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.OMAL_Maze.Map.Building;

public class Character extends Entity {
    float speed;
    float accelerate;
    float friction;
    float Xspeed = 0;
    float Yspeed = 0;

    public Character(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height,entityTexture);
    }
    /**
    *This is a reuse of the Overlaps function for readability.
     * @param bounds the rectangle object of a different Entity.
    */
    public boolean Collides(Rectangle bounds) {
        return Overlaps(bounds);
    }
    @Override
    public void logic() {
        //Checking collision. Entities can call this method from super.
    }
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {}
}
