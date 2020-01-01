package com.yasoka.eazyscreenrecord.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatButton;*/
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.utils.Constants;
/*import com.ezscreenrecorder.R;
import com.ezscreenrecorder.utils.Constants;*/

public class DrawOverAppsPermissionActivity extends AppCompatActivity implements OnClickListener {
    public static final int EXTRA_ON_BOARDING_TYPE_DEFAULT = 9901;
    public static final int EXTRA_ON_BOARDING_TYPE_NEW = 9902;
    public static final String KEY_ON_BOARDING_TYPE = "new_on_bBoarding_type";
    private boolean isAppPermissionDialogShowed = false;
    private ConstraintLayout layoutContainer;
    private AppCompatButton negativeButton;
    private int onBoardingType = EXTRA_ON_BOARDING_TYPE_DEFAULT;
    private AppCompatButton positiveButton;

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_draw_over_apps_permission);
        getOnBoardingType();
        this.positiveButton = (AppCompatButton) findViewById(R.id.id_draw_over_apps_positive_button);
        this.negativeButton = (AppCompatButton) findViewById(R.id.id_draw_over_apps_negative_button);
        this.layoutContainer = (ConstraintLayout) findViewById(R.id.id_draw_over_apps_layout_container);
        if (this.onBoardingType == 9902) {
            this.negativeButton.setText(R.string.id_skip_txt);
        }
        if (checkOverlayPermission()) {
            this.layoutContainer.setVisibility(View.GONE);
            askForPermissions();
        } else {
            this.layoutContainer.setVisibility(View.VISIBLE);
        }
        this.positiveButton.setOnClickListener(this);
        this.negativeButton.setOnClickListener(this);
    }

    private void getOnBoardingType() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            String str = KEY_ON_BOARDING_TYPE;
            if (intent.hasExtra(str)) {
                this.onBoardingType = getIntent().getIntExtra(str, EXTRA_ON_BOARDING_TYPE_DEFAULT);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_draw_over_apps_negative_button /*2131296522*/:
                startFullScreenActivity();
                finish();
                return;
            case R.id.id_draw_over_apps_positive_button /*2131296523*/:
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("package:");
                    sb.append(getPackageName());
                    startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), Constants.REQUEST_CODE_DRAW_OVER_APPS_OVERLAY);
                    return;
                } catch (Exception unused) {
                    askForPermissions();
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1124) {
            askForPermissions();
        }
    }

    private void askForPermissions() {
        if (VERSION.SDK_INT < 23) {
            startFullScreenActivity();
            finish();
        } else if (!checkStoragePermission()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DrawOverAppsPermissionActivity.this.requestStoragePermission();
                }
            }, 200);
        } else {
            startFullScreenActivity();
            finish();
        }
    }

    private boolean checkOverlayPermission() {
        if (VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }
    /* access modifiers changed from: private */
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
    }
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1121 || iArr.length <= 0) {
            return;
        }
        if (iArr[0] == 0) {
            startFullScreenActivity();
            finish();
        } else if (iArr[0] != -1) {
        } else {
            if (!this.isAppPermissionDialogShowed) {
                showStoragePermissionErrorDialog();
                return;
            }
            startFullScreenActivity();
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void startFullScreenActivity() {
        Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
        if (this.onBoardingType == 9902) {
            boolean checkOverlayPermission = checkOverlayPermission();
            String str = FullscreenActivity.KEY_SHOW_HELP;
            if (!checkOverlayPermission) {
                intent.putExtra(str, FullscreenActivity.EXTRA_HELP_TYPE_SHOW_NOTIFICATION);
            } else {
                intent.putExtra(str, FullscreenActivity.EXTRA_HELP_TYPE_SHOW_FLOATING_EXIT);
            }
        }
        startActivity(intent);
    }

    private void showStoragePermissionErrorDialog() {
        new AlertDialog.Builder(this).setMessage((int) R.string.id_storage_permission_failed_dialog_message).setPositiveButton((int) R.string.id_turn_it_on_txt, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DrawOverAppsPermissionActivity.this.requestStoragePermission();
            }
        }).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DrawOverAppsPermissionActivity.this.startFullScreenActivity();
                DrawOverAppsPermissionActivity.this.finish();
            }
        }).show();
        this.isAppPermissionDialogShowed = true;
    }
}
