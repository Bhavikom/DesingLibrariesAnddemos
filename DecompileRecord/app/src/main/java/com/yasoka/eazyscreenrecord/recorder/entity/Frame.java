package com.yasoka.eazyscreenrecord.recorder.entity;

public class Frame<T> {
    public static final int FRAME_TYPE_AUDIO = 1;
    public static final int FRAME_TYPE_CONFIGURATION = 4;
    public static final int FRAME_TYPE_INTER_FRAME = 3;
    public static final int FRAME_TYPE_KEY_FRAME = 2;
    public T data;
    public int frameType;
    public int packetType;

    public Frame(T t, int i, int i2) {
        this.data = t;
        this.packetType = i;
        this.frameType = i2;
    }
}
