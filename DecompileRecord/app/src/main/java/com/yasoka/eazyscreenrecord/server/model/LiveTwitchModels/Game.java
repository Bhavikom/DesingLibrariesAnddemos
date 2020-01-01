package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("box")
    @Expose
    private Box box;
    @SerializedName("giantbomb_id")
    @Expose
    private Integer giantbombId;
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    private Integer f116id;
    @SerializedName("logo")
    @Expose
    private Logo logo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;

    public Integer getId() {
        return this.f116id;
    }

    public void setId(Integer num) {
        this.f116id = num;
    }

    public Box getBox() {
        return this.box;
    }

    public void setBox(Box box2) {
        this.box = box2;
    }

    public Integer getGiantbombId() {
        return this.giantbombId;
    }

    public void setGiantbombId(Integer num) {
        this.giantbombId = num;
    }

    public Logo getLogo() {
        return this.logo;
    }

    public void setLogo(Logo logo2) {
        this.logo = logo2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Integer getPopularity() {
        return this.popularity;
    }

    public void setPopularity(Integer num) {
        this.popularity = num;
    }
}
