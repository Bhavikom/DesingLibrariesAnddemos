package com.yasoka.eazyscreenrecord.utils.pieview;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.ArrayList;

public class RadialMenuRenderer {
    public static final String RADIAL_NO_TEXT = "HOLLOW";
    private boolean alt = false;
    private int mMenuBackgroundColor = Color.parseColor("#100d71");
    private int mMenuBorderColor = -1;
    private int mMenuSelectedColor = Color.parseColor("#00318c");
    private int mMenuTextColor = -1;
    private View mParentView;
    private ArrayList<RadialMenuItem> mRadialMenuContent = new ArrayList<>(0);
    private float mRadius = 60.0f;
    private float mThickness = 10.0f;

    public interface OnRadailMenuClick {
        void onRadailMenuClickedListener(String str);
    }

    public RadialMenuRenderer(View view, boolean z, float f, float f2) {
        this.mParentView = view;
        this.alt = z;
        this.mThickness = f;
        this.mRadius = f2;
    }

    public View renderView() {
        final RadialMenuView radialMenuView = new RadialMenuView(this.mParentView.getContext(), this);
        this.mParentView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return radialMenuView.gestureHandler(motionEvent, true);
            }
        });
        radialMenuView.setVisibility(0);
        return radialMenuView;
    }

    public ArrayList<RadialMenuItem> getRadialMenuContent() {
        return this.mRadialMenuContent;
    }

    public void setRadialMenuContent(ArrayList<RadialMenuItem> arrayList) {
        this.mRadialMenuContent = arrayList;
    }

    public boolean isAlt() {
        return this.alt;
    }

    public float getMenuThickness() {
        return this.mThickness;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public int getMenuBackgroundColor() {
        return this.mMenuBackgroundColor;
    }

    public void setMenuBackgroundColor(int i) {
        this.mMenuBackgroundColor = i;
    }

    public int getMenuSelectedColor() {
        return this.mMenuSelectedColor;
    }

    public void setMenuSelectedColor(int i) {
        this.mMenuSelectedColor = i;
    }

    public int getMenuTextColor() {
        return this.mMenuTextColor;
    }

    public void setMenuTextColor(int i) {
        this.mMenuTextColor = i;
    }

    public int getMenuBorderColor() {
        return this.mMenuBorderColor;
    }

    public void setMenuBorderColor(int i) {
        this.mMenuBorderColor = i;
    }
}
