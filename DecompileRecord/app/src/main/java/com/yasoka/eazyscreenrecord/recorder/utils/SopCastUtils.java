package com.yasoka.eazyscreenrecord.recorder.utils;

import android.os.Build.VERSION;
import android.os.Looper;

public class SopCastUtils {

    public interface INotUIProcessor {
        void process();
    }

    public static void processNotUI(final INotUIProcessor iNotUIProcessor) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() {
                public void run() {
                    iNotUIProcessor.process();
                }
            }).start();
        } else {
            iNotUIProcessor.process();
        }
    }

    public static boolean isOverLOLLIPOP() {
        return VERSION.SDK_INT >= 21;
    }
}
