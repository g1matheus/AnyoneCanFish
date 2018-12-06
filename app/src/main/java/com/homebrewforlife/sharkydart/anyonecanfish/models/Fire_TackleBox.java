package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Fire_TackleBox implements Parcelable{
    private ArrayList<Fire_Lure> theLures;
    private String uid;
    private String desc;
    private String name;
    private String image_url;

    public Fire_TackleBox(){}

    public Fire_TackleBox(ArrayList<Fire_Lure> theLures, String desc, String name, String image_url, String uid) {
        this.theLures = theLures;
        this.desc = desc;
        this.name = name;
        this.image_url = image_url;
        this.uid = uid;
    }
    private Fire_TackleBox(Parcel in){
        this.name = in.readString();
        this.desc = in.readString();
        this.image_url = in.readString();
        this.uid = in.readString();
        ArrayList<Fire_Lure> ing_temp = new ArrayList<>();
        in.readList(ing_temp, getClass().getClassLoader());
        this.setTheLures(ing_temp);
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(desc);
        out.writeString(image_url);
        out.writeString(uid);
        out.writeList(theLures);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_TackleBox> CREATOR = new Parcelable.Creator<Fire_TackleBox>(){
        public Fire_TackleBox createFromParcel(Parcel in){
            return new Fire_TackleBox(in);
        }
        public Fire_TackleBox[] newArray(int size){
            return new Fire_TackleBox[size];
        }
    };

//    public ArrayList<Fire_Lure> getTheLures() {
//        return theLures;
//    }

    public void setTheLures(ArrayList<Fire_Lure> theLures) {
        this.theLures = theLures;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
/*

    public String getQuickDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Box: ").append(uid)
                .append(" \n Name => ").append(name)
                .append(" \n desc => ").append(desc)
                .append(" \n image_url => ").append(image_url);
        if(theLures != null){
            builder.append(" \n lure count => ").append(theLures.size());
            for(Fire_Lure oneLure : theLures){
                builder.append("\n").append(oneLure.getQuickDescription());
            }
        }
        else
            builder.append(" \n lure count => [ null ]");

        return builder.toString();
    }
*/
}
