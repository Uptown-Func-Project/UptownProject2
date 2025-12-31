package io.github.OMAL_Maze.Dialogue;
import com.badlogic.gdx.utils.Timer;

public class DialogueManager {
    private Dialogue dialogue;
    private DialogueLine currentLine;
    private static DialogueManager instance;
    private DialogueUI ui;

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
            // Apply effect if any (you can implement effects here)
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
        // For example, if effect == "unlock_door", unlock the door
        System.out.println("Applying effect: " + effect);
    }
}
