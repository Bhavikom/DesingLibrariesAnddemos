package com.yasoka.eazyscreenrecord.fragments;

import android.support.p000v4.app.Fragment;
import android.text.format.Formatter;
import java.io.File;

public class BasePreviewScreenFragment extends Fragment {
    public String getFileNameFromPath(String str) {
        return new File(str).getName();
    }

    public String getFileSizeFromPath(String str) {
        return Formatter.formatShortFileSize(getActivity(), new File(str).length());
    }
}
