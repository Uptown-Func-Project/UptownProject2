package io.github.OMAL_Maze.Dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class DialogueUI {
    private Stage stage;
    private Label speakerLabel;
    private Label textLabel;
    private Table root;
    private List<TextButton> choiceButtons;
    private ChoiceListener choiceListener;
    private BitmapFont font;
    private Texture backgroundTexture;

    public interface ChoiceListener {
        void onChoiceSelected(int index);
    }

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

        // background for text box
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.3f)); // RGB value then 80% Opacity
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        root.setBackground(background);
        
        // Position and pad the table
        root.center().bottom().pad(10).padLeft(900);
        
        root.add(speakerLabel).left().row();
        root.add(textLabel).width(800).pad(5).row();
        
        stage.addActor(root);
        choiceButtons = new ArrayList<>();
    }

    public void setChoiceListener(ChoiceListener listener) {
        this.choiceListener = listener;
    }

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
                root.add(btn).pad(5).left().row();
                choiceButtons.add(btn);
            }
        }

        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw(); 
    }

    public Stage getStage() {
        return stage; 
    }
}