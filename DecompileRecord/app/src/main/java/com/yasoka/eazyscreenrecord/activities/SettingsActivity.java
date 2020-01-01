package com.yasoka.eazyscreenrecord.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.Fragment;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;*/
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

//import com.yasoka.eazyscreenrecord.R;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.settings.NewSettingsFragment;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.settings.NewSettingsFragment;*/

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private void setToolbarTitle(String str) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_settings);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.txt_settings);
        RecorderApplication.getInstance().checkSDCardAvailablity();
        pushFragment(new NewSettingsFragment(), NewSettingsFragment.TAG);
    }

    public void pushFragment(Fragment fragment, String str) {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            String name = getSupportFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName();
            if (!TextUtils.isEmpty(name) && name.equals(str)) {
                return;
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.id_settings_frame_layout, fragment, str).addToBackStack(str).commit();
        setToolbarTitle(str);
    }

    public void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (getSupportFragmentManager().popBackStackImmediate()) {
                setToolbarTitle(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName());
            }
            return;
        }
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onBackPressed() {
        popFragment();
    }
}
