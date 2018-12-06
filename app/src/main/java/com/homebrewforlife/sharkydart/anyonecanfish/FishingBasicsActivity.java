package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homebrewforlife.sharkydart.anyonecanfish.fireX.FirestoreStuff;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_BasicInfo;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_GameFish;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FishingBasicsActivity extends AppCompatActivity {

    public static final String BASIC_EQUIPMENT_ACTIVITY = "com.homebrewforlife.sharkydart.anyonecanfish.basicequipmentactivity";
    private Context mContext;
    private ArrayList<Fire_BasicInfo> mBasicInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_basics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            //firestore references
            FirebaseFirestore mFS_Store = FirebaseFirestore.getInstance();
            //get specifically user firestore db data
            FirebaseUser mCurUser = FirebaseAuth.getInstance().getCurrentUser();

            FirestoreStuff myFirestore = new FirestoreStuff(mContext, mCurUser, mFS_Store);
            //get specifically game_fish firestore db data
            mBasicInfoArrayList = new ArrayList<Fire_BasicInfo>();
            myFirestore.Firestore_Get_BasicInfo(mBasicInfoArrayList);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        findViewById(R.id.btnBasicEquipment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BasicEquipment_Activity.class);
                intent.putParcelableArrayListExtra(BASIC_EQUIPMENT_ACTIVITY, mBasicInfoArrayList);
                mContext.startActivity(intent);
            }
        });
        findViewById(R.id.btnHowToFish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(mContext, HowToFish_Activity.class);
                startActivity(theIntent);
            }
        });
        findViewById(R.id.btnRulesResources).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fwaUrl = getResources().getString(R.string.usa_fish_and_wildlife_url);
                openWebPage(fwaUrl);
            }
        });
        findViewById(R.id.btnMaintenanceTips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(mContext, Maintenance_Activity.class);
                startActivity(theIntent);
            }
        });
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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
