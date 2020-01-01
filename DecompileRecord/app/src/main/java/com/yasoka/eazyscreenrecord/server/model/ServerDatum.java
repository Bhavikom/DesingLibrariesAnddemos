package com.yasoka.eazyscreenrecord.server.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerDatum implements Parcelable {
    public static final Creator<ServerDatum> CREATOR = new Creator<ServerDatum>() {
        public ServerDatum createFromParcel(Parcel parcel) {
            return new ServerDatum(parcel);
        }

        public ServerDatum[] newArray(int i) {
            return new ServerDatum[i];
        }
    };
    @SerializedName("a_id")
    @Expose
    private String aId;
    @SerializedName("added_date")
    @Expose
    private String added_date;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_100")
    @Expose
    private String imageUrl100;
    @SerializedName("image_url_300")
    @Expose
    private String imageUrl300;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("views")
    @Expose
    private String views;

    public int describeContents() {
        return 0;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String str) {
        this.timezone = str;
    }

    public String getAdded_date() {
        return this.added_date;
    }

    public void setAdded_date(String str) {
        this.added_date = str;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String str) {
        this.user_name = str;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String str) {
        this.imageName = str;
    }

    public String getAId() {
        return this.aId;
    }

    public void setAId(String str) {
        this.aId = str;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String str) {
        this.uId = str;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String str) {
        this.emailId = str;
    }

    public String getShareUrl() {
        return this.shareUrl;
    }

    public void setShareUrl(String str) {
        this.shareUrl = str;
    }

    public String getImageUrl100() {
        return this.imageUrl100;
    }

    public void setImageUrl100(String str) {
        this.imageUrl100 = str;
    }

    public String getImageUrl300() {
        return this.imageUrl300;
    }

    public void setImageUrl300(String str) {
        this.imageUrl300 = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    public String getViews() {
        return this.views;
    }

    public void setViews(String str) {
        this.views = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imageName);
        parcel.writeString(this.aId);
        parcel.writeString(this.uId);
        parcel.writeString(this.emailId);
        parcel.writeString(this.shareUrl);
        parcel.writeString(this.imageUrl100);
        parcel.writeString(this.imageUrl300);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.views);
        parcel.writeString(this.added_date);
        parcel.writeString(this.user_name);
        parcel.writeString(this.timezone);
    }

    public ServerDatum() {
    }

    protected ServerDatum(Parcel parcel) {
        this.imageName = parcel.readString();
        this.aId = parcel.readString();
        this.uId = parcel.readString();
        this.emailId = parcel.readString();
        this.shareUrl = parcel.readString();
        this.imageUrl100 = parcel.readString();
        this.imageUrl300 = parcel.readString();
        this.imageUrl = parcel.readString();
        this.views = parcel.readString();
        this.added_date = parcel.readString();
        this.user_name = parcel.readString();
        this.timezone = parcel.readString();
    }
}
