package com.homebrewforlife.sharkydart.anyonecanfish.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Lure;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_TackleBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class FirestoreGrabber extends AsyncTask<DataGrabListener, Void, ArrayList<Fire_Lure>> {
    private DataGrabListener myListener = null;

    private FirestoreStuff myFirestoreStuff;
    private ArrayList<Fire_Lure> myArrayList;
    private Fire_TackleBox myTackleBox;

    public void setMyFirestoreStuff(FirestoreStuff myFirestoreStuff) {
        this.myFirestoreStuff = myFirestoreStuff;
    }
    public void setMyArrayList(ArrayList<Fire_Lure> myArrayList) {
        this.myArrayList = myArrayList;
    }
    public void setMyTackleBox(Fire_TackleBox myTackleBox) {
        this.myTackleBox = myTackleBox;
    }
    CountDownLatch signal;
    @Override
    protected ArrayList<Fire_Lure> doInBackground(DataGrabListener... params) {
        myListener = params[0];
        try {
            signal=new CountDownLatch(1);
            myFirestoreStuff.Firestore_Get_TackleBox_Lures(myTackleBox.getUid(), myArrayList, signal);
            signal.await();
            Log.d("fart", "size:"+myArrayList.size());
            return myArrayList;
        } catch (Exception e) {
            Log.e("error", e.getMessage());
            return null;  //return empty string for purposes of return type consistency and ease of testing
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Fire_Lure> theResult) {
        if (this.myListener != null) {
            Log.d("fart", "result: " + theResult.size());
            this.myListener.onComplete(theResult);
        }
//        Intent intent = new Intent(mContext, ActivityJokeFromIntent.class);
//        intent.putExtra(ActivityJokeFromIntent.THE_JOKE, theResult);
//        mContext.startActivity(intent);
    }

    @Override
    protected void onCancelled() {
        if (this.myListener != null) {
            this.myListener.onComplete(null);
        }
        super.onCancelled();
    }
}
