package com.yasoka.eazyscreenrecord.p004ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;

/* renamed from: com.ezscreenrecorder.ui.CircularSeekBar */
public class CircularSeekBar extends View {
    protected static final int DEFAULT_CIRCLE_COLOR = -12303292;
    protected static final int DEFAULT_CIRCLE_FILL_COLOR = 0;
    protected static final int DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255);
    protected static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5.0f;
    protected static final float DEFAULT_CIRCLE_X_RADIUS = 30.0f;
    protected static final float DEFAULT_CIRCLE_Y_RADIUS = 30.0f;
    protected static final float DEFAULT_END_ANGLE = 270.0f;
    protected static final boolean DEFAULT_LOCK_ENABLED = true;
    protected static final boolean DEFAULT_MAINTAIN_EQUAL_CIRCLE = true;
    protected static final int DEFAULT_MAX = 100;
    protected static final boolean DEFAULT_MOVE_OUTSIDE_CIRCLE = false;
    protected static final int DEFAULT_POINTER_ALPHA = 135;
    protected static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
    protected static final int DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
    protected static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2.0f;
    protected static final int DEFAULT_POINTER_HALO_COLOR = Color.argb(DEFAULT_POINTER_ALPHA, 74, 138, 255);
    protected static final int DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(DEFAULT_POINTER_ALPHA, 74, 138, 255);
    protected static final float DEFAULT_POINTER_HALO_WIDTH = 6.0f;
    protected static final float DEFAULT_POINTER_RADIUS = 7.0f;
    protected static final int DEFAULT_PROGRESS = 0;
    protected static final float DEFAULT_START_ANGLE = 270.0f;
    protected static final boolean DEFAULT_USE_CUSTOM_RADII = false;
    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;
    protected final float MIN_TOUCH_TARGET_DP = 48.0f;
    protected float ccwDistanceFromEnd;
    protected float ccwDistanceFromPointer;
    protected float ccwDistanceFromStart;
    protected float cwDistanceFromEnd;
    protected float cwDistanceFromPointer;
    protected float cwDistanceFromStart;
    protected boolean isTouchEnabled = true;
    protected float lastCWDistanceFromStart;
    protected boolean lockAtEnd = false;
    protected boolean lockAtStart = true;
    protected boolean lockEnabled = true;
    protected int mCircleColor = DEFAULT_CIRCLE_COLOR;
    protected int mCircleFillColor = 0;
    protected Paint mCircleFillPaint;
    protected float mCircleHeight;
    protected Paint mCirclePaint;
    protected Path mCirclePath;
    protected int mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;
    protected Paint mCircleProgressGlowPaint;
    protected Paint mCircleProgressPaint;
    protected Path mCircleProgressPath;
    protected RectF mCircleRectF = new RectF();
    protected float mCircleStrokeWidth;
    protected float mCircleWidth;
    protected float mCircleXRadius;
    protected float mCircleYRadius;
    protected boolean mCustomRadii;
    protected float mEndAngle;
    protected boolean mIsMovingCW;
    protected boolean mMaintainEqualCircle;
    protected int mMax;
    protected boolean mMoveOutsideCircle;
    protected OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;
    protected int mPointerAlpha = DEFAULT_POINTER_ALPHA;
    protected int mPointerAlphaOnTouch = 100;
    protected int mPointerColor = DEFAULT_POINTER_COLOR;
    protected Paint mPointerHaloBorderPaint;
    protected float mPointerHaloBorderWidth;
    protected int mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;
    protected int mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
    protected Paint mPointerHaloPaint;
    protected float mPointerHaloWidth;
    protected Paint mPointerPaint;
    protected float mPointerPosition;
    protected float[] mPointerPositionXY = new float[2];
    protected float mPointerRadius;
    protected int mProgress;
    protected float mProgressDegrees;
    protected float mStartAngle;
    protected float mTotalCircleDegrees;
    protected boolean mUserIsMovingPointer = false;

    /* renamed from: com.ezscreenrecorder.ui.CircularSeekBar$OnCircularSeekBarChangeListener */
    public interface OnCircularSeekBarChangeListener {
        void onProgressChanged(CircularSeekBar circularSeekBar, int i, boolean z);

        void onStartTrackingTouch(CircularSeekBar circularSeekBar);

        void onStopTrackingTouch(CircularSeekBar circularSeekBar);
    }

    /* access modifiers changed from: protected */
    public void initAttributes(TypedArray typedArray) {
        this.mCircleXRadius = typedArray.getDimension(4, this.DPTOPX_SCALE * 30.0f);
        this.mCircleYRadius = typedArray.getDimension(5, this.DPTOPX_SCALE * 30.0f);
        this.mPointerRadius = typedArray.getDimension(17, this.DPTOPX_SCALE * DEFAULT_POINTER_RADIUS);
        this.mPointerHaloWidth = typedArray.getDimension(16, this.DPTOPX_SCALE * DEFAULT_POINTER_HALO_WIDTH);
        this.mPointerHaloBorderWidth = typedArray.getDimension(13, this.DPTOPX_SCALE * DEFAULT_POINTER_HALO_BORDER_WIDTH);
        this.mCircleStrokeWidth = typedArray.getDimension(3, this.DPTOPX_SCALE * DEFAULT_CIRCLE_STROKE_WIDTH);
        this.mPointerColor = typedArray.getColor(12, DEFAULT_POINTER_COLOR);
        this.mPointerHaloColor = typedArray.getColor(14, DEFAULT_POINTER_HALO_COLOR);
        this.mPointerHaloColorOnTouch = typedArray.getColor(15, DEFAULT_POINTER_HALO_COLOR_ONTOUCH);
        this.mCircleColor = typedArray.getColor(0, DEFAULT_CIRCLE_COLOR);
        this.mCircleProgressColor = typedArray.getColor(2, DEFAULT_CIRCLE_PROGRESS_COLOR);
        this.mCircleFillColor = typedArray.getColor(1, 0);
        this.mPointerAlpha = Color.alpha(this.mPointerHaloColor);
        this.mPointerAlphaOnTouch = typedArray.getInt(11, 100);
        int i = this.mPointerAlphaOnTouch;
        if (i > 255 || i < 0) {
            this.mPointerAlphaOnTouch = 100;
        }
        this.mMax = typedArray.getInt(9, 100);
        this.mProgress = typedArray.getInt(18, 0);
        this.mCustomRadii = typedArray.getBoolean(20, false);
        this.mMaintainEqualCircle = typedArray.getBoolean(8, true);
        this.mMoveOutsideCircle = typedArray.getBoolean(10, false);
        this.lockEnabled = typedArray.getBoolean(7, true);
        this.mStartAngle = ((typedArray.getFloat(19, 270.0f) % 360.0f) + 360.0f) % 360.0f;
        this.mEndAngle = ((typedArray.getFloat(6, 270.0f) % 360.0f) + 360.0f) % 360.0f;
        float f = this.mStartAngle;
        float f2 = this.mEndAngle;
        if (f == f2) {
            this.mEndAngle = f2 - 0.1f;
        }
    }

    /* access modifiers changed from: protected */
    public void initPaints() {
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setDither(true);
        this.mCirclePaint.setColor(this.mCircleColor);
        this.mCirclePaint.setStrokeWidth(this.mCircleStrokeWidth);
        this.mCirclePaint.setStyle(Style.STROKE);
        this.mCirclePaint.setStrokeJoin(Join.ROUND);
        this.mCirclePaint.setStrokeCap(Cap.ROUND);
        this.mCircleFillPaint = new Paint();
        this.mCircleFillPaint.setAntiAlias(true);
        this.mCircleFillPaint.setDither(true);
        this.mCircleFillPaint.setColor(this.mCircleFillColor);
        this.mCircleFillPaint.setStyle(Style.FILL);
        this.mCircleProgressPaint = new Paint();
        this.mCircleProgressPaint.setAntiAlias(true);
        this.mCircleProgressPaint.setDither(true);
        this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
        this.mCircleProgressPaint.setStrokeWidth(this.mCircleStrokeWidth);
        this.mCircleProgressPaint.setStyle(Style.STROKE);
        this.mCircleProgressPaint.setStrokeJoin(Join.ROUND);
        this.mCircleProgressPaint.setStrokeCap(Cap.ROUND);
        this.mCircleProgressGlowPaint = new Paint();
        this.mCircleProgressGlowPaint.set(this.mCircleProgressPaint);
        this.mCircleProgressGlowPaint.setMaskFilter(new BlurMaskFilter(this.DPTOPX_SCALE * DEFAULT_CIRCLE_STROKE_WIDTH, Blur.NORMAL));
        this.mPointerPaint = new Paint();
        this.mPointerPaint.setAntiAlias(true);
        this.mPointerPaint.setDither(true);
        this.mPointerPaint.setStyle(Style.FILL);
        this.mPointerPaint.setColor(this.mPointerColor);
        this.mPointerPaint.setStrokeWidth(this.mPointerRadius);
        this.mPointerHaloPaint = new Paint();
        this.mPointerHaloPaint.set(this.mPointerPaint);
        this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
        this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
        this.mPointerHaloPaint.setStrokeWidth(this.mPointerRadius + this.mPointerHaloWidth);
        this.mPointerHaloBorderPaint = new Paint();
        this.mPointerHaloBorderPaint.set(this.mPointerPaint);
        this.mPointerHaloBorderPaint.setStrokeWidth(this.mPointerHaloBorderWidth);
        this.mPointerHaloBorderPaint.setStyle(Style.STROKE);
    }

    /* access modifiers changed from: protected */
    public void calculateTotalDegrees() {
        this.mTotalCircleDegrees = (360.0f - (this.mStartAngle - this.mEndAngle)) % 360.0f;
        if (this.mTotalCircleDegrees <= 0.0f) {
            this.mTotalCircleDegrees = 360.0f;
        }
    }

    /* access modifiers changed from: protected */
    public void calculateProgressDegrees() {
        this.mProgressDegrees = this.mPointerPosition - this.mStartAngle;
        float f = this.mProgressDegrees;
        if (f < 0.0f) {
            f += 360.0f;
        }
        this.mProgressDegrees = f;
    }

    /* access modifiers changed from: protected */
    public void calculatePointerAngle() {
        this.mPointerPosition = ((((float) this.mProgress) / ((float) this.mMax)) * this.mTotalCircleDegrees) + this.mStartAngle;
        this.mPointerPosition %= 360.0f;
    }

    /* access modifiers changed from: protected */
    public void calculatePointerXYPosition() {
        PathMeasure pathMeasure = new PathMeasure(this.mCircleProgressPath, false);
        if (!pathMeasure.getPosTan(pathMeasure.getLength(), this.mPointerPositionXY, null)) {
            new PathMeasure(this.mCirclePath, false).getPosTan(0.0f, this.mPointerPositionXY, null);
        }
    }

    /* access modifiers changed from: protected */
    public void initPaths() {
        this.mCirclePath = new Path();
        this.mCirclePath.addArc(this.mCircleRectF, this.mStartAngle, this.mTotalCircleDegrees);
        this.mCircleProgressPath = new Path();
        this.mCircleProgressPath.addArc(this.mCircleRectF, this.mStartAngle, this.mProgressDegrees);
    }

    /* access modifiers changed from: protected */
    public void initRects() {
        RectF rectF = this.mCircleRectF;
        float f = this.mCircleWidth;
        float f2 = -f;
        float f3 = this.mCircleHeight;
        rectF.set(f2, -f3, f, f3);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float) (getWidth() / 2), (float) (getHeight() / 2));
        canvas.drawPath(this.mCirclePath, this.mCirclePaint);
        canvas.drawPath(this.mCircleProgressPath, this.mCircleProgressGlowPaint);
        canvas.drawPath(this.mCircleProgressPath, this.mCircleProgressPaint);
        canvas.drawPath(this.mCirclePath, this.mCircleFillPaint);
        float[] fArr = this.mPointerPositionXY;
        canvas.drawCircle(fArr[0], fArr[1], this.mPointerRadius + this.mPointerHaloWidth, this.mPointerHaloPaint);
        float[] fArr2 = this.mPointerPositionXY;
        canvas.drawCircle(fArr2[0], fArr2[1], this.mPointerRadius, this.mPointerPaint);
        if (this.mUserIsMovingPointer) {
            float[] fArr3 = this.mPointerPositionXY;
            canvas.drawCircle(fArr3[0], fArr3[1], this.mPointerRadius + this.mPointerHaloWidth + (this.mPointerHaloBorderWidth / DEFAULT_POINTER_HALO_BORDER_WIDTH), this.mPointerHaloBorderPaint);
        }
    }

    public int getProgress() {
        return Math.round((((float) this.mMax) * this.mProgressDegrees) / this.mTotalCircleDegrees);
    }

    public void setProgress(int i) {
        if (this.mProgress != i) {
            this.mProgress = i;
            OnCircularSeekBarChangeListener onCircularSeekBarChangeListener = this.mOnCircularSeekBarChangeListener;
            if (onCircularSeekBarChangeListener != null) {
                onCircularSeekBarChangeListener.onProgressChanged(this, i, false);
            }
            recalculateAll();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void setProgressBasedOnAngle(float f) {
        this.mPointerPosition = f;
        calculateProgressDegrees();
        this.mProgress = Math.round((((float) this.mMax) * this.mProgressDegrees) / this.mTotalCircleDegrees);
    }

    /* access modifiers changed from: protected */
    public void recalculateAll() {
        calculateTotalDegrees();
        calculatePointerAngle();
        calculateProgressDegrees();
        initRects();
        initPaths();
        calculatePointerXYPosition();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(getSuggestedMinimumHeight(), i2);
        int defaultSize2 = getDefaultSize(getSuggestedMinimumWidth(), i);
        if (this.mMaintainEqualCircle) {
            int min = Math.min(defaultSize2, defaultSize);
            setMeasuredDimension(min, min);
        } else {
            setMeasuredDimension(defaultSize2, defaultSize);
        }
        float f = ((float) defaultSize) / DEFAULT_POINTER_HALO_BORDER_WIDTH;
        float f2 = this.mCircleStrokeWidth;
        float f3 = f - f2;
        float f4 = this.mPointerRadius;
        float f5 = f3 - f4;
        float f6 = this.mPointerHaloBorderWidth;
        this.mCircleHeight = f5 - (f6 * 1.5f);
        this.mCircleWidth = (((((float) defaultSize2) / DEFAULT_POINTER_HALO_BORDER_WIDTH) - f2) - f4) - (f6 * 1.5f);
        if (this.mCustomRadii) {
            float f7 = this.mCircleYRadius;
            if (((f7 - f2) - f4) - f6 < this.mCircleHeight) {
                this.mCircleHeight = ((f7 - f2) - f4) - (f6 * 1.5f);
            }
            float f8 = this.mCircleXRadius;
            float f9 = this.mCircleStrokeWidth;
            float f10 = f8 - f9;
            float f11 = this.mPointerRadius;
            float f12 = f10 - f11;
            float f13 = this.mPointerHaloBorderWidth;
            if (f12 - f13 < this.mCircleWidth) {
                this.mCircleWidth = ((f8 - f9) - f11) - (f13 * 1.5f);
            }
        }
        if (this.mMaintainEqualCircle) {
            float min2 = Math.min(this.mCircleHeight, this.mCircleWidth);
            this.mCircleHeight = min2;
            this.mCircleWidth = min2;
        }
        recalculateAll();
    }

    public boolean isLockEnabled() {
        return this.lockEnabled;
    }

    public void setLockEnabled(boolean z) {
        this.lockEnabled = z;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isTouchEnabled) {
            return false;
        }
        float x = motionEvent.getX() - ((float) (getWidth() / 2));
        float y = motionEvent.getY() - ((float) (getHeight() / 2));
        float sqrt = (float) Math.sqrt(Math.pow((double) (this.mCircleRectF.centerX() - x), 2.0d) + Math.pow((double) (this.mCircleRectF.centerY() - y), 2.0d));
        float f = this.DPTOPX_SCALE * 48.0f;
        float f2 = this.mCircleStrokeWidth;
        float f3 = f2 < f ? f / DEFAULT_POINTER_HALO_BORDER_WIDTH : f2 / DEFAULT_POINTER_HALO_BORDER_WIDTH;
        float max = Math.max(this.mCircleHeight, this.mCircleWidth) + f3;
        float min = Math.min(this.mCircleHeight, this.mCircleWidth) - f3;
        int i = (this.mPointerRadius > (f / DEFAULT_POINTER_HALO_BORDER_WIDTH) ? 1 : (this.mPointerRadius == (f / DEFAULT_POINTER_HALO_BORDER_WIDTH) ? 0 : -1));
        float atan2 = (float) (((Math.atan2((double) y, (double) x) / 3.141592653589793d) * 180.0d) % 360.0d);
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        }
        this.cwDistanceFromStart = atan2 - this.mStartAngle;
        float f4 = this.cwDistanceFromStart;
        if (f4 < 0.0f) {
            f4 += 360.0f;
        }
        this.cwDistanceFromStart = f4;
        this.ccwDistanceFromStart = 360.0f - this.cwDistanceFromStart;
        this.cwDistanceFromEnd = atan2 - this.mEndAngle;
        float f5 = this.cwDistanceFromEnd;
        if (f5 < 0.0f) {
            f5 += 360.0f;
        }
        this.cwDistanceFromEnd = f5;
        this.ccwDistanceFromEnd = 360.0f - this.cwDistanceFromEnd;
        int action = motionEvent.getAction();
        if (action == 0) {
            float max2 = (float) (((double) (this.mPointerRadius * 180.0f)) / (((double) Math.max(this.mCircleHeight, this.mCircleWidth)) * 3.141592653589793d));
            this.cwDistanceFromPointer = atan2 - this.mPointerPosition;
            float f6 = this.cwDistanceFromPointer;
            if (f6 < 0.0f) {
                f6 += 360.0f;
            }
            this.cwDistanceFromPointer = f6;
            float f7 = this.cwDistanceFromPointer;
            this.ccwDistanceFromPointer = 360.0f - f7;
            int i2 = (sqrt > min ? 1 : (sqrt == min ? 0 : -1));
            if (i2 >= 0 && sqrt <= max && (f7 <= max2 || this.ccwDistanceFromPointer <= max2)) {
                setProgressBasedOnAngle(this.mPointerPosition);
                this.lastCWDistanceFromStart = this.cwDistanceFromStart;
                this.mIsMovingCW = true;
                this.mPointerHaloPaint.setAlpha(this.mPointerAlphaOnTouch);
                this.mPointerHaloPaint.setColor(this.mPointerHaloColorOnTouch);
                recalculateAll();
                invalidate();
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener != null) {
                    onCircularSeekBarChangeListener.onStartTrackingTouch(this);
                }
                this.mUserIsMovingPointer = true;
                this.lockAtEnd = false;
                this.lockAtStart = false;
            } else if (this.cwDistanceFromStart > this.mTotalCircleDegrees) {
                this.mUserIsMovingPointer = false;
                return false;
            } else if (i2 < 0 || sqrt > max) {
                this.mUserIsMovingPointer = false;
                return false;
            } else {
                setProgressBasedOnAngle(atan2);
                this.lastCWDistanceFromStart = this.cwDistanceFromStart;
                this.mIsMovingCW = true;
                this.mPointerHaloPaint.setAlpha(this.mPointerAlphaOnTouch);
                this.mPointerHaloPaint.setColor(this.mPointerHaloColorOnTouch);
                recalculateAll();
                invalidate();
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener2 = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener2 != null) {
                    onCircularSeekBarChangeListener2.onStartTrackingTouch(this);
                    this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, true);
                }
                this.mUserIsMovingPointer = true;
                this.lockAtEnd = false;
                this.lockAtStart = false;
            }
        } else if (action == 1) {
            this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
            this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
            if (!this.mUserIsMovingPointer) {
                return false;
            }
            this.mUserIsMovingPointer = false;
            invalidate();
            OnCircularSeekBarChangeListener onCircularSeekBarChangeListener3 = this.mOnCircularSeekBarChangeListener;
            if (onCircularSeekBarChangeListener3 != null) {
                onCircularSeekBarChangeListener3.onStopTrackingTouch(this);
            }
        } else if (action != 2) {
            if (action == 3) {
                this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
                this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
                this.mUserIsMovingPointer = false;
                invalidate();
            }
        } else if (!this.mUserIsMovingPointer) {
            return false;
        } else {
            float f8 = this.lastCWDistanceFromStart;
            float f9 = this.cwDistanceFromStart;
            if (f8 < f9) {
                if (f9 - f8 <= 180.0f || this.mIsMovingCW) {
                    this.mIsMovingCW = true;
                } else {
                    this.lockAtStart = true;
                    this.lockAtEnd = false;
                }
            } else if (f8 - f9 <= 180.0f || !this.mIsMovingCW) {
                this.mIsMovingCW = false;
            } else {
                this.lockAtEnd = true;
                this.lockAtStart = false;
            }
            if (this.lockAtStart && this.mIsMovingCW) {
                this.lockAtStart = false;
            }
            if (this.lockAtEnd && !this.mIsMovingCW) {
                this.lockAtEnd = false;
            }
            if (this.lockAtStart && !this.mIsMovingCW && this.ccwDistanceFromStart > 90.0f) {
                this.lockAtStart = false;
            }
            if (this.lockAtEnd && this.mIsMovingCW && this.cwDistanceFromEnd > 90.0f) {
                this.lockAtEnd = false;
            }
            if (!this.lockAtEnd) {
                float f10 = this.cwDistanceFromStart;
                float f11 = this.mTotalCircleDegrees;
                if (f10 > f11 && this.mIsMovingCW && this.lastCWDistanceFromStart < f11) {
                    this.lockAtEnd = true;
                }
            }
            if (this.lockAtStart && this.lockEnabled) {
                this.mProgress = 0;
                recalculateAll();
                invalidate();
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener4 = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener4 != null) {
                    onCircularSeekBarChangeListener4.onProgressChanged(this, this.mProgress, true);
                }
            } else if (this.lockAtEnd && this.lockEnabled) {
                this.mProgress = this.mMax;
                recalculateAll();
                invalidate();
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener5 = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener5 != null) {
                    onCircularSeekBarChangeListener5.onProgressChanged(this, this.mProgress, true);
                }
            } else if (this.mMoveOutsideCircle || sqrt <= max) {
                if (this.cwDistanceFromStart <= this.mTotalCircleDegrees) {
                    setProgressBasedOnAngle(atan2);
                }
                recalculateAll();
                invalidate();
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener6 = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener6 != null) {
                    onCircularSeekBarChangeListener6.onProgressChanged(this, this.mProgress, true);
                }
            }
            this.lastCWDistanceFromStart = this.cwDistanceFromStart;
        }
        if (motionEvent.getAction() == 2 && getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircularSeekBar, i, 0);
        initAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        initPaints();
    }

    public CircularSeekBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircularSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    public CircularSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("PARENT", onSaveInstanceState);
        bundle.putInt("MAX", this.mMax);
        bundle.putInt("PROGRESS", this.mProgress);
        bundle.putInt("mCircleColor", this.mCircleColor);
        bundle.putInt("mCircleProgressColor", this.mCircleProgressColor);
        bundle.putInt("mPointerColor", this.mPointerColor);
        bundle.putInt("mPointerHaloColor", this.mPointerHaloColor);
        bundle.putInt("mPointerHaloColorOnTouch", this.mPointerHaloColorOnTouch);
        bundle.putInt("mPointerAlpha", this.mPointerAlpha);
        bundle.putInt("mPointerAlphaOnTouch", this.mPointerAlphaOnTouch);
        bundle.putBoolean("lockEnabled", this.lockEnabled);
        bundle.putBoolean("isTouchEnabled", this.isTouchEnabled);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("PARENT"));
        this.mMax = bundle.getInt("MAX");
        this.mProgress = bundle.getInt("PROGRESS");
        this.mCircleColor = bundle.getInt("mCircleColor");
        this.mCircleProgressColor = bundle.getInt("mCircleProgressColor");
        this.mPointerColor = bundle.getInt("mPointerColor");
        this.mPointerHaloColor = bundle.getInt("mPointerHaloColor");
        this.mPointerHaloColorOnTouch = bundle.getInt("mPointerHaloColorOnTouch");
        this.mPointerAlpha = bundle.getInt("mPointerAlpha");
        this.mPointerAlphaOnTouch = bundle.getInt("mPointerAlphaOnTouch");
        this.lockEnabled = bundle.getBoolean("lockEnabled");
        this.isTouchEnabled = bundle.getBoolean("isTouchEnabled");
        initPaints();
        recalculateAll();
    }

    public void setOnSeekBarChangeListener(OnCircularSeekBarChangeListener onCircularSeekBarChangeListener) {
        this.mOnCircularSeekBarChangeListener = onCircularSeekBarChangeListener;
    }

    public void setCircleColor(int i) {
        this.mCircleColor = i;
        this.mCirclePaint.setColor(this.mCircleColor);
        invalidate();
    }

    public int getCircleColor() {
        return this.mCircleColor;
    }

    public void setCircleProgressColor(int i) {
        this.mCircleProgressColor = i;
        this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
        invalidate();
    }

    public int getCircleProgressColor() {
        return this.mCircleProgressColor;
    }

    public void setPointerColor(int i) {
        this.mPointerColor = i;
        this.mPointerPaint.setColor(this.mPointerColor);
        invalidate();
    }

    public int getPointerColor() {
        return this.mPointerColor;
    }

    public void setPointerHaloColor(int i) {
        this.mPointerHaloColor = i;
        this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
        invalidate();
    }

    public int getPointerHaloColor() {
        return this.mPointerHaloColor;
    }

    public void setPointerAlpha(int i) {
        if (i >= 0 && i <= 255) {
            this.mPointerAlpha = i;
            this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
            invalidate();
        }
    }

    public int getPointerAlpha() {
        return this.mPointerAlpha;
    }

    public void setPointerAlphaOnTouch(int i) {
        if (i >= 0 && i <= 255) {
            this.mPointerAlphaOnTouch = i;
        }
    }

    public int getPointerAlphaOnTouch() {
        return this.mPointerAlphaOnTouch;
    }

    public void setCircleFillColor(int i) {
        this.mCircleFillColor = i;
        this.mCircleFillPaint.setColor(this.mCircleFillColor);
        invalidate();
    }

    public int getCircleFillColor() {
        return this.mCircleFillColor;
    }

    public void setMax(int i) {
        if (i > 0) {
            if (i <= this.mProgress) {
                this.mProgress = 0;
                OnCircularSeekBarChangeListener onCircularSeekBarChangeListener = this.mOnCircularSeekBarChangeListener;
                if (onCircularSeekBarChangeListener != null) {
                    onCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, false);
                }
            }
            this.mMax = i;
            recalculateAll();
            invalidate();
        }
    }

    public synchronized int getMax() {
        return this.mMax;
    }

    public void setIsTouchEnabled(boolean z) {
        this.isTouchEnabled = z;
    }

    public boolean getIsTouchEnabled() {
        return this.isTouchEnabled;
    }
}
