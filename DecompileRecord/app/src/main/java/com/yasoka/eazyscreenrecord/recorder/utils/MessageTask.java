package com.yasoka.eazyscreenrecord.recorder.utils;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class MessageTask implements Runnable {
    protected static final int REQUEST_TASK_NON = 0;
    protected static final int REQUEST_TASK_QUIT = -9;
    protected static final int REQUEST_TASK_RUN = -1;
    protected static final int REQUEST_TASK_RUN_AND_WAIT = -2;
    protected static final int REQUEST_TASK_START = -8;
    private static final String TAG = "MessageTask";
    private volatile boolean mFinished;
    private volatile boolean mIsRunning;
    private final int mMaxRequest;
    private final LinkedBlockingQueue<Request> mRequestPool;
    private final LinkedBlockingDeque<Request> mRequestQueue;
    private final Object mSync;
    private Thread mWorkerThread;

    protected static final class Request {
        int arg1;
        int arg2;
        Object obj;
        int request;
        int request_for_result;
        Object result;

        private Request() {
            this.request_for_result = 0;
            this.request = 0;
        }

        public Request(int i, int i2, int i3, Object obj2) {
            this.request = i;
            this.arg1 = i2;
            this.arg2 = i3;
            this.obj = obj2;
            this.request_for_result = 0;
        }

        public void setResult(Object obj2) {
            synchronized (this) {
                this.result = obj2;
                this.request_for_result = 0;
                this.request = 0;
                notifyAll();
            }
        }

        public boolean equals(Object obj2) {
            if (!(obj2 instanceof Request)) {
                return super.equals(obj2);
            }
            Request request2 = (Request) obj2;
            return this.request == request2.request && this.request_for_result == request2.request_for_result && this.arg1 == request2.arg1 && this.arg2 == request2.arg2 && this.obj == request2.obj;
        }
    }

    public static class TaskBreak extends RuntimeException {
    }

    /* access modifiers changed from: protected */
    public void onBeforeStop() {
    }

    /* access modifiers changed from: protected */
    public boolean onError(Exception exc) {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract void onInit(int i, int i2, Object obj);

    /* access modifiers changed from: protected */
    public abstract void onRelease();

    /* access modifiers changed from: protected */
    public abstract void onStart();

    /* access modifiers changed from: protected */
    public abstract void onStop();

    /* access modifiers changed from: protected */
    public abstract Object processRequest(int i, int i2, int i3, Object obj) throws TaskBreak;

    public MessageTask() {
        this.mSync = new Object();
        this.mMaxRequest = -1;
        this.mRequestPool = new LinkedBlockingQueue<>();
        this.mRequestQueue = new LinkedBlockingDeque<>();
    }

    public MessageTask(int i) {
        this.mSync = new Object();
        this.mMaxRequest = -1;
        this.mRequestPool = new LinkedBlockingQueue<>();
        this.mRequestQueue = new LinkedBlockingDeque<>();
        for (int i2 = 0; i2 < i && this.mRequestPool.offer(new Request()); i2++) {
        }
    }

    public MessageTask(int i, int i2) {
        this.mSync = new Object();
        this.mMaxRequest = i;
        this.mRequestPool = new LinkedBlockingQueue<>(i);
        this.mRequestQueue = new LinkedBlockingDeque<>(i);
        for (int i3 = 0; i3 < i2 && this.mRequestPool.offer(new Request()); i3++) {
        }
    }

    /* access modifiers changed from: protected */
    public void init(int i, int i2, Object obj) {
        this.mFinished = false;
        this.mRequestQueue.offer(obtain(-8, i, i2, obj));
    }

    /* access modifiers changed from: protected */
    public Request takeRequest() throws InterruptedException {
        return (Request) this.mRequestQueue.take();
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0013 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean waitReady() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mSync
            monitor-enter(r0)
        L_0x0003:
            boolean r1 = r4.mIsRunning     // Catch:{ all -> 0x0017 }
            if (r1 != 0) goto L_0x0013
            boolean r1 = r4.mFinished     // Catch:{ all -> 0x0017 }
            if (r1 != 0) goto L_0x0013
            java.lang.Object r1 = r4.mSync     // Catch:{ InterruptedException -> 0x0013 }
            r2 = 500(0x1f4, double:2.47E-321)
            r1.wait(r2)     // Catch:{ InterruptedException -> 0x0013 }
            goto L_0x0003
        L_0x0013:
            boolean r1 = r4.mIsRunning     // Catch:{ all -> 0x0017 }
            monitor-exit(r0)     // Catch:{ all -> 0x0017 }
            return r1
        L_0x0017:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0017 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.utils.MessageTask.waitReady():boolean");
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:63:? A[ExcHandler: InterruptedException (unused java.lang.InterruptedException), SYNTHETIC, Splitter:B:28:0x0051] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r8 = this;
            r0 = 1
            r8.mIsRunning = r0
            r1 = 0
            r2 = 0
            java.util.concurrent.LinkedBlockingDeque<com.appsmartz.recorder.utils.MessageTask$Request> r3 = r8.mRequestQueue     // Catch:{ InterruptedException -> 0x000e }
            java.lang.Object r3 = r3.take()     // Catch:{ InterruptedException -> 0x000e }
            com.appsmartz.recorder.utils.MessageTask$Request r3 = (com.appsmartz.recorder.utils.MessageTask.Request) r3     // Catch:{ InterruptedException -> 0x000e }
            goto L_0x0013
        L_0x000e:
            r8.mIsRunning = r2
            r8.mFinished = r0
            r3 = r1
        L_0x0013:
            java.lang.Object r4 = r8.mSync
            monitor-enter(r4)
            boolean r5 = r8.mIsRunning     // Catch:{ all -> 0x00e3 }
            if (r5 == 0) goto L_0x0034
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x00e3 }
            r8.mWorkerThread = r5     // Catch:{ all -> 0x00e3 }
            int r5 = r3.arg1     // Catch:{ Exception -> 0x002a }
            int r6 = r3.arg2     // Catch:{ Exception -> 0x002a }
            java.lang.Object r3 = r3.obj     // Catch:{ Exception -> 0x002a }
            r8.onInit(r5, r6, r3)     // Catch:{ Exception -> 0x002a }
            goto L_0x0034
        L_0x002a:
            r3 = move-exception
            java.lang.String r5 = TAG     // Catch:{ all -> 0x00e3 }
            android.util.Log.w(r5, r3)     // Catch:{ all -> 0x00e3 }
            r8.mIsRunning = r2     // Catch:{ all -> 0x00e3 }
            r8.mFinished = r0     // Catch:{ all -> 0x00e3 }
        L_0x0034:
            java.lang.Object r3 = r8.mSync     // Catch:{ all -> 0x00e3 }
            r3.notifyAll()     // Catch:{ all -> 0x00e3 }
            monitor-exit(r4)     // Catch:{ all -> 0x00e3 }
            boolean r3 = r8.mIsRunning
            if (r3 == 0) goto L_0x004d
            r8.onStart()     // Catch:{ Exception -> 0x0042 }
            goto L_0x004d
        L_0x0042:
            r3 = move-exception
            boolean r3 = r8.callOnError(r3)
            if (r3 == 0) goto L_0x004d
            r8.mIsRunning = r2
            r8.mFinished = r0
        L_0x004d:
            boolean r3 = r8.mIsRunning
            if (r3 == 0) goto L_0x00b5
            com.appsmartz.recorder.utils.MessageTask$Request r3 = r8.takeRequest()     // Catch:{ InterruptedException -> 0x00b5 }
            int r4 = r3.request     // Catch:{ InterruptedException -> 0x00b5 }
            r5 = -9
            if (r4 == r5) goto L_0x00b5
            r5 = -2
            if (r4 == r5) goto L_0x008d
            r5 = -1
            if (r4 == r5) goto L_0x0077
            if (r4 == 0) goto L_0x00a8
            int r4 = r3.request     // Catch:{ Exception -> 0x006f, InterruptedException -> 0x00b5 }
            int r5 = r3.arg1     // Catch:{ Exception -> 0x006f, InterruptedException -> 0x00b5 }
            int r6 = r3.arg2     // Catch:{ Exception -> 0x006f, InterruptedException -> 0x00b5 }
            java.lang.Object r7 = r3.obj     // Catch:{ Exception -> 0x006f, InterruptedException -> 0x00b5 }
            r8.processRequest(r4, r5, r6, r7)     // Catch:{ Exception -> 0x006f, InterruptedException -> 0x00b5 }
            goto L_0x00a8
        L_0x006f:
            r4 = move-exception
            boolean r4 = r8.callOnError(r4)     // Catch:{ InterruptedException -> 0x00b5 }
            if (r4 == 0) goto L_0x00a8
            goto L_0x00b5
        L_0x0077:
            java.lang.Object r4 = r3.obj     // Catch:{ InterruptedException -> 0x00b5 }
            boolean r4 = r4 instanceof java.lang.Runnable     // Catch:{ InterruptedException -> 0x00b5 }
            if (r4 == 0) goto L_0x00a8
            java.lang.Object r4 = r3.obj     // Catch:{ Exception -> 0x0085 }
            java.lang.Runnable r4 = (java.lang.Runnable) r4     // Catch:{ Exception -> 0x0085 }
            r4.run()     // Catch:{ Exception -> 0x0085 }
            goto L_0x00a8
        L_0x0085:
            r4 = move-exception
            boolean r4 = r8.callOnError(r4)     // Catch:{ InterruptedException -> 0x00b5 }
            if (r4 == 0) goto L_0x00a8
            goto L_0x00b5
        L_0x008d:
            int r4 = r3.request_for_result     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            int r5 = r3.arg1     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            int r6 = r3.arg2     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            java.lang.Object r7 = r3.obj     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            java.lang.Object r4 = r8.processRequest(r4, r5, r6, r7)     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            r3.setResult(r4)     // Catch:{ TaskBreak -> 0x00b2, Exception -> 0x009d }
            goto L_0x00a8
        L_0x009d:
            r4 = move-exception
            r3.setResult(r1)     // Catch:{ InterruptedException -> 0x00b5 }
            boolean r4 = r8.callOnError(r4)     // Catch:{ InterruptedException -> 0x00b5 }
            if (r4 == 0) goto L_0x00a8
            goto L_0x00b5
        L_0x00a8:
            r3.request_for_result = r2     // Catch:{ InterruptedException -> 0x00b5 }
            r3.request = r2     // Catch:{ InterruptedException -> 0x00b5 }
            java.util.concurrent.LinkedBlockingQueue<com.appsmartz.recorder.utils.MessageTask$Request> r4 = r8.mRequestPool     // Catch:{ InterruptedException -> 0x00b5 }
            r4.offer(r3)     // Catch:{ InterruptedException -> 0x00b5 }
            goto L_0x004d
        L_0x00b2:
            r3.setResult(r1)     // Catch:{ InterruptedException -> 0x00b5 }
        L_0x00b5:
            boolean r3 = java.lang.Thread.interrupted()
            java.lang.Object r5 = r8.mSync
            monitor-enter(r5)
            r8.mWorkerThread = r1     // Catch:{ all -> 0x00e0 }
            r8.mIsRunning = r2     // Catch:{ all -> 0x00e0 }
            r8.mFinished = r0     // Catch:{ all -> 0x00e0 }
            monitor-exit(r5)     // Catch:{ all -> 0x00e0 }
            if (r3 != 0) goto L_0x00d0
            r8.onBeforeStop()     // Catch:{ Exception -> 0x00cc }
            r8.onStop()     // Catch:{ Exception -> 0x00cc }
            goto L_0x00d0
        L_0x00cc:
            r0 = move-exception
            r8.callOnError(r0)
        L_0x00d0:
            r8.onRelease()     // Catch:{ Exception -> 0x00d3 }
        L_0x00d3:
            java.lang.Object r0 = r8.mSync
            monitor-enter(r0)
            java.lang.Object r1 = r8.mSync     // Catch:{ all -> 0x00dd }
            r1.notifyAll()     // Catch:{ all -> 0x00dd }
            monitor-exit(r0)     // Catch:{ all -> 0x00dd }
            return
        L_0x00dd:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00dd }
            throw r1
        L_0x00e0:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00e0 }
            throw r0
        L_0x00e3:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00e3 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.utils.MessageTask.run():void");
    }

    /* access modifiers changed from: protected */
    public boolean callOnError(Exception exc) {
        try {
            return onError(exc);
        } catch (Exception unused) {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public Request obtain(int i, int i2, int i3, Object obj) {
        Request request = (Request) this.mRequestPool.poll();
        if (request == null) {
            return new Request(i, i2, i3, obj);
        }
        request.request = i;
        request.arg1 = i2;
        request.arg2 = i3;
        request.obj = obj;
        return request;
    }

    public boolean offer(int i, int i2, int i3, Object obj) {
        return !this.mFinished && this.mRequestQueue.offer(obtain(i, i2, i3, obj));
    }

    public boolean offer(int i, int i2, Object obj) {
        return !this.mFinished && this.mRequestQueue.offer(obtain(i, i2, 0, obj));
    }

    public boolean offer(int i, int i2, int i3) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, i2, i3, null));
    }

    public boolean offer(int i, int i2) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, i2, 0, null));
    }

    public boolean offer(int i) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, 0, 0, null));
    }

    public boolean offer(int i, Object obj) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, 0, 0, obj));
    }

    public boolean offerFirst(int i, int i2, int i3, Object obj) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offerFirst(obtain(i, i2, i3, obj));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:5|6|(4:11|12|13|7)|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0024 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Object offerAndWait(int r3, int r4, int r5, Object r6) {
        /*
            r2 = this;
            boolean r0 = r2.mFinished
            r1 = 0
            if (r0 != 0) goto L_0x002b
            if (r3 <= 0) goto L_0x002b
            r0 = -2
            com.appsmartz.recorder.utils.MessageTask$Request r4 = r2.obtain(r0, r4, r5, r6)
            monitor-enter(r4)
            r4.request_for_result = r3     // Catch:{ all -> 0x0028 }
            r4.result = r1     // Catch:{ all -> 0x0028 }
            java.util.concurrent.LinkedBlockingDeque<com.appsmartz.recorder.utils.MessageTask$Request> r3 = r2.mRequestQueue     // Catch:{ all -> 0x0028 }
            r3.offer(r4)     // Catch:{ all -> 0x0028 }
        L_0x0016:
            boolean r3 = r2.mIsRunning     // Catch:{ all -> 0x0028 }
            if (r3 == 0) goto L_0x0024
            int r3 = r4.request_for_result     // Catch:{ all -> 0x0028 }
            if (r3 == 0) goto L_0x0024
            r5 = 100
            r4.wait(r5)     // Catch:{ InterruptedException -> 0x0024 }
            goto L_0x0016
        L_0x0024:
            monitor-exit(r4)     // Catch:{ all -> 0x0028 }
            java.lang.Object r3 = r4.result
            return r3
        L_0x0028:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0028 }
            throw r3
        L_0x002b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appsmartz.recorder.utils.MessageTask.offerAndWait(int, int, int, java.lang.Object):java.lang.Object");
    }

    public boolean queueEvent(Runnable runnable) {
        return !this.mFinished && runnable != null && offer(-1, (Object) runnable);
    }

    public void removeRequest(Request request) {
        Iterator it = this.mRequestQueue.iterator();
        while (it.hasNext()) {
            Request request2 = (Request) it.next();
            if (this.mIsRunning && !this.mFinished) {
                if (request2.equals(request)) {
                    this.mRequestQueue.remove(request2);
                    this.mRequestPool.offer(request2);
                }
            } else {
                return;
            }
        }
    }

    public void removeRequest(int i) {
        Iterator it = this.mRequestQueue.iterator();
        while (it.hasNext()) {
            Request request = (Request) it.next();
            if (this.mIsRunning && !this.mFinished) {
                if (request.request == i) {
                    this.mRequestQueue.remove(request);
                    this.mRequestPool.offer(request);
                }
            } else {
                return;
            }
        }
    }

    public void release() {
        release(false);
    }

    public void release(boolean z) {
        boolean z2 = this.mIsRunning;
        this.mIsRunning = false;
        if (!this.mFinished) {
            this.mRequestQueue.clear();
            this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
            synchronized (this.mSync) {
                if (z2) {
                    long id = Thread.currentThread().getId();
                    if ((this.mWorkerThread != null ? this.mWorkerThread.getId() : id) != id) {
                        if (z && this.mWorkerThread != null) {
                            this.mWorkerThread.interrupt();
                        }
                        while (!this.mFinished) {
                            try {
                                this.mSync.wait(300);
                            } catch (InterruptedException unused) {
                            }
                        }
                    }
                }
            }
        }
    }

    public void releaseSelf() {
        this.mIsRunning = false;
        if (!this.mFinished) {
            this.mRequestQueue.clear();
            this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
        }
    }

    public void userBreak() throws TaskBreak {
        throw new TaskBreak();
    }
}
