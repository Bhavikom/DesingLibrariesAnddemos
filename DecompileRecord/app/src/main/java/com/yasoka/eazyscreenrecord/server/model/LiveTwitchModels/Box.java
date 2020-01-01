package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Box {
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("template")
    @Expose
    private String template;

    public String getLarge() {
        return this.large;
    }

    public void setLarge(String str) {
        this.large = str;
    }

    public String getMedium() {
        return this.medium;
    }

    public void setMedium(String str) {
        this.medium = str;
    }

    public String getSmall() {
        return this.small;
    }

    public void setSmall(String str) {
        this.small = str;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String str) {
        this.template = str;
    }
}
