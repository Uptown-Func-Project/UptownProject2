package io.github.OMAL_Maze;
/**
 * Leaderboard, displays the player's name and score the player got during their run
 */
public class LeaderboardScore {
    public String playerName;
    public int score;

    public LeaderboardScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
    
}
