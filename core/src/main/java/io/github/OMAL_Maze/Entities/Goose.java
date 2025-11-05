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
    private float wanderTimer = 0f;
    private boolean wandering=false;
    public Rectangle spawnTrigger;
    Boolean spawned;
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
        this.createTrigger();
    }

    /**
     * Sets the goose position and makes it visible.
     * Should only be called when spawning the goose once.
     */
    public void show(){
        //"Spawn goose" by moving it to right spawn point
        this.setPos(11*this.instance.tileSize,14*this.instance.tileSize+16);
        //Make goose visible
        this.visible=true;
        //Start goose moving
        this.isMoving=true;
        //Add a boolean to make this only happen once.
        this.spawned=true;
        //play anrgy goose sound

        Sound GooseQuack = Gdx.audio.newSound(Gdx.files.internal("assets/Geese.mp3"));
        GooseQuack.play();
        try {
        // Pause the main thread for 5 seconds
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }
        GooseQuack.pause();

        //Make goose angry by default
        this.state=gooseState.ANGRY;
    }

    /**
     * Unused so far.
     */
    public void hide(){
        visible = false;
        state = gooseState.IDLE;
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

        if (bitPlayer) {
            if (this.solidTimer>0f) {
                this.solidTimer -= delta;
            } else {
                this.isSolid=false;
            }
            if (this.biteTimer>0f) {
                this.biteTimer -= delta;
            } else {
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
            //final float speed = 1f;
            float X_diff = this.player.sprite.getX() - this.x;
            float Y_diff = this.player.sprite.getY() - this.y;
            double distance = Math.sqrt((Math.pow(X_diff, 2) + Math.pow(Y_diff, 2)));
            if (distance==0) return;
            double unitVector_x=X_diff/distance;
            double unitVector_y=Y_diff/distance;
            if (unitVector_x > 0) {
                Xspeed+=accelerate*delta;
                if (Xspeed<0) Xspeed*=0.25f;
            } else if (unitVector_x<0) {
                Xspeed-=accelerate*delta;
                if (Xspeed>0) Xspeed*=0.25f;
            } else {
                Xspeed*=Math.max(0,1-friction*delta/speed);
            }
            if (unitVector_y > 0) {
                Yspeed+=accelerate*delta;
                if (Yspeed<0) Yspeed*=0.25f;
            } else if (unitVector_y<0) {
                Yspeed-=accelerate*delta;
                if (Yspeed>0) Yspeed*=0.25f;
            } else {
                Yspeed*=Math.max(0,1-friction*delta/speed);
            }
            capSpeed(delta);
            tryMove(entities, buildings);
        } else if (isMoving && this.state.equals(gooseState.HAPPY)){
            wanderTimer -= delta;
            if (wanderTimer <= 0) {
                if (wandering) {
                    Xspeed = 0;
                    Yspeed = 0;
                    wandering = false;
                    wanderTimer = 1f + (float)(Math.random() * 3f);
                } else {
                    double angle = Math.random() * 2 * Math.PI;
                    Xspeed = (float)(Math.cos(angle) * speed);
                    Yspeed = (float)(Math.sin(angle) * speed);
                    wandering = true;
                    wanderTimer = 0.25f + (float)(Math.random() * 1.5f);
                }
            }
            capSpeed(delta);
            tryMove(entities, buildings);
        }
        this.logic();
    }

    void tryMove(Array<Entity> entities, Array<Building> buildings) {
        this.translate(moveX,0);
        if (checkOverlaps(entities,buildings)) {
            this.translate(-moveX,0);
            Xspeed=0;
        }
        this.translate(0,moveY);
        if (checkOverlaps(entities,buildings)) {
            this.translate(0,-moveY);
            Yspeed=0;
        }
        this.logic();
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
            if (building.Overlaps(this.rectangle)) {
                return true;
            }
        }
        if (!this.state.equals(gooseState.HAPPY)) {
            for (int i = 0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if (entity == this) continue;
                if (!entity.isSolid) continue;
                if (entity.Overlaps(this.rectangle)) {
                    if (entity.getClass() == Player.class) {
                        Player player = (Player) entity;
                        if (this.state != gooseState.HAPPY) {
                            if (player.hasSeeds) {
                                //Set goose to happy
                                this.state = gooseState.HAPPY;
                                Main.getInstance().decrementGoodEventCounter();
                                //Could play goose happy sound?
                                //Set goose solid to false
                                this.isSolid = false;
                                //Reward player
                                player.speed *= 2f;
                                player.hasSeeds = false;
                                this.instance.setSecondsRemaining(this.instance.getSecondsRemaining() + 30);
                            } else {
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

    /**
     * Sets the position of the entity. Makes sure to update relevant values.
     * Required function as this.x and this.y don't always sync with the rectangle or sprite.
     * @param newX The new X location as a float value.
     * @param newY The new Y location as a float value.
     */
    private void setPos(float newX, float newY) {
        this.x=(int)newX;
        this.y=(int)newY;
        this.rectangle.x=newX;
        this.rectangle.y=newY;
        this.sprite.setX(newX);
        this.sprite.setY(newY);
    }

    /**
     * Translates the position of the goose.
     * This function is required to sync all values used for the position.
     * @param distX The distance to move in the X axis as a float value.
     * @param distY The distance to move in the Y axis as a float value.
     */
    private void translate(float distX, float distY) {
        this.sprite.translate(distX,distY);
        this.x=(int)this.sprite.getX();
        this.y=(int)this.sprite.getY();
        this.rectangle.x=this.sprite.getX();
        this.rectangle.y=this.sprite.getY();
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
