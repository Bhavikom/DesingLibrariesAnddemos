package com.yasoka.eazyscreenrecord.recorder;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public abstract class MediaEncoder implements Runnable {
    private static final boolean DEBUG = false;
    protected static final int MSG_FRAME_AVAILABLE = 1;
    protected static final int MSG_STOP_RECORDING = 9;
    private static final String TAG = "MediaEncoder";
    protected static final int TIMEOUT_USEC = 10000;
    private BufferInfo mBufferInfo;
    protected volatile boolean mIsCapturing;
    protected boolean mIsEOS;
    private long mLastPausedTimeUs;
    protected final MediaEncoderListener mListener;
    protected MediaCodec mMediaCodec;
    protected boolean mMuxerStarted;
    private int mRequestDrain;
    protected volatile boolean mRequestPause;
    protected volatile boolean mRequestStop;
    protected final Object mSync = new Object();
    protected int mTrackIndex;
    protected final WeakReference<MediaMuxerWrapper> mWeakMuxer;
    private boolean mute = false;
    private long offsetPTSUs = 0;
    private long prevOutputPTSUs = 0;
    private boolean spsPpsSetted = false;

    public interface MediaEncoderListener {
        void onPrepared(MediaEncoder mediaEncoder);

        void onStopped(MediaEncoder mediaEncoder);
    }

    /* access modifiers changed from: 0000 */
    public abstract void prepare() throws IOException;

    public void setMute(boolean z) {
        this.mute = z;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|6|7|8|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0044 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MediaEncoder(com.appsmartz.recorder.MediaMuxerWrapper r3, com.appsmartz.recorder.MediaEncoder.MediaEncoderListener r4) {
        /*
            r2 = this;
            r2.<init>()
            java.lang.Object r0 = new java.lang.Object
            r0.<init>()
            r2.mSync = r0
            r0 = 0
            r2.prevOutputPTSUs = r0
            r2.offsetPTSUs = r0
            r0 = 0
            r2.spsPpsSetted = r0
            r2.mute = r0
            if (r4 == 0) goto L_0x0051
            if (r3 == 0) goto L_0x0049
            java.lang.ref.WeakReference r0 = new java.lang.ref.WeakReference
            r0.<init>(r3)
            r2.mWeakMuxer = r0
            r3.addEncoder(r2)
            r2.mListener = r4
            java.lang.Object r3 = r2.mSync
            monitor-enter(r3)
            android.media.MediaCodec$BufferInfo r4 = new android.media.MediaCodec$BufferInfo     // Catch:{ all -> 0x0046 }
            r4.<init>()     // Catch:{ all -> 0x0046 }
            r2.mBufferInfo = r4     // Catch:{ all -> 0x0046 }
            java.lang.Thread r4 = new java.lang.Thread     // Catch:{ all -> 0x0046 }
            java.lang.Class r0 = r2.getClass()     // Catch:{ all -> 0x0046 }
            java.lang.String r0 = r0.getSimpleName()     // Catch:{ all -> 0x0046 }
            r4.<init>(r2, r0)     // Catch:{ all -> 0x0046 }
            r4.start()     // Catch:{ all -> 0x0046 }
            java.lang.Object r4 = r2.mSync     // Catch:{ InterruptedException -> 0x0044 }
            r4.wait()     // Catch:{ InterruptedException -> 0x0044 }
        L_0x0044:
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            return
        L_0x0046:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            throw r4
        L_0x0049:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r4 = "MediaMuxerWrapper is null"
            r3.<init>(r4)
            throw r3
        L_0x0051:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r4 = "MediaEncoderListener is null"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaEncoder.<init>(com.appsmartz.recorder.MediaMuxerWrapper, com.appsmartz.recorder.MediaEncoder$MediaEncoderListener):void");
    }

    public void setRecordBps(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("video-bitrate", i * 1024);
        this.mMediaCodec.setParameters(bundle);
    }

    public String getOutputPath() {
        MediaMuxerWrapper mediaMuxerWrapper = (MediaMuxerWrapper) this.mWeakMuxer.get();
        if (mediaMuxerWrapper != null) {
            return mediaMuxerWrapper.getOutputPath();
        }
        return null;
    }

    public boolean frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsCapturing) {
                if (!this.mRequestStop) {
                    this.mRequestDrain++;
                    this.mSync.notifyAll();
                    return true;
                }
            }
            return false;
        }
    }

    public void run() {
        boolean z;
        boolean z2;
        synchronized (this.mSync) {
            this.mRequestStop = false;
            this.mRequestDrain = 0;
            this.mSync.notify();
        }
        while (true) {
            synchronized (this.mSync) {
                z = this.mRequestStop;
                z2 = this.mRequestDrain > 0;
                if (z2) {
                    this.mRequestDrain--;
                }
            }
            if (z) {
                drain();
                signalEndOfInputStream();
                drain();
                release();
                break;
            } else if (z2) {
                drain();
            } else {
                synchronized (this.mSync) {
                    try {
                        this.mSync.wait();
                        try {
                        } finally {
                            while (true) {
                            }
                        }
                    } catch (InterruptedException unused) {
                        synchronized (this.mSync) {
                            this.mRequestStop = true;
                            this.mIsCapturing = false;
                        }
                        return;
                    }
                }
            }
        }
        while (true) {
        }
    }

    /* access modifiers changed from: 0000 */
    public void startRecording() {
        synchronized (this.mSync) {
            this.mIsCapturing = true;
            this.mRequestStop = false;
            this.mRequestPause = false;
            this.mSync.notifyAll();
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopRecording() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.mSync
            monitor-enter(r0)
            boolean r1 = r2.mIsCapturing     // Catch:{ all -> 0x0018 }
            if (r1 == 0) goto L_0x0016
            boolean r1 = r2.mRequestStop     // Catch:{ all -> 0x0018 }
            if (r1 == 0) goto L_0x000c
            goto L_0x0016
        L_0x000c:
            r1 = 1
            r2.mRequestStop = r1     // Catch:{ all -> 0x0018 }
            java.lang.Object r1 = r2.mSync     // Catch:{ all -> 0x0018 }
            r1.notifyAll()     // Catch:{ all -> 0x0018 }
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            return
        L_0x0016:
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            return
        L_0x0018:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0018 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaEncoder.stopRecording():void");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void pauseRecording() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mSync
            monitor-enter(r0)
            boolean r1 = r5.mIsCapturing     // Catch:{ all -> 0x0021 }
            if (r1 == 0) goto L_0x001f
            boolean r1 = r5.mRequestStop     // Catch:{ all -> 0x0021 }
            if (r1 == 0) goto L_0x000c
            goto L_0x001f
        L_0x000c:
            r1 = 1
            r5.mRequestPause = r1     // Catch:{ all -> 0x0021 }
            long r1 = java.lang.System.nanoTime()     // Catch:{ all -> 0x0021 }
            r3 = 1000(0x3e8, double:4.94E-321)
            long r1 = r1 / r3
            r5.mLastPausedTimeUs = r1     // Catch:{ all -> 0x0021 }
            java.lang.Object r1 = r5.mSync     // Catch:{ all -> 0x0021 }
            r1.notifyAll()     // Catch:{ all -> 0x0021 }
            monitor-exit(r0)     // Catch:{ all -> 0x0021 }
            return
        L_0x001f:
            monitor-exit(r0)     // Catch:{ all -> 0x0021 }
            return
        L_0x0021:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0021 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaEncoder.pauseRecording():void");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void resumeRecording() {
        /*
            r7 = this;
            java.lang.Object r0 = r7.mSync
            monitor-enter(r0)
            boolean r1 = r7.mIsCapturing     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x002c
            boolean r1 = r7.mRequestStop     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x000c
            goto L_0x002c
        L_0x000c:
            long r1 = r7.mLastPausedTimeUs     // Catch:{ all -> 0x002e }
            r3 = 0
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 == 0) goto L_0x0022
            long r1 = java.lang.System.nanoTime()     // Catch:{ all -> 0x002e }
            r5 = 1000(0x3e8, double:4.94E-321)
            long r1 = r1 / r5
            long r5 = r7.mLastPausedTimeUs     // Catch:{ all -> 0x002e }
            long r1 = r1 - r5
            r7.offsetPTSUs = r1     // Catch:{ all -> 0x002e }
            r7.mLastPausedTimeUs = r3     // Catch:{ all -> 0x002e }
        L_0x0022:
            r1 = 0
            r7.mRequestPause = r1     // Catch:{ all -> 0x002e }
            java.lang.Object r1 = r7.mSync     // Catch:{ all -> 0x002e }
            r1.notifyAll()     // Catch:{ all -> 0x002e }
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            return
        L_0x002c:
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            return
        L_0x002e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaEncoder.resumeRecording():void");
    }

    /* access modifiers changed from: protected */
    public void release() {
        try {
            this.mListener.onStopped(this);
        } catch (Exception e) {
            Log.e(TAG, "failed onStopped", e);
        }
        this.mIsCapturing = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mMediaCodec.release();
                this.mMediaCodec = null;
            } catch (Exception e2) {
                Log.e(TAG, "failed releasing MediaCodec", e2);
            }
        }
        if (this.mMuxerStarted) {
            WeakReference<MediaMuxerWrapper> weakReference = this.mWeakMuxer;
            MediaMuxerWrapper mediaMuxerWrapper = weakReference != null ? (MediaMuxerWrapper) weakReference.get() : null;
            if (mediaMuxerWrapper != null) {
                try {
                    mediaMuxerWrapper.stop();
                } catch (Exception e3) {
                    Log.e(TAG, "failed stopping muxer", e3);
                }
            }
        }
        this.mBufferInfo = null;
    }

    /* access modifiers changed from: protected */
    public void signalEndOfInputStream() {
        encode(null, 0, getPTSUs());
    }

    /* access modifiers changed from: protected */
    public void encode(ByteBuffer byteBuffer, int i, long j) {
        if (this.mIsCapturing) {
            ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
            while (true) {
                if (!this.mIsCapturing) {
                    break;
                }
                try {
                    int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(10000);
                    if (dequeueInputBuffer >= 0) {
                        ByteBuffer byteBuffer2 = inputBuffers[dequeueInputBuffer];
                        byteBuffer2.clear();
                        if (byteBuffer != null) {
                            byteBuffer2.put(byteBuffer);
                        }
                        if (i <= 0) {
                            this.mIsEOS = true;
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                        } else {
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, i, j, 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x0072 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drain() {
        /*
            r10 = this;
            android.media.MediaCodec r0 = r10.mMediaCodec
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.nio.ByteBuffer[] r0 = r0.getOutputBuffers()     // Catch:{ Exception -> 0x013b }
            java.lang.ref.WeakReference<com.appsmartz.recorder.MediaMuxerWrapper> r1 = r10.mWeakMuxer     // Catch:{ Exception -> 0x013b }
            java.lang.Object r1 = r1.get()     // Catch:{ Exception -> 0x013b }
            com.appsmartz.recorder.MediaMuxerWrapper r1 = (com.appsmartz.recorder.MediaMuxerWrapper) r1     // Catch:{ Exception -> 0x013b }
            if (r1 != 0) goto L_0x001b
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x013b }
            java.lang.String r1 = "muxer is unexpectedly null"
            android.util.Log.w(r0, r1)     // Catch:{ Exception -> 0x013b }
            return
        L_0x001b:
            r2 = 0
            r3 = r0
            r0 = 0
        L_0x001e:
            boolean r4 = r10.mIsCapturing     // Catch:{ Exception -> 0x013b }
            if (r4 == 0) goto L_0x013f
            android.media.MediaCodec r4 = r10.mMediaCodec     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r5 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            r6 = 10000(0x2710, double:4.9407E-320)
            int r4 = r4.dequeueOutputBuffer(r5, r6)     // Catch:{ Exception -> 0x013b }
            r5 = -1
            if (r4 != r5) goto L_0x003a
            boolean r4 = r10.mIsEOS     // Catch:{ Exception -> 0x013b }
            if (r4 != 0) goto L_0x001e
            int r0 = r0 + 1
            r4 = 5
            if (r0 <= r4) goto L_0x001e
            goto L_0x013f
        L_0x003a:
            r5 = -3
            if (r4 != r5) goto L_0x0044
            android.media.MediaCodec r3 = r10.mMediaCodec     // Catch:{ Exception -> 0x013b }
            java.nio.ByteBuffer[] r3 = r3.getOutputBuffers()     // Catch:{ Exception -> 0x013b }
            goto L_0x001e
        L_0x0044:
            r5 = -2
            r6 = 1
            if (r4 != r5) goto L_0x0082
            boolean r4 = r10.mMuxerStarted     // Catch:{ Exception -> 0x013b }
            if (r4 != 0) goto L_0x007a
            android.media.MediaCodec r4 = r10.mMediaCodec     // Catch:{ Exception -> 0x013b }
            android.media.MediaFormat r4 = r4.getOutputFormat()     // Catch:{ Exception -> 0x013b }
            int r5 = r1.addTrack(r4)     // Catch:{ Exception -> 0x013b }
            r10.mTrackIndex = r5     // Catch:{ Exception -> 0x013b }
            r10.mMuxerStarted = r6     // Catch:{ Exception -> 0x013b }
            r10.sendSPSandPPS(r4)     // Catch:{ Exception -> 0x013b }
            r10.spsPpsSetted = r6     // Catch:{ Exception -> 0x013b }
            boolean r4 = r1.start()     // Catch:{ Exception -> 0x013b }
            if (r4 != 0) goto L_0x001e
            monitor-enter(r1)     // Catch:{ Exception -> 0x013b }
        L_0x0066:
            boolean r4 = r1.isStarted()     // Catch:{ all -> 0x0077 }
            if (r4 != 0) goto L_0x0075
            r4 = 100
            r1.wait(r4)     // Catch:{ InterruptedException -> 0x0072 }
            goto L_0x0066
        L_0x0072:
            monitor-exit(r1)     // Catch:{ all -> 0x0077 }
            goto L_0x013f
        L_0x0075:
            monitor-exit(r1)     // Catch:{ all -> 0x0077 }
            goto L_0x001e
        L_0x0077:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0077 }
            throw r0     // Catch:{ Exception -> 0x013b }
        L_0x007a:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x013b }
            java.lang.String r1 = "format changed twice"
            r0.<init>(r1)     // Catch:{ Exception -> 0x013b }
            throw r0     // Catch:{ Exception -> 0x013b }
        L_0x0082:
            if (r4 >= 0) goto L_0x0085
            goto L_0x001e
        L_0x0085:
            r5 = r3[r4]     // Catch:{ Exception -> 0x013b }
            if (r5 == 0) goto L_0x011f
            android.media.MediaCodec$BufferInfo r7 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            int r7 = r7.flags     // Catch:{ Exception -> 0x013b }
            r7 = r7 & 2
            if (r7 == 0) goto L_0x00bd
            boolean r7 = r10.spsPpsSetted     // Catch:{ Exception -> 0x013b }
            if (r7 != 0) goto L_0x00bd
            java.nio.ByteBuffer r7 = r5.duplicate()     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r8 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            int r8 = r8.size     // Catch:{ Exception -> 0x013b }
            android.util.Pair r7 = r10.decodeSpsPpsFromBuffer(r7, r8)     // Catch:{ Exception -> 0x013b }
            if (r7 == 0) goto L_0x00bd
            java.io.PrintStream r8 = java.lang.System.out     // Catch:{ Exception -> 0x013b }
            java.lang.String r9 = "MediaEncoder.drain SPS SENDING"
            r8.println(r9)     // Catch:{ Exception -> 0x013b }
            com.appsmartz.recorder.rtmp.RtmpDisplay r8 = r1.rtmpDisplay     // Catch:{ Exception -> 0x013b }
            if (r8 == 0) goto L_0x00bb
            com.appsmartz.recorder.rtmp.RtmpDisplay r8 = r1.rtmpDisplay     // Catch:{ Exception -> 0x013b }
            java.lang.Object r9 = r7.first     // Catch:{ Exception -> 0x013b }
            java.nio.ByteBuffer r9 = (java.nio.ByteBuffer) r9     // Catch:{ Exception -> 0x013b }
            java.lang.Object r7 = r7.second     // Catch:{ Exception -> 0x013b }
            java.nio.ByteBuffer r7 = (java.nio.ByteBuffer) r7     // Catch:{ Exception -> 0x013b }
            r8.onSpsPps(r9, r7)     // Catch:{ Exception -> 0x013b }
        L_0x00bb:
            r10.spsPpsSetted = r6     // Catch:{ Exception -> 0x013b }
        L_0x00bd:
            android.media.MediaCodec$BufferInfo r6 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            int r6 = r6.size     // Catch:{ Exception -> 0x013b }
            if (r6 == 0) goto L_0x010f
            boolean r0 = r10.mMuxerStarted     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x0107
            boolean r0 = r10.mRequestPause     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x00d1
            boolean r0 = r1.isLive()     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x0105
        L_0x00d1:
            android.media.MediaCodec$BufferInfo r0 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            long r6 = r10.getPTSUs()     // Catch:{ Exception -> 0x013b }
            r0.presentationTimeUs = r6     // Catch:{ Exception -> 0x013b }
            com.appsmartz.recorder.rtmp.RtmpDisplay r0 = r1.rtmpDisplay     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x00f8
            boolean r0 = r10 instanceof com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x00e9
            com.appsmartz.recorder.rtmp.RtmpDisplay r0 = r1.rtmpDisplay     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r6 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            r0.getVideoData(r5, r6)     // Catch:{ Exception -> 0x013b }
            goto L_0x00f8
        L_0x00e9:
            boolean r0 = r10 instanceof com.appsmartz.recorder.MediaAudioEncoder     // Catch:{ Exception -> 0x013b }
            if (r0 == 0) goto L_0x00f8
            boolean r0 = r10.mute     // Catch:{ Exception -> 0x013b }
            if (r0 != 0) goto L_0x00f8
            com.appsmartz.recorder.rtmp.RtmpDisplay r0 = r1.rtmpDisplay     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r6 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            r0.getAacData(r5, r6)     // Catch:{ Exception -> 0x013b }
        L_0x00f8:
            int r0 = r10.mTrackIndex     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r6 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            r1.writeSampleData(r0, r5, r6)     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r0 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            long r5 = r0.presentationTimeUs     // Catch:{ Exception -> 0x013b }
            r10.prevOutputPTSUs = r5     // Catch:{ Exception -> 0x013b }
        L_0x0105:
            r0 = 0
            goto L_0x010f
        L_0x0107:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x013b }
            java.lang.String r1 = "drain:muxer hasn't started"
            r0.<init>(r1)     // Catch:{ Exception -> 0x013b }
            throw r0     // Catch:{ Exception -> 0x013b }
        L_0x010f:
            android.media.MediaCodec r5 = r10.mMediaCodec     // Catch:{ Exception -> 0x013b }
            r5.releaseOutputBuffer(r4, r2)     // Catch:{ Exception -> 0x013b }
            android.media.MediaCodec$BufferInfo r4 = r10.mBufferInfo     // Catch:{ Exception -> 0x013b }
            int r4 = r4.flags     // Catch:{ Exception -> 0x013b }
            r4 = r4 & 4
            if (r4 == 0) goto L_0x001e
            r10.mIsCapturing = r2     // Catch:{ Exception -> 0x013b }
            goto L_0x013f
        L_0x011f:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x013b }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x013b }
            r1.<init>()     // Catch:{ Exception -> 0x013b }
            java.lang.String r2 = "encoderOutputBuffer "
            r1.append(r2)     // Catch:{ Exception -> 0x013b }
            r1.append(r4)     // Catch:{ Exception -> 0x013b }
            java.lang.String r2 = " was null"
            r1.append(r2)     // Catch:{ Exception -> 0x013b }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x013b }
            r0.<init>(r1)     // Catch:{ Exception -> 0x013b }
            throw r0     // Catch:{ Exception -> 0x013b }
        L_0x013b:
            r0 = move-exception
            r0.printStackTrace()
        L_0x013f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaEncoder.drain():void");
    }

    private void sendSPSandPPS(MediaFormat mediaFormat) {
        if (((MediaMuxerWrapper) this.mWeakMuxer.get()).rtmpDisplay != null) {
            ((MediaMuxerWrapper) this.mWeakMuxer.get()).rtmpDisplay.onSpsPps(mediaFormat.getByteBuffer("csd-0"), mediaFormat.getByteBuffer("csd-1"));
        }
    }

    private Pair<ByteBuffer, ByteBuffer> decodeSpsPpsFromBuffer(ByteBuffer byteBuffer, int i) {
        byte[] bArr;
        byte[] bArr2;
        byte[] bArr3 = new byte[i];
        byteBuffer.get(bArr3, 0, i);
        int i2 = 0;
        int i3 = -1;
        while (true) {
            if (i2 >= i - 4) {
                i2 = -1;
                break;
            }
            if (bArr3[i2] == 0 && bArr3[i2 + 1] == 0 && bArr3[i2 + 2] == 0 && bArr3[i2 + 3] == 1) {
                if (i3 != -1) {
                    break;
                }
                i3 = i2;
            }
            i2++;
        }
        if (i3 == -1 || i2 == -1) {
            bArr = null;
            bArr2 = null;
        } else {
            bArr = new byte[i2];
            System.arraycopy(bArr3, i3, bArr, 0, i2);
            int i4 = i - i2;
            bArr2 = new byte[i4];
            System.arraycopy(bArr3, i2, bArr2, 0, i4);
        }
        if (bArr == null || bArr2 == null) {
            return null;
        }
        return new Pair<>(ByteBuffer.wrap(bArr), ByteBuffer.wrap(bArr2));
    }

    /* access modifiers changed from: protected */
    public long getPTSUs() {
        long nanoTime;
        synchronized (this.mSync) {
            nanoTime = (System.nanoTime() / 1000) - this.offsetPTSUs;
        }
        long j = this.prevOutputPTSUs;
        if (nanoTime >= j) {
            return nanoTime;
        }
        long j2 = j - nanoTime;
        this.offsetPTSUs -= j2;
        return nanoTime + j2;
    }
}
