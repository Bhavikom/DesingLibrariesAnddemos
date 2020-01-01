package com.yasoka.eazyscreenrecord.imgedit.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ezscreenrecorder.C0793R;

public abstract class StickerView extends FrameLayout {
    private static final int BUTTON_SIZE_DP = 30;
    private static final int SELF_SIZE_DP = 100;
    public static final String TAG = "com.knef.stickerView";
    /* access modifiers changed from: private */
    public double centerX;
    /* access modifiers changed from: private */
    public double centerY;
    private BorderView iv_border;
    private ImageView iv_delete;
    private ImageView iv_flip;
    private ImageView iv_scale;
    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i = 0;
            String str = "com.knef.stickerView";
            if (view.getTag().equals("DraggableViewGroup")) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    Log.v(str, "sticker view action down");
                    StickerView.this.move_orgX = motionEvent.getRawX();
                    StickerView.this.move_orgY = motionEvent.getRawY();
                    StickerView.this.setControlItemsHidden(false);
                } else if (action == 1) {
                    Log.v(str, "sticker view action up");
                } else if (action == 2) {
                    Log.v(str, "sticker view action move");
                    float rawX = motionEvent.getRawX() - StickerView.this.move_orgX;
                    float rawY = motionEvent.getRawY() - StickerView.this.move_orgY;
                    StickerView stickerView = StickerView.this;
                    stickerView.setX(stickerView.getX() + rawX);
                    StickerView stickerView2 = StickerView.this;
                    stickerView2.setY(stickerView2.getY() + rawY);
                    StickerView.this.move_orgX = motionEvent.getRawX();
                    StickerView.this.move_orgY = motionEvent.getRawY();
                }
            } else if (view.getTag().equals("iv_scale")) {
                int action2 = motionEvent.getAction();
                if (action2 == 0) {
                    Log.v(str, "iv_scale action down");
                    StickerView stickerView3 = StickerView.this;
                    stickerView3.this_orgX = stickerView3.getX();
                    StickerView stickerView4 = StickerView.this;
                    stickerView4.this_orgY = stickerView4.getY();
                    StickerView.this.scale_orgX = motionEvent.getRawX();
                    StickerView.this.scale_orgY = motionEvent.getRawY();
                    StickerView stickerView5 = StickerView.this;
                    stickerView5.scale_orgWidth = (double) stickerView5.getLayoutParams().width;
                    StickerView stickerView6 = StickerView.this;
                    stickerView6.scale_orgHeight = (double) stickerView6.getLayoutParams().height;
                    StickerView.this.rotate_orgX = motionEvent.getRawX();
                    StickerView.this.rotate_orgY = motionEvent.getRawY();
                    StickerView stickerView7 = StickerView.this;
                    stickerView7.centerX = (double) (stickerView7.getX() + ((View) StickerView.this.getParent()).getX() + (((float) StickerView.this.getWidth()) / 2.0f));
                    int identifier = StickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (identifier > 0) {
                        i = StickerView.this.getResources().getDimensionPixelSize(identifier);
                    }
                    double d = (double) i;
                    StickerView stickerView8 = StickerView.this;
                    stickerView8.centerY = ((double) (stickerView8.getY() + ((View) StickerView.this.getParent()).getY())) + d + ((double) (((float) StickerView.this.getHeight()) / 2.0f));
                } else if (action2 == 1) {
                    Log.v(str, "iv_scale action up");
                } else if (action2 == 2) {
                    Log.v(str, "iv_scale action move");
                    StickerView.this.rotate_newX = motionEvent.getRawX();
                    StickerView.this.rotate_newY = motionEvent.getRawY();
                    double abs = (Math.abs(Math.atan2((double) (motionEvent.getRawY() - StickerView.this.scale_orgY), (double) (motionEvent.getRawX() - StickerView.this.scale_orgX)) - Math.atan2(((double) StickerView.this.scale_orgY) - StickerView.this.centerY, ((double) StickerView.this.scale_orgX) - StickerView.this.centerX)) * 180.0d) / 3.141592653589793d;
                    StringBuilder sb = new StringBuilder();
                    sb.append("angle_diff: ");
                    sb.append(abs);
                    Log.v(str, sb.toString());
                    StickerView stickerView9 = StickerView.this;
                    double access$1400 = stickerView9.getLength(stickerView9.centerX, StickerView.this.centerY, (double) StickerView.this.scale_orgX, (double) StickerView.this.scale_orgY);
                    StickerView stickerView10 = StickerView.this;
                    String str2 = str;
                    double access$14002 = stickerView10.getLength(stickerView10.centerX, StickerView.this.centerY, (double) motionEvent.getRawX(), (double) motionEvent.getRawY());
                    int access$1500 = StickerView.convertDpToPixel(100.0f, StickerView.this.getContext());
                    if (access$14002 > access$1400 && (abs < 25.0d || Math.abs(abs - 180.0d) < 25.0d)) {
                        double round = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                        LayoutParams layoutParams = StickerView.this.getLayoutParams();
                        layoutParams.width = (int) (((double) layoutParams.width) + round);
                        LayoutParams layoutParams2 = StickerView.this.getLayoutParams();
                        layoutParams2.height = (int) (((double) layoutParams2.height) + round);
                        StickerView.this.onScaling(true);
                    } else if (access$14002 < access$1400 && (abs < 25.0d || Math.abs(abs - 180.0d) < 25.0d)) {
                        int i2 = access$1500 / 2;
                        if (StickerView.this.getLayoutParams().width > i2 && StickerView.this.getLayoutParams().height > i2) {
                            double round2 = (double) Math.round(Math.max((double) Math.abs(motionEvent.getRawX() - StickerView.this.scale_orgX), (double) Math.abs(motionEvent.getRawY() - StickerView.this.scale_orgY)));
                            LayoutParams layoutParams3 = StickerView.this.getLayoutParams();
                            layoutParams3.width = (int) (((double) layoutParams3.width) - round2);
                            LayoutParams layoutParams4 = StickerView.this.getLayoutParams();
                            layoutParams4.height = (int) (((double) layoutParams4.height) - round2);
                            StickerView.this.onScaling(false);
                        }
                    }
                    double atan2 = (Math.atan2(((double) motionEvent.getRawY()) - StickerView.this.centerY, ((double) motionEvent.getRawX()) - StickerView.this.centerX) * 180.0d) / 3.141592653589793d;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("log angle: ");
                    sb2.append(atan2);
                    String str3 = str2;
                    Log.v(str3, sb2.toString());
                    StickerView.this.setRotation(((float) atan2) - 45.0f);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("getRotation(): ");
                    sb3.append(StickerView.this.getRotation());
                    Log.v(str3, sb3.toString());
                    StickerView.this.onRotating();
                    StickerView stickerView11 = StickerView.this;
                    stickerView11.rotate_orgX = stickerView11.rotate_newX;
                    StickerView stickerView12 = StickerView.this;
                    stickerView12.rotate_orgY = stickerView12.rotate_newY;
                    StickerView.this.scale_orgX = motionEvent.getRawX();
                    StickerView.this.scale_orgY = motionEvent.getRawY();
                    StickerView.this.postInvalidate();
                    StickerView.this.requestLayout();
                }
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public float move_orgX = -1.0f;
    /* access modifiers changed from: private */
    public float move_orgY = -1.0f;
    /* access modifiers changed from: private */
    public OnTapListener onTapListener;
    /* access modifiers changed from: private */
    public float rotate_newX = -1.0f;
    /* access modifiers changed from: private */
    public float rotate_newY = -1.0f;
    /* access modifiers changed from: private */
    public float rotate_orgX = -1.0f;
    /* access modifiers changed from: private */
    public float rotate_orgY = -1.0f;
    /* access modifiers changed from: private */
    public double scale_orgHeight = -1.0d;
    /* access modifiers changed from: private */
    public double scale_orgWidth = -1.0d;
    /* access modifiers changed from: private */
    public float scale_orgX = -1.0f;
    /* access modifiers changed from: private */
    public float scale_orgY = -1.0f;
    /* access modifiers changed from: private */
    public float this_orgX = -1.0f;
    /* access modifiers changed from: private */
    public float this_orgY = -1.0f;

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

    public interface OnTapListener {
        void deleteClick();

        void doubleTap();
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
        this.iv_scale.setImageResource(C0793R.C0794drawable.zoominout);
        this.iv_delete.setImageResource(C0793R.C0794drawable.ic_text_edit);
        this.iv_flip.setImageResource(C0793R.C0794drawable.remove);
        setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_scale.setTag("iv_scale");
        this.iv_delete.setTag("iv_delete");
        this.iv_flip.setTag("iv_flip");
        int convertDpToPixel = convertDpToPixel(30.0f, getContext()) / 2;
        int convertDpToPixel2 = convertDpToPixel(100.0f, getContext());
        LayoutParams layoutParams = new LayoutParams(convertDpToPixel2, convertDpToPixel2);
        layoutParams.gravity = 17;
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        LayoutParams layoutParams3 = new LayoutParams(-1, -1);
        layoutParams3.setMargins(convertDpToPixel, convertDpToPixel, convertDpToPixel, convertDpToPixel);
        LayoutParams layoutParams4 = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        layoutParams4.gravity = 85;
        LayoutParams layoutParams5 = new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext()));
        layoutParams5.gravity = 53;
        new LayoutParams(convertDpToPixel(30.0f, getContext()), convertDpToPixel(30.0f, getContext())).gravity = 51;
        setLayoutParams(layoutParams);
        addView(getMainView(), layoutParams2);
        addView(this.iv_border, layoutParams3);
        addView(this.iv_scale, layoutParams4);
        addView(this.iv_delete, layoutParams5);
        setOnTouchListener(this.mTouchListener);
        this.iv_scale.setOnTouchListener(this.mTouchListener);
        this.iv_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (StickerView.this.onTapListener != null) {
                    StickerView.this.onTapListener.doubleTap();
                }
            }
        });
        this.iv_flip.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.v("com.knef.stickerView", "flip the view");
                if (StickerView.this.onTapListener != null) {
                    StickerView.this.onTapListener.doubleTap();
                }
            }
        });
    }

    public void setOnTapListener(OnTapListener onTapListener2) {
        this.onTapListener = onTapListener2;
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

    public void setControlItemsHidden(boolean z) {
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
}
