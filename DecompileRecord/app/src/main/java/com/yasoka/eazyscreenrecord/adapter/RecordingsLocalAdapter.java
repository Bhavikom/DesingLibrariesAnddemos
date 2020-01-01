package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.content.FileProvider;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
//import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.TrimVideoActivity;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.model.VideoFileModel;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.FilesAccessHelper;
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
import com.yasoka.eazyscreenrecord.activities.TrimVideoActivity;
import com.yasoka.eazyscreenrecord.alertdialogs.DeleteConfirmationDialog;
import com.yasoka.eazyscreenrecord.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.model.VideoFileModel;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;
import com.yasoka.eazyscreenrecord.utils.FilesAccessHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
/*import life.knowledge4.videotrimmer.utils.FileUtils;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;*/

public class RecordingsLocalAdapter extends RecyclerView.Adapter {
    public static final int VIEW_TYPE_AD_LIST_ITEM = 1332;
    private static final int VIEW_TYPE_VIDEO_LIST_ITEM = 1331;
    /* access modifiers changed from: private */
    public Context activityContext;
    /* access modifiers changed from: private */
    public List<Object> mList = new ArrayList();
    /* access modifiers changed from: private */
    public VideoLocalListListener videoLocalListListener;

    static class RecordingsAdItemHolder extends RecyclerView.ViewHolder {
        //private UnifiedNativeAdView adView;

        public RecordingsAdItemHolder(View view) {
            super(view);
            /*this.adView = (UnifiedNativeAdView) view.findViewById(R.id.id_native_video_ad_view);
            UnifiedNativeAdView unifiedNativeAdView = this.adView;
            unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.id_native_video_app_icon_imageview));
            UnifiedNativeAdView unifiedNativeAdView2 = this.adView;
            unifiedNativeAdView2.setHeadlineView(unifiedNativeAdView2.findViewById(R.id.id_native_video_title_txt));
            UnifiedNativeAdView unifiedNativeAdView3 = this.adView;
            unifiedNativeAdView3.setBodyView(unifiedNativeAdView3.findViewById(R.id.id_native_video_subTitle_txt));
            UnifiedNativeAdView unifiedNativeAdView4 = this.adView;
            unifiedNativeAdView4.setCallToActionView(unifiedNativeAdView4.findViewById(R.id.id_native_video_button));
            initNativeAd();*/
        }

       /* private void initNativeAd() {
            Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
                public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                    AdLoader build = new Builder(RecorderApplication.getInstance().getApplicationContext(),
                            RecorderApplication.getInstance().getString(R.string.key_recording_native_ad)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
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
                    RecordingsAdItemHolder.this.addValuesAppInstallAdView(unifiedNativeAd);
                }
            });
        }*/

        /* access modifiers changed from: private */
        /*public void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
            this.adView.getIconView().setBackgroundColor(-7829368);
            ((TextView) this.adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            ((TextView) this.adView.getBodyView()).setText(unifiedNativeAd.getBody());
            if (unifiedNativeAd.getIcon() != null) {
                Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                if (drawable != null) {
                    this.adView.getIconView().setBackgroundColor(0);
                    ((ImageView) this.adView.getIconView()).setImageDrawable(drawable);
                }
            }
            ((Button) this.adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            this.adView.setNativeAd(unifiedNativeAd);
        }*/
    }

    class RecordingsViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public TextView mFileDuration;
        /* access modifiers changed from: private */
        public ImageView mFileImage;
        /* access modifiers changed from: private */
        public TextView mFileName;
        /* access modifiers changed from: private */
        public TextView mFileResolution;
        /* access modifiers changed from: private */
        public TextView mFileSize;
        /* access modifiers changed from: private */
        public TextView mFileTime;

        public RecordingsViewHolder(View view) {
            super(view);
            this.mFileImage = (ImageView) view.findViewById(R.id.img_file);
            this.mFileName = (TextView) view.findViewById(R.id.txt_file_name);
            this.mFileDuration = (TextView) view.findViewById(R.id.txt_file_duration);
            this.mFileTime = (TextView) view.findViewById(R.id.txt_time);
            this.mFileResolution = (TextView) view.findViewById(R.id.txt_resolution);
            this.mFileSize = (TextView) view.findViewById(R.id.txt_file_size);

            view.findViewById(R.id.img_delete_video).setOnClickListener(this);
            view.findViewById(R.id.img_share_video).setOnClickListener(this);
            view.findViewById(R.id.img_you_tube_video).setOnClickListener(this);
            view.findViewById(R.id.img_rename_video).setOnClickListener(this);
            view.findViewById(R.id.img_trim_video).setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            final int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                final VideoFileModel videoFileModel = (VideoFileModel) mList.get(adapterPosition);
                if (videoFileModel != null) {
                    switch (view.getId()) {
                        case R.id.img_delete_video /*2131296804*/:
                            if (activityContext instanceof GalleryActivity) {
                                DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_RECORDING_DELETE_CONFIRMATION);
                                instance.setDialogResultListener(new OnConfirmationResult() {
                                    public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                                        if (z) {
                                            Single.just(Integer.valueOf(adapterPosition)).flatMap(new Function<Integer, SingleSource<Integer>>() {
                                                public SingleSource<Integer> apply(final Integer num) throws Exception {
                                                    return Single.create(new SingleOnSubscribe<Integer>() {
                                                        public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                                                            File file = new File(videoFileModel.getPath());
                                                            activityContext.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{videoFileModel.getPath()});
                                                            file.delete();
                                                            FilesAccessHelper.getInstance().refreshGallery(activityContext, videoFileModel.getPath());
                                                            if (!singleEmitter.isDisposed()) {
                                                                singleEmitter.onSuccess(num);
                                                            }
                                                        }
                                                    });
                                                }
                                            }).subscribe(new DisposableSingleObserver<Integer>() {
                                                public void onSuccess(Integer num) {
                                                    if (mList.size() > 0 && num.intValue() < mList.size() && num.intValue() >= 0) {
                                                        mList.remove(num.intValue());
                                                        notifyItemRemoved(num.intValue());
                                                        if (videoLocalListListener != null) {
                                                            videoLocalListListener.onDelete();
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
                                instance.show(((GalleryActivity) activityContext).getSupportFragmentManager(), "recording_delete_confirmation");
                                break;
                            }
                            break;
                        case R.id.img_rename_video /*2131296821*/:
                            AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                            builder.setTitle((int) R.string.rename_video);
                            builder.setMessage((int) R.string.rename_video_msg);
                            final EditText editText = new EditText(activityContext);
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
                                            String obj = editText.getText().toString();
                                            if (obj.length() == 0) {
                                                editText.setError(activityContext.getString(R.string.valid_name));
                                                return;
                                            }
                                            File file = new File(videoFileModel.getPath());
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(obj);
                                            sb.append(videoFileModel.getName().substring(videoFileModel.getName().lastIndexOf(".")));
                                            File file2 = new File(file.getParent(), sb.toString());
                                            if (file2.exists()) {
                                                editText.setError(activityContext.getString(R.string.video_exists));
                                                return;
                                            }
                                            if (file.exists()) {
                                                file.renameTo(file2);
                                            }
                                            videoFileModel.setPath(file2.getAbsolutePath());
                                            videoFileModel.setName(file2.getName());
                                            videoFileModel.setFileSize(file2.length());
                                            mList.set(adapterPosition, videoFileModel);
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
                            MediaScannerConnection.scanFile(activityContext, new String[]{videoFileModel.getPath()}, null, new OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                    Intent intent = new Intent("android.intent.action.SEND");
                                    intent.setType("video/mp4");
                                    intent.putExtra("android.intent.extra.TITLE", R.string.share_video);
                                    intent.putExtra("android.intent.extra.SUBJECT", R.string.share_video);
                                    intent.putExtra("android.intent.extra.TEXT", activityContext.getString(R.string.share_video_txt));
                                    intent.addFlags(1);
                                    Context access$800 = activityContext;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(activityContext.getPackageName());
                                    sb.append(".my.package.name.provider");
                                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(access$800, sb.toString(), new File(str)));
                                    PrintStream printStream = System.out;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("onScanCompleted uri ");
                                    sb2.append(uri);
                                    printStream.println(sb2.toString());
                                    activityContext.startActivity(Intent.createChooser(intent, activityContext.getString(R.string.share_video)));
                                    AppUtils.addCount(activityContext, 5);
                                    FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
                                }
                            });
                            break;
                        case R.id.img_trim_video /*2131296827*/:
                            if (videoFileModel.getDuration() >= 5000) {
                                Intent intent = new Intent(activityContext, TrimVideoActivity.class);
                                intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_PATH, FileUtils.getPath(activityContext, Uri.fromFile(new File(videoFileModel.getPath()))));
                                intent.putExtra(TrimVideoActivity.EXTRA_VIDEO_DURATION, videoFileModel.getDuration());
                                if (activityContext instanceof GalleryActivity) {
                                    ((GalleryActivity) activityContext).startActivityForResult(intent, GalleryActivity.TRIM_REQUEST);
                                    break;
                                }
                            } else {
                                Toast.makeText(activityContext, R.string.id_trim_length_error_msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            break;
                        case R.id.img_you_tube_video /*2131296834*/:
                            if (RecorderApplication.getInstance().isNetworkAvailable()) {
                                if (!AppUtils.containUploadFile(activityContext)) {
                                    if (videoLocalListListener != null) {
                                        videoLocalListListener.onUploadToYoutube(videoFileModel);
                                        break;
                                    }
                                } else {
                                    Toast.makeText(activityContext, R.string.upload_in_progress_one, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(activityContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            break;
                        default:
                            Intent intent2 = new Intent(activityContext, NewVideoPlayerActivity.class);
                            intent2.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_PATH, videoFileModel.getPath());
                            intent2.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, videoFileModel.getDuration());
                            if (activityContext instanceof GalleryActivity) {
                                ((GalleryActivity) activityContext).startActivityForResult(intent2, GalleryActivity.REQUEST_VIEW);
                                break;
                            }
                            break;
                    }
                }
            }
        }
    }

    public interface VideoLocalListListener {
        void onDelete();

        void onUploadToYoutube(VideoFileModel videoFileModel);
    }

    public RecordingsLocalAdapter(Context context, VideoLocalListListener videoLocalListListener2) {
        this.activityContext = context;
        this.videoLocalListListener = videoLocalListListener2;
    }

    public void addItem(VideoFileModel videoFileModel) {
        this.mList.add(videoFileModel);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void addItemAtPosition(int i, Object obj) {
        if (!(obj instanceof NativeAdTempModel) || !(this.mList.get(i) instanceof NativeAdTempModel)) {
            this.mList.add(i, obj);
            notifyDataSetChanged();
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_VIDEO_LIST_ITEM) {
            return new RecordingsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vid_new_list_item, viewGroup, false));
        }
        return new RecordingsAdItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vid_new_ad_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_VIDEO_LIST_ITEM) {
            VideoFileModel videoFileModel = (VideoFileModel) this.mList.get(i);
            RecordingsViewHolder recordingsViewHolder = (RecordingsViewHolder) viewHolder;
            recordingsViewHolder.mFileDuration.setVisibility(View.VISIBLE);
            if (videoFileModel.getDuration() != 0) {
                recordingsViewHolder.mFileDuration.setVisibility(View.VISIBLE);
                long duration = videoFileModel.getDuration();
                if (TimeUnit.MILLISECONDS.toHours(duration) == 0) {
                    recordingsViewHolder.mFileDuration.setText(String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))}));
                } else {
                    recordingsViewHolder.mFileDuration.setText(String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(duration)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))}));
                }
            } else {
                recordingsViewHolder.mFileDuration.setVisibility(View.GONE);
            }
            try {
                if (!TextUtils.isEmpty(videoFileModel.getPath())) {
                    /*Glide.with(this.activityContext).load(videoFileModel.getPath()).asBitmap().
                            videoDecoder(new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(1000000),
                                    Glide.get(this.activityContext).getBitmapPool(), DecodeFormat.PREFER_ARGB_8888)).centerCrop().into(recordingsViewHolder.mFileImage);*/
                    Glide.with(this.activityContext).asBitmap().load(videoFileModel.getPath()).apply(new RequestOptions().centerCrop()).into(recordingsViewHolder.mFileImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            String str = ".mp4";
            if (videoFileModel.getName().endsWith(str)) {
                recordingsViewHolder.mFileName.setText(videoFileModel.getName().replace(str, ""));
            }
            try {
                if (videoFileModel.getFileSize() == 0) {
                    videoFileModel.setFileSize(new File(videoFileModel.getPath()).length());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            TextView access$300 = recordingsViewHolder.mFileSize;
            StringBuilder sb = new StringBuilder();
            sb.append(((double) Math.round((float) (((videoFileModel.getFileSize() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
            sb.append("M");
            access$300.setText(sb.toString());
            if (videoFileModel.getResolution() != null) {
                recordingsViewHolder.mFileResolution.setText(videoFileModel.getResolution());
            }
            try {
                recordingsViewHolder.mFileTime.setText(DateUtils.getRelativeTimeSpanString(videoFileModel.getCreated(), System.currentTimeMillis(), 1000).toString());
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    public int getItemViewType(int i) {
        if (this.mList.get(i) instanceof VideoFileModel) {
            return VIEW_TYPE_VIDEO_LIST_ITEM;
        }
        return 1332;
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public boolean isListEmpty() {
        List<Object> list = this.mList;
        return list == null || list.size() <= 0;
    }

    public void removeAllItems() {
        List<Object> list = this.mList;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }
}
