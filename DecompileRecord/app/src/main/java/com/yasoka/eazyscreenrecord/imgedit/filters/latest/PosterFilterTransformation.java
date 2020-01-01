package com.yasoka.eazyscreenrecord.imgedit.filters.latest;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ezscreenrecorder.imgedit.filters.gpu.GPUFilterTransformation;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;

public class PosterFilterTransformation extends GPUFilterTransformation {
    private int mIntensity;

    public PosterFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public PosterFilterTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, 50);
    }

    public PosterFilterTransformation(Context context, int i) {
        this(context, Glide.get(context).getBitmapPool(), i);
    }

    public PosterFilterTransformation(Context context, BitmapPool bitmapPool, int i) {
        super(context, bitmapPool, new GPUImagePosterizeFilter());
        this.mIntensity = i;
        ((GPUImagePosterizeFilter) getFilter()).setColorLevels(this.mIntensity);
    }

    public int getmIntensity() {
        return this.mIntensity;
    }

    public void setmIntensity(int i) {
        this.mIntensity = i;
        ((GPUImagePosterizeFilter) getFilter()).setColorLevels(i);
    }
}
