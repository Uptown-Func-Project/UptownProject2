package io.github.OMAL_Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

//each button will need the visuals associated with it to also be displayed
public abstract class AbstractButton {
    private Texture texture;
    boolean active = false;
    int x, y;
    String message;

    public AbstractButton(FileHandle image) {
        //texture = new Texture(Gdx.files.internal("button.png"));
        this.texture = new Texture(image);
    }

    public boolean isActive() {
        return active;
    }

    public Texture getTexture(){
        return texture;
    }

    public int getWidth(){
        return texture.getWidth();
    }

    public int getHeight(){
        return texture.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, x,y);
    }
    public void makeActive() {
        active = true;
    }

    public void makeInactive() {
        active = false;
    }

    public void whenPressed() {
    }

    ;


    //returns true when the button is clicked
    boolean isClicked(FitViewport viewport) {
        boolean clicked = false;
        int locationX;
        int locationY;
        if (Gdx.input.isTouched()) {
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
            locationX = Gdx.input.getX();
//            System.out.println(x);
//            System.out.print(locationX);
//            System.out.println(x+getWidth());
//            locationY = Gdx.input.getY();
            locationY = Gdx.graphics.getHeight() - Gdx.input.getY();
//            System.out.println(y);
//            System.out.print(locationY);
//            System.out.println(y+getHeight());

            if (click.x >= x && click.x <= x+getWidth()){
                if (click.y >= y && click.y <= y+getHeight()){
                    clicked = true;
                    System.out.println(message);
                }
            }
        }
        return clicked;
    }
}
