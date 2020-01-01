package com.yasoka.eazyscreenrecord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;*/
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.adapter.GalleryEditAdapter;
import com.yasoka.eazyscreenrecord.model.GalleryEditDataModel;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.adapter.GalleryEditAdapter;
import com.ezscreenrecorder.model.GalleryEditDataModel;*/

public class GalleryEditFragment extends Fragment {
    private GalleryEditAdapter mAdapter;
    private LinearLayout permissionLayout;
    private RecyclerView recyclerView;

    public static GalleryEditFragment getInstance() {
        return new GalleryEditFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_gallery_edit, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.permissionLayout = (LinearLayout) view.findViewById(R.id.id_gallery_storage_permission_layout);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        refreshList();
        this.permissionLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (GalleryEditFragment.this.isAdded()) {
                    ((GalleryActivity) GalleryEditFragment.this.getActivity()).requestStoragePermission();
                }
            }
        });
    }

    private boolean checkForStoragePermission() {
        return ((GalleryActivity) getActivity()).checkStoragePermission();
    }

    public void refreshList() {
        if (!isAdded()) {
            return;
        }
        if (checkForStoragePermission()) {
            this.permissionLayout.setVisibility(View.GONE);
            if (this.recyclerView.getLayoutManager() == null) {
                this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
            }
            if (this.mAdapter == null) {
                this.mAdapter = new GalleryEditAdapter(getActivity());
                this.mAdapter.addItem(new GalleryEditDataModel(R.id.id_edit_image, R.string.id_edit_image_txt, GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_IMAGE));
                this.mAdapter.addItem(new GalleryEditDataModel(R.id.id_edit_video, R.string.id_edit_video_txt, GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO));
            }
            this.recyclerView.setAdapter(this.mAdapter);
            return;
        }
        this.permissionLayout.setVisibility(View.VISIBLE);
    }
}
