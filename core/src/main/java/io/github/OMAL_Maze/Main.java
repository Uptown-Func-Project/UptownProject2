package io.github.OMAL_Maze;
import com.badlogic.gdx.utils.Timer;

import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.Collections;

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
    private int secondsRemaining = 10;
    private Timer.Task myTimerTask;
    private SpriteBatch batch;
    private BitmapFont font;
    private String timerText = "Time = 0:10";
    private boolean timerIsUp = false;
    FitViewport viewport;
    Texture backgroundTexture;
    Texture playerTexture;
    Texture seedsTexture;
    Entity seeds;
    Movement movement;
    Player player;
    Array<Entity> entities;
    Array<Building> buildings;
    private static Main instance;

    //button experiment
    //Button button;
    BeginButton begin;
    QuitButton quit;
    CloseSettingsButton closeSettings;
    OpenSettingsButton openSettings;
    PauseButton pause;
    UnpauseButton unpause;

    ArrayList<AbstractButton> buttons = new ArrayList<AbstractButton>(6);


    //Sounds
    Sound BackgroundMusic;

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
        seedsTexture = new Texture("entityTextures/Seeds.png");
        seeds = new seeds(110,80,15,15,seedsTexture);
        movement = new Movement();
        player = new Player(0,0,15,15,playerTexture);
        font = new BitmapFont();
        startTimer();
        Building fakeNisa = new Building(100,100,56,42,new Texture("buildingTextures/NiniLool.png"));
        Building CS_Building = new Building(50,340,64,45,new Texture("buildingTextures/CS_Building.png"));
        buildings.add(fakeNisa);
        buildings.add(CS_Building);

        //Background music plays the entire time
        BackgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3"));
        BackgroundMusic.play();

        entities.add(seeds);
        entities.add(player);
        instance = this;

        //button experiments
        //button = new Button(Gdx.files.internal("button.png"));
        begin = new BeginButton(Gdx.files.internal("button.png"));
        quit = new QuitButton(Gdx.files.internal("button.png"));
        closeSettings = new CloseSettingsButton(Gdx.files.internal("button.png"));
        openSettings = new OpenSettingsButton(Gdx.files.internal("button.png"));
        pause = new PauseButton(Gdx.files.internal("button.png"));
        unpause = new UnpauseButton(Gdx.files.internal("button.png"));

        Collections.addAll(buttons, begin, quit, closeSettings, openSettings, pause, unpause);

    }

    private void startTimer() {
        myTimerTask = new Timer.Task() {
            @Override
            public void run() {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    int minutes = secondsRemaining / 60;
                    int seconds = secondsRemaining % 60;
                    timerText = String.format("Time: %02d:%02d", minutes, seconds);
                } else {
                    timerIsUp = true; 
                    timerText = "Time: 00:00";
                    Building gameOverScreen = new Building(0,0,400,500,new Texture("buildingTextures/GAME OVER.png"));
                    buildings.add(gameOverScreen);
                    this.cancel();

                    Sound GameOverSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/Gameover.mp3"));
                    BackgroundMusic.pause();
                    GameOverSound.play();

                }
            }
        };
        Timer.schedule(myTimerTask, 1f, 1f); // dealys the timer speed by 1 second
    }

    @Override
    public void render() {
        input();
        logic();
        draw();

        batch.begin();
        font.draw(batch,timerText,50,450);
        //if player pickes up seeds then text is displayed
        if(player.HasSeeds) {
            if(secondsRemaining > 0) {
                font.draw(batch, "Inventory: Seeds", 200, 16);
            }
        }
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

        pause.makeActive();
        //begin.makeActive();

        for(AbstractButton b:buttons){  //for loop works
            //System.out.println(b);
            if (b.isActive()){
                b.draw(batch);
               // System.out.println("active");
                if (b.isClicked(viewport)){
                    System.out.println("clicked");
                }
            }
        }
        batch.end();
    }

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




