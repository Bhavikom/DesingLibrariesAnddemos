package com.yasoka.eazyscreenrecord.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
//import android.support.p000v4.app.DialogFragment;
//import android.support.p000v4.content.FileProvider;
//import android.support.p000v4.media.session.PlaybackStateCompat;
//import android.support.p003v7.app.AlertDialog;
//import android.support.p003v7.widget.RecyclerView.Adapter;
//import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
//import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.crashlytics.android.Crashlytics;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.ShowImagesActivity;
import com.ezscreenrecorder.activities.TrimVideoActivity;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;*/
import com.ezscreenrecorder.R;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
/*import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;*/
//import com.yasoka.eazyscreenrecord.R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.ShowImagesActivity;
import com.yasoka.eazyscreenrecord.activities.TrimVideoActivity;
import com.yasoka.eazyscreenrecord.alertdialogs.DeleteConfirmationDialog;
import com.yasoka.eazyscreenrecord.model.ImageVideoFile;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
//import life.knowledge4.videotrimmer.utils.FileUtils;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_AD_LIST_ITEM = 1332;
    private static final int VIEW_TYPE_VIDEO_LIST_ITEM = 1331;
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
    private final SharedPreferences sharedPreferences;

    public interface ImageVideoListener {
        void itemClick();

        void onDelete();

        void onUploadToYoutube(ImageVideoFile imageVideoFile);

        void refresh();
    }

    class VideoAdItemHolder extends RecyclerView.ViewHolder {
        public VideoAdItemHolder(View view) {
            super(view);
        }

        /*private void initNativeAd() {
            Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
                public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                    AdLoader build = new Builder(context.getApplicationContext(), context.getString(R.string.key_recording_native_ad)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
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
                    VideoAdItemHolder.addValuesAppInstallAdView(unifiedNativeAd);
                }
            });
        }*/

        /* access modifiers changed from: private */
        /*public void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
            adView.getIconView().setBackgroundColor(-7829368);
            ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
            if (unifiedNativeAd.getIcon() != null) {
                Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                if (drawable != null) {
                    adView.getIconView().setBackgroundColor(0);
                    ((ImageView) adView.getIconView()).setImageDrawable(drawable);
                }
            }
            ((Button) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            adView.setNativeAd(unifiedNativeAd);
        }*/
    }

    class VideoListItemHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView mFileDuration;
        ImageView mFileImage;
        TextView mFileName;
        TextView mFileResolution;
        TextView mFileSize;
        TextView mFileTime;

        public VideoListItemHolder(View view) {
            super(view);
            mFileImage = (ImageView) view.findViewById(R.id.img_file);
            mFileName = (TextView) view.findViewById(R.id.txt_file_name);
            mFileDuration = (TextView) view.findViewById(R.id.txt_file_duration);
            mFileTime = (TextView) view.findViewById(R.id.txt_time);
            mFileResolution = (TextView) view.findViewById(R.id.txt_resolution);
            mFileSize = (TextView) view.findViewById(R.id.txt_file_size);
            view.setOnClickListener(new OnClickListener(this) {
                public void onClick(View view) {
                    int adapterPosition = (int) VideoListItemHolder.getAdapterPosition();
                    if (adapterPosition != -1) {
                        ImageVideoFile imageVideoFile = (ImageVideoFile) mList.get(adapterPosition);
                        if (imageVideoFile.isVideo()) {
                            Intent intent = new Intent(context, NewVideoPlayerActivity.class);
                            intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, imageVideoFile.getPath());
                            intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, imageVideoFile.getDuration());
                            if (context instanceof GalleryActivity) {
                                ((GalleryActivity) context).startActivityForResult(intent, GalleryActivity.REQUEST_VIEW);
                            }
                        } else {
                            Intent intent2 = new Intent(context, ShowImagesActivity.class);
                            intent2.putExtra("ImgPath", imageVideoFile.getPath());
                            if (context instanceof GalleryActivity) {
                                ((GalleryActivity) context).startActivityForResult(intent2, GalleryActivity.REQUEST_VIEW_IMAGES);
                            }
                        }
                        if (imageVideoListener != null) {
                            imageVideoListener.itemClick();
                        }
                    }
                }
            });
            view.findViewById(R.id.img_delete_video).setOnClickListener(this);
            view.findViewById(R.id.img_share_video).setOnClickListener(this);
            view.findViewById(R.id.img_you_tube_video).setOnClickListener(this);
            view.findViewById(R.id.img_rename_video).setOnClickListener(this);
            view.findViewById(R.id.img_trim_video).setOnClickListener(this);
            setTouchForImage(R.id.img_delete_video);
            setTouchForImage(R.id.img_share_video);
            setTouchForImage(R.id.img_trim_video);
            setTouchForImage(R.id.img_you_tube_video);
            setTouchForImage(R.id.img_rename_video);
        }

        private void setTouchForImage(int i) {
            itemView.findViewById(i).setOnTouchListener(new ChangeFilterTouchListenr((ImageView) itemView.findViewById(i)));
        }

        public void onClick(View view) {
            final int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1 || context != null) {
                final ImageVideoFile imageVideoFile = (ImageVideoFile) mList.get(adapterPosition);
                switch (view.getId()) {
                    case R.id.img_delete_video /*2131296804*/:
                        if (mList.size() > 0 && adapterPosition < mList.size() && adapterPosition >= 0 && (context instanceof GalleryActivity)) {
                            DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_RECORDING_DELETE_CONFIRMATION);
                            instance.setDialogResultListener(new DeleteConfirmationDialog.OnConfirmationResult() {
                                public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                                    if (z) {
                                        Single.just(Integer.valueOf(adapterPosition)).flatMap(new Function<Integer, SingleSource<Integer>>() {
                                            public SingleSource<Integer> apply(final Integer num) throws Exception {
                                                return Single.create(new SingleOnSubscribe<Integer>() {
                                                    public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                                                        File file = new File(imageVideoFile.getPath());
                                                        context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{imageVideoFile.getPath()});
                                                        file.delete();
                                                        if (!singleEmitter.isDisposed()) {
                                                            singleEmitter.onSuccess(num);
                                                        }
                                                    }
                                                });
                                            }
                                        }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Integer>() {
                                            public void onSuccess(Integer num) {
                                                if (mList.size() > 0 && num.intValue() < mList.size() && num.intValue() >= 0) {
                                                    mList.remove(num.intValue());
                                                    notifyItemRemoved(num.intValue());
                                                    if (imageVideoListener != null) {
                                                        imageVideoListener.onDelete();
                                                    }
                                                }
                                                dialogFragment.dismiss();
                                            }

                                            public void onError(Throwable th) {
                                                th.printStackTrace();
                                                dialogFragment.dismiss();
                                            }
                                        });
                                    } else {
                                        dialogFragment.dismiss();
                                    }
                                }
                            });
                            instance.show(((GalleryActivity) context).getSupportFragmentManager(), "recording_delete_confirmation");
                            break;
                        }
                    case R.id.img_rename_video /*2131296821*/:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(imageVideoFile.isVideo() ? R.string.rename_video : R.string.rename_image);
                        builder.setMessage((int) R.string.rename_video_msg);
                        final EditText editText = new EditText(context);
                        editText.setLayoutParams(new LayoutParams(-1, -1));
                        builder.setView((View) editText);
                        builder.setPositiveButton((int) R.string.save, (DialogInterface.OnClickListener) null).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
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
                                            editText.setError(context.getString(R.string.valid_name));
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
                                            editText.setError(context.getString(R.string.video_exists));
                                            return;
                                        }
                                        if (file.exists()) {
                                            file.renameTo(file2);
                                        }
                                        imageVideoFile.setPath(file2.getAbsolutePath());
                                        imageVideoFile.setName(file2.getName());
                                        imageVideoFile.setVideo(file2.getAbsolutePath().endsWith(".mp4"));
                                        imageVideoFile.setFileSize(file2.length());
                                        mList.set(adapterPosition, imageVideoFile);
                                        notifyItemChanged(adapterPosition);
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        });
                        create.getWindow().setSoftInputMode(4);
                        create.show();
                        break;
                    case R.id.img_share_video /*2131296824*/:
                        if (imageVideoListener != null) {
                            imageVideoListener.itemClick();
                        }
                        if (imageVideoFile.isVideo()) {
                            if (prefs.getBoolean("notifications_compress", false)) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2.setTitle((int) R.string.compress_video);
                                View inflate = LayoutInflater.from(context).inflate(R.layout.compress_dialog, null, false);
                                builder2.setView(inflate);
                                final Spinner spinner = (Spinner) inflate.findViewById(R.id.spin_resolution);
                                final Spinner spinner2 = (Spinner) inflate.findViewById(R.id.spin_bit_rate);
                                builder2.setPositiveButton((int) R.string.save, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String[] split = ((String) spinner.getSelectedItem()).split("x");
                                        Integer.parseInt(split[0]);
                                        Integer.parseInt(split[1]);
                                        Integer.parseInt(((String) spinner2.getSelectedItem()).replace("%", "").trim());
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(context.getString(R.string.compressing));
                                        progressDialog.setProgressStyle(0);
                                        progressDialog.setIndeterminate(true);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        Single.timer(50, TimeUnit.MILLISECONDS).flatMapPublisher(new Function<Long, Publisher<String>>() {
                                            public Publisher<String> apply(Long l) throws Exception {
                                                return Flowable.create(new FlowableOnSubscribe<String>() {
                                                    public void subscribe(FlowableEmitter<String> flowableEmitter) throws Exception {
                                                        if (!prefs.getBoolean("notifications_compress", false)) {
                                                            flowableEmitter.onNext(imageVideoFile.getPath());
                                                        } else {
                                                            flowableEmitter.onError(new Exception("Unable To Compress Video"));
                                                        }
                                                        flowableEmitter.onComplete();
                                                    }
                                                }, BackpressureStrategy.BUFFER);
                                            }
                                        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<String>() {
                                            public void onNext(String str) {
                                                MediaScannerConnection.scanFile(context, new String[]{str}, null, new OnScanCompletedListener() {
                                                    public void onScanCompleted(String str, Uri uri) {
                                                        Intent intent = new Intent("android.intent.action.SEND");
                                                        intent.setType("video/mp4");
                                                        intent.putExtra("android.intent.extra.TITLE", R.string.share_video);
                                                        intent.putExtra("android.intent.extra.SUBJECT", R.string.share_video);
                                                        intent.putExtra("android.intent.extra.TEXT", context.getString(R.string.share_video_txt));
                                                        intent.addFlags(1);
                                                        Context access$100 = context;
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append(context.getPackageName());
                                                        sb.append(".my.package.name.provider");
                                                        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(access$100, sb.toString(), new File(str)));
                                                        PrintStream printStream = System.out;
                                                        StringBuilder sb2 = new StringBuilder();
                                                        sb2.append("onScanCompleted uri ");
                                                        sb2.append(uri);
                                                        printStream.println(sb2.toString());
                                                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_video)));
                                                        addToFirebaseAnalytics(imageVideoFile.isVideo());
                                                    }
                                                });
                                            }

                                            public void onError(Throwable th) {
                                                th.printStackTrace();
                                                try {
                                                    progressDialog.dismiss();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            public void onComplete() {
                                                try {
                                                    progressDialog.dismiss();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });
                                builder2.create().show();
                                AppUtils.addCount(context, 5);
                                break;
                            } else {
                                MediaScannerConnection.scanFile(context, new String[]{imageVideoFile.getPath()}, null, new OnScanCompletedListener() {
                                    public void onScanCompleted(String str, Uri uri) {
                                        AppUtils.addCount(context, 5);
                                        Intent intent = new Intent("android.intent.action.SEND");
                                        intent.setType("video/mp4");
                                        String str2 = "Share Video";
                                        intent.putExtra("android.intent.extra.TITLE", str2);
                                        intent.putExtra("android.intent.extra.SUBJECT", str2);
                                        intent.putExtra("android.intent.extra.TEXT", context.getString(R.string.share_video_txt));
                                        intent.addFlags(1);
                                        Context access$100 = context;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(context.getPackageName());
                                        sb.append(".my.package.name.provider");
                                        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(access$100, sb.toString(), new File(str)));
                                        PrintStream printStream = System.out;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("onScanCompleted uri ");
                                        sb2.append(uri);
                                        printStream.println(sb2.toString());
                                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_video)));
                                        addToFirebaseAnalytics(imageVideoFile.isVideo());
                                    }
                                });
                                break;
                            }
                        }
                        break;
                    case R.id.img_trim_video /*2131296827*/:
                        if (imageVideoListener != null) {
                            imageVideoListener.itemClick();
                        }
                        if (imageVideoFile.getDuration() >= 5000) {
                            Intent intent = new Intent(context, TrimVideoActivity.class);
                            intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_PATH, FileUtils.getPath(context, Uri.fromFile(new File(imageVideoFile.getPath()))));
                            intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_DURATION, imageVideoFile.getDuration());
                            if (context instanceof GalleryActivity) {
                                ((GalleryActivity) context).startActivityForResult(intent, GalleryActivity.TRIM_REQUEST);
                                break;
                            }
                        } else {
                            Toast.makeText(context, R.string.id_trim_length_error_msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case R.id.img_you_tube_video /*2131296834*/:
                        if (imageVideoFile.isVideo()) {
                            if (isNetworkConnected()) {
                                if (!AppUtils.containUploadFile(context)) {
                                    if (imageVideoListener != null) {
                                        imageVideoListener.onUploadToYoutube(imageVideoFile);
                                        break;
                                    }
                                } else {
                                    Toast.makeText(context, R.string.upload_in_progress_one, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        break;
                }
            }
        }
    }

    public VideoListAdapter(Context context2, ImageVideoListener imageVideoListener2, boolean z) {
        imageVideoListener = imageVideoListener2;
        context = context2;
        isTrim = z;
        sharedPreferences = context2.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        prefs = PreferenceManager.getDefaultSharedPreferences(context2);
    }

    public void addItem(ImageVideoFile imageVideoFile) {
        mList.add(imageVideoFile);
        notifyItemInserted(mList.size() - 1);
    }

    public void sortList() {
        Collections.sort(mList, lastModifySort);
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return mList;
    }

    public void addAllItems(List<ImageVideoFile> list, boolean z) {
        mList.clear();
        if (list.size() != 0) {
            if (z) {
                addItemAtPosition(0, new NativeAdTempModel());
            }
            mList.addAll(list);
            if (mList.size() > 0) {
                notifyItemRangeChanged(mList.size() - 1, mList.size());
            } else {
                notifyItemRangeChanged(0, mList.size());
            }
        }
    }

    public void addItemAtPosition(int i, Object obj) {
        if (!(obj instanceof NativeAdTempModel) || !(mList.get(i) instanceof NativeAdTempModel)) {
            mList.add(i, obj);
            notifyDataSetChanged();
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_VIDEO_LIST_ITEM) {
            return new VideoListItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vid_new_list_item, viewGroup, false));
        }
        return new VideoAdItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vid_new_ad_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_VIDEO_LIST_ITEM) {
            ImageVideoFile imageVideoFile = (ImageVideoFile) mList.get(i);
            VideoListItemHolder videoListItemHolder = (VideoListItemHolder) viewHolder;
            if (imageVideoFile.isVideo()) {
                videoListItemHolder.mFileDuration.setVisibility(View.VISIBLE);
                if (!imageVideoFile.isVideo() || imageVideoFile.getDuration() == 0) {
                    videoListItemHolder.mFileDuration.setVisibility(View.GONE);
                } else {
                    videoListItemHolder.mFileDuration.setVisibility(View.VISIBLE);
                    long duration = imageVideoFile.getDuration();
                    if (TimeUnit.MILLISECONDS.toHours(duration) == View.VISIBLE) {
                        videoListItemHolder.mFileDuration.setText(String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))}));
                    } else {
                        videoListItemHolder.mFileDuration.setText(String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(duration)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))}));
                    }
                }
                try {
                    if (imageVideoFile.getPath() != null) {
                        Glide.with(context).load(imageVideoFile.getPath()).asBitmap().
                                videoDecoder(new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(1000000),
                                        Glide.get(context).getBitmapPool(), DecodeFormat.PREFER_ARGB_8888)).centerCrop().into(videoListItemHolder.mFileImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            } else {
                try {
                    Glide.with(context).load(imageVideoFile.getPath()).centerCrop().into(videoListItemHolder.mFileImage);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            String str = ".mp4";
            String str2 = "";
            if (imageVideoFile.getName().endsWith(str)) {
                videoListItemHolder.mFileName.setText(imageVideoFile.getName().replace(str, str2));
            } else {
                videoListItemHolder.mFileName.setText(imageVideoFile.getName().replace(".jpg", str2));
            }
            try {
                if (imageVideoFile.getFileSize() == 0) {
                    imageVideoFile.setFileSize(new File(imageVideoFile.getPath()).length());
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            TextView textView = videoListItemHolder.mFileSize;
            StringBuilder sb = new StringBuilder();
            sb.append(((double) Math.round((float) (((imageVideoFile.getFileSize() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
            sb.append("M");
            textView.setText(sb.toString());
            if (imageVideoFile.getResolution() != null) {
                videoListItemHolder.mFileResolution.setText(imageVideoFile.getResolution());
            }
            try {
                videoListItemHolder.mFileTime.setText(DateUtils.getRelativeTimeSpanString(imageVideoFile.getCreated(), System.currentTimeMillis(), 1000).toString());
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    public int getItemViewType(int i) {
        if (mList.get(i) instanceof ImageVideoFile) {
            return VIEW_TYPE_VIDEO_LIST_ITEM;
        }
        return 1332;
    }

    public int getItemCount() {
        return mList.size();
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
        mList.clear();
        notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public boolean isNetworkConnected() {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
