package io.github.OMAL_Maze;
import com.badlogic.gdx.utils.Timer;

import java.time.chrono.MinguoChronology;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private int miniutesRemaining = 10;
    private Timer.Task myTimerTask;
    private SpriteBatch batch;
    private BitmapFont font;
    private String timerText = "Time = 0";
    FitViewport viewport;
    Texture backgroundTexture;
    Texture playerTexture;
    Movement movement;
    Player player;
    Array<Entity> entities;
    Array<Building> buildings;
    private static Main instance;

    //button experiment
    Button button;

    public static Main getInstance() {
        return instance;
    }
    @Override
    public void create() {
        entities = new Array<>();
        buildings = new Array<>();
        batch = new SpriteBatch();
        viewport = new FitViewport(400, 400);
        backgroundTexture = new Texture("screenTextures/maze1_WL.png");
        playerTexture = new Texture("entityTextures/playerCopy.png");
        movement = new Movement();
        player = new Player(0,0,15,15,playerTexture);
        font = new BitmapFont();
        timerText = "Time: " + miniutesRemaining;
        startTimer();
        Building fakeNisa = new Building(100,100,56,42,new Texture("buildingTextures/NiniLool.png"));
        Building CS_Building = new Building(50,340,64,45,new Texture("buildingTextures/CS_Building.png"));
        buildings.add(fakeNisa);
        buildings.add(CS_Building);
        //Background music plays the entire time
        Sound BackgroundMusic = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/Background.mp3"));
        BackgroundMusic.play();

        entities.add(player);
        instance = this;

        //button experiments
        button = new Button(Gdx.files.internal("button.png"));

    }

    private void startTimer() {
        myTimerTask = new Timer.Task() {
            @Override
            public void run() {
                if (miniutesRemaining > 0) {
                    miniutesRemaining--;
                    timerText = "Time: " + miniutesRemaining;
                } else {
                    System.out.println("Time is up!");
                    this.cancel();
                }
            }
        };
        Timer.schedule(myTimerTask, 1f, 1f);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();


        batch.begin();
        font.draw(batch,timerText,50,450);
        batch.end();
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
        font.draw(batch, timerText,10, worldHeight - 10);
        for (Building building: buildings) {
            building.render(batch);
        }
        batch.draw(button,0,0,button.getWidth(),button.getHeight());
        batch.end();

        if(button.isClicked()){
            System.out.println("Button clicked");
            //perform action when button is clicked
        }
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
        font.dispose();
        }
}




