package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class EnergyDrink extends Entity {
    /**
     * Creates an energy drink entity that is not solid by default.
     */
    public EnergyDrink(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
    }
}
