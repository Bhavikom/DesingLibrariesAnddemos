package com.yasoka.eazyscreenrecord.imgedit.filters.gpu;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageSepiaFilter;

public class SepiaFilterTransformation extends GPUFilterTransformation {
    private float mIntensity;

    public SepiaFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public SepiaFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.0f);
    }

    public SepiaFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public SepiaFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageSepiaFilter());
        this.mIntensity = f;
        ((GPUImageSepiaFilter) getFilter()).setIntensity(this.mIntensity);
    }

    public float getmIntensity() {
        return this.mIntensity;
    }

    public void setmIntensity(float f) {
        this.mIntensity = f;
        ((GPUImageSepiaFilter) getFilter()).setIntensity(f);
    }
}
