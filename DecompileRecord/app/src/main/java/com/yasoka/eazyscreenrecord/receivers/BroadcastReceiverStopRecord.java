package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.ServerAPI;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class BroadcastReceiverStopRecord extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            ServerAPI.getInstance().addToFireBase(context, "Stop Recording By User").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
            Intent intent2 = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
            intent2.putExtra(FloatingService.FLAG_SHOW_FLOATING, true);
            context.sendBroadcast(intent2);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
