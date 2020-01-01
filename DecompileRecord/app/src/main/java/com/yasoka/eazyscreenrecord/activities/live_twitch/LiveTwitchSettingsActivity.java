package com.yasoka.eazyscreenrecord.activities.live_twitch;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.alertdialogs.SelectResolutionDialogFragment;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import java.util.ArrayList;
import p008de.hdodenhof.circleimageview.CircleImageView;

public class LiveTwitchSettingsActivity extends BaseToolbarActivity implements OnClickListener, OnDismissListener {
    private LiveTwitchUserOutputModel liveTwitchUserData;
    private LinearLayout logoutButton;
    private AppCompatTextView profileGender;
    private CircleImageView profileImage;
    private AppCompatTextView profileName;
    private LinearLayout resolutionButton;
    private TextView resolutionTxt;
    private LinearLayout shareStream;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_twitch_settings);
        init();
        setupToolbar();
        initProfile();
    }

    private void init() {
        this.logoutButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_twitch_logout);
        this.shareStream = (LinearLayout) findViewById(C0793R.C0795id.id_live_twitch_share_stream);
        this.resolutionButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_twitch_resolution_settings);
        this.logoutButton.setOnClickListener(this);
        this.shareStream.setOnClickListener(this);
        this.resolutionButton.setOnClickListener(this);
    }

    private void initProfile() {
        this.profileGender = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_twitch_settings_profile_gender);
        this.profileName = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_twitch_settings_profile_name);
        this.profileImage = (CircleImageView) findViewById(C0793R.C0795id.id_live_twitch_settings_profile_image);
        this.resolutionTxt = (TextView) findViewById(C0793R.C0795id.txt_resolution);
        this.resolutionTxt.setText(getCurrentResolution(Constants.TYPE_PREF_RESOLUTION_TWITCH));
        this.liveTwitchUserData = PreferenceHelper.getInstance().getLiveTwitchUserData();
        if (this.liveTwitchUserData != null) {
            Glide.with((FragmentActivity) this).load(this.liveTwitchUserData.getLogo()).placeholder((int) C0793R.C0794drawable.ic_live_twitch).error((int) C0793R.C0794drawable.ic_live_twitch).dontAnimate().into(this.profileImage);
            this.profileName.setText(this.liveTwitchUserData.getDisplayName());
            this.profileGender.setText(this.liveTwitchUserData.getBio());
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
        if (id == C0793R.C0795id.id_live_twitch_logout) {
            PreferenceHelper.getInstance().setTwitchAccessToken("");
            ActivityCompat.finishAffinity(this);
        } else if (id == C0793R.C0795id.id_live_twitch_resolution_settings) {
            SelectResolutionDialogFragment.getInstance(Constants.TYPE_PREF_RESOLUTION_TWITCH).show(getSupportFragmentManager(), "RESOLUTION_DIALOG");
        } else if (id == C0793R.C0795id.id_live_twitch_share_stream && this.liveTwitchUserData != null) {
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
            String string = getString(C0793R.string.id_live_stream_share_text);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("https://www.twitch.tv/");
            sb3.append(this.liveTwitchUserData.getName());
            intent.putExtra("android.intent.extra.TEXT", String.format(string, new Object[]{sb3.toString()}));
            startActivity(Intent.createChooser(intent, getString(C0793R.string.share)));
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (!isFinishing()) {
            this.resolutionTxt.setText(PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_TWITCH));
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
}
