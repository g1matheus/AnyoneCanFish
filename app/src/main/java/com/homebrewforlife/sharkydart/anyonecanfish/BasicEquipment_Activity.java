package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.homebrewforlife.sharkydart.anyonecanfish.adapters.BasicEquipmentRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.adapters.GameFishRVAdapter;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_BasicInfo;

import java.util.ArrayList;

public class BasicEquipment_Activity extends AppCompatActivity {

    ArrayList<Fire_BasicInfo> mBasicInfoArrayList;
    BasicEquipmentRVAdapter mBasicInfoRVAdapter;
    RecyclerView mBasicInfoRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_equipment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            mBasicInfoArrayList = new ArrayList<>();
        }else if(savedInstanceState.containsKey(FishingBasicsActivity.BASIC_EQUIPMENT_ACTIVITY)){
            mBasicInfoArrayList = savedInstanceState.getParcelableArrayList(FishingBasicsActivity.BASIC_EQUIPMENT_ACTIVITY);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            mBasicInfoArrayList = intent.getParcelableArrayListExtra(FishingBasicsActivity.BASIC_EQUIPMENT_ACTIVITY);
        }

        mBasicInfoRV = findViewById(R.id.rvBasicEquipment);
        assert mBasicInfoRV != null;
        setupRecyclerView(mBasicInfoRV);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mBasicInfoRVAdapter = new BasicEquipmentRVAdapter(this, mBasicInfoArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mBasicInfoRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FishingBasicsActivity.BASIC_EQUIPMENT_ACTIVITY, mBasicInfoArrayList);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Can't find Game Fish", Toast.LENGTH_SHORT).show();
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
