package com.yasoka.eazyscreenrecord.utils;

import com.ezscreenrecorder.server.TwitchAPI;
import com.ezscreenrecorder.server.model.LiveTwitchModels.Game;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchGameListOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;
import java.util.List;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.disposables.Disposable;
import p009io.reactivex.observers.DisposableSingleObserver;

public class LiveTwitchHelper {
    private static final LiveTwitchHelper ourInstance = new LiveTwitchHelper();
    private Game selectedGame;
    /* access modifiers changed from: private */
    public LiveTwitchUserOutputModel userData;

    public interface OnGameListListener {
        void onComplete(List<Game> list);

        void onFailure();

        void onStart();
    }

    public interface OnTwitchLiveStartListener {
        void onComplete(LiveTwitchFinalModel liveTwitchFinalModel);

        void onFailure();

        void onStart();
    }

    public interface OnTwitchUserDataListener {
        void onComplete(LiveTwitchUserOutputModel liveTwitchUserOutputModel);

        void onFailure();

        void onStart();
    }

    public static LiveTwitchHelper getInstance() {
        return ourInstance;
    }

    private LiveTwitchHelper() {
    }

    public void searchGameListForText(String str, final OnGameListListener onGameListListener) {
        onGameListListener.onStart();
        TwitchAPI.getInstance().findLiveTwitchGame(str).subscribe((SingleObserver<? super T>) new SingleObserver<LiveTwitchGameListOutputModel>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(LiveTwitchGameListOutputModel liveTwitchGameListOutputModel) {
                onGameListListener.onComplete(liveTwitchGameListOutputModel.getGames());
            }

            public void onError(Throwable th) {
                onGameListListener.onFailure();
            }
        });
    }

    public void setGameSelectedForLive(Game game) {
        this.selectedGame = game;
    }

    public Game getGameSelectedForLive() {
        return this.selectedGame;
    }

    public void getUserData(final OnTwitchUserDataListener onTwitchUserDataListener) {
        onTwitchUserDataListener.onStart();
        TwitchAPI.getInstance().getUserData().subscribe((SingleObserver<? super T>) new DisposableSingleObserver<LiveTwitchUserOutputModel>() {
            public void onSuccess(LiveTwitchUserOutputModel liveTwitchUserOutputModel) {
                LiveTwitchHelper.this.userData = liveTwitchUserOutputModel;
                onTwitchUserDataListener.onComplete(liveTwitchUserOutputModel);
            }

            public void onError(Throwable th) {
                onTwitchUserDataListener.onFailure();
                LiveTwitchHelper.this.userData = null;
            }
        });
    }

    public LiveTwitchUserOutputModel getTwitchUserDataModel() {
        return this.userData;
    }

    public void startLiveOnTwitch(String str, String str2, final OnTwitchLiveStartListener onTwitchLiveStartListener) {
        onTwitchLiveStartListener.onStart();
        TwitchAPI.getInstance().startTwitchLive(str, str2).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<LiveTwitchFinalModel>() {
            public void onSuccess(LiveTwitchFinalModel liveTwitchFinalModel) {
                onTwitchLiveStartListener.onComplete(liveTwitchFinalModel);
            }

            public void onError(Throwable th) {
                onTwitchLiveStartListener.onFailure();
            }
        });
    }
}
