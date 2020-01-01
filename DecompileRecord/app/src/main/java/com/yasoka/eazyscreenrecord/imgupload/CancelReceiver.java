package com.yasoka.eazyscreenrecord.imgupload;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;

public class CancelReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            ImageUploadService.isCancelled = true;
            context.sendBroadcast(new Intent("CancelUpload"));
            ((NotificationManager) context.getSystemService("notification")).cancel(132);
            Toast.makeText(context, C0793R.string.id_cancel_success_txt, 1).show();
            context.stopService(new Intent(context, ImageUploadService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
