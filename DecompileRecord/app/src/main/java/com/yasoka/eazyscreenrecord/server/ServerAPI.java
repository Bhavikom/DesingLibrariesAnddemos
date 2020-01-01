package com.yasoka.eazyscreenrecord.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Observable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
//import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
/*import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.model.AppInputEventModel;
import com.ezscreenrecorder.model.AppOutputEventModel;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.model.AllImagesResponse;
import com.ezscreenrecorder.server.model.AllVideoOutput;
import com.ezscreenrecorder.server.model.AnonymousInput;
import com.ezscreenrecorder.server.model.AnonymousOutput;
import com.ezscreenrecorder.server.model.GameEventOutput.GameEventInputModel;
import com.ezscreenrecorder.server.model.GameEventOutput.GameEventOutputModel;
import com.ezscreenrecorder.server.model.GamesListModels.GamesMainModel;
import com.ezscreenrecorder.server.model.MoreVideosModels.MoreVideosDataModel;
import com.ezscreenrecorder.server.model.RecordingInput;
import com.ezscreenrecorder.server.model.RecordingOutput;
import com.ezscreenrecorder.server.model.SocialFeedModels.SocialFeedsMainModel;
import com.ezscreenrecorder.server.model.UploadInput;
import com.ezscreenrecorder.server.model.UploadOutput;
import com.ezscreenrecorder.server.model.UserRegInput;
import com.ezscreenrecorder.server.model.UserRegOutput;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideoMainModel;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.Logger;*/
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.iid.InstanceIdResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.activities.MainActivity;
import com.yasoka.eazyscreenrecord.model.AppInputEventModel;
import com.yasoka.eazyscreenrecord.model.AppOutputEventModel;
import com.yasoka.eazyscreenrecord.model.FirebaseDataDevice;
import com.yasoka.eazyscreenrecord.server.model.AllImagesResponse;
import com.yasoka.eazyscreenrecord.server.model.AllVideoOutput;
import com.yasoka.eazyscreenrecord.server.model.AnonymousInput;
import com.yasoka.eazyscreenrecord.server.model.AnonymousOutput;
import com.yasoka.eazyscreenrecord.server.model.GameEventOutput.GameEventInputModel;
import com.yasoka.eazyscreenrecord.server.model.GameEventOutput.GameEventOutputModel;
import com.yasoka.eazyscreenrecord.server.model.GamesListModels.GamesMainModel;
import com.yasoka.eazyscreenrecord.server.model.MoreVideosModels.MoreVideosDataModel;
import com.yasoka.eazyscreenrecord.server.model.RecordingInput;
import com.yasoka.eazyscreenrecord.server.model.RecordingOutput;
import com.yasoka.eazyscreenrecord.server.model.SocialFeedModels.SocialFeedsMainModel;
import com.yasoka.eazyscreenrecord.server.model.UploadInput;
import com.yasoka.eazyscreenrecord.server.model.UploadOutput;
import com.yasoka.eazyscreenrecord.server.model.UserRegInput;
import com.yasoka.eazyscreenrecord.server.model.UserRegOutput;
import com.yasoka.eazyscreenrecord.server.model.VideoMainScreenModels.VideoMainModel;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.ObservableSource;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.SingleSource;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.disposables.Disposable;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;*/

public class ServerAPI {
    public static final String ANONYMOUS_ID = "AnonymousID";
    public static final String BASE_URL_1 = "http://api.ezscreenrecorder.com/v2/";
    public static final String BASE_URL_2 = "http://ezs.apioz.com/v2/";
    public static final int BASE_URL_TYPE_APPSMARTZ = 1652;
    public static final int BASE_URL_TYPE_TWITCH = 1651;
    private static final String UNIQUE_ID = "UniqueID";
    public static final String USER_ID = "UserId";
    private static final ServerAPI ourInstance = new ServerAPI();
    /* access modifiers changed from: private */
    public GoogleSignInClient mGoogleApiClient;
    private Random random = new Random();
    private Retrofit retrofit;
    private String url;

    private ServerAPI() {
        setURL();
    }

    public static ServerAPI getInstance() {
        return ourInstance;
    }

    public void setURL() {
        String str = this.url;
        String str2 = BASE_URL_2;
        String str3 = BASE_URL_1;
        if (str == null) {
            if (this.random.nextBoolean()) {
                str2 = str3;
            }
            this.url = str2;
        } else if (str.equals(str3)) {
            this.url = str2;
        } else {
            this.url = str3;
        }
        this.retrofit = new Retrofit.Builder().baseUrl(this.url).addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(new OkHttpClient.Builder().
                readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder().addHeader("Token", "GfTDVBTdgvbTYhBDGnMHYDGBfTRTFGHB").build();
                ServerAPI serverAPI = ServerAPI.this;
                StringBuilder sb = new StringBuilder();
                sb.append("URL: ");
                sb.append(build.url().toString());
                serverAPI.Log(sb.toString());
                return chain.proceed(build);
            }
        }).build()).build();
    }

    public void Log(String str) {
        int i = 0;
        while (i <= str.length() / 2000) {
            int i2 = i * 2000;
            i++;
            int i3 = i * 2000;
            if (i3 > str.length()) {
                i3 = str.length();
            }
            Logger.getInstance().error(str.substring(i2, i3));
        }
    }

    public boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public APIReferences getApiReference() {
        return (APIReferences) this.retrofit.create(APIReferences.class);
    }

    public Single<String> anonymousUser(Context context) throws NameNotFoundException {
        final Context context2 = context;
        final SharedPreferences sharedPreferences = context2.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        if (!isNetworkConnected(context)) {
            return Single.error((Throwable) new Exception("No Internet"));
        }
        String token = FirebaseInstanceId.getInstance().getToken();
        String str = "";
        String str2 = ANONYMOUS_ID;
        if (token == null) {
            Log.e("APP", "Instance Id not generated");
        } else if (!sharedPreferences.contains(str2) && !sharedPreferences.contains(UNIQUE_ID)) {
            String simCountryIso = ((TelephonyManager) context2.getSystemService("phone")).getSimCountryIso();
            String str3 = Build.MANUFACTURER;
            String str4 = Build.MODEL;
            String valueOf = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            String valueOf2 = String.valueOf(VERSION.SDK_INT);
            final String uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString("UniqueId", uuid).apply();
            String language = Locale.getDefault().getLanguage();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    instanceIdResult.getToken();
                }
            });
            String token2 = FirebaseInstanceId.getInstance().getToken();
            if (simCountryIso == str) {
                simCountryIso = "NAA";
            }
            AnonymousInput anonymousInput = r6;
            AnonymousInput anonymousInput2 = new AnonymousInput(token2, simCountryIso, str3, str4, valueOf, String.valueOf(sharedPreferences.getInt("usageCount", 1)), valueOf2, uuid, language);
            return getInstance().getApiReference().anonymousUser(anonymousInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(new Function<AnonymousOutput, String>() {
                public String apply(AnonymousOutput anonymousOutput) throws Exception {
                    return String.valueOf(anonymousOutput.getAId());
                }
            }).doOnError(new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                    ServerAPI.this.setURL();
                }
            }).doOnSuccess(new Consumer<String>() {
                public void accept(String str) throws Exception {
                    sharedPreferences.edit().putString(ServerAPI.ANONYMOUS_ID, String.valueOf(str)).putString(ServerAPI.UNIQUE_ID, uuid).apply();
                    ServerAPI.getInstance().addToFireBase(context2, "Anonymouse Login Done").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                        public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                            System.out.println("sucess");
                        }

                        public void onError(Throwable th) {
                            th.printStackTrace();
                        }
                    });
                }
            });
        }
        return Single.just(sharedPreferences.getString(str2, str));
    }

    public Single<UserRegOutput> registerUser(Context context, String str, String str2, String str3) throws NameNotFoundException {
        Context context2 = context;
        if (!isNetworkConnected(context)) {
            return Single.error((Throwable) new Exception("No Internet"));
        }
        if (FirebaseInstanceId.getInstance().getToken() == null) {
            Log.e("APP", "Instance Id not generated");
            return Single.error((Throwable) new Exception("Token Issue"));
        }
        SharedPreferences sharedPreferences = context2.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        String simCountryIso = ((TelephonyManager) context2.getSystemService("phone")).getSimCountryIso();
        String str4 = Build.MANUFACTURER;
        String str5 = Build.MODEL;
        String valueOf = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        String valueOf2 = String.valueOf(VERSION.SDK_INT);
        String str6 = "";
        String string = sharedPreferences.getString("UniqueId", str6);
        String language = Locale.getDefault().getLanguage();
        if (simCountryIso == str6) {
            simCountryIso = "NAA";
        }
        UserRegInput userRegInput = new UserRegInput("", simCountryIso, str4, str5, valueOf, String.valueOf(sharedPreferences.getInt("usageCount", 1)), valueOf2, string, language, str, "", "", "", str3, str2, "", "", "");
        return getApiReference().registerationUser(userRegInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Observable<String> googleLogin(final Activity activity) throws NameNotFoundException {
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = GoogleSignIn.getClient(activity, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build());
        }
        return anonymousUser(activity).delay(500, TimeUnit.MILLISECONDS).doOnSuccess(new Consumer<String>() {
            public void accept(String str) throws Exception {
                Task signOut = ServerAPI.this.mGoogleApiClient.signOut();
                signOut.addOnSuccessListener(activity, (OnSuccessListener<? super TResult>) new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                    }
                });
                signOut.addOnFailureListener(activity, (OnFailureListener) new OnFailureListener() {
                    public void onFailure(@NonNull Exception exc) {
                    }
                });
            }
        }).flatMapObservable(new Function<String, ObservableSource<String>>() {
            public ObservableSource<String> apply(final String str) throws Exception {
                return RxActivityResult.m299on(activity).startIntent(ServerAPI.this.mGoogleApiClient.getSignInIntent()).flatMap(new Function<Result<Activity>, ObservableSource<GoogleSignInAccount>>() {
                    public ObservableSource<GoogleSignInAccount> apply(final Result<Activity> result) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<GoogleSignInAccount>() {
                            public void subscribe(ObservableEmitter<GoogleSignInAccount> observableEmitter) throws Exception {
                                if (result.resultCode() == -1) {
                                    observableEmitter.onNext(Auth.GoogleSignInApi.getSignInResultFromIntent(result.data()).getSignInAccount());
                                    ServerAPI.getInstance().addToFireBase(activity, "Google Login get Email").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
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
                        return ServerAPI.this.registerUser(activity, googleSignInAccount.getEmail(), googleSignInAccount.getDisplayName(), str).toObservable().map(new Function<UserRegOutput, String>() {
                            public String apply(UserRegOutput userRegOutput) throws Exception {
                                ServerAPI.getInstance().addToFireBase(activity, "User register to server").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                    public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                        System.out.println("sucess");
                                    }

                                    public void onError(Throwable th) {
                                        th.printStackTrace();
                                    }
                                });
                                String email = googleSignInAccount.getEmail();
                                activity.getSharedPreferences(MainActivity.SHARED_NAME, 0).edit().putString(ServerAPI.USER_ID, String.valueOf(userRegOutput.getUserId())).apply();
                                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("youtube_account_email", email).apply();
                                Task signOut = ServerAPI.this.mGoogleApiClient.signOut();
                                signOut.addOnSuccessListener(activity, (OnSuccessListener<? super TResult>) new OnSuccessListener<Void>() {
                                    public void onSuccess(Void voidR) {
                                    }
                                });
                                signOut.addOnFailureListener(activity, (OnFailureListener) new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                    }
                                });
                                return email;
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                }).subscribeOn(Schedulers.newThread());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<UploadOutput> uploadVideo(String str, String str2, String str3, String str4, String str5, String str6, String str7, long j, String str8) {
        UploadInput uploadInput = new UploadInput();
        uploadInput.setAId(str);
        uploadInput.setEmailId(str2);
        uploadInput.setDuration(String.valueOf(j));
        uploadInput.setUId(str3);
        uploadInput.setTitle(str8);
        uploadInput.setVideoId(str4);
        uploadInput.setMobileDtTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
        try {
            str5 = String.valueOf(Long.parseLong(str5) / 1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uploadInput.setBitrate(str5);
        uploadInput.setFps(str6);
        uploadInput.setResolution(str7);
        FirebaseEventsNewHelper.getInstance().sendVideoUploadSuccessEvent(str4);
        return getApiReference().youTubeUpload(uploadInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllImagesResponse> getAllImages(int i) {
        return getApiReference().getAllImages(String.valueOf(i), RecorderApplication.getCountryCode(), RecorderApplication.getDeviceLanguageISO3()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllImagesResponse> getMyImages(String str) {
        return getApiReference().getMyImages(str, RecorderApplication.getCountryCode(), RecorderApplication.getDeviceLanguageISO3()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllImagesResponse> getAllImagesTesting(int i) {
        APIReferences apiReference = getApiReference();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i);
        return apiReference.getAllImagesTesting(sb.toString()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllVideoOutput> getAllVideos(int i) {
        return getApiReference().videos(String.valueOf(i), RecorderApplication.getCountryCode(), RecorderApplication.getDeviceLanguageISO3()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllVideoOutput> getAllVideosTesting(int i) {
        APIReferences apiReference = getApiReference();
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("");
        return apiReference.testVideos(sb.toString()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<GamesMainModel> getGamesData() {
        return getApiReference().getGamesList(RecorderApplication.getCountryCode()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllVideoOutput> getMyVideosTesting(String str) {
        APIReferences apiReference = getApiReference();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("");
        return apiReference.myVideosTesting(sb.toString()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<SocialFeedsMainModel> getSocialFeeds() {
        return getApiReference().getSocialFeeds().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<VideoMainModel> getVideoMainScreen() {
        return getApiReference().getVideosMainScreen(RecorderApplication.getInstance().getApplicationContext().getSharedPreferences(MainActivity.SHARED_NAME, 0).getString(USER_ID, ""), RecorderApplication.getCountryCode()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<MoreVideosDataModel> getMoreVideo(String str, String str2, String str3) {
        return getApiReference().getMoreVideos(str, str2, str3, RecorderApplication.getCountryCode()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<AllVideoOutput> getMyVideos(String str, int i) {
        return getApiReference().myVideos(str, RecorderApplication.getCountryCode(), RecorderApplication.getDeviceLanguageISO3()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        });
    }

    public Single<FirebaseDataDevice> addToFireBase(Context context, String str) {
        return Single.just(str).map(new Function<String, FirebaseDataDevice>() {
            public FirebaseDataDevice apply(String str) throws Exception {
                return new FirebaseDataDevice(str);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public void sendGameRecordEvent(String str) {
        getApiReference().sendGameEvent(new GameEventInputModel(str, RecorderApplication.getCountryCode())).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        }).subscribe((SingleObserver<? super T>) new SingleObserver<GameEventOutputModel>() {
            public void onError(Throwable th) {
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(GameEventOutputModel gameEventOutputModel) {
            }
        });
    }

    public void deleteDB() {
        Single.create(new SingleOnSubscribe<FirebaseUser>() {
            public void subscribe(final SingleEmitter<FirebaseUser> singleEmitter) throws Exception {
                FirebaseAuth instance = FirebaseAuth.getInstance();
                if (instance.getCurrentUser() != null) {
                    singleEmitter.onSuccess(instance.getCurrentUser());
                } else {
                    instance.signInWithEmailAndPassword("admin@admin.com", "admin@admin123@123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                singleEmitter.onSuccess(((AuthResult) task.getResult()).getUser());
                            } else {
                                singleEmitter.onError(task.getException());
                            }
                        }
                    });
                }
            }
        }).flatMap(new Function<FirebaseUser, SingleSource<Boolean>>() {
            public SingleSource<Boolean> apply(FirebaseUser firebaseUser) throws Exception {
                return Single.create(new SingleOnSubscribe<Boolean>() {
                    public void subscribe(final SingleEmitter<Boolean> singleEmitter) throws Exception {
                        FirebaseDatabase.getInstance().getReference().child("Devices").removeValue(new CompletionListener() {
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    PrintStream printStream = System.out;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("EERRO-->");
                                    sb.append(databaseError.getDetails());
                                    printStream.println(sb.toString());
                                    singleEmitter.onError(databaseError.toException());
                                    return;
                                }
                                System.out.println("Deelte done");
                                singleEmitter.onSuccess(Boolean.valueOf(true));
                            }
                        });
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<Boolean>() {
            public void onSuccess(Boolean bool) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("SD-->");
                sb.append(bool);
                printStream.println(sb.toString());
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    public void recordingEvent(Context context, int i, String str, String str2) {
        File file;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_NAME, 0);
        RecordingInput recordingInput = new RecordingInput();
        String str3 = "";
        recordingInput.setUId(sharedPreferences.getString(USER_ID, str3));
        recordingInput.setRecordingPackage(str3);
        recordingInput.setRecorderType(str2);
        try {
            file = new File(str);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(((double) Math.round((float) (((file.length() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d);
                sb.append("MB");
                recordingInput.setSize(sb.toString());
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            file = null;
            e.printStackTrace();
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(context, Uri.fromFile(file));
                String extractMetadata = mediaMetadataRetriever.extractMetadata(19);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(extractMetadata);
                sb2.append("x");
                sb2.append(extractMetadata2);
                recordingInput.setResolution(sb2.toString());
                recordingInput.setDuration(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)))));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            recordingInput.setType(String.valueOf(i));
            recordingInput.setAId(sharedPreferences.getString(ANONYMOUS_ID, str3));
            recordingInput.setAppVersion(String.valueOf(RecorderApplication.getInstance().getPackageManager().getPackageInfo(RecorderApplication.getInstance().getPackageName(), 0).versionCode));
            recordingInput.setAppCounter(String.valueOf(sharedPreferences.getInt("usageCount", 1)));
            getApiReference().recordingData(recordingInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                    ServerAPI.this.setURL();
                }
            }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<RecordingOutput>() {
                public void onSuccess(RecordingOutput recordingOutput) {
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("RECORDING>>");
                    sb.append(recordingOutput.getMessage());
                    printStream.println(sb.toString());
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        }
        if ((i == 2 || i == 3) && file != null) {
            MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
            mediaMetadataRetriever2.setDataSource(context, Uri.fromFile(file));
            String extractMetadata3 = mediaMetadataRetriever2.extractMetadata(19);
            String extractMetadata22 = mediaMetadataRetriever2.extractMetadata(18);
            StringBuilder sb22 = new StringBuilder();
            sb22.append(extractMetadata3);
            sb22.append("x");
            sb22.append(extractMetadata22);
            recordingInput.setResolution(sb22.toString());
            recordingInput.setDuration(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(mediaMetadataRetriever2.extractMetadata(9)))));
        }
        recordingInput.setType(String.valueOf(i));
        recordingInput.setAId(sharedPreferences.getString(ANONYMOUS_ID, str3));
        try {
            recordingInput.setAppVersion(String.valueOf(RecorderApplication.getInstance().getPackageManager().getPackageInfo(RecorderApplication.getInstance().getPackageName(), 0).versionCode));
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            recordingInput.setAppCounter(String.valueOf(sharedPreferences.getInt("usageCount", 1)));
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        getApiReference().recordingData(recordingInput).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<RecordingOutput>() {
            public void onSuccess(RecordingOutput recordingOutput) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("RECORDING>>");
                sb.append(recordingOutput.getMessage());
                printStream.println(sb.toString());
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    public void recordAppEvent(AppInputEventModel appInputEventModel) {
        SharedPreferences sharedPreferences = RecorderApplication.getInstance().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        String str = "";
        appInputEventModel.setUId(sharedPreferences.getString(USER_ID, str));
        appInputEventModel.setAId(sharedPreferences.getString(ANONYMOUS_ID, str));
        try {
            appInputEventModel.setAppVersion(String.valueOf(RecorderApplication.getInstance().getPackageManager().getPackageInfo(RecorderApplication.getInstance().getPackageName(), 0).versionCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            appInputEventModel.setAppCounter(String.valueOf(sharedPreferences.getInt("usageCount", 1)));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        appInputEventModel.setDevCc(RecorderApplication.getCountryCode());
        appInputEventModel.setDevLc(RecorderApplication.getDeviceLanguageISO3());
        getApiReference().pushAppEvent(appInputEventModel).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable th) throws Exception {
                ServerAPI.this.setURL();
            }
        }).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<AppOutputEventModel>() {
            public void onSuccess(AppOutputEventModel appOutputEventModel) {
            }

            public void onError(Throwable th) {
                th.getMessage();
            }
        });
    }
}
