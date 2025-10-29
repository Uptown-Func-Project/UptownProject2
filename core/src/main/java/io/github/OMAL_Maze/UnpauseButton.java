package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class UnpauseButton extends AbstractButton{
    //inherits makeActive and makeInactive from the superclass
    public UnpauseButton(FileHandle image){
        super(image);
        super.x = 0;
        super.y = 200;
        super.message = "unpause button";
    }
}

