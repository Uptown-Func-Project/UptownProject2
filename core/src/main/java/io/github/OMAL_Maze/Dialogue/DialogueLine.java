package io.github.OMAL_Maze.Dialogue;

import java.util.List;

public class DialogueLine {
    public String id;  // unique id for branching
    public String speaker; // i.e "Professor"
    public String text; // actual text
    public List<Choice> choices; // null/empty = no choices
    public String nextId;  // id of next line (null to end)

    public static class Choice {
    public String text;
    public String nextId;
    public String effect;
    }
}

