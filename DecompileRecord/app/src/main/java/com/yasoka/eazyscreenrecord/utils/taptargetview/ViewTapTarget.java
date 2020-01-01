package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewTapTarget extends TapTarget {
    final View view;

    ViewTapTarget(View view2, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        super(charSequence, charSequence2);
        if (view2 != null) {
            this.view = view2;
            return;
        }
        throw new IllegalArgumentException("Given null view to target");
    }

    public View getView() {
        return this.view;
    }

    public void onReady(final Runnable runnable) {
        ViewUtil.onLaidOut(this.view, new Runnable() {
            public void run() {
                int[] iArr = new int[2];
                ViewTapTarget.this.view.getLocationOnScreen(iArr);
                ViewTapTarget viewTapTarget = ViewTapTarget.this;
                viewTapTarget.bounds = new Rect(iArr[0], iArr[1], iArr[0] + viewTapTarget.view.getWidth(), iArr[1] + ViewTapTarget.this.view.getHeight());
                if (ViewTapTarget.this.icon == null && ViewTapTarget.this.view.getWidth() > 0 && ViewTapTarget.this.view.getHeight() > 0) {
                    Bitmap createBitmap = Bitmap.createBitmap(ViewTapTarget.this.view.getWidth(), ViewTapTarget.this.view.getHeight(), Config.ARGB_8888);
                    ViewTapTarget.this.view.draw(new Canvas(createBitmap));
                    ViewTapTarget viewTapTarget2 = ViewTapTarget.this;
                    viewTapTarget2.icon = new BitmapDrawable(viewTapTarget2.view.getContext().getResources(), createBitmap);
                    ViewTapTarget.this.icon.setBounds(0, 0, ViewTapTarget.this.icon.getIntrinsicWidth(), ViewTapTarget.this.icon.getIntrinsicHeight());
                }
                runnable.run();
            }
        });
    }
}
