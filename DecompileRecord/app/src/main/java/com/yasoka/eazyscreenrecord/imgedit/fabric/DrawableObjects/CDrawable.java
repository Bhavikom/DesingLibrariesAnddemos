package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public abstract class CDrawable {
    private static int nextId;
    private int height;

    /* renamed from: id */
    private int f104id = generateNextId();
    private Paint mPaint;
    private List<CTransform> mTransforms = new ArrayList();
    private int width;

    /* renamed from: x */
    private int f105x;

    /* renamed from: y */
    private int f106y;

    public abstract void draw(Canvas canvas);

    public CDrawable() {
    }

    public CDrawable(int i, int i2, Paint paint) {
        this.f105x = i;
        this.f106y = i2;
        this.mPaint = paint;
    }

    private static int generateNextId() {
        int i = nextId;
        nextId = i + 1;
        return i;
    }

    public int getId() {
        return this.f104id;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getWidth() {
        return this.width;
    }

    public int getXcoords() {
        return this.f105x;
    }

    public int getYcoords() {
        return this.f106y;
    }

    public void setXcoords(int i) {
        this.f105x = i;
    }

    public void setYcoords(int i) {
        this.f106y = i;
    }

    public Rect computeBounds() {
        int i = this.f105x;
        float f = (float) i;
        int i2 = this.f106y;
        RectF rectF = new RectF(f, (float) i2, (float) (i + this.width), (float) (i2 + this.height));
        Matrix matrix = new Matrix();
        for (CTransform applyTransform : this.mTransforms) {
            applyTransform.applyTransform(matrix);
        }
        matrix.mapRect(rectF);
        Rect rect = new Rect();
        rectF.round(rect);
        return rect;
    }

    public boolean hasTransforms() {
        return !this.mTransforms.isEmpty();
    }

    public void removeTransform(CTransform cTransform) {
        this.mTransforms.remove(cTransform);
    }

    public void addTransform(CTransform cTransform) {
        this.mTransforms.add(cTransform);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof CDrawable)) {
            return false;
        }
        CDrawable cDrawable = (CDrawable) obj;
        if (!(cDrawable.getId() == getId() && cDrawable.getXcoords() == getXcoords() && cDrawable.getXcoords() == getXcoords() && cDrawable.getYcoords() == getYcoords() && cDrawable.getHeight() == getHeight() && cDrawable.getWidth() == getWidth() && cDrawable.getPaint() == getPaint())) {
            z = false;
        }
        return z;
    }

    public List<CTransform> getTransforms() {
        return this.mTransforms;
    }
}
