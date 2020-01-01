package com.yasoka.eazyscreenrecord.model;

public class ActionMoreModel {
    public static final int EXTRA_ACTION_MORE_TYPE_AUDIO = 7600;
    public static final int EXTRA_ACTION_MORE_TYPE_GAME_RECORD = 7603;
    public static final int EXTRA_ACTION_MORE_TYPE_INTRACTIVE_VIDEO = 7601;
    public static final int EXTRA_ACTION_MORE_TYPE_WHITE_BOARD = 7602;
    public static final String KEY_IS_MORE_FROM_NOTIFICATION = "is_from_noti_option";
    private int actionImageRes;
    private String actionText;
    private int actionTextRes;
    private int actionType;

    public ActionMoreModel(int i, String str, int i2) {
        this.actionImageRes = i;
        this.actionText = str;
        this.actionType = i2;
    }

    public ActionMoreModel(int i, int i2, int i3) {
        this.actionImageRes = i;
        this.actionTextRes = i2;
        this.actionType = i3;
    }

    public int getActionImageRes() {
        return this.actionImageRes;
    }

    public void setActionImageRes(int i) {
        this.actionImageRes = i;
    }

    public String getActionText() {
        return this.actionText;
    }

    public void setActionText(String str) {
        this.actionText = str;
    }

    public int getActionTextRes() {
        return this.actionTextRes;
    }

    public void setActionTextRes(int i) {
        this.actionTextRes = i;
    }

    public int getActionType() {
        return this.actionType;
    }

    public void setActionType(int i) {
        this.actionType = i;
    }
}
