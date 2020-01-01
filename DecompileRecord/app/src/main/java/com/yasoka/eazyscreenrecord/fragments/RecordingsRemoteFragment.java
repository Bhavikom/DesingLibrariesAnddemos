package com.yasoka.eazyscreenrecord.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.CardView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.adapter.RecordingsRemoteMainListAdapter;
import com.ezscreenrecorder.database.DataSource;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.model.VideosRemoteDataModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideoMainModel;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Publisher;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;

public class RecordingsRemoteFragment extends Fragment implements OnRefreshListener, OnClickListener {
    private AppCompatButton loginButton;
    private CardView loginLayoutContainer;
    private AppCompatTextView loginMessage;
    /* access modifiers changed from: private */
    public RecordingsRemoteMainListAdapter mAdapter;
    /* access modifiers changed from: private */
    public AppCompatTextView noInternetError;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private SharedPreferences sharedPref;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_remote_recordings, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.id_recycler_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(C0793R.C0795id.id_swipe_refresh);
        this.loginButton = (AppCompatButton) view.findViewById(C0793R.C0795id.id_remote_recordings_login_button);
        this.loginLayoutContainer = (CardView) view.findViewById(C0793R.C0795id.id_remote_recordings_login_layout_container);
        this.loginMessage = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_remote_recordings_login_text_message);
        this.noInternetError = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_empty_error_text_view);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeResources(C0793R.color.colorPrimary);
        this.loginButton.setOnClickListener(this);
        isLogin();
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
        if (eventType == 4501) {
            this.loginLayoutContainer.setOnClickListener(this);
            this.loginButton.setVisibility(8);
            this.loginMessage.setText(C0793R.string.id_recordings_login_success_msg);
            getRemoteData();
        } else if (eventType == 4502) {
            this.loginLayoutContainer.setOnClickListener(null);
            this.loginButton.setVisibility(0);
            this.loginMessage.setText(C0793R.string.id_recordings_login_default_msg);
        } else if (eventType == 4508) {
            getRemoteData();
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            try {
                if (isListEmpty()) {
                    getRemoteData();
                }
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isListEmpty() {
        try {
            if (this.mAdapter != null && this.mAdapter.getItemCount() > 0) {
                return false;
            }
        } catch (Exception unused) {
        }
        return true;
    }

    private boolean isLogin() {
        if (this.sharedPref == null) {
            this.sharedPref = getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        }
        String str = "";
        if (!this.sharedPref.getString(ServerAPI.USER_ID, str).equalsIgnoreCase(str)) {
            this.loginLayoutContainer.setOnClickListener(this);
            this.loginButton.setVisibility(8);
            this.loginMessage.setText(C0793R.string.id_recordings_login_success_msg);
            return true;
        }
        this.loginLayoutContainer.setOnClickListener(null);
        this.loginButton.setVisibility(0);
        this.loginMessage.setText(C0793R.string.id_recordings_login_default_msg);
        return false;
    }

    private void getRemoteData() {
        if (!RecorderApplication.getInstance().isNetworkAvailable()) {
            this.noInternetError.setText(C0793R.string.id_no_internet_error_list_message);
            this.noInternetError.setVisibility(0);
        } else {
            this.noInternetError.setVisibility(8);
        }
        if (this.mAdapter == null) {
            this.mAdapter = new RecordingsRemoteMainListAdapter((AppCompatActivity) getActivity());
        }
        this.mAdapter.clearList();
        setRefreshing(true);
        ServerAPI.getInstance().getVideoMainScreen().flatMapPublisher(new Function<VideoMainModel, Publisher<VideosRemoteDataModel>>() {
            public Publisher<VideosRemoteDataModel> apply(VideoMainModel videoMainModel) throws Exception {
                ArrayList arrayList = new ArrayList();
                String str = "Top Videos";
                if (videoMainModel.getData().getTopVideo() != null && videoMainModel.getData().getTopVideo().size() > 0) {
                    VideosRemoteDataModel videosRemoteDataModel = new VideosRemoteDataModel();
                    videosRemoteDataModel.setHavingMore(false);
                    videosRemoteDataModel.setListData(videoMainModel.getData().getTopVideo());
                    videosRemoteDataModel.setTitle(str);
                    videosRemoteDataModel.setTypeOfList(VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_TOP_VIDEOS);
                    arrayList.add(videosRemoteDataModel);
                }
                if (videoMainModel.getData().getEditorChoice() != null && videoMainModel.getData().getEditorChoice().size() > 0) {
                    VideosRemoteDataModel videosRemoteDataModel2 = new VideosRemoteDataModel();
                    videosRemoteDataModel2.setHavingMore(false);
                    videosRemoteDataModel2.setListData(videoMainModel.getData().getEditorChoice());
                    videosRemoteDataModel2.setTitle(str);
                    videosRemoteDataModel2.setTypeOfList(VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_EDITOR_CHOICE_VIDEOS);
                    arrayList.add(videosRemoteDataModel2);
                }
                try {
                    DataSource dataSource = new DataSource(RecordingsRemoteFragment.this.getActivity().getApplicationContext());
                    dataSource.open();
                    List allFavorites = dataSource.getAllFavorites();
                    dataSource.close();
                    if (allFavorites.size() > 0) {
                        VideosRemoteDataModel videosRemoteDataModel3 = new VideosRemoteDataModel();
                        videosRemoteDataModel3.setHavingMore(false);
                        videosRemoteDataModel3.setListData(allFavorites);
                        videosRemoteDataModel3.setTypeOfList(VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_FAVORITE_VIDEOS);
                        arrayList.add(videosRemoteDataModel3);
                    }
                } catch (Exception unused) {
                }
                if (videoMainModel.getData().getUserVideo() != null && videoMainModel.getData().getUserVideo().size() > 0) {
                    VideosRemoteDataModel videosRemoteDataModel4 = new VideosRemoteDataModel();
                    videosRemoteDataModel4.setHavingMore(false);
                    videosRemoteDataModel4.setListData(videoMainModel.getData().getUserVideo());
                    videosRemoteDataModel4.setTypeOfList(VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_USER_VIDEOS);
                    arrayList.add(videosRemoteDataModel4);
                }
                if (videoMainModel.getData().getOtherVideo() != null && videoMainModel.getData().getOtherVideo().size() > 0) {
                    VideosRemoteDataModel videosRemoteDataModel5 = new VideosRemoteDataModel();
                    videosRemoteDataModel5.setHavingMore(true);
                    videosRemoteDataModel5.setListData(videoMainModel.getData().getOtherVideo());
                    videosRemoteDataModel5.setTitle("Other Videos");
                    videosRemoteDataModel5.setTypeOfList(VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_OTHER_VIDEOS);
                    arrayList.add(videosRemoteDataModel5);
                }
                return Flowable.fromIterable(arrayList);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<VideosRemoteDataModel>() {
            public void onNext(VideosRemoteDataModel videosRemoteDataModel) {
                RecordingsRemoteFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(RecordingsRemoteFragment.this.getContext(), 1, false));
                RecordingsRemoteFragment.this.recyclerView.setAdapter(RecordingsRemoteFragment.this.mAdapter);
                RecordingsRemoteFragment.this.mAdapter.addItem(videosRemoteDataModel);
            }

            public void onError(Throwable th) {
                RecordingsRemoteFragment.this.setRefreshing(false);
                th.printStackTrace();
                if (RecordingsRemoteFragment.this.isListEmpty()) {
                    RecordingsRemoteFragment.this.noInternetError.setText(C0793R.string.id_error_api_list_txt);
                    RecordingsRemoteFragment.this.noInternetError.setVisibility(0);
                }
            }

            public void onComplete() {
                RecordingsRemoteFragment.this.setRefreshing(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setRefreshing(final boolean z) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (RecordingsRemoteFragment.this.swipeRefreshLayout != null) {
                    RecordingsRemoteFragment.this.swipeRefreshLayout.setRefreshing(z);
                }
            }
        }, 30);
    }

    public void onRefresh() {
        if (isAdded()) {
            getRemoteData();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0793R.C0795id.id_remote_recordings_login_button /*2131296743*/:
                if (!isLogin()) {
                    try {
                        setRefreshing(true);
                        YoutubeAPI.getInstance().switchGoogleAccount(getActivity()).subscribe(new Consumer<String>() {
                            public void accept(String str) throws Exception {
                                if (RecordingsRemoteFragment.this.isAdded()) {
                                    RecordingsRemoteFragment.this.setRefreshing(false);
                                    ((GalleryActivity) RecordingsRemoteFragment.this.getActivity()).setLogoutOrChangeVideo(true);
                                    ((GalleryActivity) RecordingsRemoteFragment.this.getActivity()).setLogoutOrChangeImage(true);
                                    EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_SUCCESS));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            public void accept(Throwable th) throws Exception {
                                th.printStackTrace();
                                if (RecordingsRemoteFragment.this.isAdded()) {
                                    RecordingsRemoteFragment.this.setRefreshing(false);
                                    Toast.makeText(RecordingsRemoteFragment.this.getContext(), C0793R.string.id_login_error_msg, 1).show();
                                    EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                                }
                            }
                        });
                        return;
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            case C0793R.C0795id.id_remote_recordings_login_layout_container /*2131296744*/:
                EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_MOVE_TO_LOCAL_RECORDING_LIST));
                return;
            default:
                return;
        }
    }
}
