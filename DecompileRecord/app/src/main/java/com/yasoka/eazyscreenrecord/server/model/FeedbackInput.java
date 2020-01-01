package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackInput {
    @SerializedName("a_id")
    @Expose
    private String aId;
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
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("mobile_no")
    @Expose
    private String phoneNumber;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("u_id")
    @Expose
    private String uId;

    public String getUId() {
        return this.uId;
    }

    public void setUId(String str) {
        this.uId = str;
    }

    public String getAId() {
        return this.aId;
    }

    public void setAId(String str) {
        this.aId = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
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

    public String getDevLang() {
        return this.devLang;
    }

    public void setDevLang(String str) {
        this.devLang = str;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }
}
