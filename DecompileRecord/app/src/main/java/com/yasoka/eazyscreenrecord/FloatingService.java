package com.yasoka.eazyscreenrecord;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
/*import android.support.media.ExifInterface;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.content.ContextCompat;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/*import com.appsmartz.recorder.MediaEncoder.MediaEncoderListener;
import com.appsmartz.recorder.MediaMuxerWrapper;
import com.appsmartz.recorder.MediaScreenEncoder;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.activities.ExplainerVideoActivity;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.LiveLoginActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.activities.SplashActivity;
import com.ezscreenrecorder.float_camera.CameraPreview;
import com.ezscreenrecorder.float_camera.StickerCameraView2;
import com.ezscreenrecorder.float_camera.StickerView.onScaleView;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar.OnColorChangeListener;
import com.ezscreenrecorder.imgedit.fabric.DrawingView;
import com.ezscreenrecorder.model.ActionMoreModel;
import com.ezscreenrecorder.model.MetadataInfoModel;
import com.ezscreenrecorder.model.SharedDataForOtherApp;
import com.ezscreenrecorder.receivers.BroadcastReceiverPauseRecord;
import com.ezscreenrecorder.receivers.BroadcastReceiverResumeRecord;
import com.ezscreenrecorder.receivers.BroadcastReceiverStopBubble;
import com.ezscreenrecorder.receivers.BroadcastReceiverStopRecord;
import com.ezscreenrecorder.receivers.NotificationActionBroadcastReceiver;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.ezscreenrecorder.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.FirebaseConversionEventHelper;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.InterstitialAdLoader;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.PulsatorLayout;
import com.ezscreenrecorder.utils.UtilityMethods;
import com.facebook.ads.AdError;
import com.facebook.appevents.AppEventsConstants;
import com.google.api.client.http.HttpStatusCodes;
import com.google.common.primitives.Ints;*/
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yasoka.eazyscreenrecord.activities.ExplainerVideoActivity;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.LiveLoginActivity;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.activities.SplashActivity;
import com.yasoka.eazyscreenrecord.float_camera.CameraPreview;
import com.yasoka.eazyscreenrecord.float_camera.StickerCameraView2;
import com.yasoka.eazyscreenrecord.float_camera.StickerView;
import com.yasoka.eazyscreenrecord.imgedit.colorseekbar.ColorSeekBar;
import com.yasoka.eazyscreenrecord.imgedit.fabric.DrawingView;
import com.yasoka.eazyscreenrecord.model.ActionMoreModel;
import com.yasoka.eazyscreenrecord.model.AudioFileModel;
import com.yasoka.eazyscreenrecord.model.MetadataInfoModel;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
import com.yasoka.eazyscreenrecord.receivers.BroadcastReceiverPauseRecord;
import com.yasoka.eazyscreenrecord.receivers.BroadcastReceiverResumeRecord;
import com.yasoka.eazyscreenrecord.receivers.BroadcastReceiverStopBubble;
import com.yasoka.eazyscreenrecord.receivers.BroadcastReceiverStopRecord;
import com.yasoka.eazyscreenrecord.receivers.NotificationActionBroadcastReceiver;
import com.yasoka.eazyscreenrecord.recorder.MediaEncoder;
import com.yasoka.eazyscreenrecord.recorder.MediaMuxerWrapper;
import com.yasoka.eazyscreenrecord.recorder.MediaScreenEncoder;
import com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.yasoka.eazyscreenrecord.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.FirebaseConversionEventHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.InterstitialAdLoader;
import com.yasoka.eazyscreenrecord.utils.NativeAdLoaderPreviewDialog;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.PulsatorLayout;
import com.yasoka.eazyscreenrecord.utils.UtilityMethods;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*
import p009io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableSource;
import p009io.reactivex.Observer;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.disposables.CompositeDisposable;
import p009io.reactivex.disposables.Disposable;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableObserver;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;
*/

public class FloatingService extends Service implements OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String ACTION_INTENT_PREVIEW_SCREEN = "action_show_preview_screen";
    private static final long ANIMATION_DURATION = 200;
    private static final long ANIMATION_DURATION_EXTEND = 60;
    private static final String CHANNEL_ID = "com.ezscreenrecorder.APP_CHANNEL_ID";
    public static int DISPLAY_HEIGHT = 640;
    public static int DISPLAY_WIDTH = 480;
    public static final int EXTRA_MAIN_ACTION_TYPE_AUDIO = 1343;
    public static final int EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO = 1344;
    public static final int EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE = 1350;
    public static final int EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE = 1347;
    public static final int EXTRA_MAIN_ACTION_TYPE_GAME_RECORD = 1345;
    public static final int EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO = 1342;
    public static final int EXTRA_MAIN_ACTION_TYPE_MORE = 1346;
    public static final int EXTRA_MAIN_ACTION_TYPE_NONE = 1300;
    public static final int EXTRA_MAIN_ACTION_TYPE_SCREENSHOT = 1340;
    public static final int EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD = 1348;
    public static final int EXTRA_MAIN_ACTION_TYPE_VIDEO = 1341;
    public static final int EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD = 1349;
    public static final int EXTRA_SHOW_GAME_LIST_ACTIVITY = 1440;
    public static final String EXTRA_STARTED_FROM_OTHER_APPS = "started_from_other_apps";
    public static final String FLAG_PAUSE_RECORDING = "PauseRecording";
    public static final String FLAG_RESUME_RECORDING = "ResumeRecording";
    public static final String FLAG_RUNNING_SERVICE = "RunningSerovericeCheck";
    public static final String FLAG_SHOW_FLOATING = "ShowFloating";
    private static final String FLAG_UNACCESS_CAMERA = "NoCameraAccess";
    private static final int FOREGROUND_ID = 3411;
    public static final String GALLERY_TYPE_IMAGE = "Imagegallery";
    public static final String GALLERY_VIDEO_PLAY = "VideopPlay";
    private static final int INITIAL_HEIGHT = 300;
    private static final int INITIAL_WIDTH = 300;
    public static final String IS_SCREEN_SHOT = "IsScreenShot";
    public static final String KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS = "main_floating_action_type";
    public static final String KEY_LIVE_VIDEO_STREAM_URL_DATA = "live_vid_stream_url";
    private static final int LEFT_ANGLE_FOR_X1Y1 = 20;
    private static final int LEFT_ANGLE_FOR_X2Y2 = 60;
    private static final int LEFT_ANGLE_FOR_X3Y3 = -20;
    private static final int LEFT_ANGLE_FOR_X4Y4 = -60;
    public static final String NEW_FLAG_SHOW_FLOATING = "NeShowFloatingBubble";
    public static final int NOTIFICATION_IMAGE = 112;
    public static final int NUM_OF_SIDES = 6;
    private static final int RIGHT_ANGLE_FOR_X1Y1 = 160;
    private static final int RIGHT_ANGLE_FOR_X2Y2 = 125;
    private static final int RIGHT_ANGLE_FOR_X3Y3 = -160;
    private static final int RIGHT_ANGLE_FOR_X4Y4 = -125;
    private static final String SAVED_FILES = "SavedFilesForVideorecordingFromOtherApp";
    public static final String SHOW_GALLERY = "showGallery";
    public static final String TYPE = "TyeToStartrecorder";
    public static MediaProjection mMediaProjection;
    public static int mScreenDensity;
    /* access modifiers changed from: private */

    /* renamed from: aU */
    public Animation f73aU;
    /* access modifiers changed from: private */

    /* renamed from: aV */
    public Animation f74aV;
    /* access modifiers changed from: private */
    public int actionType;
    /* access modifiers changed from: private */
    public boolean audioAddedRecorderType2;
    /* access modifiers changed from: private */
    public AudioRecorder audioRecorder;
    /* access modifiers changed from: private */
    public boolean avoidLongClick;
    /* access modifiers changed from: private */
    public LayoutParams backRecord;
    /* access modifiers changed from: private */
    public View backView;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (!intent.getBooleanExtra(FloatingService.SHOW_GALLERY, false)) {
                    if (!intent.getBooleanExtra(FloatingService.FLAG_PAUSE_RECORDING, false)) {
                        if (!intent.getBooleanExtra(FloatingService.FLAG_RESUME_RECORDING, false)) {
                            if (!intent.getBooleanExtra(FloatingService.FLAG_SHOW_FLOATING, false)) {
                                if (!intent.getBooleanExtra(FloatingService.NEW_FLAG_SHOW_FLOATING, false)) {
                                    if (FloatingService.this.floatingView != null) {
                                        FloatingService.this.floatingView.setVisibility(View.GONE);
                                    }
                                    String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
                                    if (intent.hasExtra(str)) {
                                        FloatingService.this.actionType = intent.getIntExtra(str, -1);
                                        if (FloatingService.this.whichAnimation == 1) {
                                            FloatingService.this.clicked();
                                        }
                                        PreferenceHelper.getInstance().incrementActionCount();
                                        if (PreferenceHelper.getInstance().getActionCount() % 2 != 0) {
                                            InterstitialAdLoader.getInstance().loadInterstitialAd();
                                        }
                                        FloatingService.this.gamePackageName = null;
                                        switch (FloatingService.this.actionType) {
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                                                FloatingService.this.takeScreenShot(false);
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                                                int access$3100 = FloatingService.this.actionType;
                                                String str2 = FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA;
                                                switch (access$3100) {
                                                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                                                        String str3 = RecordingActivity.EXTRA_GAME_LIST_PACKAGE_NAME;
                                                        if (intent.hasExtra(str3)) {
                                                            FloatingService.this.gamePackageName = intent.getStringExtra(str3);
                                                            if (TextUtils.isEmpty(FloatingService.this.gamePackageName)) {
                                                                FloatingService.this.gamePackageName = null;
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
                                                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                                                        if (intent.hasExtra(str2)) {
                                                            FloatingService.this.facebookLiveStreamJsonData = intent.getStringExtra(str2);
                                                            break;
                                                        }
                                                        break;
                                                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                                                        if (intent.hasExtra(str2)) {
                                                            FloatingService.this.twitchLiveData = (LiveTwitchFinalModel) intent.getSerializableExtra(str2);
                                                            break;
                                                        }
                                                        break;
                                                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                                                        if (intent.hasExtra(str2)) {
                                                            FloatingService.this.youtubeFinalModel = (LiveYoutubeFinalModel) intent.getSerializableExtra(str2);
                                                            break;
                                                        }
                                                        break;
                                                }
                                                FloatingService floatingService = FloatingService.this;
                                                // todo
                                                floatingService.count = Integer.parseInt(floatingService.prefs.getString("example_list_count_down", "3"));
                                                if (FloatingService.this.count != 0 && FloatingService.this.checkOverlayPermission()) {
                                                    try {
                                                        if (FloatingService.this.checkOverlayPermission()) {
                                                            if (FloatingService.this.countDownTimer == null) {
                                                                FloatingService.this.countDownTimer = LayoutInflater.from(FloatingService.this).inflate(R.layout.layout_count_down_timer, null);
                                                            }
                                                            FloatingService.this.windowManager.addView(FloatingService.this.countDownTimer, FloatingService.this.paramsCountDown);
                                                            if (FloatingService.this.countDownTxt == null) {
                                                                FloatingService.this.countDownTxt = (TextView) FloatingService.this.countDownTimer.findViewById(R.id.txt_count_down);
                                                            }
                                                            FloatingService.this.countDownTxt.setText(String.valueOf(FloatingService.this.count));
                                                            FloatingService.this.f73aU = AnimationUtils.loadAnimation(FloatingService.this, R.anim.scale_up);
                                                            FloatingService.this.f74aV = AnimationUtils.loadAnimation(FloatingService.this, R.anim.fade_out_200);
                                                            FloatingService.this.f73aU.setAnimationListener(new AuListener());
                                                            FloatingService.this.f74aV.setAnimationListener(new AvListener());
                                                            FloatingService.this.countDownTxt.startAnimation(FloatingService.this.f73aU);
                                                            break;
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        FloatingService.this.startRecord();
                                                        break;
                                                    }
                                                } else {
                                                    if (FloatingService.this.actionType == 1344) {
                                                        Intent intent2 = new Intent(FloatingService.this.getApplicationContext(), ExplainerVideoActivity.class);
                                                        intent2.addFlags(268468224);
                                                        FloatingService.this.startActivity(intent2);
                                                    }
                                                    Single.timer(100, TimeUnit.MILLISECONDS).subscribe((SingleObserver<? super T>) new SingleObserver<Long>() {
                                                        public void onError(Throwable th) {
                                                        }

                                                        public void onSubscribe(Disposable disposable) {
                                                        }

                                                        public void onSuccess(Long l) {
                                                            FloatingService.this.startRecord();
                                                        }
                                                    });
                                                    return;
                                                }
                                                break;
                                        }
                                        switch (FloatingService.this.actionType) {
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion("Image");
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion("Video");
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion(FirebaseConversionEventHelper.KEY_INTERACTIVE_VIDEO_RECORD_EVENT);
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion("Audio");
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion(FirebaseConversionEventHelper.KEY_EXPLAINER_VIDEO_EVENT);
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                            case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                                                if (!PreferenceHelper.getInstance().getPrefFirstActionSent()) {
                                                    FirebaseConversionEventHelper.getInstance().sendFirstActionConversion(FirebaseConversionEventHelper.KEY_GAME_RECORD_EVENT);
                                                    PreferenceHelper.getInstance().setPrefFirstActionSent(true);
                                                    break;
                                                }
                                                break;
                                        }
                                    }
                                }
                            } else {
                                // stop screenrecording here bhavik
                                FloatingService.this.getFrontAppsListNames();
                                FloatingService.this.unaccessCamera = intent.getBooleanExtra(FloatingService.FLAG_UNACCESS_CAMERA, false);
                                FloatingService.this.stopScreenRecording();
                                FloatingService.this.recordTimeOrientation = 0;
                                FloatingService.this.changeOrientation = false;
                                if (PreferenceHelper.getInstance().getPrefInteractiveRecodingCamera()) {
                                    FloatingService.this.closeCamera();
                                    FloatingService.this.stopBackgroundThread();
                                    FloatingService.this.removeSurfaceView();
                                }
                                try {
                                    FloatingService.this.windowManager.updateViewLayout(FloatingService.this.floatingView, FloatingService.this.layoutParamsMain);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                if (FloatingService.this.floatingView != null) {
                                    FloatingService.this.floatingView.setVisibility(0);
                                }
                            }
                        } else {
                            FloatingService.this.hideNotificationTray();
                            if (FloatingService.this.mRecorder != null) {
                                FloatingService.this.mRecorder.setIsRecording(true);
                            }
                            if (FloatingService.this.sMuxer != null) {
                                FloatingService.this.sMuxer.resumeRecording();
                            }
                            if (FloatingService.this.view1 != null) {
                                ((ImageView) FloatingService.this.view1.findViewById(R.id.img_option)).setImageResource(R.drawable.pause_recorder_selector);
                                FloatingService.this.view1.findViewById(R.id.img_option).setTag("Pause");
                                FloatingService.this.handler.postDelayed(FloatingService.this.removeCallbacks, 10);
                            }
                            FloatingService.this.showNotification(1);
                            if (FloatingService.this.txtTimer != null) {&&&&&&&
                                FloatingService.this.txtTimer.clearAnimation();
                            }
                            if (FloatingService.this.txtTimer != null && FloatingService.this.mExpanded) {
                                FloatingService.this.txtTimer.callOnClick();
                            }
                        }
                    } else {
                        if (FloatingService.this.mRecorder != null) {
                            FloatingService.this.mRecorder.setIsRecording(false);
                        }
                        if (FloatingService.this.sMuxer != null) {
                            FloatingService.this.sMuxer.pauseRecording();
                        }
                        FloatingService.this.pausedOnce = true;
                        if (FloatingService.this.view1 != null) {
                            ((ImageView) FloatingService.this.view1.findViewById(R.id.img_option)).setImageResource(R.drawable.play_recorder_selector);
                            FloatingService.this.view1.findViewById(R.id.img_option).setTag("Resume");
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                            alphaAnimation.setDuration(450);
                            alphaAnimation.setStartOffset(20);
                            alphaAnimation.setRepeatMode(2);
                            alphaAnimation.setRepeatCount(-1);
                            FloatingService.this.txtTimer.startAnimation(alphaAnimation);
                        }
                        FloatingService.this.showNotification(4);
                        FloatingService.this.handler.removeCallbacks(FloatingService.this.removeCallbacks);
                    }
                } else {
                    FloatingService.this.showNotification(0);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public BroadcastReceiver broadcastReceiverOnOffScreen = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                try {
                    Intent intent2 = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
                    intent2.putExtra(FloatingService.FLAG_SHOW_FLOATING, true);
                    context.sendBroadcast(intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Crashlytics.logException(e);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public PulsatorLayout[] buttons;
    /* access modifiers changed from: private */
    public Camera cameraBhavik;
    /* access modifiers changed from: private */
    public boolean cameraCloseByUser;
    private int cameraId;
    /* access modifiers changed from: private */
    public boolean changeOrientation;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    /* access modifiers changed from: private */
    public int count;
    /* access modifiers changed from: private */
    public View countDownTimer;
    /* access modifiers changed from: private */
    public TextView countDownTxt;
    /* access modifiers changed from: private */
    public int countTimer;
    private int[] enterDelay = {20, 30, 40, 10, 0, 50};
    private int[] exitDelay = {20, 10, 0, 30, 40, 50};
    /* access modifiers changed from: private */
    public String facebookLiveStreamJsonData;
    private final DateFormat fileFormat = new SimpleDateFormat("yyyyMMdd-HHmmss'.mp4'", Locale.US);
    private final DateFormat fileFormat2 = new SimpleDateFormat("yyyyMMdd-HHmmss'.jpg'", Locale.US);
    /* access modifiers changed from: private */
    public View floatingView;
    /* access modifiers changed from: private */
    public View floatingViewClose;
    /* access modifiers changed from: private */
    public String gamePackageName;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    private int height;
    /* access modifiers changed from: private */
    public ImageView imgFloat;
    /* access modifiers changed from: private */
    public boolean isAllowTouch = true;
    /* access modifiers changed from: private */
    public boolean isLongclick;
    /* access modifiers changed from: private */
    public boolean isRight;
    private String live_url;
    private boolean localRecordWithLive = true;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    /* access modifiers changed from: private */
    public Camera mCameraBhavik;
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    /* access modifiers changed from: private */
    public Semaphore mCameraOpenCloseLock = new Semaphore(1);
    /* access modifiers changed from: private
    status.streamStatus */
    public View mCameraTextureView;
    /* access modifiers changed from: private */
    public String mCurrentVideoPath;
    /* access modifiers changed from: private */
    public final Point mDisplaySize = new Point();
    /* access modifiers changed from: private */
    public boolean mExpanded;
    /* access modifiers changed from: private */
    public ImageReader mImageReader;
    /* access modifiers changed from: private */
    public final PointF mInitialDown = new PointF();
    /* access modifiers changed from: private */
    public final Point mInitialPosition = new Point();
    private MediaEncoder.MediaEncoderListener mMediaEncoderListener;
    /* access modifiers changed from: private */
    public MediaRecorder mMediaRecorder;
    /* access modifiers changed from: private */
    public View mNewFloatingView;
    private ImageView mNewimgFloat;
    private OrientationEventListener mOrientationListener;
    private Builder mPreviewBuilder;
    /* access modifiers changed from: private */
    public CameraCaptureSession mPreviewSession;
    private Size mPreviewSize;
    /* access modifiers changed from: private */
    public ScreenRecorder mRecorder;
    private StateCallback mStateCallback = new StateCallback() {
        public void onOpened(CameraDevice cameraDevice) {
            FloatingService.this.mCameraDevice = cameraDevice;
            FloatingService.this.startPreview();
            FloatingService.this.mCameraOpenCloseLock.release();
            if (FloatingService.this.mTextureView != null) {
                FloatingService floatingService = FloatingService.this;
                floatingService.confirgureTransform2(floatingService.windowManager.getDefaultDisplay().getRotation(), FloatingService.this.mTextureView.getWidth(), FloatingService.this.mTextureView.getHeight());
            }
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            FloatingService.this.mCameraOpenCloseLock.release();
            cameraDevice.close();
            FloatingService.this.removeSurfaceView();
            FloatingService.this.mCameraDevice = null;
            if (!FloatingService.this.cameraCloseByUser) {
                Single.just(Boolean.valueOf(true)).delay(20, TimeUnit.MILLISECONDS).subscribe((Consumer<? super T>) new Consumer<Boolean>() {
                    public void accept(Boolean bool) throws Exception {
                        Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
                        intent.putExtra(FloatingService.FLAG_SHOW_FLOATING, true);
                        intent.putExtra(FloatingService.FLAG_UNACCESS_CAMERA, true);
                        FloatingService.this.sendBroadcast(intent);
                    }
                });
            }
        }

        public void onError(CameraDevice cameraDevice, int i) {
            FloatingService.this.mCameraOpenCloseLock.release();
            cameraDevice.close();
            FloatingService.this.removeSurfaceView();
            FloatingService.this.mCameraDevice = null;
            if (!FloatingService.this.cameraCloseByUser) {
                Single.just(Boolean.valueOf(true)).delay(20, TimeUnit.MILLISECONDS).subscribe((Consumer<? super T>) new Consumer<Boolean>() {
                    public void accept(Boolean bool) throws Exception {
                        Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
                        intent.putExtra(FloatingService.FLAG_SHOW_FLOATING, true);
                        intent.putExtra(FloatingService.FLAG_UNACCESS_CAMERA, true);
                        FloatingService.this.sendBroadcast(intent);
                    }
                });
            }
        }
    };
    /* access modifiers changed from: private */
    public TextureView mTextureView;
    private Size mVideoSize;
    /* access modifiers changed from: private */
    public VirtualDisplay mVirtualDisplay;
    /* access modifiers changed from: private */
    public LayoutParams mWindowParams1;
    /* access modifiers changed from: private */
    public LayoutParams mWindowParams2;
    /* access modifiers changed from: private */
    public LayoutParams mWindowParams3;
    /* access modifiers changed from: private */
    public LayoutParams mWindowParams4;
    /* access modifiers changed from: private */
    public MediaScreenEncoder mediaScreenRecorder;
    /* access modifiers changed from: private */
    public int screenOrientation;
    /* access modifiers changed from: private */
    public Parameters parameter;
    /* access modifiers changed from: private */
    public LayoutParams layoutParamsMain;
    /* access modifiers changed from: private */
    public LayoutParams paramsCloseView;
    /* access modifiers changed from: private */
    public LayoutParams paramsCountDown;
    /* access modifiers changed from: private */
    public LayoutParams paramsRecord;
    /* access modifiers changed from: private */
    public boolean pausedOnce;
    private Point[] pentagonVertices;
    /* access modifiers changed from: private */
    public SharedPreferences prefs;
    private int radius;
    /* access modifiers changed from: private */
    public int recordTimeOrientation;
    /* access modifiers changed from: private */
    public View recordView;
    /* access modifiers changed from: private */
    public Runnable removeCallbacks = new Runnable() {
        public void run() {
            FloatingService.this.txtTimer.setText(String.format("%02d:%02d", new Object[]{Long.valueOf((long) ((FloatingService.this.countTimer / 60000) % 60)), Long.valueOf((long) ((FloatingService.this.countTimer / 1000) % 60))}));
            FloatingService floatingService = FloatingService.this;
            floatingService.countTimer = floatingService.countTimer + 1000;
            FloatingService.this.handler.postDelayed(this, 1000);
        }
    };
    /* access modifiers changed from: private */
    public Runnable runnable = new Runnable() {
        public void run() {
            if (!FloatingService.this.mExpanded && FloatingService.this.paramsRecord != null) {
                if (FloatingService.this.paramsRecord.x < (FloatingService.this.windowManager.getDefaultDisplay().getWidth() - FloatingService.this.recordView.getWidth()) / 2) {
                    FloatingService.this.recordView.setPivotX(0.0f);
                } else {
                    FloatingService.this.recordView.setPivotX((float) (FloatingService.this.recordView.getWidth() - ((int) (((double) FloatingService.this.recordView.getWidth()) * 0.06d))));
                }
                FloatingService.this.recordView.animate().scaleX(0.7f).scaleY(0.7f).setDuration(400).start();
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable runnable_circle = new Runnable() {
        public void run() {
            if (FloatingService.this.layoutParamsMain.x < (FloatingService.this.windowManager.getDefaultDisplay().getWidth() - FloatingService.this.floatingView.getWidth()) / 2) {
                FloatingService.this.floatingView.setPivotX(0.0f);
            } else {
                FloatingService.this.floatingView.setPivotX((float) (FloatingService.this.floatingView.getWidth() - ((int) (((double) FloatingService.this.floatingView.getWidth()) * 0.06d))));
            }
            FloatingService.this.floatingView.animate().scaleX(0.7f).scaleY(0.7f).setDuration(400).setListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    FloatingService.this.imgFloat.setAlpha(0.5f);
                }
            }).start();
        }
    };
    /* access modifiers changed from: private */
    public MediaMuxerWrapper sMuxer;
    /* access modifiers changed from: private */
    public View screenShotInflateView;
    private int startPositionX = 0;
    private int startPositionY = 0;
    /* access modifiers changed from: private */
    public Point szWindow = new Point();
    /* access modifiers changed from: private */
    public TextView textViewCameraHighLightMessage;
    /* access modifiers changed from: private */
    public TextView textViewHighLightMessage;
    /* access modifiers changed from: private */
    public TextView textViewPauseHighLightMessage;
    /* access modifiers changed from: private */
    public TextView textViewRecorHighLightMessage;
    /* access modifiers changed from: private */
    public TextView textViewStopHighLightMessage;
    /* access modifiers changed from: private */
    public LiveTwitchFinalModel twitchLiveData;
    /* access modifiers changed from: private */
    public TextView txtTimer;
    /* access modifiers changed from: private */
    public boolean unaccessCamera;
    /* access modifiers changed from: private */
    public View view1;
    /* access modifiers changed from: private */
    public View view2;
    /* access modifiers changed from: private */
    public View view3;
    /* access modifiers changed from: private */
    public View view4;
    /* access modifiers changed from: private */
    public int whichAnimation = 0;
    private int width;
    /* access modifiers changed from: private */
    public WindowManager windowManager;
    /* access modifiers changed from: private */
    public LiveYoutubeFinalModel youtubeFinalModel;

    private class AuListener implements AnimationListener {
        public void onAnimationRepeat(Animation animation) {
        }

        private AuListener() {
        }

        public void onAnimationStart(Animation animation) {
            FloatingService.this.countDownTxt.setVisibility(View.VISIBLE);
        }

        public void onAnimationEnd(Animation animation) {
            FloatingService.this.countDownTxt.startAnimation(FloatingService.this.f74aV);
        }
    }

    private class AudioRecorder {
        private final DateFormat audioFormat = new SimpleDateFormat("yyyyMMdd-HHmmss'.aac'", Locale.US);
        private String mCurrentAudioPath = "";

        public AudioRecorder() {
            Observable.timer(110, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer<? super T>) new DisposableObserver<Long>(FloatingService.this) {
                public void onComplete() {
                }

                public void onError(Throwable th) {
                }

                public void onNext(Long l) {
                    try {
                        AudioRecorder.this.startAudioRecord();
                        FloatingService.this.showFloatingRecordingBubble();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /* access modifiers changed from: private */
        public void startAudioRecord() {
            Flowable.create(new FlowableOnSubscribe<Void>() {
                public void subscribe(FlowableEmitter<Void> flowableEmitter) throws Exception {
                    try {
                        FloatingService.this.mMediaRecorder = new MediaRecorder();
                        AudioRecorder.this.initAudRecorder();
                        Thread.sleep(FloatingService.ANIMATION_DURATION);
                        FloatingService.this.mMediaRecorder.start();
                        FloatingService.this.showNotification(5);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e2) {
                        e2.printStackTrace();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        if (FloatingService.this.floatingView != null) {
                            FloatingService.this.floatingView.setVisibility(View.VISIBLE);
                        }
                        if (FloatingService.mMediaProjection != null) {
                            FloatingService.mMediaProjection.stop();
                        }
                        FloatingService.mMediaProjection = null;
                        FloatingService.this.mMediaRecorder = null;
                    }
                }
            }, BackpressureStrategy.BUFFER).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<Void>() {
                public void onComplete() {
                }

                public void onNext(Void voidR) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        }

        /* access modifiers changed from: private */
        public void initAudRecorder() {
            boolean z;
            try {
                z = FloatingService.this.m14ac();
            } catch (Throwable th) {
                th.printStackTrace();
                z = true;
            }
            if (z) {
                FloatingService.this.mMediaRecorder.setAudioSource(1);
            }
            FloatingService.this.mMediaRecorder.setOutputFormat(2);
            if (z) {
                FloatingService.this.mMediaRecorder.setAudioSamplingRate(SrsCodecAudioSampleRate.R44100);
                FloatingService.this.mMediaRecorder.setAudioEncodingBitRate(96000);
                FloatingService.this.mMediaRecorder.setAudioEncoder(3);
            }
            try {
                this.mCurrentAudioPath = createAudioFile();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (this.mCurrentAudioPath == null) {
                Toast.makeText(FloatingService.this, R.string.id_output_path_error_msg, 1).show();
                return;
            }
            FloatingService.this.mMediaRecorder.setOutputFile(this.mCurrentAudioPath);
            try {
                FloatingService.this.mMediaRecorder.prepare();
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
                Toast.makeText(FloatingService.this, R.string.no_permission_allow_save, 1).show();
            }
        }

        public String getFilePath() {
            return this.mCurrentAudioPath;
        }

        private String createAudioFile() throws NameNotFoundException {
            String audioDir = AppUtils.getAudioDir();
            String format = this.audioFormat.format(new Date());
            StringBuilder sb = new StringBuilder();
            sb.append(audioDir);
            sb.append(format);
            File file = new File(sb.toString());
            file.setWritable(true);
            file.setReadable(true);
            addAudio(file, file.getPath());
            Log.e("Audio file: ", file.getAbsolutePath());
            return file.getAbsolutePath();
        }

        public Uri addAudio(File file, String str) {
            ContentValues contentValues = new ContentValues(2);
            contentValues.put("title", str);
            contentValues.put("_data", file.getAbsolutePath());
            return FloatingService.this.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

    private class AvListener implements AnimationListener {
        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }

        private AvListener() {
        }

        public void onAnimationEnd(Animation animation) {
            if (FloatingService.this.count > 1) {
                FloatingService.this.count = FloatingService.this.count - 1;
                FloatingService.this.countDownTxt.setText(String.valueOf(FloatingService.this.count));
                FloatingService.this.countDownTxt.startAnimation(FloatingService.this.f73aU);
                return;
            }
            FloatingService.this.windowManager.removeView(FloatingService.this.countDownTimer);
            if (FloatingService.this.actionType == 1344) {
                Intent intent = new Intent(FloatingService.this.getApplicationContext(), ExplainerVideoActivity.class);
                intent.addFlags(268468224);
                FloatingService.this.startActivity(intent);
                Single.timer(FloatingService.ANIMATION_DURATION, TimeUnit.MILLISECONDS).subscribe((SingleObserver<? super T>) new SingleObserver<Long>() {
                    public void onError(Throwable th) {
                    }

                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onSuccess(Long l) {
                        FloatingService.this.startRecord();
                    }
                });
            } else {
                FloatingService.this.startRecord();
            }
        }
    }

    static class C3310b implements Comparator<Size> {
        C3310b() {
        }

        public int compare(Size size, Size size2) {
            return m14702a(size, size2);
        }

        public int m14702a(Size size, Size size2) {
            int i = ((((long) size.width) * ((long) size.height)) > (((long) size2.width) * ((long) size2.height)) ? 1 : ((((long) size.width) * ((long) size.height)) == (((long) size2.width) * ((long) size2.height)) ? 0 : -1));
            if (i == 0) {
                return 0;
            }
            return i > 0 ? 1 : -1;
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {
        CompareSizesByArea() {
        }

        public int compare(Size size, Size size2) {
            return Long.signum((((long) size.width) * ((long) size.height)) - (((long) size2.width) * ((long) size2.height)));
        }
    }

    /* access modifiers changed from: private */
    public void getFrontAppsListNames() {
    }

    private int m14715b(int i) {
        if (i == 1) {
            return 90;
        }
        if (i != 2) {
            return i != 3 ? 0 : 270;
        }
        return 180;
    }

    private void startGetFrontApps() {
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: private */
    public void startRecord() {
        Log.e("CHECK", "startRecord()");
        switch (this.actionType) {
            case EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
            case EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
            case EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
            case EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                break;
            default:
                NativeAdLoaderPreviewDialog.getInstance().loadAd(this.actionType);
                break;
        }
        this.recordTimeOrientation = 0;
        this.changeOrientation = false;
        if (this.actionType == 1343) {
            this.audioRecorder = new AudioRecorder();
        } else {
            Observable.timer(110, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer<? super T>) new DisposableObserver<Long>() {
                public void onComplete() {
                }

                public void onError(Throwable th) {
                }

                public void onNext(Long l) {
                    try {
                        FloatingService.this.recordTimeOrientation = FloatingService.this.screenOrientation;
                        FloatingService.this.changeOrientation = false;
                        FloatingService.this.startNewRecording2();
                        if (FloatingService.this.actionType == 1342) {
                            if (FloatingService.this.checkCameraPermissionAvailable()) {
                                if (PreferenceHelper.getInstance().getPrefInteractiveRecodingCamera()) {
                                    FloatingService.this.addSurfaceView();
                                }
                            } else if (PreferenceHelper.getInstance().getPrefInteractiveRecodingCamera()) {
                                Toast.makeText(FloatingService.this.getApplicationContext(), "Camera permission required", 1).show();
                                PreferenceHelper.getInstance().setPrefInteractiveRecodingCamera(false);
                            }
                        }
                        if (!PreferenceHelper.getInstance().getPrefRecordBubbleVisibility()) {
                            FloatingService.this.showFloatingRecordingBubble();
                        }
                        try {
                            if (FloatingService.this.checkWriteSettingsPermissionAvailable()) {
                                String str = "show_touches";
                                if (PreferenceHelper.getInstance().getPrefRecordTouchVisibility()) {
                                    System.putInt(FloatingService.this.getContentResolver(), str, 1);
                                } else {
                                    System.putInt(FloatingService.this.getContentResolver(), str, 0);
                                }
                            } else {
                                ActivityCompat.requestPermissions((Activity) FloatingService.this.getApplicationContext(), new String[]{"android.permission.WRITE_SETTINGS"}, 343);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }
    }

    public int getTranslateX(float f, int i) {
        return Double.valueOf(((double) i) * Math.cos(Math.toRadians((double) f))).intValue();
    }

    public int getTranslateY(float f, int i) {
        return Double.valueOf(((double) (i * -1)) * Math.sin(Math.toRadians((double) f))).intValue();
    }

    /* access modifiers changed from: private */
    public void showFloatingRecordingBubble() { &&&&&&&&&
        String str = "first_screen1";
        try {
            this.countTimer = 0;
            this.paramsRecord = this.layoutParamsMain;
            LayoutParams layoutParams = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.backRecord = layoutParams;
            LayoutParams layoutParams2 = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.mWindowParams1 = layoutParams2;
            LayoutParams layoutParams3 = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.mWindowParams2 = layoutParams3;
            LayoutParams layoutParams4 = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.mWindowParams3 = layoutParams4;
            LayoutParams layoutParams5 = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.mWindowParams4 = layoutParams5;
            this.mWindowParams1.gravity = 51;
            this.mWindowParams1.x = 0;
            this.mWindowParams1.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
            this.mWindowParams2.gravity = 51;
            this.mWindowParams2.x = 0;
            this.mWindowParams2.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
            this.mWindowParams3.gravity = 51;
            this.mWindowParams3.x = 0;
            this.mWindowParams3.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
            this.mWindowParams4.gravity = 51;
            this.mWindowParams4.x = 0;
            this.mWindowParams4.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
            this.recordView = LayoutInflater.from(this).inflate(R.layout.lay_record, null);
            this.backView = LayoutInflater.from(this).inflate(R.layout.lay_record_back, null);
            this.backView.setBackgroundColor(0);
            this.backView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    FloatingService.this.backView.setBackgroundColor(0);
                    FloatingService.this.txtTimer.callOnClick();
                }
            });
            this.windowManager.getDefaultDisplay().getSize(this.mDisplaySize);
            this.txtTimer = (TextView) this.recordView.findViewById(R.id.txt_timer);
            this.view1 = LayoutInflater.from(this).inflate(R.layout.lay_float1, null);
            ((ImageView) this.view1.findViewById(R.id.img_option)).setImageResource(R.drawable.pause_recorder_selector);
            this.view1.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FloatingService.this.txtTimer != null) {
                        FloatingService.this.txtTimer.callOnClick();
                    }
                    if (FloatingService.this.view1 != null) {
                        String str = (String) FloatingService.this.view1.findViewById(R.id.img_option).getTag();
                        if (str.equalsIgnoreCase("Pause")) {
                            FloatingService.this.sendBroadcast(new Intent(FloatingService.this, BroadcastReceiverPauseRecord.class));
                        } else if (str.equalsIgnoreCase("Resume")) {
                            FloatingService.this.sendBroadcast(new Intent(FloatingService.this, BroadcastReceiverResumeRecord.class));
                        }
                    }
                }
            });
            this.view1.findViewById(R.id.img_option).setTag("Pause");
            this.view2 = LayoutInflater.from(this).inflate(R.layout.lay_float1, null);
            ((ImageView) this.view2.findViewById(R.id.img_option)).setImageResource(R.drawable.stop_recorder_selector);
            this.view2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    FloatingService.this.sendBroadcast(new Intent(FloatingService.this, BroadcastReceiverStopRecord.class));
                }
            });
            this.view3 = LayoutInflater.from(this).inflate(R.layout.lay_float1, null);
            ((ImageView) this.view3.findViewById(R.id.img_option)).setImageResource(R.drawable.ic_floating_camera_selector);
            this.view3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FloatingService.this.txtTimer != null) {
                        FloatingService.this.txtTimer.callOnClick();
                    }
                    FloatingService.this.takeScreenShot(true);
                }
            });
            this.view4 = LayoutInflater.from(this).inflate(R.layout.lay_float1, null);
            ((ImageView) this.view4.findViewById(R.id.img_option)).setImageResource(R.drawable.ic_pencil_selector);
            this.view4.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FloatingService.this.txtTimer != null) {
                        FloatingService.this.txtTimer.callOnClick();
                    }
                    FloatingService.this.addBrushToolInRecording();
                    FirebaseEventsNewHelper.getInstance().sendDrawWhileRecordingEvent();
                }
            });
            int i = this.actionType;
            if (i == 1343) {
                this.view1.findViewById(R.id.img_option).setVisibility(View.GONE);
                this.view3.findViewById(R.id.img_option).setVisibility(View.GONE);
                this.view4.findViewById(R.id.img_option).setVisibility(View.GONE);
            } else if (i == 1344) {
                this.view4.findViewById(R.id.img_option).setVisibility(View.GONE);
            }
            this.txtTimer.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        int actionMasked = motionEvent.getActionMasked();
                        if (actionMasked != 0) {
                            if (actionMasked != 1) {
                                if (actionMasked != 2) {
                                    if (actionMasked != 3) {
                                    }
                                } else if (!FloatingService.this.mExpanded) {
                                    FloatingService.this.updateWindowPosition(FloatingService.this.mInitialPosition.x + ((int) (motionEvent.getRawX() - FloatingService.this.mInitialDown.x)), FloatingService.this.mInitialPosition.y + ((int) (motionEvent.getRawY() - FloatingService.this.mInitialDown.y)));
                                }
                            }
                            FloatingService.this.windowManager.getDefaultDisplay().getSize(FloatingService.this.mDisplaySize);
                            int[] iArr = new int[2];
                            iArr[0] = FloatingService.this.paramsRecord.x;
                            iArr[1] = FloatingService.this.isRight ? FloatingService.this.mDisplaySize.x - FloatingService.this.recordView.getWidth() : 0;
                            ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                            ofInt.setDuration(300);
                            ofInt.addUpdateListener(new AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                    if (FloatingService.this.paramsRecord != null) {
                                        FloatingService.this.paramsRecord.x = intValue;
                                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.recordView, FloatingService.this.paramsRecord);
                                        if (!FloatingService.this.mExpanded) {
                                            FloatingService.this.mWindowParams1.x = intValue;
                                            FloatingService.this.mWindowParams2.x = intValue;
                                            FloatingService.this.mWindowParams3.x = intValue;
                                            FloatingService.this.mWindowParams4.x = intValue;
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view1, FloatingService.this.mWindowParams1);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view2, FloatingService.this.mWindowParams2);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view3, FloatingService.this.mWindowParams3);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view4, FloatingService.this.mWindowParams4);
                                        }
                                    }
                                }
                            });
                            ofInt.start();
                            if (!FloatingService.this.mExpanded) {
                                FloatingService.this.handler.postDelayed(FloatingService.this.runnable, 600);
                            }
                        } else {
                            FloatingService.this.recordView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            if (FloatingService.this.paramsRecord != null) {
                                FloatingService.this.mInitialPosition.set(FloatingService.this.paramsRecord.x, FloatingService.this.paramsRecord.y);
                                FloatingService.this.mInitialDown.set(motionEvent.getRawX(), motionEvent.getRawY());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            final int dpToPx = dpToPx(150);
            final int dpToPx2 = dpToPx(120);
            this.txtTimer.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    FloatingService.this.backView.setBackgroundColor(0);
                    int height = FloatingService.this.windowManager.getDefaultDisplay().getHeight() - dpToPx;
                    if (FloatingService.this.paramsRecord != null) {
                        float f = -125.0f;
                        float f2 = -160.0f;
                        float f3 = 125.0f;
                        float f4 = 160.0f;
                        if (FloatingService.this.mExpanded) {
                            FloatingService floatingService = FloatingService.this;
                            int translateX = floatingService.getTranslateX(floatingService.isRight ? 160.0f : 20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService2 = FloatingService.this;
                            if (!floatingService2.isRight) {
                                f4 = 20.0f;
                            }
                            int translateY = floatingService2.getTranslateY(f4, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService3 = FloatingService.this;
                            int translateX2 = floatingService3.getTranslateX(floatingService3.isRight ? 125.0f : 60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService4 = FloatingService.this;
                            if (!floatingService4.isRight) {
                                f3 = 60.0f;
                            }
                            int translateY2 = floatingService4.getTranslateY(f3, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService5 = FloatingService.this;
                            int translateX3 = floatingService5.getTranslateX(floatingService5.isRight ? -160.0f : -20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService6 = FloatingService.this;
                            if (!floatingService6.isRight) {
                                f2 = -20.0f;
                            }
                            int translateY3 = floatingService6.getTranslateY(f2, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService7 = FloatingService.this;
                            int translateX4 = floatingService7.getTranslateX(floatingService7.isRight ? -125.0f : -60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService8 = FloatingService.this;
                            if (!floatingService8.isRight) {
                                f = -60.0f;
                            }
                            int translateY4 = floatingService8.getTranslateY(f, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService9 = FloatingService.this;
                            floatingService9.showAnimation(1, (float) floatingService9.paramsRecord.x, (float) FloatingService.this.paramsRecord.y, (float) translateX, (float) translateY);
                            FloatingService floatingService10 = FloatingService.this;
                            floatingService10.showAnimation(2, (float) floatingService10.paramsRecord.x, (float) FloatingService.this.paramsRecord.y, (float) translateX2, (float) translateY2);
                            FloatingService floatingService11 = FloatingService.this;
                            floatingService11.showAnimation(3, (float) floatingService11.paramsRecord.x, (float) FloatingService.this.paramsRecord.y, (float) translateX3, (float) translateY3);
                            FloatingService floatingService12 = FloatingService.this;
                            floatingService12.showAnimation(4, (float) floatingService12.paramsRecord.x, (float) FloatingService.this.paramsRecord.y, (float) translateX4, (float) translateY4);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (FloatingService.this.view1 != null) {
                                        FloatingService.this.view1.setVisibility(View.INVISIBLE);
                                    }
                                    if (FloatingService.this.view2 != null) {
                                        FloatingService.this.view2.setVisibility(View.INVISIBLE);
                                    }
                                    if (FloatingService.this.view3 != null) {
                                        FloatingService.this.view3.setVisibility(View.INVISIBLE);
                                    }
                                    if (FloatingService.this.view4 != null) {
                                        FloatingService.this.view4.setVisibility(View.INVISIBLE);
                                    }
                                    if (FloatingService.this.recordView != null) {
                                        FloatingService.this.recordView.findViewById(R.id.back_lay_record).setBackgroundResource(R.drawable.circle_color_opacity_back);
                                    }
                                    FloatingService.this.mExpanded = false;
                                }
                            }, 250);
                            if (!(FloatingService.this.backRecord == null || FloatingService.this.backView == null)) {
                                FloatingService.this.backRecord.height = 0;
                                FloatingService.this.backRecord.width = 0;
                                FloatingService.this.backView.setBackgroundColor(0);
                                FloatingService.this.windowManager.updateViewLayout(FloatingService.this.backView, FloatingService.this.backRecord);
                            }
                            FloatingService.this.mExpanded = false;
                            if (!(FloatingService.this.textViewPauseHighLightMessage == null || FloatingService.this.textViewPauseHighLightMessage.getWindowToken() == null)) {
                                FloatingService.this.windowManager.removeView(FloatingService.this.textViewPauseHighLightMessage);
                                FloatingService.this.textViewPauseHighLightMessage = null;
                            }
                            if (!(FloatingService.this.textViewStopHighLightMessage == null || FloatingService.this.textViewStopHighLightMessage.getWindowToken() == null)) {
                                FloatingService.this.windowManager.removeView(FloatingService.this.textViewStopHighLightMessage);
                                FloatingService.this.textViewStopHighLightMessage = null;
                            }
                            if (!(FloatingService.this.textViewCameraHighLightMessage == null || FloatingService.this.textViewCameraHighLightMessage.getWindowToken() == null)) {
                                FloatingService.this.windowManager.removeView(FloatingService.this.textViewCameraHighLightMessage);
                                FloatingService.this.textViewCameraHighLightMessage = null;
                            }
                            if (!(FloatingService.this.textViewHighLightMessage == null || FloatingService.this.textViewHighLightMessage.getWindowToken() == null)) {
                                FloatingService.this.windowManager.removeView(FloatingService.this.textViewHighLightMessage);
                                FloatingService.this.textViewHighLightMessage = null;
                            }
                            if (!FloatingService.this.mExpanded) {
                                FloatingService.this.handler.postDelayed(FloatingService.this.runnable, 600);
                            }
                        } else if (FloatingService.this.paramsRecord.y > height || FloatingService.this.paramsRecord.y < dpToPx) {
                            int[] iArr = new int[2];
                            iArr[0] = FloatingService.this.paramsRecord.y;
                            int i = FloatingService.this.paramsRecord.y;
                            int i2 = dpToPx;
                            if (i < i2) {
                                height = i2;
                            }
                            iArr[1] = height;
                            ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                            ofInt.setDuration(300);
                            ofInt.addUpdateListener(new AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                    if (FloatingService.this.paramsRecord != null) {
                                        FloatingService.this.paramsRecord.y = intValue;
                                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.recordView, FloatingService.this.paramsRecord);
                                        if (!FloatingService.this.mExpanded) {
                                            FloatingService.this.mWindowParams1.y = intValue;
                                            FloatingService.this.mWindowParams2.y = intValue;
                                            FloatingService.this.mWindowParams3.y = intValue;
                                            FloatingService.this.mWindowParams4.y = intValue;
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view1, FloatingService.this.mWindowParams1);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view2, FloatingService.this.mWindowParams2);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view3, FloatingService.this.mWindowParams3);
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view4, FloatingService.this.mWindowParams4);
                                        }
                                    }
                                }
                            });
                            ofInt.addListener(new AnimatorListener() {
                                public void onAnimationCancel(Animator animator) {
                                }

                                public void onAnimationRepeat(Animator animator) {
                                }

                                public void onAnimationStart(Animator animator) {
                                }

                                public void onAnimationEnd(Animator animator) {
                                    if (FloatingService.this.paramsRecord != null) {
                                        float f = 160.0f;
                                        int translateX = FloatingService.this.getTranslateX(FloatingService.this.isRight ? 160.0f : 20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                                        FloatingService floatingService = FloatingService.this;
                                        if (!FloatingService.this.isRight) {
                                            f = 20.0f;
                                        }
                                        int translateY = floatingService.getTranslateY(f, dpToPx2) + FloatingService.this.paramsRecord.y;
                                        float f2 = 125.0f;
                                        int translateX2 = FloatingService.this.getTranslateX(FloatingService.this.isRight ? 125.0f : 60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                                        FloatingService floatingService2 = FloatingService.this;
                                        if (!FloatingService.this.isRight) {
                                            f2 = 60.0f;
                                        }
                                        int translateY2 = floatingService2.getTranslateY(f2, dpToPx2) + FloatingService.this.paramsRecord.y;
                                        float f3 = -160.0f;
                                        int translateX3 = FloatingService.this.getTranslateX(FloatingService.this.isRight ? -160.0f : -20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                                        FloatingService floatingService3 = FloatingService.this;
                                        if (!FloatingService.this.isRight) {
                                            f3 = -20.0f;
                                        }
                                        int translateY3 = floatingService3.getTranslateY(f3, dpToPx2) + FloatingService.this.paramsRecord.y;
                                        float f4 = -125.0f;
                                        int translateX4 = FloatingService.this.getTranslateX(FloatingService.this.isRight ? -125.0f : -60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                                        FloatingService floatingService4 = FloatingService.this;
                                        if (!FloatingService.this.isRight) {
                                            f4 = -60.0f;
                                        }
                                        int translateY4 = floatingService4.getTranslateY(f4, dpToPx2) + FloatingService.this.paramsRecord.y;
                                        FloatingService.this.recordView.findViewById(R.id.back_lay_record).setBackgroundResource(R.drawable.circle_color_back);
                                        FloatingService.this.view1.setVisibility(View.VISIBLE);
                                        FloatingService.this.view2.setVisibility(View.VISIBLE);
                                        FloatingService.this.view3.setVisibility(View.VISIBLE);
                                        FloatingService.this.view4.setVisibility(View.VISIBLE);
                                        FloatingService.this.showAnimation(1, (float) translateX, (float) translateY, (float) FloatingService.this.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                                        FloatingService.this.showAnimation(2, (float) translateX2, (float) translateY2, (float) FloatingService.this.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                                        FloatingService.this.showAnimation(3, (float) translateX3, (float) translateY3, (float) FloatingService.this.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                                        FloatingService.this.showAnimation(4, (float) translateX4, (float) translateY4, (float) FloatingService.this.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                                        FloatingService.this.backRecord.height = -1;
                                        FloatingService.this.backRecord.width = -1;
                                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.backView, FloatingService.this.backRecord);
                                        FloatingService.this.mExpanded = true;
                                    }
                                }
                            });
                            ofInt.start();
                        } else {
                            FloatingService floatingService13 = FloatingService.this;
                            int translateX5 = floatingService13.getTranslateX(floatingService13.isRight ? 160.0f : 20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService14 = FloatingService.this;
                            if (!floatingService14.isRight) {
                                f4 = 20.0f;
                            }
                            int translateY5 = floatingService14.getTranslateY(f4, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService15 = FloatingService.this;
                            int translateX6 = floatingService15.getTranslateX(floatingService15.isRight ? 125.0f : 60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService16 = FloatingService.this;
                            if (!floatingService16.isRight) {
                                f3 = 60.0f;
                            }
                            int translateY6 = floatingService16.getTranslateY(f3, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService17 = FloatingService.this;
                            int translateX7 = floatingService17.getTranslateX(floatingService17.isRight ? -160.0f : -20.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService18 = FloatingService.this;
                            if (!floatingService18.isRight) {
                                f2 = -20.0f;
                            }
                            int translateY7 = floatingService18.getTranslateY(f2, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService floatingService19 = FloatingService.this;
                            int translateX8 = floatingService19.getTranslateX(floatingService19.isRight ? -125.0f : -60.0f, dpToPx2) + FloatingService.this.paramsRecord.x;
                            FloatingService floatingService20 = FloatingService.this;
                            if (!floatingService20.isRight) {
                                f = -60.0f;
                            }
                            int translateY8 = floatingService20.getTranslateY(f, dpToPx2) + FloatingService.this.paramsRecord.y;
                            FloatingService.this.recordView.findViewById(R.id.back_lay_record).setBackgroundResource(R.drawable.circle_color_back);
                            FloatingService.this.view1.setVisibility(View.VISIBLE);
                            FloatingService.this.view2.setVisibility(View.VISIBLE);
                            FloatingService.this.view3.setVisibility(View.VISIBLE);
                            FloatingService.this.view4.setVisibility(View.VISIBLE);
                            FloatingService.this.backRecord.height = -1;
                            FloatingService.this.backRecord.width = -1;
                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.backView, FloatingService.this.backRecord);
                            FloatingService floatingService21 = FloatingService.this;
                            floatingService21.showAnimation(1, (float) translateX5, (float) translateY5, (float) floatingService21.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                            FloatingService floatingService22 = FloatingService.this;
                            floatingService22.showAnimation(2, (float) translateX6, (float) translateY6, (float) floatingService22.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                            FloatingService floatingService23 = FloatingService.this;
                            floatingService23.showAnimation(3, (float) translateX7, (float) translateY7, (float) floatingService23.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                            FloatingService floatingService24 = FloatingService.this;
                            floatingService24.showAnimation(4, (float) translateX8, (float) translateY8, (float) floatingService24.paramsRecord.x, (float) FloatingService.this.paramsRecord.y);
                            FloatingService.this.mExpanded = true;
                        }
                    }
                }
            });
            try {
                this.windowManager.addView(this.backView, this.backRecord);
                this.windowManager.addView(this.view1, this.mWindowParams1);
                this.windowManager.addView(this.view2, this.mWindowParams2);
                this.windowManager.addView(this.view3, this.mWindowParams3);
                this.windowManager.addView(this.view4, this.mWindowParams4);
                this.windowManager.addView(this.recordView, this.paramsRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.view1.setVisibility(View.INVISIBLE);
            this.view2.setVisibility(View.INVISIBLE);
            this.view3.setVisibility(View.INVISIBLE);
            this.view4.setVisibility(View.INVISIBLE);
            this.countTimer = 0;
            this.handler.removeCallbacks(this.removeCallbacks);
            this.handler.post(this.removeCallbacks);
            this.mExpanded = false;
            if (!this.mExpanded) {
                this.handler.postDelayed(this.runnable, 600);
            }
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
            if (sharedPreferences.contains(str) && !sharedPreferences.getBoolean(str, false)) {
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void addBrushToolInRecording() {
        if (this.windowManager != null) {
            LayoutParams layoutParams = new LayoutParams(-1, -1, getTypeOfWindowManagerParam(), 264, -3);
            final View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_draw_on_recording, null, false);
            final DrawingView   drawingView = (DrawingView) inflate.findViewById(R.id.id_recording_drawing_view);
            ImageButton imageButton = (ImageButton) inflate.findViewById(R.id.id_draw_on_recording_pencil_button);
            ImageButton imageButton2 = (ImageButton) inflate.findViewById(R.id.id_draw_on_recording_erase_button);
            final ColorSeekBar colorSeekBar = (ColorSeekBar) inflate.findViewById(R.id.id_draw_on_rec_brush_color_seek_bar);
            final SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.id_draw_on_rec_brush_size_seek_bar);
            ImageButton imageButton3 = (ImageButton) inflate.findViewById(R.id.id_draw_on_rec_close_seek_bar_view);
            inflate.findViewById(R.id.id_draw_on_recording_close_button).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FloatingService.this.windowManager != null && inflate != null) {
                        FloatingService.this.windowManager.removeView(inflate);
                    }
                }
            });
            final ImageButton imageButton4 = imageButton;
            final ImageButton imageButton5 = imageButton3;
            final ImageButton imageButton6 = imageButton2;
            final DrawingView drawingView2 = drawingView;
            OnClickListener r0 = new OnClickListener() {
                public void onClick(View view) {
                    imageButton4.setSelected(false);
                    imageButton5.callOnClick();
                    imageButton6.setSelected(true);
                    drawingView2.initializeEraser();
                }
            };
            imageButton2.setOnClickListener(r0);
            final ImageButton imageButton7 = imageButton2;
            final DrawingView drawingView3 = drawingView;
            final ColorSeekBar colorSeekBar2 = colorSeekBar;
            final View view = inflate;
            final ImageButton imageButton8 = imageButton;
            OnClickListener r02 = new OnClickListener() {
                public void onClick(View view) {
                    imageButton7.setSelected(false);
                    drawingView3.initializePen();
                    drawingView3.setPenColor(colorSeekBar2.getColor());
                    if (view.findViewById(R.id.id_draw_on_rec_seek_bar_container).getVisibility() == 8) {
                        view.findViewById(R.id.id_draw_on_rec_seek_bar_container).setVisibility(0);
                        imageButton8.setSelected(true);
                        return;
                    }
                    view.findViewById(R.id.id_draw_on_rec_seek_bar_container).setVisibility(8);
                }
            };
            imageButton.setOnClickListener(r02);
            imageButton3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (inflate.findViewById(R.id.id_draw_on_rec_seek_bar_container).getVisibility() == 0) {
                        inflate.findViewById(R.id.id_draw_on_rec_seek_bar_container).setVisibility(8);
                    }
                }
            });
            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    if (i == 0) {
                        seekBar.setProgress(1);
                        return;
                    }
                    float f = (float) i;
                    drawingView.setEraserSize(f);
                    drawingView.setPenSize(f);
                    drawingView.setPenColor(colorSeekBar.getColor());
                }
            });
            colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                public void onColorChangeListener(int i, int i2, int i3) {
                    drawingView.setPenColor(colorSeekBar.getColor());
                    seekBar.getThumb().setColorFilter(colorSeekBar.getColor(), Mode.SRC_ATOP);
                    seekBar.getProgressDrawable().setColorFilter(colorSeekBar.getColor(), Mode.SRC_ATOP);
                }
            });
            seekBar.setProgress(15);
            imageButton.setSelected(true);
            colorSeekBar.setColor(-1);
            this.windowManager.addView(inflate, layoutParams);
        }
    }

    /* access modifiers changed from: private */
    public int dpToPx(int i) {
        return Math.round(((float) i) * (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    /* access modifiers changed from: private */
    public void showAnimation(final int paramInt, float f, float f2, float f3, float f4) {
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("posX", new float[]{f3, f}), PropertyValuesHolder.ofFloat("posY", new float[]{f4, f2})});
        ofPropertyValuesHolder.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue("posX")).floatValue();
                float floatValue2 = ((Float) valueAnimator.getAnimatedValue("posY")).floatValue();
                int i = paramInt;
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i != 4) {
                                return;
                            }
                        } else if (!(FloatingService.this.mWindowParams3 == null || FloatingService.this.view3 == null)) {
                            FloatingService.this.mWindowParams3.x = (int) floatValue;
                            FloatingService.this.mWindowParams3.y = (int) floatValue2;
                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view3, FloatingService.this.mWindowParams3);
                        }
                        if (FloatingService.this.mWindowParams4 != null && FloatingService.this.view4 != null) {
                            FloatingService.this.mWindowParams4.x = (int) floatValue;
                            FloatingService.this.mWindowParams4.y = (int) floatValue2;
                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view4, FloatingService.this.mWindowParams4);
                        }
                    } else if (FloatingService.this.mWindowParams2 != null && FloatingService.this.view2 != null) {
                        FloatingService.this.mWindowParams2.x = (int) floatValue;
                        FloatingService.this.mWindowParams2.y = (int) floatValue2;
                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view2, FloatingService.this.mWindowParams2);
                    }
                } else if (FloatingService.this.mWindowParams1 != null && FloatingService.this.view1 != null) {
                    FloatingService.this.mWindowParams1.x = (int) floatValue;
                    FloatingService.this.mWindowParams1.y = (int) floatValue2;
                    FloatingService.this.windowManager.updateViewLayout(FloatingService.this.view1, FloatingService.this.mWindowParams1);
                }
            }
        });
        ofPropertyValuesHolder.setDuration(100);
        ofPropertyValuesHolder.start();
    }

    /* access modifiers changed from: private */
    public void updateWindowPosition(int i, int i2) {
        this.isRight = i > this.mDisplaySize.x / 2;
        LayoutParams layoutParams = this.paramsRecord;
        layoutParams.x = i;
        layoutParams.y = i2;
        LayoutParams layoutParams2 = this.mWindowParams1;
        layoutParams2.x = i;
        layoutParams2.y = i2;
        LayoutParams layoutParams3 = this.mWindowParams2;
        layoutParams3.x = i;
        layoutParams3.y = i2;
        LayoutParams layoutParams4 = this.mWindowParams3;
        layoutParams4.x = i;
        layoutParams4.y = i2;
        LayoutParams layoutParams5 = this.mWindowParams4;
        layoutParams5.x = i;
        layoutParams5.y = i2;
        this.windowManager.updateViewLayout(this.recordView, layoutParams);
        this.windowManager.updateViewLayout(this.view1, this.mWindowParams1);
        this.windowManager.updateViewLayout(this.view2, this.mWindowParams2);
        this.windowManager.updateViewLayout(this.view3, this.mWindowParams3);
        this.windowManager.updateViewLayout(this.view4, this.mWindowParams4);
    }

    private String getStreamUrlFromLiveData() throws Exception {
        switch (this.actionType) {
            case EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
            case EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                if (!TextUtils.isEmpty(this.facebookLiveStreamJsonData)) {
                    JSONObject jSONObject = new JSONObject(this.facebookLiveStreamJsonData);
                    String str = "stream_url";
                    if (jSONObject.has(str)) {
                        return jSONObject.getString(str);
                    }
                }
                break;
            case EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                LiveTwitchFinalModel liveTwitchFinalModel = this.twitchLiveData;
                if (!(liveTwitchFinalModel == null || liveTwitchFinalModel.getChannelOutputModel() == null)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("rtmp://live-sin.twitch.tv/app/");
                    sb.append(this.twitchLiveData.getChannelOutputModel().getStreamKey().trim());
                    return sb.toString();
                }
            case EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                if (this.youtubeFinalModel != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("rtmp://a.rtmp.youtube.com/live2/");
                    sb2.append(this.youtubeFinalModel.getStreamCDNTitle());
                    return sb2.toString();
                }
                break;
        }
        return null;
    }

    private String getFacbookVideoIDFromLiveData() throws Exception {
        if (!TextUtils.isEmpty(this.facebookLiveStreamJsonData)) {
            int i = this.actionType;
            if (i == 1347 || i == 1350) {
                JSONObject jSONObject = new JSONObject(this.facebookLiveStreamJsonData);
                String str = "id";
                if (jSONObject.has(str)) {
                    return jSONObject.getString(str);
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(24:0|(4:1|2|(1:4)|5)|6|8|9|10|11|12|13|14|(1:16)(1:17)|18|(2:19|20)|25|26|(3:28|29|(1:31)(2:32|(1:34)(1:(1:36)(1:37))))(2:38|(1:40)(1:41))|42|(2:44|(1:46))(1:47)|48|(1:50)(1:51)|52|(1:55)|56|(2:58|72)(1:71)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x003a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startNewRecording2() {
        /*
            r22 = this;
            r14 = r22
            r22.setResolution()
            r15 = 0
            r14.pausedOnce = r15
            r13 = 0
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer     // Catch:{ Exception -> 0x0014 }
            if (r0 == 0) goto L_0x0012
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer     // Catch:{ Exception -> 0x0014 }
            r0.stopRecording()     // Catch:{ Exception -> 0x0014 }
        L_0x0012:
            r14.sMuxer = r13     // Catch:{ Exception -> 0x0014 }
        L_0x0014:
            r12 = 1
            r14.audioAddedRecorderType2 = r15     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r0 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.util.DisplayMetrics r1 = r0.getDisplayMetrics()     // Catch:{ Exception -> 0x0350 }
            int r0 = r14.screenOrientation     // Catch:{ Exception -> 0x0350 }
            r14.recordTimeOrientation = r0     // Catch:{ Exception -> 0x0350 }
            r14.changeOrientation = r15     // Catch:{ Exception -> 0x0350 }
            java.lang.String r0 = r22.createVideoFile()     // Catch:{ Exception -> 0x0350 }
            r14.mCurrentVideoPath = r0     // Catch:{ Exception -> 0x0350 }
            com.ezscreenrecorder.utils.PreferenceHelper r0 = com.ezscreenrecorder.utils.PreferenceHelper.getInstance()     // Catch:{ Exception -> 0x0350 }
            java.lang.String r11 = r0.getPrefRecordingOrientation()     // Catch:{ Exception -> 0x0350 }
            java.lang.String r0 = r22.getStreamUrlFromLiveData()     // Catch:{ Exception -> 0x003a }
            r14.live_url = r0     // Catch:{ Exception -> 0x003a }
            goto L_0x003c
        L_0x003a:
            r14.live_url = r13     // Catch:{ Exception -> 0x0350 }
        L_0x003c:
            java.lang.String r0 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0350 }
            if (r0 == 0) goto L_0x0047
            r14.localRecordWithLive = r12     // Catch:{ Exception -> 0x0350 }
            goto L_0x0049
        L_0x0047:
            r14.localRecordWithLive = r15     // Catch:{ Exception -> 0x0350 }
        L_0x0049:
            com.appsmartz.recorder.MediaMuxerWrapper r0 = new com.appsmartz.recorder.MediaMuxerWrapper     // Catch:{ Exception -> 0x0350 }
            java.lang.String r2 = r14.mCurrentVideoPath     // Catch:{ Exception -> 0x0350 }
            boolean r3 = r14.localRecordWithLive     // Catch:{ Exception -> 0x0350 }
            java.lang.String r4 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r0.<init>(r2, r3, r4)     // Catch:{ Exception -> 0x0350 }
            r14.sMuxer = r0     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.ezscreenrecorder.FloatingService$22 r2 = new com.ezscreenrecorder.FloatingService$22     // Catch:{ Exception -> 0x0350 }
            r2.<init>()     // Catch:{ Exception -> 0x0350 }
            r0.setMediaMuxerInterface(r2)     // Catch:{ Exception -> 0x0350 }
            boolean r0 = r22.m14ac()     // Catch:{ Throwable -> 0x0065 }
            goto L_0x006b
        L_0x0065:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()     // Catch:{ Exception -> 0x0350 }
            r0 = 1
        L_0x006b:
            com.ezscreenrecorder.FloatingService$23 r2 = new com.ezscreenrecorder.FloatingService$23     // Catch:{ Exception -> 0x0350 }
            r2.<init>()     // Catch:{ Exception -> 0x0350 }
            r14.mMediaEncoderListener = r2     // Catch:{ Exception -> 0x0350 }
            java.lang.String r2 = "Auto"
            boolean r2 = r11.equalsIgnoreCase(r2)     // Catch:{ Exception -> 0x0350 }
            r3 = 30
            java.lang.String r4 = "example_list_frame_rate"
            r5 = 1000000(0xf4240, float:1.401298E-39)
            java.lang.String r6 = "example_list_bit_rate"
            if (r2 == 0) goto L_0x01ee
            java.lang.String r2 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            if (r2 == 0) goto L_0x00e5
            com.appsmartz.recorder.MediaScreenEncoder r9 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r16 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r17 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r1 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r10 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r10.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r10 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r5 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = r5.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r19 = java.lang.Integer.parseInt(r3)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r3 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r3 = r3.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r3.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r20 = r1
            r1 = r9
            r3 = r7
            r4 = r8
            r21 = r5
            r5 = r16
            r16 = r6
            r6 = r17
            r7 = r20
            r8 = r10
            r10 = r9
            r9 = r19
            r18 = r0
            r15 = r10
            r0 = 2
            r10 = r16
            r16 = r11
            r11 = r22
            r12 = r16
            r13 = r21
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
            goto L_0x029a
        L_0x00e5:
            r18 = r0
            r16 = r11
            r0 = 2
            android.content.res.Resources r2 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r2 = r2.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r2 = r2.screenOrientation     // Catch:{ Exception -> 0x0350 }
            r15 = 1
            if (r2 != r15) goto L_0x014a
            com.appsmartz.recorder.MediaScreenEncoder r13 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r9 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r10 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r11 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r12 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r19 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r1 = r13
            r3 = r7
            r4 = r8
            r20 = r5
            r5 = r9
            r21 = r6
            r6 = r10
            r7 = r11
            r8 = r12
            r9 = r19
            r10 = r21
            r11 = r22
            r12 = r16
            r15 = r13
            r13 = r20
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
            goto L_0x029a
        L_0x014a:
            if (r2 != r0) goto L_0x019d
            com.appsmartz.recorder.MediaScreenEncoder r15 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r9 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r10 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r11 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r12 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r13 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r1 = r15
            r3 = r7
            r4 = r8
            r19 = r5
            r5 = r9
            r20 = r6
            r6 = r10
            r7 = r11
            r8 = r12
            r9 = r13
            r10 = r20
            r11 = r22
            r12 = r16
            r13 = r19
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
            goto L_0x029a
        L_0x019d:
            com.appsmartz.recorder.MediaScreenEncoder r15 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r9 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r10 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r11 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r12 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r13 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r1 = r15
            r3 = r7
            r4 = r8
            r19 = r5
            r5 = r9
            r20 = r6
            r6 = r10
            r7 = r11
            r8 = r12
            r9 = r13
            r10 = r20
            r11 = r22
            r12 = r16
            r13 = r19
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
            goto L_0x029a
        L_0x01ee:
            r18 = r0
            r16 = r11
            r0 = 2
            java.lang.String r2 = "LandScape"
            r12 = r16
            boolean r2 = r12.equalsIgnoreCase(r2)     // Catch:{ Exception -> 0x0350 }
            if (r2 == 0) goto L_0x024c
            com.appsmartz.recorder.MediaScreenEncoder r15 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r9 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r10 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r11 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r13 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r16 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r1 = r15
            r3 = r7
            r4 = r8
            r19 = r5
            r5 = r9
            r20 = r6
            r6 = r10
            r7 = r11
            r8 = r13
            r9 = r16
            r10 = r20
            r11 = r22
            r13 = r19
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
            goto L_0x029a
        L_0x024c:
            com.appsmartz.recorder.MediaScreenEncoder r15 = new com.appsmartz.recorder.MediaScreenEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r2 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r7 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            android.media.projection.MediaProjection r8 = mMediaProjection     // Catch:{ Exception -> 0x0350 }
            int r9 = DISPLAY_WIDTH     // Catch:{ Exception -> 0x0350 }
            int r10 = DISPLAY_HEIGHT     // Catch:{ Exception -> 0x0350 }
            int r11 = r1.densityDpi     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r6, r5)     // Catch:{ Exception -> 0x0350 }
            int r13 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r1 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getString(r4, r3)     // Catch:{ Exception -> 0x0350 }
            int r16 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r6 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            java.lang.String r5 = r14.live_url     // Catch:{ Exception -> 0x0350 }
            r1 = r15
            r3 = r7
            r4 = r8
            r19 = r5
            r5 = r9
            r20 = r6
            r6 = r10
            r7 = r11
            r8 = r13
            r9 = r16
            r10 = r20
            r11 = r22
            r13 = r19
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x0350 }
            r14.mediaScreenRecorder = r15     // Catch:{ Exception -> 0x0350 }
        L_0x029a:
            com.ezscreenrecorder.utils.PreferenceHelper r1 = com.ezscreenrecorder.utils.PreferenceHelper.getInstance()     // Catch:{ Exception -> 0x0350 }
            boolean r1 = r1.getPrefWatermarkVisibility()     // Catch:{ Exception -> 0x0350 }
            if (r1 == 0) goto L_0x02d1
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0350 }
            android.content.Context r2 = r22.getApplicationContext()     // Catch:{ Exception -> 0x0350 }
            java.lang.String r2 = com.ezscreenrecorder.utils.AppUtils.getVideoWatermarkDir(r2)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r3 = "watermark.png"
            r1.<init>(r2, r3)     // Catch:{ Exception -> 0x0350 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0350 }
            if (r2 == 0) goto L_0x02de
            android.graphics.BitmapFactory$Options r2 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x0350 }
            r2.<init>()     // Catch:{ Exception -> 0x0350 }
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ Exception -> 0x0350 }
            r2.inPreferredConfig = r3     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ Exception -> 0x0350 }
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeFile(r1, r2)     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaScreenEncoder r2 = r14.mediaScreenRecorder     // Catch:{ Exception -> 0x0350 }
            r2.setWatermark(r1)     // Catch:{ Exception -> 0x0350 }
            goto L_0x02de
        L_0x02d1:
            android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ Exception -> 0x0350 }
            r2 = 10
            android.graphics.Bitmap r1 = android.graphics.Bitmap.createBitmap(r2, r2, r1)     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaScreenEncoder r2 = r14.mediaScreenRecorder     // Catch:{ Exception -> 0x0350 }
            r2.setWatermark(r1)     // Catch:{ Exception -> 0x0350 }
        L_0x02de:
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch:{ Exception -> 0x0350 }
            int r1 = r1.screenOrientation     // Catch:{ Exception -> 0x0350 }
            if (r1 != r0) goto L_0x02fb
            com.appsmartz.recorder.MediaScreenEncoder r0 = r14.mediaScreenRecorder     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            r2 = 2131231078(0x7f080166, float:1.8078227E38)
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeResource(r1, r2)     // Catch:{ Exception -> 0x0350 }
            r0.setPauseWatermark(r1)     // Catch:{ Exception -> 0x0350 }
            goto L_0x030b
        L_0x02fb:
            com.appsmartz.recorder.MediaScreenEncoder r0 = r14.mediaScreenRecorder     // Catch:{ Exception -> 0x0350 }
            android.content.res.Resources r1 = r22.getResources()     // Catch:{ Exception -> 0x0350 }
            r2 = 2131231079(0x7f080167, float:1.8078229E38)
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeResource(r1, r2)     // Catch:{ Exception -> 0x0350 }
            r0.setPauseWatermark(r1)     // Catch:{ Exception -> 0x0350 }
        L_0x030b:
            com.ezscreenrecorder.utils.PreferenceHelper r0 = com.ezscreenrecorder.utils.PreferenceHelper.getInstance()     // Catch:{ Exception -> 0x0350 }
            boolean r0 = r0.getPrefRecordAudio()     // Catch:{ Exception -> 0x0350 }
            if (r0 == 0) goto L_0x0323
            if (r18 == 0) goto L_0x0323
            r1 = 1
            r14.audioAddedRecorderType2 = r1     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaAudioEncoder r0 = new com.appsmartz.recorder.MediaAudioEncoder     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r1 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaEncoder$MediaEncoderListener r2 = r14.mMediaEncoderListener     // Catch:{ Exception -> 0x0350 }
            r0.<init>(r1, r2)     // Catch:{ Exception -> 0x0350 }
        L_0x0323:
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            r0.prepare()     // Catch:{ Exception -> 0x0350 }
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer     // Catch:{ Exception -> 0x0350 }
            r0.startRecording()     // Catch:{ Exception -> 0x0350 }
            r1 = 1
            r14.showNotification(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.SharedPreferences r0 = r14.prefs     // Catch:{ Exception -> 0x0350 }
            java.lang.String r2 = "notifications_screen_off"
            boolean r0 = r0.getBoolean(r2, r1)     // Catch:{ Exception -> 0x0350 }
            if (r0 == 0) goto L_0x0386
            android.content.IntentFilter r0 = new android.content.IntentFilter     // Catch:{ Exception -> 0x0350 }
            r0.<init>()     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = "android.intent.action.SCREEN_ON"
            r0.addAction(r1)     // Catch:{ Exception -> 0x0350 }
            java.lang.String r1 = "android.intent.action.SCREEN_OFF"
            r0.addAction(r1)     // Catch:{ Exception -> 0x0350 }
            android.content.BroadcastReceiver r1 = r14.broadcastReceiverOnOffScreen     // Catch:{ Exception -> 0x0350 }
            r14.registerReceiver(r1, r0)     // Catch:{ Exception -> 0x0350 }
            goto L_0x0386
        L_0x0350:
            r0 = move-exception
            r0.printStackTrace()
            android.content.Context r0 = r22.getApplicationContext()
            r1 = 2131689904(0x7f0f01b0, float:1.9008837E38)
            java.lang.String r1 = r14.getString(r1)
            r2 = 1
            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r1, r2)
            r0.show()
            android.view.View r0 = r14.floatingView
            if (r0 == 0) goto L_0x036f
            r1 = 0
            r0.setVisibility(r1)
        L_0x036f:
            com.appsmartz.recorder.MediaMuxerWrapper r0 = r14.sMuxer
            if (r0 == 0) goto L_0x0376
            r0.stopRecording()
        L_0x0376:
            android.media.projection.MediaProjection r0 = mMediaProjection
            if (r0 == 0) goto L_0x037d
            r0.stop()
        L_0x037d:
            r1 = 0
            r14.sMuxer = r1
            r14.mediaScreenRecorder = r1
            mMediaProjection = r1
            r14.mMediaRecorder = r1
        L_0x0386:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.startNewRecording2():void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
        r3.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0040, code lost:
        r3.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[ExcHandler: Exception (unused java.lang.Exception), SYNTHETIC, Splitter:B:1:0x0008] */
    /* renamed from: ac */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean m14ac() throws Throwable {
        /*
            r11 = this;
            r0 = 44100(0xac44, float:6.1797E-41)
            r1 = 16
            r2 = 2
            r3 = 0
            r4 = 0
            int r0 = android.media.AudioRecord.getMinBufferSize(r0, r1, r2)     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            android.media.AudioRecord r1 = new android.media.AudioRecord     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            r6 = 1
            r7 = 44100(0xac44, float:6.1797E-41)
            r8 = 16
            r9 = 2
            r5 = r1
            r10 = r0
            r5.<init>(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0040, Throwable -> 0x0039 }
            r1.startRecording()     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            short[] r2 = new short[r0]     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            int r0 = r1.read(r2, r4, r0)     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            r2 = -3
            if (r0 == r2) goto L_0x002b
            if (r0 != 0) goto L_0x0029
            goto L_0x002b
        L_0x0029:
            r0 = 1
            goto L_0x002c
        L_0x002b:
            r0 = 0
        L_0x002c:
            r1.release()     // Catch:{ Exception -> 0x0038, Throwable -> 0x0030 }
            return r0
        L_0x0030:
            r0 = move-exception
            r1.release()     // Catch:{ Exception -> 0x0040, Throwable -> 0x0035 }
            throw r0     // Catch:{ Exception -> 0x0040, Throwable -> 0x0035 }
        L_0x0035:
            r0 = move-exception
            r3 = r1
            goto L_0x003a
        L_0x0038:
            return r4
        L_0x0039:
            r0 = move-exception
        L_0x003a:
            if (r3 == 0) goto L_0x003f
            r3.release()
        L_0x003f:
            throw r0
        L_0x0040:
            r3.release()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.m14ac():boolean");
    }

    private int getResolutionType() {
        switch (this.actionType) {
            case EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
            case EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
            case EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
            case EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                return Constants.TYPE_PREF_RESOLUTION_RECORDING;
            case EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
                return Constants.TYPE_PREF_RESOLUTION_FACEBOOK;
            case EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                return Constants.TYPE_PREF_RESOLUTION_TWITCH;
            case EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                return Constants.TYPE_PREF_RESOLUTION_YOUTUBE;
            default:
                return Constants.TYPE_PREF_RESOLUTION_RECORDING;
        }
    }

    private void setResolution() {
        int i;
        String str = "x";
        if (!PreferenceHelper.getInstance().hasPrefResolution(getResolutionType())) {
            String[] stringArray = getResources().getStringArray(R.array.pref_resolution_list_titles);
            Point point = new Point();
            this.windowManager.getDefaultDisplay().getRealSize(point);
            int i2 = point.y;
            int i3 = point.x;
            ArrayList arrayList = new ArrayList();
            for (String str2 : stringArray) {
                String str3 = str2.split(str)[0];
                String str4 = str2.split(str)[1];
                if (i2 > i3) {
                    if (Integer.parseInt(str3) <= i2 && Integer.parseInt(str4) <= i3) {
                        arrayList.add(str2);
                    }
                } else if (Integer.parseInt(str4) <= i2 && Integer.parseInt(str3) <= i3) {
                    arrayList.add(str2);
                }
            }
            String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
            if (strArr.length > 0) {
                PreferenceHelper.getInstance().setPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING, strArr[0]);
            }
        }
        String prefResolution = PreferenceHelper.getInstance().getPrefResolution(getResolutionType());
        if (prefResolution.contains(str)) {
            i = Integer.parseInt(prefResolution.split(str)[0]);
        } else {
            i = Integer.parseInt(prefResolution);
        }
        if (i == 426) {
            DISPLAY_HEIGHT = 426;
            DISPLAY_WIDTH = 240;
        } else if (i == 640) {
            DISPLAY_HEIGHT = 640;
            DISPLAY_WIDTH = 360;
        } else if (i == 854) {
            DISPLAY_HEIGHT = 854;
            DISPLAY_WIDTH = 480;
        } else if (i == 1024) {
            DISPLAY_HEIGHT = 1024;
            DISPLAY_WIDTH = SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT;
        } else if (i == 1280) {
            DISPLAY_HEIGHT = 1280;
            DISPLAY_WIDTH = 720;
        } else if (i == 1920) {
            DISPLAY_HEIGHT = 1920;
            DISPLAY_WIDTH = 1080;
        } else if (i == 2048) {
            DISPLAY_HEIGHT = 2048;
            DISPLAY_WIDTH = 1536;
        } else if (i == 2220) {
            DISPLAY_HEIGHT = 2220;
            DISPLAY_WIDTH = 1080;
        } else if (i == 2560) {
            DISPLAY_HEIGHT = 2560;
            DISPLAY_WIDTH = 1440;
        } else if (i == 2960) {
            DISPLAY_HEIGHT = 2960;
            DISPLAY_WIDTH = 1440;
        }
    }

    /* access modifiers changed from: private */
    public void takeScreenShot(final boolean z) {
        Log.e("CHECK", "takeScreenShot()");
        NativeAdLoaderPreviewDialog.getInstance().loadAd(this.actionType);
        startGetFrontApps();
        Observable.timer(700, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).flatMap(new Function<Long, ObservableSource<Boolean>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public ObservableSource<Boolean> apply(Long l) throws Exception {
                Log.e("Screen Recorder", "Screenshot Starts");
                FloatingService.this.mImageReader = ImageReader.newInstance(FloatingService.DISPLAY_WIDTH, FloatingService.DISPLAY_HEIGHT, 1, 1);
                int i = FloatingService.this.getResources().getDisplayMetrics().densityDpi;
                FloatingService.this.mVirtualDisplay = FloatingService.mMediaProjection.createVirtualDisplay("screencap", FloatingService.DISPLAY_WIDTH, FloatingService.DISPLAY_HEIGHT, i, 16, FloatingService.this.mImageReader.getSurface(), null, null);
                FloatingService.this.mImageReader.setOnImageAvailableListener(new OnImageAvailableListener() {
                    boolean isCaptured;
                    String outputName;

                    /* JADX WARNING: Removed duplicated region for block: B:101:0x0258 A[Catch:{ all -> 0x029e }] */
                    /* JADX WARNING: Removed duplicated region for block: B:106:0x027b A[SYNTHETIC, Splitter:B:106:0x027b] */
                    /* JADX WARNING: Removed duplicated region for block: B:113:0x0286 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:115:0x028b A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:118:0x0294 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:121:0x02a3 A[SYNTHETIC, Splitter:B:121:0x02a3] */
                    /* JADX WARNING: Removed duplicated region for block: B:128:0x02ae A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:130:0x02b3 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:133:0x02bc A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:142:? A[RETURN, SYNTHETIC] */
                    /* JADX WARNING: Removed duplicated region for block: B:143:? A[RETURN, SYNTHETIC] */
                    /* JADX WARNING: Removed duplicated region for block: B:144:? A[RETURN, SYNTHETIC] */
                    /* JADX WARNING: Removed duplicated region for block: B:60:0x01da A[SYNTHETIC, Splitter:B:60:0x01da] */
                    /* JADX WARNING: Removed duplicated region for block: B:67:0x01e5 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:69:0x01ea A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:72:0x01f3 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:78:0x0207 A[Catch:{ all -> 0x029e }] */
                    /* JADX WARNING: Removed duplicated region for block: B:83:0x022a A[SYNTHETIC, Splitter:B:83:0x022a] */
                    /* JADX WARNING: Removed duplicated region for block: B:90:0x0235 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:92:0x023a A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Removed duplicated region for block: B:95:0x0243 A[Catch:{ Exception -> 0x02c8 }] */
                    /* JADX WARNING: Unknown top exception splitter block from list: {B:75:0x01fe=Splitter:B:75:0x01fe, B:98:0x024f=Splitter:B:98:0x024f} */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void onImageAvailable(ImageReader r19) {
                        /*
                            r18 = this;
                            r1 = r18
                            r2 = 2131689860(0x7f0f0184, float:1.9008747E38)
                            r3 = 1
                            boolean r0 = r1.isCaptured     // Catch:{ Exception -> 0x02c8 }
                            r4 = 0
                            if (r0 == 0) goto L_0x0030
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.media.ImageReader r0 = r0.mImageReader     // Catch:{ Exception -> 0x02c8 }
                            r0.close()     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x0027
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ Exception -> 0x02c8 }
                            if (r0 == 0) goto L_0x0025
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ Exception -> 0x02c8 }
                            r0.stop()     // Catch:{ Exception -> 0x02c8 }
                        L_0x0025:
                            com.ezscreenrecorder.FloatingService.mMediaProjection = r4     // Catch:{ Exception -> 0x02c8 }
                        L_0x0027:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            r0.getFrontAppsListNames()     // Catch:{ Exception -> 0x02c8 }
                            goto L_0x02d7
                        L_0x0030:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.media.ImageReader r0 = r0.mImageReader     // Catch:{ Exception -> 0x02c8 }
                            android.media.Image r5 = r0.acquireLatestImage()     // Catch:{ Exception -> 0x02c8 }
                            if (r5 == 0) goto L_0x02d7
                            android.media.Image$Plane[] r0 = r5.getPlanes()     // Catch:{ Exception -> 0x02c8 }
                            r6 = 0
                            r7 = r0[r6]     // Catch:{ Exception -> 0x02c8 }
                            java.nio.ByteBuffer r7 = r7.getBuffer()     // Catch:{ Exception -> 0x02c8 }
                            r7.rewind()     // Catch:{ Exception -> 0x02c8 }
                            r8 = r0[r6]     // Catch:{ Exception -> 0x02c8 }
                            int r8 = r8.getPixelStride()     // Catch:{ Exception -> 0x02c8 }
                            r0 = r0[r6]     // Catch:{ Exception -> 0x02c8 }
                            int r0 = r0.getRowStride()     // Catch:{ Exception -> 0x02c8 }
                            int r9 = com.ezscreenrecorder.FloatingService.DISPLAY_WIDTH     // Catch:{ Exception -> 0x02c8 }
                            int r9 = r9 * r8
                            byte[] r9 = new byte[r9]     // Catch:{ Exception -> 0x02c8 }
                            int r10 = com.ezscreenrecorder.FloatingService.DISPLAY_WIDTH     // Catch:{ Exception -> 0x02c8 }
                            int r10 = r10 * r8
                            int r11 = com.ezscreenrecorder.FloatingService.DISPLAY_HEIGHT     // Catch:{ Exception -> 0x02c8 }
                            int r10 = r10 * r11
                            java.nio.ByteBuffer r10 = java.nio.ByteBuffer.allocate(r10)     // Catch:{ Exception -> 0x02c8 }
                            int r11 = com.ezscreenrecorder.FloatingService.DISPLAY_WIDTH     // Catch:{ Exception -> 0x02c8 }
                            int r12 = com.ezscreenrecorder.FloatingService.DISPLAY_HEIGHT     // Catch:{ Exception -> 0x02c8 }
                            android.graphics.Bitmap$Config r13 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ Exception -> 0x02c8 }
                            android.graphics.Bitmap r11 = android.graphics.Bitmap.createBitmap(r11, r12, r13)     // Catch:{ Exception -> 0x02c8 }
                            r1.isCaptured = r3     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r12 = 0
                        L_0x0077:
                            int r13 = com.ezscreenrecorder.FloatingService.DISPLAY_HEIGHT     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            if (r12 >= r13) goto L_0x008d
                            int r13 = com.ezscreenrecorder.FloatingService.DISPLAY_WIDTH     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            int r13 = r13 * r8
                            r7.get(r9, r6, r13)     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            int r12 = r12 + 1
                            int r13 = r12 * r0
                            r7.position(r13)     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r10.put(r9)     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            goto L_0x0077
                        L_0x008d:
                            r10.rewind()     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r11.copyPixelsFromBuffer(r10)     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            java.lang.String r0 = r0.createImageFile()     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r1.outputName = r0     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            boolean r0 = r4     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            if (r0 != 0) goto L_0x00ac
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            java.lang.String r7 = r1.outputName     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r0.mCurrentVideoPath = r7     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                        L_0x00ac:
                            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            java.lang.String r0 = r1.outputName     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            r7.<init>(r0)     // Catch:{ FileNotFoundException -> 0x024d, NameNotFoundException -> 0x01fc, Exception -> 0x01d4 }
                            android.graphics.Bitmap$CompressFormat r0 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r8 = 100
                            r11.compress(r0, r8, r7)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.screenShotInflateView = r4     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            boolean r0 = r0.checkOverlayPermission()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            if (r0 == 0) goto L_0x015d
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r8 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r8 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.LayoutInflater r8 = android.view.LayoutInflater.from(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9 = 2131493066(0x7f0c00ca, float:1.8609602E38)
                            android.view.View r8 = r8.inflate(r9, r4)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.screenShotInflateView = r8     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.View r0 = r0.screenShotInflateView     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r8 = 2131296841(0x7f090249, float:1.821161E38)
                            android.view.View r0 = r0.findViewById(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.widget.ImageView r0 = (android.widget.ImageView) r0     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            java.lang.String r8 = r1.outputName     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.graphics.Bitmap r8 = android.graphics.BitmapFactory.decodeFile(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.setImageBitmap(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.ViewGroup$LayoutParams r9 = r0.getLayoutParams()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.widget.RelativeLayout$LayoutParams r9 = (android.widget.RelativeLayout.LayoutParams) r9     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            int r10 = r8.getWidth()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            float r10 = (float) r10     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r12 = 1061997773(0x3f4ccccd, float:0.8)
                            float r10 = r10 * r12
                            int r10 = (int) r10     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9.width = r10     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            int r8 = r8.getHeight()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            float r8 = (float) r8     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            float r8 = r8 * r12
                            int r8 = (int) r8     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9.height = r8     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r8 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r8 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.WindowManager r8 = r8.windowManager     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r9 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.View r9 = r9.screenShotInflateView     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.WindowManager$LayoutParams r10 = new android.view.WindowManager$LayoutParams     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r13 = -1
                            r14 = -1
                            com.ezscreenrecorder.FloatingService$25 r12 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r12 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            int r15 = r12.getTypeOfWindowManagerParam()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r16 = 264(0x108, float:3.7E-43)
                            r17 = -3
                            r12 = r10
                            r12.<init>(r13, r14, r15, r16, r17)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r8.addView(r9, r10)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.animation.AlphaAnimation r8 = new android.view.animation.AlphaAnimation     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9 = 1065353216(0x3f800000, float:1.0)
                            r10 = 0
                            r8.<init>(r9, r10)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9 = 400(0x190, double:1.976E-321)
                            r8.setDuration(r9)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9 = 300(0x12c, double:1.48E-321)
                            r8.setStartOffset(r9)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25$1$1 r9 = new com.ezscreenrecorder.FloatingService$25$1$1     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9.<init>()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r8.setAnimationListener(r9)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.startAnimation(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            goto L_0x01a1
                        L_0x015d:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            boolean r0 = r0.checkOverlayPermission()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            if (r0 == 0) goto L_0x0192
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.View r0 = r0.screenShotInflateView     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            if (r0 == 0) goto L_0x0192
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.View r0 = r0.screenShotInflateView     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            boolean r0 = r0.isAttachedToWindow()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            if (r0 == 0) goto L_0x0192
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.WindowManager r0 = r0.windowManager     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25 r8 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService r8 = com.ezscreenrecorder.FloatingService.this     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            android.view.View r8 = r8.screenShotInflateView     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.removeView(r8)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                        L_0x0192:
                            android.os.Handler r0 = new android.os.Handler     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r0.<init>()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            com.ezscreenrecorder.FloatingService$25$1$2 r8 = new com.ezscreenrecorder.FloatingService$25$1$2     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r8.<init>()     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                            r9 = 200(0xc8, double:9.9E-322)
                            r0.postDelayed(r8, r9)     // Catch:{ FileNotFoundException -> 0x01cc, NameNotFoundException -> 0x01ca, Exception -> 0x01c7 }
                        L_0x01a1:
                            r7.close()     // Catch:{ IOException -> 0x01a5 }
                            goto L_0x01aa
                        L_0x01a5:
                            r0 = move-exception
                            r4 = r0
                            r4.printStackTrace()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01aa:
                            if (r11 == 0) goto L_0x01af
                            r11.recycle()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01af:
                            if (r5 == 0) goto L_0x01b4
                            r5.close()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01b4:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x02d7
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.view.View r0 = r0.floatingView     // Catch:{ Exception -> 0x02c8 }
                        L_0x01c2:
                            r0.setVisibility(r6)     // Catch:{ Exception -> 0x02c8 }
                            goto L_0x02d7
                        L_0x01c7:
                            r0 = move-exception
                            r4 = r7
                            goto L_0x01d5
                        L_0x01ca:
                            r0 = move-exception
                            goto L_0x01fe
                        L_0x01cc:
                            r0 = move-exception
                            goto L_0x024f
                        L_0x01cf:
                            r0 = move-exception
                            r7 = r4
                        L_0x01d1:
                            r4 = r0
                            goto L_0x02a1
                        L_0x01d4:
                            r0 = move-exception
                        L_0x01d5:
                            r0.printStackTrace()     // Catch:{ all -> 0x01cf }
                            if (r4 == 0) goto L_0x01e3
                            r4.close()     // Catch:{ IOException -> 0x01de }
                            goto L_0x01e3
                        L_0x01de:
                            r0 = move-exception
                            r4 = r0
                            r4.printStackTrace()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01e3:
                            if (r11 == 0) goto L_0x01e8
                            r11.recycle()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01e8:
                            if (r5 == 0) goto L_0x01ed
                            r5.close()     // Catch:{ Exception -> 0x02c8 }
                        L_0x01ed:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x02d7
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.view.View r0 = r0.floatingView     // Catch:{ Exception -> 0x02c8 }
                            goto L_0x01c2
                        L_0x01fc:
                            r0 = move-exception
                            r7 = r4
                        L_0x01fe:
                            r0.printStackTrace()     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            boolean r0 = r4     // Catch:{ all -> 0x029e }
                            if (r0 != 0) goto L_0x0228
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ all -> 0x029e }
                            android.media.ImageReader r0 = r0.mImageReader     // Catch:{ all -> 0x029e }
                            r0.close()     // Catch:{ all -> 0x029e }
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ all -> 0x029e }
                            if (r0 == 0) goto L_0x021b
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ all -> 0x029e }
                            r0.stop()     // Catch:{ all -> 0x029e }
                        L_0x021b:
                            com.ezscreenrecorder.FloatingService.mMediaProjection = r4     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ all -> 0x029e }
                            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r2, r3)     // Catch:{ all -> 0x029e }
                            r0.show()     // Catch:{ all -> 0x029e }
                        L_0x0228:
                            if (r7 == 0) goto L_0x0233
                            r7.close()     // Catch:{ IOException -> 0x022e }
                            goto L_0x0233
                        L_0x022e:
                            r0 = move-exception
                            r4 = r0
                            r4.printStackTrace()     // Catch:{ Exception -> 0x02c8 }
                        L_0x0233:
                            if (r11 == 0) goto L_0x0238
                            r11.recycle()     // Catch:{ Exception -> 0x02c8 }
                        L_0x0238:
                            if (r5 == 0) goto L_0x023d
                            r5.close()     // Catch:{ Exception -> 0x02c8 }
                        L_0x023d:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x02d7
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.view.View r0 = r0.floatingView     // Catch:{ Exception -> 0x02c8 }
                            goto L_0x01c2
                        L_0x024d:
                            r0 = move-exception
                            r7 = r4
                        L_0x024f:
                            r0.printStackTrace()     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            boolean r0 = r4     // Catch:{ all -> 0x029e }
                            if (r0 != 0) goto L_0x0279
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ all -> 0x029e }
                            android.media.ImageReader r0 = r0.mImageReader     // Catch:{ all -> 0x029e }
                            r0.close()     // Catch:{ all -> 0x029e }
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ all -> 0x029e }
                            if (r0 == 0) goto L_0x026c
                            android.media.projection.MediaProjection r0 = com.ezscreenrecorder.FloatingService.mMediaProjection     // Catch:{ all -> 0x029e }
                            r0.stop()     // Catch:{ all -> 0x029e }
                        L_0x026c:
                            com.ezscreenrecorder.FloatingService.mMediaProjection = r4     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ all -> 0x029e }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ all -> 0x029e }
                            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r2, r3)     // Catch:{ all -> 0x029e }
                            r0.show()     // Catch:{ all -> 0x029e }
                        L_0x0279:
                            if (r7 == 0) goto L_0x0284
                            r7.close()     // Catch:{ IOException -> 0x027f }
                            goto L_0x0284
                        L_0x027f:
                            r0 = move-exception
                            r4 = r0
                            r4.printStackTrace()     // Catch:{ Exception -> 0x02c8 }
                        L_0x0284:
                            if (r11 == 0) goto L_0x0289
                            r11.recycle()     // Catch:{ Exception -> 0x02c8 }
                        L_0x0289:
                            if (r5 == 0) goto L_0x028e
                            r5.close()     // Catch:{ Exception -> 0x02c8 }
                        L_0x028e:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x02d7
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.view.View r0 = r0.floatingView     // Catch:{ Exception -> 0x02c8 }
                            goto L_0x01c2
                        L_0x029e:
                            r0 = move-exception
                            goto L_0x01d1
                        L_0x02a1:
                            if (r7 == 0) goto L_0x02ac
                            r7.close()     // Catch:{ IOException -> 0x02a7 }
                            goto L_0x02ac
                        L_0x02a7:
                            r0 = move-exception
                            r7 = r0
                            r7.printStackTrace()     // Catch:{ Exception -> 0x02c8 }
                        L_0x02ac:
                            if (r11 == 0) goto L_0x02b1
                            r11.recycle()     // Catch:{ Exception -> 0x02c8 }
                        L_0x02b1:
                            if (r5 == 0) goto L_0x02b6
                            r5.close()     // Catch:{ Exception -> 0x02c8 }
                        L_0x02b6:
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            boolean r0 = r4     // Catch:{ Exception -> 0x02c8 }
                            if (r0 != 0) goto L_0x02c7
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this     // Catch:{ Exception -> 0x02c8 }
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x02c8 }
                            android.view.View r0 = r0.floatingView     // Catch:{ Exception -> 0x02c8 }
                            r0.setVisibility(r6)     // Catch:{ Exception -> 0x02c8 }
                        L_0x02c7:
                            throw r4     // Catch:{ Exception -> 0x02c8 }
                        L_0x02c8:
                            r0 = move-exception
                            r0.printStackTrace()
                            com.ezscreenrecorder.FloatingService$25 r0 = com.ezscreenrecorder.FloatingService.C072025.this
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this
                            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r2, r3)
                            r0.show()
                        L_0x02d7:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.C072025.C07211.onImageAvailable(android.media.ImageReader):void");
                    }
                }, new Handler());
                return Observable.just(Boolean.TRUE);
            }
        }).subscribe((Observer<? super T>) new DisposableObserver<Boolean>() {
            public void onComplete() {
            }

            public void onNext(Boolean bool) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VirtualDisplay createVirtualDisplay(int i, int i2) {
        return mMediaProjection.createVirtualDisplay("RecordScreen", i, i2, mScreenDensity, 16, this.mMediaRecorder.getSurface(), null, null);
    }

    /* access modifiers changed from: private */
    public void prepareRecorder() {
        try {
            this.mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
            Toast.makeText(this, R.string.no_permission_allow_save, Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: private */
    public boolean checkWriteSettingsPermissionAvailable() {
        return ContextCompat.checkSelfPermission(this, "android.permission.WRITE_SETTINGS") == 0;
    }

    /* access modifiers changed from: private */
    public boolean checkCameraPermissionAvailable() {
        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0;
    }

    /* access modifiers changed from: private */
    public void initRecorder() {
        boolean z;
        int i = 2;
        this.mMediaRecorder.setVideoSource(2);
        try {
            z = m14ac();
        } catch (Throwable th) {
            th.printStackTrace();
            z = true;
        }
        if (!PreferenceHelper.getInstance().getPrefRecordAudio()) {
            this.mMediaRecorder.setCaptureRate(30.0d);
        } else if (z) {
            this.mMediaRecorder.setAudioSource(1);
        }
        this.mMediaRecorder.setOutputFormat(2);
        if (PreferenceHelper.getInstance().getPrefRecordAudio() && z) {
            this.mMediaRecorder.setAudioSamplingRate(SrsCodecAudioSampleRate.R44100);
            this.mMediaRecorder.setAudioEncodingBitRate(128000);
            this.mMediaRecorder.setAudioEncoder(3);
        }
        this.mMediaRecorder.setVideoFrameRate(Integer.parseInt(this.prefs.getString("example_list_frame_rate", String.valueOf(30))));
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (this.prefs.getBoolean("notifications_green", false)) {
            i = 3;
        }
        mediaRecorder.setVideoEncoder(i);
        /* resolution for video */
        setResolution();

        /* orientation for video */
        String prefRecordingOrientation = PreferenceHelper.getInstance().getPrefRecordingOrientation();
        if (prefRecordingOrientation.equalsIgnoreCase("Auto")) {
            int i2 = screenOrientation;
            if (i2 <= 310 || i2 >= 360) {
                int i3 = this.screenOrientation;
                if (i3 < 0 || i3 >= 60) {
                    int i4 = this.screenOrientation;
                    if (i4 <= 130 || i4 >= 240) {
                        int i5 = this.screenOrientation;
                        if (i5 <= 240 || i5 >= 310) {
                            int i6 = this.screenOrientation;
                            if (i6 <= 60 || i6 >= 130) {
                                this.mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
                            }
                        }
                        this.mMediaRecorder.setVideoSize(DISPLAY_HEIGHT, DISPLAY_WIDTH);
                    }
                }
            }
            this.mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        } else if (prefRecordingOrientation.equalsIgnoreCase("LandScape")) {
            this.mMediaRecorder.setVideoSize(DISPLAY_HEIGHT, DISPLAY_WIDTH);
        } else {
            this.mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }
        this.mMediaRecorder.setVideoEncodingBitRate(Integer.parseInt(this.prefs.getString("example_list_bit_rate", String.valueOf(1000000))));
        try {
            this.mCurrentVideoPath = createVideoFile();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = this.mCurrentVideoPath;
        if (str == null) {
            Toast.makeText(this, R.string.id_output_path_error_msg, Toast.LENGTH_SHORT).show();
            return;
        }
        addPathToPref(str);
        this.mMediaRecorder.setOutputFile(this.mCurrentVideoPath);
    }

    /* access modifiers changed from: private */
    public boolean checkOverlayPermission() {
        if (VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void addPathToPref(String str) {
        if (MainActivity.showDirectly != null) {
            MainActivity.showDirectly.setVideoPath(str);
            final Gson gson = new Gson();
            final SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
            Single.create(new SingleOnSubscribe<ArrayList<SharedDataForOtherApp>>() {
                public void subscribe(SingleEmitter<ArrayList<SharedDataForOtherApp>> singleEmitter) throws Exception {
                    String string = sharedPreferences.getString(FloatingService.SAVED_FILES, null);
                    if (string == null) {
                        singleEmitter.onSuccess(new ArrayList());
                        return;
                    }
                    singleEmitter.onSuccess(new ArrayList((List) gson.fromJson(string, new TypeToken<List<SharedDataForOtherApp>>() {
                    }.getType())));
                }
            }).map(new Function<ArrayList<SharedDataForOtherApp>, ArrayList<SharedDataForOtherApp>>() {
                public ArrayList<SharedDataForOtherApp> apply(ArrayList<SharedDataForOtherApp> arrayList) throws Exception {
                    arrayList.add(MainActivity.showDirectly);
                    return arrayList;
                }
            }).flatMap(new Function<ArrayList<SharedDataForOtherApp>, SingleSource<String>>() {
                public SingleSource<String> apply(final ArrayList<SharedDataForOtherApp> arrayList) throws Exception {
                    return Single.create(new SingleOnSubscribe<String>() {
                        public void subscribe(SingleEmitter<String> singleEmitter) throws Exception {
                            singleEmitter.onSuccess(gson.toJson((Object) arrayList));
                        }
                    });
                }
            }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<String>() {
                public void onSuccess(String str) {
                    sharedPreferences.edit().putString(FloatingService.SAVED_FILES, str).apply();
                    MainActivity.showDirectly = null;
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        }
    }

    private void startRecording() {
        Flowable.create(new FlowableOnSubscribe<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void subscribe(FlowableEmitter<Void> flowableEmitter) throws Exception {
                try {
                    FloatingService.this.mMediaRecorder = new MediaRecorder();
                    FloatingService.this.initRecorder();
                    FloatingService.this.prepareRecorder();
                    String prefRecordingOrientation = PreferenceHelper.getInstance().getPrefRecordingOrientation();
                    if (prefRecordingOrientation.equalsIgnoreCase("Auto")) {
                        if ((FloatingService.this.screenOrientation > 310 && FloatingService.this.screenOrientation < 360) || ((FloatingService.this.screenOrientation >= 0 && FloatingService.this.screenOrientation < 60) || (FloatingService.this.screenOrientation > 130 && FloatingService.this.screenOrientation < 240))) {
                            FloatingService.this.mVirtualDisplay = FloatingService.this.createVirtualDisplay(FloatingService.DISPLAY_WIDTH, FloatingService.DISPLAY_HEIGHT);
                        } else if ((FloatingService.this.screenOrientation <= 240 || FloatingService.this.screenOrientation >= 310) && (FloatingService.this.screenOrientation <= 60 || FloatingService.this.screenOrientation >= 130)) {
                            FloatingService.this.mVirtualDisplay = FloatingService.this.createVirtualDisplay(FloatingService.DISPLAY_WIDTH, FloatingService.DISPLAY_HEIGHT);
                        } else {
                            FloatingService.this.mVirtualDisplay = FloatingService.this.createVirtualDisplay(FloatingService.DISPLAY_HEIGHT, FloatingService.DISPLAY_WIDTH);
                        }
                    } else if (prefRecordingOrientation.equalsIgnoreCase("LandScape")) {
                        FloatingService.this.mVirtualDisplay = FloatingService.this.createVirtualDisplay(FloatingService.DISPLAY_HEIGHT, FloatingService.DISPLAY_WIDTH);
                    } else {
                        FloatingService.this.mVirtualDisplay = FloatingService.this.createVirtualDisplay(FloatingService.DISPLAY_WIDTH, FloatingService.DISPLAY_HEIGHT);
                    }
                    Thread.sleep(FloatingService.ANIMATION_DURATION);
                    FloatingService.this.mMediaRecorder.start();
                    FloatingService.this.showNotification(1);
                    if (FloatingService.this.prefs.getBoolean("notifications_screen_off", true)) {
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction("android.intent.action.SCREEN_ON");
                        intentFilter.addAction("android.intent.action.SCREEN_OFF");
                        FloatingService.this.registerReceiver(FloatingService.this.broadcastReceiverOnOffScreen, intentFilter);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    if (FloatingService.this.floatingView != null) {
                        FloatingService.this.floatingView.setVisibility(View.VISIBLE);
                    }
                    if (FloatingService.mMediaProjection != null) {
                        FloatingService.mMediaProjection.stop();
                    }
                    FloatingService.mMediaProjection = null;
                    FloatingService.this.mMediaRecorder = null;
                    FloatingService.this.removeSurfaceView();
                }
            }
        }, BackpressureStrategy.BUFFER).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<Void>() {
            public void onComplete() {
            }

            public void onNext(Void voidR) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    private void stopScreenSharing() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
    }

    /* access modifiers changed from: private */
    public void addAnalyticEvent(int i, String str) {
        if (new File(str).exists()) {
            switch (i) {
                case EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                    FirebaseEventsNewHelper.getInstance().sendScreenshotSuccessEvent(UtilityMethods.getInstance().getImageResolutionFromPath(str), UtilityMethods.getInstance().getFileSizeFromPath(str));
                    break;
                case EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                    UtilityMethods.getInstance().getMediaMetadataFromPath(str, UtilityMethods.MEDIA_TYPE_VIDEO).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<MetadataInfoModel>() {
                        public void onSuccess(MetadataInfoModel metadataInfoModel) {
                            FirebaseEventsNewHelper.getInstance().sendVideoRecordSuccessEvent(metadataInfoModel.getDurationInSec(), metadataInfoModel.getSizeInMb());
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                    break;
                case EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                    UtilityMethods.getInstance().getMediaMetadataFromPath(str, UtilityMethods.MEDIA_TYPE_VIDEO).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<MetadataInfoModel>() {
                        public void onSuccess(MetadataInfoModel metadataInfoModel) {
                            FirebaseEventsNewHelper.getInstance().sendInteractiveRecordSuccessEvent(metadataInfoModel.getDurationInSec(), metadataInfoModel.getSizeInMb());
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                    break;
                case EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                    UtilityMethods.getInstance().getMediaMetadataFromPath(str, UtilityMethods.MEDIA_TYPE_AUDIO).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<MetadataInfoModel>() {
                        public void onSuccess(MetadataInfoModel metadataInfoModel) {
                            FirebaseEventsNewHelper.getInstance().sendAudioRecordSuccessEvent(metadataInfoModel.getDurationInSec(), metadataInfoModel.getSizeInMb());
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                    break;
                case EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                    UtilityMethods.getInstance().getMediaMetadataFromPath(str, UtilityMethods.MEDIA_TYPE_VIDEO).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<MetadataInfoModel>() {
                        public void onSuccess(MetadataInfoModel metadataInfoModel) {
                            FirebaseEventsNewHelper.getInstance().sendTutorialRecordSuccessEvent(metadataInfoModel.getDurationInSec(), metadataInfoModel.getSizeInMb());
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                    break;
                case EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                    UtilityMethods.getInstance().getMediaMetadataFromPath(str, UtilityMethods.MEDIA_TYPE_VIDEO).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<MetadataInfoModel>() {
                        public void onSuccess(MetadataInfoModel metadataInfoModel) {
                            FirebaseEventsNewHelper.getInstance().sendGameRecordSuccessEvent(metadataInfoModel.getDurationInSec(), metadataInfoModel.getSizeInMb(), FloatingService.this.gamePackageName);
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                    break;
            }
        }
    }
    private void stopScreenRecording22()
    {
            try
            {
                hideNotificationTray();
                stopScreenSharing();
                if (this.actionType == 1343) {
                    showNotification(0);
                }
                try
                {
                    if (this.recordView != null) {
                        this.windowManager.removeView(this.recordView);
                    }
                    if (this.view1 != null) {
                        this.windowManager.removeView(this.view1);
                    }
                    if (this.view2 != null) {
                        this.windowManager.removeView(this.view2);
                    }
                    if (this.view3 != null) {
                        this.windowManager.removeView(this.view3);
                    }
                    if (this.view4 != null) {
                        this.windowManager.removeView(this.view4);
                    }
                    if (this.backView != null) {
                        this.windowManager.removeView(this.backView);
                    }
                    if (this.textViewPauseHighLightMessage != null) {
                        this.windowManager.removeView(this.textViewPauseHighLightMessage);
                    }
                    if (this.textViewStopHighLightMessage != null) {
                        this.windowManager.removeView(this.textViewStopHighLightMessage);
                    }
                    if (this.textViewCameraHighLightMessage != null) {
                        this.windowManager.removeView(this.textViewCameraHighLightMessage);
                    }
                    if (this.textViewHighLightMessage != null) {
                        this.windowManager.removeView(this.textViewHighLightMessage);
                    }
                    this.paramsRecord = null;
                    this.textViewPauseHighLightMessage = null;
                    this.textViewStopHighLightMessage = null;
                    this.textViewCameraHighLightMessage = null;
                    this.textViewHighLightMessage = null;
                    this.mWindowParams1 = null;
                    this.mWindowParams2 = null;
                    this.mWindowParams3 = null;
                    this.mWindowParams4 = null;
                    this.recordView = null;
                    this.view1 = null;
                    this.view2 = null;
                    this.view3 = null;
                    this.view4 = null;
                }
                catch (Exception localException1)
                {
                    localException1.printStackTrace();
                }
                if (this.mMediaRecorder != null) {
                    this.mMediaRecorder.reset();
                }
                if (this.mRecorder != null)
                {
                    this.mRecorder.setIsRecording(false);
                    this.mRecorder.quit();
                    this.mRecorder = null;
                }
                /*if (this.sMuxer != null)
                {
                    this.sMuxer.pauseRecording();
                    Handler localHandler = this.handler;
                    Runnable local37 = new Runnable()
                    {
                        public void run()
                        {
                            if (FloatingService.this.sMuxer != null) {
                                FloatingService.this.sMuxer.stopRecording();
                            }
                            FloatingService.access$1902(FloatingService.this, null);
                        }
                    };
                    if (!this.pausedOnce) {
                        break label557;
                    }
                    l = 50L;
                    localHandler.postDelayed(local37, l);
                    return;
                }*/
                if (mMediaProjection != null) {
                    mMediaProjection.stop();
                }
                if (checkWriteSettingsPermissionAvailable()) {
                    Settings.System.putInt(getContentResolver(), "show_touches", 0);
                }
                this.mediaScreenRecorder = null;
                mMediaProjection = null;
                this.mMediaRecorder = null;
                int i = this.actionType;
                if (i != 1341)
                {
                    if (i == 1342) {
                        AppUtils.addCount(getApplicationContext(), 2);
                    }
                }
                else {
                    AppUtils.addCount(getApplicationContext(), 1);
                }
                try
                {
                    unregisterReceiver(this.broadcastReceiverOnOffScreen);
                }
                catch (Exception localException2)
                {
                    localException2.printStackTrace();
                }
                mMediaProjection = null;
                this.mMediaRecorder = null;
                if (this.actionType == 1343)
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            File localFile;
                            AudioFileModel localAudioFileModel;
                            if ((FloatingService.this.audioRecorder != null) && (!TextUtils.isEmpty(FloatingService.this.audioRecorder.getFilePath())))
                            {
                                localFile = new File(FloatingService.this.audioRecorder.getFilePath());
                                localAudioFileModel = new AudioFileModel();
                                localAudioFileModel.setFileName(localFile.getName());
                                localAudioFileModel.setFilePath(localFile.getAbsolutePath());
                                localAudioFileModel.setFileCreated(localFile.lastModified());
                                localAudioFileModel.setFileSize(localFile.length());
                                localObject1 = new MediaMetadataRetriever();
                            }
                            for (;;)
                            {
                                try
                                {
                                    ((MediaMetadataRetriever)localObject1).setDataSource(FloatingService.this.getApplicationContext(), Uri.fromFile(localFile));
                                }
                                catch (Exception localException4)
                                {
                                    Object localObject4;
                                    continue;
                                }
                                try
                                {
                                    localObject4 = new MediaMetadataRetriever();
                                }
                                catch (Exception localException5)
                                {
                                    continue;
                                }
                                try
                                {
                                    ((MediaMetadataRetriever)localObject4).setDataSource(localFile.getAbsolutePath());
                                    localObject1 = localObject4;
                                }
                                catch (Exception localException2)
                                {
                                    continue;
                                }
                            }
                            Object localObject1 = localObject4;
                            for (;;)
                            {
                                try
                                {
                                    localObject4 = new MediaMetadataRetriever();
                                }
                                catch (Exception localException6)
                                {
                                    Object localObject2;
                                    continue;
                                }
                                try
                                {
                                    ((MediaMetadataRetriever)localObject4).setDataSource(localFile.getAbsolutePath(), new HashMap());
                                    localObject1 = localObject4;
                                }
                                catch (Exception localException3)
                                {
                                    Object localObject3 = localException6;
                                }
                                try
                                {
                                    localAudioFileModel.setFileDuration(Long.parseLong(((MediaMetadataRetriever)localObject1).extractMetadata(9)));
                                }
                                catch (Exception localException1)
                                {
                                    Crashlytics.logException(localException1);
                                }
                            }
                            localObject2 = new Intent(FloatingService.this, GalleryActivity.class);
                            localObject4 = ActivityOptions.makeCustomAnimation(FloatingService.this.getApplicationContext(), 2130771987, 2130771989);
                            ((Intent)localObject2).putExtra("main_floating_action_type", FloatingService.this.actionType);
                            ((Intent)localObject2).putExtra("key_file_audio_model", localAudioFileModel);
                            ((Intent)localObject2).addFlags(268468224);
                            FloatingService.this.startActivity((Intent)localObject2, ((ActivityOptions)localObject4).toBundle());
                            localObject2 = FloatingService.this;
                            ((FloatingService)localObject2).addAnalyticEvent(((FloatingService)localObject2).actionType, localAudioFileModel.getFilePath());
                        }
                    }, 40L);
                    return;
                }
            }
            catch (Exception localException3)
            {
                localException3.printStackTrace();
                Crashlytics.logException(localException3);
            }
            return;
            label557:
            long l = 400L;
    }
    /* access modifiers changed from: private */
    public void stopScreenRecording() {
        try {
            hideNotificationTray();
            stopScreenSharing();
            if (this.actionType == 1343) {
                showNotification(0);
            }
            try {
                if (this.recordView != null) {
                    this.windowManager.removeView(this.recordView);
                }
                if (this.view1 != null) {
                    this.windowManager.removeView(this.view1);
                }
                if (this.view2 != null) {
                    this.windowManager.removeView(this.view2);
                }
                if (this.view3 != null) {
                    this.windowManager.removeView(this.view3);
                }
                if (this.view4 != null) {
                    this.windowManager.removeView(this.view4);
                }
                if (this.backView != null) {
                    this.windowManager.removeView(this.backView);
                }
                if (this.textViewPauseHighLightMessage != null) {
                    this.windowManager.removeView(this.textViewPauseHighLightMessage);
                }
                if (this.textViewStopHighLightMessage != null) {
                    this.windowManager.removeView(this.textViewStopHighLightMessage);
                }
                if (this.textViewCameraHighLightMessage != null) {
                    this.windowManager.removeView(this.textViewCameraHighLightMessage);
                }
                if (this.textViewHighLightMessage != null) {
                    this.windowManager.removeView(this.textViewHighLightMessage);
                }
                this.paramsRecord = null;
                this.textViewPauseHighLightMessage = null;
                this.textViewStopHighLightMessage = null;
                this.textViewCameraHighLightMessage = null;
                this.textViewHighLightMessage = null;
                this.mWindowParams1 = null;
                this.mWindowParams2 = null;
                this.mWindowParams3 = null;
                this.mWindowParams4 = null;
                this.recordView = null;
                this.view1 = null;
                this.view2 = null;
                this.view3 = null;
                this.view4 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.reset();
            }
            if (this.mRecorder != null) {
                this.mRecorder.setIsRecording(false);
                this.mRecorder.quit();
                this.mRecorder = null;
            }
            if (this.sMuxer != null) {
                this.sMuxer.pauseRecording();
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        if (FloatingService.this.sMuxer != null) {
                            FloatingService.this.sMuxer.stopRecording();
                        }
                        FloatingService.this.sMuxer = null;
                    }
                }, this.pausedOnce ? 50 : 400);
                return;
            }
            if (mMediaProjection != null) {
                mMediaProjection.stop();
            }
            if (checkWriteSettingsPermissionAvailable()) {
                System.putInt(getContentResolver(), "show_touches", 0);
            }
            this.mediaScreenRecorder = null;
            mMediaProjection = null;
            this.mMediaRecorder = null;
            int i = this.actionType;
            if (i == 1341) {
                AppUtils.addCount(getApplicationContext(), 1);
            } else if (i == 1342) {
                AppUtils.addCount(getApplicationContext(), 2);
            }
            try {
                unregisterReceiver(this.broadcastReceiverOnOffScreen);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            mMediaProjection = null;
            this.mMediaRecorder = null;
            if (this.actionType == 1343) {
                new Handler().postDelayed(new Runnable() {
                    /* JADX WARNING: Can't wrap try/catch for region: R(12:14|13|15|16|17|18|11|19|21|22|25|26) */
                    /* JADX WARNING: Can't wrap try/catch for region: R(12:4|5|6|7|8|(2:9|10)|11|19|21|22|25|26) */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x006a */
                    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x005b */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r5 = this;
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this
                            com.ezscreenrecorder.FloatingService$AudioRecorder r0 = r0.audioRecorder
                            if (r0 == 0) goto L_0x00d3
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this
                            com.ezscreenrecorder.FloatingService$AudioRecorder r0 = r0.audioRecorder
                            java.lang.String r0 = r0.getFilePath()
                            boolean r0 = android.text.TextUtils.isEmpty(r0)
                            if (r0 != 0) goto L_0x00d3
                            java.io.File r0 = new java.io.File
                            com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this
                            com.ezscreenrecorder.FloatingService$AudioRecorder r1 = r1.audioRecorder
                            java.lang.String r1 = r1.getFilePath()
                            r0.<init>(r1)
                            com.ezscreenrecorder.model.AudioFileModel r1 = new com.ezscreenrecorder.model.AudioFileModel
                            r1.<init>()
                            java.lang.String r2 = r0.getName()
                            r1.setFileName(r2)
                            java.lang.String r2 = r0.getAbsolutePath()
                            r1.setFilePath(r2)
                            long r2 = r0.lastModified()
                            r1.setFileCreated(r2)
                            long r2 = r0.length()
                            r1.setFileSize(r2)
                            android.media.MediaMetadataRetriever r2 = new android.media.MediaMetadataRetriever
                            r2.<init>()
                            com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x005b }
                            android.content.Context r3 = r3.getApplicationContext()     // Catch:{ Exception -> 0x005b }
                            android.net.Uri r4 = android.net.Uri.fromFile(r0)     // Catch:{ Exception -> 0x005b }
                            r2.setDataSource(r3, r4)     // Catch:{ Exception -> 0x005b }
                            goto L_0x007c
                        L_0x005b:
                            android.media.MediaMetadataRetriever r3 = new android.media.MediaMetadataRetriever     // Catch:{ Exception -> 0x006a }
                            r3.<init>()     // Catch:{ Exception -> 0x006a }
                            java.lang.String r2 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x0069 }
                            r3.setDataSource(r2)     // Catch:{ Exception -> 0x0069 }
                        L_0x0067:
                            r2 = r3
                            goto L_0x007c
                        L_0x0069:
                            r2 = r3
                        L_0x006a:
                            android.media.MediaMetadataRetriever r3 = new android.media.MediaMetadataRetriever     // Catch:{ Exception -> 0x007c }
                            r3.<init>()     // Catch:{ Exception -> 0x007c }
                            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x0067 }
                            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Exception -> 0x0067 }
                            r2.<init>()     // Catch:{ Exception -> 0x0067 }
                            r3.setDataSource(r0, r2)     // Catch:{ Exception -> 0x0067 }
                            goto L_0x0067
                        L_0x007c:
                            r0 = 9
                            java.lang.String r0 = r2.extractMetadata(r0)     // Catch:{ Exception -> 0x008a }
                            long r2 = java.lang.Long.parseLong(r0)     // Catch:{ Exception -> 0x008a }
                            r1.setFileDuration(r2)     // Catch:{ Exception -> 0x008a }
                            goto L_0x008e
                        L_0x008a:
                            r0 = move-exception
                            com.crashlytics.android.Crashlytics.logException(r0)
                        L_0x008e:
                            android.content.Intent r0 = new android.content.Intent
                            com.ezscreenrecorder.FloatingService r2 = com.ezscreenrecorder.FloatingService.this
                            java.lang.Class<com.ezscreenrecorder.activities.GalleryActivity> r3 = com.ezscreenrecorder.activities.GalleryActivity.class
                            r0.<init>(r2, r3)
                            com.ezscreenrecorder.FloatingService r2 = com.ezscreenrecorder.FloatingService.this
                            android.content.Context r2 = r2.getApplicationContext()
                            r3 = 2130771987(0x7f010013, float:1.714708E38)
                            r4 = 2130771989(0x7f010015, float:1.7147084E38)
                            android.app.ActivityOptions r2 = android.app.ActivityOptions.makeCustomAnimation(r2, r3, r4)
                            com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                            int r3 = r3.actionType
                            java.lang.String r4 = "main_floating_action_type"
                            r0.putExtra(r4, r3)
                            java.lang.String r3 = "key_file_audio_model"
                            r0.putExtra(r3, r1)
                            r3 = 268468224(0x10008000, float:2.5342157E-29)
                            r0.addFlags(r3)
                            com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                            android.os.Bundle r2 = r2.toBundle()
                            r3.startActivity(r0, r2)
                            com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this
                            int r2 = r0.actionType
                            java.lang.String r1 = r1.getFilePath()
                            r0.addAnalyticEvent(r2, r1)
                        L_0x00d3:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.C074238.run():void");
                    }
                }, 40);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            Crashlytics.logException(e3);
        }
    }

    /* access modifiers changed from: private */
    public void hideNotificationTray() {
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    private String createVideoFile() throws NameNotFoundException {
        String videoDir = AppUtils.getVideoDir(getApplicationContext());
        String format = this.fileFormat.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(videoDir);
        sb.append(format);
        File file = new File(sb.toString());
        file.setReadable(true);
        file.setWritable(true);
        addVideo(file, file.getPath());
        Log.e("video file:>>", file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public Uri addVideo(File file, String str) {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("title", str);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("_data", file.getAbsolutePath());
        return getContentResolver().insert(Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /* access modifiers changed from: private */
    public String createImageFile() throws NameNotFoundException {
        String imageDir = AppUtils.getImageDir(getApplicationContext());
        String format = this.fileFormat2.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(imageDir);
        sb.append(format);
        File file = new File(sb.toString());
        try {
            addImage(file, file.getPath());
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.no_permission_allow_save_img, 1).show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Log.e("image file:", file.getPath());
        return file.getAbsolutePath();
    }

    public Uri addImage(File file, String str) throws Exception {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("title", str);
        contentValues.put("mime_type", "image/*");
        contentValues.put("_data", file.getAbsolutePath());
        return getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /* access modifiers changed from: private */
    public void showNotification(int i) {
        NotificationCompat.Builder builder;
        NotificationCompat.Builder builder2;
        int i2 = i;
        if (VERSION.SDK_INT >= 26) {
            createNotificationChannel();
        }
        String str = CHANNEL_ID;
        if (i2 == 0) {
            String str2 = NotificationActionBroadcastReceiver.KEY_ACTION_FROM_NOTIFICATION;

            Intent intent = new Intent(this, NotificationActionBroadcastReceiver.class);
            intent.putExtra(str2, EXTRA_MAIN_ACTION_TYPE_MORE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ActionMoreModel.KEY_IS_MORE_FROM_NOTIFICATION, true);

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 51, intent, 134217728);
            Intent intent2 = new Intent(this, NotificationActionBroadcastReceiver.class);
            intent2.putExtra(str2, EXTRA_MAIN_ACTION_TYPE_VIDEO);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent broadcast2 = PendingIntent.getBroadcast(this, 52, intent2, Ints.MAX_POWER_OF_TWO);
            Intent intent3 = new Intent(this, NotificationActionBroadcastReceiver.class);
            intent3.putExtra(str2, EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent broadcast3 = PendingIntent.getBroadcast(this, 53, intent3, Ints.MAX_POWER_OF_TWO);
            Intent intent4 = new Intent(this, LiveLoginActivity.class);
            intent4.addFlags(335544320);

            PendingIntent activity = PendingIntent.getActivity(this, 54, intent4, 134217728);
            Intent intent5 = new Intent(this, GalleryActivity.class);
            intent5.addFlags(335544320);

            PendingIntent activity2 = PendingIntent.getActivity(this, 55, intent5, 134217728);
            PendingIntent broadcast4 = PendingIntent.getBroadcast(this, 56, new Intent(this, BroadcastReceiverStopBubble.class), 0);
            PendingIntent activity3 = PendingIntent.getActivity(this, 57, new Intent(this, SplashActivity.class), 0);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notifcation_buttons);
            remoteViews.setOnClickPendingIntent(R.id.btn_camera, broadcast3);
            remoteViews.setOnClickPendingIntent(R.id.btn_video, broadcast2);
            remoteViews.setOnClickPendingIntent(R.id.btn_gallery, activity2);
            remoteViews.setOnClickPendingIntent(R.id.btn_close, broadcast4);
            remoteViews.setOnClickPendingIntent(R.id.btn_more_button, broadcast);
            remoteViews.setOnClickPendingIntent(R.id.btn_go_live, activity);
            remoteViews.setOnClickPendingIntent(R.id.img_app_icon, activity3);
            remoteViews.setOnClickPendingIntent(R.id.lay_show_bubble, activity3);

            RemoteViews remoteViews2 = new RemoteViews(getPackageName(), R.layout.custom_notifcation_buttons_small);
            remoteViews2.setOnClickPendingIntent(R.id.btn_camera, broadcast3);
            remoteViews2.setOnClickPendingIntent(R.id.btn_video, broadcast2);
            remoteViews2.setOnClickPendingIntent(R.id.btn_gallery, activity2);
            remoteViews2.setOnClickPendingIntent(R.id.btn_go_live, activity);
            remoteViews2.setOnClickPendingIntent(R.id.btn_more_button, broadcast);

            builder = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.app_started))
                    .setCustomBigContentView(remoteViews).setCustomContentView(remoteViews2).setAutoCancel(false);

        } else if (i2 != 1) {
            if (i2 == 2) {
                Intent intent6 = new Intent(this, GalleryActivity.class);
                intent6.putExtra(GALLERY_TYPE_IMAGE, true);
                intent6.putExtra(GALLERY_VIDEO_PLAY, this.mCurrentVideoPath);
                intent6.addFlags(603979776); // FLAG_ACTIVITY_CLEAR_TOP
                PendingIntent activity4 = PendingIntent.getActivity(this, 0, intent6, 134217728);
                builder2 = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.image_capture)).setTicker(getString(R.string.image_capture)).setPriority(1).setAutoCancel(false);
                builder2.setContentIntent(activity4);
            } else if (i2 == 3) {
                Intent intent7 = new Intent(this, GalleryActivity.class);
                intent7.putExtra(GALLERY_TYPE_IMAGE, false);
                intent7.putExtra(GALLERY_VIDEO_PLAY, this.mCurrentVideoPath);
                intent7.addFlags(603979776);
                PendingIntent activity5 = PendingIntent.getActivity(this, 0, intent7, 134217728);
                builder2 = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.video_recorded)).setTicker(getString(R.string.video_recorded)).setPriority(1).setAutoCancel(false);
                builder2.setContentIntent(activity5);
            } else if (i2 == 4) {
                PendingIntent broadcast5 = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverStopRecord.class), 268435456);
                PendingIntent broadcast6 = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverResumeRecord.class), 268435456);
                builder = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).
                        setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.id_noti_recording_paused_msg)).setTicker(getString(R.string.id_noti_recording_paused_msg)).setPriority(1).setContentIntent(broadcast6).addAction(R.mipmap.ic_play, "Resume", broadcast6).addAction(R.drawable.ic_stop, getString(R.string.stop_recording), broadcast5).setAutoCancel(false);
                builder.setContentIntent(broadcast6);
            } else if (i2 != 5) {
                builder = null;
            } else {
                PendingIntent broadcast7 = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverStopRecord.class), 268435456);
                builder2 = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).
                        setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.id_noti_audio_record_start_message)).
                        setTicker(getString(R.string.id_noti_audio_record_start_message)).setPriority(1).setContentIntent(broadcast7).setAutoCancel(false);
                builder2.setContentIntent(broadcast7);
                builder2.addAction(R.drawable.ic_stop, getString(R.string.stop_recording), broadcast7);
            }
            builder = builder2;
        } else {
            PendingIntent broadcast8 = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverStopRecord.class), 268435456);
            PendingIntent broadcast9 = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverPauseRecord.class), 268435456);
            NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(this, str).setSmallIcon(R.mipmap.ic_launcher5).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.record_start)).setTicker(getString(R.string.record_start)).setPriority(1).setContentIntent(broadcast8).setAutoCancel(false);
            autoCancel.setContentIntent(broadcast8);
            autoCancel.addAction(R.drawable.ic_pause_white, "Pause", broadcast9);
            autoCancel.addAction(R.drawable.ic_stop, getString(R.string.stop_recording), broadcast8);
            builder = autoCancel;
        }
        if (builder != null) {
            startForeground(3411, builder.build());
        }
    }

    @RequiresApi(26)
    private void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String str = CHANNEL_ID;
        if (notificationManager.getNotificationChannel(str) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(str, getString(R.string.notification_channel), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(getString(R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.szWindow.set(this.windowManager.getDefaultDisplay().getWidth(), this.windowManager.getDefaultDisplay().getHeight());
        if (this.whichAnimation != 0) {
            clicked();
        }
        calculatePentagonVertices(this.radius, 11, this.mNewFloatingView);
        MediaScreenEncoder mediaScreenEncoder = this.mediaScreenRecorder;
        if (mediaScreenEncoder != null) {
            mediaScreenEncoder.setOrientation(configuration.orientation);
        }
    }

    /* access modifiers changed from: private */
    public int getTypeOfWindowManagerParam() {
        if (VERSION.SDK_INT >= 26) {
            return 2038;
        }
        return 0;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onCreate() {
        super.onCreate();
        try {
            showNotification(0);
            registerReceiver(this.broadcastReceiver, new IntentFilter(FLAG_RUNNING_SERVICE));
            this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);

            this.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            this.floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout22, null);

            LayoutParams layoutParams = new LayoutParams(dpToPx(52), dpToPx(52), getTypeOfWindowManagerParam(), 264, -3);
            this.layoutParamsMain = layoutParams;
            this.layoutParamsMain.gravity = 53;
            this.layoutParamsMain.x = 0;
            this.layoutParamsMain.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
            try {
                this.windowManager.addView(this.floatingView, this.layoutParamsMain);
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        FloatingService.this.layoutParamsMain.x = sharedPreferences.getInt("posX", 0);
                        FloatingService.this.layoutParamsMain.y = sharedPreferences.getInt("posY", (FloatingService.this.windowManager.getDefaultDisplay().getHeight() / 2) - 50);
                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.floatingView, FloatingService.this.layoutParamsMain);
                    }
                }, 20);
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
            LayoutParams layoutParams2 = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 264, -3);
            this.paramsCountDown = layoutParams2;
            this.paramsCountDown.gravity = 17;
            addCloseLay();
            this.floatingViewClose.setVisibility(View.GONE);
            this.imgFloat = (ImageView) this.floatingView.findViewById(R.id.img_float); &&&&
            int width2 = this.windowManager.getDefaultDisplay().getWidth();
            final int height2 = this.windowManager.getDefaultDisplay().getHeight();
            this.szWindow.set(width2, height2);
            this.imgFloat.setOnTouchListener(new OnTouchListener() {
                static final float RANGE = 30.0f;
                private long downTime;
                Handler handler_longClick = new Handler();
                private float initialTouchX;
                private float initialTouchY;
                private int initialX;
                private int initialY;
                boolean isBound;
                int remove_img_height;
                int remove_img_width;
                Runnable runnable_longClick = new Runnable() {
                    public void run() {
                        final int width = FloatingService.this.windowManager.getDefaultDisplay().getWidth();
                        final int height = FloatingService.this.windowManager.getDefaultDisplay().getHeight();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Into runnable_longClick>>");
                        sb.append(width);
                        sb.append("<><");
                        sb.append(height);
                        Log.d("sd", sb.toString());
                        FloatingService.this.isLongclick = true;
                        FloatingService.this.floatingViewClose.setVisibility(View.VISIBLE);
                        FloatingService.this.floatingViewClose.post(new Runnable() {
                            public void run() {
                                FloatingService.this.paramsCloseView.x = (width / 2) - (FloatingService.this.floatingViewClose.getWidth() / 2);
                                FloatingService.this.paramsCloseView.y = height;
                                ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{FloatingService.this.paramsCloseView.y, FloatingService.this.paramsCloseView.y - 220});
                                ofInt.addUpdateListener(new AnimatorUpdateListener() {
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        FloatingService.this.paramsCloseView.y = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                        try {
                                            FloatingService.this.windowManager.updateViewLayout(FloatingService.this.floatingViewClose, FloatingService.this.paramsCloseView);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                ofInt.setDuration(270);
                                ofInt.start();
                                remove_img_width = FloatingService.this.floatingViewClose.getWidth();
                                remove_img_height = FloatingService.this.floatingViewClose.getHeight();
                            }
                        });
                    }
                };

                /* JADX WARNING: Removed duplicated region for block: B:50:0x01b2  */
                /* JADX WARNING: Removed duplicated region for block: B:64:0x01f8  */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public boolean onTouch(View r9, MotionEvent r10) {
                    /*
                        r8 = this;
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        boolean r9 = r9.isAllowTouch
                        r0 = 0
                        if (r9 == 0) goto L_0x0365
                        int r9 = r10.getAction()
                        r1 = 2
                        if (r9 == 0) goto L_0x0268
                        r2 = 1
                        if (r9 == r2) goto L_0x0108
                        if (r9 == r1) goto L_0x0017
                        goto L_0x0365
                    L_0x0017:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.paramsCloseView
                        int r9 = r9.x
                        int r3 = r8.remove_img_width
                        int r3 = r3 / r1
                        int r9 = r9 - r3
                        int r9 = r9 + -30
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r3 = r3.paramsCloseView
                        int r3 = r3.x
                        int r4 = r8.remove_img_width
                        int r4 = r4 / r1
                        int r3 = r3 + r4
                        int r3 = r3 + 180
                        com.ezscreenrecorder.FloatingService r4 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r4 = r4.paramsCloseView
                        int r4 = r4.y
                        int r5 = r8.remove_img_height
                        int r5 = r5 / r1
                        int r4 = r4 - r5
                        int r4 = r4 + -60
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r5 = r5.paramsCloseView
                        int r5 = r5.y
                        int r6 = r8.remove_img_height
                        int r6 = r6 / r1
                        int r5 = r5 + r6
                        int r5 = r5 + 180
                        float r6 = r10.getRawX()
                        int r6 = (int) r6
                        if (r6 < r9) goto L_0x006e
                        float r9 = r10.getRawX()
                        int r9 = (int) r9
                        if (r9 > r3) goto L_0x006e
                        float r9 = r10.getRawY()
                        int r9 = (int) r9
                        if (r9 < r4) goto L_0x006e
                        float r9 = r10.getRawY()
                        int r9 = (int) r9
                        if (r9 > r5) goto L_0x006e
                        r8.isBound = r2
                        goto L_0x0070
                    L_0x006e:
                        r8.isBound = r0
                    L_0x0070:
                        float r9 = r10.getRawX()
                        float r0 = r8.initialTouchX
                        float r9 = r9 - r0
                        float r9 = java.lang.Math.abs(r9)
                        r0 = 1106247680(0x41f00000, float:30.0)
                        int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                        if (r9 <= 0) goto L_0x0095
                        float r9 = r10.getRawY()
                        float r3 = r8.initialTouchY
                        float r9 = r9 - r3
                        float r9 = java.lang.Math.abs(r9)
                        int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
                        if (r9 <= 0) goto L_0x0095
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        r9.avoidLongClick = r2
                    L_0x0095:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        boolean r9 = r9.isLongclick
                        if (r9 == 0) goto L_0x00cc
                        boolean r9 = r8.isBound
                        if (r9 == 0) goto L_0x00cc
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r10 = r10.paramsCloseView
                        int r10 = r10.x
                        int r0 = r8.remove_img_width
                        int r0 = r0 / r1
                        int r10 = r10 + r0
                        int r10 = r10 + -67
                        r9.x = r10
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r10 = r10.paramsCloseView
                        int r10 = r10.y
                        int r0 = r8.remove_img_height
                        int r0 = r0 / r1
                        int r10 = r10 + r0
                        r9.y = r10
                        goto L_0x00f2
                    L_0x00cc:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        int r0 = r8.initialX
                        float r1 = r10.getRawX()
                        float r3 = r8.initialTouchX
                        float r1 = r1 - r3
                        int r1 = (int) r1
                        int r0 = r0 + r1
                        r9.x = r0
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        int r0 = r8.initialY
                        float r10 = r10.getRawY()
                        float r1 = r8.initialTouchY
                        float r10 = r10 - r1
                        int r10 = (int) r10
                        int r0 = r0 + r10
                        r9.y = r0
                    L_0x00f2:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager r9 = r9.windowManager
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r10 = r10.floatingView
                        com.ezscreenrecorder.FloatingService r0 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r0 = r0.layoutParamsMain
                        r9.updateViewLayout(r10, r0)
                        return r2
                    L_0x0108:
                        long r9 = java.lang.System.currentTimeMillis()
                        long r3 = r8.downTime
                        long r9 = r9 - r3
                        r3 = 680(0x2a8, double:3.36E-321)
                        r5 = 1500(0x5dc, double:7.41E-321)
                        int r7 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
                        if (r7 >= 0) goto L_0x0166
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        boolean r9 = r9.isAllowTouch
                        if (r9 == 0) goto L_0x0156
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        boolean r9 = r9.isLongclick
                        if (r9 != 0) goto L_0x0141
                        android.media.projection.MediaProjection r9 = com.ezscreenrecorder.FloatingService.mMediaProjection
                        if (r9 != 0) goto L_0x0141
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.os.Handler r9 = r9.handler
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        java.lang.Runnable r10 = r10.runnable_circle
                        r9.removeCallbacks(r10)
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        r9.clicked()
                        r9 = 1
                        goto L_0x0176
                    L_0x0141:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        r9.isLongclick = r0
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.os.Handler r9 = r9.handler
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        java.lang.Runnable r10 = r10.runnable_circle
                        r9.postDelayed(r10, r5)
                        goto L_0x0175
                    L_0x0156:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.os.Handler r9 = r9.handler
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        java.lang.Runnable r10 = r10.runnable_circle
                        r9.postDelayed(r10, r5)
                        goto L_0x0175
                    L_0x0166:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.os.Handler r9 = r9.handler
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        java.lang.Runnable r10 = r10.runnable_circle
                        r9.postDelayed(r10, r5)
                    L_0x0175:
                        r9 = 0
                    L_0x0176:
                        boolean r10 = r8.isBound
                        android.os.Handler r3 = r8.handler_longClick
                        java.lang.Runnable r4 = r8.runnable_longClick
                        r3.removeCallbacks(r4)
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x01a1 }
                        android.view.WindowManager$LayoutParams r3 = r3.paramsCloseView     // Catch:{ Exception -> 0x01a1 }
                        int r4 = r3     // Catch:{ Exception -> 0x01a1 }
                        int r4 = r4 + -100
                        r3.y = r4     // Catch:{ Exception -> 0x01a1 }
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x01a1 }
                        android.view.WindowManager r3 = r3.windowManager     // Catch:{ Exception -> 0x01a1 }
                        com.ezscreenrecorder.FloatingService r4 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x01a1 }
                        android.view.View r4 = r4.floatingViewClose     // Catch:{ Exception -> 0x01a1 }
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x01a1 }
                        android.view.WindowManager$LayoutParams r5 = r5.paramsCloseView     // Catch:{ Exception -> 0x01a1 }
                        r3.updateViewLayout(r4, r5)     // Catch:{ Exception -> 0x01a1 }
                        goto L_0x01a5
                    L_0x01a1:
                        r3 = move-exception
                        r3.printStackTrace()
                    L_0x01a5:
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r3 = r3.floatingViewClose
                        r4 = 8
                        r3.setVisibility(r4)
                        if (r10 == 0) goto L_0x01f8
                        java.lang.String r9 = "Screen recording"
                        java.lang.String r10 = "TOUCH STOP SERVICE"
                        android.util.Log.e(r9, r10)
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        android.view.View r9 = r9.mNewFloatingView     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        if (r9 == 0) goto L_0x01d5
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        android.view.WindowManager r9 = r9.windowManager     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        android.view.View r10 = r10.mNewFloatingView     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        r9.removeView(r10)     // Catch:{ IllegalArgumentException -> 0x01d1 }
                        goto L_0x01d5
                    L_0x01d1:
                        r9 = move-exception
                        r9.printStackTrace()
                    L_0x01d5:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01ed }
                        android.view.View r9 = r9.floatingView     // Catch:{ IllegalArgumentException -> 0x01ed }
                        if (r9 == 0) goto L_0x01f1
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01ed }
                        android.view.WindowManager r9 = r9.windowManager     // Catch:{ IllegalArgumentException -> 0x01ed }
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this     // Catch:{ IllegalArgumentException -> 0x01ed }
                        android.view.View r10 = r10.floatingView     // Catch:{ IllegalArgumentException -> 0x01ed }
                        r9.removeView(r10)     // Catch:{ IllegalArgumentException -> 0x01ed }
                        goto L_0x01f1
                    L_0x01ed:
                        r9 = move-exception
                        r9.printStackTrace()
                    L_0x01f1:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        r9.stopSelf()
                        goto L_0x0365
                    L_0x01f8:
                        if (r9 != 0) goto L_0x0365
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        int r9 = r9.x
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        android.graphics.Point r10 = r10.szWindow
                        int r10 = r10.x
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r3 = r3.floatingView
                        int r3 = r3.getWidth()
                        int r10 = r10 - r3
                        int r10 = r10 / r1
                        if (r9 >= r10) goto L_0x021a
                        r10 = 0
                        goto L_0x022d
                    L_0x021a:
                        com.ezscreenrecorder.FloatingService r10 = com.ezscreenrecorder.FloatingService.this
                        android.graphics.Point r10 = r10.szWindow
                        int r10 = r10.x
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r3 = r3.floatingView
                        int r3 = r3.getWidth()
                        int r10 = r10 - r3
                    L_0x022d:
                        android.content.SharedPreferences r3 = r1
                        if (r3 == 0) goto L_0x024c
                        android.content.SharedPreferences$Editor r3 = r3.edit()
                        java.lang.String r4 = "posX"
                        android.content.SharedPreferences$Editor r3 = r3.putInt(r4, r10)
                        com.ezscreenrecorder.FloatingService r4 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r4 = r4.layoutParamsMain
                        int r4 = r4.y
                        java.lang.String r5 = "posY"
                        android.content.SharedPreferences$Editor r3 = r3.putInt(r5, r4)
                        r3.apply()
                    L_0x024c:
                        int[] r1 = new int[r1]
                        r1[r0] = r9
                        r1[r2] = r10
                        android.animation.ValueAnimator r9 = android.animation.ValueAnimator.ofInt(r1)
                        com.ezscreenrecorder.FloatingService$40$3 r10 = new com.ezscreenrecorder.FloatingService$40$3
                        r10.<init>()
                        r9.addUpdateListener(r10)
                        r1 = 370(0x172, double:1.83E-321)
                        r9.setDuration(r1)
                        r9.start()
                        goto L_0x0365
                    L_0x0268:
                        long r2 = java.lang.System.currentTimeMillis()
                        r8.downTime = r2
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.os.Handler r9 = r9.handler
                        com.ezscreenrecorder.FloatingService r2 = com.ezscreenrecorder.FloatingService.this
                        java.lang.Runnable r2 = r2.runnable_circle
                        r9.removeCallbacks(r2)
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.widget.ImageView r9 = r9.imgFloat
                        r2 = 1065353216(0x3f800000, float:1.0)
                        r9.setAlpha(r2)
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain     // Catch:{ Exception -> 0x031b }
                        int r9 = r9.x     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.graphics.Point r3 = r3.szWindow     // Catch:{ Exception -> 0x031b }
                        int r3 = r3.x     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r4 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.View r4 = r4.floatingView     // Catch:{ Exception -> 0x031b }
                        int r4 = r4.getWidth()     // Catch:{ Exception -> 0x031b }
                        int r3 = r3 - r4
                        int r3 = r3 / r1
                        if (r9 >= r3) goto L_0x02a8
                        r9 = 0
                        goto L_0x02bb
                    L_0x02a8:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.graphics.Point r9 = r9.szWindow     // Catch:{ Exception -> 0x031b }
                        int r9 = r9.x     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.View r1 = r1.floatingView     // Catch:{ Exception -> 0x031b }
                        int r1 = r1.getWidth()     // Catch:{ Exception -> 0x031b }
                        int r9 = r9 - r1
                    L_0x02bb:
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager$LayoutParams r1 = r1.layoutParamsMain     // Catch:{ Exception -> 0x031b }
                        r1.x = r9     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        r3 = 52
                        int r1 = r1.dpToPx(r3)     // Catch:{ Exception -> 0x031b }
                        r9.width = r1     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        int r1 = r1.dpToPx(r3)     // Catch:{ Exception -> 0x031b }
                        r9.height = r1     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager r9 = r9.windowManager     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.View r1 = r1.floatingView     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r3 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.WindowManager$LayoutParams r3 = r3.layoutParamsMain     // Catch:{ Exception -> 0x031b }
                        r9.updateViewLayout(r1, r3)     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this     // Catch:{ Exception -> 0x031b }
                        android.view.View r9 = r9.floatingView     // Catch:{ Exception -> 0x031b }
                        android.view.ViewPropertyAnimator r9 = r9.animate()     // Catch:{ Exception -> 0x031b }
                        android.view.ViewPropertyAnimator r9 = r9.scaleX(r2)     // Catch:{ Exception -> 0x031b }
                        android.view.ViewPropertyAnimator r9 = r9.scaleY(r2)     // Catch:{ Exception -> 0x031b }
                        r1 = 300(0x12c, double:1.48E-321)
                        android.view.ViewPropertyAnimator r9 = r9.setDuration(r1)     // Catch:{ Exception -> 0x031b }
                        com.ezscreenrecorder.FloatingService$40$2 r1 = new com.ezscreenrecorder.FloatingService$40$2     // Catch:{ Exception -> 0x031b }
                        r1.<init>()     // Catch:{ Exception -> 0x031b }
                        android.view.ViewPropertyAnimator r9 = r9.setListener(r1)     // Catch:{ Exception -> 0x031b }
                        r9.start()     // Catch:{ Exception -> 0x031b }
                        goto L_0x031f
                    L_0x031b:
                        r9 = move-exception
                        r9.printStackTrace()
                    L_0x031f:
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        int r9 = r9.x
                        r8.initialX = r9
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager$LayoutParams r9 = r9.layoutParamsMain
                        int r9 = r9.y
                        r8.initialY = r9
                        float r9 = r10.getRawX()
                        r8.initialTouchX = r9
                        float r9 = r10.getRawY()
                        r8.initialTouchY = r9
                        android.os.Handler r9 = r8.handler_longClick
                        java.lang.Runnable r10 = r8.runnable_longClick
                        r1 = 450(0x1c2, double:2.223E-321)
                        r9.postDelayed(r10, r1)
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r9 = r9.floatingViewClose
                        int r9 = r9.getWidth()
                        r8.remove_img_width = r9
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r9 = r9.floatingViewClose
                        int r9 = r9.getHeight()
                        r8.remove_img_height = r9
                        com.ezscreenrecorder.FloatingService r9 = com.ezscreenrecorder.FloatingService.this
                        r9.avoidLongClick = r0
                    L_0x0365:
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.C074640.onTouch(android.view.View, android.view.MotionEvent):boolean");
                }
            });
            this.imgFloat.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (!FloatingService.this.avoidLongClick) {
                        int parseInt = Integer.parseInt(FloatingService.this.prefs.getString("example_list_long_click", AppEventsConstants.EVENT_PARAM_VALUE_NO));
                        if (parseInt != 0) {
                            ((Vibrator) FloatingService.this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
                            String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
                            if (parseInt == 1) {
                                Intent intent = new Intent(FloatingService.this, RecordingActivity.class);
                                intent.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                intent.addFlags(268435456);
                                FloatingService.this.startActivity(intent);
                            } else if (parseInt == 2) {
                                Intent intent2 = new Intent(FloatingService.this, RecordingActivity.class);
                                intent2.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                intent2.addFlags(268435456);
                                FloatingService.this.startActivity(intent2);
                            } else if (parseInt == 3) {
                                Intent intent3 = new Intent(FloatingService.this, RecordingActivity.class);
                                intent3.putExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
                                intent3.addFlags(268435456);
                                FloatingService.this.startActivity(intent3);
                            } else if (parseInt == 4) {
                                Intent intent4 = new Intent(FloatingService.this, GalleryActivity.class);
                                intent4.addFlags(268468224);
                                FloatingService.this.startActivity(intent4);
                            } else if (parseInt == 5) {
                                Intent intent5 = new Intent(FloatingService.this, GalleryActivity.class);
                                intent5.putExtra(GalleryActivity.SETTINGS_VIEW, true);
                                intent5.addFlags(268468224);
                                FloatingService.this.startActivity(intent5);
                            }
                        }
                    }
                    return true;
                }
            });
            this.height = (int) getResources().getDimension(R.dimen.button_height);
            this.width = (int) getResources().getDimension(R.dimen.button_width);
            this.radius = (int) getResources().getDimension(R.dimen.radius);
            this.mNewFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout2, null);
            this.mNewimgFloat = (ImageView) this.mNewFloatingView.findViewById(R.id.img_float);
            calculatePentagonVertices(this.radius, 11, this.mNewFloatingView);
            this.mNewFloatingView.setOnClickListener(this);
            this.mOrientationListener = new OrientationEventListener(this, 3) {
                public void onOrientationChanged(int i) {
                    if (FloatingService.this.mTextureView != null && FloatingService.this.mTextureView.isAvailable()) {
                        FloatingService floatingService = FloatingService.this;
                        floatingService.confirgureTransform2(floatingService.windowManager.getDefaultDisplay().getRotation(), FloatingService.this.mTextureView.getWidth(), FloatingService.this.mTextureView.getHeight());
                    }
                    FloatingService.this.screenOrientation = i;
                    boolean z = true;
                    if ((i <= 310 || i >= 360) && ((i < 0 || i >= 60) && (i <= 130 || i >= 240))) {
                        if (((i > 240 && i < 310) || (i > 60 && i < 130)) && !FloatingService.this.changeOrientation && FloatingService.mMediaProjection != null) {
                            FloatingService floatingService2 = FloatingService.this;
                            if ((floatingService2.recordTimeOrientation <= 310 || FloatingService.this.recordTimeOrientation >= 360) && ((FloatingService.this.recordTimeOrientation < 0 || FloatingService.this.recordTimeOrientation >= 60) && (FloatingService.this.recordTimeOrientation <= 130 || FloatingService.this.recordTimeOrientation >= 240))) {
                                z = false;
                            }
                            floatingService2.changeOrientation = z;
                        }
                    } else if (!FloatingService.this.changeOrientation && FloatingService.mMediaProjection != null) {
                        FloatingService floatingService3 = FloatingService.this;
                        if ((floatingService3.recordTimeOrientation <= 240 || FloatingService.this.recordTimeOrientation >= 310) && (FloatingService.this.recordTimeOrientation <= 60 || FloatingService.this.recordTimeOrientation >= 130)) {
                            z = false;
                        }
                        floatingService3.changeOrientation = z;
                    }
                    if (FloatingService.this.changeOrientation && FloatingService.this.mCameraBhavik != null) {
                        CameraPreview.setCameraDisplayOrientation(FloatingService.this.getApplicationContext(), 0, FloatingService.this.mCameraBhavik);
                    }
                }
            };
            if (this.mOrientationListener.canDetectOrientation()) {
                this.mOrientationListener.enable();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, R.string.show_bubble_error, 1).show();
        }
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        if (mMediaProjection != null) {
            return super.onStartCommand(intent, i, i2);
        }
        if (this.layoutParamsMain == null) {
            LayoutParams layoutParams = new LayoutParams(dpToPx(52), dpToPx(52), getTypeOfWindowManagerParam(), 264, -3);
            this.layoutParamsMain = layoutParams;
        }
        LayoutParams layoutParams2 = this.layoutParamsMain;
        layoutParams2.gravity = 51;
        layoutParams2.x = 10;
        layoutParams2.y = (this.windowManager.getDefaultDisplay().getHeight() / 2) - 50;
        try {
            this.windowManager.addView(this.floatingView, this.layoutParamsMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.windowManager.updateViewLayout(this.floatingView, this.layoutParamsMain);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isAllowTouch = true;
        this.isLongclick = false;
        if (MainActivity.showDirectly != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = intent;
                    if (intent != null) {
                        String str = FloatingService.EXTRA_STARTED_FROM_OTHER_APPS;
                        if (intent.hasExtra(str) && intent.getBooleanExtra(str, false)) {
                            FloatingService.this.whichAnimation = 1;
                        }
                    }
                    FloatingService.this.clicked();
                }
            }, 500);
        }
        return super.onStartCommand(intent, i, i2);
    }

    private void addCloseLay() {
        this.floatingViewClose = LayoutInflater.from(this).inflate(R.layout.floating_close_lay, null);
        LayoutParams layoutParams = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 8, -3);
        this.paramsCloseView = layoutParams;
        this.paramsCloseView.gravity = 51;
        int width2 = this.windowManager.getDefaultDisplay().getWidth();
        int height2 = this.windowManager.getDefaultDisplay().getHeight();
        this.paramsCloseView.x = (width2 / 2) - this.floatingViewClose.getWidth();
        LayoutParams layoutParams2 = this.paramsCloseView;
        layoutParams2.y = height2 - 100;
        try {
            this.windowManager.addView(this.floatingViewClose, layoutParams2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePentagonVertices(int i, int i2, View view) {
        this.pentagonVertices = new Point[6];
        Display defaultDisplay = this.windowManager.getDefaultDisplay();
        int width2 = defaultDisplay.getWidth() / 2;
        int height2 = defaultDisplay.getHeight() / 2;
        for (int i3 = 0; i3 < 6; i3++) {
            double d = (double) i;
            double d2 = ((double) i2) + ((((double) (i3 * 2)) * 3.141592653589793d) / 6.0d);
            this.pentagonVertices[i3] = new Point(((int) (Math.cos(d2) * d)) + width2, (((int) (d * Math.sin(d2))) + height2) - 100);
        }
        this.buttons = new PulsatorLayout[this.pentagonVertices.length];
        int i4 = 0;
        while (true) {
            PulsatorLayout[] pulsatorLayoutArr = this.buttons;
            if (i4 < pulsatorLayoutArr.length) {
                pulsatorLayoutArr[i4] = new PulsatorLayout(this);
                this.buttons[i4].setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
                this.buttons[i4].setX(0.0f);
                this.buttons[i4].setY(0.0f);
                this.buttons[i4].setTag(Integer.valueOf(i4));
                this.buttons[i4].getImageView().setScaleType(ScaleType.CENTER_INSIDE);
                this.buttons[i4].setVisibility(View.INVISIBLE);
                ((RelativeLayout) view.findViewById(R.id.floating_lay)).addView(this.buttons[i4]);
                if (i4 == 0) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_video_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, RecordingActivity.class);
                                    intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                    intent.addFlags(268435456);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                } else if (i4 == 1) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_camera_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, RecordingActivity.class);
                                    intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
                                    intent.addFlags(268435456);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                } else if (i4 == 2) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_gallery_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, GalleryActivity.class);
                                    intent.addFlags(268468224);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                } else if (i4 == 3) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_more_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, RecordingActivity.class);
                                    intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_MORE);
                                    intent.addFlags(268435456);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                } else if (i4 == 4) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_explainer_video_record_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, RecordingActivity.class);
                                    intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO);
                                    intent.addFlags(268435456);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                } else if (i4 == 5) {
                    this.buttons[i4].getImageView().setImageResource(R.drawable.ic_floating_go_live_selector);
                    this.buttons[i4].setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            FloatingService.this.clicked();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(FloatingService.this, LiveLoginActivity.class);
                                    intent.addFlags(268468224);
                                    FloatingService.this.startActivity(intent);
                                }
                            }, 260);
                        }
                    });
                }
                i4++;
            } else {
                return;
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        OrientationEventListener orientationEventListener = this.mOrientationListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        this.compositeDisposable.dispose();
        unregisterReceiver(this.broadcastReceiver);
        try {
            if (this.mNewFloatingView != null) {
                this.windowManager.removeView(this.mNewFloatingView);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            if (this.floatingView != null) {
                this.windowManager.removeView(this.floatingView);
            }
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
        try {
            if (this.sMuxer != null) {
                this.sMuxer.stopRecording();
                this.sMuxer = null;
            }
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.reset();
                stopScreenSharing();
            }
            this.mMediaRecorder = null;
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.mRecorder != null) {
                this.mRecorder.quit();
                this.mRecorder = null;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        this.mediaScreenRecorder = null;
        MediaProjection mediaProjection = mMediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
            mMediaProjection = null;
        }
        try {
            unregisterReceiver(this.broadcastReceiverOnOffScreen);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    public void onClick(View view) {
        if (!this.isAllowTouch) {
            return;
        }
        if (this.isLongclick || mMediaProjection != null) {
            this.isLongclick = false;
        } else {
            clicked();
        }
    }

    /* access modifiers changed from: private */
    public void clicked() {
        PulsatorLayout[] pulsatorLayoutArr;
        if (this.whichAnimation == 0) {
            LayoutParams layoutParams = new LayoutParams(-1, -1, getTypeOfWindowManagerParam(), 262920, -3);
            layoutParams.x = 0;
            layoutParams.y = 0;
            int i = this.layoutParamsMain.x;
            int i2 = this.layoutParamsMain.y;
            this.mNewimgFloat.setX((float) i);
            this.mNewimgFloat.setY((float) i2);
            this.mNewFloatingView.setBackgroundColor(Color.parseColor("#AA000000"));
            try {
                this.windowManager.addView(this.mNewFloatingView, layoutParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.windowManager.removeViewImmediate(this.floatingView);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.mNewimgFloat.setVisibility(View.VISIBLE);
            this.startPositionX = ((int) this.mNewimgFloat.getX()) + 50;
            this.startPositionY = ((int) this.mNewimgFloat.getY()) + 50;
            for (PulsatorLayout pulsatorLayout : this.buttons) {
                pulsatorLayout.setX((float) this.startPositionX);
                pulsatorLayout.setY((float) this.startPositionY);
                pulsatorLayout.setVisibility(View.VISIBLE);
            }
            if (this.textViewRecorHighLightMessage != null) {
                ((RelativeLayout) this.mNewFloatingView.findViewById(R.id.floating_lay)).removeView(this.textViewRecorHighLightMessage);
            }
            this.buttons[0].stopAnimation();
            int i3 = 0;
            while (true) {
                PulsatorLayout[] pulsatorLayoutArr2 = this.buttons;
                if (i3 < pulsatorLayoutArr2.length) {
                    playEnterAnimation(pulsatorLayoutArr2[i3], i3);
                    i3++;
                } else {
                    this.isAllowTouch = false;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FloatingService.this.isAllowTouch = true;
                            SharedPreferences sharedPreferences = FloatingService.this.getSharedPreferences(MainActivity.SHARED_NAME, 0);
                            String str = "first_time";
                            if (!sharedPreferences.contains(str) || sharedPreferences.getBoolean(str, false)) {
                                FloatingService.this.buttons[0].stopAnimation();
                                return;
                            }
                            FloatingService.this.buttons[0].getX();
                            int y = (int) FloatingService.this.buttons[0].getY();
                            FloatingService floatingService = FloatingService.this;
                            floatingService.textViewRecorHighLightMessage = new TextView(floatingService);
                            FloatingService.this.textViewRecorHighLightMessage.setText(R.string.tap_start_recording);
                            FloatingService.this.textViewRecorHighLightMessage.setTextColor(-1);
                            FloatingService.this.textViewRecorHighLightMessage.setPadding(5, 5, 5, 5);
                            FloatingService.this.textViewRecorHighLightMessage.setBackgroundColor(Color.parseColor("#20000000"));
                            FloatingService.this.textViewRecorHighLightMessage.setX(0.0f);
                            FloatingService.this.textViewRecorHighLightMessage.setGravity(17);
                            FloatingService.this.textViewRecorHighLightMessage.setY((float) (y - 140));
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                            layoutParams.addRule(14, -1);
                            ((RelativeLayout) FloatingService.this.mNewFloatingView.findViewById(R.id.floating_lay)).addView(FloatingService.this.textViewRecorHighLightMessage, layoutParams);
                            FloatingService.this.buttons[0].startAnimation();
                            sharedPreferences.edit().putBoolean(str, true).apply();
                        }
                    }, 490);
                    this.whichAnimation = 1;
                    return;
                }
            }
        } else {
            int i4 = 0;
            while (true) {
                PulsatorLayout[] pulsatorLayoutArr3 = this.buttons;
                if (i4 >= pulsatorLayoutArr3.length) {
                    break;
                }
                playExitAnimation(pulsatorLayoutArr3[i4], i4);
                i4++;
            }
            this.whichAnimation = 0;
            this.isAllowTouch = false;
            this.mNewimgFloat.setVisibility(View.INVISIBLE);
            try {
                this.windowManager.addView(this.floatingView, this.layoutParamsMain);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    int i;
                    try {
                        FloatingService.this.handler.removeCallbacks(FloatingService.this.runnable_circle);
                        try {
                            FloatingService.this.windowManager.removeViewImmediate(FloatingService.this.mNewFloatingView);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        int width = FloatingService.this.windowManager.getDefaultDisplay().getWidth();
                        int i2 = FloatingService.this.layoutParamsMain.x;
                        if (i2 < (width - FloatingService.this.floatingView.getWidth()) / 2) {
                            i = 0;
                        } else {
                            i = width - FloatingService.this.floatingView.getWidth();
                        }
                        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i2, i});
                        ofInt.addUpdateListener(new AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                FloatingService.this.layoutParamsMain.x = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                try {
                                    FloatingService.this.windowManager.updateViewLayout(FloatingService.this.floatingView, FloatingService.this.layoutParamsMain);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        ofInt.addListener(new AnimatorListener() {
                            public void onAnimationCancel(Animator animator) {
                            }

                            public void onAnimationRepeat(Animator animator) {
                            }

                            public void onAnimationStart(Animator animator) {
                            }

                            public void onAnimationEnd(Animator animator) {
                                FloatingService.this.handler.postDelayed(FloatingService.this.runnable_circle, 1500);
                            }
                        });
                        ofInt.setDuration(370);
                        ofInt.start();
                        SharedPreferences sharedPreferences = FloatingService.this.getSharedPreferences(MainActivity.SHARED_NAME, 0);
                        if (sharedPreferences != null) {
                            sharedPreferences.edit().putInt("posX", i).putInt("posY", FloatingService.this.layoutParamsMain.y).apply();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    FloatingService.this.isAllowTouch = true;
                }
            }, 250);
        }
    }

    private void playEnterAnimation(final PulsatorLayout pulsatorLayout, int i) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) (this.startPositionX + (pulsatorLayout.getLayoutParams().width / 2)), (float) this.pentagonVertices[i].x});
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.setX(((Float) valueAnimator.getAnimatedValue()).floatValue() - ((float) (pulsatorLayout.getLayoutParams().width / 2)));
                pulsatorLayout.requestLayout();
            }
        });
        ofFloat.setDuration(ANIMATION_DURATION);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{(float) (this.startPositionY + 5), (float) this.pentagonVertices[i].y});
        ofFloat2.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
                pulsatorLayout.requestLayout();
            }
        });
        ofFloat2.setDuration(ANIMATION_DURATION);
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{5, this.width});
        ofInt.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.getLayoutParams().width = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                pulsatorLayout.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                pulsatorLayout.requestLayout();
            }
        });
        ofInt.setDuration(ANIMATION_DURATION);
        animatorSet.play(ofFloat).with(ofFloat2).with(ofInt);
        animatorSet.setStartDelay((long) this.enterDelay[i]);
        animatorSet.start();
    }

    private void playExitAnimation(final PulsatorLayout pulsatorLayout, int i) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) (this.pentagonVertices[i].x - (pulsatorLayout.getLayoutParams().width / 2)), (float) this.startPositionX});
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.setX(((Float) valueAnimator.getAnimatedValue()).floatValue());
                pulsatorLayout.requestLayout();
            }
        });
        ofFloat.setDuration(ANIMATION_DURATION);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{(float) this.pentagonVertices[i].y, (float) (this.startPositionY + 5)});
        ofFloat2.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
                pulsatorLayout.requestLayout();
            }
        });
        ofFloat2.setDuration(ANIMATION_DURATION);
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.width, 5});
        ofInt.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pulsatorLayout.getLayoutParams().width = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                pulsatorLayout.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                pulsatorLayout.requestLayout();
            }
        });
        ofInt.setDuration(ANIMATION_DURATION);
        animatorSet.play(ofFloat).with(ofFloat2).with(ofInt);
        animatorSet.setStartDelay((long) this.exitDelay[i]);
        animatorSet.start();
    }

    private int getFrontCameraId() {
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 1 && i != this.cameraId) {
                return i;
            }
            if (cameraInfo.facing == 0 && i != this.cameraId) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    @SuppressLint({"ClickableViewAccessibility"})
    public void addSurfaceView() {
        try {
            final LayoutParams layoutParams = new LayoutParams(-2, -2, getTypeOfWindowManagerParam(), 520, -3);
            layoutParams.gravity = 51;
            layoutParams.x = 20;
            layoutParams.y = 100;
            this.mCameraTextureView = LayoutInflater.from(this).inflate(R.layout.camera_layout, null);
            final StickerCameraView2 stickerCameraView2 = (StickerCameraView2) this.mCameraTextureView.findViewById(R.id.camera_preview);
            C077858 r1 = new OnTouchListener() {
                private final Point mDisplaySize = new Point();
                private final PointF mInitialDown = new PointF();
                private final Point mInitialPosition = new Point();
                boolean mIsOnLeft;
                private long startTime;

                /* JADX WARNING: Code restructure failed: missing block: B:6:0x000d, code lost:
                    if (r5 != 3) goto L_0x00c4;
                 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public boolean onTouch(View r5, MotionEvent r6) {
                    /*
                        r4 = this;
                        int r5 = r6.getActionMasked()
                        r0 = 1
                        if (r5 == 0) goto L_0x0087
                        r1 = 2
                        if (r5 == r0) goto L_0x0053
                        if (r5 == r1) goto L_0x0011
                        r6 = 3
                        if (r5 == r6) goto L_0x0053
                        goto L_0x00c4
                    L_0x0011:
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r5 = r5.mCameraTextureView
                        android.os.IBinder r5 = r5.getWindowToken()
                        if (r5 == 0) goto L_0x00c4
                        android.graphics.Point r5 = r4.mInitialPosition
                        int r5 = r5.x
                        float r1 = r6.getRawX()
                        android.graphics.PointF r2 = r4.mInitialDown
                        float r2 = r2.x
                        float r1 = r1 - r2
                        int r1 = (int) r1
                        int r5 = r5 + r1
                        android.graphics.Point r1 = r4.mInitialPosition
                        int r1 = r1.y
                        float r6 = r6.getRawY()
                        android.graphics.PointF r2 = r4.mInitialDown
                        float r2 = r2.y
                        float r6 = r6 - r2
                        int r6 = (int) r6
                        int r1 = r1 + r6
                        android.view.WindowManager$LayoutParams r6 = r0
                        r6.x = r5
                        r6.y = r1
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager r5 = r5.windowManager
                        com.ezscreenrecorder.FloatingService r6 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r6 = r6.mCameraTextureView
                        android.view.WindowManager$LayoutParams r1 = r0
                        r5.updateViewLayout(r6, r1)
                        goto L_0x00c4
                    L_0x0053:
                        android.graphics.Point r5 = r4.mDisplaySize
                        int r5 = r5.x
                        com.ezscreenrecorder.FloatingService r6 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r6 = r6.mCameraTextureView
                        int r6 = r6.getWidth()
                        android.view.WindowManager$LayoutParams r2 = r0
                        int r2 = r2.x
                        int r6 = r6 / r1
                        int r2 = r2 + r6
                        int r5 = r5 / r1
                        if (r2 >= r5) goto L_0x006c
                        r5 = 1
                        goto L_0x006d
                    L_0x006c:
                        r5 = 0
                    L_0x006d:
                        r4.mIsOnLeft = r5
                        long r5 = java.lang.System.currentTimeMillis()
                        long r1 = r4.startTime
                        long r5 = r5 - r1
                        r1 = 300(0x12c, double:1.48E-321)
                        int r3 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
                        if (r3 >= 0) goto L_0x00c4
                        com.ezscreenrecorder.float_camera.StickerCameraView2 r5 = r0
                        boolean r6 = r5.isHidden()
                        r6 = r6 ^ r0
                        r5.setControlItemsHidden(r6)
                        goto L_0x00c4
                    L_0x0087:
                        long r1 = java.lang.System.currentTimeMillis()
                        r4.startTime = r1
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r5 = r5.mCameraTextureView
                        android.os.IBinder r5 = r5.getWindowToken()
                        if (r5 == 0) goto L_0x00aa
                        com.ezscreenrecorder.FloatingService r5 = com.ezscreenrecorder.FloatingService.this
                        android.view.WindowManager r5 = r5.windowManager
                        com.ezscreenrecorder.FloatingService r1 = com.ezscreenrecorder.FloatingService.this
                        android.view.View r1 = r1.mCameraTextureView
                        android.view.WindowManager$LayoutParams r2 = r0
                        r5.updateViewLayout(r1, r2)
                    L_0x00aa:
                        android.graphics.Point r5 = r4.mInitialPosition
                        android.view.WindowManager$LayoutParams r1 = r0
                        int r1 = r1.x
                        android.view.WindowManager$LayoutParams r2 = r0
                        int r2 = r2.y
                        r5.set(r1, r2)
                        android.graphics.PointF r5 = r4.mInitialDown
                        float r1 = r6.getRawX()
                        float r6 = r6.getRawY()
                        r5.set(r1, r6)
                    L_0x00c4:
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.C077858.onTouch(android.view.View, android.view.MotionEvent):boolean");
                }
            };
            stickerCameraView2.setOnScaleView(new StickerView.onScaleView() {
                int height = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
                int width = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;

                public void move(float f, float f2) {
                }

                public void onScale(int i, int i2) {
                    this.height = i;
                    this.width = i2;
                }

                public void scaleStart() {
                    if (FloatingService.this.mCameraTextureView != null && FloatingService.this.mCameraTextureView.getWindowToken() != null) {
                        LayoutParams layoutParams = layoutParams;
                        layoutParams.width = SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT;
                        layoutParams.height = SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT;
                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.mCameraTextureView, layoutParams);
                    }
                }

                public void scaleStop() {
                    if (FloatingService.this.mCameraTextureView != null && FloatingService.this.mCameraTextureView.getWindowToken() != null) {
                        LayoutParams layoutParams = layoutParams;
                        layoutParams.width = this.width;
                        layoutParams.height = this.height;
                        FloatingService.this.windowManager.updateViewLayout(FloatingService.this.mCameraTextureView, layoutParams);
                    }
                }

                public void closeCamera() {
                    FloatingService.this.cameraCloseByUser = true;
                    FloatingService.this.removeSurfaceView();
                }

                public void flipCamera() {
                    if (FloatingService.this.cameraBhavik != null) {
                        FloatingService.this.cameraBhavik.release();
                        FloatingService.this.parameter = null;


                        FloatingService.this.cameraBhavik = null;
                    }
                    try {
                        FloatingService.this.openCamera2(FloatingService.this.mTextureView.getWidth(), FloatingService.this.mTextureView.getHeight());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.mTextureView = (TextureView) stickerCameraView2.getMainView();
            this.mTextureView.setOnTouchListener(r1);
            boolean z = false;
            if (m14724c(0) && m14724c(1)) {
                stickerCameraView2.addFlip();
            }
            this.windowManager.addView(this.mCameraTextureView, layoutParams);
            this.mCameraTextureView.getLayoutParams().width = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
            this.mCameraTextureView.getLayoutParams().height = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
            stickerCameraView2.getLayoutParams().width = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
            stickerCameraView2.getLayoutParams().height = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
            this.cameraId = 0;
            this.mTextureView.setSurfaceTextureListener(new SurfaceTextureListener() {
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return true;
                }

                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                }

                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                    try {
                        FloatingService.this.openCamera2(i, i2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                    FloatingService floatingService = FloatingService.this;
                    floatingService.confirgureTransform2(floatingService.windowManager.getDefaultDisplay().getRotation(), i, i2);
                }
            });
            startBackgroundThread();
            if (!stickerCameraView2.isHidden()) {
                z = true;
            }
            stickerCameraView2.setControlItemsHidden(z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean m14724c(int i) {
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        int i2 = 0;
        while (i2 < numberOfCameras) {
            try {
                Camera.getCameraInfo(i2, cameraInfo);
                if (i == cameraInfo.facing) {
                    return true;
                }
                i2++;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void openCamera2(final int i, final int i2) throws IOException {
        try {
            SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
            if (surfaceTexture != null) {
                this.cameraId = getFrontCameraId();
                this.cameraBhavik = Camera.open(this.cameraId);
                if (this.cameraBhavik != null) {
                    setCameraParameter(i, i2);
                    surfaceTexture.setDefaultBufferSize(i, i2);
                    this.handler.post(new Runnable() {
                        public void run() {
                            FloatingService floatingService = FloatingService.this;
                            floatingService.confirgureTransform2(floatingService.windowManager.getDefaultDisplay().getRotation(), i, i2);
                        }
                    });
                    try {
                        this.cameraBhavik.setParameters(this.parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.cameraBhavik.setPreviewTexture(surfaceTexture);
                    this.parameter = this.cameraBhavik.getParameters();
                    this.cameraBhavik.startPreview();
                    return;
                }
                return;
            }
            throw new RuntimeException("Texture NULL");
        } catch (Exception e2) {
            e2.printStackTrace();
            Crashlytics.logException(e2);
            try {
                this.windowManager.removeViewImmediate(this.mCameraTextureView);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            Single.just(Boolean.valueOf(true)).delay(20, TimeUnit.MILLISECONDS).subscribe((Consumer<? super T>) new Consumer<Boolean>() {
                public void accept(Boolean bool) throws Exception {
                    Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
                    intent.putExtra(FloatingService.FLAG_SHOW_FLOATING, true);
                    intent.putExtra(FloatingService.FLAG_UNACCESS_CAMERA, true);
                    FloatingService.this.sendBroadcast(intent);
                }
            });
        }
    }

    private int m14716b(int i, int i2) {
        CameraInfo cameraInfo = new CameraInfo();
        try {
            Camera.getCameraInfo(i2, cameraInfo);
        } catch (Exception unused) {
        }
        if (cameraInfo.facing == 1) {
            return (360 - ((cameraInfo.orientation + i) % 360)) % 360;
        }
        return ((cameraInfo.orientation - i) + 360) % 360;
    }

    /* access modifiers changed from: private */
    public void confirgureTransform2(int i, int i2, int i3) {
        RectF rectF;
        if (this.mTextureView != null && this.mPreviewSize != null && this.cameraBhavik != null) {
            int m14715b = m14715b(i);
            try {
                this.cameraBhavik.setDisplayOrientation(m14716b(m14715b, this.cameraId));
            } catch (Throwable unused) {
            }
            Matrix matrix = new Matrix();
            float f = (float) i2;
            float f2 = (float) i3;
            RectF rectF2 = new RectF(0.0f, 0.0f, f, f2);
            if (90 == m14715b || 270 == m14715b) {
                rectF = new RectF(0.0f, 0.0f, (float) this.mPreviewSize.width, (float) this.mPreviewSize.height);
            } else {
                rectF = new RectF(0.0f, 0.0f, (float) this.mPreviewSize.height, (float) this.mPreviewSize.width);
            }
            float centerX = rectF2.centerX();
            float centerY = rectF2.centerY();
            rectF.offset(centerX - rectF.centerX(), centerY - rectF.centerY());
            matrix.setRectToRect(rectF2, rectF, ScaleToFit.FILL);
            float max = Math.max(f2 / ((float) this.mPreviewSize.height), f / ((float) this.mPreviewSize.width));
            matrix.postScale(max, max, centerX, centerY);
            this.mTextureView.setTransform(matrix);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0042, code lost:
        if (r0 != 3) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004b, code lost:
        if (r1 != 270) goto L_0x004e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setCameraParameter(int r9, int r10) {
        /*
            r8 = this;
            android.hardware.Camera r0 = r8.cameraBhavik
            if (r0 == 0) goto L_0x00ac
            android.hardware.Camera$Parameters r0 = r0.getParameters()
            r8.parameter = r0
            android.hardware.Camera$Parameters r0 = r8.parameter
            r1 = 256(0x100, float:3.59E-43)
            r0.setPictureFormat(r1)
            android.hardware.Camera$Parameters r0 = r8.parameter
            java.util.List r0 = r0.getSupportedPictureSizes()
            com.ezscreenrecorder.FloatingService$C3310b r1 = new com.ezscreenrecorder.FloatingService$C3310b
            r1.<init>()
            java.lang.Object r0 = java.util.Collections.max(r0, r1)
            r7 = r0
            android.hardware.Camera$Size r7 = (android.hardware.Camera.Size) r7
            android.view.WindowManager r0 = r8.windowManager
            android.view.Display r0 = r0.getDefaultDisplay()
            int r0 = r0.getRotation()
            android.hardware.Camera$CameraInfo r1 = new android.hardware.Camera$CameraInfo
            r1.<init>()
            int r2 = r8.cameraId     // Catch:{ Exception -> 0x0037 }
            android.hardware.Camera.getCameraInfo(r2, r1)     // Catch:{ Exception -> 0x0037 }
        L_0x0037:
            int r1 = r1.screenOrientation
            if (r0 == 0) goto L_0x0045
            r2 = 1
            if (r0 == r2) goto L_0x004e
            r2 = 2
            if (r0 == r2) goto L_0x0045
            r2 = 3
            if (r0 == r2) goto L_0x004e
            goto L_0x0052
        L_0x0045:
            r0 = 90
            if (r1 == r0) goto L_0x0052
            r0 = 270(0x10e, float:3.78E-43)
            if (r1 != r0) goto L_0x004e
            goto L_0x0052
        L_0x004e:
            if (r1 == 0) goto L_0x0052
            r0 = 180(0xb4, float:2.52E-43)
        L_0x0052:
            android.graphics.Point r0 = new android.graphics.Point
            r0.<init>()
            android.view.WindowManager r1 = r8.windowManager
            android.view.Display r1 = r1.getDefaultDisplay()
            r1.getSize(r0)
            int r1 = r0.x
            int r0 = r0.y
            r2 = 1920(0x780, float:2.69E-42)
            if (r1 > r2) goto L_0x006a
            r6 = r1
            goto L_0x006c
        L_0x006a:
            r6 = 1920(0x780, float:2.69E-42)
        L_0x006c:
            r1 = 1080(0x438, float:1.513E-42)
            if (r0 > r1) goto L_0x0072
            r5 = r0
            goto L_0x0074
        L_0x0072:
            r5 = 1080(0x438, float:1.513E-42)
        L_0x0074:
            android.hardware.Camera$Parameters r0 = r8.parameter
            java.util.List r2 = r0.getSupportedPictureSizes()
            r1 = r8
            r3 = r9
            r4 = r10
            android.hardware.Camera$Size r9 = r1.m14704a(r2, r3, r4, r5, r6, r7)
            android.hardware.Camera$Parameters r10 = r8.parameter
            int r0 = r9.width
            int r1 = r9.height
            r10.setPictureSize(r0, r1)
            r8.mPreviewSize = r9
            android.hardware.Camera$Parameters r9 = r8.parameter
            android.hardware.Camera$Size r10 = r8.mPreviewSize
            int r10 = r10.width
            android.hardware.Camera$Size r0 = r8.mPreviewSize
            int r0 = r0.height
            r9.setPreviewSize(r10, r0)
            android.hardware.Camera$Parameters r9 = r8.parameter
            java.util.List r9 = r9.getSupportedFocusModes()
            java.lang.String r10 = "continuous-video"
            boolean r9 = r9.contains(r10)
            if (r9 == 0) goto L_0x00ac
            android.hardware.Camera$Parameters r9 = r8.parameter
            r9.setFocusMode(r10)
        L_0x00ac:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.FloatingService.setCameraParameter(int, int):void");
    }

    private Size m14704a(List<Size> list, int i, int i2, int i3, int i4, Size size) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i5 = size.width;
        int i6 = size.height;
        for (Size size2 : list) {
            if (size2.width <= i3 && size2.height <= i4 && size2.height == (size2.width * i6) / i5) {
                if (size2.width < i || size2.height < i2) {
                    arrayList2.add(size2);
                } else {
                    arrayList.add(size2);
                }
            }
        }
        if (arrayList.size() > 0) {
            return (Size) Collections.min(arrayList, new C3310b());
        }
        if (arrayList2.size() > 0) {
            return (Size) Collections.max(arrayList2, new C3310b());
        }
        return (Size) list.get(0);
    }

    /* access modifiers changed from: private */
    public void removeSurfaceView() {
        try {
            if (this.mCameraBhavik != null) {
                this.mCameraBhavik.release();
                this.mCameraBhavik = null;
            }
            if (this.cameraBhavik != null) {
                this.cameraBhavik.release();
                this.parameter = null;
                this.cameraBhavik = null;
            }
            if (this.mCameraTextureView != null) {
                this.windowManager.removeView(this.mCameraTextureView);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform(int i, int i2) {
        RectF rectF;
        if (this.mTextureView != null && this.mPreviewSize != null) {
            int rotation = this.windowManager.getDefaultDisplay().getRotation();
            Matrix matrix = new Matrix();
            float f = (float) i;
            float f2 = (float) i2;
            RectF rectF2 = new RectF(0.0f, 0.0f, f, f2);
            float centerX = rectF2.centerX();
            float centerY = rectF2.centerY();
            if (1 == rotation || 3 == rotation) {
                rectF = new RectF(0.0f, 0.0f, (float) this.mPreviewSize.height, (float) this.mPreviewSize.width);
            } else {
                rectF = new RectF(0.0f, 0.0f, (float) this.mPreviewSize.width, (float) this.mPreviewSize.height);
            }
            rectF.offset(centerX - rectF.centerX(), centerY - rectF.centerY());
            matrix.setRectToRect(rectF2, rectF, ScaleToFit.FILL);
            float max = Math.max(f2 / ((float) this.mPreviewSize.height), f / ((float) this.mPreviewSize.width));
            matrix.postScale(max, max, centerX, centerY);
            this.mTextureView.setTransform(matrix);
        }
    }

    /* access modifiers changed from: private */
    public void startPreview() {
        if (this.mCameraDevice != null && this.mTextureView.isAvailable() && this.mPreviewSize != null) {
            try {
                closePreviewSession();
                SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(this.mPreviewSize.width, this.mPreviewSize.height);
                this.mPreviewBuilder = this.mCameraDevice.createCaptureRequest(1);
                Surface surface = new Surface(surfaceTexture);
                this.mPreviewBuilder.addTarget(surface);
                this.mCameraDevice.createCaptureSession(Arrays.asList(new Surface[]{surface}), new CameraCaptureSession.StateCallback() {
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        FloatingService.this.mPreviewSession = cameraCaptureSession;
                        FloatingService.this.updatePreview();
                    }

                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        Log.e("sda", "failde");
                    }
                }, this.mBackgroundHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startBackgroundThread() {
        this.mBackgroundThread = new HandlerThread("CameraBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new Handler(this.mBackgroundThread.getLooper());
    }

    /* access modifiers changed from: private */
    public void updatePreview() {
        if (this.mCameraDevice != null) {
            try {
                setUpCaptureRequestBuilder(this.mPreviewBuilder);
                new HandlerThread("CameraPreview").start();
                this.mPreviewSession.setRepeatingRequest(this.mPreviewBuilder.build(), null, this.mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpCaptureRequestBuilder(Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
    }

    private Size chooseVideoSize(Size[] sizeArr) {
        for (Size size : sizeArr) {
            if (size.width == (size.height * 4) / 3 && size.width <= 1080) {
                return size;
            }
        }
        Log.e("dfdsf", "Couldn't find any suitable video size");
        return sizeArr[sizeArr.length - 1];
    }

    private Size chooseOptimalSize(Size[] sizeArr, int i, int i2, Size size) {
        ArrayList arrayList = new ArrayList();
        int i3 = size.width;
        int i4 = size.height;
        for (Size size2 : sizeArr) {
            if (size2.height == (size2.width * i4) / i3 && size2.width >= i && size2.height >= i2) {
                arrayList.add(size2);
            }
        }
        if (arrayList.size() > 0) {
            return (Size) Collections.min(arrayList, new CompareSizesByArea());
        }
        Log.e("dfs", "Couldn't find any suitable preview size");
        return sizeArr[0];
    }

    /* access modifiers changed from: private */
    public void closeCamera() {
        try {
            this.mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (this.mCameraDevice != null) {
                this.mCameraDevice.close();
                this.mCameraDevice = null;
            }
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
            }
            if (this.sMuxer != null) {
                this.sMuxer.stopRecording();
                this.sMuxer = null;
            }
        } catch (InterruptedException e) {
            Crashlytics.logException(e);
            throw new RuntimeException("Interrupted while trying to lock cameraBhavik closing.");
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            this.mCameraOpenCloseLock.release();
            throw th;
        }
        this.mCameraOpenCloseLock.release();
    }

    /* access modifiers changed from: private */
    public void stopBackgroundThread() {
        try {
            if (this.mBackgroundThread != null) {
                this.mBackgroundThread.quitSafely();
                this.mBackgroundThread.join();
            }
            this.mBackgroundThread = null;
            this.mBackgroundHandler = null;
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        CameraCaptureSession cameraCaptureSession = this.mPreviewSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            this.mPreviewSession = null;
        }
    }
}
