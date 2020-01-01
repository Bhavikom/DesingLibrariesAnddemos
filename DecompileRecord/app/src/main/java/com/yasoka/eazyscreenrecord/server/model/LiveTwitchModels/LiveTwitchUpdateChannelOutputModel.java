package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LiveTwitchUpdateChannelOutputModel implements Serializable {
    private static final long serialVersionUID = 1337615626390935829L;
    @SerializedName("broadcaster_language")
    @Expose
    private String broadcasterLanguage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    private Integer f118id;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("mature")
    @Expose
    private Boolean mature;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("partner")
    @Expose
    private Boolean partner;
    @SerializedName("profile_banner")
    @Expose
    private Object profileBanner;
    @SerializedName("profile_banner_background_color")
    @Expose
    private Object profileBannerBackgroundColor;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("video_banner")
    @Expose
    private Object videoBanner;
    @SerializedName("views")
    @Expose
    private Integer views;

    public Integer getId() {
        return this.f118id;
    }

    public void setId(Integer num) {
        this.f118id = num;
    }

    public String getBroadcasterLanguage() {
        return this.broadcasterLanguage;
    }

    public void setBroadcasterLanguage(String str) {
        this.broadcasterLanguage = str;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String str) {
        this.createdAt = str;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public Integer getFollowers() {
        return this.followers;
    }

    public void setFollowers(Integer num) {
        this.followers = num;
    }

    public String getGame() {
        return this.game;
    }

    public void setGame(String str) {
        this.game = str;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public Boolean getMature() {
        return this.mature;
    }

    public void setMature(Boolean bool) {
        this.mature = bool;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Boolean getPartner() {
        return this.partner;
    }

    public void setPartner(Boolean bool) {
        this.partner = bool;
    }

    public Object getProfileBanner() {
        return this.profileBanner;
    }

    public void setProfileBanner(Object obj) {
        this.profileBanner = obj;
    }

    public Object getProfileBannerBackgroundColor() {
        return this.profileBannerBackgroundColor;
    }

    public void setProfileBannerBackgroundColor(Object obj) {
        this.profileBannerBackgroundColor = obj;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String str) {
        this.updatedAt = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public Object getVideoBanner() {
        return this.videoBanner;
    }

    public void setVideoBanner(Object obj) {
        this.videoBanner = obj;
    }

    public Integer getViews() {
        return this.views;
    }

    public void setViews(Integer num) {
        this.views = num;
    }
}
