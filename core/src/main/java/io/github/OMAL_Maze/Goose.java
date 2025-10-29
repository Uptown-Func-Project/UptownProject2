package io.github.OMAL_Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class Goose extends Character{
    Player player;
    gooseState state;
    Sprite sprite;
    Boolean isMoving;
    enum gooseState{
        IDLE,
        ANGRY,
        HAPPY
    }

    public Goose(int x, int y, int width, int height, Texture entityTexture, Player player) {
        super(x, y, width, height, entityTexture);
        visible = true;
        this.player = player;
        //state = gooseState.IDLE;
        state = gooseState.ANGRY; // for testing, delete later
        Main instance = Main.getInstance();
        sprite = super.sprite;
        //isMoving = false;


        // trying to prevent goose from leaving screen. does not work >
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, instance.viewport.getWorldWidth()-width));
        sprite.setY(MathUtils.clamp(sprite.getY(),0,instance.viewport.getWorldHeight()-height));
    }

    public void show(){
        visible = true;
        if (state == gooseState.IDLE){
            //chill
        }
        if (state == gooseState.ANGRY){
            followPlayer();

            // bite on collision
        }
        if (state == gooseState.HAPPY){
            //reward
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
    public void logic(){
        float playerX = player.sprite.getX();
        float playerY = player.sprite.getY();
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
        hide();
    }
}
