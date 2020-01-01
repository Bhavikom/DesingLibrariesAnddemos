package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.alertdialogs.UserRateDialogFragment;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.ezscreenrecorder.model.AudioFileModel;
import com.ezscreenrecorder.p004ui.PlayPauseView;
import com.ezscreenrecorder.utils.LocalAudioPlayer;
import com.ezscreenrecorder.utils.LocalAudioPlayer.OnPlayerCallbacks;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.utils.PlayerUtils;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.io.File;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class ShowAudioDialogActivity extends AppCompatActivity implements OnNativeAdListener, OnPlayerCallbacks, OnClickListener {
    public static final String KEY_AUDIO_FILE_MODEL = "key_file_audio_model";
    /* access modifiers changed from: private */
    public AudioFileModel audioFileModel;
    private TextView currentTime;
    private boolean isRateDialogAlreadyShowed = false;
    private LocalAudioPlayer localAudioPlayer;
    private TextView maxTime;
    private UnifiedNativeAdView nativeAdView;
    private PlayPauseView playPauseButton;
    private UserRateDialogFragment rateDialogFragment;
    private TextView recordingName;

    public void onChangePlayerVisibility(int i) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (getIntent() == null) {
            finish();
        } else {
            String str = "key_file_audio_model";
            if (getIntent().hasExtra(str)) {
                this.audioFileModel = (AudioFileModel) getIntent().getSerializableExtra(str);
                if (this.audioFileModel == null) {
                    finish();
                } else {
                    setContentView((int) C0793R.layout.activity_show_audio_dialog);
                    initView();
                }
            } else {
                finish();
            }
        }
    }

    private void initView() {
        if (this.nativeAdView == null) {
            this.nativeAdView = (UnifiedNativeAdView) findViewById(C0793R.C0795id.id_install_native_ad_view);
        }
        this.recordingName = (TextView) findViewById(C0793R.C0795id.id_audio_dialog_recording_name);
        this.maxTime = (TextView) findViewById(C0793R.C0795id.id_audio_dialog_max_time_txt);
        this.currentTime = (TextView) findViewById(C0793R.C0795id.id_audio_dialog_on_going_time_txt);
        this.playPauseButton = (PlayPauseView) findViewById(C0793R.C0795id.id_audio_dialog_play_pause_button);
        findViewById(C0793R.C0795id.img_more).setOnClickListener(this);
        findViewById(C0793R.C0795id.img_share_video).setOnClickListener(this);
        findViewById(C0793R.C0795id.img_delete_video).setOnClickListener(this);
        findViewById(C0793R.C0795id.img_like_rate).setOnClickListener(this);
        this.playPauseButton.setOnClickListener(this);
        if (this.localAudioPlayer == null) {
            this.localAudioPlayer = new LocalAudioPlayer((SeekBar) (AppCompatSeekBar) findViewById(C0793R.C0795id.id_audio_dialog_seekbar), this.audioFileModel, (OnPlayerCallbacks) this);
        }
        this.recordingName.setText(this.audioFileModel.getFileName());
        this.maxTime.setText(PlayerUtils.getInstance().milliSecondsToTimer(this.audioFileModel.getFileDuration()));
    }

    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        NativeAdLoaderPreviewDialog.getInstance().getNativeAd(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        try {
            if (!isFinishing()) {
                NativeAdLoaderPreviewDialog.getInstance().loadAd();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
        try {
            if (this.nativeAdView != null) {
                this.nativeAdView.setHeadlineView(this.nativeAdView.findViewById(C0793R.C0795id.id_native_ad_dialog_title));
                this.nativeAdView.setBodyView(this.nativeAdView.findViewById(C0793R.C0795id.id_native_ad_dialog_subtitle));
                this.nativeAdView.setCallToActionView(this.nativeAdView.findViewById(C0793R.C0795id.id_native_ad_dialog_button));
                this.nativeAdView.setIconView(this.nativeAdView.findViewById(C0793R.C0795id.id_native_ad_dialog_icon));
                this.nativeAdView.setMediaView((MediaView) this.nativeAdView.findViewById(C0793R.C0795id.id_native_ad_dialog_imageview));
                ((TextView) this.nativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
                ((TextView) this.nativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
                this.nativeAdView.getIconView().setBackgroundColor(-7829368);
                if (unifiedNativeAd.getIcon() != null) {
                    Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                    if (drawable != null) {
                        this.nativeAdView.getIconView().setBackgroundColor(0);
                        ((ImageView) this.nativeAdView.getIconView()).setImageDrawable(drawable);
                    }
                }
                ((Button) this.nativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
                this.nativeAdView.setNativeAd(unifiedNativeAd);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            UnifiedNativeAdView unifiedNativeAdView = this.nativeAdView;
            if (unifiedNativeAdView != null) {
                unifiedNativeAdView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onDestroy();
        }
        super.onDestroy();
    }

    public void onNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
        addValuesAppInstallAdView(unifiedNativeAd);
    }

    public void onPlaybackToggle(boolean z) {
        if (z) {
            if (this.playPauseButton.isPlay()) {
                this.playPauseButton.toggle();
            }
        } else if (!this.playPauseButton.isPlay()) {
            this.playPauseButton.toggle();
        }
    }

    public void onTimeUpdate(long j, long j2) {
        Object[] objArr = {PlayerUtils.getInstance().milliSecondsToTimer(j2)};
        String str = "%s";
        this.maxTime.setText(String.format(str, objArr));
        this.currentTime.setText(String.format(str, new Object[]{PlayerUtils.getInstance().milliSecondsToTimer(j)}));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0793R.C0795id.id_audio_dialog_play_pause_button /*2131296486*/:
                if (this.localAudioPlayer.isPlaying()) {
                    this.localAudioPlayer.stop();
                    return;
                } else {
                    this.localAudioPlayer.resumePlay();
                    return;
                }
            case C0793R.C0795id.img_delete_video /*2131296804*/:
                deleteFile();
                return;
            case C0793R.C0795id.img_like_rate /*2131296813*/:
                this.isRateDialogAlreadyShowed = true;
                this.rateDialogFragment = new UserRateDialogFragment();
                this.rateDialogFragment.show(getSupportFragmentManager(), "rate");
                return;
            case C0793R.C0795id.img_more /*2131296815*/:
                onBackPressed();
                return;
            case C0793R.C0795id.img_share_video /*2131296824*/:
                Uri parse = Uri.parse(this.audioFileModel.getFilePath());
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("audio/*");
                intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.id_share_audio_title));
                intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.id_share_audio_text));
                intent.putExtra("android.intent.extra.STREAM", parse);
                startActivity(Intent.createChooser(intent, getString(C0793R.string.id_share_audio_title)));
                return;
            default:
                return;
        }
    }

    private void deleteFile() {
        DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_AUDIO_DELETE_CONFIRMATION);
        instance.setDialogResultListener(new OnConfirmationResult() {
            public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                if (z) {
                    Single.create(new SingleOnSubscribe<Boolean>() {
                        public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                            File file = new File(ShowAudioDialogActivity.this.audioFileModel.getFilePath());
                            ShowAudioDialogActivity.this.getApplicationContext().getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{ShowAudioDialogActivity.this.audioFileModel.getFilePath()});
                            singleEmitter.onSuccess(Boolean.valueOf(file.delete()));
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<Boolean>() {
                        public void onSuccess(Boolean bool) {
                            if (bool.booleanValue()) {
                                ShowAudioDialogActivity.this.setResult(-1, new Intent(GalleryActivity.ACTION_FILE_DELETED_FROM_DIALOG_ACTIVITY));
                                ShowAudioDialogActivity.this.finish();
                            }
                            DialogFragment dialogFragment = dialogFragment;
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.dismiss();
                            }
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                            DialogFragment dialogFragment = dialogFragment;
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.dismiss();
                            }
                        }
                    });
                } else if (dialogFragment != null && dialogFragment.isVisible()) {
                    dialogFragment.dismiss();
                }
            }
        });
        instance.show(getSupportFragmentManager(), "audio_delete_confirmation");
    }
}
