package io.github.OMAL_Maze;
import com.badlogic.gdx.utils.Timer;

import java.time.chrono.MinguoChronology;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
    private int worldWidth=880;
    private int worldHeight=880;
    private int tileSize;
    private static Main instance;

    //button experiment
    Button button;

    public static Main getInstance() {
        return instance;
    }
    @Override
    public void create() {
        buildings = new Array<>();
        batch = new SpriteBatch();
        viewport = new FitViewport(worldWidth, worldHeight);
        tileSize=worldWidth/20;
        backgroundTexture = new Texture("screenTextures/maze_grid.png");
        movement = new Movement();
        font = new BitmapFont();
        timerText = "Time: " + miniutesRemaining;
        startTimer();
        //Building fakeNisa = new Building(100,100,56,42,new Texture("buildingTextures/NiniLool.png"));
        //Building CS_Building = new Building(50,340,64,45,new Texture("buildingTextures/CS_Building.png"));
        //buildings.add(fakeNisa);
        //buildings.add(CS_Building);
        //entities.add(player);
        instance = this;

        //button experiments
        button = new Button(Gdx.files.internal("button.png"));
        MazeData mazeData = MazeLoader.loadMaze("loadAssets/assets.json");
        entities = createEntities(mazeData);
        buildings = createBuildings(mazeData);
    }

    private Array<Entity> createEntities(MazeData mazeData) {
        Array<Entity> result = new Array<>();
        for (EntityData entityData: mazeData.getEntities()) {
            Texture texture = new Texture(entityData.getTexturePath());
            //Entity entity = new Entity(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            String entityType = entityData.getType();
            Entity entity = null;
            if (entityType.equals("Player")) {
                entity = new Player(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            } else if (entityType.equals("Character")) {
                entity = new Character(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            } else if (entityType.equals("Item")) {
                //Item code needed. Deciding to add the class as seed possible
            } else if (entityType.equals("Goose")) {
                //Goose code needed. I do not have the class in this branch.
            } else if (entityType.equals("Seed")) {
                //Seed code also needed.
            } else {
                //Only other one is just Entity or should be cast to basic entity
                entity = new Entity(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
            }
            result.add(entity);
            System.out.println("Spawned new entity of type "+entityData.getType()+" at location ("+
                    entityData.getX()+","+entityData.getY()+") with texture "+entityData.getTexturePath());
        }
        return result;
    }
    private Array<Building> createBuildings(MazeData mazeData) {
        //Add some stuff for walls innit
        Array<Building> result = new Array<>();
        for (BuildingData buildingData: mazeData.getBuildings()) {
            Building building = new Building(buildingData.getX(), buildingData.getY(), buildingData.getWidth(),
                    buildingData.getHeight(), new Texture(buildingData.getTexturePath()));
            result.add(building);
        }
        int[][] walls = mazeData.getWalls();
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
            render(entity);
        }
        font.draw(batch, timerText,10, worldHeight - 10);
        for (Building building: buildings) {
            render(building);
        }
        batch.draw(button,0,0,button.getWidth(),button.getHeight());
        batch.end();

        if(button.isClicked()){
            System.out.println("Button clicked");
            //perform action when button is clicked
        }
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




