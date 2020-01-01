package com.yasoka.eazyscreenrecord.utils;

import android.os.Bundle;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.model.AppInputEventModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;
import p009io.reactivex.Single;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class FirebaseEventsNewHelper {
    private static final int KEY_AUDIO_SUCCESS_RECORDED = 13;
    private static final String KEY_AUDIO_SUCCESS_RECORDED_STR = "AudioRec";
    private static final int KEY_DRAW_ON_RECORD_INITIATED = 16;
    private static final String KEY_DRAW_ON_RECORD_INITIATED_STR = "DrawOnRec";
    private static final String KEY_GAME_INSTALL_INITIATED_STR = "GameInstallStart";
    private static final int KEY_GAME_INTSALL_INITIATED = 22;
    private static final int KEY_GAME_RECORD_INITIATED = 21;
    private static final String KEY_GAME_RECORD_INITIATED_STR = "GameRecStart";
    private static final int KEY_GAME_SUCCESS_RECORDED = 19;
    private static final String KEY_GAME_SUCCESS_RECORDED_STR = "GameRec";
    private static final int KEY_IMAGE_SUCCESS_EDIT = 12;
    private static final String KEY_IMAGE_SUCCESS_EDIT_STR = "ImageEdit";
    private static final int KEY_INTERACTIVE_SUCCESS_RECORDED = 15;
    private static final String KEY_INTERACTIVE_SUCCESS_RECORDED_STR = "InteractiveRec";
    private static final int KEY_LIVE_SUCCESSFULLY_RECORDED = 20;
    private static final String KEY_LIVE_SUCCESSFULLY_RECORDED_STR = "LiveRec";
    private static final int KEY_LOCAL_SCREENSHOT_VIEWED = 10;
    private static final String KEY_LOCAL_SCREENSHOT_VIEWED_STR = "ImageViewed";
    private static final int KEY_LOCAL_VIDEO_PLAYED = 4;
    private static final String KEY_LOCAL_VIDEO_PLAYED_STR = "VidPlay";
    private static final int KEY_PLAY_STORE_RATE_US_INITIATED = 18;
    private static final String KEY_PLAY_STORE_RATE_US_INITIATED_STR = "RateUS";
    private static final int KEY_REMOTE_SCREENSHOT_VIEWED = 11;
    private static final String KEY_REMOTE_SCREENSHOT_VIEWED_STR = "ImageViewedRemote";
    private static final int KEY_REMOTE_VIDEO_PLAYED = 5;
    private static final String KEY_REMOTE_VIDEO_PLAYED_STR = "VidPlayRemote";
    private static final int KEY_SCREENSHOT_TAKEN_SUCCESS = 9;
    private static final String KEY_SCREENSHOT_TAKEN_SUCCESS_STR = "ImageTaken";
    private static final int KEY_SHARE_INITIATED = 3;
    private static final String KEY_SHARE_INITIATED_STR = "Share";
    private static final int KEY_SOCIAL_INITIATED = 17;
    private static final String KEY_SOCIAL_INITIATED_STR = "Social";
    private static final int KEY_TUTORIAL_SUCCESS_RECORDED = 14;
    private static final String KEY_TUTORIAL_SUCCESS_RECORDED_STR = "TutorialRec";
    private static final int KEY_VIDEO_SUCCESSFULLY_RECORDED = 1;
    private static final String KEY_VIDEO_SUCCESSFULLY_RECORDED_STR = "VidRec";
    private static final int KEY_VIDEO_SUCCESSFULLY_TRIM = 6;
    private static final String KEY_VIDEO_SUCCESSFULLY_TRIM_STR = "VidTrim";
    private static final int KEY_VIDEO_UPLOAD_YOUTUBE_SUCCESS = 7;
    private static final String KEY_VIDEO_UPLOAD_YOUTUBE_SUCCESS_STR = "UploadYoutube";
    public static final String LIVE_RECORD_TYPE_FACEBOOK = "Facebook";
    public static final String LIVE_RECORD_TYPE_TWITCH = "Twitch";
    public static final String LIVE_RECORD_TYPE_YOUTUBE = "Youtube";
    public static final String SHARE_TYPE_APP = "App";
    public static final String SHARE_TYPE_AUDIO = "Audio";
    public static final String SHARE_TYPE_IMAGE = "Image";
    public static final String SHARE_TYPE_VIDEO = "Video";
    public static final String SOCIAL_TYPE_FACEBOOK = "Facebook";
    /* access modifiers changed from: private */
    public static FirebaseAnalytics firebaseAnalytics;
    private static final FirebaseEventsNewHelper ourInstance = new FirebaseEventsNewHelper();

    @Retention(RetentionPolicy.SOURCE)
    @interface EventType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface LiveRecType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface ShareType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface SocialType {
    }

    public static FirebaseEventsNewHelper getInstance() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(RecorderApplication.getInstance().getApplicationContext());
        }
        return ourInstance;
    }

    private FirebaseEventsNewHelper() {
    }

    private void sendFirebaseAnalytic(final String str, final Bundle bundle) {
        Single.timer(50, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Long>() {
            public void onSuccess(Long l) {
                if (FirebaseEventsNewHelper.firebaseAnalytics != null) {
                    FirebaseEventsNewHelper.firebaseAnalytics.logEvent(str, bundle);
                }
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    public void sendVideoRecordSuccessEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, str);
        bundle.putString("size", str2);
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_VIDEO_SUCCESSFULLY_RECORDED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(1));
        appInputEventModel.setDuration(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendLiveSuccessRecorded(String str) {
        Bundle bundle = new Bundle();
        String str2 = KEY_LIVE_SUCCESSFULLY_RECORDED_STR;
        bundle.putString(str2, str);
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(str2, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(20));
        appInputEventModel.setShareType(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendShareEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("share", str);
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_SHARE_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(3));
        appInputEventModel.setShareType(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendLocalVideoPlayEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_LOCAL_VIDEO_PLAYED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(4));
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendRemoteVideoPlayEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_REMOTE_VIDEO_PLAYED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(5));
        appInputEventModel.setYoutubeId(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendVideoTrimSuccessEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_VIDEO_SUCCESSFULLY_TRIM_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(6));
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendVideoUploadSuccessEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_VIDEO_UPLOAD_YOUTUBE_SUCCESS_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(7));
        appInputEventModel.setYoutubeId(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendScreenshotSuccessEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_SCREENSHOT_TAKEN_SUCCESS_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(9));
        appInputEventModel.setResolution(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendLocalScreenshotViewedEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_LOCAL_SCREENSHOT_VIEWED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(10));
        appInputEventModel.setResolution(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendRemoteScreenshotViewedEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_REMOTE_SCREENSHOT_VIEWED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(11));
        appInputEventModel.setImageId(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendScreenshotEditSuccessEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_IMAGE_SUCCESS_EDIT_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(12));
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendAudioRecordSuccessEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_AUDIO_SUCCESS_RECORDED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(13));
        appInputEventModel.setDuration(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendTutorialRecordSuccessEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_TUTORIAL_SUCCESS_RECORDED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(14));
        appInputEventModel.setDuration(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendInteractiveRecordSuccessEvent(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_INTERACTIVE_SUCCESS_RECORDED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(15));
        appInputEventModel.setDuration(str);
        appInputEventModel.setSize(str2);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendGameRecordSuccessEvent(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic("GameRec", bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(19));
        appInputEventModel.setDuration(str);
        appInputEventModel.setSize(str2);
        appInputEventModel.setPackageName(str3);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendGameRecordStartEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        bundle.putString("pkg_name", str);
        sendFirebaseAnalytic(KEY_GAME_RECORD_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(21));
        appInputEventModel.setPackageName(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendGameInstallStartEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        bundle.putString("pkg_name", str);
        sendFirebaseAnalytic(KEY_GAME_INSTALL_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(22));
        appInputEventModel.setPackageName(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendDrawWhileRecordingEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_DRAW_ON_RECORD_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(16));
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendSocialInitiatedEvent(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_SOCIAL_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(17));
        appInputEventModel.setSocialType(str);
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendPlayStoreRateUsEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("country_code", RecorderApplication.getCountryCode());
        sendFirebaseAnalytic(KEY_PLAY_STORE_RATE_US_INITIATED_STR, bundle);
        AppInputEventModel appInputEventModel = new AppInputEventModel();
        appInputEventModel.setType(String.valueOf(18));
        ServerAPI.getInstance().recordAppEvent(appInputEventModel);
    }

    public void sendNotificationUserProperty(int i) {
        try {
            if (firebaseAnalytics != null) {
                firebaseAnalytics.setUserProperty("Notification", String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
