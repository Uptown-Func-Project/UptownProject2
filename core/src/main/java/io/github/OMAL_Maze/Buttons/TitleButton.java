package io.github.OMAL_Maze.Buttons;

public class TitleButton extends AbstractButton {
    /**
     * Constructor for TitleButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public TitleButton(com.badlogic.gdx.files.FileHandle image){
        super(image);
        super.x = 200;
        super.y = 150;
        super.message = "title button main menu screen";
    }

    @Override
    public void dispose() {

    }
    
}
