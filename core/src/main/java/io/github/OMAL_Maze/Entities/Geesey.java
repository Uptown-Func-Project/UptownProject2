package io.github.OMAL_Maze.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
public class Geesey extends Character{
    Player player;
    gooseState state;
    Boolean isMoving;
    Boolean bitPlayer;
    float biteTimer;
    Main instance;
    float solidTimer = 0.5f;
    float soundTimer = 0f;
    public Rectangle spawnTrigger;
    Boolean spawned;
    Long soundID;
    Sound gooseQuack;
    public int healthPoints;
    public boolean knockbackActive = false;
    private Animation walkAnimation;
    private boolean[][] mapy;
    
    private boolean facingRight = true;

    // --- NEW fields to detect & temporarily block stuck tiles ---
    private float[][] tempBlockedTTL;
    private int stuckCounter = 0;
    private int lastNextX = -1;
    private int lastNextY = -1;
    private final float TEMP_BLOCK_DURATION = 2.0f; // seconds
    private final int STUCK_THRESHOLD = 4;

    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

    /**
     * Constructor for the goose class.
     */
    public Geesey(int x, int y, int width, int height, Texture entityTexture, String id) {
        super(x, y, width, height, entityTexture,id);
        visible = false;
        state = gooseState.IDLE;
        this.isMoving = true;
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
  {true,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true},
  {true,false,true,false,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,true},
  {true,false,true,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,true,true},
  {true,false,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false,true},
  {true,false,true,false,false,false,false,false,false,true,false,false,false,true,false,false,false,true,true,true,false,true},
  {true,false,true,true,true,true,true,true,false,true,false,true,false,true,false,true,false,false,false,false,false,true},
  {true,false,false,false,false,true,false,false,false,true,false,true,false,false,false,true,false,true,true,true,false,true},
  {true,true,true,true,false,true,false,true,false,true,true,true,true,true,false,true,false,false,false,true,true,true},
  {true,false,true,true,false,true,false,true,false,true,true,false,true,true,true,true,true,true,false,true,true,true},
  {true,false,true,true,false,true,false,true,true,true,true,false,true,true,true,true,true,true,false,true,true,true},
  {true,false,true,true,false,true,false,false,false,false,false,false,true,true,false,false,false,false,false,false,false,true},
  {true,false,true,true,false,true,true,true,true,true,true,true,true,true,false,true,true,true,true,true,false,true},
  {true,false,true,true,false,false,false,false,false,false,true,false,false,true,false,true,false,false,false,true,false,true},
  {true,false,true,true,false,true,true,true,true,false,true,true,false,true,false,true,false,true,true,true,false,true},
  {true,false,true,true,false,true,false,true,true,false,false,true,false,true,false,true,false,true,true,true,false,true},
  {true,false,true,true,false,true,false,true,true,true,false,true,false,true,false,true,false,false,false,false,false,true},
  {true,false,true,true,false,true,false,true,true,true,false,true,false,true,false,true,true,true,true,true,false,true},
  {true,false,false,false,false,true,false,false,false,false,false,true,false,true,false,false,false,true,true,true,false,true},
  {true,true,true,true,true,true,true,true,false,true,true,true,false,true,true,true,false,true,false,true,false,true},
  {true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false},
  {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true}
}
;
        // initialize temporary-block TTL array
        tempBlockedTTL = new float[mapy.length][mapy[0].length];

        gooseQuack = Gdx.audio.newSound(Gdx.files.internal("Sounds/Geese.mp3"));
    }

    /**
     * Returns a random free tile (x,y) from the provided grid, or null if none.
     */
    public static int[] randomZero(boolean[][] grid) {
        List<int[]> zeros = new ArrayList<>();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!grid[y][x]) { 
                    zeros.add(new int[]{x, y});
                }
            }
        }

        if (zeros.isEmpty()) {
            return null; 
        }

        return zeros.get(ThreadLocalRandom.current().nextInt(zeros.size()));
    }

    public void show(){
        int tileSize = this.instance.tileSize;
        int[] coord = randomZero(mapy);
        int baseTx = (coord != null) ? coord[0] : 11;
        int baseTy = (coord != null) ? coord[1] : 14;
        int maxRadius = 6;
        int foundTx = -1, foundTy = -1;
        float bestDist = Float.MAX_VALUE;
        for (int dx = -maxRadius; dx <= maxRadius; dx++) {
            for (int dy = -maxRadius; dy <= maxRadius; dy++) {
                int tx = baseTx + dx;
                int ty = baseTy + dy;
                if (ty < 0 || tx < 0 || ty >= mapy.length || tx >= mapy[0].length) continue;
                if (!isTileBlocked(tx, ty, this.instance.entities, Main.buildings)) {
                    float dist = dx * dx + dy * dy;
                    if (dist < bestDist) {
                        bestDist = dist;
                        foundTx = tx;
                        foundTy = ty;
                    }
                }
            }
        }
        if (foundTx == -1) { // fallback to random coord or 11,14
            foundTx = baseTx;
            foundTy = baseTy;
        }
        this.sprite.setX(foundTx * tileSize + 16);
        this.sprite.setY(foundTy * tileSize + 16);
        this.visible = true;
        this.isMoving = true;
        this.spawned = true;
        this.soundID = gooseQuack.play();
        this.soundTimer = 5f;
        this.state = gooseState.ANGRY;
    }

    @Override
    public void logic(){
        float delta = Gdx.graphics.getDeltaTime();
        // Prevent goose leaving screen
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));

        // Ensure player is set (player is a static reference in Main)
        this.player = Main.player;

        if (player.getHasBat() && (this.spawned==null || !this.spawned)) {
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

        // decrement any temporary-block TTLs
        for (int y = 0; y < tempBlockedTTL.length; y++) {
            for (int x = 0; x < tempBlockedTTL[0].length; x++) {
                if (tempBlockedTTL[y][x] > 0f) {
                    tempBlockedTTL[y][x] -= delta;
                    if (tempBlockedTTL[y][x] < 0f) tempBlockedTTL[y][x] = 0f;
                }
            }
        }

        // If being knocked back, apply existing velocity and decay it
        if (knockbackActive) {
            // apply current velocity for this frame
            capSpeed(delta);
            tryMove(entities, buildings);

            // decay velocities smoothly
            float decayRate = 4f;
            this.Xspeed = MathUtils.lerp(this.Xspeed, 0f, decayRate * delta);
            this.Yspeed = MathUtils.lerp(this.Yspeed, 0f, decayRate * delta);

            if (Math.abs(this.Xspeed) < 6f && Math.abs(this.Yspeed) < 6f) {
                this.Xspeed = 0f;
                this.Yspeed = 0f;
                this.knockbackActive = false;
                if (state == gooseState.ANGRY) this.isMoving = true;
            }

            this.logic();
            return;
        }

        // Normal AI movement when angry
        if (isMoving != null && isMoving && state == gooseState.ANGRY) {
            int tileSize = (this.instance != null) ? this.instance.tileSize : 40;

            // use sprite centers for tile conversion
            float centerX = this.sprite.getX() + this.sprite.getWidth() / 2f;
            float centerY = this.sprite.getY() + this.sprite.getHeight() / 2f;
            float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2f;
            float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2f;

            // If map not available, fallback to direct chase
            if (mapy == null || mapy.length == 0) {
                float dx = playerCenterX - centerX;
                float dy = playerCenterY - centerY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > 1f) {
                    this.Xspeed = (dx / dist) * speed;
                    this.Yspeed = (dy / dist) * speed;
                } else {
                    this.Xspeed = 0f;
                    this.Yspeed = 0f;
                }

                if (this.Xspeed > 0) { facingRight = true; }
                else if (this.Xspeed < 0) { facingRight = false; }

                capSpeed(delta);
                tryMove(entities, buildings);

                if (this.Xspeed != 0f || this.Yspeed != 0f) walkAnimation.update(delta);
                else walkAnimation.reset();

                this.logic();
                return;
            }

            int mapH = mapy.length;
            int mapW = mapy[0].length;

            int startX = (int) (centerX / tileSize);
            int startY = (int) (centerY / tileSize);
            int goalX = (int) (playerCenterX / tileSize);
            int goalY = (int) (playerCenterY / tileSize);

            // clamp indices
            startX = Math.max(0, Math.min(mapW - 1, startX));
            startY = Math.max(0, Math.min(mapH - 1, startY));
            goalX = Math.max(0, Math.min(mapW - 1, goalX));
            goalY = Math.max(0, Math.min(mapH - 1, goalY));

            int[] start = new int[] { startX, startY }; // [x,y]
            
            int[] goal = new int[] { goalX, goalY };
            

            // build merged map = static walls OR temporary blocked tiles
            boolean[][] merged = copyMap(mapy);
            for (int y = 0; y < tempBlockedTTL.length; y++) {
                for (int x = 0; x < tempBlockedTTL[0].length; x++) {
                    if (tempBlockedTTL[y][x] > 0f) merged[y][x] = true;
                }
            }

            int[] next = io.github.OMAL_Maze.Map.AStar.getNextMove(merged, start, goal);
            

            
            if (next != null) {
                int nx = next[0], ny = next[1];
                if (ny < 0 || ny >= merged.length || nx < 0 || nx >= merged[0].length || merged[ny][nx]) {
                    
                    next = null;
                }
            }

            if (next != null && isTileBlocked(next[0], next[1], entities, buildings)) {
                boolean[][] tmp = copyMap(merged); 
                tmp[next[1]][next[0]] = true;
                int[] alt = io.github.OMAL_Maze.Map.AStar.getNextMove(tmp, start, goal);
                if (alt != null && !(alt[0] == startX && alt[1] == startY)) {
                    next = alt;
                } else {
                    next = null;
                }
            }

            boolean didMove = false;

            
            if (next != null) {
                if (next[0] == lastNextX && next[1] == lastNextY) {
                    // still trying same tile as last frame
                    stuckCounter++;
                } else {
                    stuckCounter = 0;
                }
            } else {
                stuckCounter = 0;
            }

            if (stuckCounter >= STUCK_THRESHOLD && next != null) {
                // mark the not working tile blocked for a while
                tempBlockedTTL[next[1]][next[0]] = TEMP_BLOCK_DURATION;
                stuckCounter = 0;
                // rebuild merged + recompute next
                boolean[][] tmpMerged = copyMap(mapy);
                for (int y = 0; y < tempBlockedTTL.length; y++) {
                    for (int x = 0; x < tempBlockedTTL[0].length; x++) {
                        if (tempBlockedTTL[y][x] > 0f) tmpMerged[y][x] = true;
                    }
                }
                next = io.github.OMAL_Maze.Map.AStar.getNextMove(tmpMerged, start, goal);
            }
            lastNextX = (next != null) ? next[0] : -1;
            lastNextY = (next != null) ? next[1] : -1;
            // --- END stuck detection ---

            if (next != null && !(next[0] == startX && next[1] == startY)) {
                // convert next tile to world center coords
                float targetX = next[0] * tileSize + tileSize / 2f;
                float targetY = next[1] * tileSize + tileSize / 2f;

                float dx = targetX - centerX;
                float dy = targetY - centerY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                if (dist > 0.01f) {
                    this.Xspeed = (dx / dist) * speed;
                    this.Yspeed = (dy / dist) * speed;
                    didMove = true;
                } else {
                    this.Xspeed = 0f;
                    this.Yspeed = 0f;
                }
            } else {
                // fallback: direct chase player to avoid getting stuck
                float dx = playerCenterX - centerX;
                float dy = playerCenterY - centerY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > 1f) {
                    this.Xspeed = (dx / dist) * speed;
                    this.Yspeed = (dy / dist) * speed;
                    didMove = true;
                } else {
                    this.Xspeed = 0f;
                    this.Yspeed = 0f;
                }
            }

            if (this.Xspeed > 0) { facingRight = true; }
            else if (this.Xspeed < 0) { facingRight = false; }

            capSpeed(delta);
            tryMove(entities, buildings);

            if (didMove) walkAnimation.update(delta); else walkAnimation.reset();

        } else {
            // not moving / not angry
            this.Xspeed = 0f;
            this.Yspeed = 0f;
            this.moveX = 0f;
            this.moveY = 0f;
            walkAnimation.reset();
        }

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
    public boolean getGeeseyHappy(){
        return this.state == gooseState.HAPPY;
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
    public boolean isFacingRight() { return facingRight; }

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

    // --- NEW helper: check if placing the sprite centered on tile (tx,ty) would collide ---
    private boolean isTileBlocked(int tx, int ty, Array<Entity> entities, Array<Building> buildings) {
        int tileSize = this.instance.tileSize;
        float centerX = tx * tileSize + tileSize / 2f;
        float centerY = ty * tileSize + tileSize / 2f;
        Rectangle testRect = new Rectangle(centerX - this.sprite.getWidth() / 2f,
                                           centerY - this.sprite.getHeight() / 2f,
                                           this.sprite.getWidth(),
                                           this.sprite.getHeight());
        // check buildings
        for (int i = 0; i < buildings.size; i++) {
            if (buildings.get(i).Overlaps(testRect)) return true;
        }
        // check solid entities (players, other mobs)
        for (int i = 0; i < entities.size; i++) {
            Entity e = entities.get(i);
            if (e == this) continue;
            if (!e.isSolid) continue;
            if (e.Overlaps(testRect)) return true;
        }
        // also check map walls (in case of tight collision due to sprite size)
        int mapH = mapy.length;
        int mapW = mapy[0].length;
        if (ty >= 0 && ty < mapH && tx >= 0 && tx < mapW && mapy[ty][tx]) return true;

        return false;
    }

    // --- NEW helper: shallow copy boolean[][] ---
    private boolean[][] copyMap(boolean[][] src) {
        int h = src.length;
        int w = src[0].length;
        boolean[][] dst = new boolean[h][w];
        for (int y = 0; y < h; y++) {
            System.arraycopy(src[y], 0, dst[y], 0, w);
        }
        return dst;
    }
}
