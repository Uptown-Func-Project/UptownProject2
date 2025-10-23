package io.github.OMAL_Maze;

import com.google.gson.annotations.SerializedName;

public class EntityData {
    private String name;
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;

    @SerializedName("TexturePath")
    private String texturePath;

    public String getName() { return name; }
    public String getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getTexturePath() { return texturePath; }
}
