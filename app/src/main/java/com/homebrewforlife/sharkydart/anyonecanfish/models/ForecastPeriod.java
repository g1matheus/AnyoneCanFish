package com.homebrewforlife.sharkydart.anyonecanfish.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastPeriod implements Parcelable {
    /*
        represents weather data for a less-than 24 hr period of time
     */
    private int number; //id
    private String name;
    private String startTime;
    private String endTime;
    private Boolean isDayTime;   //lowercase boolean
    private double temperature;
    private String temperatureUnit;
    private String windSpeed;
    private String windDirection;   //wind from the west, fishing is best; wind from the east, fishing is least
    private String iconURL;
    private String shortForecast;
    private String detailedForecast;

    public ForecastPeriod(){}

    public ForecastPeriod(int number, String name, String startTime, String endTime,
                          Boolean isDayTime, double temperature, String temperatureUnit, String windSpeed,
                          String windDirection, String iconURL, String shortForecast, String detailedForecast) {
        this.number = number;
        this.isDayTime = isDayTime;
        this.temperature = temperature;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.temperatureUnit = temperatureUnit;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.iconURL = iconURL;
        this.shortForecast = shortForecast;
        this.detailedForecast = detailedForecast;
    }
    private ForecastPeriod(Parcel in){
        this.number = in.readInt();
        this.isDayTime = (in.readInt() == 1);
        this.temperature = in.readDouble();
        this.name = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.temperatureUnit = in.readString();
        this.windSpeed = in.readString();
        this.windDirection = in.readString();
        this.iconURL = in.readString();
        this.shortForecast = in.readString();
        this.detailedForecast = in.readString();
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(number);
        out.writeInt(isDayTime ? 1 : 0);
        out.writeDouble(temperature);
        out.writeString(name);
        out.writeString(startTime);
        out.writeString(endTime);
        out.writeString(temperatureUnit);
        out.writeString(windSpeed);
        out.writeString(windDirection);
        out.writeString(iconURL);
        out.writeString(shortForecast);
        out.writeString(detailedForecast);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<ForecastPeriod> CREATOR = new Parcelable.Creator<ForecastPeriod>(){
        public ForecastPeriod createFromParcel(Parcel in){
            return new ForecastPeriod(in);
        }
        public ForecastPeriod[] newArray(int size){
            return new ForecastPeriod[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsDayTime() {
        return isDayTime;
    }

    public void setIsDayTime(Boolean isDayTime) {
        this.isDayTime = isDayTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getShortForecast() {
        return shortForecast;
    }

    public void setShortForecast(String shortForecast) {
        this.shortForecast = shortForecast;
    }

    public String getDetailedForecast() {
        return detailedForecast;
    }

    public void setDetailedForecast(String detailedForecast) {
        this.detailedForecast = detailedForecast;
    }

    public String getQuickDescription(){
        return number
        + "\nname =>" +   name
        + "\nstartTime =>" +   startTime
        + "\nendTime =>" +   endTime
        + "\nisDayTime =>" +   isDayTime   //lowercase boolean
        + "\ntemperature =>" +   temperature
        + "\ntemperatureUnit =>" +   temperatureUnit
        + "\nwindSpeed =>" +   windSpeed
        + "\nwindDirection =>" +   windDirection   //wind from the west, fishing is best wind from the east, fishing is least
        + "\niconURL =>" +   iconURL
        + "\nshortForecast =>" +   shortForecast
        + "\ndetailedForecast =>" +   detailedForecast;
    }
}
