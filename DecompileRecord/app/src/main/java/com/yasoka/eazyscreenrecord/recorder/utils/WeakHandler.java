package com.yasoka.eazyscreenrecord.recorder.utils;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WeakHandler {
    private final Callback mCallback;
    private final ExecHandler mExec;
    private Lock mLock;
    final ChainedRef mRunnables;

    static class ChainedRef {
        Lock lock;
        ChainedRef next;
        ChainedRef prev;
        final Runnable runnable;
        final WeakRunnable wrapper;

        public ChainedRef(Lock lock2, Runnable runnable2) {
            this.runnable = runnable2;
            this.lock = lock2;
            this.wrapper = new WeakRunnable(new WeakReference(runnable2), new WeakReference(this));
        }

        /* JADX INFO: finally extract failed */
        public WeakRunnable remove() {
            this.lock.lock();
            try {
                if (this.prev != null) {
                    this.prev.next = this.next;
                }
                if (this.next != null) {
                    this.next.prev = this.prev;
                }
                this.prev = null;
                this.next = null;
                this.lock.unlock();
                return this.wrapper;
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }

        public void insertAfter(ChainedRef chainedRef) {
            this.lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = chainedRef;
                }
                chainedRef.next = this.next;
                this.next = chainedRef;
                chainedRef.prev = this;
            } finally {
                this.lock.unlock();
            }
        }

        public WeakRunnable remove(Runnable runnable2) {
            this.lock.lock();
            try {
                for (ChainedRef chainedRef = this.next; chainedRef != null; chainedRef = chainedRef.next) {
                    if (chainedRef.runnable == runnable2) {
                        WeakRunnable remove = chainedRef.remove();
                        return remove;
                    }
                }
                this.lock.unlock();
                return null;
            } finally {
                this.lock.unlock();
            }
        }
    }

    private static class ExecHandler extends Handler {
        private final WeakReference<Callback> mCallback;

        ExecHandler() {
            this.mCallback = null;
        }

        ExecHandler(WeakReference<Callback> weakReference) {
            this.mCallback = weakReference;
        }

        ExecHandler(Looper looper) {
            super(looper);
            this.mCallback = null;
        }

        ExecHandler(Looper looper, WeakReference<Callback> weakReference) {
            super(looper);
            this.mCallback = weakReference;
        }

        public void handleMessage(Message message) {
            WeakReference<Callback> weakReference = this.mCallback;
            if (weakReference != null) {
                Callback callback = (Callback) weakReference.get();
                if (callback != null) {
                    callback.handleMessage(message);
                }
            }
        }
    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> weakReference, WeakReference<ChainedRef> weakReference2) {
            this.mDelegate = weakReference;
            this.mReference = weakReference2;
        }

        public void run() {
            Runnable runnable = (Runnable) this.mDelegate.get();
            ChainedRef chainedRef = (ChainedRef) this.mReference.get();
            if (chainedRef != null) {
                chainedRef.remove();
            }
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public WeakHandler() {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = null;
        this.mExec = new ExecHandler();
    }

    public WeakHandler(Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new ExecHandler(new WeakReference<>(callback));
    }

    public WeakHandler(Looper looper) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = null;
        this.mExec = new ExecHandler(looper);
    }

    public WeakHandler(Looper looper, Callback callback) {
        this.mLock = new ReentrantLock();
        this.mRunnables = new ChainedRef(this.mLock, null);
        this.mCallback = callback;
        this.mExec = new ExecHandler(looper, new WeakReference(callback));
    }

    public final boolean post(Runnable runnable) {
        return this.mExec.post(wrapRunnable(runnable));
    }

    public final boolean postAtTime(Runnable runnable, long j) {
        return this.mExec.postAtTime(wrapRunnable(runnable), j);
    }

    public final boolean postAtTime(Runnable runnable, Object obj, long j) {
        return this.mExec.postAtTime(wrapRunnable(runnable), obj, j);
    }

    public final boolean postDelayed(Runnable runnable, long j) {
        return this.mExec.postDelayed(wrapRunnable(runnable), j);
    }

    public final boolean postAtFrontOfQueue(Runnable runnable) {
        return this.mExec.postAtFrontOfQueue(wrapRunnable(runnable));
    }

    public final void removeCallbacks(Runnable runnable) {
        WeakRunnable remove = this.mRunnables.remove(runnable);
        if (remove != null) {
            this.mExec.removeCallbacks(remove);
        }
    }

    public final void removeCallbacks(Runnable runnable, Object obj) {
        WeakRunnable remove = this.mRunnables.remove(runnable);
        if (remove != null) {
            this.mExec.removeCallbacks(remove, obj);
        }
    }

    public final boolean sendMessage(Message message) {
        return this.mExec.sendMessage(message);
    }

    public final boolean sendEmptyMessage(int i) {
        return this.mExec.sendEmptyMessage(i);
    }

    public final boolean sendEmptyMessageDelayed(int i, long j) {
        return this.mExec.sendEmptyMessageDelayed(i, j);
    }

    public final boolean sendEmptyMessageAtTime(int i, long j) {
        return this.mExec.sendEmptyMessageAtTime(i, j);
    }

    public final boolean sendMessageDelayed(Message message, long j) {
        return this.mExec.sendMessageDelayed(message, j);
    }

    public boolean sendMessageAtTime(Message message, long j) {
        return this.mExec.sendMessageAtTime(message, j);
    }

    public final boolean sendMessageAtFrontOfQueue(Message message) {
        return this.mExec.sendMessageAtFrontOfQueue(message);
    }

    public final void removeMessages(int i) {
        this.mExec.removeMessages(i);
    }

    public final void removeMessages(int i, Object obj) {
        this.mExec.removeMessages(i, obj);
    }

    public final void removeCallbacksAndMessages(Object obj) {
        this.mExec.removeCallbacksAndMessages(obj);
    }

    public final boolean hasMessages(int i) {
        return this.mExec.hasMessages(i);
    }

    public final boolean hasMessages(int i, Object obj) {
        return this.mExec.hasMessages(i, obj);
    }

    public final Looper getLooper() {
        return this.mExec.getLooper();
    }

    private WeakRunnable wrapRunnable(Runnable runnable) {
        if (runnable != null) {
            ChainedRef chainedRef = new ChainedRef(this.mLock, runnable);
            this.mRunnables.insertAfter(chainedRef);
            return chainedRef.wrapper;
        }
        throw new NullPointerException("Runnable can't be null");
    }
}