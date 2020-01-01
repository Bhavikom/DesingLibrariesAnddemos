package com.yasoka.eazyscreenrecord.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.SplashActivity;
import com.yasoka.eazyscreenrecord.activities.TrimVideoActivity;
import com.yasoka.eazyscreenrecord.alertdialogs.DeleteConfirmationDialog;
import com.yasoka.eazyscreenrecord.utils.FilesAccessHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import java.io.File;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;


public class NewVideoPlayerActivity extends AppCompatActivity implements OnClickListener, OnTouchListener {
    public static final String EXTRA_IS_PLAYER_STARTED_FROM_GALLERY = "is_player_from_gallery";
    public static final String KEY_EXTRA_VIDEO_DURATION = "duration";
    public static final String KEY_EXTRA_VIDEO_PATH = "VideoPath";
    private int deviceHeight;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public int height;
    /* access modifiers changed from: private */
    public boolean isAnimating;
    private boolean isPathFromFile = false;
    private boolean isPlayerStartedFromGallery = false;
    /* access modifiers changed from: private */
    public boolean isPlaying = false;
    /* access modifiers changed from: private */
    public int lastPosition = 0;
    /* access modifiers changed from: private */
    public ImageView mIvPlayPause;
    /* access modifiers changed from: private */
    public LinearLayout mLayoutActionBar;
    /* access modifiers changed from: private */
    public LinearLayout mLayoutBottom;
    /* access modifiers changed from: private */
    public SeekBar mSeekBar;
    /* access modifiers changed from: private */
    public TextureView mTextureView;
    /* access modifiers changed from: private */
    public TextView mTvCurrentPosition;
    /* access modifiers changed from: private */
    public MediaPlayer mediaPlayer;
    /* access modifiers changed from: private */
    public String stringExtra;
    private Timer timer;
    /* access modifiers changed from: private */
    public int width;

    class MyHideAnimation implements AnimationListener {
        final NewVideoPlayerActivity newVideoPlayerActivity;

        public void onAnimationRepeat(Animation animation) {
        }

        MyHideAnimation(NewVideoPlayerActivity newVideoPlayerActivity2) {
            this.newVideoPlayerActivity = newVideoPlayerActivity2;
        }

        public void onAnimationEnd(Animation animation) {
            if (this.newVideoPlayerActivity.isAnimating) {
                this.newVideoPlayerActivity.mLayoutBottom.setVisibility(8);
                this.newVideoPlayerActivity.mIvPlayPause.setVisibility(8);
                return;
            }
            this.newVideoPlayerActivity.findViewById(C0793R.C0795id.scroll_view).setVisibility(0);
            this.newVideoPlayerActivity.mLayoutBottom.setVisibility(0);
            this.newVideoPlayerActivity.mIvPlayPause.setVisibility(0);
        }

        public void onAnimationStart(Animation animation) {
            if (this.newVideoPlayerActivity.isAnimating) {
                this.newVideoPlayerActivity.findViewById(C0793R.C0795id.scroll_view).setVisibility(8);
            }
        }
    }

    private final class MyNewTimerTask extends TimerTask {
        final NewVideoPlayerActivity newVideoPlayerActivity;

        class TimerTaskListener implements Runnable {
            final MyNewTimerTask myNewTimerTask;

            TimerTaskListener(MyNewTimerTask myNewTimerTask2) {
                this.myNewTimerTask = myNewTimerTask2;
            }

            public void run() {
                this.myNewTimerTask.newVideoPlayerActivity.isAnimating = false;
                this.myNewTimerTask.newVideoPlayerActivity.updateAnimation();
            }
        }

        private MyNewTimerTask(NewVideoPlayerActivity newVideoPlayerActivity2) {
            this.newVideoPlayerActivity = newVideoPlayerActivity2;
        }

        public void run() {
            this.newVideoPlayerActivity.handler.post(new TimerTaskListener(this));
        }
    }

    class MySeekListener implements OnSeekBarChangeListener {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        MySeekListener() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z && NewVideoPlayerActivity.this.mediaPlayer != null) {
                NewVideoPlayerActivity.this.mediaPlayer.seekTo(i);
            }
            NewVideoPlayerActivity.this.mTvCurrentPosition.setText(NewVideoPlayerActivity.this.stringForTime((long) i));
        }
    }

    class MyShowAnimation implements AnimationListener {
        final NewVideoPlayerActivity newVideoPlayerActivity;

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }

        MyShowAnimation(NewVideoPlayerActivity newVideoPlayerActivity2) {
            this.newVideoPlayerActivity = newVideoPlayerActivity2;
        }

        public void onAnimationEnd(Animation animation) {
            if (this.newVideoPlayerActivity.isAnimating) {
                this.newVideoPlayerActivity.mLayoutActionBar.setVisibility(8);
                this.newVideoPlayerActivity.mIvPlayPause.setVisibility(8);
                return;
            }
            this.newVideoPlayerActivity.mLayoutActionBar.setVisibility(0);
            this.newVideoPlayerActivity.mIvPlayPause.setVisibility(0);
        }
    }

    class NewTimerTask extends MyTimerTask {
        final NewVideoPlayerActivity newVideoPlayerActivity;

        NewTimerTask(NewVideoPlayerActivity newVideoPlayerActivity2) {
            this.newVideoPlayerActivity = newVideoPlayerActivity2;
        }

        public void performTask() {
            if (this.newVideoPlayerActivity.mediaPlayer != null) {
                this.newVideoPlayerActivity.mSeekBar.setProgress(this.newVideoPlayerActivity.mediaPlayer.getCurrentPosition());
                if (this.newVideoPlayerActivity.mediaPlayer.isPlaying()) {
                    this.newVideoPlayerActivity.mIvPlayPause.setImageResource(C0793R.mipmap.ic_pause);
                } else {
                    this.newVideoPlayerActivity.mIvPlayPause.setImageResource(C0793R.mipmap.ic_play);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        if (!(getIntent() == null || getIntent().getAction() == null || !getIntent().getAction().equals("android.intent.action.VIEW") || getIntent().getData() == null)) {
            this.isPathFromFile = true;
            this.stringExtra = FilesAccessHelper.getInstance().getPath(getApplicationContext(), getIntent().getData(), true);
        }
        if (!this.isPathFromFile) {
            if (getIntent() != null) {
                Intent intent = getIntent();
                String str = EXTRA_IS_PLAYER_STARTED_FROM_GALLERY;
                if (intent.hasExtra(str)) {
                    this.isPlayerStartedFromGallery = getIntent().getBooleanExtra(str, false);
                }
            }
            this.stringExtra = getIntent().getStringExtra(KEY_EXTRA_VIDEO_PATH);
        } else if (TextUtils.isEmpty(this.stringExtra)) {
            Toast.makeText(getApplicationContext(), C0793R.string.id_error_playing_video_message, 1).show();
            finish();
            return;
        } else {
            Intent intent2 = new Intent(getApplicationContext(), SplashActivity.class);
            intent2.putExtra(FloatingService.EXTRA_STARTED_FROM_OTHER_APPS, true);
            startActivity(intent2);
        }
        try {
            this.formatBuilder = new StringBuilder();
            this.formatter = new Formatter(this.formatBuilder, Locale.getDefault());
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(this.stringExtra);
            this.height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
            this.width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
            int i = this.width > this.height ? 1 : 0;
            int parseInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
            if (parseInt == 90 || parseInt == 270) {
                i = i == 0 ? 1 : 0;
                int i2 = this.height;
                this.height = this.width;
                this.width = i2;
            }
            setRequestedOrientation(i ^ 1);
            this.deviceHeight = Utils.m16643d(this);
            setContentView((int) R.layout.activity_new_video_player2);
            FirebaseEventsNewHelper.getInstance().sendLocalVideoPlayEvent();
            this.mTextureView = (TextureView) findViewById(C0793R.C0795id.video_viewer);
            this.mSeekBar = (SeekBar) findViewById(C0793R.C0795id.seek_bar);
            this.mIvPlayPause = (ImageView) findViewById(C0793R.C0795id.iv_play_pause);
            this.mLayoutActionBar = (LinearLayout) findViewById(C0793R.C0795id.layout_actionbar);
            this.mLayoutBottom = (LinearLayout) findViewById(C0793R.C0795id.layout_toolbar);
            this.mTvCurrentPosition = (TextView) findViewById(C0793R.C0795id.tv_current_position);
            findViewById(C0793R.C0795id.img_share).setOnClickListener(this);
            findViewById(C0793R.C0795id.img_trim_video).setOnClickListener(this);
            if (!this.isPathFromFile) {
                findViewById(C0793R.C0795id.img_delete).setVisibility(0);
                findViewById(C0793R.C0795id.img_delete).setOnClickListener(this);
            } else {
                findViewById(C0793R.C0795id.img_delete).setVisibility(8);
            }
            init(this.stringExtra);
            new Timer().schedule(new MyNewTimerTask(this), 1000);
            this.handler = new Handler(getMainLooper());
            if (!this.isPlayerStartedFromGallery) {
                setResult(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(final String str) {
        this.mSeekBar.setOnSeekBarChangeListener(new MySeekListener());
        this.mIvPlayPause.setOnClickListener(this);
        findViewById(C0793R.C0795id.iv_undo).setOnClickListener(this);
        String name = new File(str).getName();
        ((TextView) findViewById(C0793R.C0795id.video_name_text)).setText(name.substring(0, name.length() - 4));
        this.mLayoutBottom.setOnTouchListener(this);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(C0793R.C0795id.video_container);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Utils.setViewHeight((((float) NewVideoPlayerActivity.this.width) * 1.0f) / ((float) NewVideoPlayerActivity.this.height), NewVideoPlayerActivity.this.mTextureView, true);
            }
        });
        this.mTextureView.setSurfaceTextureListener(new SurfaceTextureListener() {

            /* renamed from: com.ezscreenrecorder.video.NewVideoPlayerActivity$2$MyCompleteListener */
            class MyCompleteListener implements OnCompletionListener {
                MyCompleteListener() {
                }

                public void onCompletion(MediaPlayer mediaPlayer) {
                    NewVideoPlayerActivity.this.isPlaying = true;
                    if (NewVideoPlayerActivity.this.isAnimating) {
                        NewVideoPlayerActivity.this.updateAnimation();
                    }
                }
            }

            /* renamed from: com.ezscreenrecorder.video.NewVideoPlayerActivity$2$MyPrepareListener */
            class MyPrepareListener implements OnPreparedListener {
                MyPrepareListener() {
                }

                public void onPrepared(MediaPlayer mediaPlayer) {
                    int duration = mediaPlayer.getDuration();
                    NewVideoPlayerActivity.this.mSeekBar.setMax(duration);
                    ((TextView) NewVideoPlayerActivity.this.findViewById(C0793R.C0795id.tv_duration)).setText(NewVideoPlayerActivity.this.stringForTime((long) duration));
                    mediaPlayer.seekTo(NewVideoPlayerActivity.this.lastPosition);
                    if (NewVideoPlayerActivity.this.isPlaying) {
                        mediaPlayer.seekTo(10);
                    } else {
                        mediaPlayer.start();
                    }
                }
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                Surface surface = new Surface(surfaceTexture);
                try {
                    NewVideoPlayerActivity.this.mediaPlayer = new MediaPlayer();
                    NewVideoPlayerActivity.this.mediaPlayer.setDataSource(str);
                    NewVideoPlayerActivity.this.mediaPlayer.setAudioStreamType(3);
                    NewVideoPlayerActivity.this.mediaPlayer.setSurface(surface);
                    NewVideoPlayerActivity.this.mediaPlayer.setOnPreparedListener(new MyPrepareListener());
                    NewVideoPlayerActivity.this.mediaPlayer.prepare();
                    NewVideoPlayerActivity.this.mediaPlayer.setOnCompletionListener(new MyCompleteListener());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
        this.mTextureView.setOnClickListener(this);
    }

    /* access modifiers changed from: private */
    public void updateAnimation() {
        this.isAnimating = !this.isAnimating;
        showAnimation();
        hideAnimation();
    }

    private void hideAnimation() {
        TranslateAnimation translateAnimation = this.isAnimating ? new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) Utils.m16637a(this, 100)) : new TranslateAnimation(0.0f, 0.0f, (float) Utils.m16637a(this, 100), 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new MyHideAnimation(this));
        this.mLayoutBottom.startAnimation(translateAnimation);
    }

    private void showAnimation() {
        TranslateAnimation translateAnimation = this.isAnimating ? new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-Utils.m16637a(this, 50))) : new TranslateAnimation(0.0f, 0.0f, (float) (-Utils.m16637a(this, 50)), 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new MyShowAnimation(this));
        this.mLayoutActionBar.startAnimation(translateAnimation);
    }

    public void setResult(boolean z) {
        if (z) {
            setResult(-1);
        } else {
            setResult(0);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0793R.C0795id.img_delete /*2131296802*/:
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                    this.mIvPlayPause.setImageResource(C0793R.mipmap.ic_play);
                }
                DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_RECORDING_DELETE_CONFIRMATION);
                instance.setDialogResultListener(new OnConfirmationResult() {
                    public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                        if (z) {
                            Single.create(new SingleOnSubscribe<Boolean>() {
                                public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                                    singleEmitter.onSuccess(Boolean.valueOf(new File(NewVideoPlayerActivity.this.stringExtra).delete()));
                                }
                            }).subscribe((SingleObserver<? super T>) new SingleObserver<Boolean>() {
                                public void onSubscribe(Disposable disposable) {
                                }

                                public void onSuccess(Boolean bool) {
                                    if (bool.booleanValue()) {
                                        NewVideoPlayerActivity.this.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{NewVideoPlayerActivity.this.stringExtra});
                                        FilesAccessHelper.getInstance().refreshGallery(NewVideoPlayerActivity.this.getApplicationContext(), NewVideoPlayerActivity.this.stringExtra);
                                    }
                                    NewVideoPlayerActivity.this.setResult(bool.booleanValue());
                                    dialogFragment.dismiss();
                                    NewVideoPlayerActivity.this.finish();
                                }

                                public void onError(Throwable th) {
                                    th.printStackTrace();
                                }
                            });
                        } else {
                            dialogFragment.dismiss();
                        }
                    }
                });
                instance.show(getSupportFragmentManager(), "delete_confirmation_dialog");
                break;
            case C0793R.C0795id.img_share /*2131296822*/:
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                    this.mIvPlayPause.setImageResource(C0793R.mipmap.ic_play);
                }
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{this.stringExtra}, null, new OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("video/mp4");
                        intent.putExtra("android.intent.extra.TITLE", NewVideoPlayerActivity.this.getString(C0793R.string.share_video));
                        intent.putExtra("android.intent.extra.SUBJECT", NewVideoPlayerActivity.this.getString(C0793R.string.share_video));
                        intent.putExtra("android.intent.extra.TEXT", NewVideoPlayerActivity.this.getString(C0793R.string.share_video_txt));
                        intent.addFlags(1);
                        Context applicationContext = NewVideoPlayerActivity.this.getApplicationContext();
                        StringBuilder sb = new StringBuilder();
                        sb.append(NewVideoPlayerActivity.this.getPackageName());
                        sb.append(".my.package.name.provider");
                        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(applicationContext, sb.toString(), new File(str)));
                        PrintStream printStream = System.out;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onScanCompleted uri ");
                        sb2.append(uri);
                        printStream.println(sb2.toString());
                        NewVideoPlayerActivity newVideoPlayerActivity = NewVideoPlayerActivity.this;
                        newVideoPlayerActivity.startActivity(Intent.createChooser(intent, newVideoPlayerActivity.getString(C0793R.string.share_video)));
                        FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
                    }
                });
                break;
            case C0793R.C0795id.img_trim_video /*2131296827*/:
                Intent intent = new Intent(this, TrimVideoActivity.class);
                intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_PATH, FileUtils.getPath(this, Uri.fromFile(new File(this.stringExtra))));
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(getApplicationContext(), Uri.fromFile(new File(this.stringExtra)));
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
                    if (Long.parseLong(extractMetadata) >= 5000) {
                        intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_DURATION, Long.parseLong(extractMetadata));
                        intent.setFlags(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);
                        boolean z = this.isPathFromFile;
                        String str = TrimVideoActivity.EXTRA_IS_FROM_OTHER_APP;
                        if (z) {
                            intent.putExtra(str, true);
                        } else {
                            intent.putExtra(str, false);
                        }
                        startActivity(intent);
                        ActivityCompat.finishAfterTransition(this);
                        break;
                    } else {
                        Toast.makeText(getApplicationContext(), C0793R.string.id_trim_length_error_msg, 1).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case C0793R.C0795id.iv_play_pause /*2131296840*/:
                if (this.isPlaying) {
                    this.mTextureView.setAlpha(1.0f);
                }
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                    this.mIvPlayPause.setImageResource(C0793R.mipmap.ic_play);
                    return;
                }
                this.mediaPlayer.start();
                if (this.isPlaying) {
                    this.isPlaying = false;
                }
                this.mIvPlayPause.setImageResource(C0793R.mipmap.ic_pause);
                return;
            case C0793R.C0795id.iv_undo /*2131296842*/:
                onBackPressed();
                return;
            case C0793R.C0795id.video_viewer /*2131297107*/:
                updateAnimation();
                return;
            default:
                return;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return view.getId() == R.id.layout_toolbar;
    }

    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.pause();
            this.lastPosition = this.mediaPlayer.getCurrentPosition();
        }
        stopTimer();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            this.mediaPlayer.release();
        }
        this.mediaPlayer = null;
    }

    private void stopTimer() {
        Timer timer2 = this.timer;
        if (timer2 != null) {
            timer2.cancel();
            this.timer = null;
        }
    }

    private void timers() {
        stopTimer();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new NewTimerTask(this), 100, 1000);
    }

    public void onResume() {
        super.onResume();
        timers();
    }

    /* access modifiers changed from: private */
    public String stringForTime(long j) {
        long j2 = (j + 500) / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = j2 / 3600;
        this.formatBuilder.setLength(0);
        if (j5 > 0) {
            return this.formatter.format("%d:%02d:%02d", new Object[]{Long.valueOf(j5), Long.valueOf(j4), Long.valueOf(j3)}).toString();
        }
        return this.formatter.format("%02d:%02d", new Object[]{Long.valueOf(j4), Long.valueOf(j3)}).toString();
    }
}
