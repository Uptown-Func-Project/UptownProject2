package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NameInputUI {
    private Stage stage;
    private TextField nameField;
    private Skin skin;
    private TextButton submitButton;

    private boolean submitted = false;
    private String name = "";

    public NameInputUI(Viewport viewport) {
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        nameField = new TextField("", skin);
        nameField.setMessageText("Enter your name");
        nameField.setMaxLength(12);
        nameField.setSize(300, 50);
        nameField.setPosition(150,350);
        stage.addActor(nameField);

        submitButton = new TextButton("Submit", skin);
        submitButton.setSize(150, 50);
        submitButton.setPosition(550, 350);
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

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void draw() {
        stage.draw();
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String getName() {
        return name;
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
