package com.yasoka.eazyscreenrecord.float_camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

public class StickerCameraView extends StickerView {
    private CameraPreview tv_main;

    public StickerCameraView(Context context) {
        super(context);
    }

    public StickerCameraView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StickerCameraView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public static float pixelsToSp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public View getMainView() {
        CameraPreview cameraPreview = this.tv_main;
        if (cameraPreview != null) {
            return cameraPreview;
        }
        this.tv_main = new CameraPreview(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.tv_main.setLayoutParams(layoutParams);
        if (getImageViewFlip() != null) {
            getImageViewFlip().setVisibility(8);
        }
        return this.tv_main;
    }

    /* access modifiers changed from: protected */
    public void onScaling(boolean z) {
        super.onScaling(z);
    }
}
