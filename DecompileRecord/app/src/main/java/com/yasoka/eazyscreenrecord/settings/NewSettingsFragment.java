package com.yasoka.eazyscreenrecord.settings;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.SwitchCompat;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ezscreenrecorder.BuildConfig;
//import com.yasoka.eazyscreenrecord.R;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.SettingsActivity;
import com.yasoka.eazyscreenrecord.model.EventBusTypes;
import com.yasoka.eazyscreenrecord.model.SharedDataForOtherApp;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.YoutubeAPI;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.SettingsActivity;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.model.SharedDataForOtherApp;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.EEAConsentHelper.OnEEAConsentListener;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.facebook.appevents.AppEventsConstants;
import com.google.common.primitives.Ints;*/
import java.io.PrintStream;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
/*import p009io.reactivex.SingleObserver;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.observers.DisposableSingleObserver;*/

public class NewSettingsFragment extends Fragment implements OnDismissListener {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 344;
    private static final int ACTION_MANAGE_WRITE_PERMISSION_REQUEST_CODE = 343;
    public static final String TAG = "NewSettingsFragment";
    private TextView adSettingTypeTxt;
    private TextView bitRate;
    private TextView countDown;
    private LinearLayout countdownTimerLayout;
    private TextView defStorageTxtView;
    private TextView floatBubbleTextView;
    private TextView frameRate;
    private InternetStateListener internetStateListener;
    /* access modifiers changed from: private */
    public boolean isLogoutOrChange;
    private boolean isManualChangeForInteractiveRecordingSwitch = false;
    private boolean isManualChangeForShowTouchSwitch = false;
    private View layFloating;
    private SwitchCompat lockStopRecording;
    private TextView longClickTextView;
    private SwitchCompat notification_switch;
    private TextView orientation;
    private LinearLayout pauseButtonControlsLayout;
    private SwitchCompat pauseRecording;
    /* access modifiers changed from: private */
    public SharedPreferences prefs;
    /* access modifiers changed from: private */
    public SwitchCompat recordAudio;
    private TextView resolution;
    private SwitchCompat touch;
    private SwitchCompat watermark;
    /* access modifiers changed from: private */
    public TextView youTube;

    private class InternetStateListener extends BroadcastReceiver {
        private AlertDialog dialog;
        private boolean isRegistered;

        private InternetStateListener() {
            this.isRegistered = false;
        }

        public void onReceive(Context context, Intent intent) {
            if (!NewSettingsFragment.this.isAdded()) {
                return;
            }
            if (RecorderApplication.getInstance().isNetworkAvailable()) {
                closeInternetUnavailableDialog();
                unregisterListener(NewSettingsFragment.this.getActivity());
                return;
            }
            showInternetUnavailableDialog();
        }

        public void registerListener(Context context) {
            if (context != null && !this.isRegistered) {
                context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.isRegistered = true;
            }
        }

        public void unregisterListener(Context context) {
            if (context != null && this.isRegistered) {
                context.unregisterReceiver(this);
                this.isRegistered = false;
            }
        }

        private void showInternetUnavailableDialog() {
            this.dialog = new AlertDialog.Builder(NewSettingsFragment.this.getActivity()).setMessage((int) R.string.id_consent_internet_not_avialable_error).setPositiveButton((int) R.string.txt_settings, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (NewSettingsFragment.this.isAdded()) {
                        NewSettingsFragment.this.getActivity().startActivity(new Intent("android.settings.SETTINGS"));
                        InternetStateListener internetStateListener = InternetStateListener.this;
                        internetStateListener.unregisterListener(NewSettingsFragment.this.getActivity());
                        dialogInterface.dismiss();
                    }
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (NewSettingsFragment.this.isAdded()) {
                        InternetStateListener internetStateListener = InternetStateListener.this;
                        internetStateListener.unregisterListener(NewSettingsFragment.this.getActivity());
                        dialogInterface.dismiss();
                    }
                }
            }).create();
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return keyEvent.getKeyCode() == 4;
                }
            });
            AlertDialog alertDialog = this.dialog;
            if (alertDialog != null && !alertDialog.isShowing()) {
                this.dialog.show();
            }
        }

        private void closeInternetUnavailableDialog() {
            AlertDialog alertDialog = this.dialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_new_settings, viewGroup, false);
    }

    public void onViewCreated(final View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.resolution = (TextView) view.findViewById(R.id.txt_resolution);
        this.frameRate = (TextView) view.findViewById(R.id.txt_frame_rate);
        this.bitRate = (TextView) view.findViewById(R.id.txt_bit_rate);
        this.orientation = (TextView) view.findViewById(R.id.txt_orientation);
        this.countDown = (TextView) view.findViewById(R.id.txt_count_down_settings);
        this.youTube = (TextView) view.findViewById(R.id.txt_you_tube_account);
        this.pauseRecording = (SwitchCompat) view.findViewById(R.id.switch_pause_recording);
        this.lockStopRecording = (SwitchCompat) view.findViewById(R.id.switch_lock_stop_recording);
        this.recordAudio = (SwitchCompat) view.findViewById(R.id.switch_record_audio);
        this.touch = (SwitchCompat) view.findViewById(R.id.switch_touch);
        this.notification_switch = (SwitchCompat) view.findViewById(R.id.id_push_notification_settings_switch);
        this.watermark = (SwitchCompat) view.findViewById(R.id.switch_watermark_preference);
        this.floatBubbleTextView = (TextView) view.findViewById(R.id.txt_float_bubble);
        this.longClickTextView = (TextView) view.findViewById(R.id.txt_floating_long_click);
        this.adSettingTypeTxt = (TextView) view.findViewById(R.id.id_setting_consent_advertising_type_text_view);
        this.defStorageTxtView = (TextView) view.findViewById(R.id.txt_default_storage_choice);
        this.pauseButtonControlsLayout = (LinearLayout) view.findViewById(R.id.lay_recorder_pause_settings);
        this.countdownTimerLayout = (LinearLayout) view.findViewById(R.id.lay_count_down_timer_settings);
        if (EEAConsentHelper.getInstance().isUserFromEEALocation(getActivity())) {
            view.findViewById(R.id.id_setting_advertising_container).setVisibility(View.VISIBLE);
            loadCensentAdType();
        } else {
            view.findViewById(R.id.id_setting_advertising_container).setVisibility(View.GONE);
        }
        view.findViewById(R.id.lay_consent_advertising_preference).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.changeUserAdConsent();
            }
        });
        view.findViewById(R.id.lay_resolution_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_frame_rate_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_orientation_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_bit_rate_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        this.countdownTimerLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_double_click_bubble_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_storage_choice_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.showDialog(view.getId());
            }
        });
        view.findViewById(R.id.lay_you_tube_settings).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            public void onClick(View view) {
                if (NewSettingsFragment.this.youTube.getText().toString().equalsIgnoreCase(NewSettingsFragment.this.getString(R.string.select_you_tube))) {
                    try {
                        if (!ServerAPI.getInstance().isNetworkConnected(NewSettingsFragment.this.getContext())) {
                            Toast.makeText(NewSettingsFragment.this.getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        YoutubeAPI.getInstance().switchGoogleAccount(NewSettingsFragment.this.getActivity()).subscribe(new Consumer<String>() {
                            public void accept(String str) throws Exception {
                                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_SUCCESS));
                                NewSettingsFragment.this.getValues();
                                NewSettingsFragment.this.isLogoutOrChange = true;
                            }
                        }, new Consumer<Throwable>() {
                            public void accept(Throwable th) throws Exception {
                                PrintStream printStream = System.out;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Error: ");
                                sb.append(th.getMessage());
                                printStream.println(sb.toString());
                                th.printStackTrace();
                                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                            }
                        });
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    View inflate = LayoutInflater.from(NewSettingsFragment.this.getContext()).inflate(R.layout.select_account_dialog, null);
                    final AlertDialog create = new AlertDialog.Builder(NewSettingsFragment.this.getContext()).setView(inflate).create();
                    inflate.findViewById(R.id.txt_yes).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            NewSettingsFragment.this.isLogoutOrChange = true;
                            YoutubeAPI.getInstance().logOutFromAccount(NewSettingsFragment.this.getActivity()).subscribe(new DisposableSingleObserver<Boolean>() {
                                public void onSuccess(Boolean bool) {
                                    if (bool.booleanValue()) {
                                        Toast.makeText(NewSettingsFragment.this.getContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();
                                        NewSettingsFragment.this.getValues();
                                        EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                                        ((SettingsActivity) NewSettingsFragment.this.getActivity()).setResult(-1);
                                    }
                                    create.dismiss();
                                }

                                public void onError(Throwable th) {
                                    Toast.makeText(NewSettingsFragment.this.getActivity(), R.string.id_error_logging_out_msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    inflate.findViewById(R.id.txt_later).setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("CheckResult")
                        public void onClick(View view) {
                            try {
                                create.dismiss();
                                if (!ServerAPI.getInstance().isNetworkConnected(NewSettingsFragment.this.getContext())) {
                                    Toast.makeText(NewSettingsFragment.this.getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                                } else {
                                    YoutubeAPI.getInstance().switchGoogleAccount(NewSettingsFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                        public void accept(String str) throws Exception {
                                            NewSettingsFragment.this.getValues();
                                            NewSettingsFragment.this.isLogoutOrChange = true;
                                            if (PreferenceHelper.getInstance().getPrefYoutubeEmailId().equals(NewSettingsFragment.this.getString(R.string.id_select_account_txt))) {
                                                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                                            } else {
                                                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_SUCCESS));
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        public void accept(Throwable th) throws Exception {
                                            PrintStream printStream = System.out;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Error: ");
                                            sb.append(th.getMessage());
                                            printStream.println(sb.toString());
                                            th.printStackTrace();
                                            EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                                        }
                                    });
                                }
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    create.show();
                }
            }
        });
        this.notification_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    ((TextView) view.findViewById(R.id.id_push_notification_settings_subtitle_text_view)).setText(R.string.notification_settings_subtitle_enabled_txt);
                } else {
                    ((TextView) view.findViewById(R.id.id_push_notification_settings_subtitle_text_view)).setText(R.string.notification_settings_subtitle_disabled_txt);
                }
                PreferenceHelper.getInstance().setPrefPushNotification(z);
                FirebaseEventsNewHelper.getInstance().sendNotificationUserProperty(z ? 1 : 0);
                if (!RecorderApplication.getInstance().isNetworkAvailable()) {
                    PreferenceHelper.getInstance().setPrefNotificationUserProperty(false);
                }
            }
        });
        this.pauseRecording.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceHelper.getInstance().setPrefRecordBubbleVisibility(z);
            }
        });
        this.lockStopRecording.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                NewSettingsFragment.this.prefs.edit().putBoolean("notifications_screen_off", z).apply();
            }
        });
        this.recordAudio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z || VERSION.SDK_INT < 23 || NewSettingsFragment.this.isMicPermissionAvailable()) {
                    PreferenceHelper.getInstance().setPrefRecordAudio(z);
                    return;
                }
                NewSettingsFragment.this.recordAudio.toggle();
                NewSettingsFragment.this.requestMicPermission();
            }
        });
        this.touch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceHelper.getInstance().setPrefRecordTouchVisibility(z);
                try {
                    if (NewSettingsFragment.this.checkWriteSettingsPermissionAvailable()) {
                        String str = "show_touches";
                        if (PreferenceHelper.getInstance().getPrefRecordTouchVisibility()) {
                            System.putInt(NewSettingsFragment.this.getActivity().getContentResolver(), str, 1);
                        } else {
                            System.putInt(NewSettingsFragment.this.getActivity().getContentResolver(), str, 0);
                        }
                    } else {
                        ActivityCompat.requestPermissions(NewSettingsFragment.this.getActivity(), new String[]{"android.permission.WRITE_SETTINGS"}, NewSettingsFragment.ACTION_MANAGE_WRITE_PERMISSION_REQUEST_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.watermark.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceHelper.getInstance().setPrefWatermarkVisibility(z);
            }
        });
        view.findViewById(R.id.lay_float_bubble_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NewSettingsFragment.this.checkPermissionForOverlay();
            }
        });
        this.layFloating = view.findViewById(R.id.lay_float_bubble_settings);
        if (VERSION.SDK_INT >= 23) {
            boolean canDrawOverlays = Settings.canDrawOverlays(getContext());
            this.floatBubbleTextView.setText(getString(canDrawOverlays ? R.string.settings_enabled : R.string.settings_diabled));
            if (canDrawOverlays) {
                this.pauseButtonControlsLayout.setVisibility(View.VISIBLE);
                this.countdownTimerLayout.setVisibility(View.VISIBLE);
                return;
            }
            this.pauseButtonControlsLayout.setVisibility(View.GONE);
            this.countdownTimerLayout.setVisibility(View.GONE);
            return;
        }
        this.pauseButtonControlsLayout.setVisibility(View.VISIBLE);
        this.countdownTimerLayout.setVisibility(View.VISIBLE);
        this.layFloating.setVisibility(View.GONE);
    }

    /* access modifiers changed from: private */
    public boolean checkWriteSettingsPermissionAvailable() {
        return ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_SETTINGS") == 0;
    }

    private void showMicPermissionErrorDialog() {
        new AlertDialog.Builder(getActivity()).setMessage((int) R.string.id_record_audio_permission_failed_dialog_message).setPositiveButton((int) R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NewSettingsFragment.this.isAdded()) {
                    dialogInterface.dismiss();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewSettingsFragment.this.getActivity(), "android.permission.RECORD_AUDIO")) {
                        NewSettingsFragment.this.requestMicPermission();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(NewSettingsFragment.this.getActivity().getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                        intent.addFlags(268435456);
                        intent.addFlags(Ints.MAX_POWER_OF_TWO);
                        intent.addFlags(8388608);
                        NewSettingsFragment.this.startActivity(intent);
                    }
                }
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NewSettingsFragment.this.isAdded()) {
                    dialogInterface.dismiss();
                    Toast.makeText(NewSettingsFragment.this.getActivity(), R.string.id_record_audio_permission_failed_toast_message, Toast.LENGTH_SHORT).show();
                }
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void requestMicPermission() {
        requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, Constants.REQUEST_CODE_MIC_PERMISSION);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1122 || iArr.length <= 0) {
            return;
        }
        if (iArr[0] == 0) {
            this.recordAudio.setChecked(true);
        } else if (iArr[0] == -1) {
            showMicPermissionErrorDialog();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (VERSION.SDK_INT < 23) {
                this.pauseButtonControlsLayout.setVisibility(View.VISIBLE);
                this.countdownTimerLayout.setVisibility(View.VISIBLE);
                startFloatingService();
            } else if (Settings.canDrawOverlays(getContext())) {
                this.pauseButtonControlsLayout.setVisibility(View.VISIBLE);
                this.countdownTimerLayout.setVisibility(View.VISIBLE);
                startFloatingService();
            } else {
                this.pauseButtonControlsLayout.setVisibility(View.GONE);
                this.countdownTimerLayout.setVisibility(View.GONE);
            }
        } else if (requestCode == ACTION_MANAGE_WRITE_PERMISSION_REQUEST_CODE && VERSION.SDK_INT >= 23) {
            boolean canWrite = System.canWrite(getContext());
            PreferenceHelper.getInstance().setPrefRecordTouchVisibility(canWrite);
            this.isManualChangeForShowTouchSwitch = true;
            this.touch.setChecked(canWrite);
        }
        if (VERSION.SDK_INT >= 23) {
            this.floatBubbleTextView.setText(getString(Settings.canDrawOverlays(getContext()) ? R.string.settings_enabled : R.string.settings_diabled));
        } else {
            this.layFloating.setVisibility(View.GONE);
        }
    }

    /* access modifiers changed from: private */
    public void checkPermissionForOverlay() {
        if (VERSION.SDK_INT >= 23) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(getActivity().getPackageName());
                startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                startFloatingService();
            }
        } else {
            startFloatingService();
        }
    }

    private void startFloatingService() {
        MainActivity.showDirectly = new SharedDataForOtherApp(BuildConfig.APPLICATION_ID, "EzScreenRecorder", "");
        getActivity().startService(new Intent(getContext(), FloatingService.class));
    }

    /* access modifiers changed from: private */
    public void showDialog(int i) {
        int i2 = 0;
        switch (i) {
            case R.id.lay_bit_rate_settings /*2131296846*/:
                i2 = 2;
                break;
            case R.id.lay_count_down_timer_settings /*2131296849*/:
                i2 = 4;
                break;
            case R.id.lay_double_click_bubble_settings /*2131296850*/:
                i2 = 6;
                break;
            case R.id.lay_frame_rate_settings /*2131296854*/:
                i2 = 1;
                break;
            case R.id.lay_orientation_settings /*2131296857*/:
                i2 = 3;
                break;
            case R.id.lay_storage_choice_settings /*2131296863*/:
                i2 = 7;
                break;
        }
        SettingsListDialogFragment settingsListDialogFragment = new SettingsListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", i2);
        settingsListDialogFragment.setArguments(bundle);
        settingsListDialogFragment.show(getChildFragmentManager(), "asd");
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (getActivity() != null) {
            getValues();
        }
    }

    public void setMenuVisibility(boolean z) {
        super.setMenuVisibility(z);
        if (z && getActivity() != null) {
            getValues();
        }
    }

    private String getCurrentResolution(int i) {
        boolean z;
        String[] stringArray = getResources().getStringArray(R.array.pref_resolution_list_titles);
        String str = stringArray[0];
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);
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

    /* access modifiers changed from: private */
    public void getValues() {
        this.resolution.setText(getCurrentResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING));
        String string = this.prefs.getString("example_list_frame_rate", String.valueOf(30));
        String[] stringArray = getResources().getStringArray(R.array.pref_frame_rate_list_titles);
        int length = stringArray.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String str = stringArray[i2];
            if (str.startsWith(string)) {
                this.frameRate.setText(str);
                break;
            }
            i2++;
        }
        String prefRecordingOrientation = PreferenceHelper.getInstance().getPrefRecordingOrientation();
        String[] stringArray2 = getResources().getStringArray(R.array.pref_orientation_list_values);
        int length2 = stringArray2.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length2) {
                break;
            }
            String str2 = stringArray2[i3];
            if (str2.startsWith(prefRecordingOrientation)) {
                this.orientation.setText(str2);
                break;
            }
            i3++;
        }
        Float valueOf = Float.valueOf(Float.parseFloat(this.prefs.getString("example_list_bit_rate", String.valueOf(1000000))) / 1000000.0f);
        String[] stringArray3 = getResources().getStringArray(R.array.pref_bit_rate_list_titles);
        int length3 = stringArray3.length;
        int i4 = 0;
        while (true) {
            if (i4 >= length3) {
                break;
            }
            String str3 = stringArray3[i4];
            try {
                if (Float.valueOf(Float.parseFloat(str3.replace("Mbps", "").trim())).equals(valueOf)) {
                    this.bitRate.setText(str3);
                    break;
                }
                i4++;
            } catch (Exception e) {
                e.printStackTrace();
                if (str3.startsWith(String.valueOf(valueOf))) {
                    this.bitRate.setText(str3);
                    break;
                }
            }
        }
        int prefDefaultStorageLocation = PreferenceHelper.getInstance().getPrefDefaultStorageLocation();
        if (prefDefaultStorageLocation == 0) {
            this.defStorageTxtView.setText(getString(R.string.id_internal_storage_txt));
        } else if (prefDefaultStorageLocation == 1) {
            this.defStorageTxtView.setText(getString(R.string.id_external_storage_txt));
        }
        if (VERSION.SDK_INT >= 23 && PreferenceHelper.getInstance().getPrefRecordAudio() && !isMicPermissionAvailable()) {
            PreferenceHelper.getInstance().setPrefRecordAudio(false);
        }
        this.recordAudio.setChecked(PreferenceHelper.getInstance().getPrefRecordAudio());
        this.lockStopRecording.setChecked(this.prefs.getBoolean("notifications_screen_off", true));
        this.pauseRecording.setChecked(PreferenceHelper.getInstance().getPrefRecordBubbleVisibility());
        this.touch.setChecked(VERSION.SDK_INT >= 23 ? System.canWrite(getContext()) : false);
        this.notification_switch.setChecked(PreferenceHelper.getInstance().getPrefPushNotification());
        this.watermark.setChecked(PreferenceHelper.getInstance().getPrefWatermarkVisibility());
        Integer valueOf2 = Integer.valueOf(PreferenceHelper.getInstance().getPrefRecordingCountdown());
        String[] stringArray4 = getResources().getStringArray(R.array.pref_count_down_list_titles);
        int length4 = stringArray4.length;
        while (true) {
            if (i >= length4) {
                break;
            }
            String str4 = stringArray4[i];
            if (str4.equalsIgnoreCase("No CountDown") && valueOf2.intValue() == 0) {
                this.countDown.setText(str4);
                break;
            } else if (str4.startsWith(String.valueOf(valueOf2))) {
                this.countDown.setText(str4);
                break;
            } else {
                i++;
            }
        }
        this.youTube.setText(this.prefs.getString("youtube_account_email", getString(R.string.select_you_tube)));
        int parseInt = Integer.parseInt(this.prefs.getString("example_list_long_click", AppEventsConstants.EVENT_PARAM_VALUE_NO));
        String[] stringArray5 = getResources().getStringArray(R.array.pref_long_click_list_titles);
        if (parseInt < stringArray5.length) {
            this.longClickTextView.setText(stringArray5[parseInt]);
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (getActivity() != null) {
            getValues();
        }
    }

    /* access modifiers changed from: private */
    public void loadCensentAdType() {
        int eEAConsentAdType = EEAConsentHelper.getInstance().getEEAConsentAdType(getContext());
        if (eEAConsentAdType == 1) {
            this.adSettingTypeTxt.setText(R.string.id_advertising_preference_type_non_personalised_txt);
        } else if (eEAConsentAdType == 2) {
            this.adSettingTypeTxt.setText(R.string.id_advertising_preference_type_personalised_txt);
        } else if (eEAConsentAdType == 3) {
            this.adSettingTypeTxt.setText(R.string.id_advertising_preference_type_not_set_txt);
        }
    }

    /* access modifiers changed from: private */
    public void changeUserAdConsent() {
        if (isAdded()) {
            if (RecorderApplication.getInstance().isNetworkAvailable()) {
                EEAConsentHelper.getInstance().showEEAConsentForm(getActivity(), new EEAConsentHelper.OnEEAConsentListener() {
                    public void onConsentStart() {
                        NewSettingsFragment.this.isAdded();
                    }

                    public void onConsentComplete() {
                        if (NewSettingsFragment.this.isAdded()) {
                            NewSettingsFragment.this.loadCensentAdType();
                        }
                    }

                    public void onConsentError(String str) {
                        if (NewSettingsFragment.this.isAdded() && !EEAConsentHelper.getInstance().isUserConsentTaken(NewSettingsFragment.this.getActivity()) && EEAConsentHelper.getInstance().isUserFromEEALocation(NewSettingsFragment.this.getActivity())) {
                            Toast.makeText(NewSettingsFragment.this.getActivity(), R.string.id_consent_error_loading_dialog_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                if (this.internetStateListener == null) {
                    this.internetStateListener = new InternetStateListener();
                }
                this.internetStateListener.registerListener(getActivity());
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (isAdded()) {
            InternetStateListener internetStateListener2 = this.internetStateListener;
            if (internetStateListener2 != null) {
                internetStateListener2.unregisterListener(getActivity());
            }
        }
    }

    private boolean isStoragePermissionAvailable() {
        return ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    public boolean isMicPermissionAvailable() {
        return ContextCompat.checkSelfPermission(getActivity(), "android.permission.RECORD_AUDIO") == 0;
    }

    private boolean isCameraPermissionAvailable() {
        return ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0;
    }
}
