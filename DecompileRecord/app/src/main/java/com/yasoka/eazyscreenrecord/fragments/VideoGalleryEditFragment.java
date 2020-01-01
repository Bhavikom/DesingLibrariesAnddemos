package com.yasoka.eazyscreenrecord.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore.Video.Media;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.adapter.VideoEditAdapter;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;

public class VideoGalleryEditFragment extends Fragment {
    /* access modifiers changed from: private */
    public Cursor cursor;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public VideoEditAdapter videoEditAdapter;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_main_edit, viewGroup, false);
    }

    @SuppressLint({"CheckResult"})
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.recycler_view);
        this.videoEditAdapter = new VideoEditAdapter(getContext());
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, 1, false));
        this.recyclerView.setAdapter(this.videoEditAdapter);
        Flowable.create(new FlowableOnSubscribe<ImageVideoFile>() {
            public void subscribe(FlowableEmitter<ImageVideoFile> flowableEmitter) throws Exception {
                String[] strArr = {"_id", "_display_name", "_data", NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, "date_modified", "mime_type", "_size"};
                VideoGalleryEditFragment videoGalleryEditFragment = VideoGalleryEditFragment.this;
                videoGalleryEditFragment.cursor = videoGalleryEditFragment.getActivity().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, strArr, null, null, "date_modified DESC");
                if (VideoGalleryEditFragment.this.cursor == null) {
                    flowableEmitter.onError(new NullPointerException("Cursor is null."));
                    return;
                }
                try {
                    if (VideoGalleryEditFragment.this.cursor.getCount() > 0) {
                        int columnIndexOrThrow = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_id");
                        int columnIndexOrThrow2 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_display_name");
                        int columnIndexOrThrow3 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_data");
                        int columnIndexOrThrow4 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION);
                        int columnIndexOrThrow5 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("date_modified");
                        int columnIndexOrThrow6 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("mime_type");
                        int columnIndexOrThrow7 = VideoGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_size");
                        VideoGalleryEditFragment.this.cursor.moveToFirst();
                        while (!VideoGalleryEditFragment.this.cursor.isAfterLast()) {
                            String string = VideoGalleryEditFragment.this.cursor.getString(columnIndexOrThrow6);
                            if (!TextUtils.isEmpty(string) && string.equals("video/mp4")) {
                                ImageVideoFile imageVideoFile = new ImageVideoFile();
                                imageVideoFile.setId(VideoGalleryEditFragment.this.cursor.getInt(columnIndexOrThrow));
                                imageVideoFile.setName(VideoGalleryEditFragment.this.cursor.getString(columnIndexOrThrow2));
                                imageVideoFile.setFileSize(VideoGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow7));
                                imageVideoFile.setDuration(VideoGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow4));
                                imageVideoFile.setCreated(VideoGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow5));
                                imageVideoFile.setVideo(true);
                                imageVideoFile.setPath(VideoGalleryEditFragment.this.cursor.getString(columnIndexOrThrow3));
                                if (VideoGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow4) == 0) {
                                    try {
                                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                        mediaMetadataRetriever.setDataSource(VideoGalleryEditFragment.this.cursor.getString(columnIndexOrThrow3));
                                        imageVideoFile.setDuration((long) Integer.parseInt(mediaMetadataRetriever.extractMetadata(9)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        imageVideoFile.setDuration(0);
                                    }
                                }
                                flowableEmitter.onNext(imageVideoFile);
                            }
                            VideoGalleryEditFragment.this.cursor.moveToNext();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    flowableEmitter.onError(e2);
                }
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<ImageVideoFile>() {
            public void onComplete() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(ImageVideoFile imageVideoFile) {
                VideoGalleryEditFragment.this.videoEditAdapter.addItem(imageVideoFile);
            }
        });
    }
}
