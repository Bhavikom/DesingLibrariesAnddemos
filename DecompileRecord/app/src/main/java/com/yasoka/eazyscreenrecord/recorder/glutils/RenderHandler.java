package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;

@Deprecated
public final class RenderHandler extends Handler {
    private static final int MSG_CHECK_VALID = 3;
    private static final int MSG_RENDER_DRAW = 2;
    private static final int MSG_RENDER_QUIT = 9;
    private static final int MSG_RENDER_SET_GLCONTEXT = 1;
    private static final String TAG = "RenderHandler";
    private int mTexId;
    private final RenderThread mThread;

    private static final class ContextParams {
        final IContext sharedContext;
        final Object surface;

        public ContextParams(IContext iContext, Object obj) {
            this.sharedContext = iContext;
            this.surface = obj;
        }
    }

    private static final class RenderThread extends Thread {
        private static final String TAG_THREAD = "RenderThread";
        private GLDrawer2D mDrawer;
        private EGLBase mEgl;
        private RenderHandler mHandler;
        /* access modifiers changed from: private */
        public Surface mSurface;
        /* access modifiers changed from: private */
        public final Object mSync = new Object();
        private IEglSurface mTargetSurface;

        public RenderThread(String str) {
            super(str);
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000b */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final com.appsmartz.recorder.glutils.RenderHandler getHandler() {
            /*
                r2 = this;
                java.lang.Object r0 = r2.mSync
                monitor-enter(r0)
                java.lang.Object r1 = r2.mSync     // Catch:{ InterruptedException -> 0x000b }
                r1.wait()     // Catch:{ InterruptedException -> 0x000b }
                goto L_0x000b
            L_0x0009:
                r1 = move-exception
                goto L_0x000f
            L_0x000b:
                monitor-exit(r0)     // Catch:{ all -> 0x0009 }
                com.appsmartz.recorder.glutils.RenderHandler r0 = r2.mHandler
                return r0
            L_0x000f:
                monitor-exit(r0)     // Catch:{ all -> 0x0009 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.RenderHandler.RenderThread.getHandler():com.appsmartz.recorder.glutils.RenderHandler");
        }

        public final void handleSetEglContext(IContext iContext, Object obj, boolean z) {
            release();
            synchronized (this.mSync) {
                Surface surface = obj instanceof Surface ? (Surface) obj : obj instanceof SurfaceTexture ? new Surface((SurfaceTexture) obj) : null;
                this.mSurface = surface;
            }
            this.mEgl = EGLBase.createFrom(3, iContext, false, 0, z);
            try {
                this.mTargetSurface = this.mEgl.createFromSurface(obj);
                this.mDrawer = new GLDrawer2D(z);
            } catch (Exception e) {
                Log.w(RenderHandler.TAG, e);
                IEglSurface iEglSurface = this.mTargetSurface;
                if (iEglSurface != null) {
                    iEglSurface.release();
                    this.mTargetSurface = null;
                }
                GLDrawer2D gLDrawer2D = this.mDrawer;
                if (gLDrawer2D != null) {
                    gLDrawer2D.release();
                    this.mDrawer = null;
                }
            }
        }

        public void handleDraw(int i, float[] fArr) {
            if (i >= 0) {
                IEglSurface iEglSurface = this.mTargetSurface;
                if (iEglSurface != null) {
                    iEglSurface.makeCurrent();
                    this.mDrawer.draw(i, fArr, 0);
                    this.mTargetSurface.swap();
                }
            }
        }

        public final void run() {
            Looper.prepare();
            synchronized (this.mSync) {
                this.mHandler = new RenderHandler(this);
                this.mSync.notify();
            }
            Looper.loop();
            release();
            synchronized (this.mSync) {
                this.mHandler = null;
            }
        }

        private final void release() {
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
            synchronized (this.mSync) {
                this.mSurface = null;
            }
            if (this.mTargetSurface != null) {
                clear();
                this.mTargetSurface.release();
                this.mTargetSurface = null;
            }
            EGLBase eGLBase = this.mEgl;
            if (eGLBase != null) {
                eGLBase.release();
                this.mEgl = null;
            }
        }

        private final void clear() {
            this.mTargetSurface.makeCurrent();
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            this.mTargetSurface.swap();
        }
    }

    public static RenderHandler createHandler() {
        return createHandler("RenderThread");
    }

    public static final RenderHandler createHandler(String str) {
        RenderThread renderThread = new RenderThread(str);
        renderThread.start();
        return renderThread.getHandler();
    }

    public final void setEglContext(IContext iContext, int i, Object obj, boolean z) {
        if ((obj instanceof Surface) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceHolder)) {
            this.mTexId = i;
            sendMessage(obtainMessage(1, z ? 1 : 0, 0, new ContextParams(iContext, obj)));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unsupported window type:");
        sb.append(obj);
        throw new RuntimeException(sb.toString());
    }

    public final void draw() {
        sendMessage(obtainMessage(2, this.mTexId, 0, null));
    }

    public final void draw(int i) {
        sendMessage(obtainMessage(2, i, 0, null));
    }

    public final void draw(float[] fArr) {
        sendMessage(obtainMessage(2, this.mTexId, 0, fArr));
    }

    public final void draw(int i, float[] fArr) {
        sendMessage(obtainMessage(2, i, 0, fArr));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:2|3|4|5|6|7|8|(1:13)(1:12)|14) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0014 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isValid() {
        /*
            r2 = this;
            com.appsmartz.recorder.glutils.RenderHandler$RenderThread r0 = r2.mThread
            java.lang.Object r0 = r0.mSync
            monitor-enter(r0)
            r1 = 3
            r2.sendEmptyMessage(r1)     // Catch:{ all -> 0x002d }
            com.appsmartz.recorder.glutils.RenderHandler$RenderThread r1 = r2.mThread     // Catch:{ InterruptedException -> 0x0014 }
            java.lang.Object r1 = r1.mSync     // Catch:{ InterruptedException -> 0x0014 }
            r1.wait()     // Catch:{ InterruptedException -> 0x0014 }
        L_0x0014:
            com.appsmartz.recorder.glutils.RenderHandler$RenderThread r1 = r2.mThread     // Catch:{ all -> 0x002d }
            android.view.Surface r1 = r1.mSurface     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x002a
            com.appsmartz.recorder.glutils.RenderHandler$RenderThread r1 = r2.mThread     // Catch:{ all -> 0x002d }
            android.view.Surface r1 = r1.mSurface     // Catch:{ all -> 0x002d }
            boolean r1 = r1.isValid()     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x002a
            r1 = 1
            goto L_0x002b
        L_0x002a:
            r1 = 0
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r1
        L_0x002d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.RenderHandler.isValid():boolean");
    }

    public final void release() {
        removeMessages(1);
        removeMessages(2);
        sendEmptyMessage(9);
    }

    public final void handleMessage(Message message) {
        int i = message.what;
        boolean z = true;
        if (i == 1) {
            ContextParams contextParams = (ContextParams) message.obj;
            RenderThread renderThread = this.mThread;
            IContext iContext = contextParams.sharedContext;
            Object obj = contextParams.surface;
            if (message.arg1 == 0) {
                z = false;
            }
            renderThread.handleSetEglContext(iContext, obj, z);
        } else if (i == 2) {
            this.mThread.handleDraw(message.arg1, (float[]) message.obj);
        } else if (i == 3) {
            synchronized (this.mThread.mSync) {
                this.mThread.mSync.notify();
            }
        } else if (i != 9) {
            super.handleMessage(message);
        } else {
            Looper.myLooper().quit();
        }
    }

    private RenderHandler(RenderThread renderThread) {
        this.mTexId = -1;
        this.mThread = renderThread;
    }
}
