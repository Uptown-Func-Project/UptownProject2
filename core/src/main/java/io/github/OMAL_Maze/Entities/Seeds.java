package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class Seeds extends Entity {
    /**
     * Creates a seed entity that is not solid by default.
     */
    public Seeds(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
    }
}
