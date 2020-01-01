package com.yasoka.eazyscreenrecord.float_camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.p000v4.content.ContextCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ezscreenrecorder.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.io.PrintStream;

public abstract class StickerView extends FrameLayout {
    private static final int BUTTON_SIZE_DP = 25;
    private static final int SELF_SIZE_DP = 100;
    public static final String TAG = "com.knef.stickerView";
    /* access modifiers changed from: private */
    public double centerX;
    /* access modifiers changed from: private */
    public double centerY;
    private float f9809e;
    private float f9810f;
    private float f9811g;
    private float f9812h;
    private float f9813i;
    private float f9814j;
    private float f9815k;
    private float f9816l;
    private float f9817m;
    private float f9818n;
    private float f9819o;
    private float f9820p;
    private boolean f9821q;
    public int height = 417;
    private boolean isHidden;
    private BorderView iv_border;
    private ImageView iv_delete;
    private ImageView iv_flip;
    private ImageView iv_scale;
    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!view.getTag().equals("DraggableViewGroup") && view.getTag().equals("iv_scale")) {
                int action = motionEvent.getAction();
                String str = "<>";
                String str2 = "com.knef.stickerView";
                if (action == 0) {
                    String str3 = str;
                    StringBuilder sb = new StringBuilder();
                    sb.append("iv_scale action down:");
                    sb.append(motionEvent.getRawX());
                    sb.append(str3);
                    sb.append(motionEvent.getRawY());
                    Log.v(str2, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("iv_scale action down2:");
                    sb2.append(StickerView.this.getLayoutParams().width);
                    sb2.append(str3);
                    sb2.append(StickerView.this.getLayoutParams().height);
                    Log.v(str2, sb2.toString());
                    StickerView.this.scale_orgX = motionEvent.getRawX();
                    StickerView.this.scale_orgY = motionEvent.getRawY();
                    StickerView stickerView = StickerView.this;
                    stickerView.centerX = (double) (stickerView.getX() + ((View) StickerView.this.getParent()).getX() + (((float) StickerView.this.getWidth()) / 2.0f));
                    int identifier = StickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (identifier > 0) {
                        StickerView.this.getResources().getDimensionPixelSize(identifier);
                    }
                    StickerView stickerView2 = StickerView.this;
                    stickerView2.centerY = ((double) (stickerView2.getY() + ((View) StickerView.this.getParent()).getY())) + FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE + ((double) (((float) StickerView.this.getHeight()) / 2.0f));
                } else if (action == 1) {
                    Log.v(str2, "iv_scale action up");
                } else if (action == 2) {
                    Log.v(str2, "iv_scale action move");
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("FDF-->");
                    sb3.append(StickerView.this.getLayoutParams().width);
                    sb3.append("<>><");
                    sb3.append(StickerView.this.getLayoutParams().height);
                    printStream.println(sb3.toString());
                    if ((StickerView.this.getLayoutParams().width < 600 || StickerView.this.getLayoutParams().height < 600) && (StickerView.this.getLayoutParams().width > 290 || StickerView.this.getLayoutParams().height > 290)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("e_diff: ");
                        sb4.append(motionEvent.getRawY());
                        sb4.append(str);
                        sb4.append(StickerView.this.scale_orgY);
                        String str4 = "<><";
                        sb4.append(str4);
                        sb4.append(motionEvent.getRawX());
                        sb4.append(str);
                        sb4.append(StickerView.this.scale_orgX);
                        sb4.append(str);
                        sb4.append(StickerView.this.centerY);
                        sb4.append(str);
                        sb4.append(StickerView.this.centerX);
                        Log.v(str2, sb4.toString());
                        double abs = (Math.abs(Math.atan2((double) (motionEvent.getRawY() - StickerView.this.scale_orgY), (double) (motionEvent.getRawX() - StickerView.this.scale_orgX)) - Math.atan2(((double) StickerView.this.scale_orgY) - StickerView.this.centerY, ((double) StickerView.this.scale_orgX) - StickerView.this.centerX)) * 180.0d) / 3.141592653589793d;
                        StickerView stickerView3 = StickerView.this;
                        double access$400 = stickerView3.getLength(stickerView3.centerX, StickerView.this.centerY, (double) StickerView.this.scale_orgX, (double) StickerView.this.scale_orgY);
                        StickerView stickerView4 = StickerView.this;
                        String str5 = str4;
                        String str6 = str;
                        double access$4002 = stickerView4.getLength(stickerView4.centerX, StickerView.this.centerY, (double) motionEvent.getRawX(), (double) motionEvent.getRawY());
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("angle_diff: ");
                        sb5.append(abs);
                        String str7 = str6;
                        sb5.append(str7);
                        sb5.append(access$400);
                        sb5.append(str5);
                        sb5.append(access$4002);
                        Log.v(str2, sb5.toString());
                        int access$500 = StickerView.convertDpToPixel(100.0f, StickerView.this.getContext());
                        String str8 = "size_diff: ";
                        if (access$4002 > access$400 && (abs < 90.0d || Math.abs(abs - 180.0d) < 90.0d)) {
                            double round = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str8);
                            sb6.append(round);
                            Log.v(str2, sb6.toString());
                            LayoutParams layoutParams = StickerView.this.getLayoutParams();
                            layoutParams.width = (int) (((double) layoutParams.width) + round);
                            LayoutParams layoutParams2 = StickerView.this.getLayoutParams();
                            layoutParams2.height = (int) (((double) layoutParams2.height) + round);
                            StickerView stickerView5 = StickerView.this;
                            stickerView5.width = (int) (((double) stickerView5.width) + round);
                            StickerView stickerView6 = StickerView.this;
                            stickerView6.height = (int) (((double) stickerView6.height) + round);
                            ((View) StickerView.this.getParent()).getLayoutParams().width = StickerView.this.width;
                            ((View) StickerView.this.getParent()).getLayoutParams().height = StickerView.this.height;
                            StickerView.this.onScaling(true);
                        } else if (access$4002 < access$400 && (abs < 90.0d || Math.abs(abs - 180.0d) < 90.0d)) {
                            int i = access$500 / 2;
                            if (StickerView.this.getLayoutParams().width > i && StickerView.this.getLayoutParams().height > i) {
                                double round2 = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                                LayoutParams layoutParams3 = StickerView.this.getLayoutParams();
                                layoutParams3.width = (int) (((double) layoutParams3.width) - round2);
                                LayoutParams layoutParams4 = StickerView.this.getLayoutParams();
                                layoutParams4.height = (int) (((double) layoutParams4.height) - round2);
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str8);
                                sb7.append(round2);
                                Log.v(str2, sb7.toString());
                                StickerView stickerView7 = StickerView.this;
                                stickerView7.width = (int) (((double) stickerView7.width) - round2);
                                StickerView stickerView8 = StickerView.this;
                                stickerView8.height = (int) (((double) stickerView8.height) - round2);
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append(str8);
                                sb8.append(StickerView.this.width);
                                sb8.append(">>");
                                sb8.append(StickerView.this.height);
                                Log.v(str2, sb8.toString());
                                ((View) StickerView.this.getParent()).getLayoutParams().width = StickerView.this.width;
                                ((View) StickerView.this.getParent()).getLayoutParams().height = StickerView.this.height;
                                StickerView.this.onScaling(false);
                            }
                        }
                        PrintStream printStream2 = System.out;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("HSDH-->");
                        sb9.append(StickerView.this.height);
                        sb9.append(str7);
                        sb9.append(StickerView.this.width);
                        printStream2.println(sb9.toString());
                        StickerView.this.onScaleView.onScale(StickerView.this.getLayoutParams().height, StickerView.this.getLayoutParams().width);
                        double atan2 = (Math.atan2(((double) motionEvent.getRawY()) - StickerView.this.centerY, ((double) motionEvent.getRawX()) - StickerView.this.centerX) * 180.0d) / 3.141592653589793d;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("log angle: ");
                        sb10.append(atan2);
                        Log.v(str2, sb10.toString());
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("getRotation(): ");
                        sb11.append(StickerView.this.getRotation());
                        Log.v(str2, sb11.toString());
                        StickerView.this.scale_orgX = motionEvent.getRawX();
                        StickerView.this.scale_orgY = motionEvent.getRawY();
                        StickerView.this.postInvalidate();
                        StickerView.this.requestLayout();
                    } else {
                        System.out.println("FDF--> IN");
                        if (StickerView.this.getLayoutParams().width <= 290 || StickerView.this.getLayoutParams().height <= 290) {
                            StickerView.this.getLayoutParams().width = 292;
                            StickerView.this.getLayoutParams().height = 292;
                        } else if (StickerView.this.getLayoutParams().width >= 600 || StickerView.this.getLayoutParams().height >= 600) {
                            StickerView.this.getLayoutParams().width = 598;
                            StickerView.this.getLayoutParams().height = 598;
                        }
                    }
                }
            }
            if (motionEvent.getAction() == 0) {
                if (StickerView.this.onScaleView != null) {
                    StickerView.this.onScaleView.scaleStart();
                }
                return true;
            } else if (motionEvent.getAction() != 1 || StickerView.this.onScaleView == null) {
                return true;
            } else {
                StickerView.this.onScaleView.scaleStop();
                return true;
            }
        }
    };
    private float move_orgX = -1.0f;
    private float move_orgY = -1.0f;
    /* access modifiers changed from: private */
    public onScaleView onScaleView;
    /* access modifiers changed from: private */
    public float scale_orgX = -1.0f;
    /* access modifiers changed from: private */
    public float scale_orgY = -1.0f;
    public int width = 350;

    private class BorderView extends View {
        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public BorderView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            StringBuilder sb = new StringBuilder();
            sb.append("layoutParamsMain.leftMargin: ");
            sb.append(layoutParams.leftMargin);
            Log.v("com.knef.stickerView", sb.toString());
            Rect rect = new Rect();
            rect.left = getLeft() - layoutParams.leftMargin;
            rect.top = getTop() - layoutParams.topMargin;
            rect.right = getRight() - layoutParams.rightMargin;
            rect.bottom = getBottom() - layoutParams.bottomMargin;
            Paint paint = new Paint();
            paint.setStrokeWidth(6.0f);
            paint.setColor(-1);
            paint.setStyle(Style.STROKE);
            canvas.drawRect(rect, paint);
        }
    }

    public interface onScaleView {
        void closeCamera();

        void flipCamera();

        void move(float f, float f2);

        void onScale(int i, int i2);

        void scaleStart();

        void scaleStop();
    }

    private boolean m14755a(int i) {
        return i == 1 || i == 3 || i == 4;
    }

    /* access modifiers changed from: protected */
    public abstract View getMainView();

    /* access modifiers changed from: protected */
    public void onRotating() {
    }

    /* access modifiers changed from: protected */
    public void onScaling(boolean z) {
    }

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    /* access modifiers changed from: private */
    public static int convertDpToPixel(float f, Context context) {
        return (int) (f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    private void init(Context context) {
        this.iv_border = new BorderView(context);
        this.iv_scale = new ImageView(context);
        this.iv_delete = new ImageView(context);
        this.iv_flip = new ImageView(context);
        this.iv_scale.setImageResource(R.mipmap.ic_zoom);
        this.iv_delete.setImageResource(R.mipmap.ic_cancel);
        this.iv_flip.setImageResource(R.mipmap.ic_flip);
        setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_scale.setTag("iv_scale");
        this.iv_delete.setTag("iv_delete");
        this.iv_flip.setTag("iv_flip");
        int convertDpToPixel = convertDpToPixel(25.0f, getContext()) / 2;
        int convertDpToPixel2 = convertDpToPixel(100.0f, getContext());
        LayoutParams layoutParams = new LayoutParams(convertDpToPixel2, convertDpToPixel2);
        layoutParams.gravity = 17;
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        new LayoutParams(-1, -1).setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        LayoutParams layoutParams3 = new LayoutParams(convertDpToPixel(25.0f, getContext()), convertDpToPixel(25.0f, getContext()));
        layoutParams3.bottomMargin = 20;
        layoutParams3.rightMargin = 20;
        layoutParams3.gravity = 85;
        LayoutParams layoutParams4 = new LayoutParams(convertDpToPixel(25.0f, getContext()), convertDpToPixel(25.0f, getContext()));
        layoutParams4.gravity = 53;
        setLayoutParams(layoutParams);
        addView(getMainView(), layoutParams2);
        this.iv_scale.setPadding(convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()));
        this.iv_delete.setPadding(convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()));
        this.iv_flip.setPadding(convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()), convertDpToPixel(5.0f, getContext()));
        this.iv_scale.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryOpacity60));
        this.iv_delete.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryOpacity60));
        this.iv_flip.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryOpacity60));
        addView(this.iv_scale, layoutParams3);
        addView(this.iv_delete, layoutParams4);
        this.iv_scale.setOnTouchListener(this.mTouchListener);
        this.iv_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (StickerView.this.onScaleView != null) {
                    StickerView.this.onScaleView.closeCamera();
                }
            }
        });
        this.iv_flip.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.v("com.knef.stickerView", "flip the view");
                if (StickerView.this.onScaleView != null) {
                    StickerView.this.onScaleView.flipCamera();
                }
            }
        });
    }

    public void addFlip() {
        LayoutParams layoutParams = new LayoutParams(convertDpToPixel(25.0f, getContext()), convertDpToPixel(25.0f, getContext()));
        layoutParams.gravity = 51;
        addView(this.iv_flip, layoutParams);
    }

    private void m14753a(View view, MotionEvent motionEvent) {
        this.f9811g = motionEvent.getX();
        this.f9812h = motionEvent.getY();
        if (this.f9813i == 0.0f) {
            this.f9813i = (float) ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f9809e = this.f9811g;
            this.f9810f = this.f9812h;
        } else if (actionMasked != 1 && actionMasked != 2 && actionMasked != 3 && actionMasked != 4) {
        } else {
            if (!m14755a(motionEvent.getActionMasked()) && this.f9821q && Math.abs(this.f9811g - this.f9809e) <= this.f9813i && Math.abs(this.f9812h - this.f9810f) <= this.f9813i) {
                return;
            }
            if (this.f9821q) {
                m14772n();
                this.f9821q = false;
                return;
            }
            this.f9817m = view.getX();
            this.f9818n = view.getY();
            float f = this.f9811g;
            float f2 = this.f9817m;
            this.f9814j = (f + f2) / (this.f9809e + f2);
            float f3 = this.f9812h;
            float f4 = this.f9818n;
            this.f9815k = (f3 + f4) / (this.f9810f + f4);
            float f5 = this.f9814j;
            float f6 = this.f9815k;
            if (f5 - f6 <= 0.0f) {
                f5 = f6;
            }
            this.f9816l = f5;
            this.f9819o = this.f9816l * ((float) getWidth());
            this.f9820p = this.f9816l * ((float) getHeight());
            boolean m14755a = m14755a(motionEvent.getActionMasked());
            m14750a(this.f9819o, this.f9820p, m14755a);
            if (m14755a) {
                this.f9821q = true;
            }
        }
    }

    private void m14772n() {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.width = getWidth();
        layoutParams.height = getHeight();
        setLayoutParams(layoutParams);
    }

    private void m14750a(float f, float f2, boolean z) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.float_camera_window_size_max);
        float f3 = (float) dimensionPixelSize;
        float dimensionPixelSize2 = (float) getResources().getDimensionPixelSize(R.dimen.float_camera_window_size_min);
        int max = (int) Math.max(Math.min(f, f3), dimensionPixelSize2);
        int max2 = (int) Math.max(Math.min(f2, f3), dimensionPixelSize2);
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.width = max;
        layoutParams.height = max2;
        setLayoutParams(layoutParams);
    }

    public boolean isFlip() {
        return getMainView().getRotationY() == -180.0f;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* access modifiers changed from: private */
    public double getLength(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d3 - d, 2.0d));
    }

    private float[] getRelativePos(float f, float f2) {
        StringBuilder sb = new StringBuilder();
        sb.append("getRelativePos getX:");
        sb.append(((View) getParent()).getX());
        String str = "ken";
        Log.v(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getRelativePos getY:");
        sb2.append(((View) getParent()).getY());
        Log.v(str, sb2.toString());
        float[] fArr = {f - ((View) getParent()).getX(), f2 - ((View) getParent()).getY()};
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getRelativePos absY:");
        sb3.append(f2);
        String str2 = "com.knef.stickerView";
        Log.v(str2, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("getRelativePos relativeY:");
        sb4.append(fArr[1]);
        Log.v(str2, sb4.toString());
        return fArr;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setHidden(boolean z) {
        this.isHidden = z;
    }

    public void setControlItemsHidden(boolean z) {
        this.isHidden = z;
        if (z) {
            this.iv_border.setVisibility(4);
            this.iv_scale.setVisibility(4);
            this.iv_delete.setVisibility(4);
            this.iv_flip.setVisibility(4);
            return;
        }
        this.iv_border.setVisibility(0);
        this.iv_scale.setVisibility(0);
        this.iv_delete.setVisibility(0);
        this.iv_flip.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public View getImageViewFlip() {
        return this.iv_flip;
    }

    public void setOnScaleView(onScaleView onscaleview) {
        this.onScaleView = onscaleview;
    }
}
