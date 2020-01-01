package com.yasoka.eazyscreenrecord.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff.Mode;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.OnScrollListener;*/
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.adapter.ImageListAdapter;
import com.ezscreenrecorder.adapter.ImageListAdapter2;
import com.ezscreenrecorder.adapter.ImageListAdapter2.ImageVideoListener;
import com.ezscreenrecorder.adapter.YouTubeVideoListAdapter;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.server.model.AllImagesResponse;
import com.ezscreenrecorder.server.model.AllVideoOutput;
import com.ezscreenrecorder.server.model.Datum;
import com.ezscreenrecorder.server.model.ServerDatum;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.StorageHelper;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.adapter.ImageListAdapter;
import com.yasoka.eazyscreenrecord.adapter.ImageListAdapter2;
import com.yasoka.eazyscreenrecord.adapter.YouTubeVideoListAdapter;
import com.yasoka.eazyscreenrecord.model.EventBusTypes;
import com.yasoka.eazyscreenrecord.model.ImageVideoFile;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.YoutubeAPI;
import com.yasoka.eazyscreenrecord.server.model.AllImagesResponse;
import com.yasoka.eazyscreenrecord.server.model.AllVideoOutput;
import com.yasoka.eazyscreenrecord.server.model.Datum;
import com.yasoka.eazyscreenrecord.server.model.ServerDatum;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class ImagesFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    /* access modifiers changed from: private */
    public int currentPage = 1;
    private DisposableSubscriber<ImageVideoFile> disposable;
    /* access modifiers changed from: private */
    public GridLayoutManager gridLayoutManager;
    /* access modifiers changed from: private */
    public ImageListAdapter imageListAdapter;
    /* access modifiers changed from: private */
    public ImageListAdapter2 imageListAdapter2;
    /* access modifiers changed from: private */
    public boolean isAllVideo;
    /* access modifiers changed from: private */
    public boolean isServer;
    /* access modifiers changed from: private */
    public boolean isTrim;
    /* access modifiers changed from: private */
    public int lastSelectedPosition;
    /* access modifiers changed from: private */
    public List<Datum> mAllVideos = new ArrayList();
    /* access modifiers changed from: private */
    public TextView mEmptyTxt;
    /* access modifiers changed from: private */
    public RecyclerView mImgVideoList;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public List<Datum> mMyVideos = new ArrayList();
    /* access modifiers changed from: private */
    public Spinner mSpinnerPublic;
    /* access modifiers changed from: private */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        int pastVisibleItems;
        int totalItemCount;
        int visibleChildItem;
        int visibleItemCount;

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            if (ImagesFragment.this.gridLayoutManager != null) {
                this.totalItemCount = ImagesFragment.this.gridLayoutManager.getItemCount();
                this.visibleChildItem = ImagesFragment.this.gridLayoutManager.getChildCount();
                this.pastVisibleItems = ImagesFragment.this.gridLayoutManager.findFirstVisibleItemPosition();
                if (!ImagesFragment.this.swipeRefreshLayout.isRefreshing() && this.visibleChildItem + this.pastVisibleItems >= this.totalItemCount - 5 && ImagesFragment.this.currentPage < ImagesFragment.this.totalPages) {
                    ImagesFragment.this.currentPage = ImagesFragment.this.currentPage + 1;
                    ImagesFragment.this.getRemoteFiles();
                }
            } else if (ImagesFragment.this.mLayoutManager != null && i2 > 0) {
                this.visibleItemCount = ImagesFragment.this.mLayoutManager.getChildCount();
                this.totalItemCount = ImagesFragment.this.mLayoutManager.getItemCount();
                this.pastVisibleItems = ImagesFragment.this.mLayoutManager.findFirstVisibleItemPosition();
                if (!ImagesFragment.this.swipeRefreshLayout.isRefreshing() && this.visibleItemCount + this.pastVisibleItems >= this.totalItemCount - 10) {
                    if (ImagesFragment.this.currentPage < ImagesFragment.this.totalPages) {
                        ImagesFragment.this.currentPage = ImagesFragment.this.currentPage + 1;
                        if (ImagesFragment.this.isAllVideo) {
                            ImagesFragment.this.allVideos(true);
                        }
                    }
                    Log.v("...", "Last Item Wow !");
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public LinearLayout permissionLayout;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;
    /* access modifiers changed from: private */
    public int totalPages = 1;
    /* access modifiers changed from: private */
    public YouTubeVideoListAdapter youTubeVideoListAdapter;

    public static ImagesFragment newInstance(boolean z) {
        ImagesFragment imagesFragment = new ImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTrim", z);
        imagesFragment.setArguments(bundle);
        return imagesFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.isTrim = getArguments().getBoolean("isTrim");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_videos, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.bottomNavigationView = (BottomNavigationView) view.findViewById(C0793R.C0795id.navigation);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(C0793R.C0795id.swipe_refresh);
        this.mImgVideoList = (RecyclerView) view.findViewById(C0793R.C0795id.videos_img_list);
        this.mSpinnerPublic = (Spinner) view.findViewById(C0793R.C0795id.spin_videos);
        this.permissionLayout = (LinearLayout) view.findViewById(C0793R.C0795id.id_gallery_storage_permission_layout);
        this.mSpinnerPublic.getBackground().setColorFilter(-1, Mode.SRC_ATOP);
        this.swipeRefreshLayout.setEnabled(false);
        this.bottomNavigationView.setVisibility(View.VISIBLE);
        this.mSpinnerPublic.setVisibility(View.GONE);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == C0793R.C0795id.navigation_all_screenshots) {
                    ImagesFragment.this.permissionLayout.setVisibility(View.GONE);
                    if (ImagesFragment.this.mSpinnerPublic.getAdapter() == null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(ImagesFragment.this.getString(C0793R.string.id_all_images_txt));
                        String str = "Select Account";
                        String string = PreferenceManager.getDefaultSharedPreferences(ImagesFragment.this.getActivity()).getString("youtube_account_email", str);
                        if (string.equals(str)) {
                            ImagesFragment imagesFragment = ImagesFragment.this;
                            arrayList.add(imagesFragment.getString(C0793R.string.id_my_images_txt, imagesFragment.getString(C0793R.string.id_select_account_txt)));
                        } else {
                            arrayList.add(ImagesFragment.this.getString(C0793R.string.id_my_images_txt, string));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(ImagesFragment.this.getContext(), C0793R.layout.spinner_txt_item, arrayList);
                        arrayAdapter.setDropDownViewResource(17367049);
                        ImagesFragment.this.mSpinnerPublic.setAdapter(arrayAdapter);
                    } else {
                        ImagesFragment.this.swipeRefreshLayout.setEnabled(true);
                        ImagesFragment.this.isServer = true;
                        if (ImagesFragment.this.isServer) {
                            ImagesFragment.this.mImgVideoList.addOnScrollListener(ImagesFragment.this.onScrollListener);
                        }
                    }
                    ImagesFragment.this.mSpinnerPublic.setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }

                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                            ImagesFragment.this.lastSelectedPosition = i;
                            if (i == 0) {
                                ImagesFragment.this.swipeRefreshLayout.setEnabled(true);
                                ImagesFragment.this.gridLayoutManager = new GridLayoutManager(ImagesFragment.this.getContext(), 2);
                                ImagesFragment.this.mImgVideoList.setLayoutManager(ImagesFragment.this.gridLayoutManager);
                                if (ImagesFragment.this.imageListAdapter2 == null) {
                                    ImagesFragment.this.imageListAdapter2 = new ImageListAdapter2(ImagesFragment.this.getContext(), new ImageVideoListener() {
                                        public void refresh() {
                                            ImagesFragment.this.currentPage = 1;
                                            ImagesFragment.this.imageListAdapter2.removeAllItems();
                                            ImagesFragment.this.getRemoteFiles();
                                        }

                                        public void onDelete() {
                                            if (ImagesFragment.this.isListEmpty()) {
                                                ImagesFragment.this.currentPage = 1;
                                                ImagesFragment.this.imageListAdapter2.removeAllItems();
                                                ImagesFragment.this.getRemoteFiles();
                                            }
                                        }
                                    }, ImagesFragment.this.currentPage);
                                }
                                ImagesFragment.this.mImgVideoList.setAdapter(ImagesFragment.this.imageListAdapter2);
                                ImagesFragment.this.isServer = true;
                                ImagesFragment.this.currentPage = 1;
                                ImagesFragment.this.imageListAdapter2.removeAllItems();
                                ImagesFragment.this.getRemoteFiles();
                                if (ImagesFragment.this.isServer) {
                                    ImagesFragment.this.mImgVideoList.addOnScrollListener(ImagesFragment.this.onScrollListener);
                                }
                            } else if (i == 1) {
                                ImagesFragment.this.mImgVideoList.removeOnScrollListener(ImagesFragment.this.onScrollListener);
                                String str = "";
                                if (ImagesFragment.this.getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0).getString(ServerAPI.USER_ID, str).equalsIgnoreCase(str)) {
                                    try {
                                        YoutubeAPI.getInstance().switchGoogleAccount(ImagesFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                            public void accept(String str) throws Exception {
                                                ((GalleryActivity) ImagesFragment.this.getActivity()).setLogoutOrChangeImage(true);
                                                ((GalleryActivity) ImagesFragment.this.getActivity()).setLogoutOrChangeVideo(true);
                                                ArrayList arrayList = new ArrayList();
                                                arrayList.add(ImagesFragment.this.getString(C0793R.string.id_all_images_txt));
                                                String prefYoutubeEmailId = PreferenceHelper.getInstance().getPrefYoutubeEmailId();
                                                if (prefYoutubeEmailId.equals(ImagesFragment.this.getString(C0793R.string.id_select_account_txt))) {
                                                    arrayList.add(ImagesFragment.this.getString(C0793R.string.id_my_images_txt, ImagesFragment.this.getString(C0793R.string.id_select_account_txt)));
                                                    EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                                                } else {
                                                    arrayList.add(ImagesFragment.this.getString(C0793R.string.id_my_images_txt, prefYoutubeEmailId));
                                                    EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_SUCCESS));
                                                }
                                                ArrayAdapter arrayAdapter = new ArrayAdapter(ImagesFragment.this.getContext(), C0793R.layout.spinner_txt_item, arrayList);
                                                arrayAdapter.setDropDownViewResource(17367049);
                                                ImagesFragment.this.mSpinnerPublic.setAdapter(arrayAdapter);
                                                ImagesFragment.this.mSpinnerPublic.setSelection(1);
                                            }
                                        }, new Consumer<Throwable>() {
                                            public void accept(Throwable th) throws Exception {
                                                th.printStackTrace();
                                                Toast.makeText(ImagesFragment.this.getContext(), C0793R.string.id_login_error_msg, 1).show();
                                                ImagesFragment.this.mSpinnerPublic.setSelection(0);
                                            }
                                        });
                                    } catch (NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ImagesFragment.this.swipeRefreshLayout.setEnabled(true);
                                    if (ImagesFragment.this.gridLayoutManager == null) {
                                        ImagesFragment.this.gridLayoutManager = new GridLayoutManager(ImagesFragment.this.getContext(), 2);
                                    }
                                    ImagesFragment.this.mImgVideoList.setLayoutManager(ImagesFragment.this.gridLayoutManager);
                                    if (ImagesFragment.this.imageListAdapter2 == null) {
                                        ImagesFragment.this.imageListAdapter2 = new ImageListAdapter2(ImagesFragment.this.getContext(), new ImageVideoListener() {
                                            public void refresh() {
                                                ImagesFragment.this.currentPage = 1;
                                                ImagesFragment.this.imageListAdapter2.removeAllItems();
                                                ImagesFragment.this.getRemoteFiles();
                                            }

                                            public void onDelete() {
                                                if (ImagesFragment.this.isListEmpty()) {
                                                    ImagesFragment.this.currentPage = 1;
                                                    ImagesFragment.this.imageListAdapter2.removeAllItems();
                                                    ImagesFragment.this.getRemoteFiles();
                                                }
                                            }
                                        }, ImagesFragment.this.currentPage);
                                    }
                                    ImagesFragment.this.isServer = true;
                                    ImagesFragment.this.currentPage = 1;
                                    ImagesFragment.this.imageListAdapter2.removeAllItems();
                                    ImagesFragment.this.getRemoteFiles();
                                    ImagesFragment.this.mImgVideoList.setAdapter(ImagesFragment.this.imageListAdapter2);
                                }
                                ImagesFragment.this.mImgVideoList.removeOnScrollListener(ImagesFragment.this.onScrollListener);
                            }
                        }
                    });
                    ImagesFragment.this.mSpinnerPublic.setVisibility(0);
                    if (ImagesFragment.this.gridLayoutManager == null) {
                        ImagesFragment imagesFragment2 = ImagesFragment.this;
                        imagesFragment2.gridLayoutManager = new GridLayoutManager(imagesFragment2.getContext(), 2);
                    }
                    ImagesFragment.this.mImgVideoList.setLayoutManager(ImagesFragment.this.gridLayoutManager);
                    if (ImagesFragment.this.imageListAdapter2 == null) {
                        ImagesFragment imagesFragment3 = ImagesFragment.this;
                        imagesFragment3.imageListAdapter2 = new ImageListAdapter2(imagesFragment3.getContext(), new ImageVideoListener() {
                            public void refresh() {
                                ImagesFragment.this.currentPage = 1;
                                ImagesFragment.this.imageListAdapter2.removeAllItems();
                                ImagesFragment.this.getRemoteFiles();
                            }

                            public void onDelete() {
                                if (ImagesFragment.this.isListEmpty()) {
                                    ImagesFragment.this.currentPage = 1;
                                    ImagesFragment.this.imageListAdapter2.removeAllItems();
                                    ImagesFragment.this.getRemoteFiles();
                                }
                            }
                        }, ImagesFragment.this.currentPage);
                    }
                    ImagesFragment.this.mImgVideoList.setAdapter(ImagesFragment.this.imageListAdapter2);
                    ImagesFragment.this.isServer = true;
                    ImagesFragment.this.currentPage = 1;
                    if (ImagesFragment.this.imageListAdapter2.getItemCount() == 0) {
                        ImagesFragment.this.imageListAdapter2.removeAllItems();
                        ImagesFragment.this.mSpinnerPublic.setSelection(ImagesFragment.this.lastSelectedPosition);
                    }
                    return true;
                } else if (itemId != C0793R.C0795id.navigation_my_screenshots) {
                    return false;
                } else {
                    if (ImagesFragment.this.onScrollListener != null) {
                        ImagesFragment.this.mImgVideoList.removeOnScrollListener(ImagesFragment.this.onScrollListener);
                    }
                    ImagesFragment.this.mSpinnerPublic.setVisibility(8);
                    ImagesFragment.this.isServer = false;
                    ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                    ImagesFragment.this.swipeRefreshLayout.setEnabled(false);
                    ImagesFragment.this.mImgVideoList.removeOnScrollListener(ImagesFragment.this.onScrollListener);
                    ImagesFragment imagesFragment4 = ImagesFragment.this;
                    imagesFragment4.gridLayoutManager = new GridLayoutManager(imagesFragment4.getContext(), 2);
                    ImagesFragment.this.mImgVideoList.setLayoutManager(ImagesFragment.this.gridLayoutManager);
                    if (ImagesFragment.this.imageListAdapter == null) {
                        ImagesFragment imagesFragment5 = ImagesFragment.this;
                        imagesFragment5.imageListAdapter = new ImageListAdapter(imagesFragment5.getContext(), new ImageListAdapter.ImageVideoListener() {
                            public void refresh() {
                                ImagesFragment.this.getFiles();
                            }

                            public void onDelete() {
                                if (ImagesFragment.tpositionhis.isListEmpty()) {
                                    ImagesFragment.this.getFiles();
                                }
                            }
                        }, ImagesFragment.this.isTrim);
                    }
                    ImagesFragment.this.mImgVideoList.setAdapter(ImagesFragment.this.imageListAdapter);
                    ImagesFragment.this.refreshList();
                    return true;
                }
            }
        });
        this.mEmptyTxt = (TextView) view.findViewById(C0793R.C0795id.txt_empty_list);
        this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                ImagesFragment.this.currentPage = 1;
                ImagesFragment.this.imageListAdapter2.removeAllItems();
                ImagesFragment.this.getRemoteFiles();
            }
        });
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (checkForStoragePermission()) {
            this.bottomNavigationView.setSelectedItemId(C0793R.C0795id.navigation_my_screenshots);
        } else {
            this.permissionLayout.setVisibility(0);
        }
        this.permissionLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImagesFragment.this.isAdded()) {
                    ((GalleryActivity) ImagesFragment.this.getActivity()).requestStoragePermission();
                }
            }
        });
    }

    public void refreshList() {
        if (!isAdded()) {
            return;
        }
        if (!checkForStoragePermission()) {
            this.permissionLayout.setVisibility(0);
        } else if (isListEmpty()) {
            this.permissionLayout.setVisibility(8);
            getFiles();
        }
    }

    private boolean checkForStoragePermission() {
        return ((GalleryActivity) getActivity()).checkStoragePermission();
    }

    public void setMenuVisibility(boolean z) {
        super.setMenuVisibility(z);
        if (z) {
            refreshData();
        }
    }

    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            refreshData();
        }
    }

    private void refreshData() {
        if (getActivity() != null && ((GalleryActivity) getActivity()).isLogoutOrChangeImage()) {
            ((GalleryActivity) getActivity()).setLogoutOrChangeImage(false);
            int selectedItemPosition = this.mSpinnerPublic.getSelectedItemPosition();
            ArrayList arrayList = new ArrayList();
            arrayList.add(getString(C0793R.string.id_all_images_txt));
            String str = "Select Account";
            String string = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("youtube_account_email", str);
            if (string.equals(str)) {
                arrayList.add(getString(C0793R.string.id_my_images_txt, getString(C0793R.string.id_select_account_txt)));
                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), C0793R.layout.spinner_txt_item, arrayList);
                arrayAdapter.setDropDownViewResource(17367049);
                this.mSpinnerPublic.setAdapter(arrayAdapter);
                if (selectedItemPosition == 1) {
                    this.mSpinnerPublic.setSelection(0);
                } else {
                    this.mSpinnerPublic.setSelection(selectedItemPosition);
                }
            } else {
                arrayList.add(getString(C0793R.string.id_my_images_txt, string));
                ArrayAdapter arrayAdapter2 = new ArrayAdapter(getContext(), C0793R.layout.spinner_txt_item, arrayList);
                arrayAdapter2.setDropDownViewResource(17367049);
                this.mSpinnerPublic.setAdapter(arrayAdapter2);
                this.mSpinnerPublic.setSelection(selectedItemPosition);
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    /* access modifiers changed from: private */
    public void allVideos(boolean z) {
        if (!z) {
            this.isAllVideo = true;
            this.youTubeVideoListAdapter.clear();
            this.mAllVideos.clear();
        }
        if (!isNetworkConnected(getActivity())) {
            Toast.makeText(getContext(), C0793R.string.no_internet_connection, 1).show();
            return;
        }
        this.swipeRefreshLayout.setRefreshing(true);
        ServerAPI.getInstance().getAllVideos(this.currentPage).flatMapPublisher(new Function<AllVideoOutput, Publisher<Datum>>() {
            public Publisher<Datum> apply(AllVideoOutput allVideoOutput) throws Exception {
                if (!(allVideoOutput == null || allVideoOutput.getData() == null)) {
                    ImagesFragment.this.totalPages = allVideoOutput.getData().getTotalpages();
                    if (allVideoOutput.getData().getData() != null) {
                        return Flowable.fromIterable(allVideoOutput.getData().getData());
                    }
                }
                return Flowable.error((Throwable) new Exception("Unable to get Get Videos"));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<Datum>() {
            public void onNext(Datum datum) {
                if (ImagesFragment.this.isAllVideo) {
                    ImagesFragment.this.youTubeVideoListAdapter.addItem(datum);
                    ImagesFragment.this.mAllVideos.add(datum);
                }
            }

            public void onError(Throwable th) {
                try {
                    ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                    th.printStackTrace();
                    if (ImagesFragment.this.getContext() != null) {
                        Toast.makeText(ImagesFragment.this.getContext(), ImagesFragment.this.getContext().getString(C0793R.string.please_try_again), 1).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onComplete() {
                ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                if (!ImagesFragment.this.isAllVideo) {
                    return;
                }
                if (ImagesFragment.this.youTubeVideoListAdapter.getItemCount() == 0) {
                    ImagesFragment.this.mEmptyTxt.setVisibility(0);
                } else {
                    ImagesFragment.this.mEmptyTxt.setVisibility(8);
                }
            }
        });
    }

    private void myVideos() {
        this.isAllVideo = false;
        this.mMyVideos.clear();
        if (!isNetworkConnected(getActivity())) {
            this.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), C0793R.string.no_internet_connection, 1).show();
            return;
        }
        this.swipeRefreshLayout.setRefreshing(true);
        ServerAPI.getInstance().getMyVideos(getContext().getSharedPreferences(MainActivity.SHARED_NAME, 0).getString(ServerAPI.USER_ID, ""), this.currentPage).flatMapPublisher(new Function<AllVideoOutput, Publisher<Datum>>() {
            public Publisher<Datum> apply(AllVideoOutput allVideoOutput) throws Exception {
                if (allVideoOutput == null || allVideoOutput.getData() == null || allVideoOutput.getData().getData() == null) {
                    return Flowable.error((Throwable) new Exception("Unable to get Get Videos"));
                }
                return Flowable.fromIterable(allVideoOutput.getData().getData());
            }
        }).map(new Function<Datum, Datum>() {
            public Datum apply(Datum datum) throws Exception {
                datum.setUser_name("");
                return datum;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<Datum>() {
            public void onNext(Datum datum) {
                try {
                    if (!ImagesFragment.this.isAllVideo) {
                        ImagesFragment.this.youTubeVideoListAdapter.addItem(datum);
                        ImagesFragment.this.mMyVideos.add(datum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onError(Throwable th) {
                try {
                    ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                    th.printStackTrace();
                    if (ImagesFragment.this.getContext() != null) {
                        Toast.makeText(ImagesFragment.this.getContext(), ImagesFragment.this.getString(C0793R.string.please_try_again), 1).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onComplete() {
                try {
                    ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                    if (ImagesFragment.this.isAllVideo) {
                        return;
                    }
                    if (ImagesFragment.this.youTubeVideoListAdapter.getItemCount() == 0) {
                        ImagesFragment.this.mEmptyTxt.setVisibility(0);
                    } else {
                        ImagesFragment.this.mEmptyTxt.setVisibility(8);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getRemoteFiles() {
        this.mEmptyTxt.setVisibility(8);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        this.swipeRefreshLayout.setRefreshing(true);
        final int selectedItemPosition = this.mSpinnerPublic.getSelectedItemPosition();
        Single.just(Boolean.valueOf(true)).flatMap(new Function<Boolean, SingleSource<AllImagesResponse>>() {
            public SingleSource<AllImagesResponse> apply(Boolean bool) throws Exception {
                if (selectedItemPosition == 0) {
                    return ServerAPI.getInstance().getAllImages(ImagesFragment.this.currentPage);
                }
                return ServerAPI.getInstance().getMyImages(sharedPreferences.getString(ServerAPI.USER_ID, ""));
            }
        }).flatMapPublisher(new Function<AllImagesResponse, Publisher<ServerDatum>>() {
            public Publisher<ServerDatum> apply(AllImagesResponse allImagesResponse) throws Exception {
                if (selectedItemPosition == 0) {
                    ImagesFragment.this.totalPages = allImagesResponse.getData().getTotalpages().intValue();
                }
                return Flowable.fromIterable(allImagesResponse.getData().getData());
            }
        }).map(new Function<ServerDatum, ServerDatum>() {
            public ServerDatum apply(ServerDatum serverDatum) throws Exception {
                String added_date = serverDatum.getAdded_date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str = "America/Chicago";
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(str));
                serverDatum.setAdded_date(DateUtils.getRelativeTimeSpanString(simpleDateFormat.parse(added_date).getTime(), Calendar.getInstance(TimeZone.getTimeZone(str)).getTimeInMillis(), 1000).toString());
                return serverDatum;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<ServerDatum>() {
            public void onNext(ServerDatum serverDatum) {
                ImagesFragment.this.imageListAdapter2.addItem(serverDatum);
            }

            public void onError(Throwable th) {
                th.printStackTrace();
                ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
            }

            public void onComplete() {
                ImagesFragment.this.swipeRefreshLayout.setRefreshing(false);
                if (ImagesFragment.this.currentPage == 1 && ImagesFragment.this.imageListAdapter2.getItemCount() > 0) {
                    ImagesFragment.this.imageListAdapter2.addItemAtPosition(0, new NativeAdTempModel());
                }
            }
        });
    }

    public boolean isListEmpty() {
        ImageListAdapter imageListAdapter3 = this.imageListAdapter;
        if (imageListAdapter3 == null || imageListAdapter3.getItemCount() == 0) {
            return true;
        }
        if (this.imageListAdapter.getItemCount() == 1 && this.imageListAdapter.getItemViewType(0) == 1332) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public ImageVideoFile getModelFromFile(File file) {
        if (!file.isDirectory()) {
            if (file.length() == 0) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ImageVideoFile imageVideoFile = new ImageVideoFile();
                imageVideoFile.setPath(file.getAbsolutePath());
                imageVideoFile.setName(file.getName());
                imageVideoFile.setVideo(file.getAbsolutePath().endsWith(".mp4"));
                imageVideoFile.setFileSize(file.length());
                imageVideoFile.setCreated(file.lastModified());
                return imageVideoFile;
            }
        }
        return null;
    }

    public void getFiles() {
        if (!this.isServer) {
            this.mImgVideoList.setLayoutManager(new GridLayoutManager(getContext(), 2));
            this.imageListAdapter = new ImageListAdapter(getContext(), new ImageListAdapter.ImageVideoListener() {
                public void refresh() {
                    ImagesFragment.this.getFiles();
                }

                public void onDelete() {
                    if (ImagesFragment.this.isListEmpty()) {
                        ImagesFragment.this.getFiles();
                    }
                }
            }, this.isTrim);
            this.mImgVideoList.setAdapter(this.imageListAdapter);
        }
        this.imageListAdapter.removeAllItems();
        this.disposable = (DisposableSubscriber) Flowable.create(new FlowableOnSubscribe<ImageVideoFile>() {
            public void subscribe(FlowableEmitter<ImageVideoFile> flowableEmitter) throws Exception {
                File file = new File(AppUtils.getImageDir(ImagesFragment.this.getContext(), false));
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    Arrays.sort(listFiles, new Comparator<File>() {
                        public int compare(File file, File file2) {
                            return Long.compare(file2.lastModified(), file.lastModified());
                        }
                    });
                    for (File access$2100 : listFiles) {
                        ImageVideoFile access$21002 = ImagesFragment.this.getModelFromFile(access$2100);
                        if (access$21002 != null) {
                            flowableEmitter.onNext(access$21002);
                        }
                    }
                }
                if (StorageHelper.getInstance().externalMemoryAvailable()) {
                    File file2 = new File(AppUtils.getImageDir(ImagesFragment.this.getContext(), true));
                    if (file2.isDirectory()) {
                        File[] listFiles2 = file2.listFiles();
                        Arrays.sort(listFiles2, new Comparator<File>() {
                            public int compare(File file, File file2) {
                                return Long.compare(file2.lastModified(), file.lastModified());
                            }
                        });
                        for (File access$21003 : listFiles2) {
                            ImageVideoFile access$21004 = ImagesFragment.this.getModelFromFile(access$21003);
                            if (access$21004 != null) {
                                flowableEmitter.onNext(access$21004);
                            }
                        }
                    }
                }
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).flatMapSingle(new Function<ImageVideoFile, SingleSource<ImageVideoFile>>() {
            public SingleSource<ImageVideoFile> apply(final ImageVideoFile imageVideoFile) throws Exception {
                return Single.create(new SingleOnSubscribe<ImageVideoFile>() {
                    public void subscribe(SingleEmitter<ImageVideoFile> singleEmitter) throws Exception {
                        try {
                            if (imageVideoFile.isVideo()) {
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(ImagesFragment.this.getContext(), Uri.fromFile(new File(imageVideoFile.getPath())));
                                String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
                                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
                                String extractMetadata3 = mediaMetadataRetriever.extractMetadata(18);
                                imageVideoFile.setDuration(Long.parseLong(extractMetadata));
                                if (!(extractMetadata3 == null || extractMetadata2 == null)) {
                                    ImageVideoFile imageVideoFile = imageVideoFile;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(extractMetadata2);
                                    sb.append("x");
                                    sb.append(extractMetadata3);
                                    imageVideoFile.setResolution(sb.toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        singleEmitter.onSuccess(imageVideoFile);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<ImageVideoFile>() {
            public void onNext(ImageVideoFile imageVideoFile) {
                ImagesFragment.this.imageListAdapter.addItem(imageVideoFile);
            }

            public void onError(Throwable th) {
                th.printStackTrace();
                PackageManager packageManager = ImagesFragment.this.getContext().getPackageManager();
                int checkPermission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", ImagesFragment.this.getContext().getPackageName());
                int checkPermission2 = packageManager.checkPermission("android.permission.READ_EXTERNAL_STORAGE", ImagesFragment.this.getContext().getPackageName());
                if (checkPermission != 0 || checkPermission2 != 0 || (th instanceof NameNotFoundException)) {
                    Toast.makeText(ImagesFragment.this.getContext(), C0793R.string.no_permission_msg, 1).show();
                }
            }

            public void onComplete() {
                if (ImagesFragment.this.imageListAdapter.getItemCount() == 0) {
                    ImagesFragment.this.mEmptyTxt.setVisibility(0);
                    ImagesFragment.this.mEmptyTxt.setText(C0793R.string.first_screenshot);
                    ImagesFragment.this.mEmptyTxt.setCompoundDrawablesWithIntrinsicBounds(0, C0793R.mipmap.ic_cameraa, 0, 0);
                    ImagesFragment.this.mEmptyTxt.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(ImagesFragment.this.getContext(), RecordingActivity.class);
                            intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_SCREENSHOT);
                            intent.addFlags(268435456);
                            ImagesFragment.this.startActivity(intent);
                        }
                    });
                    if (ImagesFragment.this.getActivity() != null && ((GalleryActivity) ImagesFragment.this.getActivity()).myStartImages) {
                        ((GalleryActivity) ImagesFragment.this.getActivity()).myStartImages = false;
                        ((GalleryActivity) ImagesFragment.this.getActivity()).navigateToVideo();
                        return;
                    }
                    return;
                }
                ImagesFragment.this.mEmptyTxt.setVisibility(8);
                if (StorageHelper.getInstance().externalMemoryAvailable()) {
                    ImagesFragment.this.imageListAdapter.sortList();
                }
                ImagesFragment.this.imageListAdapter.addItemAtPosition(0, new NativeAdTempModel());
            }
        });
    }

    public void setCloudImage(String str) {
        this.bottomNavigationView.setSelectedItemId(C0793R.C0795id.navigation_all_screenshots);
        Spinner spinner = this.mSpinnerPublic;
        if (spinner != null) {
            spinner.setSelection(1);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (this.disposable != null && !this.disposable.isDisposed()) {
                this.disposable.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.disposable != null && !this.disposable.isDisposed()) {
                this.disposable.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
