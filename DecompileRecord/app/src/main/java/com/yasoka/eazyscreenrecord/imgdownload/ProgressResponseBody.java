package com.yasoka.eazyscreenrecord.imgdownload;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    private BufferedSource bufferedSource;
    /* access modifiers changed from: private */
    public final ProgressListener progressListener;
    /* access modifiers changed from: private */
    public final ResponseBody responseBody;

    interface ProgressListener {
        void update(long j, long j2, boolean z);
    }

    ProgressResponseBody(ResponseBody responseBody2, ProgressListener progressListener2) {
        this.responseBody = responseBody2;
        this.progressListener = progressListener2;
    }

    public MediaType contentType() {
        return this.responseBody.contentType();
    }

    public long contentLength() {
        return this.responseBody.contentLength();
    }

    public BufferedSource source() {
        if (this.bufferedSource == null) {
            this.bufferedSource = Okio.buffer(source(this.responseBody.source()));
        }
        return this.bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;

            public long read(Buffer buffer, long j) throws IOException {
                long read = super.read(buffer, j);
                int i = (read > -1 ? 1 : (read == -1 ? 0 : -1));
                this.totalBytesRead += i != 0 ? read : 0;
                ProgressResponseBody.this.progressListener.update(this.totalBytesRead, ProgressResponseBody.this.responseBody.contentLength(), i == 0);
                return read;
            }
        };
    }
}
