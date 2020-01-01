package com.yasoka.eazyscreenrecord;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.StorageHelper;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import p009io.fabric.sdk.android.Fabric;
import rx_activity_result2.RxActivityResult;

public class RecorderApplication extends Application {

    /* renamed from: me */
    private static RecorderApplication f93me;

    public void onCreate() {
        super.onCreate();
        f93me = this;
        RxActivityResult.register(this);
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        checkSDCardAvailablity();
        MobileAds.initialize(this, "ca-app-pub-1374032065732962~1949023764");
    }

    private void writeToSDFile(String str, String str2) {
        try {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("<<<< alarm manager generating force close caught write External file system root: ");
            sb.append(externalStorageDirectory);
            printStream.println(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(externalStorageDirectory.getAbsolutePath());
            sb2.append(File.separator);
            sb2.append("TestAppLogs");
            File file = new File(sb2.toString());
            if (!file.exists()) {
                file.mkdir();
            }
            String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date());
            System.out.println(format);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(format);
            sb3.append(".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, sb3.toString()));
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println(str);
            printWriter.println(str2);
            printWriter.flush();
            printWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("<<<< alarm manager generating force close caught file not found :  add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e2) {
            e2.printStackTrace();
            System.out.println("<<<< alarm manager generating force close caught file not found1 :  add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        }
    }

    public static RecorderApplication getInstance() {
        return f93me;
    }

    public static String getCountryCode() {
        String str = "US";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getInstance().getSystemService("phone");
            String upperCase = telephonyManager.getSimCountryIso().toUpperCase();
            if (!TextUtils.isEmpty(upperCase)) {
                return upperCase;
            }
            String upperCase2 = telephonyManager.getNetworkCountryIso().toUpperCase();
            return TextUtils.isEmpty(upperCase2) ? str : upperCase2;
        } catch (Exception unused) {
            return str;
        }
    }

    public static String getDeviceLanguageISO3() {
        try {
            return Locale.getDefault().getISO3Language();
        } catch (Exception unused) {
            return "";
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkSDCardAvailablity() {
        if (PreferenceHelper.getInstance().getPrefDefaultStorageLocation() == 1 && !StorageHelper.getInstance().externalMemoryAvailable()) {
            PreferenceHelper.getInstance().setPrefDefaultStorageLocation(0);
        }
    }
}
