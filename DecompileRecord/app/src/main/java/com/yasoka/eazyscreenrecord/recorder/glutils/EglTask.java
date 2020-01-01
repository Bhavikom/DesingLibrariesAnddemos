package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.support.annotation.Nullable;

import com.yasoka.eazyscreenrecord.recorder.utils.MessageTask;
/*import com.appsmartz.recorder.glutils.EGLBase.IConfig;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;
import com.appsmartz.recorder.utils.MessageTask;*/

public abstract class EglTask extends MessageTask {
    public static final int EGL_FLAG_DEPTH_BUFFER = 1;
    public static final int EGL_FLAG_RECORDABLE = 2;
    public static final int EGL_FLAG_STENCIL_1BIT = 4;
    public static final int EGL_FLAG_STENCIL_8BIT = 32;
    private EGLBase mEgl = null;
    private EGLBase.IEglSurface mEglHolder;

    public EglTask(EGLBase.IContext iContext, int i) {
        init(i, 3, iContext);
    }

    public EglTask(int i, EGLBase.IContext iContext, int i2) {
        init(i2, i, iContext);
    }

    /* access modifiers changed from: protected */
    public void onInit(int i, int i2, Object obj) {
        if (obj == null || (obj instanceof EGLBase.IContext)) {
            boolean z = false;
            int i3 = (i & 4) == 4 ? 1 : (i & 32) == 32 ? 8 : 0;
            EGLBase.IContext iContext = (EGLBase.IContext) obj;
            boolean z2 = (i & 1) == 1;
            if ((i & 2) == 2) {
                z = true;
            }
            this.mEgl = EGLBase.createFrom(i2, iContext, z2, i3, z);
        }
        EGLBase eGLBase = this.mEgl;
        if (eGLBase == null) {
            callOnError(new RuntimeException("failed to create EglCore"));
            releaseSelf();
            return;
        }
        this.mEglHolder = eGLBase.createOffscreen(1, 1);
        this.mEglHolder.makeCurrent();
    }

    /* access modifiers changed from: protected */
    public Request takeRequest() throws InterruptedException {
        Request takeRequest = super.takeRequest();
        this.mEglHolder.makeCurrent();
        return takeRequest;
    }

    /* access modifiers changed from: protected */
    public void onBeforeStop() {
        this.mEglHolder.makeCurrent();
    }

    /* access modifiers changed from: protected */
    public void onRelease() {
        this.mEglHolder.release();
        this.mEgl.release();
    }

    /* access modifiers changed from: protected */
    public EGLBase getEgl() {
        return this.mEgl;
    }

    /* access modifiers changed from: protected */
    public EGLBase.IContext getEGLContext() {
        return this.mEgl.getContext();
    }

    /* access modifiers changed from: protected */
    public EGLBase.IConfig getConfig() {
        return this.mEgl.getConfig();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public EGLBase.IContext getContext() {
        EGLBase eGLBase = this.mEgl;
        if (eGLBase != null) {
            return eGLBase.getContext();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void makeCurrent() {
        this.mEglHolder.makeCurrent();
    }

    /* access modifiers changed from: protected */
    public boolean isGLES3() {
        EGLBase eGLBase = this.mEgl;
        return eGLBase != null && eGLBase.getGlVersion() > 2;
    }
}
