package com.yasoka.eazyscreenrecord.model;

public class MetadataInfoModel {
    private String durationInSec;
    private String sizeInMb;

    public String getDurationInSec() {
        return this.durationInSec;
    }

    public void setDurationInSec(String str) {
        this.durationInSec = str;
    }

    public String getSizeInMb() {
        return this.sizeInMb;
    }

    public void setSizeInMb(String str) {
        this.sizeInMb = str;
    }
}
