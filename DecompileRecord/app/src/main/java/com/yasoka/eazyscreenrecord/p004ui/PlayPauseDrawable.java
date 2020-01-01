package com.yasoka.eazyscreenrecord.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.p000v4.app.NotificationCompat;
import android.util.Property;

/* renamed from: com.ezscreenrecorder.ui.PlayPauseDrawable */
public class PlayPauseDrawable extends Drawable {
    private static final Property<PlayPauseDrawable, Float> PROGRESS = new Property<PlayPauseDrawable, Float>(Float.class, NotificationCompat.CATEGORY_PROGRESS) {
        public Float get(PlayPauseDrawable playPauseDrawable) {
            return Float.valueOf(playPauseDrawable.getProgress());
        }

        public void set(PlayPauseDrawable playPauseDrawable, Float f) {
            playPauseDrawable.setProgress(f.floatValue());
        }
    };
    private final RectF mBounds = new RectF();
    private float mHeight;
    /* access modifiers changed from: private */
    public boolean mIsPlay = true;
    private final Path mLeftPauseBar = new Path();
    private final Paint mPaint = new Paint();
    private float mPauseBarDistance;
    private float mPauseBarHeight;
    private float mPauseBarWidth;
    private float mProgress = 1.0f;
    private final Path mRightPauseBar = new Path();
    private float mWidth;

    private static float lerp(float f, float f2, float f3) {
        return f + ((f2 - f) * f3);
    }

    public int getOpacity() {
        return -3;
    }

    public PlayPauseDrawable(@ColorInt int i) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(i);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mBounds.set(rect);
        this.mWidth = this.mBounds.width();
        this.mHeight = this.mBounds.height();
        this.mPauseBarHeight = this.mHeight / 2.5f;
        this.mPauseBarWidth = this.mPauseBarHeight / 2.5f;
        this.mPauseBarDistance = this.mPauseBarWidth / 1.5f;
    }

    public void draw(@NonNull Canvas canvas) {
        this.mLeftPauseBar.rewind();
        this.mRightPauseBar.rewind();
        float f = 0.0f;
        float lerp = lerp(this.mPauseBarDistance, 0.0f, this.mProgress) - 1.0f;
        float lerp2 = lerp(this.mPauseBarWidth, this.mPauseBarHeight / 2.0f, this.mProgress);
        float lerp3 = lerp(0.0f, lerp2, this.mProgress);
        float f2 = (lerp2 * 2.0f) + lerp;
        float f3 = lerp + lerp2;
        float lerp4 = lerp(f2, f3, this.mProgress);
        this.mLeftPauseBar.moveTo(0.0f, 0.0f);
        this.mLeftPauseBar.lineTo(lerp3, -this.mPauseBarHeight);
        this.mLeftPauseBar.lineTo(lerp2, -this.mPauseBarHeight);
        this.mLeftPauseBar.lineTo(lerp2, 0.0f);
        this.mLeftPauseBar.close();
        this.mRightPauseBar.moveTo(f3, 0.0f);
        this.mRightPauseBar.lineTo(f3, -this.mPauseBarHeight);
        this.mRightPauseBar.lineTo(lerp4, -this.mPauseBarHeight);
        this.mRightPauseBar.lineTo(f2, 0.0f);
        this.mRightPauseBar.close();
        canvas.save();
        canvas.translate(lerp(0.0f, this.mPauseBarHeight / 8.0f, this.mProgress), 0.0f);
        float f4 = this.mIsPlay ? 1.0f - this.mProgress : this.mProgress;
        if (this.mIsPlay) {
            f = 90.0f;
        }
        canvas.rotate(lerp(f, 90.0f + f, f4), this.mWidth / 2.0f, this.mHeight / 2.0f);
        canvas.translate((this.mWidth / 2.0f) - (f2 / 2.0f), (this.mHeight / 2.0f) + (this.mPauseBarHeight / 2.0f));
        canvas.drawPath(this.mLeftPauseBar, this.mPaint);
        canvas.drawPath(this.mRightPauseBar, this.mPaint);
        canvas.restore();
    }

    public void setPlay() {
        this.mIsPlay = true;
        this.mProgress = 1.0f;
    }

    public void setPause() {
        this.mIsPlay = false;
        this.mProgress = 0.0f;
    }

    public Animator getPausePlayAnimator() {
        Property<PlayPauseDrawable, Float> property = PROGRESS;
        float[] fArr = new float[2];
        float f = 1.0f;
        fArr[0] = this.mIsPlay ? 1.0f : 0.0f;
        if (this.mIsPlay) {
            f = 0.0f;
        }
        fArr[1] = f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, property, fArr);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                PlayPauseDrawable playPauseDrawable = PlayPauseDrawable.this;
                playPauseDrawable.mIsPlay = !playPauseDrawable.mIsPlay;
            }
        });
        return ofFloat;
    }

    public boolean isPlay() {
        return this.mIsPlay;
    }

    /* access modifiers changed from: private */
    public void setProgress(float f) {
        this.mProgress = f;
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public float getProgress() {
        return this.mProgress;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }
}
