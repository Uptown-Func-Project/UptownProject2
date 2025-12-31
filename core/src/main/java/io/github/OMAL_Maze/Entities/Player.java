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
import io.github.OMAL_Maze.Dialogue.DialogueManager;

/**
 * the player class which extends the {@link Character} class
 * Can pickup items
 * Has hearts(lives)
 */
public class Player extends Character{
    public int hearts;
    static Sound itemPickup;
    public boolean hasSeeds;
    public boolean hasDegree;
    public int degreeState;

    /**
     * Spawns a player entity and sets the default values for hearts, seeds, speed, acceleration, and friction.
     * @param x horizontal spawn location of the player.
     * @param y vertical spawn location of the player.
     * @param width width of the player in pixels.
     * @param height height of the player in pixels.
     * @param entityTexture Texture object for the player sprite.
     */
    public Player(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height, entityTexture);
        this.visible = true;
        this.hearts = 3;
        this.hasSeeds = false;
        this.hasDegree = false;
        this.degreeState = 0; // 0 = no degree, 1 = bad score, 2 = perfect score
        this.speed=150f;
        this.accelerate=800f;
        this.friction=4000f;
    }

    /**
     * Clamps the player location to the world and handles the seeds logic.
     */
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
        
        // TODO remove this - freddie
        //System.out.println(sprite.getX() + " x, " + sprite.getY() + " y");


        Rectangle playerBounds = sprite.getBoundingRectangle();
        if (!this.hasSeeds) {
            //picking up seeds
            for(int i=0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if(entity instanceof Seeds) {
                    //getting bounding box
                    
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

        if (degreeState == 0) {
            // interacting with professor
    
            for (int i=0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if (entity instanceof Professor) {
                    Professor prof = (Professor) entity;
                    Rectangle profBounds = prof.getInteractionBounds();

                    if (playerBounds.overlaps(profBounds) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        // if player is within bounds and press e, interact with professor
                        DialogueManager.getInstance().startDialogue("start");
                        System.out.println("Player is interacting with the Professor.");
                    }
                }
            }

        }
    }

    /**
     * Handles the player input
     * @param delta Time since the last frame. Used for timers.
     * @param entities Array of entities
     * @param buildings Array of buildings
     */
    @Override
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {
        if (io.github.OMAL_Maze.Dialogue.DialogueManager.getInstance().isDialogueActive()) return;
        
        //If either right arrow or D is pressed, move right.
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            Xspeed += accelerate * delta;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Xspeed < 0) Xspeed *= 0.25f;
        }
        //If either left arrow or A is pressed, move left.
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            Xspeed -= accelerate * delta;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Xspeed > 0) Xspeed *= 0.25f;
        }
        //If either up arrow or W is pressed, move up.
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            Yspeed += accelerate * delta;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Yspeed < 0) Yspeed *= 0.25f;
        }
        //If either down arrow or S is pressed, move down.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            Yspeed -= accelerate * delta;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Yspeed >0) Yspeed *= 0.25f;
        }

        //If neither left nor right is pressed, decrease the horizontal movement using a friction value.
        if (!(Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D))
                && !(Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))) {
            Xspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        //If neither up nor down is pressed, decrease the vertical movement using a friction value.
        if (!(Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W))
                && !(Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S))) {
            Yspeed *= Math.max(0, 1 - friction * delta / speed);
        }
        //Cap the speed to the maximum if the values are too high as a result of the movement calculations.
        capSpeed(delta);

        //Attempt to move in the X direction. If this collides, revert the movement.
        this.sprite.translateX(moveX);
        boolean collisionX = collidesOnMove(entities, buildings);
        if (collisionX) {
            this.sprite.translateX(-moveX);
            Xspeed = 0;
        }

        //Attempt to move in the Y direction. If this collides, revert the movement.
        this.sprite.translateY(moveY);
        boolean collisionY = collidesOnMove(entities, buildings);
        if (collisionY) {
            this.sprite.translateY(-moveY);
            Yspeed = 0;
        }
        //Now that the entity has moved, call the logic function.
        this.logic();

    }

    /**
     * Checks if the movement that has been applied causes the player to collide with any entities or buildings.
     * @param entities Array of the current map entities.
     * @param buildings Array of the current map buildings.
     * @return A boolean value that is true if the player collides and false if it does not.
     */
    private boolean collidesOnMove(Array<Entity> entities, Array<Building> buildings) {
        boolean collisionX = false;
        Rectangle playerBounds = this.sprite.getBoundingRectangle();
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
        return collisionX;
    }

    /**
     * Getter method for the player hearts.
     */
    public int getHearts(){
        return hearts;
    }

    /**
     * Decreases the player's hearts. This value ranges from 0-3 and once all 3 hearts/lives have been taken, the game ends.
     */
    public void decreaseHearts(){
        if (hearts > 1){
            //If the player still has hearts left, slow down the player and decrease the value.
            hearts--;
            this.speed*=0.75f;
        } else {
            //If the player doesn't have any hearts left, call the function to end the game.
            Main.getInstance().gameOver();
        }
        //Decrease the counter that the main game uses for the bad events as this is a bad event.
        Main.getInstance().decrementBadEventCounter();
    }
}
