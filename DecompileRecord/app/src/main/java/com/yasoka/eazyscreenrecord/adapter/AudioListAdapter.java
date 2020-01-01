package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import android.widget.TextView;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog;
import com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.OnConfirmationResult;
import com.ezscreenrecorder.interfaces.OnListItemClickListener;
import com.ezscreenrecorder.model.AudioFileModel;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.utils.EEAConsentHelper;*/
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
/*import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.alertdialogs.DeleteConfirmationDialog;
import com.yasoka.eazyscreenrecord.interfaces.OnListItemClickListener;
import com.yasoka.eazyscreenrecord.model.AudioFileModel;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.utils.EEAConsentHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
/*import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;*/

public class AudioListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_AD_LIST_ITEM = 1632;
    private static final int VIEW_TYPE_AUDIO_LIST_ITEM = 1631;
    private Comparator lastModifySort = new Comparator<AudioFileModel>() {
        public int compare(AudioFileModel audioFileModel, AudioFileModel audioFileModel2) {
            return Long.compare(audioFileModel2.getFileCreated(), audioFileModel.getFileCreated());
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public List<Object> mList = new ArrayList();
    /* access modifiers changed from: private */
    public OnItemDeleteCallback onItemDeleteCallback;
    /* access modifiers changed from: private */
    public OnListItemClickListener onListItemClickListener;

    class AudioAdItemHolder extends RecyclerView.ViewHolder {
        //private UnifiedNativeAdView adView;

        public AudioAdItemHolder(View view) {
            super(view);
            /*this.adView = (UnifiedNativeAdView) view.findViewById(C0793R.C0795id.id_native_video_ad_view);
            UnifiedNativeAdView unifiedNativeAdView = this.adView;
            unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(C0793R.C0795id.id_native_video_app_icon_imageview));
            UnifiedNativeAdView unifiedNativeAdView2 = this.adView;
            unifiedNativeAdView2.setHeadlineView(unifiedNativeAdView2.findViewById(C0793R.C0795id.id_native_video_title_txt));
            UnifiedNativeAdView unifiedNativeAdView3 = this.adView;
            unifiedNativeAdView3.setBodyView(unifiedNativeAdView3.findViewById(C0793R.C0795id.id_native_video_subTitle_txt));
            UnifiedNativeAdView unifiedNativeAdView4 = this.adView;
            unifiedNativeAdView4.setCallToActionView(unifiedNativeAdView4.findViewById(C0793R.C0795id.id_native_video_button));
            initNativeAd();*/
        }

        private void initNativeAd() {
            Single.create(new SingleOnSubscribe<UnifiedNativeAd>() {
                public void subscribe(final SingleEmitter<UnifiedNativeAd> singleEmitter) throws Exception {
                    AdLoader build = new Builder(AudioListAdapter.this.mContext.getApplicationContext(),
                            AudioListAdapter.this.mContext.getString(C0793R.string.key_recording_native_ad)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
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
                    AudioAdItemHolder.this.addValuesAppInstallAdView(unifiedNativeAd);
                }
            });
        }

        /* access modifiers changed from: private */
        public void addValuesAppInstallAdView(UnifiedNativeAd unifiedNativeAd) {
            /*((TextView) this.adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            ((TextView) this.adView.getBodyView()).setText(unifiedNativeAd.getBody());*/
            //this.adView.getIconView().setBackgroundColor(-7829368);
            if (unifiedNativeAd.getIcon() != null) {
                Drawable drawable = unifiedNativeAd.getIcon().getDrawable();
                if (drawable != null) {
                    //this.adView.getIconView().setBackgroundColor(0);
                    //((ImageView) this.adView.getIconView()).setImageDrawable(drawable);
                }
            }
            /*((Button) this.adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            this.adView.setNativeAd(unifiedNativeAd);*/
        }
    }

    private class AudioListVH extends RecyclerView.ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public TextView fileCreatedTime;
        private ImageView fileDelete;
        /* access modifiers changed from: private */
        public TextView fileDuration;
        private ImageView fileImage;
        /* access modifiers changed from: private */
        public TextView fileName;
        private ImageView fileRename;
        private ImageView fileShare;
        /* access modifiers changed from: private */
        public TextView fileSize;

        public AudioListVH(View view) {
            super(view);
            this.fileImage = (ImageView) view.findViewById(C0793R.C0795id.id_file_image_view);
            this.fileShare = (ImageView) view.findViewById(C0793R.C0795id.id_file_share_image_view);
            this.fileDelete = (ImageView) view.findViewById(C0793R.C0795id.id_file_delete_image_view);
            this.fileRename = (ImageView) view.findViewById(C0793R.C0795id.id_file_rename_image_view);
            this.fileSize = (TextView) view.findViewById(C0793R.C0795id.id_file_size_txt);
            this.fileCreatedTime = (TextView) view.findViewById(C0793R.C0795id.id_file_created_time_txt);
            this.fileDuration = (TextView) view.findViewById(C0793R.C0795id.id_file_duration_txt);
            this.fileName = (TextView) view.findViewById(C0793R.C0795id.id_file_name_txt);
            view.setOnClickListener(this);
            this.fileShare.setOnClickListener(this);
            this.fileDelete.setOnClickListener(this);
            this.fileRename.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1 && (AudioListAdapter.this.mList.get(adapterPosition) instanceof AudioFileModel)) {
                AudioFileModel audioFileModel = (AudioFileModel) AudioListAdapter.this.mList.get(adapterPosition);
                int id = view.getId();
                if (id == C0793R.C0795id.id_file_delete_image_view) {
                    deleteFileAtIndex(adapterPosition);
                } else if (id == C0793R.C0795id.id_file_rename_image_view) {
                    renameFileAtIndex(adapterPosition);
                } else if (id == C0793R.C0795id.id_file_share_image_view) {
                    Uri parse = Uri.parse(audioFileModel.getFilePath());
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("audio/*");
                    intent.putExtra("android.intent.extra.SUBJECT", AudioListAdapter.this.mContext.getString(C0793R.string.id_share_audio_title));
                    intent.putExtra("android.intent.extra.TEXT", AudioListAdapter.this.mContext.getString(C0793R.string.id_share_audio_text));
                    intent.putExtra("android.intent.extra.STREAM", parse);
                    AudioListAdapter.this.mContext.startActivity(Intent.createChooser(intent, AudioListAdapter.this.mContext.getString(C0793R.string.id_share_audio_title)));
                } else if (AudioListAdapter.this.onListItemClickListener != null) {
                    AudioListAdapter.this.onListItemClickListener.onListItemClick(adapterPosition);
                }
            }
        }

        private void renameFileAtIndex(final int i) {
            if (AudioListAdapter.this.mList.get(i) instanceof AudioFileModel) {
                final AudioFileModel audioFileModel = (AudioFileModel) AudioListAdapter.this.mList.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(AudioListAdapter.this.mContext);
                builder.setTitle((int) C0793R.string.rename_audio);
                builder.setMessage((int) C0793R.string.rename_video_msg);
                final EditText editText = new EditText(AudioListAdapter.this.mContext);
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
                                String obj = editText.getText().toString();
                                if (obj.length() == 0) {
                                    editText.setError(AudioListAdapter.this.mContext.getString(C0793R.string.valid_name));
                                    return;
                                }
                                File file = new File(audioFileModel.getFilePath());
                                StringBuilder sb = new StringBuilder();
                                sb.append(obj);
                                sb.append(audioFileModel.getFileName().substring(audioFileModel.getFileName().lastIndexOf(".")));
                                File file2 = new File(file.getParent(), sb.toString());
                                if (file2.exists()) {
                                    editText.setError(AudioListAdapter.this.mContext.getString(C0793R.string.video_exists));
                                    return;
                                }
                                if (file.exists()) {
                                    file.renameTo(file2);
                                }
                                audioFileModel.setFilePath(file2.getAbsolutePath());
                                audioFileModel.setFileName(file2.getName());
                                audioFileModel.setFileSize(file2.length());
                                AudioListAdapter.this.mList.set(i, audioFileModel);
                                AudioListAdapter.this.notifyItemChanged(i);
                                dialogInterface.dismiss();
                            }
                        });
                    }
                });
                create.getWindow().setSoftInputMode(4);
                create.show();
            }
        }

        private void deleteFileAtIndex(final int i) {
            if (AudioListAdapter.this.mList.get(i) instanceof AudioFileModel) {
                final AudioFileModel audioFileModel = (AudioFileModel) AudioListAdapter.this.mList.get(i);
                if (AudioListAdapter.this.mContext instanceof GalleryActivity) {
                    DeleteConfirmationDialog instance = DeleteConfirmationDialog.getInstance(DeleteConfirmationDialog.MESSAGE_TYPE_AUDIO_DELETE_CONFIRMATION);
                    instance.setDialogResultListener(new OnConfirmationResult() {
                        public void onOptionResult(final DialogFragment dialogFragment, boolean z) {
                            if (!z) {
                                dialogFragment.dismiss();
                            } else if (AudioListAdapter.this.mList.size() > 0 && i < AudioListAdapter.this.mList.size() && i >= 0) {
                                Single.create(new SingleOnSubscribe<Boolean>() {
                                    public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                                        File file = new File(audioFileModel.getFilePath());
                                        AudioListAdapter.this.mContext.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{audioFileModel.getFilePath()});
                                        singleEmitter.onSuccess(Boolean.valueOf(file.delete()));
                                    }
                                }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                                    public void onSuccess(Boolean bool) {
                                        if (bool.booleanValue() && AudioListAdapter.this.mList.size() > 0 && i < AudioListAdapter.this.mList.size() && i >= 0) {
                                            AudioListAdapter.this.mList.remove(i);
                                            AudioListAdapter.this.notifyItemRemoved(i);
                                            if (AudioListAdapter.this.mList.size() == 1 && (AudioListAdapter.this.mList.get(0) instanceof NativeAdTempModel)) {
                                                AudioListAdapter.this.mList.remove(0);
                                                AudioListAdapter.this.notifyItemRemoved(0);
                                            }
                                        }
                                        if (bool.booleanValue() && AudioListAdapter.this.onItemDeleteCallback != null) {
                                            AudioListAdapter.this.onItemDeleteCallback.onItemDeletedAt(i);
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
                    instance.show(((GalleryActivity) AudioListAdapter.this.mContext).getSupportFragmentManager(), "audio_delete_confirmation");
                }
            }
        }
    }

    public interface OnItemDeleteCallback {
        void onItemDeletedAt(int i);
    }

    public boolean isListEmpty() {
        List<Object> list = this.mList;
        if (list == null || list.size() == 0) {
            return true;
        }
        if (this.mList.size() == 1) {
            return this.mList.get(0) instanceof NativeAdTempModel;
        }
        return false;
    }

    public void sortList() {
        Collections.sort(this.mList, this.lastModifySort);
        notifyDataSetChanged();
    }

    public AudioListAdapter(Context context, OnListItemClickListener onListItemClickListener2) {
        this.onListItemClickListener = onListItemClickListener2;
        this.mContext = context;
    }

    public void addItem(Object obj) {
        this.mList.add(obj);
        notifyItemInserted(this.mList.size() - 1);
    }

    public List<Object> getList() throws NullPointerException {
        List<Object> list = this.mList;
        if (list != null && list.size() != 0) {
            return this.mList;
        }
        throw new NullPointerException("List is null or empty");
    }

    public void removeAll() {
        List<Object> list = this.mList;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void addItemAtPosition(int i, Object obj) {
        if (!(obj instanceof NativeAdTempModel) || this.mList.size() <= i || !(this.mList.get(i) instanceof NativeAdTempModel)) {
            this.mList.add(i, obj);
            notifyItemInserted(i);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1632) {
            return new AudioAdItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.vid_new_ad_list_item, viewGroup, false));
        }
        return new AudioListVH(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.audio_new_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i != -1 && this.mList.get(i) != null) {
            if (getItemViewType(i) == VIEW_TYPE_AUDIO_LIST_ITEM) {
                bindAudioFile((AudioListVH) viewHolder, (AudioFileModel) this.mList.get(i));
            }
        }
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public int getItemViewType(int i) {
        return this.mList.get(i) instanceof NativeAdTempModel ? VIEW_TYPE_AD_LIST_ITEM : VIEW_TYPE_AUDIO_LIST_ITEM;
    }

    private void bindAudioFile(AudioListVH audioListVH, AudioFileModel audioFileModel) {
        audioListVH.fileName.setText(audioFileModel.getFileName());
        TextView access$600 = audioListVH.fileSize;
        StringBuilder sb = new StringBuilder();
        sb.append(((double) Math.round((float) (((audioFileModel.getFileSize() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
        sb.append("M");
        access$600.setText(sb.toString());
        try {
            audioListVH.fileCreatedTime.setText(DateUtils.getRelativeTimeSpanString(audioFileModel.getFileCreated(), System.currentTimeMillis(), 1000).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        long fileDuration = audioFileModel.getFileDuration();
        if (TimeUnit.MILLISECONDS.toHours(fileDuration) == 0) {
            audioListVH.fileDuration.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(fileDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fileDuration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(fileDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(fileDuration)))}));
            return;
        }
        audioListVH.fileDuration.setText(String.format(Locale.US, "%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(fileDuration)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(fileDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fileDuration))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(fileDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(fileDuration)))}));
    }

    public void refreshItemAtIndex(int i, boolean z) {
        List<Object> list = this.mList;
        if (list != null && list.size() > i && i >= 0 && (this.mList.get(i) instanceof AudioFileModel)) {
            ((AudioFileModel) this.mList.get(i)).setPlaying(z);
            notifyItemChanged(i);
        }
    }

    public void setOnItemDeleteCallback(OnItemDeleteCallback onItemDeleteCallback2) {
        this.onItemDeleteCallback = onItemDeleteCallback2;
    }
}
