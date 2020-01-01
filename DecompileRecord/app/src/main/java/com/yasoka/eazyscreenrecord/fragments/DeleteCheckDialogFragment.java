package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.ShowVideoDialogActivity;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.utils.FilesAccessHelper;
import java.io.File;
import org.greenrobot.eventbus.EventBus;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.disposables.Disposable;

public class DeleteCheckDialogFragment extends DialogFragment {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 344;
    private SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public int type;
    /* access modifiers changed from: private */
    public String video;

    public static DeleteCheckDialogFragment getInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("typeDialog", i);
        DeleteCheckDialogFragment deleteCheckDialogFragment = new DeleteCheckDialogFragment();
        deleteCheckDialogFragment.setArguments(bundle);
        return deleteCheckDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.video = getArguments().getString("video");
            this.type = getArguments().getBoolean("isVideo", false) ^ true ? 1 : 0;
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.delete_check_dialog_fragment, viewGroup, false);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        TextView textView = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_desc);
        TextView textView2 = (TextView) view.findViewById(C0793R.C0795id.txt_yes);
        int i = this.type;
        if (i == 0) {
            textView.setText(C0793R.string.delete_recording);
        } else if (i == 1) {
            textView.setText(C0793R.string.delete_screenshot);
        }
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Single.create(new SingleOnSubscribe<Boolean>() {
                    public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                        singleEmitter.onSuccess(Boolean.valueOf(new File(DeleteCheckDialogFragment.this.video).delete()));
                    }
                }).subscribe((SingleObserver<? super T>) new SingleObserver<Boolean>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onSuccess(Boolean bool) {
                        if (bool.booleanValue()) {
                            DeleteCheckDialogFragment.this.getActivity().getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{DeleteCheckDialogFragment.this.video});
                            FilesAccessHelper.getInstance().refreshGallery(DeleteCheckDialogFragment.this.getActivity(), new File(DeleteCheckDialogFragment.this.video).getAbsolutePath());
                            if (DeleteCheckDialogFragment.this.getActivity() instanceof GalleryActivity) {
                                DeleteCheckDialogFragment.this.dismiss();
                                return;
                            }
                            Intent intent = new Intent(GalleryActivity.ACTION_FILE_DELETED_FROM_DIALOG_ACTIVITY);
                            int access$100 = DeleteCheckDialogFragment.this.type;
                            String str = ShowVideoDialogActivity.EXTRA_IS_SCREENSHOT_DIALOG;
                            if (access$100 == 1) {
                                intent.putExtra(str, true);
                            } else {
                                intent.putExtra(str, false);
                            }
                            EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOCAL_RECORDING_REFRESH_AFTER_DELETE));
                            DeleteCheckDialogFragment.this.getActivity().setResult(-1, intent);
                            DeleteCheckDialogFragment.this.getActivity().finish();
                            return;
                        }
                        Toast.makeText(DeleteCheckDialogFragment.this.getActivity(), "Some error occurred while deleting file. Try again", 0).show();
                        DeleteCheckDialogFragment.this.dismiss();
                    }

                    public void onError(Throwable th) {
                        th.printStackTrace();
                    }
                });
            }
        });
        view.findViewById(C0793R.C0795id.txt_later).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DeleteCheckDialogFragment.this.dismiss();
            }
        });
    }
}
