package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnonymousInput {
    @SerializedName("android_ver")
    @Expose
    private String androidVer;
    @SerializedName("app_usage_counter")
    @Expose
    private String appUsageCounter;
    @SerializedName("app_ver")
    @Expose
    private String appVer;
    @SerializedName("dev_cc")
    @Expose
    private String devCc;
    @SerializedName("dev_lang")
    @Expose
    private String devLang;
    @SerializedName("dev_make")
    @Expose
    private String devMake;
    @SerializedName("dev_model")
    @Expose
    private String devModel;
    @SerializedName("instance_id")
    @Expose
    private String instanceId;
    @SerializedName("user_gcm")
    @Expose
    private String userGcm;

    public AnonymousInput() {
    }

    public AnonymousInput(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.userGcm = str;
        this.devCc = str2;
        this.devMake = str3;
        this.devModel = str4;
        this.appVer = str5;
        this.appUsageCounter = str6;
        this.androidVer = str7;
        this.instanceId = str8;
        this.devLang = str9;
    }

    public String getUserGcm() {
        return this.userGcm;
    }

    public void setUserGcm(String str) {
        this.userGcm = str;
    }

    public String getDevCc() {
        return this.devCc;
    }

    public void setDevCc(String str) {
        this.devCc = str;
    }

    public String getDevMake() {
        return this.devMake;
    }

    public void setDevMake(String str) {
        this.devMake = str;
    }

    public String getDevModel() {
        return this.devModel;
    }

    public void setDevModel(String str) {
        this.devModel = str;
    }

    public String getAppVer() {
        return this.appVer;
    }

    public void setAppVer(String str) {
        this.appVer = str;
    }

    public String getAppUsageCounter() {
        return this.appUsageCounter;
    }

    public void setAppUsageCounter(String str) {
        this.appUsageCounter = str;
    }

    public String getAndroidVer() {
        return this.androidVer;
    }

    public void setAndroidVer(String str) {
        this.androidVer = str;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String str) {
        this.instanceId = str;
    }

    public String getDevLang() {
        return this.devLang;
    }

    public void setDevLang(String str) {
        this.devLang = str;
    }
}
