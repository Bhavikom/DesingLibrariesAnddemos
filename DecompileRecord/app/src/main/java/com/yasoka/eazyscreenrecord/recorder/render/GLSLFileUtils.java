package com.yasoka.eazyscreenrecord.recorder.render;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class GLSLFileUtils {
    public static String getFileContextFromAssets(Context context, String str) {
        try {
            return inputStream2String(context.getAssets().open(str));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String inputStream2String(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return stringBuffer.toString();
            }
            stringBuffer.append(new String(bArr, 0, read));
        }
    }
}
