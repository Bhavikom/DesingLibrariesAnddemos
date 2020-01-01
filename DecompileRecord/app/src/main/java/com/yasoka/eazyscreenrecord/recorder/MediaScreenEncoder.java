package com.yasoka.eazyscreenrecord.recorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.display.VirtualDisplay;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.effect.Effect;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
/*import com.appsmartz.recorder.MediaEncoder.MediaEncoderListener;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;
import com.appsmartz.recorder.glutils.EglTask;
import com.appsmartz.recorder.glutils.GLDrawer2D;
import com.appsmartz.recorder.render.RenderScreen;
import com.appsmartz.recorder.render.Watermark;
import com.appsmartz.recorder.render.effect.Effect;
import com.appsmartz.recorder.render.effect.NullEffect;*/
import com.yasoka.eazyscreenrecord.recorder.glutils.EGLBase;
import com.yasoka.eazyscreenrecord.recorder.glutils.EglTask;
import com.yasoka.eazyscreenrecord.recorder.glutils.GLDrawer2D;
import com.yasoka.eazyscreenrecord.recorder.render.RenderScreen;
import com.yasoka.eazyscreenrecord.recorder.render.Watermark;
import com.yasoka.eazyscreenrecord.recorder.render.effect.NullEffect;

import java.io.IOException;

public class MediaScreenEncoder extends MediaVideoEncoderBase {
    private static final boolean DEBUG = false;
    private static final int FRAME_RATE = 25;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "MediaScreenEncoder";
    private final int bitrate;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final int fps;
    /* access modifiers changed from: private */
    public final Callback mCallback = new Callback() {
        public void onPaused() {
        }

        public void onResumed() {
        }

        public void onStopped() {
        }
    };
    /* access modifiers changed from: private */
    public final int mDensity;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public volatile boolean mIsRecording;
    /* access modifiers changed from: private */
    public MediaProjection mMediaProjection;
    private final DrawTask mScreenCaptureTask = new DrawTask(null, 0);
    /* access modifiers changed from: private */
    public Surface mSurface;
    /* access modifiers changed from: private */
    public final Object mSync = new Object();
    /* access modifiers changed from: private */
    public int orientation;
    /* access modifiers changed from: private */
    public final String orientationValue;
    /* access modifiers changed from: private */
    public Bitmap pauseWatermark;
    /* access modifiers changed from: private */
    public boolean requestDraw;
    /* access modifiers changed from: private */
    public final int startOrientation;
    /* access modifiers changed from: private */
    public Bitmap watermark;

    private final class DrawTask extends EglTask {
        /* access modifiers changed from: private */
        public VirtualDisplay display;
        /* access modifiers changed from: private */
        public long intervals;
        private final Runnable mDrawTask = new Runnable() {
            /* JADX WARNING: Can't wrap try/catch for region: R(5:6|7|8|9|10) */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x0048, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$300(r10.this$1.this$0) == false) goto L_0x0199;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
                if (r1 == false) goto L_0x0064;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:15:0x004c, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$200(r10.this$1).updateTexImage();
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$200(r10.this$1).getTransformMatrix(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:16:0x0064, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$800(r10.this$1).makeCurrent();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x007d, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$900(r10.this$1.this$0) == com.appsmartz.recorder.MediaScreenEncoder.access$1000(r10.this$1.this$0)) goto L_0x00f3;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:0x008d, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$1100(r10.this$1.this$0).equalsIgnoreCase("Auto") == false) goto L_0x00f3;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:0x009f, code lost:
                if (((com.appsmartz.recorder.MediaMuxerWrapper) r10.this$1.this$0.mWeakMuxer.get()).isLive() != false) goto L_0x00f3;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a1, code lost:
                android.opengl.Matrix.rotateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 90.0f, 0.0f, 0.0f, 1.0f);
                android.opengl.Matrix.translateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 0.0f, -1.0f, 0.0f);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x00c6, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$1000(r10.this$1.this$0) != 2) goto L_0x00f3;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:0x00c8, code lost:
                android.opengl.Matrix.rotateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 90.0f, 0.0f, 0.0f, 1.0f);
                android.opengl.Matrix.translateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 0.0f, -1.0f, 0.0f);
                android.opengl.Matrix.rotateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 90.0f, 0.0f, 0.0f, 1.0f);
                android.opengl.Matrix.translateM(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0, 0.0f, -1.0f, 0.0f);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:0x00f3, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1300(r10.this$1).draw(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1200(r10.this$1), com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1), 0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:0x0110, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$1400(r10.this$1.this$0) == null) goto L_0x0132;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:27:0x0112, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1500(r10.this$1).draw(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:0x0127, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1600(r10.this$1) == null) goto L_0x0132;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:29:0x0129, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1600(r10.this$1).draw();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:31:0x0138, code lost:
                if (r10.this$1.this$0.mRequestPause == false) goto L_0x0176;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:0x0142, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.access$1700(r10.this$1.this$0) == null) goto L_0x0176;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x0154, code lost:
                if (((com.appsmartz.recorder.MediaMuxerWrapper) r10.this$1.this$0.mWeakMuxer.get()).isLive() == false) goto L_0x0176;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x0156, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1800(r10.this$1).draw(com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$700(r10.this$1));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:0x016b, code lost:
                if (com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1900(r10.this$1) == null) goto L_0x0176;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:38:0x016d, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$1900(r10.this$1).draw();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:39:0x0176, code lost:
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$800(r10.this$1).swap();
                com.appsmartz.recorder.MediaScreenEncoder.DrawTask.access$2000(r10.this$1);
                android.opengl.GLES20.glClear(16384);
                android.opengl.GLES20.glFlush();
                r10.this$1.this$0.frameAvailableSoon();
                r10.this$1.queueEvent(r10);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:40:0x0199, code lost:
                r10.this$1.releaseSelf();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:41:0x019e, code lost:
                return;
             */
            /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x003d */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r10 = this;
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    java.lang.Object r0 = r0.mSync
                    monitor-enter(r0)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ all -> 0x019f }
                    com.appsmartz.recorder.MediaScreenEncoder r1 = com.appsmartz.recorder.MediaScreenEncoder.this     // Catch:{ all -> 0x019f }
                    boolean r1 = r1.requestDraw     // Catch:{ all -> 0x019f }
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r2 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ all -> 0x019f }
                    com.appsmartz.recorder.MediaScreenEncoder r2 = com.appsmartz.recorder.MediaScreenEncoder.this     // Catch:{ all -> 0x019f }
                    boolean r2 = r2.requestDraw     // Catch:{ all -> 0x019f }
                    r3 = 0
                    if (r2 != 0) goto L_0x003f
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder r1 = com.appsmartz.recorder.MediaScreenEncoder.this     // Catch:{ InterruptedException -> 0x003d }
                    java.lang.Object r1 = r1.mSync     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r2 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ InterruptedException -> 0x003d }
                    long r4 = r2.intervals     // Catch:{ InterruptedException -> 0x003d }
                    r1.wait(r4)     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder r1 = com.appsmartz.recorder.MediaScreenEncoder.this     // Catch:{ InterruptedException -> 0x003d }
                    boolean r1 = r1.requestDraw     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r2 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this     // Catch:{ InterruptedException -> 0x003d }
                    com.appsmartz.recorder.MediaScreenEncoder r2 = com.appsmartz.recorder.MediaScreenEncoder.this     // Catch:{ InterruptedException -> 0x003d }
                    r2.requestDraw = r3     // Catch:{ InterruptedException -> 0x003d }
                    goto L_0x003f
                L_0x003d:
                    monitor-exit(r0)     // Catch:{ all -> 0x019f }
                    return
                L_0x003f:
                    monitor-exit(r0)     // Catch:{ all -> 0x019f }
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    boolean r0 = r0.mIsRecording
                    if (r0 == 0) goto L_0x0199
                    if (r1 == 0) goto L_0x0064
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    android.graphics.SurfaceTexture r0 = r0.mSourceTexture
                    r0.updateTexImage()
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    android.graphics.SurfaceTexture r0 = r0.mSourceTexture
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r1 = r1.mTexMatrix
                    r0.getTransformMatrix(r1)
                L_0x0064:
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r0 = r0.mEncoderSurface
                    r0.makeCurrent()
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    int r0 = r0.screenOrientation
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r1 = com.appsmartz.recorder.MediaScreenEncoder.this
                    int r1 = r1.startOrientation
                    if (r0 == r1) goto L_0x00f3
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    java.lang.String r0 = r0.orientationValue
                    java.lang.String r1 = "Auto"
                    boolean r0 = r0.equalsIgnoreCase(r1)
                    if (r0 == 0) goto L_0x00f3
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    java.lang.ref.WeakReference r0 = r0.mWeakMuxer
                    java.lang.Object r0 = r0.get()
                    com.appsmartz.recorder.MediaMuxerWrapper r0 = (com.appsmartz.recorder.MediaMuxerWrapper) r0
                    boolean r0 = r0.isLive()
                    if (r0 != 0) goto L_0x00f3
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r4 = r0.mTexMatrix
                    r5 = 0
                    r6 = 1119092736(0x42b40000, float:90.0)
                    r7 = 0
                    r8 = 0
                    r9 = 1065353216(0x3f800000, float:1.0)
                    android.opengl.Matrix.rotateM(r4, r5, r6, r7, r8, r9)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r0 = r0.mTexMatrix
                    r1 = -1082130432(0xffffffffbf800000, float:-1.0)
                    r2 = 0
                    android.opengl.Matrix.translateM(r0, r3, r2, r1, r2)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    int r0 = r0.startOrientation
                    r4 = 2
                    if (r0 != r4) goto L_0x00f3
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r4 = r0.mTexMatrix
                    r5 = 0
                    r6 = 1119092736(0x42b40000, float:90.0)
                    r7 = 0
                    r8 = 0
                    r9 = 1065353216(0x3f800000, float:1.0)
                    android.opengl.Matrix.rotateM(r4, r5, r6, r7, r8, r9)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r0 = r0.mTexMatrix
                    android.opengl.Matrix.translateM(r0, r3, r2, r1, r2)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r4 = r0.mTexMatrix
                    android.opengl.Matrix.rotateM(r4, r5, r6, r7, r8, r9)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r0 = r0.mTexMatrix
                    android.opengl.Matrix.translateM(r0, r3, r2, r1, r2)
                L_0x00f3:
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.glutils.GLDrawer2D r0 = r0.mDrawer
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    int r1 = r1.mTexId
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r2 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r2 = r2.mTexMatrix
                    r0.draw(r1, r2, r3)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    android.graphics.Bitmap r0 = r0.watermark
                    if (r0 == 0) goto L_0x0132
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.effect.Effect r0 = r0.mEffect
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r1 = r1.mTexMatrix
                    r0.draw(r1)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.RenderScreen r0 = r0.mRenderScreen
                    if (r0 == 0) goto L_0x0132
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.RenderScreen r0 = r0.mRenderScreen
                    r0.draw()
                L_0x0132:
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    boolean r0 = r0.mRequestPause
                    if (r0 == 0) goto L_0x0176
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    android.graphics.Bitmap r0 = r0.pauseWatermark
                    if (r0 == 0) goto L_0x0176
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    java.lang.ref.WeakReference r0 = r0.mWeakMuxer
                    java.lang.Object r0 = r0.get()
                    com.appsmartz.recorder.MediaMuxerWrapper r0 = (com.appsmartz.recorder.MediaMuxerWrapper) r0
                    boolean r0 = r0.isLive()
                    if (r0 == 0) goto L_0x0176
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.effect.Effect r0 = r0.mEffect1
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r1 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    float[] r1 = r1.mTexMatrix
                    r0.draw(r1)
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.RenderScreen r0 = r0.mRenderScreen1
                    if (r0 == 0) goto L_0x0176
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.render.RenderScreen r0 = r0.mRenderScreen1
                    r0.draw()
                L_0x0176:
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r0 = r0.mEncoderSurface
                    r0.swap()
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    r0.makeCurrent()
                    r0 = 16384(0x4000, float:2.2959E-41)
                    android.opengl.GLES20.glClear(r0)
                    android.opengl.GLES20.glFlush()
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    com.appsmartz.recorder.MediaScreenEncoder r0 = com.appsmartz.recorder.MediaScreenEncoder.this
                    r0.frameAvailableSoon()
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    r0.queueEvent(r10)
                    goto L_0x019e
                L_0x0199:
                    com.appsmartz.recorder.MediaScreenEncoder$DrawTask r0 = com.appsmartz.recorder.MediaScreenEncoder.DrawTask.this
                    r0.releaseSelf()
                L_0x019e:
                    return
                L_0x019f:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x019f }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.MediaScreenEncoder.DrawTask.C05652.run():void");
            }
        };
        /* access modifiers changed from: private */
        public GLDrawer2D mDrawer;
        /* access modifiers changed from: private */
        public Effect mEffect;
        /* access modifiers changed from: private */
        public Effect mEffect1;
        private int mEffectTextureId;
        private int mEffectTextureId1;
        /* access modifiers changed from: private */
        public EGLBase.IEglSurface mEncoderSurface;
        private final OnFrameAvailableListener mOnFrameAvailableListener = new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (MediaScreenEncoder.this.mIsRecording) {
                    synchronized (MediaScreenEncoder.this.mSync) {
                        MediaScreenEncoder.this.requestDraw = true;
                        MediaScreenEncoder.this.mSync.notifyAll();
                    }
                }
            }
        };
        /* access modifiers changed from: private */
        public RenderScreen mRenderScreen;
        /* access modifiers changed from: private */
        public RenderScreen mRenderScreen1;
        /* access modifiers changed from: private */
        public Surface mSourceSurface;
        /* access modifiers changed from: private */
        public SurfaceTexture mSourceTexture;
        /* access modifiers changed from: private */
        public int mTexId;
        /* access modifiers changed from: private */
        public final float[] mTexMatrix = new float[16];

        /* access modifiers changed from: protected */
        public boolean onError(Exception exc) {
            return false;
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) {
            return null;
        }

        public DrawTask(EGLBase.IContext iContext, int i) {
            super(iContext, i);
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            this.mDrawer = new GLDrawer2D(true);
            this.mTexId = this.mDrawer.initTex();
            if (MediaScreenEncoder.this.watermark != null) {
                this.mEffect.setTextureId(this.mTexId);
                this.mEffect.prepare(MediaScreenEncoder.this.mHeight, MediaScreenEncoder.this.mWidth);
                this.mEffectTextureId = this.mEffect.getEffertedTextureId();
                this.mRenderScreen = new RenderScreen(this.mEffectTextureId);
                this.mRenderScreen.setSreenSize(MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight);
                this.mRenderScreen.setVideoSize(MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight);
                RenderScreen renderScreen = this.mRenderScreen;
                Watermark watermark = new Watermark(MediaScreenEncoder.this.watermark, MediaScreenEncoder.this.mWidth > MediaScreenEncoder.this.mHeight ? MediaScreenEncoder.this.mWidth / 4 : (int) (((double) MediaScreenEncoder.this.mWidth) / 2.5d), MediaScreenEncoder.this.mWidth > MediaScreenEncoder.this.mHeight ? MediaScreenEncoder.this.mHeight / 8 : (int) (((double) MediaScreenEncoder.this.mHeight) / 16.5d), 4, 8, 8);
                renderScreen.setWatermark(watermark);
            }
            if (MediaScreenEncoder.this.pauseWatermark != null) {
                this.mEffect1.setTexsettureId(this.mTexId);
                this.mEffect1.prepare(MediaScreenEncoder.this.mHeight, MediaScreenEncoder.this.mWidth);
                this.mEffectTextureId1 = this.mEffect1.getEffertedTextureId();
                this.mRenderScreen1 = new RenderScreen(this.mEffectTextureId1);
                this.mRenderScreen1.setSreenSize(MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight);
                this.mRenderScreen1.setVideoSize(MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight);
                RenderScreen renderScreen2 = this.mRenderScreen1;
                Watermark watermark2 = new Watermark(MediaScreenEncoder.this.pauseWatermark, MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight, 1, 0, 0);
                renderScreen2.setWatermark(watermark2);
            }
            this.mSourceTexture = new SurfaceTexture(this.mTexId);
            this.mSourceTexture.setDefaultBufferSize(MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight);
            this.mSourceSurface = new Surface(this.mSourceTexture);
            this.mSourceTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener, MediaScreenEncoder.this.mHandler);
            this.mEncoderSurface = getEgl().createFromSurface(MediaScreenEncoder.this.mSurface);
            this.intervals = (long) (1000.0f / ((float) MediaScreenEncoder.this.fps));
            this.display = MediaScreenEncoder.this.mMediaProjection.createVirtualDisplay("Capturing Display", MediaScreenEncoder.this.mWidth, MediaScreenEncoder.this.mHeight, MediaScreenEncoder.this.mDensity, 16, this.mSourceSurface, MediaScreenEncoder.this.mCallback, MediaScreenEncoder.this.mHandler);
            queueEvent(this.mDrawTask);
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
            Effect effect = this.mEffect;
            if (effect != null) {
                effect.release();
                this.mEffect = null;
            }
            Effect effect2 = this.mEffect1;
            if (effect2 != null) {
                effect2.release();
                this.mEffect1 = null;
            }
            Surface surface = this.mSourceSurface;
            if (surface != null) {
                surface.release();
                this.mSourceSurface = null;
            }
            SurfaceTexture surfaceTexture = this.mSourceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                this.mSourceTexture = null;
            }
            EGLBase.IEglSurface iEglSurface = this.mEncoderSurface;
            if (iEglSurface != null) {
                iEglSurface.release();
                this.mEncoderSurface = null;
            }
            makeCurrent();
            VirtualDisplay virtualDisplay = this.display;
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            if (MediaScreenEncoder.this.mMediaProjection != null) {
                MediaScreenEncoder.this.mMediaProjection.stop();
                MediaScreenEncoder.this.mMediaProjection = null;
            }
        }
    }

    public void setPauseWatermark(Bitmap bitmap) {
        this.pauseWatermark = bitmap;
    }

    public MediaScreenEncoder(MediaMuxerWrapper mediaMuxerWrapper, MediaEncoderListener mediaEncoderListener, MediaProjection mediaProjection, int i, int i2, int i3, int i4, int i5, int i6, Context context2, String str, String str2) {
        super(mediaMuxerWrapper, mediaEncoderListener, i, i2);
        mediaMuxerWrapper.setHeightWidth(i2, i);
        this.mMediaProjection = mediaProjection;
        this.context = context2;
        this.mDensity = i3;
        this.fps = (i5 <= 0 || i5 > 30) ? 25 : i5;
        if (i4 <= 0) {
            i4 = calcBitRate(i5);
        }
        this.bitrate = i4;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        this.startOrientation = i6;
        this.orientation = i6;
        this.orientationValue = str;
    }

    public void setWatermark(Bitmap bitmap) {
        this.watermark = bitmap;
    }

    /* access modifiers changed from: protected */
    public void release() {
        this.mHandler.getLooper().quit();
        super.release();
    }

    /* access modifiers changed from: 0000 */
    public void prepare() throws IOException {
        this.mSurface = prepare_surface_encoder(MIME_TYPE, this.fps, this.bitrate);
        this.mMediaCodec.start();
        this.mIsRecording = true;
        new Thread(this.mScreenCaptureTask, "ScreenCaptureThread").start();
        if (this.mListener != null) {
            try {
                this.mListener.onPrepared(this);
            } catch (Exception e) {
                Log.e(TAG, "prepare:", e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void stopRecording() {
        synchronized (this.mSync) {
            this.mIsRecording = false;
            this.mSync.notifyAll();
        }
        super.stopRecording();
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
        try {
            if (this.mIsRecording && this.orientationValue.equalsIgnoreCase("Auto") && this.mMediaProjection != null && this.mScreenCaptureTask != null && !((MediaMuxerWrapper) this.mWeakMuxer.get()).isLive()) {
                if (this.mScreenCaptureTask.display != null) {
                    this.mScreenCaptureTask.display.release();
                    this.mScreenCaptureTask.display = null;
                }
                if (i == this.startOrientation) {
                    this.mScreenCaptureTask.display = this.mMediaProjection.createVirtualDisplay("Capturing Display", this.mWidth, this.mHeight, this.mDensity, 16, this.mScreenCaptureTask.mSourceSurface, this.mCallback, this.mHandler);
                    this.mScreenCaptureTask.mSourceTexture.setDefaultBufferSize(this.mWidth, this.mHeight);
                    ((MediaMuxerWrapper) this.mWeakMuxer.get()).setHeightWidth(this.mHeight, this.mWidth);
                    return;
                }
                this.mScreenCaptureTask.display = this.mMediaProjection.createVirtualDisplay("Capturing Display", this.mHeight, this.mWidth, this.mDensity, 16, this.mScreenCaptureTask.mSourceSurface, this.mCallback, this.mHandler);
                this.mScreenCaptureTask.mSourceTexture.setDefaultBufferSize(this.mHeight, this.mWidth);
                ((MediaMuxerWrapper) this.mWeakMuxer.get()).setHeightWidth(this.mWidth, this.mHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
