package com.yasoka.eazyscreenrecord.imgupload;

import android.os.Handler;
import android.os.Looper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private File mFile;
    /* access modifiers changed from: private */
    public UploadCallbacks mListener;
    private String mPath;

    private class ProgressUpdater implements Runnable {
        private long mTotal;
        private long mUploaded;

        ProgressUpdater(long j, long j2) {
            this.mUploaded = j;
            this.mTotal = j2;
        }

        public void run() {
            ProgressRequestBody.this.mListener.onProgressUpdate((int) ((this.mUploaded * 100) / this.mTotal));
        }
    }

    public interface UploadCallbacks {
        void onError();

        void onFinish();

        void onProgressUpdate(int i);
    }

    public ProgressRequestBody(File file, UploadCallbacks uploadCallbacks) {
        this.mFile = file;
        this.mListener = uploadCallbacks;
    }

    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    public long contentLength() throws IOException {
        return this.mFile.length();
    }

    public void writeTo(BufferedSink bufferedSink) throws IOException {
        long length = this.mFile.length();
        byte[] bArr = new byte[2048];
        FileInputStream fileInputStream = new FileInputStream(this.mFile);
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            long j = 0;
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    ProgressUpdater progressUpdater = new ProgressUpdater(j, length);
                    handler.post(progressUpdater);
                    j += (long) read;
                    bufferedSink.write(bArr, 0, read);
                } else {
                    return;
                }
            }
        } finally {
            fileInputStream.close();
        }
    }
}
