package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

public class Fire_FishEvent implements Parcelable {
    private String uid;
    private Timestamp date;
    private String desc;
    private double latitude;
    private double longitude;
    private String image_url;
    private Boolean released;
    private String species;
    private GeoPoint geo_loc;

    public Fire_FishEvent(){}

    public Fire_FishEvent(String uid, Timestamp date, String desc, double latitude, double longitude, String image_url, Boolean released, String species) {
        this.uid = uid;
        this.date = date;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_url = image_url;
        this.released = released;
        this.species = species;
    }

    public Fire_FishEvent(String uid, Timestamp date, String desc, String image_url, Boolean released, String species, GeoPoint geo_loc) {
        this.uid = uid;
        this.date = date;
        this.desc = desc;
        this.image_url = image_url;
        this.released = released;
        this.species = species;
        this.geo_loc = geo_loc;
        this.latitude = geo_loc.getLatitude();
        this.longitude = geo_loc.getLongitude();
    }

    private Fire_FishEvent(Parcel in){
        this.uid = in.readString();
        this.desc = in.readString();
        this.species = in.readString();
        this.released = (in.readInt() == 1);
        this.image_url = in.readString();
        this.date = in.readParcelable(Timestamp.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeString(desc);
        out.writeString(species);
        out.writeInt(released ? 1 : 0);
        out.writeString(image_url);
        out.writeParcelable(date, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_FishEvent> CREATOR = new Parcelable.Creator<Fire_FishEvent>(){
        public Fire_FishEvent createFromParcel(Parcel in){
            return new Fire_FishEvent(in);
        }
        public Fire_FishEvent[] newArray(int size){
            return new Fire_FishEvent[size];
        }
    };

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public GeoPoint getGeo_loc() {
        return geo_loc;
    }

    public void setGeo_loc(GeoPoint geo_loc) {
        this.geo_loc = geo_loc;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Boolean getReleased() {
        return released;
    }

    public void setReleased(Boolean released) {
        this.released = released;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Exclude
    public String getQuickDescription(){
        return  "FishEvent: " + uid
                + "\ndate =>" + date
                + "\ndesc =>" + desc
                + "\nreleased =>" + released
                + "\nspecies =>" + species
                + "\ngeo_loc =>" + geo_loc.toString()
                + "\nimg_url =>" + image_url;
    }
}
