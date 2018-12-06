package com.homebrewforlife.sharkydart.anyonecanfish.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.homebrewforlife.sharkydart.anyonecanfish.MainActivity;
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GetForecastDataTasks {
    public static final String ACTION_GET_FORECAST_DATA = "get-forecast-api-data";
    public static final String ACTION_FOUND_FORECAST_DATA = "found-forecast-api-data";
    public static final String EXTRA_THE_FORECAST_API_URL = "extra-the-forecast-api-url";
    public static final String EXTRA_THE_FORECAST_DATA = "extra-the-forecast-data";


    public static void queryThisForecastApiTask(Context theContext, String theAction, String theForecastApiUrlString){
        if(ACTION_GET_FORECAST_DATA.equals(theAction)){
            //get the current gps location
            makeForecastAPIQuery(theContext, theForecastApiUrlString);
        }
    }
    //url formatting
    private static URL buildForecastAPIUrl(String theForecastApiUrlString) {
        //  Example API call
        //https://api.weather.gov/gridpoints/ILN/36,36/forecast

        Uri builtUri = Uri.parse(theForecastApiUrlString);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    private static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    //actually performing the query
    private static void makeForecastAPIQuery(final Context theContext, String theForecastApiUrlString){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    URL forecastAPIUrl = buildForecastAPIUrl(theForecastApiUrlString);
                    try {
                        String response = getResponseFromUrl(forecastAPIUrl);
                        parseLocalWeatherForecastJSON(theContext, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("fart", "forecast api json data parse bork:" + e.toString());
                    }
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    private static void parseLocalWeatherForecastJSON(final Context theContext, String forecastApiData){
        if (forecastApiData != null) {
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
                //save forecastURL to sharedpreferences
                saveRawForecastDataStringToSharedPrefs(theContext, forecastApiData);

                // - send the message back to the receiver, so it populates views using the forecast data
                sendForecastDataBack(theForecast, theContext);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(theContext, "First Weather Data Issue", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(theContext, "First Weather Network #2", Toast.LENGTH_LONG).show();
        }
    }
    private static void sendForecastDataBack(ArrayList<ForecastPeriod> theForecastApiUrl, Context context){
        Intent forecastApiIntent = new Intent(GetForecastDataTasks.ACTION_FOUND_FORECAST_DATA);
        forecastApiIntent.putParcelableArrayListExtra(GetForecastDataTasks.EXTRA_THE_FORECAST_DATA, theForecastApiUrl);
        context.getApplicationContext().sendBroadcast(forecastApiIntent);
    }

    private static void saveRawForecastDataStringToSharedPrefs(Context theContext, String theRawForecastData){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(theContext).edit();
        editor.putString(MainActivity.RAW_FORECAST_DATA_SHAREDPREFS_CACHE, theRawForecastData);
        editor.apply();
    }

}
