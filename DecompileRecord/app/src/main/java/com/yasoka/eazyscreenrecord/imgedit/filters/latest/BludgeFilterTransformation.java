package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;

public class BludgeFilterTransformation extends GPUFilterTransformation {
    private float mfloatensity;

    public BludgeFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public BludgeFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.0f);
    }

    public BludgeFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public BludgeFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageBulgeDistortionFilter());
        this.mfloatensity = f;
        ((GPUImageBulgeDistortionFilter) getFilter()).setRadius(this.mfloatensity);
    }

    public float getmfloatensity() {
        return this.mfloatensity;
    }

    public void setmfloatensity(float f) {
        this.mfloatensity = f;
        ((GPUImageBulgeDistortionFilter) getFilter()).setRadius(f);
    }
}
