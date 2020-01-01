package com.yasoka.eazyscreenrecord.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class FilesAccessHelper {
    private static FilesAccessHelper fileAccessHelper;
    private boolean isVideo;

    public static FilesAccessHelper getInstance() {
        if (fileAccessHelper == null) {
            fileAccessHelper = new FilesAccessHelper();
        }
        return fileAccessHelper;
    }

    public String getPath(Context context, Uri uri, boolean z) {
        this.isVideo = z;
        Uri uri2 = null;
        if (!(VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if (Param.CONTENT.equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else {
            String str = ":";
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(str);
                if ("primary".equalsIgnoreCase(split[0])) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory());
                    sb.append("/");
                    sb.append(split[1]);
                    return sb.toString();
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(str);
                String str2 = split2[0];
                if ("image".equals(str2)) {
                    uri2 = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str2)) {
                    uri2 = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str2)) {
                    uri2 = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
            }
        }
        return null;
    }

    private boolean isMediaTypeMime(Context context, Uri uri) {
        String type = context.getContentResolver().getType(uri);
        return !TextUtils.isEmpty(type) && (type.startsWith("image") || type.startsWith("video"));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r9 != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        if (r9 != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003b, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getDataColumn(Context r9, Uri r10, String r11, String[] r12) {
        /*
            r8 = this;
            java.lang.String r0 = "_data"
            java.lang.String[] r3 = new java.lang.String[]{r0}
            r7 = 0
            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch:{ Exception -> 0x0031, all -> 0x002e }
            r6 = 0
            r2 = r10
            r4 = r11
            r5 = r12
            android.database.Cursor r9 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0031, all -> 0x002e }
            if (r9 == 0) goto L_0x002b
            boolean r10 = r9.moveToFirst()     // Catch:{ Exception -> 0x0029 }
            if (r10 == 0) goto L_0x002b
            int r10 = r9.getColumnIndexOrThrow(r0)     // Catch:{ Exception -> 0x0029 }
            java.lang.String r10 = r9.getString(r10)     // Catch:{ Exception -> 0x0029 }
            if (r9 == 0) goto L_0x0028
            r9.close()
        L_0x0028:
            return r10
        L_0x0029:
            r10 = move-exception
            goto L_0x0033
        L_0x002b:
            if (r9 == 0) goto L_0x003b
            goto L_0x0038
        L_0x002e:
            r10 = move-exception
            r9 = r7
            goto L_0x003d
        L_0x0031:
            r10 = move-exception
            r9 = r7
        L_0x0033:
            com.crashlytics.android.Crashlytics.logException(r10)     // Catch:{ all -> 0x003c }
            if (r9 == 0) goto L_0x003b
        L_0x0038:
            r9.close()
        L_0x003b:
            return r7
        L_0x003c:
            r10 = move-exception
        L_0x003d:
            if (r9 == 0) goto L_0x0042
            r9.close()
        L_0x0042:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.utils.FilesAccessHelper.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void refreshGallery(Context context, String str) {
        if (VERSION.SDK_INT < 19) {
            StringBuilder sb = new StringBuilder();
            sb.append("file://");
            sb.append(Environment.getExternalStorageDirectory());
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb.toString())));
            return;
        }
        MediaScannerConnection.scanFile(context, new String[]{str}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }
}
