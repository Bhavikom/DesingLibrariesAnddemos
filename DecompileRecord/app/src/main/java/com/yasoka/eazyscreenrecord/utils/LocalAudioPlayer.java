package com.yasoka.eazyscreenrecord.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.crashlytics.android.Crashlytics;
import com.yasoka.eazyscreenrecord.adapter.AudioListAdapter;
import com.yasoka.eazyscreenrecord.model.AudioFileModel;
import com.yasoka.eazyscreenrecord.p004ui.CircularSeekBar;


public class LocalAudioPlayer implements OnSeekBarChangeListener, CircularSeekBar.OnCircularSeekBarChangeListener {
    /* access modifiers changed from: private */
    public AudioFileModel audioFileModel;
    /* access modifiers changed from: private */
    public CircularSeekBar circularSeekBar;
    private int currentPlayingIndex = -1;
    private AudioListAdapter mAdapter;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long duration = (long) LocalAudioPlayer.this.player.getDuration();
                long currentPosition = (long) LocalAudioPlayer.this.player.getCurrentPosition();
                if (LocalAudioPlayer.this.onPlaybackChangeListener != null) {
                    LocalAudioPlayer.this.onPlaybackChangeListener.onTimeUpdate(currentPosition, duration);
                }
                if (LocalAudioPlayer.this.seekBar != null) {
                    LocalAudioPlayer.this.seekBar.setProgress(PlayerUtils.getInstance().getProgressPercentage(currentPosition, duration));
                } else if (LocalAudioPlayer.this.circularSeekBar != null) {
                    LocalAudioPlayer.this.circularSeekBar.setProgress(PlayerUtils.getInstance().getProgressPercentage(currentPosition, duration));
                }
                LocalAudioPlayer.this.mHandler.postDelayed(this, 100);
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        }
    };
    /* access modifiers changed from: private */
    public OnPlayerCallbacks onPlaybackChangeListener;
    /* access modifiers changed from: private */
    public MediaPlayer player;
    /* access modifiers changed from: private */
    public SeekBar seekBar;

    public interface OnPlayerCallbacks {
        void onChangePlayerVisibility(int i);

        void onPlaybackToggle(boolean z);

        void onTimeUpdate(long j, long j2);
    }

    public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
    }

    public void onProgressChanged(CircularSeekBar circularSeekBar2, int i, boolean z) {
    }

    public void onStartTrackingTouch(SeekBar seekBar2) {
        this.mHandler.removeCallbacks(this.mUpdateTimeTask);
    }

    public void onStopTrackingTouch(SeekBar seekBar2) {
        this.mHandler.removeCallbacks(this.mUpdateTimeTask);
        this.player.seekTo(PlayerUtils.getInstance().progressToTimer(seekBar2.getProgress(), this.player.getDuration()));
        updateProgressBar();
    }

    public void onStopTrackingTouch(CircularSeekBar circularSeekBar2) {
        this.mHandler.removeCallbacks(this.mUpdateTimeTask);
        this.player.seekTo(PlayerUtils.getInstance().progressToTimer(circularSeekBar2.getProgress(), this.player.getDuration()));
        updateProgressBar();
    }

    public void onStartTrackingTouch(CircularSeekBar circularSeekBar2) {
        this.mHandler.removeCallbacks(this.mUpdateTimeTask);
    }

    public LocalAudioPlayer(SeekBar seekBar2, AudioListAdapter audioListAdapter, OnPlayerCallbacks onPlayerCallbacks) {
        this.mAdapter = audioListAdapter;
        this.onPlaybackChangeListener = onPlayerCallbacks;
        if (seekBar2 != null) {
            this.seekBar = seekBar2;
            this.seekBar.setOnSeekBarChangeListener(this);
        }
    }

    public LocalAudioPlayer(SeekBar seekBar2, AudioFileModel audioFileModel2, OnPlayerCallbacks onPlayerCallbacks) {
        this.audioFileModel = audioFileModel2;
        this.onPlaybackChangeListener = onPlayerCallbacks;
        if (seekBar2 != null) {
            this.seekBar = seekBar2;
            this.seekBar.setOnSeekBarChangeListener(this);
        }
        if (this.audioFileModel != null) {
            platTrackFromModel();
        }
    }

    public LocalAudioPlayer(CircularSeekBar circularSeekBar2, AudioFileModel audioFileModel2, OnPlayerCallbacks onPlayerCallbacks) {
        this.audioFileModel = audioFileModel2;
        this.onPlaybackChangeListener = onPlayerCallbacks;
        if (this.circularSeekBar == null) {
            this.circularSeekBar = circularSeekBar2;
            this.circularSeekBar.setOnSeekBarChangeListener(this);
        }
        if (this.audioFileModel != null) {
            platTrackFromModel();
        }
    }

    public void playTrackAtIndex(int i) {
        AudioListAdapter audioListAdapter = this.mAdapter;
        if (audioListAdapter != null) {
            audioListAdapter.refreshItemAtIndex(this.currentPlayingIndex, false);
            if (this.mAdapter.getList().size() > i) {
                startPlayAtIndex(i);
            }
        }
    }

    public void platTrackFromModel() {
        initPlay(false);
    }

    private void startPlayAtIndex(int i) {
        Object obj = this.mAdapter.getList().get(i);
        if (obj instanceof AudioFileModel) {
            AudioListAdapter audioListAdapter = this.mAdapter;
            if (audioListAdapter != null) {
                audioListAdapter.refreshItemAtIndex(this.currentPlayingIndex, false);
            }
            this.currentPlayingIndex = i;
            this.audioFileModel = (AudioFileModel) obj;
            initPlay(true);
        }
    }

    private void initPlay(final boolean z) {
        if (this.player == null) {
            this.player = new MediaPlayer();
        } else if (isPlaying()) {
            stop();
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    LocalAudioPlayer.this.player.setOnPreparedListener(new OnPreparedListener() {
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            if (z) {
                                mediaPlayer.start();
                                LocalAudioPlayer.this.setPlaying(true);
                                LocalAudioPlayer.this.updateProgressBar();
                            }
                        }
                    });
                    LocalAudioPlayer.this.player.setOnErrorListener(new OnErrorListener() {
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                            LocalAudioPlayer.this.setPlaying(false);
                            return false;
                        }
                    });
                    LocalAudioPlayer.this.player.setOnCompletionListener(new OnCompletionListener() {
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            LocalAudioPlayer.this.setPlaying(false);
                        }
                    });
                    LocalAudioPlayer.this.player.reset();
                    LocalAudioPlayer.this.player.setDataSource(LocalAudioPlayer.this.audioFileModel.getFilePath());
                    LocalAudioPlayer.this.player.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        if (isPlaying()) {
            this.player.pause();
            setPlaying(false);
        }
    }

    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public void resumePlay() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            mediaPlayer.start();
            setPlaying(true);
            updateProgressBar();
        }
    }

    public void playNextTrack() {
        int i = this.currentPlayingIndex + 1;
        AudioListAdapter audioListAdapter = this.mAdapter;
        if (audioListAdapter != null && audioListAdapter.getList().size() > i) {
            startPlayAtIndex(i);
        }
    }

    public void playPreviousTrack() {
        int i = this.currentPlayingIndex - 1;
        if (i >= 0) {
            AudioListAdapter audioListAdapter = this.mAdapter;
            if (audioListAdapter != null && audioListAdapter.getList().size() > i) {
                startPlayAtIndex(i);
            }
        }
    }

    public void onPause() {
        stop();
    }

    public void onDestroy() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /* access modifiers changed from: private */
    public void setPlaying(boolean z) {
        OnPlayerCallbacks onPlayerCallbacks = this.onPlaybackChangeListener;
        if (onPlayerCallbacks != null) {
            onPlayerCallbacks.onPlaybackToggle(z);
        }
        if (!z) {
            this.mHandler.removeCallbacks(this.mUpdateTimeTask);
        }
        AudioListAdapter audioListAdapter = this.mAdapter;
        if (audioListAdapter != null) {
            audioListAdapter.refreshItemAtIndex(this.currentPlayingIndex, z);
        }
    }

    /* access modifiers changed from: private */
    public void updateProgressBar() {
        this.mHandler.postDelayed(this.mUpdateTimeTask, 100);
    }

    public void onItemDeletedAt(int i) {
        int i2 = this.currentPlayingIndex;
        if (i2 == i) {
            stop();
            OnPlayerCallbacks onPlayerCallbacks = this.onPlaybackChangeListener;
            if (onPlayerCallbacks != null) {
                onPlayerCallbacks.onChangePlayerVisibility(8);
            }
        } else if (i < i2) {
            this.currentPlayingIndex = i2 - 1;
        }
    }
}
