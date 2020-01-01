package com.yasoka.eazyscreenrecord.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p003v7.widget.AppCompatButton;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.DrawOverAppsPermissionActivity;
import com.ezscreenrecorder.activities.FullscreenActivity;
import com.ezscreenrecorder.activities.SplashActivity;
import com.ezscreenrecorder.activities.TakeTourLaunchActivity;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.activities.DrawOverAppsPermissionActivity;
import com.yasoka.eazyscreenrecord.activities.FullscreenActivity;
import com.yasoka.eazyscreenrecord.activities.SplashActivity;
import com.yasoka.eazyscreenrecord.activities.TakeTourLaunchActivity;

import java.util.Objects;

public class TakeTourNotificationHelpFragment extends Fragment implements OnClickListener {
    private AppCompatButton negativeButton;
    private AppCompatButton positiveButton;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_take_tour_notification_help, viewGroup, false);
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
        if (VERSION.SDK_INT < 23) {
            this.positiveButton.setText(C0793R.string.id_get_started_text);
            this.negativeButton.setVisibility(8);
        } else {
            this.positiveButton.setText(C0793R.string.id_use_floating_buttons_txt);
            this.negativeButton.setText(C0793R.string.id_use_notification_only_txt);
        }
        this.positiveButton.setOnClickListener(this);
        this.negativeButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (isAdded()) {
            TakeTourLaunchActivity takeTourLaunchActivity = (TakeTourLaunchActivity) getActivity();
            int id = view.getId();
            if (id == C0793R.C0795id.id_take_tour_negative_button) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(FullscreenActivity.KEY_SHOW_HELP, FullscreenActivity.EXTRA_HELP_TYPE_SHOW_NOTIFICATION);
                startActivity(intent);
                ActivityCompat.finishAfterTransition((Activity) Objects.requireNonNull(getActivity()));
            } else if (id == C0793R.C0795id.id_take_tour_positive_button) {
                if (VERSION.SDK_INT >= 23 || SplashActivity.isXiomi()) {
                    Intent intent2 = new Intent(getActivity(), DrawOverAppsPermissionActivity.class);
                    intent2.putExtra(DrawOverAppsPermissionActivity.KEY_ON_BOARDING_TYPE, DrawOverAppsPermissionActivity.EXTRA_ON_BOARDING_TYPE_NEW);
                    startActivity(intent2);
                } else {
                    startActivity(new Intent(getActivity(), FullscreenActivity.class));
                }
                ActivityCompat.finishAfterTransition((Activity) Objects.requireNonNull(getActivity()));
            }
        }
    }
}
