package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordingInput {
    @SerializedName("a_id")
    @Expose
    private String aId;
    @SerializedName("app_counter")
    @Expose
    private String appCounter;
    @SerializedName("app_ver")
    @Expose
    private String appVersion;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("recorder_type")
    @Expose
    private String recorderType;
    @SerializedName("recording_package")
    @Expose
    private String recordingPackage;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("u_id")
    @Expose
    private String uId;

    public String getRecorderType() {
        return this.recorderType;
    }

    public void setRecorderType(String str) {
        this.recorderType = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
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

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getRecordingPackage() {
        return this.recordingPackage;
    }

    public void setRecordingPackage(String str) {
        this.recordingPackage = str;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }

    public String getAppCounter() {
        return this.appCounter;
    }

    public void setAppCounter(String str) {
        this.appCounter = str;
    }
}
