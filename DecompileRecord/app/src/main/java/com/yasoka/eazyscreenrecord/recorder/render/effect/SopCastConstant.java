package com.yasoka.eazyscreenrecord.recorder.render.effect;

public class SopCastConstant {
    public static final String BRANCH = "open-source";
    public static final String SHARDE_NULL_FRAGMENT = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 textureCoordinate;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, textureCoordinate);\n    gl_FragColor = vec4(tc.r, tc.g, tc.b, 1.0);\n}";
    public static final String SHARDE_NULL_VERTEX = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform   mat4 uPosMtx;\nuniform   mat4 uTexMtx;\nvarying   vec2 textureCoordinate;\nvoid main() {\n  gl_Position = uPosMtx * position;\n  textureCoordinate   = (uTexMtx * inputTextureCoordinate).xy;\n}";
    public static final String TAG = "SopCast";
    public static final String VERSION = "1.0";
}
