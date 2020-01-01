package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LiveTwitchUserOutputModel implements Serializable {
    private static final long serialVersionUID = 8072722745125030977L;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("_id")
    @Expose

    /* renamed from: id */
    private Integer f119id;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("partnered")
    @Expose
    private Boolean partnered;
    @SerializedName("twitter_connected")
    @Expose
    private Boolean twitterConnected;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return this.f119id;
    }

    public void setId(Integer num) {
        this.f119id = num;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String str) {
        this.bio = str;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public Boolean getEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(Boolean bool) {
        this.emailVerified = bool;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Boolean getPartnered() {
        return this.partnered;
    }

    public void setPartnered(Boolean bool) {
        this.partnered = bool;
    }

    public Boolean getTwitterConnected() {
        return this.twitterConnected;
    }

    public void setTwitterConnected(Boolean bool) {
        this.twitterConnected = bool;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String str) {
        this.updatedAt = str;
    }
}
