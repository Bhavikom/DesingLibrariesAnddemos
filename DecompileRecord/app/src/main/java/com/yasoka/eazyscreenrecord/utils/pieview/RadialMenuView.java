package com.yasoka.eazyscreenrecord.utils.pieview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.io.PrintStream;
import java.util.ArrayList;

public class RadialMenuView extends View {
    private float CIRCLE_RAD = 40.0f;
    boolean alt;
    private float centerX;
    private float centerY;
    float[] endTouch;
    int lastE = -1;
    private Paint mBgPaint = new Paint(1);
    private Paint mBorderPaint = new Paint(1);
    float mHeight = -1.0f;
    private RadialMenuHelperFunctions mHelperFunctions = new RadialMenuHelperFunctions();
    private ArrayList<RadialMenuItem> mRadialMenuContent = new ArrayList<>(0);
    float mRadius;
    private Paint mSelectedPaint = new Paint(1);
    private Paint mTextPaint = new Paint(1);
    float mThickness;
    float mWidth = -1.0f;
    int selected = -1;

    public RadialMenuView(Context context, RadialMenuRenderer radialMenuRenderer) {
        super(context);
        this.mRadialMenuContent = radialMenuRenderer.getRadialMenuContent();
        this.alt = radialMenuRenderer.isAlt();
        this.mThickness = radialMenuRenderer.getMenuThickness();
        this.mRadius = radialMenuRenderer.getRadius();
        setVisibility(8);
        initSetPaint(radialMenuRenderer);
    }

    private void initSetPaint(RadialMenuRenderer radialMenuRenderer) {
        this.mBgPaint.setColor(radialMenuRenderer.getMenuBackgroundColor());
        this.mBgPaint.setStrokeWidth(radialMenuRenderer.getMenuThickness());
        this.mBgPaint.setStyle(Style.STROKE);
        this.mSelectedPaint.setColor(radialMenuRenderer.getMenuSelectedColor());
        this.mSelectedPaint.setStrokeWidth(radialMenuRenderer.getMenuThickness());
        this.mSelectedPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setColor(radialMenuRenderer.getMenuBorderColor());
        this.mBorderPaint.setStrokeWidth(radialMenuRenderer.getMenuThickness());
        this.mBorderPaint.setStyle(Style.STROKE);
    }

    public void setLoc(float f, float f2) {
        float f3 = this.mRadius;
        float f4 = this.mThickness;
        if (f < (f4 / 2.0f) + f3) {
            f = f3 + (f4 / 2.0f);
        }
        float f5 = this.mRadius;
        float f6 = this.mThickness;
        if (f2 < (f6 / 2.0f) + f5) {
            f2 = f5 + (f6 / 2.0f);
        }
        if (f2 > ((float) getHeight()) - (this.mRadius + (this.mThickness / 2.0f))) {
            f2 = ((float) getHeight()) - (this.mRadius + (this.mThickness / 2.0f));
        }
        if (f > ((float) getWidth()) - (this.mRadius + (this.mThickness / 2.0f))) {
            f = ((float) getWidth()) - (this.mRadius + (this.mThickness / 2.0f));
        }
        this.mWidth = f;
        this.mHeight = f2;
    }

    public int pxToDp2(int i) {
        return Math.round(((float) i) / (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public void onDraw(Canvas canvas) {
        String str;
        setLoc(this.mWidth, this.mHeight);
        RectF rectF = new RectF();
        float f = this.mWidth;
        float f2 = this.mRadius;
        float f3 = f - f2;
        float f4 = this.mHeight;
        rectF.set(f3, f4 - f2, f + f2, f4 + f2);
        int size = this.mRadialMenuContent.size();
        this.mBorderPaint.setStrokeWidth(this.mThickness);
        int i = 0;
        while (true) {
            str = RadialMenuRenderer.RADIAL_NO_TEXT;
            if (i >= size) {
                break;
            }
            if (!((RadialMenuItem) this.mRadialMenuContent.get(i)).equals(str)) {
                if (this.alt) {
                    int i2 = 360 / size;
                    canvas.drawArc(rectF, (float) (((i2 * i) - 90) - (i2 / 2)), (float) i2, false, this.selected == i ? this.mSelectedPaint : this.mBgPaint);
                } else {
                    int i3 = 360 / size;
                    canvas.drawArc(rectF, (float) ((i3 * i) - 90), (float) i3, false, this.selected == i ? this.mSelectedPaint : this.mBgPaint);
                }
            }
            i++;
        }
        double d = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        this.centerX = rectF.centerX();
        this.centerY = rectF.centerY();
        int i4 = 0;
        while (d < 360.0d && i4 < size) {
            float f5 = (float) ((3.141592653589793d * d) / 180.0d);
            float f6 = this.mRadius;
            double d2 = (double) f5;
            float cos = (float) (((double) (((this.mThickness / 4.0f) + f6) - 5.0f)) + (((double) f6) * Math.cos(d2)));
            float f7 = this.mRadius;
            canvas.drawBitmap(((RadialMenuItem) this.mRadialMenuContent.get(i4)).getDrawableId(), cos, (float) (((double) (((this.mThickness / 4.0f) + f7) - 10.0f)) + (((double) f7) * Math.sin(d2))), null);
            d += (double) (360 / size);
            i4++;
        }
        Canvas canvas2 = canvas;
        if (size > 1) {
            for (int i5 = 0; i5 < size; i5++) {
                if (!((RadialMenuItem) this.mRadialMenuContent.get(i5)).equals(str)) {
                    if (this.alt) {
                        int i6 = 360 / size;
                        int i7 = i6 / 2;
                        RectF rectF2 = rectF;
                        canvas.drawArc(rectF2, (float) (((i6 * i5) - 91) - i7), 2.0f, false, this.mBorderPaint);
                        canvas.drawArc(rectF2, (float) (((i6 * (i5 + 1)) - 91) - i7), 2.0f, false, this.mBorderPaint);
                    } else {
                        int i8 = 360 / size;
                        RectF rectF3 = rectF;
                        canvas.drawArc(rectF3, (float) ((i8 * i5) - 91), 2.0f, false, this.mBorderPaint);
                        canvas.drawArc(rectF3, (float) ((i8 * (i5 + 1)) - 91), 2.0f, false, this.mBorderPaint);
                    }
                }
            }
        }
        this.mBorderPaint.setStrokeWidth(2.0f);
        float f8 = this.mWidth;
        float f9 = this.mRadius;
        float f10 = f8 - f9;
        float f11 = this.mThickness;
        float f12 = f10 - (f11 / 2.0f);
        float f13 = this.mHeight;
        rectF.set(f12, (f13 - f9) - (f11 / 2.0f), f8 + f9 + (f11 / 2.0f), f13 + f9 + (f11 / 2.0f));
        float f14 = this.mWidth;
        float f15 = this.mRadius;
        float f16 = f14 - f15;
        float f17 = this.mThickness;
        float f18 = f16 + (f17 / 2.0f);
        float f19 = this.mHeight;
        rectF.set(f18, (f19 - f15) + (f17 / 2.0f), (f14 + f15) - (f17 / 2.0f), (f19 + f15) - (f17 / 2.0f));
    }

    private boolean handleEvent(int i) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("SDSD>>");
        sb.append(i);
        printStream.println(sb.toString());
        if (i == this.mRadialMenuContent.size()) {
            i = 0;
        } else if (i == -1) {
            this.selected = -1;
            return false;
        }
        if (((RadialMenuItem) this.mRadialMenuContent.get(i)).getMenuName().equals(RadialMenuRenderer.RADIAL_NO_TEXT)) {
            this.selected = -1;
            invalidate();
            return false;
        }
        if (((RadialMenuItem) this.mRadialMenuContent.get(i)).getOnRadailMenuClick() != null) {
            ((RadialMenuItem) this.mRadialMenuContent.get(i)).getOnRadailMenuClick().onRadailMenuClickedListener(((RadialMenuItem) this.mRadialMenuContent.get(i)).getMenuID());
        }
        this.selected = -1;
        invalidate();
        return true;
    }

    private void preEvent(int i) {
        if (i == this.mRadialMenuContent.size()) {
            i = 0;
        }
        if (i == -1) {
            this.selected = -1;
            invalidate();
        } else if (((RadialMenuItem) this.mRadialMenuContent.get(i)).getMenuName().equals(RadialMenuRenderer.RADIAL_NO_TEXT)) {
            this.selected = -1;
            invalidate();
        } else {
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("PRE>>");
            sb.append(i);
            printStream.println(sb.toString());
            this.selected = i;
            invalidate();
        }
    }

    public boolean gestureHandler(MotionEvent motionEvent, boolean z) {
        if (motionEvent.getAction() == 1) {
            this.endTouch = new float[]{motionEvent.getX(), motionEvent.getY()};
            RadialMenuHelperFunctions radialMenuHelperFunctions = this.mHelperFunctions;
            float f = this.mWidth;
            float f2 = this.mHeight;
            float[] fArr = this.endTouch;
            if (radialMenuHelperFunctions.distance(f, f2, fArr[0], fArr[1]) <= this.mRadius - (this.mThickness / 2.0f)) {
                return handleEvent(-1);
            }
            RadialMenuHelperFunctions radialMenuHelperFunctions2 = this.mHelperFunctions;
            float f3 = this.mWidth;
            float f4 = this.mHeight;
            float[] fArr2 = this.endTouch;
            return handleEvent((int) radialMenuHelperFunctions2.angle(f3, f4, fArr2[0], fArr2[1], this.alt, this.mRadialMenuContent.size()));
        }
        if (motionEvent.getAction() == 0) {
            this.endTouch = new float[]{motionEvent.getX(), motionEvent.getY()};
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("SD>>");
            RadialMenuHelperFunctions radialMenuHelperFunctions3 = this.mHelperFunctions;
            float f5 = this.mWidth;
            float f6 = this.mHeight;
            float[] fArr3 = this.endTouch;
            sb.append(radialMenuHelperFunctions3.distance(f5, f6, fArr3[0], fArr3[1]) > this.mRadius - (this.mThickness / 2.0f));
            printStream.println(sb.toString());
            RadialMenuHelperFunctions radialMenuHelperFunctions4 = this.mHelperFunctions;
            float f7 = this.mWidth;
            float f8 = this.mHeight;
            float[] fArr4 = this.endTouch;
            if (radialMenuHelperFunctions4.distance(f7, f8, fArr4[0], fArr4[1]) > this.mRadius - (this.mThickness / 2.0f)) {
                RadialMenuHelperFunctions radialMenuHelperFunctions5 = this.mHelperFunctions;
                float f9 = this.mWidth;
                float f10 = this.mHeight;
                float[] fArr5 = this.endTouch;
                preEvent((int) radialMenuHelperFunctions5.angle(f9, f10, fArr5[0], fArr5[1], this.alt, this.mRadialMenuContent.size()));
            } else {
                preEvent(-1);
            }
        }
        return z;
    }

    private boolean checkInCircleRange(float f, float f2) {
        float f3 = this.centerX;
        float f4 = this.CIRCLE_RAD;
        if (f >= f3 - f4 && f <= f3 + f4) {
            float f5 = this.centerY;
            if (f2 >= f5 - f4 && f2 <= f5 + f4) {
                return true;
            }
        }
        return false;
    }
}
