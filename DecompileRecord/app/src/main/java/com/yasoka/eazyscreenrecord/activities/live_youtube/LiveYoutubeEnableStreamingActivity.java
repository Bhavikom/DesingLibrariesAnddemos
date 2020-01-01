package com.yasoka.eazyscreenrecord.activities.live_youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.BaseToolbarActivity;

public class LiveYoutubeEnableStreamingActivity extends BaseToolbarActivity implements OnClickListener {
    private AppCompatButton activateButton;
    private ImageView imageGif;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_youtube_enable_stream);
        setupToolbar();
        initView();
    }

    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((int) C0793R.string.id_activate_live_stream_txt);
        }
        this.imageGif = (ImageView) findViewById(C0793R.C0795id.id_live_youtube_enable_stream_gif_image);
        this.activateButton = (AppCompatButton) findViewById(C0793R.C0795id.id_live_youtube_enable_stream_start_button);
        Glide.with(getApplicationContext()).load(Integer.valueOf(C0793R.C0794drawable.id_live_youtube_enable_stream)).asGif().placeholder((int) C0793R.C0794drawable.id_live_youtube_enable_stream).dontAnimate().into(this.imageGif);
        this.activateButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == C0793R.C0795id.id_live_youtube_enable_stream_start_button) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.youtube.com/live_dashboard")));
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (isFinishing()) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
