package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

public class CPath extends CDrawable {
    private Path mPath = new Path();

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        for (CTransform applyTransform : getTransforms()) {
            applyTransform.applyTransform(matrix);
        }
        Path path = new Path(this.mPath);
        path.transform(matrix);
        canvas.drawPath(path, getPaint());
    }

    public void lineTo(float f, float f2) {
        this.mPath.lineTo(f, f2);
        calculatePosition();
    }

    public void quadTo(float f, float f2, float f3, float f4) {
        this.mPath.quadTo(f, f2, f3, f4);
        calculatePosition();
    }

    public void moveTo(float f, float f2) {
        this.mPath.moveTo(f, f2);
        calculatePosition();
    }

    public Path getPath() {
        return this.mPath;
    }

    private void calculatePosition() {
        RectF rectF = new RectF();
        this.mPath.computeBounds(rectF, true);
        setXcoords((int) rectF.left);
        setYcoords((int) rectF.top);
        setHeight((int) (rectF.bottom - rectF.top));
        setWidth((int) (rectF.right - rectF.left));
        if (getHeight() == 0) {
            setHeight(1);
        }
        if (getWidth() == 0) {
            setWidth(1);
        }
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!super.equals(obj) || !(obj instanceof CPath)) {
            return false;
        }
        CPath cPath = (CPath) obj;
        if (cPath.mPath == null && this.mPath == null) {
            return true;
        }
        if (cPath.mPath == this.mPath) {
            z = true;
        }
        return z;
    }
}
