package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p003v7.app.AppCompatActivity;*/
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTarget;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTargetSequence2;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.utils.taptargetview.TapTarget;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2.Listener;
import com.facebook.internal.AnalyticsEvents;*/

public class HelpActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public boolean isFromTakeTour = false;
    /* access modifiers changed from: private */
    public TapTargetSequence2 tapTargetSequence2;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_help);
        if (getIntent() != null) {
            Intent intent = getIntent();
            String str = TakeTourLaunchActivity.TAKE_TOUR_KEY;
            if (intent.hasExtra(str)) {
                this.isFromTakeTour = getIntent().getBooleanExtra(str, false);
            }
        }
        findViewById(R.id.img_video).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                System.out.println("VIDEO CLICK");
            }
        });
        tapTargetSequence2 = new TapTargetSequence2(this).targets(TapTarget.forView(findViewById(R.id.img_video),
                getString(R.string.txt_video_title), getString(R.string.txt_video_desc))
                .targetCircleColor(R.color.colorPrimaryOpacity).drawShadow(true).textColor(R.color.colorWhite)
                .tintTarget(false).cancelable(false), TapTarget.forView(findViewById(R.id.img_camera),
                getString(R.string.txt_img_title), getString(R.string.txt_img_desc))
                .targetCircleColor(R.color.colorPrimaryOpacity).drawShadow(true).textColor(R.color.colorWhite)
                .tintTarget(false).cancelable(false), TapTarget.forView(findViewById(R.id.img_gallery),
                getString(R.string.gallery), getString(R.string.txt_gallery_desc)).targetCircleColor(R.color.colorPrimaryOpacity)
                .drawShadow(true).textColor(R.color.colorWhite).tintTarget(false).cancelable(false),
                TapTarget.forView(findViewById(R.id.img_more), getString(R.string.id_more_text),
                        getString(R.string.id_floating_more_dec_text)).targetCircleColor(R.color.colorPrimaryOpacity)
                        .drawShadow(true).textColor(R.color.colorWhite).tintTarget(false).cancelable(false),
                TapTarget.forView(findViewById(R.id.img_whiteboard), getString(R.string.id_white_board_txt),
                        getString(R.string.txt_create_tutorial_desc)).targetCircleColor(R.color.colorPrimaryOpacity)
                        .drawShadow(true).textColor(R.color.colorWhite).tintTarget(false).cancelable(false),
                TapTarget.forView(findViewById(R.id.img_go_live), getString(R.string.id_go_live_txt),
                        getString(R.string.id_floating_go_live_desc_txt)).targetCircleColor(R.color.colorPrimaryOpacity)
                        .drawShadow(true).textColor(R.color.colorWhite).tintTarget(false).cancelable(false)).listener(new TapTargetSequence2.Listener() {
            public void onSequenceFinish() {
                if (HelpActivity.this.isFromTakeTour) {
                    HelpActivity helpActivity = HelpActivity.this;
                    helpActivity.startActivity(new Intent(helpActivity, DrawOverAppsPermissionActivity.class));
                    ActivityCompat.finishAfterTransition(HelpActivity.this);
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        HelpActivity.this.finish();
                    }
                }, 300);
            }

            public void onSequenceStep(TapTarget tapTarget) {
                System.out.println("PPOLLL");
                HelpActivity.this.tapTargetSequence2.showNext();
            }

            public void onSequenceCanceled(TapTarget tapTarget) {
                //Log.e("sd", AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_CANCELLED);
            }

            public void onOuterCircleClick() {
                Log.e("sd", "Outer Clocick");
                HelpActivity.this.tapTargetSequence2.showNext();
            }
        });
        this.tapTargetSequence2.considerOuterCircleCanceled(true);
        this.tapTargetSequence2.start();
    }

    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("BACK PRESS>>");
    }
}
