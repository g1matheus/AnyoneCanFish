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
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarData;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarPhenomena;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class GetSolunarDataTasks {
    public static final String ACTION_GET_SOLUNAR_DATA = "get-solunar-api-data";
    public static final String ACTION_FOUND_SOLUNAR_DATA = "found-solunar-api-data";

    public static final String EXTRA_SOLUNAR_DATE = "extra-solunar-date";
    public static final String EXTRA_SOLUNAR_LAT = "extra-solunar-lat";
    public static final String EXTRA_SOLUNAR_LON = "extra-solunar-lon";
    public static final String EXTRA_SOLUNAR_TZ = "extra-solunar-tz";

    public static final String EXTRA_THE_SOLUNAR_DATA = "extra-the-solunar-data";


    public static void querySolunarApiTask(Context theContext, String theAction, String theDate, String theCoords, int tz){
        if(ACTION_GET_SOLUNAR_DATA.equals(theAction)){
            //get the solunar data
            makeSolunarAPIQuery(theContext, theDate, theCoords, tz);
        }
    }

    //actually performing the query
    private static void makeSolunarAPIQuery(final Context theContext, String theDate, String theCoords, int tz){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    URL solunarApiUrl = buildSolunarApiUrl(theContext, theDate, theCoords, tz);
                    try {
                        //rejected due to self-signed certificate...
                        //String response = getResponseFromUrl(solunarApiUrl);

                        //attempting to get around certificate issue...
                        String response = dealWithInvalidCertificateIssue(theContext, solunarApiUrl);

                        parseSolunarApiResponseJSON(theContext, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("fart", "solunar api json data parse bork:" + e.getLocalizedMessage());
                    }
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    //url formatting
    private static URL buildSolunarApiUrl(Context theContext, String theDate, String theCoords, int tz) {
        //  Example API call
        //https://api.usno.navy.mil/rstt/oneday?date=11/29/2018&coords=39.065952,-84.479897&tz=-5

        Uri builtUri = Uri.parse(theContext.getString(R.string.solunar_api_baseurl)
                + "?" + theContext.getString(R.string.solunar_param_date) + "=" + theDate
                + "&" + theContext.getString(R.string.solunar_param_coords) + "=" + theCoords
                + "&" +theContext.getString(R.string.solunar_param_timezone) + "=" + Integer.toString(tz));
        Log.i("fart", "BuiltUri: " + builtUri.toString());
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

    private static void parseSolunarApiResponseJSON(final Context theContext, String solunarJSONData){
        if (solunarJSONData != null) {
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

                saveRawSolunarDataStringToSharedPrefs(theContext, solunarJSONData);
                // - send the message back to the receiver, so it populates views using the forecast data
                sendSolunarDataBack(solunarData, theContext);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(theContext, "Solunar Data Issue", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(theContext, "Solunar Network Issue #2", Toast.LENGTH_LONG).show();
        }
    }
    private static void sendSolunarDataBack(SolunarData theSolunarData, Context context){
        Intent solunarApiIntent = new Intent(GetSolunarDataTasks.ACTION_FOUND_SOLUNAR_DATA);
        solunarApiIntent.putExtra(GetSolunarDataTasks.EXTRA_THE_SOLUNAR_DATA, theSolunarData);
        context.getApplicationContext().sendBroadcast(solunarApiIntent);
    }

    private static void saveRawSolunarDataStringToSharedPrefs(Context theContext, String theRawSolunarData){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(theContext).edit();
        editor.putString(MainActivity.RAW_SOLUNAR_DATA_SHAREDPREFS_CACHE, theRawSolunarData);
        editor.apply();
    }

    //required because whoever is in charge of the certs is slipping
    private static String dealWithInvalidCertificateIssue(Context theContext, URL theUrl) throws IOException{
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // http://crl.disa.mil/sign/DODIDSWCA_38.cer
//            InputStream caInput = new BufferedInputStream(new FileInputStream("http://crl.disa.mil/sign/DODIDSWCA_38.cer"));
            InputStream caInput = theContext.getResources().openRawResource(R.raw.dodidswca_38);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            HttpsURLConnection urlConnection = (HttpsURLConnection) theUrl.openConnection();
            try {
                urlConnection.setSSLSocketFactory(context.getSocketFactory());
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
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
