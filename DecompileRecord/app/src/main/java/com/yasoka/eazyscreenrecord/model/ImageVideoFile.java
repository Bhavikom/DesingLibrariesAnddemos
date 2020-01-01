package com.yasoka.eazyscreenrecord.model;

import android.graphics.Bitmap;

public class ImageVideoFile {
    private Bitmap bitmap;
    private long created;
    private long duration;
    private long fileSize;

    /* renamed from: id */
    private int f113id;
    private boolean isVideo;
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

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public int getId() {
        return this.f113id;
    }

    public void setId(int i) {
        this.f113id = i;
    }
}
