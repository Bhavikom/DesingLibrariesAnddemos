package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LiveTwitchUpdateChannelInputputModel implements Serializable {
    private static final long serialVersionUID = 2301455313739879287L;
    @SerializedName("channel[channel_feed_enabled]")
    @Expose
    private Boolean channelChannelFeedEnabled;
    @SerializedName("channel[game]")
    @Expose
    private String channelGame;
    @SerializedName("channel[status]")
    @Expose
    private String channelStatus;

    public String getChannelStatus() {
        return this.channelStatus;
    }

    public void setChannelStatus(String str) {
        this.channelStatus = str;
    }

    public String getChannelGame() {
        return this.channelGame;
    }

    public void setChannelGame(String str) {
        this.channelGame = str;
    }

    public Boolean getChannelChannelFeedEnabled() {
        return this.channelChannelFeedEnabled;
    }

    public void setChannelChannelFeedEnabled(Boolean bool) {
        this.channelChannelFeedEnabled = bool;
    }
}
