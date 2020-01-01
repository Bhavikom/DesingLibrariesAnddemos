package com.yasoka.eazyscreenrecord.activities.live_twitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatEditText;
import android.support.p003v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.server.model.LiveTwitchModels.Game;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;
import com.ezscreenrecorder.utils.LiveTwitchHelper;
import com.ezscreenrecorder.utils.LiveTwitchHelper.OnTwitchLiveStartListener;
import com.ezscreenrecorder.utils.LiveTwitchHelper.OnTwitchUserDataListener;
import com.ezscreenrecorder.utils.PreferenceHelper;
import p008de.hdodenhof.circleimageview.CircleImageView;

public class LiveTwitchStartActivity extends BaseToolbarActivity implements OnClickListener {
    public static final String EXTRA_LIVE_TWITCH_GAME_SELECTED_NAME = "extra_live_twitch_game_name";
    public static final int KEY_LIVE_TWITCH_GAME_SELECTED = 7801;
    private AppCompatTextView gameType;
    private ProgressBar loadingProgress;
    /* access modifiers changed from: private */
    public CircleImageView profileImage;
    private Game selectedGame;
    private AppCompatButton startLiveButton;
    private AppCompatEditText streamTitleET;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_twitch_start_screen);
        setupToolbar();
        init();
    }

    private void init() {
        this.profileImage = (CircleImageView) findViewById(C0793R.C0795id.id_live_twitch_profile_image);
        this.loadingProgress = (ProgressBar) findViewById(C0793R.C0795id.id_live_twitch_progressbar);
        this.streamTitleET = (AppCompatEditText) findViewById(C0793R.C0795id.id_live_twitch_stream_title_edit_text);
        this.startLiveButton = (AppCompatButton) findViewById(C0793R.C0795id.id_live_twitch_start_button);
        this.gameType = (AppCompatTextView) findViewById(C0793R.C0795id.id_live_twitch_stream_game_type);
        this.startLiveButton.setOnClickListener(this);
        this.gameType.setOnClickListener(this);
        getLiveTwitchUserData();
    }

    private void getLiveTwitchUserData() {
        final LiveTwitchUserOutputModel liveTwitchUserData = PreferenceHelper.getInstance().getLiveTwitchUserData();
        if (liveTwitchUserData != null) {
            Glide.with((FragmentActivity) this).load(liveTwitchUserData.getLogo()).placeholder((int) C0793R.C0794drawable.ic_live_twitch).error((int) C0793R.C0794drawable.ic_live_twitch).dontAnimate().into(this.profileImage);
        }
        LiveTwitchHelper.getInstance().getUserData(new OnTwitchUserDataListener() {
            public void onFailure() {
            }

            public void onStart() {
            }

            public void onComplete(LiveTwitchUserOutputModel liveTwitchUserOutputModel) {
                PreferenceHelper.getInstance().setLiveTwitchUserData(liveTwitchUserOutputModel);
                LiveTwitchUserOutputModel liveTwitchUserOutputModel2 = liveTwitchUserData;
                if (liveTwitchUserOutputModel2 == null) {
                    Glide.with((FragmentActivity) LiveTwitchStartActivity.this).load(liveTwitchUserOutputModel.getLogo()).placeholder((int) C0793R.C0794drawable.ic_live_twitch).error((int) C0793R.C0794drawable.ic_live_twitch).dontAnimate().into(LiveTwitchStartActivity.this.profileImage);
                } else if (!liveTwitchUserOutputModel2.getLogo().equals(liveTwitchUserOutputModel.getLogo())) {
                    Glide.with((FragmentActivity) LiveTwitchStartActivity.this).load(liveTwitchUserOutputModel.getLogo()).placeholder((int) C0793R.C0794drawable.ic_live_twitch).error((int) C0793R.C0794drawable.ic_live_twitch).dontAnimate().into(LiveTwitchStartActivity.this.profileImage);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 7801 && intent != null) {
            this.selectedGame = LiveTwitchHelper.getInstance().getGameSelectedForLive();
            Game game = this.selectedGame;
            if (game != null) {
                this.gameType.setText(game.getName());
                this.gameType.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0793R.C0794drawable.ic_close_circle_grey600_24dp, 0);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_live_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (isFinishing()) {
            return super.onOptionsItemSelected(menuItem);
        }
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else if (itemId == C0793R.C0795id.action_settings) {
            startActivity(new Intent(getApplicationContext(), LiveTwitchSettingsActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0793R.C0795id.id_live_twitch_start_button /*2131296602*/:
                if (RecorderApplication.getInstance().isNetworkAvailable()) {
                    if (!TextUtils.isEmpty(this.streamTitleET.getText().toString().trim())) {
                        if (this.selectedGame != null) {
                            LiveTwitchHelper.getInstance().startLiveOnTwitch(this.streamTitleET.getText().toString().trim(), this.selectedGame.getName(), new OnTwitchLiveStartListener() {
                                public void onStart() {
                                    LiveTwitchStartActivity.this.showProgress(true);
                                }

                                public void onFailure() {
                                    LiveTwitchStartActivity.this.showProgress(false);
                                }

                                public void onComplete(LiveTwitchFinalModel liveTwitchFinalModel) {
                                    LiveTwitchStartActivity.this.showProgress(false);
                                    LiveTwitchStartActivity.this.startActivity(new Intent(LiveTwitchStartActivity.this.getApplicationContext(), RecordingActivity.class).putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD).putExtra(FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA, liveTwitchFinalModel));
                                    LiveTwitchStartActivity.this.finish();
                                }
                            });
                            break;
                        } else {
                            Toast.makeText(getApplicationContext(), "Choose category where you would like to stream.", 1).show();
                            return;
                        }
                    } else {
                        this.streamTitleET.setError(getString(C0793R.string.id_enter_title_txt));
                        Toast.makeText(getApplicationContext(), C0793R.string.id_live_error_steam_title_msg, 1).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, C0793R.string.id_no_internet_error_list_message, 0).show();
                    return;
                }
            case C0793R.C0795id.id_live_twitch_stream_game_type /*2131296603*/:
                if (RecorderApplication.getInstance().isNetworkAvailable()) {
                    if (this.selectedGame != null) {
                        this.selectedGame = null;
                        LiveTwitchHelper.getInstance().setGameSelectedForLive(null);
                        this.gameType.setText(C0793R.string.id_tap_to_select_txt);
                        this.gameType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, C0793R.C0794drawable.ic_menu_down_grey600_36dp, 0);
                        break;
                    } else {
                        startActivityForResult(new Intent(getApplicationContext(), LiveTwitchGameListActivity.class), KEY_LIVE_TWITCH_GAME_SELECTED);
                        break;
                    }
                } else {
                    Toast.makeText(this, C0793R.string.id_no_internet_error_list_message, 0).show();
                    return;
                }
        }
    }

    /* access modifiers changed from: private */
    public void showProgress(boolean z) {
        if (z) {
            this.startLiveButton.setEnabled(false);
            this.loadingProgress.setVisibility(0);
            return;
        }
        this.startLiveButton.setEnabled(true);
        this.loadingProgress.setVisibility(4);
    }
}
