package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.appsmartz.recorder.glutils.EGLBase.IContext;

public class RendererHolder extends AbstractRendererHolder {
    private static final String TAG = "RendererHolder";

    protected static final class MyRendererTask extends RendererTask {
        public MyRendererTask(RendererHolder rendererHolder, int i, int i2) {
            super(rendererHolder, i, i2);
        }

        public MyRendererTask(@NonNull AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, IContext iContext, int i4) {
            super(abstractRendererHolder, i, i2, i3, iContext, i4);
        }
    }

    public RendererHolder(int i, int i2, @Nullable RenderHolderCallback renderHolderCallback) {
        this(i, i2, 3, null, 2, renderHolderCallback);
    }

    public RendererHolder(int i, int i2, int i3, IContext iContext, int i4, @Nullable RenderHolderCallback renderHolderCallback) {
        super(i, i2, i3, iContext, i4, renderHolderCallback);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public RendererTask createRendererTask(int i, int i2, int i3, IContext iContext, int i4) {
        MyRendererTask myRendererTask = new MyRendererTask(this, i, i2, i3, iContext, i4);
        return myRendererTask;
    }
}
