package com.yasoka.eazyscreenrecord.alertdialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezscreenrecorder.C0793R;

public class FullScreenLoadingDialog extends DialogFragment {
    private ContentLoadingProgressBar contentLoadingProgressBar;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.rewarded_ad_loading_dialog, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.contentLoadingProgressBar = (ContentLoadingProgressBar) view.findViewById(C0793R.C0795id.id_content_loading_progress_bar);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    public void dismissDialog() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) {
            dismissAllowingStateLoss();
        }
    }
}
