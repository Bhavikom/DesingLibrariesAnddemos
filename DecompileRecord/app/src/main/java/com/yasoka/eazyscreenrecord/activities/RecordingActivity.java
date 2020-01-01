package com.yasoka.eazyscreenrecord.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;*/
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.alertdialogs.ActionMoreOptionDialog;
import com.ezscreenrecorder.alertdialogs.ActionMoreOptionDialog.OnActionSelectedListener;
import com.ezscreenrecorder.fragments.ShowConfirmationForRewardAdDialog;
import com.ezscreenrecorder.fragments.ShowConfirmationForRewardAdDialog.OnConfirmationResponseCallback;
import com.ezscreenrecorder.fragments.ShowRewardAdLoadingDialog;
import com.ezscreenrecorder.fragments.ShowRewardAdLoadingDialog.OnAdRewardedCallback;
import com.ezscreenrecorder.model.ActionMoreModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.ezscreenrecorder.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.StorageHelper;
import com.google.common.primitives.Ints;*/
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.alertdialogs.ActionMoreOptionDialog;
import com.yasoka.eazyscreenrecord.fragments.ShowConfirmationForRewardAdDialog;
import com.yasoka.eazyscreenrecord.fragments.ShowRewardAdLoadingDialog;
import com.yasoka.eazyscreenrecord.model.ActionMoreModel;
import com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.yasoka.eazyscreenrecord.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
/*import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;*/

public class RecordingActivity extends AppCompatActivity implements OnDismissListener {
    public static final String EXTRA_GAME_LIST_PACKAGE_NAME = "extra_pakg_name";
    private static final int PERMISSION_CODE = 1;
    private static final int REQUEST_CODE_EXPLAINER_VIDEO = 3351;
    private static final String TAG = "MainActivity";

    /* renamed from: aU */
    private Animation f96aU;

    /* renamed from: aV */
    private Animation f97aV;
    /* access modifiers changed from: private */
    public int actionType = -1;
    /* access modifiers changed from: private */
    public AnimatorSet animatorSet;
    /* access modifiers changed from: private */
    public AnimatorSet animatorSet1;
    /* access modifiers changed from: private */
    public ImageView checkBoxImage;
    private int count = 3;
    /* access modifiers changed from: private */
    public TextView f10189d;
    private String facebookStreamURLData;
    /* access modifiers changed from: private */
    public ImageView handImageView;
    /* access modifiers changed from: private */
    public boolean isAudioContinue;
    private boolean isCameraPermissionDialogShowed;
    private boolean isMicPermissionDialogShowed;
    /* access modifiers changed from: private */
    public boolean isRecordOrientationDialogShowed;
    private boolean isStoragePermissionDialogShowed;
    /* access modifiers changed from: private */
    public boolean isTutorialFeatureUnloacked = false;
    private MediaProjectionManager mProjectionManager;
    private OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            RecordingActivity recordingActivity = RecordingActivity.this;
            recordingActivity.m15280a(recordingActivity.checkBoxImage, iArr);
            RecordingActivity recordingActivity2 = RecordingActivity.this;
            recordingActivity2.m15280a(recordingActivity2.handImageView, iArr2);
            int width = RecordingActivity.this.checkBoxImage.getWidth();
            int height = RecordingActivity.this.checkBoxImage.getHeight();
            float translationX = RecordingActivity.this.handImageView.getTranslationX();
            float translationY = RecordingActivity.this.handImageView.getTranslationY();
            RecordingActivity recordingActivity3 = RecordingActivity.this;
            recordingActivity3.m15279a(recordingActivity3.handImageView, translationX, translationY, ((float) (width / 2)) + ((float) (iArr[0] - iArr2[0])) + translationX, ((float) (iArr[1] - iArr2[1])) + translationY + ((float) (height / 2)));
            RecordingActivity.this.recordingLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };
    private SharedPreferences prefs;
    /* access modifiers changed from: private */
    public FrameLayout recordingLayout;
    private SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public boolean shouldIgnoreMicPermission;
    private LiveTwitchFinalModel twitchFinalStreamModel;
    private File watermarkFile;
    private LiveYoutubeFinalModel youtubeFinalStreamModel;

    /* access modifiers changed from: protected */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_recording);
        if (FloatingService.mMediaProjection != null) {
            Toast.makeText(getApplicationContext(), "Recorder Already Recording.Please Stop Recording then Try again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        this.recordingLayout = (FrameLayout) findViewById(C0793R.C0795id.activity_recording);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        FloatingService.mScreenDensity = displayMetrics.densityDpi;
        this.sharedPreferences = getSharedPreferences(MainActivity.SHARED_NAME, 0);
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
        int i = this.actionType;
        if (!(i == 1343 || i == 1340 || !PreferenceHelper.getInstance().getPrefWatermarkVisibility())) {
            Single.create(new SingleOnSubscribe<Boolean>() {
                public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                    try {
                        RecordingActivity.this.createWatermark();
                        singleEmitter.onSuccess(Boolean.valueOf(true));
                    } catch (Exception e) {
                        singleEmitter.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                public void onError(Throwable th) {
                }

                public void onSuccess(Boolean bool) {
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void createWatermark() {
        try {
            this.watermarkFile = new File(AppUtils.getVideoWatermarkDir(getApplicationContext()), AppUtils.APP_WATERMARK_FILE_NAME);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        File file = this.watermarkFile;
        if (file != null && !file.exists()) {
            Bitmap bitmap = null;
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(C0793R.layout.fragment_watermark, null, false).findViewById(C0793R.C0795id.lay_watermark);
            if (linearLayout.getMeasuredHeight() <= 0) {
                bitmap = loadBitmapFromView(linearLayout);
            }
            if (bitmap != null) {
                try {
                    bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(this.watermarkFile));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private Bitmap loadBitmapFromView(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        Bitmap createBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return createBitmap;
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        RecorderApplication.getInstance().checkSDCardAvailablity();
        String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
        if (intent.hasExtra(str)) {
            this.actionType = intent.getIntExtra(str, -1);
            String str2 = FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA;
            if (intent.hasExtra(str2)) {
                int i = this.actionType;
                if (i == 1347 || i == 1350) {
                    this.facebookStreamURLData = intent.getStringExtra(str2);
                }
            }
            if (intent.hasExtra(str2) && this.actionType == 1348) {
                this.twitchFinalStreamModel = (LiveTwitchFinalModel) intent.getSerializableExtra(str2);
            } else if (intent.hasExtra(str2) && this.actionType == 1349) {
                this.youtubeFinalStreamModel = (LiveYoutubeFinalModel) intent.getSerializableExtra(str2);
            }
        }
        shareScreen();
    }

    private void getScreenResolution() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        FloatingService.DISPLAY_WIDTH = point.x;
        FloatingService.DISPLAY_HEIGHT = point.y;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        switch (i) {
            case Constants.REQUEST_CODE_STORAGE_PERMISSION /*1121*/:
                if (iArr.length <= 0) {
                    return;
                }
                if (iArr[0] == 0) {
                    shareScreen();
                    return;
                } else if (iArr[0] != -1) {
                    return;
                } else {
                    if (!this.isStoragePermissionDialogShowed) {
                        showStoragePermissionErrorDialog();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), C0793R.string.id_storage_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            case Constants.REQUEST_CODE_MIC_PERMISSION /*1122*/:
                if (iArr.length <= 0) {
                    return;
                }
                if (iArr[0] == 0) {
                    PreferenceHelper.getInstance().setPrefRecordAudio(true);
                    shareScreen();
                    return;
                } else if (iArr[0] == -1) {
                    PreferenceHelper.getInstance().setPrefRecordAudio(false);
                    if (!this.isMicPermissionDialogShowed) {
                        showMicPermissionErrorDialog();
                        return;
                    } else if (this.actionType != 1343) {
                        Toast.makeText(getApplicationContext(), C0793R.string.id_record_audio_permission_warning_toast_message, Toast.LENGTH_SHORT).show();
                        this.shouldIgnoreMicPermission = true;
                        shareScreen();
                        return;
                    } else {
                        Toast.makeText(getApplicationContext(), C0793R.string.id_record_audio_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                } else {
                    return;
                }
            case Constants.REQUEST_CODE_CAMERA_PERMISSION /*1123*/:
                if (iArr.length <= 0) {
                    return;
                }
                if (iArr[0] == 0) {
                    PreferenceHelper.getInstance().setPrefInteractiveRecodingCamera(true);
                    shareScreen();
                    return;
                } else if (iArr[0] == -1) {
                    PreferenceHelper.getInstance().setPrefInteractiveRecodingCamera(false);
                    if (!this.isCameraPermissionDialogShowed) {
                        showCameraPermissionErrorDialog();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), C0793R.string.id_camera_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private void showStoragePermissionErrorDialog() {
        new AlertDialog.Builder(this).setMessage((int) C0793R.string.id_storage_permission_failed_dialog_message).setPositiveButton((int) C0793R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (ActivityCompat.shouldShowRequestPermissionRationale(RecordingActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                    RecordingActivity.this.shareScreen();
                } else if (!RecordingActivity.this.isFinishing()) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addCategory("android.intent.category.DEFAULT");
                    StringBuilder sb = new StringBuilder();
                    sb.append("package:");
                    sb.append(RecordingActivity.this.getPackageName());
                    intent.setData(Uri.parse(sb.toString()));
                    intent.addFlags(268435456);
                    intent.addFlags(Ints.MAX_POWER_OF_TWO);
                    intent.addFlags(8388608);
                    RecordingActivity.this.startActivity(intent);
                    ActivityCompat.finishAfterTransition(RecordingActivity.this);
                }
            }
        }).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(RecordingActivity.this.getApplicationContext(), C0793R.string.id_storage_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                RecordingActivity.this.finish();
            }
        }).show();
        this.isStoragePermissionDialogShowed = true;
    }

    private void showMicPermissionErrorDialog() {
        new AlertDialog.Builder(this).setMessage((int) C0793R.string.id_record_audio_permission_failed_dialog_message).setPositiveButton((int) C0793R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (!RecordingActivity.this.isFinishing()) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RecordingActivity.this, "android.permission.RECORD_AUDIO")) {
                        RecordingActivity.this.shareScreen();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(RecordingActivity.this.getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                        intent.addFlags(268435456);
                        intent.addFlags(Ints.MAX_POWER_OF_TWO);
                        intent.addFlags(8388608);
                        RecordingActivity.this.startActivity(intent);
                        ActivityCompat.finishAfterTransition(RecordingActivity.this);
                    }
                }
            }
        }).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (RecordingActivity.this.actionType == 1343) {
                    Toast.makeText(RecordingActivity.this.getApplicationContext(), C0793R.string.id_record_audio_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                    RecordingActivity.this.finish();
                    return;
                }
                RecordingActivity.this.shouldIgnoreMicPermission = true;
                Toast.makeText(RecordingActivity.this.getApplicationContext(), C0793R.string.id_record_audio_permission_warning_toast_message, Toast.LENGTH_SHORT).show();
                RecordingActivity.this.shareScreen();
            }
        }).show();
        this.isMicPermissionDialogShowed = true;
    }

    private void showCameraPermissionErrorDialog() {
        new AlertDialog.Builder(this).setMessage((int) C0793R.string.id_camera_permission_failed_dialog_message).setPositiveButton((int) C0793R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!RecordingActivity.this.isFinishing()) {
                    dialogInterface.dismiss();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RecordingActivity.this, "android.permission.CAMERA")) {
                        RecordingActivity.this.shareScreen();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(RecordingActivity.this.getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                        intent.addFlags(268435456);
                        intent.addFlags(Ints.MAX_POWER_OF_TWO);
                        intent.addFlags(8388608);
                        RecordingActivity.this.startActivity(intent);
                    }
                }
            }
        }).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!RecordingActivity.this.isFinishing()) {
                    dialogInterface.dismiss();
                    Toast.makeText(RecordingActivity.this.getApplicationContext(), C0793R.string.id_camera_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                    RecordingActivity.this.finish();
                }
            }
        }).show();
    }

    private boolean checkForPermissions() {
        if (VERSION.SDK_INT >= 23) {
            boolean isStoragePermissionAvailable = isStoragePermissionAvailable();
            boolean isMicPermissionAvailable = isMicPermissionAvailable();
            boolean isCameraPermissionAvailable = isCameraPermissionAvailable();
            String str = "android.permission.RECORD_AUDIO";
            String str2 = "android.permission.WRITE_EXTERNAL_STORAGE";
            switch (this.actionType) {
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                    if (!isStoragePermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{str2}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
                        return true;
                    }
                    break;
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                    if (!isStoragePermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{str2}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
                        return true;
                    } else if (!isMicPermissionAvailable && !this.shouldIgnoreMicPermission) {
                        ActivityCompat.requestPermissions(this, new String[]{str}, Constants.REQUEST_CODE_MIC_PERMISSION);
                        return true;
                    } else if (this.actionType == 1342 && !isCameraPermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, Constants.REQUEST_CODE_CAMERA_PERMISSION);
                        return true;
                    }
                    break;
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                    if (!isStoragePermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{str2}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
                        return true;
                    } else if (!isMicPermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{str}, Constants.REQUEST_CODE_MIC_PERMISSION);
                        return true;
                    }
                    break;
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                    if (!isMicPermissionAvailable) {
                        ActivityCompat.requestPermissions(this, new String[]{str}, Constants.REQUEST_CODE_MIC_PERMISSION);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void shareScreen() {
        int i = this.actionType;
        boolean flag = false;
        if (i != 1344) {
            if (i == 1346) {
                if (getIntent() != null) {
                    Intent intent = getIntent();
                    String str = ActionMoreModel.KEY_IS_MORE_FROM_NOTIFICATION;
                    if (intent.hasExtra(str)) {
                        flag = getIntent().getBooleanExtra(str, false);
                    }
                }
                ActionMoreOptionDialog instance = ActionMoreOptionDialog.getInstance(flag);
                instance.setOnActionSelectedListener(new ActionMoreOptionDialog.OnActionSelectedListener() {
                    public void onActionSelected(ActionMoreModel actionMoreModel) {
                        switch (actionMoreModel.getActionType()) {
                            case ActionMoreModel.EXTRA_ACTION_MORE_TYPE_AUDIO /*7600*/:
                                RecordingActivity.this.actionType = FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO;
                                RecordingActivity.this.shareScreen();
                                return;
                            case ActionMoreModel.EXTRA_ACTION_MORE_TYPE_INTRACTIVE_VIDEO /*7601*/:
                                RecordingActivity.this.actionType = FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO;
                                RecordingActivity.this.shareScreen();
                                return;
                            case ActionMoreModel.EXTRA_ACTION_MORE_TYPE_WHITE_BOARD /*7602*/:
                                RecordingActivity.this.actionType = FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO;
                                RecordingActivity.this.shareScreen();
                                return;
                            case ActionMoreModel.EXTRA_ACTION_MORE_TYPE_GAME_RECORD /*7603*/:
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (!RecordingActivity.this.isFinishing()) {
                                            Intent intent = new Intent(RecordingActivity.this.getApplicationContext(), GalleryActivity.class);
                                            intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_SHOW_GAME_LIST_ACTIVITY);
                                            intent.addFlags(268435456);
                                            RecordingActivity.this.startActivity(intent);
                                            ActivityCompat.finishAfterTransition(RecordingActivity.this);
                                        }
                                    }
                                }, 50);
                                return;
                            default:
                                return;
                        }
                    }
                });
                instance.show(getSupportFragmentManager(), "ActionMoreOptionDialog");
                return;
            }
        } else if (!this.isTutorialFeatureUnloacked && PreferenceHelper.getInstance().isEligibleForTutorialRewardAd()) {
            ShowConfirmationForRewardAdDialog showConfirmationForRewardAdDialog = new ShowConfirmationForRewardAdDialog();
            showConfirmationForRewardAdDialog.setConfirmationData(ShowConfirmationForRewardAdDialog.EXTRA_CONFIRMATION_TYPE_TUTORIAL_FEATURE, new ShowConfirmationForRewardAdDialog.OnConfirmationResponseCallback() {
                public void onUserResponse(boolean z) {
                    if (z) {
                        ShowRewardAdLoadingDialog showRewardAdLoadingDialog = new ShowRewardAdLoadingDialog();
                        showRewardAdLoadingDialog.setOnAdRewardListener(new ShowRewardAdLoadingDialog.OnAdRewardedCallback() {
                            public void onAdRewarded(boolean z) {
                                if (z) {
                                    RecordingActivity.this.isTutorialFeatureUnloacked = true;
                                    PreferenceHelper.getInstance().setTutorialAdRewardedDate();
                                    RecordingActivity.this.shareScreen();
                                    return;
                                }
                                try {
                                    RecordingActivity.this.finish();
                                } catch (Exception unused) {
                                }
                            }
                        });
                        showRewardAdLoadingDialog.show(RecordingActivity.this.getSupportFragmentManager(), "DRAW_LOAD_AD");
                        return;
                    }
                    try {
                        RecordingActivity.this.finish();
                    } catch (Exception unused) {
                    }
                }
            });
            showConfirmationForRewardAdDialog.show(getSupportFragmentManager(), "DRAW_CONF_DIALOG");
            return;
        }
        if (!checkForPermissions()) {
            String str2 = "isFirstTime";
            if (!this.sharedPreferences.contains(str2) || this.sharedPreferences.getBoolean(str2, false) || this.actionType == 1343) {
                try {
                    StatFs statFs = new StatFs(StorageHelper.getInstance().getFileBasePath());
                    long availableBlocksLong = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
                    if (this.actionType != 1340 && availableBlocksLong < 10) {
                        Toast.makeText(getApplicationContext(), C0793R.string.id_low_memory_exception_recording_txt, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isMyServiceRunning(FloatingService.class)) {
                    MainActivity.showDirectly = null;
                    startService(new Intent(this, FloatingService.class));
                }
                myStuff();
                return;
            }
            this.recordingLayout.setBackgroundResource(C0793R.C0794drawable.recording_card_shape_white_padding);
            findViewById(C0793R.C0795id.permission_prompt_hand).setVisibility(View.VISIBLE);
            findViewById(C0793R.C0795id.frame_lay).setVisibility(View.VISIBLE);
            findViewById(C0793R.C0795id.btn_done).setVisibility(View.VISIBLE);
            findViewById(C0793R.C0795id.txt_msg).setVisibility(View.VISIBLE);
            m15298h();
            findViewById(C0793R.C0795id.btn_done).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RecordingActivity.this.recordingLayout.setVisibility(View.INVISIBLE);
                    RecordingActivity.this.findViewById(C0793R.C0795id.permission_prompt_hand).setVisibility(View.GONE);
                    RecordingActivity.this.findViewById(C0793R.C0795id.btn_done).setVisibility(View.GONE);
                    RecordingActivity.this.findViewById(C0793R.C0795id.txt_msg).setVisibility(View.GONE);
                    RecordingActivity.this.shareScreen();
                }
            });
            this.sharedPreferences.edit().putBoolean(str2, true).apply();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 1) {
                try {
                    FloatingService.mMediaProjection = this.mProjectionManager.getMediaProjection(i2, intent);
                    myStuff();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    finish();
                }
            } else if (i == REQUEST_CODE_EXPLAINER_VIDEO) {
                PreferenceHelper.getInstance().setPrefExplainerVideoHelpShowed(true);
                myStuff();
            }
        } else if (i == 1) {
            Toast.makeText(this, C0793R.string.id_ask_permision_for_recording_msg, Toast.LENGTH_SHORT).show();
            finish();
        } else if (i == REQUEST_CODE_EXPLAINER_VIDEO) {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doNotShowAgainDialog() {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(C0793R.layout.checkbox, null);
        final CheckBox checkBox = (CheckBox) inflate.findViewById(C0793R.C0795id.skip);
        builder.setView(inflate);
        builder.setTitle((CharSequence) getString(C0793R.string.app_name));
        builder.setMessage((CharSequence) "Use Front Camera for User Interaction while Recording.");
        builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Editor edit = defaultSharedPreferences.edit();
                edit.putBoolean("skipMessage", checkBox.isChecked());
                edit.putBoolean("notifications_user_interaction", true);
                edit.commit();
                RecordingActivity.this.myStuff();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (OnClickListener) null);
        String str = "skipMessage";
        boolean z = defaultSharedPreferences.getBoolean(str, false);
        String str2 = "notifications_user_interaction";
        if (z && !defaultSharedPreferences.getBoolean(str2, true)) {
            Editor edit = defaultSharedPreferences.edit();
            edit.putBoolean(str, false);
            edit.commit();
        }
        if (z || !defaultSharedPreferences.getBoolean(str2, true)) {
            myStuff();
        } else {
            builder.show();
        }
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void myStuff() {
        if (isOrientationDifferent()) {
            showOrientationDifferentDialog();
        } else if (!PreferenceHelper.getInstance().getPrefExplainerVideoHelpShowed() && this.actionType == 1344) {
            startActivityForResult(new Intent(getApplicationContext(), ExplainerVideoHelpActivity.class), REQUEST_CODE_EXPLAINER_VIDEO);
        } else if (FloatingService.mMediaProjection != null || this.actionType == 1343) {
            startRecord();
        } else {
            try {
                startActivityForResult(this.mProjectionManager.createScreenCaptureIntent(), 1);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, C0793R.string.media_proj_support_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

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
    private boolean m16ac() throws Throwable {
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
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.activities.RecordingActivity.m16ac():boolean");
    }

    /* access modifiers changed from: private */
    public void startRecord() {
        if (this.actionType != 1340) {
            try {
                boolean ac = m16ac();
                boolean isCameraUseByApp = isCameraUseByApp();
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("VIDEO-->");
                sb.append(isCameraUseByApp);
                printStream.println(sb.toString());
                if (!ac && !this.isAudioContinue && isCameraUseByApp && PreferenceHelper.getInstance().getPrefRecordAudio()) {
                    new AlertDialog.Builder(this).setTitle((int) C0793R.string.app_name).setMessage((int) C0793R.string.audio_video_already_used).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (FloatingService.mMediaProjection != null) {
                                FloatingService.mMediaProjection.stop();
                            }
                            FloatingService.mMediaProjection = null;
                            RecordingActivity.this.finish();
                        }
                    }).setPositiveButton((int) C0793R.string.audio_already_continue, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            RecordingActivity.this.isAudioContinue = true;
                            RecordingActivity.this.startRecord();
                        }
                    }).show();
                    return;
                } else if (!ac && !this.isAudioContinue && PreferenceHelper.getInstance().getPrefRecordAudio()) {
                    new AlertDialog.Builder(this).setTitle((int) C0793R.string.app_name).setMessage((int) C0793R.string.audio_already_used).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (FloatingService.mMediaProjection != null) {
                                FloatingService.mMediaProjection.stop();
                            }
                            FloatingService.mMediaProjection = null;
                            RecordingActivity.this.finish();
                        }
                    }).setPositiveButton((int) C0793R.string.audio_already_continue, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            RecordingActivity.this.isAudioContinue = true;
                            RecordingActivity.this.startRecord();
                        }
                    }).show();
                    return;
                } else if (isCameraUseByApp && !this.isAudioContinue) {
                    new AlertDialog.Builder(this).setTitle((int) C0793R.string.app_name).setMessage((int) C0793R.string.video_already_used).setNegativeButton((int) C0793R.string.cancel, (OnClickListener) new OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (FloatingService.mMediaProjection != null) {
                                FloatingService.mMediaProjection.stop();
                            }
                            FloatingService.mMediaProjection = null;
                            RecordingActivity.this.finish();
                        }
                    }).setPositiveButton((int) C0793R.string.audio_already_continue, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            RecordingActivity.this.isAudioContinue = true;
                            RecordingActivity.this.startRecord();
                        }
                    }).show();
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        String str = null;
        int i = this.actionType;
        String str2 = EXTRA_GAME_LIST_PACKAGE_NAME;
        if (i == 1345 && getIntent() != null && getIntent().hasExtra(str2)) {
            str = getIntent().getStringExtra(str2);
            if (!TextUtils.isEmpty(str)) {
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(str);
                if (launchIntentForPackage != null) {
                    startActivity(launchIntentForPackage);
                } else {
                    finish();
                    finishAffinity();
                    return;
                }
            }
        }
        this.isAudioContinue = false;
        getScreenResolution();
        Intent intent = new Intent(FloatingService.FLAG_RUNNING_SERVICE);
        intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, this.actionType);
        int i2 = this.actionType;
        String str3 = FloatingService.KEY_LIVE_VIDEO_STREAM_URL_DATA;
        switch (i2) {
            case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                if (!TextUtils.isEmpty(str)) {
                    intent.putExtra(str2, str);
                    break;
                }
                break;
            case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_TIMELINE /*1347*/:
            case FloatingService.EXTRA_MAIN_ACTION_TYPE_FACEBOOK_LIVE_RECORD_ON_PAGE /*1350*/:
                if (!TextUtils.isEmpty(this.facebookStreamURLData)) {
                    intent.putExtra(str3, this.facebookStreamURLData);
                    break;
                }
                break;
            case FloatingService.EXTRA_MAIN_ACTION_TYPE_TWITCH_LIVE_RECORD /*1348*/:
                LiveTwitchFinalModel liveTwitchFinalModel = this.twitchFinalStreamModel;
                if (liveTwitchFinalModel != null) {
                    intent.putExtra(str3, liveTwitchFinalModel);
                    break;
                }
                break;
            case FloatingService.EXTRA_MAIN_ACTION_TYPE_YOUTUBE_LIVE_RECORD /*1349*/:
                LiveYoutubeFinalModel liveYoutubeFinalModel = this.youtubeFinalStreamModel;
                if (liveYoutubeFinalModel != null) {
                    intent.putExtra(str3, liveYoutubeFinalModel);
                    break;
                }
                break;
        }
        intent.putExtra(FloatingService.FLAG_SHOW_FLOATING, false);
        sendBroadcast(intent);
        finish();
        finishAffinity();
    }

    private void showOrientationDifferentDialog() {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(C0793R.layout.custom_checkbox_layout, null, false);
        CheckBox checkBox = (CheckBox) inflate.findViewById(C0793R.C0795id.id_checkbox);
        checkBox.setText(C0793R.string.id_dont_show_again_txt);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceHelper.getInstance().setPrefCanShowRecordingOrientationDialog(!z);
            }
        });
        int i = getResources().getConfiguration().orientation;
        String str = i == 1 ? getString(C0793R.string.id_alert_wrong_orientation_portrait) : i == 2 ? getString(C0793R.string.id_alert_wrong_orientation_landscape) : "";
        if (!TextUtils.isEmpty(str)) {
            new AlertDialog.Builder(this).setMessage((CharSequence) str).setView(inflate).setPositiveButton((int) C0793R.string.f83ok, (OnClickListener) new OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                public void onClick(DialogInterface dialogInterface, int i) {
                    RecordingActivity.this.isRecordOrientationDialogShowed = true;
                    RecordingActivity.this.myStuff();
                }
            }).setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    RecordingActivity.this.isRecordOrientationDialogShowed = true;
                }
            }).setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    RecordingActivity.this.isRecordOrientationDialogShowed = true;
                }
            }).show();
        }
    }

    private boolean isOrientationDifferent() {
        String prefRecordingOrientation = PreferenceHelper.getInstance().getPrefRecordingOrientation();
        boolean z = false;
        if (prefRecordingOrientation.equals("Auto")) {
            return false;
        }
        String[] stringArray = getResources().getStringArray(C0793R.array.pref_orientation_list_values);
        int i = getResources().getConfiguration().orientation;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= stringArray.length) {
                break;
            } else if (!prefRecordingOrientation.equals(stringArray[i3])) {
                i3++;
            } else if (i3 == 1) {
                i2 = 1;
            } else if (i3 == 2) {
                i2 = 2;
            }
        }
        boolean z2 = i != i2;
        int i4 = this.actionType;
        if ((i4 == 1342 || i4 == 1341 || i4 == 1345) && z2 && PreferenceHelper.getInstance().getPrefCanShowRecordingOrientationDialog() && !this.isRecordOrientationDialogShowed) {
            z = true;
        }
        return z;
    }

    private int getFrontCameraId() {
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 1) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCameraUseByApp() {
        Camera camera;
        if (!isCameraPermissionAvailable()) {
            return false;
        }
        try {
            int frontCameraId = getFrontCameraId();
            if (frontCameraId != -1) {
                camera = Camera.open(frontCameraId);
            } else {
                camera = Camera.open(0);
            }
            if (camera != null) {
                camera.release();
            }
            return false;
        } catch (RuntimeException unused) {
            return true;
        }
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onBackPressed() {
        if (this.actionType == 1340 || this.count <= 0) {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onDismiss(DialogInterface dialogInterface) {
        myStuff();
    }

    private void m15298h() {
        this.checkBoxImage = (ImageView) findViewById(C0793R.C0795id.permission_prompt_checkbox);
        this.f10189d = (TextView) findViewById(C0793R.C0795id.permission_prompt_start_now);
        this.handImageView = (ImageView) findViewById(C0793R.C0795id.permission_prompt_hand);
        this.recordingLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
    }

    /* access modifiers changed from: private */
    public void m15280a(View view, int[] iArr) {
        view.getLocationOnScreen(iArr);
    }

    /* access modifiers changed from: private */
    public void m15279a(View view, float f, float f2, float f3, float f4) {
        Handler handler = new Handler();
        final View view2 = view;
        final float f5 = f;
        final float f6 = f3;
        final float f7 = f2;
        final float f8 = f4;
        Runnable r1 = new Runnable() {

            /* renamed from: com.ezscreenrecorder.activities.RecordingActivity$24$C34661 */
            class C34661 extends AnimatorListenerAdapter {
                C34661() {
                }

                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    RecordingActivity.this.checkBoxImage.setImageResource(C0793R.C0794drawable.ic_check_box);
                    int[] iArr = new int[2];
                    int[] iArr2 = new int[2];
                    RecordingActivity.this.m15280a(RecordingActivity.this.f10189d, iArr);
                    RecordingActivity.this.m15280a(RecordingActivity.this.handImageView, iArr2);
                    int width = RecordingActivity.this.f10189d.getWidth();
                    int height = RecordingActivity.this.f10189d.getHeight();
                    float translationX = RecordingActivity.this.handImageView.getTranslationX();
                    float translationY = RecordingActivity.this.handImageView.getTranslationY();
                    RecordingActivity.this.m15289b(RecordingActivity.this.handImageView, translationX, translationY, ((float) (width / 2)) + ((float) (iArr[0] - iArr2[0])) + translationX, ((float) (iArr[1] - iArr2[1])) + translationY + ((float) (height / 2)));
                }
            }

            public void run() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{f5, f6});
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, new float[]{f7, f8});
                RecordingActivity.this.animatorSet = new AnimatorSet();
                RecordingActivity.this.animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
                RecordingActivity.this.animatorSet.addListener(new C34661());
                RecordingActivity.this.animatorSet.setDuration(800);
                RecordingActivity.this.animatorSet.start();
            }
        };
        handler.postDelayed(r1, 100);
    }

    /* access modifiers changed from: private */
    public void m15289b(View view, float f, float f2, float f3, float f4) {
        Handler handler = new Handler();
        final View view2 = view;
        final float f5 = f;
        final float f6 = f3;
        final float f7 = f2;
        final float f8 = f4;
        Runnable r1 = new Runnable() {

            /* renamed from: com.ezscreenrecorder.activities.RecordingActivity$25$C34681 */
            class C34681 extends AnimatorListenerAdapter {
                C34681() {
                }

                public void onAnimationEnd(Animator animator) {
                    RecordingActivity.this.f10189d.setSelected(true);
                    super.onAnimationEnd(animator);
                }
            }

            public void run() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{f5, f6});
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, new float[]{f7, f8});
                RecordingActivity.this.animatorSet1 = new AnimatorSet();
                RecordingActivity.this.animatorSet1.playTogether(new Animator[]{ofFloat, ofFloat2});
                RecordingActivity.this.animatorSet1.addListener(new C34681());
                RecordingActivity.this.animatorSet1.setDuration(1500);
                RecordingActivity.this.animatorSet1.start();
            }
        };
        handler.postDelayed(r1, 200);
    }

    private void m15299i() {
        AnimatorSet animatorSet2 = this.animatorSet;
        if (animatorSet2 != null) {
            animatorSet2.removeAllListeners();
            this.animatorSet.end();
            this.animatorSet.cancel();
        }
        AnimatorSet animatorSet3 = this.animatorSet1;
        if (animatorSet3 != null) {
            animatorSet3.removeAllListeners();
            this.animatorSet1.end();
            this.animatorSet1.cancel();
        }
    }

    private boolean isStoragePermissionAvailable() {
        return ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private boolean isMicPermissionAvailable() {
        return ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") == 0;
    }

    private boolean isCameraPermissionAvailable() {
        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0;
    }
}
