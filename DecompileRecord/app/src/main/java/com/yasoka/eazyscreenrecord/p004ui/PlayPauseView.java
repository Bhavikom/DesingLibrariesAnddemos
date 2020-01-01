package com.yasoka.eazyscreenrecord.p004ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.google.common.primitives.Ints;

/* renamed from: com.ezscreenrecorder.ui.PlayPauseView */
public class PlayPauseView extends FrameLayout {
    private static final Property<PlayPauseView, Integer> COLOR = new Property<PlayPauseView, Integer>(Integer.class, "color") {
        public Integer get(PlayPauseView playPauseView) {
            return Integer.valueOf(playPauseView.getColor());
        }

        public void set(PlayPauseView playPauseView, Integer num) {
            playPauseView.setColor(num.intValue());
        }
    };
    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200;
    private AnimatorSet mAnimatorSet;
    private int mBackgroundColor;
    private final PlayPauseDrawable mDrawable;
    private int mHeight;
    private final Paint mPaint;
    private final int mPauseBackgroundColor;
    private final int mPlayBackgroundColor;
    private int mWidth;

    /* renamed from: com.ezscreenrecorder.ui.PlayPauseView$SavedState */
    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean isPlay;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.isPlay = parcel.readByte() > 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.isPlay ? (byte) 1 : 0);
        }
    }

    public PlayPauseView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mPlayBackgroundColor = -16776961;
        this.mPauseBackgroundColor = -16711681;
        this.mDrawable = new PlayPauseDrawable(-1);
        init(context);
    }

    /* JADX INFO: finally extract failed */
    public PlayPauseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint = new Paint();
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.PlayPauseView, 0, 0);
        try {
            this.mPlayBackgroundColor = obtainStyledAttributes.getColor(2, -16776961);
            this.mPauseBackgroundColor = obtainStyledAttributes.getColor(1, -16711681);
            int color = obtainStyledAttributes.getColor(0, -1);
            obtainStyledAttributes.recycle();
            this.mDrawable = new PlayPauseDrawable(color);
            init(context);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void init(Context context) {
        setWillNotDraw(false);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        this.mDrawable.setCallback(this);
        this.mBackgroundColor = this.mPlayBackgroundColor;
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isPlay = this.mDrawable.isPlay();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        initStatus(savedState.isPlay);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int min = Math.min(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(min, Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(min, Ints.MAX_POWER_OF_TWO));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mDrawable.setBounds(0, 0, i, i2);
        this.mWidth = i;
        this.mHeight = i2;
        if (VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
            setClipToOutline(true);
        }
    }

    public void setColor(int i) {
        this.mBackgroundColor = i;
        invalidate();
    }

    public int getColor() {
        return this.mBackgroundColor;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mDrawable || super.verifyDrawable(drawable);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.mBackgroundColor);
        canvas.drawCircle(((float) this.mWidth) / 2.0f, ((float) this.mHeight) / 2.0f, ((float) Math.min(this.mWidth, this.mHeight)) / 2.0f, this.mPaint);
        this.mDrawable.draw(canvas);
    }

    public void toggle() {
        toggle(true);
    }

    public void change(boolean z) {
        change(z, true);
    }

    public void change(boolean z, boolean z2) {
        if (this.mDrawable.isPlay() != z) {
            toggle(z2);
        }
    }

    public void toggle(boolean z) {
        if (z) {
            AnimatorSet animatorSet = this.mAnimatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mAnimatorSet = new AnimatorSet();
            boolean isPlay = this.mDrawable.isPlay();
            Property<PlayPauseView, Integer> property = COLOR;
            int[] iArr = new int[1];
            iArr[0] = isPlay ? this.mPauseBackgroundColor : this.mPlayBackgroundColor;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, property, iArr);
            ofInt.setEvaluator(new ArgbEvaluator());
            Animator pausePlayAnimator = this.mDrawable.getPausePlayAnimator();
            this.mAnimatorSet.setInterpolator(new DecelerateInterpolator());
            this.mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
            this.mAnimatorSet.playTogether(new Animator[]{ofInt, pausePlayAnimator});
            this.mAnimatorSet.start();
            return;
        }
        initStatus(!this.mDrawable.isPlay());
        invalidate();
    }

    private void initStatus(boolean z) {
        if (z) {
            this.mDrawable.setPlay();
            setColor(this.mPlayBackgroundColor);
            return;
        }
        this.mDrawable.setPause();
        setColor(this.mPauseBackgroundColor);
    }

    public boolean isPlay() {
        return this.mDrawable.isPlay();
    }
}
