package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.appsmartz.recorder.glutils.EGLBase.IConfig;
import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.glutils.EGLBase.IEglSurface;
import com.appsmartz.recorder.utils.BuildCheck;

@TargetApi(18)
class EGLBase14 extends EGLBase {
    private static final Context EGL_NO_CONTEXT = new Context(EGL14.EGL_NO_CONTEXT);
    private static final String TAG = "EGLBase14";
    @NonNull
    private Context mContext = EGL_NO_CONTEXT;
    private EGLContext mDefaultContext = EGL14.EGL_NO_CONTEXT;
    private Config mEglConfig = null;
    /* access modifiers changed from: private */
    public EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
    private int mGlVersion = 2;
    private final int[] mSurfaceDimension = new int[2];

    public static class Config extends IConfig {
        public final EGLConfig eglConfig;

        private Config(EGLConfig eGLConfig) {
            this.eglConfig = eGLConfig;
        }
    }

    public static class Context extends IContext {
        public final EGLContext eglContext;

        private Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }

        @SuppressLint({"NewApi"})
        public long getNativeHandle() {
            if (this.eglContext != null) {
                return BuildCheck.isLollipop() ? this.eglContext.getNativeHandle() : (long) this.eglContext.getHandle();
            }
            return 0;
        }

        public Object getEGLContext() {
            return this.eglContext;
        }
    }

    public static class EglSurface implements IEglSurface {
        private final EGLBase14 mEglBase;
        private EGLSurface mEglSurface;

        private EglSurface(EGLBase14 eGLBase14, Object obj) throws IllegalArgumentException {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            this.mEglBase = eGLBase14;
            if ((obj instanceof Surface) || (obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture) || (obj instanceof SurfaceView)) {
                this.mEglSurface = this.mEglBase.createWindowSurface(obj);
                return;
            }
            throw new IllegalArgumentException("unsupported surface");
        }

        private EglSurface(EGLBase14 eGLBase14, int i, int i2) {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            this.mEglBase = eGLBase14;
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

        public void setPresentationTime(long j) {
            EGLExt.eglPresentationTimeANDROID(this.mEglBase.mEglDisplay, this.mEglSurface, j);
        }

        public IContext getContext() {
            return this.mEglBase.getContext();
        }

        public boolean isValid() {
            EGLSurface eGLSurface = this.mEglSurface;
            return eGLSurface != null && eGLSurface != EGL14.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        public void release() {
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    public EGLBase14(int i, Context context, boolean z, int i2, boolean z2) {
        init(i, context, z, i2, z2);
    }

    public void release() {
        if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            destroyContext();
            EGL14.eglTerminate(this.mEglDisplay);
            EGL14.eglReleaseThread();
        }
        this.mEglDisplay = EGL14.EGL_NO_DISPLAY;
        this.mContext = EGL_NO_CONTEXT;
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

    public String queryString(int i) {
        return EGL14.eglQueryString(this.mEglDisplay, i);
    }

    public int getGlVersion() {
        return this.mGlVersion;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Config getConfig() {
        return this.mEglConfig;
    }

    public void makeDefault() {
        if (!EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            StringBuilder sb = new StringBuilder();
            sb.append("makeDefault");
            sb.append(EGL14.eglGetError());
            Log.w("TAG", sb.toString());
        }
    }

    public void sync() {
        EGL14.eglWaitGL();
        EGL14.eglWaitNative(12379);
    }

    private void init(int i, Context context, boolean z, int i2, boolean z2) {
        if (this.mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            this.mEglDisplay = EGL14.eglGetDisplay(0);
            if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
                int[] iArr = new int[2];
                if (EGL14.eglInitialize(this.mEglDisplay, iArr, 0, iArr, 1)) {
                    if (context == null) {
                        context = EGL_NO_CONTEXT;
                    }
                    if (i >= 3) {
                        EGLConfig config = getConfig(3, z, i2, z2);
                        if (config != null) {
                            EGLContext createContext = createContext(context, config, 3);
                            if (EGL14.eglGetError() == 12288) {
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
                        if (context2 == null || context2.eglContext == EGL14.EGL_NO_CONTEXT) {
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
                    if (context3 == null || context3.eglContext == EGL14.EGL_NO_CONTEXT) {
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
                    int[] iArr2 = new int[1];
                    EGL14.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, iArr2, 0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("EGLContext created, client version ");
                    sb.append(iArr2[0]);
                    Log.d(TAG, sb.toString());
                    makeDefault();
                    return;
                }
                this.mEglDisplay = null;
                throw new RuntimeException("eglInitialize failed");
            }
            throw new RuntimeException("eglGetDisplay failed");
        }
        throw new RuntimeException("EGL already set up");
    }

    /* access modifiers changed from: private */
    public boolean makeCurrent(EGLSurface eGLSurface) {
        if (eGLSurface == null || eGLSurface == EGL14.EGL_NO_SURFACE) {
            if (EGL14.eglGetError() == 12299) {
                Log.e(TAG, "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        } else if (EGL14.eglMakeCurrent(this.mEglDisplay, eGLSurface, eGLSurface, this.mContext.eglContext)) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("eglMakeCurrent");
            sb.append(EGL14.eglGetError());
            Log.w("TAG", sb.toString());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public int swap(EGLSurface eGLSurface) {
        if (!EGL14.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return EGL14.eglGetError();
        }
        return 12288;
    }

    /* access modifiers changed from: private */
    public int swap(EGLSurface eGLSurface, long j) {
        EGLExt.eglPresentationTimeANDROID(this.mEglDisplay, eGLSurface, j);
        if (!EGL14.eglSwapBuffers(this.mEglDisplay, eGLSurface)) {
            return EGL14.eglGetError();
        }
        return 12288;
    }

    private EGLContext createContext(Context context, EGLConfig eGLConfig, int i) {
        return EGL14.eglCreateContext(this.mEglDisplay, eGLConfig, context.eglContext, new int[]{12440, i, 12344}, 0);
    }

    private void destroyContext() {
        boolean eglDestroyContext = EGL14.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext);
        String str = "eglDestroyContext:";
        String str2 = TAG;
        String str3 = " context: ";
        String str4 = "display:";
        String str5 = "destroyContext";
        if (!eglDestroyContext) {
            StringBuilder sb = new StringBuilder();
            sb.append(str4);
            sb.append(this.mEglDisplay);
            sb.append(str3);
            sb.append(this.mContext.eglContext);
            Log.e(str5, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(EGL14.eglGetError());
            Log.e(str2, sb2.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
        if (this.mDefaultContext != EGL14.EGL_NO_CONTEXT) {
            if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mDefaultContext)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(this.mEglDisplay);
                sb3.append(str3);
                sb3.append(this.mDefaultContext);
                Log.e(str5, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(EGL14.eglGetError());
                Log.e(str2, sb4.toString());
            }
            this.mDefaultContext = EGL14.EGL_NO_CONTEXT;
        }
    }

    /* access modifiers changed from: private */
    public final int getSurfaceWidth(EGLSurface eGLSurface) {
        if (!EGL14.eglQuerySurface(this.mEglDisplay, eGLSurface, 12375, this.mSurfaceDimension, 0)) {
            this.mSurfaceDimension[0] = 0;
        }
        return this.mSurfaceDimension[0];
    }

    /* access modifiers changed from: private */
    public final int getSurfaceHeight(EGLSurface eGLSurface) {
        if (!EGL14.eglQuerySurface(this.mEglDisplay, eGLSurface, 12374, this.mSurfaceDimension, 1)) {
            this.mSurfaceDimension[1] = 0;
        }
        return this.mSurfaceDimension[1];
    }

    /* access modifiers changed from: private */
    public final EGLSurface createWindowSurface(Object obj) {
        String str = TAG;
        try {
            EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, obj, new int[]{12344}, 0);
            if (eglCreateWindowSurface != null) {
                if (eglCreateWindowSurface != EGL14.EGL_NO_SURFACE) {
                    makeCurrent(eglCreateWindowSurface);
                    return eglCreateWindowSurface;
                }
            }
            int eglGetError = EGL14.eglGetError();
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
        EGLSurface eGLSurface = null;
        try {
            eGLSurface = EGL14.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, iArr, 0);
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
    public void destroyWindowSurface(EGLSurface eGLSurface) {
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.mEglDisplay, eGLSurface);
        }
        EGLSurface eGLSurface2 = EGL14.EGL_NO_SURFACE;
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError != 12288) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": EGL error: 0x");
            sb.append(Integer.toHexString(eglGetError));
            throw new RuntimeException(sb.toString());
        }
    }

    private EGLConfig getConfig(int i, boolean z, int i2, boolean z2) {
        int i3 = i;
        int i4 = 10;
        int i5 = 12;
        int[] iArr = {12352, i3 >= 3 ? 68 : 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        if (i2 > 0) {
            iArr[10] = 12326;
            iArr[11] = i2;
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
        if (!EGL14.eglChooseConfig(this.mEglDisplay, iArr, 0, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0)) {
            return null;
        }
        return eGLConfigArr[0];
    }
}
