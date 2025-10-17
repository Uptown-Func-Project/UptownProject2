package io.github.OMAL_Maze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
    Sprite playerSprite; // Declare a new Sprite variable
    Texture wallTexture;
    Character wall;
    Movement movement;
    Player player;
    Array<Entity> entities;
    private static Main instance;

    public Main() {
        instance = this;
    }
    public static Main getInstance() {
        return instance;
    }
    @Override
    public void create() {
        entities = new Array<>();
        batch = new SpriteBatch();
        viewport = new FitViewport(400, 400);
        backgroundTexture = new Texture("maze_background.png");
        playerTexture = new Texture("playerCopy.png");
        //dropTexture = new Texture("drop.png");
        playerSprite = new Sprite(playerTexture); // Initialize the sprite based on the texture
        playerSprite.setSize(15, 15); // Define the size of the sprite
        wallTexture = new Texture("wallMaybe.png");
        wall = new Character(50,50,10,10,wallTexture);
        movement = new Movement();
        //dropSprites = new Array<>();
        player = new Player(0,0,15,15,playerTexture);
        entities.add(player);
        entities.add(wall);
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
        //movement.update(delta,playerSprite);
        for (Entity entity: entities) {
            if (entity instanceof Player) {
                movement.update(delta, entity);
            }
        }
    }

    private void logic() {
        /*for (Entity entity: entities) {
            entity.logic();
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
        for (Entity entity: entities) {
            entity.render(batch);
        }
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
