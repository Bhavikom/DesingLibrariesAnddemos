package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.DynamicLayout;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

@SuppressLint({"ViewConstructor"})
public class TapTargetView extends View {
    final int CIRCLE_PADDING;
    final int GUTTER_DIM;
    final int SHADOW_DIM;
    final int TARGET_PADDING;
    final int TARGET_PULSE_RADIUS;
    final int TARGET_RADIUS;
    final int TEXT_MAX_WIDTH;
    final int TEXT_PADDING;
    final int TEXT_POSITIONING_BIAS;
    final int TEXT_SPACING;
    private ValueAnimator[] animators = {this.expandAnimation, this.pulseAnimation, this.dismissConfirmAnimation, this.dismissAnimation};
    int bottomBoundary;
    @Nullable
    final ViewGroup boundingParent;
    int calculatedOuterCircleRadius;
    boolean cancelable;
    boolean debug;
    @Nullable
    DynamicLayout debugLayout;
    @Nullable
    Paint debugPaint;
    @Nullable
    SpannableStringBuilder debugStringBuilder;
    @Nullable
    TextPaint debugTextPaint;
    @Nullable
    CharSequence description;
    @Nullable
    StaticLayout descriptionLayout;
    final TextPaint descriptionPaint;
    int dimColor;
    final ValueAnimator dismissAnimation = new FloatValueAnimatorBuilder(true).duration(250).interpolator(new AccelerateDecelerateInterpolator()).onUpdate(new FloatValueAnimatorBuilder.UpdateListener() {
        public void onUpdate(float f) {
            TapTargetView.this.expandContractUpdateListener.onUpdate(f);
        }
    }).onEnd(new FloatValueAnimatorBuilder.EndListener() {
        public void onEnd() {
            ViewUtil.removeView(TapTargetView.this.parent, TapTargetView.this);
            TapTargetView.this.onDismiss();
        }
    }).build();
    private final ValueAnimator dismissConfirmAnimation = new FloatValueAnimatorBuilder().duration(250).interpolator(new AccelerateDecelerateInterpolator()).onUpdate(new FloatValueAnimatorBuilder.UpdateListener() {
        public void onUpdate(float f) {
            float min = Math.min(1.0f, 2.0f * f);
            TapTargetView tapTargetView = TapTargetView.this;
            tapTargetView.outerCircleRadius = ((float) tapTargetView.calculatedOuterCircleRadius) * ((0.2f * min) + 1.0f);
            TapTargetView tapTargetView2 = TapTargetView.this;
            int i = (int) ((1.0f - min) * 255.0f);
            tapTargetView2.outerCircleAlpha = i;
            tapTargetView2.outerCirclePath.reset();
            TapTargetView.this.outerCirclePath.addCircle((float) TapTargetView.this.outerCircleCenter[0], (float) TapTargetView.this.outerCircleCenter[1], TapTargetView.this.outerCircleRadius, Direction.CW);
            TapTargetView tapTargetView3 = TapTargetView.this;
            float f2 = 1.0f - f;
            tapTargetView3.targetCircleRadius = ((float) tapTargetView3.TARGET_RADIUS) * f2;
            TapTargetView tapTargetView4 = TapTargetView.this;
            tapTargetView4.targetCircleAlpha = (int) (255.0f * f2);
            tapTargetView4.targetCirclePulseRadius = (f + 1.0f) * ((float) tapTargetView4.TARGET_RADIUS);
            TapTargetView tapTargetView5 = TapTargetView.this;
            tapTargetView5.targetCirclePulseAlpha = (int) (f2 * ((float) tapTargetView5.targetCirclePulseAlpha));
            TapTargetView tapTargetView6 = TapTargetView.this;
            tapTargetView6.textAlpha = i;
            tapTargetView6.calculateDrawingBounds();
            TapTargetView tapTargetView7 = TapTargetView.this;
            tapTargetView7.invalidateViewAndOutline(tapTargetView7.drawingBounds);
        }
    }).onEnd(new FloatValueAnimatorBuilder.EndListener() {
        public void onEnd() {
            ViewUtil.removeView(TapTargetView.this.parent, TapTargetView.this);
            TapTargetView.this.onDismiss();
        }
    }).build();
    Rect drawingBounds;
    final ValueAnimator expandAnimation = new FloatValueAnimatorBuilder().duration(250).delayBy(250).interpolator(new AccelerateDecelerateInterpolator()).onUpdate(new FloatValueAnimatorBuilder.UpdateListener() {
        public void onUpdate(float f) {
            TapTargetView.this.expandContractUpdateListener.onUpdate(f);
        }
    }).onEnd(new FloatValueAnimatorBuilder.EndListener() {
        public void onEnd() {
            TapTargetView.this.pulseAnimation.start();
            TapTargetView.this.outerCircleAlpha = 125;
        }
    }).build();
    final FloatValueAnimatorBuilder.UpdateListener expandContractUpdateListener = new FloatValueAnimatorBuilder.UpdateListener() {
        public void onUpdate(float f) {
            float f2 = ((float) TapTargetView.this.calculatedOuterCircleRadius) * f;
            boolean z = f2 > TapTargetView.this.outerCircleRadius;
            if (!z) {
                TapTargetView.this.calculateDrawingBounds();
            }
            TapTargetView tapTargetView = TapTargetView.this;
            tapTargetView.outerCircleRadius = f2;
            float f3 = 1.5f * f;
            tapTargetView.outerCircleAlpha = (int) Math.min(244.79999f, f3 * 244.79999f);
            TapTargetView.this.outerCirclePath.reset();
            if (TapTargetView.this.outerCircleCenter == null) {
                TapTargetView tapTargetView2 = TapTargetView.this;
                tapTargetView2.outerCircleCenter = tapTargetView2.getOuterCircleCenterPoint();
            }
            TapTargetView.this.outerCirclePath.addCircle((float) TapTargetView.this.outerCircleCenter[0], (float) TapTargetView.this.outerCircleCenter[1], TapTargetView.this.outerCircleRadius, Direction.CW);
            TapTargetView.this.targetCircleAlpha = (int) Math.min(255.0f, f3 * 255.0f);
            if (z) {
                TapTargetView tapTargetView3 = TapTargetView.this;
                tapTargetView3.targetCircleRadius = ((float) tapTargetView3.TARGET_RADIUS) * Math.min(1.0f, f3);
            } else {
                TapTargetView tapTargetView4 = TapTargetView.this;
                tapTargetView4.targetCircleRadius = ((float) tapTargetView4.TARGET_RADIUS) * f;
                TapTargetView.this.targetCirclePulseRadius *= f;
            }
            TapTargetView tapTargetView5 = TapTargetView.this;
            tapTargetView5.textAlpha = (int) (tapTargetView5.delayedLerp(f, 0.7f) * 255.0f);
            if (z) {
                TapTargetView.this.calculateDrawingBounds();
            }
            TapTargetView tapTargetView6 = TapTargetView.this;
            tapTargetView6.invalidateViewAndOutline(tapTargetView6.drawingBounds);
        }
    };
    private final OnGlobalLayoutListener globalLayoutListener;
    boolean isDark;
    private boolean isDismissed = false;
    /* access modifiers changed from: private */
    public boolean isInteractable = true;
    float lastTouchX;
    float lastTouchY;
    Listener listener;
    int outerCircleAlpha;
    int[] outerCircleCenter;
    final Paint outerCirclePaint;
    Path outerCirclePath;
    float outerCircleRadius;
    final Paint outerCircleShadowPaint;
    @Nullable
    ViewOutlineProvider outlineProvider;
    final ViewManager parent;
    final ValueAnimator pulseAnimation = new FloatValueAnimatorBuilder().duration(1000).repeat(-1).interpolator(new AccelerateDecelerateInterpolator()).onUpdate(new FloatValueAnimatorBuilder.UpdateListener() {
        public void onUpdate(float f) {
            float delayedLerp = TapTargetView.this.delayedLerp(f, 0.5f);
            TapTargetView tapTargetView = TapTargetView.this;
            tapTargetView.targetCirclePulseRadius = (delayedLerp + 1.0f) * ((float) tapTargetView.TARGET_RADIUS);
            TapTargetView tapTargetView2 = TapTargetView.this;
            tapTargetView2.targetCirclePulseAlpha = (int) ((1.0f - delayedLerp) * 255.0f);
            tapTargetView2.targetCircleRadius = ((float) tapTargetView2.TARGET_RADIUS) + (TapTargetView.this.halfwayLerp(f) * ((float) TapTargetView.this.TARGET_PULSE_RADIUS));
            if (TapTargetView.this.outerCircleRadius != ((float) TapTargetView.this.calculatedOuterCircleRadius)) {
                TapTargetView tapTargetView3 = TapTargetView.this;
                tapTargetView3.outerCircleRadius = (float) tapTargetView3.calculatedOuterCircleRadius;
            }
            TapTargetView.this.calculateDrawingBounds();
            TapTargetView tapTargetView4 = TapTargetView.this;
            tapTargetView4.invalidateViewAndOutline(tapTargetView4.drawingBounds);
        }
    }).build();
    boolean shouldDrawShadow;
    boolean shouldTintTarget;
    final TapTarget target;
    final Rect targetBounds;
    int targetCircleAlpha;
    final Paint targetCirclePaint;
    int targetCirclePulseAlpha;
    final Paint targetCirclePulsePaint;
    float targetCirclePulseRadius;
    float targetCircleRadius;
    int textAlpha;
    Rect textBounds;
    Bitmap tintedTarget;
    CharSequence title;
    StaticLayout titleLayout;
    final TextPaint titlePaint;
    int topBoundary;
    boolean visible;

    public static class Listener {
        public void onOuterCircleClick(TapTargetView tapTargetView) {
        }

        public void onTargetDismissed(TapTargetView tapTargetView, boolean z) {
        }

        public void onTargetClick(TapTargetView tapTargetView) {
            tapTargetView.dismiss(true);
        }

        public void onTargetLongClick(TapTargetView tapTargetView) {
            onTargetClick(tapTargetView);
        }

        public void onTargetCancel(TapTargetView tapTargetView) {
            tapTargetView.dismiss(false);
        }

        public void onClickOutsideOfOuterCircle(TapTargetView tapTargetView) {
            tapTargetView.dismiss(true);
        }
    }

    /* access modifiers changed from: 0000 */
    public float delayedLerp(float f, float f2) {
        if (f < f2) {
            return 0.0f;
        }
        return (f - f2) / (1.0f - f2);
    }

    /* access modifiers changed from: 0000 */
    public float halfwayLerp(float f) {
        return f < 0.5f ? f / 0.5f : (1.0f - f) / 0.5f;
    }

    public static TapTargetView showFor(Activity activity, TapTarget tapTarget) {
        return showFor(activity, tapTarget, (Listener) null);
    }

    public static TapTargetView showFor(Activity activity, TapTarget tapTarget, Listener listener2) {
        if (activity != null) {
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            TapTargetView tapTargetView = new TapTargetView(activity, viewGroup, (ViewGroup) viewGroup.findViewById(16908290), tapTarget, listener2);
            viewGroup.addView(tapTargetView, layoutParams);
            return tapTargetView;
        }
        throw new IllegalArgumentException("Activity is null");
    }

    public static TapTargetView showFor(Activity activity, ViewGroup viewGroup, TapTarget tapTarget, Listener listener2) {
        if (activity != null) {
            ViewGroup viewGroup2 = (ViewGroup) activity.getWindow().getDecorView();
            TapTargetView tapTargetView = new TapTargetView(activity, viewGroup2, viewGroup, tapTarget, listener2);
            viewGroup2.addView(tapTargetView, viewGroup.getLayoutParams());
            return tapTargetView;
        }
        throw new IllegalArgumentException("Activity is null");
    }

    public static TapTargetView showFor(Dialog dialog, TapTarget tapTarget) {
        return showFor(dialog, tapTarget, (Listener) null);
    }

    public static TapTargetView showFor(Dialog dialog, TapTarget tapTarget, Listener listener2) {
        if (dialog != null) {
            Context context = dialog.getContext();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = 2;
            layoutParams.format = 1;
            layoutParams.flags = 0;
            layoutParams.gravity = 8388659;
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.width = -1;
            layoutParams.height = -1;
            TapTargetView tapTargetView = new TapTargetView(context, windowManager, null, tapTarget, listener2);
            windowManager.addView(tapTargetView, layoutParams);
            return tapTargetView;
        }
        throw new IllegalArgumentException("Dialog is null");
    }

    public TapTargetView(final Context context, ViewManager viewManager, @Nullable final ViewGroup viewGroup, final TapTarget tapTarget, @Nullable Listener listener2) {
        super(context);
        if (tapTarget != null) {
            this.target = tapTarget;
            this.parent = viewManager;
            this.boundingParent = viewGroup;
            if (listener2 == null) {
                listener2 = new Listener();
            }
            this.listener = listener2;
            this.title = tapTarget.title;
            this.description = tapTarget.description;
            this.TARGET_PADDING = UiUtil.m20dp(context, 20);
            this.CIRCLE_PADDING = UiUtil.m20dp(context, 40);
            this.TARGET_RADIUS = UiUtil.m20dp(context, tapTarget.targetRadius);
            this.TEXT_PADDING = UiUtil.m20dp(context, 40);
            this.TEXT_SPACING = UiUtil.m20dp(context, 8);
            this.TEXT_MAX_WIDTH = UiUtil.m20dp(context, 360);
            this.TEXT_POSITIONING_BIAS = UiUtil.m20dp(context, 20);
            this.GUTTER_DIM = UiUtil.m20dp(context, 88);
            this.SHADOW_DIM = UiUtil.m20dp(context, 8);
            this.TARGET_PULSE_RADIUS = (int) (((float) this.TARGET_RADIUS) * 0.1f);
            this.outerCirclePath = new Path();
            this.targetBounds = new Rect();
            this.drawingBounds = new Rect();
            this.titlePaint = new TextPaint();
            this.titlePaint.setTextSize((float) tapTarget.titleTextSizePx(context));
            this.titlePaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            this.titlePaint.setAntiAlias(true);
            this.descriptionPaint = new TextPaint();
            this.descriptionPaint.setTextSize((float) tapTarget.descriptionTextSizePx(context));
            this.descriptionPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
            this.descriptionPaint.setAntiAlias(true);
            this.outerCirclePaint = new Paint();
            this.outerCirclePaint.setAntiAlias(true);
            this.outerCirclePaint.setAlpha(244);
            this.outerCircleShadowPaint = new Paint();
            this.outerCircleShadowPaint.setAntiAlias(true);
            this.outerCircleShadowPaint.setAlpha(50);
            this.outerCircleShadowPaint.setShadowLayer(10.0f, 0.0f, 25.0f, ViewCompat.MEASURED_STATE_MASK);
            this.targetCirclePaint = new Paint();
            this.targetCirclePaint.setAntiAlias(true);
            this.targetCirclePulsePaint = new Paint();
            this.targetCirclePulsePaint.setAntiAlias(true);
            applyTargetOptions(context);
            this.globalLayoutListener = new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    TapTargetView.this.updateTextLayouts();
                    tapTarget.onReady(new Runnable() {
                        public void run() {
                            int[] iArr = new int[2];
                            TapTargetView.this.targetBounds.set(tapTarget.bounds());
                            TapTargetView.this.getLocationOnScreen(iArr);
                            TapTargetView.this.targetBounds.offset(-iArr[0], -iArr[1]);
                            if (viewGroup != null) {
                                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                                Rect rect = new Rect();
                                viewGroup.getWindowVisibleDisplayFrame(rect);
                                TapTargetView.this.topBoundary = Math.max(0, rect.top);
                                TapTargetView.this.bottomBoundary = Math.min(rect.bottom, displayMetrics.heightPixels);
                            }
                            TapTargetView.this.drawTintedTarget();
                            TapTargetView.this.requestFocus();
                            TapTargetView.this.calculateDimensions();
                            if (!TapTargetView.this.visible) {
                                TapTargetView.this.expandAnimation.start();
                                TapTargetView.this.visible = true;
                            }
                        }
                    });
                }
            };
            getViewTreeObserver().addOnGlobalLayoutListener(this.globalLayoutListener);
            setFocusableInTouchMode(true);
            setClickable(true);
            setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (TapTargetView.this.listener != null && TapTargetView.this.outerCircleCenter != null && TapTargetView.this.isInteractable) {
                        TapTargetView tapTargetView = TapTargetView.this;
                        double distance = tapTargetView.distance(tapTargetView.targetBounds.centerX(), TapTargetView.this.targetBounds.centerY(), (int) TapTargetView.this.lastTouchX, (int) TapTargetView.this.lastTouchY);
                        boolean z = true;
                        boolean z2 = distance <= ((double) TapTargetView.this.targetCircleRadius);
                        TapTargetView tapTargetView2 = TapTargetView.this;
                        double distance2 = tapTargetView2.distance(tapTargetView2.outerCircleCenter[0], TapTargetView.this.outerCircleCenter[1], (int) TapTargetView.this.lastTouchX, (int) TapTargetView.this.lastTouchY);
                        if (distance2 > ((double) TapTargetView.this.outerCircleRadius)) {
                            z = false;
                        }
                        if (z2) {
                            TapTargetView.this.isInteractable = false;
                            TapTargetView.this.listener.onTargetClick(TapTargetView.this);
                        } else if (z) {
                            TapTargetView.this.listener.onOuterCircleClick(TapTargetView.this);
                        } else if (TapTargetView.this.cancelable) {
                            TapTargetView.this.isInteractable = false;
                            TapTargetView.this.listener.onTargetCancel(TapTargetView.this);
                        } else if (distance2 > ((double) TapTargetView.this.outerCircleRadius)) {
                            TapTargetView.this.listener.onClickOutsideOfOuterCircle(TapTargetView.this);
                        }
                    }
                }
            });
            setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (TapTargetView.this.listener == null || !TapTargetView.this.targetBounds.contains((int) TapTargetView.this.lastTouchX, (int) TapTargetView.this.lastTouchY)) {
                        return false;
                    }
                    TapTargetView.this.listener.onTargetLongClick(TapTargetView.this);
                    return true;
                }
            });
            return;
        }
        throw new IllegalArgumentException("Target cannot be null");
    }

    /* access modifiers changed from: protected */
    public void applyTargetOptions(Context context) {
        this.shouldTintTarget = this.target.tintTarget;
        this.shouldDrawShadow = this.target.drawShadow;
        this.cancelable = this.target.cancelable;
        if (this.shouldDrawShadow && VERSION.SDK_INT >= 21) {
            this.outlineProvider = new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    if (TapTargetView.this.outerCircleCenter != null) {
                        outline.setOval((int) (((float) TapTargetView.this.outerCircleCenter[0]) - TapTargetView.this.outerCircleRadius), (int) (((float) TapTargetView.this.outerCircleCenter[1]) - TapTargetView.this.outerCircleRadius), (int) (((float) TapTargetView.this.outerCircleCenter[0]) + TapTargetView.this.outerCircleRadius), (int) (((float) TapTargetView.this.outerCircleCenter[1]) + TapTargetView.this.outerCircleRadius));
                        outline.setAlpha(((float) TapTargetView.this.outerCircleAlpha) / 255.0f);
                        if (VERSION.SDK_INT >= 22) {
                            outline.offset(0, TapTargetView.this.SHADOW_DIM);
                        }
                    }
                }
            };
            setOutlineProvider(this.outlineProvider);
            setElevation((float) this.SHADOW_DIM);
        }
        boolean z = true;
        if ((!this.shouldDrawShadow || this.outlineProvider != null) && VERSION.SDK_INT >= 18) {
            setLayerType(2, null);
        } else {
            setLayerType(1, null);
        }
        Theme theme = context.getTheme();
        if (UiUtil.themeIntAttr(context, "isLightTheme") != 0) {
            z = false;
        }
        this.isDark = z;
        Integer outerCircleColorInt = this.target.outerCircleColorInt(context);
        if (outerCircleColorInt != null) {
            this.outerCirclePaint.setColor(outerCircleColorInt.intValue());
        } else if (theme != null) {
            this.outerCirclePaint.setColor(UiUtil.themeIntAttr(context, "colorPrimary"));
        } else {
            this.outerCirclePaint.setColor(-1);
        }
        Integer targetCircleColorInt = this.target.targetCircleColorInt(context);
        int i = ViewCompat.MEASURED_STATE_MASK;
        if (targetCircleColorInt != null) {
            this.targetCirclePaint.setColor(targetCircleColorInt.intValue());
        } else {
            this.targetCirclePaint.setColor(this.isDark ? ViewCompat.MEASURED_STATE_MASK : -1);
        }
        if (this.target.transparentTarget) {
            this.targetCirclePaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        this.targetCirclePulsePaint.setColor(this.targetCirclePaint.getColor());
        Integer dimColorInt = this.target.dimColorInt(context);
        if (dimColorInt != null) {
            this.dimColor = UiUtil.setAlpha(dimColorInt.intValue(), 0.3f);
        } else {
            this.dimColor = -1;
        }
        Integer titleTextColorInt = this.target.titleTextColorInt(context);
        if (titleTextColorInt != null) {
            this.titlePaint.setColor(titleTextColorInt.intValue());
        } else {
            TextPaint textPaint = this.titlePaint;
            if (!this.isDark) {
                i = -1;
            }
            textPaint.setColor(i);
        }
        Integer descriptionTextColorInt = this.target.descriptionTextColorInt(context);
        if (descriptionTextColorInt != null) {
            this.descriptionPaint.setColor(descriptionTextColorInt.intValue());
        } else {
            this.descriptionPaint.setColor(this.titlePaint.getColor());
        }
        if (this.target.typeface != null) {
            this.titlePaint.setTypeface(this.target.typeface);
            this.descriptionPaint.setTypeface(this.target.typeface);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDismiss(false);
    }

    /* access modifiers changed from: 0000 */
    public void onDismiss() {
        onDismiss(true);
    }

    /* access modifiers changed from: 0000 */
    public void onDismiss(boolean z) {
        ValueAnimator[] valueAnimatorArr;
        if (!this.isDismissed) {
            this.isDismissed = true;
            for (ValueAnimator valueAnimator : this.animators) {
                valueAnimator.cancel();
                valueAnimator.removeAllUpdateListeners();
            }
            ViewUtil.removeOnGlobalLayoutListener(getViewTreeObserver(), this.globalLayoutListener);
            this.visible = false;
            Listener listener2 = this.listener;
            if (listener2 != null) {
                listener2.onTargetDismissed(this, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!this.isDismissed && this.outerCircleCenter != null) {
            int i = this.topBoundary;
            if (i > 0 && this.bottomBoundary > 0) {
                canvas.clipRect(0, i, getWidth(), this.bottomBoundary);
            }
            int i2 = this.dimColor;
            if (i2 != -1) {
                canvas.drawColor(i2);
            }
            this.outerCirclePaint.setAlpha(this.outerCircleAlpha);
            if (this.shouldDrawShadow && this.outlineProvider == null) {
                int save = canvas.save();
                canvas.clipPath(this.outerCirclePath, Op.DIFFERENCE);
                this.outerCircleShadowPaint.setAlpha((int) (((float) this.outerCircleAlpha) * 0.2f));
                int[] iArr = this.outerCircleCenter;
                canvas.drawCircle((float) iArr[0], (float) iArr[1], this.outerCircleRadius, this.outerCircleShadowPaint);
                canvas.restoreToCount(save);
            }
            int[] iArr2 = this.outerCircleCenter;
            canvas.drawCircle((float) iArr2[0], (float) iArr2[1], this.outerCircleRadius, this.outerCirclePaint);
            this.targetCirclePaint.setAlpha(this.targetCircleAlpha);
            int i3 = this.targetCirclePulseAlpha;
            if (i3 > 0) {
                this.targetCirclePulsePaint.setAlpha(i3);
                canvas.drawCircle((float) this.targetBounds.centerX(), (float) this.targetBounds.centerY(), this.targetCirclePulseRadius, this.targetCirclePulsePaint);
            }
            canvas.drawCircle((float) this.targetBounds.centerX(), (float) this.targetBounds.centerY(), this.targetCircleRadius, this.targetCirclePaint);
            int save2 = canvas.save();
            canvas.clipPath(this.outerCirclePath);
            canvas.translate((float) this.textBounds.left, (float) this.textBounds.top);
            this.titlePaint.setAlpha(this.textAlpha);
            this.titleLayout.draw(canvas);
            if (this.descriptionLayout != null) {
                canvas.translate(0.0f, (float) (this.titleLayout.getHeight() + this.TEXT_SPACING));
                this.descriptionLayout.draw(canvas);
            }
            canvas.restoreToCount(save2);
            int save3 = canvas.save();
            if (this.tintedTarget != null) {
                canvas.translate((float) (this.targetBounds.centerX() - (this.tintedTarget.getWidth() / 2)), (float) (this.targetBounds.centerY() - (this.tintedTarget.getHeight() / 2)));
                canvas.drawBitmap(this.tintedTarget, 0.0f, 0.0f, this.targetCirclePaint);
            } else if (this.target.icon != null) {
                canvas.translate((float) (this.targetBounds.centerX() - (this.target.icon.getBounds().width() / 2)), (float) (this.targetBounds.centerY() - (this.target.icon.getBounds().height() / 2)));
                this.target.icon.setAlpha(this.targetCirclePaint.getAlpha());
                this.target.icon.draw(canvas);
            }
            canvas.restoreToCount(save3);
            if (this.debug) {
                drawDebugInformation(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.lastTouchX = motionEvent.getX();
        this.lastTouchY = motionEvent.getY();
        return super.onTouchEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!isVisible() || !this.cancelable || i != 4) {
            return false;
        }
        keyEvent.startTracking();
        return true;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!isVisible() || !this.isInteractable || !this.cancelable || i != 4 || !keyEvent.isTracking() || keyEvent.isCanceled()) {
            return false;
        }
        this.isInteractable = false;
        Listener listener2 = this.listener;
        if (listener2 != null) {
            listener2.onTargetCancel(this);
        } else {
            new Listener().onTargetCancel(this);
        }
        return true;
    }

    public void dismiss(boolean z) {
        this.pulseAnimation.cancel();
        this.expandAnimation.cancel();
        if (z) {
            this.dismissConfirmAnimation.start();
        } else {
            this.dismissAnimation.start();
        }
    }

    public void setDrawDebug(boolean z) {
        if (this.debug != z) {
            this.debug = z;
            postInvalidate();
        }
    }

    public boolean isVisible() {
        return !this.isDismissed && this.visible;
    }

    /* access modifiers changed from: 0000 */
    public void drawDebugInformation(Canvas canvas) {
        if (this.debugPaint == null) {
            this.debugPaint = new Paint();
            this.debugPaint.setARGB(255, 255, 0, 0);
            this.debugPaint.setStyle(Style.STROKE);
            this.debugPaint.setStrokeWidth((float) UiUtil.m20dp(getContext(), 1));
        }
        if (this.debugTextPaint == null) {
            this.debugTextPaint = new TextPaint();
            this.debugTextPaint.setColor(-65536);
            this.debugTextPaint.setTextSize((float) UiUtil.m21sp(getContext(), 16));
        }
        this.debugPaint.setStyle(Style.STROKE);
        canvas.drawRect(this.textBounds, this.debugPaint);
        canvas.drawRect(this.targetBounds, this.debugPaint);
        int[] iArr = this.outerCircleCenter;
        canvas.drawCircle((float) iArr[0], (float) iArr[1], 10.0f, this.debugPaint);
        int[] iArr2 = this.outerCircleCenter;
        canvas.drawCircle((float) iArr2[0], (float) iArr2[1], (float) (this.calculatedOuterCircleRadius - this.CIRCLE_PADDING), this.debugPaint);
        canvas.drawCircle((float) this.targetBounds.centerX(), (float) this.targetBounds.centerY(), (float) (this.TARGET_RADIUS + this.TARGET_PADDING), this.debugPaint);
        this.debugPaint.setStyle(Style.FILL);
        StringBuilder sb = new StringBuilder();
        sb.append("Text bounds: ");
        sb.append(this.textBounds.toShortString());
        String str = "\nTarget bounds: ";
        sb.append(str);
        sb.append(this.targetBounds.toShortString());
        sb.append("\nCenter: ");
        sb.append(this.outerCircleCenter[0]);
        String str2 = " ";
        sb.append(str2);
        sb.append(this.outerCircleCenter[1]);
        sb.append("\nView size: ");
        sb.append(getWidth());
        sb.append(str2);
        sb.append(getHeight());
        sb.append(str);
        sb.append(this.targetBounds.toShortString());
        String sb2 = sb.toString();
        SpannableStringBuilder spannableStringBuilder = this.debugStringBuilder;
        if (spannableStringBuilder == null) {
            this.debugStringBuilder = new SpannableStringBuilder(sb2);
        } else {
            spannableStringBuilder.clear();
            this.debugStringBuilder.append(sb2);
        }
        if (this.debugLayout == null) {
            DynamicLayout dynamicLayout = new DynamicLayout(sb2, this.debugTextPaint, getWidth(), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.debugLayout = dynamicLayout;
        }
        int save = canvas.save();
        this.debugPaint.setARGB(220, 0, 0, 0);
        canvas.translate(0.0f, (float) this.topBoundary);
        canvas.drawRect(0.0f, 0.0f, (float) this.debugLayout.getWidth(), (float) this.debugLayout.getHeight(), this.debugPaint);
        this.debugPaint.setARGB(255, 255, 0, 0);
        this.debugLayout.draw(canvas);
        canvas.restoreToCount(save);
    }

    /* access modifiers changed from: 0000 */
    public void drawTintedTarget() {
        Drawable drawable = this.target.icon;
        if (!this.shouldTintTarget || drawable == null) {
            this.tintedTarget = null;
        } else if (this.tintedTarget == null) {
            this.tintedTarget = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(this.tintedTarget);
            drawable.setColorFilter(new PorterDuffColorFilter(this.outerCirclePaint.getColor(), Mode.SRC_ATOP));
            drawable.draw(canvas);
            drawable.setColorFilter(null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateTextLayouts() {
        int min = Math.min(getWidth(), this.TEXT_MAX_WIDTH) - (this.TEXT_PADDING * 2);
        if (min < 0) {
            min = Math.abs(min);
        }
        StaticLayout staticLayout = new StaticLayout(this.title, this.titlePaint, min, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.titleLayout = staticLayout;
        CharSequence charSequence = this.description;
        if (charSequence != null) {
            StaticLayout staticLayout2 = new StaticLayout(charSequence, this.descriptionPaint, min, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.descriptionLayout = staticLayout2;
            return;
        }
        this.descriptionLayout = null;
    }

    /* access modifiers changed from: 0000 */
    public void calculateDimensions() {
        this.textBounds = getTextBounds();
        this.outerCircleCenter = getOuterCircleCenterPoint();
        int[] iArr = this.outerCircleCenter;
        this.calculatedOuterCircleRadius = getOuterCircleRadius(iArr[0], iArr[1], this.textBounds, this.targetBounds);
    }

    /* access modifiers changed from: 0000 */
    public void calculateDrawingBounds() {
        int[] iArr = this.outerCircleCenter;
        if (iArr != null) {
            this.drawingBounds.left = (int) Math.max(0.0f, ((float) iArr[0]) - this.outerCircleRadius);
            this.drawingBounds.top = (int) Math.min(0.0f, ((float) this.outerCircleCenter[1]) - this.outerCircleRadius);
            this.drawingBounds.right = (int) Math.min((float) getWidth(), ((float) this.outerCircleCenter[0]) + this.outerCircleRadius + ((float) this.CIRCLE_PADDING));
            this.drawingBounds.bottom = (int) Math.min((float) getHeight(), ((float) this.outerCircleCenter[1]) + this.outerCircleRadius + ((float) this.CIRCLE_PADDING));
        }
    }

    /* access modifiers changed from: 0000 */
    public int getOuterCircleRadius(int i, int i2, Rect rect, Rect rect2) {
        int centerX = rect2.centerX();
        int centerY = rect2.centerY();
        int i3 = (int) (((float) this.TARGET_RADIUS) * 1.1f);
        Rect rect3 = new Rect(centerX, centerY, centerX, centerY);
        int i4 = -i3;
        rect3.inset(i4, i4);
        return Math.max(maxDistanceToPoints(i, i2, rect), maxDistanceToPoints(i, i2, rect3)) + this.CIRCLE_PADDING;
    }

    /* access modifiers changed from: 0000 */
    public Rect getTextBounds() {
        int totalTextHeight = getTotalTextHeight();
        int totalTextWidth = getTotalTextWidth();
        int centerY = ((this.targetBounds.centerY() - this.TARGET_RADIUS) - this.TARGET_PADDING) - totalTextHeight;
        if (centerY <= this.topBoundary) {
            centerY = this.targetBounds.centerY() + this.TARGET_RADIUS + this.TARGET_PADDING;
        }
        int max = Math.max(this.TEXT_PADDING, (this.targetBounds.centerX() - ((getWidth() / 2) - this.targetBounds.centerX() < 0 ? -this.TEXT_POSITIONING_BIAS : this.TEXT_POSITIONING_BIAS)) - totalTextWidth);
        return new Rect(max, centerY, Math.min(getWidth() - this.TEXT_PADDING, totalTextWidth + max), totalTextHeight + centerY);
    }

    /* access modifiers changed from: 0000 */
    public int[] getOuterCircleCenterPoint() {
        int i;
        if (inGutter(this.targetBounds.centerY())) {
            return new int[]{this.targetBounds.centerX(), this.targetBounds.centerY()};
        }
        int max = (Math.max(this.targetBounds.width(), this.targetBounds.height()) / 2) + this.TARGET_PADDING;
        int totalTextHeight = getTotalTextHeight();
        boolean z = ((this.targetBounds.centerY() - this.TARGET_RADIUS) - this.TARGET_PADDING) - totalTextHeight > 0;
        int min = Math.min(this.textBounds.left, this.targetBounds.left - max);
        int max2 = Math.max(this.textBounds.right, this.targetBounds.right + max);
        if (z) {
            i = (((this.targetBounds.centerY() - this.TARGET_RADIUS) - this.TARGET_PADDING) - totalTextHeight) + this.titleLayout.getHeight();
        } else {
            i = this.titleLayout.getHeight() + this.targetBounds.centerY() + this.TARGET_RADIUS + this.TARGET_PADDING;
        }
        return new int[]{(min + max2) / 2, i};
    }

    /* access modifiers changed from: 0000 */
    public int getTotalTextHeight() {
        int height;
        int i;
        if (this.descriptionLayout == null) {
            height = this.titleLayout.getHeight();
            i = this.TEXT_SPACING;
        } else {
            height = this.titleLayout.getHeight() + this.descriptionLayout.getHeight();
            i = this.TEXT_SPACING;
        }
        return height + i;
    }

    /* access modifiers changed from: 0000 */
    public int getTotalTextWidth() {
        if (this.descriptionLayout == null) {
            return this.titleLayout.getWidth();
        }
        return Math.max(this.titleLayout.getWidth(), this.descriptionLayout.getWidth());
    }

    /* access modifiers changed from: 0000 */
    public boolean inGutter(int i) {
        int i2 = this.bottomBoundary;
        boolean z = false;
        if (i2 > 0) {
            int i3 = this.GUTTER_DIM;
            if (i < i3 || i > i2 - i3) {
                z = true;
            }
            return z;
        }
        if (i < this.GUTTER_DIM || i > getHeight() - this.GUTTER_DIM) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public int maxDistanceToPoints(int i, int i2, Rect rect) {
        return (int) Math.max(distance(i, i2, rect.left, rect.top), Math.max(distance(i, i2, rect.right, rect.top), Math.max(distance(i, i2, rect.left, rect.bottom), distance(i, i2, rect.right, rect.bottom))));
    }

    /* access modifiers changed from: 0000 */
    public double distance(int i, int i2, int i3, int i4) {
        return Math.sqrt(Math.pow((double) (i3 - i), 2.0d) + Math.pow((double) (i4 - i2), 2.0d));
    }

    /* access modifiers changed from: 0000 */
    public void invalidateViewAndOutline(Rect rect) {
        invalidate(rect);
        if (this.outlineProvider != null && VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }
}
