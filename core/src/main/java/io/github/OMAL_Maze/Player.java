package io.github.OMAL_Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends Character{

    public Player(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height,entityTexture);
    }
    @Override
    public void logic() {
        super.logic();
        Main instance = Main.getInstance();
        Viewport viewport = instance.viewport;
        Array<Entity> entities = instance.entities;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float playerWidth = sprite.getWidth();
        float playerHeight = sprite.getHeight();

        // Clamp x to values between 0 and worldWidth
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth-playerWidth));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,worldHeight-playerHeight));


    }

}
