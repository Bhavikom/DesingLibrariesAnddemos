package com.yasoka.eazyscreenrecord.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
//import android.support.p003v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ezscreenrecorder.BuildConfig;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.fragments.RateDialogFragment;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTarget;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTargetSequence1;
import com.yasoka.eazyscreenrecord.utils.taptargetview.ViewTapTarget;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.fragments.RateDialogFragment;
import com.ezscreenrecorder.model.SharedDataForOtherApp;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.taptargetview.TapTarget;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence1;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence1.Listener;
import com.ezscreenrecorder.utils.taptargetview.ViewTapTarget;
import com.facebook.internal.AnalyticsEvents;
import com.google.android.gms.common.util.GmsVersion;*/
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnDismissListener {
    public static final int CHECK_ALL_PERMISSIONS = 341;
    public static final String SERVICE_CHECK = "ServvericeCheck";
    public static final String SHARED_NAME = "SharedDataVideoRecorder";
    public static final String SHARED_NAME2 = "SharedDataVideoRecorder2";
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 324;
    private static final String VIDEO_RECORD = "AppVideoSupport";
    public static SharedDataForOtherApp showDirectly;
    /* access modifiers changed from: private */
    public TapTargetSequence1 sequence;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_NAME, 0);
        setShared();
        if (getIntent() != null) {
            String str = "AppPackage";
            if (getIntent().getStringExtra(str) != null) {
                String str2 = "AppName";
                if (getIntent().getStringExtra(str2) != null) {
                    String str3 = "AppSupportEmail";
                    if (getIntent().getStringExtra(str3) != null) {
                        showDirectly = new SharedDataForOtherApp(getIntent().getStringExtra(str), getIntent().getStringExtra(str2), getIntent().getStringExtra(str3));
                        checkPermissionForOverlay();
                        return;
                    }
                }
            }
        }
        String str4 = SERVICE_CHECK;
        if (sharedPreferences.getBoolean(str4, false)) {
            showDirectly = null;
            checkPermissionForOverlay();
        } else {
            this.sequence = new TapTargetSequence1(this).
                    targets(TapTarget.forView(findViewById(C0793R.C0795id.img_video),
                            getString(C0793R.string.txt_video_title), getString(C0793R.string.txt_video_desc)).
                            targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).
                            tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.img_video_user),
                            getString(C0793R.string.txt_video_user_title), getString(C0793R.string.txt_video_user_desc)).
                            targetCircleColor(C0793R.color.colorPrimaryOpacity).textColor(C0793R.color.colorWhite).
                            drawShadow(true).tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.img_camera),
                            getString(C0793R.string.txt_img_title), getString(C0793R.string.txt_img_desc)).
                            targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).
                            tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.img_gallery),
                            getString(C0793R.string.txt_gallery), getString(C0793R.string.txt_gallery_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).
                            drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false),
                            TapTarget.forView(findViewById(C0793R.C0795id.img_whiteboard), getString(C0793R.string.txt_settings),
                                    getString(C0793R.string.txt_settings_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).
                                    drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false),
                            TapTarget.forView(findViewById(C0793R.C0795id.img_more), getString(C0793R.string.txt_help_title),
                                    getString(C0793R.string.txt_help_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).
                                    drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false)).listener(new TapTargetSequence1.Listener() {
                public void onSequenceFinish() {
                    System.out.println("FINISH");
                    MainActivity.showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
                    MainActivity.this.checkPermissionForOverlay();
                }

                public void onSequenceStep(TapTarget tapTarget) {
                    System.out.println("SEQuest STEP");
                    if (tapTarget instanceof ViewTapTarget) {
                        MainActivity.showDirectly = null;
                        MainActivity.this.startFloatingService();
                        int id = ((ViewTapTarget) tapTarget).getView().getId();
                        String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
                        switch (id) {
                            case C0793R.C0795id.img_camera /*2131296797*/:
                                Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
                                intent.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
                                intent.addFlags(268468224);
                                MainActivity.this.startActivity(intent);
                                return;
                            case C0793R.C0795id.img_gallery /*2131296811*/:
                                Intent intent2 = new Intent(MainActivity.this, GalleryActivity.class);
                                intent2.addFlags(268468224);
                                MainActivity.this.startActivity(intent2);
                                return;
                            case C0793R.C0795id.img_more /*2131296815*/:
                                MainActivity.this.sequence.showNext();
                                return;
                            case C0793R.C0795id.img_video /*2131296829*/:
                                Intent intent3 = new Intent(MainActivity.this, RecordingActivity.class);
                                intent3.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                intent3.addFlags(268468224);
                                MainActivity.this.startActivity(intent3);
                                return;
                            case C0793R.C0795id.img_video_user /*2131296831*/:
                                Intent intent4 = new Intent(MainActivity.this, RecordingActivity.class);
                                intent4.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                intent4.addFlags(268468224);
                                MainActivity.this.startActivity(intent4);
                                return;
                            case C0793R.C0795id.img_whiteboard /*2131296833*/:
                                Intent intent5 = new Intent(MainActivity.this, GalleryActivity.class);
                                intent5.putExtra(GalleryActivity.SETTINGS_VIEW, true);
                                intent5.addFlags(268468224);
                                MainActivity.this.startActivity(intent5);
                                return;
                            default:
                                return;
                        }
                    }
                }

                public void onSequenceCanceled(TapTarget tapTarget) {
                    //Log.e("sd", AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_CANCELLED);
                }

                public void onOuterCircleClick() {
                    Log.e("sd", "Outer Clocick");
                }
            });
            this.sequence.contentView = (ViewGroup) findViewById(C0793R.C0795id.activity_main);
            this.sequence.start();
            sharedPreferences.edit().putBoolean(str4, true).apply();
        }
        findViewById(C0793R.C0795id.btn_skip).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
                MainActivity.this.checkPermissionForOverlay();
            }
        });
        findViewById(C0793R.C0795id.btn_next).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.sequence.showNext();
            }
        });
    }

    private void setShared() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String str = "example_list_frame_rate";
        if (!defaultSharedPreferences.contains(str)) {
            PreferenceHelper.getInstance().setPrefRecordAudio(true);
            PreferenceHelper.getInstance().setPrefRecordingOrientation("Auto");
            //todo defaultSharedPreferences.edit().putString(str, String.valueOf(30)).putString("example_list_bit_rate", String.valueOf(GmsVersion.VERSION_LONGHORN)).putBoolean("notifications_user_interaction", true).apply();
        }
        getSharedPreferences(SHARED_NAME, 0).edit().putBoolean("isFirstTime", false).putBoolean("first_screen1", false).putBoolean("first_screen2", false).putBoolean("first_screen3", false).putBoolean("first_time", false).apply();
    }

    public void onBackPressed() {
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
        showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
        checkPermissionForOverlay();
    }

    /* access modifiers changed from: private */
    public void checkPermissionForOverlay() {
        String str = "asd";
        if (VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                RateDialogFragment.getInstance(2).show(getSupportFragmentManager(), str);
            } else {
                startFloatingService();
            }
        } else if (isXiomi()) {
            RateDialogFragment.getInstance(2).show(getSupportFragmentManager(), str);
        } else {
            startFloatingService();
        }
    }

    /* access modifiers changed from: private */
    public void startFloatingService() {
        addShortcut2(true);
        addShortcut2(false);
        addGalleryShortcut();
        startService(new Intent(this, FloatingService.class));
        finish();
    }

    private void addGalleryShortcut() {
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.addFlags(268435456);
        Intent intent2 = new Intent();
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", getString(C0793R.string.gallery));
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(getApplicationContext(), C0793R.C0794drawable.ic_gallery));
        intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent2.putExtra("duplicate", false);
        getApplicationContext().sendBroadcast(intent2);
    }

    private void addShortcut(boolean z) {
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.putExtra(FloatingService.TYPE, z);
        intent.addFlags(268435456);
        Intent intent2 = new Intent();
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", getString(z ? C0793R.string.screenshots : C0793R.string.recordings));
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(getApplicationContext(), z ? C0793R.mipmap.ic_screenshot_shortcut : C0793R.mipmap.ic_recording_shortcut));
        intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent2.putExtra("duplicate", false);
        getApplicationContext().sendBroadcast(intent2);
    }

    private void addShortcut2(boolean z) {
        Intent intent = new Intent(getApplicationContext(), RecordingActivity.class);
        String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
        if (z) {
            intent.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
        } else {
            intent.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
        }
        intent.addFlags(268435456);
        Intent intent2 = new Intent();
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", getString(z ? C0793R.string.take_screen_shot : C0793R.string.record_screen));
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(getApplicationContext(), z ? C0793R.mipmap.ic_screenshot_shortcut : C0793R.mipmap.ic_recording_shortcut));
        intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent2.putExtra("duplicate", false);
        getApplicationContext().sendBroadcast(intent2);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (VERSION.SDK_INT < 23) {
            startFloatingService();
        } else if (!Settings.canDrawOverlays(this)) {
            checkPermissionForOverlay();
        } else {
            startFloatingService();
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        try {
            startFloatingService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isXiomi() {
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
}
