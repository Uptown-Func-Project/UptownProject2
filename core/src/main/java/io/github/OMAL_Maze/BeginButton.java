package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class BeginButton extends AbstractButton{
    //inherits makeActive and makeInactive from the superclass
//    int x = 0;
//    int y = 200;

    public BeginButton(FileHandle image){
       super(image);
        super.x = 200;
        super.y = 200;
        super.message = "begin button";


    }

}
