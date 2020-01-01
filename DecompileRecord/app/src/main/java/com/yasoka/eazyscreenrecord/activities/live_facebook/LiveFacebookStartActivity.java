package com.yasoka.eazyscreenrecord.activities.live_facebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatEditText;
import android.support.p003v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.activities.LiveLoginActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.alertdialogs.FullScreenLoadingDialog;
import com.ezscreenrecorder.server.model.LiveFacebookModels.LiveFacebookPageModel;
import com.ezscreenrecorder.utils.LiveFacebookHelper;
import com.ezscreenrecorder.utils.LiveFacebookHelper.OnStreamListener;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import java.util.List;
import p008de.hdodenhof.circleimageview.CircleImageView;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class LiveFacebookStartActivity extends BaseToolbarActivity implements OnClickListener, OnItemSelectedListener {
    public static final String EXTRA_PAGE_SELECT_MODEL = "selected_page_mode";
    private static final int PAGE_SELECT_RESPONSE_CODE = 5611;
    private AccessToken accessToken;
    private FullScreenLoadingDialog fullScreenDialog;
    private ProgressBar loadingProgress;
    /* access modifiers changed from: private */
    public List<LiveFacebookPageModel> pageList;
    private LinearLayout pageListContainer;
    private LiveFacebookPageModel pageSelected;
    /* access modifiers changed from: private */
    public AppCompatSpinner pageSpinner;
    private CircleImageView profileImage;
    private AppCompatButton startLiveButton;
    private AppCompatSpinner streamOnSpinner;
    private AppCompatEditText streamTitleET;

    private class SpinnerAdapter extends ArrayAdapter<LiveFacebookPageModel> {
        private LayoutInflater inflater;

        private class ViewHolder {
            /* access modifiers changed from: private */
            public TextView pageName;

            private ViewHolder() {
            }
        }

        public SpinnerAdapter(@NonNull Context context, int i, @NonNull List<LiveFacebookPageModel> list) {
            super(context, i, list);
        }

        @NonNull
        public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
            return rowView(i, view, viewGroup);
        }

        public View getDropDownView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
            return rowView(i, view, viewGroup);
        }

        private View rowView(int i, View view, ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            LiveFacebookPageModel liveFacebookPageModel = (LiveFacebookPageModel) getItem(i);
            if (view == null) {
                viewHolder = new ViewHolder();
                if (this.inflater == null) {
                    this.inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
                }
                view2 = this.inflater.inflate(17367048, viewGroup, false);
                viewHolder.pageName = (TextView) view2.findViewById(16908308);
                viewHolder.pageName.setPadding(15, 30, 15, 30);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (liveFacebookPageModel != null) {
                viewHolder.pageName.setText(liveFacebookPageModel.getPageName());
            }
            return view2;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_facebook_start);
        init();
        setupToolbar();
        loadProfileImage();
        fetchPages();
    }

    private void init() {
        this.loadingProgress = (ProgressBar) findViewById(C0793R.C0795id.id_live_facebook_progressbar);
        this.streamTitleET = (AppCompatEditText) findViewById(C0793R.C0795id.id_live_facebook_stream_title_edit_text);
        this.startLiveButton = (AppCompatButton) findViewById(C0793R.C0795id.id_live_facebook_start_button);
        this.streamOnSpinner = (AppCompatSpinner) findViewById(C0793R.C0795id.id_live_facebook_stream_on_spinner);
        this.pageSpinner = (AppCompatSpinner) findViewById(C0793R.C0795id.id_live_facebook_page_type_spinner);
        this.pageListContainer = (LinearLayout) findViewById(C0793R.C0795id.id_live_facebook_page_type_container);
        this.startLiveButton.setOnClickListener(this);
        this.streamOnSpinner.setOnItemSelectedListener(this);
        this.pageSpinner.setOnItemSelectedListener(this);
    }

    private boolean isLogined() {
        if (this.accessToken == null) {
            this.accessToken = AccessToken.getCurrentAccessToken();
        }
        AccessToken accessToken2 = this.accessToken;
        return accessToken2 != null && !accessToken2.isExpired();
    }

    private void loadProfileImage() {
        this.profileImage = (CircleImageView) findViewById(C0793R.C0795id.id_live_facebook_profile_image);
        if (isLogined()) {
            StringBuilder sb = new StringBuilder();
            sb.append("https://graph.facebook.com/");
            sb.append(this.accessToken.getUserId());
            sb.append("/picture?type=normal");
            Glide.with(getApplicationContext()).load(sb.toString()).placeholder((int) C0793R.C0794drawable.ic_live_facebook).error((int) C0793R.C0794drawable.ic_live_facebook).dontAnimate().into(this.profileImage);
        }
    }

    private void fetchPages() {
        if (isLogined() && this.accessToken.getPermissions().contains("publish_pages") && this.accessToken.getPermissions().contains("manage_pages")) {
            showFullScreenProgress(true);
            LiveFacebookHelper.getInstance().getFacebookPagesList().subscribe((SingleObserver<? super T>) new DisposableSingleObserver<List<LiveFacebookPageModel>>() {
                public void onSuccess(List<LiveFacebookPageModel> list) {
                    LiveFacebookStartActivity.this.showFullScreenProgress(false);
                    LiveFacebookStartActivity.this.pageList = list;
                    AppCompatSpinner access$200 = LiveFacebookStartActivity.this.pageSpinner;
                    LiveFacebookStartActivity liveFacebookStartActivity = LiveFacebookStartActivity.this;
                    access$200.setAdapter((android.widget.SpinnerAdapter) new SpinnerAdapter(liveFacebookStartActivity.getApplicationContext(), 17367048, LiveFacebookStartActivity.this.pageList));
                }

                public void onError(Throwable th) {
                    LiveFacebookStartActivity.this.showFullScreenProgress(false);
                    LiveFacebookStartActivity.this.pageList = null;
                }
            });
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
            if (!RecorderApplication.getInstance().isNetworkAvailable()) {
                Toast.makeText(this, C0793R.string.id_no_internet_error_list_message, 0).show();
            } else {
                startActivity(new Intent(getApplicationContext(), LiveFacebookSettingsActivity.class));
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        if (view.getId() == C0793R.C0795id.id_live_facebook_start_button) {
            if (!RecorderApplication.getInstance().isNetworkAvailable()) {
                Toast.makeText(this, C0793R.string.id_no_internet_error_list_message, 0).show();
            } else if (this.pageListContainer.getVisibility() == 0) {
                getStreamURLFromFacebookPage();
            } else {
                getStreamURLFromFacebookTimeline();
            }
        }
    }

    private void getStreamURLFromFacebookPage() {
        if (TextUtils.isEmpty(this.streamTitleET.getText().toString().trim())) {
            this.streamTitleET.setError(getString(C0793R.string.id_enter_title_txt));
            Toast.makeText(getApplicationContext(), C0793R.string.id_live_error_steam_title_msg, 1).show();
            return;
        }
        showProgress(true);
        LiveFacebookHelper.getInstance().getFacebookStreamURLForPage(this.pageSelected, this.streamTitleET.getText().toString().trim()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<String>() {
            public void onSuccess(String str) {
                LiveFacebookStartActivity.this.showProgress(false);
                LiveFacebookStartActivity.this.startActivity(new Intent(LiveFacebookStartActivity.this.getApplicationContext(), RecordingActivity.class).putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE).putExtra(FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA, str));
                LiveFacebookStartActivity.this.finish();
            }

            public void onError(Throwable th) {
                LiveFacebookStartActivity.this.showProgress(false);
                th.printStackTrace();
            }
        });
    }

    private void getStreamURLFromFacebookTimeline() {
        if (TextUtils.isEmpty(this.streamTitleET.getText().toString().trim())) {
            this.streamTitleET.setError(getString(C0793R.string.id_enter_title_txt));
            Toast.makeText(getApplicationContext(), C0793R.string.id_live_error_steam_title_msg, 1).show();
            return;
        }
        LiveFacebookHelper.getInstance().getFacebookStreamURLForTimeline(new OnStreamListener() {
            public void onStart() {
                LiveFacebookStartActivity.this.showProgress(true);
            }

            public void onSuccess(String str) {
                LiveFacebookStartActivity.this.showProgress(false);
                LiveFacebookStartActivity.this.startActivity(new Intent(LiveFacebookStartActivity.this.getApplicationContext(), RecordingActivity.class).putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE).putExtra(FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA, str));
                LiveFacebookStartActivity.this.finish();
            }

            public void onFailed() {
                LiveFacebookStartActivity.this.showProgress(false);
                new Builder(LiveFacebookStartActivity.this).setMessage((CharSequence) "Some error occurred please retry login.").setPositiveButton((int) C0793R.string.retry, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LiveFacebookStartActivity.this, LiveLoginActivity.class);
                        intent.addFlags(268468224);
                        LiveFacebookStartActivity.this.startActivity(intent);
                        LiveFacebookStartActivity.this.finish();
                    }
                }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LiveFacebookStartActivity.this.finish();
                    }
                }).show();
            }
        }, this.streamTitleET.getText().toString().trim());
    }

    /* access modifiers changed from: private */
    public void showProgress(boolean z) {
        if (z) {
            this.loadingProgress.setVisibility(0);
            this.startLiveButton.setEnabled(false);
            return;
        }
        this.loadingProgress.setVisibility(4);
        this.startLiveButton.setEnabled(true);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        int id = adapterView.getId();
        if (id == C0793R.C0795id.id_live_facebook_page_type_spinner) {
            this.pageSelected = (LiveFacebookPageModel) this.pageList.get(i);
        } else if (id == C0793R.C0795id.id_live_facebook_stream_on_spinner) {
            if (i == 0) {
                this.pageListContainer.setVisibility(4);
                this.pageSelected = null;
            } else if (i == 1) {
                List<LiveFacebookPageModel> list = this.pageList;
                if (list == null || list.size() == 0) {
                    this.streamOnSpinner.setSelection(0);
                    fetchPages();
                    return;
                }
                this.pageListContainer.setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != PAGE_SELECT_RESPONSE_CODE) {
            return;
        }
        if (i2 != -1) {
            this.streamOnSpinner.setSelection(0);
        } else if (intent != null) {
            String str = EXTRA_PAGE_SELECT_MODEL;
            if (intent.hasExtra(str)) {
                String stringExtra = intent.getStringExtra(str);
                if (!TextUtils.isEmpty(stringExtra)) {
                    this.pageSelected = (LiveFacebookPageModel) new Gson().fromJson(stringExtra, LiveFacebookPageModel.class);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void showFullScreenProgress(boolean z) {
        if (z) {
            this.fullScreenDialog = new FullScreenLoadingDialog();
            this.fullScreenDialog.show(getSupportFragmentManager(), "fullscreen_progress");
            return;
        }
        this.fullScreenDialog.dismissDialog();
    }
}
