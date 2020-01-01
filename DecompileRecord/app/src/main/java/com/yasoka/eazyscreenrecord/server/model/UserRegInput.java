package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegInput {
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
    @SerializedName("instance_id")
    @Expose
    private String instanceId;
    @SerializedName("user_city")
    @Expose
    private String userCity;
    @SerializedName("user_country")
    @Expose
    private String userCountry;
    @SerializedName("user_dob")
    @Expose
    private String userDob;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_gcm")
    @Expose
    private String userGcm;
    @SerializedName("user_gender")
    @Expose
    private String userGender;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_pincode")
    @Expose
    private String userPincode;
    @SerializedName("user_state")
    @Expose
    private String userState;

    public UserRegInput(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18) {
        this.userGcm = str;
        this.devCc = str2;
        this.devMake = str3;
        this.devModel = str4;
        this.appVer = str5;
        this.appUsageCounter = str6;
        this.androidVer = str7;
        this.instanceId = str8;
        this.devLang = str9;
        this.userEmail = str10;
        this.userCity = str11;
        this.userState = str12;
        this.userCountry = str13;
        this.aId = str14;
        this.userName = str15;
        this.userDob = str16;
        this.userGender = str17;
        this.userPincode = str18;
    }

    public UserRegInput() {
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

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String str) {
        this.userEmail = str;
    }

    public String getUserCity() {
        return this.userCity;
    }

    public void setUserCity(String str) {
        this.userCity = str;
    }

    public String getUserState() {
        return this.userState;
    }

    public void setUserState(String str) {
        this.userState = str;
    }

    public String getUserCountry() {
        return this.userCountry;
    }

    public void setUserCountry(String str) {
        this.userCountry = str;
    }

    public String getAId() {
        return this.aId;
    }

    public void setAId(String str) {
        this.aId = str;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public String getUserDob() {
        return this.userDob;
    }

    public void setUserDob(String str) {
        this.userDob = str;
    }

    public String getUserGender() {
        return this.userGender;
    }

    public void setUserGender(String str) {
        this.userGender = str;
    }

    public String getUserPincode() {
        return this.userPincode;
    }

    public void setUserPincode(String str) {
        this.userPincode = str;
    }
}
