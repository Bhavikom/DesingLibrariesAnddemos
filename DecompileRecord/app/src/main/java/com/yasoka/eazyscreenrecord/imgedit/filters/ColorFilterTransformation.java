package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class ColorFilterTransformation implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private int mColor;

    public ColorFilterTransformation(Context context, int i) {
        this(Glide.get(context).getBitmapPool(), i);
    }

    public ColorFilterTransformation(BitmapPool bitmapPool, int i) {
        this.mBitmapPool = bitmapPool;
        this.mColor = i;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap = (Bitmap) resource.get();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Config config = bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888;
        Bitmap bitmap2 = this.mBitmapPool.get(width, height, config);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(width, height, config);
        }
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(new PorterDuffColorFilter(this.mColor, Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorFilterTransformation(color=");
        sb.append(this.mColor);
        sb.append(")");
        return sb.toString();
    }
}
