package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;



public class Puddle extends Entity {

    public Texture texture;



    public Puddle(int x, int y, int width, int height, Texture entityTexture, String id){
        super(x,y,width,height,entityTexture, id);
        isSolid = false;
        this.texture = entityTexture;
    }

    public void show(){
        this.sprite.setX(0);
        this.sprite.setY(0);
        this.visible=true;
        
    }

}

