package com.yasoka.eazyscreenrecord.recorder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.appsmartz.recorder.MediaEncoder.MediaEncoderListener;
import java.io.IOException;

public abstract class MediaVideoEncoderBase extends MediaEncoder {
    private static final float BPP = 0.25f;
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaVideoEncoderBase";
    protected static int[] recognizedFormats = {2130708361};
    protected final int mHeight;
    protected final int mWidth;

    public MediaVideoEncoderBase(MediaMuxerWrapper mediaMuxerWrapper, MediaEncoderListener mediaEncoderListener, int i, int i2) {
        super(mediaMuxerWrapper, mediaEncoderListener);
        this.mWidth = i;
        this.mHeight = i2;
    }

    protected static final MediaCodecInfo selectVideoCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (String equalsIgnoreCase : supportedTypes) {
                    if (equalsIgnoreCase.equalsIgnoreCase(str) && selectColorFormat(codecInfoAt, str) > 0) {
                        return codecInfoAt;
                    }
                }
                continue;
            }
        }
        return null;
    }

    /* JADX INFO: finally extract failed */
    protected static final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        try {
            Thread.currentThread().setPriority(10);
            CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= capabilitiesForType.colorFormats.length) {
                    break;
                }
                int i3 = capabilitiesForType.colorFormats[i2];
                if (isRecognizedViewoFormat(i3)) {
                    i = i3;
                    break;
                }
                i2++;
            }
            if (i == 0) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("couldn't find a good color format for ");
                sb.append(mediaCodecInfo.getName());
                sb.append(" / ");
                sb.append(str);
                Log.e(str2, sb.toString());
            }
            return i;
        } catch (Throwable th) {
            Thread.currentThread().setPriority(5);
            throw th;
        }
    }

    protected static final boolean isRecognizedViewoFormat(int i) {
        int[] iArr = recognizedFormats;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats[i2] == i) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public MediaFormat create_encoder_format(String str, int i, int i2) {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(str, this.mWidth, this.mHeight);
        createVideoFormat.setInteger("color-format", 2130708361);
        if (i2 <= 0) {
            i2 = calcBitRate(i);
        }
        createVideoFormat.setInteger("bitrate", i2);
        createVideoFormat.setInteger("frame-rate", i);
        createVideoFormat.setInteger("i-frame-interval", 1);
        return createVideoFormat;
    }

    /* access modifiers changed from: protected */
    public Surface prepare_surface_encoder(String str, int i, int i2) throws IOException, IllegalArgumentException {
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mMuxerStarted = false;
        if (selectVideoCodec(str) != null) {
            MediaFormat create_encoder_format = create_encoder_format(str, i, i2);
            this.mMediaCodec = MediaCodec.createEncoderByType(str);
            this.mMediaCodec.configure(create_encoder_format, null, null, 1);
            return this.mMediaCodec.createInputSurface();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find an appropriate codec for ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public int calcBitRate(int i) {
        int i2 = (int) (((float) i) * BPP * ((float) this.mWidth) * ((float) this.mHeight));
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", new Object[]{Float.valueOf((((float) i2) / 1024.0f) / 1024.0f)}));
        return i2;
    }

    /* access modifiers changed from: protected */
    public void signalEndOfInputStream() {
        try {
            this.mMediaCodec.signalEndOfInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mIsEOS = true;
    }
}
