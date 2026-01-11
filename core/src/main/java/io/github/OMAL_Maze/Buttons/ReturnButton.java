package io.github.OMAL_Maze.Buttons;

public class ReturnButton extends AbstractButton {
    /**
     * Constructor for ReturnButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public ReturnButton(com.badlogic.gdx.files.FileHandle image){
        super(image);
        super.x = 300;
        super.y = 50;
        super.message = "return button leaderboard screen";
    }

    @Override
    public void dispose() {

    }
    
}
