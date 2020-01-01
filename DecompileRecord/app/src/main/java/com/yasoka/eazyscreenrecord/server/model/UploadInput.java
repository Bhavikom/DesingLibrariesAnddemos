package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadInput {
    @SerializedName("a_id")
    @Expose
    private String aId;
    @SerializedName("bitrate")
    @Expose
    private String bitrate;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("fps")
    @Expose
    private String fps;
    @SerializedName("mobile_dt_added")
    @Expose
    private String mobileDtTime;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("video_id")
    @Expose
    private String videoId;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String str) {
        this.emailId = str;
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

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String str) {
        this.videoId = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public String getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(String str) {
        this.bitrate = str;
    }

    public String getFps() {
        return this.fps;
    }

    public void setFps(String str) {
        this.fps = str;
    }

    public String getMobileDtTime() {
        return this.mobileDtTime;
    }

    public void setMobileDtTime(String str) {
        this.mobileDtTime = str;
    }
}
