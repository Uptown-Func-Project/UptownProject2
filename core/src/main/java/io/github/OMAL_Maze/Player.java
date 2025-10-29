package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends Character{
    public int hearts;
    static Sound itemPickup;
    public boolean hasSeeds;
  public Player(int x, int y, int width, int height, Texture entityTexture) {
        super(x,y,width,height, entityTexture);
        this.visible = true;
        this.hearts = 3;
        this.hasSeeds = false;
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

        //picking up seeds
        for(int i=0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            if(entity instanceof seeds) {
                //getting boudning box
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
    public void decreaseHearts(){
        if (hearts > 0){
            hearts--;
        }
        // else: game over
    }

}
