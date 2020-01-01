package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageGammaFilter;

public class GammaFilterTransformation extends GPUFilterTransformation {
    private float mIntensity;

    public GammaFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public GammaFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.6f);
    }

    public GammaFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public GammaFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageGammaFilter());
        this.mIntensity = f;
        ((GPUImageGammaFilter) getFilter()).setGamma(this.mIntensity);
    }

    public float getmIntensity() {
        return this.mIntensity;
    }

    public void setmIntensity(float f) {
        this.mIntensity = f;
        ((GPUImageGammaFilter) getFilter()).setGamma(f);
    }
}
