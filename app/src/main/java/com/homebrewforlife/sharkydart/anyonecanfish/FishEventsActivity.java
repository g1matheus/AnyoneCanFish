package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.FishEventsRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_FishEvent;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.util.ArrayList;

public class FishEventsActivity extends AppCompatActivity {

    ArrayList<Fire_FishEvent> mFishEventArrayList;
    FishEventsRVAdapter mFishEventRVAdapter;
    RecyclerView mFishEventRV;
    private Context mContext;
    Fire_Trip mInTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            mFishEventArrayList = new ArrayList<>();
        }else if(savedInstanceState.containsKey(FishingTripsActivity.FISHEVENT_ARRAYLIST)){
            mFishEventArrayList = savedInstanceState.getParcelableArrayList(FishingTripsActivity.FISHEVENT_ARRAYLIST);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mInTrip = intent.getParcelableExtra(FishingTripsActivity.THE_TRIP);
            mFishEventArrayList = intent.getParcelableArrayListExtra(FishingTripsActivity.FISHEVENT_ARRAYLIST);
        }

        mFishEventRV = findViewById(R.id.rvFishEvents);
        assert mFishEventRV != null;
        setupRecyclerView(mFishEventRV);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fishevents_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a fish event!", Snackbar.LENGTH_LONG)
                        .setAction("Add fish event", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
                                FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(mCurUser != null) {
                                    /*
                                    species
                                    released
                                    desc
                                     */
//                                    FirestoreAdds.addFS_fishEvent(mContext, mFS_Store, new Fire_User(mCurUser), mInTrip, );
                                    Toast.makeText(mContext, "Making a User...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(mContext, "mCurUser is null", Toast.LENGTH_LONG).show();
                            }
                        }).show();
                Log.i("fart", "clicked FAB");
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mFishEventRVAdapter = new FishEventsRVAdapter(this, mFishEventArrayList, mInTrip);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mFishEventRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FishingTripsActivity.FISHEVENT_ARRAYLIST, mFishEventArrayList);
        outState.putParcelable(FishingTripsActivity.THE_TRIP, mInTrip);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFishEventArrayList = savedInstanceState.getParcelableArrayList(FishingTripsActivity.FISHEVENT_ARRAYLIST);
        mInTrip = savedInstanceState.getParcelable(FishingTripsActivity.THE_TRIP);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find trip and events", Toast.LENGTH_SHORT).show();
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

}
