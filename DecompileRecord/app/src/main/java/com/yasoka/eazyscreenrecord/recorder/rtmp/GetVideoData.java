package com.yasoka.eazyscreenrecord.recorder.rtmp;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public interface GetVideoData {
    void getVideoData(ByteBuffer byteBuffer, BufferInfo bufferInfo);

    void onSpsPps(ByteBuffer byteBuffer, ByteBuffer byteBuffer2);

    void onVideoFormat(MediaFormat mediaFormat);
}
