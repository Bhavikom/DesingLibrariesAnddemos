package com.yasoka.eazyscreenrecord.imgedit.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class RoundedCornersTransformation implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private CornerType mCornerType;
    private int mDiameter;
    private int mMargin;
    private int mRadius;

    public enum CornerType {
        ALL,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        OTHER_TOP_LEFT,
        OTHER_TOP_RIGHT,
        OTHER_BOTTOM_LEFT,
        OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT,
        DIAGONAL_FROM_TOP_RIGHT
    }

    public RoundedCornersTransformation(Context context, int i, int i2) {
        this(context, i, i2, CornerType.ALL);
    }

    public RoundedCornersTransformation(BitmapPool bitmapPool, int i, int i2) {
        this(bitmapPool, i, i2, CornerType.ALL);
    }

    public RoundedCornersTransformation(Context context, int i, int i2, CornerType cornerType) {
        this(Glide.get(context).getBitmapPool(), i, i2, cornerType);
    }

    public RoundedCornersTransformation(BitmapPool bitmapPool, int i, int i2, CornerType cornerType) {
        this.mBitmapPool = bitmapPool;
        this.mRadius = i;
        this.mDiameter = this.mRadius * 2;
        this.mMargin = i2;
        this.mCornerType = cornerType;
    }

    public Resource<Bitmap> transform(Resource<Bitmap> resource, int i, int i2) {
        Bitmap bitmap = (Bitmap) resource.get();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmap2 = this.mBitmapPool.get(width, height, Config.ARGB_8888);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP));
        drawRoundRect(canvas, paint, (float) width, (float) height);
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        float f3 = f - ((float) i);
        float f4 = f2 - ((float) i);
        switch (this.mCornerType) {
            case ALL:
                int i2 = this.mMargin;
                RectF rectF = new RectF((float) i2, (float) i2, f3, f4);
                int i3 = this.mRadius;
                canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
                return;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, f3, f4);
                return;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, f3, f4);
                return;
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, f3, f4);
                return;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, f3, f4);
                return;
            case TOP:
                drawTopRoundRect(canvas, paint, f3, f4);
                return;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, f3, f4);
                return;
            case LEFT:
                drawLeftRoundRect(canvas, paint, f3, f4);
                return;
            case RIGHT:
                drawRightRoundRect(canvas, paint, f3, f4);
                return;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, f3, f4);
                return;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, f3, f4);
                return;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, f3, f4);
                return;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, f3, f4);
                return;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, f3, f4);
                return;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, f3, f4);
                return;
            default:
                int i4 = this.mMargin;
                RectF rectF2 = new RectF((float) i4, (float) i4, f3, f4);
                int i5 = this.mRadius;
                canvas.drawRoundRect(rectF2, (float) i5, (float) i5, paint);
                return;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        float f3 = (float) i;
        float f4 = (float) i;
        int i2 = this.mDiameter;
        RectF rectF = new RectF(f3, f4, (float) (i + i2), (float) (i + i2));
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        float f5 = (float) i4;
        int i5 = this.mRadius;
        canvas.drawRect(new RectF(f5, (float) (i4 + i5), (float) (i4 + i5), f2), paint);
        int i6 = this.mMargin;
        canvas.drawRect(new RectF((float) (this.mRadius + i6), (float) i6, f, f2), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mDiameter;
        float f3 = f - ((float) i);
        int i2 = this.mMargin;
        RectF rectF = new RectF(f3, (float) i2, f, (float) (i2 + i));
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        canvas.drawRect(new RectF((float) i4, (float) i4, f - ((float) this.mRadius), f2), paint);
        int i5 = this.mRadius;
        canvas.drawRect(new RectF(f - ((float) i5), (float) (this.mMargin + i5), f, f2), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        float f3 = (float) i;
        int i2 = this.mDiameter;
        RectF rectF = new RectF(f3, f2 - ((float) i2), (float) (i + i2), f2);
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        canvas.drawRect(new RectF((float) i4, (float) i4, (float) (i4 + this.mDiameter), f2 - ((float) this.mRadius)), paint);
        int i5 = this.mMargin;
        canvas.drawRect(new RectF((float) (this.mRadius + i5), (float) i5, f, f2), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mDiameter;
        RectF rectF = new RectF(f - ((float) i), f2 - ((float) i), f, f2);
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        int i3 = this.mMargin;
        canvas.drawRect(new RectF((float) i3, (float) i3, f - ((float) this.mRadius), f2), paint);
        int i4 = this.mRadius;
        canvas.drawRect(new RectF(f - ((float) i4), (float) this.mMargin, f, f2 - ((float) i4)), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        RectF rectF = new RectF((float) i, (float) i, f, (float) (i + this.mDiameter));
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        int i3 = this.mMargin;
        canvas.drawRect(new RectF((float) i3, (float) (i3 + this.mRadius), f, f2), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        RectF rectF = new RectF((float) this.mMargin, f2 - ((float) this.mDiameter), f, f2);
        int i = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i, (float) i, paint);
        int i2 = this.mMargin;
        canvas.drawRect(new RectF((float) i2, (float) i2, f, f2 - ((float) this.mRadius)), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        RectF rectF = new RectF((float) i, (float) i, (float) (i + this.mDiameter), f2);
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        int i3 = this.mMargin;
        canvas.drawRect(new RectF((float) (this.mRadius + i3), (float) i3, f, f2), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        RectF rectF = new RectF(f - ((float) this.mDiameter), (float) this.mMargin, f, f2);
        int i = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i, (float) i, paint);
        int i2 = this.mMargin;
        canvas.drawRect(new RectF((float) i2, (float) i2, f - ((float) this.mRadius), f2), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        RectF rectF = new RectF((float) this.mMargin, f2 - ((float) this.mDiameter), f, f2);
        int i = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i, (float) i, paint);
        RectF rectF2 = new RectF(f - ((float) this.mDiameter), (float) this.mMargin, f, f2);
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i2, (float) i2, paint);
        int i3 = this.mMargin;
        float f3 = (float) i3;
        float f4 = (float) i3;
        int i4 = this.mRadius;
        canvas.drawRect(new RectF(f3, f4, f - ((float) i4), f2 - ((float) i4)), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        RectF rectF = new RectF((float) i, (float) i, (float) (i + this.mDiameter), f2);
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        RectF rectF2 = new RectF((float) this.mMargin, f2 - ((float) this.mDiameter), f, f2);
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        int i5 = this.mRadius;
        canvas.drawRect(new RectF((float) (i4 + i5), (float) i4, f, f2 - ((float) i5)), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        RectF rectF = new RectF((float) i, (float) i, f, (float) (i + this.mDiameter));
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        RectF rectF2 = new RectF(f - ((float) this.mDiameter), (float) this.mMargin, f, f2);
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        float f3 = (float) i4;
        int i5 = this.mRadius;
        canvas.drawRect(new RectF(f3, (float) (i4 + i5), f - ((float) i5), f2), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        RectF rectF = new RectF((float) i, (float) i, f, (float) (i + this.mDiameter));
        int i2 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i2, (float) i2, paint);
        int i3 = this.mMargin;
        RectF rectF2 = new RectF((float) i3, (float) i3, (float) (i3 + this.mDiameter), f2);
        int i4 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i4, (float) i4, paint);
        int i5 = this.mMargin;
        int i6 = this.mRadius;
        canvas.drawRect(new RectF((float) (i5 + i6), (float) (i5 + i6), f, f2), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mMargin;
        float f3 = (float) i;
        float f4 = (float) i;
        int i2 = this.mDiameter;
        RectF rectF = new RectF(f3, f4, (float) (i + i2), (float) (i + i2));
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
        int i4 = this.mDiameter;
        RectF rectF2 = new RectF(f - ((float) i4), f2 - ((float) i4), f, f2);
        int i5 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i5, (float) i5, paint);
        int i6 = this.mMargin;
        canvas.drawRect(new RectF((float) i6, (float) (i6 + this.mRadius), f - ((float) this.mDiameter), f2), paint);
        int i7 = this.mMargin;
        canvas.drawRect(new RectF((float) (this.mDiameter + i7), (float) i7, f, f2 - ((float) this.mRadius)), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        int i = this.mDiameter;
        float f3 = f - ((float) i);
        int i2 = this.mMargin;
        RectF rectF = new RectF(f3, (float) i2, f, (float) (i2 + i));
        int i3 = this.mRadius;
        canvas.drawRoundRect(rectF, (float) i3, (float) i3, paint);
        int i4 = this.mMargin;
        float f4 = (float) i4;
        int i5 = this.mDiameter;
        RectF rectF2 = new RectF(f4, f2 - ((float) i5), (float) (i4 + i5), f2);
        int i6 = this.mRadius;
        canvas.drawRoundRect(rectF2, (float) i6, (float) i6, paint);
        int i7 = this.mMargin;
        float f5 = (float) i7;
        float f6 = (float) i7;
        int i8 = this.mRadius;
        canvas.drawRect(new RectF(f5, f6, f - ((float) i8), f2 - ((float) i8)), paint);
        int i9 = this.mMargin;
        int i10 = this.mRadius;
        canvas.drawRect(new RectF((float) (i9 + i10), (float) (i9 + i10), f, f2), paint);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoundedTransformation(radius=");
        sb.append(this.mRadius);
        sb.append(", margin=");
        sb.append(this.mMargin);
        sb.append(", diameter=");
        sb.append(this.mDiameter);
        sb.append(", cornerType=");
        sb.append(this.mCornerType.name());
        sb.append(")");
        return sb.toString();
    }
}
