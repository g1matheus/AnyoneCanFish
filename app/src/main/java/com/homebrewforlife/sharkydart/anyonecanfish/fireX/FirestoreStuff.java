package com.homebrewforlife.sharkydart.anyonecanfish.fireX;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_BasicInfo;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_FishEvent;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_GameFish;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FirestoreStuff {
    @NonNull
    private Context mContext;
    public FirebaseUser mCurUser;

    public FirebaseUser getmCurUser() {
        return this.mCurUser;
    }
    public void setmCurUser(FirebaseUser theUser){
        this.mCurUser = theUser;
    }

    public FirebaseFirestore mFS_Store;
    static DocumentReference mFS_User_document_ref;
    static CollectionReference mFS_GameFish_collection_ref;
    ArrayList<Fire_GameFish> mFire_GameFish_arraylist;
    static CollectionReference mFS_TackleBox_collection_ref;
    ArrayList<Fire_TackleBox> mFire_TackleBox_arraylist;
    static CollectionReference mFS_Trips_collection_ref;
    ArrayList<Fire_Trip> mFire_Trips_arraylist;

    public FirestoreStuff(){}
    public FirestoreStuff(@NonNull Context theContext, FirebaseUser theUser, FirebaseFirestore theFS,
                          ArrayList<Fire_GameFish> gfArray, ArrayList<Fire_TackleBox> tbArray, ArrayList<Fire_Trip> tpArray){
        this.mContext = theContext;
        this.mCurUser = theUser;
        this.mFS_Store = theFS;
        this.mFire_GameFish_arraylist = gfArray;
        this.mFire_TackleBox_arraylist = tbArray;
        this.mFire_Trips_arraylist = tpArray;
    }
    public FirestoreStuff(@NonNull Context theContext, FirebaseUser theUser, FirebaseFirestore theFS){
        this.mContext = theContext;
        this.mCurUser = theUser;
        this.mFS_Store = theFS;
        this.mFire_GameFish_arraylist = new ArrayList<>();
        this.mFire_TackleBox_arraylist = new ArrayList<>();
        this.mFire_Trips_arraylist = new ArrayList<>();
    }

    //Firebase Functions
    public void FirebaseSignOut(){
        AuthUI.getInstance()
                .signOut(mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext,"You have been signed out.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void FirebaseSignIn(){
        int RC_SIGN_IN = 0;   //request code
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        ((AppCompatActivity)mContext).startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void FirebaseUpdateUserInfo(final FirebaseUser theUser){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Curious Angler")
//                .setPhotoUri(Uri.parse(""))
                .build();

        theUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("fart", "User profile updated.");
                            Log.d("fart", "Name: " + theUser.getDisplayName()
                                    + " Email: " + theUser.getEmail()
                                    + " UID: " + theUser.getUid());
                        }
                    }
                });
    }

    public CollectionReference getFsGamefishRef(){
        return mFS_Store.collection(mContext.getString(R.string.db_game_fish));
    }
    public void Firestore_Get_GameFish(final ArrayList<Fire_GameFish> theGameFish){
        try {
            mFS_GameFish_collection_ref = getFsGamefishRef();
//                    mFS_Store.collection(mContext.getString(R.string.db_game_fish));
            mFS_GameFish_collection_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (theGameFish != null)
                                theGameFish.clear();
                            for (QueryDocumentSnapshot gamefish : queryDocumentSnapshots) {
                                Fire_GameFish bork = gamefish.toObject(Fire_GameFish.class);
                                if (theGameFish != null) {
                                    theGameFish.add(bork);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "collection grabbing error");
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public CollectionReference getFsTripsRef(){
        return mFS_Store
                .collection(mContext.getString(R.string.db_users))
                .document(mCurUser.getUid())
                .collection(mContext.getString(R.string.db_trips));

    }
    public void Firestore_Get_FishingTrips(final ArrayList<Fire_Trip> theFishingTrips){
        try{
            mFS_Trips_collection_ref = getFsTripsRef();
//                    mFS_Store.collection(mContext.getString(R.string.db_users))
//                    .document(mCurUser.getUid()).collection(mContext.getString(R.string.db_trips));
            mFS_Trips_collection_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (theFishingTrips != null)
                                theFishingTrips.clear();
                            for (QueryDocumentSnapshot trip : queryDocumentSnapshots) {
                                Fire_Trip bork = trip.toObject(Fire_Trip.class);
                                bork.setUid(trip.getId());
                                //Log.i("fart", bork.getQuickDescription());
                                if (theFishingTrips != null) {
                                    theFishingTrips.add(bork);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "trip collection grabbing error: " + e.toString());
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public CollectionReference getFsFisheventsRef(String the_trip_uid){
        return mFS_Store
                .collection(mContext.getString(R.string.db_users))
                .document(mCurUser.getUid())
                .collection(mContext.getString(R.string.db_trips))
                .document(the_trip_uid)
                .collection(mContext.getString(R.string.db_fish_events));
    }
    public void Firestore_Get_Trip_FishEvents(String uid_trip, final ArrayList<Fire_FishEvent> theFishEvents){
        try
        {
            CollectionReference FS_Trip_FishEvents = getFsFisheventsRef(uid_trip);
//                    mFS_Store
//                    .collection(mContext.getString(R.string.db_users))
//                    .document(mCurUser.getUid())
//                    .collection(mContext.getString(R.string.db_trips))
//                    .document(uid_trip)
//                    .collection(mContext.getString(R.string.db_fish_events));
            FS_Trip_FishEvents.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(theFishEvents != null)
                                theFishEvents.clear();
                            for(QueryDocumentSnapshot fishEvent : queryDocumentSnapshots){
                                Log.d("fart", fishEvent.getId()
                                        + " => " + fishEvent.getTimestamp("date")
                                        + " => " + fishEvent.getString("species")
                                        + " => " + fishEvent.getString("desc")
                                        + " => " + fishEvent.getBoolean("released")
                                        + " => " + fishEvent.getGeoPoint("geo_loc")
                                        + " => " + fishEvent.getString("image_url")
                                );
                                Fire_FishEvent bork = fishEvent.toObject(Fire_FishEvent.class);
                                Log.i("fart", bork.getQuickDescription());
                                if(theFishEvents != null) {
                                    theFishEvents.add(bork);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "fishevents grabbing error");
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public CollectionReference getFsTackleboxRef(){
        return mFS_Store
                .collection(mContext.getString(R.string.db_users))
                .document(mCurUser.getUid())
                .collection(mContext.getString(R.string.db_tackle_boxes));
    }
    public void Firestore_Get_TackleBoxes(final ArrayList<Fire_TackleBox> theTackleBoxes){
        try{
            mFS_TackleBox_collection_ref = getFsTackleboxRef();
//                    mFS_Store
//                    .collection(mContext.getString(R.string.db_users))
//                    .document(mCurUser.getUid())
//                    .collection(mContext.getString(R.string.db_tackle_boxes));
            mFS_TackleBox_collection_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(theTackleBoxes != null)
                                theTackleBoxes.clear();
                            for (QueryDocumentSnapshot tacklebox : queryDocumentSnapshots){
                                Fire_TackleBox bork = tacklebox.toObject(Fire_TackleBox.class);
                                bork.setUid(tacklebox.getId());
                                Log.d("fart", bork.getUid() + " " + bork.getDesc() + " " + bork.getName() + " " + bork.getImage_url());
                                if(theTackleBoxes != null) {
                                    theTackleBoxes.add(bork);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "tacklebox collection grabbing error: " + e.toString());
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public CollectionReference getFsLuresRef(String the_tacklebox_uid){
        return mFS_Store
                .collection(mContext.getString(R.string.db_users))
                .document(mCurUser.getUid())
                .collection(mContext.getString(R.string.db_tackle_boxes))
                .document(the_tacklebox_uid)
                .collection(mContext.getString(R.string.db_lures));
    }
    public void Firestore_Get_TackleBox_Lures(String uid_tacklebox, final ArrayList<Fire_Lure> theLures) {
        Firestore_Get_TackleBox_Lures(uid_tacklebox, theLures, null);
    }
    public void Firestore_Get_TackleBox_Lures(String uid_tacklebox, final ArrayList<Fire_Lure> theLures, final CountDownLatch signal){
        try {
            CollectionReference FS_tacklebox_lures = getFsLuresRef(uid_tacklebox);
//                    mFS_Store
//                    .collection(mContext.getString(R.string.db_users))
//                    .document(mCurUser.getUid())
//                    .collection(mContext.getString(R.string.db_tackle_boxes))
//                    .document(uid_tacklebox)
//                    .collection(mContext.getString(R.string.db_lures));
            FS_tacklebox_lures.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (theLures != null)
                                theLures.clear();
                            for (QueryDocumentSnapshot lure : queryDocumentSnapshots) {
                                Fire_Lure bork = lure.toObject(Fire_Lure.class);
                                Log.i("fart", bork.getQuickDescription());
                                if (theLures != null) {
                                    theLures.add(bork);
                                }
                            }
                            if(theLures != null)
                                Log.d("fart", "the lures count = " + theLures.size());
                            if(signal != null)
                                signal.countDown();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "lures collection grabbing error: " + e.getMessage());
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    public void Firestore_Get_BasicInfo(final ArrayList<Fire_BasicInfo> theBasicInfo){
        try {
            CollectionReference basicInfo_collection_ref = mFS_Store.collection(mContext.getString(R.string.db_basic_info));
            basicInfo_collection_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (theBasicInfo != null)
                                theBasicInfo.clear();
                            for (QueryDocumentSnapshot someInfo : queryDocumentSnapshots) {
                                Fire_BasicInfo bork = someInfo.toObject(Fire_BasicInfo.class);
                                if (theBasicInfo != null) {
                                    theBasicInfo.add(bork);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fart", "basic info grabbing error");
                }
            });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

}
