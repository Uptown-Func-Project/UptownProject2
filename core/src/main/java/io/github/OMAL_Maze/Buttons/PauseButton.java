package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * PauseButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherited and should be used to control when
 * the buttons are displayed.
 */
public class PauseButton extends AbstractButton{

    /**
     * Constructor for PauseButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public PauseButton(FileHandle image) {
        super(image);
        super.x = 630;
        super.y = 850;
        super.message = "Pause";
    }
    @Override
    public void dispose() {
    }

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
                    //need to go to the pause screen
                }
            }
        }
        return clicked;
    }

}
