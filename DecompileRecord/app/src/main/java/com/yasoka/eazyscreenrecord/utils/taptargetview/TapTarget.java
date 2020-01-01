package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TapTarget {
    Rect bounds;
    boolean cancelable;
    @Nullable
    final CharSequence description;
    private Integer descriptionTextColor;
    @ColorRes
    private int descriptionTextColorRes;
    @DimenRes
    private int descriptionTextDimen;
    private int descriptionTextSize;
    private Integer dimColor;
    @ColorRes
    private int dimColorRes;
    boolean drawShadow;
    Drawable icon;

    /* renamed from: id */
    int f121id;
    private Integer outerCircleColor;
    @ColorRes
    private int outerCircleColorRes;
    private Integer targetCircleColor;
    @ColorRes
    private int targetCircleColorRes;
    int targetRadius;
    boolean tintTarget;
    final CharSequence title;
    private Integer titleTextColor;
    @ColorRes
    private int titleTextColorRes;
    @DimenRes
    private int titleTextDimen;
    private int titleTextSize;
    boolean transparentTarget;
    Typeface typeface;

    protected TapTarget(Rect rect, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        this(charSequence, charSequence2);
        if (rect != null) {
            this.bounds = rect;
            return;
        }
        throw new IllegalArgumentException("Cannot pass null bounds or title");
    }

    protected TapTarget(CharSequence charSequence, @Nullable CharSequence charSequence2) {
        this.targetRadius = 44;
        this.f121id = -1;
        this.drawShadow = false;
        this.cancelable = true;
        this.tintTarget = true;
        this.transparentTarget = false;
        this.outerCircleColorRes = -1;
        this.targetCircleColorRes = -1;
        this.dimColorRes = -1;
        this.titleTextColorRes = -1;
        this.descriptionTextColorRes = -1;
        this.outerCircleColor = null;
        this.targetCircleColor = null;
        this.dimColor = null;
        this.titleTextColor = null;
        this.descriptionTextColor = null;
        this.titleTextDimen = -1;
        this.descriptionTextDimen = -1;
        this.titleTextSize = 20;
        this.descriptionTextSize = 18;
        if (charSequence != null) {
            this.title = charSequence;
            this.description = charSequence2;
            return;
        }
        throw new IllegalArgumentException("Cannot pass null title");
    }

    public static TapTarget forToolbarOverflow(Toolbar toolbar, CharSequence charSequence) {
        return forToolbarOverflow(toolbar, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarOverflow(Toolbar toolbar, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, false, charSequence, charSequence2);
    }

    public static TapTarget forToolbarOverflow(android.widget.Toolbar toolbar, CharSequence charSequence) {
        return forToolbarOverflow(toolbar, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarOverflow(android.widget.Toolbar toolbar, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, false, charSequence, charSequence2);
    }

    public static TapTarget forToolbarNavigationIcon(Toolbar toolbar, CharSequence charSequence) {
        return forToolbarNavigationIcon(toolbar, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarNavigationIcon(Toolbar toolbar, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, true, charSequence, charSequence2);
    }

    public static TapTarget forToolbarNavigationIcon(android.widget.Toolbar toolbar, CharSequence charSequence) {
        return forToolbarNavigationIcon(toolbar, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarNavigationIcon(android.widget.Toolbar toolbar, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, true, charSequence, charSequence2);
    }

    public static TapTarget forToolbarMenuItem(Toolbar toolbar, @IdRes int i, CharSequence charSequence) {
        return forToolbarMenuItem(toolbar, i, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarMenuItem(Toolbar toolbar, @IdRes int i, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, i, charSequence, charSequence2);
    }

    public static TapTarget forToolbarMenuItem(android.widget.Toolbar toolbar, @IdRes int i, CharSequence charSequence) {
        return forToolbarMenuItem(toolbar, i, charSequence, (CharSequence) null);
    }

    public static TapTarget forToolbarMenuItem(android.widget.Toolbar toolbar, @IdRes int i, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ToolbarTapTarget(toolbar, i, charSequence, charSequence2);
    }

    public static TapTarget forView(View view, CharSequence charSequence) {
        return forView(view, charSequence, null);
    }

    public static TapTarget forView(View view, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new ViewTapTarget(view, charSequence, charSequence2);
    }

    public static TapTarget forBounds(Rect rect, CharSequence charSequence) {
        return forBounds(rect, charSequence, null);
    }

    public static TapTarget forBounds(Rect rect, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        return new TapTarget(rect, charSequence, charSequence2);
    }

    public TapTarget transparentTarget(boolean z) {
        this.transparentTarget = z;
        return this;
    }

    public TapTarget outerCircleColor(@ColorRes int i) {
        this.outerCircleColorRes = i;
        return this;
    }

    public TapTarget outerCircleColorInt(@ColorInt int i) {
        this.outerCircleColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget targetCircleColor(@ColorRes int i) {
        this.targetCircleColorRes = i;
        return this;
    }

    public TapTarget targetCircleColorInt(@ColorInt int i) {
        this.targetCircleColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget textColor(@ColorRes int i) {
        this.titleTextColorRes = i;
        this.descriptionTextColorRes = i;
        return this;
    }

    public TapTarget textColorInt(@ColorInt int i) {
        this.titleTextColor = Integer.valueOf(i);
        this.descriptionTextColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget titleTextColor(@ColorRes int i) {
        this.titleTextColorRes = i;
        return this;
    }

    public TapTarget titleTextColorInt(@ColorInt int i) {
        this.titleTextColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget descriptionTextColor(@ColorRes int i) {
        this.descriptionTextColorRes = i;
        return this;
    }

    public TapTarget descriptionTextColorInt(@ColorInt int i) {
        this.descriptionTextColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget textTypeface(Typeface typeface2) {
        if (typeface2 != null) {
            this.typeface = typeface2;
            return this;
        }
        throw new IllegalArgumentException("Cannot use a null typeface");
    }

    public TapTarget titleTextSize(int i) {
        if (i >= 0) {
            this.titleTextSize = i;
            return this;
        }
        throw new IllegalArgumentException("Given negative text size");
    }

    public TapTarget descriptionTextSize(int i) {
        if (i >= 0) {
            this.descriptionTextSize = i;
            return this;
        }
        throw new IllegalArgumentException("Given negative text size");
    }

    public TapTarget titleTextDimen(@DimenRes int i) {
        this.titleTextDimen = i;
        return this;
    }

    public TapTarget descriptionTextDimen(@DimenRes int i) {
        this.descriptionTextDimen = i;
        return this;
    }

    public TapTarget dimColor(@ColorRes int i) {
        this.dimColorRes = i;
        return this;
    }

    public TapTarget dimColorInt(@ColorInt int i) {
        this.dimColor = Integer.valueOf(i);
        return this;
    }

    public TapTarget drawShadow(boolean z) {
        this.drawShadow = z;
        return this;
    }

    public TapTarget cancelable(boolean z) {
        this.cancelable = z;
        return this;
    }

    public TapTarget tintTarget(boolean z) {
        this.tintTarget = z;
        return this;
    }

    public TapTarget icon(Drawable drawable) {
        return icon(drawable, false);
    }

    public TapTarget icon(Drawable drawable, boolean z) {
        if (drawable != null) {
            this.icon = drawable;
            if (!z) {
                Drawable drawable2 = this.icon;
                drawable2.setBounds(new Rect(0, 0, drawable2.getIntrinsicWidth(), this.icon.getIntrinsicHeight()));
            }
            return this;
        }
        throw new IllegalArgumentException("Cannot use null drawable");
    }

    /* renamed from: id */
    public TapTarget mo14902id(int i) {
        this.f121id = i;
        return this;
    }

    public TapTarget targetRadius(int i) {
        this.targetRadius = i;
        return this;
    }

    /* renamed from: id */
    public int mo14901id() {
        return this.f121id;
    }

    public void onReady(Runnable runnable) {
        runnable.run();
    }

    public Rect bounds() {
        Rect rect = this.bounds;
        if (rect != null) {
            return rect;
        }
        throw new IllegalStateException("Requesting bounds that are not set! Make sure your target is ready");
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer outerCircleColorInt(Context context) {
        return colorResOrInt(context, this.outerCircleColor, this.outerCircleColorRes);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer targetCircleColorInt(Context context) {
        return colorResOrInt(context, this.targetCircleColor, this.targetCircleColorRes);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer dimColorInt(Context context) {
        return colorResOrInt(context, this.dimColor, this.dimColorRes);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer titleTextColorInt(Context context) {
        return colorResOrInt(context, this.titleTextColor, this.titleTextColorRes);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer descriptionTextColorInt(Context context) {
        return colorResOrInt(context, this.descriptionTextColor, this.descriptionTextColorRes);
    }

    /* access modifiers changed from: 0000 */
    public int titleTextSizePx(Context context) {
        return dimenOrSize(context, this.titleTextSize, this.titleTextDimen);
    }

    /* access modifiers changed from: 0000 */
    public int descriptionTextSizePx(Context context) {
        return dimenOrSize(context, this.descriptionTextSize, this.descriptionTextDimen);
    }

    @Nullable
    private Integer colorResOrInt(Context context, @Nullable Integer num, @ColorRes int i) {
        return i != -1 ? Integer.valueOf(ContextCompat.getColor(context, i)) : num;
    }

    private int dimenOrSize(Context context, int i, @DimenRes int i2) {
        if (i2 != -1) {
            return (int) context.getResources().getDimension(this.f121id);
        }
        return UiUtil.m21sp(context, i);
    }
}
