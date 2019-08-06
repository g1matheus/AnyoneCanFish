package com.homebrewforlife.sharkydart.anyonecanfish;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.ForecastRvAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_GameFish;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;
import com.homebrewforlife.sharkydart.anyonecanfish.models.ForecastPeriod;
import com.homebrewforlife.sharkydart.anyonecanfish.models.SolunarData;
import com.homebrewforlife.sharkydart.anyonecanfish.services.GetForecastDataService;
import com.homebrewforlife.sharkydart.anyonecanfish.services.GetForecastDataTasks;
import com.homebrewforlife.sharkydart.anyonecanfish.services.GetSolunarDataService;
import com.homebrewforlife.sharkydart.anyonecanfish.services.GetSolunarDataTasks;
import com.homebrewforlife.sharkydart.anyonecanfish.services.LocationService;
import com.homebrewforlife.sharkydart.anyonecanfish.services.LocationTasks;
import com.homebrewforlife.sharkydart.anyonecanfish.services.WeatherInfoService;
import com.homebrewforlife.sharkydart.anyonecanfish.services.WeatherInfoTasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{
    private static final String LOG_TAG = "fart.Main";
    private int RC_SIGN_IN = 0;   //request code
    private final int RC_SWEET_PERMISSIONS = 1337;
    private MenuItem mMenuSign;
    private Context mContext;

    //keys
    public static final String SAVE_SOLUNAR_ARRAY = "com.homebrewforlife.sharkydart.anyonecanfish.mainactivity.the-solunar-parcelble";
    public static final String SAVE_FORECAST_PERIODS_ARRAY = "com.homebrewforlife.sharkydart.anyonecanfish.mainactivity.the-forecast-periods-array-list";
    public static final String SAVE_GAME_FISH_ARRAY = "com.homebrewforlife.sharkydart.anyonecanfish.mainactivity.gamefish-arraylist";
    public static final String SAVE_TRIPS_ARRAY = "com.homebrewforlife.sharkydart.anyonecanfish.mainactivity.trips-arraylist";
    public static final String SAVE_TACKLEBOXES_ARRAY = "com.homebrewforlife.sharkydart.anyonecanfish.mainactivity.tackleboxes-arraylist";

    //    public static final String GPS_SHAREDPREFS_CACHE = "gps-sharedpreferences-cache";
    public static final String FORECAST_URL_SHAREDPREFS_CACHE = "last-cached-forecast-url";
    public static final String CITY_SHAREDPREFS_CACHE = "last-cached-forecast-city";
    public static final String SHAREDPREFS_LAT = "sharedpreferences-lat";
    public static final String SHAREDPREFS_LON = "sharedpreferences-lon";
    public static final String RAW_FORECAST_DATA_SHAREDPREFS_CACHE = "raw-forecast-data-cache";
    public static final String RAW_SOLUNAR_DATA_SHAREDPREFS_CACHE = "raw-solunar-data-cache";
    SharedPreferences mSharedPreferences;

    //Receivers and Intent Filters
    MainLocationReceiver mLocReceiver;
    IntentFilter mLocReceiverFilter;
    MainWeatherFirstReceiver mWeatherFirstReceiver;
    IntentFilter mWeatherFirstReceiverFilter;
    MainForecastReceiver mForecastReceiver;
    IntentFilter mForecastReceiverFilter;
    MainSolunarReceiver mSolunarReceiver;
    IntentFilter mSolunarReceiverFilter;

    //Firebase Authentication
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser mCurUser;

    FirestoreStuff myFirestoreStuff;
    //Firestore Database Reference
    FirebaseFirestore mFS_Store;
//    private DatabaseReference mMessagesDatabaseReference;
//    private ChildEventListener  mChildEventListener;
    DocumentReference mFS_User_document_ref;

    //Solunar Data
    SolunarData mSolunarDataObj;

    //weather recyclerView
    ArrayList<ForecastPeriod> mForecastPeriodsArrayList;
    ForecastRvAdapter mForecastRvAdapter;
    RecyclerView mForecastRecyclerView;
    Bundle mTheIntanceState;

    //Arraylists to send to child activities
    ArrayList<Fire_GameFish> mGameFishArrayList;
    public static final String GAME_FISH_ARRAYLIST = "game-fish-array-list";
    ArrayList<Fire_Trip> mFishingTripsArray;
    public static final String FISHING_TRIPS_ARRAYLIST = "fishing-trips-array-list";
    ArrayList<Fire_TackleBox> mTackleBoxesArray;
    public static final String TACKLE_BOXES_ARRAYLIST = "tackle-boxes-array-list";
    public static final String FIRESTORE_USER = "firestore-auth-user-obj";

    //console message from firestore warned to include this code
    private void FirestoreWarningFromDevs(){
        try {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .build();
            firestore.setFirestoreSettings(settings);
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.theMainToolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        mTheIntanceState = savedInstanceState;

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser theUser = firebaseAuth.getCurrentUser();
                if(theUser != null){
                    //user is signed in
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_LONG).show();
                    onSignedInInitialize(theUser);
                }
                else{
                    //User is signed out
                    onSignedOutCleanup();

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    );
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        //verify location permissions and start getting location and weather data
        verifyLocationPermissions(false);

        if(savedInstanceState == null){
            mForecastPeriodsArrayList = new ArrayList<>();
        }else{
            mSolunarDataObj = savedInstanceState.getParcelable(SAVE_SOLUNAR_ARRAY);
            mForecastPeriodsArrayList = savedInstanceState.getParcelableArrayList(SAVE_FORECAST_PERIODS_ARRAY);
            mGameFishArrayList = savedInstanceState.getParcelableArrayList(SAVE_GAME_FISH_ARRAY);
            mFishingTripsArray = savedInstanceState.getParcelableArrayList(SAVE_TRIPS_ARRAY);
            mTackleBoxesArray = savedInstanceState.getParcelableArrayList(SAVE_TACKLEBOXES_ARRAY);
        }

        mForecastRecyclerView = findViewById(R.id.rvWeatherDays);
        assert mForecastRecyclerView != null;
        mForecastRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
//        mForecastRecyclerView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(mForecastRecyclerView);
//        setupRecyclerView(mForecastRecyclerView);

        //TODO - Trip creation will be added in a future release.
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create a trip", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_FORECAST_PERIODS_ARRAY, mForecastPeriodsArrayList);
        outState.putParcelable(SAVE_SOLUNAR_ARRAY, mSolunarDataObj);
        outState.putParcelableArrayList(SAVE_GAME_FISH_ARRAY,mGameFishArrayList);
        outState.putParcelableArrayList(SAVE_TRIPS_ARRAY,mFishingTripsArray);
        outState.putParcelableArrayList(SAVE_TACKLEBOXES_ARRAY,mTackleBoxesArray);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSolunarDataObj = savedInstanceState.getParcelable(SAVE_SOLUNAR_ARRAY);
        mForecastPeriodsArrayList = savedInstanceState.getParcelableArrayList(SAVE_FORECAST_PERIODS_ARRAY);
        mGameFishArrayList = savedInstanceState.getParcelableArrayList(SAVE_GAME_FISH_ARRAY);
        mFishingTripsArray = savedInstanceState.getParcelableArrayList(SAVE_TRIPS_ARRAY);
        mTackleBoxesArray = savedInstanceState.getParcelableArrayList(SAVE_TACKLEBOXES_ARRAY);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mForecastRvAdapter = new ForecastRvAdapter(this, mForecastPeriodsArrayList, mSolunarDataObj);
        recyclerView.setAdapter(mForecastRvAdapter);
    }
    private void setupOnClickListenersThatDependOnFirestore(){
        //handle the clicks
        findViewById(R.id.mcFishingBasics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FishingBasicsActivity.class);
                intent.putExtra(FIRESTORE_USER, myFirestoreStuff.getmCurUser());
                //intent.putExtra(/* name of data */, /* data */);
                view.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.mcGameFish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameFishActivity.class);
                intent.putParcelableArrayListExtra(GAME_FISH_ARRAYLIST, mGameFishArrayList);
                intent.putExtra(FIRESTORE_USER, myFirestoreStuff.getmCurUser());
                view.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.mcFishingTrips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FishingTripsActivity.class);
                intent.putParcelableArrayListExtra(FISHING_TRIPS_ARRAYLIST, mFishingTripsArray);
                intent.putExtra(FIRESTORE_USER, myFirestoreStuff.getmCurUser());
                view.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.mcTackleBoxes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TackleBoxesActivity.class);
                intent.putParcelableArrayListExtra(TACKLE_BOXES_ARRAYLIST, mTackleBoxesArray);
                intent.putExtra(FIRESTORE_USER, myFirestoreStuff.getmCurUser());
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    private void onSignedInInitialize(FirebaseUser theUser){
        mCurUser = theUser;
        //FirebaseSignIn();
        Firestore_LoadData();
//        attachDatabaseReadListener();
    }
    private void onSignedOutCleanup(){
        //unset mCurUser
        mCurUser = null;
        //unset any other auth-dependent info
//        detachDatabaseReadListener();
    }

    private void verifyLocationPermissions(boolean forceUpdate){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    RC_SWEET_PERMISSIONS);
        }
        else{
            //try and get GPS coords, and send them back to get the weather
            GeoPoint coords = getCoordsFromSharedPrefs();
            if(coords == null) {
                Log.d(LOG_TAG, "no coords in shared prefs");
                startGettingLatLon();
            }
            else {   //use shared prefs to refresh weather, unless forceUpdate, in which case - get GPS coords again
                if(forceUpdate){     //button was clicked to refresh GPS
                    Log.d(LOG_TAG, "button was clicked to refresh");
                    startGettingLatLon();
                }
                else {    //button was not clicked to refresh
                    Log.d(LOG_TAG, "button not clicked to refresh, and have coords already");
                    String prefsForecastURL = getForecastURLFromSharedPrefs();
                    if(prefsForecastURL != null)
                        startGettingForecastData(prefsForecastURL);
                    else
                        startGettingFirstWeather(coords.getLatitude(), coords.getLongitude());
                }
            }
        }
    }
    private void saveCoordsToSharedPrefs(double lat, double lon){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SHAREDPREFS_LAT, Double.toString(lat));
        editor.putString(SHAREDPREFS_LON, Double.toString(lon));
        editor.apply();
    }
    public static GeoPoint getCoordsFromSharedPrefs(Context theContext){
        Log.d(LOG_TAG, "sent context for prefs...");
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(theContext);
        String lat = mSharedPreferences.getString(SHAREDPREFS_LAT, null);
        String lon = mSharedPreferences.getString(SHAREDPREFS_LON, null);
        Log.d(LOG_TAG, "[SHARED PREFS]: lat=" + lat + " lon=" + lon );
        GeoPoint coords;
        if(lat != null && lon != null)
            coords = new GeoPoint(Double.valueOf(lat), Double.valueOf(lon));
        else
            coords = null;
        return coords;
    }
    private GeoPoint getCoordsFromSharedPrefs(){
        Log.d(LOG_TAG, "using _mContext_ for prefs...");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String lat = mSharedPreferences.getString(SHAREDPREFS_LAT, null);
        String lon = mSharedPreferences.getString(SHAREDPREFS_LON, null);
        Log.d(LOG_TAG, "lat:" + lat + " lon:" + lon );
        GeoPoint coords;
        if(lat != null && lon != null)
            coords = new GeoPoint(Double.valueOf(lat), Double.valueOf(lon));
        else
            coords = null;
        return coords;
    }
    private void saveForecastURLAndCityToSharedPrefs(String forecasturl, String city){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(FORECAST_URL_SHAREDPREFS_CACHE, forecasturl);
        editor.putString(CITY_SHAREDPREFS_CACHE, city);
        editor.apply();
    }
    public String getForecastURLFromSharedPrefs(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mSharedPreferences.getString(FORECAST_URL_SHAREDPREFS_CACHE, null);
    }
    public String getForecastCITYFromSharedPrefs(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mSharedPreferences.getString(CITY_SHAREDPREFS_CACHE, null);
    }

    private void startGettingLatLon(){
        // TODO: figure out why mLocReceiver and MainLocationReceiver don't seem to be starting the process of loading Solunar data
//        mLocReceiverFilter = new IntentFilter();
//        mLocReceiverFilter.addAction(LocationTasks.ACTION_FOUND_GPS_LOCATION);
//        mLocReceiver = new MainLocationReceiver();
//        mContext.registerReceiver(mLocReceiver, mLocReceiverFilter);

        Log.d(LOG_TAG, "[startService] - {getLatLonIntent}");
        Intent getLatLonIntent = new Intent(this, LocationService.class);
        getLatLonIntent.setAction(LocationTasks.ACTION_GET_GPS_LOCATION);
        startService(getLatLonIntent);
    }
    private void startGettingFirstWeather(double lat, double lon){
//        mWeatherFirstReceiverFilter = new IntentFilter();
//        mWeatherFirstReceiverFilter.addAction(WeatherInfoTasks.ACTION_FOUND_WEATHER_FORECAST_API);
//        mWeatherFirstReceiver = new MainWeatherFirstReceiver();
//        mContext.registerReceiver(mWeatherFirstReceiver, mWeatherFirstReceiverFilter);

        Log.d(LOG_TAG, "[startService] - {getWeatherFirstIntent}");
        Intent getWeatherFirstIntent = new Intent(this, WeatherInfoService.class);
        getWeatherFirstIntent.putExtra(LocationTasks.EXTRA_LATITUDE, lat);
        getWeatherFirstIntent.putExtra(LocationTasks.EXTRA_LONGITUDE, lon);
        getWeatherFirstIntent.setAction(WeatherInfoTasks.ACTION_GET_WEATHER_FORECAST);
        startService(getWeatherFirstIntent);
    }
    private void startGettingForecastData(String theForecastApiUrl){
        Log.d(LOG_TAG, "forecast apiurl: " + theForecastApiUrl);
//        mForecastReceiverFilter = new IntentFilter();
//        mForecastReceiverFilter.addAction(GetForecastDataTasks.ACTION_FOUND_FORECAST_DATA);
//        mForecastReceiver = new MainForecastReceiver();
//        mContext.registerReceiver(mForecastReceiver, mForecastReceiverFilter);

        Log.d(LOG_TAG, "[startService] - {getForecastIntent}");
        Intent getForecastIntent = new Intent(this, GetForecastDataService.class);
        getForecastIntent.putExtra(GetForecastDataTasks.EXTRA_THE_FORECAST_API_URL, theForecastApiUrl);
        getForecastIntent.setAction(GetForecastDataTasks.ACTION_GET_FORECAST_DATA);
        startService(getForecastIntent);
    }
    private void startGettingSolunarData(String theDate, double theLat, double theLon, int theTimeZone){
//        mSolunarReceiverFilter = new IntentFilter();
//        mSolunarReceiverFilter.addAction(GetSolunarDataTasks.ACTION_FOUND_SOLUNAR_DATA);
//        mSolunarReceiver = new MainSolunarReceiver();
//        mContext.registerReceiver(mSolunarReceiver, mSolunarReceiverFilter);

        Log.d(LOG_TAG, "[startService] - {getSolunarIntent}");
        Intent getSolunarIntent = new Intent(this, GetSolunarDataService.class);
        getSolunarIntent.putExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_DATE, theDate);
        getSolunarIntent.putExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_LAT, theLat);
        getSolunarIntent.putExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_LON, theLon);
        getSolunarIntent.putExtra(GetSolunarDataTasks.EXTRA_SOLUNAR_TZ, theTimeZone);
        getSolunarIntent.setAction(GetSolunarDataTasks.ACTION_GET_SOLUNAR_DATA);
        startService(getSolunarIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case RC_SWEET_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startGettingLatLon();
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Firebase Functions
    private void FirebaseSignOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext,"You have been signed out.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void FirebaseGetUserInfo(FirebaseUser theCurUser){
        //FirebaseUser user = mAuth.getCurrentUser();
        try {
            if(theCurUser != null) {
                Log.d(LOG_TAG, "Name: " + theCurUser.getDisplayName()
                        + " Email: " + theCurUser.getEmail()
                        + " UID: " + theCurUser.getUid());
                if(theCurUser.getDisplayName() == null)
                    FirebaseUpdateUserInfo(theCurUser);   //just to set a name, if there is none

                //make a user account in the DB, and also a tacklebox
                FirestoreAdds.addFS_user(mContext, new Fire_User(theCurUser), mFS_Store);
                //mFS_User_document_ref = mFS_Store.collection(getString(R.string.db_users)).document(theCurUser.getUid());
            }
        }
        catch(NullPointerException np){
            np.printStackTrace();
        }
    }
    private void FirebaseUpdateUserInfo(final FirebaseUser theUser){
        if(theUser.getDisplayName() != null && !theUser.getDisplayName().isEmpty())
            return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Curious Angler")
//                .setPhotoUri(Uri.parse(""))
                .build();

        theUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "User profile updated.");
                            Log.d(LOG_TAG, "Name: " + theUser.getDisplayName()
                                    + " Email: " + theUser.getEmail()
                                    + " UID: " + theUser.getUid());
                        }
                    }
                });
    }
    private void Firestore_LoadData(){
        //firestore references
        mFS_Store = FirebaseFirestore.getInstance();
        //get specifically user firestore db data
        FirebaseGetUserInfo(mCurUser);
        myFirestoreStuff = new FirestoreStuff(mContext,mCurUser,mFS_Store);

        //get specifically game_fish firestore db data
        mGameFishArrayList = new ArrayList<Fire_GameFish>();
        myFirestoreStuff.Firestore_Get_GameFish(mGameFishArrayList);

        //get specifically FishingTrips firestore db data
        mFishingTripsArray = new ArrayList<Fire_Trip>();
        myFirestoreStuff.Firestore_Get_FishingTrips(mFishingTripsArray);

        //get specifically TackleBoxes firestore db data
        mTackleBoxesArray = new ArrayList<Fire_TackleBox>();
        myFirestoreStuff.Firestore_Get_TackleBoxes(mTackleBoxesArray);

        setupOnClickListenersThatDependOnFirestore();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirestoreWarningFromDevs();
                // Successfully signed in
                Firestore_LoadData();
            }
            else {
                if(response == null || resultCode == Activity.RESULT_CANCELED) {
                    Log.d(LOG_TAG, "User cancelled sign in");
                    return;
                }
                if(response.getError() == null)
                    return;
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(findViewById(android.R.id.content), "No Network!", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d(LOG_TAG, "Sign-in failed");
                finish();
            }
        }
    }

    //menu stuff
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_open_map) {
            return true;
        }
        else if(id == R.id.forcesync_forecast_data){
            //forcing a GPS refresh
            verifyLocationPermissions(true);
            Toast.makeText(mContext,"Syncing...", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_firebase_signout){
            //disable the menu button
            item.setEnabled(false);
            //sign out of Firebase
            FirebaseSignOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //receiver that changes location displayed when triggered
    private class MainLocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "MainLocationReceiver Received Something...");
            String action = intent.getAction();
            if(LocationTasks.ACTION_FOUND_GPS_LOCATION.equals(action)){
                double theLat, theLon;
                theLat = intent.getDoubleExtra(LocationTasks.EXTRA_LATITUDE, -99);
                theLon = intent.getDoubleExtra(LocationTasks.EXTRA_LONGITUDE, -99);
                saveCoordsToSharedPrefs(theLat, theLon);
                startGettingFirstWeather(theLat, theLon);

                //need to get current date/time and simplify it
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
                String theDate = df.format(c);
                Log.d(LOG_TAG, "thedate: " + theDate);

                //need current timezone offset, from milliseconds to hours, as integer
                int theTimeZone = TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60;
                Log.d(LOG_TAG, "timezoneoffset: " + theTimeZone);
                Log.d(LOG_TAG, "*** start trying to get Solunar data ***");
                startGettingSolunarData(theDate, theLat, theLon, theTimeZone);
            }
            else{
                Log.d(LOG_TAG, "-not found_gps action-: " + action);
            }
        }
    }
    //receiver that gets the result from sending location data to the first weather api
    private class MainWeatherFirstReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "[[Weather First]] Receiver Received Something...");
            String action = intent.getAction();
            if(WeatherInfoTasks.ACTION_FOUND_WEATHER_FORECAST_API.equals(action)){
                String theWeatherAPIURL, theCity;
                theWeatherAPIURL = intent.getStringExtra(WeatherInfoTasks.EXTRA_FORECAST_API_URL);
                theCity = intent.getStringExtra(WeatherInfoTasks.EXTRA_CITY);

                saveForecastURLAndCityToSharedPrefs(theWeatherAPIURL, theCity);
                startGettingForecastData(theWeatherAPIURL);
            }
            else{
                Log.d(LOG_TAG, "broadcast: " + action);
            }
        }
    }
    //MainForecastReceiver
    private class MainForecastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "[[Forecast API call]] Receiver Received Something...");
            String action = intent.getAction();
            if(GetForecastDataTasks.ACTION_FOUND_FORECAST_DATA.equals(action)){
                mForecastPeriodsArrayList = intent.getParcelableArrayListExtra(GetForecastDataTasks.EXTRA_THE_FORECAST_DATA);
                setupRecyclerView(mForecastRecyclerView);
            }
            else{
                Log.d(LOG_TAG, "broadcast: " + action);
            }
        }
    }
    //MainSolunarReceiver
    private class MainSolunarReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "[[Solunar API call]] Receiver Received Something...");
            String action = intent.getAction();
            if(GetSolunarDataTasks.ACTION_FOUND_SOLUNAR_DATA.equals(action)){
                mSolunarDataObj = intent.getParcelableExtra(GetSolunarDataTasks.EXTRA_THE_SOLUNAR_DATA);
                setupRecyclerView(mForecastRecyclerView);
            }
            else{
                Log.d(LOG_TAG, "broadcast: " + action);
            }
        }
    }

    private void letsRegisterOurReceivers(){
        //startGettingLatLong:
        mLocReceiverFilter = new IntentFilter();
        mLocReceiverFilter.addAction(LocationTasks.ACTION_FOUND_GPS_LOCATION);
        mLocReceiver = new MainLocationReceiver();
        mContext.registerReceiver(mLocReceiver, mLocReceiverFilter);
        Log.d(LOG_TAG, "[registered receiver][mLocReceiver]");

        //startGettingFirstWeather: 389
        mWeatherFirstReceiverFilter = new IntentFilter();
        mWeatherFirstReceiverFilter.addAction(WeatherInfoTasks.ACTION_FOUND_WEATHER_FORECAST_API);
        mWeatherFirstReceiver = new MainWeatherFirstReceiver();
        mContext.registerReceiver(mWeatherFirstReceiver, mWeatherFirstReceiverFilter);
        Log.d(LOG_TAG, "[registered receiver][mWeatherFirstReceiver]");

        //startGettingForecastData
        mForecastReceiverFilter = new IntentFilter();
        mForecastReceiverFilter.addAction(GetForecastDataTasks.ACTION_FOUND_FORECAST_DATA);
        mForecastReceiver = new MainForecastReceiver();
        mContext.registerReceiver(mForecastReceiver, mForecastReceiverFilter);
        Log.d(LOG_TAG, "[registered receiver][mForecastReceiver]");

        //startGettingSolunarData
        mSolunarReceiverFilter = new IntentFilter();
        mSolunarReceiverFilter.addAction(GetSolunarDataTasks.ACTION_FOUND_SOLUNAR_DATA);
        mSolunarReceiver = new MainSolunarReceiver();
        mContext.registerReceiver(mSolunarReceiver, mSolunarReceiverFilter);
        Log.d(LOG_TAG, "[registered receiver][mSolunarReceiver]");
    }

    @Override
    protected void onResume() {
        super.onResume();
        letsRegisterOurReceivers();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void letsUnregisterOurReceivers(){
        try {
            unregisterReceiver(mForecastReceiver);
            unregisterReceiver(mLocReceiver);
            unregisterReceiver(mWeatherFirstReceiver);
            unregisterReceiver(mSolunarReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

        letsUnregisterOurReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d(LOG_TAG, "onDestroy called");
//        try {
//            unregisterReceiver(mForecastReceiver);
//            unregisterReceiver(mLocReceiver);
//            unregisterReceiver(mWeatherFirstReceiver);
//            unregisterReceiver(mSolunarReceiver);
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }
    }

    /*
    private static final int RC_PHOTO_PICKER =  2;

    // ImagePickerButton shows an image picker to upload a image for a message
    mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        }
    });
*/


















}
