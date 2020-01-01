package com.yasoka.eazyscreenrecord.activities;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.utils.TouchImageView;

public class ShowImageActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public TouchImageView mTouchImageView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_show_image);
        if (getIntent() == null) {
            Toast.makeText(this, C0793R.string.id_error_view_image_message, 1).show();
            finish();
        }
        if (VERSION.SDK_INT >= 19) {
            getWindow().setFlags(512, 512);
        }
        this.mTouchImageView = (TouchImageView) findViewById(C0793R.C0795id.img_touch_image);
        Glide.with((FragmentActivity) this).load(getIntent().getStringExtra("ImgPath")).asBitmap().into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                ShowImageActivity.this.mTouchImageView.setImageBitmap(bitmap);
            }
        });
        this.mTouchImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShowImageActivity.this.mTouchImageView.setSystemUiVisibility(6);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mTouchImageView.callOnClick();
    }
}
