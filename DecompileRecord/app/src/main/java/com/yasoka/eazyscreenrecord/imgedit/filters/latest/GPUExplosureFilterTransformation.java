package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageExposureFilter;

public class GPUExplosureFilterTransformation extends GPUFilterTransformation {
    private float mfloatensity;

    public GPUExplosureFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public GPUExplosureFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.0f);
    }

    public GPUExplosureFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public GPUExplosureFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageExposureFilter());
        this.mfloatensity = f;
        ((GPUImageExposureFilter) getFilter()).setExposure(this.mfloatensity);
    }

    public float getmfloatensity() {
        return this.mfloatensity;
    }

    public void setmfloatensity(float f) {
        this.mfloatensity = f;
        ((GPUImageExposureFilter) getFilter()).setExposure(f);
    }
}
