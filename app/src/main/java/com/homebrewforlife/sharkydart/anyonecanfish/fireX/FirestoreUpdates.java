package com.homebrewforlife.sharkydart.anyonecanfish.fireX;

public class FirestoreUpdates {
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
