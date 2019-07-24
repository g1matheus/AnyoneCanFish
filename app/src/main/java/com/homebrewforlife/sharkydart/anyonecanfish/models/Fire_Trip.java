package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class Fire_Trip implements Parcelable{
    private String uid;
    private double latitude;
    private double longitude;
    private String name;
    private String desc;
    private Timestamp dateStart;    //using @ServerTimestamp annotation to a Date field indicates treatment as a server timestamp
    private Timestamp dateEnd;      //using @ServerTimestamp annotation to a Date field indicates treatment as a server timestamp
    private GeoPoint geo_loc;

    public Fire_Trip(){}

    public Fire_Trip(String uid, double latitude, double longitude, String name, String desc, Timestamp dateStart, Timestamp dateEnd) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.desc = desc;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        syncGeoPoint();
    }
    public Fire_Trip(String uid, GeoPoint geo_loc, String name, String desc, Timestamp dateStart, Timestamp dateEnd){
        this.uid = uid;
        this.geo_loc = geo_loc;
        syncCoords();
        this.name = name;
        this.desc = desc;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
//        Fire_Trip(geo_loc.getLatitude(), geo_loc.getLongitude(), name, desc, dateStart, dateEnd);
    }
    private Fire_Trip(Parcel in){
        this.uid = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.dateStart = in.readParcelable(Timestamp.class.getClassLoader());
        this.dateEnd = in.readParcelable(Timestamp.class.getClassLoader());
        syncGeoPoint();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeString(name);
        out.writeString(desc);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeParcelable(dateStart, flags);
        out.writeParcelable(dateEnd, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_Trip> CREATOR = new Parcelable.Creator<Fire_Trip>(){
        public Fire_Trip createFromParcel(Parcel in){
            return new Fire_Trip(in);
        }
        public Fire_Trip[] newArray(int size){
            return new Fire_Trip[size];
        }
    };

    private void syncGeoPoint(){this.geo_loc = new GeoPoint(this.latitude, this.longitude);}
    private void syncCoords(){
        this.latitude = geo_loc.getLatitude();
        this.longitude = geo_loc.getLongitude();
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Exclude
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Timestamp dateEnd) {
        this.dateEnd = dateEnd;
    }

    public GeoPoint getGeo_loc() {
        return geo_loc;
    }

    public void setGeo_loc(GeoPoint geo_loc) {
        this.geo_loc = geo_loc;
    }

    @Exclude
    public String getQuickDescription(){
        return "uid: " + uid
                + "\n=> " + name
                + "\n=> " + desc
                + "\n=> " + latitude + ", " + longitude
                + "\n=> " + dateStart
                + "\n=> " + dateEnd;
    }
}
