
package io.github.OMAL_Maze.Buttons;

import com.badlogic.gdx.files.FileHandle;

/**
 * BeginButton class extends AbstractButton and inherits all the methods and attributes.
 * MakeActive and MakeInactive are inherited and should be used to control when
 * the buttons are displayed.
 */
public class StartButton extends AbstractButton{
    /**
     * Constructor for BeginButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public StartButton(FileHandle image){
        super(image);
        super.x = 325;
        super.y = 175;
        super.message = "start button title screen";
    }

    @Override
    public void dispose() {

    }
}
