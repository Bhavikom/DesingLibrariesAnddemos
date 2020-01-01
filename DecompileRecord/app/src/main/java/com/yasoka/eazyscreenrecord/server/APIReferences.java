package com.yasoka.eazyscreenrecord.server;

import com.ezscreenrecorder.model.AppInputEventModel;
import com.ezscreenrecorder.model.AppOutputEventModel;
import com.ezscreenrecorder.server.model.AllImagesResponse;
import com.ezscreenrecorder.server.model.AllVideoOutput;
import com.ezscreenrecorder.server.model.AnonymousInput;
import com.ezscreenrecorder.server.model.AnonymousOutput;
import com.ezscreenrecorder.server.model.FeedbackInput;
import com.ezscreenrecorder.server.model.FeedbackOutput;
import com.ezscreenrecorder.server.model.GameEventOutput.GameEventInputModel;
import com.ezscreenrecorder.server.model.GameEventOutput.GameEventOutputModel;
import com.ezscreenrecorder.server.model.GamesListModels.GamesMainModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchChannelOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchGameListOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUpdateChannelInputputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUpdateChannelOutputModel;
import com.ezscreenrecorder.server.model.LiveTwitchModels.LiveTwitchUserOutputModel;
import com.ezscreenrecorder.server.model.MoreVideosModels.MoreVideosDataModel;
import com.ezscreenrecorder.server.model.RecordingInput;
import com.ezscreenrecorder.server.model.RecordingOutput;
import com.ezscreenrecorder.server.model.SocialFeedModels.SocialFeedsMainModel;
import com.ezscreenrecorder.server.model.UploadImageResponse;
import com.ezscreenrecorder.server.model.UploadInput;
import com.ezscreenrecorder.server.model.UploadOutput;
import com.ezscreenrecorder.server.model.UserRegInput;
import com.ezscreenrecorder.server.model.UserRegOutput;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideoMainModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import p009io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIReferences {
    @POST("user-anonymous.php")
    Single<AnonymousOutput> anonymousUser(@Body AnonymousInput anonymousInput);

    @POST("feedback.php")
    Single<FeedbackOutput> feedback(@Body FeedbackInput feedbackInput);

    @GET("imges.php")
    Single<AllImagesResponse> getAllImages(@Query("currentpage") String str, @Query("cc") String str2, @Query("lc") String str3);

    @GET("image_testing.php")
    Single<AllImagesResponse> getAllImagesTesting(@Query("currentpage") String str);

    @GET("game_records.php")
    Single<GamesMainModel> getGamesList(@Query("cc") String str);

    @GET("videos_sort.php")
    Single<MoreVideosDataModel> getMoreVideos(@Query("currentpage") String str, @Query("sort") String str2, @Query("country") String str3, @Query("cc") String str4);

    @GET("imges.php")
    Single<AllImagesResponse> getMyImages(@Query("u_id") String str, @Query("cc") String str2, @Query("lc") String str3);

    @GET("socialfeed.php")
    Single<SocialFeedsMainModel> getSocialFeeds();

    @GET("kraken/channel")
    Single<LiveTwitchChannelOutputModel> getTwitchChannel(@Header("Authorization") String str);

    @GET("kraken/search/games")
    Single<LiveTwitchGameListOutputModel> getTwitchGameList(@Query("query") String str);

    @GET("kraken/user")
    Single<LiveTwitchUserOutputModel> getTwitchUserData(@Header("Authorization") String str);

    @GET("video_home_page.php")
    Single<VideoMainModel> getVideosMainScreen(@Query("u_id") String str, @Query("cc") String str2);

    @GET("videos.php")
    Single<AllVideoOutput> myVideos(@Query("u_id") String str, @Query("cc") String str2, @Query("lc") String str3);

    @GET("video_testing.php")
    Single<AllVideoOutput> myVideosTesting(@Query("uid") String str);

    @POST("user_events.php")
    Single<AppOutputEventModel> pushAppEvent(@Body AppInputEventModel appInputEventModel);

    @POST("recording_data.php")
    Single<RecordingOutput> recordingData(@Body RecordingInput recordingInput);

    @POST("user-registration.php")
    Single<UserRegOutput> registerationUser(@Body UserRegInput userRegInput);

    @POST("game_event.php")
    Single<GameEventOutputModel> sendGameEvent(@Body GameEventInputModel gameEventInputModel);

    @GET("video_testing.php")
    Single<AllVideoOutput> testVideos(@Query("currentpage") String str);

    @PUT("kraken/channels/{channelId}")
    Single<LiveTwitchUpdateChannelOutputModel> updateChannelStatus(@Header("Authorization") String str, @Path("channelId") String str2, @Body LiveTwitchUpdateChannelInputputModel liveTwitchUpdateChannelInputputModel);

    @POST("image-upload.php")
    @Multipart
    Call<UploadImageResponse> upload(@Part MultipartBody.Part part, @Part("a_id") RequestBody requestBody, @Part("email_id") RequestBody requestBody2, @Part("u_id") RequestBody requestBody3);

    @GET("videos.php")
    Single<AllVideoOutput> videos(@Query("currentpage") String str, @Query("cc") String str2, @Query("lc") String str3);

    @POST("youtube-upload.php")
    Single<UploadOutput> youTubeUpload(@Body UploadInput uploadInput);
}
