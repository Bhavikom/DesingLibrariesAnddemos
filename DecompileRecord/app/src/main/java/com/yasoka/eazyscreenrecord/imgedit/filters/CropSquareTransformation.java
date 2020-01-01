package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class CropSquareTransformation implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private int mHeight;
    private int mWidth;

    public CropSquareTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public CropSquareTransformation(BitmapPool bitmapPool) {
        this.mBitmapPool = bitmapPool;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap = (Bitmap) resource.get();
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        this.mWidth = (bitmap.getWidth() - min) / 2;
        this.mHeight = (bitmap.getHeight() - min) / 2;
        Bitmap bitmap2 = this.mBitmapPool.get(this.mWidth, this.mHeight, bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(bitmap, this.mWidth, this.mHeight, min, min);
        }
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("CropSquareTransformation(width=");
        sb.append(this.mWidth);
        sb.append(", height=");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }
}
