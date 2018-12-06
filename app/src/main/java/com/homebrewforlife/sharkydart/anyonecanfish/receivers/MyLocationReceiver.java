package com.homebrewforlife.sharkydart.anyonecanfish.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyLocationReceiver extends BroadcastReceiver {
    public static final String ACTION_LOCATION_UPDATE = "action-location-update";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(ACTION_LOCATION_UPDATE.equals(action)){

        }
    }
}
