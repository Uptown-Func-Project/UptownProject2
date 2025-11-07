package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.OMAL_Maze.Main;

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
        super.y = 850;
        super.message = "mute button";
        Main instance = Main.getInstance();
    }

    @Override
    public void dispose() {

    }

}