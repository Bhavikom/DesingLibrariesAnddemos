package com.yasoka.eazyscreenrecord.youtubeupload;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import com.ezscreenrecorder.utils.AppUtils;
import java.io.IOException;
import java.util.Iterator;

public class CancelBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            AppUtils.uploadFile(context, false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        context.stopService(new Intent(context, UploadService.class));
        Iterator it = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RunningAppProcessInfo runningAppProcessInfo = (RunningAppProcessInfo) it.next();
            StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(":ezuploadservice");
            if (runningAppProcessInfo.processName.equals(sb.toString())) {
                Process.killProcess(runningAppProcessInfo.pid);
                break;
            }
        }
        ((NotificationManager) context.getSystemService("notification")).cancel(UploadService.UPLOAD_NOTIFICATION_ID);
    }
}
