package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class Food extends Entity {
    /**
     * Creates a seed entity that is not solid by default.
     */
    public Food(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
    }
}
