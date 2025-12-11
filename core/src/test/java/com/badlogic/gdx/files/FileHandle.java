package com.badlogic.gdx.files;

public class FileHandle {
    private final String path;
    public FileHandle(String path) { this.path = path; }
    public String path() { return path; }
    public boolean exists() { return false; }
}
