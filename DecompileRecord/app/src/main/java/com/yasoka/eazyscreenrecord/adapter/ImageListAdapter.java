package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.ShowImagesActivity;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.fragments.ShareImageDialogFragment;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgupload.ImageUploadService;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.UtilityMethods;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.Observer;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.observers.DisposableObserver;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class ImageListAdapter extends Adapter<ViewHolder> {
    public static final int VIEW_TYPE_AD_LIST_ITEM = 1332;
    private static final int VIEW_TYPE_IMAGE_LIST_ITEM = 1331;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public ImageVideoListener imageVideoListener;
    private final boolean isTrim;
    private Comparator lastModifySort = new Comparator<ImageVideoFile>() {
        public int compare(ImageVideoFile imageVideoFile, ImageVideoFile imageVideoFile2) {
            return Long.compare(imageVideoFile2.getCreated(), imageVideoFile.getCreated());
        }
    };
    /* access modifiers changed from: private */
    public List<Object> mList = new ArrayList();
    /* access modifiers changed from: private */
    public final SharedPreferences prefs;
    /* access modifiers changed from: private */
    public final SharedPreferences sharedPreferences;

    class ImageAdHolder extends ViewHolder {
        UnifiedNativeAdView adView;

        public ImageAdHolder(View view) {
            super(view);
            this.adView = (UnifiedNativeAdView) view.findViewById(C0793R.C0795id.id_native_image_ad_view);
            UnifiedNativeAdView unifiedNativeAdView = this.adView;
            unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(C0793R.C0795id.id_native_image_app_icon_img));
            UnifiedNativeAdView unifiedNativeAdView2 = this.adView;
            unifiedNativeAdView2.setHeadlineView(unifiedNativeAdView2.findViewById(C0793R.C0795id.id_native_image_title_txt));
            UnifiedNativeAdView unifiedNativeAdView3 = this.adView;
            unifiedNativeAdView3.setCallToActionView(unifiedNativeAdView3.findViewById(C0793R.C0795id.id_native_image_button));
            initNativeAd();
        }

        private void initNativeAd() {
            Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
                public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                    AdLoader build = new Builder(ImageListAdapter.this.context.getApplicationContext(), ImageListAdapter.this.context.getString(C0793R.string.key_screenshot_native_ad)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            singleEmitter.onSuccess(unifiedNativeAd);
                        }
                    }).build();
                    AdRequest.Builder builder = new AdRequest.Builder();
                    if (EEAConsentHelper.getInstance().getEEAConsentAdType(RecorderApplication.getInstance().getApplicationContext()) == 1) {
                        builder.addNetworkExtrasBundle(AdMobAdapter.class, EEAConsentHelper.getInstance().getNonPersonalisedBundle(RecorderApplication.getInstance().getApplicationContext()));
                    }
                    build.loadAd(builder.build());
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<UnifiedNativeAd>() {
                public void onError(Throwable th) {
                }

                public void onSuccess(UnifiedNativeAd unifiedNativeAd) {
                    ImageAdHolder.this.addValuesAppInstallAdView(unifiedNativeAd);
                }
            });
        }

        /* access modifiers changed from: private */
        public void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
            this.adView.getIconView().setBackgroundColor(-7829368);
            ((TextView) this.adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            if (unifiedNativeAd.getIcon() != null) {
                Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                if (drawable != null) {
                    this.adView.getIconView().setBackgroundColor(0);
                    ((ImageView) this.adView.getIconView()).setImageDrawable(drawable);
                }
            }
            ((Button) this.adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            this.adView.setNativeAd(unifiedNativeAd);
        }
    }

    class ImageListItemHolder extends ViewHolder implements OnClickListener {
        ImageView mFileImage;
        TextView mFileName;
        TextView mFileSize;
        TextView mFileTime;

        ImageListItemHolder(View view) {
            super(view);
            this.mFileImage = (ImageView) view.findViewById(C0793R.C0795id.img_file);
            this.mFileName = (TextView) view.findViewById(C0793R.C0795id.txt_file_name);
            this.mFileTime = (TextView) view.findViewById(C0793R.C0795id.txt_time);
            this.mFileSize = (TextView) view.findViewById(C0793R.C0795id.txt_file_size);
            view.setOnClickListener(new OnClickListener(ImageListAdapter.this) {
                public void onClick(View view) {
                    int adapterPosition = ImageListItemHolder.this.getAdapterPosition();
                    if (adapterPosition >= 0 && adapterPosition < ImageListAdapter.this.mList.size() && (ImageListAdapter.this.mList.get(adapterPosition) instanceof ImageVideoFile)) {
                        ImageVideoFile imageVideoFile = (ImageVideoFile) ImageListAdapter.this.mList.get(adapterPosition);
                        if (imageVideoFile.isVideo()) {
                            Intent intent = new Intent(ImageListAdapter.this.context, NewVideoPlayerActivity.class);
                            intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, imageVideoFile.getPath());
                            intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, imageVideoFile.getDuration());
                            ((GalleryActivity) ImageListAdapter.this.context).startActivityForResult(intent, GalleryActivity.REQUEST_VIEW);
                            return;
                        }
                        Intent intent2 = new Intent(ImageListAdapter.this.context, ShowImagesActivity.class);
                        intent2.putExtra("ImgPath", imageVideoFile.getPath());
                        intent2.putExtra("isFromServer", false);
                        ((GalleryActivity) ImageListAdapter.this.context).startActivityForResult(intent2, GalleryActivity.REQUEST_VIEW_IMAGES);
                        FirebaseEventsNewHelper.getInstance().sendLocalScreenshotViewedEvent(UtilityMethods.getInstance().getImageResolutionFromPath(imageVideoFile.getPath()), UtilityMethods.getInstance().getFileSizeFromPath(imageVideoFile.getPath()));
                    }
                }
            });
            view.findViewById(C0793R.C0795id.img_upload_image).setOnClickListener(this);
            view.findViewById(C0793R.C0795id.img_share_image).setOnClickListener(this);
            view.findViewById(C0793R.C0795id.img_delete_image).setOnClickListener(this);
            view.findViewById(C0793R.C0795id.img_rename_image).setOnClickListener(this);
            setTouchForImage(C0793R.C0795id.img_upload_image);
            setTouchForImage(C0793R.C0795id.img_share_image);
            setTouchForImage(C0793R.C0795id.img_rename_image);
            setTouchForImage(C0793R.C0795id.img_delete_image);
            setTouchForImage(C0793R.C0795id.img_edit_image);
            view.findViewById(C0793R.C0795id.img_edit_image).setOnClickListener(this);
        }

        private void setTouchForImage(int i) {
            this.itemView.findViewById(i).setOnTouchListener(new ChangeFilterTouchListenr((ImageView) this.itemView.findViewById(i)));
        }

        public void onClick(View view) {
            final int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                final ImageVideoFile imageVideoFile = (ImageVideoFile) ImageListAdapter.this.mList.get(adapterPosition);
                switch (view.getId()) {
                    case C0793R.C0795id.img_delete_image /*2131296803*/:
                        if (ImageListAdapter.this.context instanceof GalleryActivity) {
                            DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_IMAGE_DELETE_CONFIRMATION);
                            instance.setDialogResultListener(new OnConfirmationResult() {
                                public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                                    if (!z) {
                                        dialogFragment.dismiss();
                                    } else if (ImageListAdapter.this.mList.size() > 0 && adapterPosition < ImageListAdapter.this.mList.size() && adapterPosition >= 0) {
                                        Single.create(new SingleOnSubscribe<Boolean>() {
                                            public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                                                File file = new File(imageVideoFile.getPath());
                                                ImageListAdapter.this.context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{imageVideoFile.getPath()});
                                                singleEmitter.onSuccess(Boolean.valueOf(file.delete()));
                                            }
                                        }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                                            public void onSuccess(Boolean bool) {
                                                if (ImageListAdapter.this.mList.size() > 0 && adapterPosition < ImageListAdapter.this.mList.size() && adapterPosition >= 0) {
                                                    ImageListAdapter.this.mList.remove(adapterPosition);
                                                    ImageListAdapter.this.notifyItemRemoved(adapterPosition);
                                                    if (ImageListAdapter.this.imageVideoListener != null) {
                                                        ImageListAdapter.this.imageVideoListener.onDelete();
                                                    }
                                                }
                                                dialogFragment.dismiss();
                                            }

                                            public void onError(Throwable th) {
                                                th.printStackTrace();
                                                dialogFragment.dismiss();
                                            }
                                        });
                                    }
                                }
                            });
                            instance.show(((GalleryActivity) ImageListAdapter.this.context).getSupportFragmentManager(), "image_delete_confirmation");
                            break;
                        }
                        break;
                    case C0793R.C0795id.img_edit_image /*2131296807*/:
                        try {
                            Glide.with(ImageListAdapter.this.context).load(imageVideoFile.getPath()).preload();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(ImageListAdapter.this.context, ImageEditActivity.class);
                        intent.putExtra("image", imageVideoFile.getPath());
                        ((GalleryActivity) ImageListAdapter.this.context).startActivityForResult(intent, GalleryActivity.REQUEST_VIEW_IMAGES);
                        break;
                    case C0793R.C0795id.img_rename_image /*2131296820*/:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageListAdapter.this.context);
                        builder.setTitle(imageVideoFile.isVideo() ? C0793R.string.rename_video : C0793R.string.rename_image);
                        builder.setMessage((int) C0793R.string.rename_video_msg);
                        final EditText editText = new EditText(ImageListAdapter.this.context);
                        editText.setLayoutParams(new LayoutParams(-1, -1));
                        builder.setView((View) editText);
                        builder.setPositiveButton((int) C0793R.string.save, (DialogInterface.OnClickListener) null).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog create = builder.create();
                        create.setOnShowListener(new OnShowListener() {
                            public void onShow(final DialogInterface dialogInterface) {
                                ((AlertDialog) dialogInterface).getButton(-1).setOnClickListener(new OnClickListener() {
                                    public void onClick(View view) {
                                        String str;
                                        String obj = editText.getText().toString();
                                        if (obj.length() == 0) {
                                            editText.setError(ImageListAdapter.this.context.getString(C0793R.string.valid_name));
                                            return;
                                        }
                                        File file = new File(imageVideoFile.getPath());
                                        String str2 = ".";
                                        if (imageVideoFile.isVideo()) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(obj);
                                            sb.append(imageVideoFile.getName().substring(imageVideoFile.getName().lastIndexOf(str2)));
                                            str = sb.toString();
                                        } else {
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append(obj);
                                            sb2.append(imageVideoFile.getName().substring(imageVideoFile.getName().lastIndexOf(str2)));
                                            str = sb2.toString();
                                        }
                                        File file2 = new File(file.getParent(), str);
                                        if (file2.exists()) {
                                            editText.setError(ImageListAdapter.this.context.getString(C0793R.string.video_exists));
                                            return;
                                        }
                                        if (file.exists()) {
                                            file.renameTo(file2);
                                        }
                                        SharedPreferences sharedPreferences = ImageListAdapter.this.context.getSharedPreferences(MainActivity.SHARED_NAME2, 0);
                                        if (sharedPreferences.contains(imageVideoFile.getPath())) {
                                            String string = sharedPreferences.getString(imageVideoFile.getPath(), null);
                                            if (string != null) {
                                                sharedPreferences.edit().putString(file2.getAbsolutePath(), string).remove(imageVideoFile.getPath()).apply();
                                            }
                                        }
                                        imageVideoFile.setPath(file2.getAbsolutePath());
                                        imageVideoFile.setName(file2.getName());
                                        imageVideoFile.setVideo(file2.getAbsolutePath().endsWith(".mp4"));
                                        imageVideoFile.setFileSize(file2.length());
                                        ImageListAdapter.this.mList.set(adapterPosition, imageVideoFile);
                                        ImageListAdapter.this.notifyItemChanged(adapterPosition);
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        });
                        create.getWindow().setSoftInputMode(4);
                        create.show();
                        break;
                    case C0793R.C0795id.img_share_image /*2131296823*/:
                        SharedPreferences sharedPreferences = ImageListAdapter.this.context.getSharedPreferences(MainActivity.SHARED_NAME2, 0);
                        if (sharedPreferences.contains(imageVideoFile.getPath())) {
                            String string = sharedPreferences.getString(imageVideoFile.getPath(), null);
                            if (string != null) {
                                AppUtils.addCount(ImageListAdapter.this.context, 4);
                                Intent intent2 = new Intent("android.intent.action.SEND");
                                intent2.setType("text/plain");
                                intent2.putExtra("android.intent.extra.SUBJECT", C0793R.string.share_image);
                                StringBuilder sb = new StringBuilder();
                                sb.append(ImageListAdapter.this.context.getString(C0793R.string.server_share));
                                sb.append(" ");
                                sb.append(string);
                                sb.append(" .\n");
                                sb.append(ImageListAdapter.this.context.getString(C0793R.string.app_download));
                                intent2.putExtra("android.intent.extra.TEXT", sb.toString());
                                ImageListAdapter.this.context.startActivity(Intent.createChooser(intent2, ImageListAdapter.this.context.getString(C0793R.string.share_image)));
                                ImageListAdapter.this.addToFirebaseAnalytics(false);
                                return;
                            }
                        }
                        ShareImageDialogFragment shareImageDialogFragment = new ShareImageDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("video", imageVideoFile.getPath());
                        shareImageDialogFragment.setArguments(bundle);
                        shareImageDialogFragment.show(((GalleryActivity) ImageListAdapter.this.context).getSupportFragmentManager(), "IMG");
                        break;
                    case C0793R.C0795id.img_upload_image /*2131296828*/:
                        if (adapterPosition < ImageListAdapter.this.mList.size() && !imageVideoFile.isVideo()) {
                            Observable.create(new ObservableOnSubscribe<String>() {
                                public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                                    String str = "youtube_account_email";
                                    if (ImageListAdapter.this.prefs.contains(str)) {
                                        observableEmitter.onNext(ImageListAdapter.this.prefs.getString(str, ""));
                                        observableEmitter.onComplete();
                                        return;
                                    }
                                    try {
                                        YoutubeAPI.getInstance().switchGoogleAccount((GalleryActivity) ImageListAdapter.this.context).subscribe(new Consumer<String>() {
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
                                    Intent intent = new Intent(ImageListAdapter.this.context, ImageUploadService.class);
                                    intent.putExtra("file_path", imageVideoFile.getPath());
                                    String str2 = "";
                                    intent.putExtra("aId", ImageListAdapter.this.sharedPreferences.getString(ServerAPI.ANONYMOUS_ID, str2));
                                    intent.putExtra("uId", ImageListAdapter.this.sharedPreferences.getString(ServerAPI.USER_ID, str2));
                                    intent.putExtra("email", str);
                                    ImageListAdapter.this.context.startService(intent);
                                }
                            });
                            break;
                        }
                }
            }
        }
    }

    public interface ImageVideoListener {
        void onDelete();

        void refresh();
    }

    public ImageListAdapter(Context context2, ImageVideoListener imageVideoListener2, boolean z) {
        this.imageVideoListener = imageVideoListener2;
        this.context = context2;
        this.isTrim = z;
        this.sharedPreferences = context2.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context2);
    }

    public void addItem(ImageVideoFile imageVideoFile) {
        this.mList.add(imageVideoFile);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void sortList() {
        Collections.sort(this.mList, this.lastModifySort);
        notifyDataSetChanged();
    }

    public void addAllItems(List<ImageVideoFile> list, boolean z) {
        this.mList.clear();
        if (list.size() != 0) {
            if (z) {
                addItemAtPosition(0, new NativeAdTempModel());
            }
            this.mList.addAll(list);
            if (this.mList.size() > 0) {
                notifyItemRangeChanged(this.mList.size() - 1, this.mList.size());
            } else {
                notifyItemRangeChanged(0, this.mList.size());
            }
        }
    }

    public void addItemAtPosition(int i, Object obj) {
        if (!(obj instanceof NativeAdTempModel) || !(this.mList.get(i) instanceof NativeAdTempModel)) {
            this.mList.add(i, obj);
            notifyDataSetChanged();
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_IMAGE_LIST_ITEM) {
            return new ImageListItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.img_new_list_item, viewGroup, false));
        }
        return new ImageAdHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.img_new_ad_list_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_IMAGE_LIST_ITEM) {
            ImageListItemHolder imageListItemHolder = (ImageListItemHolder) viewHolder;
            ImageVideoFile imageVideoFile = (ImageVideoFile) this.mList.get(i);
            Glide.with(this.context).load(imageVideoFile.getPath()).centerCrop().into(imageListItemHolder.mFileImage);
            String str = ".mp4";
            String str2 = "";
            if (imageVideoFile.getName().endsWith(str)) {
                imageListItemHolder.mFileName.setText(imageVideoFile.getName().replace(str, str2));
            } else {
                imageListItemHolder.mFileName.setText(imageVideoFile.getName().replace(".jpg", str2));
            }
            try {
                if (imageVideoFile.getFileSize() == 0) {
                    imageVideoFile.setFileSize(new File(imageVideoFile.getPath()).length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                imageListItemHolder.mFileTime.setText(DateUtils.getRelativeTimeSpanString(imageVideoFile.getCreated(), System.currentTimeMillis(), 1000).toString());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            TextView textView = imageListItemHolder.mFileSize;
            StringBuilder sb = new StringBuilder();
            sb.append(((double) Math.round((float) (((imageVideoFile.getFileSize() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
            sb.append("M");
            textView.setText(sb.toString());
        }
    }

    public int getItemViewType(int i) {
        if (this.mList.get(i) instanceof ImageVideoFile) {
            return VIEW_TYPE_IMAGE_LIST_ITEM;
        }
        return 1332;
    }

    public int getItemCount() {
        return this.mList.size();
    }

    /* access modifiers changed from: private */
    public void addToFirebaseAnalytics(boolean z) {
        if (z) {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
        } else {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
        }
    }

    public void removeAllItems() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    private boolean isNetworkConnected() {
        return ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }
}
