package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Data {
    @SerializedName("currentpage")
    @Expose
    private String currentpage;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("totalpages")
    @Expose
    private int totalpages;

    public int getTotalpages() {
        return this.totalpages;
    }

    public void setTotalpages(int i) {
        this.totalpages = i;
    }

    public String getCurrentpage() {
        return this.currentpage;
    }

    public void setCurrentpage(String str) {
        this.currentpage = str;
    }

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

    public List<Datum> getData() {
        return this.data;
    }

    public void setData(List<Datum> list) {
        this.data = list;
    }
}
