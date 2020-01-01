package com.yasoka.eazyscreenrecord.alertdialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ezscreenrecorder.C0793R;

public class DeleteConfirmationDialog extends DialogFragment implements OnClickListener {
    private static final String KEY_MESSAGE_TYPE = "message_type_key";
    public static final int MESSAGE_TYPE_AUDIO_DELETE_CONFIRMATION = 1513;
    public static final int MESSAGE_TYPE_IMAGE_DELETE_CONFIRMATION = 1511;
    public static final int MESSAGE_TYPE_RECORDING_DELETE_CONFIRMATION = 1512;
    private TextView dialogDesc;
    private TextView laterDialog;
    private OnConfirmationResult onConfirmationResult;
    private TextView yesDialog;

    public interface OnConfirmationResult {
        void onOptionResult(DialogFragment dialogFragment, boolean z);
    }

    public static DeleteConfirmationDialog getInstance(int i) {
        DeleteConfirmationDialog deleteConfirmationDialog = new DeleteConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE_TYPE, i);
        deleteConfirmationDialog.setArguments(bundle);
        return deleteConfirmationDialog;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.delete_check_dialog_fragment2_test, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.dialogDesc = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_desc);
        this.yesDialog = (TextView) view.findViewById(C0793R.C0795id.txt_yes);
        this.laterDialog = (TextView) view.findViewById(C0793R.C0795id.txt_later);
        this.yesDialog.setOnClickListener(this);
        this.laterDialog.setOnClickListener(this);
        switch (getArguments().getInt(KEY_MESSAGE_TYPE, -1)) {
            case MESSAGE_TYPE_IMAGE_DELETE_CONFIRMATION /*1511*/:
                this.dialogDesc.setText(C0793R.string.delete_screenshot);
                return;
            case MESSAGE_TYPE_RECORDING_DELETE_CONFIRMATION /*1512*/:
                this.dialogDesc.setText(C0793R.string.delete_recording);
                return;
            case MESSAGE_TYPE_AUDIO_DELETE_CONFIRMATION /*1513*/:
                this.dialogDesc.setText(C0793R.string.delete_recording);
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        if (!isAdded()) {
            return;
        }
        if (this.onConfirmationResult != null) {
            int id = view.getId();
            if (id == C0793R.C0795id.txt_later) {
                this.onConfirmationResult.onOptionResult(this, false);
            } else if (id == C0793R.C0795id.txt_yes) {
                this.onConfirmationResult.onOptionResult(this, true);
            }
        } else {
            dismiss();
        }
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void setDialogResultListener(OnConfirmationResult onConfirmationResult2) {
        this.onConfirmationResult = onConfirmationResult2;
    }
}
