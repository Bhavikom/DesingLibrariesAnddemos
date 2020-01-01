package com.yasoka.eazyscreenrecord.imgedit.fabric.numberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.view.ViewCompat;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.google.common.primitives.Ints;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberPicker extends LinearLayout {
    public static final int ASCENDING = 0;
    private static final int DEFAULT_DIVIDER_COLOR = -16777216;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final int DEFAULT_MAX_HEIGHT = 180;
    private static final int DEFAULT_MAX_VALUE = 50;
    private static final int DEFAULT_MIN_VALUE = 1;
    private static final int DEFAULT_MIN_WIDTH = 64;
    private static final int DEFAULT_TEXT_COLOR = -16777216;
    private static final float DEFAULT_TEXT_SIZE = 25.0f;
    public static final int DESCENDING = 1;
    /* access modifiers changed from: private */
    public static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, '-'};
    private static final float FADING_EDGE_STRENGTH = 0.9f;
    public static final int HORIZONTAL = 0;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_THICKNESS = 2;
    public static final int VERTICAL = 1;
    private static final TwoDigitFormatter sTwoDigitFormatter = new TwoDigitFormatter();
    private final Scroller mAdjustScroller;
    private int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private final boolean mComputeMaxWidth;
    private Context mContext;
    private int mCurrentScrollOffset;
    /* access modifiers changed from: private */
    public String[] mDisplayedValues;
    private final Scroller mFlingScroller;
    private Formatter mFormatter;
    private float mHeight;
    private int mInitialScrollOffset;
    private float mLastDownEventX;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventX;
    private float mLastDownOrMoveEventY;
    private int mLastHandledDownDpadKeyCode;
    private int mLeftOfSelectionDividerLeft;
    /* access modifiers changed from: private */
    public long mLongPressUpdateInterval;
    private int mMaxHeight;
    /* access modifiers changed from: private */
    public int mMaxValue;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private int mMinHeight;
    private int mMinValue;
    private int mMinWidth;
    private int mMinimumFlingVelocity;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private int mOrder;
    private int mOrientation;
    private int mPreviousScrollerX;
    private int mPreviousScrollerY;
    private int mRightOfSelectionDividerRight;
    private int mScrollState;
    /* access modifiers changed from: private */
    public final EditText mSelectedText;
    private int mSelectedTextColor;
    private float mSelectedTextSize;
    private Drawable mSelectionDivider;
    private int mSelectionDividerColor;
    private int mSelectionDividerThickness;
    private int mSelectionDividersDistance;
    private int mSelectorElementSize;
    private final SparseArray<String> mSelectorIndexToStringCache;
    private int[] mSelectorIndices;
    private int mSelectorTextGapHeight;
    private int mSelectorTextGapWidth;
    private final Paint mSelectorWheelPaint;
    private SetSelectionCommand mSetSelectionCommand;
    private int mTextColor;
    private float mTextSize;
    private int mTopSelectionDividerTop;
    private int mTouchSlop;
    private Typeface mTypeface;
    private int mValue;
    private VelocityTracker mVelocityTracker;
    private int mWheelItemCount;
    private int mWheelMiddleItemIndex;
    private float mWidth;
    private boolean mWrapSelectorWheel;

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        /* access modifiers changed from: private */
        public void setStep(boolean z) {
            this.mIncrement = z;
        }

        public void run() {
            NumberPicker.this.changeValueByOne(this.mIncrement);
            NumberPicker numberPicker = NumberPicker.this;
            numberPicker.postDelayed(this, numberPicker.mLongPressUpdateInterval);
        }
    }

    public interface Formatter {
        String format(int i);
    }

    class InputTextFilter extends NumberKeyListener {
        public int getInputType() {
            return 1;
        }

        InputTextFilter() {
        }

        /* access modifiers changed from: protected */
        public char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            String[] access$400;
            String str = "";
            if (NumberPicker.this.mDisplayedValues == null) {
                CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                if (filter == null) {
                    filter = charSequence.subSequence(i, i2);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(spanned.subSequence(0, i3)));
                sb.append(filter);
                sb.append(spanned.subSequence(i4, spanned.length()));
                String sb2 = sb.toString();
                if (str.equals(sb2)) {
                    return sb2;
                }
                return NumberPicker.this.getSelectedPos(sb2) > NumberPicker.this.mMaxValue ? str : filter;
            }
            String valueOf = String.valueOf(charSequence.subSequence(i, i2));
            if (TextUtils.isEmpty(valueOf)) {
                return str;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(String.valueOf(spanned.subSequence(0, i3)));
            sb3.append(valueOf);
            sb3.append(spanned.subSequence(i4, spanned.length()));
            String sb4 = sb3.toString();
            String lowerCase = String.valueOf(sb4).toLowerCase();
            for (String str2 : NumberPicker.this.mDisplayedValues) {
                if (str2.toLowerCase().startsWith(lowerCase)) {
                    NumberPicker.this.postSetSelectionCommand(sb4.length(), str2.length());
                    return str2.subSequence(i3, str2.length());
                }
            }
            return str;
        }
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScrollStateChange(NumberPicker numberPicker, int i);
    }

    public interface OnValueChangeListener {
        void onValueChange(NumberPicker numberPicker, int i, int i2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Order {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    class SetSelectionCommand implements Runnable {
        /* access modifiers changed from: private */
        public int mSelectionEnd;
        /* access modifiers changed from: private */
        public int mSelectionStart;

        SetSelectionCommand() {
        }

        public void run() {
            NumberPicker.this.mSelectedText.setSelection(this.mSelectionStart, this.mSelectionEnd);
        }
    }

    private static class TwoDigitFormatter implements Formatter {
        final Object[] mArgs = new Object[1];
        final StringBuilder mBuilder = new StringBuilder();
        java.util.Formatter mFmt;
        char mZeroDigit;

        TwoDigitFormatter() {
            init(Locale.getDefault());
        }

        private void init(Locale locale) {
            this.mFmt = createFormatter(locale);
            this.mZeroDigit = getZeroDigit(locale);
        }

        public String format(int i) {
            Locale locale = Locale.getDefault();
            if (this.mZeroDigit != getZeroDigit(locale)) {
                init(locale);
            }
            this.mArgs[0] = Integer.valueOf(i);
            StringBuilder sb = this.mBuilder;
            sb.delete(0, sb.length());
            this.mFmt.format("%02d", this.mArgs);
            return this.mFmt.toString();
        }

        private static char getZeroDigit(Locale locale) {
            return new DecimalFormatSymbols(locale).getZeroDigit();
        }

        private java.util.Formatter createFormatter(Locale locale) {
            return new java.util.Formatter(this.mBuilder, locale);
        }
    }

    public static final Formatter getTwoDigitFormatter() {
        return sTwoDigitFormatter;
    }

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mSelectedTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextSize = DEFAULT_TEXT_SIZE;
        this.mSelectedTextSize = DEFAULT_TEXT_SIZE;
        this.mMinValue = 1;
        this.mMaxValue = 50;
        this.mLongPressUpdateInterval = 300;
        this.mSelectorIndexToStringCache = new SparseArray<>();
        this.mWheelItemCount = 3;
        int i2 = this.mWheelItemCount;
        this.mWheelMiddleItemIndex = i2 / 2;
        this.mSelectorIndices = new int[i2];
        this.mInitialScrollOffset = Integer.MIN_VALUE;
        this.mSelectionDividerColor = ViewCompat.MEASURED_STATE_MASK;
        this.mScrollState = 0;
        this.mLastHandledDownDpadKeyCode = -1;
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NumberPicker, i, 0);
        this.mSelectionDivider = ContextCompat.getDrawable(context, C0793R.C0794drawable.np_numberpicker_selection_divider);
        this.mSelectionDividerColor = obtainStyledAttributes.getColor(0, this.mSelectionDividerColor);
        this.mSelectionDividersDistance = obtainStyledAttributes.getDimensionPixelSize(1, (int) TypedValue.applyDimension(1, 48.0f, getResources().getDisplayMetrics()));
        this.mSelectionDividerThickness = obtainStyledAttributes.getDimensionPixelSize(2, (int) TypedValue.applyDimension(1, 2.0f, getResources().getDisplayMetrics()));
        this.mOrder = obtainStyledAttributes.getInt(7, 0);
        this.mOrientation = obtainStyledAttributes.getInt(8, 1);
        this.mWidth = (float) obtainStyledAttributes.getDimensionPixelSize(16, -1);
        this.mHeight = (float) obtainStyledAttributes.getDimensionPixelSize(4, -1);
        setWidthAndHeight();
        this.mComputeMaxWidth = true;
        this.mValue = obtainStyledAttributes.getInt(14, this.mValue);
        this.mMaxValue = obtainStyledAttributes.getInt(5, this.mMaxValue);
        this.mMinValue = obtainStyledAttributes.getInt(6, this.mMinValue);
        this.mSelectedTextColor = obtainStyledAttributes.getColor(9, this.mSelectedTextColor);
        this.mSelectedTextSize = obtainStyledAttributes.getDimension(10, spToPx(this.mSelectedTextSize));
        this.mTextColor = obtainStyledAttributes.getColor(11, this.mTextColor);
        this.mTextSize = obtainStyledAttributes.getDimension(12, spToPx(this.mTextSize));
        this.mTypeface = Typeface.create(obtainStyledAttributes.getString(13), 0);
        this.mFormatter = stringToFormatter(obtainStyledAttributes.getString(3));
        this.mWheelItemCount = obtainStyledAttributes.getInt(15, this.mWheelItemCount);
        setWillNotDraw(false);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0793R.layout.number_picker_with_selector_wheel, this, true);
        this.mSelectedText = (EditText) findViewById(C0793R.C0795id.np__numberpicker_input);
        this.mSelectedText.setEnabled(false);
        this.mSelectedText.setFocusable(false);
        this.mSelectedText.setImeOptions(1);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        this.mSelectorWheelPaint = paint;
        setSelectedTextColor(this.mSelectedTextColor);
        setTextColor(this.mTextColor);
        setTextSize(this.mTextSize);
        setSelectedTextSize(this.mSelectedTextSize);
        setTypeface(this.mTypeface);
        setFormatter(this.mFormatter);
        updateInputTextView();
        setValue(this.mValue);
        setMaxValue(this.mMaxValue);
        setMinValue(this.mMinValue);
        setDividerColor(this.mSelectionDividerColor);
        setWheelItemCount(this.mWheelItemCount);
        this.mWrapSelectorWheel = obtainStyledAttributes.getBoolean(17, this.mWrapSelectorWheel);
        setWrapSelectorWheel(this.mWrapSelectorWheel);
        float f = this.mWidth;
        if (f == -1.0f || this.mHeight == -1.0f) {
            float f2 = this.mWidth;
            if (f2 != -1.0f) {
                setScaleX(f2 / ((float) this.mMinWidth));
                setScaleY(this.mWidth / ((float) this.mMinWidth));
            } else {
                float f3 = this.mHeight;
                if (f3 != -1.0f) {
                    setScaleX(f3 / ((float) this.mMaxHeight));
                    setScaleY(this.mHeight / ((float) this.mMaxHeight));
                }
            }
        } else {
            setScaleX(f / ((float) this.mMinWidth));
            setScaleY(this.mHeight / ((float) this.mMaxHeight));
        }
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / 8;
        this.mFlingScroller = new Scroller(context, null, true);
        this.mAdjustScroller = new Scroller(context, new DecelerateInterpolator(2.5f));
        if (VERSION.SDK_INT >= 16 && getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth2 = this.mSelectedText.getMeasuredWidth();
        int measuredHeight2 = this.mSelectedText.getMeasuredHeight();
        int i5 = (measuredWidth - measuredWidth2) / 2;
        int i6 = (measuredHeight - measuredHeight2) / 2;
        this.mSelectedText.layout(i5, i6, measuredWidth2 + i5, measuredHeight2 + i6);
        if (z) {
            initializeSelectorWheel();
            initializeFadingEdges();
            if (isHorizontalMode()) {
                int width = getWidth();
                int i7 = this.mSelectionDividersDistance;
                int i8 = (width - i7) / 2;
                int i9 = this.mSelectionDividerThickness;
                this.mLeftOfSelectionDividerLeft = i8 - i9;
                this.mRightOfSelectionDividerRight = this.mLeftOfSelectionDividerLeft + (i9 * 2) + i7;
                return;
            }
            int height = getHeight();
            int i10 = this.mSelectionDividersDistance;
            int i11 = (height - i10) / 2;
            int i12 = this.mSelectionDividerThickness;
            this.mTopSelectionDividerTop = i11 - i12;
            this.mBottomSelectionDividerBottom = this.mTopSelectionDividerTop + (i12 * 2) + i10;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(makeMeasureSpec(i, this.mMaxWidth), makeMeasureSpec(i2, this.mMaxHeight));
        setMeasuredDimension(resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), i), resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), i2));
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        if (isHorizontalMode()) {
            int finalX = scroller.getFinalX() - scroller.getCurrX();
            int i = this.mInitialScrollOffset - ((this.mCurrentScrollOffset + finalX) % this.mSelectorElementSize);
            if (i != 0) {
                int abs = Math.abs(i);
                int i2 = this.mSelectorElementSize;
                if (abs > i2 / 2) {
                    i = i > 0 ? i - i2 : i + i2;
                }
                scrollBy(finalX + i, 0);
                return true;
            }
        } else {
            int finalY = scroller.getFinalY() - scroller.getCurrY();
            int i3 = this.mInitialScrollOffset - ((this.mCurrentScrollOffset + finalY) % this.mSelectorElementSize);
            if (i3 != 0) {
                int abs2 = Math.abs(i3);
                int i4 = this.mSelectorElementSize;
                if (abs2 > i4 / 2) {
                    i3 = i3 > 0 ? i3 - i4 : i3 + i4;
                }
                scrollBy(0, finalY + i3);
                return true;
            }
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || (motionEvent.getAction() & 255) != 0) {
            return false;
        }
        removeAllCallbacks();
        this.mSelectedText.setVisibility(4);
        if (isHorizontalMode()) {
            float x = motionEvent.getX();
            this.mLastDownEventX = x;
            this.mLastDownOrMoveEventX = x;
            getParent().requestDisallowInterceptTouchEvent(true);
            if (!this.mFlingScroller.isFinished()) {
                this.mFlingScroller.forceFinished(true);
                this.mAdjustScroller.forceFinished(true);
                onScrollStateChange(0);
            } else if (!this.mAdjustScroller.isFinished()) {
                this.mFlingScroller.forceFinished(true);
                this.mAdjustScroller.forceFinished(true);
            } else {
                float f = this.mLastDownEventX;
                if (f < ((float) this.mLeftOfSelectionDividerLeft)) {
                    postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
                } else if (f > ((float) this.mRightOfSelectionDividerRight)) {
                    postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
            return true;
        }
        float y = motionEvent.getY();
        this.mLastDownEventY = y;
        this.mLastDownOrMoveEventY = y;
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!this.mFlingScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
            onScrollStateChange(0);
        } else if (!this.mAdjustScroller.isFinished()) {
            this.mFlingScroller.forceFinished(true);
            this.mAdjustScroller.forceFinished(true);
        } else {
            float f2 = this.mLastDownEventY;
            if (f2 < ((float) this.mTopSelectionDividerTop)) {
                postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
            } else if (f2 > ((float) this.mBottomSelectionDividerBottom)) {
                postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
            }
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action == 1) {
            removeChangeCurrentByOneFromLongPress();
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
            if (isHorizontalMode()) {
                int xVelocity = (int) velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > this.mMinimumFlingVelocity) {
                    fling(xVelocity);
                    onScrollStateChange(2);
                } else {
                    int x = (int) motionEvent.getX();
                    if (((int) Math.abs(((float) x) - this.mLastDownEventX)) <= this.mTouchSlop) {
                        int i = (x / this.mSelectorElementSize) - this.mWheelMiddleItemIndex;
                        if (i > 0) {
                            changeValueByOne(true);
                        } else if (i < 0) {
                            changeValueByOne(false);
                        } else {
                            ensureScrollWheelAdjusted();
                        }
                    } else {
                        ensureScrollWheelAdjusted();
                    }
                    onScrollStateChange(0);
                }
            } else {
                int yVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(yVelocity) > this.mMinimumFlingVelocity) {
                    fling(yVelocity);
                    onScrollStateChange(2);
                } else {
                    int y = (int) motionEvent.getY();
                    if (((int) Math.abs(((float) y) - this.mLastDownEventY)) <= this.mTouchSlop) {
                        int i2 = (y / this.mSelectorElementSize) - this.mWheelMiddleItemIndex;
                        if (i2 > 0) {
                            changeValueByOne(true);
                        } else if (i2 < 0) {
                            changeValueByOne(false);
                        } else {
                            ensureScrollWheelAdjusted();
                        }
                    } else {
                        ensureScrollWheelAdjusted();
                    }
                    onScrollStateChange(0);
                }
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (action == 2) {
            if (isHorizontalMode()) {
                float x2 = motionEvent.getX();
                if (this.mScrollState == 1) {
                    scrollBy((int) (x2 - this.mLastDownOrMoveEventX), 0);
                    invalidate();
                } else if (((int) Math.abs(x2 - this.mLastDownEventX)) > this.mTouchSlop) {
                    removeAllCallbacks();
                    onScrollStateChange(1);
                }
                this.mLastDownOrMoveEventX = x2;
            } else {
                float y2 = motionEvent.getY();
                if (this.mScrollState == 1) {
                    scrollBy(0, (int) (y2 - this.mLastDownOrMoveEventY));
                    invalidate();
                } else if (((int) Math.abs(y2 - this.mLastDownEventY)) > this.mTouchSlop) {
                    removeAllCallbacks();
                    onScrollStateChange(1);
                }
                this.mLastDownOrMoveEventY = y2;
            }
        }
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 1 || action == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0047, code lost:
        requestFocus();
        r5.mLastHandledDownDpadKeyCode = r0;
        removeAllCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
        if (r5.mFlingScroller.isFinished() == false) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
        if (r0 != 20) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0059, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005b, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005c, code lost:
        changeValueByOne(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005f, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchKeyEvent(android.view.KeyEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getKeyCode()
            r1 = 19
            r2 = 20
            if (r0 == r1) goto L_0x0019
            if (r0 == r2) goto L_0x0019
            r1 = 23
            if (r0 == r1) goto L_0x0015
            r1 = 66
            if (r0 == r1) goto L_0x0015
            goto L_0x0060
        L_0x0015:
            r5.removeAllCallbacks()
            goto L_0x0060
        L_0x0019:
            int r1 = r6.getAction()
            r3 = 1
            if (r1 == 0) goto L_0x002b
            if (r1 == r3) goto L_0x0023
            goto L_0x0060
        L_0x0023:
            int r1 = r5.mLastHandledDownDpadKeyCode
            if (r1 != r0) goto L_0x0060
            r6 = -1
            r5.mLastHandledDownDpadKeyCode = r6
            return r3
        L_0x002b:
            boolean r1 = r5.mWrapSelectorWheel
            if (r1 != 0) goto L_0x003d
            if (r0 != r2) goto L_0x0032
            goto L_0x003d
        L_0x0032:
            int r1 = r5.getValue()
            int r4 = r5.getMinValue()
            if (r1 <= r4) goto L_0x0060
            goto L_0x0047
        L_0x003d:
            int r1 = r5.getValue()
            int r4 = r5.getMaxValue()
            if (r1 >= r4) goto L_0x0060
        L_0x0047:
            r5.requestFocus()
            r5.mLastHandledDownDpadKeyCode = r0
            r5.removeAllCallbacks()
            com.ezscreenrecorder.imgedit.fabric.numberpicker.Scroller r6 = r5.mFlingScroller
            boolean r6 = r6.isFinished()
            if (r6 == 0) goto L_0x005f
            if (r0 != r2) goto L_0x005b
            r6 = 1
            goto L_0x005c
        L_0x005b:
            r6 = 0
        L_0x005c:
            r5.changeValueByOne(r6)
        L_0x005f:
            return r3
        L_0x0060:
            boolean r6 = super.dispatchKeyEvent(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.fabric.numberpicker.NumberPicker.dispatchKeyEvent(android.view.KeyEvent):boolean");
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 1 || action == 3) {
            removeAllCallbacks();
        }
        return super.dispatchTrackballEvent(motionEvent);
    }

    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        if (isHorizontalMode()) {
            int currX = scroller.getCurrX();
            if (this.mPreviousScrollerX == 0) {
                this.mPreviousScrollerX = scroller.getStartX();
            }
            scrollBy(currX - this.mPreviousScrollerX, 0);
            this.mPreviousScrollerX = currX;
        } else {
            int currY = scroller.getCurrY();
            if (this.mPreviousScrollerY == 0) {
                this.mPreviousScrollerY = scroller.getStartY();
            }
            scrollBy(0, currY - this.mPreviousScrollerY);
            this.mPreviousScrollerY = currY;
        }
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            invalidate();
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mSelectedText.setEnabled(z);
    }

    public void scrollBy(int i, int i2) {
        int i3;
        int[] selectorIndices = getSelectorIndices();
        if (isHorizontalMode()) {
            if (isAscendingOrder()) {
                if (!this.mWrapSelectorWheel && i > 0 && selectorIndices[this.mWheelMiddleItemIndex] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    return;
                } else if (!this.mWrapSelectorWheel && i < 0 && selectorIndices[this.mWheelMiddleItemIndex] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    return;
                }
            } else if (!this.mWrapSelectorWheel && i > 0 && selectorIndices[this.mWheelMiddleItemIndex] >= this.mMaxValue) {
                this.mCurrentScrollOffset = this.mInitialScrollOffset;
                return;
            } else if (!this.mWrapSelectorWheel && i < 0 && selectorIndices[this.mWheelMiddleItemIndex] <= this.mMinValue) {
                this.mCurrentScrollOffset = this.mInitialScrollOffset;
                return;
            }
            this.mCurrentScrollOffset += i;
            i3 = this.mSelectorTextGapWidth;
        } else {
            if (isAscendingOrder()) {
                if (!this.mWrapSelectorWheel && i2 > 0 && selectorIndices[this.mWheelMiddleItemIndex] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    return;
                } else if (!this.mWrapSelectorWheel && i2 < 0 && selectorIndices[this.mWheelMiddleItemIndex] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    return;
                }
            } else if (!this.mWrapSelectorWheel && i2 > 0 && selectorIndices[this.mWheelMiddleItemIndex] >= this.mMaxValue) {
                this.mCurrentScrollOffset = this.mInitialScrollOffset;
                return;
            } else if (!this.mWrapSelectorWheel && i2 < 0 && selectorIndices[this.mWheelMiddleItemIndex] <= this.mMinValue) {
                this.mCurrentScrollOffset = this.mInitialScrollOffset;
                return;
            }
            this.mCurrentScrollOffset += i2;
            i3 = this.mSelectorTextGapHeight;
        }
        while (true) {
            int i4 = this.mCurrentScrollOffset;
            if (i4 - this.mInitialScrollOffset <= i3) {
                break;
            }
            this.mCurrentScrollOffset = i4 - this.mSelectorElementSize;
            if (isAscendingOrder()) {
                decrementSelectorIndices(selectorIndices);
            } else {
                incrementSelectorIndices(selectorIndices);
            }
            setValueInternal(selectorIndices[this.mWheelMiddleItemIndex], true);
            if (!this.mWrapSelectorWheel && selectorIndices[this.mWheelMiddleItemIndex] < this.mMinValue) {
                this.mCurrentScrollOffset = this.mInitialScrollOffset;
            }
        }
        while (true) {
            int i5 = this.mCurrentScrollOffset;
            if (i5 - this.mInitialScrollOffset < (-i3)) {
                this.mCurrentScrollOffset = i5 + this.mSelectorElementSize;
                if (isAscendingOrder()) {
                    incrementSelectorIndices(selectorIndices);
                } else {
                    decrementSelectorIndices(selectorIndices);
                }
                setValueInternal(selectorIndices[this.mWheelMiddleItemIndex], true);
                if (!this.mWrapSelectorWheel && selectorIndices[this.mWheelMiddleItemIndex] > this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            } else {
                return;
            }
        }
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != this.mFormatter) {
            this.mFormatter = formatter;
            initializeSelectorWheelIndices();
            updateInputTextView();
        }
    }

    public void setValue(int i) {
        setValueInternal(i, false);
    }

    private void tryComputeMaxWidth() {
        int i;
        if (this.mComputeMaxWidth) {
            String[] strArr = this.mDisplayedValues;
            int i2 = 0;
            if (strArr == null) {
                float f = 0.0f;
                for (int i3 = 0; i3 <= 9; i3++) {
                    float measureText = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(i3));
                    if (measureText > f) {
                        f = measureText;
                    }
                }
                for (int i4 = this.mMaxValue; i4 > 0; i4 /= 10) {
                    i2++;
                }
                i = (int) (((float) i2) * f);
            } else {
                int length = strArr.length;
                int i5 = 0;
                while (i2 < length) {
                    float measureText2 = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i2]);
                    if (measureText2 > ((float) i5)) {
                        i5 = (int) measureText2;
                    }
                    i2++;
                }
                i = i5;
            }
            int paddingLeft = i + this.mSelectedText.getPaddingLeft() + this.mSelectedText.getPaddingRight();
            if (this.mMaxWidth != paddingLeft) {
                int i6 = this.mMinWidth;
                if (paddingLeft > i6) {
                    this.mMaxWidth = paddingLeft;
                } else {
                    this.mMaxWidth = i6;
                }
                invalidate();
            }
        }
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean z) {
        boolean z2 = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length;
        if ((!z || z2) && z != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = z;
        }
    }

    public void setOnLongPressUpdateInterval(long j) {
        this.mLongPressUpdateInterval = j;
    }

    public int getValue() {
        return this.mValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int i) {
        this.mMinValue = i;
        int i2 = this.mMinValue;
        if (i2 > this.mValue) {
            this.mValue = i2;
        }
        setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
        initializeSelectorWheelIndices();
        updateInputTextView();
        tryComputeMaxWidth();
        invalidate();
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int i) {
        if (i >= 0) {
            this.mMaxValue = i;
            int i2 = this.mMaxValue;
            if (i2 < this.mValue) {
                this.mValue = i2;
            }
            setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
            initializeSelectorWheelIndices();
            updateInputTextView();
            tryComputeMaxWidth();
            invalidate();
            return;
        }
        throw new IllegalArgumentException("maxValue must be >= 0");
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public void setDisplayedValues(String[] strArr) {
        if (this.mDisplayedValues != strArr) {
            this.mDisplayedValues = strArr;
            if (this.mDisplayedValues != null) {
                this.mSelectedText.setRawInputType(524289);
            } else {
                this.mSelectedText.setRawInputType(2);
            }
            updateInputTextView();
            initializeSelectorWheelIndices();
            tryComputeMaxWidth();
        }
    }

    /* access modifiers changed from: protected */
    public float getTopFadingEdgeStrength() {
        if (isHorizontalMode()) {
            return 0.0f;
        }
        return FADING_EDGE_STRENGTH;
    }

    /* access modifiers changed from: protected */
    public float getBottomFadingEdgeStrength() {
        if (isHorizontalMode()) {
            return 0.0f;
        }
        return FADING_EDGE_STRENGTH;
    }

    /* access modifiers changed from: protected */
    public float getLeftFadingEdgeStrength() {
        if (isHorizontalMode()) {
            return FADING_EDGE_STRENGTH;
        }
        return 0.0f;
    }

    /* access modifiers changed from: protected */
    public float getRightFadingEdgeStrength() {
        if (isHorizontalMode()) {
            return FADING_EDGE_STRENGTH;
        }
        return 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllCallbacks();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        float f;
        if (isHorizontalMode()) {
            f = (float) this.mCurrentScrollOffset;
            i = this.mSelectedText.getBaseline() + this.mSelectedText.getTop();
        } else {
            f = (float) ((getRight() - getLeft()) / 2);
            i = this.mCurrentScrollOffset;
        }
        float f2 = (float) i;
        int[] selectorIndices = getSelectorIndices();
        float f3 = f2;
        float f4 = f;
        for (int i2 = 0; i2 < selectorIndices.length; i2++) {
            if (i2 == this.mWheelMiddleItemIndex) {
                this.mSelectorWheelPaint.setTextSize(this.mSelectedTextSize);
                this.mSelectorWheelPaint.setColor(this.mSelectedTextColor);
            } else {
                this.mSelectorWheelPaint.setTextSize(this.mTextSize);
                this.mSelectorWheelPaint.setColor(this.mTextColor);
            }
            String str = (String) this.mSelectorIndexToStringCache.get(selectorIndices[isAscendingOrder() ? i2 : (selectorIndices.length - i2) - 1]);
            if (!(i2 == this.mWheelMiddleItemIndex && this.mSelectedText.getVisibility() == 0)) {
                canvas.drawText(str, f4, f3, this.mSelectorWheelPaint);
            }
            if (isHorizontalMode()) {
                f4 += (float) this.mSelectorElementSize;
            } else {
                f3 += (float) this.mSelectorElementSize;
            }
        }
        if (this.mSelectionDivider == null) {
            return;
        }
        if (isHorizontalMode()) {
            int i3 = this.mLeftOfSelectionDividerLeft;
            this.mSelectionDivider.setBounds(i3, 0, this.mSelectionDividerThickness + i3, getBottom());
            this.mSelectionDivider.draw(canvas);
            int i4 = this.mRightOfSelectionDividerRight;
            this.mSelectionDivider.setBounds(i4 - this.mSelectionDividerThickness, 0, i4, getBottom());
            this.mSelectionDivider.draw(canvas);
            return;
        }
        int i5 = this.mTopSelectionDividerTop;
        this.mSelectionDivider.setBounds(0, i5, getRight(), this.mSelectionDividerThickness + i5);
        this.mSelectionDivider.draw(canvas);
        int i6 = this.mBottomSelectionDividerBottom;
        this.mSelectionDivider.setBounds(0, i6 - this.mSelectionDividerThickness, getRight(), i6);
        this.mSelectionDivider.draw(canvas);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(NumberPicker.class.getName());
        accessibilityEvent.setScrollable(true);
        int i = this.mMinValue;
        int i2 = this.mValue + i;
        int i3 = this.mSelectorElementSize;
        int i4 = i2 * i3;
        int i5 = (this.mMaxValue - i) * i3;
        if (isHorizontalMode()) {
            accessibilityEvent.setScrollX(i4);
            accessibilityEvent.setMaxScrollX(i5);
            return;
        }
        accessibilityEvent.setScrollY(i4);
        accessibilityEvent.setMaxScrollY(i5);
    }

    private int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            return MeasureSpec.makeMeasureSpec(Math.min(size, i2), Ints.MAX_POWER_OF_TWO);
        }
        if (mode == 0) {
            return MeasureSpec.makeMeasureSpec(i2, Ints.MAX_POWER_OF_TWO);
        }
        if (mode == 1073741824) {
            return i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown measure mode: ");
        sb.append(mode);
        throw new IllegalArgumentException(sb.toString());
    }

    private int resolveSizeAndStateRespectingMinSize(int i, int i2, int i3) {
        return i != -1 ? resolveSizeAndState(Math.max(i, i2), i3, 0) : i2;
    }

    public static int resolveSizeAndState(int i, int i2, int i3) {
        int mode = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i2);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0 && mode == 1073741824) {
                i = size;
            }
        } else if (size < i) {
            i = 16777216 | size;
        }
        return i | (-16777216 & i3);
    }

    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] selectorIndices = getSelectorIndices();
        int value = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int i2 = (i - this.mWheelMiddleItemIndex) + value;
            if (this.mWrapSelectorWheel) {
                i2 = getWrappedSelectorIndex(i2);
            }
            selectorIndices[i] = i2;
            ensureCachedScrollSelectorValue(selectorIndices[i]);
        }
    }

    private void setValueInternal(int i, boolean z) {
        int i2;
        if (this.mValue != i) {
            if (this.mWrapSelectorWheel) {
                i2 = getWrappedSelectorIndex(i);
            } else {
                i2 = Math.min(Math.max(i, this.mMinValue), this.mMaxValue);
            }
            int i3 = this.mValue;
            this.mValue = i2;
            updateInputTextView();
            if (z) {
                notifyChange(i3, i2);
            }
            initializeSelectorWheelIndices();
            invalidate();
        }
    }

    /* access modifiers changed from: private */
    public void changeValueByOne(boolean z) {
        this.mSelectedText.setVisibility(4);
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        if (isHorizontalMode()) {
            this.mPreviousScrollerX = 0;
            if (z) {
                this.mFlingScroller.startScroll(0, 0, -this.mSelectorElementSize, 0, 300);
            } else {
                this.mFlingScroller.startScroll(0, 0, this.mSelectorElementSize, 0, 300);
            }
        } else {
            this.mPreviousScrollerY = 0;
            if (z) {
                this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementSize, 300);
            } else {
                this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementSize, 300);
            }
        }
        invalidate();
    }

    private void initializeSelectorWheel() {
        int i;
        initializeSelectorWheelIndices();
        int[] selectorIndices = getSelectorIndices();
        int length = selectorIndices.length * ((int) this.mTextSize);
        float length2 = (float) selectorIndices.length;
        if (isHorizontalMode()) {
            this.mSelectorTextGapWidth = (int) ((((float) ((getRight() - getLeft()) - length)) / length2) + 0.5f);
            this.mSelectorElementSize = ((int) this.mTextSize) + this.mSelectorTextGapWidth;
            i = this.mSelectedText.getRight() / 2;
        } else {
            this.mSelectorTextGapHeight = (int) ((((float) ((getBottom() - getTop()) - length)) / length2) + 0.5f);
            this.mSelectorElementSize = ((int) this.mTextSize) + this.mSelectorTextGapHeight;
            i = this.mSelectedText.getBaseline() + this.mSelectedText.getTop();
        }
        this.mInitialScrollOffset = i - (this.mSelectorElementSize * this.mWheelMiddleItemIndex);
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        updateInputTextView();
    }

    private void initializeFadingEdges() {
        if (isHorizontalMode()) {
            setHorizontalFadingEdgeEnabled(true);
            setFadingEdgeLength(((getRight() - getLeft()) - ((int) this.mTextSize)) / 2);
            return;
        }
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(((getBottom() - getTop()) - ((int) this.mTextSize)) / 2);
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }
            onScrollStateChange(0);
        } else if (this.mScrollState != 1) {
            updateInputTextView();
        }
    }

    private void onScrollStateChange(int i) {
        if (this.mScrollState != i) {
            this.mScrollState = i;
            OnScrollListener onScrollListener = this.mOnScrollListener;
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChange(this, i);
            }
        }
    }

    private void fling(int i) {
        if (isHorizontalMode()) {
            this.mPreviousScrollerX = 0;
            if (i > 0) {
                this.mFlingScroller.fling(0, 0, i, 0, 0, Integer.MAX_VALUE, 0, 0);
            } else {
                this.mFlingScroller.fling(Integer.MAX_VALUE, 0, i, 0, 0, Integer.MAX_VALUE, 0, 0);
            }
        } else {
            this.mPreviousScrollerY = 0;
            if (i > 0) {
                this.mFlingScroller.fling(0, 0, 0, i, 0, 0, 0, Integer.MAX_VALUE);
            } else {
                this.mFlingScroller.fling(0, Integer.MAX_VALUE, 0, i, 0, 0, 0, Integer.MAX_VALUE);
            }
        }
        invalidate();
    }

    private int getWrappedSelectorIndex(int i) {
        int i2 = this.mMaxValue;
        if (i > i2) {
            int i3 = this.mMinValue;
            return (i3 + ((i - i2) % (i2 - i3))) - 1;
        }
        int i4 = this.mMinValue;
        return i < i4 ? (i2 - ((i4 - i) % (i2 - i4))) + 1 : i;
    }

    private int[] getSelectorIndices() {
        return this.mSelectorIndices;
    }

    private void incrementSelectorIndices(int[] iArr) {
        int i = 0;
        while (i < iArr.length - 1) {
            int i2 = i + 1;
            iArr[i] = iArr[i2];
            i = i2;
        }
        int i3 = iArr[iArr.length - 2] + 1;
        if (this.mWrapSelectorWheel && i3 > this.mMaxValue) {
            i3 = this.mMinValue;
        }
        iArr[iArr.length - 1] = i3;
        ensureCachedScrollSelectorValue(i3);
    }

    private void decrementSelectorIndices(int[] iArr) {
        for (int length = iArr.length - 1; length > 0; length--) {
            iArr[length] = iArr[length - 1];
        }
        int i = iArr[1] - 1;
        if (this.mWrapSelectorWheel && i < this.mMinValue) {
            i = this.mMaxValue;
        }
        iArr[0] = i;
        ensureCachedScrollSelectorValue(i);
    }

    private void ensureCachedScrollSelectorValue(int i) {
        String str;
        SparseArray<String> sparseArray = this.mSelectorIndexToStringCache;
        if (((String) sparseArray.get(i)) == null) {
            int i2 = this.mMinValue;
            if (i < i2 || i > this.mMaxValue) {
                str = "";
            } else {
                String[] strArr = this.mDisplayedValues;
                str = strArr != null ? strArr[i - i2] : formatNumber(i);
            }
            sparseArray.put(i, str);
        }
    }

    private String formatNumber(int i) {
        Formatter formatter = this.mFormatter;
        return formatter != null ? formatter.format(i) : formatNumberWithLocale(i);
    }

    private boolean updateInputTextView() {
        String[] strArr = this.mDisplayedValues;
        String formatNumber = strArr == null ? formatNumber(this.mValue) : strArr[this.mValue - this.mMinValue];
        if (TextUtils.isEmpty(formatNumber) || formatNumber.equals(this.mSelectedText.getText().toString())) {
            return false;
        }
        this.mSelectedText.setText(formatNumber);
        return true;
    }

    private void notifyChange(int i, int i2) {
        OnValueChangeListener onValueChangeListener = this.mOnValueChangeListener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(this, i, this.mValue);
        }
    }

    private void postChangeCurrentByOneFromLongPress(boolean z, long j) {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(z);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j);
    }

    private void removeChangeCurrentByOneFromLongPress() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    private void removeAllCallbacks() {
        ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand = this.mChangeCurrentByOneFromLongPressCommand;
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand != null) {
            removeCallbacks(setSelectionCommand);
        }
    }

    /* access modifiers changed from: private */
    public int getSelectedPos(String str) {
        if (this.mDisplayedValues == null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException unused) {
                return this.mMinValue;
            }
        } else {
            for (int i = 0; i < this.mDisplayedValues.length; i++) {
                str = str.toLowerCase();
                if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return this.mMinValue + i;
                }
            }
            return Integer.parseInt(str);
        }
    }

    /* access modifiers changed from: private */
    public void postSetSelectionCommand(int i, int i2) {
        SetSelectionCommand setSelectionCommand = this.mSetSelectionCommand;
        if (setSelectionCommand == null) {
            this.mSetSelectionCommand = new SetSelectionCommand();
        } else {
            removeCallbacks(setSelectionCommand);
        }
        this.mSetSelectionCommand.mSelectionStart = i;
        this.mSetSelectionCommand.mSelectionEnd = i2;
        post(this.mSetSelectionCommand);
    }

    private boolean ensureScrollWheelAdjusted() {
        int i = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (i == 0) {
            return false;
        }
        int abs = Math.abs(i);
        int i2 = this.mSelectorElementSize;
        if (abs > i2 / 2) {
            if (i > 0) {
                i2 = -i2;
            }
            i += i2;
        }
        int i3 = i;
        if (isHorizontalMode()) {
            this.mPreviousScrollerX = 0;
            this.mAdjustScroller.startScroll(0, 0, i3, 0, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
        } else {
            this.mPreviousScrollerY = 0;
            this.mAdjustScroller.startScroll(0, 0, 0, i3, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
        }
        invalidate();
        return true;
    }

    private String formatNumberWithLocale(int i) {
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(i)});
    }

    private void setWidthAndHeight() {
        if (isHorizontalMode()) {
            this.mMinHeight = -1;
            this.mMaxHeight = (int) dpToPx(64.0f);
            this.mMinWidth = (int) dpToPx(180.0f);
            this.mMaxWidth = -1;
            return;
        }
        this.mMinHeight = -1;
        this.mMaxHeight = (int) dpToPx(180.0f);
        this.mMinWidth = (int) dpToPx(64.0f);
        this.mMaxWidth = -1;
    }

    public void setDividerColor(@ColorInt int i) {
        this.mSelectionDividerColor = i;
        this.mSelectionDivider = new ColorDrawable(i);
    }

    public void setDividerColorResource(@ColorRes int i) {
        setDividerColor(ContextCompat.getColor(this.mContext, i));
    }

    public void setDividerDistance(int i) {
        this.mSelectionDividersDistance = (int) dpToPx((float) i);
    }

    public void setDividerThickness(int i) {
        this.mSelectionDividerThickness = (int) dpToPx((float) i);
    }

    public void setOrder(int i) {
        this.mOrder = i;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
        setWidthAndHeight();
    }

    public void setWheelItemCount(int i) {
        this.mWheelItemCount = i;
        int i2 = this.mWheelItemCount;
        this.mWheelMiddleItemIndex = i2 / 2;
        this.mSelectorIndices = new int[i2];
    }

    public void setFormatter(String str) {
        if (!TextUtils.isEmpty(str)) {
            setFormatter(stringToFormatter(str));
        }
    }

    public void setFormatter(@StringRes int i) {
        setFormatter(getResources().getString(i));
    }

    public void setSelectedTextColor(@ColorInt int i) {
        this.mSelectedTextColor = i;
        this.mSelectedText.setTextColor(this.mSelectedTextColor);
    }

    public void setSelectedTextColorResource(@ColorRes int i) {
        setSelectedTextColor(ContextCompat.getColor(this.mContext, i));
    }

    public void setSelectedTextSize(float f) {
        this.mSelectedTextSize = f;
        this.mSelectedText.setTextSize(pxToSp(this.mSelectedTextSize));
    }

    public void setSelectedTextSize(@DimenRes int i) {
        setSelectedTextSize(getResources().getDimension(i));
    }

    public void setTextColor(@ColorInt int i) {
        this.mTextColor = i;
        this.mSelectorWheelPaint.setColor(this.mTextColor);
    }

    public void setTextColorResource(@ColorRes int i) {
        setTextColor(ContextCompat.getColor(this.mContext, i));
    }

    public void setTextSize(float f) {
        this.mTextSize = f;
        this.mSelectorWheelPaint.setTextSize(this.mTextSize);
    }

    public void setTextSize(@DimenRes int i) {
        setTextSize(getResources().getDimension(i));
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        Typeface typeface2 = this.mTypeface;
        if (typeface2 != null) {
            this.mSelectedText.setTypeface(typeface2);
            this.mSelectorWheelPaint.setTypeface(this.mTypeface);
            return;
        }
        this.mSelectedText.setTypeface(Typeface.MONOSPACE);
        this.mSelectorWheelPaint.setTypeface(Typeface.MONOSPACE);
    }

    public void setTypeface(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            setTypeface(Typeface.create(str, i));
        }
    }

    public void setTypeface(String str) {
        setTypeface(str, 0);
    }

    public void setTypeface(@StringRes int i, int i2) {
        setTypeface(getResources().getString(i), i2);
    }

    public void setTypeface(@StringRes int i) {
        setTypeface(i, 0);
    }

    private Formatter stringToFormatter(final String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new Formatter() {
            public String format(int i) {
                return String.format(Locale.getDefault(), str, new Object[]{Integer.valueOf(i)});
            }
        };
    }

    private float dpToPx(float f) {
        return f * getResources().getDisplayMetrics().density;
    }

    private float pxToDp(float f) {
        return f / getResources().getDisplayMetrics().density;
    }

    private float spToPx(float f) {
        return TypedValue.applyDimension(2, f, getResources().getDisplayMetrics());
    }

    private float pxToSp(float f) {
        return f / getResources().getDisplayMetrics().scaledDensity;
    }

    public boolean isHorizontalMode() {
        return getOrientation() == 0;
    }

    public boolean isAscendingOrder() {
        return getOrder() == 0;
    }

    public int getDividerColor() {
        return this.mSelectionDividerColor;
    }

    public float getDividerDistance() {
        return pxToDp((float) this.mSelectionDividersDistance);
    }

    public float getDividerThickness() {
        return pxToDp((float) this.mSelectionDividerThickness);
    }

    public int getOrder() {
        return this.mOrder;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getWheelItemCount() {
        return this.mWheelItemCount;
    }

    public Formatter getFormatter() {
        return this.mFormatter;
    }

    public int getSelectedTextColor() {
        return this.mSelectedTextColor;
    }

    public float getSelectedTextSize() {
        return this.mSelectedTextSize;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public float getTextSize() {
        return spToPx(this.mTextSize);
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }
}
