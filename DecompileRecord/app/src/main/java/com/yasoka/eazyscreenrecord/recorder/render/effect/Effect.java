package com.yasoka.eazyscreenrecord.recorder.render.effect;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;
import com.appsmartz.recorder.glutils.ShaderConst;
import com.appsmartz.recorder.render.GlUtil;
import java.nio.FloatBuffer;
import java.util.LinkedList;

public abstract class Effect {
    private float mAngle = 270.0f;
    private final int[] mFboId = {0};
    private String mFragment;
    private int mHeight = -1;
    private final float[] mPosMtx = GlUtil.createIdentityMtx();
    private int mProgram = -1;
    private final int[] mRboId = {0};
    private final LinkedList<Runnable> mRunOnDraw = new LinkedList<>();
    private final int[] mTexId = {0};
    protected int mTextureId = -1;
    private String mVertex;
    private final FloatBuffer mVtxBuf = GlUtil.createSquareVtx();
    private int mWidth = -1;
    private int maPositionHandle = -1;
    private int maTexCoordHandle = -1;
    private int muPosMtxHandle = -1;
    private int muTexMtxHandle = -1;

    /* access modifiers changed from: protected */
    public void loadOtherParams() {
    }

    public void setShader(String str, String str2) {
        this.mVertex = str;
        this.mFragment = str2;
    }

    public void prepare(int i, int i2) {
        loadShaderAndParams(this.mVertex, this.mFragment);
        initSize(i2, i);
        createEffectTexture();
    }

    public void setTextureId(int i) {
        this.mTextureId = i;
    }

    private void loadShaderAndParams(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.e(SopCastConstant.TAG, "Couldn't load the shader, so use the null shader!");
            str = SopCastConstant.SHARDE_NULL_VERTEX;
            str2 = SopCastConstant.SHARDE_NULL_FRAGMENT;
        }
        GlUtil.checkGlError("initSH_S");
        this.mProgram = GlUtil.createProgram(str, str2);
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "position");
        this.maTexCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "inputTextureCoordinate");
        this.muPosMtxHandle = GLES20.glGetUniformLocation(this.mProgram, "uPosMtx");
        this.muTexMtxHandle = GLES20.glGetUniformLocation(this.mProgram, "uTexMtx");
        loadOtherParams();
        GlUtil.checkGlError("initSH_E");
    }

    private void initSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    private void createEffectTexture() {
        GlUtil.checkGlError("initFBO_S");
        GLES20.glGenFramebuffers(1, this.mFboId, 0);
        GLES20.glGenRenderbuffers(1, this.mRboId, 0);
        GLES20.glGenTextures(1, this.mTexId, 0);
        GLES20.glBindRenderbuffer(36161, this.mRboId[0]);
        GLES20.glRenderbufferStorage(36161, 33189, this.mWidth, this.mHeight);
        GLES20.glBindFramebuffer(36160, this.mFboId[0]);
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.mRboId[0]);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, this.mTexId[0]);
        GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10241, 9729);
        GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10240, 9729);
        GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10242, 33071);
        GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10243, 33071);
        GLES20.glTexImage2D(ShaderConst.GL_TEXTURE_2D, 0, 6408, this.mWidth, this.mHeight, 0, 6408, 5121, null);
        GLES20.glFramebufferTexture2D(36160, 36064, ShaderConst.GL_TEXTURE_2D, this.mTexId[0], 0);
        if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
            GlUtil.checkGlError("initFBO_E");
            return;
        }
        throw new RuntimeException("glCheckFramebufferStatus()");
    }

    public int getEffertedTextureId() {
        return this.mTexId[0];
    }

    /* access modifiers changed from: protected */
    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }

    /* access modifiers changed from: protected */
    public void runPendingOnDrawTasks() {
        while (!this.mRunOnDraw.isEmpty()) {
            ((Runnable) this.mRunOnDraw.removeFirst()).run();
        }
    }

    public void draw(float[] fArr) {
        if (-1 != this.mProgram && this.mTextureId != -1 && this.mWidth != -1) {
            GlUtil.checkGlError("draw_S");
            GLES20.glBindFramebuffer(36160, this.mFboId[0]);
            GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16640);
            GLES20.glUseProgram(this.mProgram);
            runPendingOnDrawTasks();
            this.mVtxBuf.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, this.mVtxBuf);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            this.mVtxBuf.position(3);
            GLES20.glVertexAttribPointer(this.maTexCoordHandle, 2, 5126, false, 20, this.mVtxBuf);
            GLES20.glEnableVertexAttribArray(this.maTexCoordHandle);
            int i = this.muPosMtxHandle;
            if (i >= 0) {
                GLES20.glUniformMatrix4fv(i, 1, false, this.mPosMtx, 0);
            }
            int i2 = this.muTexMtxHandle;
            if (i2 >= 0) {
                GLES20.glUniformMatrix4fv(i2, 1, false, fArr, 0);
            }
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(ShaderConst.GL_TEXTURE_EXTERNAL_OES, this.mTextureId);
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glBindFramebuffer(36160, 0);
            GlUtil.checkGlError("draw_E");
        }
    }

    /* access modifiers changed from: protected */
    public void setInteger(final int i, final int i2) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1i(i, i2);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloat(final int i, final float f) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1f(i, f);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec2(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec3(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform3fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec4(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform4fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatArray(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                int i = i;
                float[] fArr = fArr;
                GLES20.glUniform1fv(i, fArr.length, FloatBuffer.wrap(fArr));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setPoint(final int i, final PointF pointF) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(i, 1, new float[]{pointF.x, pointF.y}, 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setUniformMatrix3f(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix3fv(i, 1, false, fArr, 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setUniformMatrix4f(final int i, final float[] fArr) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix4fv(i, 1, false, fArr, 0);
            }
        });
    }

    public void release() {
        int i = this.mProgram;
        if (-1 != i) {
            GLES20.glDeleteProgram(i);
            this.mProgram = -1;
        }
    }
}
