package com.yasoka.eazyscreenrecord.fullscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout.LayoutParams;

import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.activities.FullscreenActivity;

import java.io.PrintStream;

public class FullFragment2 extends Fragment {
    private PointF centerPoint;
    private PointF closePoint;
    private View closeView;
    /* access modifiers changed from: private */
    public int f7051e;
    private Handler f7059m = new C23371(this);
    /* access modifiers changed from: private */
    public View fingerView;
    /* access modifiers changed from: private */
    public View floatCenterLay;
    private View guideTrack;
    private int screenHeight;
    private int screenWidth;
    /* access modifiers changed from: private */
    public float valuesX;
    /* access modifiers changed from: private */
    public float valuesY;

    class C23371 extends Handler {
        final FullFragment2 f7037a;

        C23371(FullFragment2 fullFragment2) {
            this.f7037a = fullFragment2;
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i == 1 && this.f7037a.getActivity() != null) {
                }
                return;
            }
            FullFragment2.this.animate22();
        }
    }

    class C23382 implements OnClickListener {
        C23382() {
        }

        public void onClick(View view) {
            ((FullscreenActivity) FullFragment2.this.getActivity()).startFloatingService();
        }
    }

    class C23415 extends AnimatorListenerAdapter {
        final FullFragment2 f7045a;

        C23415(FullFragment2 fullFragment2) {
            this.f7045a = fullFragment2;
        }

        public void onAnimationEnd(Animator animator) {
            this.f7045a.m10420i();
        }
    }

    class C23426 extends AnimatorListenerAdapter {
        final FullFragment2 f7046a;

        C23426(FullFragment2 fullFragment2) {
            this.f7046a = fullFragment2;
        }

        public void onAnimationEnd(Animator animator) {
            this.f7046a.m10422j();
        }
    }

    public String mo1434c() {
        return "wgsf";
    }

    public static int m20489b(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public static int m20495c(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.screenWidth = m20489b(getContext());
        this.screenHeight = m20495c(getContext());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_full_fragment2, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.floatCenterLay = view.findViewById(R.id.durec_float_center_layout);
        this.closeView = view.findViewById(R.id.durec_fw_close);
        this.guideTrack = view.findViewById(R.id.durec_guide_track);
        this.fingerView = view.findViewById(R.id.durec_guide_finger);
        this.screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        view.setOnClickListener(new C23382());
        m10413e();
        m10415f();
        m10417g();
        m10418h();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (FullFragment2.this.isAdded()) {
                    FullscreenActivity fullscreenActivity = (FullscreenActivity) FullFragment2.this.getActivity();
                    if (fullscreenActivity != null && fullscreenActivity.getSizeOfViewPager() == 1) {
                        FullFragment2.this.setUserVisibleHint(true);
                    }
                }
            }
        }, 100);
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (isAdded() && z) {
            this.screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            this.screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
            Handler handler = this.f7059m;
            handler.sendMessageDelayed(handler.obtainMessage(0), 500);
        }
    }

    private void m10413e() {
        float f = ((float) this.screenHeight) * 0.34895834f;
        int i = this.screenWidth;
        float f2 = ((float) i) * 0.10185185f;
        LayoutParams layoutParams = new LayoutParams((int) (((float) i) * 0.44444445f), -2);
        layoutParams.topMargin = (int) f;
        layoutParams.leftMargin = (int) f2;
    }

    private void m10415f() {
        LayoutParams layoutParams = (LayoutParams) this.closeView.getLayoutParams();
        layoutParams.bottomMargin = (int) (((float) this.screenHeight) * 0.13541667f);
        this.closeView.setLayoutParams(layoutParams);
        int i = (this.screenHeight - layoutParams.bottomMargin) - (layoutParams.height / 2);
        int i2 = this.screenWidth / 2;
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("CPO->");
        sb.append(i);
        sb.append("<>");
        sb.append(i2);
        printStream.println(sb.toString());
        this.closePoint = new PointF((float) i2, (float) i);
    }

    private void m10417g() {
        float f = ((float) this.screenHeight) * 0.23489584f;
        float f2 = 0.57871395f * f;
        LayoutParams layoutParams = (LayoutParams) this.guideTrack.getLayoutParams();
        layoutParams.width = (int) f2;
        layoutParams.height = (int) f;
        layoutParams.leftMargin = (int) ((f2 / 2.0f) - (f2 * 0.07662835f));
        layoutParams.bottomMargin = (int) (((float) this.screenHeight) * 0.32291666f);
        this.guideTrack.setLayoutParams(layoutParams);
        this.f7051e = this.screenHeight - layoutParams.bottomMargin;
    }

    private void m10418h() {
        LayoutParams layoutParams = (LayoutParams) this.floatCenterLay.getLayoutParams();
        layoutParams.rightMargin = (int) (((float) this.screenWidth) * 0.09259259f);
        layoutParams.topMargin = (int) (((float) this.screenHeight) * 0.38020834f);
        this.floatCenterLay.setLayoutParams(layoutParams);
        int i = (this.screenWidth - layoutParams.rightMargin) - (layoutParams.width / 2);
        int i2 = (layoutParams.height / 2) + layoutParams.topMargin;
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("SDS->");
        sb.append(i);
        sb.append("<><");
        sb.append(i2);
        printStream.println(sb.toString());
        float f = (float) i2;
        this.centerPoint = new PointF((float) i, f);
        int i3 = (int) (((float) this.screenWidth) * 0.26759258f);
        int i4 = (int) (((float) this.screenHeight) * 0.20885417f);
        this.valuesX = 0.051903114f;
        this.valuesY = 0.02244389f;
        LayoutParams layoutParams2 = new LayoutParams(i3, i4);
        layoutParams2.gravity = 5;
        layoutParams2.rightMargin = (int) (((float) this.screenWidth) - (((float) (i + layoutParams2.width)) - (((float) i3) * this.valuesX)));
        layoutParams2.topMargin = (int) (f - (this.valuesY * ((float) i4)));
        this.fingerView.setLayoutParams(layoutParams2);
    }

    private void m10406a(PointF pointF, final PointF pointF2) {
        final float abs = Math.abs((pointF2.y - pointF.y) / (pointF2.x - pointF.x));
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("SDDSSD->");
        sb.append(pointF2.x);
        String str = "<>";
        sb.append(str);
        sb.append(pointF2.y);
        sb.append(str);
        sb.append(pointF.x);
        sb.append(str);
        sb.append(pointF.y);
        sb.append(str);
        sb.append(abs);
        printStream.println(sb.toString());
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(new Object[]{pointF, pointF2});
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            public PointF evaluate(float f, PointF pointF, PointF pointF2) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("SDDD1->");
                sb.append(pointF.x);
                String str = "<>";
                sb.append(str);
                sb.append(pointF.y);
                sb.append(str);
                sb.append(pointF2.x);
                sb.append(str);
                sb.append(pointF2.y);
                sb.append(str);
                sb.append(f);
                printStream.println(sb.toString());
                return m10404a(f, pointF, pointF2);
            }

            /* access modifiers changed from: 0000 */
            public PointF m10404a(float f, PointF pointF, PointF pointF2) {
                PointF pointF3 = new PointF();
                float f2 = f * f;
                pointF3.x = pointF.x + f2;
                float f3 = pointF3.x - pointF.x;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("f2->");
                sb.append(f3);
                sb.append("<><>");
                float f4 = f2 - (f3 * f3);
                sb.append((float) (((double) abs) * Math.sqrt((double) Math.abs(f4))));
                printStream.println(sb.toString());
                pointF3.y = pointF2.y - ((float) (((double) abs) * Math.sqrt((double) Math.abs(f4))));
                PrintStream printStream2 = System.out;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("pos1->");
                sb2.append(pointF3.x);
                String str = "<>";
                sb2.append(str);
                sb2.append(pointF3.y);
                sb2.append(str);
                printStream2.println(sb2.toString());
                return pointF3;
            }
        });
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("pos2->");
                sb.append(pointF.x);
                String str = "<>";
                sb.append(str);
                sb.append(pointF.y);
                printStream.println(sb.toString());
                FullFragment2.this.floatCenterLay.setX(pointF.x - ((float) (FullFragment2.this.floatCenterLay.getLayoutParams().width / 2)));
                FullFragment2.this.floatCenterLay.setY(pointF.y - ((float) (FullFragment2.this.floatCenterLay.getLayoutParams().height / 2)));
                FullFragment2.this.fingerView.setX(pointF.x - (FullFragment2.this.valuesX * ((float) FullFragment2.this.fingerView.getLayoutParams().width)));
                FullFragment2.this.fingerView.setY(pointF.y - (FullFragment2.this.valuesY * ((float) FullFragment2.this.fingerView.getLayoutParams().height)));
                PrintStream printStream2 = System.out;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("SD<>");
                sb2.append(FullFragment2.this.fingerView.getX());
                sb2.append(str);
                sb2.append(FullFragment2.this.fingerView.getY());
                sb2.append(str);
                sb2.append(FullFragment2.this.floatCenterLay.getX());
                sb2.append(str);
                sb2.append(FullFragment2.this.floatCenterLay.getY());
                printStream2.println(sb2.toString());
                if (pointF.y >= ((float) FullFragment2.this.f7051e)) {
                    FullFragment2.this.floatCenterLay.setAlpha(1.0f - ((pointF.y - ((float) FullFragment2.this.f7051e)) / (pointF2.y - ((float) FullFragment2.this.f7051e))));
                }
            }
        });
        valueAnimator.addListener(new C23415(this));
        valueAnimator.start();
    }

    /* access modifiers changed from: private */
    public void m10420i() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.closeView, View.ALPHA, new float[]{1.0f, 0.3f, 1.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.closeView, View.SCALE_X, new float[]{1.0f, 1.5f, 1.0f});
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.closeView, View.SCALE_Y, new float[]{1.0f, 1.5f, 1.0f});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3});
        animatorSet.setDuration(500);
        animatorSet.addListener(new C23426(this));
        animatorSet.start();
    }

    /* access modifiers changed from: private */
    public void m10422j() {
        Handler handler = this.f7059m;
        handler.sendMessageDelayed(handler.obtainMessage(1), 500);
    }

    public void onDestroy() {
        this.f7059m.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void animate22() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{-1.0f, -0.45f});
        ofFloat.setDuration(2000);
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                double floatValue = ((double) ((Float) valueAnimator.getAnimatedValue()).floatValue()) * 3.141592653589793d;
                FullFragment2.this.fingerView.setTranslationX((float) (Math.sin(floatValue) * 350.0d));
                FullFragment2.this.fingerView.setTranslationY(((float) (Math.cos(floatValue) * 550.0d)) + 410.0f);
            }
        });
        ofFloat.addListener(new C23415(this));
        this.fingerView.setVisibility(View.VISIBLE);
        ofFloat.start();
    }
}
