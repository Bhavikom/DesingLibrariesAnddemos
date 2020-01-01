package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;
import com.appsmartz.recorder.utils.BuildCheck;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public abstract class AbstractRendererHolder implements IRendererHolder {
    private static final String CAPTURE_THREAD_NAME = "CaptureTask";
    private static final boolean DEBUG = false;
    private static final String RENDERER_THREAD_NAME = "RendererHolder";
    protected static final int REQUEST_ADD_SURFACE = 3;
    protected static final int REQUEST_CLEAR = 8;
    protected static final int REQUEST_CLEAR_ALL = 9;
    protected static final int REQUEST_DRAW = 1;
    protected static final int REQUEST_MIRROR = 6;
    protected static final int REQUEST_RECREATE_MASTER_SURFACE = 5;
    protected static final int REQUEST_REMOVE_SURFACE = 4;
    protected static final int REQUEST_REMOVE_SURFACE_ALL = 12;
    protected static final int REQUEST_ROTATE = 7;
    protected static final int REQUEST_SET_MVP = 10;
    protected static final int REQUEST_UPDATE_SIZE = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "AbstractRendererHolder";
    /* access modifiers changed from: private */
    public volatile boolean isRunning;
    @Nullable
    private final RenderHolderCallback mCallback;
    /* access modifiers changed from: private */
    @IntRange(from = 1, mo116to = 99)
    public int mCaptureCompression;
    /* access modifiers changed from: private */
    public int mCaptureFormat;
    /* access modifiers changed from: private */
    public OutputStream mCaptureStream;
    private final Runnable mCaptureTask;
    protected final RendererTask mRendererTask;
    protected final Object mSync;

    protected static abstract class BaseRendererTask extends EglTask {
        private final SparseArray<RendererSurfaceRec> mClients;
        /* access modifiers changed from: private */
        public volatile boolean mIsFirstFrameRendered;
        private Surface mMasterSurface;
        private SurfaceTexture mMasterTexture;
        private int mMirror;
        protected final OnFrameAvailableListener mOnFrameAvailableListener;
        private final AbstractRendererHolder mParent;
        private int mRotation;
        int mTexId;
        final float[] mTexMatrix;
        private int mVideoHeight;
        private int mVideoWidth;

        /* access modifiers changed from: protected */
        public void handleRotate(int i, int i2) {
        }

        /* access modifiers changed from: protected */
        public abstract void internalOnStart();

        /* access modifiers changed from: protected */
        public abstract void internalOnStop();

        /* access modifiers changed from: protected */
        public abstract void onDrawClient(@NonNull RendererSurfaceRec rendererSurfaceRec, int i, float[] fArr);

        /* access modifiers changed from: protected */
        public boolean onError(Exception exc) {
            return false;
        }

        /* access modifiers changed from: protected */
        public abstract void preprocess();

        public BaseRendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2) {
            this(abstractRendererHolder, i, i2, 3, null, 2);
        }

        public BaseRendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, IContext iContext, int i4) {
            super(i3, iContext, i4);
            this.mClients = new SparseArray<>();
            this.mTexMatrix = new float[16];
            this.mMirror = 0;
            this.mRotation = 0;
            this.mOnFrameAvailableListener = new OnFrameAvailableListener() {
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    BaseRendererTask.this.removeRequest(1);
                    BaseRendererTask.this.mIsFirstFrameRendered = true;
                    BaseRendererTask.this.offer(1);
                }
            };
            this.mParent = abstractRendererHolder;
            if (i <= 0) {
                i = 640;
            }
            this.mVideoWidth = i;
            if (i2 <= 0) {
                i2 = 480;
            }
            this.mVideoHeight = i2;
        }

        /* access modifiers changed from: protected */
        public final void onStart() {
            handleReCreateMasterSurface();
            internalOnStart();
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = true;
                this.mParent.mSync.notifyAll();
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = false;
                this.mParent.mSync.notifyAll();
            }
            makeCurrent();
            internalOnStop();
            handleReleaseMasterSurface();
            handleRemoveAll();
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) {
            switch (i) {
                case 1:
                    handleDraw();
                    break;
                case 2:
                    handleResize(i2, i3);
                    break;
                case 3:
                    handleAddSurface(i2, obj, i3);
                    break;
                case 4:
                    handleRemoveSurface(i2);
                    break;
                case 5:
                    handleReCreateMasterSurface();
                    break;
                case 6:
                    handleMirror(i2);
                    break;
                case 7:
                    handleRotate(i2, i3);
                    break;
                case 8:
                    handleClear(i2, i3);
                    break;
                case 9:
                    handleClearAll(i2);
                    break;
                case 10:
                    handleSetMvp(i2, i3, obj);
                    break;
                case 12:
                    handleRemoveAll();
                    break;
            }
            return null;
        }

        public Surface getSurface() {
            checkMasterSurface();
            return this.mMasterSurface;
        }

        public SurfaceTexture getSurfaceTexture() {
            checkMasterSurface();
            return this.mMasterTexture;
        }

        public void addSurface(int i, Object obj) throws IllegalStateException, IllegalArgumentException {
            addSurface(i, obj, -1);
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:10|11|(1:(2:13|(2:15|(2:17|18)(1:19))(0)))(0)|20|21) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x003e */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void addSurface(int r5, Object r6, int r7) throws IllegalStateException, IllegalArgumentException {
            /*
                r4 = this;
                r4.checkFinished()
                boolean r0 = r6 instanceof android.graphics.SurfaceTexture
                if (r0 != 0) goto L_0x0018
                boolean r0 = r6 instanceof android.view.Surface
                if (r0 != 0) goto L_0x0018
                boolean r0 = r6 instanceof android.view.SurfaceHolder
                if (r0 == 0) goto L_0x0010
                goto L_0x0018
            L_0x0010:
                java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
                java.lang.String r6 = "Surface should be one of Surface, SurfaceTexture or SurfaceHolder"
                r5.<init>(r6)
                throw r5
            L_0x0018:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ all -> 0x0040 }
                java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0040 }
                if (r1 != 0) goto L_0x003e
            L_0x0023:
                boolean r1 = r4.isRunning()     // Catch:{ all -> 0x0040 }
                if (r1 == 0) goto L_0x003e
                r1 = 3
                boolean r1 = r4.offer(r1, r5, r7, r6)     // Catch:{ all -> 0x0040 }
                if (r1 == 0) goto L_0x0036
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r5 = r4.mClients     // Catch:{ InterruptedException -> 0x003e }
                r5.wait()     // Catch:{ InterruptedException -> 0x003e }
                goto L_0x003e
            L_0x0036:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ InterruptedException -> 0x003e }
                r2 = 5
                r1.wait(r2)     // Catch:{ InterruptedException -> 0x003e }
                goto L_0x0023
            L_0x003e:
                monitor-exit(r0)     // Catch:{ all -> 0x0040 }
                return
            L_0x0040:
                r5 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0040 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.BaseRendererTask.addSurface(int, java.lang.Object, int):void");
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(1:(2:5|(2:7|(2:9|10)(1:11))(0)))(0)|12|13) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0026 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void removeSurface(int r5) {
            /*
                r4 = this;
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ all -> 0x0028 }
                java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0028 }
                if (r1 == 0) goto L_0x0026
            L_0x000b:
                boolean r1 = r4.isRunning()     // Catch:{ all -> 0x0028 }
                if (r1 == 0) goto L_0x0026
                r1 = 4
                boolean r1 = r4.offer(r1, r5)     // Catch:{ all -> 0x0028 }
                if (r1 == 0) goto L_0x001e
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r5 = r4.mClients     // Catch:{ InterruptedException -> 0x0026 }
                r5.wait()     // Catch:{ InterruptedException -> 0x0026 }
                goto L_0x0026
            L_0x001e:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ InterruptedException -> 0x0026 }
                r2 = 5
                r1.wait(r2)     // Catch:{ InterruptedException -> 0x0026 }
                goto L_0x000b
            L_0x0026:
                monitor-exit(r0)     // Catch:{ all -> 0x0028 }
                return
            L_0x0028:
                r5 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0028 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.BaseRendererTask.removeSurface(int):void");
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void removeSurfaceAll() {
            /*
                r4 = this;
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r0 = r4.mClients
                monitor-enter(r0)
            L_0x0003:
                boolean r1 = r4.isRunning()     // Catch:{ all -> 0x0021 }
                if (r1 == 0) goto L_0x001f
                r1 = 12
                boolean r1 = r4.offer(r1)     // Catch:{ all -> 0x0021 }
                if (r1 == 0) goto L_0x0017
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ InterruptedException -> 0x001f }
                r1.wait()     // Catch:{ InterruptedException -> 0x001f }
                goto L_0x001f
            L_0x0017:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ InterruptedException -> 0x001f }
                r2 = 5
                r1.wait(r2)     // Catch:{ InterruptedException -> 0x001f }
                goto L_0x0003
            L_0x001f:
                monitor-exit(r0)     // Catch:{ all -> 0x0021 }
                return
            L_0x0021:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0021 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.BaseRendererTask.removeSurfaceAll():void");
        }

        public void clearSurface(int i, int i2) {
            checkFinished();
            offer(8, i, i2);
        }

        public void clearSurfaceAll(int i) {
            checkFinished();
            offer(9, i);
        }

        public void setMvpMatrix(int i, int i2, @NonNull float[] fArr) {
            checkFinished();
            offer(10, i, i2, fArr);
        }

        public boolean isEnabled(int i) {
            boolean z;
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                z = rendererSurfaceRec != null && rendererSurfaceRec.isEnabled();
            }
            return z;
        }

        public void setEnabled(int i, boolean z) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    rendererSurfaceRec.setEnabled(z);
                }
            }
        }

        public int getCount() {
            int size;
            synchronized (this.mClients) {
                size = this.mClients.size();
            }
            return size;
        }

        public void resize(int i, int i2) throws IllegalStateException {
            checkFinished();
            if (i > 0 && i2 > 0) {
                if (this.mVideoWidth != i || this.mVideoHeight != i2) {
                    offer(2, i, i2);
                }
            }
        }

        /* access modifiers changed from: protected */
        public int width() {
            return this.mVideoWidth;
        }

        /* access modifiers changed from: protected */
        public int height() {
            return this.mVideoHeight;
        }

        public void mirror(int i) {
            checkFinished();
            if (this.mMirror != i) {
                offer(6, i);
            }
        }

        public int mirror() {
            return this.mMirror;
        }

        public void checkMasterSurface() {
            checkFinished();
            Surface surface = this.mMasterSurface;
            if (surface == null || !surface.isValid()) {
                Log.d(AbstractRendererHolder.TAG, "checkMasterSurface:invalid master surface");
                offerAndWait(5, 0, 0, null);
            }
        }

        /* access modifiers changed from: protected */
        public void checkFinished() throws IllegalStateException {
            if (isFinished()) {
                throw new IllegalStateException("already finished");
            }
        }

        /* access modifiers changed from: protected */
        public AbstractRendererHolder getParent() {
            return this.mParent;
        }

        /* access modifiers changed from: protected */
        public void handleDraw() {
            Surface surface = this.mMasterSurface;
            if (surface == null || !surface.isValid()) {
                Log.e(AbstractRendererHolder.TAG, "checkMasterSurface:invalid master surface");
                offer(5);
                return;
            }
            if (this.mIsFirstFrameRendered) {
                try {
                    makeCurrent();
                    handleUpdateTexture();
                    this.mParent.notifyCapture();
                    preprocess();
                    handleDrawClients();
                    this.mParent.callOnFrameAvailable();
                } catch (Exception e) {
                    String access$100 = AbstractRendererHolder.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("draw:thread id =");
                    sb.append(Thread.currentThread().getId());
                    Log.e(access$100, sb.toString(), e);
                    offer(5);
                    return;
                }
            }
            makeCurrent();
            GLES20.glClear(16384);
            GLES20.glFlush();
        }

        /* access modifiers changed from: protected */
        public void handleUpdateTexture() {
            this.mMasterTexture.updateTexImage();
            this.mMasterTexture.getTransformMatrix(this.mTexMatrix);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Can't wrap try/catch for region: R(5:9|10|11|12|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x002d, code lost:
            continue;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0025 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleDrawClients() {
            /*
                r5 = this;
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r0 = r5.mClients
                monitor-enter(r0)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r5.mClients     // Catch:{ all -> 0x0032 }
                int r1 = r1.size()     // Catch:{ all -> 0x0032 }
                int r1 = r1 + -1
            L_0x000b:
                if (r1 < 0) goto L_0x0030
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r2 = r5.mClients     // Catch:{ all -> 0x0032 }
                java.lang.Object r2 = r2.valueAt(r1)     // Catch:{ all -> 0x0032 }
                com.appsmartz.recorder.glutils.RendererSurfaceRec r2 = (com.appsmartz.recorder.glutils.RendererSurfaceRec) r2     // Catch:{ all -> 0x0032 }
                if (r2 == 0) goto L_0x002d
                boolean r3 = r2.canDraw()     // Catch:{ all -> 0x0032 }
                if (r3 == 0) goto L_0x002d
                int r3 = r5.mTexId     // Catch:{ Exception -> 0x0025 }
                float[] r4 = r5.mTexMatrix     // Catch:{ Exception -> 0x0025 }
                r5.onDrawClient(r2, r3, r4)     // Catch:{ Exception -> 0x0025 }
                goto L_0x002d
            L_0x0025:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r3 = r5.mClients     // Catch:{ all -> 0x0032 }
                r3.removeAt(r1)     // Catch:{ all -> 0x0032 }
                r2.release()     // Catch:{ all -> 0x0032 }
            L_0x002d:
                int r1 = r1 + -1
                goto L_0x000b
            L_0x0030:
                monitor-exit(r0)     // Catch:{ all -> 0x0032 }
                return
            L_0x0032:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0032 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.BaseRendererTask.handleDrawClients():void");
        }

        /* access modifiers changed from: protected */
        public void handleAddSurface(int i, Object obj, int i2) {
            checkSurface();
            synchronized (this.mClients) {
                if (((RendererSurfaceRec) this.mClients.get(i)) == null) {
                    try {
                        RendererSurfaceRec newInstance = RendererSurfaceRec.newInstance(getEgl(), obj, i2);
                        setMirror(newInstance, this.mMirror);
                        this.mClients.append(i, newInstance);
                    } catch (Exception e) {
                        String access$100 = AbstractRendererHolder.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("invalid surface: surface=");
                        sb.append(obj);
                        Log.w(access$100, sb.toString(), e);
                    }
                } else {
                    String access$1002 = AbstractRendererHolder.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("surface is already added: id=");
                    sb2.append(i);
                    Log.w(access$1002, sb2.toString());
                }
                this.mClients.notifyAll();
            }
        }

        /* access modifiers changed from: protected */
        public void handleRemoveSurface(int i) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    this.mClients.remove(i);
                    if (rendererSurfaceRec.isValid()) {
                        rendererSurfaceRec.clear(0);
                    }
                    rendererSurfaceRec.release();
                }
                checkSurface();
                this.mClients.notifyAll();
            }
        }

        /* access modifiers changed from: protected */
        public void handleRemoveAll() {
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.valueAt(i);
                    if (rendererSurfaceRec != null) {
                        if (rendererSurfaceRec.isValid()) {
                            rendererSurfaceRec.clear(0);
                        }
                        rendererSurfaceRec.release();
                    }
                }
                this.mClients.clear();
                this.mClients.notifyAll();
            }
        }

        /* access modifiers changed from: protected */
        public void checkSurface() {
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.valueAt(i);
                    if (rendererSurfaceRec != null && !rendererSurfaceRec.isValid()) {
                        int keyAt = this.mClients.keyAt(i);
                        ((RendererSurfaceRec) this.mClients.valueAt(i)).release();
                        this.mClients.remove(keyAt);
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void handleClear(int i, int i2) {
            synchronized (this.mClients) {
                RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                if (rendererSurfaceRec != null && rendererSurfaceRec.isValid()) {
                    rendererSurfaceRec.clear(i2);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void handleClearAll(int i) {
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i2 = 0; i2 < size; i2++) {
                    RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.valueAt(i2);
                    if (rendererSurfaceRec != null && rendererSurfaceRec.isValid()) {
                        rendererSurfaceRec.clear(i);
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void handleSetMvp(int i, int i2, Object obj) {
            if (obj instanceof float[]) {
                float[] fArr = (float[]) obj;
                if (fArr.length >= i2 + 16) {
                    synchronized (this.mClients) {
                        RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                        if (rendererSurfaceRec != null && rendererSurfaceRec.isValid()) {
                            System.arraycopy(fArr, i2, rendererSurfaceRec.mMvpMatrix, 0, 16);
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"NewApi"})
        public void handleReCreateMasterSurface() {
            makeCurrent();
            handleReleaseMasterSurface();
            makeCurrent();
            this.mTexId = GLHelper.initTex(ShaderConst.GL_TEXTURE_EXTERNAL_OES, 9728);
            this.mMasterTexture = new SurfaceTexture(this.mTexId);
            this.mMasterSurface = new Surface(this.mMasterTexture);
            if (BuildCheck.isAndroid4_1()) {
                this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);
            }
            this.mMasterTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener);
            this.mParent.callOnCreate(this.mMasterSurface);
        }

        /* access modifiers changed from: protected */
        public void handleReleaseMasterSurface() {
            Surface surface = this.mMasterSurface;
            if (surface != null) {
                try {
                    surface.release();
                } catch (Exception e) {
                    Log.w(AbstractRendererHolder.TAG, e);
                }
                this.mMasterSurface = null;
                this.mParent.callOnDestroy();
            }
            SurfaceTexture surfaceTexture = this.mMasterTexture;
            if (surfaceTexture != null) {
                try {
                    surfaceTexture.release();
                } catch (Exception e2) {
                    Log.w(AbstractRendererHolder.TAG, e2);
                }
                this.mMasterTexture = null;
            }
            int i = this.mTexId;
            if (i != 0) {
                GLHelper.deleteTex(i);
                this.mTexId = 0;
            }
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"NewApi"})
        public void handleResize(int i, int i2) {
            this.mVideoWidth = i;
            this.mVideoHeight = i2;
            if (BuildCheck.isAndroid4_1()) {
                this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);
            }
        }

        /* access modifiers changed from: protected */
        public void handleMirror(int i) {
            this.mMirror = i;
            synchronized (this.mClients) {
                int size = this.mClients.size();
                for (int i2 = 0; i2 < size; i2++) {
                    RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.valueAt(i2);
                    if (rendererSurfaceRec != null) {
                        setMirror(rendererSurfaceRec, i);
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void setMirror(RendererSurfaceRec rendererSurfaceRec, int i) {
            RendererHolder.setMirror(rendererSurfaceRec.mMvpMatrix, i);
        }
    }

    protected static abstract class RendererTask extends BaseRendererTask {
        protected GLDrawer2D mDrawer;

        /* access modifiers changed from: protected */
        public void preprocess() {
        }

        public RendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2) {
            super(abstractRendererHolder, i, i2);
        }

        public RendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, IContext iContext, int i4) {
            super(abstractRendererHolder, i, i2, i3, iContext, i4);
        }

        /* access modifiers changed from: protected */
        public void internalOnStart() {
            this.mDrawer = new GLDrawer2D(true);
        }

        /* access modifiers changed from: protected */
        public void internalOnStop() {
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
        }

        /* access modifiers changed from: protected */
        public void onDrawClient(@NonNull RendererSurfaceRec rendererSurfaceRec, int i, float[] fArr) {
            rendererSurfaceRec.draw(this.mDrawer, i, fArr);
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public abstract RendererTask createRendererTask(int i, int i2, int i3, IContext iContext, int i4);

    /* access modifiers changed from: protected */
    public void setupCaptureDrawer(GLDrawer2D gLDrawer2D) {
    }

    protected AbstractRendererHolder(int i, int i2, @Nullable RenderHolderCallback renderHolderCallback) {
        this(i, i2, 3, null, 2, renderHolderCallback);
    }

    protected AbstractRendererHolder(int i, int i2, int i3, IContext iContext, int i4, @Nullable RenderHolderCallback renderHolderCallback) {
        this.mSync = new Object();
        this.mCaptureCompression = 80;
        this.mCaptureTask = new Runnable() {
            IEglSurface captureSurface;
            GLDrawer2D drawer;
            EGLBase eglBase;
            final float[] mMvpMatrix = new float[16];

            /* JADX WARNING: Can't wrap try/catch for region: R(3:(4:7|8|3|2)|9|10) */
            /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0021 */
            /* JADX WARNING: Removed duplicated region for block: B:13:0x002a  */
            /* JADX WARNING: Removed duplicated region for block: B:36:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r4 = this;
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    java.lang.Object r0 = r0.mSync
                    monitor-enter(r0)
                L_0x0005:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r1 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0057 }
                    boolean r1 = r1.isRunning     // Catch:{ all -> 0x0057 }
                    if (r1 != 0) goto L_0x0021
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r1 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0057 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r1 = r1.mRendererTask     // Catch:{ all -> 0x0057 }
                    boolean r1 = r1.isFinished()     // Catch:{ all -> 0x0057 }
                    if (r1 != 0) goto L_0x0021
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r1 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ InterruptedException -> 0x0021 }
                    java.lang.Object r1 = r1.mSync     // Catch:{ InterruptedException -> 0x0021 }
                    r2 = 1000(0x3e8, double:4.94E-321)
                    r1.wait(r2)     // Catch:{ InterruptedException -> 0x0021 }
                    goto L_0x0005
                L_0x0021:
                    monitor-exit(r0)     // Catch:{ all -> 0x0057 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    boolean r0 = r0.isRunning
                    if (r0 == 0) goto L_0x0056
                    r4.init()
                    com.appsmartz.recorder.glutils.EGLBase r0 = r4.eglBase     // Catch:{ Exception -> 0x0046 }
                    int r0 = r0.getGlVersion()     // Catch:{ Exception -> 0x0046 }
                    r1 = 2
                    if (r0 <= r1) goto L_0x0040
                    boolean r0 = com.appsmartz.recorder.utils.BuildCheck.isAndroid4_3()     // Catch:{ Exception -> 0x0046 }
                    if (r0 == 0) goto L_0x0040
                    r4.captureLoopGLES3()     // Catch:{ Exception -> 0x0046 }
                    goto L_0x004e
                L_0x0040:
                    r4.captureLoopGLES2()     // Catch:{ Exception -> 0x0046 }
                    goto L_0x004e
                L_0x0044:
                    r0 = move-exception
                    goto L_0x0052
                L_0x0046:
                    r0 = move-exception
                    java.lang.String r1 = com.appsmartz.recorder.glutils.AbstractRendererHolder.TAG     // Catch:{ all -> 0x0044 }
                    android.util.Log.w(r1, r0)     // Catch:{ all -> 0x0044 }
                L_0x004e:
                    r4.release()
                    goto L_0x0056
                L_0x0052:
                    r4.release()
                    throw r0
                L_0x0056:
                    return
                L_0x0057:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0057 }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.C05691.run():void");
            }

            private final void init() {
                this.eglBase = EGLBase.createFrom(3, AbstractRendererHolder.this.mRendererTask.getContext(), false, 0, false);
                this.captureSurface = this.eglBase.createOffscreen(AbstractRendererHolder.this.mRendererTask.width(), AbstractRendererHolder.this.mRendererTask.height());
                Matrix.setIdentityM(this.mMvpMatrix, 0);
                this.drawer = new GLDrawer2D(true);
                AbstractRendererHolder.this.setupCaptureDrawer(this.drawer);
            }

            /* JADX WARNING: Removed duplicated region for block: B:51:0x0136 A[Catch:{ InterruptedException -> 0x003e }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private final void captureLoopGLES2() {
                /*
                    r15 = this;
                    r0 = -1
                    r1 = 0
                    r2 = 80
                    r0 = r1
                    r2 = -1
                    r3 = -1
                    r4 = 80
                L_0x0009:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r5 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    boolean r5 = r5.isRunning
                    if (r5 == 0) goto L_0x0155
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r5 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    java.lang.Object r5 = r5.mSync
                    monitor-enter(r5)
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    java.io.OutputStream r6 = r6.mCaptureStream     // Catch:{ all -> 0x0152 }
                    if (r6 != 0) goto L_0x0041
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ InterruptedException -> 0x003e }
                    java.lang.Object r6 = r6.mSync     // Catch:{ InterruptedException -> 0x003e }
                    r6.wait()     // Catch:{ InterruptedException -> 0x003e }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    java.io.OutputStream r6 = r6.mCaptureStream     // Catch:{ all -> 0x0152 }
                    if (r6 == 0) goto L_0x003c
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r4 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    int r4 = r4.mCaptureCompression     // Catch:{ all -> 0x0152 }
                    if (r4 <= 0) goto L_0x0039
                    r6 = 100
                    if (r4 < r6) goto L_0x0041
                L_0x0039:
                    r4 = 90
                    goto L_0x0041
                L_0x003c:
                    monitor-exit(r5)     // Catch:{ all -> 0x0152 }
                    goto L_0x0009
                L_0x003e:
                    monitor-exit(r5)     // Catch:{ all -> 0x0152 }
                    goto L_0x0155
                L_0x0041:
                    if (r0 == 0) goto L_0x005d
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r6 = r6.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r6 = r6.width()     // Catch:{ all -> 0x0152 }
                    if (r2 != r6) goto L_0x005d
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r6 = r6.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r6 = r6.height()     // Catch:{ all -> 0x0152 }
                    if (r3 == r6) goto L_0x0058
                    goto L_0x005d
                L_0x0058:
                    r14 = r3
                    r3 = r0
                    r0 = r2
                    r2 = r14
                    goto L_0x008d
                L_0x005d:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r0 = r0.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r0 = r0.width()     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r2 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r2 = r2.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r2 = r2.height()     // Catch:{ all -> 0x0152 }
                    int r3 = r0 * r2
                    int r3 = r3 * 4
                    java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocateDirect(r3)     // Catch:{ all -> 0x0152 }
                    java.nio.ByteOrder r6 = java.nio.ByteOrder.LITTLE_ENDIAN     // Catch:{ all -> 0x0152 }
                    r3.order(r6)     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r6 = r15.captureSurface     // Catch:{ all -> 0x0152 }
                    if (r6 == 0) goto L_0x0085
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r6 = r15.captureSurface     // Catch:{ all -> 0x0152 }
                    r6.release()     // Catch:{ all -> 0x0152 }
                    r15.captureSurface = r1     // Catch:{ all -> 0x0152 }
                L_0x0085:
                    com.appsmartz.recorder.glutils.EGLBase r6 = r15.eglBase     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r6 = r6.createOffscreen(r0, r2)     // Catch:{ all -> 0x0152 }
                    r15.captureSurface = r6     // Catch:{ all -> 0x0152 }
                L_0x008d:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    boolean r6 = r6.isRunning     // Catch:{ all -> 0x0152 }
                    if (r6 == 0) goto L_0x012e
                    if (r0 <= 0) goto L_0x012e
                    if (r2 <= 0) goto L_0x012e
                    float[] r6 = r15.mMvpMatrix     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r7 = r7.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r7 = r7.mirror()     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder.setMirror(r6, r7)     // Catch:{ all -> 0x0152 }
                    float[] r6 = r15.mMvpMatrix     // Catch:{ all -> 0x0152 }
                    r7 = 5
                    r8 = r6[r7]     // Catch:{ all -> 0x0152 }
                    r9 = -1082130432(0xffffffffbf800000, float:-1.0)
                    float r8 = r8 * r9
                    r6[r7] = r8     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.GLDrawer2D r6 = r15.drawer     // Catch:{ all -> 0x0152 }
                    float[] r7 = r15.mMvpMatrix     // Catch:{ all -> 0x0152 }
                    r8 = 0
                    r6.setMvpMatrix(r7, r8)     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r6 = r15.captureSurface     // Catch:{ all -> 0x0152 }
                    r6.makeCurrent()     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.GLDrawer2D r6 = r15.drawer     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r7 = r7.mRendererTask     // Catch:{ all -> 0x0152 }
                    int r7 = r7.mTexId     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r9 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r9 = r9.mRendererTask     // Catch:{ all -> 0x0152 }
                    float[] r9 = r9.mTexMatrix     // Catch:{ all -> 0x0152 }
                    r6.draw(r7, r9, r8)     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r6 = r15.captureSurface     // Catch:{ all -> 0x0152 }
                    r6.swap()     // Catch:{ all -> 0x0152 }
                    r3.clear()     // Catch:{ all -> 0x0152 }
                    r7 = 0
                    r8 = 0
                    r11 = 6408(0x1908, float:8.98E-42)
                    r12 = 5121(0x1401, float:7.176E-42)
                    r9 = r0
                    r10 = r2
                    r13 = r3
                    android.opengl.GLES20.glReadPixels(r7, r8, r9, r10, r11, r12, r13)     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    int r6 = r6.mCaptureFormat     // Catch:{ all -> 0x0152 }
                    android.graphics.Bitmap$CompressFormat r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.getCaptureFormat(r6)     // Catch:{ all -> 0x0152 }
                    android.graphics.Bitmap$Config r7 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ all -> 0x0118 }
                    android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r0, r2, r7)     // Catch:{ all -> 0x0118 }
                    r3.clear()     // Catch:{ all -> 0x0118 }
                    r7.copyPixelsFromBuffer(r3)     // Catch:{ all -> 0x0118 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0118 }
                    java.io.OutputStream r8 = r8.mCaptureStream     // Catch:{ all -> 0x0118 }
                    r7.compress(r6, r4, r8)     // Catch:{ all -> 0x0118 }
                    r7.recycle()     // Catch:{ all -> 0x0118 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0118 }
                    java.io.OutputStream r6 = r6.mCaptureStream     // Catch:{ all -> 0x0118 }
                    r6.flush()     // Catch:{ all -> 0x0118 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ IOException -> 0x0123 }
                    java.io.OutputStream r6 = r6.mCaptureStream     // Catch:{ IOException -> 0x0123 }
                    r6.close()     // Catch:{ IOException -> 0x0123 }
                    goto L_0x013f
                L_0x0118:
                    r6 = move-exception
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ IOException -> 0x0123 }
                    java.io.OutputStream r7 = r7.mCaptureStream     // Catch:{ IOException -> 0x0123 }
                    r7.close()     // Catch:{ IOException -> 0x0123 }
                    throw r6     // Catch:{ IOException -> 0x0123 }
                L_0x0123:
                    r6 = move-exception
                    java.lang.String r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.TAG     // Catch:{ all -> 0x0152 }
                    java.lang.String r8 = "failed to save file"
                    android.util.Log.w(r7, r8, r6)     // Catch:{ all -> 0x0152 }
                    goto L_0x013f
                L_0x012e:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    boolean r6 = r6.isRunning     // Catch:{ all -> 0x0152 }
                    if (r6 == 0) goto L_0x013f
                    java.lang.String r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.TAG     // Catch:{ all -> 0x0152 }
                    java.lang.String r7 = "#captureLoopGLES3:unexpectedly width/height is zero"
                    android.util.Log.w(r6, r7)     // Catch:{ all -> 0x0152 }
                L_0x013f:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    r6.mCaptureStream = r1     // Catch:{ all -> 0x0152 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0152 }
                    java.lang.Object r6 = r6.mSync     // Catch:{ all -> 0x0152 }
                    r6.notifyAll()     // Catch:{ all -> 0x0152 }
                    monitor-exit(r5)     // Catch:{ all -> 0x0152 }
                    r14 = r2
                    r2 = r0
                    r0 = r3
                    r3 = r14
                    goto L_0x0009
                L_0x0152:
                    r0 = move-exception
                    monitor-exit(r5)     // Catch:{ all -> 0x0152 }
                    throw r0
                L_0x0155:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    java.lang.Object r0 = r0.mSync
                    monitor-enter(r0)
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r1 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0163 }
                    java.lang.Object r1 = r1.mSync     // Catch:{ all -> 0x0163 }
                    r1.notifyAll()     // Catch:{ all -> 0x0163 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0163 }
                    return
                L_0x0163:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0163 }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.C05691.captureLoopGLES2():void");
            }

            /* JADX WARNING: Removed duplicated region for block: B:51:0x0138 A[Catch:{ InterruptedException -> 0x0040 }] */
            @android.annotation.TargetApi(18)
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private final void captureLoopGLES3() {
                /*
                    r16 = this;
                    r1 = r16
                    r2 = 90
                    r0 = -1
                    r3 = 0
                    r0 = r3
                    r4 = -1
                    r5 = -1
                    r6 = 90
                L_0x000b:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    boolean r7 = r7.isRunning
                    if (r7 == 0) goto L_0x0154
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r7 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    java.lang.Object r7 = r7.mSync
                    monitor-enter(r7)
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    java.io.OutputStream r8 = r8.mCaptureStream     // Catch:{ all -> 0x0151 }
                    if (r8 != 0) goto L_0x0043
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ InterruptedException -> 0x0040 }
                    java.lang.Object r8 = r8.mSync     // Catch:{ InterruptedException -> 0x0040 }
                    r8.wait()     // Catch:{ InterruptedException -> 0x0040 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    java.io.OutputStream r8 = r8.mCaptureStream     // Catch:{ all -> 0x0151 }
                    if (r8 == 0) goto L_0x003e
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r6 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    int r6 = r6.mCaptureCompression     // Catch:{ all -> 0x0151 }
                    if (r6 <= 0) goto L_0x003b
                    r8 = 100
                    if (r6 < r8) goto L_0x0043
                L_0x003b:
                    r6 = 90
                    goto L_0x0043
                L_0x003e:
                    monitor-exit(r7)     // Catch:{ all -> 0x0151 }
                    goto L_0x000b
                L_0x0040:
                    monitor-exit(r7)     // Catch:{ all -> 0x0151 }
                    goto L_0x0154
                L_0x0043:
                    if (r0 == 0) goto L_0x005c
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r8 = r8.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r8 = r8.width()     // Catch:{ all -> 0x0151 }
                    if (r4 != r8) goto L_0x005c
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r8 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r8 = r8.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r8 = r8.height()     // Catch:{ all -> 0x0151 }
                    if (r5 == r8) goto L_0x005a
                    goto L_0x005c
                L_0x005a:
                    r8 = r0
                    goto L_0x008f
                L_0x005c:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r0 = r0.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r0 = r0.width()     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r4 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r4 = r4.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r4 = r4.height()     // Catch:{ all -> 0x0151 }
                    int r5 = r0 * r4
                    int r5 = r5 * 4
                    java.nio.ByteBuffer r5 = java.nio.ByteBuffer.allocateDirect(r5)     // Catch:{ all -> 0x0151 }
                    java.nio.ByteOrder r8 = java.nio.ByteOrder.LITTLE_ENDIAN     // Catch:{ all -> 0x0151 }
                    r5.order(r8)     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r8 = r1.captureSurface     // Catch:{ all -> 0x0151 }
                    if (r8 == 0) goto L_0x0084
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r8 = r1.captureSurface     // Catch:{ all -> 0x0151 }
                    r8.release()     // Catch:{ all -> 0x0151 }
                    r1.captureSurface = r3     // Catch:{ all -> 0x0151 }
                L_0x0084:
                    com.appsmartz.recorder.glutils.EGLBase r8 = r1.eglBase     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r8 = r8.createOffscreen(r0, r4)     // Catch:{ all -> 0x0151 }
                    r1.captureSurface = r8     // Catch:{ all -> 0x0151 }
                    r8 = r5
                    r5 = r4
                    r4 = r0
                L_0x008f:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    boolean r0 = r0.isRunning     // Catch:{ all -> 0x0151 }
                    if (r0 == 0) goto L_0x0130
                    if (r4 <= 0) goto L_0x0130
                    if (r5 <= 0) goto L_0x0130
                    float[] r0 = r1.mMvpMatrix     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r9 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r9 = r9.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r9 = r9.mirror()     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder.setMirror(r0, r9)     // Catch:{ all -> 0x0151 }
                    float[] r0 = r1.mMvpMatrix     // Catch:{ all -> 0x0151 }
                    r9 = 5
                    r10 = r0[r9]     // Catch:{ all -> 0x0151 }
                    r11 = -1082130432(0xffffffffbf800000, float:-1.0)
                    float r10 = r10 * r11
                    r0[r9] = r10     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.GLDrawer2D r0 = r1.drawer     // Catch:{ all -> 0x0151 }
                    float[] r9 = r1.mMvpMatrix     // Catch:{ all -> 0x0151 }
                    r10 = 0
                    r0.setMvpMatrix(r9, r10)     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r0 = r1.captureSurface     // Catch:{ all -> 0x0151 }
                    r0.makeCurrent()     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.GLDrawer2D r0 = r1.drawer     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r9 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r9 = r9.mRendererTask     // Catch:{ all -> 0x0151 }
                    int r9 = r9.mTexId     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r11 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder$RendererTask r11 = r11.mRendererTask     // Catch:{ all -> 0x0151 }
                    float[] r11 = r11.mTexMatrix     // Catch:{ all -> 0x0151 }
                    r0.draw(r9, r11, r10)     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.EGLBase$IEglSurface r0 = r1.captureSurface     // Catch:{ all -> 0x0151 }
                    r0.swap()     // Catch:{ all -> 0x0151 }
                    r8.clear()     // Catch:{ all -> 0x0151 }
                    r9 = 0
                    r10 = 0
                    r13 = 6408(0x1908, float:8.98E-42)
                    r14 = 5121(0x1401, float:7.176E-42)
                    r11 = r4
                    r12 = r5
                    r15 = r8
                    android.opengl.GLES30.glReadPixels(r9, r10, r11, r12, r13, r14, r15)     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    int r0 = r0.mCaptureFormat     // Catch:{ all -> 0x0151 }
                    android.graphics.Bitmap$CompressFormat r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.getCaptureFormat(r0)     // Catch:{ all -> 0x0151 }
                    android.graphics.Bitmap$Config r9 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ all -> 0x011a }
                    android.graphics.Bitmap r9 = android.graphics.Bitmap.createBitmap(r4, r5, r9)     // Catch:{ all -> 0x011a }
                    r8.clear()     // Catch:{ all -> 0x011a }
                    r9.copyPixelsFromBuffer(r8)     // Catch:{ all -> 0x011a }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r10 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x011a }
                    java.io.OutputStream r10 = r10.mCaptureStream     // Catch:{ all -> 0x011a }
                    r9.compress(r0, r6, r10)     // Catch:{ all -> 0x011a }
                    r9.recycle()     // Catch:{ all -> 0x011a }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x011a }
                    java.io.OutputStream r0 = r0.mCaptureStream     // Catch:{ all -> 0x011a }
                    r0.flush()     // Catch:{ all -> 0x011a }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ IOException -> 0x0125 }
                    java.io.OutputStream r0 = r0.mCaptureStream     // Catch:{ IOException -> 0x0125 }
                    r0.close()     // Catch:{ IOException -> 0x0125 }
                    goto L_0x0141
                L_0x011a:
                    r0 = move-exception
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r9 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ IOException -> 0x0125 }
                    java.io.OutputStream r9 = r9.mCaptureStream     // Catch:{ IOException -> 0x0125 }
                    r9.close()     // Catch:{ IOException -> 0x0125 }
                    throw r0     // Catch:{ IOException -> 0x0125 }
                L_0x0125:
                    r0 = move-exception
                    java.lang.String r9 = com.appsmartz.recorder.glutils.AbstractRendererHolder.TAG     // Catch:{ all -> 0x0151 }
                    java.lang.String r10 = "failed to save file"
                    android.util.Log.w(r9, r10, r0)     // Catch:{ all -> 0x0151 }
                    goto L_0x0141
                L_0x0130:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    boolean r0 = r0.isRunning     // Catch:{ all -> 0x0151 }
                    if (r0 == 0) goto L_0x0141
                    java.lang.String r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.TAG     // Catch:{ all -> 0x0151 }
                    java.lang.String r9 = "#captureLoopGLES3:unexpectedly width/height is zero"
                    android.util.Log.w(r0, r9)     // Catch:{ all -> 0x0151 }
                L_0x0141:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    r0.mCaptureStream = r3     // Catch:{ all -> 0x0151 }
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0151 }
                    java.lang.Object r0 = r0.mSync     // Catch:{ all -> 0x0151 }
                    r0.notifyAll()     // Catch:{ all -> 0x0151 }
                    monitor-exit(r7)     // Catch:{ all -> 0x0151 }
                    r0 = r8
                    goto L_0x000b
                L_0x0151:
                    r0 = move-exception
                    monitor-exit(r7)     // Catch:{ all -> 0x0151 }
                    throw r0
                L_0x0154:
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this
                    java.lang.Object r2 = r0.mSync
                    monitor-enter(r2)
                    com.appsmartz.recorder.glutils.AbstractRendererHolder r0 = com.appsmartz.recorder.glutils.AbstractRendererHolder.this     // Catch:{ all -> 0x0162 }
                    java.lang.Object r0 = r0.mSync     // Catch:{ all -> 0x0162 }
                    r0.notifyAll()     // Catch:{ all -> 0x0162 }
                    monitor-exit(r2)     // Catch:{ all -> 0x0162 }
                    return
                L_0x0162:
                    r0 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0162 }
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.C05691.captureLoopGLES3():void");
            }

            private final void release() {
                IEglSurface iEglSurface = this.captureSurface;
                if (iEglSurface != null) {
                    iEglSurface.makeCurrent();
                    this.captureSurface.release();
                    this.captureSurface = null;
                }
                GLDrawer2D gLDrawer2D = this.drawer;
                if (gLDrawer2D != null) {
                    gLDrawer2D.release();
                    this.drawer = null;
                }
                EGLBase eGLBase = this.eglBase;
                if (eGLBase != null) {
                    eGLBase.release();
                    this.eglBase = null;
                }
            }
        };
        this.mCallback = renderHolderCallback;
        this.mRendererTask = createRendererTask(i, i2, i3, iContext, i4);
        new Thread(this.mRendererTask, RENDERER_THREAD_NAME).start();
        if (this.mRendererTask.waitReady()) {
            startCaptureTask();
            return;
        }
        throw new RuntimeException("failed to start renderer thread");
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void release() {
        this.mRendererTask.release();
        synchronized (this.mSync) {
            this.isRunning = false;
            this.mSync.notifyAll();
        }
    }

    @Nullable
    public IContext getContext() {
        return this.mRendererTask.getContext();
    }

    public Surface getSurface() {
        return this.mRendererTask.getSurface();
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mRendererTask.getSurfaceTexture();
    }

    public void reset() {
        this.mRendererTask.checkMasterSurface();
    }

    public void resize(int i, int i2) throws IllegalStateException {
        this.mRendererTask.resize(i, i2);
    }

    public void setMirror(int i) {
        this.mRendererTask.mirror(i % 4);
    }

    public int getMirror() {
        return this.mRendererTask.mirror();
    }

    public void addSurface(int i, Object obj, boolean z) throws IllegalStateException, IllegalArgumentException {
        this.mRendererTask.addSurface(i, obj);
    }

    public void addSurface(int i, Object obj, boolean z, int i2) throws IllegalStateException, IllegalArgumentException {
        this.mRendererTask.addSurface(i, obj, i2);
    }

    public void removeSurface(int i) {
        this.mRendererTask.removeSurface(i);
    }

    public void removeSurfaceAll() {
        this.mRendererTask.removeSurfaceAll();
    }

    public void clearSurface(int i, int i2) {
        this.mRendererTask.clearSurface(i, i2);
    }

    public void clearSurfaceAll(int i) {
        this.mRendererTask.clearSurfaceAll(i);
    }

    public void setMvpMatrix(int i, int i2, @NonNull float[] fArr) {
        this.mRendererTask.setMvpMatrix(i, i2, fArr);
    }

    public boolean isEnabled(int i) {
        return this.mRendererTask.isEnabled(i);
    }

    public void setEnabled(int i, boolean z) {
        this.mRendererTask.setEnabled(i, z);
    }

    public void requestFrame() {
        this.mRendererTask.removeRequest(1);
        this.mRendererTask.offer(1);
    }

    public int getCount() {
        return this.mRendererTask.getCount();
    }

    @Deprecated
    public void captureStillAsync(@NonNull String str) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), 80, false);
    }

    @Deprecated
    public void captureStillAsync(@NonNull String str, @IntRange(from = 1, mo116to = 99) int i) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), i, false);
    }

    public void captureStill(@NonNull String str) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), 80, true);
    }

    public void captureStill(@NonNull String str, @IntRange(from = 1, mo116to = 99) int i) throws FileNotFoundException, IllegalStateException {
        captureStill(new BufferedOutputStream(new FileOutputStream(str)), getCaptureFormat(str), i, true);
    }

    public void captureStill(@NonNull OutputStream outputStream, int i, @IntRange(from = 1, mo116to = 99) int i2) throws IllegalStateException {
        captureStill(outputStream, i, i2, true);
    }

    private void captureStill(@NonNull OutputStream outputStream, int i, @IntRange(from = 1, mo116to = 99) int i2, boolean z) throws IllegalStateException {
        synchronized (this.mSync) {
            if (!this.isRunning) {
                throw new IllegalStateException("already released?");
            } else if (this.mCaptureStream == null) {
                this.mCaptureStream = outputStream;
                this.mCaptureFormat = i;
                this.mCaptureCompression = i2;
                this.mSync.notifyAll();
                if (z) {
                    while (this.isRunning && this.mCaptureStream != null) {
                        try {
                            this.mSync.wait(1000);
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            } else {
                throw new IllegalStateException("already run still capturing now");
            }
        }
    }

    private static int getCaptureFormat(@NonNull String str) throws IllegalArgumentException {
        str.toLowerCase();
        if (str.endsWith(".jpg") || str.endsWith(".jpeg")) {
            return 0;
        }
        if (str.endsWith(".png")) {
            return 1;
        }
        if (str.endsWith(".webp")) {
            return 2;
        }
        throw new IllegalArgumentException("unknown compress format(extension)");
    }

    /* access modifiers changed from: private */
    public static CompressFormat getCaptureFormat(int i) {
        if (i == 0) {
            return CompressFormat.JPEG;
        }
        if (i == 1) {
            return CompressFormat.PNG;
        }
        if (i != 2) {
            return CompressFormat.JPEG;
        }
        return CompressFormat.WEBP;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(2:5|6)|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0018 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startCaptureTask() {
        /*
            r3 = this;
            java.lang.Thread r0 = new java.lang.Thread
            java.lang.Runnable r1 = r3.mCaptureTask
            java.lang.String r2 = "CaptureTask"
            r0.<init>(r1, r2)
            r0.start()
            java.lang.Object r0 = r3.mSync
            monitor-enter(r0)
            boolean r1 = r3.isRunning     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x0018
            java.lang.Object r1 = r3.mSync     // Catch:{ InterruptedException -> 0x0018 }
            r1.wait()     // Catch:{ InterruptedException -> 0x0018 }
        L_0x0018:
            monitor-exit(r0)     // Catch:{ all -> 0x001a }
            return
        L_0x001a:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.AbstractRendererHolder.startCaptureTask():void");
    }

    /* access modifiers changed from: protected */
    public void notifyCapture() {
        synchronized (this.mCaptureTask) {
            this.mCaptureTask.notify();
        }
    }

    /* access modifiers changed from: protected */
    public void callOnCreate(Surface surface) {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onCreate(surface);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void callOnFrameAvailable() {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onFrameAvailable();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void callOnDestroy() {
        RenderHolderCallback renderHolderCallback = this.mCallback;
        if (renderHolderCallback != null) {
            try {
                renderHolderCallback.onDestroy();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    protected static void setMirror(float[] fArr, int i) {
        if (i == 0) {
            fArr[0] = Math.abs(fArr[0]);
            fArr[5] = Math.abs(fArr[5]);
        } else if (i == 1) {
            fArr[0] = -Math.abs(fArr[0]);
            fArr[5] = Math.abs(fArr[5]);
        } else if (i == 2) {
            fArr[0] = Math.abs(fArr[0]);
            fArr[5] = -Math.abs(fArr[5]);
        } else if (i == 3) {
            fArr[0] = -Math.abs(fArr[0]);
            fArr[5] = -Math.abs(fArr[5]);
        }
    }

    protected static void rotate(float[] fArr, int i) {
        if (i % 180 != 0) {
            Matrix.rotateM(fArr, 0, (float) i, 0.0f, 0.0f, 1.0f);
        }
    }

    protected static void setRotation(float[] fArr, int i) {
        Matrix.setIdentityM(fArr, 0);
        if (i % 180 != 0) {
            Matrix.rotateM(fArr, 0, (float) i, 0.0f, 0.0f, 1.0f);
        }
    }
}
