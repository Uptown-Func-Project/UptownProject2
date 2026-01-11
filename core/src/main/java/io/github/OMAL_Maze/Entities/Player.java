package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.OMAL_Maze.Dialogue.DialogueManager;
import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Map.Building;

/**
 * the player class which extends the {@link Character} class
 * Can pickup items
 * Has hearts (lives)
 * Can collect coins
 */
public class Player extends Character{
    public int hearts;
    static Sound itemPickup;
    public boolean hasSeeds;
    public int coins;
    public String[] coins_log;
    public boolean hasDegree;
    public int degreeState;
    public boolean hasBat;
    private Animation swingAnim;
    private Animation walkAnim;
    public float knockbackForce = 100000f; //how hard the impact is

    private float swingTimer = 0f;
    private float swingDuration = 0.18f;
    private boolean swinging = false;
    private boolean rightFace = true;
    public Bat batSwingEffect;


    /**
     * Spawns a player entity and sets the default values for hearts, seeds, speed, acceleration, and friction.
     * @param x horizontal spawn location of the player.
     * @param y vertical spawn location of the player.
     * @param width width of the player in pixels.
     * @param height height of the player in pixels.
     * @param entityTexture Texture object for the player sprite.
     * @param id a unique identifier for each entity.

     */
    public Player(int x, int y, int width, int height, Texture entityTexture, String id) {
        super(x, y, width, height, entityTexture, id);
        this.visible = true;
        this.hearts = 3;
        this.hasSeeds = false;
        this.hasDegree = false;
        this.degreeState = 0; // 0 = no degree, 1 = bad score, 2 = perfect score
        this.coins = 0;
        this.speed=200f;
        this.accelerate=800f;
        this.friction=4000f;
        this.coins_log=new String[18];
        this.hasBat = false;
        this.speed=150f;
        this.accelerate=800f;
        this.friction=4000f;
        this.walkAnim = new Animation(0.1, 4);
        this.walkAnim.setLooping(true);
        this.swingAnim = new Animation(0.05, 4);
        this.swingAnim.setLooping(true);
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

        //picking up seeds, coins, energy drinks and food
        for(int i=0; i < entities.size; i++) {
            Entity entity = entities.get(i);

            // Seeds, costs 4 coins
            if(entity instanceof Seeds && this.coins >= 4) {
                //getting bounding box
                Rectangle playerBounds = sprite.getBoundingRectangle();
                Rectangle seedBounds = entity.sprite.getBoundingRectangle();

                //checking bounding box
                if (playerBounds.overlaps(seedBounds)) {
                    entities.removeIndex(i);
                    this.hasSeeds = true;
                    this.coins-=4;
                    //seeds pickup sound
                    itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                    if (this.hasSeeds) {
                        itemPickup.play();
                    }
                    break;
                }
            }

            // Coins
            else if(entity instanceof Coin coin) {
                //getting bounding box
                Rectangle playerBounds = sprite.getBoundingRectangle();
                Rectangle coinBounds = entity.sprite.getBoundingRectangle();

                //checking bounding box
                if (playerBounds.overlaps(coinBounds) && coin.visible == true) {
                    entities.get(i).visible=false;
                    this.coins ++;
                    //seeds pickup sound
                    itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                    itemPickup.play();

                    // Adds the coin to the coin log
                    String coin_collected = entity.getId();
                    coins_log[coins-1]=coin_collected;
                    break;
                }
            }

            // Energy drink, costs 2 coins
            else if(entity instanceof EnergyDrink && this.coins >= 2) {
                //getting bounding box
                Rectangle playerBounds = sprite.getBoundingRectangle();
                Rectangle energyDrinkBounds = entity.sprite.getBoundingRectangle();

                //checking bounding box
                if (playerBounds.overlaps(energyDrinkBounds)) {
                    entities.removeIndex(i);
                    this.coins-=2;
                    //seeds pickup sound
                    itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                    itemPickup.play();

                    // Provides a 50% speed boost
                    this.speed*=1.5;
                    break;
                }
            }

            // Food, costs 2 coins
            else if(entity instanceof Food && this.coins >= 2) {
                //getting bounding box
                Rectangle playerBounds = sprite.getBoundingRectangle();
                Rectangle foodBounds = entity.sprite.getBoundingRectangle();

                //checking bounding box
                if (playerBounds.overlaps(foodBounds)) {
                    entities.removeIndex(i);
                    this.coins-=2;
                    //seeds pickup sound
                    itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                    itemPickup.play();

                    // Gives an extra life
                    this.hearts+=1;
                    break;
                }
            }
        }


        // interacting with entities for dialogue
        for (int i=0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            Rectangle playerBounds = sprite.getBoundingRectangle();
            // interacting with professor
            if (entity instanceof Professor) { 
                Professor prof = (Professor) entity;
                Rectangle profBounds = prof.getInteractionBounds();

                if (playerBounds.overlaps(profBounds) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    // if player is within bounds and press e, interact with professor
                    if (degreeState == 0) {
                        DialogueManager.getInstance().startDialogue("prof_start");
                        System.out.println("Player is interacting with the Professor.");
                    }
                    else if (degreeState == 1) {
                        DialogueManager.getInstance().startDialogue("imperfect_answer2");
                    }
                    else if (degreeState == 2) {
                        DialogueManager.getInstance().startDialogue("prof_idle");
                    }

                }
            }

            // interacting with Degree guy
            if (entity instanceof degreeGuy) { 
                degreeGuy degreeG = (degreeGuy) entity;
                Rectangle degreeGBounds = degreeG.getInteractionBounds();

                if (playerBounds.overlaps(degreeGBounds) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    System.out.println("within entity bounds");
                    if (degreeState == 0) {
                        DialogueManager.getInstance().startDialogue("degreeStart0");
                    }
                    else if (degreeState == 1) {
                        DialogueManager.getInstance().startDialogue("degreeFail");
                    }
                    else if (degreeState == 2) {
                        DialogueManager.getInstance().startDialogue("degreeStart");
                    }

                }
            }
            // interacting with Dean
        }

        if (!this.hasBat) {
            //picking up bat
            for(int i=0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if(entity instanceof Bat) {
                    //getting bounding box
                    Rectangle playerBounds = sprite.getBoundingRectangle();
                    Rectangle batBounds = entity.sprite.getBoundingRectangle();

                    //checking bounding box
                    if (playerBounds.overlaps(batBounds)) {
                        entities.removeIndex(i);
                        this.hasBat = true;
                        this.batSwingEffect = Main.getInstance().bat;
                        //bat pickup sound
                        itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));
                        if (this.hasBat) {
                            itemPickup.play();
                        }
                        break;
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
        
        boolean moving = false;
        //batswing float 
        
        // Check for puddle overlap and temporarily slow the player's movement parameters
        boolean inPuddle = false;
        Rectangle pBounds = this.sprite.getBoundingRectangle();
        for (int i = 0; i < entities.size; i++) {
            Entity e = entities.get(i);
            if (e instanceof Puddle) {
                if (pBounds.overlaps(e.sprite.getBoundingRectangle())) {
                    inPuddle = true;
                    break;
                }
            }
        }
        float origSpeed = this.speed;
        float origAccelerate = this.accelerate;
        final float PUDDLE_SLOW_FACTOR = 0.15f; // % speed inside puddle
        if (inPuddle) {
            this.speed = origSpeed * PUDDLE_SLOW_FACTOR;
            this.accelerate = origAccelerate * PUDDLE_SLOW_FACTOR;
        }

        if (hasBat && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
    swinging = true;
    swingTimer = swingDuration;
    batHitGeese(entities, delta);
    
    
}


    
        //If either right arrow or D is pressed, move right.
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            Xspeed += accelerate * delta;
            moving = true;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Xspeed < 0) Xspeed *= 0.25f;
            rightFace = true;
        }
        //If either left arrow or A is pressed, move left.
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            Xspeed -= accelerate * delta;
            moving = true;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Xspeed > 0) Xspeed *= 0.25f;
            rightFace = false;
        }
        //If either up arrow or W is pressed, move up.
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            Yspeed += accelerate * delta;
            moving = true;
            //If the acceleration is in the opposite direction to previously, use a multiplier to increase the speed of deceleration.
            if (Yspeed < 0) Yspeed *= 0.25f;
        }
        //If either down arrow or S is pressed, move down.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            Yspeed -= accelerate * delta;
            moving = true;
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
        //System.out.println(this.getPlayerX()/40 + "," + this.getPlayerY()/40);
        //Cap the speed to the maximum if the values are too high as a result of the movement calculations.
        capSpeed(delta);
        

        //Attempt to move in the X direction. If this collides, revert the movement.
        this.sprite.translateX(moveX);
        boolean collisionX = collidesOnMove(entities, buildings);
        if (collisionX) {
            this.sprite.translateX(-moveX);
            Xspeed = 0;
        }
        if (moving){
            walkAnim.update(Gdx.graphics.getDeltaTime());
        } else {
            walkAnim.reset();
        }
        //Attempt to move in the Y direction. If this collides, revert the movement.
        this.sprite.translateY(moveY);
        boolean collisionY = collidesOnMove(entities, buildings);
        if (collisionY) {
            this.sprite.translateY(-moveY);
            Yspeed = 0;
        }
        // restore original movement parameters if modified
        if (inPuddle) { this.speed = origSpeed; this.accelerate = origAccelerate; }
        //Now that the entity has moved, call the logic function.
        this.logic();
        delta = Gdx.graphics.getDeltaTime();
        float frameDelta = Gdx.graphics.getDeltaTime();
        if (swinging) {
            swingAnim.update(frameDelta);
            swingTimer -= frameDelta;
            if (swingTimer <= 0) {
                swinging = false;
    }
}
        else {
            swingAnim.reset();
        }
        
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
    public int getPlayerX(){ 
        return (int)this.sprite.getX();

    }
    public int getPlayerY(){ 
        return (int)this.sprite.getY();

    }
    public void setSpeed(float newSpeed){
        this.speed=newSpeed;
    }
    /**
     * Getter method for the player hearts.
     */
    public boolean getHasBat(){
        return hasBat;
    }
    public boolean getHasDegree(){
        return hasDegree;
    }
    public int getHearts(){
        return hearts;
    }
    private void batHitGeese(Array<Entity> entities, float delta) {
    float dirX = 0f;
    float dirY = 0f;

    if (Math.abs(Xspeed) > Math.abs(Yspeed)) {
        dirX = Math.signum(Xspeed);
    } else {
        dirY = Math.signum(Yspeed);
    }

    if (dirX == 0 && dirY == 0) {
        dirX = 1f;
    }

    if (batSwingEffect != null) {
        batSwingEffect.swingAt(sprite.getX(), sprite.getY(), dirX, dirY);
    }

    float hitDistance = 26f;
    float hitSize = 34f;

    Rectangle attackBox = new Rectangle(
            sprite.getX() + sprite.getWidth() / 2 - hitSize / 2,
            sprite.getY() + sprite.getHeight() / 2 - hitSize / 2,
            hitSize,
            hitSize
    );

    attackBox.x += dirX * hitDistance;
    attackBox.y += dirY * hitDistance;

    for (Entity entity : entities) {
        if (entity instanceof Goose && entity.getVisible()) {
            Goose goose = (Goose) entity;
            if (goose.Overlaps(attackBox)) {
                goose.decreaseHealthPoints();
                goose.applyExternalVelocity(dirX * knockbackForce, dirY * knockbackForce);
            }
        }
        if (entity instanceof Geesey && entity.getVisible()) {
            Geesey geesey = (Geesey) entity;
            if (geesey.Overlaps(attackBox)) {
                geesey.decreaseHealthPoints();
                geesey.applyExternalVelocity(dirX * knockbackForce, dirY * knockbackForce);
            }
        }
        if (entity instanceof Dean && entity.getVisible()) {
            Dean dean = (Dean) entity;
            if (dean.Overlaps(attackBox)) {
                dean.decreaseHealthPoints();
                dean.applyExternalVelocity(dirX * knockbackForce, dirY * knockbackForce);
            }
        }
    }
}
    public boolean isRightFace() {
        return rightFace;
    }

    public int getDegree() {
        return degreeState;
    }

    /**
     * Getter method for the player coins.
     */
    public int getCoins(){
        return coins;
    }

    /**
     * Decreases the player's hearts. This value ranges from 0-3 and once all 3 hearts/lives have been taken, the game ends.
     */
    public void decreaseHearts(){
        if (hearts > 1){
            // If the player still has hearts left, slow down the player and decrease the value.
            hearts--;
            this.speed*=2f;
        } else {
            // If the player doesn't have any hearts left, call the function to end the game
            // Also initialises the player, for if the game restarts
            this.visible = true;
            this.hearts = 3;
            this.hasSeeds = false;
            this.coins = 0;
            this.speed=200f;
            this.accelerate=800f;
            this.friction=4000f;
            this.coins_log=new String[18];
            Main.getInstance().gameOver();
        }
        // Decrease the counter that the main game uses for the bad events as this is a bad event.
        Main.getInstance().decrementBadEventCounter();
    }
    public int getAnimationFrame() { return swingAnim.getCurrentFrame(); }
    public int getWalkAnimationFrame() { return walkAnim.getCurrentFrame(); }
}
