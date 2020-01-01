package com.yasoka.eazyscreenrecord.server;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
/*import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.app.AppCompatActivity;*/
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.live_youtube.LiveYoutubeStartActivity;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.ezscreenrecorder.server.model.UserRegOutput;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.Logger;
import com.ezscreenrecorder.utils.PreferenceHelper;*/
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
/*import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.LiveBroadcasts.Bind;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamSnippet;
import com.google.api.services.youtube.model.MonitorStreamInfo;*/
import com.jakewharton.retrofit2.adapter.rxjava2.Result;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.activities.live_youtube.LiveYoutubeStartActivity;
import com.yasoka.eazyscreenrecord.model.FirebaseDataDevice;
import com.yasoka.eazyscreenrecord.server.model.LiveYoutubeModel.LiveYoutubeFinalModel;
import com.yasoka.eazyscreenrecord.server.model.UserRegOutput;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.Logger;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
/*import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.ObservableSource;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;*/

public class YoutubeAPI {
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE};
    private static final YoutubeAPI ourInstance = new YoutubeAPI();
    /* access modifiers changed from: private */
    public static YouTube youtube;
    private GoogleAccountCredential credential;
    private GoogleSignInAccount googleSignInAccount;
    /* access modifiers changed from: private */
    public GoogleSignInClient googleSignInClient;
    /* access modifiers changed from: private */
    public LiveYoutubeFinalModel youtubeFinalModel;

    public static YoutubeAPI getInstance() {
        return ourInstance;
    }

    private YoutubeAPI() {
    }

    public GoogleSignInAccount getGoogleAccount(AppCompatActivity appCompatActivity) {
        this.googleSignInAccount = GoogleSignIn.getLastSignedInAccount(appCompatActivity);
        return this.googleSignInAccount;
    }

    public Observable<Boolean> signInGoogleAccount(final AppCompatActivity appCompatActivity) throws NameNotFoundException {
        if (this.googleSignInClient == null) {
            this.googleSignInClient = GoogleSignIn.getClient((Activity) appCompatActivity, new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build());
        }
        return ServerAPI.getInstance().anonymousUser(appCompatActivity).delay(100, TimeUnit.MILLISECONDS).flatMapObservable(new Function<String, ObservableSource<Boolean>>() {
            public ObservableSource<Boolean> apply(final String str) throws Exception {
                return RxActivityResult.m299on(appCompatActivity).startIntent(YoutubeAPI.this.googleSignInClient.getSignInIntent()).flatMap(new Function<Result<AppCompatActivity>, ObservableSource<GoogleSignInAccount>>() {
                    public ObservableSource<GoogleSignInAccount> apply(final Result<AppCompatActivity> result) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<GoogleSignInAccount>() {
                            public void subscribe(ObservableEmitter<GoogleSignInAccount> observableEmitter) throws Exception {
                                if (result.resultCode() == -1) {
                                    try {
                                        observableEmitter.onNext((GoogleSignInAccount) GoogleSignIn.getSignedInAccountFromIntent(result.data()).getResult(ApiException.class));
                                    } catch (ApiException e) {
                                        observableEmitter.onError(e);
                                    }
                                } else {
                                    try {
                                        observableEmitter.onError(new Exception("Unable to Login"));
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }).flatMap(new Function<GoogleSignInAccount, ObservableSource<Boolean>>() {
                    public ObservableSource<Boolean> apply(final GoogleSignInAccount googleSignInAccount) throws Exception {
                        return ServerAPI.getInstance().registerUser(appCompatActivity, googleSignInAccount.getEmail(), googleSignInAccount.getDisplayName(), str).toObservable().map(new Function<UserRegOutput, Boolean>() {
                            public Boolean apply(UserRegOutput userRegOutput) throws Exception {
                                String email = googleSignInAccount.getEmail();
                                PreferenceHelper.getInstance().setPrefYoutubeEmailId(email);
                                PreferenceHelper.getInstance().setPrefUserId(email);
                                return Boolean.valueOf(true);
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                }).subscribeOn(Schedulers.newThread());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                th.printStackTrace();
                ServerAPI.getInstance().setURL();
            }
        });
    }

    public Single<LiveYoutubeFinalModel> createYoutubeLiveEvent(final AppCompatActivity appCompatActivity, final String str, final String str2) {
        return Single.create(new SingleOnSubscribe<LiveYoutubeFinalModel>() {
            public void subscribe(SingleEmitter<LiveYoutubeFinalModel> singleEmitter) throws Exception {
                try {
                    LiveYoutubeFinalModel access$100 = YoutubeAPI.this.startStream(appCompatActivity, str, str2);
                    if (access$100 != null) {
                        singleEmitter.onSuccess(access$100);
                    } else {
                        singleEmitter.onError(new RuntimeException("Some unknown error occurred"));
                    }
                } catch (UserRecoverableAuthIOException e) {
                    AppCompatActivity appCompatActivity = appCompatActivity;
                    if (appCompatActivity != null && !appCompatActivity.isFinishing()) {
                        appCompatActivity.startActivityForResult(e.getIntent(), LiveYoutubeStartActivity.RESPONSE_CODE_MANAGE_YOUTUBE_ACCOUNT_PERMISSION);
                    }
                    singleEmitter.onError(e);
                } catch (Exception e2) {
                    singleEmitter.onError(e2);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public LiveYoutubeFinalModel getYoutubeFinalModel() {
        return this.youtubeFinalModel;
    }

    public void setYoutubeFinalModel(LiveYoutubeFinalModel liveYoutubeFinalModel) {
        this.youtubeFinalModel = liveYoutubeFinalModel;
    }

    public void startLiveYoutube() throws Exception {
        if (this.youtubeFinalModel != null) {
            Single.create(new SingleOnSubscribe<Boolean>() {
                public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                    try {
                        Thread.sleep(5000);
                        YoutubeAPI.youtube.liveBroadcasts().transition("live", YoutubeAPI.this.youtubeFinalModel.getBroadcastID(), "status").execute();
                        singleEmitter.onSuccess(Boolean.valueOf(true));
                    } catch (Exception e) {
                        singleEmitter.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                public void onSuccess(Boolean bool) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
            return;
        }
        throw new IllegalAccessException("LiveYoutubeFinalModel can't be null.");
    }

    public void endLiveYoutube() throws Exception {
        if (this.youtubeFinalModel != null) {
            Single.create(new SingleOnSubscribe<Boolean>() {
                public void subscribe(SingleEmitter<Boolean> singleEmitter) throws Exception {
                    try {
                        Thread.sleep(5000);
                        YoutubeAPI.youtube.liveBroadcasts().transition("complete", YoutubeAPI.this.youtubeFinalModel.getBroadcastID(), "status").execute();
                        singleEmitter.onSuccess(Boolean.valueOf(true));
                    } catch (Exception e) {
                        singleEmitter.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
                public void onSuccess(Boolean bool) {
                    if (bool.booleanValue()) {
                        YoutubeAPI.this.youtubeFinalModel = null;
                    }
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
            return;
        }
        throw new IllegalAccessException("LiveYoutubeFinalModel can't be null.");
    }

    /* access modifiers changed from: private */
    public LiveYoutubeFinalModel startStream(AppCompatActivity appCompatActivity, String str, String str2) throws Exception {
        int i;
        AppCompatActivity appCompatActivity2 = appCompatActivity;
        String str3 = str;
        LiveYoutubeFinalModel liveYoutubeFinalModel = new LiveYoutubeFinalModel();
        this.credential = GoogleAccountCredential.usingOAuth2(appCompatActivity2, Arrays.asList(SCOPES));
        Logger instance = Logger.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("YOUTUBE_LIVE: ");
        sb.append(getGoogleAccount(appCompatActivity).getEmail());
        instance.error(sb.toString());
        this.credential.setSelectedAccountName(getGoogleAccount(appCompatActivity).getEmail());
        this.credential.setBackOff(new ExponentialBackOff());
        youtube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), this.credential).setApplicationName(appCompatActivity2.getString(R.string.app_name)).build();
        youtube.liveBroadcasts().list("id,snippet,contentDetails,status").setBroadcastType("all").setMine(Boolean.valueOf(true)).execute();
        PrintStream printStream = System.out;
        StringBuilder sb2 = new StringBuilder();
        String str4 = "You chose ";
        sb2.append(str4);
        sb2.append(str3);
        sb2.append(" for broadcast title.");
        printStream.println(sb2.toString());
        LiveBroadcastSnippet liveBroadcastSnippet = new LiveBroadcastSnippet();
        liveBroadcastSnippet.setTitle(str3);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long j = 86400000 + timeInMillis;
        liveBroadcastSnippet.setScheduledStartTime(new DateTime(timeInMillis));
        liveBroadcastSnippet.setScheduledEndTime(new DateTime(j));
        LiveBroadcastStatus liveBroadcastStatus = new LiveBroadcastStatus();
        liveBroadcastStatus.setPrivacyStatus(str2.toLowerCase());
        LiveBroadcastContentDetails liveBroadcastContentDetails = new LiveBroadcastContentDetails();
        char c = 0;
        liveBroadcastContentDetails.setMonitorStream(new MonitorStreamInfo().setEnableMonitorStream(Boolean.valueOf(false)));
        LiveBroadcast liveBroadcast = new LiveBroadcast();
        liveBroadcast.setKind("youtube#liveBroadcast");
        liveBroadcast.setSnippet(liveBroadcastSnippet);
        liveBroadcast.setStatus(liveBroadcastStatus);
        liveBroadcast.setContentDetails(liveBroadcastContentDetails);
        LiveBroadcast liveBroadcast2 = (LiveBroadcast) youtube.liveBroadcasts().insert("snippet,contentDetails,status", liveBroadcast).execute();
        System.out.println("\n================== Returned Broadcast ==================\n");
        PrintStream printStream2 = System.out;
        StringBuilder sb3 = new StringBuilder();
        String str5 = "  - Id: ";
        sb3.append(str5);
        sb3.append(liveBroadcast2.getId());
        printStream2.println(sb3.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb4 = new StringBuilder();
        String str6 = "  - Title: ";
        sb4.append(str6);
        sb4.append(liveBroadcast2.getSnippet().getTitle());
        printStream3.println(sb4.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb5 = new StringBuilder();
        String str7 = "  - Description: ";
        sb5.append(str7);
        sb5.append(liveBroadcast2.getSnippet().getDescription());
        printStream4.println(sb5.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb6 = new StringBuilder();
        String str8 = "  - Published At: ";
        sb6.append(str8);
        sb6.append(liveBroadcast2.getSnippet().getPublishedAt());
        printStream5.println(sb6.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("  - Scheduled Start Time: ");
        sb7.append(liveBroadcast2.getSnippet().getScheduledStartTime());
        printStream6.println(sb7.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("  - Scheduled End Time: ");
        sb8.append(liveBroadcast2.getSnippet().getScheduledEndTime());
        printStream7.println(sb8.toString());
        liveYoutubeFinalModel.setBroadcastID(liveBroadcast2.getId());
        liveYoutubeFinalModel.setBroadcastTitle(liveBroadcast2.getSnippet().getTitle());
        PrintStream printStream8 = System.out;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(str4);
        String str9 = "Stream Title";
        sb9.append(str9);
        sb9.append(" for stream title.");
        printStream8.println(sb9.toString());
        LiveStreamSnippet liveStreamSnippet = new LiveStreamSnippet();
        liveStreamSnippet.setTitle(str9);
        String str10 = "x";
        if (!PreferenceHelper.getInstance().hasPrefResolution(Constants.TYPE_PREF_RESOLUTION_YOUTUBE)) {
            String[] stringArray = appCompatActivity.getResources().getStringArray(C0793R.array.pref_resolution_list_titles);
            Point point = new Point();
            appCompatActivity.getWindowManager().getDefaultDisplay().getRealSize(point);
            int i2 = point.y;
            int i3 = point.x;
            ArrayList arrayList = new ArrayList();
            int length = stringArray.length;
            int i4 = 0;
            while (i4 < length) {
                String str11 = stringArray[i4];
                String str12 = str11.split(str10)[c];
                String str13 = str11.split(str10)[1];
                if (i2 > i3) {
                    if (Integer.parseInt(str12) <= i2 && Integer.parseInt(str13) <= i3) {
                        arrayList.add(str11);
                    }
                } else if (Integer.parseInt(str13) <= i2 && Integer.parseInt(str12) <= i3) {
                    arrayList.add(str11);
                }
                i4++;
                c = 0;
            }
            String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
            PreferenceHelper instance2 = PreferenceHelper.getInstance();
            String str14 = strArr[0];
            i = Constants.TYPE_PREF_RESOLUTION_YOUTUBE;
            instance2.setPrefResolution(Constants.TYPE_PREF_RESOLUTION_YOUTUBE, str14);
        } else {
            i = Constants.TYPE_PREF_RESOLUTION_YOUTUBE;
        }
        String str15 = PreferenceHelper.getInstance().getPrefResolution(i).split(str10)[1];
        CdnSettings cdnSettings = new CdnSettings();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(str15);
        sb10.append("p");
        cdnSettings.setFormat(sb10.toString());
        cdnSettings.setIngestionType("rtmp");
        LiveStream liveStream = new LiveStream();
        liveStream.setKind("youtube#liveStream");
        liveStream.setSnippet(liveStreamSnippet);
        liveStream.setCdn(cdnSettings);
        LiveStream liveStream2 = (LiveStream) youtube.liveStreams().insert("snippet,cdn", liveStream).execute();
        System.out.println("\n================== Returned Stream ==================\n");
        PrintStream printStream9 = System.out;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(str5);
        sb11.append(liveStream2.getId());
        printStream9.println(sb11.toString());
        PrintStream printStream10 = System.out;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(str6);
        sb12.append(liveStream2.getSnippet().getTitle());
        printStream10.println(sb12.toString());
        PrintStream printStream11 = System.out;
        StringBuilder sb13 = new StringBuilder();
        sb13.append(str7);
        sb13.append(liveStream2.getSnippet().getDescription());
        printStream11.println(sb13.toString());
        PrintStream printStream12 = System.out;
        StringBuilder sb14 = new StringBuilder();
        sb14.append(str8);
        sb14.append(liveStream2.getSnippet().getPublishedAt());
        printStream12.println(sb14.toString());
        PrintStream printStream13 = System.out;
        StringBuilder sb15 = new StringBuilder();
        sb15.append("  - CDN_INFO_STREAM_NAME: ");
        sb15.append(liveStream2.getCdn().getIngestionInfo().getStreamName());
        printStream13.println(sb15.toString());
        Bind bind = youtube.liveBroadcasts().bind(liveBroadcast2.getId(), "id,contentDetails");
        bind.setStreamId(liveStream2.getId());
        LiveBroadcast liveBroadcast3 = (LiveBroadcast) bind.execute();
        System.out.println("\n================== Returned Bound Broadcast ==================\n");
        PrintStream printStream14 = System.out;
        StringBuilder sb16 = new StringBuilder();
        sb16.append("  - Broadcast Id: ");
        sb16.append(liveBroadcast3.getId());
        printStream14.println(sb16.toString());
        PrintStream printStream15 = System.out;
        StringBuilder sb17 = new StringBuilder();
        sb17.append("  - Bound Stream Id: ");
        sb17.append(liveBroadcast3.getContentDetails().getBoundStreamId());
        printStream15.println(sb17.toString());
        liveYoutubeFinalModel.setStreamID(liveStream2.getId());
        liveYoutubeFinalModel.setStreamTitle(liveStream2.getSnippet().getTitle());
        liveYoutubeFinalModel.setStreamCDNKey(liveStream2.getCdn().getIngestionInfo().getStreamName());
        return liveYoutubeFinalModel;
    }

    public Single<Boolean> logOutFromAccount(final FragmentActivity fragmentActivity) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            public void subscribe(final SingleEmitter<Boolean> singleEmitter) throws Exception {
                if (YoutubeAPI.this.googleSignInClient == null) {
                    YoutubeAPI.this.googleSignInClient = GoogleSignIn.getClient((Activity) fragmentActivity, new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build());
                }
                final Task signOut = YoutubeAPI.this.googleSignInClient.signOut();
                signOut.addOnSuccessListener((Activity) fragmentActivity, (OnSuccessListener<? super TResult>) new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                        if (signOut.isSuccessful()) {
                            PreferenceHelper.getInstance().removePrefYoutubeEmailId();
                            PreferenceHelper.getInstance().removePrefUserId();
                            singleEmitter.onSuccess(Boolean.valueOf(true));
                            return;
                        }
                        singleEmitter.onError(new IllegalStateException("UnableToSignOut"));
                    }
                });
                signOut.addOnFailureListener((Activity) fragmentActivity, (OnFailureListener) new OnFailureListener() {
                    public void onFailure(@NonNull Exception exc) {
                        singleEmitter.onError(exc);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> switchGoogleAccount(final FragmentActivity fragmentActivity) throws NameNotFoundException {
        if (this.googleSignInClient == null) {
            this.googleSignInClient = GoogleSignIn.getClient((Activity) fragmentActivity, new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build());
        }
        return ServerAPI.getInstance().anonymousUser(fragmentActivity).delay(500, TimeUnit.MILLISECONDS).doOnSuccess(new Consumer<String>() {
            public void accept(String str) throws Exception {
                Task signOut = YoutubeAPI.this.googleSignInClient.signOut();
                signOut.addOnSuccessListener((Activity) fragmentActivity, (OnSuccessListener<? super TResult>) new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                    }
                });
                signOut.addOnFailureListener((Activity) fragmentActivity, (OnFailureListener) new OnFailureListener() {
                    public void onFailure(@NonNull Exception exc) {
                    }
                });
            }
        }).flatMapObservable(new Function<String, ObservableSource<String>>() {
            public ObservableSource<String> apply(final String str) throws Exception {
                return RxActivityResult.m299on(fragmentActivity).startIntent(YoutubeAPI.this.googleSignInClient.getSignInIntent()).flatMap(new Function<Result<FragmentActivity>, ObservableSource<GoogleSignInAccount>>() {
                    public ObservableSource<GoogleSignInAccount> apply(final Result<FragmentActivity> result) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<GoogleSignInAccount>() {
                            public void subscribe(ObservableEmitter<GoogleSignInAccount> observableEmitter) throws Exception {
                                if (result.resultCode() == -1) {
                                    observableEmitter.onNext(Auth.GoogleSignInApi.getSignInResultFromIntent(result.data()).getSignInAccount());
                                    ServerAPI.getInstance().addToFireBase(fragmentActivity, "Google Login get Email").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                        public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                            System.out.println("sucess");
                                        }

                                        public void onError(Throwable th) {
                                            th.printStackTrace();
                                        }
                                    });
                                    return;
                                }
                                try {
                                    observableEmitter.onError(new Exception("Unable to Login"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                }).flatMap(new Function<GoogleSignInAccount, ObservableSource<String>>() {
                    public ObservableSource<String> apply(final GoogleSignInAccount googleSignInAccount) throws Exception {
                        return ServerAPI.getInstance().registerUser(fragmentActivity, googleSignInAccount.getEmail(), googleSignInAccount.getDisplayName(), str).toObservable().map(new Function<UserRegOutput, String>() {
                            public String apply(UserRegOutput userRegOutput) throws Exception {
                                ServerAPI.getInstance().addToFireBase(fragmentActivity, "User register to server").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                    public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                        System.out.println("sucess");
                                    }

                                    public void onError(Throwable th) {
                                        th.printStackTrace();
                                    }
                                });
                                String email = googleSignInAccount.getEmail();
                                PreferenceHelper.getInstance().setPrefUserId(String.valueOf(userRegOutput.getUserId()));
                                PreferenceManager.getDefaultSharedPreferences(fragmentActivity).edit().putString("youtube_account_email", email).apply();
                                return email;
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                }).subscribeOn(Schedulers.newThread());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.getInstance().setURL();
            }
        });
    }
}
