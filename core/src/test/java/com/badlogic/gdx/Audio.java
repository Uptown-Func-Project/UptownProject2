package com.badlogic.gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public interface Audio {
    Sound newSound(FileHandle fh);
}