package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.RecordingActivity;

public class BroadcastReceiverScreenShot extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            Intent intent2 = new Intent(context, RecordingActivity.class);
            intent2.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
            intent2.addFlags(268435456);
            context.startActivity(intent2);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
}
