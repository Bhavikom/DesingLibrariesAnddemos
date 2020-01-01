package com.yasoka.eazyscreenrecord.recorder.utils;

import android.util.Log;

public class SopCastLog {
    private static boolean open = false;

    public static void isOpen(boolean z) {
        open = z;
    }

    /* renamed from: d */
    public static void m10d(String str, String str2) {
        if (open) {
            Log.d(str, str2);
        }
    }

    /* renamed from: w */
    public static void m12w(String str, String str2) {
        if (open) {
            Log.w(str, str2);
        }
    }

    /* renamed from: e */
    public static void m11e(String str, String str2) {
        if (open) {
            Log.e(str, str2);
        }
    }
}
