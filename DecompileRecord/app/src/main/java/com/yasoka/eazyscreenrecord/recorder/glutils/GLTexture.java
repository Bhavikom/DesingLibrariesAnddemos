package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.text.TextUtils;
import java.io.IOException;

public class GLTexture implements ITexture {
    int mImageHeight;
    int mImageWidth;
    int mTexHeight;
    final float[] mTexMatrix;
    int mTexWidth;
    int mTextureId;
    final int mTextureTarget;
    final int mTextureUnit;

    public GLTexture(int i, int i2, int i3) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, i, i2, i3);
    }

    public GLTexture(int i, int i2, int i3, int i4, int i5) {
        this.mTexMatrix = new float[16];
        this.mTextureTarget = i;
        this.mTextureUnit = i2;
        int i6 = 32;
        int i7 = 32;
        while (i7 < i3) {
            i7 <<= 1;
        }
        while (i6 < i4) {
            i6 <<= 1;
        }
        if (!(this.mTexWidth == i7 && this.mTexHeight == i6)) {
            this.mTexWidth = i7;
            this.mTexHeight = i6;
        }
        this.mTextureId = GLHelper.initTex(this.mTextureTarget, i5);
        GLES20.glTexImage2D(this.mTextureTarget, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = ((float) i3) / ((float) this.mTexWidth);
        fArr[5] = ((float) i4) / ((float) this.mTexHeight);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public void release() {
        int i = this.mTextureId;
        if (i > 0) {
            GLHelper.deleteTex(i);
            this.mTextureId = 0;
        }
    }

    public void bind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, this.mTextureId);
    }

    public void unbind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, 0);
    }

    public int getTexTarget() {
        return this.mTextureTarget;
    }

    public int getTexture() {
        return this.mTextureId;
    }

    public float[] getTexMatrix() {
        return this.mTexMatrix;
    }

    public void getTexMatrix(float[] fArr, int i) {
        float[] fArr2 = this.mTexMatrix;
        System.arraycopy(fArr2, 0, fArr, i, fArr2.length);
    }

    public int getTexWidth() {
        return this.mTexWidth;
    }

    public int getTexHeight() {
        return this.mTexHeight;
    }

    public void loadTexture(String str) throws NullPointerException, IOException {
        double d;
        if (!TextUtils.isEmpty(str)) {
            Options options = new Options();
            int i = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            int i2 = options.outWidth;
            int i3 = options.outHeight;
            if (i3 > this.mTexHeight || i2 > this.mTexWidth) {
                if (i2 > i3) {
                    d = Math.ceil((double) (((float) i3) / ((float) this.mTexHeight)));
                } else {
                    d = Math.ceil((double) (((float) i2) / ((float) this.mTexWidth)));
                }
                i = (int) d;
            }
            options.inSampleSize = i;
            options.inJustDecodeBounds = false;
            loadTexture(BitmapFactory.decodeFile(str, options));
            return;
        }
        throw new NullPointerException("image file path should not be a null");
    }

    public void loadTexture(Bitmap bitmap) throws NullPointerException {
        this.mImageWidth = bitmap.getWidth();
        this.mImageHeight = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(this.mTexWidth, this.mTexHeight, Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, 0.0f, null);
        bitmap.recycle();
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = ((float) this.mImageWidth) / ((float) this.mTexWidth);
        fArr[5] = ((float) this.mImageHeight) / ((float) this.mTexHeight);
        bind();
        GLUtils.texImage2D(this.mTextureTarget, 0, createBitmap, 0);
        unbind();
        createBitmap.recycle();
    }
}
