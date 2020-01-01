package com.yasoka.eazyscreenrecord.utils;

import android.util.Log;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import java.io.PrintStream;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class NativeAdLoaderPreviewDialog {
    private static final NativeAdLoaderPreviewDialog ourInstance = new NativeAdLoaderPreviewDialog();
    /* access modifiers changed from: private */
    public UnifiedNativeAd adData;
    /* access modifiers changed from: private */
    public AdLoader adLoader;
    /* access modifiers changed from: private */
    public boolean isAdFailedToLoad;
    /* access modifiers changed from: private */
    public boolean isAdSentToListener = false;
    /* access modifiers changed from: private */
    public OnNativeAdListener nativeAdListener;

    public static NativeAdLoaderPreviewDialog getInstance() {
        return ourInstance;
    }

    private NativeAdLoaderPreviewDialog() {
    }

    public void loadAd() {
        resetData();
        Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
            public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                NativeAdLoaderPreviewDialog.this.adLoader = new Builder(RecorderApplication.getInstance().getApplicationContext(), RecorderApplication.getInstance().getString(C0793R.string.key_preview_dialog_native_ad_video)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        singleEmitter.onSuccess(unifiedNativeAd);
                    }
                }).withAdListener(new AdListener() {
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("AdFailedToLoad: ");
                        sb.append(i);
                        printStream.println(sb.toString());
                        NativeAdLoaderPreviewDialog.this.isAdFailedToLoad = true;
                    }

                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                }).build();
                AdRequest.Builder builder = new AdRequest.Builder();
                if (EEAConsentHelper.getInstance().getEEAConsentAdType(RecorderApplication.getInstance().getApplicationContext()) == 1) {
                    builder.addNetworkExtrasBundle(AdMobAdapter.class, EEAConsentHelper.getInstance().getNonPersonalisedBundle(RecorderApplication.getInstance().getApplicationContext()));
                }
                NativeAdLoaderPreviewDialog.this.adLoader.loadAd(builder.build());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<UnifiedNativeAd>() {
            public void onError(Throwable th) {
            }

            public void onSuccess(UnifiedNativeAd unifiedNativeAd) {
                NativeAdLoaderPreviewDialog.this.adData = unifiedNativeAd;
                if (NativeAdLoaderPreviewDialog.this.nativeAdListener != null && !NativeAdLoaderPreviewDialog.this.isAdSentToListener) {
                    NativeAdLoaderPreviewDialog.this.nativeAdListener.onNativeAdLoaded(NativeAdLoaderPreviewDialog.this.adData);
                    NativeAdLoaderPreviewDialog.this.isAdSentToListener = true;
                    NativeAdLoaderPreviewDialog.this.nativeAdListener = null;
                }
            }
        });
    }

    public void loadAd(final int i) {
        resetData();
        Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
            public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                String string = RecorderApplication.getInstance().getString(C0793R.string.key_preview_dialog_native_ad);
                switch (i) {
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                        string = RecorderApplication.getInstance().getString(C0793R.string.key_preview_dialog_native_ad_image);
                        break;
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                        string = RecorderApplication.getInstance().getString(C0793R.string.key_preview_dialog_native_ad_video);
                        break;
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                        string = RecorderApplication.getInstance().getString(C0793R.string.key_preview_dialog_native_ad_audio);
                        break;
                }
                NativeAdLoaderPreviewDialog.this.adLoader = new Builder(RecorderApplication.getInstance().getApplicationContext(), string).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        singleEmitter.onSuccess(unifiedNativeAd);
                    }
                }).withAdListener(new AdListener() {
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("AdFailedToLoad: ");
                        sb.append(i);
                        printStream.println(sb.toString());
                        NativeAdLoaderPreviewDialog.this.isAdFailedToLoad = true;
                    }

                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                }).build();
                AdRequest.Builder builder = new AdRequest.Builder();
                if (EEAConsentHelper.getInstance().getEEAConsentAdType(RecorderApplication.getInstance().getApplicationContext()) == 1) {
                    builder.addNetworkExtrasBundle(AdMobAdapter.class, EEAConsentHelper.getInstance().getNonPersonalisedBundle(RecorderApplication.getInstance().getApplicationContext()));
                }
                NativeAdLoaderPreviewDialog.this.adLoader.loadAd(builder.build());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<UnifiedNativeAd>() {
            public void onError(Throwable th) {
            }

            public void onSuccess(UnifiedNativeAd unifiedNativeAd) {
                NativeAdLoaderPreviewDialog.this.adData = unifiedNativeAd;
                if (NativeAdLoaderPreviewDialog.this.nativeAdListener != null && !NativeAdLoaderPreviewDialog.this.isAdSentToListener) {
                    NativeAdLoaderPreviewDialog.this.nativeAdListener.onNativeAdLoaded(NativeAdLoaderPreviewDialog.this.adData);
                    NativeAdLoaderPreviewDialog.this.isAdSentToListener = true;
                    NativeAdLoaderPreviewDialog.this.nativeAdListener = null;
                }
            }
        });
    }

    public void getNativeAd(OnNativeAdListener onNativeAdListener) {
        this.nativeAdListener = onNativeAdListener;
        AdLoader adLoader2 = this.adLoader;
        if (adLoader2 != null && !adLoader2.isLoading() && !this.isAdFailedToLoad) {
            UnifiedNativeAd unifiedNativeAd = this.adData;
            if (unifiedNativeAd != null) {
                if (!this.isAdSentToListener) {
                    this.nativeAdListener.onNativeAdLoaded(unifiedNativeAd);
                    this.isAdSentToListener = true;
                    this.nativeAdListener = null;
                }
                Log.e("CHECK", "onNativeAdLoaded");
            }
        }
    }

    private void resetData() {
        this.adData = null;
        this.adLoader = null;
        this.isAdFailedToLoad = false;
        this.nativeAdListener = null;
        this.isAdSentToListener = false;
    }
}
