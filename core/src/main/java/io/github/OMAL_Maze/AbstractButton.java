package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
    private Texture texture;
    boolean active = false;
    int x, y;
    String message;

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

    public int getWidth(){
        return texture.getWidth();
    }

    public int getHeight(){
        return texture.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //draws the button to the screen
    public void draw(SpriteBatch batch){
        batch.draw(texture, x,y);
    }

    //makes the button active
    public void makeActive() {
        active = true;
    }

    //makes the button inactive
    public void makeInactive() {
        active = false;
    }

    /**
     * Checks if the button has been clicked in the boundries of the image.
     * @param viewport the FitViewport that is holding the imformaton on the screen
     * @return true if the button has been clicked, false if not
     */
    boolean isClicked(Viewport viewport) {
        boolean clicked = false;
        int locationX;
        int locationY;
        if (Gdx.input.isTouched()) {
            //changes location of click to the viewport to scale the point
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
            locationX = Gdx.input.getX();
            locationY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (click.x >= x && click.x <= x+getWidth()){
                if (click.y >= y && click.y <= y+getHeight()){
                    clicked = true;
                    System.out.println(message);
                }
            }
        }
        return clicked;
    }

    protected abstract void dispose();
}
