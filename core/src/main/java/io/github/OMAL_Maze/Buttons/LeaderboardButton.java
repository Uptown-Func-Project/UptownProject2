package io.github.OMAL_Maze.Buttons;
import com.badlogic.gdx.files.FileHandle;

public class LeaderboardButton extends AbstractButton{
    /**
     * Constructor for LeaderboardButton inherited from AbstractButton.
     * @param image the file of the image to represent the button
     */
    public LeaderboardButton(FileHandle image){
        super(image);
        super.x = 450;
        super.y = 50;
        super.message = "leaderboard button title screen";
    }

    @Override
    public void dispose() {

    }
    
}
