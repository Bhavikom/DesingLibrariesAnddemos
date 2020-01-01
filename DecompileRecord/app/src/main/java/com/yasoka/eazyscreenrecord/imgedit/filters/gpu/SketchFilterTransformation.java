package com.yasoka.eazyscreenrecord.imgedit.filters.gpu;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import p012jp.p013co.cyberagent.android.gpuimage.GPUImageSketchFilter;

public class SketchFilterTransformation extends GPUFilterTransformation {
    public SketchFilterTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public SketchFilterTransformation(Context context, BitmapPool bitmapPool) {
        super(context, bitmapPool, new GPUImageSketchFilter());
    }
}
