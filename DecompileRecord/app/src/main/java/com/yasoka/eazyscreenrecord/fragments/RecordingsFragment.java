package com.yasoka.eazyscreenrecord.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.app.FragmentPagerAdapter;
import android.support.p000v4.view.ViewPager;*/
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.model.EventBusTypes;
import com.google.common.net.HttpHeaders;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.model.EventBusTypes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
/*import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;*/

import okhttp3.internal.http.HttpHeaders;

public class RecordingsFragment extends Fragment {
    private RecordingsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private class RecordingsPagerAdapter extends FragmentPagerAdapter {
        /* access modifiers changed from: private */
        public List<Fragment> fragmentList = new ArrayList();

        public CharSequence getPageTitle(int i) {
            return null;
        }

        public RecordingsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragmentList.add(new RecordingsLocalFragment());
            this.fragmentList.add(new RecordingsRemoteFragment());
        }

        public Fragment getItem(int i) {
            return (Fragment) this.fragmentList.get(i);
        }

        public int getCount() {
            return this.fragmentList.size();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_recordings, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.tabLayout = (TabLayout) view.findViewById(C0793R.C0795id.tabs_layout);
        this.viewPager = (ViewPager) view.findViewById(C0793R.C0795id.id_view_pager);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.pagerAdapter = new RecordingsPagerAdapter(getChildFragmentManager());
        this.viewPager.setAdapter(this.pagerAdapter);
        setupTabLayout();
    }

    private void setupTabLayout() {
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabLayout.getTabAt(0).setIcon((int) C0793R.C0794drawable.ic_device_on);
        this.tabLayout.getTabAt(1).setIcon((int) C0793R.C0794drawable.ic_img_cloud);
    }

    public void refreshLocalList() {
        RecordingsPagerAdapter recordingsPagerAdapter = this.pagerAdapter;
        if (recordingsPagerAdapter != null) {
            for (Fragment fragment : recordingsPagerAdapter.fragmentList) {
                if (fragment instanceof RecordingsLocalFragment) {
                    //Log.e(HttpHeaders.REFRESH, "Recording Frag ---------------------:");
                    ((RecordingsLocalFragment) fragment).refreshList();
                }
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EventBusTypes eventBusTypes) {
        int eventType = eventBusTypes.getEventType();
        if (eventType == 4506) {
            this.viewPager.setCurrentItem(1, true);
        } else if (eventType == 4507) {
            try {
                this.viewPager.setCurrentItem(0, true);
            } catch (Exception unused) {
            }
        }
    }
}
