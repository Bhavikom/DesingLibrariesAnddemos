package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.view.Surface;

public interface RenderHolderCallback {
    void onCreate(Surface surface);

    void onDestroy();

    void onFrameAvailable();
}
