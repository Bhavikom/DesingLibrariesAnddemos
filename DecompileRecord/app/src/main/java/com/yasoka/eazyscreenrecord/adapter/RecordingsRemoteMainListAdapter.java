package com.yasoka.eazyscreenrecord.adapter;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.MoreVideosActivity;
import com.ezscreenrecorder.model.VideosRemoteDataModel;
import java.util.ArrayList;
import java.util.List;

public class RecordingsRemoteMainListAdapter extends Adapter<VideoMainViewHolder> {
    /* access modifiers changed from: private */
    public AppCompatActivity mActivity;
    /* access modifiers changed from: private */
    public List<VideosRemoteDataModel> mList;

    class VideoMainViewHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public Button moreButton;
        /* access modifiers changed from: private */
        public RecyclerView recyclerView;
        /* access modifiers changed from: private */
        public AppCompatTextView titleText;

        public VideoMainViewHolder(View view) {
            super(view);
            this.titleText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_video_main_item_title);
            this.moreButton = (Button) view.findViewById(C0793R.C0795id.id_video_main_item_more_button);
            this.recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.id_video_main_item_recycler_view);
            this.moreButton.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (RecordingsRemoteMainListAdapter.this.mActivity != null && !RecordingsRemoteMainListAdapter.this.mActivity.isFinishing()) {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != -1) {
                    VideosRemoteDataModel videosRemoteDataModel = (VideosRemoteDataModel) RecordingsRemoteMainListAdapter.this.mList.get(adapterPosition);
                    if (videosRemoteDataModel != null && videosRemoteDataModel.getTypeOfList() == 10215) {
                        RecordingsRemoteMainListAdapter.this.mActivity.startActivity(new Intent(RecorderApplication.getInstance().getApplicationContext(), MoreVideosActivity.class));
                    }
                }
            }
        }
    }

    public RecordingsRemoteMainListAdapter(AppCompatActivity appCompatActivity) {
        this.mActivity = appCompatActivity;
    }

    public void addItem(VideosRemoteDataModel videosRemoteDataModel) {
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        this.mList.add(videosRemoteDataModel);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void clearList() {
        List<VideosRemoteDataModel> list = this.mList;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public VideoMainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoMainViewHolder(LayoutInflater.from(RecorderApplication.getInstance().getApplicationContext()).inflate(C0793R.layout.custom_video_main_item_view, viewGroup, false));
    }

    public void onBindViewHolder(VideoMainViewHolder videoMainViewHolder, int i) {
        VideosRemoteDataModel videosRemoteDataModel = (VideosRemoteDataModel) this.mList.get(i);
        if (videosRemoteDataModel != null) {
            switch (videosRemoteDataModel.getTypeOfList()) {
                case VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_TOP_VIDEOS /*10213*/:
                    videoMainViewHolder.titleText.setText(C0793R.string.id_top_recordings_txt);
                    videoMainViewHolder.moreButton.setVisibility(4);
                    videoMainViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(RecorderApplication.getInstance().getApplicationContext(), 0, false));
                    videoMainViewHolder.recyclerView.setAdapter(new RecordingsRemoteSubListAdapter(this.mActivity, videosRemoteDataModel.getTypeOfList(), videosRemoteDataModel.getListData()));
                    return;
                case VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_USER_VIDEOS /*10214*/:
                    String str = "Select Account";
                    String string = PreferenceManager.getDefaultSharedPreferences(RecorderApplication.getInstance()).getString("youtube_account_email", str);
                    if (!string.equalsIgnoreCase(str)) {
                        videoMainViewHolder.titleText.setText(RecorderApplication.getInstance().getString(C0793R.string.id_my_videos_txt, new Object[]{string}));
                    }
                    videoMainViewHolder.moreButton.setVisibility(4);
                    videoMainViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(RecorderApplication.getInstance().getApplicationContext(), 0, false));
                    videoMainViewHolder.recyclerView.setAdapter(new RecordingsRemoteSubListAdapter(this.mActivity, videosRemoteDataModel.getTypeOfList(), videosRemoteDataModel.getListData()));
                    return;
                case VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_OTHER_VIDEOS /*10215*/:
                    videoMainViewHolder.titleText.setText(C0793R.string.id_all_recordings_txt);
                    videoMainViewHolder.moreButton.setVisibility(0);
                    videoMainViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(RecorderApplication.getInstance().getApplicationContext(), 1, false));
                    videoMainViewHolder.recyclerView.setAdapter(new RecordingsRemoteSubListAdapter(this.mActivity, videosRemoteDataModel.getTypeOfList(), videosRemoteDataModel.getListData()));
                    return;
                case VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_FAVORITE_VIDEOS /*10216*/:
                    videoMainViewHolder.titleText.setText(C0793R.string.id_favorites_txt);
                    videoMainViewHolder.moreButton.setVisibility(4);
                    videoMainViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(RecorderApplication.getInstance().getApplicationContext(), 0, false));
                    videoMainViewHolder.recyclerView.setAdapter(new RecordingsRemoteSubListAdapter(this.mActivity, videosRemoteDataModel.getTypeOfList(), videosRemoteDataModel.getListData()));
                    return;
                case VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_EDITOR_CHOICE_VIDEOS /*10217*/:
                    videoMainViewHolder.titleText.setText(C0793R.string.id_editor_choice_recordings_txt);
                    videoMainViewHolder.moreButton.setVisibility(4);
                    videoMainViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(RecorderApplication.getInstance().getApplicationContext(), 0, false));
                    videoMainViewHolder.recyclerView.setAdapter(new RecordingsRemoteSubListAdapter(this.mActivity, videosRemoteDataModel.getTypeOfList(), videosRemoteDataModel.getListData()));
                    return;
                default:
                    return;
            }
        }
    }

    public int getItemViewType(int i) {
        return ((VideosRemoteDataModel) this.mList.get(i)).getTypeOfList();
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
