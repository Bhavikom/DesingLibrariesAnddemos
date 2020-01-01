package com.yasoka.eazyscreenrecord;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeWidthAnimation extends Animation {
    private int mStartWidth;
    private View mView;
    private int mWidth;

    public boolean willChangeBounds() {
        return true;
    }

    public ResizeWidthAnimation(View view, int i) {
        this.mView = view;
        this.mWidth = i;
        this.mStartWidth = view.getWidth();
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float f, Transformation transformation) {
        int i = this.mStartWidth;
        this.mView.getLayoutParams().width = i + ((int) (((float) (this.mWidth - i)) * f));
        this.mView.requestLayout();
    }

    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
    }
}
