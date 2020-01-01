package com.yasoka.eazyscreenrecord.server.model.LiveFacebookModels;

import java.io.Serializable;
import java.util.Set;

public class LiveFacebookPageModel implements Serializable {
    private String pageAccessToken;
    private String pageCategory;
    private String pageId;
    private String pageName;
    private Set<String> pageTasks;

    public String getPageId() {
        return this.pageId;
    }

    public void setPageId(String str) {
        this.pageId = str;
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String str) {
        this.pageName = str;
    }

    public String getPageCategory() {
        return this.pageCategory;
    }

    public void setPageCategory(String str) {
        this.pageCategory = str;
    }

    public String getPageAccessToken() {
        return this.pageAccessToken;
    }

    public void setPageAccessToken(String str) {
        this.pageAccessToken = str;
    }

    public Set<String> getPageTasks() {
        return this.pageTasks;
    }

    public void setPageTasks(Set<String> set) {
        this.pageTasks = set;
    }
}
