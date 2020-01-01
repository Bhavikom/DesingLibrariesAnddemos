package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.graphics.Bitmap;
import java.io.IOException;

public interface ITexture {
    void bind();

    int getTexHeight();

    void getTexMatrix(float[] fArr, int i);

    float[] getTexMatrix();

    int getTexTarget();

    int getTexWidth();

    int getTexture();

    void loadTexture(Bitmap bitmap) throws NullPointerException;

    void loadTexture(String str) throws NullPointerException, IOException;

    void release();

    void unbind();
}
