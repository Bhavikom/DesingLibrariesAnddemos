package com.yasoka.eazyscreenrecord.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;

public class PulsatorLayout extends RelativeLayout {
    private final ImageView imageView;
    private final PulseView pulseView;

    private class PulseView extends View {
        public PulseView(Context context) {
            super(context);
        }
    }

    public PulsatorLayout(Context context) {
        this(context, null, 0);
    }

    public PulsatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PulsatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.pulseView = new PulseView(getContext());
        addView(this.pulseView, layoutParams);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.setMargins(10, 10, 10, 10);
        this.imageView = new ImageView(getContext());
        addView(this.imageView, layoutParams2);
    }

    public void startAnimation() {
        this.pulseView.setBackgroundResource(R.drawable.circle_color_white_back);
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
        loadAnimation.setDuration(800);
        this.pulseView.setAnimation(loadAnimation);
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public void stopAnimation() {
        this.pulseView.setBackgroundColor(0);
    }
}
