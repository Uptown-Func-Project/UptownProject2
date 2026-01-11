package io.github.OMAL_Maze;

import java.util.ArrayList;
import java.util.List;

public class AchievementTracker {
    private List<Achievement> achievements;

    public AchievementTracker() {
        achievements = new ArrayList<>();
        achievements.add(new Achievement("Game Started", "Started the game"));
        achievements.add(new Achievement("Coin Collector", "Collected 5 coins"));
        achievements.add(new Achievement("Escaped Uni", "Escaped the game for the first time!"));
        achievements.add(new Achievement("Prepared and Ready", "Fail the quiz and picked the bat"));
        achievements.add(new Achievement("Who needs a degree?", "Escaped Uni without getting your degree"));
        achievements.add(new Achievement("Good Student", "Escaped Uni with your degree"));
        achievements.add(new Achievement("Speedy Escaper", "Escaped Uni in under 2 minutes"));
        achievements.add(new Achievement("Ouch that hurts!", "Get bitten by a geese"));
        achievements.add(new Achievement("Powered-Up", "Buy an item from the shop"));
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