package com.yasoka.eazyscreenrecord.utils;

import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
/*import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.media.session.PlaybackStateCompat;
import com.ezscreenrecorder.RecorderApplication;*/
import com.yasoka.eazyscreenrecord.RecorderApplication;

import java.io.File;
import java.io.PrintStream;

public class StorageHelper {
    private static final StorageHelper ourInstance = new StorageHelper();

    public static StorageHelper getInstance() {
        return ourInstance;
    }

    private StorageHelper() {
    }

    public boolean externalMemoryAvailable() {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(RecorderApplication.getInstance().getApplicationContext(), null);
        return (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) ? false : true;
    }

    public String getFileBasePath() {
        if (PreferenceHelper.getInstance().getPrefDefaultStorageLocation() == 0) {
            return getInternalBasePath();
        }
        return getExternalBasePath();
    }

    public String getInternalBasePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getExternalBasePath() {
        File externalFilesDir = RecorderApplication.getInstance().getExternalFilesDir(null);
        File[] externalFilesDirs = RecorderApplication.getInstance().getExternalFilesDirs(null);
        int length = externalFilesDirs.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            File file = externalFilesDirs[i];
            if (file != null && VERSION.SDK_INT >= 21 && file != null && Environment.isExternalStorageRemovable(file)) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("AppUtils.getStoragePath2->");
                sb.append(file.getAbsolutePath());
                printStream.println(sb.toString());
                externalFilesDir = file;
                break;
            }
            i++;
        }
        if (externalFilesDir != null) {
            return externalFilesDir.getAbsolutePath();
        }
        throw new NullPointerException("Storage Location not");
    }

    private static String splitPath(String str) {
        return str.split("Android")[0];
    }

    private static String formatSize(long j) {
        String str;
        if (j >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            j /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            if (j >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                j /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
                if (j >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                    j /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
                    str = "GB";
                } else {
                    str = "MB";
                }
            } else {
                str = "KB";
            }
        } else {
            str = null;
        }
        StringBuilder sb = new StringBuilder(Long.toString(j));
        for (int length = sb.length() - 3; length > 0; length -= 3) {
            sb.insert(length, ',');
        }
        if (str != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    public String getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize(statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public String getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize(statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public String getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return "";
        }
        StatFs statFs = new StatFs(new File(splitPath(getExternalBasePath())).getPath());
        return formatSize(statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public String getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return "";
        }
        StatFs statFs = new StatFs(new File(splitPath(getExternalBasePath())).getPath());
        return formatSize(statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }
}
