package com.yasoka.eazyscreenrecord.model;

import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideosData;
import java.util.List;

public class VideosRemoteDataModel {
    public static final int KEY_VIDEO_LIST_TYPE_EDITOR_CHOICE_VIDEOS = 10217;
    public static final int KEY_VIDEO_LIST_TYPE_FAVORITE_VIDEOS = 10216;
    public static final int KEY_VIDEO_LIST_TYPE_OTHER_VIDEOS = 10215;
    public static final int KEY_VIDEO_LIST_TYPE_TOP_VIDEOS = 10213;
    public static final int KEY_VIDEO_LIST_TYPE_USER_VIDEOS = 10214;
    private boolean isHavingMore;
    private List<VideosData> listData;
    private String title;
    private int typeOfList = KEY_VIDEO_LIST_TYPE_OTHER_VIDEOS;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public boolean isHavingMore() {
        return this.isHavingMore;
    }

    public void setHavingMore(boolean z) {
        this.isHavingMore = z;
    }

    public List<VideosData> getListData() {
        return this.listData;
    }

    public void setListData(List<VideosData> list) {
        this.listData = list;
    }

    public int getTypeOfList() {
        return this.typeOfList;
    }

    public void setTypeOfList(int i) {
        this.typeOfList = i;
    }
}
