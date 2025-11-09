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

    @Override
    public void dispose(){}
}