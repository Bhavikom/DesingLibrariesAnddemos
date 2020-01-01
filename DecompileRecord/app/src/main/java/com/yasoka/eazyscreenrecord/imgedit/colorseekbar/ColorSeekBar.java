package com.yasoka.eazyscreenrecord.imgedit.colorseekbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.support.annotation.ArrayRes;
import android.support.p000v4.view.InputDeviceCompat;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ColorSeekBar extends View {

    /* renamed from: c0 */
    private int f98c0;

    /* renamed from: c1 */
    private int f99c1;
    private int mAlpha;
    private int mAlphaBarPosition;
    private int mAlphaMaxPosition = 255;
    private int mAlphaMinPosition = 0;
    private Rect mAlphaRect;
    private int mBackgroundColor = -1;
    private int mBarHeight = 5;
    private int mBarMargin = 5;
    private int mBarWidth;
    private int mBlue;
    private int mColorBarPosition;
    private LinearGradient mColorGradient;
    private Rect mColorRect;
    private Paint mColorRectPaint;
    private int[] mColorSeeds = {ViewCompat.MEASURED_STATE_MASK, -6749953, -16776961, -16711936, -16711681, -65536, -65281, -39424, InputDeviceCompat.SOURCE_ANY, -1, ViewCompat.MEASURED_STATE_MASK};
    private List<Integer> mColors = new ArrayList();
    private int mColorsToInvoke = -1;
    private Context mContext;
    private boolean mFirstDraw = true;
    private int mGreen;
    private boolean mInit = false;
    private boolean mIsShowAlphaBar = true;
    private boolean mIsVertical;
    private int mMaxPosition;
    private boolean mMovingAlphaBar;
    private boolean mMovingColorBar;
    private OnColorChangeListener mOnColorChangeLister;
    private OnInitDoneListener mOnInitDoneListener;
    private int mPaddingSize;
    private int mRed;
    private int mThumbHeight = 10;
    private float mThumbRadius;
    private Bitmap mTransparentBitmap;
    private int mViewHeight;
    private int mViewWidth;
    private int realBottom;
    private int realLeft;
    private int realRight;
    private int realTop;

    /* renamed from: x */
    private float f100x;

    /* renamed from: y */
    private float f101y;

    public interface OnColorChangeListener {
        void onColorChangeListener(int i, int i2, int i3);
    }

    public interface OnInitDoneListener {
        void done();
    }

    public ColorSeekBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ColorSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0, 0);
    }

    public ColorSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i, 0);
    }

    @TargetApi(21)
    public ColorSeekBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void init(Context context, AttributeSet attributeSet, int i, int i2) {
        applyStyle(context, attributeSet, i, i2);
        init();
        this.mInit = true;
    }

    public void applyStyle(int i) {
        applyStyle(getContext(), null, 0, i);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mViewWidth = i;
        this.mViewHeight = i2;
        int mode = MeasureSpec.getMode(i);
        MeasureSpec.getMode(i2);
        int i3 = this.mIsShowAlphaBar ? this.mBarHeight * 2 : this.mBarHeight;
        int i4 = this.mIsShowAlphaBar ? this.mThumbHeight * 2 : this.mThumbHeight;
        if (isVertical()) {
            if (mode == Integer.MIN_VALUE || mode == 0) {
                this.mViewWidth = i4 + i3 + this.mBarMargin;
                setMeasuredDimension(this.mViewWidth, this.mViewHeight);
            }
        } else if (mode == Integer.MIN_VALUE || mode == 0) {
            this.mViewHeight = i4 + i3 + this.mBarMargin;
            setMeasuredDimension(this.mViewWidth, this.mViewHeight);
        }
    }

    /* access modifiers changed from: protected */
    public void applyStyle(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ColorSeekBar, i, i2);
        int resourceId = obtainStyledAttributes.getResourceId(5, 0);
        this.mMaxPosition = obtainStyledAttributes.getInteger(7, 100);
        this.mColorBarPosition = obtainStyledAttributes.getInteger(4, 0);
        this.mAlphaBarPosition = obtainStyledAttributes.getInteger(0, this.mAlphaMinPosition);
        this.mIsVertical = obtainStyledAttributes.getBoolean(6, false);
        this.mIsShowAlphaBar = obtainStyledAttributes.getBoolean(8, false);
        this.mBackgroundColor = obtainStyledAttributes.getColor(3, 0);
        this.mBarHeight = (int) obtainStyledAttributes.getDimension(1, (float) dp2px(5.0f));
        this.mThumbHeight = (int) obtainStyledAttributes.getDimension(9, (float) dp2px(10.0f));
        this.mBarMargin = (int) obtainStyledAttributes.getDimension(2, (float) dp2px(5.0f));
        obtainStyledAttributes.recycle();
        if (resourceId != 0) {
            this.mColorSeeds = getColorsById(resourceId);
        }
        setBackgroundColor(this.mBackgroundColor);
        cacheColors();
    }

    private int[] getColorsById(int i) {
        int i2 = 0;
        if (isInEditMode()) {
            String[] stringArray = this.mContext.getResources().getStringArray(i);
            int[] iArr = new int[stringArray.length];
            while (i2 < stringArray.length) {
                iArr[i2] = Color.parseColor(stringArray[i2]);
                i2++;
            }
            return iArr;
        }
        TypedArray obtainTypedArray = this.mContext.getResources().obtainTypedArray(i);
        int[] iArr2 = new int[obtainTypedArray.length()];
        while (i2 < obtainTypedArray.length()) {
            iArr2[i2] = obtainTypedArray.getColor(i2, ViewCompat.MEASURED_STATE_MASK);
            i2++;
        }
        obtainTypedArray.recycle();
        return iArr2;
    }

    private void init() {
        this.mThumbRadius = (float) (this.mThumbHeight / 2);
        this.mPaddingSize = (int) this.mThumbRadius;
        int height = (getHeight() - getPaddingBottom()) - this.mPaddingSize;
        int width = (getWidth() - getPaddingRight()) - this.mPaddingSize;
        this.realLeft = getPaddingLeft() + this.mPaddingSize;
        this.realRight = this.mIsVertical ? height : width;
        this.realTop = getPaddingTop() + this.mPaddingSize;
        if (this.mIsVertical) {
            height = width;
        }
        this.realBottom = height;
        int i = this.realRight;
        int i2 = this.realLeft;
        this.mBarWidth = i - i2;
        int i3 = this.realTop;
        this.mColorRect = new Rect(i2, i3, i, this.mBarHeight + i3);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, (float) this.mColorRect.width(), 0.0f, this.mColorSeeds, null, TileMode.MIRROR);
        this.mColorGradient = linearGradient;
        this.mColorRectPaint = new Paint();
        this.mColorRectPaint.setShader(this.mColorGradient);
        this.mColorRectPaint.setAntiAlias(true);
        cacheColors();
        setAlphaValue();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mIsVertical) {
            this.mTransparentBitmap = Bitmap.createBitmap(i2, i, Config.ARGB_4444);
        } else {
            this.mTransparentBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_4444);
        }
        this.mTransparentBitmap.eraseColor(0);
        init();
        this.mInit = true;
        int i5 = this.mColorsToInvoke;
        if (i5 != -1) {
            setColor(i5);
        }
    }

    private void cacheColors() {
        this.mColors.clear();
        for (int i = 0; i <= this.mMaxPosition; i++) {
            this.mColors.add(Integer.valueOf(pickColor(i)));
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.mIsVertical) {
            canvas2.rotate(-90.0f);
            canvas2.translate((float) (-getHeight()), 0.0f);
            canvas2.scale(-1.0f, 1.0f, (float) (getHeight() / 2), (float) (getWidth() / 2));
        }
        float f = (((float) this.mColorBarPosition) / ((float) this.mMaxPosition)) * ((float) this.mBarWidth);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int color = getColor(false);
        int argb = Color.argb(this.mAlphaMaxPosition, Color.red(color), Color.green(color), Color.blue(color));
        int argb2 = Color.argb(this.mAlphaMinPosition, Color.red(color), Color.green(color), Color.blue(color));
        paint.setColor(-1);
        int[] iArr = {argb, argb2};
        canvas2.drawBitmap(this.mTransparentBitmap, 0.0f, 0.0f, null);
        canvas2.drawRect(this.mColorRect, this.mColorRectPaint);
        float f2 = f + ((float) this.realLeft);
        float height = (float) (this.mColorRect.top + (this.mColorRect.height() / 2));
        canvas2.drawCircle(f2, height, (float) ((this.mBarHeight / 2) + 8), paint);
        RadialGradient radialGradient = new RadialGradient(f2, height, this.mThumbRadius, iArr, null, TileMode.MIRROR);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setShader(radialGradient);
        canvas2.drawCircle(f2, height, (float) (this.mThumbHeight / 2), paint2);
        if (this.mIsShowAlphaBar) {
            float f3 = ((float) this.mThumbHeight) + this.mThumbRadius;
            int i = this.mBarHeight;
            int i2 = (int) (f3 + ((float) i) + ((float) this.mBarMargin));
            this.mAlphaRect = new Rect(this.realLeft, i2, this.realRight, i + i2);
            Paint paint3 = new Paint();
            paint3.setAntiAlias(true);
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, (float) this.mAlphaRect.width(), 0.0f, iArr, null, TileMode.MIRROR);
            paint3.setShader(linearGradient);
            canvas2.drawRect(this.mAlphaRect, paint3);
            int i3 = this.mAlphaBarPosition;
            int i4 = this.mAlphaMinPosition;
            float f4 = ((((float) (i3 - i4)) / ((float) (this.mAlphaMaxPosition - i4))) * ((float) this.mBarWidth)) + ((float) this.realLeft);
            float height2 = (float) (this.mAlphaRect.top + (this.mAlphaRect.height() / 2));
            canvas2.drawCircle(f4, height2, (float) ((this.mBarHeight / 2) + 5), paint);
            RadialGradient radialGradient2 = new RadialGradient(f4, height2, this.mThumbRadius, iArr, null, TileMode.MIRROR);
            Paint paint4 = new Paint();
            paint4.setAntiAlias(true);
            paint4.setShader(radialGradient2);
            canvas2.drawCircle(f4, height2, (float) (this.mThumbHeight / 2), paint4);
        }
        if (this.mFirstDraw) {
            OnColorChangeListener onColorChangeListener = this.mOnColorChangeLister;
            if (onColorChangeListener != null) {
                onColorChangeListener.onColorChangeListener(this.mColorBarPosition, this.mAlphaBarPosition, getColor());
            }
            this.mFirstDraw = false;
            OnInitDoneListener onInitDoneListener = this.mOnInitDoneListener;
            if (onInitDoneListener != null) {
                onInitDoneListener.done();
            }
        }
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.f100x = this.mIsVertical ? motionEvent.getY() : motionEvent.getX();
        this.f101y = this.mIsVertical ? motionEvent.getX() : motionEvent.getY();
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("touch-->");
        sb.append(this.f100x);
        sb.append("<>");
        sb.append(this.f101y);
        printStream.println(sb.toString());
        int action = motionEvent.getAction();
        if (action != 0) {
            String str = "BAR POSITION-->";
            if (action == 1) {
                if (this.mMovingColorBar) {
                    this.mColorBarPosition = (int) (((this.f100x - ((float) this.realLeft)) / ((float) this.mBarWidth)) * ((float) this.mMaxPosition));
                    PrintStream printStream2 = System.out;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(this.mColorBarPosition);
                    printStream2.println(sb2.toString());
                    if (this.mColorBarPosition < 0) {
                        this.mColorBarPosition = 0;
                    }
                    int i = this.mColorBarPosition;
                    int i2 = this.mMaxPosition;
                    if (i > i2) {
                        this.mColorBarPosition = i2;
                    }
                    invalidate();
                } else if (this.mIsShowAlphaBar) {
                    if (this.mMovingAlphaBar) {
                        float f = (this.f100x - ((float) this.realLeft)) / ((float) this.mBarWidth);
                        int i3 = this.mAlphaMaxPosition;
                        int i4 = this.mAlphaMinPosition;
                        this.mAlphaBarPosition = (int) ((f * ((float) (i3 - i4))) + ((float) i4));
                        int i5 = this.mAlphaBarPosition;
                        if (i5 < i4) {
                            this.mAlphaBarPosition = i4;
                        } else if (i5 > i3) {
                            this.mAlphaBarPosition = i3;
                        }
                        setAlphaValue();
                    }
                    invalidate();
                }
                this.mMovingColorBar = false;
                this.mMovingAlphaBar = false;
            } else if (action == 2) {
                getParent().requestDisallowInterceptTouchEvent(true);
                if (this.mMovingColorBar) {
                    this.mColorBarPosition = (int) (((this.f100x - ((float) this.realLeft)) / ((float) this.mBarWidth)) * ((float) this.mMaxPosition));
                    PrintStream printStream3 = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(this.mColorBarPosition);
                    printStream3.println(sb3.toString());
                    if (this.mColorBarPosition < 0) {
                        this.mColorBarPosition = 0;
                    }
                    int i6 = this.mColorBarPosition;
                    int i7 = this.mMaxPosition;
                    if (i6 > i7) {
                        this.mColorBarPosition = i7;
                    }
                } else if (this.mIsShowAlphaBar && this.mMovingAlphaBar) {
                    float f2 = (this.f100x - ((float) this.realLeft)) / ((float) this.mBarWidth);
                    int i8 = this.mAlphaMaxPosition;
                    int i9 = this.mAlphaMinPosition;
                    this.mAlphaBarPosition = (int) ((f2 * ((float) (i8 - i9))) + ((float) i9));
                    int i10 = this.mAlphaBarPosition;
                    if (i10 < i9) {
                        this.mAlphaBarPosition = i9;
                    } else if (i10 > i8) {
                        this.mAlphaBarPosition = i8;
                    }
                    setAlphaValue();
                }
                if (this.mOnColorChangeLister != null && (this.mMovingAlphaBar || this.mMovingColorBar)) {
                    this.mOnColorChangeLister.onColorChangeListener(this.mColorBarPosition, this.mAlphaBarPosition, getColor());
                }
                invalidate();
            }
        } else if (isOnBar(this.mColorRect, this.f100x, this.f101y)) {
            System.out.println("IS ON BAR");
            this.mMovingColorBar = true;
        } else if (this.mIsShowAlphaBar && isOnBar(this.mAlphaRect, this.f100x, this.f101y)) {
            this.mMovingAlphaBar = true;
        }
        return true;
    }

    public int getAlphaMaxPosition() {
        return this.mAlphaMaxPosition;
    }

    public void setAlphaMaxPosition(int i) {
        this.mAlphaMaxPosition = i;
        int i2 = this.mAlphaMaxPosition;
        if (i2 > 255) {
            this.mAlphaMaxPosition = 255;
        } else {
            int i3 = this.mAlphaMinPosition;
            if (i2 <= i3) {
                this.mAlphaMaxPosition = i3 + 1;
            }
        }
        if (this.mAlphaBarPosition > this.mAlphaMinPosition) {
            this.mAlphaBarPosition = this.mAlphaMaxPosition;
        }
        invalidate();
    }

    public int getAlphaMinPosition() {
        return this.mAlphaMinPosition;
    }

    public void setAlphaMinPosition(int i) {
        this.mAlphaMinPosition = i;
        int i2 = this.mAlphaMinPosition;
        int i3 = this.mAlphaMaxPosition;
        if (i2 >= i3) {
            this.mAlphaMinPosition = i3 - 1;
        } else if (i2 < 0) {
            this.mAlphaMinPosition = 0;
        }
        int i4 = this.mAlphaBarPosition;
        int i5 = this.mAlphaMinPosition;
        if (i4 < i5) {
            this.mAlphaBarPosition = i5;
        }
        invalidate();
    }

    private boolean isOnBar(Rect rect, float f, float f2) {
        return ((float) rect.left) - this.mThumbRadius < f && f < ((float) rect.right) + this.mThumbRadius && ((float) rect.top) - this.mThumbRadius < f2 && f2 < ((float) rect.bottom) + this.mThumbRadius;
    }

    public boolean isFirstDraw() {
        return this.mFirstDraw;
    }

    private int pickColor(int i) {
        return pickColor((((float) i) / ((float) this.mMaxPosition)) * ((float) this.mBarWidth));
    }

    private int pickColor(float f) {
        float f2 = f / ((float) this.mBarWidth);
        if (((double) f2) <= FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE) {
            return this.mColorSeeds[0];
        }
        if (f2 >= 1.0f) {
            int[] iArr = this.mColorSeeds;
            return iArr[iArr.length - 1];
        }
        int[] iArr2 = this.mColorSeeds;
        float length = f2 * ((float) (iArr2.length - 1));
        int i = (int) length;
        float f3 = length - ((float) i);
        this.f98c0 = iArr2[i];
        this.f99c1 = iArr2[i + 1];
        this.mRed = mix(Color.red(this.f98c0), Color.red(this.f99c1), f3);
        this.mGreen = mix(Color.green(this.f98c0), Color.green(this.f99c1), f3);
        this.mBlue = mix(Color.blue(this.f98c0), Color.blue(this.f99c1), f3);
        return Color.rgb(this.mRed, this.mGreen, this.mBlue);
    }

    private int mix(int i, int i2, float f) {
        return i + Math.round(f * ((float) (i2 - i)));
    }

    public int getColor() {
        return getColor(this.mIsShowAlphaBar);
    }

    public void setColor(int i) {
        int rgb = Color.rgb(Color.red(i), Color.green(i), Color.blue(i));
        if (this.mInit) {
            setColorBarPosition(this.mColors.indexOf(Integer.valueOf(rgb)));
        } else {
            this.mColorsToInvoke = i;
        }
    }

    public int getColor(boolean z) {
        if (this.mColorBarPosition >= this.mColors.size()) {
            int pickColor = pickColor(this.mColorBarPosition);
            if (z) {
                return pickColor;
            }
            return Color.argb(getAlphaValue(), Color.red(pickColor), Color.green(pickColor), Color.blue(pickColor));
        }
        int intValue = ((Integer) this.mColors.get(this.mColorBarPosition)).intValue();
        return z ? Color.argb(getAlphaValue(), Color.red(intValue), Color.green(intValue), Color.blue(intValue)) : intValue;
    }

    public int getAlphaBarPosition() {
        return this.mAlphaBarPosition;
    }

    public void setAlphaBarPosition(int i) {
        this.mAlphaBarPosition = i;
        setAlphaValue();
        invalidate();
    }

    public int getAlphaValue() {
        return this.mAlpha;
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.mOnColorChangeLister = onColorChangeListener;
    }

    public int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void setColorSeeds(@ArrayRes int i) {
        setColorSeeds(getColorsById(i));
    }

    public void setColorSeeds(int[] iArr) {
        this.mColorSeeds = iArr;
        invalidate();
        cacheColors();
        setAlphaValue();
        OnColorChangeListener onColorChangeListener = this.mOnColorChangeLister;
        if (onColorChangeListener != null) {
            onColorChangeListener.onColorChangeListener(this.mColorBarPosition, this.mAlphaBarPosition, getColor());
        }
    }

    public int getColorIndexPosition(int i) {
        return this.mColors.indexOf(Integer.valueOf(Color.argb(255, Color.red(i), Color.green(i), Color.blue(i))));
    }

    public List<Integer> getColors() {
        return this.mColors;
    }

    public boolean isShowAlphaBar() {
        return this.mIsShowAlphaBar;
    }

    public void setShowAlphaBar(boolean z) {
        this.mIsShowAlphaBar = z;
        refreshLayoutParams();
        invalidate();
        OnColorChangeListener onColorChangeListener = this.mOnColorChangeLister;
        if (onColorChangeListener != null) {
            onColorChangeListener.onColorChangeListener(this.mColorBarPosition, this.mAlphaBarPosition, getColor());
        }
    }

    private void refreshLayoutParams() {
        setLayoutParams(getLayoutParams());
    }

    public boolean isVertical() {
        return this.mIsVertical;
    }

    public void setBarHeightPx(int i) {
        this.mBarHeight = i;
        refreshLayoutParams();
        invalidate();
    }

    private void setAlphaValue() {
        this.mAlpha = 255 - this.mAlphaBarPosition;
    }

    public int getMaxValue() {
        return this.mMaxPosition;
    }

    public void setMaxPosition(int i) {
        this.mMaxPosition = i;
        invalidate();
        cacheColors();
    }

    public void setBarMarginPx(int i) {
        this.mBarMargin = i;
        refreshLayoutParams();
        invalidate();
    }

    public void setColorBarPosition(int i) {
        this.mColorBarPosition = i;
        int i2 = this.mColorBarPosition;
        int i3 = this.mMaxPosition;
        if (i2 > i3) {
            i2 = i3;
        }
        this.mColorBarPosition = i2;
        int i4 = this.mColorBarPosition;
        if (i4 < 0) {
            i4 = 0;
        }
        this.mColorBarPosition = i4;
        invalidate();
        OnColorChangeListener onColorChangeListener = this.mOnColorChangeLister;
        if (onColorChangeListener != null) {
            onColorChangeListener.onColorChangeListener(this.mColorBarPosition, this.mAlphaBarPosition, getColor());
        }
    }

    public void setOnInitDoneListener(OnInitDoneListener onInitDoneListener) {
        this.mOnInitDoneListener = onInitDoneListener;
    }

    public void setThumbHeightPx(int i) {
        this.mThumbHeight = i;
        this.mThumbRadius = (float) (this.mThumbHeight / 2);
        refreshLayoutParams();
        invalidate();
    }

    public int getBarHeight() {
        return this.mBarHeight;
    }

    public void setBarHeight(float f) {
        this.mBarHeight = dp2px(f);
        refreshLayoutParams();
        invalidate();
    }

    public int getThumbHeight() {
        return this.mThumbHeight;
    }

    public void setThumbHeight(float f) {
        this.mThumbHeight = dp2px(f);
        this.mThumbRadius = (float) (this.mThumbHeight / 2);
        refreshLayoutParams();
        invalidate();
    }

    public int getBarMargin() {
        return this.mBarMargin;
    }

    public void setBarMargin(float f) {
        this.mBarMargin = dp2px(f);
        refreshLayoutParams();
        invalidate();
    }

    public float getColorBarValue() {
        return (float) this.mColorBarPosition;
    }
}
