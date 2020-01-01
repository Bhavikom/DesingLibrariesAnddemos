package com.yasoka.eazyscreenrecord.utils;

import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdLoader {
    private static final InterstitialAdLoader ourInstance = new InterstitialAdLoader();
    private InterstitialAd interstitialAd;

    private InterstitialAdLoader() {
    }

    public static InterstitialAdLoader getInstance() {
        return ourInstance;
    }

    public void loadInterstitialAd() {
        if (this.interstitialAd == null) {
            this.interstitialAd = new InterstitialAd(RecorderApplication.getInstance().getApplicationContext());
            this.interstitialAd.setAdUnitId(RecorderApplication.getInstance().getString(C0793R.string.key_interstitial_ad_unit));
        }
        if (!this.interstitialAd.isLoaded()) {
            this.interstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }
            });
            Builder builder = new Builder();
            if (EEAConsentHelper.getInstance().getEEAConsentAdType(RecorderApplication.getInstance().getApplicationContext()) == 1) {
                builder.addNetworkExtrasBundle(AdMobAdapter.class, EEAConsentHelper.getInstance().getNonPersonalisedBundle(RecorderApplication.getInstance().getApplicationContext()));
            }
            this.interstitialAd.loadAd(builder.build());
        }
    }

    public void showInterstitialAd() {
        InterstitialAd interstitialAd2 = this.interstitialAd;
        if (interstitialAd2 != null && interstitialAd2.isLoaded()) {
            this.interstitialAd.show();
        }
    }
}
