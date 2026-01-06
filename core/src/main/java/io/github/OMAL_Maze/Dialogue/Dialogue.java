package io.github.OMAL_Maze.Dialogue;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class Dialogue {
    private DialogueLine[] lines;
    private transient Map<String, DialogueLine> map;

    public void buildIndex() {
        map = new HashMap<>();
        if (lines == null) return;
        for (DialogueLine line : lines) map.put(line.id, line);
    }

    public DialogueLine get(String id) {
        if (map == null) buildIndex();
        return map.get(id);
    }

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
