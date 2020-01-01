package com.yasoka.eazyscreenrecord.utils;

import android.util.Log;

public class Logger {
    private static final String TAG = "SCR";
    private static final Logger ourInstance = new Logger();

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
    }

    public void debug(String str) {
        Log.d(TAG, str);
    }

    public void debug(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        Log.d(TAG, sb.toString());
    }

    public void debug(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(j);
        sb.append("");
        Log.d(TAG, sb.toString());
    }

    public void debug(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append(f);
        sb.append("");
        Log.d(TAG, sb.toString());
    }

    public void debug(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        sb.append("");
        Log.d(TAG, sb.toString());
    }

    public void error(String str) {
        Log.e(TAG, str);
    }

    public void error(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        Log.e(TAG, sb.toString());
    }

    public void error(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(j);
        sb.append("");
        Log.e(TAG, sb.toString());
    }

    public void error(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append(f);
        sb.append("");
        Log.e(TAG, sb.toString());
    }

    public void error(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        sb.append("");
        Log.e(TAG, sb.toString());
    }
}
