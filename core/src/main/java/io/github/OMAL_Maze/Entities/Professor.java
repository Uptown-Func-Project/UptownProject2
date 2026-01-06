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
        super(x, y, width, height, texture);
        visible = true;
    }

    public Professor(int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new Texture(TEXTURE_PATH));
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

    /**
     * if player is within this rectangle, they can interact with the professor. Player presses 'e'.
     * professor shows dialogue in the bottom (GUI) with buttons for player to choose response.
     * 
     * player gets either good/bad degree based on their choices.
     * degree is its own entity. change textures and stuff depending on good/bad degree.
     * 
     * Dean has exact same entity with the geese except they cant be killed but rather stunned. hasSeeds
     * logic applied to him but its hasBad/GoodDegree which causes him to chase you or let you pass
     * 
     * lock player's movements when dialogue with professor
     * 
     * for super hard question just use more multiple choices
     * 
     * 
     * 
     * professor checks if hasDegree = false and degreeState = 0
     * If true then player can start test, if false dialogue says "you already did exam, go to central hall to get ur degree"
     * 
     * answering exam changes degreeState to either 1 or 2
     * degreeState = 1/2 and hasDegree = false - player heads to central hall to get their degree
     * 
     * talking to person in central hall sets hasDegree = true
     * 
     * dean tells you to go get a degree if hasDegree = false. if its true, it checks if its 1 or 2 
     * and chases you or lets you pass based on the degreeState.
     * 
     */

    
    
}
