package com.yasoka.eazyscreenrecord.model;

public class SharedDataForOtherApp {
    private String appName;
    private String appPackage;
    private String appSupportEmail;
    private String videoPath;

    public SharedDataForOtherApp(String str, String str2, String str3, String str4) {
        this.appPackage = str;
        this.appName = str2;
        this.appSupportEmail = str3;
        this.videoPath = str4;
    }

    public SharedDataForOtherApp(String str, String str2, String str3) {
        this.appPackage = str;
        this.appName = str2;
        this.appSupportEmail = str3;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String str) {
        this.videoPath = str;
    }

    public String getAppPackage() {
        return this.appPackage;
    }

    public void setAppPackage(String str) {
        this.appPackage = str;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getAppSupportEmail() {
        return this.appSupportEmail;
    }

    public void setAppSupportEmail(String str) {
        this.appSupportEmail = str;
    }
}
