package com.homebrewforlife.sharkydart.anyonecanfish.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

//needs to be sent a latitude and longitude in the intent
public class GetForecastDataService extends IntentService {
    public GetForecastDataService(){
        super("GetForecastDataService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent theIntent) {
        if(theIntent != null) {
            String theIntentAction = theIntent.getAction();
            Log.d("fart", "get forecast action: " + theIntentAction);
            //get the forecast api url from intent, and send to the task
            String theForecastApiUrl;
            theForecastApiUrl = theIntent.getStringExtra(GetForecastDataTasks.EXTRA_THE_FORECAST_API_URL);
            GetForecastDataTasks.queryThisForecastApiTask(this, theIntentAction, theForecastApiUrl);
        }
    }
}
