package com.yasoka.eazyscreenrecord.model;

import java.io.Serializable;

public class AudioFileModel implements Serializable {
    private long fileCreated;
    private long fileDuration;
    private int fileId;
    private String fileName;
    private String filePath;
    private long fileSize;
    private boolean isPlaying;

    public int getFileId() {
        return this.fileId;
    }

    public void setFileId(int i) {
        this.fileId = i;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
    }

    public long getFileDuration() {
        return this.fileDuration;
    }

    public void setFileDuration(long j) {
        this.fileDuration = j;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public long getFileCreated() {
        return this.fileCreated;
    }

    public void setFileCreated(long j) {
        this.fileCreated = j;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean z) {
        this.isPlaying = z;
    }
}
