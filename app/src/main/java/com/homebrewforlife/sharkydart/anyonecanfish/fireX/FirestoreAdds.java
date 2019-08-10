package com.homebrewforlife.sharkydart.anyonecanfish.fireX;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.*;

import java.util.ArrayList;

public class FirestoreAdds {
    public static void addFS_user(final Context theContext, final Fire_User freshUser, final FirebaseFirestore mFS_Store){
        try {
            DocumentReference newUser = mFS_Store.collection(theContext.getString(R.string.db_users)).document(freshUser.getUid());
            newUser.set(freshUser, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "User Account successfully created!");
                            //create user's first tacklebox - the uid will be the same as the user for this one
                            addFS_tacklebox(theContext,mFS_Store,freshUser,
                                    new Fire_TackleBox(
                                            null,
                                            "My Tacklebox!",
                                            "My Tackle",
                                            "",
                                            freshUser.getUid()  //so that it only makes one tacklebox
                                    ));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error adding the USER", e);
                        }
                    });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    private static void addFS_tacklebox(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_TackleBox fire_tackleBox) {
        try {
            //path: users/[user.uid]/TackleBoxes/[tacklebox.uid]
            DocumentReference theNewBox;
            if(fire_tackleBox.getUid() != null && !fire_tackleBox.getUid().isEmpty()) {
                //if the tacklebox has a uid already
                theNewBox = mFS_Store.collection(theContext.getString(R.string.db_users))
                        .document(freshUser.getUid())
                        .collection(theContext.getString(R.string.db_tackle_boxes))
                        .document(fire_tackleBox.getUid());
            }else{
                //if there is no uid, create a new tacklebox
                theNewBox = mFS_Store.collection(theContext.getString(R.string.db_users))
                        .document(freshUser.getUid())
                        .collection(theContext.getString(R.string.db_tackle_boxes))
                        .document();
                fire_tackleBox.setUid(theNewBox.getId());
            }

            theNewBox.set(fire_tackleBox, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "Tacklebox successfully created!" + fire_tackleBox.getUid());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error adding TackleBox", e);
                        }
                    });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void addFS_lure(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_TackleBox fire_tackleBox, final Fire_Lure fire_lure){
        addFS_lure(theContext, mFS_Store, freshUser, fire_tackleBox, fire_lure, null);
    }
    public static void addFS_lure(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_TackleBox fire_tackleBox, final Fire_Lure fire_lure,
                                  final ArrayList<Fire_Lure> addLure) {
        try {
            //path: users/[user.uid]/TackleBoxes/[tacklebox.uid]/Lures/{lure.uid}
            DocumentReference theLure = mFS_Store.collection(theContext.getString(R.string.db_users))
                    .document(freshUser.getUid())
                    .collection(theContext.getString(R.string.db_tackle_boxes))
                    .document(fire_tackleBox.getUid())
                    .collection(theContext.getString(R.string.db_lures))
                    .document();
            fire_lure.setUid(theLure.getId());
            theLure.set(fire_lure, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "Lure successfully created!" + fire_lure.getUid());
                            if(addLure != null)
                                addLure.add(fire_lure);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error adding Lure", e);
                        }
                    });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void addFS_trip(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_Trip fire_trip){
        addFS_trip(theContext,mFS_Store,freshUser,fire_trip, null);
    }
    public static void addFS_trip(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_Trip fire_trip, final ArrayList<Fire_Trip> addNewTrip) {
        try {
            //path: users/[user.uid]/Trips/[trip.uid]
            mFS_Store.collection(theContext.getString(R.string.db_users))
                    .document(freshUser.getUid())
                    .collection(theContext.getString(R.string.db_trips))
                    .add(fire_trip)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("fart", "DocumentSnapshot of trip written with ID: " + documentReference.getId());
                            fire_trip.setUid(documentReference.getId());
                            if(addNewTrip != null)
                                addNewTrip.add(fire_trip);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error adding trip", e);
                        }
                    });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void addFS_fishEvent(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, Fire_Trip theTrip, final Fire_FishEvent fire_fishEvent){
        addFS_fishEvent(theContext, mFS_Store, freshUser, theTrip, fire_fishEvent, null);
    }
    public static void addFS_fishEvent(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, Fire_Trip theTrip, final Fire_FishEvent fire_fishEvent,
                                       final ArrayList<Fire_FishEvent> addFishEvent
    ) {
        try {
            CollectionReference whereToSaveFishEvent;

            if(theTrip == null || theTrip.getUid() == null || theTrip.getUid().isEmpty()) {
                //if there is no trip passed in, it will be saved to the public fish_events:
                // path:fishes_caught/[uid]
                whereToSaveFishEvent = mFS_Store.collection(theContext.getString(R.string.db_public_fish_events));
            }
            else {
                //if there is a trip passed in:
                //path: users/[user.uid]/Trips/[trip.uid]/Fish Events/[fish.uid]
                whereToSaveFishEvent = mFS_Store.collection(theContext.getString(R.string.db_users))
                        .document(freshUser.getUid())
                        .collection(theContext.getString(R.string.db_trips))
                        .document(theTrip.getUid())
                        .collection(theContext.getString(R.string.db_fish_events));
            }

            whereToSaveFishEvent
                    .add(fire_fishEvent)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("fart", "DocumentSnapshot of fish event written with ID: " + documentReference.getId());
                            fire_fishEvent.setUid(documentReference.getId());
                            if(addFishEvent != null)
                                addFishEvent.add(fire_fishEvent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error adding fish event", e);
                        }
                    });
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

}
