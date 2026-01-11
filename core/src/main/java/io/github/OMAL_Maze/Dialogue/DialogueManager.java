package io.github.OMAL_Maze.Dialogue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Entities.Player;
/**
 * Manages dialogue flow, acting as the logic for dialogue
 * Handles loading from the JSON file, displaying lines, processing player choices and applying effects
 * uses singleton pattern for global access
 */
public class DialogueManager {
    private Dialogue dialogue;  // loaded dialogue data containing all dialogue lines
    private DialogueLine currentLine;  // line being displayed
    private static DialogueManager instance; // singleton instance
    private DialogueUI ui;    // UI to render dialogue on screen
    static Sound itemPickup; //sound effect played when picking up item

    /**
     * Constructor for DialogueManager and sets it as the singleton instance.
     * @param ui UI to render dialogue
     */
    public DialogueManager(DialogueUI ui) {
        this.ui = ui;
        ui.setChoiceListener(this::selectChoice);
        instance = this;
    }
    /**
     * loads dialogue from JSON file
     * 
     * @param path path to JSON file
     */
    public void loadDialogue(String path) {
        dialogue = Dialogue.fromJson(path);
    }

    /**
     * Starts dialogue sequence from a specific dialogue line.
     * Shows current line. If that line has no player choices and nextID isnt null,
     * it advances to the next Id listed.
     * 
     * @param startId Id of dialogue line to start with
     */
    public void startDialogue(String startId) {
        currentLine = dialogue.get(startId);
        if (currentLine != null) {
            ui.show(currentLine);
            if (currentLine.choices.isEmpty() && currentLine.nextId != null) {  
                // if no choice, automatically next dialogue after 2s
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        advanceTo(currentLine.nextId);
                    }
                }, 2.0f);
            }

        }
    }

    /**
     * Handles player selection of dialogue choice
     * Applies effects based on the choice then advances to the next dialogue line.
     * @param index index of the selected choice in the current line's choice list

     */
    public void selectChoice(int index) {
        if (currentLine != null && currentLine.choices != null && index >= 0 && index < currentLine.choices.size()) {
            DialogueLine.Choice choice = currentLine.choices.get(index);
            // can apply effects to dialogue here
            if (choice.effect != null) {
                applyEffect(choice.effect);
            }
            advanceTo(choice.nextId);
        }
    }

    /**
     * moves to the next dialogue based on the nextId ID.
     * Ends dialogue if ID is null or if line has no choices and no nextId
     * 
     * @param id ID of dialogue line to advance to, null to end dialogue.
     */
    private void advanceTo(String id) {
        if (id == null) {
            endDialogue();
            return;
        }   
        currentLine = dialogue.get(id);
        if (currentLine != null) {
            ui.show(currentLine);
            if (currentLine.choices.isEmpty() && currentLine.nextId != null) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        advanceTo(currentLine.nextId);
                    }
                }, 2.0f);  // 2-second delay before advancing
            } else if (currentLine.choices.isEmpty() && currentLine.nextId == null) {
                endDialogue();
            }
        } else {
            endDialogue();
        }
    }
    
    /**
     * ends dialogue and hides UI and clears currentLine.
     */
    public void endDialogue() {
        ui.hide();
        currentLine = null;
    }

    /**
     * check if dialogue is currently active
     * 
     * @return true if dialogue line is being displayed
     */
    public boolean isDialogueActive() {
        return currentLine != null;
    }

    /**
     * gets current active dialogue line
     * 
     * @return current DialogueLine
     */
    public DialogueLine getCurrentLine() {
        return currentLine;
    }

    /**
     * gets singleton instance
     * 
     * @return DialogueManager instance
     */
    public static DialogueManager getInstance() {
        return instance;
    }

    /**
     * Applies game effect triggered by dialogue choice.
     * Effects typically modify variables for the player.
     * 
     * @param effect the effect id string
     */
    private void applyEffect(String effect) {
        if (effect == null) {
            return;
        }
        Player player = Main.getInstance().player;
        itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));

        switch (effect) {
            case "all_correct":
                player.degreeState = 2; //player answered everything correct
                break;
            case "take_bat":  // gives player bat
                player.hasBat = true;
                itemPickup.play();
                break;
            case "take_coins":  // adds 5 coins to player inventory
                player.coins += 5;
                itemPickup.play();
                break;
            case "got_degree":  // gives player degree
                player.hasDegree = true;
                itemPickup.play();
                break;
        }
    }
}
