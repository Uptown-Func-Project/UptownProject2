package io.github.OMAL_Maze.Entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;



public class Puddle extends Entity {
    Player player;
    public Rectangle trigger;
    public Texture texture;
    



    public Puddle(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
        this.texture = entityTexture;
        
        // Position and size are initialized by the super constructor (sprite)
    }

    
    public void show(){
        // sprite position already set in constructor; just make the puddle visible
        this.visible=true;
    }
    
}

