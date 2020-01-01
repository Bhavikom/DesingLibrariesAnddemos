package com.yasoka.eazyscreenrecord.activities;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p003v7.app.AppCompatActivity;*/
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.BuildConfig;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.fcm.MyFirebaseMessagingService;
import com.ezscreenrecorder.model.SharedDataForOtherApp;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.EEAConsentHelper.OnEEAConsentListener;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.Logger;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.youtubeupload.ResumableUpload;*/
import com.ezscreenrecorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings.Builder;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.fcm.MyFirebaseMessagingService;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.Logger;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.youtubeupload.ResumableUpload;

import io.fabric.sdk.android.Fabric;
import io.reactivex.functions.Consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
//import p009io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity {
    private Animation anim;
    /* access modifiers changed from: private */
    public ImageView appImage;
    private TextView appName;
    /* access modifiers changed from: private */
    public FirebaseRemoteConfig firebaseRemoteConfig;
    private boolean isFromNotification = false;
    /* access modifiers changed from: private */
    public TextView pleaseWaitTxt;
    private SharedPreferences sharedPreferences;
    private File watermarkFile;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (EEAConsentHelper.getInstance().isUserConsentTaken(getApplicationContext()) || !RecorderApplication.getInstance().isNetworkAvailable()) {
            setTheme(16973839);
            getTheme().applyStyle(16973839, true);
            super.onCreate(bundle);
            setContentView((int) R.layout.activity_splash);
            if (!PreferenceHelper.getInstance().getPrefNotificationUserProperty() && RecorderApplication.getInstance().isNetworkAvailable()) {
                FirebaseEventsNewHelper.getInstance().sendNotificationUserProperty(PreferenceHelper.getInstance().getPrefPushNotification() ? 1 : 0);
                PreferenceHelper.getInstance().setPrefNotificationUserProperty(true);
            }
            if (!(getIntent() == null || getIntent().getExtras() == null || getIntent().getExtras().isEmpty())) {
                String str = "noti_type";
                String str2 = "";
                final String string = getIntent().getExtras().getString(str, str2);
                final String string2 = getIntent().getExtras().getString("video_id", str2);
                final String string3 = getIntent().getExtras().getString(str, str2);
                if (!TextUtils.isEmpty(string)) {
                    this.isFromNotification = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this.getApplicationContext(), GalleryActivity.class);
                            intent.putExtra(GalleryActivity.YOU_TUBE_LIST, true);
                            intent.putExtra(ResumableUpload.YOUTUBE_ID, string2);
                            intent.putExtra(MyFirebaseMessagingService.IMAGE_LINK, string3);
                            intent.putExtra(GalleryActivity.IS_MY_VIDEOS, string);
                            SplashActivity.this.startActivity(intent);
                        }
                    }, 300);
                }
            }
            initLaunch();
            return;
        }
        setTheme(R.style.AppTheme);
        getTheme().applyStyle(R.style.AppTheme, true);
        super.onCreate(bundle);
        Fabric.with(this, new Crashlytics());
        setContentView((int) R.layout.activity_consent_splash);
        this.appImage = (ImageView) findViewById(R.id.id_consent_splash_image_view);
        this.appName = (TextView) findViewById(R.id.id_consent_splash_text_view);
        this.pleaseWaitTxt = (TextView) findViewById(R.id.id_consent_splash_wait_txt);
        showImageAnim();
    }

    private void showImageAnim() {
        this.appImage.setAlpha(0.0f);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.appImage.animate().rotationBy(360.0f).alpha(1.0f).setDuration(1000).setListener(new AnimatorListener() {
                    public void onAnimationCancel(Animator animator) {
                    }

                    public void onAnimationRepeat(Animator animator) {
                    }

                    public void onAnimationStart(Animator animator) {
                    }

                    public void onAnimationEnd(Animator animator) {
                        SplashActivity.this.showTextAnim();
                    }
                }).setInterpolator(new LinearInterpolator()).start();
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void showTextAnim() {
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_for_splash_txt_view);
        if (loadAnimation != null) {
            loadAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    SplashActivity.this.getEEAConsent();
                }
            });
            this.appName.startAnimation(loadAnimation);
            this.appName.setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public Animation getBlinkingAnim() {
        this.anim = new AlphaAnimation(0.0f, 1.0f);
        this.anim.setDuration(300);
        this.anim.setRepeatMode(2);
        this.anim.setRepeatCount(-1);
        return this.anim;
    }

    /* access modifiers changed from: private */
    public void stopBlinkingAnim() {
        if (this.anim.isInitialized()) {
            this.anim.cancel();
        }
    }

    private void createWatermark() {
        try {
            this.watermarkFile = new File(AppUtils.getVideoWatermarkDir(getApplicationContext()), AppUtils.APP_WATERMARK_FILE_NAME);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        File file = this.watermarkFile;
        if (file != null && !file.exists()) {
            Bitmap bitmap = null;
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_watermark, null, false).findViewById(R.id.lay_watermark);
            if (linearLayout.getMeasuredHeight() <= 0) {
                bitmap = loadBitmapFromView(linearLayout);
            }
            if (bitmap != null) {
                try {
                    bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(this.watermarkFile));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private Bitmap loadBitmapFromView(View view) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("WatermarkFragment.loadBitmapFromView->");
        sb.append(view.getLayoutParams().width);
        sb.append("<><><>");
        sb.append(view.getLayoutParams().height);
        printStream.println(sb.toString());
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        Bitmap createBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return createBitmap;
    }

    /* access modifiers changed from: private */
    public void getEEAConsent() {
        EEAConsentHelper.getInstance().checkConsentStatus(this, new EEAConsentHelper.OnEEAConsentListener() {
            public void onConsentStart() {
                SplashActivity.this.pleaseWaitTxt.setVisibility(View.VISIBLE);
                SplashActivity.this.pleaseWaitTxt.startAnimation(SplashActivity.this.getBlinkingAnim());
            }

            public void onConsentComplete() {
                SplashActivity.this.stopBlinkingAnim();
                SplashActivity.this.pleaseWaitTxt.setVisibility(View.INVISIBLE);
                SplashActivity.this.initLaunch();
            }

            public void onConsentError(String str) {
                SplashActivity.this.stopBlinkingAnim();
                SplashActivity.this.pleaseWaitTxt.setVisibility(View.INVISIBLE);
                if (!EEAConsentHelper.getInstance().isUserConsentTaken(SplashActivity.this.getApplicationContext()) && EEAConsentHelper.getInstance().isUserFromEEALocation(SplashActivity.this.getApplicationContext())) {
                    Toast.makeText(SplashActivity.this.getApplicationContext(), R.string.id_consent_error_loading_dialog_msg, Toast.LENGTH_SHORT).show();
                }
                SplashActivity.this.initLaunch();
            }
        });
    }

    /* access modifiers changed from: private */
    public void initLaunch() {
        String str = "usageCount";
        this.sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
        try {
            this.sharedPreferences.edit().putInt(str, this.sharedPreferences.getInt(str, 0) + 1).apply();
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        Logger.getInstance().error(((InstanceIdResult) task.getResult()).getToken());
                    }
                }
            });
            ServerAPI.getInstance().anonymousUser(this).subscribe(new Consumer<String>() {
                public void accept(String str) throws Exception {
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                    th.printStackTrace();
                }
            });
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (this.sharedPreferences.getBoolean(MainActivity.SERVICE_CHECK, false)) {
            MainActivity.showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
            if (isMyServiceRunning(FloatingService.class)) {
                startActivity(new Intent(this, GalleryActivity.class));
            } else {
                Intent intent = new Intent(this, FloatingService.class);
                Intent intent2 = getIntent();
                String str2 = FloatingService.EXTRA_STARTED_FROM_OTHER_APPS;
                if (intent2 != null && getIntent().hasExtra(str2) && getIntent().getBooleanExtra(str2, false)) {
                    intent.putExtra(str2, true);
                }
                if (this.isFromNotification) {
                    intent.putExtra(str2, true);
                }
                if (VERSION.SDK_INT >= 26) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
            }
            finish();
        } else {
            getFirebaseRemoteConfigData();
        }
        overridePendingTransition(0, 0);
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isXiomi() {
        String str = Build.MANUFACTURER;
        boolean z = false;
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.contains("xiaomi") || lowerCase.contains("meizu")) {
            z = true;
        }
        return z;
    }

    private boolean checkOverlayPermission() {
        if (VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void getFirebaseRemoteConfigData() {
        if (this.firebaseRemoteConfig == null) {
            this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        }
        this.firebaseRemoteConfig.setConfigSettings(new Builder().build());
        this.firebaseRemoteConfig.setDefaults((int) R.xml.remote_config_defaults);
        this.firebaseRemoteConfig.fetch(TimeUnit.HOURS.toSeconds(12)).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SplashActivity.this.firebaseRemoteConfig.activateFetched();
                    long j = SplashActivity.this.firebaseRemoteConfig.getLong("android_on_boarding_experience");
                    if (j == 1) {
                        if (VERSION.SDK_INT >= 23 || SplashActivity.isXiomi()) {
                            SplashActivity splashActivity = SplashActivity.this;
                            splashActivity.startActivity(new Intent(splashActivity, DrawOverAppsPermissionActivity.class));
                        } else {
                            SplashActivity splashActivity2 = SplashActivity.this;
                            splashActivity2.startActivity(new Intent(splashActivity2, FullscreenActivity.class));
                        }
                    } else if (j == 2) {
                        SplashActivity splashActivity3 = SplashActivity.this;
                        splashActivity3.startActivity(new Intent(splashActivity3, TakeTourLaunchActivity.class));
                    }
                    Logger instance = Logger.getInstance();
                    StringBuilder sb = new StringBuilder();
                    sb.append("SPLASH_VALUE RC(Success): ");
                    sb.append(j);
                    instance.error(sb.toString());
                    ActivityCompat.finishAfterTransition(SplashActivity.this);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exc) {
                Logger.getInstance().error("SPLASH_VALUE RC(Failed)");
                if (VERSION.SDK_INT >= 23 || SplashActivity.isXiomi()) {
                    SplashActivity splashActivity = SplashActivity.this;
                    splashActivity.startActivity(new Intent(splashActivity, DrawOverAppsPermissionActivity.class));
                } else {
                    SplashActivity splashActivity2 = SplashActivity.this;
                    splashActivity2.startActivity(new Intent(splashActivity2, FullscreenActivity.class));
                }
                ActivityCompat.finishAfterTransition(SplashActivity.this);
            }
        });
    }
}
