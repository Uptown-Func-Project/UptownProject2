package io.github.OMAL_Maze.Dialogue;

import java.util.List;
/**
 * DialogueLine class, how each line of dialogue is gonna be represented in the JSON file.
 * Each line would have its own unique ID, speaker, text, player choices and nextId 
 * for each player choice, it would have its own text, nextId to branch out the conversation and effect which would trigger code.
 */
public class DialogueLine {
    public String id;  // unique id for branching
    public String speaker; // i.e "Professor"
    public String text; //  text displayed
    public List<Choice> choices; // null/empty = no choices
    public String nextId;  // id of next line (null to end)

    public static class Choice {
    public String text; // text displayed
    public String nextId; // id of next line
    public String effect; // runs code based on different effects based on player choices
    }
}

