package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

class FloatValueAnimatorBuilder {
    final ValueAnimator animator;
    EndListener endListener;

    interface EndListener {
        void onEnd();
    }

    interface UpdateListener {
        void onUpdate(float f);
    }

    protected FloatValueAnimatorBuilder() {
        this(false);
    }

    protected FloatValueAnimatorBuilder(boolean z) {
        if (z) {
            this.animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        } else {
            this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        }
    }

    public FloatValueAnimatorBuilder delayBy(long j) {
        this.animator.setStartDelay(j);
        return this;
    }

    public FloatValueAnimatorBuilder duration(long j) {
        this.animator.setDuration(j);
        return this;
    }

    public FloatValueAnimatorBuilder interpolator(TimeInterpolator timeInterpolator) {
        this.animator.setInterpolator(timeInterpolator);
        return this;
    }

    public FloatValueAnimatorBuilder repeat(int i) {
        this.animator.setRepeatCount(i);
        return this;
    }

    public FloatValueAnimatorBuilder onUpdate(final UpdateListener updateListener) {
        this.animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateListener.onUpdate(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        return this;
    }

    public FloatValueAnimatorBuilder onEnd(EndListener endListener2) {
        this.endListener = endListener2;
        return this;
    }

    public ValueAnimator build() {
        if (this.endListener != null) {
            this.animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    FloatValueAnimatorBuilder.this.endListener.onEnd();
                }
            });
        }
        return this.animator;
    }
}
