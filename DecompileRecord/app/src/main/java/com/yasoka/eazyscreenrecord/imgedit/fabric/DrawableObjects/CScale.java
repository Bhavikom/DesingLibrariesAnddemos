package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Matrix;

public class CScale extends CTransform {
    private float mFactor = 0.0f;

    public CScale(CDrawable cDrawable) {
        setDrawable(cDrawable);
    }

    public CScale(CDrawable cDrawable, float f) {
        setDrawable(cDrawable);
        this.mFactor = f;
    }

    public float getFactor() {
        return this.mFactor;
    }

    public void setFactor(float f) {
        this.mFactor = f;
    }

    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Don't call draw() directly on this class.");
    }

    public void applyTransform(Matrix matrix) {
        float f = this.mFactor;
        matrix.setScale(f, f);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof CScale)) {
            return false;
        }
        CScale cScale = (CScale) obj;
        if (cScale.getDrawable() == null && getDrawable() == null) {
            return true;
        }
        if (!getDrawable().equals(cScale.getDrawable())) {
            return false;
        }
        if (cScale.mFactor == this.mFactor) {
            z = true;
        }
        return z;
    }
}
