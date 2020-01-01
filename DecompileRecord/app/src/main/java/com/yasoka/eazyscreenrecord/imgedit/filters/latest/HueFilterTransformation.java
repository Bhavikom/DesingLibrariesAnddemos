package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageHueFilter;

public class HueFilterTransformation extends GPUFilterTransformation {
    private float mfloatensity;

    public HueFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public HueFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 90.0f);
    }

    public HueFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public HueFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageHueFilter());
        this.mfloatensity = f;
        ((GPUImageHueFilter) getFilter()).setHue(this.mfloatensity);
    }

    public float getmfloatensity() {
        return this.mfloatensity;
    }

    public void setmfloatensity(float f) {
        this.mfloatensity = f;
        ((GPUImageHueFilter) getFilter()).setHue(f);
    }
}
