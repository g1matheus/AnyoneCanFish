package com.homebrewforlife.sharkydart.anyonecanfish.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class LocationService extends IntentService {
    public LocationService(){
        super("LocationService");
    }
    @Override
    protected void onHandleIntent(Intent theIntent) {
        if(theIntent != null) {
            String theIntentAction = theIntent.getAction();
            Log.d("fart", "Location action: " + theIntentAction);
            LocationTasks.getLastLocationTask(this, theIntentAction);
        }
    }
}
