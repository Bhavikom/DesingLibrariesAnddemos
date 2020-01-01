package com.yasoka.eazyscreenrecord.recorder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
//import com.appsmartz.recorder.rtmp.RtmpDisplay;
import com.yasoka.eazyscreenrecord.recorder.rtmp.RtmpDisplay;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
//import net.ossrs.rtmp.ConnectCheckerRtmp;

public class MediaMuxerWrapper {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaMuxerWrapper";
    private MediaEncoder mAudioEncoder;
    private int mEncoderCount;
    private volatile boolean mIsPaused;
    private boolean mIsStarted;
    private MediaMuxer mMediaMuxer;
    private String mOutputPath;
    private int mStatredCount;
    private MediaEncoder mVideoEncoder;
    /* access modifiers changed from: private */
    public MediaMuxerInterface mediaMuxerInterface;
    private boolean record_local = false;
    RtmpDisplay rtmpDisplay;
    private final String url;

    public interface MediaMuxerInterface {
        void onLive();

        void onOffline();
    }

    public MediaMuxerWrapper(String str, boolean z, String str2) throws IOException {
        try {
            this.mOutputPath = str;
            if (z) {
                this.mMediaMuxer = new MediaMuxer(this.mOutputPath, 0);
            }
            this.mStatredCount = 0;
            this.mEncoderCount = 0;
            this.mIsStarted = false;
            this.record_local = z;
            this.url = str2;
            if (str2 != null) {
                this.rtmpDisplay = new RtmpDisplay(new ConnectCheckerRtmp() {
                    public void onConnectionSuccessRtmp() {
                        System.out.println("MediaEncoder.onConnectionSuccessRtmp");
                        if (MediaMuxerWrapper.this.mediaMuxerInterface != null) {
                            MediaMuxerWrapper.this.mediaMuxerInterface.onLive();
                        }
                    }

                    public void onConnectionFailedRtmp(String str) {
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("MediaEncoder.onConnectionFailedRtmp->");
                        sb.append(str);
                        printStream.println(sb.toString());
                        if (MediaMuxerWrapper.this.mediaMuxerInterface != null) {
                            MediaMuxerWrapper.this.mediaMuxerInterface.onOffline();
                        }
                    }

                    public void onDisconnectRtmp() {
                        System.out.println("MediaEncoder.onDisconnectRtmp");
                        if (MediaMuxerWrapper.this.mediaMuxerInterface != null) {
                            MediaMuxerWrapper.this.mediaMuxerInterface.onOffline();
                        }
                    }

                    public void onAuthErrorRtmp() {
                        System.out.println("MediaEncoder.onAuthErrorRtmp");
                    }

                    public void onAuthSuccessRtmp() {
                        System.out.println("MediaEncoder.onAuthSuccessRtmp");
                    }
                });
                this.rtmpDisplay.setUrl(str2);
            }
        } catch (NullPointerException unused) {
            throw new RuntimeException("This app has no permission of writing external storage");
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isLive() {
        return this.url != null;
    }

    public void setMediaMuxerInterface(MediaMuxerInterface mediaMuxerInterface2) {
        this.mediaMuxerInterface = mediaMuxerInterface2;
    }

    /* access modifiers changed from: 0000 */
    public void setHeightWidth(int i, int i2) {
        RtmpDisplay rtmpDisplay2 = this.rtmpDisplay;
        if (rtmpDisplay2 != null) {
            rtmpDisplay2.setResolution(i2, i);
        }
    }

    public void mute(boolean z) {
        MediaEncoder mediaEncoder = this.mAudioEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.setMute(z);
        }
    }

    public String getOutputPath() {
        return this.mOutputPath;
    }

    public synchronized void prepare() throws IOException {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.prepare();
        }
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.prepare();
        }
    }

    public synchronized void startRecording() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.startRecording();
        }
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.startRecording();
        }
        if (this.rtmpDisplay != null) {
            this.rtmpDisplay.startStreamRtp();
        }
    }

    public synchronized void stopRecording() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.stopRecording();
        }
        this.mVideoEncoder = null;
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.stopRecording();
        }
        this.mAudioEncoder = null;
        if (this.rtmpDisplay != null) {
            this.rtmpDisplay.stopStreamRtp();
        }
        this.rtmpDisplay = null;
    }

    public synchronized boolean isStarted() {
        return this.mIsStarted;
    }

    public synchronized void pauseRecording() {
        this.mIsPaused = true;
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.pauseRecording();
        }
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.pauseRecording();
        }
        if (this.rtmpDisplay != null) {
            mute(true);
        }
    }

    public synchronized void resumeRecording() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.resumeRecording();
        }
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.resumeRecording();
        }
        this.mIsPaused = false;
        if (this.rtmpDisplay != null) {
            mute(false);
        }
    }

    public synchronized boolean isPaused() {
        return this.mIsPaused;
    }

    /* access modifiers changed from: 0000 */
    public void addEncoder(MediaEncoder mediaEncoder) {
        String str = "Video encoder already added.";
        if (mediaEncoder instanceof MediaVideoEncoderBase) {
            if (this.mVideoEncoder == null) {
                this.mVideoEncoder = mediaEncoder;
            } else {
                throw new IllegalArgumentException(str);
            }
        } else if (!(mediaEncoder instanceof MediaAudioEncoder)) {
            throw new IllegalArgumentException("unsupported encoder");
        } else if (this.mAudioEncoder == null) {
            this.mAudioEncoder = mediaEncoder;
        } else {
            throw new IllegalArgumentException(str);
        }
        int i = 1;
        int i2 = this.mVideoEncoder != null ? 1 : 0;
        if (this.mAudioEncoder == null) {
            i = 0;
        }
        this.mEncoderCount = i2 + i;
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean start() {
        this.mStatredCount++;
        if (this.mEncoderCount > 0 && this.mStatredCount == this.mEncoderCount) {
            if (this.mMediaMuxer != null) {
                this.mMediaMuxer.start();
            }
            this.mIsStarted = true;
            notifyAll();
        }
        return this.mIsStarted;
    }

    /* access modifiers changed from: 0000 */
    public synchronized void stop() {
        this.mStatredCount--;
        if (this.mEncoderCount > 0 && this.mStatredCount <= 0) {
            if (this.mMediaMuxer != null) {
                this.mMediaMuxer.stop();
                this.mMediaMuxer.release();
            }
            this.mIsStarted = false;
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized int addTrack(MediaFormat mediaFormat) {
        if (this.mIsStarted) {
            throw new IllegalStateException("muxer already started");
        } else if (this.mMediaMuxer == null) {
            return -1;
        } else {
            return this.mMediaMuxer.addTrack(mediaFormat);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void writeSampleData(int i, ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        try {
            if (this.mStatredCount > 0 && this.record_local && this.mMediaMuxer != null) {
                this.mMediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
