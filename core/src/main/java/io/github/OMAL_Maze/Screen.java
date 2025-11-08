package io.github.OMAL_Maze;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.OMAL_Maze.Buttons.AbstractButton;

/**
 * Screen Class - lays out the basic framework for how each screen will function once called
 * 
 */
public class Screen{
    protected Texture backgroundTexture; // will hold the image file (pause_background)
    protected Sprite backgroundSprite;
    protected ArrayList<AbstractButton> buttons;
    private SpriteBatch batch;
    private Viewport viewport; // will handle screen scaling / resizing
    protected Boolean active;
/**
 * Constructs a new {@code Screen} instance
 * @param batch         the {@link SpriteBatch} used for drawing
 * @param viewport      the {@link FitViewport} controlling scaling and camera.
 * @param backgroundPath the relative path to the background texture file.
 */
    public Screen(SpriteBatch batch, FitViewport viewport, String backgroundPath){
        this.batch = batch;
        this.viewport = viewport;
        this.backgroundTexture = new Texture(Gdx.files.internal(backgroundPath));
        this.backgroundSprite = new Sprite(backgroundTexture);
        this.buttons = new ArrayList<>();
        this.active = false;
        setupBackground();
    }
/**
 * Configures the background sprite so that it fills the entire screen
 */
    protected void setupBackground() {
        float vw = viewport.getWorldWidth();
        float vh = viewport.getWorldHeight();
        backgroundSprite.setSize(vw,vh);
        backgroundSprite.setPosition(0,0);
    }
/**
 * Renders the screen each frame
 */
    public void render(){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (backgroundSprite != null) {
            backgroundSprite.draw(batch);
        }

        if (buttons != null){
            for (AbstractButton b : buttons) {
                if (b.isActive()){
                    b.draw(batch);
                    if (b.isClicked((FitViewport) viewport)){{
                        handleButtonClick(b);
                    }}
                }

            }
        }
        batch.end();
    }

    protected void handleButtonClick(AbstractButton b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleButtonClick'");
    }
    public void resize(int width, int height){
        viewport.update(width, height,true);
        setupBackground(); 
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (buttons != null){
            // was dispose but changed to makeInactive
            for (AbstractButton b : buttons) b.makeInactive();
        }
    }

    //get and set for active
    public boolean getActive(){
        return active;
    }
    public void setActive(Boolean state){
        active = state;
    }
}    
