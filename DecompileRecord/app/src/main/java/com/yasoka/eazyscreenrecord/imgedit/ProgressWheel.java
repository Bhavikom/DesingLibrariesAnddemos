package com.yasoka.eazyscreenrecord.imgedit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import com.ezscreenrecorder.C0793R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class ProgressWheel extends View {
    private static final String TAG = "ProgressWheel";
    private int barColor = -1442840576;
    private float barExtraLength = 0.0f;
    private boolean barGrowingFromFront = true;
    private final int barLength = 16;
    private final int barMaxLength = 270;
    private Paint barPaint = new Paint();
    private double barSpinCycleTime = 460.0d;
    private int barWidth = 4;
    private ProgressCallback callback;
    private RectF circleBounds = new RectF();
    private int circleRadius = 28;
    private boolean fillRadius = false;
    private boolean isSpinning = false;
    private long lastTimeAnimated = 0;
    private boolean linearProgress;
    private float mProgress = 0.0f;
    private float mTargetProgress = 0.0f;
    private final long pauseGrowingTime = 200;
    private long pausedTimeWithoutGrowing = 0;
    private int rimColor = ViewCompat.MEASURED_SIZE_MASK;
    private Paint rimPaint = new Paint();
    private int rimWidth = 4;
    private boolean shouldAnimate;
    private float spinSpeed = 230.0f;
    private double timeStartGrowing = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;

    public interface ProgressCallback {
        void onProgressUpdate(float f);
    }

    static class WheelSavedState extends BaseSavedState {
        public static final Creator<WheelSavedState> CREATOR = new Creator<WheelSavedState>() {
            public WheelSavedState createFromParcel(Parcel parcel) {
                return new WheelSavedState(parcel);
            }

            public WheelSavedState[] newArray(int i) {
                return new WheelSavedState[i];
            }
        };
        int barColor;
        int barWidth;
        int circleRadius;
        boolean fillRadius;
        boolean isSpinning;
        boolean linearProgress;
        float mProgress;
        float mTargetProgress;
        int rimColor;
        int rimWidth;
        float spinSpeed;

        WheelSavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private WheelSavedState(Parcel parcel) {
            super(parcel);
            this.mProgress = parcel.readFloat();
            this.mTargetProgress = parcel.readFloat();
            boolean z = true;
            this.isSpinning = parcel.readByte() != 0;
            this.spinSpeed = parcel.readFloat();
            this.barWidth = parcel.readInt();
            this.barColor = parcel.readInt();
            this.rimWidth = parcel.readInt();
            this.rimColor = parcel.readInt();
            this.circleRadius = parcel.readInt();
            this.linearProgress = parcel.readByte() != 0;
            if (parcel.readByte() == 0) {
                z = false;
            }
            this.fillRadius = z;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.mProgress);
            parcel.writeFloat(this.mTargetProgress);
            parcel.writeByte(this.isSpinning ? (byte) 1 : 0);
            parcel.writeFloat(this.spinSpeed);
            parcel.writeInt(this.barWidth);
            parcel.writeInt(this.barColor);
            parcel.writeInt(this.rimWidth);
            parcel.writeInt(this.rimColor);
            parcel.writeInt(this.circleRadius);
            parcel.writeByte(this.linearProgress ? (byte) 1 : 0);
            parcel.writeByte(this.fillRadius ? (byte) 1 : 0);
        }
    }

    public ProgressWheel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        parseAttributes(context.obtainStyledAttributes(attributeSet, C0793R.styleable.ProgressWheel));
        setAnimationEnabled();
    }

    public ProgressWheel(Context context) {
        super(context);
        setAnimationEnabled();
    }

    @TargetApi(17)
    private void setAnimationEnabled() {
        float f;
        String str = "animator_duration_scale";
        if (VERSION.SDK_INT >= 17) {
            f = Global.getFloat(getContext().getContentResolver(), str, 1.0f);
        } else {
            f = System.getFloat(getContext().getContentResolver(), str, 1.0f);
        }
        this.shouldAnimate = f != 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int paddingLeft = this.circleRadius + getPaddingLeft() + getPaddingRight();
        int paddingTop = this.circleRadius + getPaddingTop() + getPaddingBottom();
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            paddingLeft = size;
        } else if (mode == Integer.MIN_VALUE) {
            paddingLeft = Math.min(paddingLeft, size);
        }
        if (mode2 == 1073741824 || mode == 1073741824) {
            paddingTop = size2;
        } else if (mode2 == Integer.MIN_VALUE) {
            paddingTop = Math.min(paddingTop, size2);
        }
        setMeasuredDimension(paddingLeft, paddingTop);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setupBounds(i, i2);
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Style.STROKE);
        this.barPaint.setStrokeWidth((float) this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Style.STROKE);
        this.rimPaint.setStrokeWidth((float) this.rimWidth);
    }

    private void setupBounds(int i, int i2) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (!this.fillRadius) {
            int i3 = (i - paddingLeft) - paddingRight;
            int min = Math.min(Math.min(i3, (i2 - paddingBottom) - paddingTop), (this.circleRadius * 2) - (this.barWidth * 2));
            int i4 = ((i3 - min) / 2) + paddingLeft;
            int i5 = ((((i2 - paddingTop) - paddingBottom) - min) / 2) + paddingTop;
            int i6 = this.barWidth;
            this.circleBounds = new RectF((float) (i4 + i6), (float) (i5 + i6), (float) ((i4 + min) - i6), (float) ((i5 + min) - i6));
            return;
        }
        int i7 = this.barWidth;
        this.circleBounds = new RectF((float) (paddingLeft + i7), (float) (paddingTop + i7), (float) ((i - paddingRight) - i7), (float) ((i2 - paddingBottom) - i7));
    }

    private void parseAttributes(TypedArray typedArray) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        this.barWidth = (int) TypedValue.applyDimension(1, (float) this.barWidth, displayMetrics);
        this.rimWidth = (int) TypedValue.applyDimension(1, (float) this.rimWidth, displayMetrics);
        this.circleRadius = (int) TypedValue.applyDimension(1, (float) this.circleRadius, displayMetrics);
        this.circleRadius = (int) typedArray.getDimension(3, (float) this.circleRadius);
        this.fillRadius = typedArray.getBoolean(4, false);
        this.barWidth = (int) typedArray.getDimension(2, (float) this.barWidth);
        this.rimWidth = (int) typedArray.getDimension(8, (float) this.rimWidth);
        this.spinSpeed = typedArray.getFloat(9, this.spinSpeed / 360.0f) * 360.0f;
        this.barSpinCycleTime = (double) typedArray.getInt(1, (int) this.barSpinCycleTime);
        this.barColor = typedArray.getColor(0, this.barColor);
        this.rimColor = typedArray.getColor(7, this.rimColor);
        this.linearProgress = typedArray.getBoolean(5, false);
        if (typedArray.getBoolean(6, false)) {
            spin();
        }
        typedArray.recycle();
    }

    public void setCallback(ProgressCallback progressCallback) {
        this.callback = progressCallback;
        if (!this.isSpinning) {
            runCallback();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.rimPaint);
        if (this.shouldAnimate) {
            boolean z = true;
            if (this.isSpinning) {
                long uptimeMillis = SystemClock.uptimeMillis() - this.lastTimeAnimated;
                float f5 = (((float) uptimeMillis) * this.spinSpeed) / 1000.0f;
                updateBarLength(uptimeMillis);
                this.mProgress += f5;
                float f6 = this.mProgress;
                if (f6 > 360.0f) {
                    this.mProgress = f6 - 360.0f;
                    runCallback(-1.0f);
                }
                this.lastTimeAnimated = SystemClock.uptimeMillis();
                float f7 = this.mProgress - 90.0f;
                float f8 = this.barExtraLength + 16.0f;
                if (isInEditMode()) {
                    f4 = 0.0f;
                    f3 = 135.0f;
                } else {
                    f4 = f7;
                    f3 = f8;
                }
                canvas.drawArc(this.circleBounds, f4, f3, false, this.barPaint);
            } else {
                float f9 = this.mProgress;
                if (f9 != this.mTargetProgress) {
                    this.mProgress = Math.min(this.mProgress + ((((float) (SystemClock.uptimeMillis() - this.lastTimeAnimated)) / 1000.0f) * this.spinSpeed), this.mTargetProgress);
                    this.lastTimeAnimated = SystemClock.uptimeMillis();
                } else {
                    z = false;
                }
                if (f9 != this.mProgress) {
                    runCallback();
                }
                float f10 = this.mProgress;
                if (!this.linearProgress) {
                    f2 = ((float) (1.0d - Math.pow((double) (1.0f - (f10 / 360.0f)), (double) 4.0f))) * 360.0f;
                    f = ((float) (1.0d - Math.pow((double) (1.0f - (this.mProgress / 360.0f)), (double) 2.0f))) * 360.0f;
                } else {
                    f = f10;
                    f2 = 0.0f;
                }
                canvas.drawArc(this.circleBounds, f2 - 90.0f, isInEditMode() ? 360.0f : f, false, this.barPaint);
            }
            if (z) {
                invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            this.lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    private void updateBarLength(long j) {
        long j2 = this.pausedTimeWithoutGrowing;
        if (j2 >= 200) {
            this.timeStartGrowing += (double) j;
            double d = this.timeStartGrowing;
            double d2 = this.barSpinCycleTime;
            if (d > d2) {
                this.timeStartGrowing = d - d2;
                this.pausedTimeWithoutGrowing = 0;
                this.barGrowingFromFront = !this.barGrowingFromFront;
            }
            float cos = (((float) Math.cos(((this.timeStartGrowing / this.barSpinCycleTime) + 1.0d) * 3.141592653589793d)) / 2.0f) + 0.5f;
            if (this.barGrowingFromFront) {
                this.barExtraLength = cos * 254.0f;
                return;
            }
            float f = (1.0f - cos) * 254.0f;
            this.mProgress += this.barExtraLength - f;
            this.barExtraLength = f;
            return;
        }
        this.pausedTimeWithoutGrowing = j2 + j;
    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.mProgress = 0.0f;
        this.mTargetProgress = 0.0f;
        invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.mProgress = 0.0f;
        this.mTargetProgress = 0.0f;
        invalidate();
    }

    public void spin() {
        this.lastTimeAnimated = SystemClock.uptimeMillis();
        this.isSpinning = true;
        invalidate();
    }

    private void runCallback(float f) {
        ProgressCallback progressCallback = this.callback;
        if (progressCallback != null) {
            progressCallback.onProgressUpdate(f);
        }
    }

    private void runCallback() {
        if (this.callback != null) {
            this.callback.onProgressUpdate(((float) Math.round((this.mProgress * 100.0f) / 360.0f)) / 100.0f);
        }
    }

    public void setInstantProgress(float f) {
        if (this.isSpinning) {
            this.mProgress = 0.0f;
            this.isSpinning = false;
        }
        if (f > 1.0f) {
            f -= 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        if (f != this.mTargetProgress) {
            this.mTargetProgress = Math.min(f * 360.0f, 360.0f);
            this.mProgress = this.mTargetProgress;
            this.lastTimeAnimated = SystemClock.uptimeMillis();
            invalidate();
        }
    }

    public Parcelable onSaveInstanceState() {
        WheelSavedState wheelSavedState = new WheelSavedState(super.onSaveInstanceState());
        wheelSavedState.mProgress = this.mProgress;
        wheelSavedState.mTargetProgress = this.mTargetProgress;
        wheelSavedState.isSpinning = this.isSpinning;
        wheelSavedState.spinSpeed = this.spinSpeed;
        wheelSavedState.barWidth = this.barWidth;
        wheelSavedState.barColor = this.barColor;
        wheelSavedState.rimWidth = this.rimWidth;
        wheelSavedState.rimColor = this.rimColor;
        wheelSavedState.circleRadius = this.circleRadius;
        wheelSavedState.linearProgress = this.linearProgress;
        wheelSavedState.fillRadius = this.fillRadius;
        return wheelSavedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof WheelSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        WheelSavedState wheelSavedState = (WheelSavedState) parcelable;
        super.onRestoreInstanceState(wheelSavedState.getSuperState());
        this.mProgress = wheelSavedState.mProgress;
        this.mTargetProgress = wheelSavedState.mTargetProgress;
        this.isSpinning = wheelSavedState.isSpinning;
        this.spinSpeed = wheelSavedState.spinSpeed;
        this.barWidth = wheelSavedState.barWidth;
        this.barColor = wheelSavedState.barColor;
        this.rimWidth = wheelSavedState.rimWidth;
        this.rimColor = wheelSavedState.rimColor;
        this.circleRadius = wheelSavedState.circleRadius;
        this.linearProgress = wheelSavedState.linearProgress;
        this.fillRadius = wheelSavedState.fillRadius;
        this.lastTimeAnimated = SystemClock.uptimeMillis();
    }

    public float getProgress() {
        if (this.isSpinning) {
            return -1.0f;
        }
        return this.mProgress / 360.0f;
    }

    public void setProgress(float f) {
        if (this.isSpinning) {
            this.mProgress = 0.0f;
            this.isSpinning = false;
            runCallback();
        }
        if (f > 1.0f) {
            f -= 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        float f2 = this.mTargetProgress;
        if (f != f2) {
            if (this.mProgress == f2) {
                this.lastTimeAnimated = SystemClock.uptimeMillis();
            }
            this.mTargetProgress = Math.min(f * 360.0f, 360.0f);
            invalidate();
        }
    }

    public void setLinearProgress(boolean z) {
        this.linearProgress = z;
        if (!this.isSpinning) {
            invalidate();
        }
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
        if (!this.isSpinning) {
            invalidate();
        }
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int i) {
        this.barWidth = i;
        if (!this.isSpinning) {
            invalidate();
        }
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int i) {
        this.barColor = i;
        setupPaints();
        if (!this.isSpinning) {
            invalidate();
        }
    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int i) {
        this.rimColor = i;
        setupPaints();
        if (!this.isSpinning) {
            invalidate();
        }
    }

    public float getSpinSpeed() {
        return this.spinSpeed / 360.0f;
    }

    public void setSpinSpeed(float f) {
        this.spinSpeed = f * 360.0f;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int i) {
        this.rimWidth = i;
        if (!this.isSpinning) {
            invalidate();
        }
    }
}
