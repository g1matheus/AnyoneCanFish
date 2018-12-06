package com.homebrewforlife.sharkydart.anyonecanfish.fireX;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_FishEvent;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

public class FirestoreDeletes {
    //trip, lure, fishevent, public_fish_event

    public static void deleteFS_trip(final Context theContext, final Fire_User freshUser, final FirebaseFirestore mFS_Store, final Fire_Trip theTrip){
        try {
            Toast.makeText(theContext, freshUser.getName() + ", you must delete all the events from this trip first", Toast.LENGTH_LONG).show();

            mFS_Store.collection(theContext.getString(R.string.db_users))
                    .document(freshUser.getUid()).collection(theContext.getString(R.string.db_trips))
                    .document(theTrip.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "Trip successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error deleting the trip", e);
                        }
                    });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void deleteFS_lure(final Context theContext, final Fire_User freshUser, final Fire_TackleBox theTackleBox, final Fire_Lure theLure, final FirebaseFirestore mFS_Store){
        try {
            mFS_Store.collection(theContext.getString(R.string.db_users))
                    .document(freshUser.getUid()).collection(theContext.getString(R.string.db_tackle_boxes))
                    .document(theTackleBox.getUid()).collection(theContext.getString(R.string.db_lures))
                    .document(theLure.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "Lure successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error deleting the lure", e);
                        }
                    });
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void deleteFS_fishevent(final Context theContext, final Fire_User freshUser, final Fire_Trip theTrip, final Fire_FishEvent theFishEvent, final FirebaseFirestore mFS_Store){
        try{
            mFS_Store.collection(theContext.getString(R.string.db_users))
                    .document(freshUser.getUid()).collection(theContext.getString(R.string.db_trips))
                    .document(theTrip.getUid()).collection(theContext.getString(R.string.db_fish_events))
                    .document(theFishEvent.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "FishEvent successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error deleting the fishevent", e);
                        }
                    });
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void deleteFS_publicfishevent(final Context theContext, final Fire_User freshUser, final Fire_FishEvent theFishEvent, final FirebaseFirestore mFS_Store){
        try{
            Log.d("fart", "Haven't tested the security rules for this yet");
/*
            mFS_Store.collection(theContext.getString(R.string.db_public_fish_events))
                    .document(theFishEvent.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("fart", "FishEvent successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("fart", "Error deleting the fishevent", e);
                        }
                    });
*/
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
