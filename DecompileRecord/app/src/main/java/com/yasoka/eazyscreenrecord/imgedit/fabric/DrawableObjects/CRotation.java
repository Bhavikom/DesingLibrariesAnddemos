package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Matrix;

public class CRotation extends CTransform {
    private int mRotDegree;

    public CRotation(CDrawable cDrawable) {
        setDrawable(cDrawable);
    }

    public CRotation(CDrawable cDrawable, int i) {
        setDrawable(cDrawable);
        this.mRotDegree = i;
    }

    public int getRotation() {
        return this.mRotDegree;
    }

    public void setRotation(int i) {
        this.mRotDegree = i;
    }

    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Don't call draw() directly on this class.");
    }

    public void applyTransform(Matrix matrix) {
        matrix.setRotate((float) this.mRotDegree);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof CRotation)) {
            return false;
        }
        CRotation cRotation = (CRotation) obj;
        if (cRotation.getDrawable() == null && getDrawable() == null) {
            return true;
        }
        if (!getDrawable().equals(cRotation.getDrawable())) {
            return false;
        }
        if (cRotation.mRotDegree == this.mRotDegree) {
            z = true;
        }
        return z;
    }
}
