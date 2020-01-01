package com.yasoka.eazyscreenrecord.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.app.FragmentPagerAdapter;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatImageView;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.View.OnClickListener;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.fragments.TakeTourFBHelpFragment;
import com.yasoka.eazyscreenrecord.fragments.TakeTourLandingFragment;
import com.yasoka.eazyscreenrecord.fragments.TakeTourNotificationHelpFragment;
import com.yasoka.eazyscreenrecord.p004ui.SwipeViewPager;
/*import com.ezscreenrecorder.fragments.TakeTourFBHelpFragment;
import com.ezscreenrecorder.fragments.TakeTourLandingFragment;
import com.ezscreenrecorder.fragments.TakeTourNotificationHelpFragment;
import com.ezscreenrecorder.p004ui.SwipeViewPager;*/
import java.util.ArrayList;
import java.util.List;

public class TakeTourLaunchActivity extends AppCompatActivity implements OnClickListener {
    public static final String TAKE_TOUR_KEY = "is_launched_from_take_tour";
    private static final int TIME_ANIMATION_IN_MILLI = 800;
    private AppCompatImageView galleryImage;
    private AppCompatImageView gameImage;
    private AppCompatImageView liveImage;
    private AppCompatImageView mainAppImage;
    private AppCompatImageView moreImage;
    private SwipeViewPager pager;
    private AppCompatImageView recorderImage;
    private AppCompatImageView screenshotImage;
    private AppCompatButton skipButton;
    private TabLayout tabLayout;
    private AppCompatButton takeTourButton;

    public class TourPagerAdapter extends FragmentPagerAdapter {
        public List<Fragment> mFragmentList = new ArrayList();

        public TourPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mFragmentList.add(new TakeTourLandingFragment());
            this.mFragmentList.add(new TakeTourFBHelpFragment());
            this.mFragmentList.add(new TakeTourNotificationHelpFragment());
        }

        public Fragment getItem(int i) {
            return (Fragment) this.mFragmentList.get(i);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_take_tour_launch);
        init1();
    }

    private void init1() {
        this.pager = (SwipeViewPager) findViewById(R.id.id_take_tour_view_pager);
        this.pager.setAdapter(new TourPagerAdapter(getSupportFragmentManager()));
        this.pager.setHorizontalSwipeEnabled(false);
    }

    public boolean isNextPossible() {
        SwipeViewPager swipeViewPager = this.pager;
        if (swipeViewPager == null || swipeViewPager.getAdapter() == null) {
            return false;
        }
        if (this.pager.getCurrentItem() < this.pager.getAdapter().getCount() - 1) {
            return true;
        }
        return false;
    }

    public void goToNextPage() {
        SwipeViewPager swipeViewPager = this.pager;
        if (swipeViewPager != null) {
            swipeViewPager.setCurrentItem(swipeViewPager.getCurrentItem() + 1);
        }
    }

    public boolean isPreviousPossible() {
        SwipeViewPager swipeViewPager = this.pager;
        if (swipeViewPager == null || swipeViewPager.getAdapter() == null || this.pager.getCurrentItem() <= 0) {
            return false;
        }
        return true;
    }

    public void goToPreviousPage() {
        SwipeViewPager swipeViewPager = this.pager;
        if (swipeViewPager != null) {
            swipeViewPager.setCurrentItem(swipeViewPager.getCurrentItem() - 1);
        }
    }

    public void init() {
        this.takeTourButton = (AppCompatButton) findViewById(R.id.id_take_tour_positive_button);
        this.skipButton = (AppCompatButton) findViewById(R.id.id_take_tour_negative_button);
        this.takeTourButton.setOnClickListener(this);
        this.skipButton.setOnClickListener(this);
        this.recorderImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_record_video);
        this.screenshotImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_take_screenshot);
        this.galleryImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_gallery);
        this.moreImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_more_option);
        this.gameImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_record_game);
        this.liveImage = (AppCompatImageView) findViewById(R.id.id_take_tour_floating_go_live);
        this.mainAppImage = (AppCompatImageView) findViewById(R.id.id_take_tour_main_app_image);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                TakeTourLaunchActivity.this.startAnimation();
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
        int id = view.getId();
        if (id == R.id.id_take_tour_negative_button) {
            startActivity(new Intent(this, DrawOverAppsPermissionActivity.class));
            ActivityCompat.finishAfterTransition(this);
        } else if (id == R.id.id_take_tour_positive_button) {
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            intent.putExtra(TAKE_TOUR_KEY, true);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        }
    }
}
