package com.yasoka.eazyscreenrecord.utils;

import com.ezscreenrecorder.C0793R;

public enum TabType {
    TAB_VIDEO(C0793R.C0794drawable.video_bottom, C0793R.mipmap.ic_camera_bottom_bar, C0793R.string.recordings, 0),
    TAB_CAMERA(C0793R.C0794drawable.camera_bottom, C0793R.mipmap.ic_camera_bottom_bar, C0793R.string.screenshots, 0),
    TAB_EDITOR(C0793R.C0794drawable.id_tab_editor_img, C0793R.mipmap.ic_camera_bottom_bar, C0793R.string.id_gallery_edit, 0),
    TAB_AUDIO(C0793R.C0794drawable.ic_tab_audio_img, C0793R.mipmap.ic_camera_bottom_bar, C0793R.string.txt_audio, 0);
    
    private int defaultImg;
    private int notificationCount;
    private int selectedImg;
    private int title;

    private TabType(int i, int i2, int i3, int i4) {
        this.defaultImg = i;
        this.selectedImg = i2;
        this.notificationCount = i4;
        this.title = i3;
    }

    public int getDefaultImg() {
        return this.defaultImg;
    }

    public int getSelectedImg() {
        return this.selectedImg;
    }

    public int getNotificationCount() {
        return this.notificationCount;
    }

    public void setNotificationCount(int i) {
        this.notificationCount = i;
    }

    public int getTitle() {
        return this.title;
    }
}
