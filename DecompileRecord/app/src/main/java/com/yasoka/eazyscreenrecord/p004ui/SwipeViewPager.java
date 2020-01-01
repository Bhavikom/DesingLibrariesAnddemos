package com.yasoka.eazyscreenrecord.p004ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.p000v4.view.ViewPager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* renamed from: com.ezscreenrecorder.ui.SwipeViewPager */
public class SwipeViewPager extends ViewPager {
    private boolean isSwipeEnabled = true;

    public SwipeViewPager(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isSwipeEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.isSwipeEnabled) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    public void setHorizontalSwipeEnabled(boolean z) {
        this.isSwipeEnabled = z;
    }
}
