package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;
import android.util.Log;
import com.appsmartz.recorder.utils.BuildCheck;
import java.io.IOException;

public final class GLHelper {
    private static final String TAG = "GLHelper";

    public static void checkGlError(String str) {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": glError 0x");
            sb.append(Integer.toHexString(glGetError));
            String sb2 = sb.toString();
            Log.e(TAG, sb2);
            new Throwable(sb2).printStackTrace();
        }
    }

    public static int initTex(int i, int i2) {
        return initTex(i, 33984, i2, i2, 33071);
    }

    public static int initTex(int i, int i2, int i3, int i4, int i5) {
        int[] iArr = new int[1];
        GLES20.glActiveTexture(i2);
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(i, iArr[0]);
        GLES20.glTexParameteri(i, 10242, i5);
        GLES20.glTexParameteri(i, 10243, i5);
        GLES20.glTexParameteri(i, 10241, i3);
        GLES20.glTexParameteri(i, 10240, i4);
        return iArr[0];
    }

    public static int[] initTexes(int i, int i2, int i3) {
        return initTexes(new int[i], i2, i3, i3, 33071);
    }

    public static int[] initTexes(@NonNull int[] iArr, int i, int i2) {
        return initTexes(iArr, i, i2, i2, 33071);
    }

    public static int[] initTexes(int i, int i2, int i3, int i4, int i5) {
        return initTexes(new int[i], i2, i3, i4, i5);
    }

    public static int[] initTexes(@NonNull int[] iArr, int i, int i2, int i3, int i4) {
        int[] iArr2 = new int[1];
        GLES20.glGetIntegerv(34930, iArr2, 0);
        StringBuilder sb = new StringBuilder();
        sb.append("GL_MAX_TEXTURE_IMAGE_UNITS=");
        sb.append(iArr2[0]);
        Log.v(TAG, sb.toString());
        int length = iArr.length > iArr2[0] ? iArr2[0] : iArr.length;
        for (int i5 = 0; i5 < length; i5++) {
            iArr[i5] = initTex(i, ShaderConst.TEX_NUMBERS[i5], i2, i3, i4);
        }
        return iArr;
    }

    public static int[] initTexes(int i, int i2, int i3, int i4, int i5, int i6) {
        return initTexes(new int[i], i2, i3, i4, i5, i6);
    }

    public static int[] initTexes(@NonNull int[] iArr, int i, int i2, int i3) {
        return initTexes(iArr, i, i2, i3, i3, 33071);
    }

    public static int[] initTexes(@NonNull int[] iArr, int i, int i2, int i3, int i4, int i5) {
        int[] iArr2 = new int[1];
        GLES20.glGetIntegerv(34930, iArr2, 0);
        int length = iArr.length > iArr2[0] ? iArr2[0] : iArr.length;
        for (int i6 = 0; i6 < length; i6++) {
            iArr[i6] = initTex(i, i2, i3, i4, i5);
        }
        return iArr;
    }

    public static void deleteTex(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static void deleteTex(@NonNull int[] iArr) {
        GLES20.glDeleteTextures(iArr.length, iArr, 0);
    }

    public static int loadTextureFromResource(Context context, int i) {
        return loadTextureFromResource(context, i, null);
    }

    @SuppressLint({"NewApi"})
    public static int loadTextureFromResource(Context context, int i, Theme theme) {
        Drawable drawable;
        Bitmap createBitmap = Bitmap.createBitmap(256, 256, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawARGB(0, 0, 255, 0);
        if (BuildCheck.isAndroid5()) {
            drawable = context.getResources().getDrawable(i, theme);
        } else {
            drawable = context.getResources().getDrawable(i);
        }
        drawable.setBounds(0, 0, 256, 256);
        drawable.draw(canvas);
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, iArr[0]);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10241, 9728.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10240, 9729.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10242, 10497.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10243, 10497.0f);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createBitmap, 0);
        createBitmap.recycle();
        return iArr[0];
    }

    public static int createTextureWithTextContent(String str) {
        Bitmap createBitmap = Bitmap.createBitmap(256, 256, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawARGB(0, 0, 255, 0);
        Paint paint = new Paint();
        paint.setTextSize(32.0f);
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        canvas.drawText(str, 16.0f, 112.0f, paint);
        int initTex = initTex(ShaderConst.GL_TEXTURE_2D, 33984, 9728, 9729, 10497);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createBitmap, 0);
        createBitmap.recycle();
        return initTex;
    }

    public static int loadShader(@NonNull Context context, String str, String str2) {
        try {
            return loadShader(AssetsHelper.loadString(context.getAssets(), str), AssetsHelper.loadString(context.getAssets(), str));
        } catch (IOException unused) {
            return 0;
        }
    }

    public static int loadShader(String str, String str2) {
        int[] iArr = new int[1];
        int loadShader = loadShader(35633, str);
        if (loadShader == 0) {
            return 0;
        }
        int loadShader2 = loadShader(35632, str2);
        if (loadShader2 == 0) {
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        checkGlError("glCreateProgram");
        String str3 = TAG;
        if (glCreateProgram == 0) {
            Log.e(str3, "Could not create program");
        }
        GLES20.glAttachShader(glCreateProgram, loadShader);
        String str4 = "glAttachShader";
        checkGlError(str4);
        GLES20.glAttachShader(glCreateProgram, loadShader2);
        checkGlError(str4);
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr2 = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr2, 0);
        if (iArr2[0] == 1) {
            return glCreateProgram;
        }
        Log.e(str3, "Could not link program: ");
        Log.e(str3, GLES20.glGetProgramInfoLog(glCreateProgram));
        GLES20.glDeleteProgram(glCreateProgram);
        return 0;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        StringBuilder sb = new StringBuilder();
        sb.append("glCreateShader type=");
        sb.append(i);
        checkGlError(sb.toString());
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Could not compile shader ");
        sb2.append(i);
        sb2.append(":");
        String sb3 = sb2.toString();
        String str2 = TAG;
        Log.e(str2, sb3);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" ");
        sb4.append(GLES20.glGetShaderInfoLog(glCreateShader));
        Log.e(str2, sb4.toString());
        GLES20.glDeleteShader(glCreateShader);
        return 0;
    }

    public static void checkLocation(int i, String str) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to locate '");
            sb.append(str);
            sb.append("' in program");
            throw new RuntimeException(sb.toString());
        }
    }

    @SuppressLint({"InlinedApi"})
    public static void logVersionInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("vendor  : ");
        sb.append(GLES20.glGetString(7936));
        String sb2 = sb.toString();
        String str = TAG;
        Log.i(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("renderer: ");
        sb3.append(GLES20.glGetString(7937));
        Log.i(str, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("version : ");
        sb4.append(GLES20.glGetString(7938));
        Log.i(str, sb4.toString());
        if (BuildCheck.isAndroid4_3()) {
            int[] iArr = new int[1];
            GLES30.glGetIntegerv(33307, iArr, 0);
            int i = iArr[0];
            GLES30.glGetIntegerv(33308, iArr, 0);
            int i2 = iArr[0];
            if (GLES30.glGetError() == 0) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("version: ");
                sb5.append(i);
                sb5.append(".");
                sb5.append(i2);
                Log.i(str, sb5.toString());
            }
        }
    }
}
