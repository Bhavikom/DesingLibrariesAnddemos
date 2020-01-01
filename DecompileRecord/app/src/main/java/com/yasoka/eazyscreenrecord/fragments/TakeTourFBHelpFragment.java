package com.yasoka.eazyscreenrecord.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatImageView;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.FullscreenActivity;
import com.ezscreenrecorder.activities.TakeTourLaunchActivity;
import com.ezscreenrecorder.utils.taptargetview.TapTarget;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2;
import com.ezscreenrecorder.utils.taptargetview.TapTargetSequence2.Listener;
import com.facebook.internal.AnalyticsEvents;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.activities.FullscreenActivity;
import com.yasoka.eazyscreenrecord.activities.TakeTourLaunchActivity;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTarget;
import com.yasoka.eazyscreenrecord.utils.taptargetview.TapTargetSequence2;

import java.util.Objects;

public class TakeTourFBHelpFragment extends Fragment implements OnClickListener {
    private static final int TIME_ANIMATION_IN_MILLI = 800;
    /* access modifiers changed from: private */
    public AppCompatButton positiveButton;
    /* access modifiers changed from: private */
    public TapTargetSequence2 sequence;
    private AppCompatImageView galleryImage;
    private AppCompatImageView liveImage;
    private AppCompatImageView mainAppImage;
    private AppCompatImageView moreImage;
    private AppCompatButton negativeButton;
    private AppCompatImageView recorderImage;
    private AppCompatImageView screenshotImage;
    private AppCompatImageView whiteBoardImage;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_take_tour_fb_help, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        init(view);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void init(View view) {
        this.positiveButton = (AppCompatButton) view.findViewById(C0793R.C0795id.id_take_tour_positive_button);
        this.negativeButton = (AppCompatButton) view.findViewById(C0793R.C0795id.id_take_tour_negative_button);
        if (checkOverlayPermission()) {
            this.positiveButton.setText(C0793R.string.id_get_started_text);
        } else {
            this.positiveButton.setText(C0793R.string.id_next_txt);
        }
        this.negativeButton.setVisibility(View.GONE);
        this.positiveButton.setOnClickListener(this);
        this.recorderImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_record_video);
        this.screenshotImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_take_screenshot);
        this.galleryImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_gallery);
        this.moreImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_more_option);
        this.whiteBoardImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_white_board);
        this.liveImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_go_live);
        this.mainAppImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_main_app_image);
        this.mainAppImage.setVisibility(View.INVISIBLE);
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    TakeTourFBHelpFragment.this.setupHelp();
                }
            }, 200);
            return;
        }
        TapTargetSequence2 tapTargetSequence2 = this.sequence;
        if (tapTargetSequence2 != null) {
            tapTargetSequence2.stop();
        }
        AppCompatButton appCompatButton = this.positiveButton;
        if (appCompatButton != null) {
            appCompatButton.setVisibility(View.INVISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void setupHelp() {
        this.positiveButton.setVisibility(View.INVISIBLE);
        this.sequence = new TapTargetSequence2(getActivity()).targets(TapTarget.forView(this.recorderImage, getString(C0793R.string.txt_video_title), getString(C0793R.string.txt_video_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(this.screenshotImage, getString(C0793R.string.txt_img_title), getString(C0793R.string.txt_img_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(this.galleryImage, getString(C0793R.string.gallery), getString(C0793R.string.txt_gallery_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(this.moreImage, getString(C0793R.string.id_more_text), getString(C0793R.string.id_floating_more_dec_text)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(this.whiteBoardImage, getString(C0793R.string.id_white_board_txt), getString(C0793R.string.txt_create_tutorial_desc)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false), TapTarget.forView(this.liveImage, getString(C0793R.string.id_go_live_txt), getString(C0793R.string.id_floating_go_live_desc_txt)).targetCircleColor(C0793R.color.colorPrimaryOpacity).drawShadow(true).textColor(C0793R.color.colorWhite).tintTarget(false).cancelable(false)).listener(new TapTargetSequence2.Listener() {
            public void onSequenceFinish() {
                TakeTourFBHelpFragment.this.positiveButton.setVisibility(View.VISIBLE);
            }

            public void onSequenceStep(TapTarget tapTarget) {
                TakeTourFBHelpFragment.this.sequence.showNext();
            }

            public void onSequenceCanceled(TapTarget tapTarget) {
                //Log.e("sd", AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_CANCELLED);
            }

            public void onOuterCircleClick() {
                Log.e("sd", "Outer Clocick");
                TakeTourFBHelpFragment.this.sequence.showNext();
            }
        });
        this.sequence.considerOuterCircleCanceled(true);
        this.sequence.start();
    }

    public void onClick(View view) {
        if (isAdded()) {
            TakeTourLaunchActivity takeTourLaunchActivity = (TakeTourLaunchActivity) getActivity();
            if (view.getId() == C0793R.C0795id.id_take_tour_positive_button) {
                if (checkOverlayPermission()) {
                    startFullScreenActivity();
                    ActivityCompat.finishAfterTransition((Activity) Objects.requireNonNull(getActivity()));
                } else if (takeTourLaunchActivity != null && takeTourLaunchActivity.isNextPossible()) {
                    takeTourLaunchActivity.goToNextPage();
                }
            }
        }
    }

    private void startFullScreenActivity() {
        Intent intent = new Intent(getActivity(), FullscreenActivity.class);
        intent.putExtra(FullscreenActivity.KEY_SHOW_HELP, FullscreenActivity.EXTRA_HELP_TYPE_SHOW_FLOATING_EXIT);
        startActivity(intent);
    }

    private boolean checkOverlayPermission() {
        if (VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(getContext());
        }
        return true;
    }
}
