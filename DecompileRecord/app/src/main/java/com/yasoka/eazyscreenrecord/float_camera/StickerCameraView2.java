package com.yasoka.eazyscreenrecord.float_camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

public class StickerCameraView2 extends StickerView {
    private TextureView tv_main;

    public StickerCameraView2(Context context) {
        super(context);
    }

    public StickerCameraView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StickerCameraView2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public View getMainView() {
        TextureView textureView = this.tv_main;
        if (textureView != null) {
            return textureView;
        }
        this.tv_main = new TextureView(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.tv_main.setLayoutParams(layoutParams);
        return this.tv_main;
    }

    /* access modifiers changed from: protected */
    public void onScaling(boolean z) {
        super.onScaling(z);
    }
}
