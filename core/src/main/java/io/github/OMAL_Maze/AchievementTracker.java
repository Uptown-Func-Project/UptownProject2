package io.github.OMAL_Maze;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages all achievements in the game
 * Initialise list of achievements
 */
public class AchievementTracker {
    private List<Achievement> achievements;
    /**
     * AchievementTracker constructor
     * adds all achievements in the game
     * achievements start as locked
     */
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

    /**
     * returns complete list of achievements
     */
    public List<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * unlocks an achievement by name if it exists in the achievement list
     * @param name exact name of achievement to unlock
     */
    public void unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.name.equals(name)) {
                achievement.unlock();
            }
        }
    }
}