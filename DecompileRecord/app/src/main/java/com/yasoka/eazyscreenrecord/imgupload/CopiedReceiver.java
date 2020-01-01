package com.yasoka.eazyscreenrecord.imgupload;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;

public class CopiedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", intent.getStringExtra("copied")));
            Toast.makeText(context, C0793R.string.id_link_copied_txt, 1).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
