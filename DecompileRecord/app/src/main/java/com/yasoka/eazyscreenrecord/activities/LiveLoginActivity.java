package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.widget.ContentLoadingProgressBar;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatImageButton;
import android.support.p003v7.widget.CardView;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.live_facebook.LiveFacebookStartActivity;
import com.ezscreenrecorder.activities.live_twitch.LiveTwitchLoginActivity;
import com.ezscreenrecorder.activities.live_youtube.LiveYoutubeStartActivity;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.LiveFacebookHelper;
import com.ezscreenrecorder.utils.LiveFacebookHelper.OnLoginListener;
import com.facebook.AccessToken;*/
import com.ezscreenrecorder.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.live_facebook.LiveFacebookStartActivity;
import com.yasoka.eazyscreenrecord.activities.live_twitch.LiveTwitchLoginActivity;
import com.yasoka.eazyscreenrecord.activities.live_youtube.LiveYoutubeStartActivity;
import com.yasoka.eazyscreenrecord.server.YoutubeAPI;
import com.yasoka.eazyscreenrecord.utils.LiveFacebookHelper;

import io.reactivex.observers.DisposableObserver;
/*
import p009io.reactivex.Observer;
import p009io.reactivex.observers.DisposableObserver;*/

public class LiveLoginActivity extends AppCompatActivity implements OnClickListener {
    private static final int RC_SIGN_IN = 10490;
    private AppCompatImageButton closeImageButton;
    private LinearLayout facebookButton;
    private GoogleSignInClient googleSignInClient;
    private CardView loginButtonContainer;
    private ContentLoadingProgressBar progressBar;
    private LinearLayout twitchButton;
    private LinearLayout twitterButton;
    private LinearLayout youtubeButton;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_live_login);
        this.facebookButton = (LinearLayout) findViewById(R.id.id_live_login_facebook);
        this.twitchButton = (LinearLayout) findViewById(R.id.id_live_login_twitch);
        this.youtubeButton = (LinearLayout) findViewById(R.id.id_live_login_youtube);
        this.loginButtonContainer = (CardView) findViewById(R.id.id_live_login_button_container);
        this.progressBar = (ContentLoadingProgressBar) findViewById(R.id.id_content_loading_progress_bar);
        this.closeImageButton = (AppCompatImageButton) findViewById(R.id.id_live_dialog_close_button);
        this.facebookButton.setOnClickListener(this);
        this.twitchButton.setOnClickListener(this);
        this.youtubeButton.setOnClickListener(this);
        this.closeImageButton.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        if (!isFinishing()) {
            int id = view.getId();
            if (id != R.id.id_live_dialog_close_button) {
                switch (id) {
                    case R.id.id_live_login_facebook /*2131296580*/:
                        if (RecorderApplication.getInstance().isNetworkAvailable()) {
                            performFacebookLogin();
                            break;
                        } else {
                            Toast.makeText(this, R.string.id_no_internet_error_list_message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    case R.id.id_live_login_twitch /*2131296581*/:
                        if (RecorderApplication.getInstance().isNetworkAvailable()) {
                            startActivity(new Intent(getApplicationContext(), LiveTwitchLoginActivity.class));
                            finish();
                            break;
                        } else {
                            Toast.makeText(this, R.string.id_no_internet_error_list_message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    case R.id.id_live_login_youtube /*2131296582*/:
                        if (RecorderApplication.getInstance().isNetworkAvailable()) {
                            if (YoutubeAPI.getInstance().getGoogleAccount(this) == null) {
                                try {
                                    YoutubeAPI.getInstance().signInGoogleAccount(this).subscribe(new DisposableObserver<Boolean>() {
                                        public void onComplete() {
                                        }

                                        public void onNext(Boolean bool) {
                                            if (bool.booleanValue()) {
                                                LiveLoginActivity liveLoginActivity = LiveLoginActivity.this;
                                                liveLoginActivity.startActivity(new Intent(liveLoginActivity.getApplicationContext(), LiveYoutubeStartActivity.class));
                                                ActivityCompat.finishAfterTransition(LiveLoginActivity.this);
                                                LiveLoginActivity.this.finish();
                                            }
                                        }

                                        public void onError(Throwable th) {
                                            th.printStackTrace();
                                        }
                                    });
                                    break;
                                } catch (NameNotFoundException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            } else {
                                startActivity(new Intent(getApplicationContext(), LiveYoutubeStartActivity.class));
                                ActivityCompat.finishAfterTransition(this);
                                finish();
                                break;
                            }
                        } else {
                            Toast.makeText(this, R.string.id_no_internet_error_list_message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                }
            } else {
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        LiveFacebookHelper.getInstance().setActivityResult(i, i2, intent);
        super.onActivityResult(i, i2, intent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) task.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void showProgressView(boolean z) {
        if (z) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.loginButtonContainer.setVisibility(View.GONE);
            return;
        }
        this.progressBar.setVisibility(View.GONE);
        this.loginButtonContainer.setVisibility(View.VISIBLE);
    }

    private void performFacebookLogin() {
        LiveFacebookHelper.getInstance().getAccessToken(this, new OnLoginListener() {
            public void onStart() {
                LiveLoginActivity.this.showProgressView(true);
            }

            public void onSuccess(AccessToken accessToken) {
                LiveLoginActivity liveLoginActivity = LiveLoginActivity.this;
                liveLoginActivity.startActivity(new Intent(liveLoginActivity.getApplicationContext(), LiveFacebookStartActivity.class));
                ActivityCompat.finishAfterTransition(LiveLoginActivity.this);
                LiveLoginActivity.this.showProgressView(false);
            }

            public void onFailed() {
                LiveLoginActivity.this.showProgressView(false);
            }
        });
    }
}
