package io.github.OMAL_Maze;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Movement {
    private float x;
    private float y;
    private float speed = 200f;
    private float accelerate = 400f;
    private float friction = 4000f;
    float Xspeed = 0;
    float Yspeed = 0;
    public void update(float delta, Sprite playerSprite) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Xspeed += accelerate * delta;
            //if (Xspeed < 0) Xspeed = 0;
            if (Xspeed < 0) Xspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Xspeed -= accelerate * delta;
            //if (Xspeed > 0) Xspeed = 0;
            if (Xspeed > 0) Xspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            Yspeed += accelerate * delta;
            if (Yspeed < 0) Yspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            Yspeed -= accelerate * delta;
            if (Yspeed >0) Yspeed *= 0.25f;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Xspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            Yspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        if (Xspeed>speed) Xspeed=speed;
        if (Yspeed>speed) Yspeed = speed;
        if (Xspeed<-speed) Xspeed = -speed;
        if (Yspeed<-speed) Yspeed = -speed;
        playerSprite.translate(Xspeed * delta, Yspeed * delta);

    }
}