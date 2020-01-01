package com.yasoka.eazyscreenrecord.model;

import android.graphics.Bitmap;

public class VideoFileModel {
    private Bitmap bitmap;
    private long created;
    private long duration;
    private long fileSize;

    /* renamed from: id */
    private int f114id;
    private String name;
    private String path;
    private String resolution;

    public long getCreated() {
        return this.created;
    }

    public void setCreated(long j) {
        this.created = j;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getId() {
        return this.f114id;
    }

    public void setId(int i) {
        this.f114id = i;
    }
}
