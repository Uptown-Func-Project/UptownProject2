package io.github.OMAL_Maze.Entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * The base class for all entities in the game
 */
public class Entity {
    int width;
    int height;
    public Sprite sprite;
    Texture entityTexture;
    public boolean visible=true;
    boolean isSolid;

    /**
     * Creates an entity instance and sets the location, texture, and sprite
     * @param x Horizontal location of the entity.
     * @param y Vertical location of the entity.
     * @param width Width of the entity.
     * @param height Height of the entity.
     * @param entityTexture Texture object for the entity sprite.
     */
    public Entity(int x, int y, int width, int height, Texture entityTexture) {
        this.width=width;
        this.height=height;
        this.entityTexture=entityTexture;
        this.sprite = new Sprite(entityTexture);
        this.sprite.setSize(width,height);
        this.sprite.setX(x);
        this.sprite.setY(y);
        isSolid = true;
    }

    /**
     * Checks if a certain bounding box overlaps with the entity.
     * @param bounds Bounding box to overlap with.
     * @return Boolean value that is true if the entity overlaps and false if it does not.
     */
    public boolean Overlaps(Rectangle bounds) {
        return sprite.getBoundingRectangle().overlaps(bounds);
    }

    /**
     * Renders the entity using a batch.
     * @param batch Batch to draw with.
     */
    public void render(Batch batch) {
        sprite.draw(batch);
    }

    /**
     * Initially empty logic function that is used for calculations and general logic regarding the entity.
     * Called by main class but only used when overridden.
     */
    public void logic() {
        //Not sure yet
    }

    /**
     * Getter method for the visibility.
     * @return Boolean value, true if the entity is visible and false if it isn't.
     */
    public boolean getVisible() {return this.visible;}
}
