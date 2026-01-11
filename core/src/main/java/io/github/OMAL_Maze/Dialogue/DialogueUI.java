package io.github.OMAL_Maze.Dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;
/**
 * Handles the UI of dialogue using Scene2D library
 * Displays speaker names, dialogue text, and interactive choice buttons.
 * Positioned at the bottom of the screen with a fixed width layout.
 */
public class DialogueUI {
    private Stage stage;  // stage for rendering UI components
    private Label speakerLabel; // label for the character speaking
    private Label textLabel; // label for the actual text of dialogue
    private Table root;  // root table for oganizing UI layout
    private List<TextButton> choiceButtons;  // list of buttons for players to choose
    private ChoiceListener choiceListener;   // Callback listener for handling choice selection events
    private BitmapFont font;  // font used for text

    /**
     * Interface for receiving notifications when a player selects a dialogue choice.
     */
    public interface ChoiceListener {
        void onChoiceSelected(int index);
    }

    /**
     * DialogueUI constructor, specifies layout and fonts of text.
     * speaker label = yellow, dialogue text (white, wrapped)
     * UI positioned at the bottom of the screen with some padding
     *
     * @param viewport viewport for the UI stage
     * @param batch sprite batch for rendering
     */
    public DialogueUI(FitViewport viewport, SpriteBatch batch) {
        stage = new Stage(viewport, batch);
        font = new BitmapFont();
        font.getData().setScale(1.5f);

        // Speaker style
        Label.LabelStyle speakerStyle = new Label.LabelStyle(font, Color.YELLOW);
        speakerLabel = new Label("", speakerStyle);
        
        // Text style
        Label.LabelStyle textStyle = new Label.LabelStyle(font, Color.WHITE);
        textLabel = new Label("", textStyle);
        textLabel.setWrap(true);
        
        root = new Table();
        
        // Position and pad the table
        root.center().bottom().pad(10).padLeft(900);
        
        root.add(speakerLabel).left().row();
        root.add(textLabel).width(800).pad(5).row();
        
        stage.addActor(root);
        choiceButtons = new ArrayList<>();
    }

    /**
     * listener notified when a dialogue choice is selected.
     * 
     * @param listener The ChoiceListener to handle choice selection events
     */
    public void setChoiceListener(ChoiceListener listener) {
        this.choiceListener = listener;
    }

    /**
     * Displays dialogue line 
     * speaker, text, and choice buttons shown.
     * Clears any previously displayed choices and creates new buttons for each choice.
     * Sets this UI as the active input processor to handle button clicks.
     * 
     * @param line DialogueLine to display, or null to skip display
     */

    public void show(DialogueLine line) {
        if (line == null) return;
        speakerLabel.setText(line.speaker != null ? line.speaker : "");
        textLabel.setText(line.text != null ? line.text : "");

        // Clear previous choice buttons
        for (TextButton btn : choiceButtons) {
            btn.remove();
        }
        choiceButtons.clear();

        if (line.choices != null && !line.choices.isEmpty()) {
            for (int i = 0; i < line.choices.size(); i++) {
                TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
                buttonStyle.font = font;
                TextButton btn = new TextButton(line.choices.get(i).text, buttonStyle);
                final int index = i;
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (choiceListener != null) {
                            choiceListener.onChoiceSelected(index);
                        }
                    }
                });
                root.add(btn).left().row();
                choiceButtons.add(btn);
            }
        }

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Hides the dialogue UI by clearing the input processor.
     * This prevents the UI from receiving further input events.
     */

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * updates stage and all the actors
     * called once per frame
     * 
     * @param delta Time passed since last frame in seconds
     */
    public void act(float delta) {
        stage.act(delta);
    }

    /**
     * Renders the dialogue UI to the screen
     * called during render phase after act()
     */
    public void draw() {
        stage.draw(); 
    }

    /**
     * Gets Scene2D stage
     * 
     * @return The stage instance used by the UI
     */
    public Stage getStage() {
        return stage; 
    }
}