package io.github.OMAL_Maze.Map;

import com.badlogic.gdx.math.Rectangle;
/**
 * this class represents a trigger zone
 * the player teleports to another maze with a set spawnpoint
 */
public class TriggerZone {
    public Rectangle bounds;
    public int targetMaze;
    public int spawnPointX;
    public int spawnPointY;
    public int fromMaze;
    public int x;
    public int y;
    public int width;
    public int height;

}