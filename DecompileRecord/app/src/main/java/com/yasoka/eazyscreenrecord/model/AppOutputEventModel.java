package com.yasoka.eazyscreenrecord.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppOutputEventModel {
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("result")
    @Expose
    public Boolean result;
}
