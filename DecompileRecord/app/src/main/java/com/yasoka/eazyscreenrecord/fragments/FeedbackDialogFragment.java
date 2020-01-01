package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.FeedbackInput;
import com.ezscreenrecorder.server.model.FeedbackOutput;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.model.FeedbackInput;
import com.yasoka.eazyscreenrecord.server.model.FeedbackOutput;

import java.util.Locale;
/*import p009io.reactivex.SingleObserver;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;*/

public class FeedbackDialogFragment extends DialogFragment {
    private boolean isRating;
    /* access modifiers changed from: private */
    public EditText phoneEditText;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.isRating = getArguments().getBoolean("isRating");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_feedback_dialog, viewGroup, false);
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
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        final EditText editText = (EditText) view.findViewById(C0793R.C0795id.ed_message);
        this.phoneEditText = (EditText) view.findViewById(C0793R.C0795id.ed_phone_number);
        if (!RecorderApplication.getCountryCode().equals("IN")) {
            this.phoneEditText.setVisibility(View.GONE);
        }
        TextView textView = (TextView) view.findViewById(C0793R.C0795id.txt_feedback_title);
        final Spinner spinner = (Spinner) view.findViewById(C0793R.C0795id.spin_feedback_titles);
        if (this.isRating) {
            textView.setText(C0793R.string.id_feedback_title_message);
            spinner.setSelection(6);
        }
        view.findViewById(C0793R.C0795id.btn_submit).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (FeedbackDialogFragment.this.isAdded()) {
                    String obj = editText.getText().toString();
                    String str = FeedbackDialogFragment.this.getResources().getStringArray(C0793R.array.feedback_title_list2)[spinner.getSelectedItemPosition()];
                    if (obj == null || obj.length() == 0) {
                        Toast.makeText(FeedbackDialogFragment.this.getContext(), C0793R.string.id_enter_valid_msg_error, 1).show();
                    } else {
                        FeedbackInput feedbackInput = new FeedbackInput();
                        String str2 = "";
                        feedbackInput.setAId(sharedPreferences.getString(ServerAPI.ANONYMOUS_ID, str2));
                        feedbackInput.setAndroidVer(String.valueOf(VERSION.SDK_INT));
                        try {
                            feedbackInput.setAppVer(String.valueOf(FeedbackDialogFragment.this.getActivity().getPackageManager().getPackageInfo(FeedbackDialogFragment.this.getActivity().getPackageName(), 0).versionCode));
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        feedbackInput.setDevCc(((TelephonyManager) FeedbackDialogFragment.this.getContext().getSystemService("phone")).getSimCountryIso());
                        feedbackInput.setDevLang(Locale.getDefault().getLanguage());
                        feedbackInput.setDevMake(Build.MANUFACTURER);
                        feedbackInput.setDevModel(Build.MODEL);
                        feedbackInput.setMessage(obj);
                        if (FeedbackDialogFragment.this.phoneEditText.getVisibility() == 0 && !TextUtils.isEmpty(FeedbackDialogFragment.this.phoneEditText.getText().toString())) {
                            feedbackInput.setPhoneNumber(FeedbackDialogFragment.this.phoneEditText.getText().toString());
                        }
                        feedbackInput.setTitle(str.replace("'", str2));
                        feedbackInput.setUId(sharedPreferences.getString(ServerAPI.USER_ID, str2));
                        feedbackInput.setAppUsageCounter(String.valueOf(sharedPreferences.getInt("usageCount", 0)));
                        final ProgressDialog progressDialog = new ProgressDialog(FeedbackDialogFragment.this.getContext());
                        progressDialog.setTitle(FeedbackDialogFragment.this.getString(C0793R.string.feedback_loading));
                        progressDialog.show();
                        ServerAPI.getInstance().getApiReference().feedback(feedbackInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FeedbackOutput>() {
                            public void onSuccess(FeedbackOutput feedbackOutput) {
                                if (FeedbackDialogFragment.this.isAdded()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(FeedbackDialogFragment.this.getContext(), C0793R.string.feedback_submit, 1).show();
                                    FeedbackDialogFragment.this.dismiss();
                                }
                            }

                            public void onError(Throwable th) {
                                try {
                                    progressDialog.dismiss();
                                    th.printStackTrace();
                                    Toast.makeText(FeedbackDialogFragment.this.getContext(), th.getMessage(), 1).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
