package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;

public class Sprite {
    private Texture texture;
    private float x, y, width, height;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.width = texture != null ? texture.getWidth() : 0;
        this.height = texture != null ? texture.getHeight() : 0;
    }

    public void setSize(float w, float h) { this.width = w; this.height = h; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
