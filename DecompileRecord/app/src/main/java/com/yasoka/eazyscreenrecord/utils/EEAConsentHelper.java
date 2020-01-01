package com.yasoka.eazyscreenrecord.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
/*import com.facebook.appevents.AppEventsConstants;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentForm.Builder;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;*/
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;

public class EEAConsentHelper {
    public static final int ADTYPE_NON_PERSONALISED = 1;
    public static final int ADTYPE_NOT_NEEDED = 3;
    public static final int ADTYPE_PERSONALISED = 2;
    public static final int ADTYPE_UNKNOWN = 0;
    private static final String DEBUG_DEVICE_ID = "613631D03ECDF01A0E7AC9EDEF3B072F";
    private static final boolean IS_TESTING_MODE = false;
    private static final String PREF_KEY_USER_EEA_CONSENT_TYPE = "scr_user_eea_consent_type";
    private static final String PREF_KEY_USER_EEA_LOCATION = "scr_user_eea_location";
    private static final String PREF_KEY_USER_USER_CONSENT_TAKEN = "scr_user_eea_consent_taken";
    private static final String PRIVACY_POLICY_URL = "http://appscreenrecorder.com/privacy-policy";
    private static final String PUBLISHER_ID = "pub-1374032065732962";
    private static final EEAConsentHelper ourInstance = new EEAConsentHelper();
    /* access modifiers changed from: private */
    public ConsentForm form;
    /* access modifiers changed from: private */
    public OnEEAConsentListener mConsentListener;
    private SharedPreferences sharedPref;

    public interface OnEEAConsentListener {
        void onConsentComplete();

        void onConsentError(String str);

        void onConsentStart();
    }

    public static EEAConsentHelper getInstance() {
        return ourInstance;
    }

    private EEAConsentHelper() {
    }

    public void checkConsentStatus(final Activity activity, OnEEAConsentListener onEEAConsentListener) {
        this.mConsentListener = onEEAConsentListener;
        OnEEAConsentListener onEEAConsentListener2 = this.mConsentListener;
        if (onEEAConsentListener2 != null) {
            onEEAConsentListener2.onConsentStart();
        }
        ConsentInformation.getInstance(activity).requestConsentInfoUpdate(new String[]{PUBLISHER_ID}, new ConsentInfoUpdateListener() {
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                boolean isRequestLocationInEeaOrUnknown = ConsentInformation.getInstance(activity).isRequestLocationInEeaOrUnknown();
                EEAConsentHelper.this.setEEALocationPref(activity, isRequestLocationInEeaOrUnknown);
                if (!isRequestLocationInEeaOrUnknown) {
                    EEAConsentHelper.this.setUserEEAConsentType(activity, consentStatus.ordinal());
                    EEAConsentHelper.this.setUserConsentTaken(activity, true);
                    if (EEAConsentHelper.this.mConsentListener != null) {
                        EEAConsentHelper.this.mConsentListener.onConsentComplete();
                    }
                } else if (consentStatus != ConsentStatus.UNKNOWN) {
                    EEAConsentHelper.this.setUserEEAConsentType(activity, consentStatus.ordinal());
                    if (EEAConsentHelper.this.mConsentListener != null) {
                        EEAConsentHelper.this.mConsentListener.onConsentComplete();
                    }
                } else {
                    EEAConsentHelper.this.showConsentForm(activity);
                }
            }

            public void onFailedToUpdateConsentInfo(String str) {
                if (EEAConsentHelper.this.mConsentListener != null) {
                    EEAConsentHelper.this.mConsentListener.onConsentError(str);
                }
            }
        });
    }

    public void showEEAConsentForm(Activity activity, OnEEAConsentListener onEEAConsentListener) {
        this.mConsentListener = onEEAConsentListener;
        OnEEAConsentListener onEEAConsentListener2 = this.mConsentListener;
        if (onEEAConsentListener2 != null) {
            onEEAConsentListener2.onConsentStart();
        }
        showConsentForm(activity);
    }

    /* access modifiers changed from: private */
    public void showConsentForm(final Activity activity) {
        URL url;
        try {
            url = new URL(PRIVACY_POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }
        this.form = new ConsentForm.Builder(activity, url).withListener(new ConsentFormListener() {
            public void onConsentFormOpened() {
            }

            public void onConsentFormLoaded() {
                //Activity activity = activity;
                if (activity != null && !activity.isFinishing()) {
                    EEAConsentHelper.this.form.show();
                }
            }

            public void onConsentFormClosed(ConsentStatus consentStatus, Boolean bool) {
                EEAConsentHelper.this.setUserConsentTaken(activity, true);
                EEAConsentHelper.this.setUserEEAConsentType(activity, consentStatus.ordinal());
                if (EEAConsentHelper.this.mConsentListener != null) {
                    EEAConsentHelper.this.mConsentListener.onConsentComplete();
                }
            }

            public void onConsentFormError(String str) {
                if (EEAConsentHelper.this.mConsentListener != null) {
                    EEAConsentHelper.this.mConsentListener.onConsentError(str);
                }
            }
        }).withPersonalizedAdsOption().withNonPersonalizedAdsOption().build();
        this.form.load();
    }

    public int getEEAConsentAdType(Context context) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return this.sharedPref.getInt(PREF_KEY_USER_EEA_CONSENT_TYPE, 0);
    }

    public boolean isUserFromEEALocation(Context context) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return this.sharedPref.getBoolean(PREF_KEY_USER_EEA_LOCATION, false);
    }

    public Bundle getNonPersonalisedBundle(Context context) {
        Bundle bundle = new Bundle();
        //bundle.putString("npa", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        return bundle;
    }

    /* access modifiers changed from: private */
    public void setEEALocationPref(Context context, boolean z) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        this.sharedPref.edit().putBoolean(PREF_KEY_USER_EEA_LOCATION, z).apply();
    }

    /* access modifiers changed from: private */
    public void setUserEEAConsentType(Context context, int i) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        this.sharedPref.edit().putInt(PREF_KEY_USER_EEA_CONSENT_TYPE, i).apply();
    }

    /* access modifiers changed from: private */
    public void setUserConsentTaken(Context context, boolean z) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        this.sharedPref.edit().putBoolean(PREF_KEY_USER_USER_CONSENT_TAKEN, z).apply();
    }

    public boolean isUserConsentTaken(Context context) {
        if (this.sharedPref == null) {
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return this.sharedPref.getBoolean(PREF_KEY_USER_USER_CONSENT_TAKEN, false);
    }
}
