package com.yasoka.eazyscreenrecord.adapter;

import android.os.Handler;
import android.support.p000v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import com.ezscreenrecorder.C0793R;

public class ChangeFilterTouchListenr implements OnTouchListener {
    private Handler handler;
    /* access modifiers changed from: private */
    public ImageView imageView;
    private Runnable runnable = new Runnable() {
        public void run() {
            try {
                ChangeFilterTouchListenr.this.imageView.setColorFilter(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public ChangeFilterTouchListenr(ImageView imageView2) {
        this.imageView = imageView2;
        this.handler = new Handler();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0 || action == 2 || action == 11) {
            ImageView imageView2 = this.imageView;
            imageView2.setColorFilter(ContextCompat.getColor(imageView2.getContext(), C0793R.color.colorPrimary));
            return false;
        }
        this.handler.removeCallbacks(this.runnable);
        this.handler.postDelayed(this.runnable, 300);
        return false;
    }
}
