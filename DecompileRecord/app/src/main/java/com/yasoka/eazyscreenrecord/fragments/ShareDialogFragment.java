package com.yasoka.eazyscreenrecord.fragments;

import android.accounts.Account;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.content.FileProvider;
import android.support.p003v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.video.NewVideoPlayerActivity;
import com.ezscreenrecorder.youtubeupload.UploadService;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.Observer;
import p009io.reactivex.Single;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableObserver;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;

public class ShareDialogFragment extends DialogFragment {
    /* access modifiers changed from: private */
    public String video;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.video = getArguments().getString("video");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.share_video, viewGroup, false);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    /* access modifiers changed from: private */
    public boolean isNetworkConnected() {
        return ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        view.findViewById(C0793R.C0795id.btn_youtube).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!ShareDialogFragment.this.isNetworkConnected()) {
                    Toast.makeText(ShareDialogFragment.this.getContext(), C0793R.string.no_internet_connection, 1).show();
                } else if (AppUtils.containUploadFile(ShareDialogFragment.this.getContext())) {
                    Toast.makeText(ShareDialogFragment.this.getContext(), C0793R.string.upload_in_progress_one, 1).show();
                }
                File file = new File(ShareDialogFragment.this.video);
                final ImageVideoFile imageVideoFile = new ImageVideoFile();
                imageVideoFile.setPath(file.getAbsolutePath());
                imageVideoFile.setName(file.getName());
                imageVideoFile.setVideo(file.getAbsolutePath().endsWith(".mp4"));
                imageVideoFile.setFileSize(file.length());
                Builder builder = new Builder(ShareDialogFragment.this.getContext());
                View inflate = LayoutInflater.from(ShareDialogFragment.this.getContext()).inflate(C0793R.layout.you_tube_layout, null);
                final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(C0793R.C0795id.txt_lay_title);
                textInputLayout.getEditText().setText(imageVideoFile.getName());
                final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(C0793R.C0795id.txt_lay_desc);
                textInputLayout2.getEditText().setText(C0793R.string.you_tube_desc2);
                builder.setTitle((int) C0793R.string.youtube_details).setView(inflate).setPositiveButton((int) C0793R.string.youtube_upload, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        if (textInputLayout.getEditText().getText() == null || textInputLayout.getEditText().getText().toString().trim().length() == 0) {
                            Toast.makeText(ShareDialogFragment.this.getContext(), C0793R.string.id_enter_valid_title_error_msg, 1).show();
                        } else if (textInputLayout2.getEditText().getText() == null || textInputLayout2.getEditText().getText().toString().trim().length() == 0) {
                            Toast.makeText(ShareDialogFragment.this.getContext(), C0793R.string.id_enter_valid_desc_error_msg, 1).show();
                        } else {
                            final GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(ShareDialogFragment.this.getContext(), Arrays.asList(UploadService.SCOPES));
                            usingOAuth2.setBackOff(new ExponentialBackOff());
                            Observable.create(new ObservableOnSubscribe<String>() {
                                public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                                    String str = "youtube_account_email";
                                    if (defaultSharedPreferences.contains(str)) {
                                        ServerAPI.getInstance().addToFireBase(ShareDialogFragment.this.getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
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
                                    YoutubeAPI.getInstance().switchGoogleAccount(ShareDialogFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                        public void accept(String str) throws Exception {
                                            observableEmitter.onNext(str);
                                            ServerAPI.getInstance().addToFireBase(ShareDialogFragment.this.getContext(), "Got Email for Youtube Upload").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
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
                            }).subscribe((Observer<? super T>) new DisposableObserver<String>() {
                                public void onNext(String str) {
                                    ServerAPI.getInstance().addToFireBase(ShareDialogFragment.this.getContext(), "Upload to YouTube Starts").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                                        public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                                        }

                                        public void onError(Throwable th) {
                                            th.printStackTrace();
                                        }
                                    });
                                    usingOAuth2.setSelectedAccount(new Account(str, "com.google"));
                                    Intent intent = new Intent(ShareDialogFragment.this.getContext(), UploadService.class);
                                    intent.setData(Uri.fromFile(new File(imageVideoFile.getPath())));
                                    intent.putExtra(UploadService.ACCOUNT_KEY, str);
                                    intent.putExtra("name", textInputLayout.getEditText().getText().toString());
                                    String obj = textInputLayout2.getEditText().getText().toString();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(obj);
                                    sb.append("\n\n");
                                    sb.append(ShareDialogFragment.this.getContext().getString(C0793R.string.you_tube_desc1));
                                    intent.putExtra("desc", sb.toString());
                                    intent.putExtra(NewVideoPlayerActivity.KEY_EXTRA_VIDEO_DURATION, imageVideoFile.getDuration() / 1000);
                                    ShareDialogFragment.this.getContext().startService(intent);
                                    Toast.makeText(ShareDialogFragment.this.getContext(), C0793R.string.upload_started_my, 1).show();
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
                }).setNegativeButton((int) C0793R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
        view.findViewById(C0793R.C0795id.btn_share).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int i;
                String prefResolution = PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING);
                String str = "x";
                if (prefResolution.contains(str)) {
                    i = Integer.parseInt(prefResolution.split(str)[0]);
                } else {
                    i = Integer.parseInt(prefResolution);
                }
                if (i == 426) {
                    FloatingService.DISPLAY_HEIGHT = 426;
                    FloatingService.DISPLAY_WIDTH = 240;
                } else if (i == 640) {
                    FloatingService.DISPLAY_HEIGHT = 640;
                    FloatingService.DISPLAY_WIDTH = 360;
                } else if (i == 854) {
                    FloatingService.DISPLAY_HEIGHT = 854;
                    FloatingService.DISPLAY_WIDTH = 480;
                } else if (i == 1280) {
                    FloatingService.DISPLAY_HEIGHT = 1280;
                    FloatingService.DISPLAY_WIDTH = 720;
                } else if (i == 1920) {
                    FloatingService.DISPLAY_HEIGHT = 1920;
                    FloatingService.DISPLAY_WIDTH = 1080;
                }
                Single.timer(50, TimeUnit.MILLISECONDS).flatMapPublisher(new Function<Long, Publisher<String>>() {
                    public Publisher<String> apply(Long l) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<String>() {
                            public void subscribe(FlowableEmitter<String> flowableEmitter) throws Exception {
                                if (!defaultSharedPreferences.getBoolean("notifications_compress", false)) {
                                    flowableEmitter.onNext(ShareDialogFragment.this.video);
                                } else {
                                    flowableEmitter.onError(new Exception("Unable To Compress Video"));
                                }
                                flowableEmitter.onComplete();
                            }
                        }, BackpressureStrategy.BUFFER);
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<String>() {
                    public void onComplete() {
                    }

                    public void onNext(String str) {
                        MediaScannerConnection.scanFile(ShareDialogFragment.this.getContext(), new String[]{str}, null, new OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                Intent intent = new Intent("android.intent.action.SEND");
                                intent.setType("video/mp4");
                                intent.putExtra("android.intent.extra.TITLE", ShareDialogFragment.this.getString(C0793R.string.share_video));
                                intent.putExtra("android.intent.extra.SUBJECT", ShareDialogFragment.this.getString(C0793R.string.share_video));
                                intent.putExtra("android.intent.extra.TEXT", ShareDialogFragment.this.getString(C0793R.string.share_video_txt));
                                intent.addFlags(1);
                                FragmentActivity activity = ShareDialogFragment.this.getActivity();
                                StringBuilder sb = new StringBuilder();
                                sb.append(ShareDialogFragment.this.getActivity().getPackageName());
                                sb.append(".my.package.name.provider");
                                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(str)));
                                ShareDialogFragment.this.startActivity(Intent.createChooser(intent, ShareDialogFragment.this.getString(C0793R.string.share_video)));
                                ShareDialogFragment.this.addToFireBaseAnalytics(true);
                                AppUtils.addCount(ShareDialogFragment.this.getContext(), 5);
                            }
                        });
                    }

                    public void onError(Throwable th) {
                        th.printStackTrace();
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void addToFireBaseAnalytics(boolean z) {
        if (z) {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
        } else {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
        }
    }
}
