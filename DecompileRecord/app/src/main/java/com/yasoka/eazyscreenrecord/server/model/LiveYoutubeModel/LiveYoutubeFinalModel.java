package com.yasoka.eazyscreenrecord.server.model.LiveYoutubeModel;

import java.io.Serializable;

public class LiveYoutubeFinalModel implements Serializable {
    private String broadcastDescription;
    private String broadcastID;
    private String broadcastTitle;
    private String streamCDNTitle;
    private String streamDescription;
    private String streamID;
    private String streamTitle;

    public String getBroadcastTitle() {
        return this.broadcastTitle;
    }

    public void setBroadcastTitle(String str) {
        this.broadcastTitle = str;
    }

    public String getBroadcastDescription() {
        return this.broadcastDescription;
    }

    public void setBroadcastDescription(String str) {
        this.broadcastDescription = str;
    }

    public String getStreamTitle() {
        return this.streamTitle;
    }

    public void setStreamTitle(String str) {
        this.streamTitle = str;
    }

    public String getStreamDescription() {
        return this.streamDescription;
    }

    public void setStreamDescription(String str) {
        this.streamDescription = str;
    }

    public String getStreamCDNTitle() {
        return this.streamCDNTitle;
    }

    public void setStreamCDNKey(String str) {
        this.streamCDNTitle = str;
    }

    public String getBroadcastID() {
        return this.broadcastID;
    }

    public void setBroadcastID(String str) {
        this.broadcastID = str;
    }

    public String getStreamID() {
        return this.streamID;
    }

    public void setStreamID(String str) {
        this.streamID = str;
    }
}
