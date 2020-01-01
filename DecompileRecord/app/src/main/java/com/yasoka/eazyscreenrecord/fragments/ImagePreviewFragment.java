package com.yasoka.eazyscreenrecord.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
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
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.ShowImagesActivity;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgupload.ImageUploadService;
import com.ezscreenrecorder.interfaces.OnNativeAdListener;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.NativeAdLoaderPreviewDialog;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.facebook.messenger.MessengerUtils;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.Observer;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.observers.DisposableObserver;

public class ImagePreviewFragment extends BasePreviewScreenFragment implements OnClickListener, OnNativeAdListener {
    private static final int EDIT_IMG = 3421;
    private static final int REQUEST_CODE_DELETE_IMG = 3442;
    private static final int REQUEST_CODE_EDIT_IMG = 3441;
    private static int SHARE_APP_REQUIRED = 4;
    public static final String TAG = "ImagePreviewFragment";
    private AlertDialog alertDialog;
    private ImageView contentImage;
    /* access modifiers changed from: private */
    public AppCompatTextView fileName;
    private AppCompatTextView fileSize;
    private GridLayout gridLayout;
    /* access modifiers changed from: private */
    public String imageFilePath;
    private String[] imageWhiteListPackages = {"com.whatsapp", "com.viber.voip", MessengerUtils.PACKAGE_NAME, "com.google.android.apps.fireball", "com.tencent.mm", "com.bsb.hike", "com.snapchat.android", "com.instagram.android", "com.google.android.gm", "com.facebook.katana", "com.google.android.apps.docs"};
    private Intent intentData;
    private UnifiedNativeAdView nativeAdView;

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
        return layoutInflater.inflate(C0793R.layout.fragment_preview_image, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.contentImage = (ImageView) view.findViewById(C0793R.C0795id.id_preview_screen_image_preview);
        this.fileName = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_image_name);
        this.fileSize = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_preview_screen_image_size);
        this.gridLayout = (GridLayout) view.findViewById(C0793R.C0795id.id_preview_screen_image_grid_layout);
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
            this.imageFilePath = this.intentData.getStringExtra(str);
            Glide.with((Fragment) this).load(this.imageFilePath).into(this.contentImage);
            this.fileName.setText(getFileNameFromPath(this.imageFilePath));
            this.fileSize.setText(getFileSizeFromPath(this.imageFilePath));
            this.contentImage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ImagePreviewFragment.this.getActivity(), ShowImagesActivity.class);
                    intent.putExtra("ImgPath", ImagePreviewFragment.this.imageFilePath);
                    ImagePreviewFragment.this.startActivityForResult(intent, ImagePreviewFragment.REQUEST_CODE_DELETE_IMG);
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

    private void renameFile() {
        if (isAdded()) {
            final EditText editText = (EditText) getActivity().getLayoutInflater().inflate(C0793R.layout.custom_rename_edittext, null, false).findViewById(C0793R.C0795id.id_rename_edit_text);
            File file = new File(this.imageFilePath);
            String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
            editText.setText(substring);
            editText.setSelection(substring.length());
            this.alertDialog = new Builder(getActivity()).setTitle((int) C0793R.string.rename_image).setView((View) editText).setPositiveButton((int) C0793R.string.save, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String obj = editText.getText().toString();
                    if (obj.length() == 0) {
                        editText.setError(ImagePreviewFragment.this.getString(C0793R.string.valid_name));
                        return;
                    }
                    File file = new File(ImagePreviewFragment.this.imageFilePath);
                    if (TextUtils.equals(file.getName(), obj)) {
                        dialogInterface.dismiss();
                    } else {
                        String parent = file.getParent();
                        StringBuilder sb = new StringBuilder();
                        sb.append(obj);
                        sb.append(file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()));
                        File file2 = new File(parent, sb.toString());
                        if (file2.exists()) {
                            editText.setError(ImagePreviewFragment.this.getString(C0793R.string.file_already_exists));
                        } else if (file.exists() && file.renameTo(file2)) {
                            ImagePreviewFragment.this.fileName.setText(file2.getName());
                            ImagePreviewFragment.this.imageFilePath = file2.getPath();
                            ImagePreviewFragment.this.getActivity().setResult(-1);
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
        this.gridLayout.removeViewsInLayout(3, SHARE_APP_REQUIRED);
    }

    private List<ResolveInfo> sortLaunchables(List<ResolveInfo> list) {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        for (String str : this.imageWhiteListPackages) {
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

    public void onClick(View view) {
        if (isAdded()) {
            String str = "video";
            switch (view.getId()) {
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_delete /*2131296680*/:
                    DeleteCheckDialogFragment deleteCheckDialogFragment = new DeleteCheckDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(str, this.imageFilePath);
                    bundle.putBoolean("isVideo", false);
                    deleteCheckDialogFragment.setArguments(bundle);
                    deleteCheckDialogFragment.show(getActivity().getSupportFragmentManager(), "asd");
                    break;
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_edit_img /*2131296683*/:
                    Intent intent = new Intent(getActivity(), ImageEditActivity.class);
                    intent.putExtra("image", this.imageFilePath);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_IMG);
                    break;
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_more /*2131296686*/:
                    AppUtils.addCount(getActivity(), 4);
                    ShareImageDialogFragment shareImageDialogFragment = new ShareImageDialogFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(str, this.imageFilePath);
                    shareImageDialogFragment.setArguments(bundle2);
                    shareImageDialogFragment.show(getActivity().getSupportFragmentManager(), "IMG");
                    FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
                    break;
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_share_2 /*2131296687*/:
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_share_3 /*2131296690*/:
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_share_4 /*2131296693*/:
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_share_5 /*2131296696*/:
                    handleCustomShareClick(view);
                    break;
                case C0793R.C0795id.id_preview_screen_image_gridlayout_option_upload_to_cloud /*2131296699*/:
                    Observable.create(new ObservableOnSubscribe<String>() {
                        public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                            if (PreferenceHelper.getInstance().hasPrefYoutubeEmailId()) {
                                observableEmitter.onNext(PreferenceHelper.getInstance().getPrefYoutubeEmailId());
                                observableEmitter.onComplete();
                                return;
                            }
                            try {
                                YoutubeAPI.getInstance().switchGoogleAccount(ImagePreviewFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                    public void accept(String str) throws Exception {
                                        observableEmitter.onNext(str);
                                        observableEmitter.onComplete();
                                    }
                                }, new Consumer<Throwable>() {
                                    public void accept(Throwable th) throws Exception {
                                        th.printStackTrace();
                                        observableEmitter.onError(th);
                                    }
                                });
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                                observableEmitter.onError(e);
                            }
                        }
                    }).subscribe((Observer<? super T>) new DisposableObserver<String>() {
                        public void onComplete() {
                        }

                        public void onError(Throwable th) {
                        }

                        public void onNext(String str) {
                            Intent intent = new Intent(ImagePreviewFragment.this.getActivity(), ImageUploadService.class);
                            intent.putExtra("file_path", ImagePreviewFragment.this.imageFilePath);
                            intent.putExtra("aId", PreferenceHelper.getInstance().getPrefAnonymousId());
                            intent.putExtra("uId", PreferenceHelper.getInstance().getPrefUserId());
                            intent.putExtra("email", str);
                            ImagePreviewFragment.this.getActivity().startService(intent);
                        }
                    });
                    break;
                case C0793R.C0795id.id_preview_screen_image_name /*2131296702*/:
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
            String str = "image/*";
            intent.setType(str);
            intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{str});
            intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.share_image));
            intent.putExtra("android.intent.extra.TITLE", getString(C0793R.string.share_image));
            intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.share_image_txt));
            intent.addFlags(1);
            FragmentActivity activity = getActivity();
            StringBuilder sb = new StringBuilder();
            sb.append(getActivity().getPackageName());
            sb.append(".my.package.name.provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(this.imageFilePath)));
            intent.setComponent(componentName);
            startActivity(intent);
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
}
