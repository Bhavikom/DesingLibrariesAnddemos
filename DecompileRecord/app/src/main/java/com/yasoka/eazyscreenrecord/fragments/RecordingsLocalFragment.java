package com.yasoka.eazyscreenrecord.fragments;

import android.accounts.Account;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.CardView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;*/
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.adapter.RecordingsLocalAdapter;
import com.ezscreenrecorder.adapter.RecordingsLocalAdapter.VideoLocalListListener;
import com.ezscreenrecorder.model.EventBusTypes;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.model.VideoFileModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.StorageHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;*/
//import com.yasoka.eazyscreenrecord.R;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.adapter.RecordingsLocalAdapter;
import com.yasoka.eazyscreenrecord.model.EventBusTypes;
import com.yasoka.eazyscreenrecord.model.FirebaseDataDevice;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.model.VideoFileModel;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.YoutubeAPI;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;
import com.yasoka.eazyscreenrecord.video.NewVideoPlayerActivity;
import com.yasoka.eazyscreenrecord.youtubeupload.UploadService;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
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

public class RecordingsLocalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {
    private DisposableSubscriber disposable;
    /* access modifiers changed from: private */
    public TextView emptyTextView;
    private AppCompatButton loginButton;
    private CardView loginLayoutContainer;
    /* access modifiers changed from: private */
    public RecordingsLocalAdapter mAdapater;
    private LinearLayout permissionLayout;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_local_recordings, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.id_recycler_view);
        this.emptyTextView = (TextView) view.findViewById(R.id.id_empty_text_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_refresh);
        this.loginButton = (AppCompatButton) view.findViewById(R.id.id_remote_recordings_login_button);
        this.permissionLayout = (LinearLayout) view.findViewById(R.id.id_gallery_storage_permission_layout);
        this.loginLayoutContainer = (CardView) view.findViewById(R.id.id_remote_recordings_login_layout_container);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.loginButton.setOnClickListener(this);
        refreshList();
        isLogin();
    }

    private boolean isLogin() {
        if (!PreferenceHelper.getInstance().getPrefUserId().equalsIgnoreCase("")) {
            this.loginLayoutContainer.setVisibility(View.GONE);
            return true;
        }
        this.loginLayoutContainer.setVisibility(View.VISIBLE);
        return false;
    }

    public void refreshList() {
        if (!isAdded()) {
            return;
        }
        if (checkForStoragePermission()) {
            this.permissionLayout.setVisibility(View.GONE);
            getLocalFiles();
            return;
        }
        this.permissionLayout.setVisibility(View.VISIBLE);
        this.permissionLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (isAdded()) {
                    ((GalleryActivity) getActivity()).requestStoragePermission();
                }
            }
        });
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

    /* access modifiers changed from: private */
    public VideoFileModel getModelFromFile(File file) {
        if (!file.isDirectory()) {
            if (file.length() == 0) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                VideoFileModel videoFileModel = new VideoFileModel();
                videoFileModel.setPath(file.getAbsolutePath());
                videoFileModel.setName(file.getName());
                videoFileModel.setFileSize(file.length());
                videoFileModel.setCreated(file.lastModified());
                return videoFileModel;
            }
        }
        return null;
    }

    public boolean isListEmpty() {
        if (mAdapater  == null || mAdapater.getItemCount() == 0) {
            return true;
        }
        if (this.mAdapater.getItemCount() == 1 && this.mAdapater.getItemViewType(0) == 1332) {
            return true;
        }
        return false;
    }

    public void uploadToYoutube(VideoFileModel videoFileModel) {
        if (checkGetAccountsPermission()) {
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.you_tube_layout, null);
            final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.txt_lay_title);
            EditText editText = textInputLayout.getEditText();
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.app_name));
            sb.append("-");
            sb.append(videoFileModel.getName());
            editText.setText(sb.toString());
            final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.txt_lay_desc);
            textInputLayout2.getEditText().setText(R.string.you_tube_desc2);
            AlertDialog.Builder title = builder.setView(inflate).setTitle((int) R.string.youtube_details);
            final VideoFileModel videoFileModel2 = videoFileModel;
            C11073 r1 = new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, int i) {
                    if (textInputLayout.getEditText().getText() == null || textInputLayout.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), R.string.id_enter_valid_title_error_msg, Toast.LENGTH_SHORT).show();
                    } else if (textInputLayout2.getEditText().getText() == null || textInputLayout2.getEditText().getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), R.string.id_enter_valid_desc_error_msg, Toast.LENGTH_SHORT).show();
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
                                usingOAuth2.setSelectedAccount(new Account(str, "com.google"));
                                Intent intent = new Intent(getContext(), UploadService.class);
                                intent.setData(Uri.fromFile(new File(videoFileModel2.getPath())));
                                intent.putExtra(UploadService.ACCOUNT_KEY, str);
                                intent.putExtra("name", textInputLayout.getEditText().getText().toString());
                                String obj = textInputLayout2.getEditText().getText().toString();
                                StringBuilder sb = new StringBuilder();
                                sb.append(obj);
                                sb.append("\n\n");
                                sb.append(getString(R.string.you_tube_desc1));
                                intent.putExtra("desc", sb.toString());
                                if (videoFileModel2.getDuration() == 0) {
                                    try {
                                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                        mediaMetadataRetriever.setDataSource(getContext(), Uri.fromFile(new File(videoFileModel2.getPath())));
                                        videoFileModel2.setDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, videoFileModel2.getDuration() / 1000);
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
            title.setPositiveButton((int) R.string.youtube_upload, (DialogInterface.OnClickListener) r1).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
            return;
        }
        requestGetAccountPermission();
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
            CardView cardView = this.loginLayoutContainer;
            if (cardView != null) {
                cardView.setVisibility(View.GONE);
            }
        } else if (eventType == 4502) {
            CardView cardView2 = this.loginLayoutContainer;
            if (cardView2 != null) {
                cardView2.setVisibility(View.VISIBLE);
            }
        } else if (eventType == 4510) {
            refreshList();
        }
    }

    /* access modifiers changed from: private */
    public void getLocalFiles() {
        setRefreshing(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (this.mAdapater == null) {
            this.mAdapater = new RecordingsLocalAdapter(getActivity(), new RecordingsLocalAdapter.VideoLocalListListener() {
                public void onDelete() {
                    if (isListEmpty()) {
                        getLocalFiles();
                    }
                }

                public void onUploadToYoutube(VideoFileModel videoFileModel) {
                    uploadToYoutube(videoFileModel);
                }
            });
        }
        this.recyclerView.setAdapter(this.mAdapater);
        this.mAdapater.removeAllItems();
        this.disposable = (DisposableSubscriber) Flowable.create(new FlowableOnSubscribe<VideoFileModel>() {
            public void subscribe(FlowableEmitter<VideoFileModel> flowableEmitter) throws Exception {
                File file = new File(AppUtils.getVideoDir(getContext(), false));
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    Arrays.sort(listFiles, new Comparator<File>() {
                        public int compare(File file, File file2) {
                            return Long.compare(file2.lastModified(), file.lastModified());
                        }
                    });
                    for (File access$400 : listFiles) {
                        VideoFileModel access$4002 = getModelFromFile(access$400);
                        if (access$4002 != null) {
                            flowableEmitter.onNext(access$4002);
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
                        for (File access$4003 : listFiles2) {
                            VideoFileModel access$4004 = getModelFromFile(access$4003);
                            if (access$4004 != null) {
                                flowableEmitter.onNext(access$4004);
                            }
                        }
                    }
                }
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).flatMapSingle(new Function<VideoFileModel, SingleSource<VideoFileModel>>() {
            public SingleSource<VideoFileModel> apply(final VideoFileModel videoFileModel) throws Exception {
                return Single.create(new SingleOnSubscribe<VideoFileModel>() {
                    public void subscribe(SingleEmitter<VideoFileModel> singleEmitter) throws Exception {
                        try {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(getContext(), Uri.fromFile(new File(videoFileModel.getPath())));
                            String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
                            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
                            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(18);
                            videoFileModel.setDuration(Long.parseLong(extractMetadata));
                            if (!(extractMetadata3 == null || extractMetadata2 == null)) {
                                VideoFileModel videoFileModel = videoFileModel;
                                StringBuilder sb = new StringBuilder();
                                sb.append(extractMetadata2);
                                sb.append("x");
                                sb.append(extractMetadata3);
                                videoFileModel.setResolution(sb.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        singleEmitter.onSuccess(videoFileModel);
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        }).sorted(new Comparator<VideoFileModel>() {
            public int compare(VideoFileModel videoFileModel, VideoFileModel videoFileModel2) {
                return Long.compare(videoFileModel2.getCreated(), videoFileModel.getCreated());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<VideoFileModel>() {
            public void onNext(VideoFileModel videoFileModel) {
                mAdapater.addItem(videoFileModel);
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
                if (isAdded()) {
                    setRefreshing(false);
                    if (mAdapater.getItemCount() == 0) {
                        emptyTextView.setVisibility(0);
                        emptyTextView.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), RecordingActivity.class);
                                intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_VIDEO);
                                intent.addFlags(268435456);
                                startActivity(intent);
                            }
                        });
                        EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOCAL_RECORDING_LIST_EMPTY));
                    } else {
                        emptyTextView.setVisibility(8);
                        if (mAdapater.getItemCount() > 0) {
                            mAdapater.addItemAtPosition(0, new NativeAdTempModel());
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void setRefreshing(final boolean z) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(z);
                }
            }
        }, 30);
    }

    public void onRefresh() {
        if (isAdded()) {
            Log.e("onRefresh", "Refresh list ---------------------:");
            refreshList();
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.id_remote_recordings_login_button && !isLogin()) {
            try {
                setRefreshing(true);
                YoutubeAPI.getInstance().switchGoogleAccount(getActivity()).subscribe(new Consumer<String>() {
                    public void accept(String str) throws Exception {
                        if (isAdded()) {
                            setRefreshing(false);
                            ((GalleryActivity) getActivity()).setLogoutOrChangeVideo(true);
                            ((GalleryActivity) getActivity()).setLogoutOrChangeImage(true);
                            EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_SUCCESS));
                        }
                    }
                }, new Consumer<Throwable>() {
                    public void accept(Throwable th) throws Exception {
                        th.printStackTrace();
                        if (isAdded()) {
                            setRefreshing(false);
                            Toast.makeText(getContext(), R.string.id_login_error_msg, 1).show();
                            EventBus.getDefault().post(new EventBusTypes(EventBusTypes.EVENT_TYPE_LOGIN_FAILED));
                        }
                    }
                });
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
