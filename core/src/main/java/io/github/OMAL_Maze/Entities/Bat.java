package io.github.OMAL_Maze.Entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * bat class that extends {@link Entity} class
 * weapon player can swing
 * non-solid to allow it to pass through objects during swing
 */
public class Bat extends Entity {
    public float lifetime = 0f;
    public boolean visible = false;
    public Texture texture;
    

    /**
     * bat constructor
     * 
     * @param x 
     * @param y 
     * @param width
     * @param height
     * @param entityTexture
     */
    public Bat(int x, int y, int width, int height, Texture entityTexture){
    super(x,y,width,height,entityTexture, "bat");
    this.texture = entityTexture; 
    
    isSolid = false;
}

    /**
     * bat swing animation in specified direction.
     * bat is positioned a bit off from the player's center towards wherever the player is facing
     * 
     * @param playerX players x coords
     * @param playerY player y coords
     * @param dirX x direction
     * @param dirY y direction
     */
    public void swingAt(float playerX, float playerY, float dirX, float dirY) {
    float offset = 24f;  
    float centerX = playerX + sprite.getWidth() / 2f;
    float centerY = playerY + sprite.getHeight() / 2f;
    float posX = centerX + dirX * offset - sprite.getWidth() / 2f;
    float posY = centerY + dirY * offset - sprite.getHeight() / 2f;

    sprite.setPosition(posX, posY);

   
    float angleDeg = 0f;
    if (dirX != 0 || dirY != 0) {
        angleDeg = com.badlogic.gdx.math.MathUtils.atan2Deg(dirY, dirX);
    }
    
    sprite.setOriginCenter();
    sprite.setRotation(angleDeg);

    visible = true;
    lifetime = 0.12f;

}

    /**
     * Update bat's visibility based on lifetime timer
     * decrements the lifetime timer and hides the bat when time expires
     * called every frame by game loop
     */
    @Override
    public void logic() {
        if (!visible) return;

        lifetime -= Gdx.graphics.getDeltaTime();
        if (lifetime <= 0f) {
            visible = false;
            lifetime = 0f;
        }
    }
}
