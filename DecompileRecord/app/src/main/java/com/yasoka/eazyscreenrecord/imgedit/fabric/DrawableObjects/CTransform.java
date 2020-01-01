package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Matrix;

public abstract class CTransform extends CDrawable {
    private CDrawable mDrawable;

    public abstract void applyTransform(Matrix matrix);

    public CDrawable getDrawable() {
        return this.mDrawable;
    }

    public void setDrawable(CDrawable cDrawable) {
        this.mDrawable = cDrawable;
    }
}
