package io.github.OMAL_Maze;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private int secondsRemaining = 5;
    private SpriteBatch batch;
    private BitmapFont font;
    private String timerText;
    FitViewport viewport;
    Texture backgroundTexture;
    Array<Entity> entities;
    Array<Building> buildings;
    Array<TriggerZone> triggerZones;
    static Player player;
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
    MuteButton mute;
    //add in new button here!!!!!
    StartButtonT startT;
    Screen GameOverScreen;
    Screen TitleScreen;
    Screen CongratsScreen; //will use the same quit and start button as game over screen
    //storing all buttons in an arraylist so they can be iterated through
    ArrayList<AbstractButton> buttons = new ArrayList<>(8);

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
        //font.scale(10);
        mazeData = MazeLoader.loadMaze("loadAssets/assets.json");
        instance = this;
        shapeRenderer = new ShapeRenderer();
        //Background music plays the entire time
        BackgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3"));
        long id = BackgroundMusic.play();
        BackgroundMusic.setLooping(id,true);

        loadMaze(0,40,800);
        //Debugging line below, Used to spawn at start of second level.
        //loadMaze(1, 40, 80);
        //the images of the buttons can be changed here
        begin = new BeginButton(Gdx.files.internal("startNew.png"));
        quit = new QuitButton(Gdx.files.internal("quitNew.png"));
        closeSettings = new CloseSettingsButton(Gdx.files.internal("button.png"));
        openSettings = new OpenSettingsButton(Gdx.files.internal("button.png"));
        pause = new PauseButton(Gdx.files.internal("button.png"));
        unpause = new UnpauseButton(Gdx.files.internal("button.png"));
        mute = new MuteButton(Gdx.files.internal("button.png"));
        startT = new StartButtonT(Gdx.files.internal("startNew.png"));
        //adding all buttons to the arraylist in one go
        Collections.addAll(buttons, begin, quit, closeSettings, openSettings, pause, unpause, mute, startT);
        startTimer();
        GameOverScreen = new Screen(batch, viewport, "GAME OVER.png");
        TitleScreen = new Screen (batch, viewport, "Title screen.png");
        CongratsScreen = new Screen(batch, viewport, "Congratulations.png");
        TitleScreen.setActive(true);
        //CongratsScreen.setActive(true); //CHANGE THIS BACK

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
                    entity = new Character(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            case "Item" -> {
                //Item code needed. Deciding to add the class as seed possible
            }
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
            @Override
            public void run() {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    int minutes = secondsRemaining / 60;
                    int seconds = secondsRemaining % 60;
                    timerText = String.format("Time: %02d:%02d", minutes, seconds);
                } else {
                    timerText = "Time: 00:00";
                    //Building gameOverScreen = new Building(0,0,900,1000,new Texture("buildingTextures/GAME OVER.png"));
                    //buildings.add(gameOverScreen);
                        GameOverScreen.setActive(true);
//                    GameOverScreen.render(); //need to stop displaying the map
//                    //displaying the correct buttons on game over screen
//                    begin.makeActive();
//                    quit.makeActive();
//                    openSettings.makeActive();
                    //this.cancel();

                    Sound GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gameover.mp3"));
                    GameOverSound.play();

                }
            }
        };
        Timer.schedule(myTimerTask, 1f, 1f); // dealys the timer speed by 1 second
    }

    @Override
    public void render() {
        //System.out.println("render method");
        if (TitleScreen.getActive()){
            TitleScreenLogic();
        }
        else {
            input();
            logic();
            draw();
        }
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
        batch.end();

        //making buttons active on the gameplay screen
        pause.makeActive();
        mute.makeActive();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        /*for (TriggerZone zone : triggerZones) {
            shapeRenderer.rect(zone.bounds.x, zone.bounds.y, zone.bounds.width, zone.bounds.height);
        }*/
        for (int i=0;i<entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Goose goose) {
                shapeRenderer.rect(
                  goose.spawnTrigger.x,
                  goose.spawnTrigger.y,
                  goose.spawnTrigger.width,
                  goose.spawnTrigger.height
                );
            }
        }
        shapeRenderer.end();

        if (GameOverScreen.getActive()){
            GameOverScreen.render(); //need to stop displaying the map
            //displaying the correct buttons on game over screen
            begin.makeActive();
            quit.makeActive();
            pause.makeInactive();
            mute.makeInactive();
            if (begin.isClicked(viewport)){
                System.out.println("begin clicked and new maze loaded");
                begin.makeInactive();
                quit.makeInactive();
                GameOverScreen.setActive(false);
                // loadMaze(1,40, 800);
                startGame();
            }
        }
        if (CongratsScreen.getActive()){
            CongratsScreen.render();
            batch.begin();
            //increasing font size
            font.getData().setScale(5);
            font.draw(batch, String.valueOf(secondsRemaining), 520, 500);
            //returning font size to original
            font.getData().setScale(1);
            batch.end();
            begin.makeActive();
            quit.makeActive();
            pause.makeInactive();
            mute.makeInactive();
            if (begin.isClicked(viewport)){
                System.out.println("begin clicked and new maze loaded");
                begin.makeInactive();
                quit.makeInactive();
                CongratsScreen.setActive(false);
                // loadMaze(1,40, 800);
                startGame();
            }
        }

        batch.begin();
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

    public void startGame(){
        //goes to first maze and resets character and seeds
        loadMaze(0,40,800);

        //startTimer();  //this meant it was in double time
        secondsRemaining = 300;  //resets the time
        GameOverScreen.setActive(false);
        //draw(); //this continues to show the game over screen
    }

    /**
     * Drawing the title screen and controlling what happens when the button is clicked.
     */
    public void TitleScreenLogic(){
        TitleScreen.render();
        startT.makeActive();
        batch.begin();
        startT.draw(batch);
        batch.end();
        if (startT.isClicked(viewport)){
            TitleScreen.setActive(false);
            startT.makeInactive();
            pause.makeActive();
            mute.makeActive();
            startGame();
        }
    }
}
