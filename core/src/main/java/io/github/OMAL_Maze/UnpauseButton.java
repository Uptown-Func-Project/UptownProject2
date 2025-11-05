package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
/**
 * UnpauseButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherited and should be used to control when
 * the buttons are displayed.
 */
public class UnpauseButton extends AbstractButton{

    /**
     * Constructor for UnpauseButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public UnpauseButton(FileHandle image){
        super(image);
        super.x = 0;
        super.y = 200;
        super.message = "unpause button";
    }
    @Override
    public void dispose() {

    }

}

