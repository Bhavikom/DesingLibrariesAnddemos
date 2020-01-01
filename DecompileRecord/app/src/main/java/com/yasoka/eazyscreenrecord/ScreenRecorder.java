package com.yasoka.eazyscreenrecord;

import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenRecorder extends Thread {
    private static final int BIT_RATE = 64000;
    private static int FRAME_RATE = 30;
    private static final int IFRAME_INTERVAL = 1;
    private static final String MIME_TYPE = "video/avc";
    private static final String MIME_TYPE_GREEN = "video/mp4v-es";
    private static final int SAMPLE_RATE = 44100;
    private static final String TAG = "ScreenRecorder";
    private static final int TIMEOUT_US = 10000;
    private static final String aMIME_TYPE = "audio/mp4a-latm";
    public static boolean mIsCapturing = true;
    private BufferInfo aBufferInfo = new BufferInfo();
    /* access modifiers changed from: private */
    public MediaCodec aEncoder;
    private int aVideoTrackIndex = -1;
    /* access modifiers changed from: private */
    public final Context context;
    private long gapOutputPTSUs = 0;
    private boolean isAudio;
    private final boolean isGreen;
    private final int isOrientataion;
    private boolean isPause;
    private AudioThread mAudioThread = null;
    private int mBitRate;
    private BufferInfo mBufferInfo = new BufferInfo();
    private int mDpi;
    private String mDstPath;
    private MediaCodec mEncoder;
    private int mHeight;
    private MediaProjection mMediaProjection;
    private MediaMuxer mMuxer;
    private boolean mMuxerStarted = false;
    /* access modifiers changed from: private */
    public AtomicBoolean mQuit = new AtomicBoolean(false);
    private Surface mSurface;
    private int mVideoTrackIndex = -1;
    private VirtualDisplay mVirtualDisplay;
    private int mWidth;
    private String orientation;
    private int pauseCount;
    private long pauseOutputPTSUs = 0;
    private long prevOutputPTSUs = 0;
    /* access modifiers changed from: private */
    public AtomicBoolean recording = new AtomicBoolean(true);

    class AudioThread extends Thread {
        AudioThread() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:8:0x0029 A[Catch:{ Exception -> 0x0066 }, LOOP:1: B:8:0x0029->B:39:0x0029, LOOP_START] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r9 = this;
                r0 = 44100(0xac44, float:6.1797E-41)
                r1 = 16
                r2 = 2
                int r0 = android.media.AudioRecord.getMinBufferSize(r0, r1, r2)     // Catch:{ Exception -> 0x007e }
                android.media.AudioRecord r1 = new android.media.AudioRecord     // Catch:{ Exception -> 0x007e }
                r4 = 1
                r5 = 44100(0xac44, float:6.1797E-41)
                r6 = 16
                r7 = 2
                r3 = r1
                r8 = r0
                r3.<init>(r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x007e }
                byte[] r2 = new byte[r0]     // Catch:{ Exception -> 0x0075 }
                r1.startRecording()     // Catch:{ Exception -> 0x0075 }
            L_0x001d:
                com.ezscreenrecorder.ScreenRecorder r3 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.mQuit     // Catch:{ Exception -> 0x0066 }
                boolean r3 = r3.get()     // Catch:{ Exception -> 0x0066 }
                if (r3 != 0) goto L_0x0060
            L_0x0029:
                com.ezscreenrecorder.ScreenRecorder r3 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.recording     // Catch:{ Exception -> 0x0066 }
                boolean r3 = r3.get()     // Catch:{ Exception -> 0x0066 }
                if (r3 == 0) goto L_0x001d
                r3 = 0
                int r3 = r1.read(r2, r3, r0)     // Catch:{ Exception -> 0x0066 }
                if (r3 <= 0) goto L_0x0029
                com.ezscreenrecorder.ScreenRecorder r4 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                java.util.concurrent.atomic.AtomicBoolean r4 = r4.mQuit     // Catch:{ Exception -> 0x0066 }
                boolean r4 = r4.get()     // Catch:{ Exception -> 0x0066 }
                if (r4 != 0) goto L_0x0029
                boolean r4 = com.ezscreenrecorder.ScreenRecorder.mIsCapturing     // Catch:{ Exception -> 0x0066 }
                if (r4 == 0) goto L_0x0029
                com.ezscreenrecorder.ScreenRecorder r4 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                android.media.MediaCodec r4 = r4.aEncoder     // Catch:{ Exception -> 0x0066 }
                if (r4 == 0) goto L_0x0029
                com.ezscreenrecorder.ScreenRecorder r4 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                com.ezscreenrecorder.ScreenRecorder r5 = com.ezscreenrecorder.ScreenRecorder.this     // Catch:{ Exception -> 0x0066 }
                long r5 = r5.getPTSUs()     // Catch:{ Exception -> 0x0066 }
                r4.encode(r2, r3, r5)     // Catch:{ Exception -> 0x0066 }
                goto L_0x0029
            L_0x0060:
                r1.stop()     // Catch:{ Exception -> 0x0075 }
                goto L_0x006b
            L_0x0064:
                r0 = move-exception
                goto L_0x006f
            L_0x0066:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ all -> 0x0064 }
                goto L_0x0060
            L_0x006b:
                r1.release()     // Catch:{ Exception -> 0x007e }
                goto L_0x0082
            L_0x006f:
                r1.stop()     // Catch:{ Exception -> 0x0075 }
                throw r0     // Catch:{ Exception -> 0x0075 }
            L_0x0073:
                r0 = move-exception
                goto L_0x007a
            L_0x0075:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ all -> 0x0073 }
                goto L_0x006b
            L_0x007a:
                r1.release()     // Catch:{ Exception -> 0x007e }
                throw r0     // Catch:{ Exception -> 0x007e }
            L_0x007e:
                r0 = move-exception
                r0.printStackTrace()
            L_0x0082:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.ScreenRecorder.AudioThread.run():void");
        }
    }

    public ScreenRecorder(Context context2, int i, int i2, int i3, int i4, MediaProjection mediaProjection, String str, boolean z, String str2, int i5, boolean z2, int i6) {
        super(TAG);
        this.mWidth = i;
        this.mHeight = i2;
        this.mBitRate = i3;
        this.mDpi = i4;
        this.mMediaProjection = mediaProjection;
        this.mDstPath = str;
        this.isAudio = z;
        this.orientation = str2;
        FRAME_RATE = i5;
        this.context = context2;
        this.isGreen = z2;
        this.isOrientataion = i6;
    }

    /* access modifiers changed from: 0000 */
    public void setIsRecording(boolean z) {
        this.recording.set(z);
        if (!z) {
            this.pauseOutputPTSUs = System.nanoTime() / 1000;
            this.pauseCount++;
        } else if (this.pauseOutputPTSUs != 0) {
            this.gapOutputPTSUs += (System.nanoTime() / 1000) - this.pauseOutputPTSUs;
            this.pauseOutputPTSUs = 0;
        }
    }

    /* access modifiers changed from: 0000 */
    public final void quit() {
        this.recording.set(false);
        this.mQuit.set(true);
    }

    public void run() {
        try {
            this.isAudio = this.isAudio && m15ac();
        } catch (Throwable th) {
            try {
                th.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                release();
                throw th2;
            }
        }
        prepareEncoder();
        this.mMuxer = new MediaMuxer(this.mDstPath, 0);
        try {
            this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("ScreenRecorder-display", this.mWidth, this.mHeight, this.mDpi, 1, this.mSurface, null, null);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("created virtual display: ");
            sb.append(this.mVirtualDisplay);
            Log.d(str, sb.toString());
            recordVirtualDisplay();
        } catch (Exception e2) {
            e2.printStackTrace();
            try {
                new Handler().post(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(ScreenRecorder.this.context, C0793R.string.id_error_record_vid_msg, 1).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        release();
    }

    private void recordVirtualDisplay() {
        while (!this.mQuit.get()) {
            while (this.recording.get()) {
                int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 10000);
                StringBuilder sb = new StringBuilder();
                sb.append("dequeue output buffer index=");
                sb.append(dequeueOutputBuffer);
                Log.i(TAG, sb.toString());
                if (dequeueOutputBuffer != -1) {
                    if (dequeueOutputBuffer == -2) {
                        resetOutputFormat();
                    } else if (dequeueOutputBuffer >= 0) {
                        encodeToVideoTrack(this.mEncoder, dequeueOutputBuffer, this.mVideoTrackIndex);
                        this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public long getPTSUs() {
        long nanoTime = System.nanoTime() / 1000;
        long j = this.prevOutputPTSUs;
        return nanoTime < j ? nanoTime + (j - nanoTime) : nanoTime;
    }

    private void encodeToVideoTrack(MediaCodec mediaCodec, int i, int i2) {
        ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
        this.mBufferInfo.presentationTimeUs = getPTSUs();
        if ((this.mBufferInfo.flags & 2) != 0) {
            this.mBufferInfo.size = 0;
        }
        if (this.mBufferInfo.size == 0) {
            outputBuffer = null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("got buffer, info: size=");
            sb.append(this.mBufferInfo.size);
            sb.append(", presentationTimeUs=");
            sb.append(this.mBufferInfo.presentationTimeUs);
            sb.append(", offset=");
            sb.append(this.mBufferInfo.offset);
            Log.d(TAG, sb.toString());
        }
        if (outputBuffer != null && this.mMuxerStarted) {
            outputBuffer.position(this.mBufferInfo.offset);
            outputBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
            this.mMuxer.writeSampleData(i2, outputBuffer, this.mBufferInfo);
            this.prevOutputPTSUs = this.mBufferInfo.presentationTimeUs;
            if ((this.mBufferInfo.flags & 4) != 0) {
                this.recording.set(false);
                this.mQuit.set(true);
            }
        }
    }

    private void encodeToAudioTrack(MediaCodec mediaCodec, int i, int i2) {
        ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
        if ((this.aBufferInfo.flags & 2) != 0) {
            this.aBufferInfo.size = 0;
        }
        if (this.aBufferInfo.size == 0) {
            outputBuffer = null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Audio got buffer, info: size=");
            sb.append(this.aBufferInfo.size);
            sb.append(", presentationTimeUs=");
            sb.append(this.aBufferInfo.presentationTimeUs);
            sb.append(", offset=");
            sb.append(this.aBufferInfo.offset);
            Log.d(TAG, sb.toString());
        }
        this.aBufferInfo.presentationTimeUs = getPTSUs() - 120000;
        if (outputBuffer != null && this.mMuxerStarted) {
            outputBuffer.position(this.aBufferInfo.offset);
            outputBuffer.limit(this.aBufferInfo.offset + this.aBufferInfo.size);
            this.mMuxer.writeSampleData(i2, outputBuffer, this.aBufferInfo);
            if ((this.aBufferInfo.flags & 4) != 0) {
                this.recording.set(false);
                this.mQuit.set(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void encode(byte[] bArr, int i, long j) {
        byte[] bArr2 = bArr;
        int i2 = i;
        ByteBuffer[] inputBuffers = this.aEncoder.getInputBuffers();
        int i3 = 0;
        while (!this.mQuit.get() && i3 < i2) {
            while (true) {
                if (!this.recording.get()) {
                    break;
                }
                int dequeueInputBuffer = this.aEncoder.dequeueInputBuffer(10000);
                if (dequeueInputBuffer >= 0) {
                    ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
                    byteBuffer.clear();
                    int remaining = byteBuffer.remaining();
                    if (i3 + remaining >= i2) {
                        remaining = i2 - i3;
                    }
                    int i4 = remaining;
                    if (i4 > 0 && bArr2 != null) {
                        byteBuffer.put(bArr2, i3, i4);
                    }
                    i3 += i4;
                    if (i2 <= 0) {
                        this.aEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                        break;
                    }
                    this.aEncoder.queueInputBuffer(dequeueInputBuffer, 0, i4, j, 0);
                }
                int dequeueOutputBuffer = this.aEncoder.dequeueOutputBuffer(this.aBufferInfo, 10000);
                if (dequeueOutputBuffer != -1 && dequeueOutputBuffer >= 0) {
                    encodeToAudioTrack(this.aEncoder, dequeueOutputBuffer, this.aVideoTrackIndex);
                    this.aEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
            }
        }
    }

    private void resetOutputFormat() {
        if (!this.mMuxerStarted) {
            this.mVideoTrackIndex = this.mMuxer.addTrack(this.mEncoder.getOutputFormat());
            if (this.isAudio) {
                MediaCodec mediaCodec = this.aEncoder;
                if (mediaCodec != null) {
                    this.aVideoTrackIndex = this.mMuxer.addTrack(mediaCodec.getOutputFormat());
                }
            }
            this.mMuxer.start();
            if (this.mAudioThread == null && this.isAudio) {
                this.mAudioThread = new AudioThread();
                this.mAudioThread.start();
            }
            this.mMuxerStarted = true;
            return;
        }
        throw new IllegalStateException("output format already changed!");
    }

    private void prepareEncoder() throws IOException {
        boolean z = this.isGreen;
        String str = MIME_TYPE;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(z ? MIME_TYPE_GREEN : str, this.mWidth, this.mHeight);
        createVideoFormat.setInteger("color-format", 2130708361);
        String str2 = "bitrate";
        createVideoFormat.setInteger(str2, this.mBitRate);
        createVideoFormat.setInteger("frame-rate", FRAME_RATE);
        createVideoFormat.setInteger("i-frame-interval", 1);
        StringBuilder sb = new StringBuilder();
        sb.append("created video format: ");
        sb.append(createVideoFormat);
        String sb2 = sb.toString();
        String str3 = TAG;
        Log.d(str3, sb2);
        this.mEncoder = MediaCodec.createEncoderByType(str);
        this.mEncoder.configure(createVideoFormat, null, null, 1);
        this.mSurface = this.mEncoder.createInputSurface();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("created input surface: ");
        sb3.append(this.mSurface);
        Log.d(str3, sb3.toString());
        this.mEncoder.start();
        if (this.isAudio) {
            String str4 = aMIME_TYPE;
            MediaFormat createAudioFormat = MediaFormat.createAudioFormat(str4, 44100, 1);
            createAudioFormat.setInteger("aac-profile", 2);
            createAudioFormat.setInteger("channel-mask", 16);
            createAudioFormat.setInteger(str2, 64000);
            createAudioFormat.setInteger("channel-count", 1);
            this.aEncoder = MediaCodec.createEncoderByType(str4);
            this.aEncoder.configure(createAudioFormat, null, null, 1);
            this.aEncoder.start();
        }
    }

    private void release() {
        try {
            if (this.mEncoder != null) {
                this.mEncoder.stop();
                this.mEncoder.release();
                this.mEncoder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        try {
            if (this.aEncoder != null) {
                this.aEncoder.stop();
                this.aEncoder.release();
                this.aEncoder = null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Crashlytics.logException(e2);
        }
        AudioThread audioThread = this.mAudioThread;
        if (audioThread != null) {
            audioThread.interrupt();
            this.mAudioThread = null;
        }
        try {
            if (this.mVirtualDisplay != null) {
                this.mVirtualDisplay.release();
                this.mVirtualDisplay = null;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            Crashlytics.logException(e3);
        }
        try {
            if (this.mMediaProjection != null) {
                this.mMediaProjection.stop();
                this.mMediaProjection = null;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            Crashlytics.logException(e4);
        }
        try {
            if (this.mMuxer != null) {
                this.mMuxer.stop();
                this.mMuxer.release();
                this.mMuxer = null;
            }
        } catch (Exception e5) {
            e5.printStackTrace();
            Crashlytics.logException(e5);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
        r3.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0040, code lost:
        r3.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[ExcHandler: Exception (unused java.lang.Exception), SYNTHETIC, Splitter:B:1:0x0008] */
    /* renamed from: ac */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean m15ac() throws Throwable {
        /*
            r11 = this;
            r0 = 44100(0xac44, float:6.1797E-41)
            r1 = 16
            r2 = 2
            r3 = 0
            r4 = 0
            int r0 = android.media.AudioRecord.getMinBufferSize(r0, r1, r2)     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            android.media.AudioRecord r1 = new android.media.AudioRecord     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            r6 = 1
            r7 = 44100(0xac44, float:6.1797E-41)
            r8 = 16
            r9 = 2
            r5 = r1
            r10 = r0
            r5.<init>(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            r1.startRecording()     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            short[] r2 = new short[r0]     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            int r0 = r1.read(r2, r4, r0)     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            r2 = -3
            if (r0 == r2) goto L_0x002b
            if (r0 != 0) goto L_0x0029
            goto L_0x002b
        L_0x0029:
            r0 = 1
            goto L_0x002c
        L_0x002b:
            r0 = 0
        L_0x002c:
            r1.release()     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            return r0
        L_0x0030:
            r0 = move-exception
            r1.release()     // Catch:{ Exception -> 0x0040, Throwable -> 0x0035 }
            throw r0     // Catch:{ Exception -> 0x0040, Throwable -> 0x0035 }
        L_0x0035:
            r0 = move-exception
            r3 = r1
            goto L_0x003a
        L_0x0038:
            return r4
        L_0x0039:
            r0 = move-exception
        L_0x003a:
            if (r3 == 0) goto L_0x003f
            r3.release()
        L_0x003f:
            throw r0
        L_0x0040:
            r3.release()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.ScreenRecorder.m15ac():boolean");
    }
}
