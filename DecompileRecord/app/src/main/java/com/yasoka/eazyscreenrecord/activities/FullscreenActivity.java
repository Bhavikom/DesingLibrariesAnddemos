package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.app.FragmentPagerAdapter;
import android.support.p000v4.view.ViewPager;
import android.support.p003v7.app.AppCompatActivity;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.fragments.RateDialogFragment;
import com.ezscreenrecorder.fullscreen.FullFragment;
import com.ezscreenrecorder.fullscreen.FullFragment2;
import com.ezscreenrecorder.model.SharedDataForOtherApp;
import com.ezscreenrecorder.utils.PreferenceHelper;*/
import com.ezscreenrecorder.R;
import com.google.android.gms.common.util.GmsVersion;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.fragments.RateDialogFragment;
import com.yasoka.eazyscreenrecord.fullscreen.FullFragment;
import com.yasoka.eazyscreenrecord.fullscreen.FullFragment2;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FullscreenActivity extends AppCompatActivity {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 324;
    public static final int EXTRA_HELP_TYPE_SHOW_DEFAULT = 9013;
    public static final int EXTRA_HELP_TYPE_SHOW_FLOATING_EXIT = 9012;
    public static final int EXTRA_HELP_TYPE_SHOW_NOTIFICATION = 9011;
    public static final String KEY_SHOW_HELP = "full_screen_help_key";
    /* access modifiers changed from: private */
    public int showType = -1;
    private ViewPager viewPager;

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList = new ArrayList();

        SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            if (FullscreenActivity.this.showType == 9012) {
                this.mList.add(new FullFragment2());
                return;
            }
            this.mList.add(new FullFragment());
            if (FullscreenActivity.this.checkOverlayPermission()) {
                this.mList.add(new FullFragment2());
            }
        }

        public Fragment getItem(int i) {
            return (Fragment) this.mList.get(i);
        }

        public int getCount() {
            return this.mList.size();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

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

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fullscreen);
        if (getIntent() != null) {
            Intent intent = getIntent();
            String str = KEY_SHOW_HELP;
            if (intent.hasExtra(str)) {
                this.showType = getIntent().getIntExtra(str, -1);
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
        setShared();
        String str2 = MainActivity.SERVICE_CHECK;
        if (sharedPreferences.getBoolean(str2, false)) {
            MainActivity.showDirectly = null;
        } else {
            MainActivity.showDirectly = new SharedDataForOtherApp(getIntent().getStringExtra("AppPackage"), getIntent().getStringExtra("AppName"), getIntent().getStringExtra("AppSupportEmail"));
            sharedPreferences.edit().putBoolean(str2, true).apply();
        }
        if (this.showType != 9011) {
            this.viewPager = (ViewPager) findViewById(C0793R.C0795id.view_pager);
            this.viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
            return;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FullscreenActivity.this.startFloatingService();
            }
        }, 50);
    }

    public void moveToNext() {
        if (this.viewPager.getAdapter().getCount() == 2) {
            this.viewPager.setCurrentItem(1, true);
        } else {
            startFloatingService();
        }
    }

    public int getSizeOfViewPager() {
        ViewPager viewPager2 = this.viewPager;
        if (viewPager2 == null || viewPager2.getAdapter() == null) {
            return 0;
        }
        return this.viewPager.getAdapter().getCount();
    }

    private void setShared() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String str = "example_list_frame_rate";
        if (!defaultSharedPreferences.contains(str)) {
            PreferenceHelper.getInstance().setPrefRecordAudio(true);
            PreferenceHelper.getInstance().setPrefRecordingOrientation("Auto");
            defaultSharedPreferences.edit().putString(str, String.valueOf(30)).putString("example_list_bit_rate", String.valueOf(GmsVersion.VERSION_LONGHORN)).putBoolean("notifications_user_interaction", true).apply();
        }
        getSharedPreferences(MainActivity.SHARED_NAME, 0).edit().putBoolean("isFirstTime", false).putBoolean("first_screen1", false).putBoolean("first_screen2", false).putBoolean("first_screen3", false).putBoolean("first_time", false).apply();
    }

    public void onBackPressed() {
        minimizeApp();
    }

    public void minimizeApp() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
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

    public boolean checkOverlayPermission() {
        if (VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }
}
