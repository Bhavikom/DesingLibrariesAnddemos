package com.yasoka.eazyscreenrecord.activities.live_youtube;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.ActivityCompat;
import android.support.p003v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.alertdialogs.SelectResolutionDialogFragment;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.util.ArrayList;
import p008de.hdodenhof.circleimageview.CircleImageView;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class LiveYoutubeSettingsActivity extends BaseToolbarActivity implements OnClickListener, OnDismissListener {
    private LinearLayout logoutButton;
    private ProgressBar logoutProgressBar;
    private AppCompatTextView profileEmail;
    private CircleImageView profileImage;
    private AppCompatTextView profileName;
    private LinearLayout resolutionButton;
    private TextView resolutionTxt;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_youtube_settings);
        init();
        setupToolbar();
        initProfile();
    }

    private void init() {
        this.logoutButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_youtube_logout);
        this.resolutionButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_youtube_resolution_settings);
        this.logoutProgressBar = (ProgressBar) findViewById(C0793R.C0795id.id_live_youtube_logout_progressbar);
        this.logoutButton.setOnClickListener(this);
        this.resolutionButton.setOnClickListener(this);
    }

    private void initProfile() {
        String str;
        this.profileEmail = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_youtube_settings_profile_gender);
        this.profileName = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_youtube_settings_profile_name);
        this.profileImage = (CircleImageView) findViewById(C0793R.C0795id.id_live_youtube_settings_profile_image);
        this.resolutionTxt = (TextView) findViewById(C0793R.C0795id.txt_resolution);
        this.resolutionTxt.setText(getCurrentResolution(Constants.TYPE_PREF_RESOLUTION_YOUTUBE));
        GoogleSignInAccount googleAccount = YoutubeAPI.getInstance().getGoogleAccount(this);
        if (googleAccount != null) {
            this.profileName.setText(googleAccount.getDisplayName());
            this.profileEmail.setText(googleAccount.getEmail());
            try {
                str = googleAccount.getPhotoUrl().toString();
            } catch (Exception e) {
                e.printStackTrace();
                str = null;
            }
            if (!TextUtils.isEmpty(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("=s150-rw");
                Glide.with(getApplicationContext()).load(sb.toString()).placeholder((int) C0793R.C0794drawable.ic_live_youtube).error((int) C0793R.C0794drawable.ic_live_youtube).dontAnimate().into(this.profileImage);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C0793R.C0795id.id_live_youtube_logout) {
            showLogoutProgress(true);
            YoutubeAPI.getInstance().logOutFromAccount(this).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                public void onSuccess(Boolean bool) {
                    LiveYoutubeSettingsActivity.this.showLogoutProgress(false);
                    if (bool.booleanValue()) {
                        YoutubeAPI.getInstance().setYoutubeFinalModel(null);
                        ActivityCompat.finishAffinity(LiveYoutubeSettingsActivity.this);
                    }
                }

                public void onError(Throwable th) {
                    LiveYoutubeSettingsActivity.this.showLogoutProgress(false);
                    th.printStackTrace();
                }
            });
        } else if (id == C0793R.C0795id.id_live_youtube_resolution_settings) {
            SelectResolutionDialogFragment.getInstance(Constants.TYPE_PREF_RESOLUTION_YOUTUBE).show(getSupportFragmentManager(), "RESOLUTION_DIALOG");
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (!isFinishing()) {
            this.resolutionTxt.setText(PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_YOUTUBE));
        }
    }

    private String getCurrentResolution(int i) {
        boolean z;
        String[] stringArray = getResources().getStringArray(C0793R.array.pref_resolution_list_titles);
        String str = stringArray[0];
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        int i2 = point.y;
        int i3 = point.x;
        ArrayList arrayList = new ArrayList();
        int length = stringArray.length;
        int i4 = 0;
        while (true) {
            z = true;
            if (i4 >= length) {
                break;
            }
            String str2 = stringArray[i4];
            String str3 = "x";
            String str4 = str2.split(str3)[0];
            String str5 = str2.split(str3)[1];
            if (i2 > i3) {
                if (Integer.parseInt(str4) <= i2 && Integer.parseInt(str5) <= i3) {
                    arrayList.add(str2);
                }
            } else if (Integer.parseInt(str5) <= i2 && Integer.parseInt(str4) <= i3) {
                arrayList.add(str2);
            }
            i4++;
        }
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        if (PreferenceHelper.getInstance().hasPrefResolution(i)) {
            String prefResolution = PreferenceHelper.getInstance().getPrefResolution(i);
            int length2 = strArr.length;
            int i5 = 0;
            while (true) {
                if (i5 >= length2) {
                    break;
                }
                String str6 = strArr[i5];
                if (str6.startsWith(prefResolution)) {
                    str = str6;
                    break;
                }
                i5++;
            }
            if (z && strArr.length > 0) {
                PreferenceHelper.getInstance().setPrefResolution(i, strArr[0]);
                return strArr[0];
            }
        }
        z = false;
        return z ? str : str;
    }

    /* access modifiers changed from: private */
    public void showLogoutProgress(boolean z) {
        if (z) {
            this.logoutProgressBar.setVisibility(0);
        } else {
            this.logoutProgressBar.setVisibility(4);
        }
    }
}
