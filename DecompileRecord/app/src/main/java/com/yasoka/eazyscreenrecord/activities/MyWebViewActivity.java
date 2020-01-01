package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;
import java.io.PrintStream;

public class MyWebViewActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String desc;
    /* access modifiers changed from: private */
    public long duration;
    /* access modifiers changed from: private */
    public Uri fileUri;
    /* access modifiers changed from: private */
    public String name;
    /* access modifiers changed from: private */

    /* renamed from: s */
    public String f95s;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_my_web_view);
        if (getIntent() != null) {
            this.fileUri = getIntent().getData();
            this.f95s = getIntent().getStringExtra(UploadService.ACCOUNT_KEY);
            this.duration = getIntent().getLongExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, 0);
            this.name = getIntent().getStringExtra("name");
            this.desc = getIntent().getStringExtra("desc");
        }
        WebView webView = (WebView) findViewById(C0793R.C0795id.web_view);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("URLL>>>");
                sb.append(str);
                printStream.println(sb.toString());
                if (str.contains("https://m.youtube.com/channel_creation_done")) {
                    Intent intent = new Intent(MyWebViewActivity.this, UploadService.class);
                    intent.setData(MyWebViewActivity.this.fileUri);
                    intent.putExtra(UploadService.ACCOUNT_KEY, MyWebViewActivity.this.f95s);
                    intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, MyWebViewActivity.this.duration);
                    intent.putExtra("name", MyWebViewActivity.this.name);
                    intent.putExtra("desc", MyWebViewActivity.this.desc);
                    MyWebViewActivity.this.startService(intent);
                    MyWebViewActivity.this.finish();
                }
            }
        });
        webView.loadUrl("https://m.youtube.com/create_channel?chromeless=1&next=/channel_creation_done");
    }
}
