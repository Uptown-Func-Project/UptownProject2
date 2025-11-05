package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * QuitButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherited and should be used to control when
 * the buttons are displayed.
 */
public class QuitButton extends AbstractButton{
    /**
     * Constructor for QuitButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public QuitButton(FileHandle image){
        super(image);
        super.x = 500;
        super.y = 150;
        super.message = "quit button";
    }

    /**
     * Checks if the button has been clicked in the boundaries of the image. Exits the game
     * if a click is found.
     * @param viewport the FitViewport that is holding the information on the screen
     * @return true if the button has been clicked, false if not
     */
    boolean isClicked(FitViewport viewport) {
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
                    //the below line is a test
                    Gdx.app.exit();
                }
            }
        }
        return clicked;
    }
    @Override
    public void dispose(){}
}