package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

public class StaticTextureSource {
    private static final boolean DEBUG = false;
    private static final int REQUEST_ADD_SURFACE = 3;
    private static final int REQUEST_DRAW = 1;
    private static final int REQUEST_REMOVE_SURFACE = 4;
    private static final int REQUEST_SET_BITMAP = 7;
    /* access modifiers changed from: private */
    public static final String TAG = "StaticTextureSource";
    /* access modifiers changed from: private */
    public volatile boolean isRunning;
    /* access modifiers changed from: private */
    public Runnable mOnFrameTask;
    /* access modifiers changed from: private */
    public RendererTask mRendererTask;
    /* access modifiers changed from: private */
    public final Object mSync;

    private static class RendererTask extends EglTask {
        private final Object mClientSync = new Object();
        private final SparseArray<RendererSurfaceRec> mClients = new SparseArray<>();
        private GLDrawer2D mDrawer;
        /* access modifiers changed from: private */
        public TextureOffscreen mImageSource;
        /* access modifiers changed from: private */
        public final long mIntervalsNs;
        private final StaticTextureSource mParent;
        /* access modifiers changed from: private */
        public int mVideoHeight;
        /* access modifiers changed from: private */
        public int mVideoWidth;

        /* access modifiers changed from: protected */
        public boolean onError(Exception exc) {
            return false;
        }

        public RendererTask(StaticTextureSource staticTextureSource, int i, int i2, float f) {
            super(3, null, 0);
            this.mParent = staticTextureSource;
            this.mVideoWidth = i;
            this.mVideoHeight = i2;
            this.mIntervalsNs = f <= 0.0f ? 100000000 : (long) (1.0E9f / f);
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            this.mDrawer = new GLDrawer2D(false);
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = true;
                this.mParent.mSync.notifyAll();
            }
            new Thread(this.mParent.mOnFrameTask, StaticTextureSource.TAG).start();
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            synchronized (this.mParent.mSync) {
                this.mParent.isRunning = false;
                this.mParent.mSync.notifyAll();
            }
            makeCurrent();
            GLDrawer2D gLDrawer2D = this.mDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mDrawer = null;
            }
            TextureOffscreen textureOffscreen = this.mImageSource;
            if (textureOffscreen != null) {
                textureOffscreen.release();
                this.mImageSource = null;
            }
            handleRemoveAll();
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) {
            if (i == 1) {
                handleDraw();
            } else if (i == 7) {
                handleSetBitmap((Bitmap) obj);
            } else if (i == 3) {
                handleAddSurface(i2, obj, i3);
            } else if (i == 4) {
                handleRemoveSurface(i2);
            }
            return null;
        }

        public void addSurface(int i, Object obj) {
            addSurface(i, obj, -1);
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:10|11|(3:(2:17|13)|15|16)|18|19) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0038 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void addSurface(int r5, Object r6, int r7) {
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
                java.lang.Object r0 = r4.mClientSync
                monitor-enter(r0)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ all -> 0x003a }
                java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x003a }
                if (r1 != 0) goto L_0x0038
            L_0x0023:
                r1 = 3
                boolean r1 = r4.offer(r1, r5, r7, r6)     // Catch:{ all -> 0x003a }
                if (r1 == 0) goto L_0x0030
                java.lang.Object r5 = r4.mClientSync     // Catch:{ InterruptedException -> 0x0038 }
                r5.wait()     // Catch:{ InterruptedException -> 0x0038 }
                goto L_0x0038
            L_0x0030:
                java.lang.Object r1 = r4.mClientSync     // Catch:{ InterruptedException -> 0x0038 }
                r2 = 10
                r1.wait(r2)     // Catch:{ InterruptedException -> 0x0038 }
                goto L_0x0023
            L_0x0038:
                monitor-exit(r0)     // Catch:{ all -> 0x003a }
                return
            L_0x003a:
                r5 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x003a }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.StaticTextureSource.RendererTask.addSurface(int, java.lang.Object, int):void");
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(3:(2:9|5)|7|8)|10|11) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0020 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void removeSurface(int r5) {
            /*
                r4 = this;
                java.lang.Object r0 = r4.mClientSync
                monitor-enter(r0)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r1 = r4.mClients     // Catch:{ all -> 0x0022 }
                java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0022 }
                if (r1 == 0) goto L_0x0020
            L_0x000b:
                r1 = 4
                boolean r1 = r4.offer(r1, r5)     // Catch:{ all -> 0x0022 }
                if (r1 == 0) goto L_0x0018
                java.lang.Object r5 = r4.mClientSync     // Catch:{ InterruptedException -> 0x0020 }
                r5.wait()     // Catch:{ InterruptedException -> 0x0020 }
                goto L_0x0020
            L_0x0018:
                java.lang.Object r1 = r4.mClientSync     // Catch:{ InterruptedException -> 0x0020 }
                r2 = 10
                r1.wait(r2)     // Catch:{ InterruptedException -> 0x0020 }
                goto L_0x000b
            L_0x0020:
                monitor-exit(r0)     // Catch:{ all -> 0x0022 }
                return
            L_0x0022:
                r5 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0022 }
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.StaticTextureSource.RendererTask.removeSurface(int):void");
        }

        public void setBitmap(@NonNull Bitmap bitmap) {
            offer(7, (Object) bitmap);
        }

        public int getCount() {
            int size;
            synchronized (this.mClientSync) {
                size = this.mClients.size();
            }
            return size;
        }

        private void checkFinished() {
            if (isFinished()) {
                throw new RuntimeException("already finished");
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:11|12|13|14|25) */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x003c, code lost:
            continue;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0034 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void handleDraw() {
            /*
                r6 = this;
                r6.makeCurrent()
                com.appsmartz.recorder.glutils.TextureOffscreen r0 = r6.mImageSource
                if (r0 == 0) goto L_0x0044
                int r0 = r0.getTexture()
                java.lang.Object r1 = r6.mClientSync
                monitor-enter(r1)
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r2 = r6.mClients     // Catch:{ all -> 0x0041 }
                int r2 = r2.size()     // Catch:{ all -> 0x0041 }
                int r2 = r2 + -1
            L_0x0016:
                if (r2 < 0) goto L_0x003f
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r3 = r6.mClients     // Catch:{ all -> 0x0041 }
                java.lang.Object r3 = r3.valueAt(r2)     // Catch:{ all -> 0x0041 }
                com.appsmartz.recorder.glutils.RendererSurfaceRec r3 = (com.appsmartz.recorder.glutils.RendererSurfaceRec) r3     // Catch:{ all -> 0x0041 }
                if (r3 == 0) goto L_0x003c
                boolean r4 = r3.canDraw()     // Catch:{ all -> 0x0041 }
                if (r4 == 0) goto L_0x003c
                com.appsmartz.recorder.glutils.GLDrawer2D r4 = r6.mDrawer     // Catch:{ Exception -> 0x0034 }
                r5 = 0
                r3.draw(r4, r0, r5)     // Catch:{ Exception -> 0x0034 }
                java.lang.String r4 = "handleSetBitmap"
                com.appsmartz.recorder.glutils.GLHelper.checkGlError(r4)     // Catch:{ Exception -> 0x0034 }
                goto L_0x003c
            L_0x0034:
                android.util.SparseArray<com.appsmartz.recorder.glutils.RendererSurfaceRec> r4 = r6.mClients     // Catch:{ all -> 0x0041 }
                r4.removeAt(r2)     // Catch:{ all -> 0x0041 }
                r3.release()     // Catch:{ all -> 0x0041 }
            L_0x003c:
                int r2 = r2 + -1
                goto L_0x0016
            L_0x003f:
                monitor-exit(r1)     // Catch:{ all -> 0x0041 }
                goto L_0x004d
            L_0x0041:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0041 }
                throw r0
            L_0x0044:
                java.lang.String r0 = com.appsmartz.recorder.glutils.StaticTextureSource.TAG
                java.lang.String r1 = "mImageSource is not ready"
                android.util.Log.w(r0, r1)
            L_0x004d:
                r0 = 16384(0x4000, float:2.2959E-41)
                android.opengl.GLES20.glClear(r0)
                android.opengl.GLES20.glFlush()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.StaticTextureSource.RendererTask.handleDraw():void");
        }

        private void handleAddSurface(int i, Object obj, int i2) {
            checkSurface();
            synchronized (this.mClientSync) {
                if (((RendererSurfaceRec) this.mClients.get(i)) == null) {
                    try {
                        this.mClients.append(i, RendererSurfaceRec.newInstance(getEgl(), obj, i2));
                    } catch (Exception e) {
                        String access$500 = StaticTextureSource.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("invalid surface: surface=");
                        sb.append(obj);
                        Log.w(access$500, sb.toString(), e);
                    }
                } else {
                    String access$5002 = StaticTextureSource.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("surface is already added: id=");
                    sb2.append(i);
                    Log.w(access$5002, sb2.toString());
                }
                this.mClientSync.notifyAll();
            }
        }

        private void handleRemoveSurface(int i) {
            synchronized (this.mClientSync) {
                RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.get(i);
                if (rendererSurfaceRec != null) {
                    this.mClients.remove(i);
                    rendererSurfaceRec.release();
                }
                checkSurface();
                this.mClientSync.notifyAll();
            }
        }

        private void handleRemoveAll() {
            synchronized (this.mClientSync) {
                int size = this.mClients.size();
                for (int i = 0; i < size; i++) {
                    RendererSurfaceRec rendererSurfaceRec = (RendererSurfaceRec) this.mClients.valueAt(i);
                    if (rendererSurfaceRec != null) {
                        makeCurrent();
                        rendererSurfaceRec.release();
                    }
                }
                this.mClients.clear();
            }
        }

        private void checkSurface() {
            synchronized (this.mClientSync) {
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

        private void handleSetBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            TextureOffscreen textureOffscreen = this.mImageSource;
            if (textureOffscreen == null) {
                this.mImageSource = new TextureOffscreen(width, height, false);
                GLHelper.checkGlError("handleSetBitmap");
                this.mImageSource.loadBitmap(bitmap);
            } else {
                textureOffscreen.loadBitmap(bitmap);
            }
            this.mVideoWidth = width;
            this.mVideoHeight = height;
        }
    }

    public StaticTextureSource(float f) {
        this(null, f);
    }

    public StaticTextureSource(@Nullable Bitmap bitmap) {
        this(bitmap, 10.0f);
    }

    public StaticTextureSource(@Nullable Bitmap bitmap, float f) {
        this.mSync = new Object();
        this.mOnFrameTask = new Runnable() {
            public void run() {
                long access$700 = StaticTextureSource.this.mRendererTask.mIntervalsNs / 1000000;
                int access$7002 = (int) (StaticTextureSource.this.mRendererTask.mIntervalsNs % 1000000);
                while (StaticTextureSource.this.isRunning && StaticTextureSource.this.mRendererTask != null) {
                    synchronized (StaticTextureSource.this.mSync) {
                        try {
                            StaticTextureSource.this.mSync.wait(access$700, access$7002);
                            if (StaticTextureSource.this.mRendererTask.mImageSource != null) {
                                StaticTextureSource.this.mRendererTask.removeRequest(1);
                                StaticTextureSource.this.mRendererTask.offer(1);
                                StaticTextureSource.this.mSync.notify();
                            }
                        } catch (Exception e) {
                            Log.w(StaticTextureSource.TAG, e);
                        }
                    }
                }
            }
        };
        int i = 1;
        int width = bitmap != null ? bitmap.getWidth() : 1;
        if (bitmap != null) {
            i = bitmap.getHeight();
        }
        this.mRendererTask = new RendererTask(this, width, i, f);
        new Thread(this.mRendererTask, TAG).start();
        if (this.mRendererTask.waitReady()) {
            setBitmap(bitmap);
            return;
        }
        throw new RuntimeException("failed to start renderer thread");
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void release() {
        this.isRunning = false;
        synchronized (this.mSync) {
            this.mSync.notifyAll();
        }
        RendererTask rendererTask = this.mRendererTask;
        if (rendererTask != null) {
            rendererTask.release();
        }
        synchronized (this.mSync) {
            this.mRendererTask = null;
            this.mSync.notifyAll();
        }
    }

    public void addSurface(int i, Object obj, boolean z) {
        synchronized (this.mSync) {
            this.mRendererTask.addSurface(i, obj);
        }
    }

    public void addSurface(int i, Object obj, boolean z, int i2) {
        synchronized (this.mSync) {
            this.mRendererTask.addSurface(i, obj, i2);
        }
    }

    public void removeSurface(int i) {
        synchronized (this.mSync) {
            this.mRendererTask.removeSurface(i);
        }
    }

    public void requestFrame() {
        synchronized (this.mSync) {
            this.mRendererTask.removeRequest(1);
            this.mRendererTask.offer(1);
            this.mSync.notify();
        }
    }

    public int getCount() {
        return this.mRendererTask.getCount();
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (this.mSync) {
                this.mRendererTask.setBitmap(bitmap);
            }
        }
    }

    public int getWidth() {
        int access$000;
        synchronized (this.mSync) {
            access$000 = this.mRendererTask != null ? this.mRendererTask.mVideoWidth : 0;
        }
        return access$000;
    }

    public int getHeight() {
        int access$100;
        synchronized (this.mSync) {
            access$100 = this.mRendererTask != null ? this.mRendererTask.mVideoHeight : 0;
        }
        return access$100;
    }
}
