package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Building {
    Rectangle rectangle;
    int x;
    int y;
    int width;
    int height;
    Sprite sprite;
    Texture buildingTexture;
    boolean visible=true;

    /**
     * Spawns a building object in the map using a certain location, dimensions, and texture.
     * @param x Horizontal spawn location for the building.
     * @param y Vertical spawn location for the building.
     * @param width Width of the building in pixels.
     * @param height Height of the building in pixels.
     * @param buildingTexture Texture object storing the graphics for the building.
     */
    public Building(int x, int y, int width, int height, Texture buildingTexture) {
        rectangle = new Rectangle(x,y,width,height);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.buildingTexture = buildingTexture;
        this.sprite = new Sprite(this.buildingTexture);
        this.sprite.setSize(width,height);
        this.sprite.setX(this.x);
        this.sprite.setY(this.y);
    }

    /**
     * A simple function used to check if the building overlaps with a given set of bounds.
     * @param bounds Rectangular bounding box to check against.
     * @return Boolean value of true if the building overlaps with the bounds and false if not.
     */
    public boolean Overlaps(Rectangle bounds) {
        return rectangle.overlaps(bounds);
    }

    /**
     * Uses the batch to draw the building.
     * @param batch Batch object that is used for rendering graphics.
     */
    public void render(Batch batch) {
        sprite.draw(batch);
    }

    /**
     * Getter method for the visibility of the building.
     * @return Boolean value for if the building is visible or not
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Setter method for the building visibility.
     * @param nVisible Boolean value that is true if the building is visible and false if not.
     */
    public void setVisible(boolean nVisible) {
        this.visible=nVisible;
    }
}
