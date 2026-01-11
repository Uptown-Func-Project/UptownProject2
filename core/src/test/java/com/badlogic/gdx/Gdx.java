package com.badlogic.gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

// Replacement Gdx class used in LibGDX, compatible with JUnit testing.
// Provides access to core subsystems like Audio, Files, Graphics, Input, and Application while testing.
public class Gdx {
    public static Audio audio = new TestAudio();
    public static Files files = new TestFiles();
    public static Graphics graphics = new TestGraphics();
    public static Input input = new TestInput();
    public static Application app = new TestApplication();

    public static TestAudio testAudio() {
        return (TestAudio) audio;
    }

    public static TestGraphics testGraphics() {
        return (TestGraphics) graphics;
    }

    public static TestInput testInput() {
        return (TestInput) input;
    }

    public static final class TestApplication implements Application {
        @Override
        public void exit() {
            // no-op, prevents crashes during tests
        }
    }

    public static final class TestFiles implements Files {
        @Override
        public FileHandle internal(String path) {
            return new FileHandle(path);
        }
    }

    public static final class TestGraphics implements Graphics {
        private float deltaTime = 0.016f;

        @Override
        public float getDeltaTime() {
            return deltaTime;
        }

        public void setDeltaTime(float deltaTime) {
            this.deltaTime = deltaTime;
        }
    }

    public static final class TestAudio implements Audio {
        public com.badlogic.gdx.TestSound lastSound;
        public FileHandle lastFile;

        @Override
        public Sound newSound(FileHandle fh) {
            lastFile = fh;
            lastSound = new com.badlogic.gdx.TestSound();
            return lastSound;
        }
    }
}
