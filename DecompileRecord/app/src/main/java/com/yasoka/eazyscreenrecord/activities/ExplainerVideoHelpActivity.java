package com.yasoka.eazyscreenrecord.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.utils.taptargetview.TapTarget;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2.Listener;

public class ExplainerVideoHelpActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public TapTargetSequence2 sequence;

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        hideStatusBarAndNavigationBar();
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_explainer_video_help);
        this.sequence = new TapTargetSequence2(this).targets(TapTarget.forView(findViewById(C0793R.C0795id.id_explainer_color_bar_resizer_button), getString(C0793R.string.id_explainer_help_resize_title), getString(C0793R.string.id_explainer_help_resize_desc)).targetCircleColor(C0793R.color.colorPrimary).outerCircleColor(C0793R.color.colorPrimaryDark).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.id_explainer_color_bar_brush_button), getString(C0793R.string.id_explainer_help_brush_title), getString(C0793R.string.id_explainer_help_brush_desc)).targetCircleColor(C0793R.color.colorPrimary).outerCircleColor(C0793R.color.colorPrimaryDark).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.id_explainer_color_bar_eraser_button), getString(C0793R.string.id_explainer_help_eraser_title), getString(C0793R.string.id_explainer_help_eraser_desc)).targetCircleColor(C0793R.color.colorPrimary).outerCircleColor(C0793R.color.colorPrimaryDark).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.id_explainer_color_bar_background_button), getString(C0793R.string.id_explainer_help_background_title), getString(C0793R.string.id_explainer_help_background_desc)).targetCircleColor(C0793R.color.colorPrimary).outerCircleColor(C0793R.color.colorPrimaryDark).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(findViewById(C0793R.C0795id.id_explainer_color_bar_clear_button), getString(C0793R.string.id_explainer_help_clear_title), getString(C0793R.string.id_explainer_help_clear_desc)).targetCircleColor(C0793R.color.colorPrimary).outerCircleColor(C0793R.color.colorPrimaryDark).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false));
        this.sequence.considerOuterCircleCanceled(true);
        this.sequence.listener(new Listener() {
            public void onSequenceCanceled(TapTarget tapTarget) {
            }

            public void onSequenceFinish() {
                ExplainerVideoHelpActivity.this.setResult(-1);
                ExplainerVideoHelpActivity.this.finish();
            }

            public void onSequenceStep(TapTarget tapTarget) {
                ExplainerVideoHelpActivity.this.sequence.showNext();
            }

            public void onOuterCircleClick() {
                ExplainerVideoHelpActivity.this.sequence.showNext();
            }
        });
        findViewById(C0793R.C0795id.id_explainer_color_bar_transparent_color).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExplainerVideoHelpActivity.this.sequence.showNext();
            }
        });
        this.sequence.start();
    }

    private void hideStatusBarAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(5894);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int i) {
                if ((i & 4) == 0) {
                    decorView.setSystemUiVisibility(5894);
                }
            }
        });
    }
}
