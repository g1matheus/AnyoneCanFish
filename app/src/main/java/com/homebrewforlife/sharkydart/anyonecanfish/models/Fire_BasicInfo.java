package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fire_BasicInfo implements Parcelable{
    private String title;
    private String body;
    private String image_url;

    public Fire_BasicInfo(){}

    public Fire_BasicInfo(String title, String body, String image_url) {
        this.title = title;
        this.body = body;
        this.image_url = image_url;
    }
    private Fire_BasicInfo(Parcel in){
        this.title = in.readString();
        this.body = in.readString();
        this.image_url = in.readString();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(body);
        out.writeString(image_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_BasicInfo> CREATOR = new Parcelable.Creator<Fire_BasicInfo>(){
        public Fire_BasicInfo createFromParcel(Parcel in){
            return new Fire_BasicInfo(in);
        }
        public Fire_BasicInfo[] newArray(int size){
            return new Fire_BasicInfo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
