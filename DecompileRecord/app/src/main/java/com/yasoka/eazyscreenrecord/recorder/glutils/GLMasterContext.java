package com.yasoka.eazyscreenrecord.recorder.glutils;

import com.appsmartz.recorder.glutils.EGLBase.IContext;
import com.appsmartz.recorder.utils.MessageTask.TaskBreak;

public class GLMasterContext {
    private static final String TAG = "GLMasterContext";
    private MasterTask mMasterTask;

    private static class MasterTask extends EglTask {
        /* access modifiers changed from: protected */
        public void onStart() {
        }

        /* access modifiers changed from: protected */
        public void onStop() {
        }

        /* access modifiers changed from: protected */
        public Object processRequest(int i, int i2, int i3, Object obj) throws TaskBreak {
            return null;
        }

        public MasterTask(int i, int i2) {
            super(i, null, i2);
        }
    }

    public GLMasterContext(int i, int i2) {
        this.mMasterTask = new MasterTask(i, i2);
        new Thread(this.mMasterTask, TAG).start();
        this.mMasterTask.waitReady();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public synchronized void release() {
        if (this.mMasterTask != null) {
            this.mMasterTask.release();
            this.mMasterTask = null;
        }
    }

    public synchronized IContext getContext() throws IllegalStateException {
        if (this.mMasterTask != null) {
        } else {
            throw new IllegalStateException("already released");
        }
        return this.mMasterTask.getContext();
    }
}
