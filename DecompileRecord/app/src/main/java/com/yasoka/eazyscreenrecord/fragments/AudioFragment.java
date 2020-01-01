package com.yasoka.eazyscreenrecord.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.widget.AppCompatSeekBar;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;*/
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.adapter.AudioListAdapter;
import com.ezscreenrecorder.adapter.AudioListAdapter.OnItemDeleteCallback;
import com.ezscreenrecorder.interfaces.OnListItemClickListener;
import com.ezscreenrecorder.model.AudioFileModel;
import com.ezscreenrecorder.model.NativeAdTempModel;
import com.ezscreenrecorder.p004ui.PlayPauseView;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.LocalAudioPlayer;
import com.ezscreenrecorder.utils.LocalAudioPlayer.OnPlayerCallbacks;
import com.ezscreenrecorder.utils.PlayerUtils;
import com.ezscreenrecorder.utils.StorageHelper;*/
import com.ezscreenrecorder.R;
//import com.yasoka.eazyscreenrecord.R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.activities.RecordingActivity;
import com.yasoka.eazyscreenrecord.adapter.AudioListAdapter;
import com.yasoka.eazyscreenrecord.interfaces.OnListItemClickListener;
import com.yasoka.eazyscreenrecord.model.AudioFileModel;
import com.yasoka.eazyscreenrecord.model.NativeAdTempModel;
import com.yasoka.eazyscreenrecord.p004ui.PlayPauseView;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.LocalAudioPlayer;
import com.yasoka.eazyscreenrecord.utils.PlayerUtils;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

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
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class AudioFragment extends Fragment implements OnListItemClickListener, OnClickListener, SwipeRefreshLayout.OnRefreshListener, AudioListAdapter.OnItemDeleteCallback, LocalAudioPlayer.OnPlayerCallbacks {
    private DisposableSubscriber<AudioFileModel> disposable;
    /* access modifiers changed from: private */
    public TextView emptyTextView;
    private LocalAudioPlayer localAudioPlayer;
    /* access modifiers changed from: private */
    public AudioListAdapter mAdapter;
    private TextView maxTimeTxt;
    private ImageButton nextButton;
    private TextView onGoingTimeTxt;
    private LinearLayout permissionLayout;
    private PlayPauseView playPauseButton;
    private LinearLayout playerContainer;
    private ImageButton previousButton;
    private RecyclerView recyclerView;
    private AppCompatSeekBar seekBar;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;

    public static AudioFragment getInstance() {
        return new AudioFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_audio, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.id_audio_frag_recycler_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_audio_frag_swipe_refresh);
        this.emptyTextView = (TextView) view.findViewById(R.id.id_audio_frag_empty_list_txt);
        this.previousButton = (ImageButton) view.findViewById(R.id.id_audio_frag_previous_track);
        this.nextButton = (ImageButton) view.findViewById(R.id.id_audio_frag_next_track);
        this.playPauseButton = (PlayPauseView) view.findViewById(R.id.id_audio_frag_play_pause_button);
        this.seekBar = (AppCompatSeekBar) view.findViewById(R.id.id_audio_frag_seekbar);
        this.playerContainer = (LinearLayout) view.findViewById(R.id.id_audio_frag_player_container);
        this.onGoingTimeTxt = (TextView) view.findViewById(R.id.id_audio_frag_on_going_time_txt);
        this.maxTimeTxt = (TextView) view.findViewById(R.id.id_audio_frag_max_time_txt);
        this.permissionLayout = (LinearLayout) view.findViewById(R.id.id_gallery_storage_permission_layout);
        this.playPauseButton.setOnClickListener(this);
        this.nextButton.setOnClickListener(this);
        this.previousButton.setOnClickListener(this);
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (this.mAdapter == null) {
            this.mAdapter = new AudioListAdapter(getActivity(), this);
        }
        if (this.localAudioPlayer == null) {
            this.localAudioPlayer = new LocalAudioPlayer((SeekBar) this.seekBar, this.mAdapter, (OnPlayerCallbacks) this);
        }
        this.mAdapter.setOnItemDeleteCallback(this);
        this.recyclerView.setAdapter(this.mAdapter);
        if (!checkForStoragePermission()) {
            this.permissionLayout.setVisibility(View.VISIBLE);
        } else if (isListEmpty()) {
            getFiles();
        }
        this.permissionLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AudioFragment.this.isAdded()) {
                    ((GalleryActivity) AudioFragment.this.getActivity()).requestStoragePermission();
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

    private boolean isListEmpty() {
        AudioListAdapter audioListAdapter = this.mAdapter;
        return audioListAdapter == null || audioListAdapter.getItemCount() == 0;
    }

    /* access modifiers changed from: private */
    public AudioFileModel getModelFromFile(File file) {
        if (!file.isDirectory()) {
            if (file.length() == 0) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AudioFileModel audioFileModel = new AudioFileModel();
                audioFileModel.setFilePath(file.getAbsolutePath());
                audioFileModel.setFileName(file.getName());
                audioFileModel.setFileSize(file.length());
                audioFileModel.setFileCreated(file.lastModified());
                return audioFileModel;
            }
        }
        return null;
    }

    private void getFiles() {
        this.disposable = (DisposableSubscriber) Flowable.create(new FlowableOnSubscribe<AudioFileModel>() {
            public void subscribe(FlowableEmitter<AudioFileModel> flowableEmitter) throws Exception {
                File file = new File(AppUtils.getAudioDir(false));
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    Arrays.sort(listFiles, new Comparator<File>() {
                        public int compare(File file, File file2) {
                            return Long.compare(file2.lastModified(), file.lastModified());
                        }
                    });
                    for (File access$300 : listFiles) {
                        AudioFileModel access$3002 = AudioFragment.this.getModelFromFile(access$300);
                        if (access$3002 != null) {
                            flowableEmitter.onNext(access$3002);
                        }
                    }
                }
                if (StorageHelper.getInstance().externalMemoryAvailable()) {
                    File file2 = new File(AppUtils.getAudioDir(true));
                    if (file2.isDirectory()) {
                        File[] listFiles2 = file2.listFiles();
                        Arrays.sort(listFiles2, new Comparator<File>() {
                            public int compare(File file, File file2) {
                                return Long.compare(file2.lastModified(), file.lastModified());
                            }
                        });
                        for (File access$3003 : listFiles2) {
                            AudioFileModel access$3004 = AudioFragment.this.getModelFromFile(access$3003);
                            if (access$3004 != null) {
                                flowableEmitter.onNext(access$3004);
                            }
                        }
                    }
                }
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).flatMapSingle(new Function<AudioFileModel, SingleSource<AudioFileModel>>() {
            public SingleSource<AudioFileModel> apply(final AudioFileModel audioFileModel) throws Exception {
                return Single.create(new SingleOnSubscribe<AudioFileModel>() {
                    public void subscribe(SingleEmitter<AudioFileModel> singleEmitter) throws Exception {
                        try {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(AudioFragment.this.getContext(), Uri.fromFile(new File(audioFileModel.getFilePath())));
                            audioFileModel.setFileDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        singleEmitter.onSuccess(audioFileModel);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<AudioFileModel>() {
            public void onNext(AudioFileModel audioFileModel) {
                AudioFragment.this.mAdapter.addItem(audioFileModel);
            }

            public void onError(Throwable th) {
                PackageManager packageManager = AudioFragment.this.getContext().getPackageManager();
                int checkPermission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", AudioFragment.this.getContext().getPackageName());
                int checkPermission2 = packageManager.checkPermission("android.permission.READ_EXTERNAL_STORAGE", AudioFragment.this.getContext().getPackageName());
                if (checkPermission != 0 || checkPermission2 != 0 || (th instanceof NameNotFoundException)) {
                    Toast.makeText(AudioFragment.this.getContext(), R.string.no_permission_msg, 1).show();
                }
            }

            public void onComplete() {
                if (AudioFragment.this.swipeRefreshLayout != null && AudioFragment.this.swipeRefreshLayout.isRefreshing()) {
                    AudioFragment.this.swipeRefreshLayout.setRefreshing(false);
                }
                if (AudioFragment.this.mAdapter.getItemCount() == 0) {
                    AudioFragment.this.emptyTextView.setVisibility(0);
                    AudioFragment.this.emptyTextView.setText(R.string.id_empty_audio_screen_txt);
                    AudioFragment.this.emptyTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.C0794drawable.ic_floating_audio_record_pressed, 0, 0);
                    AudioFragment.this.emptyTextView.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(AudioFragment.this.getContext(), RecordingActivity.class);
                            intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_AUDIO);
                            AudioFragment.this.startActivity(intent);
                        }
                    });
                    return;
                }
                AudioFragment.this.emptyTextView.setVisibility(8);
                if (StorageHelper.getInstance().externalMemoryAvailable()) {
                    AudioFragment.this.mAdapter.sortList();
                }
                if (AudioFragment.this.mAdapter.getItemCount() > 0) {
                    AudioFragment.this.mAdapter.addItemAtPosition(0, new NativeAdTempModel());
                }
            }
        });
    }

    public void onListItemClick(int i) {
        if (this.playerContainer.getVisibility() == View.GONE) {
            this.playerContainer.setVisibility(View.VISIBLE);
        }
        this.localAudioPlayer.playTrackAtIndex(i);
    }

    public void onPause() {
        super.onPause();
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onPause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onDestroy();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.id_audio_frag_next_track) {
            LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
            if (localAudioPlayer2 != null) {
                localAudioPlayer2.playNextTrack();
            }
        } else if (id == R.id.id_audio_frag_play_pause_button) {
            LocalAudioPlayer localAudioPlayer3 = this.localAudioPlayer;
            if (localAudioPlayer3 != null) {
                if (localAudioPlayer3.isPlaying()) {
                    this.localAudioPlayer.stop();
                } else {
                    this.localAudioPlayer.resumePlay();
                }
            }
        } else if (id == R.id.id_audio_frag_previous_track) {
            LocalAudioPlayer localAudioPlayer4 = this.localAudioPlayer;
            if (localAudioPlayer4 != null) {
                localAudioPlayer4.playPreviousTrack();
            }
        }
    }

    public void onRefresh() {
        AudioListAdapter audioListAdapter = this.mAdapter;
        if (audioListAdapter != null) {
            audioListAdapter.removeAll();
            getFiles();
        }
    }

    public void onItemDeletedAt(int i) {
        LocalAudioPlayer localAudioPlayer2 = this.localAudioPlayer;
        if (localAudioPlayer2 != null) {
            localAudioPlayer2.onItemDeletedAt(i);
        }
        AudioListAdapter audioListAdapter = this.mAdapter;
        if (audioListAdapter != null && audioListAdapter.isListEmpty()) {
            getFiles();
        }
    }

    public void onPlaybackToggle(boolean z) {
        if (z) {
            if (this.playPauseButton.isPlay()) {
                this.playPauseButton.toggle();
            }
        } else if (!this.playPauseButton.isPlay()) {
            this.playPauseButton.toggle();
        }
    }

    public void onTimeUpdate(long j, long j2) {
        Object[] objArr = {PlayerUtils.getInstance().milliSecondsToTimer(j2)};
        String str = "%s";
        this.maxTimeTxt.setText(String.format(str, objArr));
        this.onGoingTimeTxt.setText(String.format(str, new Object[]{PlayerUtils.getInstance().milliSecondsToTimer(j)}));
    }

    public void onChangePlayerVisibility(int i) {
        this.playerContainer.setVisibility(i);
    }
}
