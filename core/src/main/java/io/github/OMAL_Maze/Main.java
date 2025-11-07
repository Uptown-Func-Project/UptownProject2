package io.github.OMAL_Maze;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.OMAL_Maze.Buttons.AbstractButton;
import io.github.OMAL_Maze.Buttons.BeginButton;
import io.github.OMAL_Maze.Buttons.CloseSettingsButton;
import io.github.OMAL_Maze.Buttons.OpenSettingsButton;
import io.github.OMAL_Maze.Buttons.PauseButton;
import io.github.OMAL_Maze.Buttons.QuitButton;
import io.github.OMAL_Maze.Buttons.UnpauseButton;
import io.github.OMAL_Maze.Entities.Character;
import io.github.OMAL_Maze.Entities.Entity;
import io.github.OMAL_Maze.Entities.EntityData;
import io.github.OMAL_Maze.Entities.Goose;
import io.github.OMAL_Maze.Entities.Player;
import io.github.OMAL_Maze.Entities.Seeds;
import io.github.OMAL_Maze.Map.Building;
import io.github.OMAL_Maze.Map.BuildingData;
import io.github.OMAL_Maze.Map.MazeData;
import io.github.OMAL_Maze.Map.MazeLoader;
import io.github.OMAL_Maze.Map.TriggerZone;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    //volume added in order to set to 0
    public static final float volume = 3;
    private int secondsRemaining = 300;
    private int badEventsRemaining = 1;
    private int goodEventsRemaining = 1;
    private int hiddenEventsRemaining = 1;
    private SpriteBatch batch;
    private BitmapFont font;
    private String timerText;
    public FitViewport viewport;
    Texture backgroundTexture;
    public Array<Entity> entities;
    Array<Building> buildings;
    Array<TriggerZone> triggerZones;
    public static Player player;
    public int tileSize;
    ShapeRenderer shapeRenderer; //for debugging, delete when necessary
    private float triggerCooldown = 0f;
    private static Main instance;
    private MazeData mazeData;

    BeginButton begin;
    QuitButton quit;
    CloseSettingsButton closeSettings;
    OpenSettingsButton openSettings;
    PauseButton pause;
    UnpauseButton unpause;
    //storing all buttons in an arraylist so they can be iterated through
    ArrayList<AbstractButton> buttons = new ArrayList<>(6);

    //Sounds
    Sound BackgroundMusic;

    public Main() {
        instance = this;
    }
    public static Main getInstance() {
        return instance;
    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        int worldWidth = 880;
        int worldHeight = 880;
        viewport = new FitViewport(worldWidth, worldHeight);
        tileSize= worldWidth /22;
        font = new BitmapFont();
        mazeData = MazeLoader.loadMaze("loadAssets/assets.json");
        instance = this;
        shapeRenderer = new ShapeRenderer();

        //Background music plays the entire time
        //Debugging line below, Used to spawn at start of second level.
        //loadMaze(1, 40, 80);
        //the images of the buttons can be changed here
        //begin = new BeginButton(Gdx.files.internal("button.png"));
        //quit = new QuitButton(Gdx.files.internal("button.png"));
        //closeSettings = new CloseSettingsButton(Gdx.files.internal("button.png"));
        //openSettings = new OpenSettingsButton(Gdx.files.internal("button.png"));
        //pause = new PauseButton(Gdx.files.internal("button.png"));
        //unpause = new UnpauseButton(Gdx.files.internal("button.png"));
        //adding all buttons to the arraylist in one go
        //Collections.addAll(buttons, begin, quit, closeSettings, openSettings, pause, unpause);
        startGame();
    }

    public void startGame() {
        BackgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3"));
        long id = BackgroundMusic.play(volume);
        BackgroundMusic.setLooping(id,true);
        loadMaze(0,40,800);
        startTimer();
    }

    private Array<Entity> createEntities(MazeData.LevelData level) {
        Array<Entity> result = new Array<>();
        for (EntityData entityData: level.getEntities()) {
            Texture texture = new Texture(entityData.getTexturePath());
            String entityType = entityData.getType();
            Entity entity = getEntity(entityData, entityType, texture);
            result.add(entity);
            //System.out.println("Spawned new entity of type "+entityData.getType()+" at location ("+
            //        entityData.getX()+","+entityData.getY()+") with texture "+entityData.getTexturePath());
        }
        return result;
    }
    private Array<TriggerZone> createTriggerZones(MazeData.LevelData level) {
        Array<TriggerZone> result = new Array<>();
        for (TriggerZone triggerZone: level.getTriggerZones()) {
            triggerZone.bounds = new Rectangle(triggerZone.x, triggerZone.y, triggerZone.width, triggerZone.height);
            result.add(triggerZone);
            //System.out.println("Added new triggerzone with target maze "+triggerZone.targetMaze);
        }
        return result;

    }

    private static Entity getEntity(EntityData entityData, String entityType, Texture texture) {
        Entity entity = null;
        switch (entityType) {
            case "Player" -> {
                    entity = new Player(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
                    player = (Player) entity;
            }
            case "Character" ->
                    entity = new io.github.OMAL_Maze.Entities.Character(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            case "Goose" -> entity = new Goose(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(),
                    texture);
            case "Seeds" -> entity = new Seeds(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(),
                    texture);
            default ->
                //Only other one is just Entity or should be cast to basic entity
                    entity = new Entity(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
        }
        return entity;
    }

    private Array<Building> createBuildings(MazeData.LevelData level) {
        //Add some stuff for walls innit
        Array<Building> result = new Array<>();
        for (BuildingData buildingData: level.getBuildings()) {
            Building building = new Building(buildingData.getX(), buildingData.getY(), buildingData.getWidth(),
                    buildingData.getHeight(), new Texture(buildingData.getTexturePath()));
            result.add(building);
        }
        int[][] walls = level.getWalls();
        for (int i=0;i<walls.length;i++) {
            for (int j=0;j<walls[i].length;j++) {
                if (walls[i][j]==1) {
                    int x = j*tileSize;
                    int y = (walls.length - 1 - i)*tileSize;
                    Building wall = new Building(x,y,tileSize,tileSize, new Texture("buildingTextures/wallMaybe.png"));
                    wall.setVisible(false);
                    result.add(wall);
                }
            }
        }
        return result;
    }

    private void startTimer() {
        Timer.Task myTimerTask = new Timer.Task() {
        Sound GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gameover.mp3"));
        boolean hasPlayed = false;
            @Override
            public void run() {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    int minutes = secondsRemaining / 60;
                    int seconds = secondsRemaining % 60;
                    timerText = String.format("Time: %02d:%02d", minutes, seconds);
                } else {
                    timerText = "Time: 00:00";
                    Building gameOverScreen = new Building(0,0,900,1000,new Texture("buildingTextures/GAME OVER.png"));
                    buildings.add(gameOverScreen);
                    //this.cancel();

                    //pauses the background music in order to play the game over sound 
                    if(!hasPlayed){
                        hasPlayed=true;
                        GameOverSound.play(volume);
                        BackgroundMusic.pause();
                    }
                }
            }
        };
        Timer.schedule(myTimerTask, 1f, 1f); // delays the timer speed by 1 second
    }
    /**
     * sets the hidden event counter to 0
     */
    public void decrementHiddenEventCounter(){
        hiddenEventsRemaining=0;//set it to 0 instead of -- since the hidden event only happens once
    }
    /**
     * sets the bad event counter to 0
     */
    public void decrementBadEventCounter(){
        badEventsRemaining=0;
    }
    /**
     * sets the good event counter to 0
     */
    public void decrementGoodEventCounter(){
        goodEventsRemaining=0;
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        for (int i=0;i<entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Character character) {
                character.movement(delta,entities,buildings);
            }
        }
    }

    private void logic() {
        triggerCooldown -= Gdx.graphics.getDeltaTime();
        if (triggerCooldown < 0) triggerCooldown = 0;

        Rectangle playerRect = player.sprite.getBoundingRectangle();

        if (triggerCooldown <= 0) {
            for (TriggerZone zone : triggerZones) {
                if (playerRect.overlaps(zone.bounds)) {
                    changeLevel(zone.targetMaze, zone.spawnPointX, zone.spawnPointY);
                    triggerCooldown = 1.0f;
                    break;
                }

            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
        for (int i=0;i<entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity==null) continue;
            render(entity);
        }
        float timerX = (float) tileSize /2;
        float timerY = worldHeight -((float) tileSize /2)+15;
        if (timerText!=null) {
            font.draw(batch, timerText, timerX, timerY);
        }
        //if seeds are collected then text is displayed
        if(player.hasSeeds) {
            if(secondsRemaining > 0){
                font.draw(batch, " Inventory: Seeds", timerX, timerY-15);
            }
        }
        for (Building building: buildings) {
            render(building);
        }
 //text displaying how many of each event remains
        font.draw(batch, "Events Remaining:", timerX +90, timerY);
        font.draw(batch, "Good:" + goodEventsRemaining, timerX + 220, timerY);  //give goose seed
        font.draw(batch, "Bad:" + badEventsRemaining, timerX + 300, timerY); //goose bites
        font.draw(batch, "Hidden:" + hiddenEventsRemaining, timerX + 380, timerY);//goose appears
        font.draw(batch, "Lives:" + player.hearts, timerX + 120, timerY-15);//lives remaining
        //all buttons are initially inactive, making one button active for testing purposes
        //pause.makeActive();
        //begin.makeActive();

        //for loop to go through all buttons to draw if needed
        for(AbstractButton b:buttons){
            //only draw if active
            if (b.isActive()){
                b.draw(batch);
                // System.out.println("active");
                if (b.isClicked(viewport)){
                    System.out.println("clicked");
                }
            }
        }
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        /*for (TriggerZone zone : triggerZones) {
            shapeRenderer.rect(zone.bounds.x, zone.bounds.y, zone.bounds.width, zone.bounds.height);
        }*/
        /*for (int i=0;i<entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Goose goose) {
                shapeRenderer.rect(
                  goose.spawnTrigger.x,
                  goose.spawnTrigger.y,
                  goose.spawnTrigger.width,
                  goose.spawnTrigger.height
                );
            }
        }*/
        shapeRenderer.end();


    }
    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }
    public void setSecondsRemaining(int nSecondsRemaining) {
        this.secondsRemaining=nSecondsRemaining;
    }
    private void render(Entity entity) {
        if (entity.getVisible()) {
            entity.render(batch);
        }
    }
    private void render(Building building) {
        if (building.getVisible()) {
            building.render(batch);
        }
    }
    private void changeLevel(int newMaze, int spawnPointX, int spawnPointY) {
        loadMaze(newMaze, spawnPointX, spawnPointY);
    }

    private void loadMaze(int maze, int spawnPointX, int spawnPointY) {
        //Clear all previous buildings, entities, and trigger zones
        //These will be null upon first use of the function (initialization)
        boolean seedCheck = false;
        if (buildings!=null) buildings.clear();
        if (triggerZones!=null) triggerZones.clear();
        if (entities!=null) {
            if (player.hasSeeds) seedCheck = true;
            entities.clear();
        }
        //Level int is 1 behind naming convention, add 1 when loading.
        MazeData.LevelData currentLevel = mazeData.getLevel("level_"+(maze+1));
        //Recreate all level
        backgroundTexture = new Texture(currentLevel.getBackgroundImage());

        entities = createEntities(currentLevel);
        buildings = createBuildings(currentLevel);
        triggerZones = createTriggerZones(currentLevel);
        player.sprite.setPosition(spawnPointX,spawnPointY);
        player.hasSeeds=seedCheck;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(909, 909, true); // true centers the camera
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
