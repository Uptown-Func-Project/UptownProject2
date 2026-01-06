package io.github.OMAL_Maze;

public class Achievement {
    public String name;
    public String description;
    public boolean unlocked;

    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.unlocked = false;
    }

    public void unlock() {
        this.unlocked = true;
    }

}
