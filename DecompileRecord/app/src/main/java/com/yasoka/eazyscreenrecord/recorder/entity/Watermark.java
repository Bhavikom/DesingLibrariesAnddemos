package com.yasoka.eazyscreenrecord.recorder.entity;

import android.graphics.Bitmap;

public class Watermark {
    public int hMargin;
    public int height;
    public Bitmap markImg;
    public int orientation;
    public int vMargin;
    public int width;

    public Watermark(Bitmap bitmap, int i, int i2, int i3, int i4, int i5) {
        this.markImg = bitmap;
        this.width = i;
        this.height = i2;
        this.orientation = i3;
        this.vMargin = i4;
        this.hMargin = i5;
    }
}
