package com.yasoka.eazyscreenrecord.activities.live_youtube;

import com.google.api.services.youtube.model.LiveBroadcast;

public class EventData {
    private LiveBroadcast mEvent;
    private String mIngestionAddress;

    public LiveBroadcast getEvent() {
        return this.mEvent;
    }

    public void setEvent(LiveBroadcast liveBroadcast) {
        this.mEvent = liveBroadcast;
    }

    public String getId() {
        return this.mEvent.getId();
    }

    public String getTitle() {
        return this.mEvent.getSnippet().getTitle();
    }

    public String getThumbUri() {
        String url = this.mEvent.getSnippet().getThumbnails().getDefault().getUrl();
        if (!url.startsWith("//")) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https:");
        sb.append(url);
        return sb.toString();
    }

    public String getIngestionAddress() {
        return this.mIngestionAddress;
    }

    public void setIngestionAddress(String str) {
        this.mIngestionAddress = str;
    }

    public String getWatchUri() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://www.youtube.com/watch?v=");
        sb.append(getId());
        return sb.toString();
    }
}
