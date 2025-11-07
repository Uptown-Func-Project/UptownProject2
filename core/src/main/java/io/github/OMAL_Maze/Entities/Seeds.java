package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

/**
 * this class makes the seed solid
 */
public class Seeds extends Entity {
    public Seeds(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
    }
}
