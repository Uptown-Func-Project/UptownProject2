package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class EnergyDrink extends Entity {
    /**
     * Creates a seed entity that is not solid by default.
     */
    public EnergyDrink(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
    }
}
