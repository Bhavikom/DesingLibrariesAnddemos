package com.yasoka.eazyscreenrecord.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatImageButton;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.PopupMenu;
import android.support.p003v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.database.DataSource;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideosData;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

public class RecordingsRemoteSubListAdapter extends Adapter<VideoSubViewHolder> {
    /* access modifiers changed from: private */
    public DataSource dataSource;
    /* access modifiers changed from: private */
    public AppCompatActivity mActivity;
    /* access modifiers changed from: private */
    public List<VideosData> mList;
    private SimpleDateFormat sdf;
    /* access modifiers changed from: private */
    public int viewType;

    class VideoSubViewHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public AppCompatTextView durationText;
        private AppCompatImageButton favouriteButton;
        /* access modifiers changed from: private */
        public AppCompatImageView previewImage;
        /* access modifiers changed from: private */
        public AppCompatTextView timeText;
        /* access modifiers changed from: private */
        public AppCompatTextView titleText;
        /* access modifiers changed from: private */
        public AppCompatTextView usernameText;
        /* access modifiers changed from: private */
        public LinearLayout viewsContainer;
        /* access modifiers changed from: private */
        public AppCompatTextView viewsText;

        public VideoSubViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.previewImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_video_sub_item_preview_image_view);
            this.durationText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_sub_item_time_text_view);
            this.viewsText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_sub_item_views_text_view);
            this.titleText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_sub_item_title_text_view);
            this.usernameText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_sub_item_username_text_view);
            this.favouriteButton = (AppCompatImageButton) view.findViewById(C0793R.C0795id.id_video_sub_item_more_image_button);
            this.timeText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_sub_item_upload_date_text_view);
            this.viewsContainer = (LinearLayout) view.findViewById(C0793R.C0795id.id_video_sub_item_views_container);
            this.favouriteButton.setOnClickListener(this);
        }

        public void onClick(View view) {
            String str = "android.intent.action.VIEW";
            if (RecordingsRemoteSubListAdapter.this.mActivity != null && !RecordingsRemoteSubListAdapter.this.mActivity.isFinishing()) {
                final int adapterPosition = getAdapterPosition();
                if (adapterPosition != -1) {
                    final VideosData videosData = (VideosData) RecordingsRemoteSubListAdapter.this.mList.get(adapterPosition);
                    if (videosData == null) {
                        return;
                    }
                    if (view.getId() == C0793R.C0795id.id_video_sub_item_more_image_button) {
                        PopupMenu popupMenu = new PopupMenu(RecordingsRemoteSubListAdapter.this.mActivity, view);
                        popupMenu.getMenuInflater().inflate(C0793R.C0797menu.recordings_pop_up_menu, popupMenu.getMenu());
                        if (RecordingsRemoteSubListAdapter.this.viewType == 10216) {
                            popupMenu.getMenu().removeItem(C0793R.C0795id.action_add_to_favorite);
                        } else {
                            popupMenu.getMenu().removeItem(C0793R.C0795id.action_remove_favorite);
                        }
                        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (RecordingsRemoteSubListAdapter.this.mActivity != null && !RecordingsRemoteSubListAdapter.this.mActivity.isFinishing()) {
                                    int itemId = menuItem.getItemId();
                                    if (itemId == C0793R.C0795id.action_add_to_favorite) {
                                        RecordingsRemoteSubListAdapter.this.dataSource.open();
                                        RecordingsRemoteSubListAdapter.this.dataSource.addToFavorite(videosData);
                                        RecordingsRemoteSubListAdapter.this.dataSource.close();
                                    } else if (itemId == C0793R.C0795id.action_remove_favorite) {
                                        RecordingsRemoteSubListAdapter.this.dataSource.open();
                                        boolean deleteFavorite = RecordingsRemoteSubListAdapter.this.dataSource.deleteFavorite(videosData.getVideoId());
                                        RecordingsRemoteSubListAdapter.this.dataSource.close();
                                        if (deleteFavorite) {
                                            RecordingsRemoteSubListAdapter.this.mList.remove(adapterPosition);
                                            RecordingsRemoteSubListAdapter.this.notifyItemRemoved(adapterPosition);
                                            if (RecordingsRemoteSubListAdapter.this.mList.size() == 0) {
                                                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_REMOTE_RECORDING_REFRESH));
                                            }
                                        }
                                    } else if (itemId == C0793R.C0795id.action_share) {
                                        Intent intent = new Intent("android.intent.action.SEND");
                                        intent.setType("text/plain");
                                        intent.putExtra("android.intent.extra.SUBJECT", RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.watch_youtube));
                                        String str = "\n";
                                        String str2 = "android.intent.extra.TEXT";
                                        if (RecordingsRemoteSubListAdapter.this.viewType == 10214) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.youtube_share_part1));
                                            sb.append(videosData.getVideoId());
                                            sb.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.you_tube_share_part2));
                                            sb.append(str);
                                            sb.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.you_tube_share_part3));
                                            intent.putExtra(str2, sb.toString());
                                        } else {
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.you_tube_share_part11));
                                            sb2.append(videosData.getVideoId());
                                            sb2.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.you_tube_sharet_part21));
                                            sb2.append(str);
                                            sb2.append(RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.you_tube_share_part3));
                                            intent.putExtra(str2, sb2.toString());
                                        }
                                        RecordingsRemoteSubListAdapter.this.mActivity.startActivity(Intent.createChooser(intent, RecordingsRemoteSubListAdapter.this.mActivity.getString(C0793R.string.share_video)));
                                        FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
                                    }
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    } else if (!TextUtils.isEmpty(videosData.getVideoId())) {
                        try {
                            AppCompatActivity access$700 = RecordingsRemoteSubListAdapter.this.mActivity;
                            StringBuilder sb = new StringBuilder();
                            sb.append("vnd.youtube://");
                            sb.append(videosData.getVideoId());
                            access$700.startActivity(new Intent(str, Uri.parse(sb.toString())));
                        } catch (Exception unused) {
                            AppCompatActivity access$7002 = RecordingsRemoteSubListAdapter.this.mActivity;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("http://www.youtube.com/watch?v=");
                            sb2.append(videosData.getVideoId());
                            access$7002.startActivity(new Intent(str, Uri.parse(sb2.toString())));
                        }
                        FirebaseEventsNewHelper.getInstance().sendRemoteVideoPlayEvent(videosData.getVideoId());
                    }
                }
            }
        }
    }

    RecordingsRemoteSubListAdapter(AppCompatActivity appCompatActivity, int i, List<VideosData> list) {
        this.mList = list;
        this.viewType = i;
        this.dataSource = new DataSource(RecorderApplication.getInstance().getApplicationContext());
        this.mActivity = appCompatActivity;
    }

    public RecordingsRemoteSubListAdapter(AppCompatActivity appCompatActivity, int i) {
        this.viewType = i;
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        this.mActivity = appCompatActivity;
        this.dataSource = new DataSource(RecorderApplication.getInstance().getApplicationContext());
    }

    public void addItem(VideosData videosData) {
        this.mList.add(videosData);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void clearList() {
        List<VideosData> list = this.mList;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public VideoSubViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoSubViewHolder(LayoutInflater.from(RecorderApplication.getInstance().getApplicationContext()).inflate(i != 10215 ? C0793R.layout.custom_video_horizontal_sub_item_view : C0793R.layout.custom_video_vertical_sub_item_view, viewGroup, false));
    }

    public void onBindViewHolder(VideoSubViewHolder videoSubViewHolder, int i) {
        String str = "%s";
        VideosData videosData = (VideosData) this.mList.get(i);
        if (videosData != null) {
            RequestManager with = Glide.with(RecorderApplication.getInstance().getApplicationContext());
            StringBuilder sb = new StringBuilder();
            sb.append("https://i.ytimg.com/vi/");
            sb.append(videosData.getVideoId());
            sb.append("/0.jpg");
            with.load(sb.toString()).centerCrop().into(videoSubViewHolder.previewImage);
            videoSubViewHolder.titleText.setText((videosData.getTitle() == null || videosData.getTitle().length() <= 0) ? RecorderApplication.getInstance().getString(C0793R.string.no_title) : videosData.getTitle());
            String str2 = "";
            if (videosData.getUserName() == null || videosData.getUserName().length() <= 0) {
                videoSubViewHolder.usernameText.setText(str2);
            } else {
                videoSubViewHolder.usernameText.setText(videosData.getUserName());
            }
            try {
                long parseLong = Long.parseLong(videosData.getDuration()) * 1000;
                if (TimeUnit.MILLISECONDS.toHours(parseLong) == 0) {
                    videoSubViewHolder.durationText.setText(String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(parseLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(parseLong))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(parseLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(parseLong)))}));
                } else {
                    videoSubViewHolder.durationText.setText(String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(parseLong)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(parseLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(parseLong))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(parseLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(parseLong)))}));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (this.viewType == 10216) {
                    videoSubViewHolder.viewsContainer.setVisibility(4);
                } else {
                    videoSubViewHolder.viewsContainer.setVisibility(0);
                    long parseLong2 = Long.parseLong(videosData.getViewCount());
                    if (parseLong2 == 0) {
                        videoSubViewHolder.viewsText.setText(0);
                    } else if (parseLong2 > 1000) {
                        videoSubViewHolder.viewsText.setText(String.format("%sK", new Object[]{String.valueOf((int) (parseLong2 / 1000))}));
                    } else {
                        videoSubViewHolder.viewsText.setText(String.format(str, new Object[]{String.valueOf(parseLong2)}));
                    }
                }
            } catch (Exception unused) {
                videoSubViewHolder.viewsText.setText(String.format(str, new Object[]{videosData.getViewCount()}));
            }
            try {
                videoSubViewHolder.timeText.setText(DateUtils.getRelativeTimeSpanString(formatUploadDate(videosData.getMobileDtAdded()), System.currentTimeMillis(), 1000).toString());
            } catch (Exception unused2) {
                videoSubViewHolder.timeText.setText(str2);
            }
        }
    }

    private long formatUploadDate(String str) {
        try {
            if (this.sdf == null) {
                this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            }
            return this.sdf.parse(str).getTime();
        } catch (Exception unused) {
            return 0;
        }
    }

    public int getItemViewType(int i) {
        return this.viewType;
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
