package com.yasoka.eazyscreenrecord.server.model.GameEventOutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameEventOutputModel {
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("result")
    @Expose
    public Boolean result;
}
