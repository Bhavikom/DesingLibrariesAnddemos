package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class CropTransformation implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private CropType mCropType;
    private int mHeight;
    private int mWidth;

    /* renamed from: com.ezscreenrecorder.imgedit.filters.CropTransformation$1 */
    static /* synthetic */ class C12461 {

        /* renamed from: $SwitchMap$com$ezscreenrecorder$imgedit$filters$CropTransformation$CropType */
        static final /* synthetic */ int[] f109x5e133d93 = new int[CropType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.ezscreenrecorder.imgedit.filters.CropTransformation$CropType[] r0 = com.ezscreenrecorder.imgedit.filters.CropTransformation.CropType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f109x5e133d93 = r0
                int[] r0 = f109x5e133d93     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.ezscreenrecorder.imgedit.filters.CropTransformation$CropType r1 = com.ezscreenrecorder.imgedit.filters.CropTransformation.CropType.TOP     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f109x5e133d93     // Catch:{ NoSuchFieldError -> 0x001f }
                com.ezscreenrecorder.imgedit.filters.CropTransformation$CropType r1 = com.ezscreenrecorder.imgedit.filters.CropTransformation.CropType.CENTER     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f109x5e133d93     // Catch:{ NoSuchFieldError -> 0x002a }
                com.ezscreenrecorder.imgedit.filters.CropTransformation$CropType r1 = com.ezscreenrecorder.imgedit.filters.CropTransformation.CropType.BOTTOM     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.filters.CropTransformation.C12461.<clinit>():void");
        }
    }

    public enum CropType {
        TOP,
        CENTER,
        BOTTOM
    }

    public CropTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public CropTransformation(BitmapPool bitmapPool) {
        this(bitmapPool, 0, 0);
    }

    public CropTransformation(Context context, int i, int i2) {
        this(Glide.get(context).getBitmapPool(), i, i2);
    }

    public CropTransformation(BitmapPool bitmapPool, int i, int i2) {
        this(bitmapPool, i, i2, CropType.CENTER);
    }

    public CropTransformation(Context context, int i, int i2, CropType cropType) {
        this(Glide.get(context).getBitmapPool(), i, i2, cropType);
    }

    public CropTransformation(BitmapPool bitmapPool, int i, int i2, CropType cropType) {
        this.mCropType = CropType.CENTER;
        this.mBitmapPool = bitmapPool;
        this.mWidth = i;
        this.mHeight = i2;
        this.mCropType = cropType;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap = (Bitmap) resource.get();
        int i3 = this.mWidth;
        if (i3 == 0) {
            i3 = bitmap.getWidth();
        }
        this.mWidth = i3;
        int i4 = this.mHeight;
        if (i4 == 0) {
            i4 = bitmap.getHeight();
        }
        this.mHeight = i4;
        Config config = bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888;
        Bitmap bitmap2 = this.mBitmapPool.get(this.mWidth, this.mHeight, config);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(this.mWidth, this.mHeight, config);
        }
        float max = Math.max(((float) this.mWidth) / ((float) bitmap.getWidth()), ((float) this.mHeight) / ((float) bitmap.getHeight()));
        float width = ((float) bitmap.getWidth()) * max;
        float height = max * ((float) bitmap.getHeight());
        float f = (((float) this.mWidth) - width) / 2.0f;
        float top = getTop(height);
        new Canvas(bitmap2).drawBitmap(bitmap, null, new RectF(f, top, width + f, height + top), null);
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("CropTransformation(width=");
        sb.append(this.mWidth);
        sb.append(", height=");
        sb.append(this.mHeight);
        sb.append(", cropType=");
        sb.append(this.mCropType);
        sb.append(")");
        return sb.toString();
    }

    private float getTop(float f) {
        int i = C12461.f109x5e133d93[this.mCropType.ordinal()];
        if (i == 1) {
            return 0.0f;
        }
        if (i == 2) {
            return (((float) this.mHeight) - f) / 2.0f;
        }
        if (i != 3) {
            return 0.0f;
        }
        return ((float) this.mHeight) - f;
    }
}
