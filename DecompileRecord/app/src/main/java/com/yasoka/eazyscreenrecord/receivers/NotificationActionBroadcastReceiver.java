package com.yasoka.eazyscreenrecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.model.ActionMoreModel;
/*import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.model.ActionMoreModel;*/

public class NotificationActionBroadcastReceiver extends BroadcastReceiver {
    public static final String KEY_ACTION_FROM_NOTIFICATION = "action_from_notification";

    public void onReceive(Context context, Intent intent) {
        String str = KEY_ACTION_FROM_NOTIFICATION;
        String str2 = ActionMoreModel.KEY_IS_MORE_FROM_NOTIFICATION;
        try {
            context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent != null) {
            try {
                if (intent.hasExtra(str)) {
                    int intExtra = intent.getIntExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_NONE);
                    if (intExtra != 1300) {
                        Intent intent2 = new Intent(context, RecordingActivity.class);
                        intent2.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, intExtra);
                        intent2.addFlags(268435456);
                        if (intent.hasExtra(str2)) {
                            intent2.putExtra(str2, intent.getBooleanExtra(str2, false));
                        }
                        context.startActivity(intent2);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
