package com.yasoka.eazyscreenrecord.fullscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.activities.FullscreenActivity;


public class FullFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_full, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (!((FullscreenActivity) getActivity()).checkOverlayPermission()) {
            view.findViewById(R.id.id_full_screen_floating_button_view).setVisibility(View.GONE);
        }
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((FullscreenActivity) FullFragment.this.getActivity()).moveToNext();
            }
        });
    }
}
