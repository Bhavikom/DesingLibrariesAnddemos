package com.yasoka.eazyscreenrecord.utils;

import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
//import android.support.media.ExifInterface;
import android.text.TextUtils;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;*/
import com.google.gson.Gson;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;

import java.util.Calendar;

public class PreferenceHelper {
    public static final int EXTRA_DEFAULT_STORAGE_LOCATION_EXTERNAL = 1;
    public static final int EXTRA_DEFAULT_STORAGE_LOCATION_INTERNAL = 0;
    private static final String KEY_ACTION_COUNT = "no_of_actions_in_app";
    private static final String KEY_ANONYMOUS_ID = "AnonymousID";
    private static final String KEY_GAME_LIST_DATA_BACKUP = "game_list_backup";
    private static final String KEY_PREF_FACEBOOK_RESOLUTION = "example_list_resolution_facebook";
    private static final String KEY_PREF_FCM_ID = "pref_user_fcm_id";
    private static final String KEY_PREF_FIRST_ACTION_COMPLETED = "is_first_action_completed";
    private static final String KEY_PREF_LIVE_CAMERA_SETTING = "live_camera_setting";
    private static final String KEY_PREF_PUSH_NOTIFICATION_SETTINGS = "pref_push_notification_settings";
    private static final String KEY_PREF_PUSH_NOTIFICATION_USER_PROPERTY = "user_property_push_notification";
    private static final String KEY_PREF_RATING_DIALOG_SHOWED = "isRatingDone";
    private static final String KEY_PREF_RECORDING_BUBBLE_VISIBILITY = "notifications_bubble_recording_hide";
    private static final String KEY_PREF_RECORDING_COUNTDOWN = "example_list_count_down";
    private static final String KEY_PREF_RECORDING_FRAME_RATE = "example_list_frame_rate";
    private static final String KEY_PREF_RECORDING_ORIENTATION = "example_list_orientation";
    private static final String KEY_PREF_RECORDING_RESOLUTION = "example_list_resolution";
    private static final String KEY_PREF_RECORDING_TOUCH_VISIBILITY = "notifications_touches";
    private static final String KEY_PREF_RECORDING_TYPE = "example_list_recorder";
    private static final String KEY_PREF_RECORD_AUDIO = "notifications_audio";
    private static final String KEY_PREF_STORAGE_LOCATION = "storage_location_preference";
    private static final String KEY_PREF_TUTORIAL_AD_REWARD_DATE = "pref_tutorial_reward_date";
    private static final String KEY_PREF_TWITCH_RESOLUTION = "example_list_resolution_twitch";
    private static final String KEY_PREF_WATERMARK_VISIBILITY = "pref_show_watermark_on_video";
    private static final String KEY_PREF_YOUTUBE_RESOLUTION = "example_list_resolution_youtube";
    private static final String KEY_RECORD_ORIENTATION_DIALOG_DONT_SHOW_AGAIN = "pref_record_orientation_guide_do_not_show";
    private static final String KEY_SHOW_EXPLAINER_VIDEO_HELP_SCREEN = "explainer_video_help_screen";
    private static final String KEY_SHOW_INTERACTIVE_RECODING_CAMERA = "interactive_recording_camera_bubble";
    private static final String KEY_TWITCH_ACCESS_TOKEN = "access_token_twitch";
    private static final String KEY_TWITCH_USER_INFO = "user_info_twitch";
    private static final String KEY_USER_ID = "UserId";
    private static final String KEY_YOUTUBE_EMAIL_ACCOUNT = "youtube_account_email";
    private static final PreferenceHelper ourInstance = new PreferenceHelper();
    private SharedPreferences preferences;
    private SharedPreferences privatePreference;

    public static PreferenceHelper getInstance() {
        return ourInstance;
    }

    private PreferenceHelper() {
    }

    private void initPreference() {
        if (this.preferences == null) {
            this.preferences = PreferenceManager.getDefaultSharedPreferences(RecorderApplication.getInstance().getApplicationContext());
        }
    }

    private void initPrivatePreference() {
        if (this.privatePreference == null) {
            this.privatePreference = RecorderApplication.getInstance().getApplicationContext().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        }
    }

    public void setPrefRecordAudio(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_RECORD_AUDIO, z).apply();
    }

    public boolean getPrefRecordAudio() {
        initPreference();
        int i = VERSION.SDK_INT;
        String str = KEY_PREF_RECORD_AUDIO;
        if (i >= 23) {
            return this.preferences.getBoolean(str, false);
        }
        return this.preferences.getBoolean(str, true);
    }

    public void setPrefResolution(int i, String str) {
        initPreference();
        switch (i) {
            case Constants.TYPE_PREF_RESOLUTION_RECORDING /*1071*/:
                this.preferences.edit().putString(KEY_PREF_RECORDING_RESOLUTION, str).apply();
                return;
            case Constants.TYPE_PREF_RESOLUTION_FACEBOOK /*1072*/:
                this.preferences.edit().putString(KEY_PREF_FACEBOOK_RESOLUTION, str).apply();
                return;
            case Constants.TYPE_PREF_RESOLUTION_YOUTUBE /*1073*/:
                this.preferences.edit().putString(KEY_PREF_YOUTUBE_RESOLUTION, str).apply();
                return;
            case Constants.TYPE_PREF_RESOLUTION_TWITCH /*1074*/:
                this.preferences.edit().putString(KEY_PREF_TWITCH_RESOLUTION, str).apply();
                return;
            default:
                return;
        }
    }

    public String getPrefResolution(int i) {
        initPreference();
        String str = KEY_PREF_RECORDING_RESOLUTION;
        String str2 = "1280x720";
        switch (i) {
            case Constants.TYPE_PREF_RESOLUTION_RECORDING /*1071*/:
                return this.preferences.getString(str, str2);
            case Constants.TYPE_PREF_RESOLUTION_FACEBOOK /*1072*/:
                return this.preferences.getString(KEY_PREF_FACEBOOK_RESOLUTION, str2);
            case Constants.TYPE_PREF_RESOLUTION_YOUTUBE /*1073*/:
                return this.preferences.getString(KEY_PREF_YOUTUBE_RESOLUTION, str2);
            case Constants.TYPE_PREF_RESOLUTION_TWITCH /*1074*/:
                return this.preferences.getString(KEY_PREF_TWITCH_RESOLUTION, str2);
            default:
                return this.preferences.getString(str, str2);
        }
    }

    public boolean hasPrefResolution(int i) {
        initPreference();
        String str = KEY_PREF_RECORDING_RESOLUTION;
        switch (i) {
            case Constants.TYPE_PREF_RESOLUTION_RECORDING /*1071*/:
                return this.preferences.contains(str);
            case Constants.TYPE_PREF_RESOLUTION_FACEBOOK /*1072*/:
                return this.preferences.contains(KEY_PREF_FACEBOOK_RESOLUTION);
            case Constants.TYPE_PREF_RESOLUTION_YOUTUBE /*1073*/:
                return this.preferences.contains(KEY_PREF_YOUTUBE_RESOLUTION);
            case Constants.TYPE_PREF_RESOLUTION_TWITCH /*1074*/:
                return this.preferences.contains(KEY_PREF_TWITCH_RESOLUTION);
            default:
                return this.preferences.contains(str);
        }
    }

    public void setPrefRecordBubbleVisibility(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_RECORDING_BUBBLE_VISIBILITY, z).apply();
    }

    public boolean getPrefRecordBubbleVisibility() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_RECORDING_BUBBLE_VISIBILITY, false);
    }

    public void setPrefRecordTouchVisibility(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_RECORDING_TOUCH_VISIBILITY, z).apply();
    }

    public boolean getPrefRecordTouchVisibility() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_RECORDING_TOUCH_VISIBILITY, false);
    }

    public void setPrefRecordingOrientation(String str) {
        initPreference();
        this.preferences.edit().putString(KEY_PREF_RECORDING_ORIENTATION, str).apply();
    }

    public String getPrefRecordingOrientation() {
        initPreference();
        return this.preferences.getString(KEY_PREF_RECORDING_ORIENTATION, "Auto");
    }

    public void setPrefRecordingCountdown(String str) {
        initPreference();
        this.preferences.edit().putString(KEY_PREF_RECORDING_COUNTDOWN, str).apply();
    }

    public int getPrefRecordingCountdown() {
        initPreference();
        return Integer.parseInt(this.preferences.getString(KEY_PREF_RECORDING_COUNTDOWN, ExifInterface.GPS_MEASUREMENT_3D));
    }

    public void setPrefRatingDialogShowed(boolean z) {
        initPrivatePreference();
        this.privatePreference.edit().putBoolean(KEY_PREF_RATING_DIALOG_SHOWED, z).apply();
    }

    public boolean getPrefRatingDialogShowed() {
        initPrivatePreference();
        return this.privatePreference.getBoolean(KEY_PREF_RATING_DIALOG_SHOWED, false);
    }

    public void setPrefDefaultStorageLocation(int i) {
        initPreference();
        this.preferences.edit().putInt(KEY_PREF_STORAGE_LOCATION, i).apply();
    }

    public int getPrefDefaultStorageLocation() {
        initPreference();
        return this.preferences.getInt(KEY_PREF_STORAGE_LOCATION, 0);
    }

    public void setPrefFirstActionSent(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_FIRST_ACTION_COMPLETED, z).apply();
    }

    public boolean getPrefFirstActionSent() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_FIRST_ACTION_COMPLETED, false);
    }

    public void setPrefInteractiveRecodingCamera(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_SHOW_INTERACTIVE_RECODING_CAMERA, z).apply();
    }

    public boolean getPrefInteractiveRecodingCamera() {
        initPreference();
        return this.preferences.getBoolean(KEY_SHOW_INTERACTIVE_RECODING_CAMERA, false);
    }

    public void setPrefExplainerVideoHelpShowed(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_SHOW_EXPLAINER_VIDEO_HELP_SCREEN, z).apply();
    }

    public boolean getPrefExplainerVideoHelpShowed() {
        initPreference();
        return this.preferences.getBoolean(KEY_SHOW_EXPLAINER_VIDEO_HELP_SCREEN, false);
    }

    public void setPrefFcmId(String str) {
        initPreference();
        this.preferences.edit().putString(KEY_PREF_FCM_ID, str).apply();
    }

    public String getPrefFcmId() {
        initPreference();
        return this.preferences.getString(KEY_PREF_FCM_ID, null);
    }

    public void incrementActionCount() {
        initPreference();
        this.preferences.edit().putLong(KEY_ACTION_COUNT, getActionCount() + 1).apply();
    }

    public long getActionCount() {
        initPreference();
        return this.preferences.getLong(KEY_ACTION_COUNT, 0);
    }

    public void setPrefCanShowRecordingOrientationDialog(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_RECORD_ORIENTATION_DIALOG_DONT_SHOW_AGAIN, z).apply();
    }

    public boolean getPrefCanShowRecordingOrientationDialog() {
        initPreference();
        return this.preferences.getBoolean(KEY_RECORD_ORIENTATION_DIALOG_DONT_SHOW_AGAIN, true);
    }

    public void setPrefWatermarkVisibility(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_WATERMARK_VISIBILITY, z).apply();
    }

    public boolean getPrefWatermarkVisibility() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_WATERMARK_VISIBILITY, true);
    }

    public void setGameListData(String str) {
        initPreference();
        this.preferences.edit().putString(KEY_GAME_LIST_DATA_BACKUP, str).apply();
    }

    public String getGameListData() {
        initPreference();
        return this.preferences.getString(KEY_GAME_LIST_DATA_BACKUP, null);
    }

    public boolean hasGameListData() {
        initPreference();
        return !TextUtils.isEmpty(this.preferences.getString(KEY_GAME_LIST_DATA_BACKUP, null));
    }

    public void setTutorialAdRewardedDate() {
        initPreference();
        this.preferences.edit().putLong(KEY_PREF_TUTORIAL_AD_REWARD_DATE, Calendar.getInstance().getTimeInMillis()).apply();
    }

    private long getTutorialAdRewardedDate() {
        initPreference();
        return this.preferences.getLong(KEY_PREF_TUTORIAL_AD_REWARD_DATE, 0);
    }

    public boolean isEligibleForTutorialRewardAd() {
        long tutorialAdRewardedDate = getTutorialAdRewardedDate();
        boolean z = true;
        if (tutorialAdRewardedDate == 0) {
            return true;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(tutorialAdRewardedDate);
        Calendar instance2 = Calendar.getInstance();
        if (!instance2.after(instance) || instance2.get(5) == instance.get(5)) {
            z = false;
        }
        return z;
    }

    public void setPrefAnonymousId(String str) {
        initPrivatePreference();
        this.privatePreference.edit().putString("AnonymousID", str).apply();
    }

    public String getPrefAnonymousId() {
        initPrivatePreference();
        return this.privatePreference.getString("AnonymousID", "");
    }

    public void setPrefUserId(String str) {
        initPrivatePreference();
        this.privatePreference.edit().putString("UserId", str).apply();
    }

    public String getPrefUserId() {
        initPrivatePreference();
        return this.privatePreference.getString("UserId", "");
    }

    public void removePrefUserId() {
        initPrivatePreference();
        this.privatePreference.edit().remove("UserId").apply();
    }

    public void setPrefYoutubeEmailId(String str) {
        initPreference();
        this.preferences.edit().putString(KEY_YOUTUBE_EMAIL_ACCOUNT, str).apply();
    }

    public String getPrefYoutubeEmailId() {
        initPreference();
        return this.preferences.getString(KEY_YOUTUBE_EMAIL_ACCOUNT, RecorderApplication.getInstance().getString(C0793R.string.id_select_account_txt));
    }

    public boolean hasPrefYoutubeEmailId() {
        initPreference();
        return this.preferences.contains(KEY_YOUTUBE_EMAIL_ACCOUNT);
    }

    public void removePrefYoutubeEmailId() {
        initPreference();
        this.preferences.edit().remove(KEY_YOUTUBE_EMAIL_ACCOUNT).apply();
    }

    public void setTwitchAccessToken(String str) {
        initPrivatePreference();
        this.privatePreference.edit().putString(KEY_TWITCH_ACCESS_TOKEN, str).apply();
    }

    public String getTwitchAccessToken() {
        initPrivatePreference();
        return this.privatePreference.getString(KEY_TWITCH_ACCESS_TOKEN, "");
    }

    public void setLiveTwitchUserData(LiveTwitchUserOutputModel liveTwitchUserOutputModel) {
        String json = new Gson().toJson((Object) liveTwitchUserOutputModel);
        initPrivatePreference();
        this.privatePreference.edit().putString(KEY_TWITCH_USER_INFO, json).apply();
    }

    public LiveTwitchUserOutputModel getLiveTwitchUserData() {
        Gson gson = new Gson();
        initPrivatePreference();
        String string = this.privatePreference.getString(KEY_TWITCH_USER_INFO, "");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (LiveTwitchUserOutputModel) gson.fromJson(string, LiveTwitchUserOutputModel.class);
    }

    public void setPrefPushNotification(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_PUSH_NOTIFICATION_SETTINGS, z).apply();
    }

    public boolean getPrefPushNotification() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_PUSH_NOTIFICATION_SETTINGS, true);
    }

    public void setPrefNotificationUserProperty(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_PUSH_NOTIFICATION_USER_PROPERTY, z).apply();
    }

    public boolean getPrefNotificationUserProperty() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_PUSH_NOTIFICATION_USER_PROPERTY, false);
    }

    public void setPrefLiveCameraSetting(boolean z) {
        initPreference();
        this.preferences.edit().putBoolean(KEY_PREF_LIVE_CAMERA_SETTING, z).apply();
    }

    public boolean getPrefLiveCameraSetting() {
        initPreference();
        return this.preferences.getBoolean(KEY_PREF_LIVE_CAMERA_SETTING, false);
    }
}
