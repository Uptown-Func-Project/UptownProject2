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
    Boolean spawned;
    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

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
        this.createTrigger();
    }

    public void show(){
        //"Spawn goose" by moving it to right spawn point
        this.setPos(11*this.instance.tileSize,14*this.instance.tileSize+16);
        //Make goose visible
        this.visible=true;
        //Start goose moving
        this.isMoving=true;
        //Add a boolean to make this only happen once.
        this.spawned=true;
        /*switch (state) {
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
        }*/
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


    @Override
    public void logic(){
        // trying to prevent goose from leaving screen. does not work >
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        if (playerBounds.overlaps(this.spawnTrigger)&&(this.spawned==null || !this.spawned)) {
            this.show();
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
        if (isMoving) {

            /* TODO:
            if (goose collides with player)   {
                bitePlayer();
            }
            */

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
            if (Xspeed>speed)Xspeed=speed;
            if (Yspeed>speed)Yspeed=speed;
            if (Xspeed<-speed)Xspeed=-speed;
            if (Yspeed<-speed)Yspeed=-speed;
            float moveX=Xspeed*delta;
            float moveY=Yspeed*delta;
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
        } else if (bitPlayer) {
            if (this.biteTimer>0f) {
                this.biteTimer -= delta;
            } else {
                this.bitPlayer = false;
                this.biteTimer = 5000f;
                this.isMoving=true;
            }
        }
        this.logic();
    }

    private boolean checkOverlaps(Array<Entity> entities, Array<Building> buildings) {
        for (int i=0;i< buildings.size;i++) {
            Building building=buildings.get(i);
            if (building.Overlaps(this.rectangle)) {
                return true;
            }
        }
        for (int i=0;i< entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity==this) continue;
            if (!entity.isSolid) continue;
            if (entity.Overlaps(this.rectangle)) {
                return true;
            }
        }
        return false;
    }

    private void setPos(float newX, float newY) {
        this.x=(int)newX;
        this.y=(int)newY;
        this.rectangle.x=newX;
        this.rectangle.y=newY;
        this.sprite.setX(newX);
        this.sprite.setY(newY);
    }
    private void translate(float distX, float distY) {
        this.sprite.translate(distX,distY);
        this.x=(int)this.sprite.getX();
        this.y=(int)this.sprite.getY();
        this.rectangle.x=this.sprite.getX();
        this.rectangle.y=this.sprite.getY();
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
