package com.yasoka.eazyscreenrecord.utils.pieview;

import android.graphics.Bitmap;
import com.ezscreenrecorder.utils.pieview.RadialMenuRenderer.OnRadailMenuClick;

public class RadialMenuItem {
    private Bitmap drawable;
    private OnRadailMenuClick mCallback;
    private String mMenuID;
    private String mMenuName;

    public RadialMenuItem(String str, String str2, Bitmap bitmap) {
        this.mMenuID = str;
        this.mMenuName = str2;
        this.drawable = bitmap;
    }

    public Bitmap getDrawableId() {
        return this.drawable;
    }

    public String getMenuID() {
        return this.mMenuID;
    }

    public String getMenuName() {
        return this.mMenuName;
    }

    public void setOnRadialMenuClickListener(OnRadailMenuClick onRadailMenuClick) {
        this.mCallback = onRadailMenuClick;
    }

    public OnRadailMenuClick getOnRadailMenuClick() {
        return this.mCallback;
    }
}
