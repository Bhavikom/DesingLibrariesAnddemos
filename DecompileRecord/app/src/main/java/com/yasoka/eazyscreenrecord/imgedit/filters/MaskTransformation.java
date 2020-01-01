package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.ezscreenrecorder.imgedit.filters.internal.Utils;

public class MaskTransformation implements Transformation<Bitmap> {
    private static Paint sMaskingPaint = new Paint();
    private BitmapPool mBitmapPool;
    private Context mContext;
    private int mMaskId;

    static {
        sMaskingPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    }

    public MaskTransformation(Context context, int i) {
        this(context, Glide.get(context).getBitmapPool(), i);
    }

    public MaskTransformation(Context context, BitmapPool bitmapPool, int i) {
        this.mBitmapPool = bitmapPool;
        this.mContext = context.getApplicationContext();
        this.mMaskId = i;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap = (Bitmap) resource.get();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmap2 = this.mBitmapPool.get(width, height, Config.ARGB_8888);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
        Drawable maskDrawable = Utils.getMaskDrawable(this.mContext, this.mMaskId);
        Canvas canvas = new Canvas(bitmap2);
        maskDrawable.setBounds(0, 0, width, height);
        maskDrawable.draw(canvas);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, sMaskingPaint);
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("MaskTransformation(maskId=");
        sb.append(this.mContext.getResources().getResourceEntryName(this.mMaskId));
        sb.append(")");
        return sb.toString();
    }
}
