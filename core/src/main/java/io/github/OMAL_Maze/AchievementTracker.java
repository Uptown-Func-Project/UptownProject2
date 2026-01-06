package io.github.OMAL_Maze;

import java.util.ArrayList;
import java.util.List;

public class AchievementTracker {
    private List<Achievement> achievements;

    public AchievementTracker() {
        achievements = new ArrayList<>();
        achievements.add(new Achievement("Game Started", "Started the game"));
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.name.equals(name)) {
                achievement.unlock();
            }
        }
    }
}