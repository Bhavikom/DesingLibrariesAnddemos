package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.p003v7.widget.PopupMenu;
import android.support.p003v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.server.model.Datum;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeVideoListAdapter extends Adapter<YouTubeVideoHolder> {
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public List<Datum> data = new ArrayList();
    /* access modifiers changed from: private */
    public boolean isAllVideo;

    class YouTubeVideoHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final TextView fileDuration;
        /* access modifiers changed from: private */
        public final TextView fileName;
        /* access modifiers changed from: private */
        public final TextView fileUser;
        /* access modifiers changed from: private */
        public final ImageView thumbnail;
        /* access modifiers changed from: private */
        public final TextView viewCount;

        public YouTubeVideoHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(C0793R.C0795id.img_video_thumbnail);
            this.fileName = (TextView) view.findViewById(C0793R.C0795id.txt_file_name);
            this.fileUser = (TextView) view.findViewById(C0793R.C0795id.txt_user);
            this.fileDuration = (TextView) view.findViewById(C0793R.C0795id.txt_file_duration);
            this.viewCount = (TextView) view.findViewById(C0793R.C0795id.txt_youtube_view_count);
            view.setOnClickListener(new OnClickListener(YouTubeVideoListAdapter.this) {
                public void onClick(View view) {
                    String str = "android.intent.action.VIEW";
                    int adapterPosition = YouTubeVideoHolder.this.getAdapterPosition();
                    if (adapterPosition != -1) {
                        Datum datum = (Datum) YouTubeVideoListAdapter.this.data.get(adapterPosition);
                        if (datum != null && !TextUtils.isEmpty(datum.getVideoId())) {
                            try {
                                Context access$500 = YouTubeVideoListAdapter.this.context;
                                StringBuilder sb = new StringBuilder();
                                sb.append("vnd.youtube://");
                                sb.append(datum.getVideoId());
                                access$500.startActivity(new Intent(str, Uri.parse(sb.toString())));
                            } catch (Exception unused) {
                                Context access$5002 = YouTubeVideoListAdapter.this.context;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("http://www.youtube.com/watch?v=");
                                sb2.append(datum.getVideoId());
                                access$5002.startActivity(new Intent(str, Uri.parse(sb2.toString())));
                            }
                            FirebaseEventsNewHelper.getInstance().sendRemoteVideoPlayEvent(datum.getVideoId());
                        }
                    }
                }
            });
            view.findViewById(C0793R.C0795id.img_options).setOnTouchListener(new ChangeFilterTouchListenr((ImageView) view.findViewById(C0793R.C0795id.img_options)));
            view.findViewById(C0793R.C0795id.img_options).setOnClickListener(new OnClickListener(YouTubeVideoListAdapter.this) {
                public void onClick(View view) {
                    Datum datum = (Datum) YouTubeVideoListAdapter.this.data.get(YouTubeVideoHolder.this.getAdapterPosition());
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", YouTubeVideoListAdapter.this.context.getString(C0793R.string.watch_youtube));
                    String str = "\n";
                    String str2 = "android.intent.extra.TEXT";
                    if (YouTubeVideoListAdapter.this.isAllVideo) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part11));
                        sb.append(datum.getVideoId());
                        sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_sharet_part21));
                        sb.append(str);
                        sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part3));
                        intent.putExtra(str2, sb.toString());
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.youtube_share_part1));
                        sb2.append(datum.getVideoId());
                        sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part2));
                        sb2.append(str);
                        sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part3));
                        intent.putExtra(str2, sb2.toString());
                    }
                    YouTubeVideoListAdapter.this.context.startActivity(Intent.createChooser(intent, YouTubeVideoListAdapter.this.context.getString(C0793R.string.share_video)));
                    YouTubeVideoListAdapter.this.addToFirebaseAnalyticsYouTube();
                }
            });
        }
    }

    public YouTubeVideoListAdapter(Context context2, boolean z) {
        this.context = context2;
        this.isAllVideo = z;
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void addItem(Datum datum) {
        this.data.add(datum);
        notifyItemInserted(this.data.size() - 1);
    }

    public void addAllItem(List<Datum> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public YouTubeVideoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new YouTubeVideoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.you_tube_list_item, viewGroup, false));
    }

    public void onBindViewHolder(YouTubeVideoHolder youTubeVideoHolder, int i) {
        Datum datum = (Datum) this.data.get(i);
        if (datum != null) {
            RequestManager with = Glide.with(this.context);
            StringBuilder sb = new StringBuilder();
            sb.append("https://i.ytimg.com/vi/");
            sb.append(datum.getVideoId());
            sb.append("/0.jpg");
            with.load(sb.toString()).centerCrop().into(youTubeVideoHolder.thumbnail);
            youTubeVideoHolder.fileName.setText((datum.getTitle() == null || datum.getTitle().length() <= 0) ? this.context.getString(C0793R.string.no_title) : datum.getTitle());
            String str = "";
            if (datum.getUser_name() == null || datum.getUser_name().length() <= 0) {
                youTubeVideoHolder.fileUser.setText(str);
            } else {
                TextView access$200 = youTubeVideoHolder.fileUser;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(datum.getUser_name());
                access$200.setText(sb2.toString());
            }
            try {
                long parseLong = Long.parseLong(datum.getDuration()) * 1000;
                if (TimeUnit.MILLISECONDS.toHours(parseLong) == 0) {
                    youTubeVideoHolder.fileDuration.setText(String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(parseLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(parseLong))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(parseLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(parseLong)))}));
                } else {
                    youTubeVideoHolder.fileDuration.setText(String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(parseLong)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(parseLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(parseLong))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(parseLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(parseLong)))}));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                long parseLong2 = Long.parseLong(datum.getViewCount());
                if (parseLong2 == 0) {
                    youTubeVideoHolder.viewCount.setText("No views");
                } else if (parseLong2 > 1000) {
                    int i2 = (int) (parseLong2 / 1000);
                    TextView access$400 = youTubeVideoHolder.viewCount;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(String.valueOf(i2));
                    sb3.append("K views");
                    access$400.setText(sb3.toString());
                } else {
                    TextView access$4002 = youTubeVideoHolder.viewCount;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(parseLong2);
                    sb4.append(" views");
                    access$4002.setText(sb4.toString());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                TextView access$4003 = youTubeVideoHolder.viewCount;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("(");
                sb5.append(datum.getViewCount());
                sb5.append(")");
                access$4003.setText(sb5.toString());
            }
        }
    }

    public int getItemCount() {
        return this.data.size();
    }

    public void moreOption(View view, int i) {
        if (i >= 0 && i < this.data.size()) {
            final Datum datum = (Datum) this.data.get(i);
            PopupMenu popupMenu = new PopupMenu(this.context, view);
            popupMenu.getMenuInflater().inflate(C0793R.C0797menu.popup_menu, popupMenu.getMenu());
            popupMenu.getMenu().findItem(C0793R.C0795id.action_youtube).setVisible(false);
            popupMenu.getMenu().findItem(C0793R.C0795id.action_delete).setVisible(false);
            popupMenu.getMenu().findItem(C0793R.C0795id.action_trim).setVisible(false);
            popupMenu.getMenu().findItem(C0793R.C0795id.action_rename).setVisible(false);
            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == C0793R.C0795id.action_share) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", YouTubeVideoListAdapter.this.context.getString(C0793R.string.watch_youtube));
                        String str = "\n";
                        String str2 = "android.intent.extra.TEXT";
                        if (YouTubeVideoListAdapter.this.isAllVideo) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part11));
                            sb.append(datum.getVideoId());
                            sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_sharet_part21));
                            sb.append(str);
                            sb.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part3));
                            intent.putExtra(str2, sb.toString());
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.youtube_share_part1));
                            sb2.append(datum.getVideoId());
                            sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part2));
                            sb2.append(str);
                            sb2.append(YouTubeVideoListAdapter.this.context.getString(C0793R.string.you_tube_share_part3));
                            intent.putExtra(str2, sb2.toString());
                        }
                        YouTubeVideoListAdapter.this.context.startActivity(Intent.createChooser(intent, YouTubeVideoListAdapter.this.context.getString(C0793R.string.share_video)));
                        YouTubeVideoListAdapter.this.addToFirebaseAnalyticsYouTube();
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    /* access modifiers changed from: private */
    public void addToFirebaseAnalyticsYouTube() {
        FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
    }

    public static String extractYTId(String str) {
        Matcher matcher = Pattern.compile("^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$", 2).matcher(str);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
