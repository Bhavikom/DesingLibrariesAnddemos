package com.yasoka.eazyscreenrecord.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class AppUtils {
    public static final String APP_WATERMARK_FILE_NAME = "watermark.png";
    private static final String APP_WATERMARK_FOLDER_NAME = ".SCRWatermark";

    public static String getVideoDir(Context context) throws NameNotFoundException {
        int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        StringBuilder sb = new StringBuilder();
        sb.append(StorageHelper.getInstance().getFileBasePath());
        sb.append(File.separator);
        sb.append("VideoRecorder");
        sb.append(File.separator);
        String sb2 = sb.toString();
        if (i >= 6 && renameToNewFolder()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(StorageHelper.getInstance().getFileBasePath());
            sb3.append(File.separator);
            sb3.append("EZ-VideoRecorder");
            sb3.append(File.separator);
            sb2 = sb3.toString();
        }
        File file = new File(sb2);
        if (!file.exists() && !file.mkdirs()) {
            String str = "failed to create file ic_default_storage_location directory.";
            Log.e("error", str);
            Crashlytics.logException(new Exception(str));
        }
        return sb2;
    }

    public static String getVideoWatermarkDir(Context context) throws NameNotFoundException {
        File file = new File(getVideoDir(context), APP_WATERMARK_FOLDER_NAME);
        if (!file.exists() && !file.mkdirs()) {
            Crashlytics.logException(new Exception("failed to create file Video Watermark directory."));
        }
        return file.getAbsolutePath();
    }

    public static String getVideoDir(Context context, boolean z) throws NameNotFoundException {
        String str;
        String str2 = "EZ-VideoRecorder";
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append(StorageHelper.getInstance().getExternalBasePath());
            sb.append(File.separator);
            sb.append(str2);
            sb.append(File.separator);
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(StorageHelper.getInstance().getInternalBasePath());
            sb2.append(File.separator);
            sb2.append(str2);
            sb2.append(File.separator);
            str = sb2.toString();
        }
        File file = new File(str);
        if (!file.exists() && !file.mkdirs()) {
            String str3 = "failed to create file ic_default_storage_location directory.";
            Log.e("error", str3);
            Crashlytics.logException(new Exception(str3));
        }
        return str;
    }

    public static String getAudioDir() {
        StringBuilder sb = new StringBuilder();
        sb.append(StorageHelper.getInstance().getFileBasePath());
        sb.append(File.separator);
        sb.append("EZ-AudioRecorder");
        sb.append(File.separator);
        String sb2 = sb.toString();
        File file = new File(sb2);
        if (!file.exists() && !file.mkdirs()) {
            String str = "failed to create file ic_default_storage_location directory.";
            Log.e("error", str);
            Crashlytics.logException(new Exception(str));
        }
        return sb2;
    }

    public static String getAudioDir(boolean z) {
        String str;
        String str2 = "EZ-AudioRecorder";
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append(StorageHelper.getInstance().getExternalBasePath());
            sb.append(File.separator);
            sb.append(str2);
            sb.append(File.separator);
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(StorageHelper.getInstance().getInternalBasePath());
            sb2.append(File.separator);
            sb2.append(str2);
            sb2.append(File.separator);
            str = sb2.toString();
        }
        File file = new File(str);
        if (!file.exists() && !file.mkdirs()) {
            String str3 = "failed to create file ic_default_storage_location directory.";
            Log.e("error", str3);
            Crashlytics.logException(new Exception(str3));
        }
        return str;
    }

    public static void uploadFile(Context context, boolean z) throws NameNotFoundException, IOException {
        String videoDir = getVideoDir(context);
        StringBuilder sb = new StringBuilder();
        sb.append(videoDir);
        sb.append(File.separator);
        sb.append("upload.txt");
        File file = new File(sb.toString());
        if (z) {
            file.createNewFile();
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static boolean containUploadFile(Context context) {
        try {
            String videoDir = getVideoDir(context);
            StringBuilder sb = new StringBuilder();
            sb.append(videoDir);
            sb.append(File.separator);
            sb.append("upload.txt");
            return new File(sb.toString()).exists();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean renameToNewFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(StorageHelper.getInstance().getFileBasePath());
        sb.append(File.separator);
        sb.append("VideoRecorder");
        sb.append(File.separator);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(StorageHelper.getInstance().getFileBasePath());
        sb3.append(File.separator);
        sb3.append("EZ-VideoRecorder");
        sb3.append(File.separator);
        String sb4 = sb3.toString();
        File file = new File(sb2);
        if (!file.exists()) {
            return true;
        }
        File file2 = new File(sb4);
        if (file2.exists()) {
            return true;
        }
        return file.renameTo(file2);
    }

    public static String getImageDir(Context context) throws NameNotFoundException {
        int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        StringBuilder sb = new StringBuilder();
        sb.append(StorageHelper.getInstance().getFileBasePath());
        sb.append(File.separator);
        sb.append("VideoRecorder");
        sb.append(File.separator);
        String str = "ScreenShots";
        sb.append(str);
        sb.append(File.separator);
        String sb2 = sb.toString();
        if (i >= 6 && renameToNewFolder()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(StorageHelper.getInstance().getFileBasePath());
            sb3.append(File.separator);
            sb3.append("EZ-VideoRecorder");
            sb3.append(File.separator);
            sb3.append(str);
            sb3.append(File.separator);
            sb2 = sb3.toString();
        }
        File file = new File(sb2);
        if (file.exists() || file.mkdirs()) {
            return sb2;
        }
        return null;
    }

    public static String getImageDir(Context context, boolean z) throws NameNotFoundException {
        String str;
        String str2 = "ScreenShots";
        String str3 = "EZ-VideoRecorder";
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append(StorageHelper.getInstance().getExternalBasePath());
            sb.append(File.separator);
            sb.append(str3);
            sb.append(File.separator);
            sb.append(str2);
            sb.append(File.separator);
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(StorageHelper.getInstance().getInternalBasePath());
            sb2.append(File.separator);
            sb2.append(str3);
            sb2.append(File.separator);
            sb2.append(str2);
            sb2.append(File.separator);
            str = sb2.toString();
        }
        File file = new File(str);
        if (file.exists() || file.mkdirs()) {
            return str;
        }
        Log.e("error", "failed to create file ic_default_storage_location directory.");
        return null;
    }

    public static String getTrimDir(Context context) throws NameNotFoundException {
        int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        StringBuilder sb = new StringBuilder();
        sb.append(StorageHelper.getInstance().getFileBasePath());
        sb.append("/VideoRecorder/Trim Videos");
        String sb2 = sb.toString();
        if (i >= 6 && renameToNewFolder()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(StorageHelper.getInstance().getFileBasePath());
            sb3.append("/EZ-VideoRecorder/Trim Videos");
            sb2 = sb3.toString();
        }
        File file = new File(sb2);
        if (file.exists() || file.mkdirs()) {
            return sb2;
        }
        Log.e("error", "failed to create file ic_default_storage_location directory.");
        return null;
    }

    public static void addCount(Context context, int i) {
        String str;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        switch (i) {
            case 0:
                str = "ScreenShotCount";
                break;
            case 1:
                str = "VideoRecordCount";
                break;
            case 2:
                str = "IVideoRecordCount";
                break;
            case 3:
                str = "YouTubeUploadCount";
                break;
            case 4:
                str = "ShareImageCount";
                break;
            case 5:
                str = "ShareVideoCount";
                break;
            case 6:
                str = "ShareAudioCount";
                break;
            default:
                str = null;
                break;
        }
        if (str != null) {
            sharedPreferences.edit().putLong(str, sharedPreferences.getLong(str, 0) + 1).apply();
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" -- COUNT>>");
            sb.append(sharedPreferences.getLong(str, 0));
            printStream.println(sb.toString());
        }
    }
}
