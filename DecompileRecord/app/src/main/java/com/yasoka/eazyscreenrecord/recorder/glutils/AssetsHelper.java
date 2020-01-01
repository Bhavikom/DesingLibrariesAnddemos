package com.yasoka.eazyscreenrecord.recorder.glutils;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetsHelper {
    public static String loadString(@NonNull AssetManager assetManager, @NonNull String str) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        char[] cArr = new char[1024];
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(str)));
        for (int read = bufferedReader.read(cArr); read > 0; read = bufferedReader.read(cArr)) {
            stringBuffer.append(cArr, 0, read);
        }
        return stringBuffer.toString();
    }
}
