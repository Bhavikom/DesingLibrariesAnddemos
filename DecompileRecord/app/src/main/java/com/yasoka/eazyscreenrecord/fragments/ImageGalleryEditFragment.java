package com.yasoka.eazyscreenrecord.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;*/
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
/*import com.ezscreenrecorder.adapter.ImageEditAdapter;
import com.ezscreenrecorder.model.ImageVideoFile;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.adapter.ImageEditAdapter;
import com.yasoka.eazyscreenrecord.model.ImageVideoFile;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/*import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class ImageGalleryEditFragment extends Fragment {
    /* access modifiers changed from: private */
    public Cursor cursor;
    /* access modifiers changed from: private */
    public ImageEditAdapter imageEditAdapter;
    private RecyclerView recyclerView;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_main_edit, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.recycler_view);
        this.imageEditAdapter = new ImageEditAdapter(getContext());
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, 1, false));
        this.recyclerView.setAdapter(this.imageEditAdapter);
        Flowable.create(new FlowableOnSubscribe<ImageVideoFile>() {
            public void subscribe(FlowableEmitter<ImageVideoFile> flowableEmitter) throws Exception {
                String[] strArr = {"_id", "_display_name", "_data", "date_modified", "mime_type", "_size"};
                ImageGalleryEditFragment imageGalleryEditFragment = ImageGalleryEditFragment.this;
                imageGalleryEditFragment.cursor = imageGalleryEditFragment.getActivity().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, strArr, null, null, "date_modified DESC");
                if (ImageGalleryEditFragment.this.cursor == null) {
                    flowableEmitter.onError(new NullPointerException("Cursor is null."));
                    return;
                }
                try {
                    if (ImageGalleryEditFragment.this.cursor.getCount() > 0) {
                        int columnIndexOrThrow = ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_id");
                        int columnIndexOrThrow2 = ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_display_name");
                        int columnIndexOrThrow3 = ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_data");
                        int columnIndexOrThrow4 = ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("date_modified");
                        ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("mime_type");
                        int columnIndexOrThrow5 = ImageGalleryEditFragment.this.cursor.getColumnIndexOrThrow("_size");
                        ImageGalleryEditFragment.this.cursor.moveToFirst();
                        while (!ImageGalleryEditFragment.this.cursor.isAfterLast()) {
                            ImageVideoFile imageVideoFile = new ImageVideoFile();
                            imageVideoFile.setId(ImageGalleryEditFragment.this.cursor.getInt(columnIndexOrThrow));
                            imageVideoFile.setName(ImageGalleryEditFragment.this.cursor.getString(columnIndexOrThrow2));
                            imageVideoFile.setFileSize(ImageGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow5));
                            imageVideoFile.setCreated(ImageGalleryEditFragment.this.cursor.getLong(columnIndexOrThrow4));
                            imageVideoFile.setVideo(false);
                            imageVideoFile.setPath(ImageGalleryEditFragment.this.cursor.getString(columnIndexOrThrow3));
                            flowableEmitter.onNext(imageVideoFile);
                            ImageGalleryEditFragment.this.cursor.moveToNext();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    flowableEmitter.onError(e);
                }
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<ImageVideoFile>() {
            public void onComplete() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(ImageVideoFile imageVideoFile) {
                ImageGalleryEditFragment.this.imageEditAdapter.addItem(imageVideoFile);
            }
        });
    }
}
