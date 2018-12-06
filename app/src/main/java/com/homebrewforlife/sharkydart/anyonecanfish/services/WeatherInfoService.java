package com.homebrewforlife.sharkydart.anyonecanfish.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

//needs to be sent a latitude and longitude in the intent
public class WeatherInfoService extends IntentService {
    public WeatherInfoService(){
        super("WeatherInfoService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent theIntent) {
        if(theIntent != null) {
            String theIntentAction = theIntent.getAction();
            Log.d("fart", "weather action: " + theIntentAction);
            //get latitude and longitude from intent, and send to the task
            double theLat, theLon;
            theLat = theIntent.getDoubleExtra(LocationTasks.EXTRA_LATITUDE, LocationTasks.DEFAULT_LAT);
            theLon = theIntent.getDoubleExtra(LocationTasks.EXTRA_LONGITUDE, LocationTasks.DEFAULT_LON);
            WeatherInfoTasks.queryWeatherDotGovTask(this, theIntentAction, theLat, theLon);
        }
    }
}
