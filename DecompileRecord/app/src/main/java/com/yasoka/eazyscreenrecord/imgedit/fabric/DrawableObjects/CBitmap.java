package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class CBitmap extends CDrawable {
    private Bitmap mBitmap;

    public CBitmap(Bitmap bitmap, int i, int i2) {
        this(bitmap, i, i2, null);
    }

    public CBitmap(Bitmap bitmap, int i, int i2, Paint paint) {
        this.mBitmap = bitmap;
        setHeight(this.mBitmap.getHeight());
        setWidth(this.mBitmap.getWidth());
        setXcoords(i);
        setYcoords(i2);
        setPaint(paint);
    }

    public CBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) {
        this(bitmap, i, i2, i3, i4, null);
    }

    public CBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, Paint paint) {
        this.mBitmap = Bitmap.createScaledBitmap(this.mBitmap, i3, i4, true);
        setHeight(i3);
        setWidth(i4);
        setXcoords(i);
        setYcoords(i2);
        setPaint(paint);
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        for (CTransform applyTransform : getTransforms()) {
            applyTransform.applyTransform(matrix);
        }
        Bitmap createBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
        new Canvas(createBitmap);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawBitmap(this.mBitmap, (float) getXcoords(), (float) getYcoords(), getPaint());
        canvas.restore();
        canvas.drawBitmap(createBitmap, 0.0f, 0.0f, getPaint());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof CBitmap)) {
            return false;
        }
        CBitmap cBitmap = (CBitmap) obj;
        if (cBitmap.mBitmap == null && this.mBitmap == null) {
            return true;
        }
        return cBitmap.mBitmap.sameAs(this.mBitmap);
    }
}
