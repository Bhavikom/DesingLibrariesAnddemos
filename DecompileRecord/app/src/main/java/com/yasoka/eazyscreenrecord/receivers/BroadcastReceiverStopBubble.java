package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ezscreenrecorder.FloatingService;

public class BroadcastReceiverStopBubble extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            context.stopService(new Intent(context, FloatingService.class));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
}
