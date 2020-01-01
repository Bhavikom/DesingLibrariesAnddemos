package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.p000v4.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yasoka.eazyscreenrecord.C0793R;
//import com.ezscreenrecorder.C0793R;

public class ShowConfirmationForRewardAdDialog extends DialogFragment implements OnClickListener {
    public static final String EXTRA_CONFIRMATION_RESPONSE_INTERFACE = "extra_response_interface";
    public static final int EXTRA_CONFIRMATION_TYPE_DRAW_FEATURE = 761;
    public static final String EXTRA_CONFIRMATION_TYPE_MESSAGE = "extra_type_message";
    public static final int EXTRA_CONFIRMATION_TYPE_TUTORIAL_FEATURE = 762;
    private TextView cancelButton;
    private TextView messageDescTextView;
    private int messageType = -1;
    private OnConfirmationResponseCallback onConfirmationCallback;
    private TextView watchNowButton;

    public interface OnConfirmationResponseCallback {
        void onUserResponse(boolean z);
    }

    public void setConfirmationData(int i, OnConfirmationResponseCallback onConfirmationResponseCallback) {
        this.messageType = i;
        this.onConfirmationCallback = onConfirmationResponseCallback;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.rewarded_ad_dialog_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.cancelButton = (TextView) view.findViewById(C0793R.C0795id.txt_cancel);
        this.watchNowButton = (TextView) view.findViewById(C0793R.C0795id.txt_watch_now);
        this.messageDescTextView = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_desc);
        int i = this.messageType;
        if (i == 761) {
            this.messageDescTextView.setText(C0793R.string.draw_rewarded_ad_dialog_message);
        } else if (i == 762) {
            this.messageDescTextView.setText(C0793R.string.tutorial_rewarded_ad_dialog_message);
        }
        this.cancelButton.setOnClickListener(this);
        this.watchNowButton.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C0793R.C0795id.txt_cancel) {
            sendResult(false);
        } else if (id == C0793R.C0795id.txt_watch_now) {
            sendResult(true);
        }
    }

    private void sendResult(boolean z) {
        OnConfirmationResponseCallback onConfirmationResponseCallback = this.onConfirmationCallback;
        if (onConfirmationResponseCallback != null) {
            onConfirmationResponseCallback.onUserResponse(z);
        }
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        dismiss();
    }
}
