package com.yasoka.eazyscreenrecord.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.p000v4.app.ActivityCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import java.io.File;
import java.io.PrintStream;
import java.util.Random;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.K4LVideoTrimmer.VideotrimListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class TrimVideoActivity extends AppCompatActivity implements OnTrimVideoListener {
    public static final String EXTRA_IS_FROM_OTHER_APP = "TrimFromOtherApp";
    public static final String EXTRA_VIDEO_DURATION = "VideoDuration";
    public static final String EXTRA_VIDEO_PATH = "TrimVideoPath";
    private File dir;
    private long duration;
    private boolean isFromOtherApp = false;
    private ProgressDialog mProgressDialog;
    private K4LVideoTrimmer mVideoTrimmer;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_trim_video);
        Intent intent = getIntent();
        if (intent != null) {
            str = intent.getStringExtra(EXTRA_VIDEO_PATH);
            this.duration = intent.getLongExtra(EXTRA_VIDEO_DURATION, 0);
            String str2 = EXTRA_IS_FROM_OTHER_APP;
            if (intent.hasExtra(str2)) {
                this.isFromOtherApp = intent.getBooleanExtra(str2, false);
            }
        } else {
            str = "";
        }
        try {
            this.dir = new File(AppUtils.getVideoDir(this));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (this.dir == null) {
            Toast.makeText(this, C0793R.string.id_unable_to_setup_vid_error_msg, 1).show();
            finish();
        }
        this.toolbar = (Toolbar) findViewById(C0793R.C0795id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle((int) C0793R.string.trim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setMessage(getString(C0793R.string.id_trimming_txt));
        this.mVideoTrimmer = (K4LVideoTrimmer) findViewById(C0793R.C0795id.timeLine);
        K4LVideoTrimmer k4LVideoTrimmer = this.mVideoTrimmer;
        if (k4LVideoTrimmer != null) {
            long j = this.duration;
            k4LVideoTrimmer.setMaxDurationMilliSeconds(j != 0 ? (int) j : 50000);
            this.mVideoTrimmer.setOnTrimVideoListener(this);
            K4LVideoTrimmer k4LVideoTrimmer2 = this.mVideoTrimmer;
            File file = this.dir;
            StringBuilder sb = new StringBuilder();
            sb.append(new Random().nextInt(2454));
            sb.append(".mp4");
            k4LVideoTrimmer2.setDestinationPath(new File(file, sb.toString()).getPath());
            this.mVideoTrimmer.setVideoURI(Uri.parse(str));
            this.mVideoTrimmer.setVideotrimListener(new VideotrimListener() {
                public void onSave() {
                    FirebaseEventsNewHelper.getInstance().sendVideoTrimSuccessEvent();
                }
            });
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            cancelAction();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onTrimStarted() {
        this.mProgressDialog.show();
    }

    public void getResult(Uri uri) {
        this.mProgressDialog.dismiss();
        if (this.isFromOtherApp) {
            startActivity(new Intent(this, GalleryActivity.class));
        } else {
            Intent intent = new Intent();
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("UR->");
            sb.append(uri.toString());
            printStream.println(sb.toString());
            intent.putExtra("imageEdit", uri.toString());
            setResult(-1, intent);
        }
        ActivityCompat.finishAfterTransition(this);
    }

    public void cancelAction() {
        this.mProgressDialog.dismiss();
        this.mVideoTrimmer.destroy();
        finish();
    }

    public void onError(final String str) {
        this.mProgressDialog.dismiss();
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(TrimVideoActivity.this, str, 0).show();
            }
        });
    }
}
