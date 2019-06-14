package com.homebrewforlife.sharkydart.anyonecanfish.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.Task;

public class TestAsyncReceiver extends BroadcastReceiver {
    private static final String LOGTAG = "fart_broadcast";

    public static final String ACTION_LOCATION_UPDATE = "action-location-update";

    @Override
    public void onReceive(Context context, Intent intent) {
        String theAction = intent.getAction();
        if(ACTION_LOCATION_UPDATE.equals(theAction)) {
            final PendingResult pendingResult = goAsync();
            MyBRTask asyncTask = new MyBRTask(pendingResult, intent);
            asyncTask.execute();
        }
    }

    private static class MyBRTask extends AsyncTask<String, Integer, String> {
        private final PendingResult pendingResult;
        private final Intent intent;

        private MyBRTask(PendingResult pendingResult, Intent intent){
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(String... strings){
            String sb =
                    "Action: " + intent.getAction() + "\n"
                    + "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n";
            Log.d(LOGTAG, sb);
            return sb;
        }

        @Override
        protected void onPostExecute(String theString){
            super.onPostExecute(theString);
            //Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}
