package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class Bat extends Entity {
    /**
     * Creates a bat entity that is not solid by default.
     */
    public Bat(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
    }
}
