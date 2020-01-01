package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import java.io.Serializable;

public class LiveTwitchFinalModel implements Serializable {
    private LiveTwitchChannelOutputModel channelOutputModel;
    private LiveTwitchUpdateChannelOutputModel updateChannelOutputModel;

    public LiveTwitchFinalModel(LiveTwitchChannelOutputModel liveTwitchChannelOutputModel, LiveTwitchUpdateChannelOutputModel liveTwitchUpdateChannelOutputModel) {
        this.channelOutputModel = liveTwitchChannelOutputModel;
        this.updateChannelOutputModel = liveTwitchUpdateChannelOutputModel;
    }

    public LiveTwitchChannelOutputModel getChannelOutputModel() {
        return this.channelOutputModel;
    }

    public void setChannelOutputModel(LiveTwitchChannelOutputModel liveTwitchChannelOutputModel) {
        this.channelOutputModel = liveTwitchChannelOutputModel;
    }

    public LiveTwitchUpdateChannelOutputModel getUpdateChannelOutputModel() {
        return this.updateChannelOutputModel;
    }

    public void setUpdateChannelOutputModel(LiveTwitchUpdateChannelOutputModel liveTwitchUpdateChannelOutputModel) {
        this.updateChannelOutputModel = liveTwitchUpdateChannelOutputModel;
    }
}
