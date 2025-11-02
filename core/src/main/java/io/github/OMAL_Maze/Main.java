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
    Movement movement;
    Array<Entity> entities;
    Array<Building> buildings;
    Array<TriggerZone> triggerZones;
    static Player player;
    private int tileSize;
    ShapeRenderer shapeRenderer; //for debugging, delete when necessary
    private float triggerCooldown = 0f;
    private static Main instance;
    private MazeData mazeData;

    //button experiment
    //Button button;
    BeginButton begin;
    QuitButton quit;
    CloseSettingsButton closeSettings;
    OpenSettingsButton openSettings;
    PauseButton pause;
    UnpauseButton unpause;

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
        movement = new Movement();
        font = new BitmapFont();
        mazeData = MazeLoader.loadMaze("loadAssets/assets.json");
        instance = this;
        shapeRenderer = new ShapeRenderer();
        //Background music plays the entire time
        BackgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3"));
        long id = BackgroundMusic.play();
        BackgroundMusic.setLooping(id,true);

        loadMaze(0,40,800);

        begin = new BeginButton(Gdx.files.internal("button.png"));
        quit = new QuitButton(Gdx.files.internal("button.png"));
        closeSettings = new CloseSettingsButton(Gdx.files.internal("button.png"));
        openSettings = new OpenSettingsButton(Gdx.files.internal("button.png"));
        pause = new PauseButton(Gdx.files.internal("button.png"));
        unpause = new UnpauseButton(Gdx.files.internal("button.png"));

        Collections.addAll(buttons, begin, quit, closeSettings, openSettings, pause, unpause);
        startTimer();
    }

    private Array<Entity> createEntities(MazeData.LevelData level) {
        Array<Entity> result = new Array<>();
        for (EntityData entityData: level.getEntities()) {
            Texture texture = new Texture(entityData.getTexturePath());
            String entityType = entityData.getType();
            Entity entity = getEntity(entityData, entityType, texture);
            result.add(entity);
            System.out.println("Spawned new entity of type "+entityData.getType()+" at location ("+
                    entityData.getX()+","+entityData.getY()+") with texture "+entityData.getTexturePath());
        }
        return result;
    }
    private Array<TriggerZone> createTriggerZones(MazeData.LevelData level) {
        Array<TriggerZone> result = new Array<>();
        for (TriggerZone triggerZone: level.getTriggerZones()) {
            triggerZone.bounds = new Rectangle(triggerZone.x, triggerZone.y, triggerZone.width, triggerZone.height);
            result.add(triggerZone);
            System.out.println("Added new triggerzone with target maze "+triggerZone.targetMaze);
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
                    texture, player);
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
                    Building gameOverScreen = new Building(0,0,900,1000,new Texture("buildingTextures/GAME OVER.png"));
                    buildings.add(gameOverScreen);
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
        for (Entity entity: entities) {
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
        pause.makeActive();
        //begin.makeActive();

        for(AbstractButton b:buttons){  //for loop works
            if (b.isActive()){
                b.draw(batch);
                // System.out.println("active");
                if (b.isClicked(viewport));
            }
        }
        //batch.draw(button,0,0,button.getWidth(),button.getHeight());
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (TriggerZone zone : triggerZones) {
            shapeRenderer.rect(zone.bounds.x, zone.bounds.y, zone.bounds.width, zone.bounds.height);
        }
        shapeRenderer.end();


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
