package com.yasoka.eazyscreenrecord.activities.live_twitch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.utils.PreferenceHelper;

public class LiveTwitchLoginActivity extends BaseToolbarActivity {
    private String URL = "";
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private WebView webView;

    private class TwitchWebViewChromeClient extends WebChromeClient {
        private TwitchWebViewChromeClient() {
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            LiveTwitchLoginActivity.this.progressBar.setProgress(i);
            if (i == 100) {
                LiveTwitchLoginActivity.this.progressBar.setVisibility(4);
            } else {
                LiveTwitchLoginActivity.this.progressBar.setVisibility(0);
            }
        }
    }

    private class TwitchWebViewClient extends WebViewClient {
        private TwitchWebViewClient() {
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (str.startsWith("http://appsmartz.com/screen-recorder/")) {
                String substring = str.substring(str.indexOf("/#") + 2);
                String substring2 = substring.substring(0, substring.indexOf("&"));
                PreferenceHelper.getInstance().setTwitchAccessToken(substring2.substring(substring2.indexOf("=") + 1, substring2.length()));
                LiveTwitchLoginActivity.this.startActivity(new Intent(LiveTwitchLoginActivity.this.getApplicationContext(), LiveTwitchStartActivity.class));
                LiveTwitchLoginActivity.this.finish();
            }
            super.onPageStarted(webView, str, bitmap);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_twitch_login);
        setupToolbar();
        initView();
    }

    private void initView() {
        this.progressBar = (ProgressBar) findViewById(C0793R.C0795id.id_live_twitch_login_progress);
        this.webView = (WebView) findViewById(C0793R.C0795id.id_live_twitch_login_webView);
        if (!TextUtils.isEmpty(PreferenceHelper.getInstance().getTwitchAccessToken().trim())) {
            this.URL = getString(C0793R.string.key_twitch_login_url);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(C0793R.string.key_twitch_login_url));
            sb.append("&force_verify=true");
            this.URL = sb.toString();
        }
        loadWebView();
    }

    private void loadWebView() {
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new TwitchWebViewClient());
        this.webView.setWebChromeClient(new TwitchWebViewChromeClient());
        this.webView.loadUrl(this.URL);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
