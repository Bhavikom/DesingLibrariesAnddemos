package com.yasoka.eazyscreenrecord.recorder.rtmp;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public interface GetAacData {
    void getAacData(ByteBuffer byteBuffer, BufferInfo bufferInfo);

    void onAudioFormat(MediaFormat mediaFormat);
}
