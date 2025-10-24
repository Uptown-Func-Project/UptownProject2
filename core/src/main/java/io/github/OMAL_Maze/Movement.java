package io.github.OMAL_Maze;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

// player movement???
public class Movement {
    private final float speed = 100f;
    private final float accelerate = 800;
    private final float friction = 4000f;
    float Xspeed = 0;
    float Yspeed = 0;
    public void update(float delta, Entity entity) {
        Main instance = Main.getInstance();
        Array<Entity> entities = instance.entities;
        Sprite playerSprite = entity.sprite;
        Array<Building> buildings = instance.buildings;
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
        if (Xspeed>speed) Xspeed = speed;
        if (Yspeed>speed) Yspeed = speed;
        if (Xspeed<-speed) Xspeed = -speed;
        if (Yspeed<-speed) Yspeed = -speed;

        float moveX = Xspeed * delta;
        float moveY = Yspeed * delta;

        playerSprite.translateX(moveX);
        Rectangle playerBounds = playerSprite.getBoundingRectangle();

        boolean collisionX = false;
        for (Entity possibleEntity : entities) {
            if (possibleEntity==entity) continue;
            if (possibleEntity.Overlaps(playerBounds) && possibleEntity.visible) {
                collisionX = true;
                break;
            }
        }
        for (Building building: buildings) {
            if (building.Overlaps(playerBounds)) {
                collisionX = true;
                break;
            }
        }

        if (collisionX) {
            playerSprite.translateX(-moveX);
            Xspeed = 0;
        }

        playerSprite.translateY(moveY);
        playerBounds = playerSprite.getBoundingRectangle();

        boolean collisionY = false;
        for (Entity possibleEntity : entities) {
            if (possibleEntity==entity) continue;
            if (possibleEntity.Overlaps(playerBounds) && possibleEntity.visible) {
                collisionY = true;
                break;
            }
        }
        for (Building building: buildings) {
            if (building.Overlaps(playerBounds)) {
                collisionY = true;
                break;
            }
        }

        if (collisionY) {
            playerSprite.translateY(-moveY);
            Yspeed = 0;
        }
        entity.logic();
    }
}