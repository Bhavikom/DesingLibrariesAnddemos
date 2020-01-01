package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnonymousOutput {
    @SerializedName("a_id")
    @Expose
    private Integer aId;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public Integer getAId() {
        return this.aId;
    }

    public void setAId(Integer num) {
        this.aId = num;
    }
}
