package com.homebrewforlife.sharkydart.anyonecanfish.fireX;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.R;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_User;

import java.util.ArrayList;

public class FirestoreUpdates {
/*
    public static void updateFS_tripPhoto(Context theContext, FirebaseFirestore mFS_Store, Fire_User freshUser, final Fire_Trip fire_trip, final ArrayList<Fire_Trip> addNewTrip) {
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
*/

    /*DocumentReference washingtonRef = db.collection("cities").document("DC");

// Set the "isCapital" field of the city 'DC'
washingtonRef
        .update("capital", true)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });
        */

    /*
    Update fields in nested objects
If your document contains nested objects, you can use "dot notation" to reference nested fields within the document when you call update():

    * // Assume the document contains:
// {
//   name: "Frank",
//   favorites: { food: "Pizza", color: "Blue", subject: "recess" }
//   age: 12
// }
//
// To update age and favorite color:
db.collection("users").document("frank")
        .update(
                "age", 13,
                "favorites.color", "Red"
        );
*/

    /*
    can also add timestamps

    * // If you're using custom Java objects in Android, add an @ServerTimestamp
// annotation to a Date field for your custom object classes. This indicates
// that the Date field should be treated as a server timestamp by the object mapper.
DocumentReference docRef = db.collection("objects").document("some-id");

// Update the timestamp field with the value from the server
Map<String,Object> updates = new HashMap<>();
updates.put("timestamp", FieldValue.serverTimestamp());

docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
 */
}
