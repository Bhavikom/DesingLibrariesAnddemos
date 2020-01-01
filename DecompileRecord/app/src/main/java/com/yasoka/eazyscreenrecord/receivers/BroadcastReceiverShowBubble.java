package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.FloatingService;

public class BroadcastReceiverShowBubble extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            new Intent(FloatingService.FLAG_RUNNING_SERVICE).putExtra(FloatingService.NEW_FLAG_SHOW_FLOATING, true);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
