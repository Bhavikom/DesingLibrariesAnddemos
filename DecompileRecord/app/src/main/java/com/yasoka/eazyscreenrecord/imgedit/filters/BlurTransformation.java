package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.RSRuntimeException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.ezscreenrecorder.imgedit.filters.internal.FastBlur;
import com.ezscreenrecorder.imgedit.filters.internal.RSBlur;

public class BlurTransformation implements Transformation<Bitmap> {
    private static int DEFAULT_DOWN_SAMPLING = 1;
    private static int MAX_RADIUS = 25;
    private BitmapPool mBitmapPool;
    private Context mContext;
    private int mRadius;
    private int mSampling;

    public BlurTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, BitmapPool bitmapPool) {
        this(context, bitmapPool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, BitmapPool bitmapPool, int i) {
        this(context, bitmapPool, i, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, int i) {
        this(context, Glide.get(context).getBitmapPool(), i, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, int i, int i2) {
        this(context, Glide.get(context).getBitmapPool(), i, i2);
    }

    public BlurTransformation(Context context, BitmapPool bitmapPool, int i, int i2) {
        this.mContext = context.getApplicationContext();
        this.mBitmapPool = bitmapPool;
        this.mRadius = i;
        this.mSampling = i2;
    }

    public int getmRadius() {
        return this.mRadius;
    }

    public void setmRadius(int i) {
        this.mRadius = i;
    }

    public int getmSampling() {
        return this.mSampling;
    }

    public void setmSampling(int i) {
        this.mSampling = i;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap;
        Bitmap bitmap2 = (Bitmap) resource.get();
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int i3 = this.mSampling;
        int i4 = width / i3;
        int i5 = height / i3;
        Bitmap bitmap3 = this.mBitmapPool.get(i4, i5, Config.ARGB_8888);
        if (bitmap3 == null) {
            bitmap3 = Bitmap.createBitmap(i4, i5, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap3);
        int i6 = this.mSampling;
        canvas.scale(1.0f / ((float) i6), 1.0f / ((float) i6));
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        try {
            bitmap = RSBlur.blur(this.mContext, bitmap3, this.mRadius);
        } catch (RSRuntimeException unused) {
            bitmap = FastBlur.blur(bitmap3, this.mRadius, true);
        }
        return BitmapResource.obtain(bitmap, this.mBitmapPool);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("BlurTransformation(radius=");
        sb.append(this.mRadius);
        sb.append(", sampling=");
        sb.append(this.mSampling);
        sb.append(")");
        return sb.toString();
    }
}
