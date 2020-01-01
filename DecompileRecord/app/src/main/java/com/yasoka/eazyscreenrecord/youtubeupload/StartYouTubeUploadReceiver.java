package com.yasoka.eazyscreenrecord.youtubeupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;

public class StartYouTubeUploadReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        System.out.println("Upload Received");
        Uri data = intent.getData();
        String str = UploadService.ACCOUNT_KEY;
        String stringExtra = intent.getStringExtra(str);
        String str2 = NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION;
        long longExtra = intent.getLongExtra(str2, 0);
        String str3 = "name";
        String stringExtra2 = intent.getStringExtra(str3);
        String str4 = "desc";
        String stringExtra3 = intent.getStringExtra(str4);
        Intent intent2 = new Intent(context, UploadService.class);
        intent2.setData(data);
        intent2.putExtra(str, stringExtra);
        intent2.putExtra(str2, longExtra);
        intent2.putExtra(str3, stringExtra2);
        intent2.putExtra(str4, stringExtra3);
        context.startService(intent2);
    }
}
