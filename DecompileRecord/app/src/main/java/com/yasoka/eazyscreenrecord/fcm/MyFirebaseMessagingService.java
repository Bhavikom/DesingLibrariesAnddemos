package com.yasoka.eazyscreenrecord.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.p000v4.app.NotificationCompat.BigTextStyle;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.utils.Logger;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.youtubeupload.ResumableUpload;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    public static final String IMAGE_LINK = "ImageLink";
    private static final String TAG = "MyFirebaseMsgService";

    public void onMessageReceived(RemoteMessage remoteMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ");
        sb.append(remoteMessage.getFrom());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (remoteMessage.getData().size() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Message data payload: ");
            sb3.append(remoteMessage.getData());
            Log.d(str, sb3.toString());
            if (PreferenceHelper.getInstance().getPrefPushNotification()) {
                sendNotification(remoteMessage.getData());
            }
        }
        if (remoteMessage.getNotification() != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Message Notification Body: ");
            sb4.append(remoteMessage.getNotification().getBody());
            Log.d(str, sb4.toString());
        }
    }

    private void sendNotification(Map<String, String> map) {
        Logger instance = Logger.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("SERVICE NOTI_TYPE->");
        String str = "noti_type";
        sb.append((String) map.get(str));
        sb.append("image_link->");
        String str2 = "image_link";
        sb.append((String) map.get(str2));
        instance.error(sb.toString());
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(GalleryActivity.YOU_TUBE_LIST, true);
        intent.putExtra(ResumableUpload.YOUTUBE_ID, (String) map.get("video_id"));
        intent.putExtra(IMAGE_LINK, (String) map.get(str2));
        intent.putExtra(GalleryActivity.IS_MY_VIDEOS, (String) map.get(str));
        intent.addFlags(67108864);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 134217728);
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        String str3 = "heading";
        String str4 = "";
        String str5 = map.containsKey(str3) ? (String) map.get(str3) : str4;
        if (TextUtils.isEmpty(str5)) {
            str5 = getString(C0793R.string.app_name);
        }
        String str6 = "description";
        if (map.containsKey(str6)) {
            str4 = (String) map.get(str6);
        }
        String str7 = CHANNEL_ID;
        Builder contentIntent = new Builder(this, str7).setSmallIcon(C0793R.mipmap.ic_launcher5).setLargeIcon(BitmapFactory.decodeResource(getResources(), C0793R.mipmap.ic_launcher)).setContentTitle(str5).setContentText(str4).setAutoCancel(true).setSound(defaultUri).setStyle(new BigTextStyle().bigText(str4)).setContentIntent(activity);
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (VERSION.SDK_INT >= 26 && notificationManager.getNotificationChannel(str7) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(str7, getString(C0793R.string.notification_channel), 2);
            notificationChannel.setDescription(getString(C0793R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(new Random().nextInt(), contentIntent.build());
    }
}
