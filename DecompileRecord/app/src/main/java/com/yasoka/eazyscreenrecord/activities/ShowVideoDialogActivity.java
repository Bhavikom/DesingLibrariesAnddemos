package com.yasoka.eazyscreenrecord.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
/*import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;*/
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.alertdialogs.UserRateDialogFragment;
import com.ezscreenrecorder.fragments.DeleteCheckDialogFragment;
import com.ezscreenrecorder.fragments.ShareDialogFragment;
import com.ezscreenrecorder.fragments.ShareImageDialogFragment;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.ezscreenrecorder.settings.SettingsListDialogFragment;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.facebook.appevents.AppEventsConstants;*/
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
//import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.alertdialogs.UserRateDialogFragment;
import com.yasoka.eazyscreenrecord.fragments.DeleteCheckDialogFragment;
import com.yasoka.eazyscreenrecord.fragments.ShareDialogFragment;
import com.yasoka.eazyscreenrecord.fragments.ShareImageDialogFragment;
import com.yasoka.eazyscreenrecord.imgedit.ImageEditActivity;
import com.yasoka.eazyscreenrecord.interfaces.OnNativeAdListener;
import com.yasoka.eazyscreenrecord.settings.SettingsListDialogFragment;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.NativeAdLoaderPreviewDialog;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;

import java.io.File;
//import life.knowledge4.videotrimmer.utils.FileUtils;

public class ShowVideoDialogActivity extends AppCompatActivity implements OnDismissListener, OnNativeAdListener {
    private static final int EDIT_IMG = 3421;
    public static final String EXTRA_IS_SCREENSHOT_DIALOG = "isScreenshot";
    private AlertDialog alert;
    private boolean changeOrientation;
    private boolean isRateDialogAlreadyShowed = false;
    private boolean isShared;
    private boolean isUnAspected;
    /* access modifiers changed from: private */
    public boolean isVideo;
    private UnifiedNativeAdView nativeAdView;
    /* access modifiers changed from: private */
    public UserRateDialogFragment rateDialogFragment;
    private SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public String video;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_show_video_dialog_tst);
        if (getIntent() == null) {
            finish();
            return;
        }
        this.sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
        this.video = getIntent().getStringExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH);
        this.isVideo = getIntent().getBooleanExtra("isVideo", true);
        this.isUnAspected = getIntent().getBooleanExtra("isUnAspected", true);
        this.changeOrientation = getIntent().getBooleanExtra("changeOrientation", false);
        if (this.video == null) {
            finish();
            return;
        }
        Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
        intent.putExtra(FloatingService.SHOW_GALLERY, true);
        sendBroadcast(intent);
        if (this.isUnAspected && this.isVideo) {
            this.alert = new Builder(this).setTitle((int) C0793R.string.app_name).setMessage((int) C0793R.string.recording_stopped_later).setPositiveButton((int) C0793R.string.f83ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    if (!new File(ShowVideoDialogActivity.this.video).exists()) {
                        ShowVideoDialogActivity.this.finish();
                    }
                }
            }).show();
        } else if (this.changeOrientation && this.sharedPreferences.getBoolean("videoRotationCheck", true)) {
            boolean z = this.isVideo;
        }
        setUp();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
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

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setContentView((int) C0793R.layout.activity_show_video_dialog_tst);
        setUp();
        NativeAdLoaderPreviewDialog.getInstance().loadAd();
        NativeAdLoaderPreviewDialog.getInstance().getNativeAd(this);
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

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    private void setUp() {
        this.nativeAdView = (UnifiedNativeAdView) findViewById(C0793R.C0795id.id_install_native_ad_view);
        ImageView imageView = (ImageView) findViewById(C0793R.C0795id.img_video_thumbnail);
        if (this.isVideo) {
            try {
                Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(this.video, 1);
                if (createVideoThumbnail != null) {
                    imageView.setImageBitmap(createVideoThumbnail);
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                } else {
                    Glide.with((FragmentActivity) this).load(this.video).asBitmap().videoDecoder(new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(1000000), Glide.get(this).getBitmapPool(), DecodeFormat.PREFER_ARGB_8888)).centerCrop().into(imageView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Glide.with((FragmentActivity) this).load(this.video).into(imageView);
            findViewById(C0793R.C0795id.img_play_video).setVisibility(8);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ShowVideoDialogActivity.this, ShowImagesActivity.class);
                    intent.putExtra("ImgPath", ShowVideoDialogActivity.this.video);
                    ShowVideoDialogActivity.this.startActivityForResult(intent, GalleryActivity.REQUEST_VIEW_IMAGES);
                }
            });
        }
        findViewById(C0793R.C0795id.img_play_video).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ShowVideoDialogActivity.this, NewVideoPlayerActivity.class);
                intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, ShowVideoDialogActivity.this.video);
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(ShowVideoDialogActivity.this.getApplicationContext(), Uri.fromFile(new File(ShowVideoDialogActivity.this.video)));
                    intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                    intent.putExtra(NewVideoPlayerActivity.EXTRA_IS_PLAYER_STARTED_FROM_GALLERY, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ShowVideoDialogActivity.this.startActivityForResult(intent, GalleryActivity.REQUEST_VIEW);
            }
        });
        findViewById(C0793R.C0795id.img_more).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShowVideoDialogActivity.this.onBackPressed();
            }
        });
        findViewById(C0793R.C0795id.img_like_rate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShowVideoDialogActivity.this.rateDialogFragment = new UserRateDialogFragment();
                ShowVideoDialogActivity.this.rateDialogFragment.show(ShowVideoDialogActivity.this.getSupportFragmentManager(), "rate");
            }
        });
        findViewById(C0793R.C0795id.img_edit_image).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!ShowVideoDialogActivity.this.isVideo) {
                    Intent intent = new Intent(ShowVideoDialogActivity.this, ImageEditActivity.class);
                    intent.putExtra("image", ShowVideoDialogActivity.this.video);
                    ShowVideoDialogActivity.this.startActivityForResult(intent, 3421);
                    return;
                }
                Intent intent2 = new Intent(ShowVideoDialogActivity.this, TrimVideoActivity.class);
                ShowVideoDialogActivity showVideoDialogActivity = ShowVideoDialogActivity.this;
                intent2.putExtra(TrimVideoActivity.EXTRA_VIDEO_PATH, FileUtils.getPath(showVideoDialogActivity, Uri.fromFile(new File(showVideoDialogActivity.video))));
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(ShowVideoDialogActivity.this.getApplicationContext(), Uri.fromFile(new File(ShowVideoDialogActivity.this.video)));
                    intent2.putExtra(TrimVideoActivity.EXTRA_VIDEO_DURATION, Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ShowVideoDialogActivity.this.startActivityForResult(intent2, 3421);
            }
        });
        if (this.isVideo) {
            findViewById(C0793R.C0795id.img_edit_image).setVisibility(0);
            findViewById(C0793R.C0795id.line_img_edit).setVisibility(0);
            ((ImageView) findViewById(C0793R.C0795id.img_edit_image)).setImageResource(C0793R.C0794drawable.ic_trim);
        } else {
            findViewById(C0793R.C0795id.img_edit_image).setVisibility(0);
            findViewById(C0793R.C0795id.line_img_edit).setVisibility(0);
            ((ImageView) findViewById(C0793R.C0795id.img_edit_image)).setImageResource(C0793R.C0794drawable.ic_img_edit);
        }
        findViewById(C0793R.C0795id.img_share_video).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String str = "video";
                if (ShowVideoDialogActivity.this.isVideo) {
                    ShareDialogFragment shareDialogFragment = new ShareDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(str, ShowVideoDialogActivity.this.video);
                    shareDialogFragment.setArguments(bundle);
                    shareDialogFragment.show(ShowVideoDialogActivity.this.getSupportFragmentManager(), "asd");
                    return;
                }
                AppUtils.addCount(ShowVideoDialogActivity.this, 4);
                ShareImageDialogFragment shareImageDialogFragment = new ShareImageDialogFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(str, ShowVideoDialogActivity.this.video);
                shareImageDialogFragment.setArguments(bundle2);
                shareImageDialogFragment.show(ShowVideoDialogActivity.this.getSupportFragmentManager(), "IMG");
                ShowVideoDialogActivity.this.addToFireBaseAnalytics(false);
            }
        });
        findViewById(C0793R.C0795id.img_delete_video).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DeleteCheckDialogFragment deleteCheckDialogFragment = new DeleteCheckDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("video", ShowVideoDialogActivity.this.video);
                bundle.putBoolean("isVideo", ShowVideoDialogActivity.this.isVideo);
                deleteCheckDialogFragment.setArguments(bundle);
                if (!ShowVideoDialogActivity.this.isFinishing()) {
                    deleteCheckDialogFragment.show(ShowVideoDialogActivity.this.getSupportFragmentManager(), "asd");
                }
            }
        });
        try {
            if (this.isVideo) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            File file = new File(ShowVideoDialogActivity.this.video);
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(ShowVideoDialogActivity.this, Uri.fromFile(new File(ShowVideoDialogActivity.this.video)));
                            String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
                            if (file.length() == 0 || extractMetadata == null || extractMetadata.length() == 0 || extractMetadata.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                                ShowVideoDialogActivity.this.findViewById(C0793R.C0795id.lay_main).setVisibility(4);
                                SettingsListDialogFragment settingsListDialogFragment = new SettingsListDialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("type", 5);
                                settingsListDialogFragment.setArguments(bundle);
                                settingsListDialogFragment.show(ShowVideoDialogActivity.this.getSupportFragmentManager(), "asd");
                                file.delete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 100);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        AlertDialog alertDialog = this.alert;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.alert.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        UnifiedNativeAdView unifiedNativeAdView = this.nativeAdView;
        if (unifiedNativeAdView != null) {
            unifiedNativeAdView.destroy();
        }
    }

    public void onBackPressed() {
        finish();
    }

    private void playStoreRating() {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(getPackageName());
        String str = "android.intent.action.VIEW";
        Intent intent = new Intent(str, Uri.parse(sb.toString()));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://play.google.com/store/apps/details?id=");
            sb2.append(getPackageName());
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i != 734) {
                if (i == 3411) {
                    finish();
                } else if (i == 3421 && intent != null) {
                    final String stringExtra = intent.getStringExtra("imageEdit");
                    if (stringExtra == null) {
                        return;
                    }
                    if (this.isVideo) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(ShowVideoDialogActivity.this, NewVideoPlayerActivity.class);
                                intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, stringExtra);
                                ShowVideoDialogActivity.this.startActivityForResult(intent, GalleryActivity.REQUEST_VIEW);
                            }
                        }, 400);
                        return;
                    }
                    Intent intent2 = new Intent(this, ShowImagesActivity.class);
                    intent2.putExtra("ImgPath", stringExtra);
                    startActivityForResult(intent2, GalleryActivity.REQUEST_VIEW_IMAGES);
                }
            }
        } else if (i2 == 0 && i == 734) {
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void addToFireBaseAnalytics(boolean z) {
        this.isShared = true;
        if (z) {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
        } else {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        finish();
    }

    public void onNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
        addValuesAppInstallAdView(unifiedNativeAd);
    }
}
