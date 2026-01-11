package com.badlogic.gdx.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandle {
    private final String path;

    public FileHandle(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    public boolean exists() {
        return Files.exists(resolvePath());
    }

    public String readString() {
        Path resolved = resolvePath();
        try {
            return Files.readString(resolved, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read file: " + resolved.toAbsolutePath(), exception);
        }
    }

    private Path resolvePath() {
        Path fromCwd = Paths.get("assets").resolve(path);
        if (Files.exists(fromCwd)) {
            return fromCwd;
        }

        Path fromParent = Paths.get("..").resolve("assets").resolve(path).normalize();
        return fromParent;
    }
}
