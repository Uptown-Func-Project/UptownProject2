package com.badlogic.gdx.utils.viewport;

import com.badlogic.gdx.math.Vector2;

public class Viewport {
    protected float worldWidth;
    protected float worldHeight;

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public Vector2 unproject(Vector2 screenCoords) {
        return screenCoords;
    }
}