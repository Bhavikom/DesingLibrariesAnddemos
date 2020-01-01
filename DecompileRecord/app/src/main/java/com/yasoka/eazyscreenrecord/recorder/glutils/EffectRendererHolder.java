package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import com.appsmartz.recorder.glutils.EGLBase.IContext;

public class EffectRendererHolder extends AbstractRendererHolder {
    public static final int EFFECT_BIN = 3;
    public static final int EFFECT_BIN_GREEN = 5;
    public static final int EFFECT_BIN_REVERSE = 6;
    public static final int EFFECT_BIN_REVERSE_GREEN = 8;
    public static final int EFFECT_BIN_REVERSE_YELLOW = 7;
    public static final int EFFECT_BIN_YELLOW = 4;
    public static final int EFFECT_EMPHASIZE_RED_YELLOW = 9;
    public static final int EFFECT_EMPHASIZE_RED_YELLOW_WHITE = 10;
    public static final int EFFECT_EMPHASIZE_YELLOW_WHITE = 11;
    public static final int EFFECT_GRAY = 1;
    public static final int EFFECT_GRAY_REVERSE = 2;
    public static final int EFFECT_NON = 0;
    public static final int EFFECT_NUM = 12;
    private static final String FRAGMENT_SHADER_BIN_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * bin, 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_GREEN_OES;
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_OES;
    private static final String FRAGMENT_SHADER_BIN_REVERSE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * (vec3(1.0, 1.0, 1.0) - bin), 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_REVERSE_GREEN_OES;
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_REVERSE_OES;
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_REVERSE_YELLOW_OES;
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_BIN_YELLOW_OES;
    private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_OES;
    private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else if ((hsv.g < uParams[12]) && (hsv.b < uParams[13])) {\n        hsv = hsv * vec3(1.0, 0.0, 2.0);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_OES;
    private static final String FRAGMENT_SHADER_EMPHASIZE_YELLOW_WHITE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 rgb = texture2D(sTexture, vTextureCoord).rgb;\n    vec3 hsv = rgb2hsv(rgb);\n    if (   ((hsv.r >= uParams[0]) && (hsv.r <= uParams[1]))\n        && ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else if ((hsv.g < uParams[12]) && (hsv.b > uParams[13])) {\n        hsv = hsv * vec3(1.0, 0.0, 2.0);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_EMPHASIZE_YELLOW_WHITE_OES;
    private static final String FRAGMENT_SHADER_GRAY_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(cl3, 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_GRAY_OES;
    private static final String FRAGMENT_SHADER_GRAY_REVERSE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(clamp(vec3(1.0, 1.0, 1.0) - cl3, 0.0, 1.0), 1.0);\n}\n";
    /* access modifiers changed from: private */
    public static final String FRAGMENT_SHADER_GRAY_REVERSE_OES;
    private static final int MAX_PARAM_NUM = 18;
    private static final int REQUEST_CHANGE_EFFECT = 100;
    private static final int REQUEST_SET_PARAMS = 101;
    /* access modifiers changed from: private */
    public static final String TAG = "EffectRendererHolder";

    protected static final class MyRendererTask extends RendererTask {
        private float[] mCurrentParams;
        /* access modifiers changed from: private */
        public int mEffect;
        private final SparseArray<float[]> mParams = new SparseArray<>();
        private int muParamsLoc;

        public MyRendererTask(EffectRendererHolder effectRendererHolder, int i, int i2) {
            super(effectRendererHolder, i, i2);
        }

        public MyRendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, IContext iContext, int i4) {
            super(abstractRendererHolder, i, i2, i3, iContext, i4);
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"NewApi"})
        public void internalOnStart() {
            super.internalOnStart();
            this.mParams.clear();
            this.mParams.put(9, new float[]{0.17f, 0.85f, 0.5f, 1.0f, 0.4f, 1.0f, 1.0f, 1.0f, 5.0f, 1.0f, 1.0f, 1.0f});
            this.mParams.put(10, new float[]{0.17f, 0.85f, 0.5f, 1.0f, 0.4f, 1.0f, 1.0f, 1.0f, 5.0f, 1.0f, 1.0f, 1.0f});
            this.mParams.put(11, new float[]{0.1f, 0.19f, 0.3f, 1.0f, 0.3f, 1.0f, 1.0f, 1.0f, 5.0f, 1.0f, 0.8f, 0.8f, 0.15f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f});
            this.mEffect = 0;
            handleChangeEffect(0);
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) {
            if (i == 100) {
                handleChangeEffect(i2);
            } else if (i != 101) {
                return super.processRequest(i, i2, i3, obj);
            } else {
                handleSetParam(i2, (float[]) obj);
            }
            return null;
        }

        public void changeEffect(int i) {
            checkFinished();
            if (this.mEffect != i) {
                offer(100, i);
            }
        }

        public void setParams(int i, @NonNull float[] fArr) {
            checkFinished();
            offer(101, i, 0, fArr);
        }

        /* access modifiers changed from: protected */
        public void handleChangeEffect(int i) {
            this.mEffect = i;
            switch (i) {
                case 0:
                    this.mDrawer.updateShader(ShaderConst.FRAGMENT_SHADER_SIMPLE_OES);
                    break;
                case 1:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_GRAY_OES);
                    break;
                case 2:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_GRAY_REVERSE_OES);
                    break;
                case 3:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_OES);
                    break;
                case 4:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_YELLOW_OES);
                    break;
                case 5:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_GREEN_OES);
                    break;
                case 6:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_REVERSE_OES);
                    break;
                case 7:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_REVERSE_YELLOW_OES);
                    break;
                case 8:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_BIN_REVERSE_GREEN_OES);
                    break;
                case 9:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_OES);
                    break;
                case 10:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_OES);
                    break;
                case 11:
                    this.mDrawer.updateShader(EffectRendererHolder.FRAGMENT_SHADER_EMPHASIZE_YELLOW_WHITE_OES);
                    break;
                default:
                    try {
                        ((EffectRendererHolder) getParent()).handleDefaultEffect(i, this.mDrawer);
                        break;
                    } catch (Exception e) {
                        this.mDrawer.resetShader();
                        Log.w(EffectRendererHolder.TAG, e);
                        break;
                    }
            }
            this.muParamsLoc = this.mDrawer.glGetUniformLocation("uParams");
            this.mCurrentParams = (float[]) this.mParams.get(i);
            updateParams();
        }

        private void handleSetParam(int i, @NonNull float[] fArr) {
            if (i < 0 || this.mEffect == i) {
                this.mCurrentParams = fArr;
                this.mParams.put(this.mEffect, fArr);
                updateParams();
                return;
            }
            this.mParams.put(i, fArr);
        }

        private void updateParams() {
            float[] fArr = this.mCurrentParams;
            int min = Math.min(fArr != null ? fArr.length : 0, 18);
            if (this.muParamsLoc >= 0 && min > 0) {
                this.mDrawer.glUseProgram();
                GLES20.glUniform1fv(this.muParamsLoc, min, this.mCurrentParams, 0);
            }
        }
    }

    static {
        String str = ShaderConst.HEADER_OES;
        String str2 = ShaderConst.SAMPLER_OES;
        FRAGMENT_SHADER_GRAY_OES = String.format(FRAGMENT_SHADER_GRAY_BASE, new Object[]{str, str2});
        FRAGMENT_SHADER_GRAY_REVERSE_OES = String.format(FRAGMENT_SHADER_GRAY_REVERSE_BASE, new Object[]{str, str2});
        String str3 = "1.0, 1.0, 1.0";
        Object[] objArr = {str, str2, str3};
        String str4 = FRAGMENT_SHADER_BIN_BASE;
        FRAGMENT_SHADER_BIN_OES = String.format(str4, objArr);
        String str5 = "1.0, 1.0, 0.0";
        FRAGMENT_SHADER_BIN_YELLOW_OES = String.format(str4, new Object[]{str, str2, str5});
        String str6 = "0.0, 1.0, 0.0";
        FRAGMENT_SHADER_BIN_GREEN_OES = String.format(str4, new Object[]{str, str2, str6});
        Object[] objArr2 = {str, str2, str3};
        String str7 = FRAGMENT_SHADER_BIN_REVERSE_BASE;
        FRAGMENT_SHADER_BIN_REVERSE_OES = String.format(str7, objArr2);
        FRAGMENT_SHADER_BIN_REVERSE_YELLOW_OES = String.format(str7, new Object[]{str, str2, str5});
        FRAGMENT_SHADER_BIN_REVERSE_GREEN_OES = String.format(str7, new Object[]{str, str2, str6});
        FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_OES = String.format(FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_BASE, new Object[]{str, str2});
        FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_OES = String.format(FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_BASE, new Object[]{str, str2});
        FRAGMENT_SHADER_EMPHASIZE_YELLOW_WHITE_OES = String.format(FRAGMENT_SHADER_EMPHASIZE_YELLOW_WHITE_BASE, new Object[]{str, str2});
    }

    public EffectRendererHolder(int i, int i2, @Nullable RenderHolderCallback renderHolderCallback) {
        this(i, i2, 3, null, 2, renderHolderCallback);
    }

    public EffectRendererHolder(int i, int i2, int i3, IContext iContext, int i4, @Nullable RenderHolderCallback renderHolderCallback) {
        super(i, i2, i3, iContext, i4, renderHolderCallback);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public RendererTask createRendererTask(int i, int i2, int i3, IContext iContext, int i4) {
        MyRendererTask myRendererTask = new MyRendererTask(this, i, i2, i3, iContext, i4);
        return myRendererTask;
    }

    public void changeEffect(int i) {
        ((MyRendererTask) this.mRendererTask).changeEffect(i);
    }

    public int getCurrentEffect() {
        return ((MyRendererTask) this.mRendererTask).mEffect;
    }

    public void setParams(@NonNull float[] fArr) {
        ((MyRendererTask) this.mRendererTask).setParams(-1, fArr);
    }

    public void setParams(int i, @NonNull float[] fArr) throws IllegalArgumentException {
        if (i > 0) {
            ((MyRendererTask) this.mRendererTask).setParams(i, fArr);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("invalid effect number:");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void handleDefaultEffect(int i, @NonNull IDrawer2dES2 iDrawer2dES2) {
        if (iDrawer2dES2 instanceof GLDrawer2D) {
            ((GLDrawer2D) iDrawer2dES2).resetShader();
        }
    }
}
