package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * BeginButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherited and should be used to control when
 * the buttons are displayed.
 */
public class BeginButton extends AbstractButton{
    /**
     * Constructor for BeginButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public BeginButton(FileHandle image){
       super(image);
        super.x = 200;
        super.y = 200;
        super.message = "begin button";


    }

    @Override
    protected void dispose() {

    }

}
