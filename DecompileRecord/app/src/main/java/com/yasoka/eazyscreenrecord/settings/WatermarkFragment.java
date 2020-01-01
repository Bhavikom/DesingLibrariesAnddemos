package com.yasoka.eazyscreenrecord.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.utils.AppUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class WatermarkFragment extends DialogFragment {
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnDismissListener) {
            ((OnDismissListener) parentFragment).onDismiss(dialogInterface);
        } else if (getActivity() instanceof OnDismissListener) {
            ((OnDismissListener) getActivity()).onDismiss(dialogInterface);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_watermark, viewGroup, false);
    }

    public void onViewCreated(@NonNull final View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(C0793R.C0795id.btn_bitmap).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final LinearLayout linearLayout = (LinearLayout) view.findViewById(C0793R.C0795id.lay_watermark);
                linearLayout.post(new Runnable() {
                    public void run() {
                        FileOutputStream fileOutputStream;
                        Throwable th;
                        Bitmap loadBitmapFromView = WatermarkFragment.this.loadBitmapFromView(linearLayout);
                        try {
                            fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), AppUtils.APP_WATERMARK_FILE_NAME));
                            loadBitmapFromView.compress(CompressFormat.PNG, 100, fileOutputStream);
                            fileOutputStream.close();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                });
            }
        });
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public Bitmap loadBitmapFromView(View view) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("WatermarkFragment.loadBitmapFromView->");
        sb.append(view.getLayoutParams().width);
        sb.append("<><><>");
        sb.append(view.getLayoutParams().height);
        printStream.println(sb.toString());
        Bitmap createBitmap = Bitmap.createBitmap(view.getLayoutParams().width, view.getLayoutParams().height, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return createBitmap;
    }
}
