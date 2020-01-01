package com.yasoka.eazyscreenrecord.video;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Utils {
    public static int m16637a(Context context, int i) {
        return (int) (((double) (((float) i) * m16644e(context))) + 0.5d);
    }

    public static DisplayMetrics m16638a(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static void setViewHeight(float f, View view, boolean z) {
        LayoutParams layoutParams;
        RelativeLayout relativeLayout = (RelativeLayout) view.getParent();
        float width = (float) relativeLayout.getWidth();
        float height = (float) relativeLayout.getHeight();
        if (f > (1.0f * width) / height) {
            layoutParams = new LayoutParams(-1, (int) (width / f));
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            LayoutParams layoutParams2 = new LayoutParams((int) (height * f), -1);
            layoutParams2.setMargins(0, 0, 0, 0);
            layoutParams = layoutParams2;
        }
        layoutParams.addRule(13);
        view.setLayoutParams(layoutParams);
    }

    public static int m16640b(Context context) {
        return Math.round((((float) m16642c(context)) * 1.0f) / m16644e(context));
    }

    public static int m16641b(Context context, int i) {
        return (int) (((double) (((float) i) / m16644e(context))) + 0.5d);
    }

    public static int m16642c(Context context) {
        return m16638a(context).widthPixels;
    }

    public static int m16643d(Context context) {
        return m16638a(context).heightPixels;
    }

    public static float m16644e(Context context) {
        return m16638a(context).density;
    }

    public static boolean m16645f(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static boolean m16646g(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return identifier > 0 && resources.getBoolean(identifier);
    }

    public static int m16647h(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int m16648i(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }
}
