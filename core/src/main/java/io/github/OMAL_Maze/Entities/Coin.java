package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;

public class Coin extends Entity {
    private static int nextId = 0;
    private final int id;
    
    public Coin(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
        this.id = nextId++;
    }

    public int getId(){
        return this.id;
    }
}
