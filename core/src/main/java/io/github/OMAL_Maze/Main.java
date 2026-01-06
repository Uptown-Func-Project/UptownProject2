package io.github.OMAL_Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.jar.Attributes.Name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
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
import com.badlogic.gdx.Input;

import io.github.OMAL_Maze.Buttons.AbstractButton;
import io.github.OMAL_Maze.Buttons.BeginButton;
import io.github.OMAL_Maze.Buttons.ReturnButton;
import io.github.OMAL_Maze.Buttons.LeaderboardButton;
import io.github.OMAL_Maze.Buttons.MuteButton;
import io.github.OMAL_Maze.Buttons.PauseButton;
import io.github.OMAL_Maze.Buttons.QuitButton;
import io.github.OMAL_Maze.Buttons.StartButton;
import io.github.OMAL_Maze.Buttons.UnpauseButton;
import io.github.OMAL_Maze.Entities.Character;
import io.github.OMAL_Maze.Entities.Entity;
import io.github.OMAL_Maze.Entities.EntityData;
import io.github.OMAL_Maze.Entities.Goose;
import io.github.OMAL_Maze.Entities.Player;
import io.github.OMAL_Maze.Entities.Seeds;
import io.github.OMAL_Maze.Map.BackgroundMusic;
import io.github.OMAL_Maze.Map.Building;
import io.github.OMAL_Maze.Map.BuildingData;
import io.github.OMAL_Maze.Map.MazeData;
import io.github.OMAL_Maze.Map.MazeLoader;
import io.github.OMAL_Maze.Map.TriggerZone;

/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public float volume = 100f;
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
    public static Array<Building> buildings;
    Array<TriggerZone> triggerZones;
    public static Player player;
    public int tileSize;
    ShapeRenderer shapeRenderer; //for debugging, delete when necessary
    private float triggerCooldown = 0f;
    private static Main instance;
    private MazeData mazeData;
    public ArrayList<LeaderboardScore> scores = new ArrayList<>(); 
    public boolean enteringName = false;
    public boolean hasAddedScore = false;
    public String playerName = "";



    
    BeginButton begin;
    QuitButton quit;
    PauseButton pause;
    UnpauseButton unpause;
    MuteButton mute;
    LeaderboardButton leaderboard;
    ReturnButton returnbutton;
    //add in new button here!!!!!
    StartButton start;
    Screen GameOverScreen;
    Screen TitleScreen;
    Screen CongratsScreen; //will use the same quit and start button as game over screen
    Screen PauseScreen;
    Screen LeaderboardScreen;
    Screen AchievementScreen;
    boolean secondsDecreasing = false;
    NameInputUI nameInputUI;
    AchievementTracker achievementTracker;
    //storing all buttons in an arraylist so they can be iterated through
    ArrayList<AbstractButton> buttons = new ArrayList<>(6);


    //Sounds
    Sound backgroundSound;
    BackgroundMusic backgroundMusic;

    /**
     * Main class called in the lwjgl launcher.
     */
    public Main() {
        instance = this;
    }

    /**
     * Getter method so that the current instance can be interacted with.
     * @return Current game instance including all variable states.
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Creates the sprite batch, buttons, screens and other needed variables.
     */
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
        backgroundSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Background.mp3"));
        backgroundMusic = new BackgroundMusic(backgroundSound);

        //Background music plays the entire time
        //Debugging line below, Used to spawn at start of second level.
        //loadMaze(1, 40, 80);
        //the images of the buttons can be changed here
        begin = new BeginButton(Gdx.files.internal("buttonTextures/startNew.png"));
        quit = new QuitButton(Gdx.files.internal("buttonTextures/quitNew.png"));
        pause = new PauseButton(Gdx.files.internal("buttonTextures/greenbutton.png"));
        unpause = new UnpauseButton(Gdx.files.internal("buttonTextures/resumebutton.png"));
        mute = new MuteButton(Gdx.files.internal("buttonTextures/greenbutton.png"));
        start = new StartButton(Gdx.files.internal("buttonTextures/startNew.png"));
        leaderboard = new LeaderboardButton(Gdx.files.internal("buttonTextures/leaderboard.png"));
        returnbutton = new ReturnButton(Gdx.files.internal("buttonTextures/returnbutton.png"));
        //adding all buttons to the arraylist in one go
        Collections.addAll(buttons, begin, quit, pause, unpause, mute, start);
        startTimer();
        GameOverScreen = new Screen(batch, viewport, "screenTextures/GAME OVER.png");
        TitleScreen = new Screen (batch, viewport, "screenTextures/Title screen.png");
        CongratsScreen = new Screen(batch, viewport, "screenTextures/Congratulations.png");
        PauseScreen = new Screen(batch, viewport, "screenTextures/pausescreen.png");
        LeaderboardScreen = new Screen(batch, viewport, "screenTextures/blankbackground.png");
        AchievementScreen = new Screen(batch, viewport, "screenTextures/blankbackground.png");
        achievementTracker = new AchievementTracker();
        TitleScreen.setActive(true);
    }

    /**
     * Spawns the entities from the maze data object for each level.
     * @param level The LevelData object for each level
     * @return The array of entities in the current map
     */
    private Array<Entity> createEntities(MazeData.LevelData level) {
        Array<Entity> result = new Array<>();
        for (EntityData entityData: level.getEntities()) {
            Texture texture = new Texture(Gdx.files.internal(entityData.getTexturePath()));
            String entityType = entityData.getType();
            Entity entity = getEntity(entityData, entityType, texture);
            result.add(entity);
        }
        return result;
    }

    /**
     * Creates the trigger zones to change level based on the map data.
     * @param level Contains the level data.
     * @return Array of trigger zone objects.
     */
    private Array<TriggerZone> createTriggerZones(MazeData.LevelData level) {
        Array<TriggerZone> result = new Array<>();
        for (TriggerZone triggerZone: level.getTriggerZones()) {
            triggerZone.bounds = new Rectangle(triggerZone.x, triggerZone.y, triggerZone.width, triggerZone.height);
            result.add(triggerZone);
        }
        return result;

    }

    /**
     * Creates an entity of a specific type using the level data.
     * @param entityData Location, dimensions, and texture for the entity.
     * @param entityType String value with the class name.
     * @param texture Texture object for the entity.
     * @return The entity object with its own subclass.
     */
    private static Entity getEntity(EntityData entityData, String entityType, Texture texture) {
        Entity entity;
        switch (entityType) {
            case "Player" -> {
                    entity = new Player(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
                    player = (Player) entity;
            }
            case "Character" ->
                    entity = new Character(entityData.getX(), entityData.getY(), entityData.getWidth(), entityData.getHeight(), texture);
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

    /**
     * Spawns the buildings and walls from the maze data object for each level.
     * @param level The LevelData object for each level
     * @return The array of buildings/walls in the current map
     */
    private Array<Building> createBuildings(MazeData.LevelData level) {
        Array<Building> result = new Array<>();
        for (BuildingData buildingData: level.getBuildings()) {
            Building building = new Building(buildingData.getX(), buildingData.getY(), buildingData.getWidth(),
                    buildingData.getHeight(), new Texture(Gdx.files.internal(buildingData.getTexturePath())));
            result.add(building);
        }
        int[][] walls = level.getWalls();
        for (int i=0;i<walls.length;i++) {
            for (int j=0;j<walls[i].length;j++) {
                if (walls[i][j]==1) {
                    int x = j*tileSize;
                    int y = (walls.length - 1 - i)*tileSize;
                    Building wall = new Building(x,y,tileSize,tileSize, new Texture(Gdx.files.internal("buildingTextures/wallMaybe.png")));
                    wall.setVisible(false);
                    result.add(wall);
                }
            }
        }
        return result;
    }
    /**
     * startTimer method is responsible for the in-built game timer functionality
     */
    private void startTimer() {
        //Start looping background music. Looping done in class.
        Timer.Task myTimerTask = new Timer.Task() {
            final Sound GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gameover.mp3"));
            boolean hasPlayed = false;
            @Override
            public void run() {
                if (secondsRemaining > 0) {
                    if (secondsDecreasing) secondsRemaining--;
                    int minutes = secondsRemaining / 60;
                    int seconds = secondsRemaining % 60;
                    // formats the time into min:sec
                    timerText = String.format("Time: %02d:%02d", minutes, seconds);
                } else {
                    timerText = "Time: 00:00";
                    // once timer hits zero Game Over screen is displayed
                    GameOverScreen.setActive(true);
                    backgroundMusic.stop();
                    // pauses the background music in order to play the game over sound
                    if(!hasPlayed) {
                        hasPlayed=true;
                        GameOverSound.play(volume);
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

    /**
     * Setter for the game volume.
     * @param nVolume float: The new volume value
     */

    public void setVolume(float nVolume) {
        this.volume = nVolume;
    }

    /**
     * Renders the respective screen.
     */
    @Override
    public void render() {
        if (TitleScreen.getActive()){
            TitleScreenLogic();
        }
        else if(PauseScreen.getActive()){
            PauseScreenLogic();
        }
        else if(CongratsScreen.getActive()){
            CongratsScreenLogic();
        }
        else if (GameOverScreen.getActive()){
            GameOverScreenLogic();
        }
        else if (LeaderboardScreen.getActive()){
            LeaderboardScreenLogic();
        }
        else if (AchievementScreen.getActive()){
            achievementScreenLogic();
        }
        else {
            input();
            logic();
            draw();
        }
    }

    /**
     * Ends the game.
     * Needed for when the goose bites the player too many times.
     */
    public void gameOver() {
        final Sound GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gameover.mp3"));
        GameOverScreen.setActive(true);
        backgroundMusic.stop();
        GameOverSound.play(volume);
    }

    /**
     * Handles user input by calling entity movement functions.
     */
    private void input() {
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        for (int i=0;i<entities.size;i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Character character) {
                character.movement(delta,entities,buildings);
            }
        }
    }

    /**
     * General logic for the game. Used to check for game level trigger zones.
     */
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

    /**
     * Sets background to black initially before rendering all text, buttons, entities, and buildings.
     */
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
        //Specific timer location
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
        font.draw(batch, "Bad:" + badEventsRemaining, timerX + 300, timerY);    //goose bites
        font.draw(batch, "Hidden:" + hiddenEventsRemaining, timerX + 380, timerY);  //goose appears
        font.draw(batch, "Lives:" + player.hearts, timerX + 120, timerY-15);    //lives remaining


        //making buttons active on the gameplay screen
        pause.setActive(true);
        mute.setActive(true);

        //for loop to go through all buttons to draw if needed
        for(AbstractButton b:buttons){
            //only draw if active
            if (b.isActive()){
                b.draw(batch);
                //Empty statement. Calls the isClicked function for functionality.
                if (b.isClicked(viewport)){
                    if (b==mute) {
                        backgroundMusic.changeVolume(volume);
                    } else if (b==pause) {
                        PauseScreen.setActive(true);
                        backgroundMusic.pause();
                        secondsDecreasing=false;
                    }
                }
            }
        }


        //Optional code to render the trigger zones. Commented out unless needed for debugging
        /*shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (TriggerZone zone : triggerZones) {
            shapeRenderer.rect(zone.bounds.x, zone.bounds.y, zone.bounds.width, zone.bounds.height);
        }*/
        //Uses the same renderer to show the goose spawn zone
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
        }
        shapeRenderer.end();*/

        //Draws the text for the mute button and the pause button
        font.draw(batch, mute.getMutedStr(), 770, 870);
        font.draw(batch, "Pause", 650, 870);
        batch.end();
    }

    /**
     * Getter method to get the number of seconds left
     * @return the current value of the timer "secondsRemaining"
     */
    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }

    /**
     * Sets the timer to a new value. Used when the goose rewards the player for grabbing the seeds.
     * @param nSecondsRemaining the new value for the timer.
     */
    public void setSecondsRemaining(int nSecondsRemaining) {
        this.secondsRemaining=nSecondsRemaining;
    }

    /**
     * Renders an entity if its visible attribute is set to true
     * @param entity entity object to render
     */
    private void render(Entity entity) {
        if (entity.getVisible()) {
            entity.render(batch);
        }
    }

    /**
     * Renders a building if its visible attribute is set to true
     * @param building building object to render
     */
    private void render(Building building) {
        if (building.getVisible()) {
            building.render(batch);
        }
    }

    /**
     * Changes the level, called from the trigger boxes.
     * Has a single case for ending the game/winning.
     * @param newMaze index of the new maze.
     * @param spawnPointX horizontal location of the player to spawn at.
     * @param spawnPointY vertical location of the player to spawn at.
     */
    private void changeLevel(int newMaze, int spawnPointX, int spawnPointY) {
        //Specific implementation for winning, rather than making a redundant win hitbox
        if (newMaze==10) { // TODO will need to make this higher - freddie
            secondsDecreasing=false;
            CongratsScreen.setActive(true);
        } else {
            loadMaze(newMaze, spawnPointX, spawnPointY);
        }
    }

    /**
     * Loads the maze based on its index and loads the assets for that maze.
     * @param maze index of the maze (0-3 in the case of V1).
     * @param spawnPointX horizontal location of the player to spawn at.
     * @param spawnPointY vertical location of the player to spawn at.
     */
    private void loadMaze(int maze, int spawnPointX, int spawnPointY) {
        //Clear all previous buildings, entities, and trigger zones
        //These will be null upon first use of the function (initialization)
        boolean seedCheck = false;
        int currenthearts=3;
        float speed = 150f;
        if (buildings!=null) buildings.clear();
        if (triggerZones!=null) triggerZones.clear();
        if (entities!=null) {
            if (player.hasSeeds) seedCheck = true;
            currenthearts=player.getHearts();
            speed=player.speed;
            if (maze==0) {
                currenthearts=3;
            }
            entities.clear();
        }
        //Level int is 1 behind naming convention, add 1 when loading.
        MazeData.LevelData currentLevel = mazeData.getLevel("level_"+(maze+1));
        //Recreate the level background texture
        backgroundTexture = new Texture(Gdx.files.internal(currentLevel.getBackgroundImage()));

        //Spawn the entities and buildings (Walls usually)
        entities = createEntities(currentLevel);
        buildings = createBuildings(currentLevel);
        triggerZones = createTriggerZones(currentLevel);
        //Set start values for the player
        player.sprite.setPosition(spawnPointX,spawnPointY);
        player.hasSeeds=seedCheck;
        player.hearts=currenthearts;
        player.speed=speed;
    }

    /**
     * Resizes the screen viewport. Used by standard libgdx, not needed to change.
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(909, 909, true); // true centers the camera
    }

    /**
     * Disposes batches and fonts when the application is destroyed.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }

    /**
     * Starts the game by loading the map assets and setting some variables back to their initial values.
     */
    public void startGame(){
        //Goes to first maze and resets character and seeds
        loadMaze(0,40,800);

        //Set timer back to 5 minutes.
        secondsRemaining = 300;  //resets the time
        secondsDecreasing=true;
        //Remove the Game Over screen if it exists.
        GameOverScreen.setActive(false);
        //Starts the background music, uses the object for this
        backgroundMusic.stop();
        backgroundMusic.start(volume);
        //Reset values for the events.
        this.badEventsRemaining = 1;
        this.goodEventsRemaining = 1;
        this.hiddenEventsRemaining = 1;
        //draw(); //this continues to show the game over screen
        enteringName = false;
        hasAddedScore = false;
    }

    /**
     * Drawing the title screen and controlling what happens when the button is clicked.
     */
    public void TitleScreenLogic(){
        secondsDecreasing = false;
        batch.begin();
        TitleScreen.render();
        start.setActive(true);
        mute.setActive(true);
        leaderboard.setActive(true);
        start.draw(batch);
        leaderboard.draw(batch);
        batch.end();
        if (start.isClicked(viewport)){
            TitleScreen.setActive(false);
            start.setActive(false);
            leaderboard.setActive(false);
            pause.setActive(true);
            mute.setActive(true);
            startGame();
        } else if (leaderboard.isClicked(viewport)){
            //code to show leaderboard goes here
            TitleScreen.setActive(false);
            start.setActive(false);
            leaderboard.setActive(false);
            LeaderboardScreen.setActive(true);
        }
    }

    public void LeaderboardScreenLogic(){
        //code to show leaderboard goes here
        batch.begin();
        secondsDecreasing = false;
        LeaderboardScreen.render();
        font.getData().setScale(4);
        font.draw(batch, "Leaderboard", 275, 800);
        font.getData().setScale(1);
        int yPosition = 700;
        int maxRows = 5;
        for (int i = 0; i < maxRows; i++) {
            String scoreText;
            if (i < scores.size()) {
                LeaderboardScore score = scores.get(i);
                scoreText = (i + 1) + ". " + score.playerName + " - " + score.score + " seconds";
            } else {
                scoreText = (i + 1) + ". ---";
            }
            font.draw(batch, scoreText, 300, yPosition);
            yPosition -= 50; // Move down for the next score
        }

        returnbutton.setActive(true);
        returnbutton.draw(batch);
        batch.end();
        if (returnbutton.isClicked(viewport)){
            LeaderboardScreen.setActive(false);
            returnbutton.setActive(false);
            TitleScreen.setActive(true);
        }
    }

    /**
     * Renders the pause screen and causes the buttons to function.
     */
    public void PauseScreenLogic(){
        batch.begin();
        secondsDecreasing = false;
        PauseScreen.render();
        unpause.setActive(true);
        quit.setActive(true);
        unpause.draw(batch);
        quit.draw(batch);
        batch.end();
        if (quit.isClicked(viewport)){
            Gdx.app.exit();
        }
        if (unpause.isClicked(viewport)){
            PauseScreen.setActive(false);
            secondsDecreasing=true;
            backgroundMusic.resume();
            unpause.setActive(false);
            quit.setActive(false);
            pause.setActive(true);
            mute.setActive(true);
            //need to add in the logic of restarting the game
        }
    }
    /**
     * Renders the congratulations screen and causes the buttons to function.
     */
    public void CongratsScreenLogic(){

        if (nameInputUI == null) {
            nameInputUI = new NameInputUI(viewport);
            nameInputUI.show();
            hasAddedScore = false;
        }

        batch.begin();
        CongratsScreen.render();
        //increasing font size
        font.getData().setScale(5);
        font.draw(batch, String.valueOf(secondsRemaining), 520, 500);
        font.getData().setScale(1);
        
        if (hasAddedScore) {
            begin.setActive(true);
            quit.setActive(true);
            begin.draw(batch);
            quit.draw(batch);
        }
    
        batch.end();
        nameInputUI.update(Gdx.graphics.getDeltaTime());
        nameInputUI.draw();
        if (nameInputUI.isSubmitted() && !hasAddedScore) {
            String playerName = nameInputUI.getName();
            scores.add(new LeaderboardScore(playerName, secondsRemaining));
            scores.sort((a, b) -> b.score - a.score); // Sort scores in descending order
            if (scores.size() > 5) {
                scores = new ArrayList<>(scores.subList(0, 5)); // Keep only top 10 scores
            }
            hasAddedScore = true;
        }


        pause.setActive(false);
        mute.setActive(false);

        if (hasAddedScore) {
            if (quit.isClicked(viewport)){
                Gdx.app.exit();
            }
            if (begin.isClicked(viewport)){
                begin.setActive(false);
                quit.setActive(false);
                CongratsScreen.setActive(false);
                // Reset the game state and return to the title screen instead of
                // immediately starting a new game.
                if (nameInputUI != null) {
                nameInputUI.dispose();
                nameInputUI = null; // Reset the name input UI for the next game
            }

            resetToTitle();
            TitleScreen.setActive(true);
            }
        }
       
    }

    /**
     * Reset game state back to the initial title-screen state without
     * immediately starting gameplay.
     */
    public void resetToTitle(){
        // Reset timer and pause timer progression until player starts a game
        secondsRemaining = 300;
        secondsDecreasing = false;

        // Stop any background music
        if (backgroundMusic != null) backgroundMusic.stop();

        // Deactivate other screens that might be visible
        GameOverScreen.setActive(false);
        PauseScreen.setActive(false);
        LeaderboardScreen.setActive(false);
        CongratsScreen.setActive(false);

        // Ensure title screen buttons are in the correct state
        start.setActive(false);
        pause.setActive(false);
        mute.setActive(false);
        begin.setActive(false);
        quit.setActive(false);

        // Reset event counters to defaults
        this.badEventsRemaining = 1;
        this.goodEventsRemaining = 1;
        this.hiddenEventsRemaining = 1;

        // Clear gameplay objects so the title screen is clean. They'll be
        // recreated when a new game is started via `startGame()`.
        if (entities != null) entities.clear();
        if (buildings != null) buildings.clear();
        if (triggerZones != null) triggerZones.clear();

        if (nameInputUI != null) {
            nameInputUI.dispose();
            nameInputUI = null; // Reset the name input UI for the next game
        }
    }
    /**
     * Renders the game over screen and causes the buttons to function.
     */
    public void GameOverScreenLogic(){
        batch.begin();
        GameOverScreen.render(); //need to stop displaying the map
        //displaying the correct buttons on game over screen
        begin.setActive(true);
        quit.setActive(true);
        begin.draw(batch);
        quit.draw(batch);
        batch.end();
        pause.setActive(false);
        mute.setActive(false);
        if (quit.isClicked(viewport)){
            Gdx.app.exit();
        }
        if (begin.isClicked(viewport)){
            begin.setActive(false);
            quit.setActive(false);
            GameOverScreen.setActive(false);
            startGame();
        }
    }

    public void achievementScreenLogic(){
        batch.begin();
        achievementScreenLogic();

        font.getData().setScale(4);
        font.draw(batch, "Achievements", 275, 800);
        font.getData().setScale(1);

        int y = 700;

        for (Achievement achievement : achievementTracker.getAchievements()) {
            if (achievement.unlocked) {
                font.setColor(Color.GREEN);
                font.draw(batch, achievement.name + " - " + achievement.description, 300, y);
            } else {
                font.setColor(Color.RED);
                font.draw(batch, achievement.name + " - " + achievement.description, 300, y);
            }
            y -= 50;
        }

        returnbutton.setActive(true);
        returnbutton.draw(batch);
        batch.end();
        if (returnbutton.isClicked(viewport)){
            AchievementScreen.setActive(false);
            returnbutton.setActive(false);
            TitleScreen.setActive(true);
        }
    }
}
