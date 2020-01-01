package com.yasoka.eazyscreenrecord.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.server.model.SocialFeedModels.SocialFeedModel;
import com.ezscreenrecorder.utils.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SocialFeedAdapter extends Adapter<SocialFeedVH> {
    /* access modifiers changed from: private */
    public List<SocialFeedModel> mList = new ArrayList();

    class SocialFeedVH extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public AppCompatImageView feedImageView;
        /* access modifiers changed from: private */
        public AppCompatTextView feedSubTitle;
        /* access modifiers changed from: private */
        public AppCompatTextView feedTime;
        /* access modifiers changed from: private */
        public AppCompatTextView feedTitle;
        /* access modifiers changed from: private */
        public AppCompatImageView socialImageView;

        public SocialFeedVH(View view) {
            super(view);
            this.socialImageView = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_social_feed_item_social_image_view);
            this.feedImageView = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_social_feed_item_image_view);
            this.feedSubTitle = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_social_feed_item_subtitle_text_view);
            this.feedTitle = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_social_feed_item_title_text_view);
            this.feedTime = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_social_feed_item_time_text_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                String url = ((SocialFeedModel) SocialFeedAdapter.this.mList.get(adapterPosition)).getUrl();
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent.addFlags(268435456);
                    RecorderApplication.getInstance().getApplicationContext().startActivity(intent);
                }
            }
        }
    }

    public void addItem(SocialFeedModel socialFeedModel) {
        this.mList.add(socialFeedModel);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void clearList() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    public SocialFeedVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SocialFeedVH(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.custom_social_feel_row_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull SocialFeedVH socialFeedVH, int i) {
        if (i != -1) {
            socialFeedVH.feedTitle.setText(C0793R.string.app_name);
            if (!TextUtils.isEmpty(((SocialFeedModel) this.mList.get(i)).getTitle())) {
                socialFeedVH.feedSubTitle.setVisibility(0);
                socialFeedVH.feedSubTitle.setText(((SocialFeedModel) this.mList.get(i)).getTitle());
            } else {
                socialFeedVH.feedSubTitle.setVisibility(8);
            }
            try {
                socialFeedVH.feedTime.setVisibility(0);
                socialFeedVH.feedTime.setText(DateUtils.getRelativeTimeSpanString(getPublishTimeInMillis(((SocialFeedModel) this.mList.get(i)).getCreatedDate()), System.currentTimeMillis(), 1000).toString());
            } catch (Exception e) {
                e.printStackTrace();
                socialFeedVH.feedTime.setVisibility(8);
            }
            if (!TextUtils.isEmpty(((SocialFeedModel) this.mList.get(i)).getImage())) {
                socialFeedVH.feedImageView.setVisibility(0);
                Glide.with(RecorderApplication.getInstance().getApplicationContext()).load(((SocialFeedModel) this.mList.get(i)).getImage()).into(socialFeedVH.feedImageView);
            } else {
                socialFeedVH.feedImageView.setVisibility(8);
            }
            String source = ((SocialFeedModel) this.mList.get(i)).getSource();
            int i2 = C0793R.C0794drawable.ic_social_feeds_img_48dp;
            if (!TextUtils.isEmpty(((SocialFeedModel) this.mList.get(i)).getSource())) {
                if (TextUtils.equals(source, "facebook")) {
                    i2 = C0793R.C0794drawable.ic_social_feed_facebook_img_48dp;
                } else if (TextUtils.equals(source, "instagram")) {
                    i2 = C0793R.C0794drawable.ic_social_feed_instagram_img_48dp;
                } else if (TextUtils.equals(source, "twitter")) {
                    i2 = C0793R.C0794drawable.ic_social_feed_twitter_img_48dp;
                }
            }
            socialFeedVH.socialImageView.setImageResource(i2);
        }
    }

    public long getPublishTimeInMillis(String str) {
        long j;
        try {
            j = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            j = -1;
        }
        Logger instance = Logger.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("TIME: ");
        sb.append(j);
        instance.error(sb.toString());
        return j;
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
