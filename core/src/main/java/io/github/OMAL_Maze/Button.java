package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class Button extends Texture {
    Button(FileHandle s){
        super(s);
    }
    boolean isClicked(){
        return Gdx.input.isTouched();
    }

}
