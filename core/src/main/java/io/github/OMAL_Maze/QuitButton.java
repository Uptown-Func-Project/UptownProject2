package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
/**
 * QuitButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class QuitButton extends AbstractButton{
    /**
     * Consrtuctor for QuitButton inheritted from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public QuitButton(FileHandle image){
        super(image);
        super.x = 0;
        super.y = 0;
        super.message = "quit button";
    }
}