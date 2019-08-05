package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.TripsRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.homebrewforlife.sharkydart.anyonecanfish.MainActivity.SHAREDPREFS_LAT;
import static com.homebrewforlife.sharkydart.anyonecanfish.MainActivity.SHAREDPREFS_LON;

public class FishingTripsActivity extends AppCompatActivity {

    public static final String FISHEVENT_ARRAYLIST = "homebrew-sharkydart-fishing-event";
    public static final String THE_TRIP = "homebrew-sharkydart-fishing-trip";

    private Context mContext;
    ArrayList<Fire_Trip> mTripsArrayList;
    TripsRVAdapter mTripsRVAdapter;
    RecyclerView mTripsRV;

    private FirebaseAuth mAuthObj;
    private EventListener<QuerySnapshot> mFsTripsEventListener;
    private ListenerRegistration theRegistration;
    private CollectionReference mFsTripsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_trips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mContext = this;

        if(savedInstanceState == null){
            mTripsArrayList = new ArrayList<>();
        }else if(savedInstanceState.containsKey(MainActivity.FISHING_TRIPS_ARRAYLIST)){
            mTripsArrayList = savedInstanceState.getParcelableArrayList(MainActivity.FISHING_TRIPS_ARRAYLIST);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mTripsArrayList = intent.getParcelableArrayListExtra(MainActivity.FISHING_TRIPS_ARRAYLIST);
        }

        mAuthObj = FirebaseAuth.getInstance();
        FirebaseUser theUser = mAuthObj.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fishingtrips_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a Trip!", Snackbar.LENGTH_LONG)
                        .setAction("Add Trip", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
                                FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(mCurUser != null){
                                    String trip_name = ((EditText)findViewById(R.id.etName)).getText().toString();
                                    String trip_desc = ((EditText)findViewById(R.id.etDescription)).getText().toString();

                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
                                    String theDate = df.format(c);

                                    FirestoreAdds.addFS_trip(mContext, mFS_Store, new Fire_User(mCurUser),
                                            new Fire_Trip("",
                                                    new GeoPoint(0.0,0.0),
                                                    trip_name,
                                                    trip_desc,
                                                    Timestamp.now(),
                                                    Timestamp.now())
                                    );
                                    Toast.makeText(mContext, "Making a trip...", Toast.LENGTH_SHORT).show();
                                    ((EditText)findViewById(R.id.etName)).setText("");
                                    ((EditText)findViewById(R.id.etDescription)).setText("");
                                }
                                else
                                    Toast.makeText(mContext, "mCurUser is null", Toast.LENGTH_LONG).show();
                            }
                        }).show();
                Log.i("fart", "clicked FAB");
            }
        });

        //todo: Recycler View needs to be tied to a listener getting snapshots of changing documents
        //      - documents in Trips collection should be added to and removed from recycler view via a listener
        mTripsRV = findViewById(R.id.rvTrips);
        assert mTripsRV != null;
        setupRecyclerView(mTripsRV);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mTripsRVAdapter = new TripsRVAdapter(this, mTripsArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mTripsRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MainActivity.FISHING_TRIPS_ARRAYLIST, mTripsArrayList);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTripsArrayList = savedInstanceState.getParcelableArrayList(MainActivity.FISHING_TRIPS_ARRAYLIST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachFirestoreTripReadListener(){
        //construct a new listener if it doesn't exist
        if(mFsTripsEventListener == null) {
            mFsTripsEventListener = new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("fart", "listen:error", e);
                        return;
                    }

                    if(snapshots != null) {
                        for (DocumentChange trip : snapshots.getDocumentChanges()) {
                            switch (trip.getType()) {
                                case ADDED:
                                    Log.d("fart", "New trip: " + trip.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d("fart", "Modified trip: " + trip.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d("fart", "Removed trip: " + trip.getDocument().getData());
                                    break;
                            }
                        }
                    }
                }
            };
            theRegistration = mFsTripsDatabaseReference.addSnapshotListener(mFsTripsEventListener);    //add that listener
        }
    }
    private void detachFirestoreTripReadListener(){
        if(mFsTripsEventListener != null) {
            //mFsTripsDatabaseReference.removeEventListener(mFsTripsEventListener);
            theRegistration.remove();
            mFsTripsEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attach Listener
        attachFirestoreTripReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //detach Listener
        detachFirestoreTripReadListener();
//        mTackleBoxAdapter.clear();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find Trips", Toast.LENGTH_SHORT).show();
    }

}
