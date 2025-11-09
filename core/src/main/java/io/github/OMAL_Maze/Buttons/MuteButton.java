package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.OMAL_Maze.Main;

/**
 * MuteButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherrited and should be used to control when
 * the buttons are displayed.
 */
public class MuteButton extends AbstractButton{
    //Time delay between muting the game. Without this, clicks can be registered multiple times.
    float changeTime = 0.5f;
    boolean muted = false;
    /**
     * Constructor for BeginButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public MuteButton(FileHandle image){
        super(image);
        super.x = 750;
        super.y = 850;
        super.message = "mute button";
    }

    /**
     * Checks if the button has been clicked in the boundaries of the image.
     * @param viewport the FitViewport that is holding the information on the screen.
     * @return true if the button has been clicked, false if not.
     */
    @Override
    public boolean isClicked(Viewport viewport) {
        boolean clicked = super.isClicked(viewport);
        float delta = Gdx.graphics.getDeltaTime();
        this.changeTime -= delta;
        if (clicked && changeTime<=0f){
            //Sets volume to 0 by default. If already muted, volume is returned to 100f (100%)
            float nVolume = 0f;
            if (!muted) {
                Main.getInstance().setVolume(nVolume);
                muted = true;
            } else {
                nVolume = 100f;
                Main.getInstance().setVolume(nVolume);
                muted = false;
            }
            changeTime = 0.5f;
        }
        return clicked;
    }

    @Override
    public void dispose() {

    }

    /**
     * Function to generate the string for the mute button display.
     * @return The string "Mute" or "Unmute".
     */
    public String getMutedStr() {
        if (!muted) {
            return "Mute";
        } else {
            return "Unmute";
        }
    }

}