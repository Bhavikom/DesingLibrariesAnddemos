package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageEmbossFilter;

public class EmbossFilterTransformation extends GPUFilterTransformation {
    private float mfloatensity;

    public EmbossFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public EmbossFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 1.5f);
    }

    public EmbossFilterTransformation(Context context, float f) {
        this(context, Glide.get(context).getBitmapPool(), f);
    }

    public EmbossFilterTransformation(Context context, BitmapPool bitmapPool, float f) {
        super(context, bitmapPool, new GPUImageEmbossFilter());
        this.mfloatensity = f;
        ((GPUImageEmbossFilter) getFilter()).setIntensity(this.mfloatensity);
    }

    public float getmfloatensity() {
        return this.mfloatensity;
    }

    public void setmfloatensity(float f) {
        this.mfloatensity = f;
        ((GPUImageEmbossFilter) getFilter()).setIntensity(f);
    }
}
