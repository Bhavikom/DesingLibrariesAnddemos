package com.yasoka.eazyscreenrecord.server.model.GameEventOutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameEventInputModel {
    @SerializedName("cc")
    @Expose

    /* renamed from: cc */
    public String f115cc;
    @SerializedName("package_name")
    @Expose
    public String packageName;

    public GameEventInputModel(String str, String str2) {
        this.packageName = str;
        this.f115cc = str2;
    }
}
