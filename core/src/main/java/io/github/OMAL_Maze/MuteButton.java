package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * MuteButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class MuteButton extends AbstractButton{
    /**
     * Consrtuctor for BeginButton inheritted from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public MuteButton(FileHandle image){
        super(image);
        super.x = 750;
        super.y = 830;
        super.message = "mute button";


    }

}