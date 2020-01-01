package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.widget.ContentLoadingProgressBar;*/
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.utils.EEAConsentHelper;*/
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;

public class ShowRewardAdLoadingDialog extends DialogFragment implements RewardedVideoAdListener {
    /* access modifiers changed from: private */
    public OnAdRewardedCallback callback;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    /* access modifiers changed from: private */
    public boolean isAdRewarded = false;
    private RewardedVideoAd rewardedVideoAd;

    public interface OnAdRewardedCallback {
        void onAdRewarded(boolean z);
    }

    public void onRewardedVideoAdLeftApplication() {
    }

    public void onRewardedVideoAdOpened() {
    }

    public void onRewardedVideoCompleted() {
    }

    public void onRewardedVideoStarted() {
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.rewarded_ad_loading_dialog, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.contentLoadingProgressBar = (ContentLoadingProgressBar) view.findViewById(C0793R.C0795id.id_content_loading_progress_bar);
        this.contentLoadingProgressBar.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShowRewardAdLoadingDialog.this.sendResult(true);
            }
        });
        this.rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity().getApplicationContext());
        this.rewardedVideoAd.setRewardedVideoAdListener(this);
        Builder builder = new Builder();
        if (EEAConsentHelper.getInstance().getEEAConsentAdType(RecorderApplication.getInstance().getApplicationContext()) == 1) {
            builder.addNetworkExtrasBundle(AdMobAdapter.class, EEAConsentHelper.getInstance().getNonPersonalisedBundle(RecorderApplication.getInstance().getApplicationContext()));
        }
        this.rewardedVideoAd.loadAd(getString(C0793R.string.key_img_edit_rewarded_ad), builder.build());
        this.isAdRewarded = false;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    ShowRewardAdLoadingDialog showRewardAdLoadingDialog = ShowRewardAdLoadingDialog.this;
                    showRewardAdLoadingDialog.sendResult(showRewardAdLoadingDialog.isAdRewarded);
                }
            });
        }
    }

    public void onResume() {
        RewardedVideoAd rewardedVideoAd2 = this.rewardedVideoAd;
        if (rewardedVideoAd2 != null) {
            rewardedVideoAd2.resume(getContext());
        }
        super.onResume();
    }

    public void onPause() {
        RewardedVideoAd rewardedVideoAd2 = this.rewardedVideoAd;
        if (rewardedVideoAd2 != null) {
            rewardedVideoAd2.pause(getContext());
        }
        super.onPause();
    }

    public void onDestroy() {
        RewardedVideoAd rewardedVideoAd2 = this.rewardedVideoAd;
        if (rewardedVideoAd2 != null) {
            rewardedVideoAd2.destroy(getContext());
        }
        super.onDestroy();
    }

    public void onRewardedVideoAdLoaded() {
        this.contentLoadingProgressBar.hide();
        RewardedVideoAd rewardedVideoAd2 = this.rewardedVideoAd;
        if (rewardedVideoAd2 != null) {
            rewardedVideoAd2.show();
        } else {
            sendResult(false);
        }
    }

    public void onRewardedVideoAdClosed() {
        sendResult(this.isAdRewarded);
    }

    public void onRewarded(RewardItem rewardItem) {
        this.isAdRewarded = true;
    }

    public void onRewardedVideoAdFailedToLoad(int i) {
        sendResult(true);
    }

    public void setOnAdRewardListener(OnAdRewardedCallback onAdRewardedCallback) {
        this.callback = onAdRewardedCallback;
    }

    /* access modifiers changed from: private */
    public void sendResult(boolean z) {
        if (isAdded()) {
            OnAdRewardedCallback onAdRewardedCallback = this.callback;
            if (onAdRewardedCallback != null) {
                onAdRewardedCallback.onAdRewarded(z);
            }
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        if (ShowRewardAdLoadingDialog.this.getDialog() != null) {
                            ShowRewardAdLoadingDialog.this.callback = null;
                            if (ShowRewardAdLoadingDialog.this.getDialog().isShowing()) {
                                ShowRewardAdLoadingDialog.this.dismiss();
                            }
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }, 30);
        }
    }
}
