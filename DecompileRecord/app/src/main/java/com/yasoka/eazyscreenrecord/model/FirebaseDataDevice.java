package com.yasoka.eazyscreenrecord.model;

import java.util.Calendar;

public class FirebaseDataDevice {
    private long created = Calendar.getInstance().getTimeInMillis();
    private String message;

    public FirebaseDataDevice(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(long j) {
        this.created = j;
    }
}
