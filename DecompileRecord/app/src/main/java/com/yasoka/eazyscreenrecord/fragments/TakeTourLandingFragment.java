package com.yasoka.eazyscreenrecord.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;*/
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.activities.TakeTourLaunchActivity;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.TakeTourLaunchActivity;*/

public class TakeTourLandingFragment extends Fragment implements OnClickListener {
    private static final int TIME_ANIMATION_IN_MILLI = 800;
    private AppCompatImageView galleryImage;
    private AppCompatImageView gameImage;
    private AppCompatImageView liveImage;
    private AppCompatImageView mainAppImage;
    private AppCompatImageView moreImage;
    private AppCompatButton negativeButton;
    private AppCompatButton positiveButton;
    private AppCompatImageView recorderImage;
    private AppCompatImageView screenshotImage;
    private AppCompatTextView titleText;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_take_tour_landing, viewGroup, false);
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
        this.negativeButton.setVisibility(View.GONE);
        this.positiveButton.setOnClickListener(this);
        this.titleText = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_take_tour_title_text);
        this.titleText.setText(C0793R.string.app_name);
        this.recorderImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_record_video);
        this.screenshotImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_take_screenshot);
        this.galleryImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_gallery);
        this.moreImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_more_option);
        this.gameImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_record_game);
        this.liveImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_floating_go_live);
        this.mainAppImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_take_tour_main_app_image);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                TakeTourLandingFragment.this.startAnimation();
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void startAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{getScaleAnimationForView(this.recorderImage), getScaleAnimationForView(this.galleryImage), getScaleAnimationForView(this.gameImage)});
        animatorSet.setDuration(800);
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(new Animator[]{getScaleAnimationForView(this.screenshotImage), getScaleAnimationForView(this.moreImage), getScaleAnimationForView(this.liveImage)});
        animatorSet2.setDuration(800);
        animatorSet.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                animatorSet2.start();
            }
        }, 400);
    }

    private ObjectAnimator getScaleAnimationForView(View view) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleX", new float[]{1.4f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.4f})});
        ofPropertyValuesHolder.setRepeatMode(ValueAnimator.REVERSE);
        ofPropertyValuesHolder.setRepeatCount(ValueAnimator.INFINITE);
        return ofPropertyValuesHolder;
    }

    public void onClick(View view) {
        if (isAdded()) {
            TakeTourLaunchActivity takeTourLaunchActivity = (TakeTourLaunchActivity) getActivity();
            if (view.getId() == C0793R.C0795id.id_take_tour_positive_button && takeTourLaunchActivity != null && takeTourLaunchActivity.isNextPossible()) {
                takeTourLaunchActivity.goToNextPage();
            }
        }
    }
}
