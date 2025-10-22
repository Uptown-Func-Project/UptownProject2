package io.github.OMAL_Maze;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.Text;

public class Entity {
    Rectangle rectangle;
    int x;
    int y;
    int width;
    int height;
    Sprite sprite;
    Texture entityTexture;
    boolean visible;

    public Entity(int x, int y, int width, int height, Texture entityTexture) {
        rectangle = new Rectangle(x,y,width,height);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.sprite = new Sprite(entityTexture);
        this.sprite.setSize(width,height);
        this.sprite.setX(this.x);
        this.sprite.setY(this.y);
    }

    public boolean Overlaps(Rectangle bounds) {
        return rectangle.overlaps(bounds);
    }

    public void render(Batch batch) {
        sprite.draw(batch);
    }
    public void logic() {
        //Not sure yet
    }
}
