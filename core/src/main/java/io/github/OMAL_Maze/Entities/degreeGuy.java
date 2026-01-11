package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class degreeGuy extends Entity {
    public static final int DEFAULT_X = 382;
    public static final int DEFAULT_Y = 529;
    public static final int DEFAULT_WIDTH = 64;
    public static final int DEFAULT_HEIGHT = 64;
    public static final String TEXTURE_PATH = "degreeGuy.png";

    /**
     * 
     * Creates a degreeGuy entity.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param texture
     */

    public degreeGuy(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture, "degreeGuy");
        visible = true;
    }

    public degreeGuy(int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new Texture(TEXTURE_PATH), "degreeGuy");
        visible = true;
    }

    public degreeGuy() {
        this(DEFAULT_X, DEFAULT_Y);
    }

    public Rectangle getInteractionBounds() {
        float interactionWidth = 80;
        float interactionHeight = 60;
        float interactionX = sprite.getX() + (sprite.getWidth() - interactionWidth) / 2;
        float interactionY = sprite.getY() + (sprite.getHeight() - interactionHeight) / 2;
        return new Rectangle(interactionX, interactionY, interactionWidth, interactionHeight);
    }

}
