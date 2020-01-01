package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLDrawer2D implements IDrawer2dES2 {
    private static final int FLOAT_SZ = 4;
    private static final float[] TEXCOORD = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
    private static final float[] VERTICES = {1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
    private final int VERTEX_NUM;
    private final int VERTEX_SZ;
    private int hProgram;
    private final float[] mMvpMatrix;
    private final int mTexTarget;
    int maPositionLoc;
    int maTextureCoordLoc;
    int muMVPMatrixLoc;
    int muTexMatrixLoc;
    private final FloatBuffer pTexCoord;
    private final FloatBuffer pVertex;

    public GLDrawer2D(boolean z) {
        this(VERTICES, TEXCOORD, z);
    }

    public GLDrawer2D(float[] fArr, float[] fArr2, boolean z) {
        this.mMvpMatrix = new float[16];
        this.VERTEX_NUM = Math.min(fArr != null ? fArr.length : 0, fArr2 != null ? fArr2.length : 0) / 2;
        this.VERTEX_SZ = this.VERTEX_NUM * 2;
        this.mTexTarget = z ? ShaderConst.GL_TEXTURE_EXTERNAL_OES : ShaderConst.GL_TEXTURE_2D;
        this.pVertex = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pVertex.put(fArr);
        this.pVertex.flip();
        this.pTexCoord = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pTexCoord.put(fArr2);
        this.pTexCoord.flip();
        String str = ShaderConst.VERTEX_SHADER;
        if (z) {
            this.hProgram = GLHelper.loadShader(str, ShaderConst.FRAGMENT_SHADER_SIMPLE_OES);
        } else {
            this.hProgram = GLHelper.loadShader(str, ShaderConst.FRAGMENT_SHADER_SIMPLE);
        }
        Matrix.setIdentityM(this.mMvpMatrix, 0);
        init();
    }

    public void release() {
        int i = this.hProgram;
        if (i >= 0) {
            GLES20.glDeleteProgram(i);
        }
        this.hProgram = -1;
    }

    public boolean isOES() {
        return this.mTexTarget == 36197;
    }

    public float[] getMvpMatrix() {
        return this.mMvpMatrix;
    }

    public IDrawer2D setMvpMatrix(float[] fArr, int i) {
        System.arraycopy(fArr, i, this.mMvpMatrix, 0, 16);
        return this;
    }

    public void getMvpMatrix(float[] fArr, int i) {
        System.arraycopy(this.mMvpMatrix, 0, fArr, i, 16);
    }

    public synchronized void draw(int i, float[] fArr, int i2) {
        if (this.hProgram >= 0) {
            GLES20.glUseProgram(this.hProgram);
            if (fArr != null) {
                GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, fArr, i2);
            }
            GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(this.mTexTarget, i);
            GLES20.glDrawArrays(5, 0, this.VERTEX_NUM);
            GLES20.glBindTexture(this.mTexTarget, 0);
            GLES20.glUseProgram(0);
        }
    }

    public void draw(ITexture iTexture) {
        draw(iTexture.getTexture(), iTexture.getTexMatrix(), 0);
    }

    public void draw(TextureOffscreen textureOffscreen) {
        draw(textureOffscreen.getTexture(), textureOffscreen.getTexMatrix(), 0);
    }

    public int initTex() {
        return GLHelper.initTex(this.mTexTarget, 9728);
    }

    public void deleteTex(int i) {
        GLHelper.deleteTex(i);
    }

    public synchronized void updateShader(String str, String str2) {
        release();
        this.hProgram = GLHelper.loadShader(str, str2);
        init();
    }

    public void updateShader(String str) {
        updateShader(ShaderConst.VERTEX_SHADER, str);
    }

    public void resetShader() {
        release();
        boolean isOES = isOES();
        String str = ShaderConst.VERTEX_SHADER;
        if (isOES) {
            this.hProgram = GLHelper.loadShader(str, ShaderConst.FRAGMENT_SHADER_SIMPLE_OES);
        } else {
            this.hProgram = GLHelper.loadShader(str, ShaderConst.FRAGMENT_SHADER_SIMPLE);
        }
        init();
    }

    public int glGetAttribLocation(String str) {
        GLES20.glUseProgram(this.hProgram);
        return GLES20.glGetAttribLocation(this.hProgram, str);
    }

    public int glGetUniformLocation(String str) {
        GLES20.glUseProgram(this.hProgram);
        return GLES20.glGetUniformLocation(this.hProgram, str);
    }

    public void glUseProgram() {
        GLES20.glUseProgram(this.hProgram);
    }

    private void init() {
        GLES20.glUseProgram(this.hProgram);
        this.maPositionLoc = GLES20.glGetAttribLocation(this.hProgram, "aPosition");
        this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.hProgram, "aTextureCoord");
        this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uMVPMatrix");
        this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uTexMatrix");
        GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, this.mMvpMatrix, 0);
        GLES20.glVertexAttribPointer(this.maPositionLoc, 2, 5126, false, this.VERTEX_SZ, this.pVertex);
        GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, this.VERTEX_SZ, this.pTexCoord);
        GLES20.glEnableVertexAttribArray(this.maPositionLoc);
        GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
    }
}
