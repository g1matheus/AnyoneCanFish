package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fire_GameFish implements Parcelable{
    private String image_url;
    private String information;
    private String species;
    private String wiki;

    public Fire_GameFish(){}

    public Fire_GameFish(String image_url, String information, String species, String wiki) {
        this.image_url = image_url;
        this.information = information;
        this.species = species;
        this.wiki = wiki;
    }
    private Fire_GameFish(Parcel in){
        this.species = in.readString();
        this.information = in.readString();
        this.image_url = in.readString();
        this.wiki = in.readString();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(species);
        out.writeString(information);
        out.writeString(image_url);
        out.writeString(wiki);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Fire_GameFish> CREATOR = new Parcelable.Creator<Fire_GameFish>(){
        public Fire_GameFish createFromParcel(Parcel in){
            return new Fire_GameFish(in);
        }
        public Fire_GameFish[] newArray(int size){
            return new Fire_GameFish[size];
        }
    };

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getQuickDescription() {
        return "Fish: " + species + " /n info => " + information + " /n wiki page => " + wiki + " /n imgurl => " + image_url;
    }
}
