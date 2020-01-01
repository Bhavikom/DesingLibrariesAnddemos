package com.yasoka.eazyscreenrecord.recorder.rtmp;

import android.util.Log;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class CreateSSLSocket {
    public static Socket createSSlSocket(String str, int i) {
        try {
            return new TLSSocketFactory().createSocket(str, i);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            Log.e("CreateSSLSocket", "Error", e);
            return null;
        }
    }
}
