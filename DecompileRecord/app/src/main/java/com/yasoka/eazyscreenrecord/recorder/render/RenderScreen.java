package com.yasoka.eazyscreenrecord.recorder.render;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.appsmartz.recorder.glutils.ShaderConst;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RenderScreen {
    private FloatBuffer mCameraTexCoordBuffer;
    private int mFboTexId;
    private final FloatBuffer mNormalTexCoordBuf = GlUtil.createTexCoordBuffer();
    private final FloatBuffer mNormalVtxBuf = GlUtil.createVertexBuffer();
    private final float[] mPosMtx = GlUtil.createIdentityMtx();
    private int mProgram = -1;
    private int mScreenH = -1;
    private int mScreenW = -1;
    private Watermark mWatermark;
    private Bitmap mWatermarkImg;
    private float mWatermarkRatio = 1.0f;
    private int mWatermarkTextureId = -1;
    private FloatBuffer mWatermarkVertexBuffer;
    private int maPositionHandle = -1;
    private int maTexCoordHandle = -1;
    private int muPosMtxHandle = -1;
    private int muSamplerHandle = -1;

    public RenderScreen(int i) {
        this.mFboTexId = i;
        initGL();
    }

    public void setSreenSize(int i, int i2) {
        this.mScreenW = i;
        this.mScreenH = i2;
        initCameraTexCoordBuffer();
    }

    public void setTextureId(int i) {
        this.mFboTexId = i;
    }

    public void setVideoSize(int i, int i2) {
        this.mWatermarkRatio = ((float) this.mScreenW) / ((float) i);
        if (this.mWatermark != null) {
            initWatermarkVertexBuffer();
        }
    }

    private void initCameraTexCoordBuffer() {
        int i = this.mScreenW;
        int i2 = this.mScreenH;
        float f = (float) i;
        float f2 = ((float) i) / f;
        float f3 = (float) i2;
        float f4 = ((float) i2) / f3;
        if (f2 > f4) {
            float f5 = (((float) i2) / (f3 * f2)) / 2.0f;
            float f6 = f5 + 0.5f;
            float f7 = 0.5f - f5;
            float[] fArr = {0.0f, f6, 0.0f, f7, 1.0f, f6, 1.0f, f7};
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            this.mCameraTexCoordBuffer = allocateDirect.asFloatBuffer();
            this.mCameraTexCoordBuffer.put(fArr);
            this.mCameraTexCoordBuffer.position(0);
            return;
        }
        float f8 = (((float) i) / (f * f4)) / 2.0f;
        float f9 = 0.5f - f8;
        float f10 = f8 + 0.5f;
        float[] fArr2 = {f9, 1.0f, f9, 0.0f, f10, 1.0f, f10, 0.0f};
        ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(fArr2.length * 4);
        allocateDirect2.order(ByteOrder.nativeOrder());
        this.mCameraTexCoordBuffer = allocateDirect2.asFloatBuffer();
        this.mCameraTexCoordBuffer.put(fArr2);
        this.mCameraTexCoordBuffer.position(0);
    }

    public void setWatermark(Watermark watermark) {
        this.mWatermark = watermark;
        this.mWatermarkImg = watermark.markImg;
        initWatermarkVertexBuffer();
    }

    private void initWatermarkVertexBuffer() {
        float f;
        float f2;
        float f3;
        if (this.mScreenW > 0 && this.mScreenH > 0) {
            int i = (int) (((float) this.mWatermark.width) * this.mWatermarkRatio);
            int i2 = (int) (((float) this.mWatermark.height) * this.mWatermarkRatio);
            int i3 = (int) (((float) this.mWatermark.vMargin) * this.mWatermarkRatio);
            int i4 = (int) (((float) this.mWatermark.hMargin) * this.mWatermarkRatio);
            boolean z = this.mWatermark.orientation == 1 || this.mWatermark.orientation == 2;
            boolean z2 = this.mWatermark.orientation == 2 || this.mWatermark.orientation == 4;
            int i5 = this.mScreenW;
            float f4 = (float) i4;
            float f5 = (((((float) i5) / 2.0f) - f4) - ((float) i)) / (((float) i5) / 2.0f);
            float f6 = ((((float) i5) / 2.0f) - f4) / (((float) i5) / 2.0f);
            int i6 = this.mScreenH;
            float f7 = (float) i3;
            float f8 = ((((float) i6) / 2.0f) - f7) / (((float) i6) / 2.0f);
            float f9 = (((((float) i6) / 2.0f) - f7) - ((float) i2)) / (((float) i6) / 2.0f);
            if (!z2) {
                f2 = -f6;
                f = -f5;
            } else {
                f = f6;
                f2 = f5;
            }
            if (!z) {
                f3 = -f9;
                f9 = -f8;
            } else {
                f3 = f8;
            }
            float[] fArr = {f2, f9, 0.0f, f2, f3, 0.0f, f, f9, 0.0f, f, f3, 0.0f};
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            this.mWatermarkVertexBuffer = allocateDirect.asFloatBuffer();
            this.mWatermarkVertexBuffer.put(fArr);
            this.mWatermarkVertexBuffer.position(0);
        }
    }

    public void draw() {
        if (this.mScreenW > 0 && this.mScreenH > 0) {
            GlUtil.checkGlError("draw_S");
            GLES20.glViewport(0, 0, this.mScreenW, this.mScreenH);
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            GLES20.glClear(16640);
            GLES20.glUseProgram(this.mProgram);
            this.mNormalVtxBuf.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 12, this.mNormalVtxBuf);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            this.mCameraTexCoordBuffer.position(0);
            GLES20.glVertexAttribPointer(this.maTexCoordHandle, 2, 5126, false, 8, this.mCameraTexCoordBuffer);
            GLES20.glEnableVertexAttribArray(this.maTexCoordHandle);
            GLES20.glUniformMatrix4fv(this.muPosMtxHandle, 1, false, this.mPosMtx, 0);
            GLES20.glUniform1i(this.muSamplerHandle, 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, this.mFboTexId);
            GLES20.glDrawArrays(5, 0, 4);
            drawWatermark();
            GlUtil.checkGlError("draw_E");
        }
    }

    private void drawWatermark() {
        if (this.mWatermarkImg != null) {
            this.mWatermarkVertexBuffer.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 12, this.mWatermarkVertexBuffer);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            this.mNormalTexCoordBuf.position(0);
            GLES20.glVertexAttribPointer(this.maTexCoordHandle, 2, 5126, false, 8, this.mNormalTexCoordBuf);
            GLES20.glEnableVertexAttribArray(this.maTexCoordHandle);
            if (this.mWatermarkTextureId == -1) {
                int[] iArr = new int[1];
                GLES20.glGenTextures(1, iArr, 0);
                GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, iArr[0]);
                GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, this.mWatermarkImg, 0);
                GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10241, 9729);
                GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10240, 9729);
                GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10242, 33071);
                GLES20.glTexParameteri(ShaderConst.GL_TEXTURE_2D, 10243, 33071);
                this.mWatermarkTextureId = iArr[0];
            }
            GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, this.mWatermarkTextureId);
            GLES20.glBlendFunc(770, 771);
            GLES20.glEnable(3042);
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisable(3042);
        }
    }

    private void initGL() {
        GlUtil.checkGlError("initGL_S");
        this.mProgram = GlUtil.createProgram("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform   mat4 uPosMtx;\nvarying   vec2 textureCoordinate;\nvoid main() {\n  gl_Position = uPosMtx * position;\n  textureCoordinate   = inputTextureCoordinate.xy;\n}\n", "precision mediump float;\nuniform sampler2D uSampler;\nvarying vec2  textureCoordinate;\nvoid main() {\n  gl_FragColor = texture2D(uSampler, textureCoordinate);\n}\n");
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "position");
        this.maTexCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "inputTextureCoordinate");
        this.muPosMtxHandle = GLES20.glGetUniformLocation(this.mProgram, "uPosMtx");
        this.muSamplerHandle = GLES20.glGetUniformLocation(this.mProgram, "uSampler");
        GlUtil.checkGlError("initGL_E");
    }
}
