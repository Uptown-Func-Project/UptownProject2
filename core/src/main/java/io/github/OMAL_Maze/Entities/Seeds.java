package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.graphics.Texture;


//takes most attributes from superclass but solid is false so it can be collected
public class Seeds extends Entity {
    public Seeds(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
    }
}
