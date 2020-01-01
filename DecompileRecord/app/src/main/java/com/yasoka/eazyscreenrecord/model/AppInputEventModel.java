package com.yasoka.eazyscreenrecord.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppInputEventModel {
    @SerializedName("a_id")
    @Expose
    private String aId;
    @SerializedName("app_counter")
    @Expose
    private String appCounter;
    @SerializedName("app_ver")
    @Expose
    private String appVersion;
    @SerializedName("dev_cc")
    @Expose
    private String devCc;
    @SerializedName("dev_lc")
    @Expose
    private String devLc;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("share_type")
    @Expose
    private String shareType;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("youtube_id")
    @Expose
    private String youtubeId;

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getDevLc() {
        return this.devLc;
    }

    public void setDevLc(String str) {
        this.devLc = str;
    }

    public String getDevCc() {
        return this.devCc;
    }

    public void setDevCc(String str) {
        this.devCc = str;
    }

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

    public String getAppCounter() {
        return this.appCounter;
    }

    public void setAppCounter(String str) {
        this.appCounter = str;
    }

    public String getSocialType() {
        return this.socialType;
    }

    public void setSocialType(String str) {
        this.socialType = str;
    }

    public String getYoutubeId() {
        return this.youtubeId;
    }

    public void setYoutubeId(String str) {
        this.youtubeId = str;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String str) {
        this.imageId = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getShareType() {
        return this.shareType;
    }

    public void setShareType(String str) {
        this.shareType = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }
}
