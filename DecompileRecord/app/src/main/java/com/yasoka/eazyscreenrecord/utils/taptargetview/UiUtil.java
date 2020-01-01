package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;

class UiUtil {
    static int setAlpha(int i, float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f <= 0.0f) {
            f = 0.0f;
        }
        return (i & ViewCompat.MEASURED_SIZE_MASK) | (((int) (((float) (i >>> 24)) * f)) << 24);
    }

    UiUtil() {
    }

    /* renamed from: dp */
    static int m20dp(Context context, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    /* renamed from: sp */
    static int m21sp(Context context, int i) {
        return (int) TypedValue.applyDimension(2, (float) i, context.getResources().getDisplayMetrics());
    }

    static int themeIntAttr(Context context, String str) {
        Theme theme = context.getTheme();
        if (theme == null) {
            return -1;
        }
        TypedValue typedValue = new TypedValue();
        int identifier = context.getResources().getIdentifier(str, "attr", context.getPackageName());
        if (identifier == 0) {
            return -1;
        }
        theme.resolveAttribute(identifier, typedValue, true);
        return typedValue.data;
    }
}
