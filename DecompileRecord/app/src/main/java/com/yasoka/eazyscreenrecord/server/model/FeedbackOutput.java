package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackOutput {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private boolean result;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean z) {
        this.result = z;
    }
}
