package com.yasoka.eazyscreenrecord.utils;

import android.os.Bundle;
import com.ezscreenrecorder.RecorderApplication;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.TimeUnit;
import p009io.reactivex.Single;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class FirebaseConversionEventHelper {
    public static final String KEY_AUDIO_RECORD_EVENT = "Audio";
    public static final String KEY_EXPLAINER_VIDEO_EVENT = "Tutorial";
    public static final String KEY_FIRST_ACTION_AFTER_INSTALL = "FirstAction";
    public static final String KEY_GAME_RECORD_EVENT = "GameRec";
    public static final String KEY_INTERACTIVE_VIDEO_RECORD_EVENT = "iVideo";
    public static final String KEY_SCREENSHOT_TAKEN_EVENT = "Image";
    public static final String KEY_VIDEO_RECORD_EVENT = "Video";
    /* access modifiers changed from: private */
    public static FirebaseAnalytics firebaseAnalytics;
    private static final FirebaseConversionEventHelper ourInstance = new FirebaseConversionEventHelper();

    public static FirebaseConversionEventHelper getInstance() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(RecorderApplication.getInstance().getApplicationContext());
        return ourInstance;
    }

    private FirebaseConversionEventHelper() {
    }

    public void sendFirstActionConversion(String str) {
        final Bundle bundle = new Bundle();
        bundle.putString("ActionType", str);
        Single.timer(200, TimeUnit.MILLISECONDS).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Long>() {
            public void onSuccess(Long l) {
                if (FirebaseConversionEventHelper.firebaseAnalytics != null) {
                    FirebaseConversionEventHelper.firebaseAnalytics.logEvent(FirebaseConversionEventHelper.KEY_FIRST_ACTION_AFTER_INSTALL, bundle);
                }
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }
}
