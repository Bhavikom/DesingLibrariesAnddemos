package com.yasoka.eazyscreenrecord.youtubeupload;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.p000v4.app.NotificationCompat.Action;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.facebook.appevents.AppEventsConstants;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Videos.List;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.IOException;

public class ResumableUpload {
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    private static final String DEFAULT_KEYWORD = "Ez Screen Recorder 123";
    public static final int MAX_KEYWORD_LENGTH = 30;
    public static int PLAYBACK_NOTIFICATION_ID = 1002;
    public static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
    public static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
    private static final String SUCCEEDED = "succeeded";
    private static final String TAG = "UploadingActivity";
    /* access modifiers changed from: private */
    public static int UPLOAD_NOTIFICATION_ID = 1001;
    public static final String UPLOAD_PLAYLIST = "Ez recorder PlayList";
    private static String VIDEO_FILE_FORMAT = "video/*";
    public static final String YOUTUBE_ID = "PlayYouTubeId";

    /* renamed from: com.ezscreenrecorder.youtubeupload.ResumableUpload$2 */
    static /* synthetic */ class C14902 {

        /* renamed from: $SwitchMap$com$google$api$client$googleapis$media$MediaHttpUploader$UploadState */
        static final /* synthetic */ int[] f122xc191db1b = new int[UploadState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState[] r0 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f122xc191db1b = r0
                int[] r0 = f122xc191db1b     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.INITIATION_STARTED     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f122xc191db1b     // Catch:{ NoSuchFieldError -> 0x001f }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.INITIATION_COMPLETE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f122xc191db1b     // Catch:{ NoSuchFieldError -> 0x002a }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = f122xc191db1b     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_COMPLETE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = f122xc191db1b     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.NOT_STARTED     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.youtubeupload.ResumableUpload.C14902.<clinit>():void");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x011d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x011f, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x012a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x012b, code lost:
        r15 = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x012d, code lost:
        r14 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0142, code lost:
        r0 = new android.content.Intent(r7, com.ezscreenrecorder.activities.MyWebViewActivity.class);
        r0.addFlags(268468224);
        r0.setData(r26);
        r0.putExtra(com.ezscreenrecorder.youtubeupload.UploadService.ACCOUNT_KEY, r23);
        r0.putExtra(com.ezscreenrecorder.video.NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, r24);
        r0.putExtra("name", r8);
        r7.startActivity(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x016b, code lost:
        r12 = r23;
        r4 = r24;
        r6 = r26;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x018a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x018b, code lost:
        r14 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x01a7, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x01a8, code lost:
        r12 = r23;
        r6 = r26;
        r15 = r27;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x012a A[ExcHandler: IOException (e java.io.IOException), Splitter:B:1:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x016b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x018a A[ExcHandler: UserRecoverableAuthIOException (e com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException), Splitter:B:1:0x0075] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String upload(com.google.api.services.youtube.YouTube r16, java.io.InputStream r17, long r18, Context r20, com.ezscreenrecorder.youtubeupload.UploadService r21, String r22, String r23, long r24, Uri r26, String r27) {
        /*
            r7 = r20
            r8 = r22
            java.lang.String r9 = "UploadingActivity"
            java.lang.String r0 = "notification"
            java.lang.Object r0 = r7.getSystemService(r0)
            r10 = r0
            android.app.NotificationManager r10 = (android.app.NotificationManager) r10
            android.support.v4.app.NotificationCompat$Builder r11 = new android.support.v4.app.NotificationCompat$Builder
            java.lang.String r0 = "com.ezscreenrecorder.APP_CHANNEL_ID"
            r11.<init>(r7, r0)
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.ezscreenrecorder.activities.SplashActivity> r1 = com.ezscreenrecorder.activities.SplashActivity.class
            r0.<init>(r7, r1)
            java.lang.String r1 = "android.intent.action.VIEW"
            r0.setAction(r1)
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.ezscreenrecorder.youtubeupload.CancelBroadCastReceiver> r1 = com.ezscreenrecorder.youtubeupload.CancelBroadCastReceiver.class
            r0.<init>(r7, r1)
            r12 = 0
            r1 = 134217728(0x8000000, float:3.85186E-34)
            android.app.PendingIntent r0 = android.app.PendingIntent.getBroadcast(r7, r12, r0, r1)
            android.support.v4.app.NotificationCompat$Action$Builder r1 = new android.support.v4.app.NotificationCompat$Action$Builder
            r2 = 2131689531(0x7f0f003b, float:1.900808E38)
            java.lang.String r2 = r7.getString(r2)
            r3 = 2131230911(0x7f0800bf, float:1.8077888E38)
            r1.<init>(r3, r2, r0)
            android.support.v4.app.NotificationCompat$Action r0 = r1.build()
            r1 = 2131690037(0x7f0f0235, float:1.9009106E38)
            java.lang.String r1 = r7.getString(r1)
            android.support.v4.app.NotificationCompat$Builder r1 = r11.setContentTitle(r1)
            r2 = 2131690038(0x7f0f0236, float:1.9009108E38)
            java.lang.String r2 = r7.getString(r2)
            android.support.v4.app.NotificationCompat$Builder r1 = r1.setContentText(r2)
            r2 = 17301640(0x1080088, float:2.4979636E-38)
            android.support.v4.app.NotificationCompat$Builder r1 = r1.setSmallIcon(r2)
            android.support.v4.app.NotificationCompat$Builder r1 = r1.setAutoCancel(r12)
            r13 = 1
            android.support.v4.app.NotificationCompat$Builder r1 = r1.setOngoing(r13)
            r1.addAction(r0)
            int r0 = UPLOAD_NOTIFICATION_ID
            android.app.Notification r1 = r11.build()
            r10.notify(r0, r1)
            com.google.api.services.youtube.model.Video r0 = new com.google.api.services.youtube.model.Video     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x01a7, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r0.<init>()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            com.google.api.services.youtube.model.VideoStatus r1 = new com.google.api.services.youtube.model.VideoStatus     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r1.<init>()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            java.lang.String r2 = "public"
            r1.setPrivacyStatus(r2)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r0.setStatus(r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r1 = 2131689515(0x7f0f002b, float:1.9008048E38)
            java.lang.String r1 = r7.getString(r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r0.setEtag(r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            com.google.api.services.youtube.model.VideoSnippet r1 = new com.google.api.services.youtube.model.VideoSnippet     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r1.<init>()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r1.setTitle(r8)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0121, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x012a }
            r15 = r27
            r1.setDescription(r15)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r3 = "Ez Screen Recorder 123"
            r2[r12] = r3     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r3 = "Ez recorder PlayList"
            java.lang.String r3 = generateKeywordFromPlaylistId(r3)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r2[r13] = r3     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.util.List r2 = java.util.Arrays.asList(r2)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r1.setTags(r2)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r0.setSnippet(r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            com.google.api.client.http.InputStreamContent r1 = new com.google.api.client.http.InputStreamContent     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r2 = VIDEO_FILE_FORMAT     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r4 = r17
            r3.<init>(r4)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r1.<init>(r2, r3)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r4 = r18
            r1.setLength(r4)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            com.google.api.services.youtube.YouTube$Videos r2 = r16.videos()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r3 = "snippet,statistics,status"
            com.google.api.services.youtube.YouTube$Videos$Insert r0 = r2.insert(r3, r0, r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            com.google.api.client.googleapis.media.MediaHttpUploader r6 = r0.getMediaHttpUploader()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r1 = 524288(0x80000, float:7.34684E-40)
            r6.setChunkSize(r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r6.setDirectUploadEnabled(r12)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            com.ezscreenrecorder.youtubeupload.ResumableUpload$1 r3 = new com.ezscreenrecorder.youtubeupload.ResumableUpload$1     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r1 = r3
            r2 = r11
            r14 = r3
            r3 = r20
            r4 = r18
            r12 = r6
            r6 = r10
            r1.<init>(r2, r3, r4, r6)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            r12.setProgressListener(r14)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.Object r0 = r0.execute()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            com.google.api.services.youtube.model.Video r0 = (com.google.api.services.youtube.model.Video) r0     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r1 = "Video upload completed"
            android.util.Log.d(r9, r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r14 = r0.getId()     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x011f, UserRecoverableAuthIOException -> 0x018a, IOException -> 0x011d }
            java.lang.String r0 = "videoId = [%s]"
            java.lang.Object[] r1 = new java.lang.Object[r13]     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0116, UserRecoverableAuthIOException -> 0x0113, IOException -> 0x0111 }
            r2 = 0
            r1[r2] = r14     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0116, UserRecoverableAuthIOException -> 0x0113, IOException -> 0x0111 }
            java.lang.String r0 = java.lang.String.format(r0, r1)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0116, UserRecoverableAuthIOException -> 0x0113, IOException -> 0x0111 }
            android.util.Log.d(r9, r0)     // Catch:{ GooglePlayServicesAvailabilityIOException -> 0x0116, UserRecoverableAuthIOException -> 0x0113, IOException -> 0x0111 }
            goto L_0x01cf
        L_0x0111:
            r0 = move-exception
            goto L_0x012e
        L_0x0113:
            r0 = move-exception
            goto L_0x018c
        L_0x0116:
            r0 = move-exception
            r12 = r23
            r6 = r26
            goto L_0x01af
        L_0x011d:
            r0 = move-exception
            goto L_0x012d
        L_0x011f:
            r0 = move-exception
            goto L_0x0124
        L_0x0121:
            r0 = move-exception
            r15 = r27
        L_0x0124:
            r12 = r23
            r6 = r26
            goto L_0x01ae
        L_0x012a:
            r0 = move-exception
            r15 = r27
        L_0x012d:
            r14 = 0
        L_0x012e:
            java.lang.String r1 = "IOException"
            android.util.Log.e(r9, r1, r0)
            r0.printStackTrace()
            java.lang.String r0 = r0.getMessage()
            java.lang.String r1 = "\"code\": 401"
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L_0x016b
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.ezscreenrecorder.activities.MyWebViewActivity> r1 = com.ezscreenrecorder.activities.MyWebViewActivity.class
            r0.<init>(r7, r1)
            r1 = 268468224(0x10008000, float:2.5342157E-29)
            r0.addFlags(r1)
            r6 = r26
            r0.setData(r6)
            java.lang.String r1 = "accountName"
            r12 = r23
            r0.putExtra(r1, r12)
            java.lang.String r1 = "duration"
            r4 = r24
            r0.putExtra(r1, r4)
            java.lang.String r1 = "name"
            r0.putExtra(r1, r8)
            r7.startActivity(r0)
            goto L_0x0171
        L_0x016b:
            r12 = r23
            r4 = r24
            r6 = r26
        L_0x0171:
            r0 = 2131689889(0x7f0f01a1, float:1.9008806E38)
            java.lang.String r2 = r7.getString(r0)
            r1 = r20
            r3 = r10
            r4 = r11
            r5 = r22
            r6 = r26
            r7 = r23
            r8 = r24
            r10 = r27
            notifyFailedUpload(r1, r2, r3, r4, r5, r6, r7, r8, r10)
            goto L_0x01cf
        L_0x018a:
            r0 = move-exception
            r14 = 0
        L_0x018c:
            java.lang.Object[] r1 = new java.lang.Object[r13]
            java.lang.String r2 = r0.getMessage()
            r3 = 0
            r1[r3] = r2
            java.lang.String r2 = "UserRecoverableAuthIOException: %s"
            java.lang.String r1 = java.lang.String.format(r2, r1)
            android.util.Log.i(r9, r1)
            r1 = r21
            requestAuth(r7, r0, r1, r10)
            r0.printStackTrace()
            goto L_0x01cf
        L_0x01a7:
            r0 = move-exception
            r12 = r23
            r6 = r26
            r15 = r27
        L_0x01ae:
            r14 = 0
        L_0x01af:
            java.lang.String r1 = "GooglePlayServicesAvailabilityIOException"
            android.util.Log.e(r9, r1, r0)
            r0.printStackTrace()
            r0 = 2131689532(0x7f0f003c, float:1.9008082E38)
            java.lang.String r2 = r7.getString(r0)
            r1 = r20
            r3 = r10
            r4 = r11
            r5 = r22
            r6 = r26
            r7 = r23
            r8 = r24
            r10 = r27
            notifyFailedUpload(r1, r2, r3, r4, r5, r6, r7, r8, r10)
        L_0x01cf:
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.youtubeupload.ResumableUpload.upload(com.google.api.services.youtube.YouTube, java.io.InputStream, long, android.content.Context, com.ezscreenrecorder.youtubeupload.UploadService, java.lang.String, java.lang.String, long, android.net.Uri, java.lang.String):java.lang.String");
    }

    private static void requestAuth(Context context, UserRecoverableAuthIOException userRecoverableAuthIOException, UploadService uploadService, NotificationManager notificationManager) {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        Intent intent = userRecoverableAuthIOException.getIntent();
        String str = REQUEST_AUTHORIZATION_INTENT;
        Intent intent2 = new Intent(str);
        intent2.putExtra(REQUEST_AUTHORIZATION_INTENT_PARAM, intent);
        instance.sendBroadcast(intent2);
        uploadService.requestIntent(intent);
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
        try {
            AppUtils.uploadFile(context, false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Log.d(TAG, String.format("Sent broadcast %s", new Object[]{str}));
    }

    @SuppressLint({"RestrictedApi"})
    private static void notifyFailedUpload(Context context, String str, NotificationManager notificationManager, Builder builder, String str2, Uri uri, String str3, long j, String str4) {
        Intent intent = new Intent(context, StartYouTubeUploadReceiver.class);
        intent.setData(uri);
        intent.putExtra(UploadService.ACCOUNT_KEY, str3);
        intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, j);
        intent.putExtra("name", str2);
        intent.putExtra("desc", str4);
        Action build = new Action.Builder(C0793R.C0794drawable.ic_retry, context.getString(C0793R.string.retry), PendingIntent.getBroadcast(context, 0, intent, 134217728)).build();
        builder.mActions.clear();
        builder.setContentTitle(context.getString(C0793R.string.yt_upload_failed)).setContentText(str).setOngoing(false).addAction(build);
        notificationManager.notify(UPLOAD_NOTIFICATION_ID, builder.build());
        Log.e(ResumableUpload.class.getSimpleName(), str);
        try {
            AppUtils.uploadFile(context, false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void showSelectableNotification(String str, Context context) {
        String format = String.format("Posting selectable notification for video ID [%s]", new Object[]{str});
        String str2 = TAG;
        Log.d(str2, format);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        String str3 = CHANNEL_ID;
        Builder builder = new Builder(context, str3);
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(YOUTUBE_ID, str);
        intent.putExtra(GalleryActivity.IS_MY_VIDEOS, AppEventsConstants.EVENT_PARAM_VALUE_YES);
        intent.putExtra(GalleryActivity.YOU_TUBE_LIST, true);
        intent.putExtra(GalleryActivity.YOU_TUBE_LIST_UPLOAD, true);
        intent.setAction("android.intent.action.VIEW");
        builder.setContentTitle(context.getString(C0793R.string.youtube_upload)).setContentText(context.getString(C0793R.string.see_the_newly_uploaded_video)).setContentIntent(PendingIntent.getActivity(context, 0, intent, 268435456)).setSmallIcon(C0793R.mipmap.ic_launcher5);
        try {
            if (VERSION.SDK_INT >= 26 && notificationManager.getNotificationChannel(str3) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(str3, context.getString(C0793R.string.notification_channel), 2);
                notificationChannel.setDescription(context.getString(C0793R.string.notification_channel_description));
                notificationChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(PLAYBACK_NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        Log.d(str2, String.format("Selectable notification for video ID [%s] posted", new Object[]{str}));
    }

    public static boolean checkIfProcessed(String str, YouTube youTube) {
        String str2 = TAG;
        try {
            List list = youTube.videos().list("processingDetails");
            list.setId(str);
            java.util.List items = ((VideoListResponse) list.execute()).getItems();
            if (items.size() == 1) {
                String processingStatus = ((Video) items.get(0)).getProcessingDetails().getProcessingStatus();
                Log.e(str2, String.format("Processing status of [%s] is [%s]", new Object[]{str, processingStatus}));
                if (processingStatus.equals("succeeded")) {
                    return true;
                }
                return false;
            }
            Log.e(str2, String.format("Can't find video with ID [%s]", new Object[]{str}));
            return false;
        } catch (IOException e) {
            Log.e(str2, "Error fetching video metadata", e);
        }
    }

    private static String generateKeywordFromPlaylistId(String str) {
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        if (str.indexOf("PL") == 0) {
            str = str.substring(2);
        }
        String concat = DEFAULT_KEYWORD.concat(str.replaceAll("\\W", str2));
        return concat.length() > 30 ? concat.substring(0, 30) : concat;
    }
}
