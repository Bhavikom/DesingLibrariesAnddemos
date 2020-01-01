package com.yasoka.eazyscreenrecord.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.EditMainActivity;
import com.ezscreenrecorder.activities.TrimVideoActivity;
import com.ezscreenrecorder.activities.VideoToGifActivity;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoEditAdapter extends Adapter<ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public List<ImageVideoFile> mList;

    class VideoEditVH extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public TextView endTxt;
        /* access modifiers changed from: private */
        public ImageView imageView;
        /* access modifiers changed from: private */
        public TextView startTxt;

        VideoEditVH(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(C0793R.C0795id.id_gallery_edit_row_img);
            this.startTxt = (TextView) view.findViewById(C0793R.C0795id.id_gallery_edit_row_start_txt);
            this.endTxt = (TextView) view.findViewById(C0793R.C0795id.id_gallery_edit_row_end_txt);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                ImageVideoFile imageVideoFile = (ImageVideoFile) VideoEditAdapter.this.mList.get(adapterPosition);
                view.getId();
                if (VideoEditAdapter.this.context instanceof EditMainActivity) {
                    int editType = ((EditMainActivity) VideoEditAdapter.this.context).getEditType();
                    EditMainActivity editMainActivity = (EditMainActivity) VideoEditAdapter.this.context;
                    String str = TrimVideoActivity.EXTRA_VIDEO_DURATION;
                    String str2 = TrimVideoActivity.EXTRA_VIDEO_PATH;
                    if (editType == 6501) {
                        Intent intent = new Intent(VideoEditAdapter.this.context, TrimVideoActivity.class);
                        intent.putExtra(str2, imageVideoFile.getPath());
                        intent.putExtra(str, imageVideoFile.getDuration());
                        intent.setFlags(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);
                        editMainActivity.startActivity(intent);
                        ActivityCompat.finishAfterTransition(editMainActivity);
                    } else if (editType == 6503) {
                        Intent intent2 = new Intent(VideoEditAdapter.this.context, VideoToGifActivity.class);
                        intent2.putExtra(str2, imageVideoFile.getPath());
                        intent2.putExtra(str, imageVideoFile.getDuration());
                        intent2.setFlags(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);
                        editMainActivity.startActivity(intent2);
                        ActivityCompat.finishAfterTransition(editMainActivity);
                    }
                }
            }
        }
    }

    public VideoEditAdapter(Context context2) {
        this.context = context2;
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
    }

    public void addItem(ImageVideoFile imageVideoFile) {
        this.mList.add(imageVideoFile);
        if (this.mList.size() == 0) {
            notifyItemChanged(0);
        } else {
            notifyItemChanged(this.mList.size() - 1);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoEditVH(LayoutInflater.from(this.context).inflate(C0793R.layout.custom_gallery_edit_row, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i != -1) {
            ImageVideoFile imageVideoFile = (ImageVideoFile) this.mList.get(i);
            if (imageVideoFile != null) {
                VideoEditVH videoEditVH = (VideoEditVH) viewHolder;
                videoEditVH.startTxt.setText(getVideoDuration(Long.valueOf(imageVideoFile.getDuration()).longValue()));
                videoEditVH.endTxt.setText(getVideoFileSize(imageVideoFile.getFileSize()));
                Glide.with(this.context).load(imageVideoFile.getPath()).asBitmap().videoDecoder(new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(1000000), Glide.get(this.context).getBitmapPool(), DecodeFormat.PREFER_ARGB_8888)).centerCrop().into(videoEditVH.imageView);
            }
        }
    }

    public int getItemCount() {
        return this.mList.size();
    }

    @SuppressLint({"DefaultLocale"})
    private String getVideoDuration(long j) {
        if (TimeUnit.MILLISECONDS.toHours(j) == 0) {
            return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(j))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))});
        }
        return String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(j)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(j))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))});
    }

    private String getVideoFileSize(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(((double) Math.round((float) (((j * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
        sb.append("M");
        return sb.toString();
    }
}
