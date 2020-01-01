package com.yasoka.eazyscreenrecord.model;

public class EventBusTypes {
    public static final int EVENT_TYPE_LIVE_TWITCH_GAME_SELECTED = 4509;
    public static final int EVENT_TYPE_LOCAL_RECORDING_LIST_EMPTY = 4506;
    public static final int EVENT_TYPE_LOCAL_RECORDING_REFRESH = 4503;
    public static final int EVENT_TYPE_LOCAL_RECORDING_REFRESH_AFTER_DELETE = 4510;
    public static final int EVENT_TYPE_LOGIN_FAILED = 4502;
    public static final int EVENT_TYPE_LOGIN_SUCCESS = 4501;
    public static final int EVENT_TYPE_MOVE_TO_LOCAL_RECORDING_LIST = 4507;
    public static final int EVENT_TYPE_REMOTE_RECORDING_REFRESH = 4508;
    private int eventType;

    public EventBusTypes(int i) {
        this.eventType = i;
    }

    public int getEventType() {
        return this.eventType;
    }
}
