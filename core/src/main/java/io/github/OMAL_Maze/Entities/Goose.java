package io.github.OMAL_Maze.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Map.Building;

/**
 * Goose NPC with different states.
 * Can bite the player to reduce health and slow them down.
 */
public class Goose extends Character{
    Player player;
    gooseState state;
    Boolean isMoving;
    Boolean bitPlayer;
    float biteTimer;
    Main instance;
    float solidTimer = 0.5f;
    float soundTimer = 0f;
    private float wanderTimer = 0f;
    private boolean wandering=false;
    public Rectangle spawnTrigger;
    Boolean spawned;
    Long soundID;
    Sound gooseQuack;
    public int healthPoints;
    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

    /**
     * Constructor for the goose class.
     * Calls super and sets goose to an idle, invisible entity.
     * Creates a trigger area for the goose to spawn in.
     * @param x X-axis (horizontal) location of the goose
     * @param y Y-axis (vertical) location of the goose
     * @param width width of the goose
     * @param height height of the goose
     * @param entityTexture the Texture object for the goose sprite
     */
    public Goose(int x, int y, int width, int height, Texture entityTexture) {
        super(x, y, width, height, entityTexture);
        visible = false;
        state = gooseState.IDLE;
        //state = gooseState.ANGRY; // for testing, delete later
        this.isMoving = false;
        this.speed=75f;
        this.accelerate=600f;
        this.friction=3000f;
        this.Xspeed=0f;
        this.Yspeed=0f;
        this.instance = Main.getInstance();
        this.bitPlayer=false;
        this.biteTimer=5f;
        this.healthPoints=1;
        this.createTrigger();
        gooseQuack = Gdx.audio.newSound(Gdx.files.internal("Sounds/Geese.mp3"));
    }

    /**
     * Sets the goose position and makes it visible.
     * Should only be called when spawning the goose once.
     */
    public void show(){
        //"Spawn goose" by moving it to right spawn point
        this.sprite.setX(11*this.instance.tileSize);
        this.sprite.setY(14*this.instance.tileSize+16);
        //Make goose visible
        this.visible=true;
        //Start goose moving
        this.isMoving=true;
        //Add a boolean to make this only happen once.
        this.spawned=true;
        //play angry goose sound
        this.soundID = gooseQuack.play();
        this.soundTimer=5f;
        //Make goose angry by default
        this.state=gooseState.ANGRY;
    }

    /**
     * Overridden logic class.
     * Used to clamp sprite.
     * Also used to determine goose spawn from player location.
     * Also checks the bite timer to restart goose movement.
     */
    @Override
    public void logic(){
        float delta = Gdx.graphics.getDeltaTime();
        // trying to prevent goose from leaving screen. does not work >
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        if (playerBounds.overlaps(this.spawnTrigger)&&(this.spawned==null || !this.spawned)) {
            this.show();
            Main.getInstance().decrementHiddenEventCounter();
        }

        if (this.soundTimer>0f) {
            this.soundTimer-=delta;
        } else {
            //If the sound is playing, and it shouldn't be, stop the sound.
            if (this.soundID!=null) {
                gooseQuack.stop(this.soundID);
            }
        }

        if (bitPlayer) {
            if (this.solidTimer>0f) {
                this.solidTimer -= delta;
            } else {
                this.isSolid=false;
            }
            if (this.biteTimer>0f) {
                this.biteTimer -= delta;
            } else {
                //Once the player has been bitten, and the cooldown ends, the goose can bite the player again.
                this.bitPlayer = false;
                this.biteTimer = 5f;
                this.isMoving=true;
                /* TODO:
                Check if this has any issues for collision (aka do an overlap check first)
                Might be annoying if it is actively overlapping as safe location needs to be found.
                 */
                this.solidTimer=0.5f;
                this.isSolid=true;
            }
        }

    }

    /**
     * Decreases the player's hearts.
     * Also stops the goose from moving and attacking for 5s.
     * When not attacking, the goose becomes an entity that can be moved past.
     */
    public void bitePlayer(){
        //Lower the player's hearts by 1 and stop moving for 5 seconds.
        player.decreaseHearts();
        isMoving = false;
        this.bitPlayer=true;
        //Timer starts.
        this.biteTimer=5f;
        //Goose becomes unsolid so it can be walked past.
        //Now handled using a timer to add a delay
        this.solidTimer=0.5f;
        //this.isSolid=false;
    }

    /**
     * Movement override, moves the goose towards the player if "isMoving" set to true.
     * @param delta Time in seconds since last frame, used for speed difference.
     * @param entities Array of the current entities in the map
     * @param buildings Array of the current buildings in the map
     */
    @Override
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {
        this.player= Main.player;
        //Follow player if moving and angry (angry upon spawn)
        if (isMoving && state.equals(gooseState.ANGRY) ) {
            //Determines the difference between the player and the goose to get the distance.
            float X_diff = this.player.sprite.getX() - this.sprite.getX();
            float Y_diff = this.player.sprite.getY() - this.sprite.getY();
            double distance = Math.sqrt((Math.pow(X_diff, 2) + Math.pow(Y_diff, 2)));
            //Returns if the goose is already at the player. This avoids divide by 0 exceptions.
            if (distance==0) return;
            double unitVector_x=X_diff/distance;
            double unitVector_y=Y_diff/distance;
            //Accelerate in the direction of the player horizontally.
            if (unitVector_x > 0) {
                Xspeed+=accelerate*delta;
                if (Xspeed<0) Xspeed*=0.25f;
            } else if (unitVector_x<0) {
                Xspeed-=accelerate*delta;
                if (Xspeed>0) Xspeed*=0.25f;
            } else {
                Xspeed*=Math.max(0,1-friction*delta/speed);
            }
            //Accelerate in the direction of the player vertically.
            if (unitVector_y > 0) {
                Yspeed+=accelerate*delta;
                if (Yspeed<0) Yspeed*=0.25f;
            } else if (unitVector_y<0) {
                Yspeed-=accelerate*delta;
                if (Yspeed>0) Yspeed*=0.25f;
            } else {
                Yspeed*=Math.max(0,1-friction*delta/speed);
            }
            //Call capSpeed to ensure the goose isn't moving too fast.
            capSpeed(delta);
            //Call try move to attempt to move based on the current speed.
            tryMove(entities, buildings);
        } else if (isMoving && this.state.equals(gooseState.HAPPY)){
            //The goose is happy after being given seeds and can now wander around.
            wanderTimer -= delta;
            //A timer is used to ensure the goose doesn't look jittery or change directions too often.
            if (wanderTimer <= 0) {
                if (wandering) {
                    //If recently wandering, stand still for a small amount of time.
                    Xspeed = 0;
                    Yspeed = 0;
                    wandering = false;
                    wanderTimer = 1f + (float)(Math.random() * 3f);
                } else {
                    //If not wandered recently, start to wander around for a small amount of time.
                    double angle = Math.random() * 2 * Math.PI;
                    Xspeed = (float)(Math.cos(angle) * speed);
                    Yspeed = (float)(Math.sin(angle) * speed);
                    wandering = true;
                    wanderTimer = 0.25f + (float)(Math.random() * 1.5f);
                }
            }
            //Call capSpeed to ensure the goose isn't moving too fast.
            capSpeed(delta);
            //Attempt to move based on the current speed.
            tryMove(entities, buildings);
        }
        //Call the logic function to execute any logic such as out of bounds or other overlapping.
        this.logic();
    }

    /**
     * Attempts to move using the current move values and if a collision is detected, the goose cannot move there.
     * @param entities Array of the current map entities.
     * @param buildings Array of the current map buildings.
     */
    private void tryMove(Array<Entity> entities, Array<Building> buildings) {
        this.sprite.translate(moveX,0);
        if (checkOverlaps(entities,buildings)) {
            this.sprite.translate(-moveX,0);
            Xspeed=0;
        }
        this.sprite.translate(0,moveY);
        if (checkOverlaps(entities,buildings)) {
            this.sprite.translate(0,-moveY);
            Yspeed=0;
        }
    }

    /**
     * Checks if the goose entity overlaps with any of the current entities or buildings.
     * Used for the movement to stop goose moving into solid entities or buildings.
     * @param entities Array of current entities in the map
     * @param buildings Array of current buildings in the map
     * @return boolean for whether the function overlaps or not
     */
    private boolean checkOverlaps(Array<Entity> entities, Array<Building> buildings) {
        for (int i=0;i< buildings.size;i++) {
            Building building=buildings.get(i);
            if (building.Overlaps(this.sprite.getBoundingRectangle())) {
                return true;
            }
        }
        //The goose will only overlap if it isn't happy.
        if (!this.state.equals(gooseState.HAPPY)) {
            //Iterate through each entity.
            for (int i = 0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                //Do not compare the entity to itself.
                if (entity == this) continue;
                //Do not check for overlaps if the entity isn't solid and therefore doesn't collide.
                if (!entity.isSolid) continue;
                if (entity.Overlaps(this.sprite.getBoundingRectangle())) {
                    if (entity.getClass() == Player.class) {
                        //Collision with the player has specific behaviour and so is compared using the class.
                        Player player = (Player) entity;
                        if (this.state != gooseState.HAPPY) {
                            if (player.hasSeeds) {
                                //Set goose to happy
                                this.state = gooseState.HAPPY;
                                //Could play goose happy sound?
                                Main.getInstance().decrementGoodEventCounter();
                                //Set goose solid to false. Commenting this out so that the player has to get the seeds.
                                //this.isSolid = false;
                                //Reward player
                                player.speed *= 2f;
                                player.hasSeeds = false;
                                this.instance.setSecondsRemaining(this.instance.getSecondsRemaining() + 30);
                            } else {
                                //Bite player if the player doesn't bring the goose seeds.
                                this.bitePlayer();
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    //getter for health points
    public int getHealthPoints(){
        return healthPoints;
    }
    //decreaser for health points
    public void decreaseHealthPoints(){
        if (healthPoints > 0){
            healthPoints--;
        }
        if (healthPoints == 0){
            //goose dies
            this.visible = false;
            this.isMoving = false;
            this.isSolid = false;
            Main.getInstance().decrementGoodEventCounter();
        }
    }
    /**
     * Creates the trigger area for spawning the goose.
     * These values are hardcoded for level 2, however different functionality should be used if any other goose is used in the future.
     */
    void createTrigger() {
        int leftX = 6;
        int leftY = 8;
        int width = 9;
        int height = 8;
        int tileSize = this.instance.tileSize;
        this.spawnTrigger = new Rectangle(
                (leftX-1)*tileSize+ (float) tileSize /2,
                (leftY-1)*tileSize+ (float) tileSize /2,
                (width*tileSize),
                (height*tileSize)
        );
    }

}
