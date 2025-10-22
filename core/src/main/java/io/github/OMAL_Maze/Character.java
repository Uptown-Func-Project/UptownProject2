package io.github.OMAL_Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Character extends Entity {
    Rectangle rectangle;
    Sprite sprite;
    public Character(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height,entityTexture);
        this.sprite = super.sprite;
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
}
