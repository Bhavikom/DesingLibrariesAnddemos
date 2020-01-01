package com.yasoka.eazyscreenrecord.activities;

import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import com.ezscreenrecorder.C0793R;

public class BaseToolbarActivity extends AppCompatActivity {
    public void setupToolbar() {
        setSupportActionBar((Toolbar) findViewById(C0793R.C0795id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((CharSequence) "");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
