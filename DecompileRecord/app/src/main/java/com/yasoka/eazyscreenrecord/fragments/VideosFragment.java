package com.yasoka.eazyscreenrecord.fragments;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff.Mode;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TextInputLayout;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.OnScrollListener;*/
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.adapter.VideoListAdapter;
import com.ezscreenrecorder.adapter.VideoListAdapter.ImageVideoListener;
import com.ezscreenrecorder.adapter.YouTubeVideoListAdapter;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.server.model.AllVideoOutput;
import com.ezscreenrecorder.server.model.Datum;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.StorageHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.common.primitives.Ints;*/
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.adapter.VideoListAdapter;
import com.yasoka.eazyscreenrecord.adapter.YouTubeVideoListAdapter;
import com.yasoka.eazyscreenrecord.model.FirebaseDataDevice;
import com.yasoka.eazyscreenrecord.model.ImageVideoFile;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.YoutubeAPI;
import com.yasoka.eazyscreenrecord.server.model.AllVideoOutput;
import com.yasoka.eazyscreenrecord.server.model.Datum;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;
import com.yasoka.eazyscreenrecord.youtubeupload.UploadService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.ObservableSource;
import p009io.reactivex.Observer;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableObserver;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class VideosFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    /* access modifiers changed from: private */
    public int currentPage = 1;
    private DisposableSubscriber disposable;
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
        int visibleItemCount;

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            if (mLayoutManager != null && i2 > 0) {
                this.visibleItemCount = mLayoutManager.getChildCount();
                this.totalItemCount = mLayoutManager.getItemCount();
                this.pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!swipeRefreshLayout.isRefreshing() && this.visibleItemCount + this.pastVisibleItems >= this.totalItemCount - 10) {
                    if (currentPage < totalPages) {
                        currentPage = currentPage + 1;
                        if (isAllVideo) {
                            allVideos(true);
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
    public VideoListAdapter videoListAdapter;
    /* access modifiers changed from: private */
    public YouTubeVideoListAdapter youTubeVideoListAdapter;

    public static VideosFragment newInstance(boolean z) {
        VideosFragment videosFragment = new VideosFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTrim", z);
        videosFragment.setArguments(bundle);
        return videosFragment;
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
        this.bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.navigation);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        this.mImgVideoList = (RecyclerView) view.findViewById(R.id.videos_img_list);
        this.mSpinnerPublic = (Spinner) view.findViewById(R.id.spin_videos);
        this.permissionLayout = (LinearLayout) view.findViewById(R.id.id_gallery_storage_permission_layout);
        this.mSpinnerPublic.getBackground().setColorFilter(-1, Mode.SRC_ATOP);
        this.swipeRefreshLayout.setEnabled(false);
        this.bottomNavigationView.setVisibility(View.VISIBLE);
        this.mSpinnerPublic.setVisibility(View.GONE);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.navigation_all_screenshots) {
                    permissionLayout.setVisibility(View.GONE);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(getString(R.string.id_all_videos_txt));
                    String str = "Select Account";
                    String string = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("youtube_account_email", str);
                    if (string.equals(str)) {
                        VideosFragment videosFragment = VideosFragment.this;
                        arrayList.add(videosFragment.getString(R.string.id_my_videos_txt, videosFragment.getString(R.string.id_select_account_txt)));
                    } else {
                        arrayList.add(getString(R.string.id_my_videos_txt, string));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_txt_item, arrayList);
                    arrayAdapter.setDropDownViewResource(17367049);
                    mSpinnerPublic.setAdapter(arrayAdapter);
                    mSpinnerPublic.setOnItemSelectedListener(new OnItemSelectedListener() {
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }

                        @SuppressLint("CheckResult")
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                            lastSelectedPosition = i;
                            if (i == 0) {
                                swipeRefreshLayout.setEnabled(true);
                                youTubeVideoListAdapter = new YouTubeVideoListAdapter(getContext(), true);
                                isAllVideo = true;
                                currentPage = 1;
                                if (mAllVideos.size() > 0) {
                                    youTubeVideoListAdapter.clear();
                                    youTubeVideoListAdapter.addAllItem(mAllVideos);
                                    mEmptyTxt.setVisibility(View.GONE);
                                } else {
                                    allVideos(false);
                                }
                                mImgVideoList.setAdapter(youTubeVideoListAdapter);
                                mImgVideoList.addOnScrollListener(onScrollListener);
                            } else if (i == 1) {
                                swipeRefreshLayout.setEnabled(true);
                                String str = "";
                                if (getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0).getString(ServerAPI.USER_ID, str).equalsIgnoreCase(str)) {
                                    try {
                                        YoutubeAPI.getInstance().switchGoogleAccount(getActivity()).subscribe(new Consumer<String>() {
                                            public void accept(String str) throws Exception {
                                                ((GalleryActivity) getActivity()).setLogoutOrChangeVideo(true);
                                                ((GalleryActivity) getActivity()).setLogoutOrChangeImage(true);
                                                ArrayList arrayList = new ArrayList();
                                                arrayList.add(getString(R.string.id_all_videos_txt));
                                                String str2 = "Select Account";
                                                String string = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("youtube_account_email", str2);
                                                if (string.equals(str2)) {
                                                    arrayList.add(getString(R.string.id_my_videos_txt, getString(R.string.id_select_account_txt)));
                                                } else {
                                                    arrayList.add(getString(R.string.id_my_videos_txt, string));
                                                }
                                                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_txt_item, arrayList);
                                                arrayAdapter.setDropDownViewResource(17367049);
                                                mSpinnerPublic.setAdapter(arrayAdapter);
                                                mSpinnerPublic.setSelection(1);
                                            }
                                        }, new Consumer<Throwable>() {
                                            public void accept(Throwable th) throws Exception {
                                                th.printStackTrace();
                                                if (isAdded()) {
                                                    Toast.makeText(getContext(), R.string.id_login_error_msg, Toast.LENGTH_SHORT).show();
                                                    mSpinnerPublic.setSelection(0);
                                                }
                                            }
                                        });
                                    } catch (NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    youTubeVideoListAdapter = new YouTubeVideoListAdapter(getContext(), false);
                                    isAllVideo = false;
                                    myVideos();
                                    mImgVideoList.setAdapter(youTubeVideoListAdapter);
                                    mImgVideoList.removeOnScrollListener(onScrollListener);
                                }
                            }
                        }
                    });
                    mSpinnerPublic.setVisibility(View.VISIBLE);
                    VideosFragment videosFragment2 = VideosFragment.this;
                    videosFragment2.mLayoutManager = new LinearLayoutManager(videosFragment2.getContext());
                    mImgVideoList.setLayoutManager(mLayoutManager);
                    mSpinnerPublic.setSelection(lastSelectedPosition);
                    return true;
                } else if (itemId != R.id.navigation_my_screenshots) {
                    return false;
                } else {
                    mSpinnerPublic.setVisibility(View.GONE);
                    isServer = false;
                    swipeRefreshLayout.setEnabled(false);
                    mImgVideoList.removeOnScrollListener(onScrollListener);
                    mImgVideoList.setLayoutManager(new LinearLayoutManager(getContext()));
                    VideosFragment videosFragment3 = VideosFragment.this;
                    videosFragment3.videoListAdapter = new VideoListAdapter(videosFragment3.getContext(), new ImageVideoListener() {
                        public void itemClick() {
                        }

                        public void refresh() {
                            getFiles();
                        }

                        public void onUploadToYoutube(ImageVideoFile imageVideoFile) {
                            uploadToYoutube(imageVideoFile);
                        }

                        public void onDelete() {
                            if (isListEmpty()) {
                                getFiles();
                            }
                        }
                    }, isTrim);
                    mImgVideoList.setAdapter(videoListAdapter);
                    refreshList();
                    return true;
                }
            }
        });
        this.mEmptyTxt = (TextView) view.findViewById(R.id.txt_empty_list);
        this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (mSpinnerPublic == null || mSpinnerPublic.getSelectedItemPosition() != 0) {
                    VideosFragment videosFragment = VideosFragment.this;
                    videosFragment.youTubeVideoListAdapter = new YouTubeVideoListAdapter(videosFragment.getContext(), false);
                    isAllVideo = false;
                    myVideos();
                    mImgVideoList.setAdapter(youTubeVideoListAdapter);
                    mImgVideoList.removeOnScrollListener(onScrollListener);
                    return;
                }
                swipeRefreshLayout.setEnabled(true);
                VideosFragment videosFragment2 = VideosFragment.this;
                videosFragment2.youTubeVideoListAdapter = new YouTubeVideoListAdapter(videosFragment2.getContext(), true);
                isAllVideo = true;
                currentPage = 1;
                if (mAllVideos.size() > 0) {
                    swipeRefreshLayout.setRefreshing(false);
                    youTubeVideoListAdapter.clear();
                    youTubeVideoListAdapter.addAllItem(mAllVideos);
                    mEmptyTxt.setVisibility(View.GONE);
                } else {
                    allVideos(false);
                }
                mImgVideoList.setAdapter(youTubeVideoListAdapter);
                mImgVideoList.addOnScrollListener(onScrollListener);
            }
        });
    }

    public void uploadToYoutube(ImageVideoFile imageVideoFile) {
        if (checkGetAccountsPermission()) {
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Builder builder = new Builder(getActivity());
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.you_tube_layout, null);
            final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.txt_lay_title);
            EditText editText = textInputLayout.getEditText();
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.app_name));
            sb.append("-");
            sb.append(imageVideoFile.getName());
            editText.setText(sb.toString());
            final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.txt_lay_desc);
            textInputLayout2.getEditText().setText(R.string.you_tube_desc2);
            Builder title = builder.setView(inflate).setTitle((int) R.string.youtube_details);
            final ImageVideoFile imageVideoFile2 = imageVideoFile;
            C11975 r1 = new OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, int i) {
                    if (textInputLayout.getEditText().getText() == null || textInputLayout.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), R.string.id_enter_valid_title_error_msg, 1).show();
                    } else if (textInputLayout2.getEditText().getText() == null || textInputLayout2.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), R.string.id_enter_valid_desc_error_msg, 1).show();
                    } else {
                        final GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(getContext(), Arrays.asList(UploadService.SCOPES));
                        usingOAuth2.setBackOff(new ExponentialBackOff());
                        Observable.create(new ObservableOnSubscribe<String>() {
                            public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                                String str = "youtube_account_email";
                                if (defaultSharedPreferences.contains(str)) {
                                    ServerAPI.getInstance().addToFireBase(getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                        public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                            System.out.println("sucess");
                                        }

                                        public void onError(Throwable th) {
                                            th.printStackTrace();
                                        }
                                    });
                                    observableEmitter.onNext(defaultSharedPreferences.getString(str, ""));
                                    observableEmitter.onComplete();
                                    return;
                                }
                                YoutubeAPI.getInstance().switchGoogleAccount(getActivity()).subscribe(new Consumer<String>() {
                                    public void accept(String str) throws Exception {
                                        observableEmitter.onNext(str);
                                        ServerAPI.getInstance().addToFireBase(getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                            public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                                System.out.println("sucess");
                                            }

                                            public void onError(Throwable th) {
                                                th.printStackTrace();
                                            }
                                        });
                                        observableEmitter.onComplete();
                                    }
                                }, new Consumer<Throwable>() {
                                    public void accept(Throwable th) throws Exception {
                                        th.printStackTrace();
                                        observableEmitter.onError(th);
                                    }
                                });
                            }
                        }).flatMap(new Function<String, ObservableSource<String>>() {
                            public ObservableSource<String> apply(final String str) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<String>() {
                                    public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                                        observableEmitter.onNext(str);
                                        observableEmitter.onComplete();
                                    }
                                });
                            }
                        }).subscribe((Observer<? super T>) new DisposableObserver<String>() {
                            public void onNext(String str) {
                                ServerAPI.getInstance().addToFireBase(getContext(), "Upload to YouTube Starts").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                    public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                    }

                                    public void onError(Throwable th) {
                                        th.printStackTrace();
                                    }
                                });
                                usingOAuth2.setSelectedAccount(new Account(str, "com.google"));
                                Intent intent = new Intent(getContext(), UploadService.class);
                                intent.setData(Uri.fromFile(new File(imageVideoFile2.getPath())));
                                intent.putExtra(UploadService.ACCOUNT_KEY, str);
                                intent.putExtra("name", textInputLayout.getEditText().getText().toString());
                                String obj = textInputLayout2.getEditText().getText().toString();
                                StringBuilder sb = new StringBuilder();
                                sb.append(obj);
                                sb.append("\n\n");
                                sb.append(getString(R.string.you_tube_desc1));
                                intent.putExtra("desc", sb.toString());
                                if (imageVideoFile2.getDuration() == 0) {
                                    try {
                                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                        mediaMetadataRetriever.setDataSource(getContext(), Uri.fromFile(new File(imageVideoFile2.getPath())));
                                        imageVideoFile2.setDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, imageVideoFile2.getDuration() / 1000);
                                getActivity().startService(intent);
                                Toast.makeText(getContext(), R.string.upload_started_my, 1).show();
                            }

                            public void onError(Throwable th) {
                                th.printStackTrace();
                                dialogInterface.dismiss();
                            }

                            public void onComplete() {
                                dialogInterface.dismiss();
                            }
                        });
                    }
                }
            };
            title.setPositiveButton((int) R.string.youtube_upload, (OnClickListener) r1).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
            return;
        }
        requestGetAccountPermission();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1125 && iArr.length > 0 && iArr[0] != 0 && iArr[0] == -1) {
            showGetAccountsPermissionErrorDailog();
        }
    }

    private void showGetAccountsPermissionErrorDailog() {
        new Builder(getActivity()).setMessage((int) R.string.id_get_account_permission_failed_dialog_message).setPositiveButton((int) R.string.id_turn_it_on_txt, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isAdded()) {
                    dialogInterface.dismiss();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.GET_ACCOUNTS")) {
                        requestGetAccountPermission();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(getActivity().getPackageName());
                        intent.setData(Uri.parse(sb.toString()));
                        intent.addFlags(268435456);
                        intent.addFlags(Ints.MAX_POWER_OF_TWO);
                        intent.addFlags(8388608);
                        startActivity(intent);
                    }
                }
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(getContext(), R.string.id_get_account_permission_failed_toast_message, 1).show();
            }
        }).show();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (checkForStoragePermission()) {
            this.bottomNavigationView.setSelectedItemId(R.id.navigation_my_screenshots);
        } else {
            this.permissionLayout.setVisibility(View.VISIBLE);
        }
        this.permissionLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isAdded()) {
                    ((GalleryActivity) getActivity()).requestStoragePermission();
                }
            }
        });
    }

    public void refreshList() {
        if (!isAdded()) {
            return;
        }
        if (!checkForStoragePermission()) {
            this.permissionLayout.setVisibility(View.VISIBLE);
        } else if (isListEmpty()) {
            this.permissionLayout.setVisibility(View.GONE);
            getFiles();
        }
    }

    private boolean checkForStoragePermission() {
        return ((GalleryActivity) getActivity()).checkStoragePermission();
    }

    public boolean checkGetAccountsPermission() {
        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(getContext(), "android.permission.GET_ACCOUNTS") == 0) {
            return true;
        }
        return false;
    }

    public void requestGetAccountPermission() {
        requestPermissions(new String[]{"android.permission.GET_ACCOUNTS"}, Constants.REQUEST_CODE_GET_ACCOUNT_PERMISSION);
    }

    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            refreshData();
        }
    }

    public void setMenuVisibility(boolean z) {
        super.setMenuVisibility(z);
        if (z) {
            refreshData();
        }
    }

    private void refreshData() {
        if (getActivity() != null && ((GalleryActivity) getActivity()).isLogoutOrChangeVideo()) {
            ((GalleryActivity) getActivity()).setLogoutOrChangeVideo(false);
            int selectedItemPosition = this.mSpinnerPublic.getSelectedItemPosition();
            ArrayList arrayList = new ArrayList();
            arrayList.add(getString(R.string.id_all_videos_txt));
            String str = "Select Account";
            String string = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("youtube_account_email", str);
            if (string.equals(str)) {
                arrayList.add(getString(R.string.id_my_videos_txt, getString(R.string.id_select_account_txt)));
                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_txt_item, arrayList);
                arrayAdapter.setDropDownViewResource(17367049);
                this.mSpinnerPublic.setAdapter(arrayAdapter);
                if (selectedItemPosition == 1) {
                    this.mSpinnerPublic.setSelection(0);
                } else {
                    this.mSpinnerPublic.setSelection(selectedItemPosition);
                }
            } else {
                arrayList.add(getString(R.string.id_my_videos_txt, string));
                new ArrayAdapter(getContext(), R.layout.spinner_txt_item, arrayList).setDropDownViewResource(17367049);
                this.mSpinnerPublic.setSelection(selectedItemPosition);
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /* access modifiers changed from: private */
    public void allVideos(boolean z) {
        if (!z) {
            this.isAllVideo = true;
            this.youTubeVideoListAdapter.clear();
            this.mAllVideos.clear();
        }
        if (!isNetworkConnected(getActivity())) {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        this.swipeRefreshLayout.setRefreshing(true);
        ServerAPI.getInstance().getAllVideos(this.currentPage).flatMapPublisher(new Function<AllVideoOutput, Publisher<Datum>>() {
            public Publisher<Datum> apply(AllVideoOutput allVideoOutput) throws Exception {
                if (!(allVideoOutput == null || allVideoOutput.getData() == null)) {
                    totalPages = allVideoOutput.getData().getTotalpages();
                    if (allVideoOutput.getData().getData() != null) {
                        return Flowable.fromIterable(allVideoOutput.getData().getData());
                    }
                }
                return Flowable.error((Throwable) new Exception("Unable to get Get Videos"));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<Datum>() {
            public void onNext(Datum datum) {
                if (isAllVideo) {
                    youTubeVideoListAdapter.addItem(datum);
                    mAllVideos.add(datum);
                }
            }

            public void onError(Throwable th) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    th.printStackTrace();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), getContext().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onComplete() {
                swipeRefreshLayout.setRefreshing(false);
                if (!isAllVideo) {
                    return;
                }
                if (youTubeVideoListAdapter.getItemCount() == 0) {
                    mEmptyTxt.setVisibility(View.VISIBLE);
                } else {
                    mEmptyTxt.setVisibility(View.GONE);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void myVideos() {
        this.isAllVideo = false;
        this.mMyVideos.clear();
        if (!isNetworkConnected(getActivity())) {
            this.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
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
                    if (!isAllVideo) {
                        youTubeVideoListAdapter.addItem(datum);
                        mMyVideos.add(datum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onError(Throwable th) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    th.printStackTrace();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), getString(R.string.please_try_again), 1).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onComplete() {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    if (isAllVideo) {
                        return;
                    }
                    if (youTubeVideoListAdapter.getItemCount() == 0) {
                        mEmptyTxt.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyTxt.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isListEmpty() {
        VideoListAdapter videoListAdapter2 = this.videoListAdapter;
        if (videoListAdapter2 == null || videoListAdapter2.getItemCount() == 0) {
            return true;
        }
        if (this.videoListAdapter.getItemCount() == 1 && this.videoListAdapter.getItemViewType(0) == 1332) {
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
        this.mImgVideoList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.videoListAdapter = new VideoListAdapter(getContext(), new ImageVideoListener() {
            public void itemClick() {
            }

            public void refresh() {
                getFiles();
            }

            public void onUploadToYoutube(ImageVideoFile imageVideoFile) {
                uploadToYoutube(imageVideoFile);
            }

            public void onDelete() {
                if (isListEmpty()) {
                    getFiles();
                }
            }
        }, this.isTrim);
        this.mImgVideoList.setAdapter(this.videoListAdapter);
        this.videoListAdapter.removeAllItems();
        this.disposable = (DisposableSubscriber) Flowable.create(new FlowableOnSubscribe<ImageVideoFile>() {
            public void subscribe(FlowableEmitter<ImageVideoFile> flowableEmitter) throws Exception {
                File file = new File(AppUtils.getVideoDir(getContext(), false));
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    Arrays.sort(listFiles, new Comparator<File>() {
                        public int compare(File file, File file2) {
                            return Long.compare(file2.lastModified(), file.lastModified());
                        }
                    });
                    for (File access$1900 : listFiles) {
                        ImageVideoFile access$19002 = getModelFromFile(access$1900);
                        if (access$19002 != null) {
                            flowableEmitter.onNext(access$19002);
                        }
                    }
                }
                if (StorageHelper.getInstance().externalMemoryAvailable()) {
                    File file2 = new File(AppUtils.getVideoDir(getContext(), true));
                    if (file2.isDirectory()) {
                        File[] listFiles2 = file2.listFiles();
                        Arrays.sort(listFiles2, new Comparator<File>() {
                            public int compare(File file, File file2) {
                                return Long.compare(file2.lastModified(), file.lastModified());
                            }
                        });
                        for (File access$19003 : listFiles2) {
                            ImageVideoFile access$19004 = getModelFromFile(access$19003);
                            if (access$19004 != null) {
                                flowableEmitter.onNext(access$19004);
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
                                mediaMetadataRetriever.setDataSource(getContext(), Uri.fromFile(new File(imageVideoFile.getPath())));
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
        }).sorted(new Comparator<ImageVideoFile>() {
            public int compare(ImageVideoFile imageVideoFile, ImageVideoFile imageVideoFile2) {
                return Long.compare(imageVideoFile2.getCreated(), imageVideoFile.getCreated());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<ImageVideoFile>() {
            public void onNext(ImageVideoFile imageVideoFile) {
                videoListAdapter.addItem(imageVideoFile);
            }

            public void onError(Throwable th) {
                th.printStackTrace();
                PackageManager packageManager = getContext().getPackageManager();
                int checkPermission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getContext().getPackageName());
                int checkPermission2 = packageManager.checkPermission("android.permission.READ_EXTERNAL_STORAGE", getContext().getPackageName());
                if (checkPermission != 0 || checkPermission2 != 0 || (th instanceof NameNotFoundException)) {
                    Toast.makeText(getContext(), R.string.no_permission_msg, 1).show();
                }
            }

            public void onComplete() {
                if (videoListAdapter.getItemCount() == 0) {
                    mEmptyTxt.setVisibility(View.VISIBLE);
                    mEmptyTxt.setText(R.string.first_recording);
                    mEmptyTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_videoo, 0, 0);
                    mEmptyTxt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), RecordingActivity.class);
                            intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                            intent.addFlags(268435456);
                            startActivity(intent);
                        }
                    });
                    if (getActivity() != null && ((GalleryActivity) getActivity()).myStartVideos) {
                        ((GalleryActivity) getActivity()).myStartVideos = false;
                        getAllVideos();
                        ((GalleryActivity) getActivity()).navigateToImage();
                        return;
                    }
                    return;
                }
                mEmptyTxt.setVisibility(View.GONE);
                if (videoListAdapter.getItemCount() > 0) {
                    videoListAdapter.addItemAtPosition(0, new NativeAdTempModel());
                }
            }
        });
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

    public void getMyVideos() {
        this.bottomNavigationView.setSelectedItemId(R.id.navigation_my_screenshots);
    }

    public void getAllVideos() {
        BottomNavigationView bottomNavigationView2 = this.bottomNavigationView;
        if (bottomNavigationView2 != null) {
            bottomNavigationView2.setSelectedItemId(R.id.navigation_all_screenshots);
        }
        if (this.mSpinnerPublic != null) {
            String str = "Select Account";
            if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("youtube_account_email", str).equalsIgnoreCase(str)) {
                this.mSpinnerPublic.setSelection(1);
            }
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
