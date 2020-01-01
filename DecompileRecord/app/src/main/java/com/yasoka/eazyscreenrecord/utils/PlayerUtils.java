package com.yasoka.eazyscreenrecord.utils;

//import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class PlayerUtils {
    private static final PlayerUtils ourInstance = new PlayerUtils();

    public static PlayerUtils getInstance() {
        return ourInstance;
    }

    private PlayerUtils() {
    }

    public String milliSecondsToTimer(long j) {
        String str;
        String str2;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        String str3 = ":";
        String str4 = "";
        if (i > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(str3);
            str = sb.toString();
        } else {
            str = str4;
        }
        if (i3 < 10) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            sb2.append(i3);
            str2 = sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str4);
            sb3.append(i3);
            str2 = sb3.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append(i2);
        sb4.append(str3);
        sb4.append(str2);
        return sb4.toString();
    }

    public int getProgressPercentage(long j, long j2) {
        Double.valueOf(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
        return Double.valueOf((((double) ((long) ((int) (j / 1000)))) / ((double) ((long) ((int) (j2 / 1000))))) * 100.0d).intValue();
    }

    public int progressToTimer(int i, int i2) {
        return ((int) ((((double) i) / 100.0d) * ((double) (i2 / 1000)))) * 1000;
    }
}
