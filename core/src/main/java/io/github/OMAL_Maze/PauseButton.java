package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class PauseButton extends AbstractButton{
    //inherits makeActive and makeInactive from the superclass

    public PauseButton(FileHandle image) {
        super(image);
        super.x = 300;
        super.y = 350;
        super.message = "Pause";
    }
}
