package com.yasoka.eazyscreenrecord.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.content.FileProvider;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.ezscreenrecorder.model.AudioFileModel;
import com.ezscreenrecorder.p004ui.CircularSeekBar;
import com.ezscreenrecorder.p004ui.PlayPauseView;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.LocalAudioPlayer;
import com.ezscreenrecorder.utils.LocalAudioPlayer.OnPlayerCallbacks;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.utils.PlayerUtils;
import com.facebook.messenger.MessengerUtils;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class AudioPreviewFragment extends BasePreviewScreenFragment implements OnClickListener, OnNativeAdListener, OnPlayerCallbacks {
    private static final int EDIT_IMG = 3421;
    private static final int REQUEST_CODE_DELETE_IMG = 3442;
    private static final int REQUEST_CODE_EDIT_IMG = 3441;
    private static int SHARE_APP_REQUIRED = 6;
    public static final String TAG = "AudioPreviewFragment";
    private AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public AudioFileModel audioFileModel;
    private String[] audioWhiteListPackages = {"com.whatsapp", "com.viber.voip", MessengerUtils.PACKAGE_NAME, "com.google.android.apps.fireball", "com.tencent.mm", "com.bsb.hike", "com.google.android.gm", "com.google.android.apps.docs"};
    private CircularSeekBar circularSeekBar;
    private TextView currentTime;
    /* access modifiers changed from: private */
    public AppCompatTextView fileName;
    private AppCompatTextView fileSize;
    private GridLayout gridLayout;
    private Intent intentData;
    private LocalAudioPlayer localAudioPlayer;
    private TextView maxTime;
    private UnifiedNativeAdView nativeAdView;
    private PlayPauseView playPauseView;

    public void onChangePlayerVisibility(int i) {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.intentData = getActivity().getIntent();
        Intent intent = this.intentData;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_preview_audio, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.circularSeekBar = (CircularSeekBar) view.findViewById(C0793R.C0795id.id_preview_screen_audio_seekbar);
        this.playPauseView = (PlayPauseView) view.findViewById(C0793R.C0795id.id_preview_screen_audio_play_pause_button);
        this.fileName = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_audio_name);
        this.fileSize = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_audio_size);
        this.gridLayout = (GridLayout) view.findViewById(C0793R.C0795id.id_preview_screen_audio_grid_layout);
        this.nativeAdView = (UnifiedNativeAdView) view.findViewById(C0793R.C0795id.id_preview_screen_native_ad_view);
        this.maxTime = (TextView) view.findViewById(C0793R.C0795id.id_preview_screen_audio_total_time_text);
        this.currentTime = (TextView) view.findViewById(C0793R.C0795id.id_preview_screen_audio_remaining_time_text);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        for (int i = 0; i < this.gridLayout.getChildCount(); i++) {
            this.gridLayout.getChildAt(i).setOnClickListener(this);
        }
        String str = "key_file_audio_model";
        if (this.intentData.hasExtra(str)) {
            this.fileName.setOnClickListener(this);
            this.audioFileModel = (AudioFileModel) this.intentData.getSerializableExtra(str);
            this.fileName.setText(getFileNameFromPath(this.audioFileModel.getFilePath()));
            this.fileSize.setText(getFileSizeFromPath(this.audioFileModel.getFilePath()));
            this.playPauseView.setOnClickListener(this);
            if (this.localAudioPlayer == null) {
                this.localAudioPlayer = new LocalAudioPlayer(this.circularSeekBar, this.audioFileModel, (OnPlayerCallbacks) this);
            }
            this.maxTime.setText(PlayerUtils.getInstance().milliSecondsToTimer(this.audioFileModel.getFileDuration()));
            setupShareView();
        }
    }

    public void onStart() {
        super.onStart();
        NativeAdLoaderPreviewDialog.getInstance().getNativeAd(this);
    }

    public void onStop() {
        super.onStop();
        try {
            if (!getActivity().isFinishing()) {
                NativeAdLoaderPreviewDialog.getInstance().loadAd();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void onPause() {
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onPause();
        }
        super.onPause();
    }

    public void onDestroy() {
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onDestroy();
        }
        super.onDestroy();
    }

    private void setupShareView() {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent("android.intent.action.SEND");
        String str = "audio/*";
        intent.setType(str);
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{str});
        List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        if (!queryIntentActivities.isEmpty()) {
            List sortLaunchables = sortLaunchables(queryIntentActivities);
            for (int i = 1; i < this.gridLayout.getChildCount(); i++) {
                int i2 = i - 1;
                try {
                    sortLaunchables.get(i2);
                    if (i != this.gridLayout.getChildCount() - 1) {
                        CardView cardView = (CardView) this.gridLayout.getChildAt(i);
                        for (int i3 = 0; i3 < cardView.getChildCount(); i3++) {
                            View childAt = cardView.getChildAt(i3);
                            if (childAt instanceof LinearLayout) {
                                int i4 = 0;
                                while (true) {
                                    LinearLayout linearLayout = (LinearLayout) childAt;
                                    if (i4 >= linearLayout.getChildCount()) {
                                        break;
                                    }
                                    View childAt2 = linearLayout.getChildAt(i4);
                                    if (childAt2 instanceof ImageView) {
                                        ((ImageView) childAt2).setImageDrawable(((ResolveInfo) sortLaunchables.get(i2)).loadIcon(packageManager));
                                    } else if (childAt2 instanceof TextView) {
                                        ((TextView) childAt2).setText(((ResolveInfo) sortLaunchables.get(i2)).loadLabel(packageManager));
                                    }
                                    i4++;
                                }
                            }
                        }
                        cardView.setTag(sortLaunchables.get(i2));
                        cardView.setOnClickListener(this);
                    }
                } catch (IndexOutOfBoundsException unused) {
                }
            }
            return;
        }
        this.gridLayout.removeViewsInLayout(1, 5);
    }

    private List<ResolveInfo> sortLaunchables(List<ResolveInfo> list) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        for (String str : this.audioWhiteListPackages) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ResolveInfo resolveInfo = (ResolveInfo) it.next();
                if (str.equals(resolveInfo.activityInfo.packageName)) {
                    arrayList.add(resolveInfo);
                    break;
                }
            }
            if (arrayList.size() == SHARE_APP_REQUIRED) {
                break;
            }
        }
        if (arrayList.size() < SHARE_APP_REQUIRED) {
            for (ResolveInfo resolveInfo2 : list) {
                ResolveInfo resolveInfo3 = null;
                Iterator it2 = arrayList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    ResolveInfo resolveInfo4 = (ResolveInfo) it2.next();
                    if (resolveInfo2.equals(resolveInfo4)) {
                        resolveInfo3 = resolveInfo4;
                        break;
                    }
                }
                if (resolveInfo3 == null) {
                    arrayList.add(resolveInfo2);
                }
                if (arrayList.size() == SHARE_APP_REQUIRED) {
                    break;
                }
            }
        }
        return arrayList;
    }

    private void renameFile() {
        if (isAdded()) {
            final EditText editText = (EditText) getActivity().getLayoutInflater().inflate(C0793R.layout.custom_rename_edittext, null, false).findViewById(C0793R.C0795id.id_rename_edit_text);
            File file = new File(this.audioFileModel.getFilePath());
            String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
            editText.setText(substring);
            editText.setSelection(substring.length());
            this.alertDialog = new Builder(getActivity()).setTitle((int) C0793R.string.rename_image).setView((View) editText).setPositiveButton((int) C0793R.string.save, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String obj = editText.getText().toString();
                    if (obj.length() == 0) {
                        editText.setError(AudioPreviewFragment.this.getString(C0793R.string.valid_name));
                        return;
                    }
                    File file = new File(AudioPreviewFragment.this.audioFileModel.getFilePath());
                    if (TextUtils.equals(file.getName(), obj)) {
                        dialogInterface.dismiss();
                    } else {
                        String parent = file.getParent();
                        StringBuilder sb = new StringBuilder();
                        sb.append(obj);
                        sb.append(file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()));
                        File file2 = new File(parent, sb.toString());
                        if (file2.exists()) {
                            editText.setError(AudioPreviewFragment.this.getString(C0793R.string.file_already_exists));
                        } else if (file.exists() && file.renameTo(file2)) {
                            AudioPreviewFragment.this.fileName.setText(file2.getName());
                            AudioPreviewFragment.this.audioFileModel.setFilePath(file2.getPath());
                            AudioPreviewFragment.this.getActivity().setResult(-1);
                        }
                    }
                }
            }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create();
            this.alertDialog.setCanceledOnTouchOutside(false);
            AlertDialog alertDialog2 = this.alertDialog;
            if (alertDialog2 != null && !alertDialog2.isShowing()) {
                this.alertDialog.show();
            }
        }
    }

    public void onClick(View view) {
        if (isAdded()) {
            switch (view.getId()) {
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_delete /*2131296647*/:
                    deleteFile();
                    break;
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_more /*2131296650*/:
                    Uri parse = Uri.parse(this.audioFileModel.getFilePath());
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("audio/*");
                    intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.id_share_audio_title));
                    intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.id_share_audio_text));
                    intent.putExtra("android.intent.extra.STREAM", parse);
                    startActivity(Intent.createChooser(intent, getString(C0793R.string.id_share_audio_title)));
                    AppUtils.addCount(getActivity(), 6);
                    FirebaseEventsNewHelper.getInstance().sendShareEvent("Audio");
                    break;
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_1 /*2131296651*/:
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_2 /*2131296654*/:
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_3 /*2131296657*/:
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_4 /*2131296660*/:
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_5 /*2131296663*/:
                case C0793R.C0795id.id_preview_screen_audio_gridlayout_option_share_6 /*2131296666*/:
                    handleCustomShareClick(view);
                    break;
                case C0793R.C0795id.id_preview_screen_audio_name /*2131296669*/:
                    renameFile();
                    break;
                case C0793R.C0795id.id_preview_screen_audio_play_pause_button /*2131296671*/:
                    if (!this.localAudioPlayer.isPlaying()) {
                        this.localAudioPlayer.resumePlay();
                        break;
                    } else {
                        this.localAudioPlayer.stop();
                        break;
                    }
            }
        }
    }

    private void handleCustomShareClick(View view) {
        Object tag = view.getTag();
        if (tag != null && (tag instanceof ResolveInfo)) {
            ActivityInfo activityInfo = ((ResolveInfo) tag).activityInfo;
            ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
            Intent intent = new Intent("android.intent.action.SEND");
            String str = "audio/*";
            intent.setType(str);
            intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{str});
            intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.id_share_audio_title));
            intent.putExtra("android.intent.extra.TITLE", getString(C0793R.string.id_share_audio_title));
            intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.id_share_audio_text));
            intent.addFlags(1);
            FragmentActivity activity = getActivity();
            StringBuilder sb = new StringBuilder();
            sb.append(getActivity().getPackageName());
            sb.append(".my.package.name.provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(this.audioFileModel.getFilePath())));
            intent.setFlags(270532608);
            intent.setComponent(componentName);
            startActivity(intent);
            AppUtils.addCount(getActivity(), 6);
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Audio");
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == REQUEST_CODE_EDIT_IMG) {
            getActivity().setResult(-1, intent);
        } else if (i == REQUEST_CODE_DELETE_IMG) {
            if (!getActivity().isFinishing()) {
                getActivity().setResult(-1);
            }
            getActivity().finish();
        }
    }

    public void onNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
        addValuesAppInstallAdView(unifiedNativeAd);
    }

    private void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
        try {
            if (this.nativeAdView != null) {
                this.nativeAdView.setHeadlineView(this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_title_text));
                this.nativeAdView.setBodyView(this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_subtitle_text));
                this.nativeAdView.setCallToActionView(this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_button_view));
                this.nativeAdView.setIconView(this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_imageview));
                this.nativeAdView.setMediaView((MediaView) this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_mediaview));
                ((TextView) this.nativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
                ((TextView) this.nativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
                this.nativeAdView.getIconView().setBackgroundColor(-7829368);
                if (unifiedNativeAd.getIcon() != null) {
                    Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                    if (drawable != null) {
                        this.nativeAdView.getIconView().setBackgroundColor(0);
                        ((ImageView) this.nativeAdView.getIconView()).setImageDrawable(drawable);
                    }
                }
                ((TextView) this.nativeAdView.findViewById(C0793R.C0795id.id_preview_screen_button_text)).setText(unifiedNativeAd.getCallToAction().trim());
                this.nativeAdView.setNativeAd(unifiedNativeAd);
                this.nativeAdView.setVisibility(0);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            UnifiedNativeAdView unifiedNativeAdView = this.nativeAdView;
            if (unifiedNativeAdView != null) {
                unifiedNativeAdView.setVisibility(8);
            }
        }
    }

    private void deleteFile() {
        DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_AUDIO_DELETE_CONFIRMATION);
        instance.setDialogResultListener(new OnConfirmationResult() {
            public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                if (z) {
                    Single.create(new SingleOnSubscribe<Boolean>() {
                        public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                            singleEmitter.onSuccess(Boolean.valueOf(new File(AudioPreviewFragment.this.audioFileModel.getFilePath()).delete()));
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<Boolean>() {
                        public void onSuccess(Boolean bool) {
                            if (bool.booleanValue()) {
                                AudioPreviewFragment.this.getActivity().getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{AudioPreviewFragment.this.audioFileModel.getFilePath()});
                                AudioPreviewFragment.this.getActivity().setResult(-1, new Intent(GalleryActivity.ACTION_FILE_DELETED_FROM_DIALOG_ACTIVITY));
                                AudioPreviewFragment.this.getActivity().finish();
                            }
                            DialogFragment dialogFragment = dialogFragment;
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.dismiss();
                            }
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                            DialogFragment dialogFragment = dialogFragment;
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.dismiss();
                            }
                        }
                    });
                } else if (dialogFragment != null && dialogFragment.isVisible()) {
                    dialogFragment.dismiss();
                }
            }
        });
        instance.show(getActivity().getSupportFragmentManager(), "audio_delete_confirmation");
    }

    public void onPlaybackToggle(boolean z) {
        if (z) {
            if (this.playPauseView.isPlay()) {
                this.playPauseView.toggle();
            }
        } else if (!this.playPauseView.isPlay()) {
            this.playPauseView.toggle();
        }
    }

    public void onTimeUpdate(long j, long j2) {
        Object[] objArr = {PlayerUtils.getInstance().milliSecondsToTimer(j2)};
        String str = "%s";
        this.maxTime.setText(String.format(str, objArr));
        this.currentTime.setText(String.format(str, new Object[]{PlayerUtils.getInstance().milliSecondsToTimer(j)}));
    }
}
