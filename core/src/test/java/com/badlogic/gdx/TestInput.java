package com.badlogic.gdx;

import java.util.HashSet;
import java.util.Set;

public final class TestInput implements Input {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> justPressedKeys = new HashSet<>();
    private boolean touched;
    private boolean justTouched;
    private int x;
    private int y;

    @Override
    public boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }

    @Override
    public boolean isKeyJustPressed(int key) {
        return justPressedKeys.remove(key);
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public boolean justTouched() {
        boolean result = justTouched;
        justTouched = false;
        return result;
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
        if (pressed) {
            if (!pressedKeys.contains(key)) {
                justPressedKeys.add(key);
            }
            pressedKeys.add(key);
        } else {
            pressedKeys.remove(key);
            justPressedKeys.remove(key);
        }
    }

    public void setTouched(boolean touched) {
        if (touched && !this.touched) {
            this.justTouched = true;
        }
        this.touched = touched;
    }

    public void setPointer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void clear() {
        pressedKeys.clear();
        justPressedKeys.clear();
        touched = false;
        justTouched = false;
        x = 0;
        y = 0;
    }
}