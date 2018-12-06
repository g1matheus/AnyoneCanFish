package com.homebrewforlife.sharkydart.anyonecanfish.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

//needs to be sent a latitude and longitude in the intent
public class GetSolunarDataService extends IntentService {
    public GetSolunarDataService(){
        super("GetSolunarDataService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent theIntent) {
        if(theIntent != null) {
            String theIntentAction = theIntent.getAction();
            Log.d("fart", "get solunar action: " + theIntentAction);
            //get the inputs from the intent
            String date = theIntent.getStringExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_DATE);
            int tz = theIntent.getIntExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_TZ, -5);
            Double lat = theIntent.getDoubleExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_LAT, LocationTasks.DEFAULT_LAT);
            Double lon = theIntent.getDoubleExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_LON, LocationTasks.DEFAULT_LON);
            String coords = lat + "," + lon;
            GetSolunarDataTasks.querySolunarApiTask(this, theIntentAction, date, coords, tz);
        }
    }
}
