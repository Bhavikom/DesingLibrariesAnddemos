package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
/*import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;*/
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.C0793R;

import com.yasoka.eazyscreenrecord.fragments.ImageGalleryEditFragment;
import com.yasoka.eazyscreenrecord.fragments.VideoGalleryEditFragment;
import com.yasoka.eazyscreenrecord.model.GalleryEditDataModel;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.fragments.ImageGalleryEditFragment;
import com.ezscreenrecorder.fragments.VideoGalleryEditFragment;
import com.ezscreenrecorder.model.GalleryEditDataModel;*/

public class EditMainActivity extends AppCompatActivity {
    public static final String EXTRA_IS_VIDEO_EDIT = "extra_is_vid_edit";
    private int editType = -1;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() != null) {
            Intent intent = getIntent();
            String str = EXTRA_IS_VIDEO_EDIT;
            if (intent.hasExtra(str)) {
                this.editType = getIntent().getIntExtra(str, -1);
            }
        }
        setContentView((int) R.layout.activity_main_edit);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String str2 = "video_edit_picker";
        switch (this.editType) {
            case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO /*6501*/:
                getSupportFragmentManager().beginTransaction().replace(R.id.id_edit_fragment, new VideoGalleryEditFragment(), str2).commit();
                getSupportActionBar().setTitle((int) R.string.id_select_vid_txt);
                return;
            case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_IMAGE /*6502*/:
                getSupportFragmentManager().beginTransaction().replace(R.id.id_edit_fragment, new ImageGalleryEditFragment(), "image_edit_picker").commit();
                getSupportActionBar().setTitle((int) R.string.id_select_img_txt);
                return;
            case GalleryEditDataModel.GALLERY_EDIT_TYPE_EDIT_VIDEO_TO_GIF /*6503*/:
                getSupportFragmentManager().beginTransaction().replace(R.id.id_edit_fragment, new VideoGalleryEditFragment(), str2).commit();
                getSupportActionBar().setTitle((int) R.string.id_select_vid_txt);
                return;
            default:
                if (!isFinishing()) {
                    Toast.makeText(this, "Some Error occurred!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                return;
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public int getEditType() {
        return this.editType;
    }
}
