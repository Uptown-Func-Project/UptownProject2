package io.github.OMAL_Maze.Dialogue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import io.github.OMAL_Maze.Main;
import io.github.OMAL_Maze.Entities.Player;

public class DialogueManager {
    private Dialogue dialogue;
    private DialogueLine currentLine;
    private static DialogueManager instance;
    private DialogueUI ui;
    static Sound itemPickup;

    public DialogueManager(DialogueUI ui) {
        this.ui = ui;
        ui.setChoiceListener(this::selectChoice);
        instance = this;
    }

    public void loadDialogue(String path) {
        dialogue = Dialogue.fromJson(path);
    }

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

    public void endDialogue() {
        ui.hide();
        currentLine = null;
    }

    public boolean isDialogueActive() {
        return currentLine != null;
    }

    public DialogueLine getCurrentLine() {
        return currentLine;
    }

    public static DialogueManager getInstance() {
        return instance;
    }

    private void applyEffect(String effect) {
        // Implement effects based on your game logic
        // Used to keep track on the type of degree the player will get
        if (effect == null) {
            return;
        }
        Player player = Main.getInstance().player;
        itemPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/ItemPickup.mp3"));

        switch (effect) {
            case "all_correct":
                player.degreeState = 2; //player answered everything correct
                // System.out.println("degreeState set to 2");
                break;
            case "take_bat":  // code to spawn bat
                player.degreeState = 1;
                break;
            case "take_coins":  // code to spawn coins
                player.degreeState = 1;
                break;
            case "got_degree":
                player.hasDegree = true;
                itemPickup.play();
                // System.out.println("hasDegree set to true");
                break;
            case "dean_spawn":
                // Can spawn dean after dialogue.
                break;
        }
    }
}
