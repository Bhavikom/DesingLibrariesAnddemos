package com.yasoka.eazyscreenrecord.server;

import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchChannelOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchFinalModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchGameListOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUpdateChannelInputputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUpdateChannelOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;
import com.ezscreenrecorder.utils.PreferenceHelper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import p009io.reactivex.Single;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitchAPI {
    private static final String BASE_URL = "https://api.twitch.tv/";
    private static final TwitchAPI ourInstance = new TwitchAPI();
    private Retrofit retrofit;

    public static TwitchAPI getInstance() {
        return ourInstance;
    }

    private TwitchAPI() {
        setTwitchURL();
    }

    private void setTwitchURL() {
        this.retrofit = new Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder().addHeader("Accept", "application/vnd.twitchtv.v5+json").addHeader("Client-ID", RecorderApplication.getInstance().getString(C0793R.string.key_client_id_twitch)).build());
            }
        }).build()).build();
    }

    public APIReferences getApiReference() {
        return (APIReferences) this.retrofit.create(APIReferences.class);
    }

    public Single<LiveTwitchGameListOutputModel> findLiveTwitchGame(String str) {
        return getApiReference().getTwitchGameList(str).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LiveTwitchUserOutputModel> getUserData() {
        APIReferences apiReference = getApiReference();
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        sb.append(PreferenceHelper.getInstance().getTwitchAccessToken());
        return apiReference.getTwitchUserData(sb.toString()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LiveTwitchFinalModel> startTwitchLive(String str, String str2) {
        final LiveTwitchUpdateChannelInputputModel liveTwitchUpdateChannelInputputModel = new LiveTwitchUpdateChannelInputputModel();
        liveTwitchUpdateChannelInputputModel.setChannelChannelFeedEnabled(Boolean.valueOf(true));
        liveTwitchUpdateChannelInputputModel.setChannelGame(str2);
        liveTwitchUpdateChannelInputputModel.setChannelStatus(str);
        APIReferences apiReference = getApiReference();
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        sb.append(PreferenceHelper.getInstance().getTwitchAccessToken());
        return apiReference.getTwitchChannel(sb.toString()).flatMap(new Function<LiveTwitchChannelOutputModel, SingleSource<LiveTwitchFinalModel>>() {
            public SingleSource<LiveTwitchFinalModel> apply(final LiveTwitchChannelOutputModel liveTwitchChannelOutputModel) throws Exception {
                APIReferences apiReference = TwitchAPI.this.getApiReference();
                StringBuilder sb = new StringBuilder();
                sb.append("OAuth ");
                sb.append(PreferenceHelper.getInstance().getTwitchAccessToken());
                return apiReference.updateChannelStatus(sb.toString(), liveTwitchChannelOutputModel.getId(), liveTwitchUpdateChannelInputputModel).map(new Function<LiveTwitchUpdateChannelOutputModel, LiveTwitchFinalModel>() {
                    public LiveTwitchFinalModel apply(LiveTwitchUpdateChannelOutputModel liveTwitchUpdateChannelOutputModel) throws Exception {
                        return new LiveTwitchFinalModel(liveTwitchChannelOutputModel, liveTwitchUpdateChannelOutputModel);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
