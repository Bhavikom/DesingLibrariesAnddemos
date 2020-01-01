package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LiveTwitchChannelOutputModel implements Serializable {
    private static final long serialVersionUID = 1636680693858591590L;
    @SerializedName("broadcaster_language")
    @Expose
    private String broadcasterLanguage;
    @SerializedName("broadcaster_type")
    @Expose
    private String broadcasterType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    private String f117id;
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
    @SerializedName("stream_key")
    @Expose
    private String streamKey;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("video_banner")
    @Expose
    private String videoBanner;
    @SerializedName("views")
    @Expose
    private Integer views;

    public Boolean getMature() {
        return this.mature;
    }

    public void setMature(Boolean bool) {
        this.mature = bool;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getBroadcasterLanguage() {
        return this.broadcasterLanguage;
    }

    public void setBroadcasterLanguage(String str) {
        this.broadcasterLanguage = str;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
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

    public String getId() {
        return this.f117id;
    }

    public void setId(String str) {
        this.f117id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String str) {
        this.createdAt = str;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String str) {
        this.updatedAt = str;
    }

    public Boolean getPartner() {
        return this.partner;
    }

    public void setPartner(Boolean bool) {
        this.partner = bool;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public String getVideoBanner() {
        return this.videoBanner;
    }

    public void setVideoBanner(String str) {
        this.videoBanner = str;
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public Integer getViews() {
        return this.views;
    }

    public void setViews(Integer num) {
        this.views = num;
    }

    public Integer getFollowers() {
        return this.followers;
    }

    public void setFollowers(Integer num) {
        this.followers = num;
    }

    public String getBroadcasterType() {
        return this.broadcasterType;
    }

    public void setBroadcasterType(String str) {
        this.broadcasterType = str;
    }

    public String getStreamKey() {
        return this.streamKey;
    }

    public void setStreamKey(String str) {
        this.streamKey = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }
}
