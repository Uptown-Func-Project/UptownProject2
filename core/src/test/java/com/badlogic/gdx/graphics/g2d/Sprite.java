package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Sprite {
    private Texture texture;
    private float x, y, width, height;

    public Sprite(Texture texture) {
        this.texture = texture;
        if (texture != null) {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        } else {
            this.width = 0;
            this.height = 0;
        }
    }

    public void setSize(float w, float h) {
        this.width = w; this.height = h;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void translateX(float amount) {
        this.x += amount;
    }
    
    
    public void translateY(float amount) {
        this.y += amount; 
    }

    public void translate(float xAmount, float yAmount) {
        this.x += xAmount;
        this.y += yAmount;
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Batch batch) {}
}
