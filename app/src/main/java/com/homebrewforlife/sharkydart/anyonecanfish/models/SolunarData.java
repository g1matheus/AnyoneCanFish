package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SolunarData implements Parcelable{
/*
The computed 'phen' are identified by the following abbreviations or symbols:

BC = Begin civil twilight
R = Rise
U = Upper Transit
S = Set
EC = End civil twilight
L = Lower Transit (above the horizon)
** = object continuously above the horizon
-- = object continuously below the horizon
^^ = object continuously above the twilight limit
~~ = object continuously below the twilight limit

    sundata - array(5) of objects
            { phen - phenomenon code, time - local time of the phase}
    moondata - array(3) of objects
            { phen - phenomenon code, time - local time of the phase}
    fracillum - fraction of the Moon's apparent surface which is lit
                (if "closestphase" occurs on date requested, "fracillum" will not be in JSON)
    curphase - phase of the Moon on the date requested (if "closestphase" occurs on date requested, "curphase" will not be in JSON)

    //not including this
    "closestphase," which contains information on the nearest primary phase to the date requested:
        "phase" — phase of the moon at the nearest primary phase to the date requested
        "date" — the date of the primary phase
        "time" — the time of the primary phase
*/
    private ArrayList<SolunarPhenomena> sundata;
    private ArrayList<SolunarPhenomena> moondata;
    private String fracillum;
    private String curphase;
    private String closestPhase;
    private String dayOfWeek;

    public SolunarData(){}

    public SolunarData(ArrayList<SolunarPhenomena> sundata, ArrayList<SolunarPhenomena> moondata, String fracillum, String curphase, String closestPhase, String dayOfWeek) {
        this.sundata = sundata;
        this.moondata = moondata;
        this.fracillum = fracillum;
        this.curphase = curphase;
        this.closestPhase = closestPhase;
        this.dayOfWeek = dayOfWeek;
    }
    private SolunarData(Parcel in){
        this.fracillum = in.readString();
        this.curphase = in.readString();
        this.closestPhase = in.readString();
        this.dayOfWeek = in.readString();
        ArrayList<SolunarPhenomena> in_sun = new ArrayList<>();
        in.readList(in_sun, getClass().getClassLoader());
        this.setSundata(in_sun);
        ArrayList<SolunarPhenomena> in_moon = new ArrayList<>();
        in.readList(in_moon, getClass().getClassLoader());
        this.setMoondata(in_moon);
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(fracillum);
        out.writeString(curphase);
        out.writeString(closestPhase);
        out.writeString(dayOfWeek);
        out.writeList(sundata);
        out.writeList(moondata);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<SolunarData> CREATOR = new Parcelable.Creator<SolunarData>(){
        public SolunarData createFromParcel(Parcel in){
            return new SolunarData(in);
        }
        public SolunarData[] newArray(int size){
            return new SolunarData[size];
        }
    };

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getClosestPhase() {
        return closestPhase;
    }

    public void setClosestPhase(String closestPhase) {
        this.closestPhase = closestPhase;
    }

    public ArrayList<SolunarPhenomena> getSundata() {
        return sundata;
    }

    public void setSundata(ArrayList<SolunarPhenomena> sundata) {
        this.sundata = sundata;
    }

    public ArrayList<SolunarPhenomena> getMoondata() {
        return moondata;
    }

    public void setMoondata(ArrayList<SolunarPhenomena> moondata) {
        this.moondata = moondata;
    }

    public String getFracillum() {
        return fracillum;
    }

    public void setFracillum(String fracillum) {
        this.fracillum = fracillum;
    }

    public String getCurphase() {
        return curphase;
    }

    public void setCurphase(String curphase) {
        this.curphase = curphase;
    }

    public String getQuickDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("curphase: ").append(curphase)
                .append(" \n Day of Week => ").append(dayOfWeek)
                .append(" \n Fracillum => ").append(fracillum)
                .append(" \n Closest Phase => ").append(closestPhase);

        if(sundata != null){
            builder.append(" \n sun phenomena count => ").append(sundata.size());
            for(SolunarPhenomena onePhen : sundata){
                builder.append("\n").append(onePhen.getQuickDescription());
            }
        }
        else
            builder.append(" \n sun phenomena count => [ null ]");

        if(moondata != null){
            builder.append(" \n moon phenomena count => ").append(moondata.size());
            for(SolunarPhenomena onePhen : moondata){
                builder.append("\n").append(onePhen.getQuickDescription());
            }
        }
        else
            builder.append(" \n moon phenomena count => [ null ]");

        return builder.toString();
    }

}
