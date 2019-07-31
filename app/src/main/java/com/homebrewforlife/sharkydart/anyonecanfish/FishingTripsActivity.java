package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.homebrewforlife.sharkydart.anyonecanfish.MainActivity.SHAREDPREFS_LAT;
import static com.homebrewforlife.sharkydart.anyonecanfish.MainActivity.SHAREDPREFS_LON;

public class FishingTripsActivity extends AppCompatActivity {

    public static final String FISHEVENT_ARRAYLIST = "homebrew-sharkydart-fishing-event";
    public static final String THE_TRIP = "homebrew-sharkydart-fishing-trip";

    private Context mContext;

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

    private void attachDatabaseReadListener(){
//        if(mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    //gets called initially, for each, and also when one is added
//                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                    mMessageAdapter.add(friendlyMessage);
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            };
//            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
//        }
    }
    private void detachDatabaseReadListener(){
//        if(mChildEventListener != null) {
//            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
//            mChildEventListener = null;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attach Listener
//        attachDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //detach Listener
//        detachDatabaseReadListener();
//        mTackleBoxAdapter.clear();
    }

}
