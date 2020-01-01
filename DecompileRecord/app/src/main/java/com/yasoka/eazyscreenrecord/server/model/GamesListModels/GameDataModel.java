package com.yasoka.eazyscreenrecord.server.model.GamesListModels;

import android.graphics.drawable.Drawable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameDataModel {
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("game_image")
    @Expose
    private String gameImage;
    private Drawable gameImageDrawable;
    @SerializedName("game_name")
    @Expose
    private String gameName;
    private boolean installed = false;
    private boolean localApplication;
    @SerializedName("package_name")
    @Expose
    private String packageName;

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String str) {
        this.gameName = str;
    }

    public String getGameImage() {
        return this.gameImage;
    }

    public void setGameImage(String str) {
        this.gameImage = str;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public boolean isInstalled() {
        return this.installed;
    }

    public void setInstalled(boolean z) {
        this.installed = z;
    }

    public boolean isLocalApplication() {
        return this.localApplication;
    }

    public void setLocalApplication(boolean z) {
        this.localApplication = z;
    }

    public Drawable getGameImageDrawable() {
        return this.gameImageDrawable;
    }

    public void setGameImageDrawable(Drawable drawable) {
        this.gameImageDrawable = drawable;
    }
}
