package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.opengl.GLES20;
import android.view.MotionEvent;
import java.nio.FloatBuffer;

public class Texture2dProgram {
    private static final boolean DEBUG = false;
    private static final String TAG = "Texture2dProgram";
    private float mColorAdjust;
    private final int[] mFlags;
    protected boolean mHasKernel2;
    private final float[] mKernel;
    private final float[] mLastTouchPosition;
    private int mProgramHandle;
    private final ProgramType mProgramType;
    private final float[] mSummedTouchPosition;
    private final Object mSync;
    private float mTexHeight;
    private float[] mTexOffset;
    private float mTexWidth;
    private int mTextureTarget;
    private final int maPositionLoc;
    private final int maTextureCoordLoc;
    private int muColorAdjustLoc;
    private int muFlagsLoc;
    private int muKernelLoc;
    private final int muMVPMatrixLoc;
    private final int muTexMatrixLoc;
    private int muTexOffsetLoc;
    private int muTouchPositionLoc;

    /* renamed from: com.appsmartz.recorder.glutils.Texture2dProgram$1 */
    static /* synthetic */ class C05761 {

        /* renamed from: $SwitchMap$com$appsmartz$recorder$glutils$Texture2dProgram$ProgramType */
        static final /* synthetic */ int[] f49xa6249d9a = new int[ProgramType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(32:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(34:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType[] r0 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f49xa6249d9a = r0
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_2D     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x001f }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_FILT3x3     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x002a }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_BW     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_NIGHT     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x004b }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_CHROMA_KEY     // Catch:{ NoSuchFieldError -> 0x004b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0056 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_SQUEEZE     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0062 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_TWIRL     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x006e }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_TUNNEL     // Catch:{ NoSuchFieldError -> 0x006e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006e }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006e }
            L_0x006e:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x007a }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_BULGE     // Catch:{ NoSuchFieldError -> 0x007a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x007a }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x007a }
            L_0x007a:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0086 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_FISHEYE     // Catch:{ NoSuchFieldError -> 0x0086 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0086 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0086 }
            L_0x0086:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x0092 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_DENT     // Catch:{ NoSuchFieldError -> 0x0092 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0092 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0092 }
            L_0x0092:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x009e }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_MIRROR     // Catch:{ NoSuchFieldError -> 0x009e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009e }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009e }
            L_0x009e:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x00aa }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_STRETCH     // Catch:{ NoSuchFieldError -> 0x00aa }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00aa }
                r2 = 14
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00aa }
            L_0x00aa:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x00b6 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_EXT_FILT3x3     // Catch:{ NoSuchFieldError -> 0x00b6 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b6 }
                r2 = 15
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b6 }
            L_0x00b6:
                int[] r0 = f49xa6249d9a     // Catch:{ NoSuchFieldError -> 0x00c2 }
                com.appsmartz.recorder.glutils.Texture2dProgram$ProgramType r1 = com.appsmartz.recorder.glutils.Texture2dProgram.ProgramType.TEXTURE_CUSTOM     // Catch:{ NoSuchFieldError -> 0x00c2 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c2 }
                r2 = 16
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c2 }
            L_0x00c2:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.Texture2dProgram.C05761.<clinit>():void");
        }
    }

    public enum ProgramType {
        TEXTURE_2D,
        TEXTURE_FILT3x3,
        TEXTURE_CUSTOM,
        TEXTURE_EXT,
        TEXTURE_EXT_BW,
        TEXTURE_EXT_NIGHT,
        TEXTURE_EXT_CHROMA_KEY,
        TEXTURE_EXT_SQUEEZE,
        TEXTURE_EXT_TWIRL,
        TEXTURE_EXT_TUNNEL,
        TEXTURE_EXT_BULGE,
        TEXTURE_EXT_DENT,
        TEXTURE_EXT_FISHEYE,
        TEXTURE_EXT_STRETCH,
        TEXTURE_EXT_MIRROR,
        TEXTURE_EXT_FILT3x3
    }

    public Texture2dProgram(int i, String str) {
        this(ProgramType.TEXTURE_CUSTOM, i, ShaderConst.VERTEX_SHADER, str);
    }

    public Texture2dProgram(int i, String str, String str2) {
        this(ProgramType.TEXTURE_CUSTOM, i, str, str2);
    }

    public Texture2dProgram(ProgramType programType) {
        this(programType, 0, null, null);
    }

    protected Texture2dProgram(ProgramType programType, int i, String str, String str2) {
        this.mSync = new Object();
        this.mKernel = new float[18];
        this.mSummedTouchPosition = new float[2];
        this.mLastTouchPosition = new float[2];
        this.mFlags = new int[4];
        this.mProgramType = programType;
        int i2 = C05761.f49xa6249d9a[programType.ordinal()];
        String str3 = ShaderConst.VERTEX_SHADER;
        switch (i2) {
            case 1:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_2D;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_2D);
                break;
            case 2:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_2D;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_FILT3x3);
                break;
            case 3:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT);
                break;
            case 4:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_BW);
                break;
            case 5:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_NIGHT);
                break;
            case 6:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_CHROMA_KEY);
                break;
            case 7:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_SQUEEZE);
                break;
            case 8:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_TWIRL);
                break;
            case 9:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_TUNNEL);
                break;
            case 10:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_BULGE);
                break;
            case 11:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_FISHEYE);
                break;
            case 12:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_DENT);
                break;
            case 13:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_MIRROR);
                break;
            case 14:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_STRETCH);
                break;
            case 15:
                this.mTextureTarget = ShaderConst.GL_TEXTURE_EXTERNAL_OES;
                this.mProgramHandle = GLHelper.loadShader(str3, ShaderConst.FRAGMENT_SHADER_EXT_FILT3x3);
                break;
            case 16:
                if (i == 3553 || i == 36197) {
                    this.mTextureTarget = i;
                    this.mProgramHandle = GLHelper.loadShader(str, str2);
                    break;
                } else {
                    throw new IllegalArgumentException("target should be GL_TEXTURE_2D or GL_TEXTURE_EXTERNAL_OES");
                }
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unhandled type ");
                sb.append(programType);
                throw new RuntimeException(sb.toString());
        }
        int i3 = this.mProgramHandle;
        if (i3 != 0) {
            String str4 = "aPosition";
            this.maPositionLoc = GLES20.glGetAttribLocation(i3, str4);
            GLHelper.checkLocation(this.maPositionLoc, str4);
            String str5 = "aTextureCoord";
            this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.mProgramHandle, str5);
            GLHelper.checkLocation(this.maTextureCoordLoc, str5);
            String str6 = "uMVPMatrix";
            this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, str6);
            GLHelper.checkLocation(this.muMVPMatrixLoc, str6);
            this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexMatrix");
            initLocation(null, null);
            return;
        }
        throw new RuntimeException("Unable to create program");
    }

    public void release() {
        GLES20.glDeleteProgram(this.mProgramHandle);
        this.mProgramHandle = -1;
    }

    public ProgramType getProgramType() {
        return this.mProgramType;
    }

    public int getProgramHandle() {
        return this.mProgramHandle;
    }

    public int createTextureObject() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLHelper.checkGlError("glGenTextures");
        int i = iArr[0];
        GLES20.glBindTexture(this.mTextureTarget, i);
        StringBuilder sb = new StringBuilder();
        sb.append("glBindTexture ");
        sb.append(i);
        GLHelper.checkGlError(sb.toString());
        GLES20.glTexParameterf(this.mTextureTarget, 10241, 9728.0f);
        GLES20.glTexParameterf(this.mTextureTarget, 10240, 9729.0f);
        GLES20.glTexParameteri(this.mTextureTarget, 10242, 33071);
        GLES20.glTexParameteri(this.mTextureTarget, 10243, 33071);
        GLHelper.checkGlError("glTexParameter");
        return i;
    }

    public void handleTouchEvent(MotionEvent motionEvent) {
        synchronized (this.mSync) {
            if (motionEvent.getAction() == 2) {
                if (!(this.mTexHeight == 0.0f || this.mTexWidth == 0.0f)) {
                    float[] fArr = this.mSummedTouchPosition;
                    fArr[0] = fArr[0] + (((motionEvent.getX() - this.mLastTouchPosition[0]) * 2.0f) / this.mTexWidth);
                    float[] fArr2 = this.mSummedTouchPosition;
                    fArr2[1] = fArr2[1] + (((motionEvent.getY() - this.mLastTouchPosition[1]) * 2.0f) / (-this.mTexHeight));
                    this.mLastTouchPosition[0] = motionEvent.getX();
                    this.mLastTouchPosition[1] = motionEvent.getY();
                }
            } else if (motionEvent.getAction() == 0) {
                this.mLastTouchPosition[0] = motionEvent.getX();
                this.mLastTouchPosition[1] = motionEvent.getY();
            }
        }
    }

    public void setKernel(float[] fArr, float f) {
        if (fArr.length >= 9) {
            System.arraycopy(fArr, 0, this.mKernel, 0, 9);
            this.mColorAdjust = f;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Kernel size is ");
        sb.append(fArr.length);
        sb.append(" vs. ");
        sb.append(9);
        throw new IllegalArgumentException(sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0016 A[Catch:{ all -> 0x000d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setKernel2(float[] r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mSync
            monitor-enter(r0)
            r1 = 0
            r2 = 9
            if (r5 == 0) goto L_0x000f
            int r3 = r5.length     // Catch:{ all -> 0x000d }
            if (r3 != r2) goto L_0x000f
            r3 = 1
            goto L_0x0010
        L_0x000d:
            r5 = move-exception
            goto L_0x001d
        L_0x000f:
            r3 = 0
        L_0x0010:
            r4.mHasKernel2 = r3     // Catch:{ all -> 0x000d }
            boolean r3 = r4.mHasKernel2     // Catch:{ all -> 0x000d }
            if (r3 == 0) goto L_0x001b
            float[] r3 = r4.mKernel     // Catch:{ all -> 0x000d }
            java.lang.System.arraycopy(r5, r1, r3, r2, r2)     // Catch:{ all -> 0x000d }
        L_0x001b:
            monitor-exit(r0)     // Catch:{ all -> 0x000d }
            return
        L_0x001d:
            monitor-exit(r0)     // Catch:{ all -> 0x000d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.glutils.Texture2dProgram.setKernel2(float[]):void");
    }

    public void setColorAdjust(float f) {
        synchronized (this.mSync) {
            this.mColorAdjust = f;
        }
    }

    public void setTexSize(int i, int i2) {
        float f = (float) i2;
        this.mTexHeight = f;
        float f2 = (float) i;
        this.mTexWidth = f2;
        float f3 = 1.0f / f2;
        float f4 = 1.0f / f;
        synchronized (this.mSync) {
            float f5 = -f3;
            float f6 = -f4;
            this.mTexOffset = new float[]{f5, f6, 0.0f, f6, f3, f6, f5, 0.0f, 0.0f, 0.0f, f3, 0.0f, f5, f4, 0.0f, f4, f3, f4};
        }
    }

    public void setFlags(int[] iArr) {
        int min = Math.min(4, iArr != null ? iArr.length : 0);
        if (min > 0) {
            synchronized (this.mSync) {
                System.arraycopy(iArr, 0, this.mFlags, 0, min);
            }
        }
    }

    public void setFlag(int i, int i2) {
        if (i >= 0 && i < this.mFlags.length) {
            synchronized (this.mSync) {
                this.mFlags[i] = i2;
            }
        }
    }

    public void draw(float[] fArr, int i, FloatBuffer floatBuffer, int i2, int i3, int i4, int i5, float[] fArr2, int i6, FloatBuffer floatBuffer2, int i7, int i8) {
        GLHelper.checkGlError("draw start");
        GLES20.glUseProgram(this.mProgramHandle);
        GLHelper.checkGlError("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(this.mTextureTarget, i7);
        GLHelper.checkGlError("glBindTexture");
        synchronized (this.mSync) {
            float[] fArr3 = fArr;
            int i9 = i;
            GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, fArr, i);
            GLHelper.checkGlError("glUniformMatrix4fv");
            if (this.muTexMatrixLoc >= 0) {
                GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, fArr2, i6);
                GLHelper.checkGlError("glUniformMatrix4fv");
            }
            GLES20.glEnableVertexAttribArray(this.maPositionLoc);
            GLHelper.checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(this.maPositionLoc, i4, 5126, false, i5, floatBuffer);
            GLHelper.checkGlError("glVertexAttribPointer");
            GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
            GLHelper.checkGlError("glEnableVertexAttribArray");
            GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, i8, floatBuffer2);
            GLHelper.checkGlError("glVertexAttribPointer");
            if (this.muKernelLoc >= 0) {
                if (!this.mHasKernel2) {
                    GLES20.glUniform1fv(this.muKernelLoc, 9, this.mKernel, 0);
                } else {
                    GLES20.glUniform1fv(this.muKernelLoc, 18, this.mKernel, 0);
                }
                GLHelper.checkGlError("set kernel");
            }
            if (this.muTexOffsetLoc >= 0 && this.mTexOffset != null) {
                GLES20.glUniform2fv(this.muTexOffsetLoc, 9, this.mTexOffset, 0);
            }
            if (this.muColorAdjustLoc >= 0) {
                GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust);
            }
            if (this.muTouchPositionLoc >= 0) {
                GLES20.glUniform2fv(this.muTouchPositionLoc, 1, this.mSummedTouchPosition, 0);
            }
            if (this.muFlagsLoc >= 0) {
                GLES20.glUniform1iv(this.muFlagsLoc, 4, this.mFlags, 0);
            }
        }
        int i10 = i2;
        internal_draw(i2, i3);
        GLES20.glDisableVertexAttribArray(this.maPositionLoc);
        GLES20.glDisableVertexAttribArray(this.maTextureCoordLoc);
        GLES20.glBindTexture(this.mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }

    /* access modifiers changed from: protected */
    public void initLocation(float[] fArr, float[] fArr2) {
        this.muKernelLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uKernel");
        if (this.muKernelLoc < 0) {
            this.muKernelLoc = -1;
            this.muTexOffsetLoc = -1;
        } else {
            this.muTexOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexOffset");
            if (this.muTexOffsetLoc < 0) {
                this.muTexOffsetLoc = -1;
            }
            if (fArr == null) {
                fArr = ShaderConst.KERNEL_NULL;
            }
            setKernel(fArr, 0.0f);
            setTexSize(256, 256);
        }
        if (fArr2 != null) {
            setKernel2(fArr2);
        }
        this.muColorAdjustLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uColorAdjust");
        if (this.muColorAdjustLoc < 0) {
            this.muColorAdjustLoc = -1;
        }
        this.muTouchPositionLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uPosition");
        if (this.muTouchPositionLoc < 0) {
            this.muTouchPositionLoc = -1;
        }
        this.muFlagsLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uFlags");
        if (this.muFlagsLoc < 0) {
            this.muFlagsLoc = -1;
        }
    }

    /* access modifiers changed from: protected */
    public void internal_draw(int i, int i2) {
        GLES20.glDrawArrays(5, i, i2);
        GLHelper.checkGlError("glDrawArrays");
    }
}
