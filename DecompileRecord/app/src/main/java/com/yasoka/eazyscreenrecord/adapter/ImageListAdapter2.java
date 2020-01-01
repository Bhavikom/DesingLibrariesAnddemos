package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.ShowImagesActivity;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.server.model.ServerDatum;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.EEAConsentHelper;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.util.ArrayList;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class ImageListAdapter2 extends Adapter<ViewHolder> {
    public static final int VIEW_TYPE_AD_LIST_ITEM = 1332;
    private static final int VIEW_TYPE_IMAGE_LIST_ITEM = 1331;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public int currentPage;
    private ImageVideoListener imageVideoListener;
    /* access modifiers changed from: private */
    public ArrayList<Object> mList = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<ServerDatum> mainList = new ArrayList<>();

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
                    AdLoader build = new Builder(ImageListAdapter2.this.context.getApplicationContext(), ImageListAdapter2.this.context.getString(C0793R.string.key_screenshot_native_ad)).forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
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

    class ImageVideoListHolder extends ViewHolder implements OnClickListener {
        ImageView mFileImage;
        TextView mFileName;
        TextView mFileSize;
        TextView mFileTime;
        TextView mFileViews;

        ImageVideoListHolder(View view) {
            super(view);
            this.mFileImage = (ImageView) view.findViewById(C0793R.C0795id.img_file);
            this.mFileName = (TextView) view.findViewById(C0793R.C0795id.txt_file_name);
            this.mFileTime = (TextView) view.findViewById(C0793R.C0795id.txt_time);
            this.mFileSize = (TextView) view.findViewById(C0793R.C0795id.txt_file_size);
            this.mFileViews = (TextView) view.findViewById(C0793R.C0795id.txt_file_views);
            view.findViewById(C0793R.C0795id.img_upload_image).setVisibility(8);
            view.findViewById(C0793R.C0795id.img_rename_image).setVisibility(8);
            view.setOnClickListener(new OnClickListener(ImageListAdapter2.this) {
                public void onClick(View view) {
                    String str;
                    int adapterPosition = ImageVideoListHolder.this.getAdapterPosition();
                    if (adapterPosition != -1 && (ImageListAdapter2.this.mList.get(adapterPosition) instanceof ServerDatum)) {
                        new ArrayList();
                        ServerDatum serverDatum = (ServerDatum) ImageListAdapter2.this.mList.get(adapterPosition);
                        Intent intent = new Intent(ImageListAdapter2.this.context, ShowImagesActivity.class);
                        intent.putExtra("ImgPath", serverDatum.getImageUrl());
                        intent.putExtra("sharePath", serverDatum.getShareUrl());
                        intent.putExtra("isFromServer", true);
                        intent.putExtra("currentPosition", ImageListAdapter2.this.currentPage);
                        intent.putExtra("files", ImageListAdapter2.this.mainList);
                        ((GalleryActivity) ImageListAdapter2.this.context).startActivityForResult(intent, GalleryActivity.REQUEST_VIEW_IMAGES);
                        try {
                            str = serverDatum.getShareUrl().substring(serverDatum.getShareUrl().lastIndexOf("/") + 1);
                        } catch (Exception unused) {
                            str = "";
                        }
                        if (TextUtils.isEmpty(str)) {
                            FirebaseEventsNewHelper.getInstance().sendRemoteScreenshotViewedEvent(str);
                        }
                    }
                }
            });
            view.findViewById(C0793R.C0795id.img_share_image).setOnClickListener(this);
            setTouchForImage(C0793R.C0795id.img_share_image);
            setTouchForImage(C0793R.C0795id.img_rename_image);
            setTouchForImage(C0793R.C0795id.img_edit_image);
            view.findViewById(C0793R.C0795id.img_edit_image).setOnClickListener(this);
        }

        private void setTouchForImage(int i) {
            this.itemView.findViewById(i).setOnTouchListener(new ChangeFilterTouchListenr((ImageView) this.itemView.findViewById(i)));
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                ServerDatum serverDatum = (ServerDatum) ImageListAdapter2.this.mList.get(adapterPosition);
                int id = view.getId();
                if (id == C0793R.C0795id.img_edit_image) {
                    try {
                        Glide.with(ImageListAdapter2.this.context).load(serverDatum.getImageUrl()).preload();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ImageListAdapter2.this.context, ImageEditActivity.class);
                    intent.putExtra("image", serverDatum.getImageUrl());
                    intent.putExtra(ImageEditActivity.EXTRA_IS_FROM_SERVER, true);
                    ((GalleryActivity) ImageListAdapter2.this.context).startActivityForResult(intent, GalleryActivity.REQUEST_VIEW_IMAGES);
                } else if (id == C0793R.C0795id.img_share_image) {
                    AppUtils.addCount(ImageListAdapter2.this.context, 4);
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType("text/plain");
                    intent2.putExtra("android.intent.extra.SUBJECT", C0793R.string.share_image);
                    StringBuilder sb = new StringBuilder();
                    sb.append(ImageListAdapter2.this.context.getString(C0793R.string.server_share));
                    sb.append(" ");
                    sb.append(serverDatum.getShareUrl());
                    sb.append(" .\n");
                    sb.append(ImageListAdapter2.this.context.getString(C0793R.string.app_download));
                    intent2.putExtra("android.intent.extra.TEXT", sb.toString());
                    ImageListAdapter2.this.context.startActivity(Intent.createChooser(intent2, ImageListAdapter2.this.context.getString(C0793R.string.share_image)));
                    ImageListAdapter2.this.addToFirebaseAnalytics(false);
                }
            }
        }
    }

    public interface ImageVideoListener {
        void onDelete();

        void refresh();
    }

    public ImageListAdapter2(Context context2, ImageVideoListener imageVideoListener2, int i) {
        this.imageVideoListener = imageVideoListener2;
        this.context = context2;
        this.currentPage = i;
    }

    public void setCurrentPage(int i) {
        this.currentPage = i;
    }

    public void addItem(ServerDatum serverDatum) {
        this.mList.add(serverDatum);
        this.mainList.add(serverDatum);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void addItemAtPosition(int i, Object obj) {
        if (!(obj instanceof NativeAdTempModel) || !(this.mList.get(i) instanceof NativeAdTempModel)) {
            this.mList.add(i, obj);
            notifyDataSetChanged();
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_IMAGE_LIST_ITEM) {
            return new ImageVideoListHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.img_new_list_item2, viewGroup, false));
        }
        return new ImageAdHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.img_new_ad_list_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_IMAGE_LIST_ITEM) {
            ServerDatum serverDatum = (ServerDatum) this.mList.get(i);
            ImageVideoListHolder imageVideoListHolder = (ImageVideoListHolder) viewHolder;
            Glide.with(this.context).load(serverDatum.getImageUrl300()).centerCrop().into(imageVideoListHolder.mFileImage);
            imageVideoListHolder.mFileName.setText(serverDatum.getImageName());
            imageVideoListHolder.mFileSize.setText(serverDatum.getUser_name());
            imageVideoListHolder.mFileViews.setText(this.context.getString(C0793R.string.id_no_of_views_txt, new Object[]{serverDatum.getViews()}));
            try {
                imageVideoListHolder.mFileTime.setText(serverDatum.getAdded_date());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public int getItemViewType(int i) {
        if (this.mList.get(i) instanceof ServerDatum) {
            return VIEW_TYPE_IMAGE_LIST_ITEM;
        }
        return 1332;
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
}
