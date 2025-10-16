package io.github.OMAL_Maze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    FitViewport viewport;
    Texture backgroundTexture;
    Texture playerTexture;
    Texture dropTexture;
    Sprite playerSprite; // Declare a new Sprite variable
    Vector2 touchPos;
    Texture wallTexture;
    Sprite wallSprite;
    Array<Sprite> wallSprites;
    Movement movement;


    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(400, 400);
        backgroundTexture = new Texture("maze_background.png");
        playerTexture = new Texture("playerCopy.png");
        //dropTexture = new Texture("drop.png");
        playerSprite = new Sprite(playerTexture); // Initialize the sprite based on the texture
        playerSprite.setSize(15, 15); // Define the size of the sprite
        wallTexture = new Texture("wallMaybe.png");
        wallSprite = new Sprite(wallTexture);
        wallSprite.setSize(2,20);
        //touchPos = new Vector2();
        movement = new Movement();
        //dropSprites = new Array<>();

    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        //float speed = 40f;
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        movement.update(delta,playerSprite);
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerSprite.translateX(speed * delta * 0.75f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerSprite.translateX(-speed * delta * 0.75f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerSprite.translateY(speed * delta * 0.5f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerSprite.translateY(-speed * delta * 0.5f);
        }*/
        /*if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            playerSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }*/
    }

    private void logic() {
        // Store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        // Clamp x to values between 0 and worldWidth
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth-playerWidth));
        wallSprite.setX(playerSprite.getX()+20);
        wallSprite.setY(playerSprite.getY());
        //float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        /*
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);

            // if the top of the drop goes below the bottom of the view, remove it
            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
        }
        dropTimer += delta; // Adds the current delta to the timer
        if (dropTimer > 1f) { // Check if it has been more than a second
            dropTimer = 0; // Reset the timer
            createDroplet(); // Create the droplet
        }*/
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
        //batch.draw(playerTexture, 0, 0, 1, 1); // draw the bucket with width/height of 1 meter
        playerSprite.draw(batch);
        /*for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(batch);
        }*/
        wallSprite.draw(batch);
        batch.end();
    }

    /*private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); // Randomize the drop's x position
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }*/

    @Override
    public void resize(int width, int height) {
        viewport.update(909, 909, true); // true centers the camera
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
