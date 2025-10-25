package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class CloseSettingsButton extends AbstractButton{
    //inherits makeActive and makeInactive from the superclass


    public CloseSettingsButton(FileHandle image){
        super(image);
        super.x = 200;
        super.y = 200;
        super.message = "Close";
    }
}

