package com.yasoka.eazyscreenrecord.activities.live_facebook;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.ActivityCompat;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.activities.LiveLoginActivity;
import com.ezscreenrecorder.alertdialogs.SelectResolutionDialogFragment;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.LiveFacebookHelper;
import com.ezscreenrecorder.utils.LiveFacebookHelper.OnLoginListener;
import com.ezscreenrecorder.utils.LiveFacebookHelper.OnLogoutListener;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import p008de.hdodenhof.circleimageview.CircleImageView;

public class LiveFacebookSettingsActivity extends BaseToolbarActivity implements OnClickListener, OnDismissListener {
    private LinearLayout logoutButton;
    /* access modifiers changed from: private */
    public ProgressBar logoutProgressBar;
    /* access modifiers changed from: private */
    public AppCompatTextView profileGender;
    /* access modifiers changed from: private */
    public CircleImageView profileImage;
    /* access modifiers changed from: private */
    public AppCompatTextView profileName;
    private LinearLayout resolutionButton;
    private TextView resolutionTxt;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_facebook_settings);
        init();
        setupToolbar();
        initProfile();
    }

    private void init() {
        this.logoutButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_facebook_logout);
        this.resolutionButton = (LinearLayout) findViewById(C0793R.C0795id.id_live_facebook_resolution_settings);
        this.logoutProgressBar = (ProgressBar) findViewById(C0793R.C0795id.id_live_facebook_logout_progressbar);
        this.logoutButton.setOnClickListener(this);
        this.resolutionButton.setOnClickListener(this);
    }

    private void initProfile() {
        this.profileGender = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_facebook_settings_profile_gender);
        this.profileName = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_facebook_settings_profile_name);
        this.profileImage = (CircleImageView) findViewById(C0793R.C0795id.id_live_facebook_settings_profile_image);
        this.resolutionTxt = (TextView) findViewById(C0793R.C0795id.txt_resolution);
        this.resolutionTxt.setText(getCurrentResolution(Constants.TYPE_PREF_RESOLUTION_FACEBOOK));
        LiveFacebookHelper.getInstance().getAccessToken(this, new OnLoginListener() {
            public void onStart() {
            }

            public void onSuccess(AccessToken accessToken) {
                StringBuilder sb = new StringBuilder();
                sb.append("https://graph.facebook.com/");
                sb.append(accessToken.getUserId());
                sb.append("/picture?type=normal");
                Glide.with(LiveFacebookSettingsActivity.this.getApplicationContext()).load(sb.toString()).into(LiveFacebookSettingsActivity.this.profileImage);
                GraphRequest newMeRequest = GraphRequest.newMeRequest(accessToken, new GraphJSONObjectCallback() {
                    public void onCompleted(JSONObject jSONObject, GraphResponse graphResponse) {
                        String str = "gender";
                        String str2 = "name";
                        if (jSONObject != null) {
                            try {
                                if (jSONObject.has(str2)) {
                                    LiveFacebookSettingsActivity.this.profileName.setText(jSONObject.getString(str2));
                                }
                                if (jSONObject.has(str)) {
                                    LiveFacebookSettingsActivity.this.profileGender.setText(jSONObject.getString(str));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new Builder(LiveFacebookSettingsActivity.this).setMessage((CharSequence) "Some error occurred please retry login.").setPositiveButton((int) C0793R.string.retry, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(LiveFacebookSettingsActivity.this, LiveLoginActivity.class);
                                    intent.addFlags(268468224);
                                    LiveFacebookSettingsActivity.this.startActivity(intent);
                                    ActivityCompat.finishAffinity(LiveFacebookSettingsActivity.this);
                                }
                            }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LiveFacebookSettingsActivity.this.finish();
                                }
                            }).show();
                        }
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString(GraphRequest.FIELDS_PARAM, "id,name,gender,location");
                newMeRequest.setParameters(bundle);
                newMeRequest.setParameters(bundle);
                newMeRequest.executeAsync();
            }

            public void onFailed() {
                new Builder(LiveFacebookSettingsActivity.this).setMessage((CharSequence) "Some error occurred please retry login.").setPositiveButton((int) C0793R.string.retry, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LiveFacebookSettingsActivity.this, LiveLoginActivity.class);
                        intent.addFlags(268468224);
                        LiveFacebookSettingsActivity.this.startActivity(intent);
                        ActivityCompat.finishAffinity(LiveFacebookSettingsActivity.this);
                    }
                }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LiveFacebookSettingsActivity.this.finish();
                    }
                }).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != C0793R.C0795id.id_live_facebook_logout) {
            if (id == C0793R.C0795id.id_live_facebook_resolution_settings) {
                SelectResolutionDialogFragment.getInstance(Constants.TYPE_PREF_RESOLUTION_FACEBOOK).show(getSupportFragmentManager(), "RESOLUTION_DIALOG");
            }
        } else if (!RecorderApplication.getInstance().isNetworkAvailable()) {
            Toast.makeText(this, C0793R.string.id_no_internet_error_list_message, 0).show();
        } else {
            LiveFacebookHelper.getInstance().logoutFacebook(new OnLogoutListener() {
                public void onStart() {
                    LiveFacebookSettingsActivity.this.logoutProgressBar.setVisibility(0);
                }

                public void onSuccess() {
                    LiveFacebookSettingsActivity.this.logoutProgressBar.setVisibility(4);
                    ActivityCompat.finishAffinity(LiveFacebookSettingsActivity.this);
                }
            });
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (!isFinishing()) {
            this.resolutionTxt.setText(PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_FACEBOOK));
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
