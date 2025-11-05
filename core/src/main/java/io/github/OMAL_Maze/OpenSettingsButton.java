package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
/**
 * OpenSettingsButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class OpenSettingsButton extends AbstractButton{

    /**
     * Consrtuctor for OpenSettingsButton inheritted from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public OpenSettingsButton(FileHandle image){
        super(image);
        super.x = 300;
        super.y = 200;
        super.message = "Open Settings";
    }
}
