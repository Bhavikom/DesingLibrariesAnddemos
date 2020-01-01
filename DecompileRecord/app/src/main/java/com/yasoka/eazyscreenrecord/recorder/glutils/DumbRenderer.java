package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import com.yasoka.eazyscreenrecord.recorder.utils.MessageTask;
/*import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.utils.MessageTask.TaskBreak;*/

public class DumbRenderer implements IRenderer {
    private static final int REQUEST_DRAW = 2;
    private static final int REQUEST_MIRROR = 4;
    private static final int REQUEST_RESIZE = 3;
    private static final int REQUEST_SET_SURFACE = 1;
    /* access modifiers changed from: private */
    public static final String TAG = "DumbRenderer";
    private int mMirror;
    private RendererTask mRendererTask;
    private final Object mSync;

    public interface RendererDelegater {
        void onDraw(EGLBase eGLBase, Object... objArr);

        void onMirror(EGLBase eGLBase, int i);

        void onResize(EGLBase eGLBase, int i, int i2);

        void onSetSurface(EGLBase eGLBase, Object obj);

        void onStart(EGLBase eGLBase);

        void onStop(EGLBase eGLBase);
    }

    private static class RendererTask extends EglTask {
        private int frameHeight;
        private int frameRotation;
        private int frameWidth;
        private final RendererDelegater mDelegater;
        private boolean mirror;
        private int surfaceHeight;
        private int surfaceWidth;

        public RendererTask(EGLBase.IContext iContext, int i, @NonNull RendererDelegater rendererDelegater) {
            this(3, iContext, i, rendererDelegater);
        }

        public RendererTask(int i, EGLBase.IContext iContext, int i2, @NonNull RendererDelegater rendererDelegater) {
            super(i, iContext, i2);
            this.mDelegater = rendererDelegater;
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            makeCurrent();
            try {
                this.mDelegater.onStart(getEgl());
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            makeCurrent();
            try {
                this.mDelegater.onStop(getEgl());
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) throws MessageTask.TaskBreak {
            if (i == 1) {
                handleSetSurface(obj);
            } else if (i == 2) {
                handleDraw(obj);
            } else if (i == 3) {
                handleResize(i2, i3);
            } else if (i == 4) {
                handleMirror(i2);
            }
            return null;
        }

        private void handleSetSurface(Object obj) {
            makeCurrent();
            try {
                this.mDelegater.onSetSurface(getEgl(), obj);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        private void handleResize(int i, int i2) {
            if (this.surfaceWidth != i || this.surfaceHeight != i2) {
                this.surfaceWidth = i;
                this.surfaceHeight = i2;
                makeCurrent();
                try {
                    this.mDelegater.onResize(getEgl(), i, i2);
                } catch (Exception e) {
                    Log.w(DumbRenderer.TAG, e);
                }
                handleDraw(new Object[0]);
            }
        }

        private void handleDraw(Object... objArr) {
            makeCurrent();
            try {
                this.mDelegater.onDraw(getEgl(), objArr);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }

        private void handleMirror(int i) {
            makeCurrent();
            try {
                this.mDelegater.onMirror(getEgl(), i);
            } catch (Exception e) {
                Log.w(DumbRenderer.TAG, e);
            }
        }
    }

    public DumbRenderer(EGLBase.IContext iContext, int i, RendererDelegater rendererDelegater) {
        this(3, iContext, i, rendererDelegater);
    }

    public DumbRenderer(int i, EGLBase.IContext iContext, int i2, RendererDelegater rendererDelegater) {
        this.mSync = new Object();
        this.mMirror = 0;
        this.mRendererTask = new RendererTask(i, iContext, i2, rendererDelegater);
        new Thread(this.mRendererTask, TAG).start();
        if (!this.mRendererTask.waitReady()) {
            throw new RuntimeException("failed to start renderer thread");
        }
    }

    public void release() {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.release();
                this.mRendererTask = null;
            }
        }
    }

    public void setSurface(Surface surface) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(1, (Object) surface);
            }
        }
    }

    public void setSurface(SurfaceTexture surfaceTexture) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(1, (Object) surfaceTexture);
            }
        }
    }

    public void setMirror(int i) {
        synchronized (this.mSync) {
            if (this.mMirror != i) {
                this.mMirror = i;
                if (this.mRendererTask != null) {
                    this.mRendererTask.offer(4, i % 4);
                }
            }
        }
    }

    public int getMirror() {
        return this.mMirror;
    }

    public void resize(int i, int i2) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(3, i, i2);
            }
        }
    }

    public void requestRender(Object... objArr) {
        synchronized (this.mSync) {
            if (this.mRendererTask != null) {
                this.mRendererTask.offer(2, (Object) objArr);
            }
        }
    }
}
