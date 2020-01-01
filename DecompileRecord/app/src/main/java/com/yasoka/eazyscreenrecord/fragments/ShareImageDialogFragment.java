package com.yasoka.eazyscreenrecord.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.app.FragmentActivity;
import android.support.p000v4.content.FileProvider;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.MainActivity;
import com.ezscreenrecorder.imgupload.ImageUploadService;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.YoutubeAPI;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import java.io.File;
import p009io.reactivex.Observable;
import p009io.reactivex.ObservableEmitter;
import p009io.reactivex.ObservableOnSubscribe;
import p009io.reactivex.Observer;
import p009io.reactivex.functions.Consumer;
import p009io.reactivex.observers.DisposableObserver;

public class ShareImageDialogFragment extends DialogFragment {
    /* access modifiers changed from: private */
    public SharedPreferences prefs;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public String video;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.video = getArguments().getString("video");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.share_image, viewGroup, false);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_NAME, 0);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        TextView textView = (TextView) view.findViewById(C0793R.C0795id.btn_terms);
        SpannableString spannableString = new SpannableString(getString(C0793R.string.terms_image_cloud));
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        textView.setText(spannableString);
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://appscreenrecorder.com/privacy-policy"));
                ShareImageDialogFragment.this.startActivity(intent);
            }
        });
        view.findViewById(C0793R.C0795id.btn_youtube).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    public void subscribe(final ObservableEmitter<String> observableEmitter) throws Exception {
                        String str = "youtube_account_email";
                        if (ShareImageDialogFragment.this.prefs.contains(str)) {
                            observableEmitter.onNext(ShareImageDialogFragment.this.prefs.getString(str, ""));
                            observableEmitter.onComplete();
                            return;
                        }
                        try {
                            YoutubeAPI.getInstance().switchGoogleAccount(ShareImageDialogFragment.this.getActivity()).subscribe(new Consumer<String>() {
                                public void accept(String str) throws Exception {
                                    observableEmitter.onNext(str);
                                    observableEmitter.onComplete();
                                }
                            }, new Consumer<Throwable>() {
                                public void accept(Throwable th) throws Exception {
                                    th.printStackTrace();
                                    observableEmitter.onError(th);
                                }
                            });
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                            observableEmitter.onError(e);
                        }
                    }
                }).subscribe((Observer<? super T>) new DisposableObserver<String>() {
                    public void onComplete() {
                    }

                    public void onNext(String str) {
                        Intent intent = new Intent(ShareImageDialogFragment.this.getContext(), ImageUploadService.class);
                        intent.putExtra("file_path", ShareImageDialogFragment.this.video);
                        String str2 = "";
                        intent.putExtra("aId", ShareImageDialogFragment.this.sharedPreferences.getString(ServerAPI.ANONYMOUS_ID, str2));
                        intent.putExtra("uId", ShareImageDialogFragment.this.sharedPreferences.getString(ServerAPI.USER_ID, str2));
                        intent.putExtra("email", str);
                        ShareImageDialogFragment.this.getActivity().startService(intent);
                        ShareImageDialogFragment.this.dismiss();
                    }

                    public void onError(Throwable th) {
                        th.printStackTrace();
                    }
                });
            }
        });
        view.findViewById(C0793R.C0795id.btn_share).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShareImageDialogFragment.this.isAdded()) {
                    AppUtils.addCount(ShareImageDialogFragment.this.getContext(), 4);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/jpeg");
                    intent.putExtra("android.intent.extra.SUBJECT", ShareImageDialogFragment.this.getString(C0793R.string.share_image));
                    intent.putExtra("android.intent.extra.TEXT", ShareImageDialogFragment.this.getString(C0793R.string.share_image_txt));
                    intent.addFlags(1);
                    FragmentActivity activity = ShareImageDialogFragment.this.getActivity();
                    StringBuilder sb = new StringBuilder();
                    sb.append(ShareImageDialogFragment.this.getActivity().getPackageName());
                    sb.append(".my.package.name.provider");
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(activity, sb.toString(), new File(ShareImageDialogFragment.this.video)));
                    ShareImageDialogFragment shareImageDialogFragment = ShareImageDialogFragment.this;
                    shareImageDialogFragment.startActivity(Intent.createChooser(intent, shareImageDialogFragment.getString(C0793R.string.share_image)));
                    ShareImageDialogFragment.this.addToFirebaseAnalytics(false);
                    ShareImageDialogFragment.this.dismiss();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void addToFirebaseAnalytics(boolean z) {
        if (z) {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Video");
        } else {
            FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
        }
    }
}
