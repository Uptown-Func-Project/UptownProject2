package io.github.OMAL_Maze;
import com.badlogic.gdx.graphics.Texture;


//takes most attributes from suerclass but solid is false so it can be collected 
public class seeds extends Entity {
    public seeds(int x, int y, int width, int height, Texture entityTexture){
        super(x,y,width,height,entityTexture);
        isSolid = false;
    }
}
