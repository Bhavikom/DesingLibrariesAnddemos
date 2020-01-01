package com.yasoka.eazyscreenrecord.imgedit.fabric.DrawableObjects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import java.util.Vector;

public class CTranslation extends CTransform {
    private Vector<Integer> mDirection = new Vector<>(2);

    public CTranslation(CDrawable cDrawable) {
        setDrawable(cDrawable);
    }

    public CTranslation(CDrawable cDrawable, Vector<Integer> vector) {
        setDrawable(cDrawable);
        this.mDirection = vector;
    }

    public Vector<Integer> getDirection() {
        return this.mDirection;
    }

    public void setDirection(Vector<Integer> vector) {
        this.mDirection = vector;
    }

    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Don't call draw() directly on this class.");
    }

    public void applyTransform(Matrix matrix) {
        matrix.setTranslate((float) ((Integer) this.mDirection.get(0)).intValue(), (float) ((Integer) this.mDirection.get(1)).intValue());
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof CTranslation)) {
            return false;
        }
        CTranslation cTranslation = (CTranslation) obj;
        if (cTranslation.getDrawable() == null && getDrawable() == null) {
            return true;
        }
        if (!getDrawable().equals(cTranslation.getDrawable())) {
            return false;
        }
        if (cTranslation.mDirection == this.mDirection) {
            z = true;
        }
        return z;
    }
}
