package io.github.OMAL_Maze.Dialogue;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class Dialogue {

    /**
    * Manages dialogue lines loaded from dialogue.json. 
    * 
    */

    private DialogueLine[] lines;  // Array of dialogue lines from json file
    private transient Map<String, DialogueLine> map;  // map to look up dialogue based on ID.

    /**
     * builds an index which links dialogue IDs to DialogueLine objects
     */
    public void buildIndex() {
        map = new HashMap<>();
        if (lines == null) return;
        for (DialogueLine line : lines) map.put(line.id, line);
    }

    /**
     * gets dialogue line based on ID.
     * builds the index if not ready
     * 
     * @param id unique ID for the dialogue line
     * @return DialogueLine object or null
     */
    public DialogueLine get(String id) {
        if (map == null) buildIndex();
        return map.get(id);
    }

    /**
     * Loads the dialogue from the JSON file and creates a Dialogue instance.
     * Json file will contain an array of DialogueLine objects
     * 
     * @param path Path to the JSON file 
     * @return new Dialgoue instance with loaded lines and built index
     */
    public static Dialogue fromJson(String path) {
        FileHandle fh = Gdx.files.internal(path);
        Json json = new Json();
        DialogueLine[] lines = json.fromJson(DialogueLine[].class, fh);
        Dialogue d = new Dialogue();
        d.lines = lines;
        d.buildIndex();
        return d;
    }

}
