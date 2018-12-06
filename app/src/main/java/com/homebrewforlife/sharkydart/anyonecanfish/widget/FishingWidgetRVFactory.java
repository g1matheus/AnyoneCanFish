package com.homebrewforlife.sharkydart.anyonecanfish.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.homebrewforlife.sharkydart.anyonecanfish.MainActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import static com.homebrewforlife.sharkydart.anyonecanfish.handwavyfishingmagic.OptimusCalculatron.*;
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarData;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarPhenomena;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FishingWidgetRVFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<ForecastPeriod> myForecastObj = new ArrayList<>();
    private SolunarData mySolunarObj = new SolunarData();

    private Context theContext;

    FishingWidgetRVFactory(Context context, Intent intent) {
        this.theContext = context;
    }

    @Override
    public int getCount() {
        int theReturnCount;
//        if(FishingWidgetProvider.mMyForecastPeriod != null)
//            theReturnCount = FishingWidgetProvider.mTheIngredients.size();
//        else
            theReturnCount = myForecastObj.size();
        return theReturnCount;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }


    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView;

        remoteView = new RemoteViews(theContext.getPackageName(), android.R.layout.simple_list_item_1);

        AppWidgetManager aWM = AppWidgetManager.getInstance(theContext);
        int[] appWidgetIds = aWM.getAppWidgetIds(new ComponentName(theContext, FishingWidgetProvider.class));

        String temp_withUnits = Math.round(myForecastObj.get(position).getTemperature()) + " " + myForecastObj.get(position).getTemperatureUnit();
        String tempUnitsToday = myForecastObj.get(position).getName() + ":\n"
                + "High: " + temp_withUnits + "\n"
                + "Wind: " + myForecastObj.get(position).getWindSpeed() + "\n"
                + "From: " + myForecastObj.get(position).getWindDirection();
        remoteView.setTextViewText(android.R.id.text1, tempUnitsToday);
        remoteView.setViewPadding(android.R.id.text1, 16, 16, 16,16);
        remoteView.setTextColor(android.R.id.text1, theContext.getResources().getColor(R.color.ink_a800));

        //adds information unique to the view item at the position into a bundle, which is put in the intent for the list item
        // ...defining the unique action
        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(FishingWidgetProvider.ACTION_TOAST);

        final Bundle theBundle = new Bundle();
        String wordsOfGuidance = pleaseSendWordsOfGuidance(theContext, myForecastObj.get(position), mySolunarObj);
        theBundle.putString(FishingWidgetProvider.THE_FISHING_FORECAST, wordsOfGuidance); //myForecastObj.get(position));

        fillInIntent.putExtras(theBundle);
        remoteView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return remoteView;
    }

    @Override
    public void onCreate() {
        if(FishingWidgetProvider.mMyForecastPeriod == null || myForecastObj == null)
            initData();
    }

    @Override
    public void onDataSetChanged() {
        if(FishingWidgetProvider.mMyForecastPeriod == null || myForecastObj == null)
            initData();
    }

    private void initData(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(theContext);
        String theForecastD = sharedPref.getString(MainActivity.RAW_FORECAST_DATA_SHAREDPREFS_CACHE, "A widget doesn't have a forecast.");
        String theSolunarD = sharedPref.getString(MainActivity.RAW_SOLUNAR_DATA_SHAREDPREFS_CACHE, "A widget doesn't have a sun or a moon.");
        Log.i("fart", "widget got info from sharedprefs");
        myForecastObj = parseLocalWeatherForecastJSON(theForecastD);
        mySolunarObj = parseSolunarApiResponseJSON(theSolunarD);
    }

    private ArrayList<ForecastPeriod> parseLocalWeatherForecastJSON(String forecastApiData){
        if (forecastApiData != null && !forecastApiData.isEmpty()) {
            try {
                //try and find JUST the periods data
                JSONObject forecastPropertiesObj = new JSONObject(forecastApiData).getJSONObject("properties");
                JSONArray pArray = forecastPropertiesObj.getJSONArray("periods");
                ArrayList<ForecastPeriod> theForecast = new ArrayList<>();
                for(int i=0; i<pArray.length(); i++){
                    ForecastPeriod tPeriod = new ForecastPeriod();
                    tPeriod.setNumber(pArray.getJSONObject(i).getInt("number"));
                    tPeriod.setName(pArray.getJSONObject(i).getString("name"));
                    tPeriod.setStartTime(pArray.getJSONObject(i).getString("startTime"));
                    tPeriod.setEndTime(pArray.getJSONObject(i).getString("endTime"));
                    tPeriod.setIsDayTime(pArray.getJSONObject(i).getBoolean("isDaytime"));
                    tPeriod.setTemperature(pArray.getJSONObject(i).getDouble("temperature"));
                    tPeriod.setTemperatureUnit(pArray.getJSONObject(i).getString("temperatureUnit"));
                    tPeriod.setWindSpeed(pArray.getJSONObject(i).getString("windSpeed"));
                    tPeriod.setWindDirection(pArray.getJSONObject(i).getString("windDirection"));
                    tPeriod.setIconURL(pArray.getJSONObject(i).getString("icon"));
                    tPeriod.setShortForecast(pArray.getJSONObject(i).getString("shortForecast"));
                    tPeriod.setDetailedForecast(pArray.getJSONObject(i).getString("detailedForecast"));
                    theForecast.add(tPeriod);
                }
                return theForecast;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("fart", "error");
        }
        return null;
    }
    private SolunarData parseSolunarApiResponseJSON(String solunarJSONData){
        if (solunarJSONData != null && !solunarJSONData.isEmpty()) {
            try {
                SolunarData solunarData = new SolunarData();
                //try and find the data
                JSONObject solDataObj = new JSONObject(solunarJSONData);

                JSONArray sunArray = solDataObj.getJSONArray("sundata");
                ArrayList<SolunarPhenomena> theSundata = new ArrayList<>();
                for(int i=0; i<sunArray.length(); i++){
                    SolunarPhenomena tPheno = new SolunarPhenomena();
                    tPheno.setPhen(sunArray.getJSONObject(i).getString("phen"));
                    tPheno.setTime(sunArray.getJSONObject(i).getString("time"));
                    theSundata.add(tPheno);
                }
                solunarData.setSundata(theSundata);

                JSONArray moonArray = solDataObj.getJSONArray("moondata");
                ArrayList<SolunarPhenomena> theMoondata = new ArrayList<>();
                for(int i=0; i<moonArray.length(); i++){
                    SolunarPhenomena tPheno = new SolunarPhenomena();
                    tPheno.setPhen(moonArray.getJSONObject(i).getString("phen"));
                    tPheno.setTime(moonArray.getJSONObject(i).getString("time"));
                    theMoondata.add(tPheno);
                }
                solunarData.setMoondata(theMoondata);

                String dayofweek = solDataObj.getString("dayofweek");
                solunarData.setDayOfWeek(dayofweek);
                String closestPhase = solDataObj.getJSONObject("closestphase").getString("phase");
                solunarData.setClosestPhase(closestPhase);

                //there might not be a fracillum
                String fracillum = "";
                if(solDataObj.has("fracillum"))
                    fracillum = solDataObj.getString("fracillum");
                solunarData.setFracillum(fracillum);

                //there might not be a curphase
                String curphase = "";
                if(solDataObj.has("curphase"))
                    curphase = solDataObj.getString("curphase");
                solunarData.setCurphase(curphase);

                return solunarData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("fart", "solunar data issue");
        }
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
