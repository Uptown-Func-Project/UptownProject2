package com.badlogic.gdx.utils;

public final class Timer {

    public abstract static class Task implements Runnable {
        @Override
        public abstract void run();
    }

    private static Task lastScheduled;

    public static Task schedule(Task task, float delaySeconds, float intervalSeconds) {
        lastScheduled = task;
        return task;
    }

    public static Task schedule(Task task, float delaySeconds) {
        lastScheduled = task;
        return task;
    }

    public static Task getLastScheduledTask() {
        return lastScheduled;
    }

    private Timer() {}
}