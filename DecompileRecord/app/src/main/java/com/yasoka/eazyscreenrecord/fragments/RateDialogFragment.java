package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p003v7.widget.AppCompatRatingBar;*/
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ezscreenrecorder.BuildConfig;
import com.yasoka.eazyscreenrecord.R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.ShowVideoDialogActivity;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.ShowVideoDialogActivity;
import com.ezscreenrecorder.model.SharedDataForOtherApp;*/

public class RateDialogFragment extends DialogFragment {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 344;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences;
    private int type;

    public static RateDialogFragment getInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("typeDialog", i);
        RateDialogFragment rateDialogFragment = new RateDialogFragment();
        rateDialogFragment.setArguments(bundle);
        return rateDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.type = getArguments().getInt("typeDialog");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.rate_dialog_fragment, viewGroup, false);
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
        TextView textView = (TextView) view.findViewById(R.C0795id.txt_dialog_desc);
        AppCompatRatingBar appCompatRatingBar = (AppCompatRatingBar) view.findViewById(R.C0795id.rating_bar);
        TextView textView2 = (TextView) view.findViewById(R.C0795id.txt_dialog_title);
        TextView textView3 = (TextView) view.findViewById(R.C0795id.txt_yes);
        int i = this.type;
        if (i == 1) {
            appCompatRatingBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.enjoy_app);
            view.findViewById(R.C0795id.view_line).setVisibility(View.GONE);
            view.findViewById(R.C0795id.txt_note_settings).setVisibility(View.VISIBLE);
            textView2.setText(R.string.experience_share);
            textView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RateDialogFragment.this.sharedPreferences.edit().putBoolean("isRatingDone", true).apply();
                    Toast.makeText(RateDialogFragment.this.getContext(), R.string.rating_play, Toast.LENGTH_SHORT).show();
                    RateDialogFragment.this.playStoreRating();
                    RateDialogFragment.this.dismiss();
                }
            });
            view.findViewById(R.C0795id.txt_later).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RateDialogFragment.this.sharedPreferences.edit().putBoolean("isRatingDone", true).apply();
                    FeedbackDialogFragment feedbackDialogFragment = new FeedbackDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isRating", true);
                    feedbackDialogFragment.setArguments(bundle);
                    feedbackDialogFragment.show(RateDialogFragment.this.getFragmentManager(), "Feedback");
                    RateDialogFragment.this.dismiss();
                }
            });
        } else if (i == 2) {
            appCompatRatingBar.setVisibility(View.GONE);
            textView.setText(R.string.overlay_permission_msg);
            textView3.setText(R.string.enable_now);
            textView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {
                        RateDialogFragment.this.dismiss();
                        if (VERSION.SDK_INT >= 23) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("package:");
                            sb.append(RateDialogFragment.this.getActivity().getPackageName());
                            RateDialogFragment.this.startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), RateDialogFragment.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                            return;
                        }
                        RateDialogFragment.this.startFloatingService();
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
                        RateDialogFragment.this.startActivityForResult(intent, RateDialogFragment.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        RateDialogFragment.this.startFloatingService();
                    }
                }
            });
            view.findViewById(R.C0795id.txt_later).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RateDialogFragment.this.startFloatingService();
                    RateDialogFragment.this.dismiss();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startFloatingService() {
        MainActivity.showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
        getActivity().startService(new Intent(getContext(), FloatingService.class));
        dismiss();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (VERSION.SDK_INT < 23) {
                startFloatingService();
            } else if (Settings.canDrawOverlays(getContext())) {
                startFloatingService();
            }
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if ((getActivity() != null && ((getActivity() instanceof GalleryActivity) || (getActivity() instanceof ShowVideoDialogActivity))) || (getActivity() instanceof MainActivity)) {
            ((OnDismissListener) getActivity()).onDismiss(dialogInterface);
        }
    }

    /* access modifiers changed from: private */
    public void playStoreRating() {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(getActivity().getPackageName());
        String str = "android.intent.action.VIEW";
        Intent intent = new Intent(str, Uri.parse(sb.toString()));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://play.google.com/store/apps/details?id=");
            sb2.append(getActivity().getPackageName());
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }
}
