package io.github.OMAL_Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Building {
    Rectangle rectangle;
    int x;
    int y;
    int width;
    int height;
    Sprite sprite;
    Texture buildingTexture;
    boolean visible=true;
    public Building(int x, int y, int width, int height, Texture buildingTexture) {
        rectangle = new Rectangle(x,y,width,height);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.buildingTexture = buildingTexture;
        this.sprite = new Sprite(this.buildingTexture);
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
    public boolean getVisible() {
        return this.visible;
    }
    public void setVisible(boolean nVisible) {
        this.visible=nVisible;
    }
}
