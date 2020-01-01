package com.yasoka.eazyscreenrecord.imgedit.sticker;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.ezscreenrecorder.imgedit.sticker.util.FontFitTextView;

public class StickerTextView extends StickerView {
    private FontFitTextView tv_main;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StickerTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public static float pixelsToSp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public View getMainView() {
        FontFitTextView fontFitTextView = this.tv_main;
        if (fontFitTextView != null) {
            return fontFitTextView;
        }
        this.tv_main = new FontFitTextView(getContext());
        this.tv_main.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.tv_main.setGravity(17);
        this.tv_main.setTextSize(1600.0f);
        this.tv_main.setMaxLines(1);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.tv_main.setLayoutParams(layoutParams);
        this.tv_main.setFocusable(true);
        this.tv_main.setFocusableInTouchMode(true);
        if (getImageViewFlip() != null) {
            getImageViewFlip().setVisibility(8);
        }
        return this.tv_main;
    }

    public void setTextColor(int i) {
        this.tv_main.setTextColor(i);
    }

    public int getTextColor() {
        return this.tv_main.getCurrentTextColor();
    }

    public String getText() {
        FontFitTextView fontFitTextView = this.tv_main;
        if (fontFitTextView != null) {
            return fontFitTextView.getText().toString();
        }
        return null;
    }

    public void setText(String str) {
        FontFitTextView fontFitTextView = this.tv_main;
        if (fontFitTextView != null) {
            fontFitTextView.setText(str);
        }
    }

    /* access modifiers changed from: protected */
    public void onScaling(boolean z) {
        super.onScaling(z);
    }

    public void setFont(String str) {
        this.tv_main.setTypeface(Typeface.create(str, 0));
    }

    public void setDefaultFont() {
        this.tv_main.setTypeface(Typeface.SANS_SERIF);
    }

    public int getTextBackgroundColor() {
        return ((ColorDrawable) this.tv_main.getBackground()).getColor();
    }

    public void setTextBackgroundColor(int i) {
        this.tv_main.setBackgroundColor(i);
    }
}
