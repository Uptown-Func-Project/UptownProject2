package com.badlogic.gdx;

public interface Input {
    final class Keys {
        public static final int RIGHT = 22;
        public static final int LEFT = 21;
        public static final int UP = 19;
        public static final int DOWN = 20;
        public static final int SPACE = 62;
        public static final int E = 33;
        public static final int W = 51;
        public static final int A = 29;
        public static final int S = 47;
        public static final int D = 32;

        private Keys() {}
    }

    boolean isKeyPressed(int key);
    boolean isKeyJustPressed(int key);
    boolean isTouched();
    boolean justTouched();
    int getX();
    int getY();
}