package com.badlogic.gdx;

import java.util.HashSet;
import java.util.Set;

public final class TestInput implements Input {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private boolean touched;
    private int x;
    private int y;

    @Override
    public boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void pressKey(int key, boolean pressed) {
        if (pressed) pressedKeys.add(key);
        else pressedKeys.remove(key);
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setPointer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void clear() {
        pressedKeys.clear();
        touched = false;
        x = 0;
        y = 0;
    }
}