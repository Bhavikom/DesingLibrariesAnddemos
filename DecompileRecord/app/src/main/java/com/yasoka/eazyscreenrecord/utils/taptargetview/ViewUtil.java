package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

class ViewUtil {
    ViewUtil() {
    }

    private static boolean isLaidOut(View view) {
        return ViewCompat.isLaidOut(view) && view.getWidth() > 0 && view.getHeight() > 0;
    }

    static void onLaidOut(final View view, final Runnable runnable) {
        if (isLaidOut(view)) {
            runnable.run();
            return;
        }
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = null;
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver = viewTreeObserver;
                } else {
                    viewTreeObserver = view.getViewTreeObserver();
                }
                ViewUtil.removeOnGlobalLayoutListener(viewTreeObserver, this);
                runnable.run();
            }
        });
    }

    static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, OnGlobalLayoutListener onGlobalLayoutListener) {
        if (VERSION.SDK_INT >= 16) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
    }

    static void removeView(ViewManager viewManager, View view) {
        if (viewManager != null && view != null) {
            try {
                viewManager.removeView(view);
            } catch (Exception unused) {
            }
        }
    }
}
