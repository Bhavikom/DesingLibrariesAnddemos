package com.yasoka.eazyscreenrecord.server.model.GamesListModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Data {
    @SerializedName("currentpage")
    @Expose
    private Integer currentpage;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("Data")
    @Expose
    private List<GameDataModel> gameData = null;
    @SerializedName("totaldata")
    @Expose
    private Integer totaldata;
    @SerializedName("totalpages")
    @Expose
    private Integer totalpages;

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

    public Integer getCurrentpage() {
        return this.currentpage;
    }

    public void setCurrentpage(Integer num) {
        this.currentpage = num;
    }

    public List<GameDataModel> getGameList() {
        return this.gameData;
    }

    public void setGameList(List<GameDataModel> list) {
        this.gameData = list;
    }
}
