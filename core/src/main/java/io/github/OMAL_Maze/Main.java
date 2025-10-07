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

import java.util.Vector;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    FitViewport viewport;
    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sprite bucketSprite; // Declare a new Sprite variable
    Vector2 touchPos;
    Array<Sprite> dropSprites;



    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        //image = new Texture("libgdx.png");
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        bucketSprite = new Sprite(bucketTexture); // Initialize the sprite based on the texture
        bucketSprite.setSize(1, 1); // Define the size of the sprite
        touchPos = new Vector2();
        dropSprites = new Array<>();
        createDroplet();
    }

    @Override
    public void render() {
        //batch.begin();
        //batch.draw(image, 140, 210);
        //batch.end();
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta);
        }
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }

    private void logic() {
        // Store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        // Clamp x to values between 0 and worldWidth
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth-bucketWidth));
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
        //batch.draw(bucketTexture, 0, 0, 1, 1); // draw the bucket with width/height of 1 meter
        bucketSprite.draw(batch);
        batch.end();
    }

    private void createDroplet() {
        // create local variables for convenience
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // create the drop sprite
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(0);
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite); // Add it to the list
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(640, 480, true); // true centers the camera
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
