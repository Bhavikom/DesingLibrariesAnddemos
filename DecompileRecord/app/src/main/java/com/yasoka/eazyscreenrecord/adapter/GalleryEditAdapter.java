package com.yasoka.eazyscreenrecord.adapter;

import android.content.Context;
import android.content.Intent;
/*import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.EditMainActivity;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.model.GalleryEditDataModel;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.activities.EditMainActivity;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.model.GalleryEditDataModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryEditAdapter extends RecyclerView.Adapter {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public List<GalleryEditDataModel> mList;

    class GalleryEditVH extends RecyclerView.ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public ImageView imageView;
        /* access modifiers changed from: private */
        public TextView textView;

        public GalleryEditVH(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(C0793R.C0795id.id_gallery_edit_row_img);
            this.textView = (TextView) view.findViewById(C0793R.C0795id.id_gallery_edit_row_txt_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1 && (GalleryEditAdapter.this.mContext instanceof GalleryActivity)) {
                GalleryEditDataModel galleryEditDataModel = (GalleryEditDataModel) GalleryEditAdapter.this.mList.get(adapterPosition);
                if (galleryEditDataModel != null) {
                    view.getId();
                    int editType = galleryEditDataModel.getEditType();
                    String str = EditMainActivity.EXTRA_IS_VIDEO_EDIT;
                    switch (editType) {
                        case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO /*6501*/:
                            Intent intent = new Intent(GalleryEditAdapter.this.mContext, EditMainActivity.class);
                            intent.putExtra(str, GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO);
                            ((GalleryActivity) GalleryEditAdapter.this.mContext).startActivityForResult(intent, GalleryActivity.TRIM_REQUEST);
                            return;
                        case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_IMAGE /*6502*/:
                            Intent intent2 = new Intent(GalleryEditAdapter.this.mContext, EditMainActivity.class);
                            intent2.putExtra(str, GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_IMAGE);
                            ((GalleryActivity) GalleryEditAdapter.this.mContext).startActivityForResult(intent2, GalleryActivity.REQUEST_VIEW_IMAGES);
                            return;
                        case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO_TO_GIF /*6503*/:
                            Intent intent3 = new Intent(GalleryEditAdapter.this.mContext, EditMainActivity.class);
                            intent3.putExtra(str, GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO_TO_GIF);
                            ((GalleryActivity) GalleryEditAdapter.this.mContext).startActivity(intent3);
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }

    public GalleryEditAdapter(Context context) {
        this.mContext = context;
    }

    public void addItem(GalleryEditDataModel galleryEditDataModel) {
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        this.mList.add(galleryEditDataModel);
        notifyItemChanged(this.mList.size() - 1);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GalleryEditVH(LayoutInflater.from(this.mContext).inflate(C0793R.layout.custom_gallery_edit_item_view, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i != -1) {
            GalleryEditDataModel galleryEditDataModel = (GalleryEditDataModel) this.mList.get(i);
            if (galleryEditDataModel != null) {
                GalleryEditVH galleryEditVH = (GalleryEditVH) viewHolder;
                galleryEditVH.textView.setText(galleryEditDataModel.getResName());
                galleryEditVH.imageView.setImageResource(galleryEditDataModel.getResImage());
            }
        }
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
