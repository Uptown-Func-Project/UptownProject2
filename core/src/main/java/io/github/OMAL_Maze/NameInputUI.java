package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * UI to collect player's names
 * Displays a text input field and submit button, then disables them after submission.
 * defaults to "Player" if no name is provided
 */
public class NameInputUI {
    private Stage stage;
    private TextField nameField;
    private Skin skin;
    private TextButton submitButton;

    private boolean submitted = false;
    private String name = "";

    /**
     * NameInputUI constructor with text field and submit button
     * text field has a 12-character limit and placeholder text
     * after submitting, UI disables itself and stores the name
     * 
     * @param viewport
     */
    public NameInputUI(Viewport viewport) {
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        nameField = new TextField("", skin);
        nameField.setMessageText("Enter your name");
        nameField.setMaxLength(12);
        nameField.setSize(300, 50);
        nameField.setPosition(150,280);
        stage.addActor(nameField);

        submitButton = new TextButton("Submit", skin);
        submitButton.setSize(150, 50);
        submitButton.setPosition(550, 280);
        stage.addActor(submitButton);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (submitted == false) {
                    name = nameField.getText().trim();
                }
                if (name.isEmpty()) {
                    name = "Player";
                }
                submitted = true;

                nameField.setDisabled(true);
                submitButton.setDisabled(true);
                Gdx.input.setInputProcessor(null);
            }
        });
    }

    /**
     * makes UI active and ready to receive input
     * sets UI as active input processor so text field can read keyboard input
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    /**
     * renders name input UI to screen
     */
    public void draw() {
        stage.draw();
    }
    /**
     * checks if player submitted
     * @return true/false if submit buttons been clicked
     */
    public boolean isSubmitted() {
        return submitted;
    }
    /**
     * @return submitted player's name
     */
    public String getName() {
        return name;
    }
    /**
     * Updates the stage and all its actors
     * called once per frame
     * 
     * @param delta Time passed since last frame in seconds
     */
    public void update(float delta) {
        stage.act(delta);
    }

    /**
     * Dispose of stage and skin resource when UI no longer needed
     */
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
