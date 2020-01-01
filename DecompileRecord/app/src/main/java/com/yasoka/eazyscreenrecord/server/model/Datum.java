package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("a_id")
    @Expose
    private String aId;
    @SerializedName("bitrate")
    @Expose
    private String bitrate;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;
    @SerializedName("dislikeCount")
    @Expose
    private String dislikeCount;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("favoriteCount")
    @Expose
    private String favoriteCount;
    @SerializedName("fps")
    @Expose
    private String fps;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;
    @SerializedName("mobile_dt_added")
    @Expose
    private String mobileDtAdded;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("viewCount")
    @Expose
    private String viewCount;

    public String getViewCount() {
        return this.viewCount;
    }

    public void setViewCount(String str) {
        this.viewCount = str;
    }

    public String getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(String str) {
        this.likeCount = str;
    }

    public String getDislikeCount() {
        return this.dislikeCount;
    }

    public void setDislikeCount(String str) {
        this.dislikeCount = str;
    }

    public String getFavoriteCount() {
        return this.favoriteCount;
    }

    public void setFavoriteCount(String str) {
        this.favoriteCount = str;
    }

    public String getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(String str) {
        this.commentCount = str;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String str) {
        this.user_name = str;
    }

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

    public String getMobileDtAdded() {
        return this.mobileDtAdded;
    }

    public void setMobileDtAdded(String str) {
        this.mobileDtAdded = str;
    }
}
