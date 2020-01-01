package com.yasoka.eazyscreenrecord.imgedit.fabric;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private boolean mDrawMode;
    private float mEraserSize;
    private Paint mPaint;
    private Path mPath;
    private float mPenSize;

    /* renamed from: mX */
    private float f107mX;

    /* renamed from: mY */
    private float f108mY;

    /* renamed from: com.ezscreenrecorder.imgedit.fabric.DrawingView$1 */
    static /* synthetic */ class C12441 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat = new int[CompressFormat.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                android.graphics.Bitmap$CompressFormat[] r0 = android.graphics.Bitmap.CompressFormat.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$graphics$Bitmap$CompressFormat = r0
                int[] r0 = $SwitchMap$android$graphics$Bitmap$CompressFormat     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$CompressFormat     // Catch:{ NoSuchFieldError -> 0x001f }
                android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.fabric.DrawingView.C12441.<clinit>():void");
        }
    }

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPenSize = 10.0f;
        this.mEraserSize = 10.0f;
        init();
    }

    private void init() {
        this.mPath = new Path();
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(this.mPenSize);
        this.mDrawMode = true;
        this.mPaint.setXfermode(null);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mBitmap == null) {
            this.mBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        }
        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.drawColor(0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mBitmapPaint);
        canvas.drawPath(this.mPath, this.mPaint);
    }

    private void touch_start(float f, float f2) {
        this.mPath.reset();
        this.mPath.moveTo(f, f2);
        this.f107mX = f;
        this.f108mY = f2;
        this.mCanvas.drawPath(this.mPath, this.mPaint);
    }

    private void touch_move(float f, float f2) {
        float abs = Math.abs(f - this.f107mX);
        float abs2 = Math.abs(f2 - this.f108mY);
        if (abs >= TOUCH_TOLERANCE || abs2 >= TOUCH_TOLERANCE) {
            Path path = this.mPath;
            float f3 = this.f107mX;
            float f4 = this.f108mY;
            path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
            this.f107mX = f;
            this.f108mY = f2;
        }
        this.mCanvas.drawPath(this.mPath, this.mPaint);
    }

    private void touch_up() {
        this.mPath.lineTo(this.f107mX, this.f108mY);
        this.mCanvas.drawPath(this.mPath, this.mPaint);
        this.mPath.reset();
        if (this.mDrawMode) {
            this.mPaint.setXfermode(null);
        } else {
            this.mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            if (!this.mDrawMode) {
                this.mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            } else {
                this.mPaint.setXfermode(null);
            }
            touch_start(x, y);
            invalidate();
        } else if (action == 1) {
            touch_up();
            invalidate();
        } else if (action == 2) {
            touch_move(x, y);
            if (!this.mDrawMode) {
                this.mPath.lineTo(this.f107mX, this.f108mY);
                this.mPath.reset();
                this.mPath.moveTo(x, y);
            }
            this.mCanvas.drawPath(this.mPath, this.mPaint);
            invalidate();
        }
        return true;
    }

    public void initializePen() {
        this.mDrawMode = true;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(this.mPenSize);
        this.mPaint.setXfermode(null);
    }

    public void initializeEraser() {
        this.mDrawMode = false;
        this.mPaint.setColor(Color.parseColor("#f4f4f4"));
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(this.mEraserSize * 2.7f);
        this.mPaint.setStrokeMiter(50.0f);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    }

    public void setBackgroundColor(int i) {
        this.mCanvas.drawColor(i);
        super.setBackgroundColor(i);
    }

    public float getEraserSize() {
        return this.mEraserSize;
    }

    public void setEraserSize(float f) {
        this.mEraserSize = f;
        initializeEraser();
    }

    public void clearBackground() {
        this.mCanvas.drawColor(0, Mode.CLEAR);
        invalidate();
    }

    public float getPenSize() {
        return this.mPenSize;
    }

    public void setPenSize(float f) {
        this.mPenSize = f;
        initializePen();
    }

    @ColorInt
    public int getPenColor() {
        return this.mPaint.getColor();
    }

    public void setPenColor(@ColorInt int i) {
        this.mPaint.setColor(i);
    }

    public void loadImage(Bitmap bitmap) {
        this.mBitmap = bitmap.copy(Config.ARGB_8888, true);
        bitmap.recycle();
        invalidate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ac A[SYNTHETIC, Splitter:B:47:0x00ac] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00b7 A[SYNTHETIC, Splitter:B:53:0x00b7] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveImage(String r5, String r6, CompressFormat r7, int r8) {
        /*
            r4 = this;
            r0 = 0
            r1 = 100
            if (r8 <= r1) goto L_0x000d
            java.lang.String r5 = "saveImage"
            java.lang.String r6 = "quality cannot be greater that 100"
            android.util.Log.d(r5, r6)
            return r0
        L_0x000d:
            r1 = 0
            int[] r2 = com.ezscreenrecorder.imgedit.fabric.DrawingView.C12441.$SwitchMap$android$graphics$Bitmap$CompressFormat     // Catch:{ Exception -> 0x00a6 }
            int r7 = r7.ordinal()     // Catch:{ Exception -> 0x00a6 }
            r7 = r2[r7]     // Catch:{ Exception -> 0x00a6 }
            r2 = 1
            java.lang.String r3 = ".png"
            if (r7 == r2) goto L_0x007a
            r2 = 2
            if (r7 == r2) goto L_0x004e
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a6 }
            r2.<init>()     // Catch:{ Exception -> 0x00a6 }
            r2.append(r6)     // Catch:{ Exception -> 0x00a6 }
            r2.append(r3)     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r6 = r2.toString()     // Catch:{ Exception -> 0x00a6 }
            r7.<init>(r5, r6)     // Catch:{ Exception -> 0x00a6 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00a6 }
            r5.<init>(r7)     // Catch:{ Exception -> 0x00a6 }
            android.graphics.Bitmap r6 = r4.mBitmap     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            android.graphics.Bitmap$CompressFormat r7 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            boolean r6 = r6.compress(r7, r8, r5)     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            r5.close()     // Catch:{ IOException -> 0x0043 }
            goto L_0x0047
        L_0x0043:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0047:
            return r6
        L_0x0048:
            r6 = move-exception
            r1 = r5
            goto L_0x00b5
        L_0x004b:
            r6 = move-exception
            r1 = r5
            goto L_0x00a7
        L_0x004e:
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a6 }
            r2.<init>()     // Catch:{ Exception -> 0x00a6 }
            r2.append(r6)     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r6 = ".jpg"
            r2.append(r6)     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r6 = r2.toString()     // Catch:{ Exception -> 0x00a6 }
            r7.<init>(r5, r6)     // Catch:{ Exception -> 0x00a6 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00a6 }
            r5.<init>(r7)     // Catch:{ Exception -> 0x00a6 }
            android.graphics.Bitmap r6 = r4.mBitmap     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            android.graphics.Bitmap$CompressFormat r7 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            boolean r6 = r6.compress(r7, r8, r5)     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            r5.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0079
        L_0x0075:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0079:
            return r6
        L_0x007a:
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a6 }
            r2.<init>()     // Catch:{ Exception -> 0x00a6 }
            r2.append(r6)     // Catch:{ Exception -> 0x00a6 }
            r2.append(r3)     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r6 = r2.toString()     // Catch:{ Exception -> 0x00a6 }
            r7.<init>(r5, r6)     // Catch:{ Exception -> 0x00a6 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00a6 }
            r5.<init>(r7)     // Catch:{ Exception -> 0x00a6 }
            android.graphics.Bitmap r6 = r4.mBitmap     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            android.graphics.Bitmap$CompressFormat r7 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            boolean r6 = r6.compress(r7, r8, r5)     // Catch:{ Exception -> 0x004b, all -> 0x0048 }
            r5.close()     // Catch:{ IOException -> 0x009f }
            goto L_0x00a3
        L_0x009f:
            r5 = move-exception
            r5.printStackTrace()
        L_0x00a3:
            return r6
        L_0x00a4:
            r6 = move-exception
            goto L_0x00b5
        L_0x00a6:
            r6 = move-exception
        L_0x00a7:
            r6.printStackTrace()     // Catch:{ all -> 0x00a4 }
            if (r1 == 0) goto L_0x00b4
            r1.close()     // Catch:{ IOException -> 0x00b0 }
            goto L_0x00b4
        L_0x00b0:
            r5 = move-exception
            r5.printStackTrace()
        L_0x00b4:
            return r0
        L_0x00b5:
            if (r1 == 0) goto L_0x00bf
            r1.close()     // Catch:{ IOException -> 0x00bb }
            goto L_0x00bf
        L_0x00bb:
            r5 = move-exception
            r5.printStackTrace()
        L_0x00bf:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.fabric.DrawingView.saveImage(java.lang.String, java.lang.String, android.graphics.Bitmap$CompressFormat, int):boolean");
    }
}
