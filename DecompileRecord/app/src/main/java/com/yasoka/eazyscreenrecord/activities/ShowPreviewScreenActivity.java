package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.MenuItem;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.fragments.AudioPreviewFragment;
import com.ezscreenrecorder.fragments.ImagePreviewFragment;
import com.ezscreenrecorder.fragments.VideoPreviewFragment;

public class ShowPreviewScreenActivity extends AppCompatActivity {
    public static final String KEY_AUDIO_FILE_MODEL = "key_file_audio_model";
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_preview_dialog);
        this.toolbar = (Toolbar) findViewById(C0793R.C0795id.id_toolbar);
        Toolbar toolbar2 = this.toolbar;
        if (toolbar2 != null) {
            setSupportActionBar(toolbar2);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Intent intent = getIntent();
            String str = FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS;
            if (intent.hasExtra(str)) {
                switch (getIntent().getIntExtra(str, FloatingService.EXTRA_MAIN_ACTION_TYPE_NONE)) {
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT /*1340*/:
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        String str2 = ImagePreviewFragment.TAG;
                        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(str2);
                        if (findFragmentByTag == null) {
                            replaceFragment(new ImagePreviewFragment(), str2);
                            return;
                        } else if (findFragmentByTag instanceof ImagePreviewFragment) {
                            replaceFragment(findFragmentByTag, str2);
                            return;
                        } else {
                            replaceFragment(new ImagePreviewFragment(), str2);
                            return;
                        }
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO /*1341*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_INTERACTIVE_VIDEO /*1342*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_EXPLAINER_VIDEO /*1344*/:
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD /*1345*/:
                        FragmentManager supportFragmentManager2 = getSupportFragmentManager();
                        String str3 = VideoPreviewFragment.TAG;
                        Fragment findFragmentByTag2 = supportFragmentManager2.findFragmentByTag(str3);
                        if (findFragmentByTag2 == null) {
                            replaceFragment(new VideoPreviewFragment(), str3);
                            return;
                        } else if (findFragmentByTag2 instanceof VideoPreviewFragment) {
                            replaceFragment(findFragmentByTag2, str3);
                            return;
                        } else {
                            replaceFragment(new VideoPreviewFragment(), str3);
                            return;
                        }
                    case FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO /*1343*/:
                        FragmentManager supportFragmentManager3 = getSupportFragmentManager();
                        String str4 = AudioPreviewFragment.TAG;
                        Fragment findFragmentByTag3 = supportFragmentManager3.findFragmentByTag(str4);
                        if (findFragmentByTag3 == null) {
                            replaceFragment(new AudioPreviewFragment(), str4);
                            return;
                        } else if (findFragmentByTag3 instanceof AudioPreviewFragment) {
                            replaceFragment(findFragmentByTag3, str4);
                            return;
                        } else {
                            replaceFragment(new AudioPreviewFragment(), str4);
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment, String str) {
        getSupportFragmentManager().beginTransaction().replace(C0793R.C0795id.id_preview_screen, fragment, str).commit();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
