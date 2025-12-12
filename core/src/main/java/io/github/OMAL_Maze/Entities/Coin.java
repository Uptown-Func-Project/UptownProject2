package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class Coin extends Entity { 
    /**
     * Creates a coin entity that is not solid by default.
     */   
    public Coin(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
    }
}
