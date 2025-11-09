package io.github.OMAL_Maze.Entities;
/**
 * Contains the data and getter methods for entity data.
 * These will not be seen as assigned due to the JSON implementation used however they are given values.
 */
public class EntityData {
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;

    private String texturePath;

    //Getter methods for all the values.
    public String getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getTexturePath() { return texturePath; }
}
