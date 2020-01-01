package com.yasoka.eazyscreenrecord.activities.live_youtube;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatEditText;
import android.support.p003v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.LiveBroadcasts.Bind;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamSnippet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Calendar;
import p008de.hdodenhof.circleimageview.CircleImageView;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class LiveYoutubeStartActivity extends BaseToolbarActivity implements OnClickListener {
    public static final int RESPONSE_CODE_MANAGE_YOUTUBE_ACCOUNT_PERMISSION = 2022;
    public static final String[] SCOPES = {YouTubeScopes.YOUTUBE};
    private static YouTube youtube;
    private AccessToken accessToken;
    private ProgressBar loadingProgress;
    private AppCompatSpinner privacySpinner;
    private CircleImageView profileImage;
    private GoogleSignInAccount signInAccount;
    private AppCompatButton startLiveButton;
    private AppCompatEditText streamTitleET;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_live_youtube_start);
        init();
        setupToolbar();
        loadProfileImage();
    }

    private void init() {
        this.loadingProgress = (ProgressBar) findViewById(R.id.id_live_youtube_progressbar);
        this.streamTitleET = (AppCompatEditText) findViewById(R.id.id_live_youtube_stream_title_edit_text);
        this.startLiveButton = (AppCompatButton) findViewById(R.id.id_live_youtube_start_button);
        this.privacySpinner = (AppCompatSpinner) findViewById(R.id.id_live_youtube_stream_privacy_spinner);
        this.startLiveButton.setOnClickListener(this);
    }

    private void loadProfileImage() {
        String str;
        this.profileImage = (CircleImageView) findViewById(R.id.id_live_youtube_profile_image);
        this.signInAccount = YoutubeAPI.getInstance().getGoogleAccount(this);
        GoogleSignInAccount googleSignInAccount = this.signInAccount;
        if (googleSignInAccount != null) {
            try {
                str = googleSignInAccount.getPhotoUrl().toString();
            } catch (Exception e) {
                e.printStackTrace();
                str = null;
            }
            if (!TextUtils.isEmpty(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("=s150-rw");
                Glide.with(getApplicationContext()).load(sb.toString()).placeholder((int) R.C0794drawable.ic_live_youtube).error((int) R.C0794drawable.ic_live_youtube).dontAnimate().into(this.profileImage);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.C0797menu.menu_live_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (isFinishing()) {
            return super.onOptionsItemSelected(menuItem);
        }
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), LiveYoutubeSettingsActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!isFinishing() && i == 2022) {
            if (i2 == -1) {
                this.startLiveButton.performClick();
            } else {
                Toast.makeText(this, getString(R.string.id_manage_account_permission_required_message), 0).show();
            }
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.id_live_youtube_start_button) {
            if (!TextUtils.isEmpty(this.streamTitleET.getText().toString().trim())) {
                String str = getResources().getStringArray(R.array.id_youtube_live_privacy_types)[this.privacySpinner.getSelectedItemPosition()];
                showProgress(true);
                YoutubeAPI.getInstance().createYoutubeLiveEvent(this, this.streamTitleET.getText().toString(), str).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<LiveYoutubeFinalModel>() {
                    public void onSuccess(LiveYoutubeFinalModel liveYoutubeFinalModel) {
                        YoutubeAPI.getInstance().setYoutubeFinalModel(liveYoutubeFinalModel);
                        LiveYoutubeStartActivity.this.showProgress(false);
                        LiveYoutubeStartActivity.this.startActivity(new Intent(LiveYoutubeStartActivity.this.getApplicationContext(), RecordingActivity.class).putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD).putExtra(FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA, liveYoutubeFinalModel));
                        LiveYoutubeStartActivity.this.finish();
                    }

                    public void onError(Throwable th) {
                        LiveYoutubeStartActivity.this.showProgress(false);
                        YoutubeAPI.getInstance().setYoutubeFinalModel(null);
                        boolean z = true;
                        if (!(!TextUtils.isEmpty(th.getClass().getCanonicalName()) && th.getClass().getCanonicalName().contains("auth.UserRecoverableAuthIOException"))) {
                            if (LiveYoutubeStartActivity.this.isFinishing() || TextUtils.isEmpty(th.getMessage())) {
                                z = false;
                            } else if (th.getMessage().contains("youtube.liveBroadcast") && th.getMessage().contains("liveStreamingNotEnabled")) {
                                AlertDialog create = new Builder(LiveYoutubeStartActivity.this).setMessage((CharSequence) "Live Streaming not enable for this Youtube account. Please, go to Youtube Channel and enable Live Streaming.").setPositiveButton((int) R.string.f83ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!LiveYoutubeStartActivity.this.isFinishing()) {
                                            LiveYoutubeStartActivity.this.startActivity(new Intent(LiveYoutubeStartActivity.this.getApplicationContext(), LiveYoutubeEnableStreamingActivity.class));
                                        }
                                    }
                                }).create();
                                create.setCanceledOnTouchOutside(false);
                                create.show();
                            }
                            if (!z) {
                                Toast.makeText(LiveYoutubeStartActivity.this, "Some error occurred. Please retry", 0).show();
                            }
                        }
                    }
                });
                return;
            }
            this.streamTitleET.setError(getString(R.string.id_enter_title_txt));
            Toast.makeText(getApplicationContext(), R.string.id_live_error_steam_title_msg, 1).show();
        }
    }

    private LiveYoutubeFinalModel startStream(String str) throws Exception {
        LiveYoutubeFinalModel liveYoutubeFinalModel = new LiveYoutubeFinalModel();
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES));
        GoogleSignInAccount googleSignInAccount = this.signInAccount;
        if (googleSignInAccount != null) {
            usingOAuth2.setSelectedAccountName(googleSignInAccount.getEmail());
        }
        usingOAuth2.setBackOff(new ExponentialBackOff());
        youtube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), usingOAuth2).setApplicationName("Screen Recorder").build();
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        String str2 = "You chose ";
        sb.append(str2);
        sb.append(str);
        sb.append(" for broadcast title.");
        printStream.println(sb.toString());
        LiveBroadcastSnippet liveBroadcastSnippet = new LiveBroadcastSnippet();
        liveBroadcastSnippet.setTitle(str);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long j = 86400000 + timeInMillis;
        liveBroadcastSnippet.setScheduledStartTime(new DateTime(timeInMillis));
        liveBroadcastSnippet.setScheduledEndTime(new DateTime(j));
        String str3 = getResources().getStringArray(R.array.id_youtube_live_privacy_types)[this.privacySpinner.getSelectedItemPosition()];
        LiveBroadcastStatus liveBroadcastStatus = new LiveBroadcastStatus();
        liveBroadcastStatus.setPrivacyStatus(str3.toLowerCase());
        LiveBroadcast liveBroadcast = new LiveBroadcast();
        liveBroadcast.setKind("youtube#liveBroadcast");
        liveBroadcast.setSnippet(liveBroadcastSnippet);
        liveBroadcast.setStatus(liveBroadcastStatus);
        LiveBroadcast liveBroadcast2 = (LiveBroadcast) youtube.liveBroadcasts().insert("snippet,status", liveBroadcast).execute();
        System.out.println("\n================== Returned Broadcast ==================\n");
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        String str4 = "  - Id: ";
        sb2.append(str4);
        sb2.append(liveBroadcast2.getId());
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        String str5 = "  - Title: ";
        sb3.append(str5);
        sb3.append(liveBroadcast2.getSnippet().getTitle());
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        String str6 = "  - Description: ";
        sb4.append(str6);
        sb4.append(liveBroadcast2.getSnippet().getDescription());
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        String str7 = "  - Published At: ";
        sb5.append(str7);
        sb5.append(liveBroadcast2.getSnippet().getPublishedAt());
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("  - Scheduled Start Time: ");
        sb6.append(liveBroadcast2.getSnippet().getScheduledStartTime());
        printStream6.println(sb6.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("  - Scheduled End Time: ");
        sb7.append(liveBroadcast2.getSnippet().getScheduledEndTime());
        printStream7.println(sb7.toString());
        liveYoutubeFinalModel.setBroadcastID(liveBroadcast2.getId());
        liveYoutubeFinalModel.setBroadcastTitle(liveBroadcast2.getSnippet().getTitle());
        String str8 = "Stream Title";
        PrintStream printStream8 = System.out;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str2);
        sb8.append(str8);
        sb8.append(" for stream title.");
        printStream8.println(sb8.toString());
        LiveStreamSnippet liveStreamSnippet = new LiveStreamSnippet();
        liveStreamSnippet.setTitle(str8);
        CdnSettings cdnSettings = new CdnSettings();
        cdnSettings.setFormat("1080p");
        cdnSettings.setIngestionType("rtmp");
        LiveStream liveStream = new LiveStream();
        liveStream.setKind("youtube#liveStream");
        liveStream.setSnippet(liveStreamSnippet);
        liveStream.setCdn(cdnSettings);
        LiveStream liveStream2 = (LiveStream) youtube.liveStreams().insert("snippet,cdn", liveStream).execute();
        System.out.println("\n================== Returned Stream ==================\n");
        PrintStream printStream9 = System.out;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(str4);
        sb9.append(liveStream2.getId());
        printStream9.println(sb9.toString());
        PrintStream printStream10 = System.out;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(str5);
        sb10.append(liveStream2.getSnippet().getTitle());
        printStream10.println(sb10.toString());
        PrintStream printStream11 = System.out;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(str6);
        sb11.append(liveStream2.getSnippet().getDescription());
        printStream11.println(sb11.toString());
        PrintStream printStream12 = System.out;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(str7);
        sb12.append(liveStream2.getSnippet().getPublishedAt());
        printStream12.println(sb12.toString());
        PrintStream printStream13 = System.out;
        StringBuilder sb13 = new StringBuilder();
        sb13.append("  - CDN_INFO_STREAM_NAME: ");
        sb13.append(liveStream2.getCdn().getIngestionInfo().getStreamName());
        printStream13.println(sb13.toString());
        Bind bind = youtube.liveBroadcasts().bind(liveBroadcast2.getId(), "id,contentDetails");
        bind.setStreamId(liveStream2.getId());
        LiveBroadcast liveBroadcast3 = (LiveBroadcast) bind.execute();
        System.out.println("\n================== Returned Bound Broadcast ==================\n");
        PrintStream printStream14 = System.out;
        StringBuilder sb14 = new StringBuilder();
        sb14.append("  - Broadcast Id: ");
        sb14.append(liveBroadcast3.getId());
        printStream14.println(sb14.toString());
        PrintStream printStream15 = System.out;
        StringBuilder sb15 = new StringBuilder();
        sb15.append("  - Bound Stream Id: ");
        sb15.append(liveBroadcast3.getContentDetails().getBoundStreamId());
        printStream15.println(sb15.toString());
        liveYoutubeFinalModel.setStreamID(liveStream2.getId());
        liveYoutubeFinalModel.setStreamTitle(liveStream2.getSnippet().getTitle());
        liveYoutubeFinalModel.setStreamCDNKey(liveStream2.getCdn().getIngestionInfo().getStreamName());
        try {
            youtube.liveBroadcasts().transition("testing", liveYoutubeFinalModel.getBroadcastID(), "status").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liveYoutubeFinalModel;
    }

    private static String getBroadcastTitle() throws IOException {
        System.out.print("Please enter a broadcast title: ");
        String readLine = new BufferedReader(new InputStreamReader(System.in)).readLine();
        return readLine.length() < 1 ? "New Broadcast" : readLine;
    }

    private static String getStreamTitle() throws IOException {
        System.out.print("Please enter a stream title: ");
        String readLine = new BufferedReader(new InputStreamReader(System.in)).readLine();
        return readLine.length() < 1 ? "New Stream" : readLine;
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
}
