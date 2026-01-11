package io.github.OMAL_Maze.Buttons;

public class AchievementButton extends AbstractButton {
    /**
     * Constructor for AchievementButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public AchievementButton(com.badlogic.gdx.files.FileHandle image){
        super(image);
        super.x = 200;
        super.y = 50;
        super.message = "achievement button title screen";
    }

    @Override
    public void dispose() {

    }
    
}
