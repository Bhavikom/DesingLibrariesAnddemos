package com.yasoka.eazyscreenrecord.imgedit.filters.gpu;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImagePixelationFilter;

public class PixelationFilterTransformation extends GPUFilterTransformation {
    private float mPixel;

    public PixelationFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public PixelationFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 10.0f);
    }

    public PixelationFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public PixelationFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImagePixelationFilter());
        this.mPixel = f;
        ((GPUImagePixelationFilter) getFilter()).setPixel(this.mPixel);
    }

    public float getmPixel() {
        return this.mPixel;
    }

    public void setmPixel(float f) {
        this.mPixel = f;
        ((GPUImagePixelationFilter) getFilter()).setPixel(f);
    }
}
