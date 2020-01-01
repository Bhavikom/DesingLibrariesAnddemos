package com.yasoka.eazyscreenrecord.model;

public class GalleryEditDataModel {
    public static final int GALLERY_EDIT_TYPE_EDIT_IMAGE = 6502;
    public static final int GALLERY_EDIT_TYPE_EDIT_VIDEO = 6501;
    public static final int GALLERY_EDIT_TYPE_EDIT_VIDEO_TO_GIF = 6503;
    private int editType;
    private int resImage;
    private int resName;

    public GalleryEditDataModel(int i, int i2, int i3) {
        this.resImage = i;
        this.resName = i2;
        this.editType = i3;
    }

    public int getResImage() {
        return this.resImage;
    }

    public void setResImage(int i) {
        this.resImage = i;
    }

    public int getResName() {
        return this.resName;
    }

    public void setResName(int i) {
        this.resName = i;
    }

    public int getEditType() {
        return this.editType;
    }

    public void setEditType(int i) {
        this.editType = i;
    }
}
