package com.yasoka.eazyscreenrecord.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.server.model.LiveFacebookModels.LiveFacebookPageModel;
import com.facebook.AccessToken;
import com.facebook.AccessToken.AccessTokenRefreshCallback;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.internal.ShareConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.schedulers.Schedulers;

public class LiveFacebookHelper {
    private static final LiveFacebookHelper ourInstance = new LiveFacebookHelper();
    private CallbackManager callbackManager;
    /* access modifiers changed from: private */
    public JSONObject dataResponseJSONObject;
    private LoginManager loginManager;
    /* access modifiers changed from: private */
    public LiveFacebookPageModel pageData;

    public interface OnLoginListener {
        void onFailed();

        void onStart();

        void onSuccess(AccessToken accessToken);
    }

    public interface OnLogoutListener {
        void onStart();

        void onSuccess();
    }

    public interface OnStreamListener {
        void onFailed();

        void onStart();

        void onSuccess(String str);
    }

    public static LiveFacebookHelper getInstance() {
        return ourInstance;
    }

    private LiveFacebookHelper() {
    }

    public void getAccessToken(Activity activity, OnLoginListener onLoginListener) {
        boolean z = false;
        if (!RecorderApplication.getInstance().isNetworkAvailable()) {
            Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), C0793R.string.id_no_internet_error_list_message, 0).show();
            return;
        }
        AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
        if (currentAccessToken != null && !currentAccessToken.isExpired() && currentAccessToken.getPermissions().contains("publish_video")) {
            onLoginListener.onSuccess(currentAccessToken);
            z = true;
        }
        if (!z) {
            loginFacebook(activity, onLoginListener);
        }
    }

    public void loginFacebook(Activity activity, final OnLoginListener onLoginListener) {
        if (!RecorderApplication.getInstance().isNetworkAvailable()) {
            Toast.makeText(RecorderApplication.getInstance().getApplicationContext(), C0793R.string.id_no_internet_error_list_message, 0).show();
            return;
        }
        if (this.callbackManager == null) {
            this.callbackManager = Factory.create();
        }
        if (this.loginManager == null) {
            this.loginManager = LoginManager.getInstance();
        }
        onLoginListener.onStart();
        this.loginManager.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                if (loginResult.getAccessToken() != null && loginResult.getAccessToken().getPermissions().contains("publish_video")) {
                    onLoginListener.onSuccess(loginResult.getAccessToken());
                }
            }

            public void onCancel() {
                onLoginListener.onFailed();
            }

            public void onError(FacebookException facebookException) {
                onLoginListener.onFailed();
            }
        });
        this.loginManager.logInWithPublishPermissions(activity, (Collection<String>) Arrays.asList(new String[]{"publish_video", "publish_pages", "manage_pages"}));
    }

    public void setActivityResult(int i, int i2, Intent intent) {
        CallbackManager callbackManager2 = this.callbackManager;
        if (callbackManager2 != null) {
            callbackManager2.onActivityResult(i, i2, intent);
        }
    }

    public void logoutFacebook(final OnLogoutListener onLogoutListener) {
        onLogoutListener.onStart();
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new Callback() {
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                onLogoutListener.onSuccess();
            }
        });
        graphRequest.executeAsync();
    }

    public Single<Boolean> endLiveStream(final int i) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            public void subscribe(final SingleEmitter<Boolean> singleEmitter) throws Exception {
                AccessToken.refreshCurrentAccessTokenAsync(new AccessTokenRefreshCallback() {
                    /* JADX WARNING: Removed duplicated region for block: B:11:0x0037  */
                    /* JADX WARNING: Removed duplicated region for block: B:23:0x0091  */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void OnTokenRefreshed(com.facebook.AccessToken r7) {
                        /*
                            r6 = this;
                            java.lang.String r0 = "end_live_video"
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this
                            com.ezscreenrecorder.utils.LiveFacebookHelper r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.this
                            org.json.JSONObject r1 = r1.dataResponseJSONObject
                            java.lang.String r2 = "UnknownErrorOccurred"
                            if (r1 == 0) goto L_0x009c
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this
                            com.ezscreenrecorder.utils.LiveFacebookHelper r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.this
                            org.json.JSONObject r1 = r1.dataResponseJSONObject
                            java.lang.String r3 = "id"
                            boolean r1 = r1.has(r3)
                            if (r1 == 0) goto L_0x002f
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this     // Catch:{ JSONException -> 0x002b }
                            com.ezscreenrecorder.utils.LiveFacebookHelper r1 = com.ezscreenrecorder.utils.LiveFacebookHelper.this     // Catch:{ JSONException -> 0x002b }
                            org.json.JSONObject r1 = r1.dataResponseJSONObject     // Catch:{ JSONException -> 0x002b }
                            java.lang.String r1 = r1.getString(r3)     // Catch:{ JSONException -> 0x002b }
                            goto L_0x0031
                        L_0x002b:
                            r1 = move-exception
                            r1.printStackTrace()
                        L_0x002f:
                            java.lang.String r1 = ""
                        L_0x0031:
                            boolean r3 = android.text.TextUtils.isEmpty(r1)
                            if (r3 != 0) goto L_0x0091
                            org.json.JSONObject r3 = new org.json.JSONObject
                            r3.<init>()
                            java.lang.String r4 = "true"
                            r3.put(r0, r4)     // Catch:{ JSONException -> 0x0042 }
                            goto L_0x0046
                        L_0x0042:
                            r4 = move-exception
                            r4.printStackTrace()
                        L_0x0046:
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r4 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this
                            int r4 = r2
                            r5 = 1350(0x546, float:1.892E-42)
                            if (r4 != r5) goto L_0x0062
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r4 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this
                            com.ezscreenrecorder.utils.LiveFacebookHelper r4 = com.ezscreenrecorder.utils.LiveFacebookHelper.this
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3 r5 = com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.this
                            com.ezscreenrecorder.utils.LiveFacebookHelper r5 = com.ezscreenrecorder.utils.LiveFacebookHelper.this
                            com.ezscreenrecorder.server.model.LiveFacebookModels.LiveFacebookPageModel r5 = r5.pageData
                            java.lang.String r5 = r5.getPageAccessToken()
                            com.facebook.AccessToken r7 = r4.getPageAccessToken(r5, r7)
                        L_0x0062:
                            boolean r0 = r3.has(r0)
                            if (r0 == 0) goto L_0x0086
                            java.lang.StringBuilder r0 = new java.lang.StringBuilder
                            r0.<init>()
                            java.lang.String r2 = "/"
                            r0.append(r2)
                            r0.append(r1)
                            java.lang.String r0 = r0.toString()
                            com.ezscreenrecorder.utils.LiveFacebookHelper$3$1$1 r1 = new com.ezscreenrecorder.utils.LiveFacebookHelper$3$1$1
                            r1.<init>()
                            com.facebook.GraphRequest r7 = com.facebook.GraphRequest.newPostRequest(r7, r0, r3, r1)
                            r7.executeAsync()
                            goto L_0x00a6
                        L_0x0086:
                            io.reactivex.SingleEmitter r7 = r2
                            java.lang.RuntimeException r0 = new java.lang.RuntimeException
                            r0.<init>(r2)
                            r7.onError(r0)
                            goto L_0x00a6
                        L_0x0091:
                            io.reactivex.SingleEmitter r7 = r2
                            java.lang.RuntimeException r0 = new java.lang.RuntimeException
                            r0.<init>(r2)
                            r7.onError(r0)
                            goto L_0x00a6
                        L_0x009c:
                            io.reactivex.SingleEmitter r7 = r2
                            java.lang.RuntimeException r0 = new java.lang.RuntimeException
                            r0.<init>(r2)
                            r7.onError(r0)
                        L_0x00a6:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.utils.LiveFacebookHelper.C14323.C14331.OnTokenRefreshed(com.facebook.AccessToken):void");
                    }

                    public void OnTokenRefreshFailed(FacebookException facebookException) {
                        singleEmitter.onError(facebookException);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<String> getFacebookStreamURLForPage(final LiveFacebookPageModel liveFacebookPageModel, final String str) {
        return Single.create(new SingleOnSubscribe<String>() {
            public void subscribe(final SingleEmitter<String> singleEmitter) throws Exception {
                AccessToken.refreshCurrentAccessTokenAsync(new AccessTokenRefreshCallback() {
                    public void OnTokenRefreshed(AccessToken accessToken) {
                        AccessToken accessToken2 = new AccessToken(liveFacebookPageModel.getPageAccessToken(), accessToken.getApplicationId(), accessToken.getUserId(), accessToken.getPermissions(), accessToken.getDeclinedPermissions(), accessToken.getExpiredPermissions(), accessToken.getSource(), accessToken.getExpires(), accessToken.getLastRefresh(), accessToken.getDataAccessExpirationTime());
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("description", str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("/");
                        sb.append(liveFacebookPageModel.getPageId());
                        sb.append("/live_videos");
                        GraphRequest.newPostRequest(accessToken2, sb.toString(), jSONObject, new Callback() {
                            public void onCompleted(GraphResponse graphResponse) {
                                LiveFacebookHelper.this.pageData = liveFacebookPageModel;
                                LiveFacebookHelper.this.dataResponseJSONObject = graphResponse.getJSONObject();
                                String rawResponse = graphResponse.getRawResponse();
                                if (!TextUtils.isEmpty(rawResponse)) {
                                    singleEmitter.onSuccess(rawResponse);
                                } else {
                                    singleEmitter.onError(new RuntimeException("DataNotFound"));
                                }
                                singleEmitter.onSuccess(rawResponse);
                            }
                        }).executeAsync();
                    }

                    public void OnTokenRefreshFailed(FacebookException facebookException) {
                        singleEmitter.onError(facebookException);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public void getFacebookStreamURLForTimeline(final OnStreamListener onStreamListener, final String str) {
        onStreamListener.onStart();
        AccessToken.refreshCurrentAccessTokenAsync(new AccessTokenRefreshCallback() {
            public void OnTokenRefreshed(AccessToken accessToken) {
                if (accessToken.getPermissions().contains("publish_video")) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("value", "EVERYONE");
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("description", str);
                        jSONObject2.put(ShareConstants.WEB_DIALOG_PARAM_PRIVACY, jSONObject.toString());
                        StringBuilder sb = new StringBuilder();
                        sb.append("/");
                        sb.append(accessToken.getUserId());
                        sb.append("/live_videos");
                        GraphRequest.newPostRequest(accessToken, sb.toString(), jSONObject2, new Callback() {
                            public void onCompleted(GraphResponse graphResponse) {
                                LiveFacebookHelper.this.dataResponseJSONObject = graphResponse.getJSONObject();
                                String rawResponse = graphResponse.getRawResponse();
                                if (!TextUtils.isEmpty(rawResponse)) {
                                    onStreamListener.onSuccess(rawResponse);
                                } else {
                                    onStreamListener.onFailed();
                                }
                            }
                        }).executeAsync();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onStreamListener.onFailed();
                    }
                } else {
                    onStreamListener.onFailed();
                }
            }

            public void OnTokenRefreshFailed(FacebookException facebookException) {
                onStreamListener.onFailed();
            }
        });
    }

    public Single<List<LiveFacebookPageModel>> getFacebookPagesList() {
        return Single.create(new SingleOnSubscribe<List<LiveFacebookPageModel>>() {
            public void subscribe(final SingleEmitter<List<LiveFacebookPageModel>> singleEmitter) throws Exception {
                AccessToken.refreshCurrentAccessTokenAsync(new AccessTokenRefreshCallback() {
                    public void OnTokenRefreshed(AccessToken accessToken) {
                        GraphRequest newMeRequest = GraphRequest.newMeRequest(accessToken, new GraphJSONObjectCallback() {
                            public void onCompleted(JSONObject jSONObject, GraphResponse graphResponse) {
                                String str = ShareConstants.WEB_DIALOG_PARAM_DATA;
                                String str2 = "accounts";
                                try {
                                    ArrayList arrayList = new ArrayList();
                                    if (jSONObject != null && jSONObject.has(str2) && jSONObject.getJSONObject(str2).has(str)) {
                                        JSONArray jSONArray = jSONObject.getJSONObject(str2).getJSONArray(str);
                                        for (int i = 0; i < jSONArray.length(); i++) {
                                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                            LiveFacebookPageModel liveFacebookPageModel = new LiveFacebookPageModel();
                                            liveFacebookPageModel.setPageId(jSONObject2.getString("id"));
                                            liveFacebookPageModel.setPageAccessToken(jSONObject2.getString("access_token"));
                                            liveFacebookPageModel.setPageName(jSONObject2.getString("name"));
                                            liveFacebookPageModel.setPageCategory(jSONObject2.getString("category"));
                                            arrayList.add(liveFacebookPageModel);
                                        }
                                    }
                                    if (arrayList.size() > 0) {
                                        singleEmitter.onSuccess(arrayList);
                                    } else {
                                        singleEmitter.onError(new RuntimeException("NoPageFound"));
                                    }
                                } catch (JSONException e) {
                                    singleEmitter.onError(e);
                                }
                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString(GraphRequest.FIELDS_PARAM, "id,name,accounts");
                        newMeRequest.setParameters(bundle);
                        newMeRequest.executeAsync();
                    }

                    public void OnTokenRefreshFailed(FacebookException facebookException) {
                        singleEmitter.onError(new RuntimeException("AccessTokenError"));
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public AccessToken getPageAccessToken(String str, AccessToken accessToken) {
        AccessToken accessToken2 = new AccessToken(str, accessToken.getApplicationId(), accessToken.getUserId(), accessToken.getPermissions(), accessToken.getDeclinedPermissions(), accessToken.getExpiredPermissions(), accessToken.getSource(), accessToken.getExpires(), accessToken.getLastRefresh(), accessToken.getDataAccessExpirationTime());
        return accessToken2;
    }
}
