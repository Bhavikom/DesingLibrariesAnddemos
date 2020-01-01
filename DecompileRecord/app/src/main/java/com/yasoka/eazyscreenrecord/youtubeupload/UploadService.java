package com.yasoka.eazyscreenrecord.youtubeupload;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.CheckYouTubeActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.UploadOutput;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.facebook.internal.AnalyticsEvents;
import com.google.android.gms.common.Scopes;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;
import java.io.IOException;
import p009io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import p009io.reactivex.functions.Consumer;

public class UploadService extends IntentService {
    public static final String ACCOUNT_KEY = "accountName";
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_UPLOAD};
    private static String TAG1 = "SDSD";
    public static int UPLOAD_NOTIFICATION_ID = 1001;
    private final int MAX_RETRY = 0;
    private int PLAYBACK_NOTIFICATION_ID = 1002;
    private final int PROCESSING_POLL_INTERVAL_SEC = 6;
    private final int PROCESSING_TIMEOUT_SEC = 1200;
    private final String SUCCEEDED = AnalyticsEvents.PARAMETER_SHARE_OUTCOME_SUCCEEDED;
    private final String TAG = "UploadingActivity";
    private final int UPLOAD_REATTEMPT_DELAY_SEC = 6;
    private String VIDEO_FILE_FORMAT = "video/*";
    private Intent authIntent;
    private String chosenAccountName;
    GoogleAccountCredential credential;
    private String desc;
    private long duration;
    private Uri fileUri;
    final JsonFactory jsonFactory = new GsonFactory();
    private long mStartTime;
    private int mUploadAttemptCount;
    private String name;
    private SharedPreferences sharedPreferences1;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();

    public UploadService() {
        super("YTUploadService");
    }

    private static void zzz(int i) throws InterruptedException {
        Log.d(TAG1, String.format("Sleeping for [%d] ms ...", new Object[]{Integer.valueOf(i)}));
        Thread.sleep((long) i);
        Log.d(TAG1, String.format("Sleeping for [%d] ms ... done", new Object[]{Integer.valueOf(i)}));
    }

    public void showProcessingNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        String str = CHANNEL_ID;
        Builder builder = new Builder(context, str);
        builder.setContentTitle(getString(C0793R.string.youtube_upload)).setContentText("Processing Video... ").setSmallIcon(C0793R.mipmap.ic_launcher5);
        if (VERSION.SDK_INT >= 26 && notificationManager.getNotificationChannel(str) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(str, context.getString(C0793R.string.notification_channel), 2);
            notificationChannel.setDescription(context.getString(C0793R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(this.PLAYBACK_NOTIFICATION_ID, builder.build());
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
    }

    private boolean timeoutExpired(long j, int i) {
        return System.currentTimeMillis() - j >= ((long) (i * 1000));
    }

    public void removeNotification(Context context) {
        ((NotificationManager) context.getSystemService("notification")).cancel(UPLOAD_NOTIFICATION_ID);
        try {
            AppUtils.uploadFile(this, false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public int onStartCommand(@Nullable Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        this.fileUri = intent.getData();
        this.chosenAccountName = intent.getStringExtra(ACCOUNT_KEY);
        this.duration = intent.getLongExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, 0);
        this.name = intent.getStringExtra("name");
        this.desc = intent.getStringExtra("desc");
        try {
            AppUtils.uploadFile(this, true);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Lists.newArrayList((E[]) SCOPES));
        this.credential.setSelectedAccountName(this.chosenAccountName);
        this.credential.setBackOff(new ExponentialBackOff());
        try {
            tryUploadAndShowSelectableNotification(this.fileUri, new YouTube.Builder(this.transport, this.jsonFactory, this.credential).setApplicationName(getResources().getString(C0793R.string.app_name)).build());
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }
    }

    private void tryUploadAndShowSelectableNotification(Uri uri, YouTube youTube) throws InterruptedException {
        int i;
        int i2;
        while (true) {
            String tryUpload = tryUpload(uri, youTube);
            if (tryUpload != null) {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String prefResolution = PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING);
                String str = "x";
                if (prefResolution.contains(str)) {
                    i = Integer.parseInt(prefResolution.split(str)[0]);
                } else {
                    i = Integer.parseInt(prefResolution);
                }
                int i3 = 640;
                if (i == 426) {
                    i2 = 240;
                    i3 = 426;
                } else if (i == 640) {
                    i2 = 360;
                } else if (i == 854) {
                    i2 = 480;
                    i3 = 854;
                } else if (i == 1024) {
                    i2 = SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT;
                    i3 = 1024;
                } else if (i == 1280) {
                    i2 = 720;
                    i3 = 1280;
                } else if (i == 1920) {
                    i2 = 1080;
                    i3 = 1920;
                } else if (i == 2048) {
                    i2 = 1536;
                    i3 = 2048;
                } else if (i == 2220) {
                    i2 = 1080;
                    i3 = 2220;
                } else if (i == 2560) {
                    i2 = FloatingService.EXTRA_SHOW_GAME_LIST_ACTIVITY;
                    i3 = 2560;
                } else if (i != 2960) {
                    i2 = 480;
                } else {
                    i2 = FloatingService.EXTRA_SHOW_GAME_LIST_ACTIVITY;
                    i3 = 2960;
                }
                if (!isNetworkConnected(this)) {
                    Toast.makeText(this, C0793R.string.no_internet_connection, 1).show();
                } else {
                    ServerAPI instance = ServerAPI.getInstance();
                    String string = sharedPreferences.getString(ServerAPI.ANONYMOUS_ID, "");
                    String str2 = this.chosenAccountName;
                    String string2 = sharedPreferences.getString(ServerAPI.USER_ID, "");
                    String string3 = defaultSharedPreferences.getString("example_list_bit_rate", String.valueOf(1000000));
                    String string4 = defaultSharedPreferences.getString("example_list_frame_rate", String.valueOf(30));
                    StringBuilder sb = new StringBuilder();
                    sb.append(i3);
                    sb.append("*");
                    sb.append(i2);
                    instance.uploadVideo(string, str2, string2, tryUpload, string3, string4, sb.toString(), this.duration, this.name).subscribe(new Consumer<UploadOutput>() {
                        public void accept(UploadOutput uploadOutput) throws Exception {
                        }
                    }, new Consumer<Throwable>() {
                        public void accept(Throwable th) throws Exception {
                            th.printStackTrace();
                        }
                    });
                }
                AppUtils.addCount(this, 3);
                tryShowSelectableNotification(tryUpload, youTube);
                return;
            }
            YouTube youTube2 = youTube;
            try {
                AppUtils.uploadFile(this, false);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            String str3 = "UploadingActivity";
            Log.e(str3, String.format("Failed to upload %s", new Object[]{uri.toString()}));
            int i4 = this.mUploadAttemptCount;
            this.mUploadAttemptCount = i4 + 1;
            if (i4 < 0) {
                Log.i(str3, String.format("Will retry to upload the video ([%d] out of [%d] reattempts)", new Object[]{Integer.valueOf(this.mUploadAttemptCount), Integer.valueOf(0)}));
                zzz(6000);
            } else {
                Log.e(str3, String.format("Giving up on trying to upload %s after %d attempts", new Object[]{uri.toString(), Integer.valueOf(this.mUploadAttemptCount)}));
                return;
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    private void tryShowSelectableNotification(String str, YouTube youTube) throws InterruptedException {
        this.mStartTime = System.currentTimeMillis();
        showProcessingNotification(this);
        boolean z = false;
        while (true) {
            if (z) {
                break;
            }
            z = ResumableUpload.checkIfProcessed(str, youTube);
            if (z) {
                ResumableUpload.showSelectableNotification(str, getApplicationContext());
                break;
            }
            String str2 = "UploadingActivity";
            Log.d(str2, String.format("Video [%s] is not processed yet, will retry after [%d] seconds", new Object[]{str, Integer.valueOf(6)}));
            if (!timeoutExpired(this.mStartTime, 1200)) {
                zzz(6000);
            } else {
                Log.e(str2, String.format("Bailing out polling for processing status after [%d] seconds", new Object[]{Integer.valueOf(1200)}));
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x006b A[SYNTHETIC, Splitter:B:20:0x006b] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0072 A[SYNTHETIC, Splitter:B:26:0x0072] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String tryUpload(Uri r17, com.google.api.services.youtube.YouTube r18) {
        /*
            r16 = this;
            r13 = r16
            r0 = r17
            r14 = 0
            android.content.ContentResolver r1 = r16.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0058 }
            java.lang.String r2 = "r"
            android.os.ParcelFileDescriptor r1 = r1.openFileDescriptor(r0, r2)     // Catch:{ FileNotFoundException -> 0x0058 }
            long r3 = r1.getStatSize()     // Catch:{ FileNotFoundException -> 0x0058 }
            android.content.ContentResolver r1 = r16.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0058 }
            java.io.InputStream r15 = r1.openInputStream(r0)     // Catch:{ FileNotFoundException -> 0x0058 }
            android.content.Context r5 = r16.getApplicationContext()     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            java.lang.String r7 = r13.name     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            java.lang.String r8 = r13.chosenAccountName     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            long r9 = r13.duration     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            java.lang.String r12 = r13.desc     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            r1 = r18
            r2 = r15
            r6 = r16
            r11 = r17
            java.lang.String r14 = com.ezscreenrecorder.youtubeupload.ResumableUpload.upload(r1, r2, r3, r5, r6, r7, r8, r9, r11, r12)     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            if (r14 == 0) goto L_0x0049
            com.ezscreenrecorder.server.ServerAPI r0 = com.ezscreenrecorder.server.ServerAPI.getInstance()     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            java.lang.String r1 = "Upload to YouTube Done"
            io.reactivex.Single r0 = r0.addToFireBase(r13, r1)     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            com.ezscreenrecorder.youtubeupload.UploadService$3 r1 = new com.ezscreenrecorder.youtubeupload.UploadService$3     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            r1.<init>()     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            r0.subscribe(r1)     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
            r13.removeNotification(r13)     // Catch:{ FileNotFoundException -> 0x0051, all -> 0x004f }
        L_0x0049:
            if (r15 == 0) goto L_0x006f
            r15.close()     // Catch:{ IOException -> 0x006f }
            goto L_0x006f
        L_0x004f:
            r0 = move-exception
            goto L_0x0070
        L_0x0051:
            r0 = move-exception
            r1 = r14
            r14 = r15
            goto L_0x005a
        L_0x0055:
            r0 = move-exception
            r15 = r14
            goto L_0x0070
        L_0x0058:
            r0 = move-exception
            r1 = r14
        L_0x005a:
            android.content.Context r2 = r16.getApplicationContext()     // Catch:{ all -> 0x0055 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0055 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0055 }
            android.util.Log.e(r2, r0)     // Catch:{ all -> 0x0055 }
            if (r14 == 0) goto L_0x006e
            r14.close()     // Catch:{ IOException -> 0x006e }
        L_0x006e:
            r14 = r1
        L_0x006f:
            return r14
        L_0x0070:
            if (r15 == 0) goto L_0x0075
            r15.close()     // Catch:{ IOException -> 0x0075 }
        L_0x0075:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.youtubeupload.UploadService.tryUpload(android.net.Uri, com.google.api.services.youtube.YouTube):java.lang.String");
    }

    public void onDestroy() {
        try {
            AppUtils.uploadFile(this, false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }

    public void requestIntent(Intent intent) {
        this.authIntent = intent;
        Intent intent2 = new Intent(this, CheckYouTubeActivity.class);
        intent2.addFlags(268468224);
        intent2.putExtra("openFile", intent);
        intent2.putExtra(ACCOUNT_KEY, this.chosenAccountName);
        intent2.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, this.duration);
        intent2.putExtra("name", this.name);
        intent2.putExtra("desc", this.desc);
        intent2.setData(this.fileUri);
        startActivity(intent2);
        stopSelf();
    }
}
