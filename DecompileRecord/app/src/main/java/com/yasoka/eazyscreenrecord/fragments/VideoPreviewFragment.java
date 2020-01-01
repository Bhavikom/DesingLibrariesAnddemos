package com.yasoka.eazyscreenrecord.fragments;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.content.ContextCompat;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.TrimVideoActivity;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.Logger;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;
import com.facebook.messenger.MessengerUtils;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.common.primitives.Ints;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import life.knowledge4.videotrimmer.utils.FileUtils;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.Observer;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.observers.DisposableObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class VideoPreviewFragment extends BasePreviewScreenFragment implements OnClickListener, OnNativeAdListener {
    private static final int EDIT_IMG = 3421;
    private static final int REQUEST_CODE_DELETE_VID = 3444;
    private static final int REQUEST_CODE_EDIT_IMG = 3443;
    private static int SHARE_APP_REQUIRED = 4;
    public static final String TAG = "VideoPreviewFragment";
    private AlertDialog alertDialog;
    private ImageView contentImage;
    /* access modifiers changed from: private */
    public AppCompatTextView fileName;
    private AppCompatTextView fileSize;
    private GridLayout gridLayout;
    private Intent intentData;
    private UnifiedNativeAdView nativeAdView;
    /* access modifiers changed from: private */
    public String videoFilePath;
    private String[] videoWhiteListPackages = {"com.whatsapp", "com.viber.voip", MessengerUtils.PACKAGE_NAME, "com.google.android.apps.fireball", "com.tencent.mm", "com.bsb.hike", "com.snapchat.android", "com.instagram.android", "com.google.android.gm", "com.facebook.katana", "com.google.android.youtube", "com.google.android.apps.docs"};

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
        return layoutInflater.inflate(C0793R.layout.fragment_preview_video, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.contentImage = (ImageView) view.findViewById(C0793R.C0795id.id_preview_screen_video_preview);
        this.fileName = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_video_name);
        this.fileSize = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_video_size);
        this.gridLayout = (GridLayout) view.findViewById(C0793R.C0795id.id_preview_screen_video_grid_layout);
        this.nativeAdView = (UnifiedNativeAdView) view.findViewById(C0793R.C0795id.id_preview_screen_native_ad_view);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        for (int i = 0; i < this.gridLayout.getChildCount(); i++) {
            this.gridLayout.getChildAt(i).setOnClickListener(this);
        }
        Intent intent = this.intentData;
        String str = NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH;
        if (intent.hasExtra(str)) {
            this.fileName.setOnClickListener(this);
            this.videoFilePath = this.intentData.getStringExtra(str);
            Glide.with((Fragment) this).load(this.videoFilePath).asBitmap().error((int) C0793R.mipmap.ic_default_video).placeholder((int) C0793R.mipmap.ic_default_video).into(this.contentImage);
            this.fileName.setText(getFileNameFromPath(this.videoFilePath));
            this.fileSize.setText(getFileSizeFromPath(this.videoFilePath));
            this.contentImage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(VideoPreviewFragment.this.getActivity(), NewVideoPlayerActivity.class);
                    intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, VideoPreviewFragment.this.videoFilePath);
                    try {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(VideoPreviewFragment.this.getActivity(), Uri.fromFile(new File(VideoPreviewFragment.this.videoFilePath)));
                        intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                        intent.putExtra(NewVideoPlayerActivity.EXTRA_IS_PLAYER_STARTED_FROM_GALLERY, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    VideoPreviewFragment.this.startActivityForResult(intent, VideoPreviewFragment.REQUEST_CODE_DELETE_VID);
                }
            });
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

    private void setupShareView() {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent("android.intent.action.SEND");
        String str = "image/*";
        intent.setType(str);
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{str});
        List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        if (!queryIntentActivities.isEmpty()) {
            List sortLaunchables = sortLaunchables(queryIntentActivities);
            for (int i = 3; i < this.gridLayout.getChildCount(); i++) {
                int i2 = i - 3;
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
        this.gridLayout.removeViewsInLayout(3, 5);
    }

    private List<ResolveInfo> sortLaunchables(List<ResolveInfo> list) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        for (String str : this.videoWhiteListPackages) {
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
            File file = new File(this.videoFilePath);
            String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
            editText.setText(substring);
            editText.setSelection(substring.length());
            this.alertDialog = new Builder(getActivity()).setTitle((int) C0793R.string.rename_image).setView((View) editText).setPositiveButton((int) C0793R.string.save, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String obj = editText.getText().toString();
                    if (obj.length() == 0) {
                        editText.setError(VideoPreviewFragment.this.getString(C0793R.string.valid_name));
                        return;
                    }
                    File file = new File(VideoPreviewFragment.this.videoFilePath);
                    if (TextUtils.equals(file.getName(), obj)) {
                        dialogInterface.dismiss();
                    } else {
                        String parent = file.getParent();
                        StringBuilder sb = new StringBuilder();
                        sb.append(obj);
                        sb.append(file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()));
                        File file2 = new File(parent, sb.toString());
                        if (file2.exists()) {
                            editText.setError(VideoPreviewFragment.this.getString(C0793R.string.file_already_exists));
                        } else if (file.exists() && file.renameTo(file2)) {
                            VideoPreviewFragment.this.fileName.setText(file2.getName());
                            VideoPreviewFragment.this.videoFilePath = file2.getPath();
                            VideoPreviewFragment.this.getActivity().setResult(-1);
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
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_delete /*2131296713*/:
                    DeleteCheckDialogFragment deleteCheckDialogFragment = new DeleteCheckDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("video", this.videoFilePath);
                    bundle.putBoolean("isVideo", true);
                    deleteCheckDialogFragment.setArguments(bundle);
                    deleteCheckDialogFragment.show(getActivity().getSupportFragmentManager(), "asd");
                    break;
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_more /*2131296716*/:
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("video/*");
                    intent.putExtra("android.intent.extra.TITLE", getString(C0793R.string.share_video));
                    intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.share_video));
                    intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.share_video_txt));
                    intent.addFlags(1);
                    FragmentActivity activity = getActivity();
                    StringBuilder sb = new StringBuilder();
                    sb.append(getActivity().getPackageName());
                    sb.append(".my.package.name.provider");
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(this.videoFilePath)));
                    startActivity(Intent.createChooser(intent, getString(C0793R.string.share_video)));
                    FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
                    break;
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_share_1 /*2131296717*/:
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_share_2 /*2131296720*/:
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_share_3 /*2131296723*/:
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_share_4 /*2131296726*/:
                    handleCustomShareClick(view);
                    break;
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_trim /*2131296729*/:
                    Intent intent2 = new Intent(getActivity(), TrimVideoActivity.class);
                    intent2.putExtra(TrimVideoActivity.EXTRA_VIDEO_PATH, FileUtils.getPath(getActivity(), Uri.fromFile(new File(this.videoFilePath))));
                    try {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(getActivity(), Uri.fromFile(new File(this.videoFilePath)));
                        intent2.putExtra(TrimVideoActivity.EXTRA_VIDEO_DURATION, Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivityForResult(intent2, REQUEST_CODE_EDIT_IMG);
                    break;
                case C0793R.C0795id.id_preview_screen_video_gridlayout_option_upload_to_youtube /*2131296732*/:
                    uploadToYoutube();
                    break;
                case C0793R.C0795id.id_preview_screen_video_name /*2131296735*/:
                    renameFile();
                    break;
            }
        }
    }

    private void handleCustomShareClick(View view) {
        Object tag = view.getTag();
        if (tag != null && (tag instanceof ResolveInfo)) {
            ActivityInfo activityInfo = ((ResolveInfo) tag).activityInfo;
            ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
            Intent intent = new Intent("android.intent.action.SEND");
            String str = "video/*";
            intent.setType(str);
            intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{str});
            intent.putExtra("android.intent.extra.TITLE", getString(C0793R.string.share_video));
            intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.share_video));
            intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.share_video_txt));
            intent.addFlags(1);
            FragmentActivity activity = getActivity();
            StringBuilder sb = new StringBuilder();
            sb.append(getActivity().getPackageName());
            sb.append(".my.package.name.provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(this.videoFilePath)));
            intent.setFlags(270532608);
            intent.setComponent(componentName);
            startActivity(intent);
            AppUtils.addCount(getActivity(), 5);
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Logger instance = Logger.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("ReqCode: ");
        sb.append(i);
        sb.append(" Res_Code: ");
        sb.append(i2);
        sb.append(" data: ");
        sb.append(intent != null);
        instance.error(sb.toString());
        if (i2 != -1) {
            return;
        }
        if (i == REQUEST_CODE_EDIT_IMG) {
            getActivity().setResult(-1, intent);
        } else if (i == REQUEST_CODE_DELETE_VID) {
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

    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    public boolean checkGetAccountsPermission() {
        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(getContext(), "android.permission.GET_ACCOUNTS") == 0) {
            return true;
        }
        return false;
    }

    public void requestGetAccountPermission() {
        requestPermissions(new String[]{"android.permission.GET_ACCOUNTS"}, Constants.REQUEST_CODE_GET_ACCOUNT_PERMISSION);
    }

    private void showGetAccountsPermissionErrorDailog() {
        new Builder(getActivity()).setMessage((int) C0793R.string.id_get_account_permission_failed_dialog_message).setPositiveButton((int) C0793R.string.id_turn_it_on_txt, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (VideoPreviewFragment.this.isAdded()) {
                    dialogInterface.dismiss();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(VideoPreviewFragment.this.getActivity(), "android.permission.GET_ACCOUNTS")) {
                        VideoPreviewFragment.this.requestGetAccountPermission();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(VideoPreviewFragment.this.getActivity().getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                        intent.addFlags(268435456);
                        intent.addFlags(Ints.MAX_POWER_OF_TWO);
                        intent.addFlags(8388608);
                        VideoPreviewFragment.this.startActivity(intent);
                    }
                }
            }
        }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(VideoPreviewFragment.this.getContext(), C0793R.string.id_get_account_permission_failed_toast_message, 1).show();
            }
        }).show();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1125 || iArr.length <= 0) {
            return;
        }
        if (iArr[0] == 0) {
            uploadToYoutube();
        } else if (iArr[0] == -1) {
            showGetAccountsPermissionErrorDailog();
        }
    }

    private void uploadToYoutube() {
        if (checkGetAccountsPermission()) {
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            if (!isNetworkConnected()) {
                Toast.makeText(getContext(), C0793R.string.no_internet_connection, 1).show();
            } else if (AppUtils.containUploadFile(getContext())) {
                Toast.makeText(getContext(), C0793R.string.upload_in_progress_one, 1).show();
            }
            File file = new File(this.videoFilePath);
            final ImageVideoFile imageVideoFile = new ImageVideoFile();
            imageVideoFile.setPath(file.getAbsolutePath());
            imageVideoFile.setName(file.getName());
            imageVideoFile.setVideo(file.getAbsolutePath().endsWith(".mp4"));
            imageVideoFile.setFileSize(file.length());
            Builder builder = new Builder(getContext());
            View inflate = LayoutInflater.from(getContext()).inflate(C0793R.layout.you_tube_layout, null);
            final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(C0793R.C0795id.txt_lay_title);
            textInputLayout.getEditText().setText(imageVideoFile.getName());
            final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(C0793R.C0795id.txt_lay_desc);
            textInputLayout2.getEditText().setText(C0793R.string.you_tube_desc2);
            Builder view = builder.setTitle((int) C0793R.string.youtube_details).setView(inflate);
            C11687 r1 = new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, int i) {
                    if (textInputLayout.getEditText().getText() == null || textInputLayout.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(VideoPreviewFragment.this.getContext(), C0793R.string.id_enter_valid_title_error_msg, 1).show();
                    } else if (textInputLayout2.getEditText().getText() == null || textInputLayout2.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(VideoPreviewFragment.this.getContext(), C0793R.string.id_enter_valid_desc_error_msg, 1).show();
                    } else {
                        final GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(VideoPreviewFragment.this.getContext(), Arrays.asList(UploadService.SCOPES));
                        usingOAuth2.setBackOff(new ExponentialBackOff());
                        Observable.create(new ObservableOnSubscribe<String>() {
                            public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                                String str = "youtube_account_email";
                                if (defaultSharedPreferences.contains(str)) {
                                    ServerAPI.getInstance().addToFireBase(VideoPreviewFragment.this.getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                        public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                            System.out.println("sucess");
                                        }

                                        public void onError(Throwable th) {
                                            th.printStackTrace();
                                        }
                                    });
                                    observableEmitter.onNext(defaultSharedPreferences.getString(str, ""));
                                    observableEmitter.onComplete();
                                    return;
                                }
                                YoutubeAPI.getInstance().switchGoogleAccount(VideoPreviewFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                    public void accept(String str) throws Exception {
                                        observableEmitter.onNext(str);
                                        ServerAPI.getInstance().addToFireBase(VideoPreviewFragment.this.getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                            public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                                System.out.println("sucess");
                                            }

                                            public void onError(Throwable th) {
                                                th.printStackTrace();
                                            }
                                        });
                                        observableEmitter.onComplete();
                                    }
                                }, new Consumer<Throwable>() {
                                    public void accept(Throwable th) throws Exception {
                                        th.printStackTrace();
                                        observableEmitter.onError(th);
                                    }
                                });
                            }
                        }).subscribe((Observer<? super T>) new DisposableObserver<String>() {
                            public void onNext(String str) {
                                ServerAPI.getInstance().addToFireBase(VideoPreviewFragment.this.getContext(), "Upload to YouTube Starts").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                    public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                    }

                                    public void onError(Throwable th) {
                                        th.printStackTrace();
                                    }
                                });
                                usingOAuth2.setSelectedAccount(new Account(str, "com.google"));
                                Intent intent = new Intent(VideoPreviewFragment.this.getContext(), UploadService.class);
                                intent.setData(Uri.fromFile(new File(imageVideoFile.getPath())));
                                intent.putExtra(UploadService.ACCOUNT_KEY, str);
                                intent.putExtra("name", textInputLayout.getEditText().getText().toString());
                                String obj = textInputLayout2.getEditText().getText().toString();
                                StringBuilder sb = new StringBuilder();
                                sb.append(obj);
                                sb.append("\n\n");
                                sb.append(VideoPreviewFragment.this.getContext().getString(C0793R.string.you_tube_desc1));
                                intent.putExtra("desc", sb.toString());
                                intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, imageVideoFile.getDuration() / 1000);
                                VideoPreviewFragment.this.getContext().startService(intent);
                                Toast.makeText(VideoPreviewFragment.this.getContext(), C0793R.string.upload_started_my, 1).show();
                            }

                            public void onError(Throwable th) {
                                th.printStackTrace();
                                dialogInterface.dismiss();
                            }

                            public void onComplete() {
                                dialogInterface.dismiss();
                            }
                        });
                    }
                }
            };
            view.setPositiveButton((int) C0793R.string.youtube_upload, (DialogInterface.OnClickListener) r1).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
            return;
        }
        requestGetAccountPermission();
    }
}
