package com.yasoka.eazyscreenrecord.imgdownload;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.ShowImagesActivity;
import com.ezscreenrecorder.utils.AppUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageDownloadService extends IntentService {
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    private final DateFormat fileFormat2 = new SimpleDateFormat("yyyyMMdd-HHmmss'.jpg'", Locale.US);
    /* access modifiers changed from: private */
    public Builder notificationBuilder;
    /* access modifiers changed from: private */
    public NotificationManager notificationManager;
    private int totalFileSize;

    public ImageDownloadService() {
        super("ImageDownloadService");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        Response execute;
        this.notificationManager = (NotificationManager) getSystemService("notification");
        String str = CHANNEL_ID;
        this.notificationBuilder = new Builder(this, str).setSmallIcon(17301633).setContentTitle(getString(C0793R.string.id_noti_down_prog_title_txt)).setContentText(getString(C0793R.string.id_noti_down_prog_desc_txt)).setFullScreenIntent(PendingIntent.getActivity(this, 0, new Intent(), 0), true).setAutoCancel(true);
        if (VERSION.SDK_INT >= 26 && this.notificationManager.getNotificationChannel(str) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(str, getString(C0793R.string.notification_channel), 2);
            notificationChannel.setDescription(getString(C0793R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }
        this.notificationManager.notify(0, this.notificationBuilder.build());
        Request build = new Request.Builder().url(intent.getStringExtra("file_url")).build();
        final C12161 r0 = new ProgressListener() {
            public void update(long j, long j2, boolean z) {
                System.out.println(j);
                System.out.println(j2);
                System.out.println(z);
                long j3 = (j * 100) / j2;
                System.out.format("%d%% done\n", new Object[]{Long.valueOf(j3)});
                ImageDownloadService.this.notificationBuilder.setProgress(100, (int) j3, false);
                ImageDownloadService.this.notificationManager.notify(0, ImageDownloadService.this.notificationBuilder.build());
            }
        };
        try {
            execute = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Response proceed = chain.proceed(chain.request());
                    return proceed.newBuilder().body(new ProgressResponseBody(proceed.body(), r0)).build();
                }
            }).build().newCall(build).execute();
            if (execute.isSuccessful()) {
                downloadFile(execute.body());
                if (execute != null) {
                    execute.close();
                    return;
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected code ");
            sb.append(execute);
            throw new IOException(sb.toString());
        } catch (NameNotFoundException e) {
            try {
                e.printStackTrace();
                this.notificationBuilder.setContentText(getString(C0793R.string.id_error_downloading_txt));
                this.notificationBuilder.setProgress(0, 0, false);
                this.notificationManager.notify(0, this.notificationBuilder.build());
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                this.notificationBuilder.setContentText(getString(C0793R.string.id_error_downloading_txt));
                this.notificationBuilder.setProgress(0, 0, false);
                this.notificationManager.notify(0, this.notificationBuilder.build());
                return;
            }
        } catch (Throwable th) {
            r0.addSuppressed(th);
        }
        throw th;
    }

    private void downloadFile(ResponseBody responseBody) throws IOException, NameNotFoundException {
        byte[] bArr = new byte[4096];
        long contentLength = responseBody.contentLength();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(responseBody.byteStream(), 8192);
        File file = new File(createImageFile());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        long currentTimeMillis = System.currentTimeMillis();
        int i = 1;
        long j = 0;
        while (true) {
            int read = bufferedInputStream.read(bArr);
            if (read != -1) {
                j += (long) read;
                BufferedInputStream bufferedInputStream2 = bufferedInputStream;
                File file2 = file;
                FileOutputStream fileOutputStream2 = fileOutputStream;
                long j2 = currentTimeMillis;
                this.totalFileSize = (int) (((double) contentLength) / Math.pow(1024.0d, 2.0d));
                int i2 = (int) ((100 * j) / contentLength);
                long currentTimeMillis2 = System.currentTimeMillis() - j2;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("CONT-->");
                sb.append(contentLength);
                printStream.println(sb.toString());
                if (currentTimeMillis2 > ((long) (i * 1000))) {
                    this.notificationBuilder.setProgress(100, i2, false);
                    this.notificationManager.notify(0, this.notificationBuilder.build());
                    i++;
                }
                FileOutputStream fileOutputStream3 = fileOutputStream2;
                fileOutputStream3.write(bArr, 0, read);
                fileOutputStream = fileOutputStream3;
                bufferedInputStream = bufferedInputStream2;
                currentTimeMillis = j2;
                file = file2;
            } else {
                BufferedInputStream bufferedInputStream3 = bufferedInputStream;
                File file3 = file;
                FileOutputStream fileOutputStream4 = fileOutputStream;
                onDownloadComplete(file3);
                fileOutputStream4.flush();
                fileOutputStream4.close();
                bufferedInputStream3.close();
                return;
            }
        }
    }

    public Uri addImage(File file, String str) throws Exception {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("title", str);
        contentValues.put("mime_type", "image/*");
        contentValues.put("_data", file.getAbsolutePath());
        return getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    private String createImageFile() throws NameNotFoundException {
        String imageDir = AppUtils.getImageDir(getApplicationContext());
        String format = this.fileFormat2.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(imageDir);
        sb.append(format);
        File file = new File(sb.toString());
        try {
            addImage(file, file.getPath());
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            Toast.makeText(this, C0793R.string.no_permission_allow_save_img, 1).show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Log.e("image file:", file.getPath());
        return file.getAbsolutePath();
    }

    private void onDownloadComplete(File file) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("PATH-->");
        sb.append(file.getAbsolutePath());
        printStream.println(sb.toString());
        this.notificationManager.cancel(0);
        this.notificationBuilder.setProgress(0, 0, false).setSmallIcon(17301634).setContentTitle(getString(C0793R.string.id_download_success_txt));
        this.notificationBuilder.setContentText(getString(C0793R.string.id_click_to_view));
        Intent intent = new Intent(this, ShowImagesActivity.class);
        intent.putExtra("ImgPath", file.getAbsolutePath());
        intent.putExtra("isFromServer", false);
        intent.setFlags(268468224);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 134217728);
        this.notificationBuilder.addAction(0, getString(C0793R.string.id_view_txt), activity).setFullScreenIntent(activity, true);
        this.notificationManager.notify(0, this.notificationBuilder.build());
    }
}
