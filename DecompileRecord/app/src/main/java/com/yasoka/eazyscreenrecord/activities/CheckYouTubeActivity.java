package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;

public class CheckYouTubeActivity extends AppCompatActivity {
    private String desc;
    private long duration;
    private Uri fileUri;
    private String name;

    /* renamed from: s */
    private String f94s;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_check_you_tube);
        if (getIntent() != null) {
            this.fileUri = getIntent().getData();
            this.f94s = getIntent().getStringExtra(UploadService.ACCOUNT_KEY);
            this.duration = getIntent().getLongExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, 0);
            this.name = getIntent().getStringExtra("name");
            this.desc = getIntent().getStringExtra("desc");
            startActivityForResult((Intent) getIntent().getParcelableExtra("openFile"), 45);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 45 && i2 == -1) {
            Intent intent2 = new Intent(this, MyWebViewActivity.class);
            intent2.setData(this.fileUri);
            intent2.putExtra(UploadService.ACCOUNT_KEY, this.f94s);
            intent2.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, this.duration);
            intent2.putExtra("name", this.name);
            intent2.putExtra("desc", this.desc);
            startActivity(intent2);
            finish();
        }
    }
}
