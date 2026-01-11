package io.github.OMAL_Maze;
/**
 * Achievement class used to display achievements players can get
 * displays name and description of achievement
 * unlocked boolean used to keep track of what achievements the player got 
 */
public class Achievement {
    public String name;
    public String description;
    public boolean unlocked;
    /**
     * Achievement constructor
     * 
     * @param name  
     * @param description
     */
    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.unlocked = false;
    }

    public void unlock() {
        this.unlocked = true;
    }

}
