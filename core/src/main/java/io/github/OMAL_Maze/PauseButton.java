package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
/**
 * PauseButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class PauseButton extends AbstractButton{

    /**
     * Consrtuctor for PauseButton inheritted from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public PauseButton(FileHandle image) {
        super(image);
        super.x = 300;
        super.y = 350;
        super.message = "Pause";
    }
}
