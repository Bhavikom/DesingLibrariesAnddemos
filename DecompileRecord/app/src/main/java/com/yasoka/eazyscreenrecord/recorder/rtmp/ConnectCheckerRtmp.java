package com.yasoka.eazyscreenrecord.recorder.rtmp;

public interface ConnectCheckerRtmp {
    void onAuthErrorRtmp();

    void onAuthSuccessRtmp();

    void onConnectionFailedRtmp(String str);

    void onConnectionSuccessRtmp();

    void onDisconnectRtmp();
}
