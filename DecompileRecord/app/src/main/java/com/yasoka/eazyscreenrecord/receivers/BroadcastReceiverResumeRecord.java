package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.FloatingService;

public class BroadcastReceiverResumeRecord extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            Intent intent2 = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
            intent2.putExtra(FloatingService.FLAG_RESUME_RECORDING, true);
            context.sendBroadcast(intent2);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
