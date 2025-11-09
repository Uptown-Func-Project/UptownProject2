package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

//each button will need the visuals associated with it to also be displayed

/**
 * Abstract button class to hold methods for all buttons made.
 * texture holds the texture
 * active represents if the button is active (if it is displayed and can be clicked)
 * x and y are the locations of the button on screen
 * message is the message to be displayed for testing purposes
 */
public abstract class AbstractButton {
    private final Texture texture;
    boolean active = false;
    protected int x;
    protected int y;
    protected String message;

    /**
     * Constructor that each subclass implements
     * @param image is the image representing the button
     */
    public AbstractButton(FileHandle image) {
        //texture = new Texture(Gdx.files.internal("button.png"));
        this.texture = new Texture(image);
    }

    /**
     * Makes the button active
     * @return true so that the button is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Shows the texture of the button
     * @return the texture of the button object
     */
    public Texture getTexture(){
        return texture;
    }

    /**
     * Getter method for the width of the button
     * @return width in pixels
     */
    public int getWidth(){
        return texture.getWidth();
    }

    /**
     * Getter method for the height of the button
     * @return height in pixels
     */
    public int getHeight(){
        return texture.getHeight();
    }

    /**
     * Getter method for the horizontal position of the button
     * @return horizontal position in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method for the vertical position of the button
     * @return vertical position in pixels
     */
    public int getY() {
        return y;
    }


    /**
     * Draws the button to the screen
     * @param batch the SpriteBatch object used to render the button
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, x,y);
    }

    /**
     * Setter method for active
     */
    public void setActive(boolean nActive) {
        active = nActive;
    }

    /**
     * Checks if the button has been clicked in the boundaries of the image.
     * @param viewport the FitViewport that is holding the information on the screen
     * @return true if the button has been clicked, false if not
     */
    public boolean isClicked(Viewport viewport) {
        boolean clicked = false;
        if (Gdx.input.isTouched()) {
            //changes location of click to the viewport to scale the point
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

            if (click.x >= x && click.x <= x+getWidth()){
                if (click.y >= y && click.y <= y+getHeight()){
                    clicked = true;
                    System.out.println(message);
                }
            }
        }
        return clicked;
    }

    public abstract void dispose();
}
