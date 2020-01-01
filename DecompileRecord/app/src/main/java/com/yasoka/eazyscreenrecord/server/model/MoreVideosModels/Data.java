package com.yasoka.eazyscreenrecord.server.model.MoreVideosModels;

import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideosData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Data {
    @SerializedName("currentpage")
    @Expose
    private String currentpage;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("totaldata")
    @Expose
    private Integer totaldata;
    @SerializedName("totalpages")
    @Expose
    private Integer totalpages;
    @SerializedName("Data")
    @Expose
    private List<VideosData> videosData = null;

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

    public Integer getTotaldata() {
        return this.totaldata;
    }

    public void setTotaldata(Integer num) {
        this.totaldata = num;
    }

    public Integer getTotalpages() {
        return this.totalpages;
    }

    public void setTotalpages(Integer num) {
        this.totalpages = num;
    }

    public String getCurrentpage() {
        return this.currentpage;
    }

    public void setCurrentpage(String str) {
        this.currentpage = str;
    }

    public List<VideosData> getVideosData() {
        return this.videosData;
    }

    public void setVideosData(List<VideosData> list) {
        this.videosData = list;
    }
}
