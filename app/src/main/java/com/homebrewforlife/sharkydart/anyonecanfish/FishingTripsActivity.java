package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.FishingTripsRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreAdds;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.EXTRA_CHOSEN_COMPONENT;

public class FishingTripsActivity extends AppCompatActivity implements TripDetailsSheet.TripSheetListener {
    private static final String LOG_TAG = "fart.FishingTrips";
    public static final String FISHEVENT_ARRAYLIST = "homebrew-sharkydart-fishing-event";
    public static final String THE_TRIP = "homebrew-sharkydart-fishing-trip";
    public static final int TRIP_PHOTO_PICKER =  2;
    public static final int REQUEST_FISH_THUMBNAIL_CAPTURE = -3;
    public static final int REQUEST_FISH_BIG_CAPTURE = -4;
    public static String mCurrentPhotoPath;
    private static final String mSheetTag = "AddTripBottomModalSheet";

    private Context mContext;
    ArrayList<Fire_Trip> mTripsArrayList;
    FishingTripsRVAdapter mTripsRVAdapter;
    RecyclerView mTripsRV;

    private ImageButton mPhotoPickerButton;
    private FirestoreStuff firestoreStuff;
    private FirebaseAuth mAuthObj;
    private StorageReference mTripPhotosStorageReference;   //FirebaseStorage storage reference

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
            Log.d(LOG_TAG, "Loading fishing trips from savedinstancestate");
            mTripsArrayList = savedInstanceState.getParcelableArrayList(MainActivity.FISHING_TRIPS_ARRAYLIST);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
/*
        else {
            mTripsArrayList = intent.getParcelableArrayListExtra(MainActivity.FISHING_TRIPS_ARRAYLIST);
        }
*/

        mAuthObj = FirebaseAuth.getInstance();
        FirebaseUser theUser = mAuthObj.getCurrentUser();
        if(theUser != null) {
            firestoreStuff = new FirestoreStuff(mContext, theUser, FirebaseFirestore.getInstance());
            mFsTripsDatabaseReference = firestoreStuff.getFsTripsRef();
            mTripPhotosStorageReference = FirebaseStorage.getInstance().getReference().child("user").child(theUser.getUid());
        }else
            closeOnError();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fishingtrips_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                todo: instead of snackbar, have it open a model bottom sheet - trips_sheet.xml
                TripDetailsSheet tripSheet = new TripDetailsSheet();
                tripSheet.show(getSupportFragmentManager(), mSheetTag);
/*
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
*/
                Log.i(LOG_TAG, "clicked FAB");
            }
        });

        mTripsRV = findViewById(R.id.rvTrips);
        assert mTripsRV != null;
        setupRecyclerView(mTripsRV);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mTripsRVAdapter = new FishingTripsRVAdapter(this, mTripsArrayList);
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
                        Log.w(LOG_TAG, "listen:error", e);
                        return;
                    }

                    if(snapshots != null) {
                        for (DocumentChange trip : snapshots.getDocumentChanges()) {
                            switch (trip.getType()) {
                                case ADDED:
                                    Fire_Trip addedTrip = trip.getDocument().toObject(Fire_Trip.class);
                                    addedTrip.setUid(trip.getDocument().getId());
                                    Log.d(LOG_TAG, "the FIRE_TRIP: " + addedTrip.getQuickDescription());
                                    mTripsArrayList.add(addedTrip);
                                    break;
                                case MODIFIED:
                                    Fire_Trip changedTrip = trip.getDocument().toObject(Fire_Trip.class);
                                    Log.d(LOG_TAG, "Modified trip: " + changedTrip.getQuickDescription());
                                    mTripsArrayList.set(trip.getNewIndex(),trip.getDocument().toObject(Fire_Trip.class));
                                    break;
                                case REMOVED:
                                    Fire_Trip removedTrip = trip.getDocument().toObject(Fire_Trip.class);
                                    Log.d(LOG_TAG, "Removed trip: " + removedTrip.getQuickDescription());
                                    mTripsArrayList.remove(trip.getOldIndex());
                                    break;
                            }
                        }
                        mTripsRVAdapter.notifyDataSetChanged();
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
        mTripsArrayList.clear();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find Trips", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FISH_THUMBNAIL_CAPTURE && resultCode == RESULT_OK){
            if(data != null) {
                Bundle extras = data.getExtras();
                if(extras != null) {
                    Bitmap imageBitmap = (Bitmap)extras.get("data");
                    //ImageView to display in:
                    //imageView.setImageBitmap(imageBitmap);
                }
            }
        }else if(requestCode >= 0 && resultCode == RESULT_OK){
            if(data != null) {
                Log.d(LOG_TAG, "index in array clicked = " + requestCode);
                Uri selectedImageUri = data.getData();
                final StorageReference photoRef;

                if (selectedImageUri != null) {
                    String imagepathend = selectedImageUri.getLastPathSegment();
                    if (imagepathend != null) {
                        photoRef = mTripPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
                        photoRef.putFile(selectedImageUri).addOnSuccessListener(this,
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(
                                                new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Log.d(LOG_TAG, "URL of photo in storage: " + uri.toString());
                                                        //need to save this URL to the trip that was clicked
                                                        mTripsArrayList.get(requestCode).setImage_url(uri.toString());
                                                        mFsTripsDatabaseReference.document(mTripsArrayList.get(requestCode).getUid())
                                                                .update("image_url", uri.toString());
                                                    }
                                                });
                                    }
                                });
                    }
                }
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onAddTripBtnClicked(Fire_Trip theTrip) {
        FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
        FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mCurUser != null){
            Log.d(LOG_TAG,"user:good, add the trip...");
            FirestoreAdds.addFS_trip(mContext, mFS_Store, new Fire_User(mCurUser), theTrip);
            Toast.makeText(mContext, "Making a trip...", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(mContext, "mCurUser is null", Toast.LENGTH_LONG).show();
    }
}
