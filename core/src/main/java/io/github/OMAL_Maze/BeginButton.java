package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * BeginButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class BeginButton extends AbstractButton{
    /**
     * Consrtuctor for BeginButton inheritted from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public BeginButton(FileHandle image){
       super(image);
        super.x = 200;
        super.y = 150;
        super.message = "begin button";
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
                    //this is where the next maze is loaded
                    //newGame.startGame();

                }
            }
        }
        return clicked;
    }
}
