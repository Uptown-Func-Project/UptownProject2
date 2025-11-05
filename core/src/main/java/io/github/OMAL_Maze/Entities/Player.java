package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Map.Building;

public class Player extends Character{
    public int hearts;
    static Sound itemPickup;
    public boolean hasSeeds;

    public Player(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height, entityTexture);
        this.visible = true;
        this.hearts = 3;
        this.hasSeeds = false;
        this.speed=150f;
        this.accelerate=800f;
        this.friction=4000f;
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

        if (!this.hasSeeds) {
            //picking up seeds
            for(int i=0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if(entity instanceof Seeds) {
                    //getting bounding box
                    Rectangle playerBounds = sprite.getBoundingRectangle();
                    Rectangle seedBounds = entity.sprite.getBoundingRectangle();

                    //checking bounding box
                    if (playerBounds.overlaps(seedBounds)) {
                        entities.removeIndex(i);
                        this.hasSeeds = true;
                        //seeds pickup sound
                        itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                        if (this.hasSeeds) {
                            itemPickup.play();
                        }
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            Xspeed += accelerate * delta;
            //if (Xspeed < 0) Xspeed = 0;
            if (Xspeed < 0) Xspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            Xspeed -= accelerate * delta;
            //if (Xspeed > 0) Xspeed = 0;
            if (Xspeed > 0) Xspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            Yspeed += accelerate * delta;
            if (Yspeed < 0) Yspeed *= 0.25f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            Yspeed -= accelerate * delta;
            if (Yspeed >0) Yspeed *= 0.25f;
        }

        if (!(Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D))
                && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))) {
            Xspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        if (!(Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W))
                && !(Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S))) {
            Yspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        capSpeed(delta);

        this.sprite.translateX(moveX);
        Rectangle playerBounds = this.sprite.getBoundingRectangle();

        boolean collisionX = false;
        for (int i=0;i<entities.size;i++) {
            Entity possibleEntity = entities.get(i);
            if (possibleEntity==this) continue;
            if (!possibleEntity.isSolid) continue;
            if (!this.isSolid) continue;
            if (possibleEntity.Overlaps(playerBounds)) {
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
            this.sprite.translateX(-moveX);
            Xspeed = 0;
        }

        this.sprite.translateY(moveY);
        playerBounds = this.sprite.getBoundingRectangle();

        boolean collisionY = false;
        for (int i=0;i<entities.size;i++) {
            Entity possibleEntity = entities.get(i);
            if (possibleEntity==this) continue;
            if (!possibleEntity.isSolid) continue;
            if (!this.isSolid) continue;
            if (possibleEntity.Overlaps(playerBounds)) {
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
            this.sprite.translateY(-moveY);
            Yspeed = 0;
        }
        this.logic();

    }

    public void decreaseHearts(){
        if (hearts > 0){
            hearts--;
            //Slow down player.
            this.speed*=0.8f;
        } else {
            System.out.println("Bit again at 0 hearts. Game should maybe end here.");
        }
        // else: game over
        Main.getInstance().decrementBadEventCounter();
    }
}
