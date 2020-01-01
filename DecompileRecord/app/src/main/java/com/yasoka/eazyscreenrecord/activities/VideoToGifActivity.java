package com.yasoka.eazyscreenrecord.activities;

import android.os.Bundle;
import android.view.MenuItem;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;

public class VideoToGifActivity extends BaseToolbarActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video_to_gif);
        setupToolbar();
        initView();
    }

    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle((CharSequence) "Video To Gif");
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
