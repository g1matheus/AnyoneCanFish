package com.homebrewforlife.sharkydart.anyonecanfish.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.homebrewforlife.sharkydart.anyonecanfish.MainActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class FishingWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOAST = "com.homebrewforlife.sharkydart.anyonecanfish.ACTION_TOAST";
    public static final String ACTION_GOBACK = "com.homebrewforlife.sharkydart.anyonecanfish.ACTION_GOBACK";
    public static final String THE_FISHING_FORECAST = "the-fishing-forecast-guess";
    public static final String MAKE_A_FISHING_EVENT = "com.homebrewforlife.sharkydart.anyonecanfish.widget.make-a-new-fishing-event";

    public static ForecastPeriod mMyForecastPeriod;

    public FishingWidgetProvider(){}

    @Override
    public void onReceive(Context theContext, Intent intent) {
        try{
            if(intent != null && intent.getAction() != null){
                if(intent.getAction().equals(ACTION_TOAST)) {
                    if (intent.hasExtra(THE_FISHING_FORECAST) && intent.getStringExtra(THE_FISHING_FORECAST) != null) {
                        String theFishingForecast = intent.getStringExtra(THE_FISHING_FORECAST);
                        Toast.makeText(theContext, "Fishing forecast: " + theFishingForecast, Toast.LENGTH_LONG).show();
                    }
/*
                        AppWidgetManager aWM = AppWidgetManager.getInstance(theContext);
                        int[] appWidgetIds = aWM.getAppWidgetIds(new ComponentName(theContext, FishingWidgetProvider.class));
                        RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), R.layout.fishing_widget_forecast_detail);
                        for (int widgetId : appWidgetIds) {
                            aWM.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
                        }
                        remoteView.setImageViewResource(R.id.widget_img_icon, R.drawable.hooked);
                        aWM.updateAppWidget(appWidgetIds, remoteView);
                    } else {
                        Toast.makeText(theContext, "no extra or arraylist NULL", Toast.LENGTH_LONG).show();
                    }
                }
                else if(intent.getAction().equals(ACTION_GOBACK)){
                    //mMyForecastPeriod empty the object? not necessary

                    AppWidgetManager aWM = AppWidgetManager.getInstance(theContext);
                    int[] appWidgetIds = aWM.getAppWidgetIds(new ComponentName(theContext, FishingWidgetProvider.class));
                    RemoteViews remoteView = new RemoteViews(theContext.getPackageName(), R.layout.fishing_widget_provider_layout);

                    Picasso.get()
                            .load(mMyForecastPeriod.getIconURL())
                            .into(remoteView, R.id.widgetForecastIcon, appWidgetIds);

                    for (int widgetId : appWidgetIds) {
                        aWM.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
                    }
                    remoteView.setImageViewResource(R.id.widget_img_icon, R.drawable.fish_leaping_icon_001);
                    aWM.updateAppWidget(appWidgetIds, remoteView);
*/
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            Log.d("fart", "something failed in onReceive");
        }
        super.onReceive(theContext, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fishing_widget_provider_layout);

            Intent raIntent = new Intent(context, FishingWidgetRVService.class);    //the service pushes it off to the factory, which handles the "setremoteadapter" call
            raIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            raIntent.setData(Uri.parse(raIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_list, raIntent);

            //set up the click for the logo image to open the app
            final Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent openMainPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_img_icon, openMainPendingIntent);

            //set up the click for the logo image to open the app
            final Intent fishEventIntent = new Intent(context, MainActivity.class);
            PendingIntent openAddFishEventPendingIntent = PendingIntent.getActivity(context, 0, fishEventIntent, 0);
            views.setOnClickPendingIntent(R.id.imgLogFishEvent, openAddFishEventPendingIntent);

            final Intent onFishClickIntent = new Intent(context, FishingWidgetProvider.class);
            onFishClickIntent.setData(Uri.parse(onFishClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
//            onFishClickIntent.setAction(MAKE_A_FISHING_EVENT);

            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onFishClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);
            Log.d("fart", "going through onUpdate");

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

