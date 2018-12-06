package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;


public class SolunarPhenomena implements Parcelable {
    private String phen;
    private String time;

    public SolunarPhenomena(){}

    public SolunarPhenomena(String phen, String time) {
        this.phen = phen;
        this.time = time;
    }
    private SolunarPhenomena(Parcel in){
        this.phen = in.readString();
        this.time = in.readString();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(phen);
        out.writeString(time);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<SolunarPhenomena> CREATOR = new Parcelable.Creator<SolunarPhenomena>(){
        public SolunarPhenomena createFromParcel(Parcel in){
            return new SolunarPhenomena(in);
        }
        public SolunarPhenomena[] newArray(int size){
            return new SolunarPhenomena[size];
        }
    };

    public String getPhen() {
        return phen;
    }

    public void setPhen(String phen) {
        this.phen = phen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuickDescription(){
        return "{ Phen: " + phen + ", Time: " + time + " }";
    }
}
