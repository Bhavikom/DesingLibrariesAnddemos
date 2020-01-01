package com.yasoka.eazyscreenrecord.activities;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout.LayoutParams;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
/*import android.support.media.ExifInterface;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.app.FragmentPagerAdapter;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.content.LocalBroadcastManager;
import android.support.p000v4.view.GravityCompat;
import android.support.p000v4.view.ViewPager;
import android.support.p000v4.view.ViewPager.OnPageChangeListener;
import android.support.p000v4.widget.DrawerLayout;
import android.support.p003v7.app.ActionBarDrawerToggle;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.alertdialogs.UserRateDialogFragment;
import com.ezscreenrecorder.fcm.MyFirebaseMessagingService;
import com.ezscreenrecorder.fragments.AudioFragment;
import com.ezscreenrecorder.fragments.GalleryEditFragment;
import com.ezscreenrecorder.fragments.ImagesFragment;
import com.ezscreenrecorder.fragments.RecordingsFragment;
import com.ezscreenrecorder.fragments.VideosFragment;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.InterstitialAdLoader;
import com.ezscreenrecorder.utils.Logger;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.TabType;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.ResumableUpload;
import com.facebook.appevents.AppEventsConstants;
import com.google.common.primitives.Ints;*/
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.alertdialogs.UserRateDialogFragment;
import com.yasoka.eazyscreenrecord.fcm.MyFirebaseMessagingService;
import com.yasoka.eazyscreenrecord.fragments.AudioFragment;
import com.yasoka.eazyscreenrecord.fragments.GalleryEditFragment;
import com.yasoka.eazyscreenrecord.fragments.ImagesFragment;
import com.yasoka.eazyscreenrecord.fragments.RecordingsFragment;
import com.yasoka.eazyscreenrecord.fragments.VideosFragment;
import com.yasoka.eazyscreenrecord.model.EventBusTypes;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.InterstitialAdLoader;
import com.yasoka.eazyscreenrecord.utils.Logger;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.TabType;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;
import com.yasoka.eazyscreenrecord.youtubeupload.ResumableUpload;

import org.greenrobot.eventbus.EventBus;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/*import org.greenrobot.eventbus.EventBus;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.disposables.Disposable;
import p009io.reactivex.schedulers.Schedulers;*/

public class GalleryActivity extends AppCompatActivity implements OnDismissListener {
    public static final String ACTION_FILE_DELETED_FROM_DIALOG_ACTIVITY = "key_is_file_deleted";
    public static final String EXTRA_TYPE_OF_ACTION_DIALOG_ACTIVITY = "action_activity_dialog_key";
    public static final int EXTRA_TYPE_OF_ACTION_DIALOG_TYPE_AUDIO = 3421;
    public static final int EXTRA_TYPE_OF_ACTION_DIALOG_TYPE_VIDEO = 3422;
    public static final int EXTRA_TYPE_OF_ACTION_PREVIEW_SCREEN = 3420;
    public static final int GALLERY_OPEN_AUDIO_LIST = 3440;
    public static final String HELP_VIEW = "HelpScreen";
    public static final String IS_MY_VIDEOS = "is_my_videos";
    public static final String KEY_GALLERY_MOVE_TO_PAGE = "move_to_page_gallery";
    private static final int KEY_SETTINGS_ACTIVITY_REQUEST_CODE = 3415;
    private static final int REQUEST_AUTHORIZATION = 213;
    public static final int REQUEST_IMAGES_SHARE = 3412;
    public static final int REQUEST_VIEW = 734;
    public static final int REQUEST_VIEW_IMAGES = 3411;
    public static final String SETTINGS_VIEW = "Settings";
    public static final int TRIM_REQUEST = 342;
    public static final String YOUTUBE_ID = "youVideoId";
    public static final String YOU_TUBE_LIST = "YouTubeList";
    public static final String YOU_TUBE_LIST_UPLOAD = "vnasdList23";
    private UploadBroadcastReceiver broadcastReceiver;
    /* access modifiers changed from: private */
    public DrawerLayout drawer;
    public boolean isFromGetIntent;
    public boolean isLogoutOrChangeImage;
    public boolean isLogoutOrChangeVideo;
    private boolean isRateDialogAlreadyShowed = false;
    private boolean isStoragePermissionDialogShowed;
    /* access modifiers changed from: private */
    public SectionsPagerAdapter mSectionsPagerAdapter;
    /* access modifiers changed from: private */
    public TabLayout mTabLayout;
    /* access modifiers changed from: private */
    public ViewPager mViewPager;
    public boolean myStartImages;
    public boolean myStartVideos;
    private NavigationView navigationView;
    private Random random = new Random();
    private UserRateDialogFragment rateDialogFragment;
    private SharedPreferences sharedPreferences;

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        /* access modifiers changed from: private */
        public List<Fragment> mList = new ArrayList();

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mList.add(new RecordingsFragment());
            this.mList.add(ImagesFragment.newInstance(false));
            this.mList.add(GalleryEditFragment.getInstance());
            this.mList.add(new AudioFragment());
        }

        public Fragment getItem(int i) {
            return (Fragment) this.mList.get(i);
        }

        public void finishUpdate(ViewGroup viewGroup) {
            try {
                super.finishUpdate(viewGroup);
            } catch (NullPointerException e) {
                Crashlytics.logException(e);
            }
        }

        public int getCount() {
            return this.mList.size();
        }

        public CharSequence getPageTitle(int i) {
            if (i == 0) {
                return GalleryActivity.this.getString(C0793R.string.recordings);
            }
            if (i == 1) {
                return GalleryActivity.this.getString(C0793R.string.screenshots);
            }
            if (i == 2) {
                return GalleryActivity.this.getString(C0793R.string.id_gallery_edit);
            }
            if (i != 3) {
                return null;
            }
            return GalleryActivity.this.getString(C0793R.string.txt_audio);
        }
    }

    private class UploadBroadcastReceiver extends BroadcastReceiver {
        private UploadBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ResumableUpload.REQUEST_AUTHORIZATION_INTENT)) {
                Toast.makeText(context, C0793R.string.id_allow_and_try_again_msg, Toast.LENGTH_SHORT).show();
                GalleryActivity.this.startActivityForResult((Intent) intent.getParcelableExtra(ResumableUpload.REQUEST_AUTHORIZATION_INTENT_PARAM), GalleryActivity.REQUEST_AUTHORIZATION);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_gallery);
        if (getCallingActivity() != null) {
            Logger instance = Logger.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("SplashActivity ");
            sb.append(getCallingActivity().getClassName());
            instance.error(sb.toString());
        } else {
            Logger.getInstance().error("SplashActivity NULL");
        }
        this.sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
        if (FloatingService.mMediaProjection == null) {
            Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
            intent.putExtra(FloatingService.SHOW_GALLERY, true);
            sendBroadcast(intent);
        }
        final Toolbar toolbar = (Toolbar) findViewById(C0793R.C0795id.toolbar);
        setSupportActionBar(toolbar);
        this.myStartVideos = true;
        this.myStartImages = true;
        this.drawer = (DrawerLayout) findViewById(C0793R.C0795id.drawer_layout);
        this.navigationView = (NavigationView) findViewById(C0793R.C0795id.nav_view);
        View headerView = this.navigationView.getHeaderView(0);
        if (headerView != null) {
            ImageView imageView = (ImageView) headerView.findViewById(C0793R.C0795id.img_center);
            ImageView imageView2 = (ImageView) headerView.findViewById(C0793R.C0795id.img_back);
            if (imageView2 != null) {
                Glide.with((FragmentActivity) this).load(Integer.valueOf(C0793R.C0794drawable.mixed_background)).centerCrop().into(imageView2);
            }
            if (imageView != null) {
                Glide.with((FragmentActivity) this).load(Integer.valueOf(C0793R.C0794drawable.rocorder_white)).fitCenter().into(imageView);
            }
        }
        this.navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0085 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean onNavigationItemSelected(@NonNull MenuItem r8) {
                /*
                    r7 = this;
                    com.ezscreenrecorder.activities.GalleryActivity r0 = com.ezscreenrecorder.activities.GalleryActivity.this
                    boolean r0 = r0.isFinishing()
                    r1 = 0
                    if (r0 != 0) goto L_0x0129
                    int r8 = r8.getItemId()
                    r0 = 268435456(0x10000000, float:2.5243549E-29)
                    java.lang.String r2 = "main_floating_action_type"
                    java.lang.String r3 = "android.intent.action.VIEW"
                    r4 = 1
                    switch(r8) {
                        case 2131296271: goto L_0x0110;
                        case 2131296284: goto L_0x00ff;
                        case 2131296285: goto L_0x00de;
                        case 2131296287: goto L_0x00bd;
                        case 2131296295: goto L_0x0058;
                        case 2131296299: goto L_0x0040;
                        case 2131296300: goto L_0x0029;
                        case 2131296302: goto L_0x001f;
                        case 2131296303: goto L_0x0019;
                        default: goto L_0x0017;
                    }
                L_0x0017:
                    goto L_0x0129
                L_0x0019:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    r8.shareApp()
                    return r4
                L_0x001f:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    r8.closeDrawers()
                    return r4
                L_0x0029:
                    android.content.Intent r8 = new android.content.Intent
                    com.ezscreenrecorder.activities.GalleryActivity r1 = com.ezscreenrecorder.activities.GalleryActivity.this
                    java.lang.Class<com.ezscreenrecorder.activities.RecordingActivity> r3 = com.ezscreenrecorder.activities.RecordingActivity.class
                    r8.<init>(r1, r3)
                    r1 = 1340(0x53c, float:1.878E-42)
                    r8.putExtra(r2, r1)
                    r8.addFlags(r0)
                    com.ezscreenrecorder.activities.GalleryActivity r0 = com.ezscreenrecorder.activities.GalleryActivity.this
                    r0.startActivity(r8)
                    return r4
                L_0x0040:
                    android.content.Intent r8 = new android.content.Intent
                    com.ezscreenrecorder.activities.GalleryActivity r3 = com.ezscreenrecorder.activities.GalleryActivity.this
                    java.lang.Class<com.ezscreenrecorder.activities.RecordingActivity> r4 = com.ezscreenrecorder.activities.RecordingActivity.class
                    r8.<init>(r3, r4)
                    r3 = 1341(0x53d, float:1.879E-42)
                    r8.putExtra(r2, r3)
                    r8.addFlags(r0)
                    com.ezscreenrecorder.activities.GalleryActivity r0 = com.ezscreenrecorder.activities.GalleryActivity.this
                    r0.startActivity(r8)
                    goto L_0x0129
                L_0x0058:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    java.lang.String r8 = r8.getPackageName()
                    com.ezscreenrecorder.activities.GalleryActivity r0 = com.ezscreenrecorder.activities.GalleryActivity.this     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    android.content.Intent r2 = new android.content.Intent     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    r5.<init>()     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    java.lang.String r6 = "https://play.google.com/store/apps/details?id="
                    r5.append(r6)     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    r5.append(r8)     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    java.lang.String r5 = r5.toString()     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    android.net.Uri r5 = android.net.Uri.parse(r5)     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    r2.<init>(r3, r5)     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    r0.startActivity(r2)     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    com.ezscreenrecorder.utils.FirebaseEventsNewHelper r0 = com.ezscreenrecorder.utils.FirebaseEventsNewHelper.getInstance()     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    r0.sendPlayStoreRateUsEvent()     // Catch:{ ActivityNotFoundException -> 0x0085 }
                    goto L_0x00bc
                L_0x0085:
                    com.ezscreenrecorder.activities.GalleryActivity r0 = com.ezscreenrecorder.activities.GalleryActivity.this     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    android.content.Intent r2 = new android.content.Intent     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    r5.<init>()     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    java.lang.String r6 = "market://details?id="
                    r5.append(r6)     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    r5.append(r8)     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    java.lang.String r8 = r5.toString()     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    android.net.Uri r8 = android.net.Uri.parse(r8)     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    r2.<init>(r3, r8)     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    r0.startActivity(r2)     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    com.ezscreenrecorder.utils.FirebaseEventsNewHelper r8 = com.ezscreenrecorder.utils.FirebaseEventsNewHelper.getInstance()     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    r8.sendPlayStoreRateUsEvent()     // Catch:{ ActivityNotFoundException -> 0x00ac }
                    goto L_0x00bc
                L_0x00ac:
                    r8 = move-exception
                    r8.printStackTrace()
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    r0 = 2131689696(0x7f0f00e0, float:1.9008415E38)
                    android.widget.Toast r8 = android.widget.Toast.makeText(r8, r0, r1)
                    r8.show()
                L_0x00bc:
                    return r4
                L_0x00bd:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    if (r8 == 0) goto L_0x00ce
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    r8.closeDrawers()
                L_0x00ce:
                    android.os.Handler r8 = new android.os.Handler
                    r8.<init>()
                    com.ezscreenrecorder.activities.GalleryActivity$1$2 r0 = new com.ezscreenrecorder.activities.GalleryActivity$1$2
                    r0.<init>()
                    r2 = 200(0xc8, double:9.9E-322)
                    r8.postDelayed(r0, r2)
                    goto L_0x0129
                L_0x00de:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    if (r8 == 0) goto L_0x00ef
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    r8.closeDrawers()
                L_0x00ef:
                    android.os.Handler r8 = new android.os.Handler
                    r8.<init>()
                    com.ezscreenrecorder.activities.GalleryActivity$1$1 r0 = new com.ezscreenrecorder.activities.GalleryActivity$1$1
                    r0.<init>()
                    r1 = 230(0xe6, double:1.136E-321)
                    r8.postDelayed(r0, r1)
                    return r4
                L_0x00ff:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.content.Intent r0 = new android.content.Intent
                    java.lang.String r1 = "http://appscreenrecorder.com/faqs"
                    android.net.Uri r1 = android.net.Uri.parse(r1)
                    r0.<init>(r3, r1)
                    r8.startActivity(r0)
                    return r4
                L_0x0110:
                    com.ezscreenrecorder.activities.GalleryActivity r8 = com.ezscreenrecorder.activities.GalleryActivity.this
                    android.support.v4.widget.DrawerLayout r8 = r8.drawer
                    r8.closeDrawers()
                    android.os.Handler r8 = new android.os.Handler
                    r8.<init>()
                    com.ezscreenrecorder.activities.GalleryActivity$1$3 r0 = new com.ezscreenrecorder.activities.GalleryActivity$1$3
                    r0.<init>()
                    r1 = 300(0x12c, double:1.48E-321)
                    r8.postDelayed(r0, r1)
                    return r4
                L_0x0129:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.activities.GalleryActivity.C08101.onNavigationItemSelected(android.view.MenuItem):boolean");
            }
        });
        ActionBarDrawerToggle r2 = new ActionBarDrawerToggle(this, this.drawer, toolbar, C0793R.string.openDrawer, C0793R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };
        this.drawer.addDrawerListener(r2);
        r2.syncState();
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(C0793R.C0795id.container);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mTabLayout = (TabLayout) findViewById(C0793R.C0795id.tab_layout);
        this.mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabReselected(Tab tab) {
            }

            public void onTabSelected(Tab tab) {
                tab.getCustomView().findViewById(C0793R.C0795id.tab_title).setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(C0793R.C0795id.tab_img);
                if (imageView != null) {
                    imageView.setColorFilter(Color.parseColor("#FFFFFF"));
                }
                GalleryActivity.this.mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(Tab tab) {
                tab.getCustomView().findViewById(C0793R.C0795id.tab_title).setVisibility(View.GONE);
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(C0793R.C0795id.tab_img);
                if (imageView != null) {
                    imageView.setColorFilter(Color.parseColor("#50FFFFFF"));
                }
            }
        });
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                Tab tabAt = GalleryActivity.this.mTabLayout.getTabAt(i);
                if (tabAt != null) {
                    tabAt.select();
                }
                Fragment item = GalleryActivity.this.mSectionsPagerAdapter.getItem(i);
                if (item != null) {
                    LayoutParams layoutParams = (LayoutParams) toolbar.getLayoutParams();
                    if (item instanceof AudioFragment) {
                        layoutParams.setScrollFlags(0);
                    } else if (item instanceof GalleryEditFragment) {
                        layoutParams.setScrollFlags(0);
                    } else {
                        layoutParams.setScrollFlags(21);
                    }
                    toolbar.setLayoutParams(layoutParams);
                }
            }
        });
        addTabs();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(112);
        notificationManager.cancel(ResumableUpload.PLAYBACK_NOTIFICATION_ID);
        this.mViewPager.setOffscreenPageLimit(3);
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
    }

    public boolean checkStoragePermission() {
        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1121 && iArr.length > 0) {
            if (iArr[0] == 0) {
                SectionsPagerAdapter sectionsPagerAdapter = this.mSectionsPagerAdapter;
                if (sectionsPagerAdapter != null && sectionsPagerAdapter.mList.size() > 0) {
                    for (int i2 = 0; i2 < this.mSectionsPagerAdapter.mList.size(); i2++) {
                        Fragment fragment = (Fragment) this.mSectionsPagerAdapter.mList.get(i2);
                        if (fragment instanceof ImagesFragment) {
                            ((ImagesFragment) fragment).refreshList();
                        } else if (fragment instanceof VideosFragment) {
                            ((VideosFragment) fragment).refreshList();
                        } else if (fragment instanceof RecordingsFragment) {
                            ((RecordingsFragment) fragment).refreshLocalList();
                        } else if (fragment instanceof GalleryEditFragment) {
                            ((GalleryEditFragment) fragment).refreshList();
                        } else if (fragment instanceof AudioFragment) {
                            ((AudioFragment) fragment).refreshList();
                        }
                    }
                }
            } else if (iArr[0] == -1) {
                showStoragePermissionErrorDailog();
            }
        }
    }

    private void showStoragePermissionErrorDailog() {
        new AlertDialog.Builder(this).setMessage((int) C0793R.string.id_storage_permission_failed_dialog_message).setPositiveButton((int) C0793R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                    GalleryActivity.this.requestStoragePermission();
                } else if (!GalleryActivity.this.isFinishing()) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addCategory("android.intent.category.DEFAULT");
                    StringBuilder sb = new StringBuilder();
                    sb.append("package:");
                    sb.append(GalleryActivity.this.getPackageName());
                    intent.setData(Uri.parse(sb.toString()));
                    intent.addFlags(268435456);
                    intent.addFlags(Ints.MAX_POWER_OF_TWO);
                    intent.addFlags(8388608);
                    GalleryActivity.this.startActivity(intent);
                }
            }
        }).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(GalleryActivity.this.getApplicationContext(), C0793R.string.id_storage_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
            }
        }).show();
        this.isStoragePermissionDialogShowed = true;
    }

    public void navigateToImage() {
        if (!this.isFromGetIntent) {
            this.mViewPager.setCurrentItem(1);
        }
    }

    /* access modifiers changed from: private */
    public void shareApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        String str = "2131689944 ";
        sb.append(str);
        sb.append(getString(C0793R.string.app_name));
        intent.putExtra("android.intent.extra.TITLE", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(getString(C0793R.string.app_name));
        intent.putExtra("android.intent.extra.SUBJECT", sb2.toString());
        intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.share_video_txt));
        startActivity(Intent.createChooser(intent, getString(C0793R.string.share)));
        FirebaseEventsNewHelper.getInstance().sendShareEvent(FirebaseEventsNewHelper.SHARE_TYPE_APP);
    }

    private void addTabs() {
        int i = 0;
        while (i < TabType.values().length) {
            TabLayout tabLayout = this.mTabLayout;
            tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(TabType.values()[i], this.mTabLayout.getSelectedTabPosition() == i)));
            i++;
        }
        Tab tabAt = this.mTabLayout.getTabAt(0);
        if (tabAt != null) {
            tabAt.getCustomView().findViewById(C0793R.C0795id.tab_title).setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) tabAt.getCustomView().findViewById(C0793R.C0795id.tab_img);
            if (imageView != null) {
                imageView.setColorFilter(Color.parseColor("#FFFFFF"));
            }
            this.mViewPager.setCurrentItem(tabAt.getPosition());
        }
    }

    public View getTabView(TabType tabType, boolean z) {
        View inflate = LayoutInflater.from(this).inflate(C0793R.layout.custom_bottom_lay, null);
        ImageView imageView = (ImageView) inflate.findViewById(C0793R.C0795id.tab_img);
        ((TextView) inflate.findViewById(C0793R.C0795id.tab_title)).setText(tabType.getTitle());
        imageView.setImageResource(tabType.getDefaultImg());
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String str = KEY_GALLERY_MOVE_TO_PAGE;
            if (intent.hasExtra(str)) {
                int intExtra = intent.getIntExtra(str, -1);
                if (intExtra != -1) {
                    this.isFromGetIntent = true;
                    if (intExtra == 3440) {
                        this.mViewPager.postDelayed(new Runnable() {
                            public void run() {
                                GalleryActivity.this.mViewPager.setCurrentItem(3);
                            }
                        }, 40);
                        return;
                    }
                }
            }
            String str2 = FloatingService.GALLERY_TYPE_IMAGE;
            if (intent.getBooleanExtra(str2, false)) {
                this.isFromGetIntent = true;
                this.mViewPager.postDelayed(new Runnable() {
                    public void run() {
                        GalleryActivity.this.mViewPager.setCurrentItem(1);
                    }
                }, 200);
            }
            boolean booleanExtra = intent.getBooleanExtra(str2, false);
            String str3 = FloatingService.GALLERY_VIDEO_PLAY;
            if (booleanExtra) {
                if (intent.getStringExtra(str3) != null) {
                    this.isFromGetIntent = true;
                    Intent intent2 = new Intent(this, ShowImageActivity.class);
                    intent2.putExtra("ImgP  ath", intent.getStringExtra(str3));
                    startActivity(intent2);
                }
            } else if (intent.getStringExtra(str3) != null) {
                this.isFromGetIntent = true;
                Intent intent3 = new Intent(this, NewVideoPlayerActivity.class);
                intent3.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, intent.getStringExtra(str3));
                startActivity(intent3);
            } else if (intent.getBooleanExtra(YOU_TUBE_LIST, false)) {
                this.mViewPager.postDelayed(new Runnable() {
                    public void run() {
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("GALLERY-->");
                        Intent intent = intent;
                        String str = GalleryActivity.IS_MY_VIDEOS;
                        sb.append(intent.getStringExtra(str));
                        sb.append("<>LINK-->");
                        Intent intent2 = intent;
                        String str2 = MyFirebaseMessagingService.IMAGE_LINK;
                        sb.append(intent2.getStringExtra(str2));
                        printStream.println(sb.toString());
                        if (intent.getStringExtra(str).equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
                            GalleryActivity galleryActivity = GalleryActivity.this;
                            galleryActivity.isFromGetIntent = true;
                            galleryActivity.mViewPager.setCurrentItem(0);
                            Fragment item = GalleryActivity.this.mSectionsPagerAdapter.getItem(0);
                            if (item != null && (item instanceof VideosFragment)) {
                                ((VideosFragment) item).getMyVideos();
                            }
                        }
                        if (intent.getStringExtra(str).equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                            Intent intent3 = intent;
                            String str3 = ResumableUpload.YOUTUBE_ID;
                            if (intent3.getStringExtra(str3) != null) {
                                GalleryActivity galleryActivity2 = GalleryActivity.this;
                                galleryActivity2.isFromGetIntent = true;
                                galleryActivity2.mViewPager.setCurrentItem(0);
                                Fragment item2 = GalleryActivity.this.mSectionsPagerAdapter.getItem(1);
                                if (item2 != null && (item2 instanceof VideosFragment)) {
                                    ((VideosFragment) item2).getAllVideos();
                                }
                                final String stringExtra = intent.getStringExtra(str3);
                                Single.timer(500, TimeUnit.MILLISECONDS).subscribe((SingleObserver<? super T>) new SingleObserver<Long>() {
                                    public void onError(Throwable th) {
                                    }

                                    public void onSubscribe(Disposable disposable) {
                                    }

                                    public void onSuccess(Long l) {
                                        String str = "android.intent.action.VIEW";
                                        try {
                                            GalleryActivity galleryActivity = GalleryActivity.this;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("vnd.youtube://");
                                            sb.append(stringExtra);
                                            galleryActivity.startActivity(new Intent(str, Uri.parse(sb.toString())));
                                        } catch (Exception unused) {
                                            GalleryActivity galleryActivity2 = GalleryActivity.this;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("http://www.youtube.com/watch?v=");
                                            sb2.append(stringExtra);
                                            galleryActivity2.startActivity(new Intent(str, Uri.parse(sb2.toString())));
                                        }
                                    }
                                });
                            }
                        }
                        if (intent.getStringExtra(str).equalsIgnoreCase("4") && intent.getStringExtra(str2) != null) {
                            GalleryActivity galleryActivity3 = GalleryActivity.this;
                            galleryActivity3.isFromGetIntent = true;
                            galleryActivity3.mViewPager.setCurrentItem(1);
                            String stringExtra2 = intent.getStringExtra(str2);
                            Fragment item3 = GalleryActivity.this.mSectionsPagerAdapter.getItem(1);
                            if (item3 != null && (item3 instanceof ImagesFragment)) {
                                ((ImagesFragment) item3).setCloudImage(stringExtra2);
                                Intent intent4 = new Intent(GalleryActivity.this, ShowImagesActivity.class);
                                intent4.putExtra("ImgPath", stringExtra2);
                                intent4.putExtra("sharePath", stringExtra2);
                                intent4.putExtra("isFromServer", true);
                                intent4.putExtra("currentPosition", 0);
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(stringExtra2);
                                intent4.putStringArrayListExtra("files2", arrayList);
                                GalleryActivity.this.startActivityForResult(intent4, GalleryActivity.REQUEST_VIEW_IMAGES);
                            }
                        }
                    }
                }, 200);
            } else if (intent.getBooleanExtra(SETTINGS_VIEW, false)) {
                this.isFromGetIntent = true;
                this.mViewPager.postDelayed(new Runnable() {
                    public void run() {
                        GalleryActivity galleryActivity = GalleryActivity.this;
                        galleryActivity.startActivityForResult(new Intent(galleryActivity.getApplicationContext(), SettingsActivity.class),
                                GalleryActivity.KEY_SETTINGS_ACTIVITY_REQUEST_CODE);
                    }
                }, 50);
            } else if (intent.getBooleanExtra(HELP_VIEW, false)) {
                this.isFromGetIntent = true;
                startActivity(new Intent(this, HelpActivity.class));
            } else {
                String str4 = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
                if (intent.hasExtra(str4)) {
                    final int intExtra2 = intent.getIntExtra(str4, -1);
                    ActivityOptions makeCustomAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in_200, C0793R.anim.fade_out_200);
                    if (intExtra2 != 1440) {
                        switch (intExtra2) {
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                                Intent intent4 = new Intent(this, ShowPreviewScreenActivity.class);
                                intent4.putExtras(intent);
                                ActivityCompat.startActivityForResult(this, intent4, EXTRA_TYPE_OF_ACTION_PREVIEW_SCREEN, makeCustomAnimation.toBundle());
                                break;
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                                Intent intent5 = new Intent(this, ShowPreviewScreenActivity.class);
                                intent5.putExtras(intent);
                                startActivityForResult(intent5, EXTRA_TYPE_OF_ACTION_PREVIEW_SCREEN);
                                break;
                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                                Intent intent6 = new Intent(this, ShowPreviewScreenActivity.class);
                                intent6.putExtras(intent);
                                ActivityCompat.startActivityForResult(this, intent6, EXTRA_TYPE_OF_ACTION_PREVIEW_SCREEN, makeCustomAnimation.toBundle());
                                break;
                        }
                    } else {
                        startActivity(new Intent(this, GamesListActivity.class));
                    }
                    Single.create(new SingleOnSubscribe<Integer>() {
                        public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                            int i = 0;
                            while (i < GalleryActivity.this.mSectionsPagerAdapter.getCount()) {
                                try {
                                    Fragment item = GalleryActivity.this.mSectionsPagerAdapter.getItem(i);
                                    switch (intExtra2) {
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                                            if (!(item instanceof ImagesFragment)) {
                                                break;
                                            } else {
                                                singleEmitter.onSuccess(Integer.valueOf(i));
                                                break;
                                            }
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                                            if (!(item instanceof VideosFragment)) {
                                                break;
                                            } else {
                                                singleEmitter.onSuccess(Integer.valueOf(i));
                                                break;
                                            }
                                        case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                                            if (!(item instanceof AudioFragment)) {
                                                break;
                                            } else {
                                                singleEmitter.onSuccess(Integer.valueOf(i));
                                                break;
                                            }
                                    }
                                    i++;
                                } catch (Exception e) {
                                    singleEmitter.onError(e);
                                    return;
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new SingleObserver<Integer>() {
                        public void onError(Throwable th) {
                        }

                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onSuccess(final Integer num) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (GalleryActivity.this.mViewPager != null) {
                                        GalleryActivity.this.mViewPager.setCurrentItem(num.intValue(), false);
                                    }
                                }
                            }, 100);
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.broadcastReceiver == null) {
            this.broadcastReceiver = new UploadBroadcastReceiver();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver, new IntentFilter(ResumableUpload.REQUEST_AUTHORIZATION_INTENT));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_gallery_top, menu);
        return true;
    }

    private Intent getOpenFacebookIntent(Context context) {
        String str = "android.intent.action.VIEW";
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(str, Uri.parse("fb://page/123781808228682"));
        } catch (Exception unused) {
            return new Intent(str, Uri.parse("https://www.facebook.com/AppScreenRecorder"));
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (isFinishing()) {
            return false;
        }
        int itemId = menuItem.getItemId();
        if (itemId == C0793R.C0795id.action_apps) {
            String str = "android.intent.action.VIEW";
            Intent intent = new Intent(str, Uri.parse("market://developer?id=AppSmartz"));
            intent.addFlags(1208483840);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                startActivity(new Intent(str, Uri.parse("https://play.google.com/store/apps/developer?id=AppSmartz")));
            }
        } else if (itemId != C0793R.C0795id.action_game_list) {
            switch (itemId) {
                case C0793R.C0795id.action_settings /*2131296302*/:
                    startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), KEY_SETTINGS_ACTIVITY_REQUEST_CODE);
                    break;
                case C0793R.C0795id.action_share /*2131296303*/:
                    shareApp();
                    break;
                case C0793R.C0795id.action_social_feed /*2131296304*/:
                    startActivity(new Intent(getApplicationContext(), SocialFeedsActivity.class));
                    break;
            }
        } else {
            startActivity(new Intent(this, GamesListActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        if (!isFinishing()) {
            long actionCount = PreferenceHelper.getInstance().getActionCount();
            if (this.drawer.isDrawerOpen((int) GravityCompat.START)) {
                this.drawer.closeDrawers();
            } else if (this.sharedPreferences.getBoolean("isRatingDone", false)) {
                super.onBackPressed();
            } else if (this.isRateDialogAlreadyShowed || actionCount % 2 != 0 || PreferenceHelper.getInstance().getPrefRatingDialogShowed()) {
                finish();
            } else {
                this.isRateDialogAlreadyShowed = true;
                this.rateDialogFragment = new UserRateDialogFragment();
                this.rateDialogFragment.show(getSupportFragmentManager(), "rate");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!isFinishing()) {
            if (i == 3420 && PreferenceHelper.getInstance().getActionCount() % 2 != 0) {
                InterstitialAdLoader.getInstance().showInterstitialAd();
            }
            if (i2 == -1) {
                if (i == REQUEST_AUTHORIZATION) {
                    Toast.makeText(this, C0793R.string.id_click_to_start_upload_msg, 1).show();
                } else if (i == 342) {
                    this.mViewPager.setCurrentItem(0);
                    Fragment item = this.mSectionsPagerAdapter.getItem(this.mViewPager.getCurrentItem());
                    if (item != null && (item instanceof VideosFragment)) {
                        ((VideosFragment) item).getFiles();
                    }
                } else if (i == 734) {
                    this.mViewPager.setCurrentItem(0);
                    Fragment item2 = this.mSectionsPagerAdapter.getItem(this.mViewPager.getCurrentItem());
                    if (item2 != null && (item2 instanceof VideosFragment)) {
                        ((VideosFragment) item2).getFiles();
                    }
                } else if (i == 3411) {
                    this.mViewPager.setCurrentItem(1);
                    Fragment item3 = this.mSectionsPagerAdapter.getItem(this.mViewPager.getCurrentItem());
                    if (item3 != null && (item3 instanceof ImagesFragment)) {
                        ((ImagesFragment) item3).getFiles();
                    }
                    String stringExtra = intent.getStringExtra("imageEdit");
                    if (stringExtra != null) {
                        Intent intent2 = new Intent(this, ShowImagesActivity.class);
                        intent2.putExtra("ImgPath", stringExtra);
                        startActivityForResult(intent2, REQUEST_VIEW_IMAGES);
                    }
                } else if (i == KEY_SETTINGS_ACTIVITY_REQUEST_CODE) {
                    this.isLogoutOrChangeImage = true;
                    this.isLogoutOrChangeVideo = true;
                } else if (i == 3420) {
                    try {
                        Fragment item4 = this.mSectionsPagerAdapter.getItem(this.mViewPager.getCurrentItem());
                        if (item4 instanceof AudioFragment) {
                            ((AudioFragment) item4).onRefresh();
                        } else if (item4 instanceof RecordingsFragment) {
                            EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOCAL_RECORDING_REFRESH));
                        } else if (item4 instanceof ImagesFragment) {
                            ((ImagesFragment) item4).getFiles();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        onBackPressed();
    }

    public void navigateToVideo() {
        if (!this.isFromGetIntent) {
            this.mViewPager.setCurrentItem(0);
        }
    }

    public boolean isLogoutOrChangeImage() {
        return this.isLogoutOrChangeImage;
    }

    public void setLogoutOrChangeImage(boolean z) {
        this.isLogoutOrChangeImage = z;
    }

    public boolean isLogoutOrChangeVideo() {
        return this.isLogoutOrChangeVideo;
    }

    public void setLogoutOrChangeVideo(boolean z) {
        this.isLogoutOrChangeVideo = z;
    }
}
