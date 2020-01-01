package com.yasoka.eazyscreenrecord.server.model.VideoMainScreenModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private static final long serialVersionUID = -127469847103633844L;
    @SerializedName("editor_choice")
    @Expose
    private List<VideosData> editorChoice = null;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("other_video")
    @Expose
    private List<VideosData> otherVideo = null;
    @SerializedName("top_video")
    @Expose
    private List<VideosData> topVideo = null;
    @SerializedName("user_video")
    @Expose
    private List<VideosData> userVideo = null;

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(Integer num) {
        this.errorCode = num;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String str) {
        this.errorMessage = str;
    }

    public List<VideosData> getTopVideo() {
        return this.topVideo;
    }

    public void setTopVideo(List<VideosData> list) {
        this.topVideo = list;
    }

    public List<VideosData> getEditorChoice() {
        return this.editorChoice;
    }

    public void setEditorChoice(List<VideosData> list) {
        this.editorChoice = list;
    }

    public List<VideosData> getUserVideo() {
        return this.userVideo;
    }

    public void setUserVideo(List<VideosData> list) {
        this.userVideo = list;
    }

    public List<VideosData> getOtherVideo() {
        return this.otherVideo;
    }

    public void setOtherVideo(List<VideosData> list) {
        this.otherVideo = list;
    }
}
