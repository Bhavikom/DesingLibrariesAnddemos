package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class SaturationFilterTransformation extends GPUFilterTransformation {
    private float mfloatensity;

    public SaturationFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public SaturationFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.3f);
    }

    public SaturationFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public SaturationFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageSaturationFilter());
        this.mfloatensity = f;
        ((GPUImageSaturationFilter) getFilter()).setSaturation(this.mfloatensity);
    }

    public float getmfloatensity() {
        return this.mfloatensity;
    }

    public void setmfloatensity(float f) {
        this.mfloatensity = f;
        ((GPUImageSaturationFilter) getFilter()).setSaturation(f);
    }
}
