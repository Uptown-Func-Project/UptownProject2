package com.badlogic.gdx.graphics;

import com.badlogic.gdx.files.FileHandle;

public class Texture {
    private int width;
    private int height;

    public Texture(FileHandle fh) {
        this.width = 16;
        this.height = 16;
    }

    public Texture(int width, int height, Pixmap.Format format) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void dispose() {}
}
