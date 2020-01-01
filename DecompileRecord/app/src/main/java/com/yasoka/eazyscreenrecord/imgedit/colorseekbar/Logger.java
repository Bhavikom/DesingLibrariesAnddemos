package com.yasoka.eazyscreenrecord.imgedit.colorseekbar;

import android.util.Log;

class Logger {
    private static final String TAG = "ColorSeekBarLib";
    private static boolean debug = false;

    Logger() {
    }

    /* renamed from: i */
    public static void m17i(String str) {
        if (debug) {
            Log.i(TAG, str);
        }
    }

    public static void spec(int i) {
        if (debug) {
            String str = TAG;
            if (i == Integer.MIN_VALUE) {
                Log.i(str, "AT_MOST");
            } else if (i == 0) {
                Log.i(str, "UNSPECIFIED");
            } else if (i != 1073741824) {
                Log.i(str, String.valueOf(i));
            } else {
                Log.i(str, "EXACTLY");
            }
        }
    }
}
