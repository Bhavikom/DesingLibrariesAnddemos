package com.yasoka.eazyscreenrecord.server.model.GamesListModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamesMainModel {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("http_response_code")
    @Expose
    private Integer httpResponseCode;
    @SerializedName("http_response_message")
    @Expose
    private String httpResponseMessage;

    public Integer getHttpResponseCode() {
        return this.httpResponseCode;
    }

    public void setHttpResponseCode(Integer num) {
        this.httpResponseCode = num;
    }

    public String getHttpResponseMessage() {
        return this.httpResponseMessage;
    }

    public void setHttpResponseMessage(String str) {
        this.httpResponseMessage = str;
    }

    public Data getData() {
        return this.data;
    }

    public void setData(Data data2) {
        this.data = data2;
    }
}
