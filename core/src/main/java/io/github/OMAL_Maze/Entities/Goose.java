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
    public boolean knockbackActive = false;
    private Animation walkAnimation;
    private boolean[][] mapy;
    private boolean facingRight = true;
    private int lastMoveX = 0;

    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

    /**
     * Constructor for the goose class.
     */
    public Goose(int x, int y, int width, int height, Texture entityTexture) {
        super(x, y, width, height, entityTexture);
        visible = false;
        state = gooseState.IDLE;
        this.isMoving = false;
        this.speed=75f;
        this.accelerate=600f;
        this.friction=3000f;
        this.Xspeed=0f;
        this.Yspeed=0f;
        this.instance = Main.getInstance();
        this.bitPlayer=false;
        this.biteTimer=5f;
        this.healthPoints=3;
        this.createTrigger();
        this.walkAnimation = new Animation(0.1,4);
        this.walkAnimation.setLooping(true);
        mapy = new boolean[][]{
    {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true},
    {true,false,false,false,false,false,true,true,false,false,true,false,false,false,false,false,false,false,false,false,false,false},
    {true,true,true,false,true,false,true,true,true,false,true,false,true,true,true,true,true,true,false,true,false,true},
    {true,false,false,false,true,false,false,false,false,false,true,false,false,false,true,false,false,false,false,false,true,false,true},
    {true,false,true,true,true,true,true,true,true,true,true,true,true,true,false,true,true,true,true,true,false,true},
    {true,false,true,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,true,false,true},
    {true,false,true,false,true,true,true,true,true,false,false,true,true,true,true,true,false,true,true,true,false,true},
    {true,false,true,false,true,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,true,false,true},
    {true,false,false,false,true,false,true,true,true,true,true,true,true,true,false,false,false,true,false,true,false,true},
    {true,false,true,true,true,false,false,false,true,true,true,true,true,true,false,false,false,true,false,true,false,true},
    {true,false,true,false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,false,true,false,true},
    {true,false,true,false,true,true,true,false,true,true,true,true,true,true,true,true,false,false,true,false,true,false,true},
    {true,false,true,false,false,false,true,false,true,true,true,true,true,true,true,true,false,false,true,true,true,false,true},
    {true,true,true,false,true,false,true,true,true,true,true,true,true,true,true,false,false,false,false,false,false,true},
    {true,false,false,false,true,false,false,false,false,false,true,true,false,true,true,false,false,false,false,false,false,true},
    {true,false,true,false,true,true,true,true,true,false,false,true,false,true,true,false,false,true,false,true,true,true},
    {true,false,true,false,false,false,false,true,true,true,false,true,false,true,true,false,false,true,false,false,false,true},
    {true,true,true,true,true,true,false,true,true,true,false,true,false,true,true,true,true,true,true,true,true,true,true},
    {true,false,false,false,true,false,false,true,true,true,false,true,false,false,false,false,false,false,true,false,false,false,true},
    {true,false,true,false,true,false,true,true,false,true,false,true,true,true,true,true,false,true,false,true,false,true},
    {false,false,true,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,true,false,true},
    {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,true}
};
        gooseQuack = Gdx.audio.newSound(Gdx.files.internal("Sounds/Geese.mp3"));
    }

    /**
     * Sets the goose position and makes it visible.
     */
    public void show(){
        this.sprite.setX(11*this.instance.tileSize);
        this.sprite.setY(14*this.instance.tileSize+16);
        this.visible=true;
        this.isMoving=true;
        this.spawned=true;
        this.soundID = gooseQuack.play();
        this.soundTimer=5f;
        this.state=gooseState.ANGRY;
    }

    @Override
    public void logic(){
        float delta = Gdx.graphics.getDeltaTime();
        // Prevent goose leaving screen
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));

        // Ensure player is set (player is a static reference in Main)
        this.player = Main.player;

        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        if (playerBounds.overlaps(this.spawnTrigger) && (this.spawned==null || !this.spawned)) {
            this.show();
            Main.getInstance().decrementHiddenEventCounter();
        }

        if (this.soundTimer>0f) {
            this.soundTimer-=delta;
        } else {
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
                this.bitPlayer = false;
                this.biteTimer = 5f;
                this.isMoving=true;
                this.solidTimer=0.5f;
                this.isSolid=true;
            }
        }
    }

    /**
     * Decreases the player's hearts and sets bite state.
     */
    public void bitePlayer(){
        player.decreaseHearts();
        isMoving = false;
        this.bitPlayer=true;
        this.biteTimer=5f;
        this.solidTimer=0.5f;
    }

    /**
     * Applies an external velocity to the goose (used by Player when hitting with bat).
     * This method will cancel bite state and enable knockbackActive so the movement() decay code runs.
     */
    public void applyExternalVelocity(float xVel, float yVel) {
        this.Xspeed = xVel;
        this.Yspeed = yVel;
        this.bitPlayer = false;      // cancel any bite
        this.isMoving = false;       // stop chasing while being knocked back
        this.knockbackActive = true; // enable knockback-handling in movement
    }

    /**
     * Movement override. If knockbackActive, goose is pushed by Xspeed/Yspeed and slows down.
     */
    @Override
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {
        this.player = Main.player;

        // If currently being knocked back, apply that movement and decay to zero
        if (knockbackActive) {
            // Cap speed according to your usual caps
            capSpeed(delta);

            // Try to move using the current velocities
            tryMove(entities, buildings);

            // Smoothly decay velocities (game-feel)
            float decayRate = 4f; // higher = quicker stop
            Xspeed = MathUtils.lerp(Xspeed, 0f, decayRate * delta);
            Yspeed = MathUtils.lerp(Yspeed, 0f, decayRate * delta);

            // If velocity is small, stop knockback and resume normal behaviour
            if (Math.abs(Xspeed) < 6f && Math.abs(Yspeed) < 6f) {
                Xspeed = 0f;
                Yspeed = 0f;
                knockbackActive = false;
                // After being knocked back, resume chasing if goose is angry
                if (state == gooseState.ANGRY) {
                    this.isMoving = true;
                }
            }

            // Run regular logic (timers, sounds, spawn triggers)
            this.logic();
            return; // skip chasing logic this frame
        }

        // Normal chasing / wandering behaviour when not knocked back
        if (isMoving && state.equals(gooseState.ANGRY) ) {

            int[] goal = new int[] {
        (int)(player.getPlayerX() / 40),
        (int)(player.getPlayerY() / 40)
    };
            int[] start = new int[] {
        (int)(this.getGooseX() / 40),
        (int)(this.getGooseY() / 40)
    };
            int[] next = io.github.OMAL_Maze.Map.AStar.getNextMove(mapy, start, goal);
            float vx = 0, vy = 0;
            boolean isMoving = false;

            if (next != null) {
                float targetX = next[0] * 16 + 8;
                float targetY = next[1] * 16 + 8;

                float dx = targetX - player.getPlayerX();
                float dy = targetY - player.getPlayerY();

                float dist = (float)Math.sqrt(dx * dx + dy * dy);

                if (dist > 0.1f) {
                    vx = (dx / dist) * speed;
                    vy = (dy / dist) * speed;
                    isMoving = true;
                }

                if (vx > 0) { facingRight = true; lastMoveX = 1; }
                if (vx < 0) { facingRight = false; lastMoveX = -1; }
                Xspeed = vx;
                Yspeed = vy;
                System.out.println(player.getPlayerX()/40 + "," + player.getPlayerY()/40);
                capSpeed(delta);
                 tryMove(entities, buildings);
            }
        }
        else{
            Xspeed=0;
            Yspeed=0;
        }
         if (isMoving){
        walkAnimation.update(delta);
    }
    else{
        walkAnimation.reset();
    }


        // Call normal logic after movement code
        this.logic();
    }
   
    /**
     * Attempts to move using the current move values and if a collision is detected, the goose cannot move there.
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
     */
    private boolean checkOverlaps(Array<Entity> entities, Array<Building> buildings) {
        for (int i=0;i< buildings.size;i++) {
            Building building=buildings.get(i);
            if (building.Overlaps(this.sprite.getBoundingRectangle())) {
                return true;
            }
        }
        if (!this.state.equals(gooseState.HAPPY)) {
            for (int i = 0; i < entities.size; i++) {
                Entity entity = entities.get(i);
                if (entity == this) continue;
                if (!entity.isSolid) continue;
                if (entity.Overlaps(this.sprite.getBoundingRectangle())) {
                    if (entity.getClass() == Player.class) {
                        Player player = (Player) entity;
                        if (this.state != gooseState.HAPPY) {
                            if (player.hasSeeds) {
                                this.state = gooseState.HAPPY;
                                Main.getInstance().decrementGoodEventCounter();
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

    // getter for health points
    public int getHealthPoints(){
        return healthPoints;
    }

    // decreases health points
    public void decreaseHealthPoints(){
        if (healthPoints > 0){
            healthPoints--;
        }
        if (healthPoints == 0){
            this.visible = false;
            this.isMoving = false;
            this.isSolid = false;
            this.state = gooseState.HAPPY;
            Main.getInstance().decrementGoodEventCounter();
        }
    }
    public int getGooseX(){
        return (int) this.sprite.getX();
    }
    public int getGooseY(){
        return (int) this.sprite.getY();
    }
    /**
     * Creates the trigger area for spawning the goose.
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
    public int getWalkFrame(){
        return walkAnimation.getCurrentFrame();
    }
}
