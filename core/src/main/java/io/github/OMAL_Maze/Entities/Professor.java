package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Professor extends Entity {
    public static final int DEFAULT_X = 432;
    public static final int DEFAULT_Y = 480;
    public static final int DEFAULT_WIDTH = 64;
    public static final int DEFAULT_HEIGHT = 64;
    public static final String TEXTURE_PATH = "professor.png";

    /**
     * 
     * Creates a Professor entity.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param texture
     */

    public Professor(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture, "professor");
        visible = true;
    }

    public Professor(int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new Texture(TEXTURE_PATH), "professor");
        visible = true;
    }

    public Professor() {
        this(DEFAULT_X, DEFAULT_Y);
    }

    public Rectangle getInteractionBounds() {
        float interactionWidth = 100;
        float interactionHeight = 150;
        float interactionX = sprite.getX() + (sprite.getWidth() - interactionWidth) / 2;
        float interactionY = sprite.getY() + (sprite.getHeight() - interactionHeight) / 2;
        return new Rectangle(interactionX, interactionY, interactionWidth, interactionHeight);
    }

}
