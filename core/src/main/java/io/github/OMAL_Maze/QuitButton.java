package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class QuitButton extends AbstractButton{
    //inherits makeActive and makeInactive from the superclass


    public QuitButton(FileHandle image){
        super(image);
        super.x = 0;
        super.y = 0;
        super.message = "quit button";
    }
}