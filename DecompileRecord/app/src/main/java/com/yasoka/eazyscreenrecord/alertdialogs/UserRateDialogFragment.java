package com.yasoka.eazyscreenrecord.alertdialogs;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.fragments.FeedbackDialogFragment;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smilerating.SmileRating.OnSmileySelectionListener;

public class UserRateDialogFragment extends DialogFragment implements OnSmileySelectionListener {
    private SharedPreferences sharedPreferences;
    private SmileRating smileRating;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.rate_dialog_fragment_test, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        this.smileRating = (SmileRating) view.findViewById(C0793R.C0795id.id_smiley_rating_View);
        this.smileRating.setPlaceholderBackgroundColor(Color.parseColor("#626566"));
        String str = "";
        this.smileRating.setNameForSmile(0, str);
        this.smileRating.setNameForSmile(1, str);
        this.smileRating.setNameForSmile(2, str);
        this.smileRating.setNameForSmile(3, str);
        this.smileRating.setNameForSmile(4, str);
        this.smileRating.setOnSmileySelectionListener(this);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (getActivity() instanceof GalleryActivity) {
                dialog.setOnDismissListener((GalleryActivity) getActivity());
            }
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

    /* access modifiers changed from: private */
    public void playStoreRating() {
        String str = "android.intent.action.VIEW";
        if (isAdded()) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("http://play.google.com/store/apps/details?id=");
                sb.append(getActivity().getPackageName());
                startActivity(new Intent(str, Uri.parse(sb.toString())));
            } catch (ActivityNotFoundException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("market://details?id=");
                sb2.append(getActivity().getPackageName());
                Intent intent = new Intent(str, Uri.parse(sb2.toString()));
                intent.addFlags(1208483840);
                startActivity(intent);
            }
            FirebaseEventsNewHelper.getInstance().sendPlayStoreRateUsEvent();
        }
    }

    public void onSmileySelected(int i, boolean z) {
        if (isAdded() && !getActivity().isFinishing()) {
            this.sharedPreferences.edit().putBoolean("isRatingDone", true).apply();
            if (i == -1 || i == 0 || i == 1 || i == 2) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (UserRateDialogFragment.this.isAdded()) {
                            FeedbackDialogFragment feedbackDialogFragment = new FeedbackDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isRating", true);
                            feedbackDialogFragment.setArguments(bundle);
                            feedbackDialogFragment.show(UserRateDialogFragment.this.getFragmentManager(), "Feedback");
                            UserRateDialogFragment.this.dismiss();
                        }
                    }
                }, 500);
            } else if (i == 3 || i == 4) {
                Toast.makeText(getActivity(), C0793R.string.rating_play, 1).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (UserRateDialogFragment.this.isAdded()) {
                            UserRateDialogFragment.this.playStoreRating();
                            if (UserRateDialogFragment.this.getDialog() != null && UserRateDialogFragment.this.getDialog().isShowing()) {
                                UserRateDialogFragment.this.dismiss();
                            }
                        }
                    }
                }, 500);
            }
        }
    }
}
