package com.yasoka.eazyscreenrecord.recorder.rtmp;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
/*import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;*/

public class RtmpDisplay implements GetVideoData, GetAacData {
    private SrsFlvMuxer srsFlvMuxer;
    private boolean streaming = false;
    private String url;

    public void onAudioFormat(MediaFormat mediaFormat) {
    }

    public void onVideoFormat(MediaFormat mediaFormat) {
    }

    public RtmpDisplay(ConnectCheckerRtmp connectCheckerRtmp) {
        this.srsFlvMuxer = new SrsFlvMuxer(connectCheckerRtmp);
    }

    public void setResolution(int i, int i2) {
        this.srsFlvMuxer.setVideoResolution(i, i2);
    }

    public void startStreamRtp() {
        this.streaming = true;
        this.srsFlvMuxer.start(this.url);
    }

    public void stopStreamRtp() {
        if (this.streaming) {
            this.streaming = false;
            this.srsFlvMuxer.stop();
        }
    }

    public void onSpsPps(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        this.srsFlvMuxer.setSpsPPs(byteBuffer, byteBuffer2);
    }

    public void getVideoData(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        if (this.streaming) {
            this.srsFlvMuxer.sendVideo(byteBuffer, bufferInfo);
        }
    }

    public void getAacData(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        if (this.streaming) {
            this.srsFlvMuxer.sendAudio(byteBuffer, bufferInfo);
        }
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
