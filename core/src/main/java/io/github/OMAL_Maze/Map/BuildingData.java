package io.github.OMAL_Maze.Map;

/**
 * Class to store the data of each building so that it can be spawned.
 * This will read as unassigned but the data is loaded into this class from the JSON implementation.
 */
public class BuildingData {
    private int x;
    private int y;
    private int width;
    private int height;

    private String texturePath;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getTexturePath() { return texturePath; }
}
