package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Goose extends Character{
    Player player;
    gooseState state;
    Boolean isMoving;
    Boolean bitPlayer;
    float biteTimer;
    Main instance;
    Rectangle spawnTrigger;
    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

    public Goose(int x, int y, int width, int height, Texture entityTexture) {
        super(x, y, width, height, entityTexture);
        visible = true;
        state = gooseState.IDLE;
        //state = gooseState.ANGRY; // for testing, delete later
        this.isMoving = false;
        this.speed=100f;
        this.accelerate=600f;
        this.friction=3000f;
        this.instance = Main.getInstance();
        this.bitPlayer=false;
        this.createTrigger();
    }

    public void show(){
        visible = true;
        switch (state) {
            case IDLE:
                //Chill
                break;
            case ANGRY:
                followPlayer();
                break;
            case HAPPY:
                //Reward, increment timer by 30s?
                break;
            default:
                break;
        }
    }

    public void hide(){
        visible = false;
        state = gooseState.IDLE;
    }

    /*
    causes goose to follow player
     */
    public void followPlayer(){
        isMoving = true;
    }

    /*
    Where the goose moves - in future the code within can be hopefully replaced with calling a Movement class
    */
    @Override
    public void logic(){
        // trying to prevent goose from leaving screen. does not work >
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));
        float playerX = player.sprite.getX();
        float playerY = player.sprite.getY();
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        if (playerBounds.overlaps(this.spawnTrigger)) {
            //"Spawn goose" by moving it to right spawn point
            //Make goose visible
            //Start goose moving
            //Add a boolean to make this only happen once.
        }

        if (isMoving) {

            /* TODO:
            if (goose collides with player)   {
                bitePlayer();
            }
            */

            final float speed = 1f;
            float X_diff = playerX - this.x;
            float Y_diff = playerY - this.y;
            double distance = Math.sqrt((Math.pow(X_diff, 2) + Math.pow(Y_diff, 2)));
            double unitVector_x = X_diff / distance;
            double unitVector_y = Y_diff / distance;
            sprite.translate((float) (unitVector_x * speed), (float) (unitVector_y * speed));
//            rectangle.x = sprite.getX();
//            rectangle.y = sprite.getY();
        } else if (bitPlayer) {
            float delta = Gdx.graphics.getDeltaTime();
            if (this.biteTimer>0f) {
                this.biteTimer -= delta;
            } else {
                this.bitPlayer = false;
                this.biteTimer = 5000f;
                this.isMoving=true;
            }
        }
    }

    /*
    // When angry goose collides with player, this method should be called to decrease the health of the player.
     */
    public void bitePlayer(){
        player.decreaseHearts();
        // need to prevent goose from immediately re-biting player - move goose negative direction a small distance
        // temporary solution for testing - goose hides and stops moving on collision with player (delete later):
        isMoving = false;
        this.biteTimer=5000f;
        //hide();
    }
    @Override
    public void movement(float delta, Array<Entity> entities, Array<Building> buildings) {
        this.player= Main.player;
        this.logic();
    }



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
