package io.github.OMAL_Maze.Dialogue;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class Dialogue {
    private transient Map<String, DialogueLine> map;

    public void buildIndex() {
        map = new HashMap<>();
        if (lines == null) return;
        for (DialogueLine 1 : lines) map.put(1.id, 1);
    }

    public DialogueLine get(String id) {
        if (map == null) buildIndex();
        return map.get(id);
    }

    public static Dialogue fromJson(String path) {
        FileHandle fh = new FileHandle(path);
        Json json = new Json();
        Dialogue d = json.fromJson(Dialogue.class, fh);
        d.buildIndex();
        return d;
    }

}
