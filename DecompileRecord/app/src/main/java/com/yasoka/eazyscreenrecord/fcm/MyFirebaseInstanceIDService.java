package com.yasoka.eazyscreenrecord.fcm;

import android.content.pm.PackageManager.NameNotFoundException;
import com.ezscreenrecorder.server.ServerAPI;
import com.google.firebase.messaging.FirebaseMessagingService;
import p009io.reactivex.functions.Consumer;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";

    public void onNewToken(String str) {
        super.onNewToken(str);
        sendRegistrationToServer(str);
    }

    private void sendRegistrationToServer(String str) {
        try {
            ServerAPI.getInstance().anonymousUser(this).subscribe(new Consumer<String>() {
                public void accept(String str) throws Exception {
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                    th.printStackTrace();
                }
            });
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
