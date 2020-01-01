package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
/*import com.appsmartz.recorder.glutils.EGLBase.IConfig;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;
import com.appsmartz.recorder.utils.BuildCheck;*/
import com.yasoka.eazyscreenrecord.recorder.utils.BuildCheck;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

class EGLBase10 extends EGLBase {
    private static final Context EGL_NO_CONTEXT = new Context(EGL10.EGL_NO_CONTEXT);
    private static final String TAG = "EGLBase10";
    @NonNull
    private Context mContext = EGL_NO_CONTEXT;
    private EGL10 mEgl = null;
    private Config mEglConfig = null;
    private EGLDisplay mEglDisplay = null;
    private int mGlVersion = 2;

    public static class Config extends IConfig {
        public final EGLConfig eglConfig;

        private Config(EGLConfig eGLConfig) {
            this.eglConfig = eGLConfig;
        }
    }

    public static class Context extends IContext {
        public final EGLContext eglContext;

        public long getNativeHandle() {
            return 0;
        }

        private Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }

        public Object getEGLContext() {
            return this.eglContext;
        }
    }

    public static class EglSurface implements IEglSurface {
        private final EGLBase10 mEglBase;
        private EGLSurface mEglSurface;

        public void setPresentationTime(long j) {
        }

        private EglSurface(EGLBase10 eGLBase10, Object obj) throws IllegalArgumentException {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
            this.mEglBase = eGLBase10;
            boolean z = obj instanceof Surface;
            if (z && !BuildCheck.isAndroid4_2()) {
                this.mEglSurface = this.mEglBase.createWindowSurface(new MySurfaceHolder((Surface) obj));
            } else if (z || (obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceView)) {
                this.mEglSurface = this.mEglBase.createWindowSurface(obj);
            } else {
                throw new IllegalArgumentException("unsupported surface");
            }
        }

        private EglSurface(EGLBase10 eGLBase10, int i, int i2) {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
            this.mEglBase = eGLBase10;
            if (i <= 0 || i2 <= 0) {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
            } else {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(i, i2);
            }
        }

        public void makeCurrent() {
            this.mEglBase.makeCurrent(this.mEglSurface);
            if (this.mEglBase.getGlVersion() >= 2) {
                GLES20.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            } else {
                GLES10.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            }
        }

        public void swap() {
            this.mEglBase.swap(this.mEglSurface);
        }

        public void swap(long j) {
            this.mEglBase.swap(this.mEglSurface, j);
        }

        public IContext getContext() {
            return this.mEglBase.getContext();
        }

        public boolean isValid() {
            EGLSurface eGLSurface = this.mEglSurface;
            return eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        public void release() {
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    public static class MySurfaceHolder implements SurfaceHolder {
        private final Surface surface;

        public void addCallback(Callback callback) {
        }

        public Rect getSurfaceFrame() {
            return null;
        }

        public boolean isCreating() {
            return false;
        }

        public Canvas lockCanvas() {
            return null;
        }

        public Canvas lockCanvas(Rect rect) {
            return null;
        }

        public void removeCallback(Callback callback) {
        }

        public void setFixedSize(int i, int i2) {
        }

        public void setFormat(int i) {
        }

        public void setKeepScreenOn(boolean z) {
        }

        public void setSizeFromLayout() {
        }

        public void setType(int i) {
        }

        public void unlockCanvasAndPost(Canvas canvas) {
        }

        public MySurfaceHolder(Surface surface2) {
            this.surface = surface2;
        }

        public Surface getSurface() {
            return this.surface;
        }
    }

    public EGLBase10(int i, Context context, boolean z, int i2, boolean z2) {
        init(i, context, z, i2, z2);
    }

    public void release() {
        destroyContext();
        this.mContext = EGL_NO_CONTEXT;
        EGL10 egl10 = this.mEgl;
        if (egl10 != null) {
            egl10.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglTerminate(this.mEglDisplay);
            this.mEglDisplay = null;
            this.mEglConfig = null;
            this.mEgl = null;
        }
    }

    public EglSurface createFromSurface(Object obj) {
        EglSurface eglSurface = new EglSurface(obj);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    public EglSurface createOffscreen(int i, int i2) {
        EglSurface eglSurface = new EglSurface(i, i2);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Config getConfig() {
        return this.mEglConfig;
    }

    public void makeDefault() {
        if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
            StringBuilder sb = new StringBuilder();
            sb.append("makeDefault:eglMakeCurrent:err=");
            sb.append(this.mEgl.eglGetError());
            Log.w(TAG, sb.toString());
        }
    }

    public void sync() {
        this.mEgl.eglWaitGL();
        this.mEgl.eglWaitNative(12379, null);
    }

    public String queryString(int i) {
        return this.mEgl.eglQueryString(this.mEglDisplay, i);
    }

    public int getGlVersion() {
        return this.mGlVersion;
    }

    private final void init(int i, @Nullable Context context, boolean z, int i2, boolean z2) {
        if (context == null) {
            context = EGL_NO_CONTEXT;
        }
        if (this.mEgl == null) {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay != EGL10.EGL_NO_DISPLAY) {
                if (!this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                    this.mEglDisplay = null;
                    throw new RuntimeException("eglInitialize failed");
                }
            } else {
                throw new RuntimeException("eglGetDisplay failed");
            }
        }
        if (i >= 3) {
            EGLConfig config = getConfig(3, z, i2, z2);
            if (config != null) {
                EGLContext createContext = createContext(context, config, 3);
                if (this.mEgl.eglGetError() == 12288) {
                    this.mEglConfig = new Config(config);
                    this.mContext = new Context(createContext);
                    this.mGlVersion = 3;
                }
            }
        }
        String str = "chooseConfig failed";
        String str2 = "eglCreateContext";
        if (i >= 2) {
            Context context2 = this.mContext;
            if (context2 == null || context2.eglContext == EGL10.EGL_NO_CONTEXT) {
                EGLConfig config2 = getConfig(2, z, i2, z2);
                if (config2 != null) {
                    try {
                        EGLContext createContext2 = createContext(context, config2, 2);
                        checkEglError(str2);
                        this.mEglConfig = new Config(config2);
                        this.mContext = new Context(createContext2);
                        this.mGlVersion = 2;
                    } catch (Exception unused) {
                        if (z2) {
                            EGLConfig config3 = getConfig(2, z, i2, false);
                            if (config3 != null) {
                                EGLContext createContext3 = createContext(context, config3, 2);
                                checkEglError(str2);
                                this.mEglConfig = new Config(config3);
                                this.mContext = new Context(createContext3);
                                this.mGlVersion = 2;
                            } else {
                                throw new RuntimeException(str);
                            }
                        }
                    }
                } else {
                    throw new RuntimeException(str);
                }
            }
        }
        Context context3 = this.mContext;
        if (context3 == null || context3.eglContext == EGL10.EGL_NO_CONTEXT) {
            EGLConfig config4 = getConfig(1, z, i2, z2);
            if (config4 != null) {
                EGLContext createContext4 = createContext(context, config4, 1);
                checkEglError(str2);
                this.mEglConfig = new Config(config4);
                this.mContext = new Context(createContext4);
                this.mGlVersion = 1;
            } else {
                throw new RuntimeException(str);
            }
        }
        int[] iArr = new int[1];
        this.mEgl.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, iArr);
        StringBuilder sb = new StringBuilder();
        sb.append("EGLContext created, client version ");
        sb.append(iArr[0]);
        Log.d(TAG, sb.toString());
        makeDefault();
    }

    /* access modifiers changed from: private */
    public final boolean makeCurrent(EGLSurface eGLSurface) {
        if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
            if (this.mEgl.eglGetError() == 12299) {
                Log.e(TAG, "makeCurrent:EGL_BAD_NATIVE_WINDOW");
            }
            return false;
        } else if (this.mEgl.eglMakeCurrent(this.mEglDisplay, eGLSurface, eGLSurface, this.mContext.eglContext)) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("eglMakeCurrent");
            sb.append(this.mEgl.eglGetError());
            Log.w("TAG", sb.toString());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public final int swap(EGLSurface eGLSurface) {
        if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return this.mEgl.eglGetError();
        }
        return 12288;
    }

    /* access modifiers changed from: private */
    public final int swap(EGLSurface eGLSurface, long j) {
        if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return this.mEgl.eglGetError();
        }
        return 12288;
    }

    private final EGLContext createContext(@NonNull Context context, EGLConfig eGLConfig, int i) {
        return this.mEgl.eglCreateContext(this.mEglDisplay, eGLConfig, context.eglContext, new int[]{12440, i, 12344});
    }

    private final void destroyContext() {
        if (!this.mEgl.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
            StringBuilder sb = new StringBuilder();
            sb.append("display:");
            sb.append(this.mEglDisplay);
            sb.append(" context: ");
            sb.append(this.mContext.eglContext);
            Log.e("destroyContext", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("eglDestroyContext:");
            sb2.append(this.mEgl.eglGetError());
            Log.e(TAG, sb2.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
    }

    /* access modifiers changed from: private */
    public final int getSurfaceWidth(EGLSurface eGLSurface) {
        int[] iArr = new int[1];
        if (!this.mEgl.eglQuerySurface(this.mEglDisplay, eGLSurface, 12375, iArr)) {
            iArr[0] = 0;
        }
        return iArr[0];
    }

    /* access modifiers changed from: private */
    public final int getSurfaceHeight(EGLSurface eGLSurface) {
        int[] iArr = new int[1];
        if (!this.mEgl.eglQuerySurface(this.mEglDisplay, eGLSurface, 12374, iArr)) {
            iArr[0] = 0;
        }
        return iArr[0];
    }

    /* access modifiers changed from: private */
    public final EGLSurface createWindowSurface(Object obj) {
        String str = TAG;
        try {
            EGLSurface eglCreateWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, obj, new int[]{12344});
            if (eglCreateWindowSurface != null) {
                if (eglCreateWindowSurface != EGL10.EGL_NO_SURFACE) {
                    makeCurrent(eglCreateWindowSurface);
                    return eglCreateWindowSurface;
                }
            }
            int eglGetError = this.mEgl.eglGetError();
            if (eglGetError == 12299) {
                Log.e(str, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("createWindowSurface failed error=");
            sb.append(eglGetError);
            throw new RuntimeException(sb.toString());
        } catch (Exception e) {
            Log.e(str, "eglCreateWindowSurface", e);
            throw new IllegalArgumentException(e);
        }
    }

    /* access modifiers changed from: private */
    public final EGLSurface createOffscreenSurface(int i, int i2) {
        String str = "createOffscreenSurface";
        String str2 = TAG;
        int[] iArr = {12375, i, 12374, i2, 12344};
        this.mEgl.eglWaitGL();
        EGLSurface eGLSurface = null;
        try {
            eGLSurface = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, iArr);
            checkEglError("eglCreatePbufferSurface");
            if (eGLSurface != null) {
                return eGLSurface;
            }
            throw new RuntimeException("surface was null");
        } catch (IllegalArgumentException e) {
            Log.e(str2, str, e);
        } catch (RuntimeException e2) {
            Log.e(str2, str, e2);
        }
    }

    /* access modifiers changed from: private */
    public final void destroyWindowSurface(EGLSurface eGLSurface) {
        if (eGLSurface != EGL10.EGL_NO_SURFACE) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroySurface(this.mEglDisplay, eGLSurface);
        }
        EGLSurface eGLSurface2 = EGL10.EGL_NO_SURFACE;
    }

    private final void checkEglError(String str) {
        int eglGetError = this.mEgl.eglGetError();
        if (eglGetError != 12288) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": EGL error: 0x");
            sb.append(Integer.toHexString(eglGetError));
            throw new RuntimeException(sb.toString());
        }
    }

    private final EGLConfig getConfig(int i, boolean z, int i2, boolean z2) {
        int i3 = i;
        int i4 = 10;
        int i5 = 12;
        int[] iArr = {12352, i3 >= 3 ? 68 : 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        if (i2 > 0) {
            iArr[10] = 12326;
            iArr[11] = 8;
        } else {
            i5 = 10;
        }
        if (z) {
            int i6 = i5 + 1;
            iArr[i5] = 12325;
            i5 = i6 + 1;
            iArr[i6] = 16;
        }
        if (z2 && BuildCheck.isAndroid4_3()) {
            int i7 = i5 + 1;
            iArr[i5] = 12610;
            i5 = i7 + 1;
            iArr[i7] = 1;
        }
        for (int length = iArr.length - 1; length >= i5; length--) {
            iArr[length] = 12344;
        }
        EGLConfig internalGetConfig = internalGetConfig(iArr);
        if (internalGetConfig == null && i3 == 2 && z2) {
            int length2 = iArr.length;
            while (true) {
                if (i4 >= length2 - 1) {
                    break;
                } else if (iArr[i4] == 12610) {
                    while (i4 < length2) {
                        iArr[i4] = 12344;
                        i4++;
                    }
                } else {
                    i4 += 2;
                }
            }
            internalGetConfig = internalGetConfig(iArr);
        }
        if (internalGetConfig != null) {
            return internalGetConfig;
        }
        Log.w(TAG, "try to fallback to RGB565");
        iArr[3] = 5;
        iArr[5] = 6;
        iArr[7] = 5;
        return internalGetConfig(iArr);
    }

    private EGLConfig internalGetConfig(int[] iArr) {
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        if (!this.mEgl.eglChooseConfig(this.mEglDisplay, iArr, eGLConfigArr, eGLConfigArr.length, new int[1])) {
            return null;
        }
        return eGLConfigArr[0];
    }
}
