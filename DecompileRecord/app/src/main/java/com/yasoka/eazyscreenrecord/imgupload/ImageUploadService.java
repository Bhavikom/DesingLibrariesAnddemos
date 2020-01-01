package com.yasoka.eazyscreenrecord.imgupload;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.imgupload.ProgressRequestBody.UploadCallbacks;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.UploadImageResponse;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ImageUploadService extends IntentService implements UploadCallbacks {
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    private static final int REQ_CODE_COPY_URL = 1201;
    private static final int REQ_CODE_SHARE_URL = 1202;
    public static boolean isCancelled = false;
    private String aaId;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (ImageUploadService.this.f112ss != null) {
                ImageUploadService.this.f112ss.cancel();
            }
            ImageUploadService.isCancelled = true;
        }
    };
    private String emailId;
    private String filePath;

    /* renamed from: id */
    private int f111id = 132;
    private Builder mBuilder;
    private NotificationManager mNotifyManager;
    private SharedPreferences sharedPreferences2;
    /* access modifiers changed from: private */

    /* renamed from: ss */
    public Call<UploadImageResponse> f112ss;
    private String userId;

    public ImageUploadService() {
        super("ImageUploadService");
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"RestrictedApi"})
    public void onHandleIntent(Intent intent) {
        String str = "text/plain";
        this.filePath = intent.getStringExtra("file_path");
        this.emailId = intent.getStringExtra("email");
        this.userId = intent.getStringExtra("uId");
        this.aaId = intent.getStringExtra("aId");
        File file = new File(this.filePath);
        isCancelled = false;
        this.sharedPreferences2 = getSharedPreferences(MainActivity.SHARED_NAME2, 0);
        registerReceiver(this.broadcastReceiver, new IntentFilter("CancelUpload"));
        this.mNotifyManager = (NotificationManager) getSystemService("notification");
        String str2 = CHANNEL_ID;
        this.mBuilder = new Builder(this, str2);
        this.mBuilder.setContentTitle(getString(C0793R.string.upload_img_cloud)).setContentText(getString(C0793R.string.upload_prg)).setProgress(0, 0, false).setSmallIcon(17301640).setPriority(1);
        this.mBuilder.addAction(0, getString(C0793R.string.cancel), PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), CancelReceiver.class), 0));
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file, this);
        if (VERSION.SDK_INT >= 26 && this.mNotifyManager.getNotificationChannel(str2) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(str2, getString(C0793R.string.notification_channel), 2);
            notificationChannel.setDescription(getString(C0793R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            this.mNotifyManager.createNotificationChannel(notificationChannel);
        }
        this.mNotifyManager.notify(this.f111id, this.mBuilder.build());
        Part createFormData = Part.createFormData("image", file.getName(), progressRequestBody);
        String str3 = "multipart/form-data";
        RequestBody create = RequestBody.create(MediaType.parse(str3), this.emailId);
        RequestBody create2 = RequestBody.create(MediaType.parse(str3), this.userId);
        try {
            this.f112ss = ServerAPI.getInstance().getApiReference().upload(createFormData, RequestBody.create(MediaType.parse(str3), this.aaId), create, create2);
            UploadImageResponse uploadImageResponse = (UploadImageResponse) this.f112ss.execute().body();
            if (uploadImageResponse != null) {
                String imgUrl = uploadImageResponse.getImgUrl();
                if (isCancelled) {
                    return;
                }
                if (!TextUtils.isEmpty(imgUrl)) {
                    String trim = imgUrl.trim();
                    this.sharedPreferences2.edit().putString(this.filePath, trim).apply();
                    this.mBuilder.mActions.clear();
                    this.mBuilder.setContentTitle(getString(C0793R.string.img_upload_success));
                    this.mBuilder.setContentText(getString(C0793R.string.share_short_url));
                    this.mBuilder.setProgress(0, 0, false);
                    this.mBuilder.setSmallIcon(17301641);
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType(str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(C0793R.string.server_share));
                    sb.append(" ");
                    sb.append(trim);
                    sb.append("\n\n");
                    sb.append(getString(C0793R.string.app_download));
                    intent2.putExtra("android.intent.extra.TEXT", sb.toString());
                    intent2.setType(str);
                    PendingIntent activity = PendingIntent.getActivity(this, REQ_CODE_SHARE_URL, Intent.createChooser(intent2, "share..."), 134217728);
                    this.mBuilder.setFullScreenIntent(activity, false);
                    this.mBuilder.addAction(0, getString(C0793R.string.share), activity);
                    Intent intent3 = new Intent(getApplicationContext(), CopiedReceiver.class);
                    intent3.putExtra("copied", trim);
                    this.mBuilder.addAction(0, getString(C0793R.string.copy), PendingIntent.getBroadcast(getApplicationContext(), REQ_CODE_COPY_URL, intent3, 134217728));
                    this.mNotifyManager.notify(this.f111id, this.mBuilder.build());
                    return;
                }
                onError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void onProgressUpdate(int i) {
        this.mBuilder.setProgress(100, i, false);
        this.mNotifyManager.notify(this.f111id, this.mBuilder.build());
    }

    public void onError() {
        this.mNotifyManager.cancel(this.f111id);
        this.mBuilder.setContentTitle(getString(C0793R.string.img_upload_failed));
        this.mBuilder.setContentText(getString(C0793R.string.tap_retry));
        this.mBuilder.setSmallIcon(17301641);
        Intent intent = new Intent(getApplicationContext(), ImageUploadService.class);
        intent.putExtra("file_path", this.filePath);
        intent.putExtra("aId", this.aaId);
        intent.putExtra("uId", this.userId);
        intent.putExtra("email", this.emailId);
        this.mBuilder.addAction(0, getString(C0793R.string.retry), PendingIntent.getService(getApplicationContext(), 0, intent, 0));
        this.mBuilder.setProgress(0, 0, false);
        this.mNotifyManager.notify(this.f111id, this.mBuilder.build());
        stopSelf();
    }

    public void onFinish() {
        System.out.println("FINISH");
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiver);
    }
}
