package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Fire_Lure implements Parcelable{
    private String uid;
    private String name;
    private String size;
    private String type;
    private String desc;
    private String image_url;
    private String hook_type;
    private int hook_count;

    public Fire_Lure(){}

    public Fire_Lure(String name, String size, String type, String desc){
        this.name = name;
        this.size = size;
        this.type = type;
        this.desc = desc;
        this.image_url = "";
        this.hook_count = 1;
        this.hook_type = "treble";
    }

    public Fire_Lure(String uid, String name, String size, String type, String desc, String image_url, String hook_type, int hook_count) {
        this.uid = uid;
        this.name = name;
        this.size = size;
        this.type = type;
        this.desc = desc;
        this.image_url = image_url;
        this.hook_type = hook_type;
        this.hook_count = hook_count;
    }

    private Fire_Lure(Parcel in){
        this.uid = in.readString();
        this.name = in.readString();
        this.size = in.readString();
        this.type = in.readString();
        this.desc = in.readString();
        this.image_url = in.readString();
        this.hook_type = in.readString();
        this.hook_count = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeString(name);
        out.writeString(size);
        out.writeString(type);
        out.writeString(desc);
        out.writeString(image_url);
        out.writeString(hook_type);
        out.writeInt(hook_count);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_Lure> CREATOR = new Parcelable.Creator<Fire_Lure>(){
        public Fire_Lure createFromParcel(Parcel in){
            return new Fire_Lure(in);
        }
        public Fire_Lure[] newArray(int size){
            return new Fire_Lure[size];
        }
    };
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getHook_type() {
        return hook_type;
    }

    public void setHook_type(String hook_type) {
        this.hook_type = hook_type;
    }

    public int getHook_count() {
        return hook_count;
    }

    public void setHook_count(int hook_count) {
        this.hook_count = hook_count;
    }

    public String getQuickDescription(){
        return "UID: " + uid
                + "\nName => " + name
                + "\ntype => " + size + ", " + type
                + "\ndesc => " + desc
                + "\nhooks => " + hook_count + " " + hook_type
                + "\nimgurl => " + image_url;
    }
}
